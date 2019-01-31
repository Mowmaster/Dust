package com.mowmaster.dust.items;

import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.renderer.block.model.ModelBakery;
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

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        for(int i = 0; i < CrystalItems.BitTypes.values().length; i++)
        {
            items.add(new ItemStack(this,1,i));
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

    public static void bakeItem()
    {
        ModelBakery.registerItemVariants(ItemRegistry.bit,
                new ResourceLocation(Reference.MODID,"bit_red"),
                new ResourceLocation(Reference.MODID,"bit_blue"),
                new ResourceLocation(Reference.MODID,"bit_yellow"),
                new ResourceLocation(Reference.MODID,"bit_purple"),
                new ResourceLocation(Reference.MODID,"bit_green"),
                new ResourceLocation(Reference.MODID,"bit_orange"),
                new ResourceLocation(Reference.MODID,"bit_white"),
                new ResourceLocation(Reference.MODID,"bit_black")
        );
    }
}
