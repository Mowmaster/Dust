package com.mowmaster.dust.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by KingMowmaster on 2/7/2016.
 */
public class fuelHandlers implements IFuelHandler
{
    @Override
    public int getBurnTime (ItemStack fuel)
    {

        /* Block blockFuel = Block.getBlockFromItem(fuel.getItem())*/
        Item itemFuel = fuel.getItem();
        if (itemFuel == ModItems.ItemCrystalRed)
        {
            return 2400;
        }
        else if (itemFuel == ModItems.ItemCrystalYellow)
        {
            return 4000;
        }
        else if (itemFuel == ModItems.ItemCrystalOrange)
        {
            return 3200;
        }
        else
        {
            return 0;
        }


    }

    public static void fuelHandles()
    {
        GameRegistry.registerFuelHandler(new fuelHandlers());
    }
}
