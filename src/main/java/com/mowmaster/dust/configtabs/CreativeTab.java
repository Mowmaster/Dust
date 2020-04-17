package com.mowmaster.dust.configtabs;

import com.mowmaster.dust.item.ItemRegistry;
import com.mowmaster.dust.item.pedestalUpgrades.ItemUpgradeFilterItemStack;
import com.mowmaster.dust.references.Reference;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreativeTab extends ItemGroup
{
    public CreativeTab() {
        super(Reference.MODID+"_items");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemUpgradeFilterItemStack.ITEMSTACK);
    }
}
