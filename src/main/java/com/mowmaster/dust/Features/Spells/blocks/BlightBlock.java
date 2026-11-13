package com.mowmaster.dust.Features.Spells.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

// Flesh's block effect: blight. Whatever walks on it gets Hunger and Nausea, and it creeps onto the ground around it
// (random ticks) until it's purified (a Healing spell) or built over.
public class BlightBlock extends SpellLayerBlock
{
    public static final float SPREAD_CHANCE = 0.25F;

    public BlightBlock(Properties properties)
    {
        super(properties.randomTicks());
    }

    @Override
    protected void affect(ServerLevel level, BlockPos pos, LivingEntity entity)
    {
        if (!entity.hasEffect(MobEffects.HUNGER)) entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20 * 5, 0));
        if (!entity.hasEffect(MobEffects.NAUSEA)) entity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 20 * 5, 0));
    }

    // Creeps to a random spot next to it (up or down a block too), if there's open ground there
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if (random.nextFloat() >= SPREAD_CHANCE) return;
        BlockPos next = pos.offset(random.nextInt(3) - 1, random.nextInt(3) - 1, random.nextInt(3) - 1);
        if (canLieAt(level, next)) level.setBlockAndUpdate(next, defaultBlockState());
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (random.nextInt(6) == 0) {
            level.addParticle(ParticleTypes.CRIMSON_SPORE, pos.getX() + random.nextDouble(), pos.getY() + 0.2, pos.getZ() + random.nextDouble(), 0, 0, 0);
        }
    }
}
