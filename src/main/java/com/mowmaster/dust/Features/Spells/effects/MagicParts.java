package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.Magic.ChunkAura;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

// Magic's self and block parts (its entity part is magic damage, SimpleParts.magicDamage). Mana and aura are slow, so
// these last much longer than the spell's duration.
public final class MagicParts
{
    // Mana Surge: duration x 36 (3 minutes at the default). Amplified block: duration x 36 too.
    public static final int SURGE_TIME_SCALE = 36;
    public static final int AMPLIFY_TIME_SCALE = 36;

    private MagicParts() {}

    // Self: Mana Surge, mana regenerating twice as fast (only once awakened, like all regeneration)
    public static void surge(SpellCast cast, LivingEntity target)
    {
        target.addEffect(new MobEffectInstance(DustEffectsRegistry.MANA_SURGE, cast.stats().duration() * SURGE_TIME_SCALE, cast.stats().amplifier()), cast.caster());
    }

    // Block: aura effects at the block are 1 + potency times as strong (x2 at the default), good or bad
    public static void amplify(SpellCast cast, BlockPos pos, Direction face)
    {
        ChunkAura.amplify(cast.level(), pos, 1.0F + cast.stats().potency(), cast.stats().duration() * AMPLIFY_TIME_SCALE);
        cast.level().sendParticles(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 30, 0.4, 0.4, 0.4, 0.8);
        cast.level().sendParticles(ParticleTypes.WITCH, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8, 0.4, 0.4, 0.4, 0.0);
    }
}
