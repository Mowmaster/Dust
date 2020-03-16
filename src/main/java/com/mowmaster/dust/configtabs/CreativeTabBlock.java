package com.mowmaster.dust.configtabs;

import com.mowmaster.dust.blocks.BlockDustStone;
import com.mowmaster.dust.item.pedestalUpgrades.ItemUpgradeFilterItemStack;
import com.mowmaster.dust.references.Reference;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreativeTabBlock extends ItemGroup
{
    public CreativeTabBlock() {
        super(Reference.MODID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(BlockDustStone.ITEM_STONE_333);
    }
}
