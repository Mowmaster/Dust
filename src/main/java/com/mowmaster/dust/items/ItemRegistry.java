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
    public static Item debug;
    public static Item akashic;
    public static Item dust;
    public static Item crystal;
    public static Item bit;
    public static Item dustyCharcoal;
    public static Item scroll;
    public static Item crushingComponents; // fixed the capitalization to use proper camel casing instead of being all lowercase
    public static Item finnisher;

    public static Item ancientCoinA;
    public static Item ancientCoinB;
    public static Item ancientCoinC;
    public static Item ancientCoinD;
    public static Item ancientCoinE;
    public static Item ancientCoinF;
    public static Item ancientCoinG;
    public static Item ancientCoinH;
    public static Item ancientCoinI;
    public static Item ancientCoinJ;
    public static Item ancientCoinK;
    public static Item ancientCoinL;
    public static Item ancientCoinM;
    public static Item ancientCoinN;
    public static Item ancientCoinO;
    public static Item ancientCoinP;
    public static Item ancientCoinQ;
    public static Item ancientCoinR;
    public static Item ancientCoinS;
    public static Item ancientCoinT;
    public static Item ancientCoinU;
    public static Item ancientCoinV;
    public static Item ancientCoinW;
    public static Item ancientCoinX;
    public static Item ancientCoinY;
    public static Item ancientCoinZ;




    public static void init()
    {
        ancientCoinA = new ItemBasic("ancientcoina","ancientcoina",64);
        ancientCoinB = new ItemBasic("ancientcoinb","ancientcoinb",64);
        ancientCoinC = new ItemBasic("ancientcoinc","ancientcoinc",64);
        ancientCoinD = new ItemBasic("ancientcoind","ancientcoind",64);
        ancientCoinE = new ItemBasic("ancientcoine","ancientcoine",64);
        ancientCoinF = new ItemBasic("ancientcoinf","ancientcoinf",64);
        ancientCoinG = new ItemBasic("ancientcoing","ancientcoing",64);
        ancientCoinH = new ItemBasic("ancientcoinh","ancientcoinh",64);
        ancientCoinI = new ItemBasic("ancientcoini","ancientcoini",64);
        ancientCoinJ = new ItemBasic("ancientcoinj","ancientcoinj",64);
        ancientCoinK = new ItemBasic("ancientcoink","ancientcoink",64);
        ancientCoinL = new ItemBasic("ancientcoinl","ancientcoinl",64);
        ancientCoinM = new ItemBasic("ancientcoinm","ancientcoinm",64);
        ancientCoinN = new ItemBasic("ancientcoinn","ancientcoinn",64);
        ancientCoinO = new ItemBasic("ancientcoino","ancientcoino",64);
        ancientCoinP = new ItemBasic("ancientcoinp","ancientcoinp",64);
        ancientCoinQ = new ItemBasic("ancientcoinq","ancientcoinq",64);
        ancientCoinR = new ItemBasic("ancientcoinr","ancientcoinr",64);
        ancientCoinS = new ItemBasic("ancientcoins","ancientcoins",64);
        ancientCoinT = new ItemBasic("ancientcoint","ancientcoint",64);
        ancientCoinU = new ItemBasic("ancientcoinu","ancientcoinu",64);
        ancientCoinV = new ItemBasic("ancientcoinv","ancientcoinv",64);
        ancientCoinW = new ItemBasic("ancientcoinw","ancientcoinw",64);
        ancientCoinX = new ItemBasic("ancientcoinx","ancientcoinx",64);
        ancientCoinY = new ItemBasic("ancientcoiny","ancientcoiny",64);
        ancientCoinZ = new ItemBasic("ancientcoinz","ancientcoinz",64);
        debug = new ItemBasic("debug","debug");
        akashic = new ItemBasic("akashic","akashic");
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
        registerItem(ancientCoinD);
        registerItem(ancientCoinE);
        registerItem(ancientCoinF);
        registerItem(ancientCoinG);
        registerItem(ancientCoinH);
        registerItem(ancientCoinI);
        registerItem(ancientCoinJ);
        registerItem(ancientCoinK);
        registerItem(ancientCoinL);
        registerItem(ancientCoinM);
        registerItem(ancientCoinN);
        registerItem(ancientCoinO);
        registerItem(ancientCoinP);
        registerItem(ancientCoinQ);
        registerItem(ancientCoinR);
        registerItem(ancientCoinS);
        registerItem(ancientCoinT);
        registerItem(ancientCoinU);
        registerItem(ancientCoinV);
        registerItem(ancientCoinW);
        registerItem(ancientCoinX);
        registerItem(ancientCoinY);
        registerItem(ancientCoinZ);
        registerItem(debug);
        registerItem(akashic);
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
        registerRender(akashic);
        registerRender(ancientCoinA);
        registerRender(ancientCoinB);
        registerRender(ancientCoinC);
        registerRender(ancientCoinD);
        registerRender(ancientCoinE);
        registerRender(ancientCoinF);
        registerRender(ancientCoinG);
        registerRender(ancientCoinH);
        registerRender(ancientCoinI);
        registerRender(ancientCoinJ);
        registerRender(ancientCoinK);
        registerRender(ancientCoinL);
        registerRender(ancientCoinM);
        registerRender(ancientCoinN);
        registerRender(ancientCoinO);
        registerRender(ancientCoinP);
        registerRender(ancientCoinQ);
        registerRender(ancientCoinR);
        registerRender(ancientCoinS);
        registerRender(ancientCoinT);
        registerRender(ancientCoinU);
        registerRender(ancientCoinV);
        registerRender(ancientCoinW);
        registerRender(ancientCoinX);
        registerRender(ancientCoinY);
        registerRender(ancientCoinZ);

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