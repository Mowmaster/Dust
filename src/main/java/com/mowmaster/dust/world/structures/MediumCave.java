package com.mowmaster.dust.world.structures;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.blocks.machines.BlockMachineBase;
import com.mowmaster.dust.world.structures.structurebits.StructureParts;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

/**
 * Created by KingMowmaster on 6/30/2017.
 */
public class MediumCave extends WorldGenerator
{
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos)
    {
        while (!(worldIn.getBlockState(pos).equals(Blocks.GRASS.getDefaultState()) || worldIn.getBlockState(pos).equals(Blocks.SAND.getDefaultState()))&& pos.getY() > -50)
        {
            pos = pos.down();
        }
        Random rn = new Random();
        int cavedepth = rn.nextInt(30);
        pos = pos.add(0,(-50)+cavedepth,0);

        StructureParts.clearArea(worldIn,pos,0,3,-1,1,-1,1);
        StructureParts.clearArea(worldIn,pos,1,1,-2,2,-2,2);
        for(int x = -1;x<=1;x++)
        {
            for(int z = -1;z<=1;z++)
            {
                StructureParts.spawnCrystal(worldIn,rand,pos,x,0,z,"UP");
            }
        }
        for(int x = -2;x<=2;x++)
        {
            for(int z = -2;z<=2;z++)
            {
                StructureParts.spawnCrystal(worldIn,rand,pos,x,1,z,"UP");
            }
        }
        for(int x = -2;x<=2;x++)
        {
            for(int z = -2;z<=2;z++)
            {
                StructureParts.spawnCrystal(worldIn,rand,pos,x,2,z,"DOWN");
            }
        }

        for(int x = -1;x<=1;x++)
        {
            for(int z = -1;z<=1;z++)
            {
                StructureParts.spawnCrystal(worldIn,rand,pos,x,3,z,"DOWN");
            }
        }

        StructureParts.clearArea(worldIn,pos,1,2,-1,1,-1,1);
        worldIn.setBlockState(pos.add(0,0,0), BlockMachineBase.machineBase.getDefaultState());

        return true;
    }
}
