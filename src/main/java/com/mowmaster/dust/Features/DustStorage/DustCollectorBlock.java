package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

// The Dust Collector (models/block/apparatus/dust_collector.json): gathers the Dust Piles lying on the 3x3 blocks centred
// over it and sends the dust out along the Glass Dust Tubes joined on to its sides and bottom (DustCollectorBlockEntity).
// Right-click it with a Dust Jar to draw its dust into the jar; with an empty hand: what it holds.
public class DustCollectorBlock extends Block implements EntityBlock, DustTubeConnectable
{
    public DustCollectorBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DustCollectorBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (level.isClientSide() || type != DustBlockEntityRegistry.DUST_COLLECTOR.get()) return null;
        return (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof DustCollectorBlockEntity collector && tickLevel instanceof ServerLevel server) collector.serverTick(server);
        };
    }

    // Tubes on the sides and bottom (the top is the grate the piles lie on)
    @Override
    public boolean connectsDustTube(BlockState state, Direction side)
    {
        return side != Direction.UP;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!(stack.getItem() instanceof DustJarItem) || !(level.getBlockEntity(pos) instanceof DustCollectorBlockEntity collector)) return InteractionResult.TRY_WITH_EMPTY_HAND;
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        return collector.fillJar(player, hand) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof DustCollectorBlockEntity collector)) return InteractionResult.PASS;
        if (!level.isClientSide()) player.sendOverlayMessage(collector.status(player));
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
