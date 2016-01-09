package com.mowmaster.dust.init;


import com.mowmaster.dust.item.ItemCrystalRed;
import com.mowmaster.dust.item.ItemDust;
import net.minecraftforge.fml.common.registry.GameRegistry;


public final class ModItems{
    // *******
    // * NOTE: @GameRegistry.ObjectHolder requires these fields to have the same name as the unlocalized name of the
    // *       object.
    // *
    public static final ItemDust crystalred = new ItemCrystalRed();

    private ModItems()
    {
        throw new AssertionError();
    }

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
