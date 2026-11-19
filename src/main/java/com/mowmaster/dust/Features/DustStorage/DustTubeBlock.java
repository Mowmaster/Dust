package com.mowmaster.dust.Features.DustStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

// The Glass Dust Tube: a glass vacuum tube in brass collars that carries element dust (DustNetwork). It joins on to the
// tubes beside it and to the blocks that take tubes (DustTubeConnectable: Dust Jars, the Dust Grinder, the alchemical
// apparatus, Spell Casters), a block state property for each side (TubeJoin: none, a tube, or a block; the model shows
// an arm on each joined side, a longer one reaching into a block's space: a multipart blockstate). Tubes have no block
// entity: the network is found by walking them.
public class DustTubeBlock extends Block implements DustTubeConnectable
{
    // How it's joined on each side (named like vanilla's six sides: "north", ...)
    public static final Map<Direction, EnumProperty<TubeJoin>> CONNECTIONS = makeConnections();

    private static Map<Direction, EnumProperty<TubeJoin>> makeConnections()
    {
        Map<Direction, EnumProperty<TubeJoin>> map = new java.util.EnumMap<>(Direction.class);
        for (Direction side : Direction.values()) map.put(side, EnumProperty.create(side.getSerializedName(), TubeJoin.class));
        return java.util.Collections.unmodifiableMap(map);
    }

    // Whether it's joined (to anything) on this side
    public static boolean isJoined(BlockState state, Direction side)
    {
        return state.getValue(CONNECTIONS.get(side)).joined();
    }

    // The middle, and an arm out to each side (the glass is 5 wide; the collars at the ends 6)
    private static final VoxelShape CORE = Block.box(5, 5, 5, 11, 11, 11);
    private static final Map<Direction, VoxelShape> ARMS = Map.of(
            Direction.NORTH, Block.box(5, 5, 0, 11, 11, 5),
            Direction.SOUTH, Block.box(5, 5, 11, 11, 11, 16),
            Direction.WEST, Block.box(0, 5, 5, 5, 11, 11),
            Direction.EAST, Block.box(11, 5, 5, 16, 11, 11),
            Direction.DOWN, Block.box(5, 0, 5, 11, 5, 11),
            Direction.UP, Block.box(5, 11, 5, 11, 16, 11));
    // Every combination of arms, worked out once: index bit i is Direction.values()[i] joined
    private static final VoxelShape[] SHAPES = new VoxelShape[64];

    static {
        for (int mask = 0; mask < 64; mask++) {
            VoxelShape shape = CORE;
            for (Direction side : Direction.values()) {
                if ((mask & (1 << side.ordinal())) != 0) shape = Shapes.or(shape, ARMS.get(side));
            }
            SHAPES[mask] = shape;
        }
    }

    public DustTubeBlock(Properties properties)
    {
        super(properties);
        BlockState state = stateDefinition.any();
        for (EnumProperty<TubeJoin> property : CONNECTIONS.values()) state = state.setValue(property, TubeJoin.NONE);
        registerDefaultState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        CONNECTIONS.values().forEach(builder::add);
    }

    // Placed: joined on to whatever round it takes tubes
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = defaultBlockState();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        for (Direction side : Direction.values()) {
            BlockPos next = pos.relative(side);
            state = state.setValue(CONNECTIONS.get(side), join(level.getBlockState(next), side.getOpposite()));
        }
        return state;
    }

    // A neighbour changed: join on to it or let go of it
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction,
                                     BlockPos neighborPos, BlockState neighborState, RandomSource random)
    {
        return state.setValue(CONNECTIONS.get(direction), join(neighborState, direction.getOpposite()));
    }

    // How a tube joins on to this block: to another tube, to a block that takes tubes, or not at all
    public static TubeJoin join(BlockState neighbor, Direction side)
    {
        if (neighbor.getBlock() instanceof DustTubeBlock) return TubeJoin.TUBE;
        return joins(neighbor, side) ? TubeJoin.BLOCK : TubeJoin.NONE;
    }

    // Whether a tube joins on to this block (side: the block's side facing the tube)
    public static boolean joins(BlockState neighbor, Direction side)
    {
        return neighbor.getBlock() instanceof DustTubeConnectable connectable && connectable.connectsDustTube(neighbor, side);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        int mask = 0;
        for (Direction side : Direction.values()) {
            if (isJoined(state, side)) mask |= 1 << side.ordinal();
        }
        return SHAPES[mask];
    }

    // Glass: light passes, and mobs don't try to path through it
    @Override
    protected boolean propagatesSkylightDown(BlockState state)
    {
        return true;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type)
    {
        return false;
    }

    // Any change to the tubes (placed, joined, let go, broken) means networks have to be found again
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
    {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!oldState.equals(state)) DustNetwork.changed(level);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston)
    {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        DustNetwork.changed(level);
    }
}
