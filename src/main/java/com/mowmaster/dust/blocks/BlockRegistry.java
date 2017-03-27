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
    public static Block redCrystalFive;


    public static void init()
    {
        redCrystalFive = new BlockCrystal("blockCrystalFive","blockCrystalFive");

        //redore = new BlockBasic("redore","redore");
        //oreyellow = new BlockWithMultipleStates("ore_yellow");



    }

    public static void register()
    {
        registerBlock(redCrystalFive);
        //registerBlock(redore);
        //registerBlock(oreyellow, new ItemBlockOre(oreyellow));
    }

    public static void registerRenders()
    {
        //registerRender(redore);
        registerRender(redCrystalFive);


        //for (int i = 0; i < CrystalBlocks.CrystalOres.values().length; i++){registerRender(oreyellow,i,"ore_yellow_" + CrystalBlocks.CrystalOres.values()[i].getName());}
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
