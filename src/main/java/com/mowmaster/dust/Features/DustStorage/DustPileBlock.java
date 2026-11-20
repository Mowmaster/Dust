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
import net.minecraft.world.level.block.Blocks;
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
// like snow layers (LAYERS: one for every DustPileBlockEntity.PER_LAYER of dust), but walked through, and tinted with
// the mix of the elements in it (Client/DustPileTint). It holds any elements together (DustPileBlockEntity).
// Out in the open (nothing solid on top), it makes the air round it dusty (DustClouds): a cloud thick enough catches
// from a flame and blows up. Nothing drops when it's broken; it's lost if what it lies on goes.
public class DustPileBlock extends Block implements EntityBlock
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

    // A thin pile is replaced by whatever's placed in it (as one layer of snow is)
    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context)
    {
        return state.getValue(LAYERS) == 1;
    }

    // It lies on a full top face, or on a full pile
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        return supports(level, pos.below());
    }

    public static boolean supports(BlockGetter level, BlockPos below)
    {
        BlockState under = level.getBlockState(below);
        if (under.getBlock() instanceof DustPileBlock) return under.getValue(LAYERS) == 8;
        return Block.isFaceFull(under.getCollisionShape(level, below), Direction.UP);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction,
                                     BlockPos neighborPos, BlockState neighborState, RandomSource random)
    {
        if (!state.canSurvive(level, pos)) return Blocks.AIR.defaultBlockState();
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
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
