package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.entities.RisingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

// The Flight + Chaos fusion, Inversion: gravity turned upside down.
//   self, creatures  the Inverted effect (DustEffectsRegistry.INVERTED): their gravity attribute goes negative (Potency:
//                    stronger; Duration: longer). Anything whose gravity is below zero, from this or anything else, is
//                    treated as upside down:
//                      it falls up, and stands on what's above it as on a floor (onEntityTick: it counts as on the
//                      ground when it bumps a ceiling, so it can walk and jump there)
//                      a jump goes down, away from the ceiling (onJump)
//                      its eyes are at the bottom of its box, where its head now hangs (onSize; the box itself is
//                      the same)
//                      it's drawn upside down, as vanilla draws a mob named Dinnerbone, and a player's view is turned
//                      over too (Client/GravityInversionClient)
//                    When it wears off they fall back down (and take fall damage as usual).
//   on a block       the block rises (RisingBlockEntity), faster with Potency, until it meets a ceiling or the top of the
//                    world, and is placed there, with its block entity's contents
// The gravity attribute is synced to clients for every creature, so both sides tell who's upside down the same way.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class GravityInversion
{
    // Lift a tick for a block (blocks a tick, each tick), and this much more for each point of potency
    private static final double BLOCK_LIFT = 0.03;
    private static final double BLOCK_LIFT_PER_POTENCY = 0.02;

    private GravityInversion() {}

    // Whether its gravity pulls it up. Safe while the creature is still being made (its attributes aren't there yet).
    public static boolean inverted(Entity entity)
    {
        if (!(entity instanceof LivingEntity living) || living.getAttributes() == null) return false;
        AttributeInstance gravity = living.getAttribute(Attributes.GRAVITY);
        return gravity != null && gravity.getValue() < 0 && !living.isNoGravity();
    }

    // Whether its size is set upside down now (an attachment of its own on each side, so nothing is shared between the
    // server's and the client's copy; hasData first: this is asked while an entity is still being made)
    private static boolean flipped(Entity entity)
    {
        return entity.hasData(DustAttachmentTypeRegistry.EYES_FLIPPED) && entity.getData(DustAttachmentTypeRegistry.EYES_FLIPPED);
    }

    // ---- on a block ----

    public static void liftBlock(SpellCast cast, BlockPos pos, Direction face)
    {
        double lift = BLOCK_LIFT + BLOCK_LIFT_PER_POTENCY * Math.max(0, cast.stats().potency());
        RisingBlockEntity rising = RisingBlockEntity.lift(cast.level(), pos, lift);
        if (rising == null) return;
        cast.level().sendParticles(ParticleTypes.REVERSE_PORTAL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 16, 0.4, 0.4, 0.4, 0.05);
        cast.level().playSound(null, pos, SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.BLOCKS, 0.8F, 1.3F);
    }

    // ---- upside down ----

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event)
    {
        if (!(event.getEntity() instanceof LivingEntity living)) return;
        boolean inverted = inverted(living);
        // Turned over, or back: its eyes move (onSize)
        if (inverted != flipped(living)) {
            living.setData(DustAttachmentTypeRegistry.EYES_FLIPPED, inverted);
            living.refreshDimensions();
        }
        // Against a ceiling: that's its floor now
        if (inverted && living.verticalCollision && !living.verticalCollisionBelow) {
            living.setOnGround(true);
            living.resetFallDistance();
        }
    }

    // A jump pushes away from the ceiling it stands on
    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event)
    {
        LivingEntity living = event.getEntity();
        if (!inverted(living)) return;
        Vec3 motion = living.getDeltaMovement();
        living.setDeltaMovement(motion.x, -Math.abs(motion.y), motion.z);
    }

    // Upside down, its eyes are as far from the bottom of its box as they were from the top
    @SubscribeEvent
    public static void onSize(EntityEvent.Size event)
    {
        if (!flipped(event.getEntity())) return;
        EntityDimensions size = event.getNewSize();
        event.setNewSize(size.withEyeHeight(Math.max(0.05F, size.height() - size.eyeHeight())));
    }
}
