package com.mowmaster.dust.init;

import com.mowmaster.dust.item.ItemDust;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModItems
{
    public static Item ItemCrystalRed;

    public static void dustItems()
    {
        GameRegistry.registerItem(ItemCrystalRed = new ItemDust("crystal_red"), "crystal_red");
    }
}
