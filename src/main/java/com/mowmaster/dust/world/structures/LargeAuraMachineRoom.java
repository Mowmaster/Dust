package com.mowmaster.dust.world.structures;

import com.mowmaster.dust.blocks.BlockRegistry;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.ArrayList;
import java.util.Random;





public class LargeAuraMachineRoom extends WorldGenerator
{
    public static void generateLight(World worldIn, BlockPos pos, int x, int z)
    {
        worldIn.setBlockState(pos.add(x+0, 3, z+0), Blocks.GLOWSTONE.getDefaultState());
        worldIn.setBlockState(pos.add(x-1, 3, z+1), Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
        worldIn.setBlockState(pos.add(x-1, 3, z), Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
        worldIn.setBlockState(pos.add(x-1, 3, z-1), Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));

        worldIn.setBlockState(pos.add(x+1, 3, z+1), Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
        worldIn.setBlockState(pos.add(x+1, 3, z), Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
        worldIn.setBlockState(pos.add(x+1, 3, z-1), Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));

        worldIn.setBlockState(pos.add(x, 3, z+1), Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
        worldIn.setBlockState(pos.add(x, 3, z-1), Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
    }


    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos)
    {
        ArrayList<IBlockState> RingMaterial = new ArrayList<>();
        RingMaterial.add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT));
        RingMaterial.add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED));
        RingMaterial.add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY));

        ArrayList<IBlockState> FloorMaterial = new ArrayList<>();
        FloorMaterial.add(Blocks.STONE.getDefaultState());
        FloorMaterial.add(Blocks.COBBLESTONE.getDefaultState());
        FloorMaterial.add(Blocks.GRAVEL.getDefaultState());

        ArrayList<IBlockState> CeilingMaterial = new ArrayList<>();
        CeilingMaterial.add(Blocks.STONE_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP).withProperty(BlockStoneSlab.VARIANT,BlockStoneSlab.EnumType.COBBLESTONE));
        CeilingMaterial.add(Blocks.STONE_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP).withProperty(BlockStoneSlab.VARIANT,BlockStoneSlab.EnumType.BRICK));

        ArrayList<IBlockState> FloorStairMaterialNorth = new ArrayList<>();
        FloorStairMaterialNorth.add(Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM));
        FloorStairMaterialNorth.add(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM));

        ArrayList<IBlockState> FloorStairMaterialEast = new ArrayList<>();
        FloorStairMaterialEast.add(Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM));
        FloorStairMaterialEast.add(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM));

        ArrayList<IBlockState> FloorStairMaterialSouth = new ArrayList<>();
        FloorStairMaterialSouth.add(Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM));
        FloorStairMaterialSouth.add(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM));

        ArrayList<IBlockState> FloorStairMaterialWest = new ArrayList<>();
        FloorStairMaterialWest.add(Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM));
        FloorStairMaterialWest.add(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM));

        ArrayList<IBlockState> FloorLoot = new ArrayList<>();
        FloorLoot.add(BlockRegistry.redCrystalFive.getDefaultState());
        FloorLoot.add(BlockRegistry.blueCrystalFive.getDefaultState());
        FloorLoot.add(BlockRegistry.yellowCrystalFive.getDefaultState());
        FloorLoot.add(BlockRegistry.purpleCrystalFive.getDefaultState());
        FloorLoot.add(BlockRegistry.orangeCrystalFive.getDefaultState());
        FloorLoot.add(BlockRegistry.greenCrystalFive.getDefaultState());
        FloorLoot.add(BlockRegistry.pot1.getDefaultState());
        FloorLoot.add(BlockRegistry.crate1.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());
        FloorLoot.add(Blocks.AIR.getDefaultState());




        Random random = new Random();

        while (!(worldIn.getBlockState(pos).equals(Blocks.GRASS.getDefaultState()) || worldIn.getBlockState(pos).equals(Blocks.SAND.getDefaultState()))&& pos.getY() > -50)
        {
            pos = pos.down();
        }
        Random rn = new Random();
        int roomdepth = rn.nextInt(30);
        pos = pos.add(0,(-50)+roomdepth,0);


        StructureParts.clearArea(worldIn,pos,-1,3,-5,5,-5,5);

        for(int c=-5;c<=5;c++) {
            for (int a = -5; a <= 5; a++)
            {
                int randomStoneBricksState = Math.abs(random.nextInt(RingMaterial.size() - 1));
                worldIn.setBlockState(pos.add(a, -1, c), RingMaterial.get(randomStoneBricksState));
            }
        }
        //clear 4 areas leaving outlineand plus walkways
        //bottomright
        for(int z=-1;z<=2;z++)
        {
            for (int x=-2;x<=1;x++)
                worldIn.setBlockState(pos.add(x+3,-1,z-3),Blocks.AIR.getDefaultState());
        }
        //bottomleft
        for(int z=-1;z<=2;z++)
        {
            for (int x=-1;x<=2;x++)
                worldIn.setBlockState(pos.add(x-3,-1,z-3),Blocks.AIR.getDefaultState());
        }
        //topright
        for(int z=-2;z<=1;z++)
        {
            for (int x=-2;x<=1;x++)
                worldIn.setBlockState(pos.add(x+3,-1,z+3),Blocks.AIR.getDefaultState());
        }
        //topleft
        for(int z=-2;z<=1;z++)
        {
            for (int x=-1;x<=2;x++)
                worldIn.setBlockState(pos.add(x-3,-1,z+3),Blocks.AIR.getDefaultState());
        }

        worldIn.setBlockState(pos.add(0,-1,0),Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED));

        for(int c=-1;c<=1;c++) {
            for (int a = -1; a <= 1; a++)
            {
                int randomStoneState = Math.abs(random.nextInt(FloorMaterial.size() - 1));
                worldIn.setBlockState(pos.add(a+3, -1, c+3), FloorMaterial.get(randomStoneState));
                worldIn.setBlockState(pos.add(a+3, -1, c+3), FloorMaterial.get(randomStoneState));
            }
        }
        for(int c=-1;c<=1;c++) {
            for (int a = -1; a <= 1; a++)
            {
                int randomStoneState = Math.abs(random.nextInt(FloorMaterial.size() - 1));
                worldIn.setBlockState(pos.add(a-3, -1, c+3), FloorMaterial.get(randomStoneState));
            }
        }
        for(int c=-1;c<=1;c++) {
            for (int a = -1; a <= 1; a++)
            {
                int randomStoneState = Math.abs(random.nextInt(FloorMaterial.size() - 1));
                worldIn.setBlockState(pos.add(a+3, -1, c-3), FloorMaterial.get(randomStoneState));
            }
        }
        for(int c=-1;c<=1;c++) {
            for (int a = -1; a <= 1; a++)
            {
                int randomStoneState = Math.abs(random.nextInt(FloorMaterial.size() - 1));
                worldIn.setBlockState(pos.add(a-3, -1, c-3), FloorMaterial.get(randomStoneState));
            }
        }




        //Stair bit, starting rightsidesouth
        for (int z = -4; z <= -1; z++)
        {
            int randomStoneState = Math.abs(random.nextInt(FloorStairMaterialEast .size() - 1));
            worldIn.setBlockState(pos.add(1, -1, z), FloorStairMaterialEast .get(randomStoneState));
        }
        for (int z = 1; z <= 4; z++)
        {
            int randomStoneState = Math.abs(random.nextInt(FloorStairMaterialEast .size() - 1));
            worldIn.setBlockState(pos.add(1, -1, z), FloorStairMaterialEast.get(randomStoneState));
        }
        //Stair bit, starting leftsidenorth
        for (int z = -4; z <= -1; z++)
        {
            int randomStoneState = Math.abs(random.nextInt(FloorStairMaterialWest .size() - 1));
            worldIn.setBlockState(pos.add(-1, -1, z), FloorStairMaterialWest .get(randomStoneState));
        }
        for (int z = 1; z <= 4; z++)
        {
            int randomStoneState = Math.abs(random.nextInt(FloorStairMaterialWest .size() - 1));
            worldIn.setBlockState(pos.add(-1, -1, z), FloorStairMaterialWest.get(randomStoneState));
        }

        for (int x = 2; x <= 4; x++)
        {
            int randomStoneState = Math.abs(random.nextInt(FloorStairMaterialNorth .size() - 1));
            worldIn.setBlockState(pos.add(x, -1, -1), FloorStairMaterialNorth.get(randomStoneState));
        }
        for (int x = -4; x <= -2; x++)
        {
            int randomStoneState = Math.abs(random.nextInt(FloorStairMaterialNorth .size() - 1));
            worldIn.setBlockState(pos.add(x, -1, -1), FloorStairMaterialNorth.get(randomStoneState));
        }
        for (int x = 2; x <= 4; x++)
        {
            int randomStoneState = Math.abs(random.nextInt(FloorStairMaterialSouth .size() - 1));
            worldIn.setBlockState(pos.add(x, -1, 1), FloorStairMaterialSouth.get(randomStoneState));
        }
        for (int x = -4; x <= -2; x++)
        {
            int randomStoneState = Math.abs(random.nextInt(FloorStairMaterialSouth .size() - 1));
            worldIn.setBlockState(pos.add(x, -1, 1), FloorStairMaterialSouth.get(randomStoneState));
        }

        //Loot on Floor
        worldIn.setBlockState(pos.add(0, 0, 0), BlockRegistry.machineBase.getDefaultState());

        for(int c=-1;c<=1;c++) {
            for (int a = -1; a <= 1; a++)
            {
                int randomStoneState = Math.abs(random.nextInt(FloorLoot.size() - 1));
                worldIn.setBlockState(pos.add(a-3, 0, c-3), FloorLoot.get(randomStoneState));
            }
        }
        for(int c=-1;c<=1;c++) {
            for (int a = -1; a <= 1; a++)
            {
                int randomStoneState = Math.abs(random.nextInt(FloorLoot.size() - 1));
                worldIn.setBlockState(pos.add(a-3, 0, c+3), FloorLoot.get(randomStoneState));
            }
        }
        for(int c=-1;c<=1;c++) {
            for (int a = -1; a <= 1; a++)
            {
                int randomStoneState = Math.abs(random.nextInt(FloorLoot.size() - 1));
                worldIn.setBlockState(pos.add(a+3, 0, c-3), FloorLoot.get(randomStoneState));
            }
        }
        for(int c=-1;c<=1;c++) {
            for (int a = -1; a <= 1; a++)
            {
                int randomStoneState = Math.abs(random.nextInt(FloorLoot.size() - 1));
                worldIn.setBlockState(pos.add(a+3, 0, c+3), FloorLoot.get(randomStoneState));
            }
        }

        //ceiling
        for(int c=-5;c<=5;c++) {
            for (int a = -5; a <= 5; a++)
            {
                int randomStoneBricksState = Math.abs(random.nextInt(CeilingMaterial.size() - 1));
                worldIn.setBlockState(pos.add(a, 3, c), CeilingMaterial.get(randomStoneBricksState));
            }
        }

        generateLight(worldIn,pos,0,0);
        generateLight(worldIn,pos,-4,0);
        generateLight(worldIn,pos,4,0);
        generateLight(worldIn,pos,-4,-4);
        generateLight(worldIn,pos,-4,4);
        generateLight(worldIn,pos,0,4);
        generateLight(worldIn,pos,0,-4);
        generateLight(worldIn,pos,4,4);
        generateLight(worldIn,pos,4,-4);

        return true;
    }
}
