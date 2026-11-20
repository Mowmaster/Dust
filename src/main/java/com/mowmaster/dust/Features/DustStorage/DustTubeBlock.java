package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.Features.DustComposition.DustElement;
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
// One element to a network, so a tube placed between networks set to different elements (their jars: DustNetwork
// .networkElement), or beside a jar of another element, joins only one: the element of what it was placed against
// (else the first side that has one); the sides of other elements are SEALED. A sealed side stays apart while its
// neighbour stays; once either is broken, a tube placed there decides afresh.
// FILLED while dust runs through it: a see-through core of flowing dust shows inside the glass, tinted with the colour
// of what last ran through (Client/DustTubeTint). Set by the server as it sends a flow (DustNetwork: filled); it stays
// solid while dust keeps coming, and once it stops the tubes empty one by one from the far end back (a scheduled tick:
// FILL_HOLD, DRAIN_STEP).
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

    public static final net.minecraft.world.level.block.state.properties.BooleanProperty FILLED = net.minecraft.world.level.block.state.properties.BooleanProperty.create("filled");
    // How long a tube stays filled after dust last ran through (ticks), and how much sooner each tube nearer the far end
    // empties: once nothing runs, the dust drains out from the far end back towards where it came from
    private static final int FILL_HOLD = 100;
    private static final int DRAIN_STEP = 3;
    // When each tube empties (game time), by level (server; forgotten with the level)
    private static final Map<Level, Map<BlockPos, Long>> EMPTY_AT = new java.util.WeakHashMap<>();

    // Dust ran along this way (where it came from first): its tubes are FILLED, and stay so FILL_HOLD ticks, each one
    // nearer where it came from DRAIN_STEP longer, so the dust drains back from the far end
    public static void filled(ServerLevel level, java.util.List<BlockPos> path)
    {
        Map<BlockPos, Long> emptyAt = EMPTY_AT.computeIfAbsent(level, key -> new java.util.HashMap<>());
        long now = level.getGameTime();
        int count = path.size();
        for (int i = 0; i < count; i++) {
            BlockPos pos = path.get(i);
            if (!level.isLoaded(pos)) continue;
            BlockState state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof DustTubeBlock)) continue;
            long until = now + FILL_HOLD + (long) (count - 1 - i) * DRAIN_STEP;
            emptyAt.merge(pos.immutable(), until, Math::max);
            if (state.getValue(FILLED)) continue;
            // UPDATE_KNOWN_SHAPE: the neighbours needn't look again (only the look changed)
            level.setBlock(pos, state.setValue(FILLED, true), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
            level.scheduleTick(pos, state.getBlock(), (int) (emptyAt.get(pos) - now));
        }
    }

    // Empty again once its time is up (put off while dust keeps running through)
    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if (!state.getValue(FILLED)) return;
        Map<BlockPos, Long> emptyAt = EMPTY_AT.computeIfAbsent(level, key -> new java.util.HashMap<>());
        Long until = emptyAt.get(pos);
        long now = level.getGameTime();
        if (until != null && now < until) {
            level.scheduleTick(pos, this, (int) (until - now));
            return;
        }
        emptyAt.remove(pos);
        level.setBlock(pos, state.setValue(FILLED, false), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
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
        registerDefaultState(state.setValue(FILLED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        CONNECTIONS.values().forEach(builder::add);
        builder.add(FILLED);
    }

    // Placed: joined on to whatever round it takes tubes, but kept apart from sides of another element (the server
    // works that out; the client's guess is put right by it)
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = defaultBlockState();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Map<Direction, DustElement> elements = level instanceof ServerLevel server ? sideElements(server, pos) : Map.of();
        // The element it takes: what it was placed against, else the first side with one
        DustElement chosen = elements.get(context.getClickedFace().getOpposite());
        if (chosen == null) {
            for (Direction side : Direction.values()) {
                if (elements.containsKey(side)) {
                    chosen = elements.get(side);
                    break;
                }
            }
        }
        for (Direction side : Direction.values()) {
            TubeJoin join = join(level.getBlockState(pos.relative(side)), side.getOpposite());
            DustElement element = elements.get(side);
            if (join.joined() && element != null && !element.equals(chosen)) join = TubeJoin.SEALED;
            state = state.setValue(CONNECTIONS.get(side), join);
        }
        return state;
    }

    // The element on each side, where there is one: a jar set to one, or a tube whose network's jars are
    private static Map<Direction, DustElement> sideElements(ServerLevel level, BlockPos pos)
    {
        Map<Direction, DustElement> elements = new java.util.EnumMap<>(Direction.class);
        for (Direction side : Direction.values()) {
            BlockPos next = pos.relative(side);
            if (!level.isLoaded(next)) continue;
            BlockState neighbor = level.getBlockState(next);
            DustElement element = null;
            if (neighbor.getBlock() instanceof DustTubeBlock) {
                DustNetwork.Holds holds = DustNetwork.networkElement(level, next);
                if (!holds.mixed()) element = holds.element();
            } else if (joins(neighbor, side.getOpposite()) && level.getBlockEntity(next) instanceof DustJarBlockEntity jar) {
                element = jar.element();
            }
            if (element != null) elements.put(side, element);
        }
        return elements;
    }

    // A neighbour changed: join on to it or let go of it. A sealed side stays sealed while the neighbour is still
    // something it would join; a new tube beside it is matched (sealed if that tube sealed itself off from it).
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction,
                                     BlockPos neighborPos, BlockState neighborState, RandomSource random)
    {
        EnumProperty<TubeJoin> property = CONNECTIONS.get(direction);
        TubeJoin join = join(neighborState, direction.getOpposite());
        boolean sealed = state.getValue(property) == TubeJoin.SEALED
                || (neighborState.getBlock() instanceof DustTubeBlock && neighborState.getValue(CONNECTIONS.get(direction.getOpposite())) == TubeJoin.SEALED);
        if (join.joined() && sealed) join = TubeJoin.SEALED;
        return state.setValue(property, join);
    }

    // How a tube joins on to this block: to another tube, to a block that takes tubes, or not at all
    public static TubeJoin join(BlockState neighbor, Direction side)
    {
        if (neighbor.getBlock() instanceof DustTubeBlock) return TubeJoin.TUBE;
        return joins(neighbor, side) ? ((DustTubeConnectable) neighbor.getBlock()).tubeJoin(neighbor, side) : TubeJoin.NONE;
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
        // Only a change in how it's joined (not filling or emptying) changes the networks
        if (!oldState.is(this) || !CONNECTIONS.values().stream().allMatch(property -> oldState.getValue(property) == state.getValue(property))) {
            DustNetwork.changed(level);
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston)
    {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        DustNetwork.changed(level);
    }
}
