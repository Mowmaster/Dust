package com.mowmaster.dust.recipes.fuels;

import com.mowmaster.dust.blocks.treebits.BlockCharcoal;
import com.mowmaster.dust.items.ItemCharcoal;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class FuelTypes implements IFuelHandler
{
    @Override
    public int getBurnTime(ItemStack fuel){
        if(fuel.getItem() instanceof ItemCharcoal)
        {
            if(fuel.getItem().equals(ItemRegistry.charcoalRed)){return 1800;}
            else if(fuel.getItem().equals(ItemRegistry.charcoalBlue)){return 2400;}
            else if(fuel.getItem().equals(ItemRegistry.charcoalYellow)){return 2200;}
            else if(fuel.getItem().equals(ItemRegistry.charcoalPurple)){return 1400;}
            else if(fuel.getItem().equals(ItemRegistry.charcoalGreen)){return 1600;}
            else if(fuel.getItem().equals(ItemRegistry.charcoalOrange)){return 2000;}
            else if(fuel.getItem().equals(ItemRegistry.charcoalWhite)){return 2600;}
            else if(fuel.getItem().equals(ItemRegistry.charcoalBlack)){return 1200;}
            else if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalRed).getItem())){return 16200;}
            else if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalBlue).getItem())){return 21600;}
            else if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalYellow).getItem())){return 19800;}
            else if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalPurple).getItem())){return 12600;}
            else if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalGreen).getItem())){return 14400;}
            else if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalOrange).getItem())){return 18000;}
            else if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalWhite).getItem())){return 23400;}
            else if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalBlack).getItem())){return 10800;}
        }
        if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalRed).getItem())){return 16200;}
        else if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalBlue).getItem())){return 21600;}
        else if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalYellow).getItem())){return 19800;}
        else if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalPurple).getItem())){return 12600;}
        else if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalGreen).getItem())){return 14400;}
        else if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalOrange).getItem())){return 18000;}
        else if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalWhite).getItem())){return 23400;}
        else if(fuel.getItem().equals(new ItemStack(BlockCharcoal.charcoalBlack).getItem())){return 10800;}

        return 0;
    }
}
