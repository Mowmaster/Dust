package com.mowmaster.dust.Features.Spells.soul;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.mixin.MannequinAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Soul's three effects. The event side (death, respawn, reunions, timeouts, broken blocks) is in SoulEvents.
//   Self    a ghost of you stays where you cast it: die before it fades and you respawn there, keeping what you carried
//           when you cast it (only what you've picked up since drops)
//   Block   soul-binds the block: broken, it drops nothing and comes back a few minutes later (once)
//   Entity  pulls the soul out: the body is left lifeless and can't be hurt, the soul is thrown a block away per second
//           of the spell and has to find its body before the spell ends, or it dies where its body lies
public final class SoulParts
{
    // Ghost: duration x 24 (2 minutes at the default). Soul-binding: duration x 12 to break it (a minute), and it comes
    // back duration x 36 after it's broken (3 minutes).
    public static final int ANCHOR_TIME_SCALE = 24;
    public static final int BIND_TIME_SCALE = 12;
    public static final int RESPAWN_TIME_SCALE = 36;

    private SoulParts() {}

    // ---- self ----

    public static void anchor(SpellCast cast, LivingEntity target)
    {
        if (!(target instanceof ServerPlayer player)) return;
        ServerLevel level = cast.level();
        int ticks = cast.stats().duration() * ANCHOR_TIME_SCALE;

        // One anchor at a time: the old ghost goes
        player.getExistingData(DustAttachmentTypeRegistry.SOUL_ANCHOR).ifPresent(old -> SoulEvents.removeGhost(level.getServer(), old));

        Mannequin ghost = mannequin(level, player, player.position(), player.getYRot());
        if (ghost == null) return;
        ghost.setData(DustAttachmentTypeRegistry.SOUL_TETHER, new SoulTether(SoulTether.Kind.ANCHOR, player.getUUID(), Optional.empty()));
        // Ghostly: it can't be hurt, and when it runs out the ghost fades (SoulEvents)
        ghost.addEffect(new MobEffectInstance(DustEffectsRegistry.GHOSTLY, ticks, 0, false, false));
        level.addFreshEntity(ghost);

        player.setData(DustAttachmentTypeRegistry.SOUL_ANCHOR, new SoulAnchor(level.dimension(), player.position(), player.getYRot(),
                snapshot(player.getInventory()), level.getGameTime() + ticks, Optional.of(ghost.getUUID()), List.of(), false));
        level.sendParticles(ParticleTypes.SOUL, player.getX(), player.getY() + 1, player.getZ(), 20, 0.3, 0.6, 0.3, 0.02);
        level.playSound(null, player.blockPosition(), SoundEvents.SOUL_ESCAPE.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    // Copies of everything in the inventory (main, armour, offhand), merged later by SoulEvents when matching
    private static List<ItemStack> snapshot(Inventory inventory)
    {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) stacks.add(stack.copy());
        }
        return stacks;
    }

    // ---- block ----

    public static void bind(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        if (level.getBlockState(pos).isAir() || level.getBlockState(pos).getDestroySpeed(level, pos) < 0) return;
        SoulEvents.bind(level, pos, cast.stats().duration() * BIND_TIME_SCALE, cast.stats().duration() * RESPAWN_TIME_SCALE);
        level.sendParticles(ParticleTypes.SOUL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.4, 0.4, 0.4, 0.01);
    }

    // ---- entity ----

    public static void pull(SpellCast cast, Entity target)
    {
        if (!(target instanceof LivingEntity living) || !living.isAlive() || living.is(Tags.EntityTypes.BOSSES)) return;
        if (living.hasData(DustAttachmentTypeRegistry.SOUL_TETHER) || living.hasEffect(DustEffectsRegistry.SOUL_SEVERED)) return;
        ServerLevel level = cast.level();
        int ticks = cast.stats().duration();
        // A block away per second of the spell
        double distance = Math.max(2, ticks / 20.0);
        Optional<UUID> caster = Optional.ofNullable(cast.caster()).map(Entity::getUUID);

        if (living instanceof ServerPlayer player) {
            if (player.isCreative() || player.isSpectator()) return;
            // The body stays: a mannequin wearing their skin, lying where they stood
            Mannequin body = mannequin(level, player, player.position(), player.getYRot());
            if (body == null) return;
            body.setPose(Pose.SLEEPING);
            body.setData(DustAttachmentTypeRegistry.SOUL_TETHER, new SoulTether(SoulTether.Kind.PLAYER_BODY, player.getUUID(), caster));
            body.addEffect(new MobEffectInstance(DustEffectsRegistry.GHOSTLY, ticks, 0, false, false));
            level.addFreshEntity(body);
            // The soul: the player, flying through walls, thrown away from the body
            player.addEffect(new MobEffectInstance(DustEffectsRegistry.SOUL_SEVERED, ticks, 0));
            player.addEffect(new MobEffectInstance(DustEffectsRegistry.SPIRIT_FORM, ticks, 0));
            throwPlayer(level, player, distance);
        } else if (living instanceof Mob) {
            // The body stays put, lifeless (Ghostly: no AI, can't be hurt); the soul is a wisp
            Vec3 at = away(level, living.getBoundingBox().getCenter(), distance, true);
            SoulWispEntity wisp = SoulWispEntity.spawn(level, living, at);
            if (wisp == null) return;
            living.setData(DustAttachmentTypeRegistry.SOUL_TETHER, new SoulTether(SoulTether.Kind.MOB_BODY, wisp.getUUID(), caster));
            living.addEffect(new MobEffectInstance(DustEffectsRegistry.GHOSTLY, ticks, 0, false, true));
        } else {
            return;
        }
        level.sendParticles(ParticleTypes.SOUL, living.getX(), living.getY() + living.getBbHeight() / 2, living.getZ(), 20, 0.3, 0.5, 0.3, 0.05);
        level.playSound(null, living.blockPosition(), SoundEvents.SOUL_ESCAPE.value(), SoundSource.NEUTRAL, 1.2F, 0.7F);
    }

    // Throws a player distance away: onto ground nearby if there's somewhere to stand (randomTeleport looks downward for
    // a spot), otherwise just there (they're in Spirit Form, so walls don't matter)
    private static void throwPlayer(ServerLevel level, ServerPlayer player, double distance)
    {
        Vec3 at = away(level, player.position(), distance, false);
        if (!player.randomTeleport(at.x, at.y, at.z, true, state -> false)) player.teleportTo(at.x, at.y, at.z);
    }

    // A point distance away in a random direction, about the same height. free: anywhere will do (a wisp goes through
    // blocks); otherwise, if the ground there is far above (a cliff, a cave roof), it stays level for randomTeleport
    private static Vec3 away(ServerLevel level, Vec3 from, double distance, boolean free)
    {
        float angle = level.getRandom().nextFloat() * Mth.TWO_PI;
        double x = from.x + Mth.cos(angle) * distance;
        double z = from.z + Mth.sin(angle) * distance;
        double y = from.y + level.getRandom().nextDouble() - 0.5;
        if (!free) {
            int surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mth.floor(x), Mth.floor(z));
            if (surface > from.y + distance) y = from.y;
        }
        return new Vec3(x, y, z);
    }

    // A mannequin wearing the player's skin, with no "NPC" label, that can't be pushed around
    static @Nullable Mannequin mannequin(ServerLevel level, Player player, Vec3 at, float yaw)
    {
        Mannequin mannequin = EntityTypes.MANNEQUIN.create(level, EntitySpawnReason.TRIGGERED);
        if (mannequin == null) return null;
        // Casting to the mixin interface: Mixin made Mannequin implement it
        MannequinAccessor access = (MannequinAccessor) mannequin;
        access.dust$setProfile(ResolvableProfile.createResolved(player.getGameProfile()));
        access.dust$setHideDescription(true);
        access.dust$setImmovable(true);
        mannequin.snapTo(at.x, at.y, at.z, yaw, 0);
        mannequin.setYHeadRot(yaw);
        mannequin.setYBodyRot(yaw);
        return mannequin;
    }
}
