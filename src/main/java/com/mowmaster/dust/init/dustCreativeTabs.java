package com.mowmaster.dust.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by KingMowmaster on 2/4/2016.
 */
public class dustCreativeTabs
{

    public static final CreativeTabs dustItems = new CreativeTabs("dustItems") {
        @Override
        public Item getTabIconItem() {
            return ModItems.ItemCrystalRed;
        }
    };

    public static final CreativeTabs dustArmor = new CreativeTabs("dustArmor") {
        @Override
        public Item getTabIconItem() {
            return ModItems.RedCrystalChestplate;
        }
    };

    public static final CreativeTabs dustTool = new CreativeTabs("dustTool") {
        @Override
        public Item getTabIconItem() {
            return ModItems.crystalSwordRed;
        }
    };

}
