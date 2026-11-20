package com.mowmaster.dust.Features.Apparatus.still;

import com.mowmaster.dust.Features.Apparatus.ApparatusBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.List;

// The Still Pot (models/block/apparatus/still_pot.json): the still's working block (StillPotBlockEntity), an apparatus
// like the others (intake on top, status with an empty hand, a redstone signal pauses it), but tubes don't join on: its
// dust goes up into the towers, and its soot into the containers beside it.
// A hopper pointing into it from a side or from above gets a brass port on the pot reaching out to meet its spout
// (still_pot_port.json / still_pot_port_up.json, drawn on that side by the multipart blockstate): one state property per
// side, set as hoppers come and go (updateShape).
public class StillPotBlock extends ApparatusBlock
{
    public static final BooleanProperty HOPPER_NORTH = PipeBlock.NORTH;
    public static final BooleanProperty HOPPER_EAST = PipeBlock.EAST;
    public static final BooleanProperty HOPPER_SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty HOPPER_WEST = PipeBlock.WEST;
    public static final BooleanProperty HOPPER_UP = PipeBlock.UP;
    // The sides a hopper can feed it from, in that order
    public static final List<Direction> FED_FROM = List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP);

    public StillPotBlock(Properties properties)
    {
        super(properties, Shapes.or(Block.box(0, 0, 0, 16, 16, 16)), StillPotBlockEntity::new);
        BlockState state = defaultBlockState();
        for (Direction side : FED_FROM) state = state.setValue(hopperProperty(side), false);
        registerDefaultState(state);
    }

    public static BooleanProperty hopperProperty(Direction side)
    {
        return PipeBlock.PROPERTY_BY_DIRECTION.get(side);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(HOPPER_NORTH, HOPPER_EAST, HOPPER_SOUTH, HOPPER_WEST, HOPPER_UP);
    }

    // Whether this neighbour is a hopper pointing into the pot (side: where it is, seen from the pot)
    public static boolean feeds(BlockState neighbor, Direction side)
    {
        return neighbor.getBlock() instanceof HopperBlock && neighbor.getValue(HopperBlock.FACING) == side.getOpposite();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = super.getStateForPlacement(context);
        for (Direction side : FED_FROM) {
            state = state.setValue(hopperProperty(side), feeds(context.getLevel().getBlockState(context.getClickedPos().relative(side)), side));
        }
        return state;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction,
                                     BlockPos neighborPos, BlockState neighborState, RandomSource random)
    {
        BlockState updated = super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
        return FED_FROM.contains(direction) ? updated.setValue(hopperProperty(direction), feeds(neighborState, direction)) : updated;
    }

    @Override
    public boolean connectsDustTube(BlockState state, Direction side)
    {
        return false;
    }
}
