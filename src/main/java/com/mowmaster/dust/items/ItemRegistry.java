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

    public static Item scrollA;
    public static Item scrollB;
    public static Item scrollC;
    public static Item scrollD;
    public static Item scrollE;
    public static Item scrollF;
    public static Item scrollG;
    public static Item scrollH;
    public static Item scrollI;
    public static Item scrollJ;
    public static Item scrollK;
    public static Item scrollL;
    public static Item scrollM;
    public static Item scrollN;
    public static Item scrollO;
    public static Item scrollP;
    public static Item scrollQ;
    public static Item scrollR;
    public static Item scrollS;
    public static Item scrollT;
    public static Item scrollU;
    public static Item scrollV;
    public static Item scrollW;
    public static Item scrollX;
    public static Item scrollY;
    public static Item scrollZ;




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

        scrollA = new ItemBasic("scrolla","scrolla",1);
        scrollB = new ItemBasic("scrollb","scrollb",1);
        scrollC = new ItemBasic("scrollc","scrollc",1);
        scrollD = new ItemBasic("scrolld","scrolld",1);
        scrollE = new ItemBasic("scrolle","scrolle",1);
        scrollF = new ItemBasic("scrollf","scrollf",1);
        scrollG = new ItemBasic("scrollg","scrollg",1);
        scrollH = new ItemBasic("scrollh","scrollh",1);
        scrollI = new ItemBasic("scrolli","scrolli",1);
        scrollJ = new ItemBasic("scrollj","scrollj",1);
        scrollK = new ItemBasic("scrollk","scrollk",1);
        scrollL = new ItemBasic("scrolll","scrolll",1);
        scrollM = new ItemBasic("scrollm","scrollm",1);
        scrollN = new ItemBasic("scrolln","scrolln",1);
        scrollO = new ItemBasic("scrollo","scrollo",1);
        scrollP = new ItemBasic("scrollp","scrollp",1);
        scrollQ = new ItemBasic("scrollq","scrollq",1);
        scrollR = new ItemBasic("scrollr","scrollr",1);
        scrollS = new ItemBasic("scrolls","scrolls",1);
        scrollT = new ItemBasic("scrollt","scrollt",1);
        scrollU = new ItemBasic("scrollu","scrollu",1);
        scrollV = new ItemBasic("scrollv","scrollv",1);
        scrollW = new ItemBasic("scrollw","scrollw",1);
        scrollX = new ItemBasic("scrollx","scrollx",1);
        scrollY = new ItemBasic("scrolly","scrolly",1);
        scrollZ = new ItemBasic("scrollz","scrollz",1);
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

        registerItem(scrollA);
        registerItem(scrollB);
        registerItem(scrollC);
        registerItem(scrollD);
        registerItem(scrollE);
        registerItem(scrollF);
        registerItem(scrollG);
        registerItem(scrollH);
        registerItem(scrollI);
        registerItem(scrollJ);
        registerItem(scrollK);
        registerItem(scrollL);
        registerItem(scrollM);
        registerItem(scrollN);
        registerItem(scrollO);
        registerItem(scrollP);
        registerItem(scrollQ);
        registerItem(scrollR);
        registerItem(scrollS);
        registerItem(scrollT);
        registerItem(scrollU);
        registerItem(scrollV);
        registerItem(scrollW);
        registerItem(scrollX);
        registerItem(scrollY);
        registerItem(scrollZ);
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

        registerRender(scrollA);
        registerRender(scrollB);
        registerRender(scrollC);
        registerRender(scrollD);
        registerRender(scrollE);
        registerRender(scrollF);
        registerRender(scrollG);
        registerRender(scrollH);
        registerRender(scrollI);
        registerRender(scrollJ);
        registerRender(scrollK);
        registerRender(scrollL);
        registerRender(scrollM);
        registerRender(scrollN);
        registerRender(scrollO);
        registerRender(scrollP);
        registerRender(scrollQ);
        registerRender(scrollR);
        registerRender(scrollS);
        registerRender(scrollT);
        registerRender(scrollU);
        registerRender(scrollV);
        registerRender(scrollW);
        registerRender(scrollX);
        registerRender(scrollY);
        registerRender(scrollZ);

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