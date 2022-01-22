package com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.Furnaces.Furnace;

import com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.DustFueledMachineBaseBlock;
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

public class SmelterFurnaceBlock_T15 extends DustFueledMachineBaseBlock
{

    protected final VoxelShape FURNACE;

    public SmelterFurnaceBlock_T15(Properties p_152915_) {
        super(p_152915_);
        this.FURNACE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 2.0D, 14.0D, 16.0D),
                Block.box(14.0D, 0.0D, 0.0D, 16.0D, 14.00, 16.0D),
                Block.box(2.0D, 0.0D, 0.0D, 14.0D, 14.0D, 2.0D),
                Block.box(2.0D, 0.0D, 14.0D, 14.0D, 14.0D, 16.0D),
                Block.box(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return this.FURNACE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return DeferredBlockEntityTypes.FURNACE_SMELTER_T15.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : (level0, pos, state0, blockEntity) -> ((SmelterFurnaceBlockEntity_T15) blockEntity).tick();
    }
}
