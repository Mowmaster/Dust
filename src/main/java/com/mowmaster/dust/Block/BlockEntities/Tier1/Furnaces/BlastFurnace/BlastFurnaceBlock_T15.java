package com.mowmaster.dust.Block.BlockEntities.Tier1.Furnaces.BlastFurnace;

import com.mowmaster.dust.Block.BlockEntities.Tier1.Furnaces.DustFurnacesBaseBlock;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlastFurnaceBlock_T15 extends DustFurnacesBaseBlock
{

    protected final VoxelShape FURNACE;

    public BlastFurnaceBlock_T15(Properties p_152915_) {
        super(p_152915_);
        this.FURNACE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 2.0D, 8.0D, 16.0D),
                Block.box(14.0D, 0.0D, 0.0D, 16.0D, 8.00, 16.0D),
                Block.box(2.0D, 0.0D, 0.0D, 14.0D, 8.0D, 2.0D),
                Block.box(2.0D, 0.0D, 14.0D, 14.0D, 8.0D, 16.0D));

    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return this.FURNACE;
    }


}
