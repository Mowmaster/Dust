package com.mowmaster.dust.items;

import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.items.trinkets.ItemFinnisher;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class ItemRegistry
{


    public static Item dust;
    public static Item crystal;
    public static Item bit;
    public static Item scroll;
    public static Item crushingcomponents;
    public static Item finnisher;




    public static void init()
    {
        dust = new ItemDust("dust");
        crystal = new ItemCrystal("crystal");
        bit = new ItemBit("bit");
        scroll = new ItemScroll("scroll","scroll");
        crushingcomponents = new ItemBasic("crusheritems","crusheritems");
        finnisher = new ItemFinnisher("finnisher","finnisher",10,10.0F,false,
                new PotionEffect(Potion.getPotionById(3),1200,10),
                new PotionEffect(Potion.getPotionById(5),1200,10),
        new PotionEffect(Potion.getPotionById(10),1200,5));
    }

    public static void register()
    {
        registerItem(dust);
        registerItem(crystal);
        registerItem(bit);
        registerItem(scroll);
        registerItem(crushingcomponents);
        registerItem(finnisher);
    }

    public static void registerRenders()
    {

        for(int i = 0; i < CrystalItems.CrystalTypes.values().length; i++)
        {
            registerRender(crystal,i,"crystal_" + CrystalItems.CrystalTypes.values()[i].getName());
        }

        for(int i = 0; i < CrystalItems.DustTypes.values().length; i++)
        {
            registerRender(dust,i,"dust_" + CrystalItems.DustTypes.values()[i].getName());
        }
        for(int i = 0; i < CrystalItems.BitTypes.values().length; i++)
        {
            registerRender(bit,i,"bit_" + CrystalItems.BitTypes.values()[i].getName());
        }

        registerRender(scroll);
        registerRender(crushingcomponents);
        registerRender(finnisher);
    }

    public static void registerItem(Item item)
    {
        ForgeRegistries.ITEMS.register(item);
    }

    public static void registerRender(Item item)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(Reference.MODID, item.getUnlocalizedName().substring(5)), "inventory"));
    }

    public static void registerRender(Item item, int meta, String fileName)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Reference.MODID, fileName), "inventory"));
    }




}