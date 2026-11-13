package com.mowmaster.dust.Features.soul;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerRespawnPositionEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// What happens after Soul's spells (SoulParts):
//   Self    on death, what the player carried at the cast is kept back from the drops; they respawn at the ghost and
//           get it back
//   Block   a soul-bound block that's broken drops nothing and is scheduled to come back (SoulBlockRespawns)
//   Entity  a player soul touching its body, or a wisp touching its body, puts it back; a body whose Ghostly runs out
//           first dies (a player is pulled back to their body to die there)
@EventBusSubscriber(modid = DustReferences.MODID)
public final class SoulEvents
{
    // Soul-bound blocks (in memory: you've a minute or so to break one): until when, and how long it takes to come back
    private record BoundKey(ResourceKey<Level> dimension, BlockPos pos) {}
    private record Bound(long until, int respawnTicks) {}
    private static final Map<BoundKey, Bound> BOUND = new HashMap<>();
    // Blocks a player just broke that were soul-bound: their drops are thrown away when they come (the same tick)
    private static final Map<BoundKey, Long> TAKEN = new HashMap<>();

    private SoulEvents() {}

    // ---- self: the anchor ----

    // Death: what's in the snapshot is kept back, matched by kind and counted (a damaged tool still counts as the tool);
    // only the rest drops. HIGH: before other drop handlers (the void, research) see the drops.
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onDrops(LivingDropsEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        SoulAnchor anchor = player.getExistingDataOrNull(DustAttachmentTypeRegistry.SOUL_ANCHOR);
        if (anchor == null || anchor.respawning()) return;
        if (player.level().getGameTime() > anchor.expiresAt()) {
            player.removeData(DustAttachmentTypeRegistry.SOUL_ANCHOR);
            return;
        }
        // How many of each snapshot stack are still owed (copies, so counting down doesn't change the record)
        List<ItemStack> owed = new ArrayList<>();
        for (ItemStack stack : anchor.snapshot()) owed.add(stack.copy());
        List<ItemStack> kept = new ArrayList<>();
        // removeIf: goes through the drops, and takes out the ones the lambda says true for
        event.getDrops().removeIf(drop -> {
            ItemStack stack = drop.getItem();
            for (ItemStack want : owed) {
                if (want.isEmpty() || !matches(want, stack)) continue;
                int take = Math.min(want.getCount(), stack.getCount());
                kept.add(stack.split(take));
                want.shrink(take);
                if (stack.isEmpty()) return true;
            }
            return false;
        });
        player.setData(DustAttachmentTypeRegistry.SOUL_ANCHOR, anchor.withDeath(kept));
    }

    private static boolean matches(ItemStack want, ItemStack stack)
    {
        return ItemStack.isSameItemSameComponents(want, stack)
                || (want.isDamageableItem() && ItemStack.isSameItem(want, stack));
    }

    // Respawning: at the ghost instead of the bed or world spawn. The event's player is the one that died, whose anchor
    // still says respawning.
    @SubscribeEvent
    public static void onRespawnPosition(PlayerRespawnPositionEvent event)
    {
        if (event.isFromEndFight()) return;
        SoulAnchor anchor = event.getEntity().getExistingDataOrNull(DustAttachmentTypeRegistry.SOUL_ANCHOR);
        if (anchor == null || !anchor.respawning()) return;
        ServerLevel level = event.getEntity().level().getServer().getLevel(anchor.dimension());
        if (level == null) return;
        event.setTeleportTransition(new TeleportTransition(level, anchor.pos(), Vec3.ZERO, anchor.yaw(), 0, TeleportTransition.DO_NOTHING));
    }

    // Respawned (the new player has the anchor: it's copied on death): hand back what was kept, and the ghost goes
    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        SoulAnchor anchor = player.getExistingDataOrNull(DustAttachmentTypeRegistry.SOUL_ANCHOR);
        if (anchor == null || !anchor.respawning()) return;
        player.removeData(DustAttachmentTypeRegistry.SOUL_ANCHOR);
        // Anything with no room drops at their feet (add() leaves what didn't fit in the stack)
        for (ItemStack stack : anchor.kept()) {
            if (!player.getInventory().add(stack) && !stack.isEmpty()) player.spawnAtLocation(player.level(), stack);
        }
        removeGhost(player.level().getServer(), anchor);
        if (player.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.SOUL, player.getX(), player.getY() + 1, player.getZ(), 30, 0.3, 0.6, 0.3, 0.03);
            level.playSound(null, player.blockPosition(), SoundEvents.SOUL_ESCAPE.value(), SoundSource.PLAYERS, 1.0F, 1.2F);
        }
    }

    // The ghost vanishes (if its chunk is loaded; otherwise it fades when its own Ghostly runs out)
    static void removeGhost(MinecraftServer server, SoulAnchor anchor)
    {
        ServerLevel level = server.getLevel(anchor.dimension());
        if (level == null || anchor.ghost().isEmpty()) return;
        Entity ghost = level.getEntity(anchor.ghost().get());
        if (ghost != null) fade(level, ghost);
    }

    private static void fade(ServerLevel level, Entity entity)
    {
        level.sendParticles(ParticleTypes.SOUL, entity.getX(), entity.getY() + 1, entity.getZ(), 15, 0.3, 0.6, 0.3, 0.02);
        entity.discard();
    }

    // ---- entity: souls finding their bodies, bodies running out of time ----

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event)
    {
        Entity entity = event.getEntity();
        if (!(entity.level() instanceof ServerLevel level)) return;

        // A player soul: every few ticks, look for its body within reach
        if (entity instanceof ServerPlayer player && entity.tickCount % 5 == 0 && player.hasEffect(DustEffectsRegistry.SOUL_SEVERED)) {
            for (Mannequin body : level.getEntitiesOfClass(Mannequin.class, player.getBoundingBox().inflate(1.0), m -> isBodyOf(m, player))) {
                reunitePlayer(level, body, player);
                return;
            }
        }

        // Something tethered whose time (its Ghostly effect) has run out. Done here, after the entity's tick, rather than
        // when the effect expires: the effect list is being gone through then, and Ghostly would still block the damage.
        SoulTether tether = entity.getExistingDataOrNull(DustAttachmentTypeRegistry.SOUL_TETHER);
        if (tether == null || !(entity instanceof LivingEntity living) || living.hasEffect(DustEffectsRegistry.GHOSTLY)) return;
        living.removeData(DustAttachmentTypeRegistry.SOUL_TETHER);
        switch (tether.kind()) {
            case ANCHOR -> fade(level, living);
            case PLAYER_BODY -> playerDies(level, living, tether);
            case MOB_BODY -> mobDies(level, living, tether);
        }
    }

    private static boolean isBodyOf(Mannequin mannequin, Player player)
    {
        SoulTether tether = mannequin.getExistingDataOrNull(DustAttachmentTypeRegistry.SOUL_TETHER);
        return tether != null && tether.kind() == SoulTether.Kind.PLAYER_BODY && tether.spirit().equals(player.getUUID());
    }

    private static void reunitePlayer(ServerLevel level, Mannequin body, ServerPlayer player)
    {
        body.removeData(DustAttachmentTypeRegistry.SOUL_TETHER);
        body.discard();
        player.removeEffect(DustEffectsRegistry.SOUL_SEVERED);
        player.removeEffect(DustEffectsRegistry.SPIRIT_FORM);
        player.teleportTo(body.getX(), body.getY(), body.getZ());
        level.sendParticles(ParticleTypes.SOUL, player.getX(), player.getY() + 1, player.getZ(), 25, 0.3, 0.6, 0.3, 0.03);
        level.playSound(null, player.blockPosition(), SoundEvents.SOUL_ESCAPE.value(), SoundSource.PLAYERS, 1.0F, 1.4F);
    }

    // A wisp reached its body (SoulWispEntity): the body wakes up (losing Ghostly gives it its AI back)
    static void reuniteMob(ServerLevel level, LivingEntity body, SoulWispEntity wisp)
    {
        body.removeData(DustAttachmentTypeRegistry.SOUL_TETHER);
        body.removeEffect(DustEffectsRegistry.GHOSTLY);
        wisp.discard();
        level.sendParticles(ParticleTypes.SOUL, body.getX(), body.getY() + body.getBbHeight() / 2, body.getZ(), 20, 0.3, 0.5, 0.3, 0.03);
        level.playSound(null, body.blockPosition(), SoundEvents.SOUL_ESCAPE.value(), SoundSource.NEUTRAL, 1.0F, 1.4F);
    }

    // The player didn't make it back: pulled to their body and killed there (so they drop their things by it). If they
    // aren't severed any more (they died some other way, or logged out), the body just goes.
    private static void playerDies(ServerLevel level, LivingEntity body, SoulTether tether)
    {
        ServerPlayer player = level.getServer().getPlayerList().getPlayer(tether.spirit());
        Vec3 at = body.position();
        fade(level, body);
        if (player == null || !player.isAlive() || !player.hasEffect(DustEffectsRegistry.SOUL_SEVERED)) return;
        player.removeEffect(DustEffectsRegistry.SOUL_SEVERED);
        player.removeEffect(DustEffectsRegistry.SPIRIT_FORM);
        player.teleport(new TeleportTransition(level, at, Vec3.ZERO, player.getYRot(), player.getXRot(), TeleportTransition.DO_NOTHING));
        player.hurtServer(level, killer(level, tether), Float.MAX_VALUE);
    }

    // The wisp didn't make it back: the body dies (as the caster's kill, so it drops what a player kill drops)
    private static void mobDies(ServerLevel level, LivingEntity body, SoulTether tether)
    {
        if (level.getEntity(tether.spirit()) instanceof SoulWispEntity wisp) wisp.discard();
        level.sendParticles(ParticleTypes.SOUL, body.getX(), body.getY() + body.getBbHeight() / 2, body.getZ(), 20, 0.3, 0.5, 0.3, 0.05);
        body.hurtServer(level, killer(level, tether), Float.MAX_VALUE);
    }

    // Magic damage from the caster if they're about (it gets past armour); out-of-world damage if not
    private static DamageSource killer(ServerLevel level, SoulTether tether)
    {
        Entity caster = tether.caster().map(level::getEntity).orElse(null);
        return caster != null ? level.damageSources().indirectMagic(caster, caster) : level.damageSources().fellOutOfWorld();
    }

    // ---- block: soul-bound blocks ----

    static void bind(ServerLevel level, BlockPos pos, int bindTicks, int respawnTicks)
    {
        long now = level.getGameTime();
        if (BOUND.size() > 256) BOUND.values().removeIf(bound -> bound.until() < now);
        BOUND.put(new BoundKey(level.dimension(), pos.immutable()), new Bound(now + bindTicks, respawnTicks));
    }

    // Takes the soul-binding off pos and schedules the block to come back; false if it wasn't bound
    private static boolean release(ServerLevel level, BlockPos pos, net.minecraft.world.level.block.state.BlockState state)
    {
        Bound bound = BOUND.remove(new BoundKey(level.dimension(), pos));
        if (bound == null || bound.until() < level.getGameTime()) return false;
        SoulBlockRespawns.get(level).schedule(pos, state, level.getGameTime() + bound.respawnTicks());
        level.sendParticles(ParticleTypes.SOUL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.3, 0.3, 0.3, 0.03);
        return true;
    }

    // A player breaks it (in any game mode; creative breaks drop nothing anyway, so this is where they're caught)
    @SubscribeEvent
    public static void onBreak(BreakBlockEvent event)
    {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (TAKEN.size() > 64) TAKEN.values().removeIf(at -> level.getGameTime() - at > 1);
        if (release(level, event.getPos(), event.getState())) TAKEN.put(new BoundKey(level.dimension(), event.getPos().immutable()), level.getGameTime());
    }

    // Its drops: none. Also catches soul-bound blocks broken without a player (explosions). LOWEST: research still
    // learns what it would have dropped.
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockDrops(BlockDropsEvent event)
    {
        ServerLevel level = event.getLevel();
        BoundKey key = new BoundKey(level.dimension(), event.getPos());
        Long taken = TAKEN.remove(key);
        boolean wasTaken = taken != null && level.getGameTime() - taken <= 1;
        if (wasTaken || release(level, event.getPos(), event.getState())) {
            event.getDrops().clear();
            event.setDroppedExperience(0);
        }
    }

    // Once a second, bring back the blocks whose time is up
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event)
    {
        if (event.getLevel() instanceof ServerLevel level && level.getGameTime() % 20 == 0) SoulBlockRespawns.get(level).tick(level);
    }
}
