package com.mowmaster.dust.recipes.fuels;

import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class FuelTypes implements IFuelHandler
{
    @Override
    public int getBurnTime(ItemStack fuel){
        if(fuel.getItem() == ItemRegistry.dustyCharcoal)
        {
            switch (fuel.getMetadata())
            {
                case 0:
                    return 1800;
                case 1:
                    return 2400;
                case 2:
                    return 2200;
                case 3:
                    return 1400;
                case 4:
                    return 1600;
                case 5:
                    return 2000;
                case 6:
                    return 2600;
                case 7:
                    return 1200;
            }
        }
        return 0;
    }
}
