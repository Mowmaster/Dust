package com.mowmaster.dust.world.structures;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.blocks.machines.BlockMachineBase;
import com.mowmaster.dust.world.structures.structurebits.StructureParts;
import net.minecraft.block.BlockPurpurSlab;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

/**
 * Created by KingMowmaster on 6/30/2017.
 */
public class MediumCrusherLab extends WorldGenerator
{
    IBlockState stone = Blocks.STONE.getDefaultState();
    IBlockState stoneBrick = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT);
    IBlockState stoneBrickChiseled = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos)
    {
        Random rn = new Random();


        while (!(worldIn.getBlockState(pos).equals(Blocks.GRASS.getDefaultState()) || worldIn.getBlockState(pos).equals(Blocks.STONE.getDefaultState()) || worldIn.getBlockState(pos).equals(Blocks.SAND.getDefaultState()))&& pos.getY() > -50)
        {
            pos = pos.down();
        }
        pos = pos.up();
        if(worldIn.getBlockState(pos.down()).equals(Blocks.GRASS.getDefaultState()))
        {
            StructureParts.createSolidFloor(worldIn,pos,Blocks.GRASS.getDefaultState(),0,-1,0,-1,1,-1,1);
        }
        if(worldIn.getBlockState(pos.down()).equals(Blocks.STONE.getDefaultState()))
        {
            StructureParts.createSolidFloor(worldIn,pos,Blocks.STONE.getDefaultState(),0,-1,0,-1,1,-1,1);
        }
        if(worldIn.getBlockState(pos.down()).equals(Blocks.SAND.getDefaultState()))
        {
            StructureParts.createSolidFloor(worldIn,pos,Blocks.SAND.getDefaultState(),0,-1,0,-1,1,-1,1);
        }

        worldIn.setBlockState(pos.add(0,-1,0), Blocks.TRAPDOOR.getDefaultState().withProperty(BlockTrapDoor.HALF, BlockTrapDoor.DoorHalf.TOP));


        int cavedepth = (rn.nextInt(30))-50;
        pos = pos.add(0,cavedepth,0);
        int stairlength = Math.abs(cavedepth)-2;
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-1,0,0,stairlength,0,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,1,0,0,stairlength,0,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,0,0,1,stairlength,0,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,0,0,-1,stairlength,0,true);
        StructureParts.createSolidWall(worldIn,pos,Blocks.LADDER.getDefaultState(),0,0,0,stairlength,0,true);
        pos = pos.add(4,0,3);
        StructureParts.createSolidFloor(worldIn,pos,stoneBrick,0,-1,0,-3,3,-3,3);
        StructureParts.createSolidFloor(worldIn,pos,stone,0,-1,0,-2,2,-2,2);
        worldIn.setBlockState(pos.add(0,-1,0), stoneBrickChiseled);
        StructureParts.clearArea(worldIn,pos,0,4,-3,3,-3,3);
        StructureParts.createSolidFloor(worldIn,pos,stoneBrick,0,5,0,-3,3,-3,3);
        StructureParts.createSolidFloor(worldIn,pos,stone,0,5,0,-2,2,-2,2);
        //33 corner
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,3,0,4,4,0,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,4,0,3,4,0,true);
        //3-3 corner
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,3,0,-4,4,0,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,4,0,-3,4,0,true);
        //-33 corner
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-3,0,4,4,0,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-4,0,3,4,0,true);
        //-3-3 corner *has ladder at -4-3*
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-3,0,-4,4,0,true);
        StructureParts.generateFloorLoot(worldIn,pos,0,0,0,-3,3,0,0,-3,3,"normal");
        StructureParts.clearArea(worldIn,pos,0,0,-2,2,-2,2);
        //Fill with dust and spawn machine
        StructureParts.generateFloorLoot(worldIn,pos,0,0,0,-2,2,0,4,-2,2,"dust");
        worldIn.setBlockState(pos.add(0,0,0), BlockMachineBase.machineBase.getDefaultState());

        return true;
    }
}
