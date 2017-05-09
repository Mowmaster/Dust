package com.mowmaster.dust.blocks.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by KingMowmaster on 3/16/2017.
 */
public class ItemBlockOre extends ItemBlock
{
    public ItemBlockOre(Block block)
    {
        super(block);
        if (!(block instanceof IMetaBlockName))
        {
            throw new IllegalArgumentException(String.format("The given Block %s is not an instance of IMetaBlockName!",block.getUnlocalizedName()));
        }
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "_" + ((IMetaBlockName) this.block).getSpecialName(stack);
    }

    public int getMetadata(int damage) {
        return damage;
    }
}
