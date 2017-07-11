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

        this.theBiomeDecorator.coalGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(),8);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.redOre.getDefaultState(),6);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.blueOre.getDefaultState(),9);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.purpleOre.getDefaultState(),9);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.blackOre.getDefaultState(),1);

        this.theBiomeDecorator.grassPerChunk = 5;
        this.theBiomeDecorator.treesPerChunk = 1;
    }


}