package com.mowmaster.dust.Features.Apparatus.cyclone;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustStorage.DustNetwork;
import com.mowmaster.dust.Features.DustStorage.DustTubeConnectable;
import com.mowmaster.dust.Features.Machines.StationBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

// The Dust Cyclone (models/block/apparatus/dust_cyclone_top.json), over a Dust Cyclone Hopper: spins a solid element
// apart into the two it's made of (DustCycloneBlockEntity). FACING: its front, toward the player who placed it, where
// the lighter part goes out along the tubes there (the red crystal); dust is piped in at its back (the blue crystal).
// Tubes join on at those two sides only. Right-click with an empty hand: what it's doing.
public class DustCycloneBlock extends Block implements EntityBlock, DustTubeConnectable
{
    public static final net.minecraft.world.level.block.state.properties.EnumProperty<Direction> FACING = StationBlock.FACING;
    private static final VoxelShape SHAPE = Block.box(1.5, 0, 1.5, 14.5, 16, 14.5);

    public DustCycloneBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DustCycloneBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (type != DustBlockEntityRegistry.DUST_CYCLONE.get()) return null;
        return (tickLevel, pos, tickState, blockEntity) -> {
            if (!(blockEntity instanceof DustCycloneBlockEntity cyclone)) return;
            if (tickLevel instanceof ServerLevel server) cyclone.serverTick(server);
            else cyclone.clientTick();
        };
    }

    // Tubes at its front (out) and back (in) only
    @Override
    public boolean connectsDustTube(BlockState state, Direction side)
    {
        Direction facing = state.getValue(FACING);
        return side == facing || side == facing.getOpposite();
    }

    // Its barrel is 2 pixels in from its edges
    @Override
    public double attachInset(BlockState state, Direction side)
    {
        return side.getAxis().isHorizontal() ? 2 : 0;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof DustCycloneBlockEntity cyclone)) return InteractionResult.PASS;
        if (!level.isClientSide()) player.sendOverlayMessage(cyclone.status(player));
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
    {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!oldState.is(this)) DustNetwork.changed(level);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston)
    {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        DustNetwork.changed(level);
    }
}
