package com.mowmaster.dust.world;
import com.mowmaster.dust.blocks.*;
import com.mowmaster.dust.enums.CrystalBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.*;

import static com.mowmaster.dust.blocks.BlockRedOre.REDSTATE;
import static com.mowmaster.dust.blocks.BlockRegistry.*;

public class OreGeneration implements IWorldGenerator
{

    private WorldGenerator red_ore;
    private WorldGenerator blue_ore;
    private WorldGenerator yellow_ore;
    private WorldGenerator purple_ore;
    private WorldGenerator orange_ore;
    private WorldGenerator green_ore;
    private WorldGenerator white_ore;
    private WorldGenerator black_ore;


    public OreGeneration()
    {
        int count = 1;
        red_ore = new WorldGenMinable(orered.getDefaultState().withProperty(BlockRedOre.REDSTATE, CrystalBlocks.CrystalOres.ORE),count);
        blue_ore = new WorldGenMinable(oreblue.getDefaultState().withProperty(BlockBlueOre.BLUESTATE, CrystalBlocks.CrystalOres.ORE),count);
        yellow_ore = new WorldGenMinable(oreyellow.getDefaultState().withProperty(BlockYellowOre.YELLOWSTATE, CrystalBlocks.CrystalOres.ORE),count);
        purple_ore = new WorldGenMinable(orepurple.getDefaultState().withProperty(BlockPurpleOre.PURPLESTATE, CrystalBlocks.CrystalOres.ORE),count);
        orange_ore = new WorldGenMinable(oreorange.getDefaultState().withProperty(BlockOrangeOre.ORANGESTATE, CrystalBlocks.CrystalOres.ORE),count);
        green_ore = new WorldGenMinable(oregreen.getDefaultState().withProperty(BlockGreenOre.GREENSTATE, CrystalBlocks.CrystalOres.ORE),count);
        white_ore = new WorldGenMinable(orewhite.getDefaultState().withProperty(BlockWhiteOre.WHITESTATE, CrystalBlocks.CrystalOres.ORE),count);
        black_ore = new WorldGenMinable(oreblack.getDefaultState().withProperty(BlockBlackOre.BLACKSTATE, CrystalBlocks.CrystalOres.ORE),count);

    }

    private void runGenerator(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {
        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chancesToSpawn; i ++) {
            int x = chunk_X * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunk_Z * 16 + rand.nextInt(16);
            generator.generate(world, rand, new BlockPos(x, y, z));
        }
    }

    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        switch(world.provider.getDimension())
        {
            case 0: //Overworld
                this.runGenerator(red_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runGenerator(blue_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runGenerator(yellow_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runGenerator(purple_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runGenerator(orange_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runGenerator(green_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runGenerator(white_ore,world,random,chunkX,chunkZ,1,0,20);
                this.runGenerator(black_ore,world,random,chunkX,chunkZ,1,0,20);
                break;
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
