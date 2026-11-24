package com.mowmaster.dust.Features.Trees;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// An elemental wood chest's contents: a vanilla chest's, under Dust's own block entity type
public class ElementalChestBlockEntity extends ChestBlockEntity
{
    public ElementalChestBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(DustBlockEntityRegistry.ELEMENTAL_CHEST.get(), worldPosition, blockState);
    }
}
