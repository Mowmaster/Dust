package com.mowmaster.dust.items;

import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.items.trinkets.ItemFinnisher;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class ItemRegistry
{
    public static Item akashic;
    public static Item dust;
    public static Item crystal;
    public static Item bit;
    public static Item charcoalRed;
    public static Item charcoalBlue;
    public static Item charcoalYellow;
    public static Item charcoalPurple;
    public static Item charcoalGreen;
    public static Item charcoalOrange;
    public static Item charcoalWhite;
    public static Item charcoalBlack;
    public static Item scroll;
    public static Item crushingComponents; // fixed the capitalization to use proper camel casing instead of being all lowercase
    public static Item furnaceComponents;
    public static Item finnisher;
    public static Item dustbread;

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

    public static Item spellPaper;

    public static Item tomeGuideBook;




    public static void init()
    {
        ancientCoinA = new ItemCoin("ancientcoina","ancientcoina");
        ancientCoinB = new ItemCoin("ancientcoinb","ancientcoinb");
        ancientCoinC = new ItemCoin("ancientcoinc","ancientcoinc");
        ancientCoinD = new ItemCoin("ancientcoind","ancientcoind");
        ancientCoinE = new ItemCoin("ancientcoine","ancientcoine");
        ancientCoinF = new ItemCoin("ancientcoinf","ancientcoinf");
        ancientCoinG = new ItemCoin("ancientcoing","ancientcoing");
        ancientCoinH = new ItemCoin("ancientcoinh","ancientcoinh");
        ancientCoinI = new ItemCoin("ancientcoini","ancientcoini");
        ancientCoinJ = new ItemCoin("ancientcoinj","ancientcoinj");
        ancientCoinK = new ItemCoin("ancientcoink","ancientcoink");
        ancientCoinL = new ItemCoin("ancientcoinl","ancientcoinl");
        ancientCoinM = new ItemCoin("ancientcoinm","ancientcoinm");
        ancientCoinN = new ItemCoin("ancientcoinn","ancientcoinn");
        ancientCoinO = new ItemCoin("ancientcoino","ancientcoino");
        ancientCoinP = new ItemCoin("ancientcoinp","ancientcoinp");
        ancientCoinQ = new ItemCoin("ancientcoinq","ancientcoinq");
        ancientCoinR = new ItemCoin("ancientcoinr","ancientcoinr");
        ancientCoinS = new ItemCoin("ancientcoins","ancientcoins");
        ancientCoinT = new ItemCoin("ancientcoint","ancientcoint");
        ancientCoinU = new ItemCoin("ancientcoinu","ancientcoinu");
        ancientCoinV = new ItemCoin("ancientcoinv","ancientcoinv");
        ancientCoinW = new ItemCoin("ancientcoinw","ancientcoinw");
        ancientCoinX = new ItemCoin("ancientcoinx","ancientcoinx");
        ancientCoinY = new ItemCoin("ancientcoiny","ancientcoiny");
        ancientCoinZ = new ItemCoin("ancientcoinz","ancientcoinz");

        scrollA = new ItemScroll("scrolla","scrolla");
        scrollB = new ItemScroll("scrollb","scrollb");
        scrollC = new ItemScroll("scrollc","scrollc");
        scrollD = new ItemScroll("scrolld","scrolld");
        scrollE = new ItemScroll("scrolle","scrolle");
        scrollF = new ItemScroll("scrollf","scrollf");
        scrollG = new ItemScroll("scrollg","scrollg");
        scrollH = new ItemScroll("scrollh","scrollh");
        scrollI = new ItemScroll("scrolli","scrolli");
        scrollJ = new ItemScroll("scrollj","scrollj");
        scrollK = new ItemScroll("scrollk","scrollk");
        scrollL = new ItemScroll("scrolll","scrolll");
        scrollM = new ItemScroll("scrollm","scrollm");
        scrollN = new ItemScroll("scrolln","scrolln");
        scrollO = new ItemScroll("scrollo","scrollo");
        scrollP = new ItemScroll("scrollp","scrollp");
        scrollQ = new ItemScroll("scrollq","scrollq");
        scrollR = new ItemScroll("scrollr","scrollr");
        scrollS = new ItemScroll("scrolls","scrolls");
        scrollT = new ItemScroll("scrollt","scrollt");
        scrollU = new ItemScroll("scrollu","scrollu");
        scrollV = new ItemScroll("scrollv","scrollv");
        scrollW = new ItemScroll("scrollw","scrollw");
        scrollX = new ItemScroll("scrollx","scrollx");
        scrollY = new ItemScroll("scrolly","scrolly");
        scrollZ = new ItemScroll("scrollz","scrollz");

        spellPaper = new ItemSpellScroll("spellpaper","spellpaper");

        tomeGuideBook = new ItemGuideBook("guidebook","guidebook");

        akashic = new ItemBasic("akashic","akashic");
        dust = new ItemDust("dust");
        crystal = new ItemCrystal("crystal");
        bit = new ItemBit("bit");
        charcoalRed = new ItemCharcoal("charcoalred","charcoalred");
        charcoalBlue = new ItemCharcoal("charcoalblue","charcoalblue");
        charcoalYellow = new ItemCharcoal("charcoalyellow","charcoalyellow");
        charcoalPurple = new ItemCharcoal("charcoalpurple","charcoalpurple");
        charcoalGreen = new ItemCharcoal("charcoalgreen","charcoalgreen");
        charcoalOrange = new ItemCharcoal("charcoalorange","charcoalorange");
        charcoalWhite = new ItemCharcoal("charcoalwhite","charcoalwhite");
        charcoalBlack = new ItemCharcoal("charcoalblack","charcoalblack");
        scroll = new ItemScroll("scroll","scroll");
        crushingComponents = new ItemBasic("crusheritems","crusheritems");
        furnaceComponents = new ItemUpgrade("crystalfurnaceupgrade","crystalfurnaceupgrade");
        finnisher = new ItemFinnisher("finnisher","finnisher",10,10.0F,false,
                new PotionEffect(Potion.getPotionById(3),1200,10),
                new PotionEffect(Potion.getPotionById(5),1200,10),
        new PotionEffect(Potion.getPotionById(10),1200,5));

        dustbread = new ItemDustBread("dustbread","dustbread");
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
        registerItem(spellPaper);
        registerItem(tomeGuideBook);
        registerItem(akashic);
        registerItem(dust);
        registerItem(crystal);
        registerItem(bit);
        registerItem(charcoalRed);
        registerItem(charcoalBlue);
        registerItem(charcoalYellow);
        registerItem(charcoalPurple);
        registerItem(charcoalGreen);
        registerItem(charcoalOrange);
        registerItem(charcoalWhite);
        registerItem(charcoalBlack);
        registerItem(scroll);
        registerItem(crushingComponents);
        registerItem(furnaceComponents);
        registerItem(finnisher);
        registerItem(dustbread);
    }

    public static void registerRenders()
    {
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

        registerRender(spellPaper);
        registerRender(tomeGuideBook);

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

        registerRender(charcoalRed);
        registerRender(charcoalBlue);
        registerRender(charcoalYellow);
        registerRender(charcoalPurple);
        registerRender(charcoalGreen);
        registerRender(charcoalOrange);
        registerRender(charcoalWhite);
        registerRender(charcoalBlack);
        registerRender(scroll);
        registerRender(crushingComponents);
        registerRender(furnaceComponents);
        registerRender(finnisher);
        registerRender(dustbread);
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