package com.mowmaster.dust.Block.BlockEntities.Tier1.Furnaces.Smoker;

import com.mowmaster.dust.Block.BlockEntities.Tier1.Furnaces.BlastFurnace.BlastFurnaceBlockEntity_T15;
import com.mowmaster.dust.Block.BlockEntities.Tier1.Furnaces.DustFurnacesBaseBlock;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SmokerFurnaceBlock_T15 extends DustFurnacesBaseBlock
{

    protected final VoxelShape FURNACE;

    public SmokerFurnaceBlock_T15(Properties p_152915_) {
        super(p_152915_);
        this.FURNACE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 2.0D, 8.0D, 16.0D),
                Block.box(14.0D, 0.0D, 0.0D, 16.0D, 8.00, 16.0D),
                Block.box(2.0D, 0.0D, 0.0D, 14.0D, 8.0D, 2.0D),
                Block.box(2.0D, 0.0D, 14.0D, 14.0D, 8.0D, 16.0D),
                Block.box(2.0D, 7.0D, 2.0D, 14.0D, 8.0D, 14.0D));

    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return this.FURNACE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return DeferredBlockEntityTypes.FURNACE_SMOKER_T15.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : (level0, pos, state0, blockEntity) -> ((SmokerFurnaceBlockEntity_T15) blockEntity).tick();
    }


}
