package com.mowmaster.dust.blocks;

import com.mowmaster.dust.blocks.buildingblocks.*;
import com.mowmaster.dust.blocks.crystal.BlockCrystal;
import com.mowmaster.dust.blocks.crystal.BlockCrystalCluster;
import com.mowmaster.dust.blocks.crystal.BlockCrystalOre;
import com.mowmaster.dust.blocks.machines.TierZero.CrystalCrusherBasic;
import com.mowmaster.dust.blocks.machines.TierZero.CrystalFurnaceBasic;
import com.mowmaster.dust.blocks.utility.*;
import com.mowmaster.dust.blocks.machines.*;
import com.mowmaster.dust.blocks.treebits.*;

public class BlockRegistry
{
    public static void init() {

        BlockCrystalOre.Init();
        BlockCrystal.Init();
        BlockCrystalCluster.Init();
        BlockDustCloud.Init();
        SaplingRegister.Init();
        BlockDustLeaf.Init();
        BlockDustLog.BlockLogInit();
        BlockCharcoal.Init();
        BlockDustBasicMeta.Init();
        BlockDustSlab.Init();
        BlockDustStair.Init();
        BlockDustFence.Init();
        BlockDustWall.Init();
        BlockCrystalTorch.Init();
        BlockPedestal.Init();
        BlockPath.Init();
        BlockEnchantedFarmland.Init();
        BlockDustBasic.Init();
        BlockMobSoil.Init();
        BlockSpike.Init();
        BlockTrap.Init();
        BlockMachineBase.Init();
        CrystalCrusherBasic.Init();
        CrystalFurnaceBasic.Init();
        BlockCrystalFurnace.Init();
        BlockEnchantMod.Init();
        BlockCrate.Init();
        BlockPot.Init();
        BlockVoidPot.Init();

        //Dust Creative
        BlockLootBlock.Init();
        BlockStructureSpawner.Init();
    }

    public static void register()
    {
        BlockCrystalOre.Register();
        BlockCrystal.Register();
        BlockCrystalCluster.Register();
        BlockDustCloud.Register();
        SaplingRegister.Register();
        BlockDustLeaf.Register();
        BlockDustLog.BlockLogRegister();
        BlockCharcoal.Register();
        BlockDustBasicMeta.Register();
        BlockDustSlab.Register();
        BlockDustStair.Register();
        BlockDustFence.Register();
        BlockDustWall.Register();
        BlockCrystalTorch.Register();
        BlockPedestal.Register();
        BlockPath.Register();
        BlockEnchantedFarmland.Register();
        BlockDustBasic.Register();
        BlockMobSoil.Register();
        BlockSpike.Register();
        BlockTrap.Register();
        BlockMachineBase.Register();
        CrystalCrusherBasic.Register();
        CrystalFurnaceBasic.Register();
        BlockCrystalFurnace.Register();
        BlockEnchantMod.Register();
        BlockCrate.Register();
        BlockPot.Register();
        BlockVoidPot.Register();
        BlockLootBlock.Register();
        BlockStructureSpawner.Register();
    }

    public static void registerRenders()
    {

        BlockCrystalOre.RegisterRender();
        BlockCrystalTorch.RegisterRender();
        BlockCharcoal.RegisterRender();
        BlockDustLog.BlockLogRegisterRender();
        BlockCrystal.RegisterRender();
        BlockDustLeaf.RegisterRender();
        BlockDustBasic.RegisterRender();
        BlockDustBasicMeta.RegisterRender();
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
        CrystalCrusherBasic.RegisterRender();
        CrystalFurnaceBasic.RegisterRender();
        BlockCrystalFurnace.RegisterRender();
        BlockTrap.RegisterRender();
        BlockEnchantedFarmland.RegisterRender();
        BlockMobSoil.RegisterRender();
        BlockVoidPot.RegisterRender();
        BlockCrystalCluster.RegisterRender();
        BlockMachineBase.RegisterRender();
        BlockEnchantMod.RegisterRender();
        BlockStructureSpawner.RegisterRender();
    }

}