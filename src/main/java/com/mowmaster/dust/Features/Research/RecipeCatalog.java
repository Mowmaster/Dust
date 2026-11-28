package com.mowmaster.dust.Features.Research;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustRecipeRegistry;
import com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe;
import com.mowmaster.dust.Features.DustComposition.CraftedCompositions;
import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.DustComposition.DustRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

// Every way to make each item at a crafting table or an Element Crafter, for the research book: an item's "Crafted
// from" pages list these once its entry is unlocked (alongside the recipes the player has seen made). Clients don't
// have the recipes, so the server writes them down as RecipeRecords whenever datapacks load and sends them to each
// player, like CraftedCompositions.
//   - Crafting table: plain shaped and shapeless recipes, and Dust's dust recipes (their dust needs as dust items)
//   - Element Crafter: its recipes, with the dust they use from the jars (RecipeRecord.elements)
//   - An ingredient that takes several items shows one of them: an oak one if there is one, else a vanilla one
// Previews: the crafting table recipes anyone can make (not ones needing research): an entry not yet researched can
// still be opened to see these (ResearchBook). Element Crafter recipes stay hidden until the entry is unlocked.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class RecipeCatalog
{
    // Item id -> its recipes. Swapped whole, never changed in place (volatile: every thread sees the newest).
    private static volatile Map<Identifier, List<RecipeRecord>> recipes = Map.of();
    private static volatile Map<Identifier, List<RecipeRecord>> previews = Map.of();
    // The Element Crafter recipes that need a research entry unlocked to be made: the entry (for recipe viewers)
    private static volatile Map<RecipeRecord, DustRecipe.ResearchKey> locks = Map.of();
    // The crafting table recipes that are Dust's own (dust:dust_crafting, DustRecipe), which recipe viewers don't know
    private static volatile Set<RecipeRecord> dustTable = Set.of();

    private RecipeCatalog() {}

    // The recipes that make this item (empty if none)
    public static List<RecipeRecord> recipesFor(Identifier item)
    {
        return recipes.getOrDefault(item, List.of());
    }

    // The research entry a recipe needs unlocked before it can be made, if any (Element Crafter recipes)
    // Whether a crafting table recipe is one of Dust's own (DustRecipe), for recipe viewers to show themselves
    public static boolean isDustTableRecipe(RecipeRecord recipe)
    {
        return dustTable.contains(recipe);
    }

    public static Optional<DustRecipe.ResearchKey> lockOf(RecipeRecord recipe)
    {
        return Optional.ofNullable(locks.get(recipe));
    }

    // Every item's recipes (item id -> its recipes), as last sent; for recipe viewers (Integration/jei)
    public static Map<Identifier, List<RecipeRecord>> all()
    {
        return recipes;
    }

    // The open crafting table recipes for this item: what an unresearched entry shows (empty if none)
    public static List<RecipeRecord> previewsFor(Identifier item)
    {
        return previews.getOrDefault(item, List.of());
    }

    // ---- building it (server) ----

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event)
    {
        build(event.getServer());
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event)
    {
        if (event.getPlayer() == null) build(event.getPlayerList().getServer());
        Sync payload = new Sync(recipes, previews, locks, List.copyOf(dustTable));
        event.getRelevantPlayers().forEach(player -> PacketDistributor.sendToPlayer(player, payload));
    }

    private static void build(MinecraftServer server)
    {
        Map<Identifier, List<RecipeRecord>> found = new HashMap<>();
        Map<Identifier, List<RecipeRecord>> open = new HashMap<>();
        Optional<Identifier> table = Optional.of(BuiltInRegistries.ITEM.getKey(Items.CRAFTING_TABLE));
        Optional<Identifier> crafter = Optional.of(BuiltInRegistries.ITEM.getKey(DustBlockRegistry.ELEMENT_CRAFTER.get().asItem()));
        // Sorted by id, so the book lists them the same way every time
        List<RecipeHolder<CraftingRecipe>> crafting = new ArrayList<>(server.getRecipeManager().recipeMap().byType(RecipeType.CRAFTING));
        crafting.sort(Comparator.comparing(holder -> holder.id().identifier().toString()));
        Map<RecipeRecord, DustRecipe.ResearchKey> lockedBy = new HashMap<>();
        Set<RecipeRecord> ownTable = new java.util.HashSet<>();
        for (RecipeHolder<CraftingRecipe> holder : crafting) {
            RecipeRecord record = fromCrafting(holder.value(), table);
            if (record == null) continue;
            found.computeIfAbsent(record.output(), id -> new ArrayList<>()).add(record);
            if (holder.value() instanceof DustRecipe dust) {
                ownTable.add(record);
                dust.research().ifPresent(key -> lockedBy.put(record, key));
            }
            if (!(holder.value() instanceof DustRecipe dust && dust.needsResearch())) open.computeIfAbsent(record.output(), id -> new ArrayList<>()).add(record);
        }
        List<RecipeHolder<ElementCraftingRecipe>> element = new ArrayList<>(server.getRecipeManager().recipeMap().byType(DustRecipeRegistry.ELEMENT_CRAFTING_TYPE.get()));
        element.sort(Comparator.comparing(holder -> holder.id().identifier().toString()));
        for (RecipeHolder<ElementCraftingRecipe> holder : element) {
            RecipeRecord record = fromElementCrafting(holder.value(), crafter);
            if (record == null) continue;
            found.computeIfAbsent(record.output(), id -> new ArrayList<>()).add(record);
            holder.value().research().ifPresent(key -> lockedBy.put(record, key));
        }
        locks = Map.copyOf(lockedBy);
        dustTable = Set.copyOf(ownTable);
        Map<Identifier, List<RecipeRecord>> frozen = new HashMap<>();
        found.forEach((id, list) -> frozen.put(id, List.copyOf(list)));
        recipes = Map.copyOf(frozen);
        Map<Identifier, List<RecipeRecord>> frozenOpen = new HashMap<>();
        open.forEach((id, list) -> frozenOpen.put(id, List.copyOf(list)));
        previews = Map.copyOf(frozenOpen);
    }

    private static RecipeRecord fromCrafting(CraftingRecipe recipe, Optional<Identifier> station)
    {
        if (recipe instanceof ShapedRecipe shaped) {
            return record(station, shaped.getWidth(), shaped.getIngredients(), shaped.assemble(CraftingInput.EMPTY), List.of());
        }
        if (recipe instanceof ShapelessRecipe shapeless) {
            return shapeless(station, shapeless.placementInfo().ingredients(), List.of(), shapeless.result(), List.of());
        }
        if (recipe instanceof DustRecipe dust) {
            return shapeless(station, dust.ingredients(), dust.dusts(), dust.result(), List.of());
        }
        return null;
    }

    private static RecipeRecord fromElementCrafting(ElementCraftingRecipe recipe, Optional<Identifier> station)
    {
        List<RecipeRecord.Cost> costs = recipe.elements().stream().map(cost -> new RecipeRecord.Cost(cost.element(), cost.amount())).toList();
        if (recipe.shapedPattern().isPresent()) {
            ShapedRecipePattern pattern = recipe.shapedPattern().get();
            return record(station, pattern.width(), pattern.ingredients(), recipe.result().create(), costs);
        }
        return shapeless(station, recipe.ingredients(), recipe.dusts(), recipe.result(), costs);
    }

    // A shapeless recipe laid out three to a row, the dust needs (as that dust's item) after the ingredients
    private static RecipeRecord shapeless(Optional<Identifier> station, List<Ingredient> ingredients, List<DustRecipe.DustNeed> dusts,
                                          ItemStackTemplate result, List<RecipeRecord.Cost> costs)
    {
        List<Optional<Ingredient>> slots = new ArrayList<>();
        ingredients.forEach(ingredient -> slots.add(Optional.of(ingredient)));
        List<Identifier> dustSlots = new ArrayList<>();
        for (DustRecipe.DustNeed need : dusts) {
            ItemStack dust = CrystalElements.dustOf(DustElements.of(need.element()), 1);
            for (int i = 0; i < need.min() && !dust.isEmpty(); i++) dustSlots.add(BuiltInRegistries.ITEM.getKey(dust.getItem()));
        }
        RecipeRecord record = record(station, 3, slots, result.create(), costs);
        if (record == null || dustSlots.isEmpty()) return record;
        List<Identifier> all = new ArrayList<>(record.slots());
        all.addAll(dustSlots);
        return new RecipeRecord(station, 3, all, record.output(), costs);
    }

    // The grid as item ids (each ingredient as the item it shows), or null for a recipe that makes nothing
    private static RecipeRecord record(Optional<Identifier> station, int width, List<Optional<Ingredient>> grid, ItemStack result, List<RecipeRecord.Cost> costs)
    {
        if (result.isEmpty()) return null;
        List<Identifier> slots = new ArrayList<>();
        for (Optional<Ingredient> slot : grid) slots.add(slot.map(RecipeCatalog::shown).orElse(RecipeRecord.EMPTY));
        return new RecipeRecord(station, Math.max(1, width), slots, BuiltInRegistries.ITEM.getKey(result.getItem()), costs);
    }

    // Which of an ingredient's items it shows
    private static Identifier shown(Ingredient ingredient)
    {
        return ingredient.items().min(Comparator.comparingInt(CraftedCompositions::preference))
                .map(Holder::value).map(BuiltInRegistries.ITEM::getKey).orElse(RecipeRecord.EMPTY);
    }

    // ---- sending it ----

    public record Sync(Map<Identifier, List<RecipeRecord>> recipes, Map<Identifier, List<RecipeRecord>> previews,
                       Map<RecipeRecord, DustRecipe.ResearchKey> locks, List<RecipeRecord> dustTable) implements CustomPacketPayload
    {
        public static final Type<Sync> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "recipe_catalog"));
        private static final StreamCodec<RegistryFriendlyByteBuf, RecipeRecord> RECORD = ByteBufCodecs.fromCodecWithRegistries(RecipeRecord.CODEC);
        public static final StreamCodec<RegistryFriendlyByteBuf, Sync> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, RECORD.apply(ByteBufCodecs.list())), Sync::recipes,
                ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, RECORD.apply(ByteBufCodecs.list())), Sync::previews,
                ByteBufCodecs.map(HashMap::new, RECORD, DustRecipe.ResearchKey.STREAM_CODEC), Sync::locks,
                RECORD.apply(ByteBufCodecs.list()), Sync::dustTable,
                Sync::new);

        @Override
        public Type<Sync> type()
        {
            return TYPE;
        }

        // Only swaps the map, so it needs no client-only code
        public static void handle(Sync payload, IPayloadContext context)
        {
            RecipeCatalog.recipes = Map.copyOf(payload.recipes());
            RecipeCatalog.previews = Map.copyOf(payload.previews());
            RecipeCatalog.locks = Map.copyOf(payload.locks());
            RecipeCatalog.dustTable = Set.copyOf(payload.dustTable());
        }
    }
}
