package com.mowmaster.dust.Blocks.GeneratedBlocks;

import com.mowmaster.dust.Blocks.BaseBlocks.BaseColoredBlock;
import com.mowmaster.dust.Blocks.BaseBlocks.BaseColoredCrystalBlock;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;

import java.util.Random;

public class BaseCrystalNodeBlock extends BaseColoredCrystalBlock
{
    public static final int GROWTH_CHANCE = 5;
    private static final Direction[] DIRECTIONS = Direction.values();

    public BaseCrystalNodeBlock(BlockBehaviour.Properties p_152726_) {
        super(p_152726_);
    }

    public PushReaction getPistonPushReaction(BlockState p_152733_) {
        return PushReaction.DESTROY;
    }

    public void randomTick(BlockState p_152728_, ServerLevel p_152729_, BlockPos p_152730_, Random p_152731_) {
        if (p_152731_.nextInt(5) == 0) {
            Direction direction = DIRECTIONS[p_152731_.nextInt(DIRECTIONS.length)];
            BlockState blockstateOrigin = p_152729_.getBlockState(p_152730_);
            BlockPos blockpos = p_152730_.relative(direction);
            BlockState blockstate = p_152729_.getBlockState(blockpos);
            int getColor = ColorReference.DEFAULTCOLOR;
            Block block = null;

            if (canClusterGrowAtState(blockstate)) {
                blockstate = p_152729_.getBlockState(p_152730_);
                getColor = ColorReference.getColorFromStateInt(blockstate);
                block = DeferredRegisterBlocks.CRYSTAL_CLUSTER_SMALL.get();
            } else if (blockstate.is(DeferredRegisterBlocks.CRYSTAL_CLUSTER_SMALL.get()) && blockstate.getValue(BaseCrystalClusterBlock.FACING) == direction) {
                getColor = ColorReference.getColorFromStateInt(blockstate);
                block = DeferredRegisterBlocks.CRYSTAL_CLUSTER_MEDIUM.get();
            } else if (blockstate.is(DeferredRegisterBlocks.CRYSTAL_CLUSTER_MEDIUM.get()) && blockstate.getValue(BaseCrystalClusterBlock.FACING) == direction) {
                getColor = ColorReference.getColorFromStateInt(blockstate);
                block = DeferredRegisterBlocks.CRYSTAL_CLUSTER_LARGE.get();
            } else if (blockstate.is(DeferredRegisterBlocks.CRYSTAL_CLUSTER_LARGE.get()) && blockstate.getValue(BaseCrystalClusterBlock.FACING) == direction) {
                getColor = ColorReference.getColorFromStateInt(blockstate);
                block = DeferredRegisterBlocks.CRYSTAL_CLUSTER_FULL.get();
            }
            //Make Colored Stone
            else if (blockstate.is(Blocks.STONE)) {
                blockstate = p_152729_.getBlockState(p_152730_);
                getColor = ColorReference.getColorFromStateInt(blockstate);
                block = DeferredRegisterBlocks.CRYSTAL_STONE.get();
            }
            else if (blockstate.is(DeferredRegisterBlocks.CRYSTAL_STONE.get())) {
                blockstate = p_152729_.getBlockState(p_152730_);
                getColor = ColorReference.getColorFromStateInt(blockstate);
                block = DeferredRegisterBlocks.CRYSTAL_STONE.get();
                int numberX = p_152731_.nextInt(1 - -1) + -1;
                int numberY = p_152731_.nextInt(1 - -1) + -1;
                int numberZ = p_152731_.nextInt(1 - -1) + -1;
                blockpos = p_152730_.relative(direction).offset(numberX,numberY,numberZ);
            }

            if (block != null) {
                if(block instanceof BaseCrystalClusterBlock)
                {
                    BlockState blockstate1 = ColorReference.addColorToBlockState(block.defaultBlockState(),getColor).setValue(BaseCrystalClusterBlock.FACING, direction).setValue(BaseCrystalClusterBlock.WATERLOGGED, Boolean.valueOf(blockstate.getFluidState().getType() == Fluids.WATER));
                    p_152729_.setBlockAndUpdate(blockpos, blockstate1);
                }
                else if(block instanceof BaseColoredBlock)
                {
                    if(p_152729_.getBlockState(blockpos).getBlock().equals(Blocks.STONE))
                    {
                        BlockState blockstate1 = ColorReference.addColorToBlockState(block.defaultBlockState(),getColor);
                        p_152729_.setBlockAndUpdate(blockpos, blockstate1);
                    }
                    else if(p_152729_.getBlockState(blockpos).getBlock().equals(DeferredRegisterBlocks.CRYSTAL_STONE.get()))
                    {
                        int colorOrigin = ColorReference.getColorFromStateInt(blockstateOrigin);
                        int colorToMix = ColorReference.getColorFromStateInt(p_152729_.getBlockState(blockpos));
                        if(colorOrigin != colorToMix)
                        {
                            int colorMix = ColorReference.mixColorsInt(colorOrigin,colorToMix);
                            BlockState blockstate1 = ColorReference.addColorToBlockState(block.defaultBlockState(),colorMix);
                            p_152729_.setBlockAndUpdate(blockpos, blockstate1);
                        }
                    }
                }
            }

        }
    }

    public static boolean canClusterGrowAtState(BlockState p_152735_) {
        return p_152735_.isAir() || p_152735_.is(Blocks.WATER) && p_152735_.getFluidState().getAmount() == 8;
    }
}
