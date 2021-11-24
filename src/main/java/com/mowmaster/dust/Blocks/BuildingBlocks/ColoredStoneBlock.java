package com.mowmaster.dust.Blocks.BuildingBlocks;

import com.mowmaster.dust.Blocks.BaseBlocks.BaseColoredBlock;
import com.mowmaster.dust.Items.ColorApplicator;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ColoredStoneBlock extends BaseColoredBlock
{
    public ColoredStoneBlock(BlockBehaviour.Properties p_152915_)
    {
        super(p_152915_);
        this.registerDefaultState(ColorReference.addColorToBlockState(this.defaultBlockState(),ColorReference.DEFAULTCOLOR));
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {

        if(p_60506_.getItemInHand(p_60507_).getItem() instanceof ColorApplicator)
        {
            int getColor = ColorReference.getColorFromItemStackInt(p_60506_.getItemInHand(p_60507_));
            BlockState newState = ColorReference.addColorToBlockState(p_60503_,getColor);
            p_60504_.setBlock(p_60505_,newState,3);
            //p_60504_.markAndNotifyBlock(p_60505_,null,p_60503_,newState,3,1);
            return InteractionResult.SUCCESS;
        }

        /*if(p_60506_.getItemInHand(p_60507_).getItem() instanceof ItemChisel)
        {
            if(p_60503_.getBlock() instanceof CrystalBlock)
            {
                p_60504_.setBlock(p_60505_, BlockReference.getBlock(p_60503_,false),3);
                return InteractionResult.SUCCESS;
            }
        }*/

        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }
}
