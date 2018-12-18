package com.mowmaster.dust.blocks;

import com.mowmaster.dust.blocks.machines.*;
import com.mowmaster.dust.blocks.sapling.*;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;


public class BlockRegistry
{

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

    public static Block spike1;
    public static Block spike2;
    public static Block spike3;
    public static Block spike4;
    public static Block spike5;
    public static Block voidpot;

    public static void init() {

        BlockCrystalOre.BlockCrystalOreInit();
        BlockCrystalTorch.BlockCrystalTorchInit();
        BlockCharcoal.BlockCharcoalInit();
        BlockCrystal.Init();
        BlockDustSlab.Init();
        BlockDustLeaf.Init();
        BlockDustLog.BlockLogInit();
        BlockDustBasic.Init();
        BlockDustStair.Init();
        BlockDustFence.Init();
        BlockDustWall.Init();
        BlockDustCloud.Init();
        SaplingRegister.Init();
        BlockPedestal.Init();
        BlockLootBlock.Init();

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

        spike1 = new BlockSpike("spike1","spike1");
        spike2 = new BlockSpike("spike2","spike2");
        spike3 = new BlockSpike("spike3","spike3");
        spike4 = new BlockSpike("spike4","spike4");
        spike5 = new BlockSpike("spike5","spike5");
        voidpot = new BlockVoidPot("voidpot","voidpot");
    }

    public static void register()
    {
        BlockCrystalOre.BlockCrystalOreRegister();
        BlockCrystalTorch.BlockCrystalTorchRegister();
        BlockCharcoal.BlockCharcoalRegister();
        BlockDustLog.BlockLogRegister();
        BlockCrystal.Register();
        BlockDustLeaf.Register();
        BlockDustBasic.Register();
        BlockDustStair.Register();
        BlockDustSlab.Register();
        BlockDustFence.Init();
        BlockDustWall.Init();
        BlockLootBlock.Register();
        BlockDustCloud.Register();
        SaplingRegister.Register();
        BlockPedestal.Register();

        registerBlock(crystalCluster);
        registerBlock(machineBase);
        registerBlock(enchantmod);
        registerBlock(crystalcrusher);
        registerBlock(crystalfurnace);
        registerBlock(blockTrap);

        registerBlock(crate1);
        registerBlock(pot1);
        registerBlock(path1);
        registerBlock(path2);
        registerBlock(path3);
        registerBlock(path4);
        registerBlock(farmland);
        registerBlock(darksoil);

        registerBlock(spike1);
        registerBlock(spike2);
        registerBlock(spike3);
        registerBlock(spike4);
        registerBlock(spike5);
        registerBlock(voidpot);
    }

    public static void registerRenders()
    {

        BlockCrystalOre.BlockCrystalOreRegisterRender();
        BlockCrystalTorch.BlockCrystalTorchRegisterRender();
        BlockCharcoal.BlockCharcoalRegisterRender();
        BlockDustLog.BlockLogRegisterRender();
        BlockCrystal.RegisterRender();
        BlockDustLeaf.RegisterRender();
        BlockDustBasic.RegisterRender();
        BlockDustStair.RegisterRender();
        BlockDustSlab.RegisterRender();
        BlockDustFence.RegisterRender();
        BlockDustWall.RegisterRender();
        BlockLootBlock.RegisterRender();
        BlockDustCloud.RegisterRender();
        SaplingRegister.RegisterRender();
        BlockPedestal.RegisterRender();

        registerRender(crystalCluster);
        registerRender(machineBase);
        registerRender(enchantmod);
        registerRender(crystalcrusher);
        registerRender(crystalfurnace);
        registerRender(blockTrap);

        registerRender(crate1);
        registerRender(pot1);
        registerRender(path1);
        registerRender(path2);
        registerRender(path3);
        registerRender(path4);
        registerRender(farmland);
        registerRender(darksoil);

        registerRender(spike1);
        registerRender(spike2);
        registerRender(spike3);
        registerRender(spike4);
        registerRender(spike5);
        registerRender(voidpot);
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