package com.mowmaster.dust.world.oregeneration;


import com.mowmaster.dust.init.ModBlocks;
import com.mowmaster.dust.world.biomes.biomeCrystal;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class CrystalOreGenBlue implements IWorldGenerator
{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        switch (world.provider.getDimensionId())
        {
            case 0: //Overworld
                this.runGenerator(this.gen_BlockOreBlue, world, random, chunkX, chunkZ, 32,1,256);
                break;
            case -1: //Nether
                break;

            case 1: //End
                break;

        }
    }
    private WorldGenerator gen_BlockOreBlue;

    public CrystalOreGenBlue()
    {
        this.gen_BlockOreBlue = new WorldGenMinable(ModBlocks.BlueCrystalOre.getDefaultState(), 16);
    }

    private void runGenerator(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chanceToSpawn, int minHeight, int maxHeight)
    {




            if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
                throw new IllegalArgumentException("FUUUUUUUUUCK YOU STEVEN!!!");

            int heightDiff = maxHeight - minHeight + 1;
            for (int i = 0; i < chanceToSpawn; i++)
            {
                int x = chunk_X * 16 + rand.nextInt(16);
                int y = minHeight + rand.nextInt(heightDiff);
                int z = chunk_Z * 16 + rand.nextInt(16);
                if (world.getBiomeGenForCoords(new BlockPos(x, y, z)).equals(biomeCrystal.getBiome(119))) {
                    generator.generate(world, rand, new BlockPos(x, y, z));
                }
            }

    }


}
