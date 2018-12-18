package com.mowmaster.dust.world.structures;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.world.structures.structurebits.StructureParts;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;


public class LargeHouseFireplace extends WorldGenerator
{
    IBlockState colorWood;
    IBlockState colorLog;
    IBlockState colorStair;
    IBlockState stone = Blocks.STONE.getDefaultState();
    IBlockState stoneBrick = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT);
    IBlockState stoneBrickStairs = Blocks.STONE_BRICK_STAIRS.getDefaultState();

    String[] color = new String[] {"red","blue","yellow","purple","orange","green","white","black"};
    Random rn = new Random();

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos)
    {
        int colorchoice = rn.nextInt(color.length);
        String colorpicked = color[colorchoice];

        if(colorpicked.equals("red"))
        {
            colorWood = BlockRegistry.redplanks.getDefaultState();
            colorLog = BlockRegistry.logred.getDefaultState();
            colorStair = BlockRegistry.redplankstairs.getDefaultState();
        }
        else if(colorpicked.equals("blue"))
        {
            colorWood = BlockRegistry.blueplanks.getDefaultState();
            colorLog = BlockRegistry.logblue.getDefaultState();
            colorStair = BlockRegistry.blueplankstairs.getDefaultState();

        }
        else if(colorpicked.equals("yellow"))
        {
            colorWood = BlockRegistry.yellowplanks.getDefaultState();
            colorLog = BlockRegistry.logyellow.getDefaultState();
            colorStair = BlockRegistry.yellowplankstairs.getDefaultState();

        }
        else if(colorpicked.equals("purple"))
        {
            colorWood = BlockRegistry.purpleplanks.getDefaultState();
            colorLog = BlockRegistry.logpurple.getDefaultState();
            colorStair = BlockRegistry.purpleplankstairs.getDefaultState();
        }
        else if(colorpicked.equals("orange"))
        {
            colorWood = BlockRegistry.orangeplanks.getDefaultState();
            colorLog = BlockRegistry.logorange.getDefaultState();
            colorStair = BlockRegistry.orangeplankstairs.getDefaultState();
        }
        else if(colorpicked.equals("green"))
        {
            colorWood = BlockRegistry.greenplanks.getDefaultState();
            colorLog = BlockRegistry.loggreen.getDefaultState();
            colorStair = BlockRegistry.greenplankstairs.getDefaultState();
        }
        else if(colorpicked.equals("white"))
        {
            colorWood = BlockRegistry.whiteplanks.getDefaultState();
            colorLog = BlockRegistry.logwhite.getDefaultState();
            colorStair = BlockRegistry.whiteplankstairs.getDefaultState();
        }else if(colorpicked.equals("black"))
        {
            colorWood = BlockRegistry.blackplanks.getDefaultState();
            colorLog = BlockRegistry.logblack.getDefaultState();
            colorStair = BlockRegistry.blackplankstairs.getDefaultState();
        }


        while (!(worldIn.getBlockState(pos).equals(Blocks.GRASS.getDefaultState()) || worldIn.getBlockState(pos).equals(Blocks.SAND.getDefaultState()))&& pos.getY() > -50)
        {
            pos = pos.down();
        }
        pos = pos.up();


        StructureParts.clearArea(worldIn,pos,0,15,-3,3,-4,4);
        StructureParts.createSolidFloor(worldIn,pos,stoneBrick,0,-1,0,-3,3,-4,4);
        StructureParts.createSolidFloor(worldIn,pos,colorWood,0,-1,0,-1,1,-2,2);
        StructureParts.createSolidFloor(worldIn,pos,stoneBrick,0,0,0,-3,3,-4,4);
        StructureParts.createSolidFloor(worldIn,pos,stone,0,1,0,-3,3,-4,4);
        StructureParts.createSolidFloor(worldIn,pos,stone,0,2,0,-3,3,-4,4);
        StructureParts.createSolidFloor(worldIn,pos,stone,0,3,0,-3,3,-4,4);
        StructureParts.createSolidFloor(worldIn,pos,stone,0,4,0,-3,3,-4,4);
        StructureParts.createSolidWall(worldIn,pos,stone,4,5,-1,0,2,false);
        StructureParts.createSolidWall(worldIn,pos,stone,-4,5,-1,0,2,false);
        StructureParts.clearArea(worldIn,pos,0,15,-2,2,-3,3);

        //DoorWayBit
        //Brick 3x3
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,1,0,-3,2,2,true);
        //Stairs 3x1
        StructureParts.createSolidWall(worldIn,pos,stoneBrickStairs.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, EnumFacing.SOUTH),1,0,-4,0,2,true);
        //DoorHole
        StructureParts.createSolidWall(worldIn,pos,Blocks.AIR.getDefaultState(),2,0,-4,1,1,false);

        //Wood Pillers
        StructureParts.createSolidWall(worldIn,pos,colorLog,4,0,3,3,0,true);
        StructureParts.createSolidWall(worldIn,pos,colorLog,4,0,-3,3,0,true);
        StructureParts.createSolidWall(worldIn,pos,colorLog,-4,0,3,3,0,true);
        StructureParts.createSolidWall(worldIn,pos,colorLog,-4,0,-3,3,0,true);
        //Longer Pillers
        StructureParts.createSolidWall(worldIn,pos,colorLog,4,0,0,5,0,true);
        StructureParts.createSolidWall(worldIn,pos,colorLog,-4,0,0,5,0,true);

        //Roof
        //Front
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, EnumFacing.SOUTH),-5,3,-4,0,10,true);
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, EnumFacing.SOUTH),-5,4,-3,0,10,true);
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, EnumFacing.SOUTH),-5,5,-2,0,10,true);
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, EnumFacing.SOUTH),-5,6,-1,0,10,true);
        //Ridge
        StructureParts.createSolidWall(worldIn,pos,colorWood,-5,6,0,0,10,true);
        //Back
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, EnumFacing.NORTH),-5,3,4,0,10,true);
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, EnumFacing.NORTH),-5,4,3,0,10,true);
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, EnumFacing.NORTH),-5,5,2,0,10,true);
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, EnumFacing.NORTH),-5,6,1,0,10,true);

        //Chimney
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-4,0,0,9,0,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-5,0,1,8,0,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-5,0,-1,8,0,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-6,0,0,7,0,true);
        worldIn.setBlockState(pos.add(-4,10,0),stoneBrickStairs.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, EnumFacing.WEST));
        worldIn.setBlockState(pos.add(-5,9,1),stoneBrickStairs.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, EnumFacing.NORTH));
        worldIn.setBlockState(pos.add(-5,9,-1),stoneBrickStairs.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
        worldIn.setBlockState(pos.add(-6,8,0),stoneBrickStairs.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, EnumFacing.EAST));
        worldIn.setBlockState(pos.add(-5,20,0),Blocks.AIR.getDefaultState());

        //Fireplace
        worldIn.setBlockState(pos.add(-4,0,1),stoneBrickStairs.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, EnumFacing.WEST));
        worldIn.setBlockState(pos.add(-4,1,1),stoneBrickStairs.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.FACING, EnumFacing.WEST));
        worldIn.setBlockState(pos.add(-4,2,1),stoneBrick);

        worldIn.setBlockState(pos.add(-4,0,0),Blocks.AIR.getDefaultState());
        worldIn.setBlockState(pos.add(-4,1,0),Blocks.AIR.getDefaultState());

        worldIn.setBlockState(pos.add(-5,0,0),BlockRegistry.machineBase.getDefaultState());

        worldIn.setBlockState(pos.add(-4,0,-1),stoneBrickStairs.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.FACING, EnumFacing.WEST));
        worldIn.setBlockState(pos.add(-4,1,-1),stoneBrickStairs.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.FACING, EnumFacing.WEST));
        worldIn.setBlockState(pos.add(-4,2,-1),stoneBrick);

        StructureParts.generateFloorLoot(worldIn,pos,0,0,0,-3,3,0,0,-2,2,"reduced");

        return true;
    }
}
