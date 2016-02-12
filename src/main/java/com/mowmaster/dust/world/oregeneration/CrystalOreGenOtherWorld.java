package com.mowmaster.dust.world.oregeneration;


import com.mowmaster.dust.init.ModBlocks;
import com.mowmaster.dust.world.biomes.biomeCrystal;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class CrystalOreGenOtherWorld implements IWorldGenerator
{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        switch (world.provider.getDimensionId())
        {

            // Iron spawns as 8 size by 20 per chunk between 0 and 64
            case 0: //Overworld
                //this.runGenerator(this.gen_BlockOreRed, world, random, chunkX, chunkZ, 2,0,64);/*veins per chunk, min height, max height*/
               // this.runGenerator(this.gen_BlockOreBlue, world, random, chunkX, chunkZ, 4,0,64);
                //this.runGenerator(this.gen_BlockOreYellow, world, random, chunkX, chunkZ, 2,0,64);


               // this.runGenerator(this.gen_BlockOreGreen, world, random, chunkX, chunkZ, 4,0,32);
                //this.runGenerator(this.gen_BlockOreOrange, world, random, chunkX, chunkZ, 2,0,32);
               // this.runGenerator(this.gen_BlockOrePurple, world, random, chunkX, chunkZ, 4,0,32);

               // this.runGenerator(this.gen_BlockOreBlack, world, random, chunkX, chunkZ, 2,240,256);
               // this.runGenerator(this.gen_BlockOreWhite, world, random, chunkX, chunkZ, 2,128,256);// should be rare enough ;)
                break;
            // Quartz spawns as 13 size by 16 per chunk between 10 and 118
            case -1: //Nether
                  this.runGenerator(this.gen_BlockOreRedNether, world, random, chunkX, chunkZ, 6,0,128);/* Overworld spawns at 2 and nether at 6 to equal an average of 4 */
                  this.runGenerator(this.gen_BlockOreYellowNether, world, random, chunkX, chunkZ, 6,0,128);
                  this.runGenerator(this.gen_BlockOreOrangeNether, world, random, chunkX, chunkZ, 6,0,128);
                break;

            case 1: //End
                 this.runGenerator(this.gen_BlockEndOreBlack, world, random, chunkX, chunkZ, 8,0,128);/* Until 1.9 the end doesn't get bigger so the limit is end island*/
                break;

        }
    }

    //private WorldGenerator gen_BlockOreRed;
    //private WorldGenerator gen_BlockOreBlue;
    //private WorldGenerator gen_BlockOreYellow;
    //private WorldGenerator gen_BlockOreGreen;
    //private WorldGenerator gen_BlockOreOrange;
    //private WorldGenerator gen_BlockOrePurple;
    //private WorldGenerator gen_BlockOreBlack;
    //private WorldGenerator gen_BlockOreWhite;

     private WorldGenerator gen_BlockOreRedNether;
     private WorldGenerator gen_BlockOreYellowNether;
     private WorldGenerator gen_BlockOreOrangeNether;

     private WorldGenerator gen_BlockEndOreBlack;

    public CrystalOreGenOtherWorld()
    {
        //this.gen_BlockOreRed = new WorldGenMinable(ModBlocks.RedCrystalOre.getDefaultState(), 8/* vein size */);
        // ^Add back in later when i get biome specific per type
        //this.gen_BlockOreBlue = new WorldGenMinable(ModBlocks.BlueCrystalOre.getDefaultState(), 16);
        //this.gen_BlockOreYellow = new WorldGenMinable(ModBlocks.YellowCrystalOre.getDefaultState(), 30);
        // ^Add back in later when i get biome specific per type
        //this.gen_BlockOreGreen = new WorldGenMinable(ModBlocks.GreenCrystalOre.getDefaultState(), 16);
        //this.gen_BlockOreOrange = new WorldGenMinable(ModBlocks.OrangeCrystalOre.getDefaultState(), 30);
        // ^Add back in later when i get biome specific per type
        //this.gen_BlockOrePurple = new WorldGenMinable(ModBlocks.PurpleCrystalOre.getDefaultState(), 16);
        //this.gen_BlockOreBlack = new WorldGenMinable(ModBlocks.BlackCrystalOre.getDefaultState(), 6);
        //this.gen_BlockOreWhite = new WorldGenMinable(ModBlocks.WhiteCrystalOre.getDefaultState(), 12);


         this.gen_BlockOreRedNether = new WorldGenMinable(ModBlocks.RedCrystalNetherOre.getDefaultState(), 12, BlockHelper.forBlock(Blocks.netherrack));
         this.gen_BlockOreYellowNether = new WorldGenMinable(ModBlocks.YellowCrystalNetherOre.getDefaultState(), 12, BlockHelper.forBlock(Blocks.netherrack));
         this.gen_BlockOreOrangeNether = new WorldGenMinable(ModBlocks.OrangeCrystalNetherOre.getDefaultState(), 12, BlockHelper.forBlock(Blocks.netherrack));


         this.gen_BlockEndOreBlack = new WorldGenMinable(ModBlocks.BlackCrystalEndOre.getDefaultState(), 6, BlockHelper.forBlock(Blocks.end_stone));
    }

    private void runGenerator(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chanceToSpawn, int minHeight, int maxHeight)
    {




        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("FUUUUUUUUUCK YOU STEVEN!!!");
            /*"Illegal Height Arguments for WorldGenerator" side note: who's steven?*/

        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chanceToSpawn; i++)
        {
            int x = chunk_X * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunk_Z * 16 + rand.nextInt(16);
            generator.generate(world, rand, new BlockPos(x, y, z));
            //if (world.getBiomeGenForCoords(new BlockPos(x, y, z)).equals(biomeCrystal.getBiome(117))) {
            //    generator.generate(world, rand, new BlockPos(x, y, z));
            //}
        }

    }


}
