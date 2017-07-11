package com.mowmaster.dust.world.structures.allbiomestructures;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.world.structures.structurebits.StructureParts;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.ArrayList;
import java.util.Random;

import static net.minecraft.block.BlockStoneBrick.VARIANT;


public class SmallStoneWell extends WorldGenerator
{
    private final IBlockState stoneDefault = Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.DEFAULT);
    private final IBlockState stoneCracked = Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.CRACKED);
    private final IBlockState stoneMossy = Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.MOSSY);
    private final IBlockState stoneChiseled = Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.CHISELED);


    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos) {
        //System.out.println(Blocks.GRASS.getDefaultState());
        //System.out.println(worldIn.getBlockState(pos));
        IBlockState mobspawnerpicked = Blocks.MOB_SPAWNER.getDefaultState();
        IBlockState crystalblock = BlockRegistry.redCrystalFive.getDefaultState();
        ArrayList<IBlockState> BlockLoot = new ArrayList<>();
        BlockLoot.add(mobspawnerpicked);
        BlockLoot.add(BlockRegistry.crate1.getDefaultState());
        BlockLoot.add(BlockRegistry.pot1.getDefaultState());
        BlockLoot.add(BlockRegistry.crate1.getDefaultState());
        BlockLoot.add(BlockRegistry.pot1.getDefaultState());
        BlockLoot.add(BlockRegistry.crate1.getDefaultState());
        BlockLoot.add(BlockRegistry.pot1.getDefaultState());
        BlockLoot.add(crystalblock);
        BlockLoot.add(crystalblock);
        BlockLoot.add(crystalblock);
        BlockLoot.add(crystalblock);
        BlockLoot.add(crystalblock);
        BlockLoot.add(crystalblock);
        BlockLoot.add(crystalblock);
        BlockLoot.add(crystalblock);
        BlockLoot.add(crystalblock);

        Random rn = new Random();
        int lootblock = Math.abs(rn.nextInt(BlockLoot.size() - 1));



        while (!(worldIn.getBlockState(pos).equals(Blocks.GRASS.getDefaultState()))&& pos.getY() > -50)
        {
            pos = pos.down();
        }

        StructureParts.clearArea(worldIn,pos,0,2,-2,2,-2,2);
        StructureParts.createSolidFloor(worldIn,pos,stoneDefault,0,0,0,-2,2,-2,2);
        spawnStonePiller(worldIn,pos,-2,1,2);
        spawnStonePiller(worldIn,pos,-2,1,-2);
        spawnStonePiller(worldIn,pos,2,1,2);
        spawnStonePiller(worldIn,pos,2,1,-2);
        worldIn.setBlockState(pos.add(-1,0,0),Blocks.AIR.getDefaultState());
        worldIn.setBlockState(pos.add(0,0,1),Blocks.AIR.getDefaultState());
        worldIn.setBlockState(pos.add(0,0,-1),Blocks.AIR.getDefaultState());
        worldIn.setBlockState(pos.add(1,0,0),Blocks.AIR.getDefaultState());
        if(BlockLoot.get(lootblock).equals(crystalblock))
        {
            StructureParts.spawnCrystal(worldIn,rand,pos,0,1,0,"UP");
        }
        else{
            StructureParts.spawnSpawnerPassive(worldIn,rand,pos,BlockLoot,lootblock,0,1,0,mobspawnerpicked);
            worldIn.setBlockState(pos.add(0,1,0),BlockLoot.get(lootblock));
        }
        worldIn.setBlockState(pos.add(0,2,0),Blocks.FLOWING_WATER.getDefaultState());


        return true;
    }

    private void spawnStonePiller(World worldIn,BlockPos pos, int x, int y, int z)
    {
        ArrayList<IBlockState> PillarMaterial = new ArrayList<>();
        PillarMaterial.add(Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.DEFAULT));
        PillarMaterial.add(Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.CRACKED));
        PillarMaterial.add(Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.MOSSY));

        Random rn = new Random();
        int stone = Math.abs(rn.nextInt(PillarMaterial.size() - 1));
        int stone1 = Math.abs(rn.nextInt(PillarMaterial.size() - 1));
        worldIn.setBlockState(pos.add(x,y,z),PillarMaterial.get(stone));
        worldIn.setBlockState(pos.add(x,y+1,z),PillarMaterial.get(stone1));
        worldIn.setBlockState(pos.add(x,y+2,z),stoneChiseled);
    }

}



//[-2+2][-1+2][+0+2][+1+2][+2+2]
//[-2+1][-1+1][+0+1][+1+1][+2+1]
//[-2+0][-1+0][+0+0][+1+0][+2+0]
//[-2-1][-1-1][+0-1][+1-1][+2-1]
//[-2-2][-1-2][+0-2][+1-2][+2-2]
