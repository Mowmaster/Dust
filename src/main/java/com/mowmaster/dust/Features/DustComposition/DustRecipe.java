package com.mowmaster.dust.Features.DustComposition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustRegistries.DustRecipeRegistry;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

// A dust recipe (shapeless, in a crafting grid). Besides ordinary ingredients it asks for items by the dust in them:
// "any item of pure fire dust" takes a fire charge, or anything else that holds only fire dust. And only a
// player who has unlocked its research entry can craft it (checked in mixin/CraftingMenuMixin: the recipe itself
// is never told who's crafting).
//
// JSON (data/<namespace>/recipe/<name>.json):
//   {
//     "type": "dust:dust_crafting",
//     "result": { "id": "dust:element_tester" },
//     "dusts": [ { "element": "fire" }, { "element": "water", "min": 2, "pure": false } ],
//     "ingredients": [ "minecraft:crafting_table" ],
//     "research": { "tab": "dust:research", "entry": "dust:element_tester" }
//   }
// A dust need is one item with at least min of that dust (default 1), and nothing else if pure (default true).
// Leaving out "research" makes the recipe open to everyone.
public class DustRecipe extends CustomRecipe
{
    // One grid item whose dust has at least min of element (and, if pure, no other kind of dust)
    public record DustNeed(EnumElement element, int min, boolean pure) implements Predicate<ItemStack>
    {
        public static final Codec<DustNeed> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EnumElement.CODEC.fieldOf("element").forGetter(DustNeed::element),
                Codec.INT.optionalFieldOf("min", 1).forGetter(DustNeed::min),
                Codec.BOOL.optionalFieldOf("pure", true).forGetter(DustNeed::pure)
        ).apply(instance, DustNeed::new));
        // idMapper sends an enum as its index number
        public static final StreamCodec<RegistryFriendlyByteBuf, DustNeed> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.idMapper(i -> EnumElement.values()[i], EnumElement::ordinal), DustNeed::element,
                ByteBufCodecs.VAR_INT, DustNeed::min,
                ByteBufCodecs.BOOL, DustNeed::pure,
                DustNeed::new);

        public static DustNeed pure(EnumElement element)
        {
            return new DustNeed(element, 1, true);
        }

        @Override
        public boolean test(ItemStack stack)
        {
            DustComposition composition = DustCompositions.of(stack);
            return composition != null && composition.get(element) >= min && (!pure || composition.isPure());
        }
    }

    // The research entry a player must have to craft it
    public record ResearchKey(Identifier tab, Identifier entry)
    {
        public static final Codec<ResearchKey> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("tab").forGetter(ResearchKey::tab),
                Identifier.CODEC.fieldOf("entry").forGetter(ResearchKey::entry)
        ).apply(instance, ResearchKey::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, ResearchKey> STREAM_CODEC = StreamCodec.composite(
                Identifier.STREAM_CODEC, ResearchKey::tab,
                Identifier.STREAM_CODEC, ResearchKey::entry,
                ResearchKey::new);
    }

    // ItemStackTemplate: 26.3's "an item, count and components" for recipe results (create() makes the ItemStack)
    private final ItemStackTemplate result;
    private final List<DustNeed> dusts;
    private final List<Ingredient> ingredients;
    private final Optional<ResearchKey> research;

    public DustRecipe(ItemStackTemplate result, List<DustNeed> dusts, List<Ingredient> ingredients, Optional<ResearchKey> research)
    {
        this.result = result;
        this.dusts = List.copyOf(dusts);
        this.ingredients = List.copyOf(ingredients);
        this.research = research;
    }

    public static final MapCodec<DustRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
            DustNeed.CODEC.listOf().optionalFieldOf("dusts", List.of()).forGetter(recipe -> recipe.dusts),
            Ingredient.CODEC.listOf().optionalFieldOf("ingredients", List.of()).forGetter(recipe -> recipe.ingredients),
            ResearchKey.CODEC.optionalFieldOf("research").forGetter(recipe -> recipe.research)
    ).apply(instance, DustRecipe::new));

    // .apply(ByteBufCodecs.list()) turns a codec for one thing into a codec for a list of them
    public static final StreamCodec<RegistryFriendlyByteBuf, DustRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC, recipe -> recipe.result,
            DustNeed.STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.dusts,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.ingredients,
            ResearchKey.STREAM_CODEC.apply(ByteBufCodecs::optional), recipe -> recipe.research,
            DustRecipe::new);

    // ---- matching ----

    // Every grid item fills exactly one need (a dust need or an ingredient), and every need is filled
    @Override
    public boolean matches(CraftingInput input, Level level)
    {
        List<ItemStack> items = new ArrayList<>();
        for (int slot = 0; slot < input.size(); slot++) {
            if (!input.getItem(slot).isEmpty()) items.add(input.getItem(slot));
        }
        List<Predicate<ItemStack>> needs = new ArrayList<>(dusts);
        // Ingredient::test as a Predicate: a method reference standing in for "stack -> ingredient.test(stack)"
        for (Ingredient ingredient : ingredients) needs.add(ingredient::test);
        return items.size() == needs.size() && assign(items, needs, 0, new boolean[needs.size()]);
    }

    // Tries giving item i (and each item after it) a need not yet taken. Backs up and tries another need when one
    // choice leads nowhere, so an item that fits two needs doesn't block a third. Grids are at most 9 items.
    private static boolean assign(List<ItemStack> items, List<Predicate<ItemStack>> needs, int i, boolean[] taken)
    {
        if (i == items.size()) return true;
        for (int n = 0; n < needs.size(); n++) {
            if (taken[n] || !needs.get(n).test(items.get(i))) continue;
            taken[n] = true;
            if (assign(items, needs, i + 1, taken)) return true;
            taken[n] = false;
        }
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput input)
    {
        return result.create();
    }

    // ---- research ----

    // Whether this player may craft it: they've unlocked its research entry (or it needs none)
    public boolean allows(Player player)
    {
        if (research.isEmpty()) return true;
        ResearchEntry entry = ResearchTabs.entry(research.get().tab(), research.get().entry());
        return entry != null && Research.get(player).has(entry);
    }

    // For the crafting check: false only for a dust recipe this player hasn't unlocked
    public static boolean allows(Player player, RecipeHolder<?> holder)
    {
        Recipe<?> recipe = holder.value();
        return !(recipe instanceof DustRecipe dustRecipe) || dustRecipe.allows(player);
    }

    @Override
    public RecipeSerializer<DustRecipe> getSerializer()
    {
        return DustRecipeRegistry.DUST_CRAFTING.get();
    }
}
