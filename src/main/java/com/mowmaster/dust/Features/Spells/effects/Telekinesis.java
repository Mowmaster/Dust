package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellConfig;
import com.mowmaster.dust.Features.Spells.entities.FloatingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jspecify.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// Telekinesis (the Warp + Vacuum fusion, SpellRegistries.FUSION_WARP_VACUUM): a hitscan spell that takes hold of what it
// lands on. With AOE it takes the nearest creatures there too (1, plus Target Count, as AOE always does).
//   a block    lifted out as a floating block (FloatingBlockEntity) and held like a creature
//   tap        (cast and let go at once, or a grimoire key, or a Spell Caster): pushed away the way the caster faces
//   hold       (the scroll or grimoire held down: SpellScrollItem / GrimoireItem.onUseTick -> hold): pulled to the caster
//              and kept floating in front of them
//   swipe      held, then let go while turning quickly: thrown the way the view was moving
// How far a push or throw goes: PUSH_DISTANCE, plus a block for each level of Range. How fast a throw is: times the
// spell's speed over the base speed (Velocity). Self: Vacuum's own self effect (the fusion leaves it to the element).
@EventBusSubscriber(modid = DustReferences.MODID)
public final class Telekinesis
{
    public static final double PUSH_DISTANCE = 4.0;
    // Held this long or less (ticks) when let go: a tap (a push)
    public static final int TAP_TICKS = 6;
    // How far the view must have turned over the last few ticks to count as a swipe (a length of the change in the
    // look vector)
    private static final double SWIPE = 0.25;
    private static final int LOOK_HISTORY = 4;
    // Where a held target floats: this far in front of the caster's eyes
    private static final double HOLD_DISTANCE = 2.5;
    // A grab not held for this many ticks is let go as a tap (no scroll held down: a key, a Spell Caster)
    private static final int UNHELD_TICKS = 2;
    // How long a lifted block floats after it's let go before it falls (ticks)
    private static final int BLOCK_FLOAT_AFTER = 60;

    // A caster's grip: what they hold, how far a push / throw goes, how fast a throw is, when it was last held, and their
    // last few looks (for a swipe)
    private static final class Grip
    {
        final ServerLevel level;
        final List<Entity> targets = new ArrayList<>();
        final double distance;
        final double speed;
        long lastHeld;
        boolean everHeld;
        final Deque<Vec3> looks = new ArrayDeque<>();

        Grip(ServerLevel level, double distance, double speed, long now)
        {
            this.level = level;
            this.distance = distance;
            this.speed = speed;
            this.lastHeld = now;
        }
    }

    private static final Map<UUID, Grip> GRIPS = new HashMap<>();

    private Telekinesis() {}

    public static boolean applies(@Nullable Spell spell)
    {
        return spell != null && spell.modifiers().contains(SpellRegistries.FUSION_WARP_VACUUM.getId());
    }

    // ---- the fusion's parts ----

    public static void grabEntity(SpellCast cast, Entity target)
    {
        if (target == cast.caster() || !target.isAlive()) return;
        take(cast, target);
    }

    public static void grabBlock(SpellCast cast, BlockPos pos, Direction face)
    {
        FloatingBlockEntity floating = FloatingBlockEntity.liftEntity(cast.level(), pos, 20 * 60);
        if (floating != null) take(cast, floating);
    }

    private static void take(SpellCast cast, Entity target)
    {
        double distance = PUSH_DISTANCE + cast.spell().levelsOf(SpellRegistries.RANGE_MODIFIER.getId());
        double speed = Math.max(0.1, cast.stats().speed() / SpellConfig.BASE_SPEED);
        LivingEntity caster = cast.caster();
        cast.level().sendParticles(ParticleTypes.REVERSE_PORTAL, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 12, 0.3, 0.3, 0.3, 0.05);
        // No caster to hold it (a Spell Caster): a push along the cast at once
        if (caster == null) {
            push(target, cast.direction(), distance, null);
            return;
        }
        long now = cast.level().getGameTime();
        Grip grip = GRIPS.get(caster.getUUID());
        if (grip == null || grip.level != cast.level() || now - grip.lastHeld > UNHELD_TICKS) {
            grip = new Grip(cast.level(), distance, speed, now);
            GRIPS.put(caster.getUUID(), grip);
        }
        if (!grip.targets.contains(target)) grip.targets.add(target);
    }

    // ---- holding, letting go ----

    // Each tick the scroll / grimoire is held: everything gripped floats to in front of the caster
    public static void hold(ServerPlayer player)
    {
        Grip grip = GRIPS.get(player.getUUID());
        if (grip == null) return;
        grip.lastHeld = player.level().getGameTime();
        grip.everHeld = true;
        grip.looks.addLast(player.getLookAngle());
        while (grip.looks.size() > LOOK_HISTORY) grip.looks.removeFirst();
        Vec3 eyes = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        for (int i = 0; i < grip.targets.size(); i++) {
            Entity target = grip.targets.get(i);
            if (!target.isAlive()) continue;
            // Several held: spread a little around the spot
            Vec3 spot = eyes.add(look.scale(HOLD_DISTANCE + i * 0.4)).add(0, -target.getBbHeight() / 2 + (i % 2) * 0.5, 0);
            Vec3 toward = spot.subtract(target.position());
            Vec3 motion = toward.scale(0.35);
            if (motion.length() > 1.2) motion = motion.normalize().scale(1.2);
            move(target, motion, player);
            target.fallDistance = 0;
        }
    }

    // Let go after heldTicks: a tap pushes, a swipe throws, otherwise it just drops where it is
    public static void release(ServerPlayer player, int heldTicks)
    {
        Grip grip = GRIPS.remove(player.getUUID());
        if (grip == null) return;
        if (heldTicks <= TAP_TICKS || !grip.everHeld) {
            for (Entity target : grip.targets) push(target, player.getLookAngle(), grip.distance, player);
            return;
        }
        Vec3 now = player.getLookAngle();
        Vec3 before = grip.looks.isEmpty() ? now : grip.looks.peekFirst();
        Vec3 swipe = now.subtract(before);
        for (Entity target : grip.targets) {
            if (swipe.length() >= SWIPE) {
                throwAlong(target, swipe.normalize(), grip.distance, grip.speed, player);
            } else {
                move(target, Vec3.ZERO, player);
                if (target instanceof FloatingBlockEntity floating) floating.floatFor(BLOCK_FLOAT_AFTER);
            }
        }
    }

    // Grips nobody is holding (a quick cast, a grimoire key) are let go as taps
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event)
    {
        if (GRIPS.isEmpty()) return;
        for (Iterator<Map.Entry<UUID, Grip>> it = GRIPS.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<UUID, Grip> entry = it.next();
            Grip grip = entry.getValue();
            if (grip.level.getGameTime() - grip.lastHeld <= UNHELD_TICKS) continue;
            it.remove();
            ServerPlayer player = event.getServer().getPlayerList().getPlayer(entry.getKey());
            if (player == null) continue;
            if (!grip.everHeld) for (Entity target : grip.targets) push(target, player.getLookAngle(), grip.distance, player);
        }
    }

    // ---- moving things ----

    // Away along direction, far enough to travel about `distance` blocks
    private static void push(Entity target, Vec3 direction, double distance, @Nullable Entity by)
    {
        Vec3 along = direction.normalize();
        if (target instanceof FloatingBlockEntity floating) {
            // No gravity, drag 0.9 a tick: it drifts about ten times its starting speed
            floating.launch(by, along.scale(distance / 10.0));
            floating.floatFor(BLOCK_FLOAT_AFTER);
        } else {
            // A little up so it clears the ground; in the air a creature keeps about 0.91 of its speed a tick
            move(target, along.scale(distance / 5.8).add(0, 0.3, 0), by);
        }
        target.level().playSound(null, target.blockPosition(), net.minecraft.sounds.SoundEvents.ENDER_PEARL_THROW, net.minecraft.sounds.SoundSource.PLAYERS, 0.6F, 0.8F);
    }

    // Thrown: like a push, but along the swipe and faster with Velocity
    private static void throwAlong(Entity target, Vec3 direction, double distance, double speed, @Nullable Entity by)
    {
        if (target instanceof FloatingBlockEntity floating) {
            floating.launch(by, direction.scale(distance / 10.0 * speed));
            floating.floatFor(BLOCK_FLOAT_AFTER);
        } else {
            move(target, direction.scale(distance / 5.8 * speed).add(0, 0.2, 0), by);
        }
        target.level().playSound(null, target.blockPosition(), net.minecraft.sounds.SoundEvents.PLAYER_ATTACK_SWEEP, net.minecraft.sounds.SoundSource.PLAYERS, 0.8F, 1.2F);
    }

    // Sets its motion (a player's own client moves them, so it's sent the new speed: needsSync, as SimpleParts.pushAway)
    private static void move(Entity target, Vec3 motion, @Nullable Entity by)
    {
        if (target instanceof FloatingBlockEntity floating) {
            floating.launch(by, motion);
            return;
        }
        target.setDeltaMovement(motion);
        target.needsSync = true;
    }
}
