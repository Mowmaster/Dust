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

        this.theBiomeDecorator.coalGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(),8);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.redOre.getDefaultState(),3);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.blueOre.getDefaultState(),3);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.yellowOre.getDefaultState(),3);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.purpleOre.getDefaultState(),3);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.orangeOre.getDefaultState(),3);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.greenOre.getDefaultState(),3);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.whiteOre.getDefaultState(),1);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.blackOre.getDefaultState(),1);

        this.theBiomeDecorator.grassPerChunk = 5;
        this.theBiomeDecorator.treesPerChunk = 1;
    }


}