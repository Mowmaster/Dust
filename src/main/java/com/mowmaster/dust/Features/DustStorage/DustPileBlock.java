package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
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
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

// A Dust Pile: element dust spilled out of the open end of a Glass Dust Tube with no jar on it (DustNetwork). Shaped
// like snow layers (LAYERS: a full block at DustPileBlockEntity.CAPACITY dust), but walked through, and tinted with
// the mix of the elements in it (Client/DustPileTint). It holds any elements together (DustPileBlockEntity).
// It falls like sand when what's under it goes (Fallable: a falling block carrying its dust; where it can't land as a
// pile, its dust is poured out there instead: DustClouds.deposit). Broken with a shovel, it gives back what it held as element orbs (they fly to the player and are absorbed into their
// element stores, as the orbs mobs drop are: ElementOrbEntity).
// Out in the open (nothing solid on top), it makes the air round it dusty (DustClouds): a cloud thick enough catches
// from a flame and blows up. Nothing drops when it's broken (but see the shovel above).
public class DustPileBlock extends Block implements EntityBlock, net.minecraft.world.level.block.Fallable
{
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
    private static final VoxelShape[] SHAPES = new VoxelShape[9];

    static {
        SHAPES[0] = Shapes.empty();
        for (int layers = 1; layers <= 8; layers++) SHAPES[layers] = Block.box(0, 0, 0, 16, layers * 2, 16);
    }

    public DustPileBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(LAYERS, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(LAYERS);
    }

    // The outline and what it holds up are the pile's height; nothing collides with it (it's walked through)
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPES[state.getValue(LAYERS)];
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos)
    {
        return SHAPES[state.getValue(LAYERS)];
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state)
    {
        return true;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type)
    {
        return type == PathComputationType.LAND;
    }

    // A thin pile is replaced by a block a player places in it (as one layer of snow is); never by a falling block (a
    // falling pile breaks on it instead and pours its dust in: onBrokenAfterFall)
    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context)
    {
        return state.getValue(LAYERS) == 1 && !context.getItemInHand().isEmpty();
    }

    // It lies on anything a falling block would stop on, or on a full pile
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        return supports(level, pos.below());
    }

    public static boolean supports(BlockGetter level, BlockPos below)
    {
        BlockState under = level.getBlockState(below);
        if (under.getBlock() instanceof DustPileBlock) return under.getValue(LAYERS) == 8;
        return !FallingBlock.isFree(under);
    }

    // ---- falling ----

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
    {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!oldState.is(this)) level.scheduleTick(pos, this, FALL_DELAY);
    }

    // Something next to it changed: it checks whether it's lost what holds it up
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction,
                                     BlockPos neighborPos, BlockState neighborState, RandomSource random)
    {
        ticks.scheduleTick(pos, this, FALL_DELAY);
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    private static final int FALL_DELAY = 2;

    // Nothing under it: it falls, carrying its dust (the falling block's blockData: what the pile saves)
    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if (pos.getY() < level.getMinY() || supports(level, pos.below()) || !FallingBlock.isFree(level.getBlockState(pos.below()))) return;
        if (!(level.getBlockEntity(pos) instanceof DustPileBlockEntity pile)) return;
        net.minecraft.nbt.CompoundTag data = pile.saveCustomOnly(level.registryAccess());
        FallingBlockEntity falling = FallingBlockEntity.fall(level, pos, state);
        falling.blockData = data;
    }

    // It couldn't land as a pile (on another pile, say): its dust is poured out where it stopped
    @Override
    public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity falling)
    {
        if (!(level instanceof ServerLevel server) || falling.blockData == null) return;
        DustPileBlockEntity.readHeld(server, falling.blockData).forEach((element, amount) ->
                DustClouds.deposit(server, pos, element, amount, server.getMaxY()));
    }

    // Broken with a shovel: its dust comes back as orbs, one for each element
    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.player.Player player)
    {
        if (level instanceof ServerLevel server && player.getMainHandItem().is(net.minecraft.tags.ItemTags.SHOVELS)
                && level.getBlockEntity(pos) instanceof DustPileBlockEntity pile) {
            net.minecraft.world.phys.Vec3 at = net.minecraft.world.phys.Vec3.atCenterOf(pos);
            pile.held().forEach((element, amount) -> {
                if (amount > 0) com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity.spawn(server, element, amount, at);
            });
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DustPileBlockEntity(pos, state);
    }

    // Server: watching for a flame or a creeper in its cloud. Client: motes drifting in its cloud.
    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (type != DustBlockEntityRegistry.DUST_PILE.get()) return null;
        if (level.isClientSide()) return (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof DustPileBlockEntity pile) pile.clientTick();
        };
        return (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof DustPileBlockEntity pile && tickLevel instanceof ServerLevel server) pile.serverTick(server);
        };
    }
}
