package com.mowmaster.dust.recipes.fuels;

import com.mowmaster.dust.blocks.treebits.BlockCharcoal;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class FuelTypes implements IFuelHandler
{
    @Override
    public int getBurnTime(ItemStack fuel){
        /*if(fuel.getItem() instanceof ItemCharcoal)
        {
            if(fuel.getItem().equals(ItemRegistry.charcoalRed)){return 1800;}
            else if(fuel.getItem().equals(ItemRegistry.charcoalBlue)){return 2400;}
            else if(fuel.getItem().equals(ItemRegistry.charcoalYellow)){return 2200;}
            else if(fuel.getItem().equals(ItemRegistry.charcoalPurple)){return 1400;}
            else if(fuel.getItem().equals(ItemRegistry.charcoalGreen)){return 1600;}
            else if(fuel.getItem().equals(ItemRegistry.charcoalOrange)){return 2000;}
            else if(fuel.getItem().equals(ItemRegistry.charcoalWhite)){return 2600;}
            else if(fuel.getItem().equals(ItemRegistry.charcoalBlack)){return 1200;}
        }*/

        if(Block.getBlockFromItem(fuel.getItem()) instanceof BlockCharcoal)
        {
            switch (fuel.getMetadata())
            {
                case 0:
                    return 16200;

                case 1:
                    return 21600;

                case 2:
                    return 19800;

                case 3:
                    return 12600;

                case 4:
                    return 14400;

                case 5:
                    return 18000;

                case 6:
                    return 23400;

                case 7:
                    return 10800;

                default:
                    return 14400;
            }
        }

        return 0;
    }
}
