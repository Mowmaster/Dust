package com.mowmaster.dust.init;

import com.mowmaster.dust.block.BlockDust;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModBlocks
{
    public static Block RedCrystalOre;

    public static void dustBlocks()
    {
        GameRegistry.registerBlock(RedCrystalOre = new BlockDust("crystalore_red"), "crystalore_red");
    }

}
