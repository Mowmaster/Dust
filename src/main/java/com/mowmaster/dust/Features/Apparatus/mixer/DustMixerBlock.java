package com.mowmaster.dust.Features.Apparatus.mixer;

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

// The Dust Mixer (models/block/apparatus/dust_mixer.json; the block draws its frame, dust_mixer_base.json, and
// Client/DustMixerRenderer the moving bowl, grate and paddles): mixes two solid elements piped in from its sides (the
// blue crystals; tubes join on there only) into the element made of them, and tips the result out in front of it as a
// Dust Pile (DustMixerBlockEntity). FACING: its front, toward the player who placed it. A redstone signal stops it
// (it tips out what it has) and holds it tipped until the signal goes. Right-click with an empty hand: what it's doing.
public class DustMixerBlock extends Block implements EntityBlock, DustTubeConnectable
{
    public static final net.minecraft.world.level.block.state.properties.EnumProperty<Direction> FACING = StationBlock.FACING;
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 15, 16);

    public DustMixerBlock(Properties properties)
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
        return new DustMixerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        return (tickLevel, pos, tickState, blockEntity) -> {
            if (!(blockEntity instanceof DustMixerBlockEntity mixer)) return;
            if (tickLevel instanceof ServerLevel server) mixer.serverTick(server);
            else mixer.clientTick();
        };
    }

    // Tubes on its two sides only (the inputs)
    @Override
    public boolean connectsDustTube(BlockState state, Direction side)
    {
        Direction facing = state.getValue(FACING);
        return side == facing.getClockWise() || side == facing.getCounterClockWise();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof DustMixerBlockEntity mixer)) return InteractionResult.PASS;
        if (!level.isClientSide()) player.sendOverlayMessage(mixer.status(player));
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
