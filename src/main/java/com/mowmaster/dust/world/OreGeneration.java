package com.mowmaster.dust.world;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.world.structures.JsonParseTester;
import com.mowmaster.dust.world.structures.SmallPiller;
import com.mowmaster.dust.world.structures.smallwell;
import com.mowmaster.dust.world.treegeneration.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.ArrayList;
import java.util.Random;

import static com.mowmaster.dust.blocks.BlockRegistry.*;
import static com.mowmaster.dust.world.biome.BiomeRegistry.*;

public class OreGeneration implements IWorldGenerator
{

    int randomnum = 9;//Biome Specific Specialized Biomes
    int randomworld = 19; //All Overworld Biomes
    int count = 3;// How many in a cluster
    private WorldGenerator red_ore;
    private WorldGenerator blue_ore;
    private WorldGenerator yellow_ore;
    private WorldGenerator purple_ore;
    private WorldGenerator orange_ore;
    private WorldGenerator green_ore;
    private WorldGenerator white_ore;
    private WorldGenerator black_ore;

    private TreeRed smalltreered;
    private TreeBlue smalltreeblue;
    private TreeYellow smalltreeyellow;
    private TreePurple smalltreepurple;
    private TreeOrange smalltreeorange;
    private TreeGreen smalltreegreen;
    private TreeWhite smalltreewhite;
    private TreeBlack smalltreeblack;

    private SmallPiller smallpiller;
    private smallwell smallwell;



    public OreGeneration()
    {

        red_ore = new WorldGenMinable(redOre.getDefaultState(),count);
        blue_ore = new WorldGenMinable(blueOre.getDefaultState(),count);
        yellow_ore = new WorldGenMinable(yellowOre.getDefaultState(),count);
        purple_ore = new WorldGenMinable(purpleOre.getDefaultState(),count);
        orange_ore = new WorldGenMinable(orangeOre.getDefaultState(),count);
        green_ore = new WorldGenMinable(greenOre.getDefaultState(),count);
        white_ore = new WorldGenMinable(whiteOre.getDefaultState(),count);
        black_ore = new WorldGenMinable(blackOre.getDefaultState(),count);

        smalltreered = new TreeRed(true);
        smalltreeblue = new TreeBlue(true);
        smalltreeyellow = new TreeYellow(true);
        smalltreepurple = new TreePurple(true);
        smalltreeorange = new TreeOrange(true);
        smalltreegreen = new TreeGreen(true);
        smalltreewhite = new TreeWhite(true);
        smalltreeblack = new TreeBlack(true);

        smallpiller = new SmallPiller();
        smallwell = new smallwell();
    }


    private void runWorldGenerator(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");
        if (rand.nextInt(randomworld) == 1) {
            int heightDiff = maxHeight - minHeight + 1;
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(16);
                int y = minHeight + rand.nextInt(heightDiff);
                int z = chunk_Z*16 + rand.nextInt(16);
                generator.generate(world, rand, new BlockPos(x, y, z));
            }
        }
    }

    private void runGenerator(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

            if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
                throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

                int heightDiff = maxHeight - minHeight + 1;
                for (int i = 0; i < chancesToSpawn; i++) {
                    int x = chunk_X*16 + rand.nextInt(16);
                    int y = minHeight + rand.nextInt(heightDiff);
                    int z = chunk_Z*16 + rand.nextInt(16);
                    if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_hot) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_warm) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_cold))
                    {generator.generate(world, rand, new BlockPos(x, y, z));}
                    }
    }

    private void runTreeGenerator(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(randomnum) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(9)+ 3;
                int y = world.getHeight();
                int z = chunk_Z*16 + rand.nextInt(9)+ 3;

                if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_hot) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_warm) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_cold)) {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}
            }
        }
    }


    private void runStructureGeneratorFiveByFive(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

    if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
        throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

    if (rand.nextInt(4) <= 1) {
        for (int i = 0; i < chancesToSpawn; i++) {
            int x = chunk_X*16 + rand.nextInt(9)+ 3;
            int y = world.getHeight(chunk_X,chunk_Z) + 1;
            int z = chunk_Z*16 + rand.nextInt(9)+ 3;
            generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));
            //Once this is good the below is biome specific
            //if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_hot) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_warm) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_cold)) {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}

        }
    }
}

    private void runStructureGeneratorOneByOne(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(4) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(16);
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + rand.nextInt(16);
                generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));
                //Once this is good the below is biome specific
                //if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_hot) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_warm) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_cold)) {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}

            }
        }
    }

    /*
    private void runGenerator(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

            if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
                throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

            if (rand.nextInt(randomnum) == 1) {
                int heightDiff = maxHeight - minHeight + 1;
                for (int i = 0; i < chancesToSpawn; i++) {
                    int x = chunk_X * 16 + rand.nextInt(16);
                    int y = minHeight + rand.nextInt(heightDiff);
                    int z = chunk_Z * 16 + rand.nextInt(16);
                    generator.generate(world, rand, new BlockPos(x, y, z));
                }
            }

    }

    private void generateTree(WorldGenerator trees, World world, Random rand, int chunkX, int chunkZ){
            int x = (chunkX * 16) + rand.nextInt(16);
            int z = (chunkZ * 16) + rand.nextInt(16);
            BlockPos y = world.getHeight(new BlockPos(x, 0, z));
        

            new TreeRed(true).generate(world, rand, y);
            new TreeBlue(true).generate(world, rand, y);
            new TreeYellow(true).generate(world, rand, y);
            new TreePurple(true).generate(world, rand, y);
            new TreeOrange(true).generate(world, rand, y);
            new TreeGreen(true).generate(world, rand, y);
            new TreeWhite(true).generate(world, rand, y);
            new TreeBlack(true).generate(world, rand, y);
    }
    */





    //{smalltreered,smalltreeblue,smalltreeyellow,smalltreepurple,smalltreeorange,smalltreegreen,smalltreewhite,smalltreeblack}
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {



        switch(world.provider.getDimension())
        {

            case 0: //Overworld

                //                         Chance to spawn,min height, max height

                this.runWorldGenerator(red_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runWorldGenerator(blue_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runWorldGenerator(yellow_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runWorldGenerator(purple_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runWorldGenerator(orange_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runWorldGenerator(green_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runWorldGenerator(white_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runWorldGenerator(black_ore,world,random,chunkX,chunkZ,1,0,20);

                this.runGenerator(red_ore,world,random,chunkX,chunkZ,5,0,60);
                this.runGenerator(blue_ore,world,random,chunkX,chunkZ,5,0,60);
                this.runGenerator(yellow_ore,world,random,chunkX,chunkZ,5,0,60);
                this.runGenerator(purple_ore,world,random,chunkX,chunkZ,3,0,30);
                this.runGenerator(orange_ore,world,random,chunkX,chunkZ,3,0,30);
                this.runGenerator(green_ore,world,random,chunkX,chunkZ,3,0,30);
                this.runGenerator(white_ore,world,random,chunkX,chunkZ,1,0,10);
                this.runGenerator(black_ore,world,random,chunkX,chunkZ,1,0,10);

                this.runTreeGenerator(smalltreered, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreeblue, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreeyellow, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreepurple, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreeorange, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreegreen, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreewhite, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreeblack, world, random, chunkX, chunkZ,1,0,20);

                int getrando = random.nextInt(1);
                switch (getrando)
                {
                    case 0: this.runStructureGeneratorOneByOne(smallpiller,world,random,chunkX,chunkZ,1,0,20);
                    case 1: this.runStructureGeneratorFiveByFive(smallwell,world,random,chunkX,chunkZ,1,0,20);
                }






            /*
            case 1://End
            case -1://Nether
            case 6://Aroma Mining Dim
                this.runGenerator(red_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runGenerator(blue_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runGenerator(yellow_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runGenerator(purple_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runGenerator(orange_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runGenerator(green_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runGenerator(white_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runGenerator(black_ore,world,random,chunkX,chunkZ,1,0,20);
                */
        }
    }

}
