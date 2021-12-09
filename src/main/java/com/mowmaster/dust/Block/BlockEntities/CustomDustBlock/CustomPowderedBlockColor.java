package com.mowmaster.dust.Block.BlockEntities.CustomDustBlock;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CustomPowderedBlockColor implements BlockColor {

    @Override
    public int getColor(BlockState blockState, BlockAndTintGetter blockDisplayReader, BlockPos blockPos,
                        int tintindex) {
        if (blockDisplayReader == null) {
            return -1;
        }
        BlockEntity tileEntity = blockDisplayReader.getBlockEntity(blockPos);
        if (tileEntity == null) {
            tileEntity = blockDisplayReader.getBlockEntity(blockPos.below());
            if (tileEntity == null) {
                return -1;
            }
        }
        if (tileEntity instanceof CustomPowderedBlockEntity) {
            return ((CustomPowderedBlockEntity) tileEntity).getColor();
        } else {
            return -1;
        }
    }
}
