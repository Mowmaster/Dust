package com.mowmaster.dust.items;

import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.items.itemPedestalUpgrades.*;
import com.mowmaster.dust.items.tools.ItemDustTippedArrow;
import com.mowmaster.dust.items.trinkets.ItemFinnisher;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
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
    public static Item wikiscroll;



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
    public static Item craftingPlaceholder;
    //public static Item dustbread;

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
    public static Item ancientCoin;

    public static Item userUpgrade;
    public static Item shearingUpgrade;
    public static Item chopperUpgrade;
    public static Item crafter1Upgrade;
    public static Item crafter4Upgrade;
    public static Item crafter9Upgrade;
    public static Item exportUpgrade;
    public static Item singleExportUpgrade;
    public static Item importUpgrade;
    public static Item dropperUpgrade;
    public static Item placerUpgrade;
    public static Item breakerUpgrade;
    public static Item filterUpgrade;
    public static Item fuzzyFilterUpgrade;
    public static Item filterModUpgrade;
    public static Item filterBlacklistUpgrade;
    public static Item fuzzyFilterBlacklistUpgrade;
    public static Item filterModBlacklistUpgrade;
    public static Item furnaceUpgrade;
    public static Item crusherUpgrade;
    public static Item cobbleGenUpgrade;
    public static Item upgradeEnchanted;
    public static Item upgradeEnchantedBlacklist;
    public static Item effectUpgrade;
    public static Item growerUpgrade;
    public static Item planterUpgrade;
    public static Item harvesterUpgrade;

    public static Item enchantUpgrade;
    public static Item expCollector;
    public static Item expDropper;
    public static Item expRelay;
    public static Item expTank;
    public static Item expBottler;
    public static Item expAnvil;

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
    public static Item crystalWrench;

    public static Item dustTippedArrow;




    public static void init()
    {
        dustTippedArrow = new ItemDustTippedArrow("dusttippedarrow");

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
        ancientCoin = new ItemCoin("coin","coin");

        importUpgrade = new ipuImport("importupgrade","importupgrade");
        exportUpgrade = new ipuExport("exportupgrade","exportupgrade");
        singleExportUpgrade = new ipuExportRestock("singleexportupgrade","singleexportupgrade");
        dropperUpgrade = new ipuDropper("dropperupgrade","dropperupgrade");
        chopperUpgrade = new ipuChopper("chopperupgrade","chopperupgrade");
        craftingPlaceholder = new ItemBasic("craftingplaceholder","craftingplaceholder",64);
        crafter1Upgrade = new ipuCrafter("crafter1upgrade","crafter1upgrade",1);
        crafter4Upgrade = new ipuCrafter("crafter4upgrade","crafter4upgrade",2);
        crafter9Upgrade = new ipuCrafter("crafter9upgrade","crafter9upgrade",3);
        furnaceUpgrade = new ipuFurnace("upgradefurnace","upgradefurnace");
        crusherUpgrade = new ipuCrystalCrusher("upgradecrusher","upgradecrusher");
        cobbleGenUpgrade = new ipuCobbleGen("upgradecobblegen","upgradecobblegen");//
        upgradeEnchanted = new ipuFilterEnchanted("upgradeenchanted","upgradeenchanted");
        upgradeEnchantedBlacklist = new ipuFilterEnchantedBlacklist("upgradeenchantedblacklist","upgradeenchantedblacklist");
        filterModUpgrade = new ipuFilterMod("filtermodupgrade","filtermodupgrade");
        filterModBlacklistUpgrade = new ipuFilterModBlacklist("filtermodblacklistupgrade","filtermodblacklistupgrade");
        filterUpgrade = new ipuFilterItemStack("filterupgrade","filterupgrade");
        filterBlacklistUpgrade = new ipuFilterItemStackBlacklist("filterblacklistupgrade","filterblacklistupgrade");
        fuzzyFilterUpgrade = new ipuFilterItem("fuzzyfilterupgrade","fuzzyfilterupgrade");
        fuzzyFilterBlacklistUpgrade = new ipuFilterItemBlacklist("fuzzyfilterblacklistupgrade","fuzzyfilterblacklistupgrade");
        placerUpgrade = new ipuPlacer("placerupgrade","placerupgrade");//
        breakerUpgrade = new ipuBreaker("breakerupgrade","breakerupgrade");//

        shearingUpgrade = new ipuShearer("shearingupgrade","shearingupgrade");//Shearing
        userUpgrade = new ipuMilker("userupgrade","userupgrade");//Milking
        effectUpgrade = new ipuEffectMagnet("effectupgrade","effectupgrade");//Magnetism
        growerUpgrade = new ipuEffectGrower("effectgrower","effectgrower");//
        planterUpgrade = new ipuEffectSower("effectplanter","effectplanter");//
        harvesterUpgrade = new ipuEffectHarvester("effectharvester","effectharvester");//
        enchantUpgrade = new ipuExpEnchanter("enchantupgrade","enchantupgrade");//
        expCollector = new ipuExpCollector("expcollector","expcollector");//
        expDropper = new ipuExpDropper("expdropper","expdropper");//
        expRelay = new ipuExpRelay("exprelay","exprelay");//
        expTank = new ipuExpTank("exptank","exptank");//
        expBottler = new ipuExpBottler("expbottler","expbottler");//

        expAnvil = new ipuExpAnvil("expanvil","expanvil");//Anvil

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
        crystalWrench = new ItemCrystalWrench("wrench","wrench");

        akashic = new ItemBasic("akashic","akashic");
        dust = new ItemDust("dust",CrystalItems.DustTypes.values());
        crystal = new ItemCrystal("crystal");
        bit = new ItemBit("bit");
        wikiscroll = new ItemWikiScroll("wikiscroll");
        charcoalRed = new ItemCharcoal("charcoalred","charcoalred",1800);
        charcoalBlue = new ItemCharcoal("charcoalblue","charcoalblue",2400);
        charcoalYellow = new ItemCharcoal("charcoalyellow","charcoalyellow",2200);
        charcoalPurple = new ItemCharcoal("charcoalpurple","charcoalpurple",1400);
        charcoalGreen = new ItemCharcoal("charcoalgreen","charcoalgreen",1600);
        charcoalOrange = new ItemCharcoal("charcoalorange","charcoalorange",2000);
        charcoalWhite = new ItemCharcoal("charcoalwhite","charcoalwhite",2600);
        charcoalBlack = new ItemCharcoal("charcoalblack","charcoalblack",1200);
        scroll = new ItemScroll("scroll","scroll");
        crushingComponents = new ItemUpgrade("crusheritems","crusheritems");
        furnaceComponents = new ItemUpgrade("crystalfurnaceupgrade","crystalfurnaceupgrade");
        finnisher = new ItemFinnisher("finnisher","finnisher",10,10.0F,false,
                new PotionEffect(Potion.getPotionById(3),1200,10),
                new PotionEffect(Potion.getPotionById(5),1200,10),
        new PotionEffect(Potion.getPotionById(10),1200,5));

        //dustbread = new ItemDustBread("dustbread","dustbread",1,1f);
    }

    public static void register()
    {
        registerItem(dustTippedArrow);

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
        registerItem(ancientCoin);

        registerItem(userUpgrade);
        registerItem(shearingUpgrade);
        registerItem(chopperUpgrade);
        registerItem(crafter1Upgrade);
        registerItem(crafter4Upgrade);
        registerItem(crafter9Upgrade);
        registerItem(singleExportUpgrade);
        registerItem(exportUpgrade);
        registerItem(importUpgrade);
        registerItem(dropperUpgrade);
        registerItem(placerUpgrade);
        registerItem(filterUpgrade);
        registerItem(fuzzyFilterUpgrade);
        registerItem(filterModUpgrade);
        registerItem(filterBlacklistUpgrade);
        registerItem(fuzzyFilterBlacklistUpgrade);
        registerItem(filterModBlacklistUpgrade);
        registerItem(effectUpgrade);
        registerItem(growerUpgrade);
        registerItem(planterUpgrade);
        registerItem(harvesterUpgrade);

        registerItem(enchantUpgrade);
        registerItem(expCollector);
        registerItem(expDropper);
        registerItem(expRelay);
        registerItem(expTank);
        registerItem(expBottler);
        registerItem(expAnvil);

        registerItem(breakerUpgrade);
        registerItem(furnaceUpgrade);
        registerItem(crusherUpgrade);
        registerItem(cobbleGenUpgrade);
        registerItem(upgradeEnchanted);
        registerItem(upgradeEnchantedBlacklist);

        registerItem(craftingPlaceholder);

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
        registerItem(crystalWrench);
        registerItem(akashic);
        registerItem(dust);
        registerItem(crystal);
        registerItem(bit);
        registerItem(wikiscroll);
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
        //registerItem(dustbread);
    }

    public static void registerRenders()
    {
        registerRender(dustTippedArrow);

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
        registerRender(ancientCoin);

        registerRender(userUpgrade);
        registerRender(shearingUpgrade);
        registerRender(chopperUpgrade);
        registerRender(crafter1Upgrade);
        registerRender(crafter4Upgrade);
        registerRender(crafter9Upgrade);

        registerRender(singleExportUpgrade);
        registerRender(exportUpgrade);
        registerRender(importUpgrade);
        registerRender(dropperUpgrade);
        registerRender(placerUpgrade);
        registerRender(filterUpgrade);
        registerRender(fuzzyFilterUpgrade);
        registerRender(filterModUpgrade);
        registerRender(filterBlacklistUpgrade);
        registerRender(fuzzyFilterBlacklistUpgrade);
        registerRender(filterModBlacklistUpgrade);
        registerRender(effectUpgrade);
        registerRender(growerUpgrade);
        registerRender(planterUpgrade);
        registerRender(harvesterUpgrade);

        registerRender(enchantUpgrade);
        registerRender(expCollector);
        registerRender(expDropper);
        registerRender(expRelay);
        registerRender(expTank);
        registerRender(expBottler);
        registerRender(expAnvil);

        registerRender(breakerUpgrade);
        registerRender(furnaceUpgrade);
        registerRender(crusherUpgrade);
        registerRender(cobbleGenUpgrade);
        registerRender(upgradeEnchanted);
        registerRender(upgradeEnchantedBlacklist);

        registerRender(craftingPlaceholder);

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
        registerRender(crystalWrench);

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
        for(int i = 0; i < CrystalItems.CountTypes.values().length; i++)
        {
            registerRender(wikiscroll,i,"wikiscroll_" + CrystalItems.CountTypes.values()[i].getName());
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
        //registerRender(dustbread);
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

    public static void registerItemColor(Item item, int meta, String fileName)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Reference.MODID, fileName), "inventory"));

    }




}