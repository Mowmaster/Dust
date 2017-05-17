package com.mowmaster.dust.blocks;

import com.mowmaster.dust.blocks.item.ItemBlockOre;
import com.mowmaster.dust.enums.CrystalBlocks;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;


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




    public static void init()
    {
        redOre = new BlockBasic("redore","redore");
        blueOre = new BlockBasic("blueore","blueore");
        yellowOre = new BlockBasic("yellowore","yellowore");
        purpleOre = new BlockBasic("purpleore","purpleore");
        orangeOre = new BlockBasic("orangeore","orangeore");
        greenOre = new BlockBasic("greenore","greenore");
        whiteOre = new BlockBasic("whiteore","whiteore");
        blackOre = new BlockBasic("blackore","blackore");

        logred = new BlockLog("log_red","red/log_red");
        logblue = new BlockLog("log_blue","blue/log_blue");
        logyellow = new BlockLog("log_yellow","yellow/log_yellow");
        logpurple = new BlockLog("log_purple","purple/log_purple");
        logorange = new BlockLog("log_orange","orange/log_orange");
        loggreen = new BlockLog("log_green","green/log_green");
        logwhite = new BlockLog("log_white","white/log_white");
        logblack = new BlockLog("log_black","black/log_black");

        redCrystalFive = new BlockCrystal("blockredcrystalfive","red/blockredcrystalfive");
        redCrystalFour = new BlockCrystal("blockredcrystalfour","red/blockredcrystalfour");
        redCrystalThree = new BlockCrystal("blockredcrystalthree","red/blockredcrystalthree");
        redCrystalTwo = new BlockCrystal("blockredcrystaltwo","red/blockredcrystaltwo");
        redCrystalOne = new BlockCrystal("blockredcrystalone","red/blockredcrystalone");

        blueCrystalFive = new BlockCrystal("blockbluecrystalfive","blue/blockbluecrystalfive");
        blueCrystalFour = new BlockCrystal("blockbluecrystalfour","blue/blockbluecrystalfour");
        blueCrystalThree = new BlockCrystal("blockbluecrystalthree","blue/blockbluecrystalthree");
        blueCrystalTwo = new BlockCrystal("blockbluecrystaltwo","blue/blockbluecrystaltwo");
        blueCrystalOne = new BlockCrystal("blockbluecrystalone","blue/blockbluecrystalone");

        yellowCrystalFive = new BlockCrystal("blockyellowcrystalfive","yellow/blockyellowcrystalfive");
        yellowCrystalFour = new BlockCrystal("blockyellowcrystalfour","yellow/blockyellowcrystalfour");
        yellowCrystalThree = new BlockCrystal("blockyellowcrystalthree","yellow/blockyellowcrystalthree");
        yellowCrystalTwo = new BlockCrystal("blockyellowcrystaltwo","yellow/blockyellowcrystaltwo");
        yellowCrystalOne = new BlockCrystal("blockyellowcrystalone","yellow/blockyellowcrystalone");

        purpleCrystalFive = new BlockCrystal("blockpurplecrystalfive","purple/blockpurplecrystalfive");
        purpleCrystalFour = new BlockCrystal("blockpurplecrystalfour","purple/blockpurplecrystalfour");
        purpleCrystalThree = new BlockCrystal("blockpurplecrystalthree","purple/blockpurplecrystalthree");
        purpleCrystalTwo = new BlockCrystal("blockpurplecrystaltwo","purple/blockpurplecrystaltwo");
        purpleCrystalOne = new BlockCrystal("blockpurplecrystalone","purple/blockpurplecrystalone");

        orangeCrystalFive = new BlockCrystal("blockorangecrystalfive","orange/blockorangecrystalfive");
        orangeCrystalFour = new BlockCrystal("blockorangecrystalfour","orange/blockorangecrystalfour");
        orangeCrystalThree = new BlockCrystal("blockorangecrystalthree","orange/blockorangecrystalthree");
        orangeCrystalTwo = new BlockCrystal("blockorangecrystaltwo","orange/blockorangecrystaltwo");
        orangeCrystalOne = new BlockCrystal("blockorangecrystalone","orange/blockorangecrystalone");

        greenCrystalFive = new BlockCrystal("blockgreencrystalfive","green/blockgreencrystalfive");
        greenCrystalFour = new BlockCrystal("blockgreencrystalfour","green/blockgreencrystalfour");
        greenCrystalThree = new BlockCrystal("blockgreencrystalthree","green/blockgreencrystalthree");
        greenCrystalTwo = new BlockCrystal("blockgreencrystaltwo","green/blockgreencrystaltwo");
        greenCrystalOne = new BlockCrystal("blockgreencrystalone","green/blockgreencrystalone");

        whiteCrystalFive = new BlockCrystal("blockwhitecrystalfive","white/blockwhitecrystalfive");
        whiteCrystalFour = new BlockCrystal("blockwhitecrystalfour","white/blockwhitecrystalfour");
        whiteCrystalThree = new BlockCrystal("blockwhitecrystalthree","white/blockwhitecrystalthree");
        whiteCrystalTwo = new BlockCrystal("blockwhitecrystaltwo","white/blockwhitecrystaltwo");
        whiteCrystalOne = new BlockCrystal("blockwhitecrystalone","white/blockWhiteCrystalOne");

        blackCrystalFive = new BlockCrystal("blockblackcrystalfive","black/blockblackcrystalfive");
        blackCrystalFour = new BlockCrystal("blockblackcrystalfour","black/blockblackcrystalfour");
        blackCrystalThree = new BlockCrystal("blockblackcrystalthree","black/blockblackcrystalthree");
        blackCrystalTwo = new BlockCrystal("blockblackcrystaltwo","black/blockblackcrystaltwo");
        blackCrystalOne = new BlockCrystal("blockblackcrystalone","black/blockblackcrystalone");

        leaf = new BlockLeaf("leaves");





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

    }

    public static void registerBlock(Block block)
    {
        GameRegistry.register(block);
        GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    public static void registerBlock(Block block, ItemBlock itemBlock)
    {
        GameRegistry.register(block);
        GameRegistry.register(itemBlock.setRegistryName(block.getRegistryName()));
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

    public static void registerRender(Block block, int meta, String fileName)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(new ResourceLocation(Reference.MODID, fileName), "inventory"));
    }

}
