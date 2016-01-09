package com.mowmaster.dust.init;


import com.mowmaster.dust.item.ItemCrystalRed;
import com.mowmaster.dust.item.ItemDust;
import net.minecraftforge.fml.common.registry.GameRegistry;


public final class ModItems{

    public static final ItemDust crystalred = new ItemCrystalRed();

    public static void init(){

        registerItems();

    }

    public static void registerItems()
    {
        GameRegistry.registerItem(crystalred, Names.CRYSTALRED);
    }

    public enum Names
    {
        INSTANCE;

        // Items
        public static final String CRYSTALRED = "crystalred";
    }
}
