package com.mowmaster.dust.Features.Crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustRegistries.DustRecipeRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.DustComposition.DustRecipe;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// An Element Crafter recipe (type dust:element_crafting): its own recipe type, so the vanilla table never sees it.
// Like a dust recipe (DustRecipe) it's shapeless and can ask for grid items by the dust in them; and it can also use up
// dust of up to MAX_ELEMENTS elements from the crafter's jars. A research entry, if given, must be unlocked by whoever
// crafts it (the crafter knows who's clicking).
//
// JSON (data/<namespace>/recipe/<name>.json):
//   {
//     "type": "dust:element_crafting",
//     "result": { "id": "minecraft:blaze_powder" },
//     "ingredients": [ "minecraft:gunpowder" ],
//     "dusts": [ { "element": "fire" } ],                       (optional: grid items by their dust, as DustRecipe)
//     "elements": [ { "element": "dust:fire", "amount": 20 } ], (optional: dust from the jars, up to 4 elements)
//     "research": { "tab": "dust:research", "entry": "dust:element_tester" }   (optional)
//   }
public class ElementCraftingRecipe implements Recipe<CraftingInput>
{
    public static final int MAX_ELEMENTS = 4;

    // Dust from the jars: this much of this element
    public record ElementCost(DustElement element, int amount)
    {
        public static final Codec<ElementCost> CODEC = RecordCodecBuilder.create(i -> i.group(
                DustElements.CODEC.fieldOf("element").forGetter(ElementCost::element),
                Codec.INT.fieldOf("amount").forGetter(ElementCost::amount)
        ).apply(i, ElementCost::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ElementCost> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
    }

    // At most MAX_ELEMENTS, each element once: a recipe asking for more is refused when it loads (with this message)
    private static final Codec<List<ElementCost>> ELEMENTS_CODEC = ElementCost.CODEC.listOf().validate(list -> {
        if (list.size() > MAX_ELEMENTS) return DataResult.error(() -> "An element crafting recipe uses at most " + MAX_ELEMENTS + " elements");
        if (list.stream().map(ElementCost::element).distinct().count() != list.size()) return DataResult.error(() -> "Each element only once");
        return DataResult.success(list);
    });

    private final ItemStackTemplate result;
    private final List<DustRecipe.DustNeed> dusts;
    private final List<Ingredient> ingredients;
    private final List<ElementCost> elements;
    private final Optional<DustRecipe.ResearchKey> research;

    public ElementCraftingRecipe(ItemStackTemplate result, List<DustRecipe.DustNeed> dusts, List<Ingredient> ingredients,
                                 List<ElementCost> elements, Optional<DustRecipe.ResearchKey> research)
    {
        this.result = result;
        this.dusts = List.copyOf(dusts);
        this.ingredients = List.copyOf(ingredients);
        this.elements = List.copyOf(elements);
        this.research = research;
    }

    public static final MapCodec<ElementCraftingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.result),
            DustRecipe.DustNeed.CODEC.listOf().optionalFieldOf("dusts", List.of()).forGetter(r -> r.dusts),
            Ingredient.CODEC.listOf().optionalFieldOf("ingredients", List.of()).forGetter(r -> r.ingredients),
            ELEMENTS_CODEC.optionalFieldOf("elements", List.of()).forGetter(r -> r.elements),
            DustRecipe.ResearchKey.CODEC.optionalFieldOf("research").forGetter(r -> r.research)
    ).apply(i, ElementCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ElementCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC, r -> r.result,
            DustRecipe.DustNeed.STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.dusts,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.ingredients,
            ElementCost.STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.elements,
            DustRecipe.ResearchKey.STREAM_CODEC.apply(ByteBufCodecs::optional), r -> r.research,
            ElementCraftingRecipe::new);

    public List<ElementCost> elements()
    {
        return elements;
    }

    // ---- matching: the grid items, as a dust recipe matches them ----

    @Override
    public boolean matches(CraftingInput input, Level level)
    {
        List<ItemStack> items = new ArrayList<>();
        for (int slot = 0; slot < input.size(); slot++) {
            if (!input.getItem(slot).isEmpty()) items.add(input.getItem(slot));
        }
        return DustRecipe.matchesItems(items, dusts, ingredients);
    }

    @Override
    public ItemStack assemble(CraftingInput input)
    {
        return result.create();
    }

    // Whether this player may craft it: they've unlocked its research entry (or it needs none)
    public boolean allows(Player player)
    {
        if (research.isEmpty()) return true;
        ResearchEntry entry = ResearchTabs.entry(research.get().tab(), research.get().entry());
        return entry != null && Research.get(player).has(entry);
    }

    // ---- the rest of what a recipe has to say (it isn't in the recipe book) ----

    @Override
    public boolean showNotification() { return false; }

    @Override
    public String group() { return ""; }

    @Override
    public boolean isSpecial() { return true; }

    @Override
    public PlacementInfo placementInfo() { return PlacementInfo.NOT_PLACEABLE; }

    @Override
    public RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.CRAFTING_MISC; }

    @Override
    public RecipeSerializer<ElementCraftingRecipe> getSerializer() { return DustRecipeRegistry.ELEMENT_CRAFTING.get(); }

    @Override
    public RecipeType<ElementCraftingRecipe> getType() { return DustRecipeRegistry.ELEMENT_CRAFTING_TYPE.get(); }
}
