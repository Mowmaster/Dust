package com.mowmaster.dust.blocks;

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
        BlockCrate.Init();
        BlockPot.Init();
        BlockPath.Init();
        BlockSpike.Init();
        BlockCrystalCrusher.Init();
        BlockCrystalFurnace.Init();
        BlockTrap.Init();
        BlockEnchantedFarmland.Init();
        BlockNaturalSpawns.Init();
        BlockVoidPot.Init();
        BlockCrystalClusterBasic.Init();
        BlockMachineBase.Init();
        BlockEnchantMod.Init();
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
        BlockCrate.Register();
        BlockPot.Register();
        BlockPath.Register();
        BlockSpike.Register();
        BlockCrystalCrusher.Register();
        BlockCrystalFurnace.Register();
        BlockTrap.Register();
        BlockEnchantedFarmland.Register();
        BlockNaturalSpawns.Register();
        BlockVoidPot.Register();
        BlockCrystalClusterBasic.Register();
        BlockMachineBase.Register();
        BlockEnchantMod.Register();
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
        BlockCrate.RegisterRender();
        BlockPot.RegisterRender();
        BlockPath.RegisterRender();
        BlockSpike.RegisterRender();
        BlockCrystalCrusher.RegisterRender();
        BlockCrystalFurnace.RegisterRender();
        BlockTrap.RegisterRender();
        BlockEnchantedFarmland.RegisterRender();
        BlockNaturalSpawns.RegisterRender();
        BlockVoidPot.RegisterRender();
        BlockCrystalClusterBasic.RegisterRender();
        BlockMachineBase.RegisterRender();
        BlockEnchantMod.RegisterRender();
    }

}