package com.mowmaster.dust.items.trinkets;

import com.mowmaster.dust.misc.DustyTab;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class ItemCrystalAxe extends ItemAxe
{
       public ItemCrystalAxe(ToolMaterial material)
       {
           // the damage and speed specified here are to pervent a index out of bounds exception from minecraft ItemAxe class needs to be made feel free to play around with these values mow
           super(material,   5.75F, 6.75F * -0.344444F);
           this.setUnlocalizedName(material.name().toLowerCase()+"Axe");
           this.setCreativeTab(DustyTab.DUSTTABS);
           this.setRegistryName("crystalAxe");
       }
}
