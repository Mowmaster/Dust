package com.mowmaster.dust.blocks.treebits;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SaplingRegister
{
    public static Block saplingred;
    public static Block saplingblue;
    public static Block saplingyellow;
    public static Block saplingpurple;
    public static Block saplingorange;
    public static Block saplinggreen;
    public static Block saplingwhite;
    public static Block saplingblack;

    public static void Init()
    {
        saplingred = new SaplingRed("saplingred","red/saplingred");
        saplingblue = new SaplingBlue("saplingblue","blue/saplingblue");
        saplingyellow = new SaplingYellow("saplingyellow","yellow/saplingyellow");
        saplingpurple = new SaplingPurple("saplingpurple","purple/saplingpurple");
        saplingorange = new SaplingOrange("saplingorange","orange/saplingorange");
        saplinggreen = new SaplingGreen("saplinggreen","green/saplinggreen");
        saplingwhite = new SaplingWhite("saplingwhite","white/saplingwhite");
        saplingblack = new SaplingBlack("saplingblack","black/saplingblack");
    }

    public static void Register()
    {
        registerBlock(saplingred);
        registerBlock(saplingblue);
        registerBlock(saplingyellow);
        registerBlock(saplingpurple);
        registerBlock(saplingorange);
        registerBlock(saplinggreen);
        registerBlock(saplingwhite);
        registerBlock(saplingblack);
    }

    public static void RegisterRender()
    {
        registerRender(saplingred);
        registerRender(saplingblue);
        registerRender(saplingyellow);
        registerRender(saplingpurple);
        registerRender(saplingorange);
        registerRender(saplinggreen);
        registerRender(saplingwhite);
        registerRender(saplingblack);
    }

    public static void registerBlock(Block block)
    {
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    public static void registerRender(Block block)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MODID, block.getUnlocalizedName().substring(5)), "inventory"));
    }
}
