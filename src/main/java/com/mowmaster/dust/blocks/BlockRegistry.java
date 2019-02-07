package com.mowmaster.dust.blocks;

import com.mowmaster.dust.blocks.buildingblocks.*;
import com.mowmaster.dust.blocks.crystal.BlockCrystal;
import com.mowmaster.dust.blocks.crystal.BlockCrystalCluster;
import com.mowmaster.dust.blocks.crystal.BlockCrystalOre;
import com.mowmaster.dust.blocks.machines.TierZero.CrystalCrusherBasic;
import com.mowmaster.dust.blocks.machines.TierZero.CrystalFurnaceBasic;
import com.mowmaster.dust.blocks.utility.BlockEnchantedFarmland;
import com.mowmaster.dust.blocks.utility.BlockMobSoil;
import com.mowmaster.dust.blocks.utility.BlockPath;
import com.mowmaster.dust.blocks.utility.BlockSpike;
import com.mowmaster.dust.blocks.machines.*;
import com.mowmaster.dust.blocks.treebits.*;

public class BlockRegistry
{
    public static void init() {

        BlockCrystalOre.Init();
        BlockCrystalTorch.Init();
        BlockCharcoal.Init();
        BlockCrystal.Init();
        BlockDustSlab.Init();
        BlockDustLeaf.Init();
        BlockDustLog.BlockLogInit();
        BlockDustBasic.Init();
        BlockDustBasicMeta.Init();
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
        CrystalCrusherBasic.Init();
        CrystalFurnaceBasic.Init();
        BlockCrystalFurnace.Init();
        BlockTrap.Init();
        BlockEnchantedFarmland.Init();
        BlockMobSoil.Init();
        BlockVoidPot.Init();
        BlockCrystalCluster.Init();
        BlockMachineBase.Init();
        BlockEnchantMod.Init();
    }

    public static void register()
    {
        BlockCrystalOre.Register();
        BlockCrystalTorch.Register();
        BlockCharcoal.Register();
        BlockDustLog.BlockLogRegister();
        BlockCrystal.Register();
        BlockDustLeaf.Register();
        BlockDustBasic.Register();
        BlockDustBasicMeta.Register();
        BlockDustStair.Register();
        BlockDustSlab.Register();
        BlockDustFence.Register();
        BlockDustWall.Register();
        BlockLootBlock.Register();
        BlockDustCloud.Register();
        SaplingRegister.Register();
        BlockPedestal.Register();
        BlockCrate.Register();
        BlockPot.Register();
        BlockPath.Register();
        BlockSpike.Register();
        CrystalCrusherBasic.Register();
        CrystalFurnaceBasic.Register();
        BlockCrystalFurnace.Register();
        BlockTrap.Register();
        BlockEnchantedFarmland.Register();
        BlockMobSoil.Register();
        BlockVoidPot.Register();
        BlockCrystalCluster.Register();
        BlockMachineBase.Register();
        BlockEnchantMod.Register();
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
    }

}