package com.mowmaster.dust.Features.Research;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.SmokerMenu;
import java.util.Optional;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.component.DataComponents;
import com.mowmaster.dust.Features.Research.Tabs.EffectResearchTab;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

// Watches players who carry Research Notes and records what they observe and experiment with (see Research).
@EventBusSubscriber(modid = DustReferences.MODID)
public final class ResearchObserver
{
    // How far a player can observe from: see ResearchLook (10 blocks, 64 through a spyglass)
    // How long something must be looked at before it's noted: each tab's observeTicks() (blocks and items 1 s, mobs 3 s)
    // The look is checked every CHECK_EVERY ticks, so a player looking around doesn't cost a ray trace every tick
    private static final int CHECK_EVERY = 4;

    // What each player is looking at and for how long (server memory only; nothing to save).
    // reported: the client has been sent progress for it (and so must be told when it stops)
    private static final Map<UUID, Look> LOOKS = new HashMap<>();

    // effectsDone: the effects on the looked-at mob have been noted for this look
    private record Look(ResearchEntry entry, BlockPos pos, int ticks, boolean done, boolean reported, boolean effectsDone) {}

    private ResearchObserver() {}

    // Plants and bosses come from tags, so the tab lists are rebuilt when tags (re)load
    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event)
    {
        ResearchTabs.clearCaches();
    }

    // ---- observation ----

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player) || player.tickCount % CHECK_EVERY != 0) return;
        Look look = LOOKS.get(player.getUUID());
        ResearchLook.Target target = player.isSpectator() || !Research.carriesNotes(player) ? null : ResearchLook.lookedAt(player);

        if (target == null) {
            stopLooking(player, look);
            return;
        }
        if (look == null || !look.entry().equals(target.entry())) {
            stopLooking(player, look);
            look = new Look(target.entry(), target.pos(), 0, false, false, false);
        }
        look = new Look(look.entry(), target.pos(), look.ticks() + CHECK_EVERY, look.done(), look.reported(), look.effectsDone());

        // Watching a mob for a moment also notes the effects it has (Effects tab)
        if (!look.effectsDone() && look.ticks() >= ResearchTabs.EFFECTS.observeTicks() && target.entity() instanceof LivingEntity mob) {
            for (MobEffectInstance effect : mob.getActiveEffects()) {
                ResearchEntry effectEntry = new ResearchEntry(ResearchTabs.EFFECTS, EffectResearchTab.idOf(effect.getEffect()));
                Research.observe(player, effectEntry, mob.blockPosition());
            }
            look = new Look(look.entry(), look.pos(), look.ticks(), look.done(), look.reported(), true);
        }

        // Nothing new to learn here: no progress shown (and no waiting)
        if (look.done() || !Research.wouldLearn(player, look.entry(), look.pos())) {
            if (look.reported()) PacketDistributor.sendToPlayer(player, ObservationPayloads.Progress.stop());
            LOOKS.put(player.getUUID(), new Look(look.entry(), look.pos(), look.ticks(), true, false, look.effectsDone()));
            return;
        }

        int needed = look.entry().tab().observeTicks();
        if (look.ticks() >= needed) {
            PacketDistributor.sendToPlayer(player, ObservationPayloads.Progress.stop());
            Research.observe(player, look.entry(), look.pos());
            look = new Look(look.entry(), look.pos(), look.ticks(), true, false, look.effectsDone());
        } else {
            PacketDistributor.sendToPlayer(player, new ObservationPayloads.Progress(look.entry().tab().id(), look.entry().id(), (float) look.ticks() / needed));
            look = new Look(look.entry(), look.pos(), look.ticks(), false, true, look.effectsDone());
        }
        LOOKS.put(player.getUUID(), look);
    }

    // Forgets what the player was looking at, telling their client to hide the progress bar if it was showing
    private static void stopLooking(ServerPlayer player, Look look)
    {
        if (look != null && look.reported()) PacketDistributor.sendToPlayer(player, ObservationPayloads.Progress.stop());
        LOOKS.remove(player.getUUID());
    }

    // ---- experimentation ----

    // Watching a furnace, smoker or blast furnace work: what each player's open furnace screen held last tick.
    // When the output count goes up, the item that was in the input slot just before is what got cooked.
    private record FurnaceWatch(int containerId, Item input, Item output, int outputCount) {}
    private static final Map<UUID, FurnaceWatch> FURNACES = new HashMap<>();

    @SubscribeEvent
    public static void onWatchFurnace(PlayerTickEvent.Post event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!(player.containerMenu instanceof AbstractFurnaceMenu menu) || !Research.carriesNotes(player)) {
            FURNACES.remove(player.getUUID());
            return;
        }
        ItemStack input = menu.getSlot(AbstractFurnaceMenu.INGREDIENT_SLOT).getItem();
        ItemStack output = menu.getSlot(AbstractFurnaceMenu.RESULT_SLOT).getItem();
        FurnaceWatch now = new FurnaceWatch(menu.containerId, input.getItem(), output.getItem(), output.getCount());
        FurnaceWatch before = FURNACES.put(player.getUUID(), now);

        boolean sameScreen = before != null && before.containerId() == now.containerId();
        boolean cooked = sameScreen && !output.isEmpty() && before.input() != Items.AIR
                && (before.output() == Items.AIR ? now.outputCount() > 0 : before.output() == now.output() && now.outputCount() > before.outputCount());
        if (cooked) {
            Item furnace = menu instanceof SmokerMenu ? Items.SMOKER : menu instanceof BlastFurnaceMenu ? Items.BLAST_FURNACE : Items.FURNACE;
            recordMaking(player, Optional.of(itemId(furnace)), 1, List.of(new ItemStack(before.input())), output);
        }
    }

    // Getting an effect yourself: you know what it feels like. If it came from the food you just finished eating, the
    // food's entry notes it too (a golden apple adds Regeneration and Absorption; raw chicken adds Hunger only when
    // it happens to, and then it's added to the list).
    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player) || !Research.carriesNotes(player)) return;
        Identifier effectId = EffectResearchTab.idOf(event.getEffectInstance().getEffect());
        Research.observe(player, new ResearchEntry(ResearchTabs.EFFECTS, effectId), player.blockPosition());

        ResearchEntry food = foodBeingFinished(player);
        if (food != null) {
            Research.observe(player, food, player.blockPosition());
            Research.experiment(player, food, research -> research.withFoodEffect(effectId));
        }
    }

    // The food the player is finishing eating right now, or null. A food's effects are applied while the player is
    // still "using" it, on the tick its use time runs out (LivingEntity#completeUsingItem), so an effect added then
    // came from that food (and not from, say, a splash potion that hit them halfway through the meal).
    private static @Nullable ResearchEntry foodBeingFinished(ServerPlayer player)
    {
        if (!player.isUsingItem() || player.getUseItemRemainingTicks() > 0) return null;
        ItemStack eating = player.getUseItem();
        return eating.has(DataComponents.FOOD) ? ResearchTabs.entryForItem(eating) : null;
    }

    // Finishing eating something: it counts as finding it, and its entry learns how much saturation it gives.
    // FoodProperties is the item's "minecraft:food" component: nutrition (hunger points), saturation, canAlwaysEat.
    // Finish is posted after the food has done everything (effects included); getItem() is a copy of the food as it
    // was before the bite.
    @SubscribeEvent
    public static void onAte(LivingEntityUseItemEvent.Finish event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player) || !Research.carriesNotes(player)) return;
        FoodProperties food = event.getItem().get(DataComponents.FOOD);
        ResearchEntry entry = food == null ? null : ResearchTabs.entryForItem(event.getItem());
        if (entry == null) return;
        Research.observe(player, entry, player.blockPosition());
        Research.experiment(player, entry, research -> research.withSaturation(food.saturation()));
    }

    // Watching a brewing stand work: what each player's open brewing screen held last tick (the ingredient, and the
    // effects in each of the three bottles). When the ingredient is used up and a bottle changes, the ingredient either
    // brewed a new effect ("Brewed with") or lengthened / strengthened one ("Modified with").
    private record BrewingWatch(int containerId, Item ingredient, int ingredientCount, List<Map<Identifier, MobEffectInstance>> bottles) {}
    private static final Map<UUID, BrewingWatch> BREWING = new HashMap<>();

    @SubscribeEvent
    public static void onWatchBrewing(PlayerTickEvent.Post event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!(player.containerMenu instanceof BrewingStandMenu menu) || !Research.carriesNotes(player)) {
            BREWING.remove(player.getUUID());
            return;
        }
        ItemStack ingredient = menu.getSlot(3).getItem();
        List<Map<Identifier, MobEffectInstance>> bottles = new ArrayList<>();
        for (int slot = 0; slot < 3; slot++) bottles.add(effectsIn(menu.getSlot(slot).getItem()));
        BrewingWatch now = new BrewingWatch(menu.containerId, ingredient.getItem(), ingredient.getCount(), bottles);
        BrewingWatch before = BREWING.put(player.getUUID(), now);

        // A brew: same screen, the ingredient was used up, and a bottle changed
        if (before == null || before.containerId() != now.containerId() || before.ingredient() == Items.AIR) return;
        boolean used = now.ingredient() == Items.AIR ? before.ingredientCount() > 0
                : now.ingredient() == before.ingredient() && now.ingredientCount() < before.ingredientCount();
        if (!used) return;

        Identifier ingredientId = BuiltInRegistries.ITEM.getKey(before.ingredient());
        for (int slot = 0; slot < 3; slot++) {
            Map<Identifier, MobEffectInstance> was = before.bottles().get(slot);
            for (Map.Entry<Identifier, MobEffectInstance> effect : now.bottles().get(slot).entrySet()) {
                ResearchEntry entry = new ResearchEntry(ResearchTabs.EFFECTS, effect.getKey());
                MobEffectInstance old = was.get(effect.getKey());
                if (old == null) {
                    Research.observe(player, entry, player.blockPosition());
                    Research.experiment(player, entry, research -> research.withMadeFrom(List.of(ingredientId)));
                } else if (effect.getValue().getAmplifier() > old.getAmplifier() || effect.getValue().getDuration() > old.getDuration()) {
                    Research.experiment(player, entry, research -> research.withModifier(ingredientId));
                }
            }
        }
    }

    // The effects a potion gives, by effect id
    private static Map<Identifier, MobEffectInstance> effectsIn(ItemStack stack)
    {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        if (contents == null) return Map.of();
        Map<Identifier, MobEffectInstance> effects = new HashMap<>();
        for (MobEffectInstance effect : contents.getAllEffects()) effects.put(EffectResearchTab.idOf(effect.getEffect()), effect);
        return effects;
    }

    // Food popping off a campfire: the raw item is still on the campfire when the cooked one appears (the campfire
    // empties its slot just after). Players carrying notes who are close by and looking at the campfire see it cook.
    @SubscribeEvent
    public static void onCampfireFood(EntityJoinLevelEvent event)
    {
        if (event.loadedFromDisk() || !(event.getEntity() instanceof ItemEntity dropped) || !(event.getLevel() instanceof ServerLevel level)) return;
        BlockPos at = dropped.blockPosition();
        CampfireBlockEntity campfire = level.getBlockEntity(at) instanceof CampfireBlockEntity here ? here
                : level.getBlockEntity(at.below()) instanceof CampfireBlockEntity below ? below : null;
        if (campfire == null) return;

        ItemStack cooked = dropped.getItem();
        ItemStack raw = ItemStack.EMPTY;
        for (ItemStack onFire : campfire.getItems()) {
            if (onFire.isEmpty()) continue;
            SingleRecipeInput input = new SingleRecipeInput(onFire);
            ItemStack result = level.recipeAccess().getRecipeFor(RecipeType.CAMPFIRE_COOKING, input, level)
                    .map(recipe -> recipe.value().assemble(input)).orElse(ItemStack.EMPTY);
            if (!result.isEmpty() && result.is(cooked.getItem())) {
                raw = onFire.copyWithCount(1);
                break;
            }
        }
        if (raw.isEmpty()) return;

        Vec3 centre = Vec3.atCenterOf(campfire.getBlockPos());
        for (ServerPlayer player : level.players()) {
            if (player.distanceToSqr(centre) > ResearchLook.RANGE * ResearchLook.RANGE || !Research.carriesNotes(player)) continue;
            // Roughly facing it (within about 30 degrees of the view)
            Vec3 toFire = centre.subtract(player.getEyePosition()).normalize();
            if (player.getViewVector(1.0F).dot(toFire) < 0.85) continue;
            recordMaking(player, Optional.of(itemId(campfire.getBlockState().getBlock().asItem())), 1, List.of(raw), cooked);
        }
    }

    // Breaking a block: you've clearly seen it (observe), and you learn what it drops
    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event)
    {
        if (!(event.getBreaker() instanceof ServerPlayer player) || !Research.carriesNotes(player)) return;
        ResearchEntry entry = ResearchTabs.entryForBlock(event.getState());
        if (entry == null) return;
        Research.observe(player, entry, event.getPos());
        List<ItemStack> dropped = event.getDrops().stream().map(ItemEntity::getItem).toList();
        List<Identifier> drops = Research.itemIds(dropped);
        if (!drops.isEmpty()) Research.experiment(player, entry, old -> old.withDrops(drops));
        noteResults(player, entry, dropped, event.getPos());
    }

    // Defeating a mob: observe it, and learn its drops and health
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event)
    {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player) || !Research.carriesNotes(player)) return;
        LivingEntity mob = event.getEntity();
        ResearchEntry entry = ResearchTabs.entryForEntity(mob);
        if (entry == null) return;
        Research.observe(player, entry, mob.blockPosition());
        List<ItemStack> dropped = event.getDrops().stream().map(ItemEntity::getItem).toList();
        List<Identifier> drops = Research.itemIds(dropped);
        Research.experiment(player, entry, old -> old.withDrops(drops).withMaxHealth(mob.getMaxHealth()));
        noteResults(player, entry, dropped, mob.blockPosition());
    }

    // Whatever an experiment gives you is noted too (each drop starts its own entry, with the usual toast), so the
    // book's drop icons can open them and the items' Sources pages can point back
    private static void noteResults(ServerPlayer player, ResearchEntry from, List<ItemStack> results, BlockPos where)
    {
        results.stream()
                .filter(stack -> !stack.isEmpty())
                .map(ResearchTabs::entryForItem)
                .filter(Objects::nonNull)
                .filter(result -> !result.equals(from))
                .distinct()
                .forEach(result -> Research.observe(player, result, where));
    }

    // Fighting a mob: learn its health
    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player) || !(event.getTarget() instanceof LivingEntity mob) || !Research.carriesNotes(player)) return;
        ResearchEntry entry = ResearchTabs.entryForEntity(mob);
        if (entry != null) Research.experiment(player, entry, old -> old.withMaxHealth(mob.getMaxHealth()));
    }

    // Feeding an animal its breeding food: learn what it eats
    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.EntityInteract event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player) || !(event.getTarget() instanceof Animal animal) || !Research.carriesNotes(player)) return;
        ItemStack food = event.getItemStack();
        if (food.isEmpty() || !animal.isFood(food)) return;
        ResearchEntry entry = ResearchTabs.entryForEntity(animal);
        if (entry != null) Research.experiment(player, entry, old -> old.withBreeding(BuiltInRegistries.ITEM.getKey(food.getItem())));
    }

    // Crafting: the result's entry learns what it's made from, each ingredient's entry learns what it makes.
    // Also fills in a Shared Research Guide with the crafter's notes.
    @SubscribeEvent
    public static void onCrafted(PlayerEvent.ItemCraftedEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ItemStack result = event.getCrafting();

        if (result.is(DustItemRegistry.SHARED_RESEARCH_GUIDE.get())) {
            fillGuide(player, result);
            return;
        }
        if (!Research.carriesNotes(player)) return;

        // The crafting grid as it was (2x2 in the inventory, 3x3 at a table), with empty rows and columns trimmed off
        Container grid = event.getInventory();
        int width = grid instanceof CraftingContainer crafting ? crafting.getWidth() : grid.getContainerSize();
        List<ItemStack> slots = new ArrayList<>();
        for (int slot = 0; slot < grid.getContainerSize(); slot++) slots.add(grid.getItem(slot));
        // Which station: a crafting table's screen, or nothing for the inventory's own 2x2 grid
        Optional<Identifier> station = player.containerMenu instanceof CraftingMenu ? Optional.of(itemId(Items.CRAFTING_TABLE)) : Optional.empty();
        recordMaking(player, station, width, slots, result);
    }

    // Something was made: the result's entry learns the recipe ("Crafted from") and each ingredient's entry learns it
    // too ("Crafted into"). Making something counts as finding it. slots: the ingredient grid, row by row.
    private static void recordMaking(ServerPlayer player, Optional<Identifier> station, int width, List<ItemStack> slots, ItemStack result)
    {
        RecipeRecord recipe = trimmed(station, width, slots, itemId(result.getItem()));
        ResearchEntry resultEntry = ResearchTabs.entryForItem(result);
        if (resultEntry != null) {
            Research.observe(player, resultEntry, player.blockPosition());
            Research.experiment(player, resultEntry, old -> old.withMadeBy(recipe));
        }
        slots.stream()
                .filter(stack -> !stack.isEmpty())
                .map(ResearchTabs::entryForItem)
                .filter(Objects::nonNull)
                .distinct()
                .forEach(entry -> Research.experiment(player, entry, old -> old.withUsedIn(recipe)));
    }

    private static Identifier itemId(Item item)
    {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    // The grid with fully empty rows and columns removed (an ingot in the corner of a 3x3 grid becomes a 1x1 grid)
    private static RecipeRecord trimmed(Optional<Identifier> station, int width, List<ItemStack> slots, Identifier output)
    {
        int rows = width <= 0 ? 0 : (slots.size() + width - 1) / width;
        int minRow = rows, maxRow = -1, minCol = width, maxCol = -1;
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i).isEmpty()) continue;
            int row = i / width, col = i % width;
            minRow = Math.min(minRow, row); maxRow = Math.max(maxRow, row);
            minCol = Math.min(minCol, col); maxCol = Math.max(maxCol, col);
        }
        if (maxRow < 0) return new RecipeRecord(station, 0, List.of(), output);
        List<Identifier> ids = new ArrayList<>();
        for (int row = minRow; row <= maxRow; row++) {
            for (int col = minCol; col <= maxCol; col++) {
                int i = row * width + col;
                ItemStack stack = i < slots.size() ? slots.get(i) : ItemStack.EMPTY;
                ids.add(stack.isEmpty() ? RecipeRecord.EMPTY : itemId(stack.getItem()));
            }
        }
        return new RecipeRecord(station, maxCol - minCol + 1, ids, output);
    }

    // The guide gets a copy of the crafter's research and their name (crafting recipes can't see who's crafting, so
    // this happens here, as the result is taken out of the grid)
    private static void fillGuide(ServerPlayer player, ItemStack guide)
    {
        guide.set(DustComponentDataRegistry.RESEARCH_SNAPSHOT.get(), Research.get(player));
        String author = player.getGameProfile().name();
        guide.set(DustComponentDataRegistry.RESEARCH_AUTHOR.get(), author);
        guide.set(DataComponents.LORE, new ItemLore(List.of(
                Component.translatable("item.dust.shared_research_guide.author", author))));
    }
}
