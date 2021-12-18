package com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter;

import com.mowmaster.dust.Block.BaseBlocks.BaseColoredBlock;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class ScrollCrafterBlockBase extends BaseColoredBlock implements EntityBlock {

    public ScrollCrafterBlockBase(Properties p_152915_) {
        super(p_152915_);
        this.registerDefaultState(ColorReference.addColorToBlockState(this.defaultBlockState(),ColorReference.DEFAULTCOLOR));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return null;
    }
}
