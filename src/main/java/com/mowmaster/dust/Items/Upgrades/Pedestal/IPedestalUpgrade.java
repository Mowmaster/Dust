package com.mowmaster.dust.Items.Upgrades.Pedestal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IPedestalUpgrade
{
    int getComparatorRedstoneLevel(Level worldIn, BlockPos pos);
}
