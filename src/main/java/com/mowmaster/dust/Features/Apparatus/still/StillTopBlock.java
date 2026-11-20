package com.mowmaster.dust.Features.Apparatus.still;

import com.mowmaster.dust.Features.Machines.StationBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import org.jspecify.annotations.Nullable;

// The Still Top (models/block/apparatus/still_top.json): the still's cap (StillLayout). It does nothing itself: the pot
// below checks it's there, and its smoke comes from the pot. (A StationBlock for its facing and shape; no block entity.)
public class StillTopBlock extends StationBlock
{
    public StillTopBlock(Properties properties)
    {
        super(properties, Shapes.or(Block.box(2, 0, 2, 14, 9.5, 14), Block.box(5, 9, 5, 11, 16, 11)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return null;
    }
}
