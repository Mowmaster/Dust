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
    // How far a player can observe from: by eye, and looking through a spyglass
    public static final double RANGE = 10.0;
    public static final double SPYGLASS_RANGE = 64.0;
    // How long something must be looked at before it's noted (ticks; 20 = one second)
    public static final int BLOCK_TICKS = 20;
    public static final int MOB_TICKS = 60;
    // The look is checked every CHECK_EVERY ticks, so a player looking around doesn't cost a ray trace every tick
    private static final int CHECK_EVERY = 4;

    // What each player is looking at and for how long (server memory only; nothing to save)
    private static final Map<UUID, Look> LOOKS = new HashMap<>();

    private record Look(ResearchEntry entry, BlockPos pos, int ticks, boolean done) {}

    private ResearchObserver() {}

    // Plants and bosses come from tags, so the tab lists are rebuilt when tags (re)load
    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event)
    {
        ResearchCategory.clearCache();
    }

    // ---- observation ----

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player) || player.tickCount % CHECK_EVERY != 0) return;
        if (player.isSpectator() || !Research.carriesNotes(player)) {
            LOOKS.remove(player.getUUID());
            return;
        }

        Target target = lookedAt(player);
        Look look = LOOKS.get(player.getUUID());
        if (target == null) {
            LOOKS.remove(player.getUUID());
            return;
        }
        if (look == null || !look.entry().equals(target.entry())) {
            look = new Look(target.entry(), target.pos(), 0, false);
        }
        look = new Look(look.entry(), target.pos(), look.ticks() + CHECK_EVERY, look.done());
        int needed = target.entry().isEntity() ? MOB_TICKS : BLOCK_TICKS;
        if (!look.done() && look.ticks() >= needed) {
            Research.observe(player, look.entry(), look.pos());
            look = new Look(look.entry(), look.pos(), look.ticks(), true);
        }
        LOOKS.put(player.getUUID(), look);
    }

    private record Target(ResearchEntry entry, BlockPos pos) {}

    // The nearest researchable block or mob along the player's view, within range
    private static Target lookedAt(ServerPlayer player)
    {
        double range = player.isScoping() ? SPYGLASS_RANGE : RANGE;
        Vec3 eye = player.getEyePosition();
        Vec3 view = player.getViewVector(1.0F);
        Vec3 end = eye.add(view.scale(range));

        BlockHitResult blockHit = player.level().clip(new ClipContext(eye, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        Vec3 entityEnd = blockHit.getType() == HitResult.Type.MISS ? end : blockHit.getLocation();
        AABB search = player.getBoundingBox().expandTowards(view.scale(range)).inflate(1.0);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(player, eye, entityEnd, search,
                entity -> entity instanceof LivingEntity && entity.isPickable() && !entity.isSpectator() && entity != player,
                eye.distanceToSqr(entityEnd));

        if (entityHit != null) {
            ResearchEntry entry = ResearchCategory.entryFor(entityHit.getEntity().getType());
            if (entry != null) return new Target(entry, entityHit.getEntity().blockPosition());
        }
        if (blockHit.getType() == HitResult.Type.BLOCK) {
            ResearchEntry entry = ResearchCategory.entryFor(player.level().getBlockState(blockHit.getBlockPos()).getBlock());
            if (entry != null) return new Target(entry, blockHit.getBlockPos());
        }
        return null;
    }

    // ---- experimentation ----

    // Breaking a block: you've clearly seen it (observe), and you learn what it drops
    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event)
    {
        if (!(event.getBreaker() instanceof ServerPlayer player) || !Research.carriesNotes(player)) return;
        ResearchEntry entry = ResearchCategory.entryFor(event.getState().getBlock());
        if (entry == null) return;
        Research.observe(player, entry, event.getPos());
        List<Identifier> drops = Research.itemIds(event.getDrops().stream().map(ItemEntity::getItem).toList());
        if (!drops.isEmpty()) Research.experiment(player, entry, old -> old.withDrops(drops));
    }

    // Defeating a mob: observe it, and learn its drops and health
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event)
    {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player) || !Research.carriesNotes(player)) return;
        LivingEntity mob = event.getEntity();
        ResearchEntry entry = ResearchCategory.entryFor(mob.getType());
        if (entry == null) return;
        Research.observe(player, entry, mob.blockPosition());
        List<Identifier> drops = Research.itemIds(event.getDrops().stream().map(ItemEntity::getItem).toList());
        Research.experiment(player, entry, old -> old.withDrops(drops).withMaxHealth(mob.getMaxHealth()));
    }

    // Fighting a mob: learn its health
    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player) || !(event.getTarget() instanceof LivingEntity mob) || !Research.carriesNotes(player)) return;
        ResearchEntry entry = ResearchCategory.entryFor(mob.getType());
        if (entry != null) Research.experiment(player, entry, old -> old.withMaxHealth(mob.getMaxHealth()));
    }

    // Feeding an animal its breeding food: learn what it eats
    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.EntityInteract event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player) || !(event.getTarget() instanceof Animal animal) || !Research.carriesNotes(player)) return;
        ItemStack food = event.getItemStack();
        if (food.isEmpty() || !animal.isFood(food)) return;
        ResearchEntry entry = ResearchCategory.entryFor(animal.getType());
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

        Container grid = event.getInventory();
        List<ItemStack> ingredients = new ArrayList<>();
        for (int slot = 0; slot < grid.getContainerSize(); slot++) {
            if (!grid.getItem(slot).isEmpty()) ingredients.add(grid.getItem(slot));
        }
        Identifier resultId = BuiltInRegistries.ITEM.getKey(result.getItem());

        if (result.getItem() instanceof BlockItem blockItem) {
            ResearchEntry entry = ResearchCategory.entryFor(blockItem.getBlock());
            List<Identifier> from = Research.itemIds(ingredients);
            if (entry != null) Research.experiment(player, entry, old -> old.withMadeFrom(from));
        }
        ingredients.stream()
                .filter(stack -> stack.getItem() instanceof BlockItem)
                .map(stack -> ResearchCategory.entryFor(((BlockItem) stack.getItem()).getBlock()))
                .filter(Objects::nonNull)
                .distinct()
                .forEach(entry -> Research.experiment(player, entry, old -> old.withMakes(resultId)));
    }

    // The guide gets a copy of the crafter's research and their name (crafting recipes can't see who's crafting, so
    // this happens here, as the result is taken out of the grid)
    private static void fillGuide(ServerPlayer player, ItemStack guide)
    {
        guide.set(DustComponentDataRegistry.RESEARCH_SNAPSHOT.get(), Research.get(player));
        String author = player.getGameProfile().name();
        guide.set(DustComponentDataRegistry.RESEARCH_AUTHOR.get(), author);
        guide.set(net.minecraft.core.component.DataComponents.LORE, new ItemLore(List.of(
                Component.translatable("item.dust.shared_research_guide.author", author))));
    }
}
