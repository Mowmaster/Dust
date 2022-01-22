package com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.Furnaces.Furnace;

import com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.DustFueledMachineBaseBlockEntity;
import com.mowmaster.dust.Configs.DustConfig;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SmelterFurnaceBlockEntity_T15 extends DustFueledMachineBaseBlockEntity {
    public SmelterFurnaceBlockEntity_T15(BlockPos p_155229_, BlockState p_155230_) {
        super(DeferredBlockEntityTypes.FURNACE_SMELTER_T15.get(), p_155229_, p_155230_);
    }

    /*============================================================================
    ==============================================================================
    =====================   BASE CLASS OVERRIDES - START   =======================
    ==============================================================================
    ============================================================================*/

    @Override
    public int getRepairSlotsForRepairs()
    {
        return DustConfig.COMMON.repairitemsToCraft_Smelter_T15.get();
    }

    @Override
    public Block getBlockForThisBlockEntity()
    {
        return DeferredRegisterTileBlocks.BLOCK_FURNACE_SMELTER_T15.get();
    }

    @Override
    public RecipeType getRecipeTypeForBlock()
    {
        return RecipeType.SMELTING;
    }

    /*============================================================================
    ==============================================================================
    =====================    BASE CLASS OVERRIDES - END    =======================
    ==============================================================================
    ============================================================================*/
}
