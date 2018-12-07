package com.mowmaster.dust.blocks;

import com.mowmaster.dust.blocks.ancientblocks.BlockAncientFences;
import com.mowmaster.dust.blocks.ancientblocks.BlockAncientSlabs;
import com.mowmaster.dust.blocks.ancientblocks.BlockAncientStairs;
import com.mowmaster.dust.blocks.ancientblocks.BlockAncientWalls;
import com.mowmaster.dust.blocks.item.ItemBlockOre;
import com.mowmaster.dust.blocks.machines.*;
import com.mowmaster.dust.blocks.sapling.*;
import com.mowmaster.dust.enums.CrystalBlocks;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;


public class BlockRegistry
{
    public static Block redOre;
    public static Block blueOre;
    public static Block yellowOre;
    public static Block purpleOre;
    public static Block orangeOre;
    public static Block greenOre;
    public static Block whiteOre;
    public static Block blackOre;

    public static Block redCrystalTorch;
    public static Block blueCrystalTorch;
    public static Block yellowCrystalTorch;
    public static Block purpleCrystalTorch;
    public static Block greenCrystalTorch;
    public static Block orangeCrystalTorch;
    public static Block whiteCrystalTorch;
    public static Block blackCrystalTorch;

    public static Block logred;
    public static Block logblue;
    public static Block logyellow;
    public static Block logpurple;
    public static Block logorange;
    public static Block loggreen;
    public static Block logwhite;
    public static Block logblack;

    public static Block leaf;


    public static Block redCrystalFive;
    public static Block redCrystalFour;
    public static Block redCrystalThree;
    public static Block redCrystalTwo;
    public static Block redCrystalOne;

    public static Block blueCrystalFive;
    public static Block blueCrystalFour;
    public static Block blueCrystalThree;
    public static Block blueCrystalTwo;
    public static Block blueCrystalOne;

    public static Block yellowCrystalFive;
    public static Block yellowCrystalFour;
    public static Block yellowCrystalThree;
    public static Block yellowCrystalTwo;
    public static Block yellowCrystalOne;

    public static Block purpleCrystalFive;
    public static Block purpleCrystalFour;
    public static Block purpleCrystalThree;
    public static Block purpleCrystalTwo;
    public static Block purpleCrystalOne;

    public static Block orangeCrystalFive;
    public static Block orangeCrystalFour;
    public static Block orangeCrystalThree;
    public static Block orangeCrystalTwo;
    public static Block orangeCrystalOne;

    public static Block greenCrystalFive;
    public static Block greenCrystalFour;
    public static Block greenCrystalThree;
    public static Block greenCrystalTwo;
    public static Block greenCrystalOne;

    public static Block whiteCrystalFive;
    public static Block whiteCrystalFour;
    public static Block whiteCrystalThree;
    public static Block whiteCrystalTwo;
    public static Block whiteCrystalOne;

    public static Block blackCrystalFive;
    public static Block blackCrystalFour;
    public static Block blackCrystalThree;
    public static Block blackCrystalTwo;
    public static Block blackCrystalOne;

    public static Block redstone;
    public static Block redstonestairs;
    public static Block redstoneslabs;
    public static Block redstonefences;
    public static Block redstonewalls;
    public static Block redbricks;
    public static Block redbrickstairs;
    public static Block redbrickslabs;
    public static Block redbrickfences;
    public static Block redbrickwalls;
    public static Block redbricks2;
    public static Block redbrickstairs2;
    public static Block redbrickslabs2;
    public static Block redbrickfences2;
    public static Block redbrickwalls2;
    public static Block redplanks;
    public static Block redplankstairs;
    public static Block redplankslabs;
    public static Block redplankfences;
    public static Block bluestone;
    public static Block bluestonestairs;
    public static Block bluestoneslabs;
    public static Block bluestonefences;
    public static Block bluestonewalls;
    public static Block bluebricks;
    public static Block bluebrickstairs;
    public static Block bluebrickslabs;
    public static Block bluebrickfences;
    public static Block bluebrickwalls;
    public static Block bluebricks2;
    public static Block bluebrickstairs2;
    public static Block bluebrickslabs2;
    public static Block bluebrickfences2;
    public static Block bluebrickwalls2;
    public static Block blueplanks;
    public static Block blueplankstairs;
    public static Block blueplankslabs;
    public static Block blueplankfences;
    public static Block yellowstone;
    public static Block yellowstonestairs;
    public static Block yellowstoneslabs;
    public static Block yellowstonefences;
    public static Block yellowstonewalls;
    public static Block yellowbricks;
    public static Block yellowbrickstairs;
    public static Block yellowbrickslabs;
    public static Block yellowbrickfences;
    public static Block yellowbrickwalls;
    public static Block yellowbricks2;
    public static Block yellowbrickstairs2;
    public static Block yellowbrickslabs2;
    public static Block yellowbrickfences2;
    public static Block yellowbrickwalls2;
    public static Block yellowplanks;
    public static Block yellowplankstairs;
    public static Block yellowplankslabs;
    public static Block yellowplankfences;
    public static Block purplestone;
    public static Block purplestonestairs;
    public static Block purplestoneslabs;
    public static Block purplestonefences;
    public static Block purplestonewalls;
    public static Block purplebricks;
    public static Block purplebrickstairs;
    public static Block purplebrickslabs;
    public static Block purplebrickfences;
    public static Block purplebrickwalls;
    public static Block purplebricks2;
    public static Block purplebrickstairs2;
    public static Block purplebrickslabs2;
    public static Block purplebrickfences2;
    public static Block purplebrickwalls2;
    public static Block purpleplanks;
    public static Block purpleplankstairs;
    public static Block purpleplankslabs;
    public static Block purpleplankfences;
    public static Block orangestone;
    public static Block orangestonestairs;
    public static Block orangestoneslabs;
    public static Block orangestonefences;
    public static Block orangestonewalls;
    public static Block orangebricks;
    public static Block orangebrickstairs;
    public static Block orangebrickslabs;
    public static Block orangebrickfences;
    public static Block orangebrickwalls;
    public static Block orangebricks2;
    public static Block orangebrickstairs2;
    public static Block orangebrickslabs2;
    public static Block orangebrickfences2;
    public static Block orangebrickwalls2;
    public static Block orangeplanks;
    public static Block orangeplankstairs;
    public static Block orangeplankslabs;
    public static Block orangeplankfences;
    public static Block greenstone;
    public static Block greenstonestairs;
    public static Block greenstoneslabs;
    public static Block greenstonefences;
    public static Block greenstonewalls;
    public static Block greenbricks;
    public static Block greenbrickstairs;
    public static Block greenbrickslabs;
    public static Block greenbrickfences;
    public static Block greenbrickwalls;
    public static Block greenbricks2;
    public static Block greenbrickstairs2;
    public static Block greenbrickslabs2;
    public static Block greenbrickfences2;
    public static Block greenbrickwalls2;
    public static Block greenplanks;
    public static Block greenplankstairs;
    public static Block greenplankslabs;
    public static Block greenplankfences;
    public static Block whitestone;
    public static Block whitestonestairs;
    public static Block whitestoneslabs;
    public static Block whitestonefences;
    public static Block whitestonewalls;
    public static Block whitebricks;
    public static Block whitebrickstairs;
    public static Block whitebrickslabs;
    public static Block whitebrickfences;
    public static Block whitebrickwalls;
    public static Block whitebricks2;
    public static Block whitebrickstairs2;
    public static Block whitebrickslabs2;
    public static Block whitebrickfences2;
    public static Block whitebrickwalls2;
    public static Block whiteplanks;
    public static Block whiteplankstairs;
    public static Block whiteplankslabs;
    public static Block whiteplankfences;
    public static Block blackstone;
    public static Block blackstonestairs;
    public static Block blackstoneslabs;
    public static Block blackstonefences;
    public static Block blackstonewalls;
    public static Block blackbricks;
    public static Block blackbrickstairs;
    public static Block blackbrickslabs;
    public static Block blackbrickfences;
    public static Block blackbrickwalls;
    public static Block blackbricks2;
    public static Block blackbrickstairs2;
    public static Block blackbrickslabs2;
    public static Block blackbrickfences2;
    public static Block blackbrickwalls2;
    public static Block blackplanks;
    public static Block blackplankstairs;
    public static Block blackplankslabs;
    public static Block blackplankfences;

    public static Block saplingred;
    public static Block saplingblue;
    public static Block saplingyellow;
    public static Block saplingpurple;
    public static Block saplingorange;
    public static Block saplinggreen;
    public static Block saplingwhite;
    public static Block saplingblack;

    public static Block leafRed;
    public static Block leafBlue;
    public static Block leafYellow;
    public static Block leafPurple;
    public static Block leafGreen;
    public static Block leafOrange;
    public static Block leafWhite;
    public static Block leafBlack;

    public static Block redDust;
    public static Block blueDust;
    public static Block yellowDust;
    public static Block purpleDust;
    public static Block orangeDust;
    public static Block greenDust;
    public static Block whiteDust;
    public static Block blackDust;
    public static Block blazeDust;
    public static Block carbonDust;
    public static Block redstoneDust;
    public static Block ironDust;
    public static Block goldDust;
    public static Block wheatDust;
    public static Block potatoDust;
    public static Block sugarDust;
    public static Block dustBlock;

    public static Block crystalCluster;
    public static Block machineBase;
    public static Block enchantmod;
    public static Block crystalcrusher;
    public static Block crystalfurnace;
    public static Block blockTrap;

    public static Block crate1;
    public static Block pot1;
    public static Block path1;
    public static Block path2;
    public static Block path3;
    public static Block path4;
    public static Block farmland;
    public static Block darksoil;
    public static Block darksoilbase;
    public static Block spike1;
    public static Block spike2;
    public static Block spike3;
    public static Block spike4;
    public static Block spike5;
    public static Block voidpot;

    public static Block pedestalstone;
    public static Block pedestalred;
    public static Block pedestalblue;
    public static Block pedestalyellow;
    public static Block pedestalpurple;
    public static Block pedestalorange;
    public static Block pedestalgreen;
    public static Block pedestalwhite;
    public static Block pedestalblack;


    public static Block lootblockaircommon;
    public static Block lootblockairuncommon;
    public static Block lootblockairrare;
    public static Block lootblockairlegendary;
    public static Block lootblockairexotic;
    public static Block lootblockcommon;
    public static Block lootblockuncommon;
    public static Block lootblockrare;
    public static Block lootblocklegendary;
    public static Block lootblockexotic;
    public static Block lootblockpassivespawner;
    public static Block lootblockhostilespawner;
    public static Block lootblockpillar;
    public static Block lootblockcrystalcluster;

    public static Block charcoalRed;
    public static Block charcoalBlue;
    public static Block charcoalYellow;
    public static Block charcoalPurple;
    public static Block charcoalGreen;
    public static Block charcoalOrange;
    public static Block charcoalWhite;
    public static Block charcoalBlack;


    public static void init() {
        redOre = new BlockCrystalOre("redore", "red/redore", Material.ROCK, SoundType.STONE, 3, 15, 10);
        blueOre = new BlockCrystalOre("blueore", "blue/blueore", Material.ROCK, SoundType.STONE, 3, 15, 10);
        yellowOre = new BlockCrystalOre("yellowore", "yellow/yellowore", Material.ROCK, SoundType.STONE, 3, 15, 10);
        purpleOre = new BlockCrystalOre("purpleore", "purple/purpleore", Material.ROCK, SoundType.STONE, 3, 15, 10);
        orangeOre = new BlockCrystalOre("orangeore", "orange/orangeore", Material.ROCK, SoundType.STONE, 3, 15, 10);
        greenOre = new BlockCrystalOre("greenore", "green/greenore", Material.ROCK, SoundType.STONE, 3, 15, 10);
        whiteOre = new BlockCrystalOre("whiteore", "white/whiteore", Material.ROCK, SoundType.STONE, 3, 15, 10);
        blackOre = new BlockCrystalOre("blackore", "black/blackore", Material.ROCK, SoundType.STONE, 3, 15, 10);

        redCrystalTorch = new BlockCrystalTorch("redtorch", "red/redtorch");
        blueCrystalTorch = new BlockCrystalTorch("bluetorch", "blue/bluetorch");
        yellowCrystalTorch = new BlockCrystalTorch("yellowtorch", "yellow/yellowtorch");
        purpleCrystalTorch = new BlockCrystalTorch("purpletorch", "purple/purpletorch");
        greenCrystalTorch = new BlockCrystalTorch("greentorch", "green/greentorch");
        orangeCrystalTorch = new BlockCrystalTorch("orangetorch", "orange/orangetorch");
        whiteCrystalTorch = new BlockCrystalTorch("whitetorch", "white/whitetorch");
        blackCrystalTorch = new BlockCrystalTorch("blacktorch", "black/blacktorch");

        redCrystalFive = new BlockCrystal("blockredcrystalfive", "red/blockredcrystalfive");
        redCrystalFour = new BlockCrystal("blockredcrystalfour", "red/blockredcrystalfour");
        redCrystalThree = new BlockCrystal("blockredcrystalthree", "red/blockredcrystalthree");
        redCrystalTwo = new BlockCrystal("blockredcrystaltwo", "red/blockredcrystaltwo");
        redCrystalOne = new BlockCrystal("blockredcrystalone", "red/blockredcrystalone");

        blueCrystalFive = new BlockCrystal("blockbluecrystalfive", "blue/blockbluecrystalfive");
        blueCrystalFour = new BlockCrystal("blockbluecrystalfour", "blue/blockbluecrystalfour");
        blueCrystalThree = new BlockCrystal("blockbluecrystalthree", "blue/blockbluecrystalthree");
        blueCrystalTwo = new BlockCrystal("blockbluecrystaltwo", "blue/blockbluecrystaltwo");
        blueCrystalOne = new BlockCrystal("blockbluecrystalone", "blue/blockbluecrystalone");

        yellowCrystalFive = new BlockCrystal("blockyellowcrystalfive", "yellow/blockyellowcrystalfive");
        yellowCrystalFour = new BlockCrystal("blockyellowcrystalfour", "yellow/blockyellowcrystalfour");
        yellowCrystalThree = new BlockCrystal("blockyellowcrystalthree", "yellow/blockyellowcrystalthree");
        yellowCrystalTwo = new BlockCrystal("blockyellowcrystaltwo", "yellow/blockyellowcrystaltwo");
        yellowCrystalOne = new BlockCrystal("blockyellowcrystalone", "yellow/blockyellowcrystalone");

        purpleCrystalFive = new BlockCrystal("blockpurplecrystalfive", "purple/blockpurplecrystalfive");
        purpleCrystalFour = new BlockCrystal("blockpurplecrystalfour", "purple/blockpurplecrystalfour");
        purpleCrystalThree = new BlockCrystal("blockpurplecrystalthree", "purple/blockpurplecrystalthree");
        purpleCrystalTwo = new BlockCrystal("blockpurplecrystaltwo", "purple/blockpurplecrystaltwo");
        purpleCrystalOne = new BlockCrystal("blockpurplecrystalone", "purple/blockpurplecrystalone");

        orangeCrystalFive = new BlockCrystal("blockorangecrystalfive", "orange/blockorangecrystalfive");
        orangeCrystalFour = new BlockCrystal("blockorangecrystalfour", "orange/blockorangecrystalfour");
        orangeCrystalThree = new BlockCrystal("blockorangecrystalthree", "orange/blockorangecrystalthree");
        orangeCrystalTwo = new BlockCrystal("blockorangecrystaltwo", "orange/blockorangecrystaltwo");
        orangeCrystalOne = new BlockCrystal("blockorangecrystalone", "orange/blockorangecrystalone");

        greenCrystalFive = new BlockCrystal("blockgreencrystalfive", "green/blockgreencrystalfive");
        greenCrystalFour = new BlockCrystal("blockgreencrystalfour", "green/blockgreencrystalfour");
        greenCrystalThree = new BlockCrystal("blockgreencrystalthree", "green/blockgreencrystalthree");
        greenCrystalTwo = new BlockCrystal("blockgreencrystaltwo", "green/blockgreencrystaltwo");
        greenCrystalOne = new BlockCrystal("blockgreencrystalone", "green/blockgreencrystalone");

        whiteCrystalFive = new BlockCrystal("blockwhitecrystalfive", "white/blockwhitecrystalfive");
        whiteCrystalFour = new BlockCrystal("blockwhitecrystalfour", "white/blockwhitecrystalfour");
        whiteCrystalThree = new BlockCrystal("blockwhitecrystalthree", "white/blockwhitecrystalthree");
        whiteCrystalTwo = new BlockCrystal("blockwhitecrystaltwo", "white/blockwhitecrystaltwo");
        whiteCrystalOne = new BlockCrystal("blockwhitecrystalone", "white/blockWhiteCrystalOne");

        blackCrystalFive = new BlockCrystal("blockblackcrystalfive", "black/blockblackcrystalfive");
        blackCrystalFour = new BlockCrystal("blockblackcrystalfour", "black/blockblackcrystalfour");
        blackCrystalThree = new BlockCrystal("blockblackcrystalthree", "black/blockblackcrystalthree");
        blackCrystalTwo = new BlockCrystal("blockblackcrystaltwo", "black/blockblackcrystaltwo");
        blackCrystalOne = new BlockCrystal("blockblackcrystalone", "black/blockblackcrystalone");

        redDust = new BlockDustCloud("reddust","red/reddust");
        blueDust = new BlockDustCloud("bluedust","blue/bluedust");
        yellowDust = new BlockDustCloud("yellowdust","yellow/yellowdust");
        purpleDust = new BlockDustCloud("purpledust","purple/purpledust");
        greenDust = new BlockDustCloud("greendust","green/greendust");
        orangeDust = new BlockDustCloud("orangedust","orange/orangedust");
        whiteDust = new BlockDustCloud("whitedust","white/whitedust");
        blackDust = new BlockDustCloud("blackdust","black/blackdust");
        blazeDust = new BlockDustCloud("blazedust","dust/blazedust");
        carbonDust = new BlockDustCloud("carbondust","dust/carbondust");
        ironDust = new BlockDustCloud("irondust","dust/irondust");
        goldDust = new BlockDustCloud("golddust","dust/golddust");
        redstoneDust = new BlockDustCloud("redstonedust","dust/redstonedust");
        wheatDust = new BlockDustCloud("wheatdust","dust/wheatdust");
        potatoDust = new BlockDustCloud("potatodust","dust/potatodust");
        sugarDust = new BlockDustCloud("sugardust","dust/sugardust");



        dustBlock = new BlockDust("blockdust","blockdust");

        leaf = new BlockLeaf("leaves");



        logred = new BlockLog("log_red", "red/log_red");
        logblue = new BlockLog("log_blue", "blue/log_blue");
        logyellow = new BlockLog("log_yellow", "yellow/log_yellow");
        logpurple = new BlockLog("log_purple", "purple/log_purple");
        logorange = new BlockLog("log_orange", "orange/log_orange");
        loggreen = new BlockLog("log_green", "green/log_green");
        logwhite = new BlockLog("log_white", "white/log_white");
        logblack = new BlockLog("log_black", "black/log_black");

        saplingred = new SaplingRed("saplingred","red/saplingred");
        saplingblue = new SaplingBlue("saplingblue","blue/saplingblue");
        saplingyellow = new SaplingYellow("saplingyellow","yellow/saplingyellow");
        saplingpurple = new SaplingPurple("saplingpurple","purple/saplingpurple");
        saplingorange = new SaplingOrange("saplingorange","orange/saplingorange");
        saplinggreen = new SaplingGreen("saplinggreen","green/saplinggreen");
        saplingwhite = new SaplingWhite("saplingwhite","white/saplingwhite");
        saplingblack = new SaplingBlack("saplingblack","black/saplingblack");

        redstone = new BlockBasic("redstone", "ancient/redstone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        redstonestairs = new BlockAncientStairs("redstonestairs", "ancient/redstonestairs", redstone, SoundType.STONE, 3, 20, 10);
        redstoneslabs = new BlockAncientSlabs("redstoneslabs", "ancient/redstoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        redstonefences = new BlockAncientFences("redstonefences", "ancient/redstonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        redstonewalls = new BlockAncientWalls("redstonewalls", "ancient/redstonewalls", redstone, SoundType.STONE, 3, 20, 10);
        redbricks = new BlockBasic("redbricks", "ancient/redbricks", Material.ROCK, SoundType.STONE, 3, 20, 10);
        redbrickstairs = new BlockAncientStairs("redbrickstairs", "ancient/redbrickstairs", redbricks, SoundType.STONE, 3, 20, 10);
        redbrickslabs = new BlockAncientSlabs("redbrickslabs", "ancient/redbrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        redbrickfences = new BlockAncientFences("redbrickfences", "ancient/redbrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        redbrickwalls = new BlockAncientWalls("redbrickwalls", "ancient/redbrickwalls", redbricks, SoundType.STONE, 3, 20, 10);
        redbricks2 = new BlockBasic("redbricks2", "ancient/redbricks2", Material.ROCK, SoundType.STONE, 3, 20, 10);
        redbrickstairs2 = new BlockAncientStairs("redbrickstairs2", "ancient/redbrickstairs2", redbricks2, SoundType.STONE, 3, 20, 10);
        redbrickslabs2 = new BlockAncientSlabs("redbrickslabs2", "ancient/redbrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        redbrickfences2 = new BlockAncientFences("redbrickfences2", "ancient/redbrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        redbrickwalls2 = new BlockAncientWalls("redbrickwalls2", "ancient/redbrickwalls2", redbricks2, SoundType.STONE, 3, 20, 10);
        redplanks = new BlockBasic("redplanks", "ancient/redplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);
        redplankstairs = new BlockAncientStairs("redplankstairs", "ancient/redplankstairs", redplanks, SoundType.WOOD, 2, 10, 10);
        redplankslabs = new BlockAncientSlabs("redplankslabs", "ancient/redplankslabs",Material.WOOD, SoundType.WOOD, 2, 10, 10);
        redplankfences = new BlockAncientFences("redplankfences", "ancient/redplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        bluestone = new BlockBasic("bluestone", "ancient/bluestone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        bluestonestairs = new BlockAncientStairs("bluestonestairs", "ancient/bluestonestairs", bluestone, SoundType.STONE, 3, 20, 10);
        bluestoneslabs = new BlockAncientSlabs("bluestoneslabs", "ancient/bluestoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        bluestonefences = new BlockAncientFences("bluestonefences", "ancient/bluestonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        bluestonewalls = new BlockAncientWalls("bluestonewalls", "ancient/bluestonewalls", bluestone, SoundType.STONE, 3, 20, 10);
        bluebricks = new BlockBasic("bluebricks", "ancient/bluebricks", Material.ROCK, SoundType.STONE, 3, 20, 10);
        bluebrickstairs = new BlockAncientStairs("bluebrickstairs", "ancient/bluebrickstairs", bluebricks, SoundType.STONE, 3, 20, 10);
        bluebrickslabs = new BlockAncientSlabs("bluebrickslabs", "ancient/bluebrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        bluebrickfences = new BlockAncientFences("bluebrickfences", "ancient/bluebrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        bluebrickwalls = new BlockAncientWalls("bluebrickwalls", "ancient/bluebrickwalls", bluebricks, SoundType.STONE, 3, 20, 10);
        bluebricks2 = new BlockBasic("bluebricks2", "ancient/bluebricks2", Material.ROCK, SoundType.STONE, 3, 20, 10);
        bluebrickstairs2 = new BlockAncientStairs("bluebrickstairs2", "ancient/bluebrickstairs2", bluebricks2, SoundType.STONE, 3, 20, 10);
        bluebrickslabs2 = new BlockAncientSlabs("bluebrickslabs2", "ancient/bluebrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        bluebrickfences2 = new BlockAncientFences("bluebrickfences2", "ancient/bluebrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        bluebrickwalls2 = new BlockAncientWalls("bluebrickwalls2", "ancient/bluebrickwalls2", bluebricks2, SoundType.STONE, 3, 20, 10);
        blueplanks = new BlockBasic("blueplanks", "ancient/blueplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);
        blueplankstairs = new BlockAncientStairs("blueplankstairs", "ancient/blueplankstairs", blueplanks, SoundType.WOOD, 2, 10, 10);
        blueplankslabs = new BlockAncientSlabs("blueplankslabs", "ancient/blueplankslabs",Material.WOOD, SoundType.WOOD, 2, 10, 10);
        blueplankfences = new BlockAncientFences("blueplankfences", "ancient/blueplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        yellowstone = new BlockBasic("yellowstone", "ancient/yellowstone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        yellowstonestairs = new BlockAncientStairs("yellowstonestairs", "ancient/yellowstonestairs", yellowstone, SoundType.STONE, 3, 20, 10);
        yellowstoneslabs = new BlockAncientSlabs("yellowstoneslabs", "ancient/yellowstoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        yellowstonefences = new BlockAncientFences("yellowstonefences", "ancient/yellowstonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        yellowstonewalls = new BlockAncientWalls("yellowstonewalls", "ancient/yellowstonewalls", yellowstone, SoundType.STONE, 3, 20, 10);
        yellowbricks = new BlockBasic("yellowbricks", "ancient/yellowbricks", Material.ROCK, SoundType.STONE, 3, 20, 10);
        yellowbrickstairs = new BlockAncientStairs("yellowbrickstairs", "ancient/yellowbrickstairs", yellowbricks, SoundType.STONE, 3, 20, 10);
        yellowbrickslabs = new BlockAncientSlabs("yellowbrickslabs", "ancient/yellowbrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        yellowbrickfences = new BlockAncientFences("yellowbrickfences", "ancient/yellowbrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        yellowbrickwalls = new BlockAncientWalls("yellowbrickwalls", "ancient/yellowbrickwalls", yellowbricks, SoundType.STONE, 3, 20, 10);
        yellowbricks2 = new BlockBasic("yellowbricks2", "ancient/yellowbricks2", Material.ROCK, SoundType.STONE, 3, 20, 10);
        yellowbrickstairs2 = new BlockAncientStairs("yellowbrickstairs2", "ancient/yellowbrickstairs2", yellowbricks2, SoundType.STONE, 3, 20, 10);
        yellowbrickslabs2 = new BlockAncientSlabs("yellowbrickslabs2", "ancient/yellowbrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        yellowbrickfences2 = new BlockAncientFences("yellowbrickfences2", "ancient/yellowbrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        yellowbrickwalls2 = new BlockAncientWalls("yellowbrickwalls2", "ancient/yellowbrickwalls2", yellowbricks2, SoundType.STONE, 3, 20, 10);
        yellowplanks = new BlockBasic("yellowplanks", "ancient/yellowplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);
        yellowplankstairs = new BlockAncientStairs("yellowplankstairs", "ancient/yellowplankstairs", yellowplanks, SoundType.WOOD, 2, 10, 10);
        yellowplankslabs = new BlockAncientSlabs("yellowplankslabs", "ancient/yellowplankslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        yellowplankfences = new BlockAncientFences("yellowplankfences", "ancient/yellowplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        purplestone = new BlockBasic("purplestone", "ancient/purplestone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        purplestonestairs = new BlockAncientStairs("purplestonestairs", "ancient/purplestonestairs", purplestone, SoundType.STONE, 3, 20, 10);
        purplestoneslabs = new BlockAncientSlabs("purplestoneslabs", "ancient/purplestoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        purplestonefences = new BlockAncientFences("purplestonefences", "ancient/purplestonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        purplestonewalls = new BlockAncientWalls("purplestonewalls", "ancient/purplestonewalls", purplestone, SoundType.STONE, 3, 20, 10);
        purplebricks = new BlockBasic("purplebricks", "ancient/purplebricks", Material.ROCK, SoundType.STONE, 3, 20, 10);
        purplebrickstairs = new BlockAncientStairs("purplebrickstairs", "ancient/purplebrickstairs", purplebricks, SoundType.STONE, 3, 20, 10);
        purplebrickslabs = new BlockAncientSlabs("purplebrickslabs", "ancient/purplebrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        purplebrickfences = new BlockAncientFences("purplebrickfences", "ancient/purplebrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        purplebrickwalls = new BlockAncientWalls("purplebrickwalls", "ancient/purplebrickwalls", purplebricks, SoundType.STONE, 3, 20, 10);
        purplebricks2 = new BlockBasic("purplebricks2", "ancient/purplebricks2", Material.ROCK, SoundType.STONE, 3, 20, 10);
        purplebrickstairs2 = new BlockAncientStairs("purplebrickstairs2", "ancient/purplebrickstairs2", purplebricks2, SoundType.STONE, 3, 20, 10);
        purplebrickslabs2 = new BlockAncientSlabs("purplebrickslabs2", "ancient/purplebrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        purplebrickfences2 = new BlockAncientFences("purplebrickfences2", "ancient/purplebrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        purplebrickwalls2 = new BlockAncientWalls("purplebrickwalls2", "ancient/purplebrickwalls2", purplebricks2, SoundType.STONE, 3, 20, 10);
        purpleplanks = new BlockBasic("purpleplanks", "ancient/purpleplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);
        purpleplankstairs = new BlockAncientStairs("purpleplankstairs", "ancient/purpleplankstairs", purpleplanks, SoundType.WOOD, 2, 10, 10);
        purpleplankslabs = new BlockAncientSlabs("purpleplankslabs", "ancient/purpleplankslabs",Material.WOOD, SoundType.WOOD, 2, 10, 10);
        purpleplankfences = new BlockAncientFences("purpleplankfences", "ancient/purpleplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        orangestone = new BlockBasic("orangestone", "ancient/orangestone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        orangestonestairs = new BlockAncientStairs("orangestonestairs", "ancient/orangestonestairs", orangestone, SoundType.STONE, 3, 20, 10);
        orangestoneslabs = new BlockAncientSlabs("orangestoneslabs", "ancient/orangestoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        orangestonefences = new BlockAncientFences("orangestonefences", "ancient/orangestonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        orangestonewalls = new BlockAncientWalls("orangestonewalls", "ancient/orangestonewalls", orangestone, SoundType.STONE, 3, 20, 10);
        orangebricks = new BlockBasic("orangebricks", "ancient/orangebricks", Material.ROCK, SoundType.STONE, 3, 20, 10);
        orangebrickstairs = new BlockAncientStairs("orangebrickstairs", "ancient/orangebrickstairs", orangebricks, SoundType.STONE, 3, 20, 10);
        orangebrickslabs = new BlockAncientSlabs("orangebrickslabs", "ancient/orangebrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        orangebrickfences = new BlockAncientFences("orangebrickfences", "ancient/orangebrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        orangebrickwalls = new BlockAncientWalls("orangebrickwalls", "ancient/orangebrickwalls", orangebricks, SoundType.STONE, 3, 20, 10);
        orangebricks2 = new BlockBasic("orangebricks2", "ancient/orangebricks2", Material.ROCK, SoundType.STONE, 3, 20, 10);
        orangebrickstairs2 = new BlockAncientStairs("orangebrickstairs2", "ancient/orangebrickstairs2", orangebricks2, SoundType.STONE, 3, 20, 10);
        orangebrickslabs2 = new BlockAncientSlabs("orangebrickslabs2", "ancient/orangebrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        orangebrickfences2 = new BlockAncientFences("orangebrickfences2", "ancient/orangebrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        orangebrickwalls2 = new BlockAncientWalls("orangebrickwalls2", "ancient/orangebrickwalls2", orangebricks2, SoundType.STONE, 3, 20, 10);
        orangeplanks = new BlockBasic("orangeplanks", "ancient/orangeplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);
        orangeplankstairs = new BlockAncientStairs("orangeplankstairs", "ancient/orangeplankstairs", orangeplanks, SoundType.WOOD, 2, 10, 10);
        orangeplankslabs = new BlockAncientSlabs("orangeplankslabs", "ancient/orangeplankslabs",Material.WOOD, SoundType.WOOD, 2, 10, 10);
        orangeplankfences = new BlockAncientFences("orangeplankfences", "ancient/orangeplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        greenstone = new BlockBasic("greenstone", "ancient/greenstone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        greenstonestairs = new BlockAncientStairs("greenstonestairs", "ancient/greenstonestairs", greenstone, SoundType.STONE, 3, 20, 10);
        greenstoneslabs = new BlockAncientSlabs("greenstoneslabs", "ancient/greenstoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        greenstonefences = new BlockAncientFences("greenstonefences", "ancient/greenstonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        greenstonewalls = new BlockAncientWalls("greenstonewalls", "ancient/greenstonewalls", greenstone, SoundType.STONE, 3, 20, 10);
        greenbricks = new BlockBasic("greenbricks", "ancient/greenbricks", Material.ROCK, SoundType.STONE, 30, 20, 10);
        greenbrickstairs = new BlockAncientStairs("greenbrickstairs", "ancient/greenbrickstairs", greenbricks, SoundType.STONE, 3, 20, 10);
        greenbrickslabs = new BlockAncientSlabs("greenbrickslabs", "ancient/greenbrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        greenbrickfences = new BlockAncientFences("greenbrickfences", "ancient/greenbrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        greenbrickwalls = new BlockAncientWalls("greenbrickwalls", "ancient/greenbrickwalls", greenbricks, SoundType.STONE, 3, 20, 10);
        greenbricks2 = new BlockBasic("greenbricks2", "ancient/greenbricks2", Material.ROCK, SoundType.STONE, 30, 20, 10);
        greenbrickstairs2 = new BlockAncientStairs("greenbrickstairs2", "ancient/greenbrickstairs2", greenbricks2, SoundType.STONE, 3, 20, 10);
        greenbrickslabs2 = new BlockAncientSlabs("greenbrickslabs2", "ancient/greenbrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        greenbrickfences2 = new BlockAncientFences("greenbrickfences2", "ancient/greenbrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        greenbrickwalls2 = new BlockAncientWalls("greenbrickwalls2", "ancient/greenbrickwalls2", greenbricks2, SoundType.STONE, 3, 20, 10);
        greenplanks = new BlockBasic("greenplanks", "ancient/greenplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);
        greenplankstairs = new BlockAncientStairs("greenplankstairs", "ancient/greenplankstairs", greenplanks, SoundType.WOOD, 2, 10, 10);
        greenplankslabs = new BlockAncientSlabs("greenplankslabs", "ancient/greenplankslabs",Material.WOOD, SoundType.WOOD, 2, 10, 10);
        greenplankfences = new BlockAncientFences("greenplankfences", "ancient/greenplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        whitestone = new BlockBasic("whitestone", "ancient/whitestone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        whitestonestairs = new BlockAncientStairs("whitestonestairs", "ancient/whitestonestairs", whitestone, SoundType.STONE, 3, 20, 10);
        whitestoneslabs = new BlockAncientSlabs("whitestoneslabs", "ancient/whitestoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        whitestonefences = new BlockAncientFences("whitestonefences", "ancient/whitestonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        whitestonewalls = new BlockAncientWalls("whitestonewalls", "ancient/whitestonewalls", whitestone, SoundType.STONE, 3, 20, 10);
        whitebricks = new BlockBasic("whitebricks", "ancient/whitebricks", Material.ROCK, SoundType.STONE, 3, 20, 10);
        whitebrickstairs = new BlockAncientStairs("whitebrickstairs", "ancient/whitebrickstairs", whitebricks, SoundType.STONE, 3, 20, 10);
        whitebrickslabs = new BlockAncientSlabs("whitebrickslabs", "ancient/whitebrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        whitebrickfences = new BlockAncientFences("whitebrickfences", "ancient/whitebrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        whitebrickwalls = new BlockAncientWalls("whitebrickwalls", "ancient/whitebrickwalls", whitebricks, SoundType.STONE, 3, 20, 10);
        whitebricks2 = new BlockBasic("whitebricks2", "ancient/whitebricks2", Material.ROCK, SoundType.STONE, 3, 20, 10);
        whitebrickstairs2 = new BlockAncientStairs("whitebrickstairs2", "ancient/whitebrickstairs2", whitebricks2, SoundType.STONE, 3, 20, 10);
        whitebrickslabs2 = new BlockAncientSlabs("whitebrickslabs2", "ancient/whitebrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        whitebrickfences2 = new BlockAncientFences("whitebrickfences2", "ancient/whitebrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        whitebrickwalls2 = new BlockAncientWalls("whitebrickwalls2", "ancient/whitebrickwalls2", whitebricks2, SoundType.STONE, 3, 20, 10);
        whiteplanks = new BlockBasic("whiteplanks", "ancient/whiteplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);
        whiteplankstairs = new BlockAncientStairs("whiteplankstairs", "ancient/whiteplankstairs", whiteplanks, SoundType.WOOD, 2, 10, 10);
        whiteplankslabs = new BlockAncientSlabs("whiteplankslabs", "ancient/whiteplankslabs",Material.WOOD, SoundType.WOOD, 2, 10, 10);
        whiteplankfences = new BlockAncientFences("whiteplankfences", "ancient/whiteplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);
        blackstone = new BlockBasic("blackstone", "ancient/blackstone", Material.ROCK, SoundType.STONE, 3, 20, 10);
        blackstonestairs = new BlockAncientStairs("blackstonestairs", "ancient/blackstonestairs", blackstone, SoundType.STONE, 3, 20, 10);
        blackstoneslabs = new BlockAncientSlabs("blackstoneslabs", "ancient/blackstoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        blackstonefences = new BlockAncientFences("blackstonefences", "ancient/blackstonefences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        blackstonewalls = new BlockAncientWalls("blackstonewalls", "ancient/blackstonewalls", blackstone, SoundType.STONE, 3, 20, 10);
        blackbricks = new BlockBasic("blackbricks", "ancient/blackbricks", Material.ROCK, SoundType.STONE, 3, 20, 10);
        blackbrickstairs = new BlockAncientStairs("blackbrickstairs", "ancient/blackbrickstairs", blackbricks, SoundType.STONE, 3, 20, 10);
        blackbrickslabs = new BlockAncientSlabs("blackbrickslabs", "ancient/blackbrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        blackbrickfences = new BlockAncientFences("blackbrickfences", "ancient/blackbrickfences",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        blackbrickwalls = new BlockAncientWalls("blackbrickwalls", "ancient/blackbrickwalls", blackbricks, SoundType.STONE, 3, 20, 10);
        blackbricks2 = new BlockBasic("blackbricks2", "ancient/blackbricks2", Material.ROCK, SoundType.STONE, 3, 20, 10);
        blackbrickstairs2 = new BlockAncientStairs("blackbrickstairs2", "ancient/blackbrickstairs2", blackbricks2, SoundType.STONE, 3, 20, 10);
        blackbrickslabs2 = new BlockAncientSlabs("blackbrickslabs2", "ancient/blackbrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        blackbrickfences2 = new BlockAncientFences("blackbrickfences2", "ancient/blackbrickfences2",Material.ROCK,MapColor.STONE, SoundType.STONE, 3, 20, 10);
        blackbrickwalls2 = new BlockAncientWalls("blackbrickwalls2", "ancient/blackbrickwalls2", blackbricks2, SoundType.STONE, 3, 20, 10);
        blackplanks = new BlockBasic("blackplanks", "ancient/blackplanks", Material.WOOD, SoundType.WOOD, 2, 10, 10);
        blackplankstairs = new BlockAncientStairs("blackplankstairs", "ancient/blackplankstairs", blackplanks, SoundType.WOOD, 2, 10, 10);
        blackplankslabs = new BlockAncientSlabs("blackplankslabs", "ancient/blackplankslabs",Material.WOOD, SoundType.WOOD, 2, 10, 10);
        blackplankfences = new BlockAncientFences("blackplankfences", "ancient/blackplankfences",Material.WOOD,MapColor.WOOD, SoundType.WOOD, 2, 10, 10);

        crystalCluster = new BlockCrystalClusterBasic("crystalcluster", "crystalcluster");
        machineBase = new BlockMachineBase("machinebase", "machinebase");
        enchantmod = new BlockEnchantMod("enchantmod","enchantmod");
        crystalcrusher = new BlockCrystalCrusher("crystalcrusher","crystalcrusher");
        crystalfurnace = new BlockCrystalFurnace("crystalfurnace","crystalfurnace");
        blockTrap = new BlockTrap("blocktrap","blocktrap");

        crate1 = new BlockCrate("crate1","crate1");
        pot1 = new BlockPot("pot1","pot1");
        path1 = new BlockPath("path1","path1");
        path2 = new BlockPath("path2","path2");
        path3 = new BlockPath("path3","path3");
        path4 = new BlockPath("path4","path4");
        farmland = new BlockEnhancedFarmland("farmland","farmland");
        darksoil = new BlockNaturalSpawns("darksoil","darksoil");
        darksoilbase = new BlockBasic("darksoilbase","darksoilbase",Material.GROUND,SoundType.GROUND,3,20,0);
        spike1 = new BlockSpike("spike1","spike1");
        spike2 = new BlockSpike("spike2","spike2");
        spike3 = new BlockSpike("spike3","spike3");
        spike4 = new BlockSpike("spike4","spike4");
        spike5 = new BlockSpike("spike5","spike5");
        voidpot = new BlockVoidPot("voidpot","voidpot");

        pedestalstone = new BlockPedestal("pedestal_stone","pedestal_stone");
        pedestalred = new BlockPedestal("pedestal_red","pedestal_red");
        pedestalblue = new BlockPedestal("pedestal_blue","pedestal_blue");
        pedestalyellow = new BlockPedestal("pedestal_yellow","pedestal_yellow");
        pedestalpurple = new BlockPedestal("pedestal_purple","pedestal_purple");
        pedestalorange = new BlockPedestal("pedestal_orange","pedestal_orange");
        pedestalgreen = new BlockPedestal("pedestal_green","pedestal_green");
        pedestalwhite = new BlockPedestal("pedestal_white","pedestal_white");
        pedestalblack = new BlockPedestal("pedestal_black","pedestal_black");

        lootblockaircommon = new BlockLootBlock("lootblockaircommon","lootblockaircommon");
        lootblockairuncommon = new BlockLootBlock("lootblockairuncommon","lootblockairuncommon");
        lootblockairrare = new BlockLootBlock("lootblockairrare","lootblockairrare");
        lootblockairlegendary = new BlockLootBlock("lootblockairlegendary","lootblockairlegendary");
        lootblockairexotic = new BlockLootBlock("lootblockairexotic","lootblockairexotic");

        lootblockcommon = new BlockLootBlock("lootblockcommon","lootblockcommon");
        lootblockuncommon = new BlockLootBlock("lootblockuncommon","lootblockuncommon");
        lootblockrare = new BlockLootBlock("lootblockrare","lootblockrare");
        lootblocklegendary = new BlockLootBlock("lootblocklegendary","lootblocklegendary");
        lootblockexotic = new BlockLootBlock("lootblockexotic","lootblockexotic");
        lootblockpassivespawner = new BlockLootBlock("lootblockpassivespawner","lootblockpassivespawner");
        lootblockhostilespawner = new BlockLootBlock("lootblockhostilespawner","lootblockhostilespawner");
        lootblockpillar = new BlockLootBlock("lootblockpillar","lootblockpillar");
        lootblockcrystalcluster = new BlockLootBlock("lootblockcrystalcluster","lootblockcrystalcluster");

        charcoalRed = new BlockCharcoal("blockcharcoalred","blockcharcoalred");
        charcoalBlue = new BlockCharcoal("blockcharcoalblue","blockcharcoalblue");
        charcoalYellow = new BlockCharcoal("blockcharcoalyellow","blockcharcoalyellow");
        charcoalPurple = new BlockCharcoal("blockcharcoalpurple","blockcharcoalpurple");
        charcoalGreen = new BlockCharcoal("blockcharcoalgreen","blockcharcoalgreen");
        charcoalOrange = new BlockCharcoal("blockcharcoalorange","blockcharcoalorange");
        charcoalWhite = new BlockCharcoal("blockcharcoalwhite","blockcharcoalwhite");
        charcoalBlack = new BlockCharcoal("blockcharcoalblack","blockcharcoalblack");
    }

    public static void register()
    {
        registerBlock(redOre);
        registerBlock(blueOre);
        registerBlock(yellowOre);
        registerBlock(purpleOre);
        registerBlock(orangeOre);
        registerBlock(greenOre);
        registerBlock(whiteOre);
        registerBlock(blackOre);

        registerBlock(redCrystalTorch);
        registerBlock(blueCrystalTorch);
        registerBlock(yellowCrystalTorch);
        registerBlock(purpleCrystalTorch);
        registerBlock(greenCrystalTorch);
        registerBlock(orangeCrystalTorch);
        registerBlock(whiteCrystalTorch);
        registerBlock(blackCrystalTorch);

        registerBlock(logred);
        registerBlock(logblue);
        registerBlock(logyellow);
        registerBlock(logpurple);
        registerBlock(logorange);
        registerBlock(loggreen);
        registerBlock(logwhite);
        registerBlock(logblack);

        registerBlock(redCrystalFive);
        registerBlock(redCrystalFour);
        registerBlock(redCrystalThree);
        registerBlock(redCrystalTwo);
        registerBlock(redCrystalOne);

        registerBlock(blueCrystalFive);
        registerBlock(blueCrystalFour);
        registerBlock(blueCrystalThree);
        registerBlock(blueCrystalTwo);
        registerBlock(blueCrystalOne);

        registerBlock(yellowCrystalFive);
        registerBlock(yellowCrystalFour);
        registerBlock(yellowCrystalThree);
        registerBlock(yellowCrystalTwo);
        registerBlock(yellowCrystalOne);

        registerBlock(purpleCrystalFive);
        registerBlock(purpleCrystalFour);
        registerBlock(purpleCrystalThree);
        registerBlock(purpleCrystalTwo);
        registerBlock(purpleCrystalOne);

        registerBlock(orangeCrystalFive);
        registerBlock(orangeCrystalFour);
        registerBlock(orangeCrystalThree);
        registerBlock(orangeCrystalTwo);
        registerBlock(orangeCrystalOne);

        registerBlock(greenCrystalFive);
        registerBlock(greenCrystalFour);
        registerBlock(greenCrystalThree);
        registerBlock(greenCrystalTwo);
        registerBlock(greenCrystalOne);

        registerBlock(whiteCrystalFive);
        registerBlock(whiteCrystalFour);
        registerBlock(whiteCrystalThree);
        registerBlock(whiteCrystalTwo);
        registerBlock(whiteCrystalOne);

        registerBlock(blackCrystalFive);
        registerBlock(blackCrystalFour);
        registerBlock(blackCrystalThree);
        registerBlock(blackCrystalTwo);
        registerBlock(blackCrystalOne);



        registerBlock(leaf, new ItemBlockOre(leaf));

        registerBlock(redstone);
        registerBlock(redstonestairs);
        registerBlock(redstoneslabs);
        registerBlock(redstonefences);
        registerBlock(redstonewalls);
        registerBlock(redbricks);
        registerBlock(redbrickstairs);
        registerBlock(redbrickslabs);
        registerBlock(redbrickfences);
        registerBlock(redbrickwalls);
        registerBlock(redbricks2);
        registerBlock(redbrickstairs2);
        registerBlock(redbrickslabs2);
        registerBlock(redbrickfences2);
        registerBlock(redbrickwalls2);
        registerBlock(redplanks);
        registerBlock(redplankstairs);
        registerBlock(redplankslabs);
        registerBlock(redplankfences);
        registerBlock(bluestone);
        registerBlock(bluestonestairs);
        registerBlock(bluestoneslabs);
        registerBlock(bluestonefences);
        registerBlock(bluestonewalls);
        registerBlock(bluebricks);
        registerBlock(bluebrickstairs);
        registerBlock(bluebrickslabs);
        registerBlock(bluebrickfences);
        registerBlock(bluebrickwalls);
        registerBlock(bluebricks2);
        registerBlock(bluebrickstairs2);
        registerBlock(bluebrickslabs2);
        registerBlock(bluebrickfences2);
        registerBlock(bluebrickwalls2);
        registerBlock(blueplanks);
        registerBlock(blueplankstairs);
        registerBlock(blueplankslabs);
        registerBlock(blueplankfences);
        registerBlock(yellowstone);
        registerBlock(yellowstonestairs);
        registerBlock(yellowstoneslabs);
        registerBlock(yellowstonefences);
        registerBlock(yellowstonewalls);
        registerBlock(yellowbricks);
        registerBlock(yellowbrickstairs);
        registerBlock(yellowbrickslabs);
        registerBlock(yellowbrickfences);
        registerBlock(yellowbrickwalls);
        registerBlock(yellowbricks2);
        registerBlock(yellowbrickstairs2);
        registerBlock(yellowbrickslabs2);
        registerBlock(yellowbrickfences2);
        registerBlock(yellowbrickwalls2);
        registerBlock(yellowplanks);
        registerBlock(yellowplankstairs);
        registerBlock(yellowplankslabs);
        registerBlock(yellowplankfences);
        registerBlock(purplestone);
        registerBlock(purplestonestairs);
        registerBlock(purplestoneslabs);
        registerBlock(purplestonefences);
        registerBlock(purplestonewalls);
        registerBlock(purplebricks);
        registerBlock(purplebrickstairs);
        registerBlock(purplebrickslabs);
        registerBlock(purplebrickfences);
        registerBlock(purplebrickwalls);
        registerBlock(purplebricks2);
        registerBlock(purplebrickstairs2);
        registerBlock(purplebrickslabs2);
        registerBlock(purplebrickfences2);
        registerBlock(purplebrickwalls2);
        registerBlock(purpleplanks);
        registerBlock(purpleplankstairs);
        registerBlock(purpleplankslabs);
        registerBlock(purpleplankfences);
        registerBlock(orangestone);
        registerBlock(orangestonestairs);
        registerBlock(orangestoneslabs);
        registerBlock(orangestonefences);
        registerBlock(orangestonewalls);
        registerBlock(orangebricks);
        registerBlock(orangebrickstairs);
        registerBlock(orangebrickslabs);
        registerBlock(orangebrickfences);
        registerBlock(orangebrickwalls);
        registerBlock(orangebricks2);
        registerBlock(orangebrickstairs2);
        registerBlock(orangebrickslabs2);
        registerBlock(orangebrickfences2);
        registerBlock(orangebrickwalls2);
        registerBlock(orangeplanks);
        registerBlock(orangeplankstairs);
        registerBlock(orangeplankslabs);
        registerBlock(orangeplankfences);
        registerBlock(greenstone);
        registerBlock(greenstonestairs);
        registerBlock(greenstoneslabs);
        registerBlock(greenstonefences);
        registerBlock(greenstonewalls);
        registerBlock(greenbricks);
        registerBlock(greenbrickstairs);
        registerBlock(greenbrickslabs);
        registerBlock(greenbrickfences);
        registerBlock(greenbrickwalls);
        registerBlock(greenbricks2);
        registerBlock(greenbrickstairs2);
        registerBlock(greenbrickslabs2);
        registerBlock(greenbrickfences2);
        registerBlock(greenbrickwalls2);
        registerBlock(greenplanks);
        registerBlock(greenplankstairs);
        registerBlock(greenplankslabs);
        registerBlock(greenplankfences);
        registerBlock(whitestone);
        registerBlock(whitestonestairs);
        registerBlock(whitestoneslabs);
        registerBlock(whitestonefences);
        registerBlock(whitestonewalls);
        registerBlock(whitebricks);
        registerBlock(whitebrickstairs);
        registerBlock(whitebrickslabs);
        registerBlock(whitebrickfences);
        registerBlock(whitebrickwalls);
        registerBlock(whitebricks2);
        registerBlock(whitebrickstairs2);
        registerBlock(whitebrickslabs2);
        registerBlock(whitebrickfences2);
        registerBlock(whitebrickwalls2);
        registerBlock(whiteplanks);
        registerBlock(whiteplankstairs);
        registerBlock(whiteplankslabs);
        registerBlock(whiteplankfences);
        registerBlock(blackstone);
        registerBlock(blackstonestairs);
        registerBlock(blackstoneslabs);
        registerBlock(blackstonefences);
        registerBlock(blackstonewalls);
        registerBlock(blackbricks);
        registerBlock(blackbrickstairs);
        registerBlock(blackbrickslabs);
        registerBlock(blackbrickfences);
        registerBlock(blackbrickwalls);
        registerBlock(blackbricks2);
        registerBlock(blackbrickstairs2);
        registerBlock(blackbrickslabs2);
        registerBlock(blackbrickfences2);
        registerBlock(blackbrickwalls2);
        registerBlock(blackplanks);
        registerBlock(blackplankstairs);
        registerBlock(blackplankslabs);
        registerBlock(blackplankfences);

        registerBlock(crystalCluster);
        registerBlock(machineBase);
        registerBlock(enchantmod);
        registerBlock(crystalcrusher);
        registerBlock(crystalfurnace);
        registerBlock(blockTrap);

        registerBlock(saplingred);
        registerBlock(saplingblue);
        registerBlock(saplingyellow);
        registerBlock(saplingpurple);
        registerBlock(saplingorange);
        registerBlock(saplinggreen);
        registerBlock(saplingwhite);
        registerBlock(saplingblack);

        registerBlock(redDust);
        registerBlock(blueDust);
        registerBlock(yellowDust);
        registerBlock(purpleDust);
        registerBlock(orangeDust);
        registerBlock(greenDust);
        registerBlock(whiteDust);
        registerBlock(blackDust);
        registerBlock(blazeDust);
        registerBlock(carbonDust);
        registerBlock(ironDust);
        registerBlock(goldDust);
        registerBlock(redstoneDust);
        registerBlock(wheatDust);
        registerBlock(potatoDust);
        registerBlock(sugarDust);
        registerBlock(dustBlock);

        registerBlock(crate1);
        registerBlock(pot1);
        registerBlock(path1);
        registerBlock(path2);
        registerBlock(path3);
        registerBlock(path4);
        registerBlock(farmland);
        registerBlock(darksoil);
        registerBlock(darksoilbase);
        registerBlock(spike1);
        registerBlock(spike2);
        registerBlock(spike3);
        registerBlock(spike4);
        registerBlock(spike5);
        registerBlock(voidpot);

        registerBlock(pedestalstone);
        registerBlock(pedestalred);
        registerBlock(pedestalblue);
        registerBlock(pedestalyellow);
        registerBlock(pedestalpurple);
        registerBlock(pedestalorange);
        registerBlock(pedestalgreen);
        registerBlock(pedestalwhite);
        registerBlock(pedestalblack);

        registerBlock(lootblockaircommon);
        registerBlock(lootblockairuncommon);
        registerBlock(lootblockairrare);
        registerBlock(lootblockairlegendary);
        registerBlock(lootblockairexotic);
        registerBlock(lootblockcommon);
        registerBlock(lootblockuncommon);
        registerBlock(lootblockrare);
        registerBlock(lootblocklegendary);
        registerBlock(lootblockexotic);
        registerBlock(lootblockpassivespawner);
        registerBlock(lootblockhostilespawner);
        registerBlock(lootblockpillar);
        registerBlock(lootblockcrystalcluster);

        registerBlock(charcoalRed);
        registerBlock(charcoalBlue);
        registerBlock(charcoalYellow);
        registerBlock(charcoalPurple);
        registerBlock(charcoalGreen);
        registerBlock(charcoalOrange);
        registerBlock(charcoalWhite);
        registerBlock(charcoalBlack);
    }

    public static void registerRenders()
    {
        registerRender(redOre);
        registerRender(blueOre);
        registerRender(yellowOre);
        registerRender(purpleOre);
        registerRender(orangeOre);
        registerRender(greenOre);
        registerRender(whiteOre);
        registerRender(blackOre);

        registerRender(redCrystalTorch);
        registerRender(blueCrystalTorch);
        registerRender(yellowCrystalTorch);
        registerRender(purpleCrystalTorch);
        registerRender(greenCrystalTorch);
        registerRender(orangeCrystalTorch);
        registerRender(whiteCrystalTorch);
        registerRender(blackCrystalTorch);

        registerRenderLog(logred);
        registerRenderLog(logblue);
        registerRenderLog(logyellow);
        registerRenderLog(logpurple);
        registerRenderLog(logorange);
        registerRenderLog(loggreen);
        registerRenderLog(logwhite);
        registerRenderLog(logblack);

        registerRenderCrystal(redCrystalFive);
        registerRenderCrystal(redCrystalFour);
        registerRenderCrystal(redCrystalThree);
        registerRenderCrystal(redCrystalTwo);
        registerRenderCrystal(redCrystalOne);

        registerRenderCrystal(blueCrystalFive);
        registerRenderCrystal(blueCrystalFour);
        registerRenderCrystal(blueCrystalThree);
        registerRenderCrystal(blueCrystalTwo);
        registerRenderCrystal(blueCrystalOne);

        registerRenderCrystal(yellowCrystalFive);
        registerRenderCrystal(yellowCrystalFour);
        registerRenderCrystal(yellowCrystalThree);
        registerRenderCrystal(yellowCrystalTwo);
        registerRenderCrystal(yellowCrystalOne);

        registerRenderCrystal(purpleCrystalFive);
        registerRenderCrystal(purpleCrystalFour);
        registerRenderCrystal(purpleCrystalThree);
        registerRenderCrystal(purpleCrystalTwo);
        registerRenderCrystal(purpleCrystalOne);

        registerRenderCrystal(orangeCrystalFive);
        registerRenderCrystal(orangeCrystalFour);
        registerRenderCrystal(orangeCrystalThree);
        registerRenderCrystal(orangeCrystalTwo);
        registerRenderCrystal(orangeCrystalOne);

        registerRenderCrystal(greenCrystalFive);
        registerRenderCrystal(greenCrystalFour);
        registerRenderCrystal(greenCrystalThree);
        registerRenderCrystal(greenCrystalTwo);
        registerRenderCrystal(greenCrystalOne);

        registerRenderCrystal(whiteCrystalFive);
        registerRenderCrystal(whiteCrystalFour);
        registerRenderCrystal(whiteCrystalThree);
        registerRenderCrystal(whiteCrystalTwo);
        registerRenderCrystal(whiteCrystalOne);

        registerRenderCrystal(blackCrystalFive);
        registerRenderCrystal(blackCrystalFour);
        registerRenderCrystal(blackCrystalThree);
        registerRenderCrystal(blackCrystalTwo);
        registerRenderCrystal(blackCrystalOne);



        for (int i = 0; i < CrystalBlocks.CrystalLeaves.values().length; i++)
        {
            registerRender(leaf,i,"leaves_" + CrystalBlocks.CrystalLeaves.values()[i].getName());
        }

        registerRenderAncient(redstone);
        registerRenderAncient(redstonestairs);
        registerRenderAncient(redstoneslabs);
        registerRenderAncient(redstonefences);
        registerRenderAncient(redstonewalls);
        registerRenderAncient(redbricks);
        registerRenderAncient(redbrickstairs);
        registerRenderAncient(redbrickslabs);
        registerRenderAncient(redbrickfences);
        registerRenderAncient(redbrickwalls);
        registerRenderAncient(redbricks2);
        registerRenderAncient(redbrickstairs2);
        registerRenderAncient(redbrickslabs2);
        registerRenderAncient(redbrickfences2);
        registerRenderAncient(redbrickwalls2);
        registerRenderAncient(redplanks);
        registerRenderAncient(redplankstairs);
        registerRenderAncient(redplankslabs);
        registerRenderAncient(redplankfences);
        registerRenderAncient(bluestone);
        registerRenderAncient(bluestonestairs);
        registerRenderAncient(bluestoneslabs);
        registerRenderAncient(bluestonefences);
        registerRenderAncient(bluestonewalls);
        registerRenderAncient(bluebricks);
        registerRenderAncient(bluebrickstairs);
        registerRenderAncient(bluebrickslabs);
        registerRenderAncient(bluebrickfences);
        registerRenderAncient(bluebrickwalls);
        registerRenderAncient(bluebricks2);
        registerRenderAncient(bluebrickstairs2);
        registerRenderAncient(bluebrickslabs2);
        registerRenderAncient(bluebrickfences2);
        registerRenderAncient(bluebrickwalls2);
        registerRenderAncient(blueplanks);
        registerRenderAncient(blueplankstairs);
        registerRenderAncient(blueplankslabs);
        registerRenderAncient(blueplankfences);
        registerRenderAncient(yellowstone);
        registerRenderAncient(yellowstonestairs);
        registerRenderAncient(yellowstoneslabs);
        registerRenderAncient(yellowstonefences);
        registerRenderAncient(yellowstonewalls);
        registerRenderAncient(yellowbricks);
        registerRenderAncient(yellowbrickstairs);
        registerRenderAncient(yellowbrickslabs);
        registerRenderAncient(yellowbrickfences);
        registerRenderAncient(yellowbrickwalls);
        registerRenderAncient(yellowbricks2);
        registerRenderAncient(yellowbrickstairs2);
        registerRenderAncient(yellowbrickslabs2);
        registerRenderAncient(yellowbrickfences2);
        registerRenderAncient(yellowbrickwalls2);
        registerRenderAncient(yellowplanks);
        registerRenderAncient(yellowplankstairs);
        registerRenderAncient(yellowplankslabs);
        registerRenderAncient(yellowplankfences);
        registerRenderAncient(purplestone);
        registerRenderAncient(purplestonestairs);
        registerRenderAncient(purplestoneslabs);
        registerRenderAncient(purplestonefences);
        registerRenderAncient(purplestonewalls);
        registerRenderAncient(purplebricks);
        registerRenderAncient(purplebrickstairs);
        registerRenderAncient(purplebrickslabs);
        registerRenderAncient(purplebrickfences);
        registerRenderAncient(purplebrickwalls);
        registerRenderAncient(purplebricks2);
        registerRenderAncient(purplebrickstairs2);
        registerRenderAncient(purplebrickslabs2);
        registerRenderAncient(purplebrickfences2);
        registerRenderAncient(purplebrickwalls2);
        registerRenderAncient(purpleplanks);
        registerRenderAncient(purpleplankstairs);
        registerRenderAncient(purpleplankslabs);
        registerRenderAncient(purpleplankfences);
        registerRenderAncient(orangestone);
        registerRenderAncient(orangestonestairs);
        registerRenderAncient(orangestoneslabs);
        registerRenderAncient(orangestonefences);
        registerRenderAncient(orangestonewalls);
        registerRenderAncient(orangebricks);
        registerRenderAncient(orangebrickstairs);
        registerRenderAncient(orangebrickslabs);
        registerRenderAncient(orangebrickfences);
        registerRenderAncient(orangebrickwalls);
        registerRenderAncient(orangebricks2);
        registerRenderAncient(orangebrickstairs2);
        registerRenderAncient(orangebrickslabs2);
        registerRenderAncient(orangebrickfences2);
        registerRenderAncient(orangebrickwalls2);
        registerRenderAncient(orangeplanks);
        registerRenderAncient(orangeplankstairs);
        registerRenderAncient(orangeplankslabs);
        registerRenderAncient(orangeplankfences);
        registerRenderAncient(greenstone);
        registerRenderAncient(greenstonestairs);
        registerRenderAncient(greenstoneslabs);
        registerRenderAncient(greenstonefences);
        registerRenderAncient(greenstonewalls);
        registerRenderAncient(greenbricks);
        registerRenderAncient(greenbrickstairs);
        registerRenderAncient(greenbrickslabs);
        registerRenderAncient(greenbrickfences);
        registerRenderAncient(greenbrickwalls);
        registerRenderAncient(greenbricks2);
        registerRenderAncient(greenbrickstairs2);
        registerRenderAncient(greenbrickslabs2);
        registerRenderAncient(greenbrickfences2);
        registerRenderAncient(greenbrickwalls2);
        registerRenderAncient(greenplanks);
        registerRenderAncient(greenplankstairs);
        registerRenderAncient(greenplankslabs);
        registerRenderAncient(greenplankfences);
        registerRenderAncient(whitestone);
        registerRenderAncient(whitestonestairs);
        registerRenderAncient(whitestoneslabs);
        registerRenderAncient(whitestonefences);
        registerRenderAncient(whitestonewalls);
        registerRenderAncient(whitebricks);
        registerRenderAncient(whitebrickstairs);
        registerRenderAncient(whitebrickslabs);
        registerRenderAncient(whitebrickfences);
        registerRenderAncient(whitebrickwalls);
        registerRenderAncient(whitebricks2);
        registerRenderAncient(whitebrickstairs2);
        registerRenderAncient(whitebrickslabs2);
        registerRenderAncient(whitebrickfences2);
        registerRenderAncient(whitebrickwalls2);
        registerRenderAncient(whiteplanks);
        registerRenderAncient(whiteplankstairs);
        registerRenderAncient(whiteplankslabs);
        registerRenderAncient(whiteplankfences);
        registerRenderAncient(blackstone);
        registerRenderAncient(blackstonestairs);
        registerRenderAncient(blackstoneslabs);
        registerRenderAncient(blackstonefences);
        registerRenderAncient(blackstonewalls);
        registerRenderAncient(blackbricks);
        registerRenderAncient(blackbrickstairs);
        registerRenderAncient(blackbrickslabs);
        registerRenderAncient(blackbrickfences);
        registerRenderAncient(blackbrickwalls);
        registerRenderAncient(blackbricks2);
        registerRenderAncient(blackbrickstairs2);
        registerRenderAncient(blackbrickslabs2);
        registerRenderAncient(blackbrickfences2);
        registerRenderAncient(blackbrickwalls2);
        registerRenderAncient(blackplanks);
        registerRenderAncient(blackplankstairs);
        registerRenderAncient(blackplankslabs);
        registerRenderAncient(blackplankfences);

        registerRender(crystalCluster);
        registerRender(machineBase);
        registerRender(enchantmod);
        registerRender(crystalcrusher);
        registerRender(crystalfurnace);
        registerRender(blockTrap);

        registerRender(saplingred);
        registerRender(saplingblue);
        registerRender(saplingyellow);
        registerRender(saplingpurple);
        registerRender(saplingorange);
        registerRender(saplinggreen);
        registerRender(saplingwhite);
        registerRender(saplingblack);

        registerRender(redDust);
        registerRender(blueDust);
        registerRender(yellowDust);
        registerRender(purpleDust);
        registerRender(orangeDust);
        registerRender(greenDust);
        registerRender(whiteDust);
        registerRender(blackDust);
        registerRender(blazeDust);
        registerRender(carbonDust);
        registerRender(ironDust);
        registerRender(goldDust);
        registerRender(redstoneDust);
        registerRender(wheatDust);
        registerRender(potatoDust);
        registerRender(sugarDust);
        registerRender(dustBlock);

        registerRender(crate1);
        registerRender(pot1);
        registerRender(path1);
        registerRender(path2);
        registerRender(path3);
        registerRender(path4);
        registerRender(farmland);
        registerRender(darksoil);
        registerRender(darksoilbase);
        registerRender(spike1);
        registerRender(spike2);
        registerRender(spike3);
        registerRender(spike4);
        registerRender(spike5);
        registerRender(voidpot);

        registerRender(pedestalstone);
        registerRender(pedestalred);
        registerRender(pedestalblue);
        registerRender(pedestalyellow);
        registerRender(pedestalpurple);
        registerRender(pedestalorange);
        registerRender(pedestalgreen);
        registerRender(pedestalwhite);
        registerRender(pedestalblack);

        registerRender(lootblockaircommon);
        registerRender(lootblockairuncommon);
        registerRender(lootblockairrare);
        registerRender(lootblockairlegendary);
        registerRender(lootblockairexotic);
        registerRender(lootblockcommon);
        registerRender(lootblockuncommon);
        registerRender(lootblockrare);
        registerRender(lootblocklegendary);
        registerRender(lootblockexotic);
        registerRender(lootblockpassivespawner);
        registerRender(lootblockhostilespawner);
        registerRender(lootblockpillar);
        registerRender(lootblockcrystalcluster);

        registerRender(charcoalRed);
        registerRender(charcoalBlue);
        registerRender(charcoalYellow);
        registerRender(charcoalPurple);
        registerRender(charcoalGreen);
        registerRender(charcoalOrange);
        registerRender(charcoalWhite);
        registerRender(charcoalBlack);
    }

    public static void registerBlock(Block block)
    {
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    public static void registerBlock(Block block, ItemBlock itemBlock)
    {

        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(itemBlock.setRegistryName(block.getRegistryName()));
    }

    //Regular Block regRender
    public static void registerRender(Block block)
    {
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MODID, block.getUnlocalizedName().substring(5),"inventory")));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MODID, block.getUnlocalizedName().substring(5)), "inventory"));
    }

    //Special Package for Crystal Item Renders
    public static void registerRenderCrystal(Block block)
    {
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MODID, block.getUnlocalizedName().substring(5),"inventory")));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MODID,"crystals/" + block.getUnlocalizedName().substring(5)), "inventory"));
    }
    //Special Package for Crystal Item Renders
    public static void registerRenderLog(Block block)
    {
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MODID, block.getUnlocalizedName().substring(5),"inventory")));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MODID,"logs/" + block.getUnlocalizedName().substring(5)), "inventory"));
    }
    //Special Package for Ancient Item Renders
    public static void registerRenderAncient(Block block)
    {
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MODID, block.getUnlocalizedName().substring(5),"inventory")));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MODID,"ancient/" + block.getUnlocalizedName().substring(5)), "inventory"));
    }

    public static void registerRender(Block block, int meta, String fileName)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(new ResourceLocation(Reference.MODID, fileName), "inventory"));
    }

}