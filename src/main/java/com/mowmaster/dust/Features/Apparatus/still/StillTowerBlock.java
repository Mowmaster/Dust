package com.mowmaster.dust.Features.Apparatus.still;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustStorage.DustNetwork;
import com.mowmaster.dust.Features.DustStorage.DustTubeConnectable;
import com.mowmaster.dust.Features.DustStorage.TubeJoin;
import com.mowmaster.dust.Features.Machines.StationBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

// The Still Tower (models/block/apparatus/still_tower.json): one element of what the still boils, caught as dust
// (StillTowerBlockEntity). Glass Dust Tubes join on to its four sides, reaching 2 pixels in to meet its barrel
// (TubeJoin.NEAR); its window shows the element's colour (Client/StillTowerTint). Right-click with an empty hand: what
// it holds.
public class StillTowerBlock extends StationBlock implements DustTubeConnectable
{
    public StillTowerBlock(Properties properties)
    {
        super(properties, Block.box(1.5, 0, 1, 14.5, 16, 14.5));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new StillTowerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (level.isClientSide() || type != DustBlockEntityRegistry.STILL_TOWER.get()) return null;
        return (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof StillTowerBlockEntity tower && tickLevel instanceof ServerLevel server) tower.serverTick(server);
        };
    }

    // Tubes on the sides only (above and below are the rest of the still)
    @Override
    public boolean connectsDustTube(BlockState state, Direction side)
    {
        return side.getAxis().isHorizontal();
    }

    // Its element: a tube network set to another won't join on to it
    @Override
    public com.mowmaster.dust.Features.DustComposition.@Nullable DustElement tubeElement(net.minecraft.world.level.BlockGetter level, BlockPos pos)
    {
        return level.getBlockEntity(pos) instanceof StillTowerBlockEntity tower ? tower.element() : null;
    }

    @Override
    public TubeJoin tubeJoin(BlockState state, Direction side)
    {
        return TubeJoin.NEAR;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof StillTowerBlockEntity tower)) return InteractionResult.PASS;
        if (!level.isClientSide()) player.sendOverlayMessage(tower.status(player));
        return InteractionResult.SUCCESS;
    }

    // Placed or taken away: the tube networks round it have to be found again
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
