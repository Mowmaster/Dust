package com.mowmaster.dust.Block.BlockEntities.Tier1.Furnaces.Smoker;

import com.mowmaster.dust.Block.BlockEntities.Tier1.Furnaces.DustFurnacesBaseBlockEntity;
import com.mowmaster.dust.Configs.DustConfig;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import com.mowmaster.dust.Networking.DustPacketHandler;
import com.mowmaster.dust.Networking.DustPacketParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SmokerFurnaceBlockEntity_T15 extends DustFurnacesBaseBlockEntity {
    public SmokerFurnaceBlockEntity_T15(BlockPos p_155229_, BlockState p_155230_) {
        super(DeferredBlockEntityTypes.FURNACE_SMOKER_T15.get(), p_155229_, p_155230_);
    }

    /*============================================================================
    ==============================================================================
    =====================   BASE CLASS OVERRIDES - START   =======================
    ==============================================================================
    ============================================================================*/

    @Override
    public int getRepairSlotsForRepairs()
    {
        return DustConfig.COMMON.repairitemsToCraft_Smoker_T15.get();
    }

    @Override
    public Block getBlockForThisBlockEntity()
    {
        return DeferredRegisterTileBlocks.BLOCK_FURNACE_SMOKER_T15.get();
    }

    @Override
    public RecipeType getRecipeTypeForBlock() { return RecipeType.SMOKING; }

    /*============================================================================
    ==============================================================================
    =====================    BASE CLASS OVERRIDES - END    =======================
    ==============================================================================
    ============================================================================*/

    @Override
    public void tick()
    {
        if(getLevel().getGameTime()%20 == 0){if(!isFullyRepaired()){ DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX(),getPos().getY()+0.5,getPos().getZ(),255,255,255));}}
    }
}
