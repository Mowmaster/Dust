package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.mowlib.compacting.CompactingChains;
import com.mowmaster.mowlib.compacting.CompactingContents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

// What crafted things are made of: the elements of what goes into them. A hay bale is 9 wheat, so it's 9 growth and
// 9 plants; a piston is 3 planks, a redstone dust, an iron ingot and 4 cobblestone, so it's all of theirs added up.
// Worked out on the server from the crafting recipes whenever datapacks load (recipes may change), and sent to each
// player (the client has no recipes, but tooltips and the research book need these).
//   - Only plain shaped and shapeless crafting recipes count (not special ones like map cloning or firework stars).
//   - An ingredient that takes several items (any planks) counts as one of them: oak if there's an oak one ("default to
//     oak"), else a vanilla one, else the first.
//   - What the grid gives back (the bucket from a milk bucket) is taken off that ingredient's elements.
//   - All of that is shared out between the items made (4 planks from a log: each a quarter of it), rounded, but each
//     element there at least once.
//   - Storage pairs (MowLib's CompactingChains: 9 nuggets <-> ingot <-> block, 9 wheat <-> hay bale): only the top of
//     the chain is worked out; the lower tiers keep their own elements (else an ingot would be 9 nuggets, as one of
//     the ways to craft it).
//   - An item with several recipes uses the one named after it (minecraft:piston), else the first by name that can
//     be worked out. Recipes going round in a circle are skipped.
//   - Anything that isn't crafted (or whose recipes can't be worked out) keeps the dust:elements data map's entry.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class CraftedCompositions
{
    // Item -> what it's made of. Swapped whole when worked out again (or when the server's copy arrives on a client),
    // never changed in place, so reading it from another thread is safe. volatile: every thread sees the newest one.
    private static volatile Map<Item, DustComposition> crafted = Map.of();

    private CraftedCompositions() {}

    // What this item is made of by its recipe, or null if it isn't worked out from one
    public static @Nullable DustComposition of(Item item)
    {
        return crafted.get(item);
    }

    // ---- working it out (server) ----

    // Once the server has loaded its datapacks...
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event)
    {
        crafted = compute(event.getServer());
    }

    // ...and again when they're reloaded (no player: /reload, sent to everyone), then sent to whoever needs it (a
    // player joining, or everyone). EventPriority.LOW: after MowLib's handler for this event has forgotten its old
    // compacting chains.
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDatapackSync(OnDatapackSyncEvent event)
    {
        if (event.getPlayer() == null) crafted = compute(event.getPlayerList().getServer());
        Sync payload = new Sync(crafted);
        event.getRelevantPlayers().forEach(player -> PacketDistributor.sendToPlayer(player, payload));
    }

    private static Map<Item, DustComposition> compute(MinecraftServer server)
    {
        // Each item made by a plain crafting recipe, with those recipes
        Map<Item, List<RecipeHolder<CraftingRecipe>>> byResult = new HashMap<>();
        for (RecipeHolder<CraftingRecipe> holder : server.getRecipeManager().recipeMap().byType(RecipeType.CRAFTING)) {
            ItemStack result = resultOf(holder.value());
            if (!result.isEmpty()) byResult.computeIfAbsent(result.getItem(), item -> new ArrayList<>()).add(holder);
        }
        Solver solver = new Solver(byResult);
        Map<Item, DustComposition> found = new HashMap<>();
        for (Item item : byResult.keySet()) {
            DustComposition composition = solver.solve(item);
            if (composition != null) found.put(item, composition);
        }
        return Map.copyOf(found);
    }

    // What a plain crafting recipe makes (EMPTY for any other kind). A shaped recipe keeps its result to itself, but
    // assemble gives a copy of it whatever's in the grid.
    private static ItemStack resultOf(CraftingRecipe recipe)
    {
        if (recipe instanceof ShapelessRecipe shapeless) return shapeless.result().create();
        if (recipe instanceof ShapedRecipe shaped) return shaped.assemble(CraftingInput.EMPTY);
        return ItemStack.EMPTY;
    }

    private static List<Ingredient> ingredientsOf(CraftingRecipe recipe)
    {
        List<Ingredient> ingredients = new ArrayList<>();
        if (recipe instanceof ShapelessRecipe shapeless) {
            // placementInfo: the recipe's ingredients as the recipe book places them (a shapeless recipe's own list
            // is private)
            ingredients.addAll(shapeless.placementInfo().ingredients());
        } else if (recipe instanceof ShapedRecipe shaped) {
            // Optional.stream(): the ingredient, or nothing for an empty slot of the pattern
            shaped.getIngredients().forEach(slot -> slot.ifPresent(ingredients::add));
        }
        return ingredients;
    }

    // Works items out one at a time, each from the items in its recipe (worked out first, the same way), remembering
    // every answer
    private static final class Solver
    {
        private final Map<Item, List<RecipeHolder<CraftingRecipe>>> byResult;
        // Optional.empty(): worked out, and it has no composition
        private final Map<Item, Optional<DustComposition>> done = new HashMap<>();
        // Being worked out right now: a recipe that needs one of these goes round in a circle
        private final Set<Item> working = new HashSet<>();

        Solver(Map<Item, List<RecipeHolder<CraftingRecipe>>> byResult)
        {
            this.byResult = byResult;
        }

        // What it's made of by its recipe, or null to leave it to the data map
        @Nullable DustComposition solve(Item item)
        {
            Optional<DustComposition> known = done.get(item);
            if (known != null) return known.orElse(null);
            List<RecipeHolder<CraftingRecipe>> recipes = byResult.get(item);
            if (recipes == null || isLowerTier(item) || !working.add(item)) return null;
            Identifier name = BuiltInRegistries.ITEM.getKey(item);
            List<RecipeHolder<CraftingRecipe>> ordered = new ArrayList<>(recipes);
            // Comparator.comparing(...).thenComparing(...): sort by the first thing, ties by the second. false sorts
            // before true, so the recipe named after the item comes first.
            ordered.sort(Comparator.comparing((RecipeHolder<CraftingRecipe> holder) -> !holder.id().identifier().equals(name))
                    .thenComparing(holder -> holder.id().identifier().toString()));
            DustComposition result = null;
            for (RecipeHolder<CraftingRecipe> holder : ordered) {
                result = fromRecipe(holder.value());
                if (result != null) break;
            }
            working.remove(item);
            done.put(item, Optional.ofNullable(result));
            return result;
        }

        // An item with a tier above it in a compacting chain (a nugget, an ingot, wheat) keeps its own elements
        private static boolean isLowerTier(Item item)
        {
            List<CompactingContents.Tier> chain = CompactingChains.find(item);
            return chain != null && chain.getLast().item() != item;
        }

        // The recipe's ingredients added up and shared out between what it makes, or null if one of them can't be
        // worked out (or would go round in a circle)
        private @Nullable DustComposition fromRecipe(CraftingRecipe recipe)
        {
            List<Ingredient> ingredients = ingredientsOf(recipe);
            if (ingredients.isEmpty()) return null;
            Map<DustElement, Integer> total = new LinkedHashMap<>();
            for (Ingredient ingredient : ingredients) {
                Item used = choose(ingredient);
                if (used == null) return null;
                DustComposition part = compositionOf(used);
                if (part == null) {
                    // On its way already (a circle), or it has no dust at all: this recipe won't do
                    return null;
                }
                Map<DustElement, Integer> counts = new LinkedHashMap<>(part.counted());
                // What the grid hands back keeps its elements: take them off
                ItemStackTemplate remainder = used.getCraftingRemainder();
                if (remainder != null) {
                    DustComposition back = compositionOf(remainder.item().value());
                    if (back != null) back.counted().forEach((element, count) -> counts.computeIfPresent(element, (key, had) -> had > count ? had - count : null));
                }
                counts.forEach((element, count) -> total.merge(element, count, Integer::sum));
            }
            int made = Math.max(1, resultOf(recipe).getCount());
            List<DustElement> elements = new ArrayList<>();
            total.forEach((element, count) -> {
                int each = Math.max(1, Math.round(count / (float) made));
                for (int i = 0; i < each; i++) elements.add(element);
            });
            return elements.isEmpty() ? null : new DustComposition(elements);
        }

        // An item's composition: from its own recipe if it has one that works, else its own (data map, crystal...)
        private @Nullable DustComposition compositionOf(Item item)
        {
            if (working.contains(item)) return null;
            DustComposition fromRecipe = solve(item);
            if (fromRecipe != null) return fromRecipe;
            return DustCompositions.ofUncrafted(new ItemStack(item));
        }

        // Which of an ingredient's items it counts as: an oak one, else a vanilla one, else the first; only ones with
        // any dust. null if none has.
        private @Nullable Item choose(Ingredient ingredient)
        {
            List<Holder<Item>> items = ingredient.items().toList();
            if (items.isEmpty()) return null;
            List<Holder<Item>> ordered = new ArrayList<>(items);
            ordered.sort(Comparator.comparingInt(CraftedCompositions::preference));
            for (Holder<Item> holder : ordered) {
                if (!working.contains(holder.value()) && compositionOf(holder.value()) != null) return holder.value();
            }
            return null;
        }
    }

    // Lower is better: 0 an oak item, 1 a vanilla one, 2 anything else
    private static int preference(Holder<Item> holder)
    {
        Identifier id = holder.unwrapKey().map(key -> key.identifier()).orElse(null);
        if (id == null) return 2;
        if (id.getNamespace().equals("minecraft") && id.getPath().startsWith("oak_")) return 0;
        return id.getNamespace().equals("minecraft") ? 1 : 2;
    }

    // ---- sending it (server -> client) ----

    // The whole table. Registered in DustMagicEvents; the handler only swaps the map, so it needs no client code.
    public record Sync(Map<Item, DustComposition> compositions) implements CustomPacketPayload
    {
        public static final Type<Sync> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "crafted_compositions"));
        // An element by its id; a composition as its list of elements
        private static final StreamCodec<RegistryFriendlyByteBuf, DustComposition> COMPOSITION = ByteBufCodecs.fromCodecWithRegistries(DustComposition.CODEC);
        // ByteBufCodecs.map: a count, then each key and value. registry(Registries.ITEM): an item by its network id.
        public static final StreamCodec<RegistryFriendlyByteBuf, Sync> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.map(HashMap::new, ByteBufCodecs.registry(Registries.ITEM), COMPOSITION), Sync::compositions,
                Sync::new);

        @Override
        public Type<Sync> type()
        {
            return TYPE;
        }

        public static void handle(Sync payload, IPayloadContext context)
        {
            crafted = Map.copyOf(payload.compositions());
        }
    }
}
