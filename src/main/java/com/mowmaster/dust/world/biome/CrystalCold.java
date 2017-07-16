package com.mowmaster.dust.world.biome;

import com.mowmaster.dust.blocks.BlockRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenMinable;

/**
 * Created by KingMowmaster on 5/20/2017.
 */
public class CrystalCold extends Biome
{
    public CrystalCold(BiomeProperties properties)
    {
        super(properties);
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        this.spawnableCreatureList.clear();
    }


}
