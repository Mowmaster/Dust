package com.mowmaster.dust.Features.Apparatus.cyclone;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

// The Dust Cyclone Hopper (models/block/apparatus/dust_cyclone_hopper.json): goes under a Dust Cyclone. The heavier
// part of what the cyclone spins apart spirals down it and drops out of its spout as a Dust Pile under it (the cyclone
// does the work: DustCycloneBlockEntity). The same every way round.
public class DustCycloneHopperBlock extends Block
{
    private static final VoxelShape SHAPE = Shapes.or(Block.box(1.5, 9, 1.5, 14.5, 16, 14.5), Block.box(3, 4, 3, 13, 9, 13), Block.box(5, 0, 5, 11, 5, 11));

    public DustCycloneHopperBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }
}
