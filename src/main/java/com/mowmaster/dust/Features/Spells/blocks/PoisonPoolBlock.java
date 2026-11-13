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

// Poison's block effect: a pool of poison. Whatever stands in it is Poisoned; it dries up after LIFETIME ticks.
public class PoisonPoolBlock extends SpellLayerBlock
{
    public static final int LIFETIME = 20 * 30;

    public PoisonPoolBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected void affect(ServerLevel level, BlockPos pos, LivingEntity entity)
    {
        if (!entity.hasEffect(MobEffects.POISON)) entity.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 5, 0));
    }

    // Placed: it starts drying up
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState old, boolean moved)
    {
        super.onPlace(state, level, pos, old, moved);
        if (!old.is(this)) level.scheduleTick(pos, this, LIFETIME);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        level.removeBlock(pos, false);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (random.nextInt(4) == 0) {
            level.addParticle(new net.minecraft.core.particles.DustParticleOptions(0x4E9A2E, 0.6F), pos.getX() + random.nextDouble(), pos.getY() + 0.1, pos.getZ() + random.nextDouble(), 0, 0.02, 0);
        }
    }
}
