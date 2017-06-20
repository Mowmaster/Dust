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
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.redOre.getDefaultState(),9);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.yellowOre.getDefaultState(),6);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.orangeOre.getDefaultState(),9);
        this.theBiomeDecorator.lapisGen = new WorldGenMinable(BlockRegistry.whiteOre.getDefaultState(),1);

        this.theBiomeDecorator.cactiPerChunk = 2;
        this.theBiomeDecorator.deadBushPerChunk = 1;
    }


}
