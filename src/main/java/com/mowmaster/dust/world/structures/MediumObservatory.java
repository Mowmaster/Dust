package com.mowmaster.dust.world.structures;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.blocks.buildingblocks.BlockDustBasic;
import com.mowmaster.dust.blocks.buildingblocks.BlockDustFence;
import com.mowmaster.dust.blocks.buildingblocks.BlockDustSlab;
import com.mowmaster.dust.blocks.buildingblocks.BlockDustStair;
import com.mowmaster.dust.blocks.machines.BlockMachineBase;
import com.mowmaster.dust.world.structures.structurebits.StructureParts;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;


public class MediumObservatory extends WorldGenerator
{
    IBlockState colorWood;
    IBlockState colorStair;
    IBlockState colorSlab;
    IBlockState colorFence;
    IBlockState stoneBrick = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT);
    IBlockState stoneBrickSlabs = Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SMOOTHBRICK);

    String[] color = new String[] {"red","blue","yellow","purple","orange","green","white","black"};
    Random rn = new Random();

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos)
    {
        int colorchoice = rn.nextInt(color.length);
        String colorpicked = color[colorchoice];

        if(colorpicked.equals("red"))
        {
            colorWood = BlockDustBasic.redplanks.getDefaultState();
            colorStair = BlockDustStair.redstonestairs.getDefaultState();
            colorSlab = BlockDustSlab.redstoneslabs.getDefaultState();
            colorFence = BlockDustFence.redstonefences.getDefaultState();
        }
        else if(colorpicked.equals("blue"))
        {
            colorWood = BlockDustBasic.blueplanks.getDefaultState();
            colorStair = BlockDustStair.bluestonestairs.getDefaultState();
            colorSlab = BlockDustSlab.bluestoneslabs.getDefaultState();
            colorFence = BlockDustFence.bluestonefences.getDefaultState();
        }
        else if(colorpicked.equals("yellow"))
        {
            colorWood = BlockDustBasic.yellowplanks.getDefaultState();
            colorStair = BlockDustStair.yellowstonestairs.getDefaultState();
            colorSlab = BlockDustSlab.yellowstoneslabs.getDefaultState();
            colorFence = BlockDustFence.yellowstonefences.getDefaultState();
        }
        else if(colorpicked.equals("purple"))
        {
            colorWood = BlockDustBasic.purpleplanks.getDefaultState();
            colorStair = BlockDustStair.purplestonestairs.getDefaultState();
            colorSlab = BlockDustSlab.purplestoneslabs.getDefaultState();
            colorFence = BlockDustFence.purplestonefences.getDefaultState();
        }
        else if(colorpicked.equals("orange"))
        {
            colorWood = BlockDustBasic.orangeplanks.getDefaultState();
            colorStair = BlockDustStair.orangestonestairs.getDefaultState();
            colorSlab = BlockDustSlab.orangestoneslabs.getDefaultState();
            colorFence = BlockDustFence.orangestonefences.getDefaultState();
        }
        else if(colorpicked.equals("green"))
        {
            colorWood = BlockDustBasic.greenplanks.getDefaultState();
            colorStair = BlockDustStair.greenstonestairs.getDefaultState();
            colorSlab = BlockDustSlab.greenstoneslabs.getDefaultState();
            colorFence = BlockDustFence.greenstonefences.getDefaultState();
        }
        else if(colorpicked.equals("white"))
        {
            colorWood = BlockDustBasic.whiteplanks.getDefaultState();
            colorStair = BlockDustStair.whitestonestairs.getDefaultState();
            colorSlab = BlockDustSlab.whitestoneslabs.getDefaultState();
            colorFence = BlockDustFence.whitestonefences.getDefaultState();
        }else if(colorpicked.equals("black"))
        {
            colorWood = BlockDustBasic.blackplanks.getDefaultState();
            colorStair = BlockDustStair.blackstonestairs.getDefaultState();
            colorSlab = BlockDustSlab.blackstoneslabs.getDefaultState();
            colorFence = BlockDustFence.blackstonefences.getDefaultState();
        }


        while (!(worldIn.getBlockState(pos).equals(Blocks.GRASS.getDefaultState()) || worldIn.getBlockState(pos).equals(Blocks.SAND.getDefaultState()))&& pos.getY() > -50)
        {
            pos = pos.down();
        }
        pos = pos.up();


        StructureParts.clearArea(worldIn,pos,0,50,-4,4,-4,4);
        StructureParts.createSolidFloor(worldIn,pos,colorWood,0,-1,0,-2,2,-2,2);


        //stairs
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM),-1,0,4,0,2,true);
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.FACING, EnumFacing.SOUTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM),-1,0,-4,0,2,true);
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM),4,0,-1,0,2,false);
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM),-4,0,-1,0,2,false);
        StructureParts.createSolidWall(worldIn,pos,Blocks.AIR.getDefaultState(),-4,0,0,0,8,true);
        StructureParts.createSolidWall(worldIn,pos,Blocks.AIR.getDefaultState(),0,0,-4,0,8,false);
        //slabs
        StructureParts.createSolidWall(worldIn,pos,colorSlab.withProperty(BlockDustSlab.FACING,EnumFacing.SOUTH),-1,1,4,0,2,true);
        StructureParts.createSolidWall(worldIn,pos,colorSlab.withProperty(BlockDustSlab.FACING,EnumFacing.NORTH),-1,1,-4,0,2,true);
        StructureParts.createSolidWall(worldIn,pos,colorSlab.withProperty(BlockDustSlab.FACING,EnumFacing.EAST),4,1,-1,0,2,false);
        StructureParts.createSolidWall(worldIn,pos,colorSlab.withProperty(BlockDustSlab.FACING,EnumFacing.WEST),-4,1,-1,0,2,false);
        StructureParts.createSolidWall(worldIn,pos,Blocks.AIR.getDefaultState(),-4,1,0,0,8,true);
        StructureParts.createSolidWall(worldIn,pos,Blocks.AIR.getDefaultState(),0,1,-4,0,8,false);
        //Stair top ledge
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP),-1,2,4,0,2,true);
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.FACING, EnumFacing.SOUTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP),-1,2,-4,0,2,true);
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP),4,2,-1,0,2,false);
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP),-4,2,-1,0,2,false);

        worldIn.setBlockState(pos.add(0,2,-4),colorSlab.withProperty(BlockDustSlab.FACING,EnumFacing.NORTH));
        worldIn.setBlockState(pos.add(0,2,4),colorSlab.withProperty(BlockDustSlab.FACING,EnumFacing.SOUTH));
        worldIn.setBlockState(pos.add(-4,2,0),colorSlab.withProperty(BlockDustSlab.FACING,EnumFacing.WEST));
        worldIn.setBlockState(pos.add(4,2,0),colorSlab.withProperty(BlockDustSlab.FACING,EnumFacing.EAST));

        worldIn.setBlockState(pos.add(0,3,-4),colorSlab.withProperty(BlockDustSlab.FACING,EnumFacing.UP));
        worldIn.setBlockState(pos.add(0,3,4),colorSlab.withProperty(BlockDustSlab.FACING,EnumFacing.UP));
        worldIn.setBlockState(pos.add(-4,3,0),colorSlab.withProperty(BlockDustSlab.FACING,EnumFacing.UP));
        worldIn.setBlockState(pos.add(4,3,0),colorSlab.withProperty(BlockDustSlab.FACING,EnumFacing.UP));

        StructureParts.createSolidFloor(worldIn,pos,colorWood,0,4,0,-2,2,-2,2);

        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-1,0,3,4,2,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-1,0,-3,4,2,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-3,0,-1,4,2,false);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,3,0,-1,4,2,false);

        StructureParts.createSolidWall(worldIn,pos,stoneBrick,2,0,-2,4,0,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,2,0,2,4,0,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-2,0,-2,4,0,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-2,0,2,4,0,true);

        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP),-1,10,3,0,2,true);
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.FACING, EnumFacing.SOUTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP),-1,10,-3,0,2,true);
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP),3,10,-1,0,2,false);
        StructureParts.createSolidWall(worldIn,pos,colorStair.withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP),-3,10,-1,0,2,false);
        StructureParts.createSolidWall(worldIn,pos,Blocks.AIR.getDefaultState(),-3,10,0,0,6,true);
        StructureParts.createSolidWall(worldIn,pos,Blocks.AIR.getDefaultState(),0,10,-3,0,6,false);

        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-1,5,2,5,2,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-1,5,-2,5,2,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,2,5,-1,5,2,false);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-2,5,-1,5,2,false);

        StructureParts.createSolidWall(worldIn,pos,colorFence,0,6,2,1,0,true);
        StructureParts.createSolidWall(worldIn,pos,colorFence,0,6,-2,1,0,true);
        StructureParts.createSolidWall(worldIn,pos,colorFence,2,6,0,1,0,true);
        StructureParts.createSolidWall(worldIn,pos,colorFence,-2,6,0,1,0,true);
        StructureParts.createSolidFloor(worldIn,pos,colorWood,0,9,0,-1,1,-1,1);

        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-1,11,3,0,2,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-1,11,-3,0,2,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-3,11,-1,0,2,false);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,3,11,-1,0,2,false);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,2,11,-2,0,0,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,2,11,2,0,0,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-2,11,-2,0,0,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrick,-2,11,2,0,0,true);


        StructureParts.createSolidWall(worldIn,pos,stoneBrickSlabs.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM),-1,12,3,0,2,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrickSlabs.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM),-1,12,-3,0,2,true);
        StructureParts.createSolidWall(worldIn,pos,stoneBrickSlabs.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM),-3,12,-1,0,2,false);
        StructureParts.createSolidWall(worldIn,pos,stoneBrickSlabs.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM),3,12,-1,0,2,false);
        StructureParts.createSolidWall(worldIn,pos,Blocks.AIR.getDefaultState(),-3,12,0,0,6,true);
        StructureParts.createSolidWall(worldIn,pos,Blocks.AIR.getDefaultState(),0,12,-3,0,6,false);

        StructureParts.generateFloorLoot(worldIn,pos,0,0,0,-1,1,0,0,-1,1,"normal");
        StructureParts.generateFloorLoot(worldIn,pos,0,5,0,-1,1,0,0,-1,1,"normal");
        StructureParts.generateFloorLoot(worldIn,pos,0,10,0,-1,1,0,0,-1,1,"normal");
        worldIn.setBlockState(pos.add(0,10,0), BlockMachineBase.machineBase.getDefaultState());

        StructureParts.createSolidWall(worldIn,pos, colorWood,-1,0,2,3,0,true);
        StructureParts.createSolidWall(worldIn,pos, Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING,EnumFacing.NORTH),-1,0,1,10,0,true);

        return true;
    }
}
