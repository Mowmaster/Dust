package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.Features.Spells.api.ElementEffect;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

// Water
//   Self     Water Breathing
//   Block    waterlogs the block if it can be, otherwise places a water source against the face hit (where water
//            evaporates, like the Nether, it just hisses)
//   Entity   drowning damage
public class WaterEffect implements ElementEffect
{
    @Override
    public void onSelf(SpellCast cast, LivingEntity target)
    {
        EffectHelper.give(cast, target, MobEffects.WATER_BREATHING);
    }

    @Override
    public void onBlock(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        BlockState state = level.getBlockState(pos);
        BlockPos target = pos;
        boolean waterlog = state.getBlock() instanceof SimpleWaterloggedBlock && state.hasProperty(BlockStateProperties.WATERLOGGED)
                && !state.getValue(BlockStateProperties.WATERLOGGED);
        if (!waterlog) {
            target = pos.relative(face);
            if (!level.getBlockState(target).canBeReplaced() || level.getFluidState(target).isSource()) return;
        }
        // environmentAttributes: 26.x's per-place world rules (the Nether's "water evaporates" is one)
        if (level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, target)) {
            level.playSound(null, target, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F);
            level.sendParticles(ParticleTypes.LARGE_SMOKE, target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5, 8, 0.3, 0.2, 0.3, 0.01);
            return;
        }
        if (waterlog) ((SimpleWaterloggedBlock) state.getBlock()).placeLiquid(level, target, state, Fluids.WATER.getSource(false));
        else level.setBlockAndUpdate(target, Blocks.WATER.defaultBlockState());
    }

    @Override
    public void onEntity(SpellCast cast, Entity target)
    {
        target.hurtServer(cast.level(), cast.level().damageSources().drown(), cast.stats().damage());
    }
}
