package com.mowmaster.dust.recipes.fuels;


import net.minecraftforge.fml.common.registry.GameRegistry;

public class FuelRegistry
{
    public static void init()
    {
        GameRegistry.registerFuelHandler(new FuelTypes());
    }
}
