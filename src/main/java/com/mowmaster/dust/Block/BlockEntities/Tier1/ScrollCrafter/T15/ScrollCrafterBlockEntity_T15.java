package com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.T15;

import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ScrollCrafterBlockEntity_T15 extends BlockEntity {
    public ScrollCrafterBlockEntity_T15(BlockPos p_155229_, BlockState p_155230_) {
        super(DeferredBlockEntityTypes.CRAFTER_SCROLL_T15.get(), p_155229_, p_155230_);
    }

    public static void tick()
    {

    }
}
