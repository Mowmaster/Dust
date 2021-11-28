package com.mowmaster.dust.Items.Upgrades.Pedestal;

import com.mowmaster.dust.CreativeTabs.DustItemTabs;
import com.mowmaster.dust.Util.PedestalUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;


public class ItemUpgradeBase extends Item implements IPedestalUpgrade
{
    public ItemUpgradeBase(Properties p_41383_) {
        super(new Properties().tab(DustItemTabs.TAB_ITEMS));
    }

    @Override
    public int getComparatorRedstoneLevel(Level worldIn, BlockPos pos) {
        return PedestalUtilities.getRedstoneLevelPedestal(worldIn, pos);
    }
}
