package com.mowmaster.dust.item;


import com.mowmaster.dust.reference.reference;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;


public class ItemDust extends Item {
    public ItemDust(){
        setCreativeTab(CreativeTabs.tabMaterials);
    }

     //public static String getFormattedName(Material material){
     //    return WordUtils.capitalizeFully(material.name().toLowerCase());
     //}

    @Override
    public String getUnlocalizedName(){
        return String.format("items.%s%s", reference.ITEM_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return getUnlocalizedName();
    }

    public static String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf('.') + 1);
    }

}
