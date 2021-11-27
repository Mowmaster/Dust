package com.mowmaster.dust.Items.Upgrades.Pedestal;

import com.mowmaster.dust.CreativeTabs.DustItemTabs;
import net.minecraft.world.item.Item;

public class ItemUpgradeBase extends Item implements IPedestalUpgrade
{
    public ItemUpgradeBase(Properties p_41383_) {
        super(new Properties().tab(DustItemTabs.TAB_ITEMS));
    }
}
