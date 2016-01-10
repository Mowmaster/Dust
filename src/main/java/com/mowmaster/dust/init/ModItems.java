package com.mowmaster.dust.init;

import com.mowmaster.dust.item.ItemDust;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModItems
{
    public static Item ItemCrystalRed;
    public static Item ItemCrystalBlue;
    public static Item ItemCrystalYellow;

    public static void dustItems()
    {
        GameRegistry.registerItem(ItemCrystalRed = new ItemDust("crystal_red"), "crystal_red");
        GameRegistry.registerItem(ItemCrystalBlue = new ItemDust("crystal_blue"), "crystal_blue");
        GameRegistry.registerItem(ItemCrystalYellow = new ItemDust("crystal_yellow"), "crystal_yellow");
    }
}
