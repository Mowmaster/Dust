package com.mowmaster.dust.items;

import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.references.Reference;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class ItemDust extends Item
{
    Enum[] enumToUse;

    public ItemDust(String unlocName,Enum[] type)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, unlocName));
        this.enumToUse = type;
        this.setHasSubtypes(true);
        this.setCreativeTab(DUSTTABS);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        for(int i = 0; i < enumToUse.length; i++)
        {
            items.add(new ItemStack(this,1,i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        for(int i = 0; i <enumToUse.length; i++)
        {
            if(stack.getItemDamage() == i)
            {
                return this.getUnlocalizedName() + "." + enumToUse[i];
            }
            else {
                continue;
            }
        }
        return this.getUnlocalizedName() + "." + enumToUse;
    }

    public static void bakeItem()
    {
        ModelBakery.registerItemVariants(ItemRegistry.dust,
                new ResourceLocation(Reference.MODID,"dust_red"),
                new ResourceLocation(Reference.MODID,"dust_blue"),
                new ResourceLocation(Reference.MODID,"dust_yellow"),
                new ResourceLocation(Reference.MODID,"dust_purple"),
                new ResourceLocation(Reference.MODID,"dust_green"),
                new ResourceLocation(Reference.MODID,"dust_orange"),
                new ResourceLocation(Reference.MODID,"dust_white"),
                new ResourceLocation(Reference.MODID,"dust_black"),
                new ResourceLocation(Reference.MODID,"dust_stone"),
                new ResourceLocation(Reference.MODID,"dust_iron"),
                new ResourceLocation(Reference.MODID,"dust_gold")
        );
    }



}
