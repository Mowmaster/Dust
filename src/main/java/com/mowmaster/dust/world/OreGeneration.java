package com.mowmaster.dust.world;

import com.mowmaster.dust.world.structures.*;
import com.mowmaster.dust.world.structures.allbiomestructures.SmallPiller;
import com.mowmaster.dust.world.structures.allbiomestructures.SmallSandWell;
import com.mowmaster.dust.world.structures.allbiomestructures.SmallStoneWell;
import com.mowmaster.dust.world.treegeneration.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

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
    private SmallSandWell smallsandwell;
    private SmallStoneWell smallstonewell;
    private MediumStoneHenge mediumStoneHenge;
    private MediumCave mediumCave;
    private LargeAuraMachineRoom largeAuraMachineRoom;
    private MediumObservatory mediumObservatory;
    private LargeHouseFireplace largeHouseFireplace;



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
        smallsandwell = new SmallSandWell();
        smallstonewell = new SmallStoneWell();
        mediumStoneHenge = new MediumStoneHenge();
        mediumCave = new MediumCave();
        largeAuraMachineRoom = new LargeAuraMachineRoom();
        mediumObservatory = new MediumObservatory();
        largeHouseFireplace = new LargeHouseFireplace();
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

    int OverWorldStructureChance = 100; //1 in 50 chunck chance
    private void runAllBiomeStructures(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(OverWorldStructureChance) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(5)+ 5;
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + rand.nextInt(5)+ 5;
                generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));
            }
        }
    }




    private void runStructureGeneratorFiveteenByFiveteen(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(4) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + 8;
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + 8;
                generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));
                //Once this is good the below is biome specific
                //if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_hot) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_warm) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_cold)) {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}

            }
        }
    }

    private void runStructureGeneratorThreeteenByThreeteen(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(4) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(1)+ 7;
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + rand.nextInt(1)+ 7;
                generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));
                //Once this is good the below is biome specific
                //if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_hot) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_warm) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_cold)) {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}

            }
        }
    }

    private void runStructureGeneratorElevenByEleven(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(4) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(3)+ 6;
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + rand.nextInt(3)+ 6;
                generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));
                //Once this is good the below is biome specific
                //if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_hot) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_warm) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_cold)) {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}

            }
        }
    }

    private void runStructureGeneratorDireBox(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(4) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(5)+ 5;
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + rand.nextInt(5)+ 5;
                generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));
                //Once this is good the below is biome specific
                //if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_hot) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_warm) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_cold)) {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}

            }
        }
    }

    private void runStructureGeneratorSevenBySeven(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(4) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(7)+ 4;
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + rand.nextInt(7)+ 4;
                generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));
                //Once this is good the below is biome specific
                //if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_hot) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_warm) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_cold)) {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}

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

    private void runStructureGeneratorThreeByThree(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(4) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(11)+2;
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + rand.nextInt(11)+2;
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
                int x = chunk_X*16 + rand.nextInt(15);
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + rand.nextInt(15);
                generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));
                //Once this is good the below is biome specific
                //if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_hot) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_warm) || world.getBiome(new BlockPos(x,y,z)).equals(crystal_cold)) {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}

            }
        }
    }

    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {

        switch(world.provider.getDimension())
        {
            case 0: //Overworld
                //                         Chance to spawn,min height, max height
                //Ore Generator All Biomes
                this.runWorldGenerator(red_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runWorldGenerator(blue_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runWorldGenerator(yellow_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runWorldGenerator(purple_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runWorldGenerator(orange_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runWorldGenerator(green_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runWorldGenerator(white_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runWorldGenerator(black_ore,world,random,chunkX,chunkZ,1,0,20);
                //Ore Generator Crystal Biomes
                this.runGenerator(red_ore,world,random,chunkX,chunkZ,5,0,60);
                this.runGenerator(blue_ore,world,random,chunkX,chunkZ,5,0,60);
                this.runGenerator(yellow_ore,world,random,chunkX,chunkZ,5,0,60);
                this.runGenerator(purple_ore,world,random,chunkX,chunkZ,3,0,30);
                this.runGenerator(orange_ore,world,random,chunkX,chunkZ,3,0,30);
                this.runGenerator(green_ore,world,random,chunkX,chunkZ,3,0,30);
                this.runGenerator(white_ore,world,random,chunkX,chunkZ,1,0,10);
                this.runGenerator(black_ore,world,random,chunkX,chunkZ,1,0,10);
                //Tree Generator Crystal Biomes
                this.runTreeGenerator(smalltreered, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreeblue, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreeyellow, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreepurple, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreeorange, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreegreen, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreewhite, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreeblack, world, random, chunkX, chunkZ,1,0,20);
                //Overworld Structures All Biomes
                this.runAllBiomeStructures(smallsandwell,world,random,chunkX,chunkZ,1,0,20);
                this.runAllBiomeStructures(smallstonewell,world,random,chunkX,chunkZ,1,0,20);
                this.runAllBiomeStructures(smallpiller,world,random,chunkX,chunkZ,1,0,20);


                //Random Chance given structures will be placed in Crystal Biomes [WIP]
                //int getrando = random.nextInt(1);
                //switch (getrando)
                //{
                    //case 0: this.runStructureGeneratorDireBox(mediumCave,world,random,chunkX,chunkZ,1,0,20);
                        //Start of Machine Specific Structures
                    //case 1: this.runStructureGeneratorElevenByEleven(largeAuraMachineRoom,world,random,chunkX,chunkZ,1,0,20);
                    //case 2: this.runStructureGeneratorDireBox(mediumStoneHenge,world,random,chunkX,chunkZ,1,0,20);
                    //case 0: this.runStructureGeneratorDireBox(mediumObservatory,world,random,chunkX,chunkZ,1,0,20);
                    this.runStructureGeneratorElevenByEleven(largeHouseFireplace,world,random,chunkX,chunkZ,1,0,20);

                //}






            /*
            case 1://End
            case -1://Nether
            case 6://Aroma Mining Dim
                */
        }
    }

}
