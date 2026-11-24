package com.mowmaster.dust.Features.Trees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

// A slab stood on its side: half a block against one side (FACING, the side it's against), or a whole block (DOUBLE)
// when a second one is put into the empty half. Burns like planks (Flammable) and holds water like a slab.
// Placing one against a block's side stands it against that side; on a floor or ceiling it goes against the edge of the
// block nearest where you clicked (or the side you're facing, clicking near the middle). Clicking a slab's open face
// with another fills it. Model: models/block/template_vertical_slab.json (the half against the north side, turned).
public class VerticalSlabBlock extends Block implements SimpleWaterloggedBlock, Flammable
{
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty DOUBLE = BooleanProperty.create("double");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape NORTH = Block.box(0, 0, 0, 16, 16, 8);
    private static final VoxelShape SOUTH = Block.box(0, 0, 8, 16, 16, 16);
    private static final VoxelShape WEST = Block.box(0, 0, 0, 8, 16, 16);
    private static final VoxelShape EAST = Block.box(8, 0, 0, 16, 16, 16);
    // How far from the middle (of 0.5) a click on a floor or ceiling must be to pick the edge it's nearest
    private static final double EDGE_PICK = 0.1;

    public VerticalSlabBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(DOUBLE, false).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, DOUBLE, WATERLOGGED);
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state)
    {
        return !state.getValue(DOUBLE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        if (state.getValue(DOUBLE)) return Shapes.block();
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            default -> NORTH;
        };
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockPos pos = context.getClickedPos();
        BlockState there = context.getLevel().getBlockState(pos);
        if (there.is(this)) return there.setValue(DOUBLE, true).setValue(WATERLOGGED, false);
        boolean water = context.getLevel().getFluidState(pos).is(Fluids.WATER);
        Direction clicked = context.getClickedFace();
        Direction facing;
        if (clicked.getAxis().isHorizontal()) facing = clicked.getOpposite();
        else {
            Vec3 hit = context.getClickLocation();
            double dx = hit.x - pos.getX() - 0.5, dz = hit.z - pos.getZ() - 0.5;
            if (Math.max(Math.abs(dx), Math.abs(dz)) < EDGE_PICK) facing = context.getHorizontalDirection();
            else if (Math.abs(dx) > Math.abs(dz)) facing = dx > 0 ? Direction.EAST : Direction.WEST;
            else facing = dz > 0 ? Direction.SOUTH : Direction.NORTH;
        }
        return defaultBlockState().setValue(FACING, facing).setValue(WATERLOGGED, water);
    }

    // A half slab is filled by another clicked on to its open face (or placed into its empty half from beside it)
    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context)
    {
        if (state.getValue(DOUBLE) || !context.getItemInHand().is(asItem())) return false;
        if (context.replacingClickedOnBlock()) return context.getClickedFace() == state.getValue(FACING).getOpposite();
        return true;
    }

    @Override
    protected FluidState getFluidState(BlockState state)
    {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState)
    {
        return !state.getValue(DOUBLE) && SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type)
    {
        return !state.getValue(DOUBLE) && SimpleWaterloggedBlock.super.canPlaceLiquid(user, level, pos, state, type);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour,
                                     BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        if (state.getValue(WATERLOGGED)) ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type)
    {
        return type == PathComputationType.WATER && state.getFluidState().is(Fluids.WATER);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public int igniteOdds()
    {
        return PLANKS_IGNITE;
    }

    @Override
    public int burnOdds()
    {
        return PLANKS_BURN;
    }
}
