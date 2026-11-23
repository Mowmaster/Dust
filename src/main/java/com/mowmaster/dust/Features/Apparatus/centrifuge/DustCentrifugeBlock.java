package com.mowmaster.dust.Features.Apparatus.centrifuge;

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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

// The Dust Centrifuge (models/block/apparatus/dust_centrifuge.json; the block draws its base and stand,
// dust_centrifuge_base.json, and Client/DustCentrifugeRenderer the spinning arms and vials): spins a gaseous or
// aqueous element apart into the two it's made of (DustCentrifugeBlockEntity). FACING: its front, toward the player
// who placed it; dust is piped in at its back (tubes join on there only). What it makes drains out of Dust Chutes fixed
// to its sides (a filtered chute lets out only its element). Right-click with an empty hand: what it's doing.
public class DustCentrifugeBlock extends Block implements EntityBlock, DustTubeConnectable
{
    public static final net.minecraft.world.level.block.state.properties.EnumProperty<Direction> FACING = StationBlock.FACING;
    private static final VoxelShape SHAPE = Shapes.or(Block.box(0, 0, 0, 16, 12, 16), Block.box(5, 12, 5, 11, 16, 11));

    public DustCentrifugeBlock(Properties properties)
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
        return new DustCentrifugeBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (type != DustBlockEntityRegistry.DUST_CENTRIFUGE.get()) return null;
        return (tickLevel, pos, tickState, blockEntity) -> {
            if (!(blockEntity instanceof DustCentrifugeBlockEntity centrifuge)) return;
            if (tickLevel instanceof ServerLevel server) centrifuge.serverTick(server);
            else centrifuge.clientTick();
        };
    }

    // Tubes at its back only (the input)
    @Override
    public boolean connectsDustTube(BlockState state, Direction side)
    {
        return side == state.getValue(FACING).getOpposite();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof DustCentrifugeBlockEntity centrifuge)) return InteractionResult.PASS;
        if (!level.isClientSide()) player.sendOverlayMessage(centrifuge.status(player));
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
