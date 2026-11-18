package com.mowmaster.dust.Features.Lamps;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

// A lamp (the Growth Lamp, the Lovely Lamp): a lantern (placed on a block or hanging under one, and waterloggable, all
// from vanilla's LanternBlock) with a block entity that burns element dust for an effect around it (LampBlockEntity).
// Right-clicked with an empty hand it says how much dust it holds.
public abstract class LampBlock extends LanternBlock implements EntityBlock
{
    protected LampBlock(Properties properties)
    {
        super(properties);
    }

    // Its block entity's type
    protected abstract BlockEntityType<? extends LampBlockEntity> lampType();

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return lampType().create(pos, state);
    }

    // Ticking on both sides: the server burns and works, the client shows its area (LampBlockEntity)
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (type != lampType()) return null;
        BlockEntityTicker<LampBlockEntity> ticker = level.isClientSide() ? LampBlockEntity::clientTick : LampBlockEntity::serverTick;
        return (BlockEntityTicker<T>) ticker;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof LampBlockEntity lamp) lamp.tellFuel(player);
        return InteractionResult.SUCCESS;
    }
}
