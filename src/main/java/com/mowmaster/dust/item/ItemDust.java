package com.mowmaster.dust.item;



import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;



public class ItemDust extends Item
{
    public ItemDust(String unloc)
    {
        super();

        this.setUnlocalizedName(unloc);
        this.setCreativeTab(dustCreativeTabs.dustItems);
    }
}