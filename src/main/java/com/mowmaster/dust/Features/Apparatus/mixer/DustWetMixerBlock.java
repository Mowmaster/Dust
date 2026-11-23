package com.mowmaster.dust.Features.Apparatus.mixer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

// The Wet Dust Mixer (models/block/apparatus/dust_mixer_wet.json; the block draws its stand, dust_mixer_wet_base.json,
// and Client/DustWetMixerRenderer the pot, the tilting head and its beater): mixes an element with a gaseous or aqueous
// one into the element made of them (DustWetMixerBlockEntity), like a stand mixer. FACING: its front (the pot's open
// side, away from the stand), toward the player who placed it; dust is piped in at its back (the stand: the blue
// crystal) and underneath; what it makes is tipped out in front.
public class DustWetMixerBlock extends DustMixerBlock
{
    // The stand's base and column, and the pot (facing north: the column at the back, south)
    private static final VoxelShape NORTH_SHAPE = Shapes.or(Block.box(0, 0, 0, 16, 1, 16), Block.box(2, 1, 0, 14, 12, 12), Block.box(2, 1, 12.5, 14, 16, 16));
    private static final Map<Direction, VoxelShape> SHAPES = com.mowmaster.dust.Features.Apparatus.ApparatusShapes.turned(NORTH_SHAPE);

    public DustWetMixerBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DustWetMixerBlockEntity(pos, state);
    }

    // Tubes at its back and underneath only (the inputs)
    @Override
    public boolean connectsDustTube(BlockState state, Direction side)
    {
        return side == Direction.DOWN || side == state.getValue(FACING).getOpposite();
    }
}
