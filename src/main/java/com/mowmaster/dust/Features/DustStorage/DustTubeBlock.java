package com.mowmaster.dust.Features.DustStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.BlockHitResult;
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
// One element to a tube network: a tube won't join on to a tube network, jar or Still Tower set to another element than
// its own network (it's cut off from it: SEALED). Machines join any (each of a machine's tube networks takes only its
// own element from it: DustNetwork.insert).
// Redstone on the end of a tube joined to a jar (right-click that end with redstone: TubeJoin.PULL, a red band on its
// collar) makes it pull from that jar: every PULL_TICKS it draws dust out and along its network to the other stores
// (other jars, a Dust Mixer...; never back into that jar, never spilled). Each end can pull from its own jar. Sneak +
// right-click an end with an empty hand takes its redstone off.
// Shears on a side (the arm clicked, or the face of the middle): a joined side is cut (SEALED, and the tube on the other
// side with it), a cut side is joined again (unless it's set to another element). A cut side stays cut while its neighbour stays (it lets go once that's
// gone). Honeycomb waxes a tube (WAXED): it keeps how it's joined on every side, joining nothing new, not even tubes
// placed beside it (they're cut off from it), until an axe scrapes the wax off. Only a side gone missing lets go.
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
    public static final net.minecraft.world.level.block.state.properties.BooleanProperty WAXED = net.minecraft.world.level.block.state.properties.BooleanProperty.create("waxed");
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
            // A pulling tube already ticks every PULL_TICKS and empties itself then (a tube has one scheduled tick at a
            // time: a later one asked for would be refused, and its pulling would stall until this one came round)
            if (!pulls(state)) level.scheduleTick(pos, state.getBlock(), (int) (emptyAt.get(pos) - now));
        }
    }

    // A scheduled tick (one at a time per tube, so it does both jobs and asks again for whichever is due first):
    //   pulling  a tube with redstone on it draws dust out of the jars it's joined to, every PULL_TICKS
    //   filled   it empties again once its time is up (put off while dust keeps running through)
    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        long next = Long.MAX_VALUE;
        if (pulls(state)) {
            pull(level, pos, state);
            next = PULL_TICKS;
        }
        state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof DustTubeBlock)) return;
        if (state.getValue(FILLED)) {
            Map<BlockPos, Long> emptyAt = EMPTY_AT.computeIfAbsent(level, key -> new java.util.HashMap<>());
            Long until = emptyAt.get(pos);
            long now = level.getGameTime();
            if (until != null && now < until) {
                next = Math.min(next, until - now);
            } else {
                emptyAt.remove(pos);
                level.setBlock(pos, state.setValue(FILLED, false), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
            }
        }
        if (next != Long.MAX_VALUE) level.scheduleTick(pos, this, (int) Math.max(1, next));
    }

    // ---- pulling (redstone on it) ----

    private static final int PULL_TICKS = 10;
    private static final int PULL_AMOUNT = 20;

    // Whether any of its ends pulls
    public static boolean pulls(BlockState state)
    {
        for (Direction side : Direction.values()) if (state.getValue(CONNECTIONS.get(side)) == TubeJoin.PULL) return true;
        return false;
    }

    // Out of the jar at each pulling end and along its network to the other stores (never back into that jar, never
    // spilled)
    private static void pull(ServerLevel level, BlockPos pos, BlockState state)
    {
        for (Direction side : Direction.values()) {
            if (state.getValue(CONNECTIONS.get(side)) != TubeJoin.PULL) continue;
            BlockPos jarPos = pos.relative(side);
            if (!level.isLoaded(jarPos) || !(level.getBlockEntity(jarPos) instanceof DustJarBlockEntity jar)) continue;
            com.mowmaster.dust.Features.DustComposition.DustElement element = jar.element();
            if (element == null || jar.amount() <= 0) continue;
            int moved = DustNetwork.insert(level, pos, element, Math.min(PULL_AMOUNT, jar.amount()), jarPos);
            if (moved > 0) jar.extractDust(element, moved, false);
        }
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
        registerDefaultState(state.setValue(FILLED, false).setValue(WAXED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        CONNECTIONS.values().forEach(builder::add);
        builder.add(FILLED, WAXED);
    }

    // Placed: joined on to whatever round it takes tubes (cut off from a waxed tube not joined to this side). One element
    // to a tube network: it takes the element of what it was placed against (else of the first side that has one), and
    // is cut off from sides of another (a tube network whose jars are set to it, a jar, a Still Tower). The server works
    // that out; the client's guess is put right by it.
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = defaultBlockState();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Map<Direction, DustNetwork.Holds> elements = new java.util.EnumMap<>(Direction.class);
        for (Direction side : Direction.values()) {
            TubeJoin join = sideJoin(level.getBlockState(pos.relative(side)), side, TubeJoin.NONE);
            state = state.setValue(CONNECTIONS.get(side), join);
            if (join.joined() && level instanceof ServerLevel) elements.put(side, sideElement(level, pos, side));
        }
        DustNetwork.Holds chosen = elements.get(context.getClickedFace().getOpposite());
        if (chosen == null || chosen.element() == null) {
            for (Direction side : Direction.values()) {
                DustNetwork.Holds holds = elements.get(side);
                if (holds != null && holds.element() != null && !holds.mixed()) {
                    chosen = holds;
                    break;
                }
            }
        }
        for (Map.Entry<Direction, DustNetwork.Holds> entry : elements.entrySet()) {
            if (chosen != null && clash(chosen, entry.getValue())) state = state.setValue(CONNECTIONS.get(entry.getKey()), TubeJoin.SEALED);
        }
        return state;
    }

    // The element on one side of a tube at pos: the tube network there (not counting pos), or the block's own element
    private static DustNetwork.Holds sideElement(LevelReader level, BlockPos pos, Direction side)
    {
        BlockPos next = pos.relative(side);
        BlockState neighbor = level.getBlockState(next);
        if (neighbor.getBlock() instanceof DustTubeBlock) return DustNetwork.elementFrom(level, next, side.getOpposite());
        if (neighbor.getBlock() instanceof DustTubeConnectable connectable) return new DustNetwork.Holds(connectable.tubeElement(level, next), false);
        return new DustNetwork.Holds(null, false);
    }

    // A tube at pos looks again at what's joined on its side (whose element has just become known): cut off from it if
    // its network is set to another element (not a waxed tube)
    public static void recheck(Level level, BlockPos pos, Direction side)
    {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof DustTubeBlock) || state.getValue(WAXED) || !isJoined(state, side)) return;
        if (clash(DustNetwork.elementFrom(level, pos, side), sideElement(level, pos, side))) {
            level.setBlock(pos, state.setValue(CONNECTIONS.get(side), TubeJoin.SEALED), Block.UPDATE_ALL);
        }
    }

    // Whether two tube networks (or a network and a block) mustn't join: set to different elements, or either mixed
    private static boolean clash(DustNetwork.Holds a, DustNetwork.Holds b)
    {
        if (a.mixed() || b.mixed()) return true;
        return a.element() != null && b.element() != null && !a.element().equals(b.element());
    }

    // A neighbour changed: join on to it or let go of it (a waxed tube only lets go of a side whose neighbour is gone)
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction,
                                     BlockPos neighborPos, BlockState neighborState, RandomSource random)
    {
        EnumProperty<TubeJoin> property = CONNECTIONS.get(direction);
        TubeJoin current = state.getValue(property);
        if (state.getValue(WAXED)) return join(neighborState, direction.getOpposite()).joined() ? state : state.setValue(property, TubeJoin.NONE);
        // A pulling end stays pulling while there's still a jar there
        if (current == TubeJoin.PULL && join(neighborState, direction.getOpposite()) == TubeJoin.BLOCK && neighborState.getBlock() instanceof DustJarBlock) return state;
        // The jar's gone: the redstone drops
        if (current == TubeJoin.PULL && level instanceof ServerLevel server) Block.popResource(server, pos, new ItemStack(Items.REDSTONE));
        TubeJoin join = sideJoin(neighborState, direction, current);
        // Something new to join on to: cut off from it if it's set to another element than this tube's network
        if (current == TubeJoin.NONE && join.joined() && level instanceof ServerLevel
                && clash(DustNetwork.elementFrom(level, pos, direction), sideElement(level, pos, direction))) join = TubeJoin.SEALED;
        return state.setValue(property, join);
    }

    // How it's joined on one side (side: from it towards the neighbour): as join says, but cut (SEALED) if it was cut,
    // or if the neighbour is a tube cut off from it (cut, or waxed and not joined to it)
    private static TubeJoin sideJoin(BlockState neighbor, Direction side, TubeJoin current)
    {
        TubeJoin join = join(neighbor, side.getOpposite());
        if (!join.joined()) return TubeJoin.NONE;
        if (current == TubeJoin.SEALED) return TubeJoin.SEALED;
        if (neighbor.getBlock() instanceof DustTubeBlock) {
            TubeJoin facing = neighbor.getValue(CONNECTIONS.get(side.getOpposite()));
            if (facing == TubeJoin.SEALED || (neighbor.getValue(WAXED) && !facing.joined())) return TubeJoin.SEALED;
        }
        return join;
    }

    // ---- shears, wax ----

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        // Redstone on an end joined to a jar: that end pulls out of the jar
        if (stack.is(Items.REDSTONE)) {
            Direction side = clickedSide(pos, hit);
            EnumProperty<TubeJoin> property = CONNECTIONS.get(side);
            if (state.getValue(property) != TubeJoin.BLOCK || !(level.getBlockEntity(pos.relative(side)) instanceof DustJarBlockEntity)) return InteractionResult.PASS;
            if (level.isClientSide()) return InteractionResult.SUCCESS;
            level.setBlock(pos, state.setValue(property, TubeJoin.PULL), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
            level.scheduleTick(pos, this, PULL_TICKS);
            stack.consume(1, player);
            level.playSound(null, pos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 0.6F, 1.4F);
            return InteractionResult.SUCCESS;
        }
        boolean waxed = state.getValue(WAXED);
        if (stack.is(Items.HONEYCOMB)) {
            if (waxed) return InteractionResult.PASS;
            if (level.isClientSide()) return InteractionResult.SUCCESS;
            level.setBlock(pos, state.setValue(WAXED, true), Block.UPDATE_ALL);
            //? if >=26.3 {
            level.levelEvent(null, LevelEvent.PARTICLES_WAX_ON, pos, 0);
            //?} else {
            /*level.levelEvent(null, LevelEvent.PARTICLES_AND_SOUND_WAX_ON, pos, 0);
            *///?}
            level.playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0F, 1.0F);
            stack.consume(1, player);
            return InteractionResult.SUCCESS;
        }
        if (stack.is(ItemTags.AXES)) {
            if (!waxed) return InteractionResult.PASS;
            if (level.isClientSide()) return InteractionResult.SUCCESS;
            level.setBlock(pos, state.setValue(WAXED, false), Block.UPDATE_ALL);
            level.levelEvent(null, LevelEvent.PARTICLES_WAX_OFF, pos, 0);
            level.playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0F, 0.6F);
            stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            return InteractionResult.SUCCESS;
        }
        if (!stack.is(Items.SHEARS) && !stack.is(net.neoforged.neoforge.common.Tags.Items.TOOLS_SHEAR)) return InteractionResult.PASS;
        Direction side = clickedSide(pos, hit);
        TubeJoin current = state.getValue(CONNECTIONS.get(side));
        if (current == TubeJoin.NONE) return InteractionResult.PASS;
        if (waxed) {
            if (!level.isClientSide()) player.sendOverlayMessage(Component.translatable("dust.tube.waxed"));
            return InteractionResult.FAIL;
        }
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        BlockPos nextPos = pos.relative(side);
        BlockState next = level.getBlockState(nextPos);
        boolean nextTube = next.getBlock() instanceof DustTubeBlock;
        EnumProperty<TubeJoin> back = CONNECTIONS.get(side.getOpposite());
        if (current == TubeJoin.SEALED) {
            // Joined again (not to a waxed tube that isn't joined to it, nor to something set to another element)
            TubeJoin join = join(next, side.getOpposite());
            if (!join.joined()) return InteractionResult.FAIL;
            if (clash(DustNetwork.elementFrom(level, pos, side), sideElement(level, pos, side))) {
                player.sendOverlayMessage(Component.translatable("dust.tube.other_element"));
                return InteractionResult.FAIL;
            }
            if (nextTube) {
                if (next.getValue(WAXED) && !next.getValue(back).joined()) {
                    player.sendOverlayMessage(Component.translatable("dust.tube.waxed"));
                    return InteractionResult.FAIL;
                }
                level.setBlock(nextPos, next.setValue(back, TubeJoin.TUBE), Block.UPDATE_ALL);
            }
            level.setBlock(pos, level.getBlockState(pos).setValue(CONNECTIONS.get(side), join), Block.UPDATE_ALL);
        } else {
            // Cut, the tube beside with it (unless it's waxed: then it keeps its arm, but nothing passes); a pulling end's
            // redstone drops
            if (current == TubeJoin.PULL) Block.popResource(level, pos, new ItemStack(Items.REDSTONE));
            level.setBlock(pos, state.setValue(CONNECTIONS.get(side), TubeJoin.SEALED), Block.UPDATE_ALL);
            if (nextTube && !next.getValue(WAXED)) {
                BlockState nextNow = level.getBlockState(nextPos);
                if (nextNow.getBlock() instanceof DustTubeBlock) level.setBlock(nextPos, nextNow.setValue(back, TubeJoin.SEALED), Block.UPDATE_ALL);
            }
        }
        level.playSound(null, pos, SoundEvents.SHEARS_SNIP, SoundSource.BLOCKS, 1.0F, 1.0F);
        stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        return InteractionResult.SUCCESS;
    }

    // Which side was clicked: the arm the hit is on (more than the middle's half-width out from the centre), else the
    // face of the middle that was hit
    private static Direction clickedSide(BlockPos pos, BlockHitResult hit)
    {
        double x = hit.getLocation().x - pos.getX() - 0.5, y = hit.getLocation().y - pos.getY() - 0.5, z = hit.getLocation().z - pos.getZ() - 0.5;
        double ax = Math.abs(x), ay = Math.abs(y), az = Math.abs(z), core = 3.0 / 16 + 1.0E-3;
        if (Math.max(ax, Math.max(ay, az)) <= core) return hit.getDirection();
        if (ax >= ay && ax >= az) return x > 0 ? Direction.EAST : Direction.WEST;
        if (ay >= az) return y > 0 ? Direction.UP : Direction.DOWN;
        return z > 0 ? Direction.SOUTH : Direction.NORTH;
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
        // Its redstone comes back
        for (Direction side : Direction.values()) {
            if (state.getValue(CONNECTIONS.get(side)) == TubeJoin.PULL) Block.popResource(level, pos, new ItemStack(Items.REDSTONE));
        }
    }

    // Sneak + right-click an end with an empty hand: its redstone comes off (that end stops pulling)
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        EnumProperty<TubeJoin> property = CONNECTIONS.get(clickedSide(pos, hit));
        if (!player.isShiftKeyDown() || state.getValue(property) != TubeJoin.PULL) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        level.setBlock(pos, state.setValue(property, TubeJoin.BLOCK), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
        if (!player.getInventory().add(new ItemStack(Items.REDSTONE))) Block.popResource(level, pos, new ItemStack(Items.REDSTONE));
        level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 0.6F, 1.4F);
        return InteractionResult.SUCCESS;
    }
}
