package com.mowmaster.dust.world.structures.allbiomestructures;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.blocks.buildingblocks.BlockCrate;
import com.mowmaster.dust.blocks.buildingblocks.BlockPot;
import com.mowmaster.dust.blocks.crystal.BlockCrystal;
import com.mowmaster.dust.world.structures.structurebits.StructureParts;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.ArrayList;
import java.util.Random;

import static net.minecraft.block.BlockSand.VARIANT;
import static net.minecraft.block.BlockSandStone.TYPE;

/**
 * Created by KingMowmaster on 4/3/2017.
 */
public class SmallSandWell extends WorldGenerator
{
    private final IBlockState sandstoneChiseled = Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.CHISELED);
    private final IBlockState sandstoneSmooth = Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.SMOOTH);
    private final IBlockState sandstoneDefault = Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.DEFAULT);
    private final IBlockState sand = Blocks.SAND.getDefaultState().withProperty(VARIANT,BlockSand.EnumType.SAND);

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos) {
        IBlockState mobspawnerpicked = Blocks.MOB_SPAWNER.getDefaultState();
        IBlockState crystalblock = BlockCrystal.redCrystalFive.getDefaultState();
        ArrayList<IBlockState> BlockLoot = new ArrayList<>();
        BlockLoot.add(mobspawnerpicked);
        BlockLoot.add(BlockCrate.crate1.getDefaultState());
        BlockLoot.add(BlockPot.pot1.getDefaultState());
        BlockLoot.add(BlockCrate.crate1.getDefaultState());
        BlockLoot.add(BlockPot.pot1.getDefaultState());
        BlockLoot.add(BlockCrate.crate1.getDefaultState());
        BlockLoot.add(BlockPot.pot1.getDefaultState());
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



        while (!(worldIn.getBlockState(pos).equals(Blocks.SAND.getDefaultState()))&& pos.getY() > -50)
        {
            pos = pos.down();
        }

        StructureParts.clearArea(worldIn,pos,0,2,-2,2,-2,2);
        StructureParts.createSolidFloor(worldIn,pos,sandstoneDefault,0,0,0,-2,2,-2,2);
        spawnSandPiller(worldIn,pos,-2,1,2);
        spawnSandPiller(worldIn,pos,-2,1,-2);
        spawnSandPiller(worldIn,pos,2,1,2);
        spawnSandPiller(worldIn,pos,2,1,-2);
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

    private void spawnSandPiller(World worldIn,BlockPos pos, int x, int y, int z)
    {
        worldIn.setBlockState(pos.add(x,y,z),sandstoneSmooth);
        worldIn.setBlockState(pos.add(x,y+1,z),sandstoneSmooth);
        worldIn.setBlockState(pos.add(x,y+2,z),sandstoneChiseled);
    }

}



//[-2+2][-1+2][+0+2][+1+2][+2+2]
//[-2+1][-1+1][+0+1][+1+1][+2+1]
//[-2+0][-1+0][+0+0][+1+0][+2+0]
//[-2-1][-1-1][+0-1][+1-1][+2-1]
//[-2-2][-1-2][+0-2][+1-2][+2-2]
