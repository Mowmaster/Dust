package com.mowmaster.dust.Features.CrystalBlocks.Block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CrystalPathBaseBlock extends Block {
    private static final int blockSpeedMultiplier = 0;
    public CrystalPathBaseBlock(Properties properties, int speedMultiplier) {
        int blockSpeedMultiplier = speedMultiplier;
        super(properties);
    }

    /*@Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        //Rightclicking method, plays a sound
        level.playSound(player,pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 2f, 1f);

        return InteractionResult.SUCCESS;
    }*/

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
        //used when ANY ENTITY is on the block
        if(entity instanceof Player player && !player.hasEffect(MobEffects.SPEED))
        {
            player.addEffect(new MobEffectInstance(MobEffects.SPEED, 20, blockSpeedMultiplier));
            //player.setSpeed(blockSpeedMultiplier);
        }
        super.stepOn(level, pos, onState, entity);
    }
}
