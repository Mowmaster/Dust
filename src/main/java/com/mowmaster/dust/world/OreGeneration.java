package com.mowmaster.dust.world;

import com.mowmaster.dust.world.structures.*;
import com.mowmaster.dust.world.structures.allbiomestructures.SmallPiller;
import com.mowmaster.dust.world.structures.allbiomestructures.SmallSandWell;
import com.mowmaster.dust.world.structures.allbiomestructures.SmallStoneWell;
import com.mowmaster.dust.world.treegeneration.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

import static com.mowmaster.dust.blocks.BlockRegistry.*;
import static com.mowmaster.dust.misc.DustConfigurationFile.*;
import static com.mowmaster.dust.world.biome.BiomeRegistry.*;

public class OreGeneration implements IWorldGenerator
{

    int BiomeSpecificStructureChance = structuresInCrystal; //Biome Specific Structures
    int OverWorldStructureChance = structuresInWorld; //1 in X chunck chance
    int randomnum = treeGenChance;//Biome Specific Specialized Biomes Tree Generation
    int randomworld = anyBiomeOreSpawnChance; //All Overworld Biomes ore spawn rate
    int count = oreClusterSize;// How many in an ore cluster
    int crystalOreInCrystal = oreChanceInCrystalBiome;

    private WorldGenerator red_ore;
    private WorldGenerator blue_ore;
    private WorldGenerator yellow_ore;
    private WorldGenerator purple_ore;
    private WorldGenerator orange_ore;
    private WorldGenerator green_ore;
    private WorldGenerator white_ore;
    private WorldGenerator black_ore;

    //For Use in crystal Biomes above y=70
    private WorldGenerator glowstone_ore;
    private WorldGenerator coal_ore;
    private WorldGenerator lapis_ore;
    private WorldGenerator redstone_ore;
    private WorldGenerator iron_ore;
    private WorldGenerator gold_ore;
    private WorldGenerator diamond_ore;
    private WorldGenerator emerald_ore;

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
    private MediumCrusherLab mediumCrusherLab;

    private StructureGenerator tester;//the same as tester1 atm
    private StructureGenerator tester1;
    private StructureGenerator tester2;
    private StructureGenerator tester3;
    private StructureGenerator tester4;
    private StructureGenerator tester5;
    private StructureGenerator tester6;
    private StructureGenerator tester7;
    private StructureGenerator tester8;
    private StructureGenerator tester9;
    private StructureGenerator tester10;
    private StructureGenerator tester11;



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


        // for use in crystal biomes
        glowstone_ore = new WorldGenMinable(Blocks.GLOWSTONE.getDefaultState(),count);
        coal_ore = new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(),count);
        lapis_ore = new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(),count);
        redstone_ore = new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(),count);
        iron_ore = new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(),count);
        gold_ore = new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(),count);
        diamond_ore = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(),count);
        emerald_ore = new WorldGenMinable(Blocks.EMERALD_ORE.getDefaultState(),count);


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
        mediumCrusherLab = new MediumCrusherLab();


        tester = new StructureGenerator("pillarchiseled1");
        tester1 = new StructureGenerator("pillarchiseled1");
        tester2 = new StructureGenerator("pillarchiseled2");
        tester3 = new StructureGenerator("pillarchiseled3");
        tester4 = new StructureGenerator("pillarchiseled4");

        tester5 = new StructureGenerator("pillarcobble1");
        tester6 = new StructureGenerator("pillarcobble2");
        tester7 = new StructureGenerator("pillarcobble3");

        tester8 = new StructureGenerator("pillarstone1");
        tester9 = new StructureGenerator("pillarstone2");
        tester10 = new StructureGenerator("pillarstone3");
        tester11 = new StructureGenerator("pillarstone4");


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
        if (rand.nextInt(crystalOreInCrystal) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(16);
                int y = minHeight + rand.nextInt(heightDiff);
                int z = chunk_Z*16 + rand.nextInt(16);
                if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal))
                {generator.generate(world, rand, new BlockPos(x, y, z));}
            }
        }



    }
    /*
    private void runGeneratorHot(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chancesToSpawn; i++) {
            int x = chunk_X*16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunk_Z*16 + rand.nextInt(16);
            if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_hot))
            {generator.generate(world, rand, new BlockPos(x, y, z));}
        }
    }
    private void runGeneratorWarm(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chancesToSpawn; i++) {
            int x = chunk_X*16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunk_Z*16 + rand.nextInt(16);
            if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_warm))
            {generator.generate(world, rand, new BlockPos(x, y, z));}
        }
    }
    private void runGeneratorCold(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chancesToSpawn; i++) {
            int x = chunk_X*16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunk_Z*16 + rand.nextInt(16);
            if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_cold))
            {generator.generate(world, rand, new BlockPos(x, y, z));}
        }
    }
     */

    private void runTreeGenerator(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(randomnum) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(9)+ 3;
                int y = world.getHeight();
                int z = chunk_Z*16 + rand.nextInt(9)+ 3;
                if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal))
                {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}
            }
        }
    }
    /*
    private void runTreeGeneratorHot(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(randomnum) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(9)+ 3;
                int y = world.getHeight();
                int z = chunk_Z*16 + rand.nextInt(9)+ 3;
                if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_hot))
                {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}
            }
        }
    }
    private void runTreeGeneratorWarm(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(randomnum) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(9)+ 3;
                int y = world.getHeight();
                int z = chunk_Z*16 + rand.nextInt(9)+ 3;
                if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_warm))
                {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}
            }
        }
    }
    private void runTreeGeneratorCold(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(randomnum) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(9)+ 3;
                int y = world.getHeight();
                int z = chunk_Z*16 + rand.nextInt(9)+ 3;
                if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_cold))
                {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}
            }
        }
    }
     */


    private void runAllBiomeStructures(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(OverWorldStructureChance) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(3)+ 6;
                int y = world.getHeight(chunk_X,chunk_Z);
                int z = chunk_Z*16 + rand.nextInt(3)+ 6;
                generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));
            }
        }
    }




    private void runStructureGeneratorFiveteenByFiveteen(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(BiomeSpecificStructureChance) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + 8;
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + 8;
                //Once this is good the below is biome specific
                if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal)) {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}

            }
        }
    }

    private void runStructureGeneratorThreeteenByThreeteen(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(BiomeSpecificStructureChance) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(1)+ 7;
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + rand.nextInt(1)+ 7;
                //Once this is good the below is biome specific
                if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal)) {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}

            }
        }
    }
//worldgen structures
    private void runStructureGeneratorElevenByEleven(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(BiomeSpecificStructureChance) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(3)+ 6;
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + rand.nextInt(3)+ 6;
                //Once this is good the below is biome specific
                if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal)) {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}

            }
        }
    }
    //worldgen structures
    private void runStructureGeneratorDireBox(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(BiomeSpecificStructureChance) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(5)+ 5;
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + rand.nextInt(5)+ 5;
                //Once this is good the below is biome specific
                if(world.getBiome(new BlockPos(x,y,z)).equals(crystal_crystal)) {generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));}

            }
        }
    }

    private void runStructureGeneratorSevenBySeven(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(OverWorldStructureChance) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(7)+ 4;
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + rand.nextInt(7)+ 4;
                //Once this is good the below is biome specific
                generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));

            }
        }
    }

    private void runStructureGeneratorFiveByFive(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(OverWorldStructureChance) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(9)+ 3;
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + rand.nextInt(9)+ 3;
                //Once this is good the below is biome specific
                generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));

            }
        }
    }

    private void runStructureGeneratorThreeByThree(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(OverWorldStructureChance) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(11)+2;
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + rand.nextInt(11)+2;
                //Once this is good the below is biome specific
                generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));

            }
        }
    }

    private void runStructureGeneratorOneByOne(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        if (rand.nextInt(OverWorldStructureChance) <= 1) {
            for (int i = 0; i < chancesToSpawn; i++) {
                int x = chunk_X*16 + rand.nextInt(15);
                int y = world.getHeight(chunk_X,chunk_Z) + 1;
                int z = chunk_Z*16 + rand.nextInt(15);
                //Once this is good the below is biome specific
                generator.generate(world, rand, world.getHeight(new BlockPos(x,y,z)));

            }
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
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
                //Ore Generator Crystal Biome
                this.runGenerator(red_ore,world,random,chunkX,chunkZ,30,0,200);
                this.runGenerator(blue_ore,world,random,chunkX,chunkZ,30,0,200);
                this.runGenerator(yellow_ore,world,random,chunkX,chunkZ,30,0,200);
                this.runGenerator(purple_ore,world,random,chunkX,chunkZ,20,0,200);
                this.runGenerator(orange_ore,world,random,chunkX,chunkZ,20,0,200);
                this.runGenerator(green_ore,world,random,chunkX,chunkZ,20,0,200);
                this.runGenerator(white_ore,world,random,chunkX,chunkZ,3,0,30);
                this.runGenerator(black_ore,world,random,chunkX,chunkZ,3,0,30);

                //Regular ores in crystal biomes above y=70
                this.runGenerator(glowstone_ore,world,random,chunkX,chunkZ,30,50,250);
                this.runGenerator(coal_ore,world,random,chunkX,chunkZ,30,50,250);
                this.runGenerator(redstone_ore,world,random,chunkX,chunkZ,30,50,250);
                this.runGenerator(lapis_ore,world,random,chunkX,chunkZ,30,50,250);
                this.runGenerator(iron_ore,world,random,chunkX,chunkZ,30,50,250);
                this.runGenerator(gold_ore,world,random,chunkX,chunkZ,30,50,250);
                this.runGenerator(diamond_ore,world,random,chunkX,chunkZ,30,50,250);
                this.runGenerator(emerald_ore,world,random,chunkX,chunkZ,30,50,250);
                /*
                //Ore Generator Crystal Hot Biome
                this.runGeneratorHot(red_ore,world,random,chunkX,chunkZ,15,0,60);
                this.runGeneratorHot(yellow_ore,world,random,chunkX,chunkZ,15,0,60);
                this.runGeneratorHot(orange_ore,world,random,chunkX,chunkZ,9,0,30);
                this.runGeneratorHot(white_ore,world,random,chunkX,chunkZ,3,0,10);
                //Ore Generator Crystal Warm Biome
                this.runGeneratorWarm(yellow_ore,world,random,chunkX,chunkZ,15,0,60);
                this.runGeneratorWarm(orange_ore,world,random,chunkX,chunkZ,9,0,30);
                this.runGeneratorWarm(green_ore,world,random,chunkX,chunkZ,9,0,30);
                this.runGeneratorWarm(white_ore,world,random,chunkX,chunkZ,3,0,10);
                this.runGeneratorWarm(black_ore,world,random,chunkX,chunkZ,3,0,10);
                //Ore Generator Crystal Cold Biome
                this.runGeneratorCold(blue_ore,world,random,chunkX,chunkZ,15,0,60);
                this.runGeneratorCold(purple_ore,world,random,chunkX,chunkZ,9,0,30);
                this.runGeneratorCold(green_ore,world,random,chunkX,chunkZ,9,0,30);
                this.runGeneratorCold(black_ore,world,random,chunkX,chunkZ,3,0,10);
                 */
                //Tree Generator Crystal Biome
                this.runTreeGenerator(smalltreered, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreeblue, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreeyellow, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreepurple, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreeorange, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreegreen, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreewhite, world, random, chunkX, chunkZ,1,0,20);
                this.runTreeGenerator(smalltreeblack, world, random, chunkX, chunkZ,1,0,20);

                //Overworld Structures All Biomes
                int getrandoOW = random.nextInt(23);
                switch (getrandoOW)
                {
                    case 0: this.runAllBiomeStructures(smallsandwell,world,random,chunkX,chunkZ,1,0,20);
                        this.runAllBiomeStructures(smallstonewell,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    //Start of Machine Specific Structures
                    case 1: this.runAllBiomeStructures(mediumStoneHenge,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 2: this.runAllBiomeStructures(mediumCrusherLab,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 3: this.runAllBiomeStructures(largeAuraMachineRoom,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 4: this.runAllBiomeStructures(tester,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 5: this.runAllBiomeStructures(tester1,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 6: this.runAllBiomeStructures(tester2,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 7: this.runAllBiomeStructures(tester3,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 8: this.runAllBiomeStructures(tester4,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 9: this.runAllBiomeStructures(tester5,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 10: this.runAllBiomeStructures(tester6,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 11: this.runAllBiomeStructures(tester7,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 12: this.runAllBiomeStructures(tester8,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 13: this.runAllBiomeStructures(tester9,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 14: this.runAllBiomeStructures(tester10,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 15: this.runAllBiomeStructures(smalltreered,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 16: this.runAllBiomeStructures(smalltreeblue,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 17: this.runAllBiomeStructures(smalltreeyellow,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 18: this.runAllBiomeStructures(smalltreepurple,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 19: this.runAllBiomeStructures(smalltreegreen,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 20: this.runAllBiomeStructures(smalltreeorange,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 21: this.runAllBiomeStructures(smalltreewhite,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 22: this.runAllBiomeStructures(smalltreeblack,world,random,chunkX,chunkZ,1,0,20);
                        return;

                }





                //this.runAllBiomeStructures(tester,world,random,chunkX,chunkZ,1,0,20);



                //Random Chance given structures will be placed in Crystal Biomes [WIP]
                int getrando = random.nextInt(6);
                //System.out.println(getrando);
                switch (getrando)
                {
                    case 0: this.runStructureGeneratorDireBox(mediumCave,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    //Start of Machine Specific Structures
                    case 1: this.runStructureGeneratorElevenByEleven(largeAuraMachineRoom,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 2: this.runStructureGeneratorDireBox(mediumStoneHenge,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 3: this.runStructureGeneratorDireBox(mediumObservatory,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 4: this.runStructureGeneratorElevenByEleven(largeHouseFireplace,world,random,chunkX,chunkZ,1,0,20);
                        return;
                    case 5: this.runStructureGeneratorDireBox(mediumCrusherLab,world,random,chunkX,chunkZ,1,0,20);
                        return;

                }






            /*
            case 1://End
            case -1://Nether
            case 6://Aroma Mining Dim
                */
        }
    }

}