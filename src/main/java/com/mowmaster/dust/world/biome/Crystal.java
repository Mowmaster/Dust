package com.mowmaster.dust.world.biome;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

/**
 * Created by KingMowmaster on 5/20/2017.
 */
public class Crystal extends Biome
{
    public Crystal(BiomeProperties properties)
    {
        super(properties);
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        this.spawnableCreatureList.clear();
    }


}
