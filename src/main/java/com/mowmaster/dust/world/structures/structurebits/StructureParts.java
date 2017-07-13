package com.mowmaster.dust.world.structures.structurebits;

import com.mowmaster.dust.blocks.BlockCrystal;
import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.world.structures.structurebits.SpawnerTypesHostile;
import com.mowmaster.dust.world.structures.structurebits.SpawnerTypesPassive;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

import static net.minecraft.block.BlockStoneBrick.VARIANT;


public class StructureParts
{

    public static void clearArea(World worldIn,BlockPos pos,int heightmin,int heightmax, int zmin, int zmax, int xmin, int xmax)
{
    for(int y=heightmin;y<=heightmax;y++)
    {
        for(int z=zmin;z<=zmax;z++)
        {
            for (int x=xmin;x<=xmax;x++)
                worldIn.setBlockState(pos.add(x,y,z),Blocks.AIR.getDefaultState());
        }
    }
}

    public static void createSolidFloor(World worldIn,BlockPos pos,IBlockState blockState,int x, int y,int z, int zmin, int zmax, int xmin, int xmax)
    {
        for(int c=zmin;c<=zmax;c++) {
            for (int a = xmin; a <= xmax; a++)
                worldIn.setBlockState(pos.add(a+x, 0+y, c+z), blockState);
        }
    }

    public static void createSolidWall(World worldIn, BlockPos pos,IBlockState blockState, int x, int y, int z, int wallHeight,int wallLength, Boolean isXDirection)
    {
        if(isXDirection.equals(true))
        {
            for (int a=0;a<=wallLength;a++)
            {
                for (int b=0;b<=wallHeight; b++)
                {
                    worldIn.setBlockState(pos.add(x+a,y+b,z),blockState);
                }
            }
        }
        else{
            for (int c=0;c<=wallLength;c++)
            {
                for (int b=0;b<=wallHeight; b++)
                {
                    worldIn.setBlockState(pos.add(x,y+b,z+c),blockState);
                }
            }
        }
    }

    public static void buildLootPiller(World worldIn, Random rand, BlockPos pos, int x, int y, int z)
    {
        ArrayList<IBlockState> PillarMaterial = new ArrayList<>();
        PillarMaterial.add(Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.DEFAULT));
        PillarMaterial.add(Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.CRACKED));
        PillarMaterial.add(Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.MOSSY));

        IBlockState mobspawnerpicked = Blocks.MOB_SPAWNER.getDefaultState();
        ArrayList<IBlockState> PillarBottom = new ArrayList<>();
        PillarBottom.add(mobspawnerpicked);
        PillarBottom.add(Blocks.DIAMOND_ORE.getDefaultState());
        PillarBottom.add(Blocks.MAGMA.getDefaultState());
        PillarBottom.add(Blocks.NETHER_WART_BLOCK.getDefaultState());
        PillarBottom.add(Blocks.IRON_BLOCK.getDefaultState());
        PillarBottom.add(Blocks.IRON_BLOCK.getDefaultState());
        PillarBottom.add(Blocks.GOLD_ORE.getDefaultState());
        PillarBottom.add(Blocks.GOLD_ORE.getDefaultState());
        PillarBottom.add(Blocks.GOLD_ORE.getDefaultState());
        PillarBottom.add(Blocks.IRON_ORE.getDefaultState());
        PillarBottom.add(Blocks.IRON_ORE.getDefaultState());
        PillarBottom.add(Blocks.IRON_ORE.getDefaultState());
        PillarBottom.add(Blocks.COAL_BLOCK.getDefaultState());
        PillarBottom.add(Blocks.COAL_BLOCK.getDefaultState());
        PillarBottom.add(Blocks.COAL_BLOCK.getDefaultState());
        PillarBottom.add(Blocks.LAVA.getDefaultState());
        PillarBottom.add(Blocks.LAVA.getDefaultState());
        PillarBottom.add(Blocks.COAL_ORE.getDefaultState());
        PillarBottom.add(Blocks.COAL_ORE.getDefaultState());
        PillarBottom.add(Blocks.COAL_ORE.getDefaultState());
        PillarBottom.add(Blocks.COAL_ORE.getDefaultState());
        PillarBottom.add(Blocks.BONE_BLOCK.getDefaultState());
        PillarBottom.add(Blocks.BONE_BLOCK.getDefaultState());
        PillarBottom.add(Blocks.BONE_BLOCK.getDefaultState());
        PillarBottom.add(Blocks.BONE_BLOCK.getDefaultState());
        PillarBottom.add(Blocks.BONE_BLOCK.getDefaultState());


        Random random = new Random();
        int pillerbottomloot = Math.abs(random.nextInt(PillarBottom.size() - 1));
        int pillermaterialchosen = Math.abs(random.nextInt(PillarMaterial.size() - 1));
        int pillermaterialchosen2 = Math.abs(random.nextInt(PillarMaterial.size() - 1));
        int pillermaterialchosen3 = Math.abs(random.nextInt(PillarMaterial.size() - 1));

        spawnSpawnerHostile(worldIn,rand,pos,PillarBottom,pillerbottomloot,x,y,z,mobspawnerpicked);
        worldIn.setBlockState(pos.add(x,y,z),PillarBottom.get(pillerbottomloot));
        worldIn.setBlockState(pos.add(x,y+1,z),PillarMaterial.get(pillermaterialchosen));
        worldIn.setBlockState(pos.add(x,y+2,z),PillarMaterial.get(pillermaterialchosen2));
        worldIn.setBlockState(pos.add(x,y+3,z),PillarMaterial.get(pillermaterialchosen3));
        worldIn.setBlockState(pos.add(x,y+4,z),Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.CHISELED));

    }

    public static void buildFloorThreeByThree(World worldIn, Random rand, BlockPos pos, int x, int y, int z)
    {
        ArrayList<IBlockState> PillarMaterial = new ArrayList<>();
        PillarMaterial.add(Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.DEFAULT));
        PillarMaterial.add(Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.CRACKED));
        PillarMaterial.add(Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.MOSSY));

        Random random = new Random();

        for(int c=-1;c<=1;c++)
        {

            int pillermaterialchosen = Math.abs(random.nextInt(PillarMaterial.size() - 1));
            worldIn.setBlockState(pos.add(x-1,y,z+c),PillarMaterial.get(pillermaterialchosen));
            int pillermaterialchosen1 = Math.abs(random.nextInt(PillarMaterial.size() - 1));
            worldIn.setBlockState(pos.add(x+0,y,z+c),PillarMaterial.get(pillermaterialchosen1));
            int pillermaterialchosen2 = Math.abs(random.nextInt(PillarMaterial.size() - 1));
            worldIn.setBlockState(pos.add(x+1,y,z+c),PillarMaterial.get(pillermaterialchosen2));

        }
        worldIn.setBlockState(pos.add(x,y,z),Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.CHISELED));

    }

    public static void spawnCrystal(World worldIn, Random rand, BlockPos pos, int x, int y, int z, String facing)
    {
        ArrayList<IBlockState> BlockCrystalTop = new ArrayList<>();
        BlockCrystalTop.add(BlockRegistry.redCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.blueCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.yellowCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.purpleCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.orangeCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.greenCrystalFive.getDefaultState());

        BlockCrystalTop.add(BlockRegistry.redCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.blueCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.yellowCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.purpleCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.orangeCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.greenCrystalFive.getDefaultState());

        BlockCrystalTop.add(BlockRegistry.redCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.blueCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.yellowCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.purpleCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.orangeCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.greenCrystalFive.getDefaultState());

        BlockCrystalTop.add(BlockRegistry.redCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.blueCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.yellowCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.purpleCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.orangeCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.greenCrystalFive.getDefaultState());

        BlockCrystalTop.add(BlockRegistry.redCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.blueCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.yellowCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.purpleCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.orangeCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.greenCrystalFive.getDefaultState());

        BlockCrystalTop.add(BlockRegistry.whiteCrystalFive.getDefaultState());
        BlockCrystalTop.add(BlockRegistry.blackCrystalFive.getDefaultState());

        Random rn = new Random();
        int crystalcolor = Math.abs(rn.nextInt(BlockCrystalTop.size() - 1));

        worldIn.setBlockState(pos.add(x,y,z),BlockCrystalTop.get(crystalcolor).withProperty(BlockCrystal.FACING, EnumFacing.byName(facing)));
    }

    public static void spawnSpawnerHostile(World worldIn,Random rand, BlockPos pos,ArrayList<IBlockState> LootListName,int lootlist, int x, int y, int z, IBlockState mobspawnerpicked)
    {
        if(LootListName.get(lootlist).equals(mobspawnerpicked)) {
            worldIn.setBlockState(pos.add(x, y, z), mobspawnerpicked, 2);
            TileEntity tileentity = worldIn.getTileEntity(pos.add(x, y, z));

            if (tileentity instanceof TileEntityMobSpawner) {
                ((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setEntityId(SpawnerTypesHostile.getRandomDungeonMob(rand));
            }
        }
    }

    public static void spawnSpawnerPassive(World worldIn,Random rand, BlockPos pos,ArrayList<IBlockState> LootListName,int lootlist, int x, int y, int z, IBlockState mobspawnerpicked)
    {
        if(LootListName.get(lootlist).equals(mobspawnerpicked)) {
            worldIn.setBlockState(pos.add(x, y, z), mobspawnerpicked, 2);
            TileEntity tileentity = worldIn.getTileEntity(pos.add(x, y, z));

            if (tileentity instanceof TileEntityMobSpawner) {
                ((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setEntityId(SpawnerTypesPassive.getRandomDungeonMob(rand));
            }
        }
    }

    public static void generateFloorLoot(World worldIn,BlockPos pos,int x, int y, int z,int xmin, int xmax, int ymin, int ymax, int zmin, int zmax, String lootPoolType)
    {
        Random random = new Random();


        //3air per 1 loot 25%
        ArrayList<IBlockState> FloorLoot1 = new ArrayList<>();
        FloorLoot1.add(BlockRegistry.redCrystalFive.getDefaultState());
        FloorLoot1.add(BlockRegistry.blueCrystalFive.getDefaultState());
        FloorLoot1.add(BlockRegistry.yellowCrystalFive.getDefaultState());
        FloorLoot1.add(BlockRegistry.purpleCrystalFive.getDefaultState());
        FloorLoot1.add(BlockRegistry.orangeCrystalFive.getDefaultState());
        FloorLoot1.add(BlockRegistry.greenCrystalFive.getDefaultState());
        FloorLoot1.add(BlockRegistry.pot1.getDefaultState());
        FloorLoot1.add(BlockRegistry.crate1.getDefaultState());
        FloorLoot1.add(BlockRegistry.pot1.getDefaultState());
        FloorLoot1.add(BlockRegistry.crate1.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());
        FloorLoot1.add(Blocks.AIR.getDefaultState());

        //1/6 ~16.6%
        ArrayList<IBlockState> FloorLoot2 = new ArrayList<>();
        FloorLoot2.add(BlockRegistry.redCrystalFive.getDefaultState());
        FloorLoot2.add(BlockRegistry.blueCrystalFive.getDefaultState());
        FloorLoot2.add(BlockRegistry.yellowCrystalFive.getDefaultState());
        FloorLoot2.add(BlockRegistry.purpleCrystalFive.getDefaultState());
        FloorLoot2.add(BlockRegistry.orangeCrystalFive.getDefaultState());
        FloorLoot2.add(BlockRegistry.greenCrystalFive.getDefaultState());
        FloorLoot2.add(BlockRegistry.pot1.getDefaultState());
        FloorLoot2.add(BlockRegistry.crate1.getDefaultState());
        FloorLoot2.add(BlockRegistry.pot1.getDefaultState());
        FloorLoot2.add(BlockRegistry.crate1.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());
        FloorLoot2.add(Blocks.AIR.getDefaultState());

        ArrayList<IBlockState> DustType = new ArrayList<>();
        DustType.add(BlockRegistry.redDust.getDefaultState());
        DustType.add(BlockRegistry.blueDust.getDefaultState());
        DustType.add(BlockRegistry.yellowDust.getDefaultState());
        DustType.add(BlockRegistry.purpleDust.getDefaultState());
        DustType.add(BlockRegistry.orangeDust.getDefaultState());
        DustType.add(BlockRegistry.greenDust.getDefaultState());
        DustType.add(BlockRegistry.whiteDust.getDefaultState());
        DustType.add(BlockRegistry.blackDust.getDefaultState());
        int randomDust = Math.abs(random.nextInt(DustType.size() - 1));

        ArrayList<IBlockState> FloorLoot3 = new ArrayList<>();
        FloorLoot3.add(DustType.get(randomDust));
        FloorLoot3.add(Blocks.AIR.getDefaultState());
        FloorLoot3.add(Blocks.AIR.getDefaultState());
        FloorLoot3.add(Blocks.AIR.getDefaultState());


        for(int b=ymin;b<=ymax;b++)
        {
            for(int c=zmin;c<=zmax;c++) {
                for (int a = xmin; a <= xmax; a++)
                {
                    if(lootPoolType == "normal")
                    {
                        int randomStoneState = Math.abs(random.nextInt(FloorLoot1.size() - 1));
                        worldIn.setBlockState(pos.add(a+x, b+y, c+z), FloorLoot1.get(randomStoneState));
                    }
                    if(lootPoolType == "reduced")
                    {
                        int randomStoneState = Math.abs(random.nextInt(FloorLoot2.size() - 1));
                        worldIn.setBlockState(pos.add(a+x, b+y, c+z), FloorLoot2.get(randomStoneState));
                    }
                    if(lootPoolType == "dust")
                    {
                        int randomDustOrAir = Math.abs(random.nextInt(FloorLoot3.size() - 1));
                        worldIn.setBlockState(pos.add(a+x, b+y, c+z), FloorLoot3.get(randomDustOrAir));
                    }

                }
            }
        }

    }
}
