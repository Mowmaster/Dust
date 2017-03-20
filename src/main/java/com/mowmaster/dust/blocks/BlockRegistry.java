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

    //public static Block redore;

    public static Block orered;
    public static Block oreblue;
    public static Block oreyellow;
    public static Block orepurple;
    public static Block oreorange;
    public static Block oregreen;
    public static Block orewhite;
    public static Block oreblack;

    public static void init()
    {
        //redore = new BlockBasic("redore","redore");
        orered = new BlockRedOre("ore_red");
        oreblue = new BlockBlueOre("ore_blue");
        oreyellow = new BlockYellowOre("ore_yellow");
        orepurple = new BlockPurpleOre("ore_purple");
        oreorange = new BlockOrangeOre("ore_orange");
        oregreen = new BlockGreenOre("ore_green");
        orewhite = new BlockWhiteOre("ore_white");
        oreblack = new BlockBlackOre("ore_black");



    }

    public static void register()
    {
        //registerBlock(redore);

        registerBlock(orered, new ItemBlockOre(orered));
        registerBlock(oreblue, new ItemBlockOre(oreblue));
        registerBlock(oreyellow, new ItemBlockOre(oreyellow));
        registerBlock(orepurple, new ItemBlockOre(orepurple));
        registerBlock(oreorange, new ItemBlockOre(oreorange));
        registerBlock(oregreen, new ItemBlockOre(oregreen));
        registerBlock(orewhite, new ItemBlockOre(orewhite));
        registerBlock(oreblack, new ItemBlockOre(oreblack));
    }

    public static void registerRenders()
    {
        //registerRender(redore);

        for (int i = 0; i < CrystalBlocks.CrystalOres.values().length; i++)
        {
            registerRender(orered,i,"ore_red_" + CrystalBlocks.CrystalOres.values()[i].getName());
            registerRender(oreblue,i,"ore_blue_" + CrystalBlocks.CrystalOres.values()[i].getName());
            registerRender(oreyellow,i,"ore_yellow_" + CrystalBlocks.CrystalOres.values()[i].getName());
            registerRender(orepurple,i,"ore_purple_" + CrystalBlocks.CrystalOres.values()[i].getName());
            registerRender(oreorange,i,"ore_orange_" + CrystalBlocks.CrystalOres.values()[i].getName());
            registerRender(oregreen,i,"ore_green_" + CrystalBlocks.CrystalOres.values()[i].getName());
            registerRender(orewhite,i,"ore_white_" + CrystalBlocks.CrystalOres.values()[i].getName());
            registerRender(oreblack,i,"ore_black_" + CrystalBlocks.CrystalOres.values()[i].getName());
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

    public static void registerRender(Block block)
    {
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MODID, block.getUnlocalizedName().substring(5),"inventory")));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MODID, block.getUnlocalizedName().substring(5)), "inventory"));
    }


    public static void registerRender(Block block, int meta, String fileName)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(new ResourceLocation(Reference.MODID, fileName), "inventory"));
    }

}
