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
import net.neoforged.neoforge.network.PacketDistributor;

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

    private record Look(ResearchEntry entry, BlockPos pos, int ticks, boolean done, boolean reported) {}

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
            look = new Look(target.entry(), target.pos(), 0, false, false);
        }
        look = new Look(look.entry(), target.pos(), look.ticks() + CHECK_EVERY, look.done(), look.reported());

        // Nothing new to learn here: no progress shown (and no waiting)
        if (look.done() || !Research.wouldLearn(player, look.entry(), look.pos())) {
            if (look.reported()) PacketDistributor.sendToPlayer(player, ObservationPayloads.Progress.stop());
            LOOKS.put(player.getUUID(), new Look(look.entry(), look.pos(), look.ticks(), true, false));
            return;
        }

        int needed = look.entry().tab().observeTicks();
        if (look.ticks() >= needed) {
            PacketDistributor.sendToPlayer(player, ObservationPayloads.Progress.stop());
            Research.observe(player, look.entry(), look.pos());
            look = new Look(look.entry(), look.pos(), look.ticks(), true, false);
        } else {
            PacketDistributor.sendToPlayer(player, new ObservationPayloads.Progress(look.entry().tab().id(), look.entry().id(), (float) look.ticks() / needed));
            look = new Look(look.entry(), look.pos(), look.ticks(), false, true);
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

    // Breaking a block: you've clearly seen it (observe), and you learn what it drops
    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event)
    {
        if (!(event.getBreaker() instanceof ServerPlayer player) || !Research.carriesNotes(player)) return;
        ResearchEntry entry = ResearchTabs.entryForBlock(event.getState());
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
        ResearchEntry entry = ResearchTabs.entryForEntity(mob);
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

        Container grid = event.getInventory();
        List<ItemStack> ingredients = new ArrayList<>();
        for (int slot = 0; slot < grid.getContainerSize(); slot++) {
            if (!grid.getItem(slot).isEmpty()) ingredients.add(grid.getItem(slot));
        }
        Identifier resultId = BuiltInRegistries.ITEM.getKey(result.getItem());

        // The result's entry (its block, or the item itself) learns what it's made from; each ingredient's entry learns
        // what it makes
        ResearchEntry resultEntry = ResearchTabs.entryForItem(result);
        List<Identifier> from = Research.itemIds(ingredients);
        if (resultEntry != null) Research.experiment(player, resultEntry, old -> old.withMadeFrom(from));
        ingredients.stream()
                .map(ResearchTabs::entryForItem)
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
