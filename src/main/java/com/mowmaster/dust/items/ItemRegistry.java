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

    public static Item ancientCoinA;
    public static Item ancientCoinB;
    public static Item ancientCoinC;
    public static Item debug;
    public static Item dust;
    public static Item crystal;
    public static Item bit;
    public static Item dustyCharcoal;
    public static Item scroll;
    public static Item crushingComponents; // fixed the capitalization to use proper camel casing instead of being all lowercase
    public static Item finnisher;




    public static void init()
    {
        ancientCoinA = new ItemBasic("ancientcoina","ancientcoina",64);
        ancientCoinB = new ItemBasic("ancientcoinb","ancientcoinb",64);
        ancientCoinC = new ItemBasic("ancientcoinc","ancientcoinc",64);
        debug = new ItemBasic("debug","debug");
        dust = new ItemDust("dust");
        crystal = new ItemCrystal("crystal");
        bit = new ItemBit("bit");
        dustyCharcoal = new ItemCharcoal("charcoal");
        scroll = new ItemScroll("scroll","scroll");
        crushingComponents = new ItemBasic("crusheritems","crusheritems");
        finnisher = new ItemFinnisher("finnisher","finnisher",10,10.0F,false,
                new PotionEffect(Potion.getPotionById(3),1200,10),
                new PotionEffect(Potion.getPotionById(5),1200,10),
        new PotionEffect(Potion.getPotionById(10),1200,5));
    }

    public static void register()
    {
        registerItem(ancientCoinA);
        registerItem(ancientCoinB);
        registerItem(ancientCoinC);
        registerItem(debug);
        registerItem(dust);
        registerItem(crystal);
        registerItem(bit);
        registerItem(dustyCharcoal);
        registerItem(scroll);
        registerItem(crushingComponents);
        registerItem(finnisher);
    }

    public static void registerRenders()
    {
        registerRender(debug);
        registerRender(ancientCoinA);
        registerRender(ancientCoinB);
        registerRender(ancientCoinC);
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
        for(int i = 0; i < CrystalItems.CharcoalTypes.values().length; i++)
        {
            registerRender(dustyCharcoal,i,"charcoal_" + CrystalItems.CharcoalTypes.values()[i].getName());
        }

        registerRender(scroll);
        registerRender(crushingComponents);
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