package com.mowmaster.dust.Features.Sanctums;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

// The stone at the foot of a sanctum's portal frame (SanctumKeystoneBlockEntity keeps the sanctum's records and opens
// the portal). Unbreakable, as bedrock.
public class SanctumKeystoneBlock extends Block implements EntityBlock
{
    public SanctumKeystoneBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new SanctumKeystoneBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (level.isClientSide() || type != Sanctums.KEYSTONE_ENTITY.get()) return null;
        return (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof SanctumKeystoneBlockEntity keystone && tickLevel instanceof ServerLevel server) keystone.serverTick(server);
        };
    }
}
