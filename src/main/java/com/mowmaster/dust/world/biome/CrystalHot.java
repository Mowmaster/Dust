package com.mowmaster.dust.world.biome;

import com.mowmaster.dust.blocks.BlockRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenMinable;

/**
 * Created by KingMowmaster on 5/20/2017.
 */
public class CrystalHot extends Biome
{
    public CrystalHot(BiomeProperties properties)
    {
        super(properties);
        this.topBlock = Blocks.SAND.getDefaultState();
        this.fillerBlock = Blocks.SAND.getDefaultState();
        this.spawnableCreatureList.clear();
    }


}
