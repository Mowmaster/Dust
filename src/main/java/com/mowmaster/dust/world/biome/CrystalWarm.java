package com.mowmaster.dust.world.biome;

import com.mowmaster.dust.blocks.BlockRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenMinable;

/**
 * Created by KingMowmaster on 5/20/2017.
 */
public class CrystalWarm extends Biome
{
    public CrystalWarm(BiomeProperties properties)
    {
        super(properties);
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        this.spawnableCreatureList.clear();

        this.theBiomeDecorator.coalGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(),8);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.blueOre.getDefaultState(),6);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.yellowOre.getDefaultState(),9);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.greenOre.getDefaultState(),9);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.whiteOre.getDefaultState(),1);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.blackOre.getDefaultState(),1);

        this.theBiomeDecorator.grassPerChunk = 5;
    }


}
