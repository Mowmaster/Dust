package com.mowmaster.dust.items;

import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.references.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class ItemBit extends Item
{
    public ItemBit(String unlocName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, unlocName));
        this.setHasSubtypes(true);
        this.setCreativeTab(DUSTTABS);
    }
    //@Override
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for(int i = 0; i < CrystalItems.BitTypes.values().length; i++)
        {
            list.add(new ItemStack(item,1,i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        for(int i = 0; i <CrystalItems.BitTypes.values().length; i++)
        {
            if(stack.getItemDamage() == i)
            {
                return this.getUnlocalizedName() + "." + CrystalItems.BitTypes.values()[i].getName();
            }
            else {
                continue;
            }
        }
        return this.getUnlocalizedName() + "." + CrystalItems.BitTypes.RED.getName();
    }
}
