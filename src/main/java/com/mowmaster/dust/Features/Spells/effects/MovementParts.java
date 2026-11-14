package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.Tags;

// Step 7c's effect parts: flight by strength, spirit forms, lightning marks
public final class MovementParts
{
    // Flight on yourself: slow falling; from SOAR_POTENCY (the highest tier) it flies freely like creative. Elytra-like
    // gliding comes from Flight fused with Motion (glide, the Flight + Motion fusion: SpellRegistries.FUSION_FLIGHT_MOTION).
    public static final float SOAR_POTENCY = 5;

    private MovementParts() {}

    public static void flight(SpellCast cast, LivingEntity target)
    {
        if (cast.stats().potency() >= SOAR_POTENCY) EffectHelper.give(cast, target, DustEffectsRegistry.SOARING);
        EffectHelper.give(cast, target, MobEffects.SLOW_FALLING);
    }

    // Flight + Motion (a fusion) on yourself: glide like an elytra, without one (and slow falling, to land safely)
    public static void glide(SpellCast cast, LivingEntity target)
    {
        EffectHelper.give(cast, target, DustEffectsRegistry.GLIDING);
        EffectHelper.give(cast, target, MobEffects.SLOW_FALLING);
    }

    // Spirit on a mob: a ghost (it can't act or be hurt); bosses for half as long
    public static void ghost(SpellCast cast, Entity target)
    {
        if (!(target instanceof LivingEntity living)) return;
        int duration = living.is(Tags.EntityTypes.BOSSES) ? cast.stats().duration() / 2 : cast.stats().duration();
        living.addEffect(new MobEffectInstance(DustEffectsRegistry.GHOSTLY, duration, 0), cast.caster());
        living.addEffect(new MobEffectInstance(MobEffects.GLOWING, duration, 0), cast.caster());
    }

    // Lightning on a block: it draws lightning spells to it for a while (SpellProjectileEntity homes in)
    public static void conductiveBlock(SpellCast cast, BlockPos pos, Direction face)
    {
        BlockMarks.mark(cast.level(), pos, BlockMarks.CONDUCTIVE, cast.stats().duration() * 12);
        cast.level().sendParticles(ParticleTypes.ELECTRIC_SPARK, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 10, 0.3, 0.3, 0.3, 0.05);
    }
}
