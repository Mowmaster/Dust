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

/**
 * Created by KingMowmaster on 2/25/2017.
 */
public class ItemCrystal extends Item
{
    public ItemCrystal(String unlocName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, unlocName));
        this.setHasSubtypes(true);
        this.setCreativeTab(DUSTTABS);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        for(int i = 0; i < CrystalItems.CrystalTypes.values().length; i++)
        {
            list.add(new ItemStack(this,1,i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        for(int i = 0; i <CrystalItems.CrystalTypes.values().length; i++)
        {
            if(stack.getItemDamage() == i)
            {
                return this.getUnlocalizedName() + "." + CrystalItems.CrystalTypes.values()[i].getName();
            }
            else {
                continue;
            }
        }
        return this.getUnlocalizedName() + "." + CrystalItems.CrystalTypes.RED.getName();
    }
}
