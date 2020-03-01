package com.mowmaster.dust.world.structures;

import com.mowmaster.dust.blocks.machines.BlockMachineBase;
import com.mowmaster.dust.world.structures.structurebits.StructureParts;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

import static com.mowmaster.dust.world.structures.structurebits.StructureParts.*;

public class MediumStoneHenge extends WorldGenerator
{

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos)
    {
        while (!(worldIn.getBlockState(pos).equals(Blocks.GRASS.getDefaultState()) || worldIn.getBlockState(pos).equals(Blocks.SAND.getDefaultState()))&& pos.getY() > -50)
        {
            pos = pos.down();
        }

        StructureParts.clearArea(worldIn,pos,1,4,-4,4,-4,4);

        buildFloorThreeByThree(worldIn,rand,pos,0,0,0);
        worldIn.setBlockState(pos.add(0,1,0), BlockMachineBase.machineBase.getDefaultState());
        buildLootPiller(worldIn,rand,pos,0,0,3);
        buildLootPiller(worldIn,rand,pos,0,0,-3);
        buildLootPiller(worldIn,rand,pos,-4,0,0);
        buildLootPiller(worldIn,rand,pos,4,0,0);
        buildLootPiller(worldIn,rand,pos,-3,0,-2);
        buildLootPiller(worldIn,rand,pos,3,0,-2);
        buildLootPiller(worldIn,rand,pos,-3,0,2);
        buildLootPiller(worldIn,rand,pos,3,0,2);
        spawnCrystal(worldIn,rand,pos,-2,1,0,"UP");
        spawnCrystal(worldIn,rand,pos,2,1,0,"UP");

        return true;
    }

}
