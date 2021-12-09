package com.mowmaster.dust.Util;

import com.mowmaster.dust.Block.BlockEntities.CustomDustBlock.CustomPowderedBlockEntity;
import com.mowmaster.dust.Configs.DustEffectConfig;
import com.mowmaster.dust.Configs.DustMachineConfig;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import com.mowmaster.dust.Items.ColoredCrystalBase;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CustomPlaceBlock
{
    public static void placeCustomDustBlock(Level world, BlockPos pos, ItemStack input)
    {
        if(input.getItem() instanceof ColoredCrystalBase)
        {
            int dustAmount = DustMachineConfig.COMMON.dustPerCrystal.get();
            for(int i=1;i<=dustAmount;i++)
            {
                world.setBlock(pos.offset(0,i-1,0), ColorReference.addColorToBlockState(DeferredRegisterBlocks.CRYSTAL_DUST_BLOCK.get().defaultBlockState(),ColorReference.getColorFromItemStackInt(input)),3);
            }
        }
        else
        {
            world.setBlock(pos, DeferredRegisterTileBlocks.BLOCK_POWDERED_DUST.get().defaultBlockState(),3);
            if(world.getBlockEntity(pos) instanceof CustomPowderedBlockEntity)
            {
                CustomPowderedBlockEntity customDust = ((CustomPowderedBlockEntity)world.getBlockEntity(pos));
                //Set these based on some recipe handler
                customDust.addItem(input);
                customDust.setColor(6750207);
            }
        }
    }
}
