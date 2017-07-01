package com.mowmaster.dust.world.structures;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.world.structures.LootTables.SpawnerTypesPassive;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.*;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
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
public class SmallWell extends WorldGenerator
{
    private final IBlockState sandstoneChiseled = Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.CHISELED);
    private final IBlockState sandstoneSmooth = Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.SMOOTH);
    private final IBlockState sandstoneDefault = Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.DEFAULT);
    private final IBlockState sand = Blocks.SAND.getDefaultState().withProperty(VARIANT,BlockSand.EnumType.SAND);

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos) {
        //System.out.println(Blocks.GRASS.getDefaultState());
        //System.out.println(worldIn.getBlockState(pos));
        IBlockState mobspawnerpicked = Blocks.MOB_SPAWNER.getDefaultState();
        IBlockState crystalblock = BlockRegistry.redCrystalFive.getDefaultState();
        ArrayList<IBlockState> BlockLoot = new ArrayList<>();
        BlockLoot.add(mobspawnerpicked);
        BlockLoot.add(BlockRegistry.machineBase.getDefaultState());
        BlockLoot.add(BlockRegistry.machineBase.getDefaultState());
        BlockLoot.add(BlockRegistry.machineBase.getDefaultState());
        BlockLoot.add(crystalblock);
        BlockLoot.add(crystalblock);
        BlockLoot.add(crystalblock);
        BlockLoot.add(crystalblock);
        BlockLoot.add(crystalblock);

        Random rn = new Random();
        int lootblock = Math.abs(rn.nextInt(BlockLoot.size() - 1));



        while (!(worldIn.getBlockState(pos).equals(Blocks.GRASS.getDefaultState()) || worldIn.getBlockState(pos).equals(Blocks.SAND.getDefaultState()))&& pos.getY() > -50)
        {
            pos = pos.down();
        }

        StructureParts.clearArea(worldIn,pos,0,2,-2,2,-2,2);
        //setBlockAndNotifyAdequately();
        //Layer y-1 Top Row
        worldIn.setBlockState(pos.add(-2,-1,2),Blocks.SAND.getDefaultState().withProperty(VARIANT,BlockSand.EnumType.SAND));
        worldIn.setBlockState(pos.add(-1,-1,2),Blocks.SAND.getDefaultState().withProperty(VARIANT,BlockSand.EnumType.SAND));
        worldIn.setBlockState(pos.add(0,-1,2),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.DEFAULT));
        worldIn.setBlockState(pos.add(1,-1,2),Blocks.SAND.getDefaultState().withProperty(VARIANT,BlockSand.EnumType.SAND));
        worldIn.setBlockState(pos.add(2,-1,2),Blocks.SAND.getDefaultState().withProperty(VARIANT,BlockSand.EnumType.SAND));
        //Layer y-1 2nd from Top Row
        worldIn.setBlockState(pos.add(-2,-1,1),Blocks.SAND.getDefaultState().withProperty(VARIANT,BlockSand.EnumType.SAND));
        worldIn.setBlockState(pos.add(-1,-1,1),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.DEFAULT));
        worldIn.setBlockState(pos.add(0,-1,1),Blocks.AIR.getDefaultState());
        worldIn.setBlockState(pos.add(1,-1,1),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.DEFAULT));
        worldIn.setBlockState(pos.add(2,-1,1),Blocks.SAND.getDefaultState().withProperty(VARIANT,BlockSand.EnumType.SAND));
        //Layer y-1 Middle Row
        worldIn.setBlockState(pos.add(-2,-1,0),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.DEFAULT));
        worldIn.setBlockState(pos.add(-1,-1,0),Blocks.AIR.getDefaultState());
        worldIn.setBlockState(pos.add(0,-1,0),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.SMOOTH));
        worldIn.setBlockState(pos.add(1,-1,0),Blocks.AIR.getDefaultState());
        worldIn.setBlockState(pos.add(2,-1,0),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.DEFAULT));
        //Layer y-1 4nd from Top Row
        worldIn.setBlockState(pos.add(-2,-1,-1),Blocks.SAND.getDefaultState().withProperty(VARIANT,BlockSand.EnumType.SAND));
        worldIn.setBlockState(pos.add(-1,-1,-1),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.DEFAULT));
        worldIn.setBlockState(pos.add(0,-1,-1),Blocks.AIR.getDefaultState());
        worldIn.setBlockState(pos.add(1,-1,-1),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.DEFAULT));
        worldIn.setBlockState(pos.add(2,-1,-1),Blocks.SAND.getDefaultState().withProperty(VARIANT,BlockSand.EnumType.SAND));
        //Layer y-1 Bottom Row
        worldIn.setBlockState(pos.add(-2,-1,-2),Blocks.SAND.getDefaultState().withProperty(VARIANT,BlockSand.EnumType.SAND));
        worldIn.setBlockState(pos.add(-1,-1,-2),Blocks.SAND.getDefaultState().withProperty(VARIANT,BlockSand.EnumType.SAND));
        worldIn.setBlockState(pos.add(0,-1,-2),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.DEFAULT));
        worldIn.setBlockState(pos.add(1,-1,-2),Blocks.SAND.getDefaultState().withProperty(VARIANT,BlockSand.EnumType.SAND));
        worldIn.setBlockState(pos.add(2,-1,-2),Blocks.SAND.getDefaultState().withProperty(VARIANT,BlockSand.EnumType.SAND));



        //Layer y Left Top
        worldIn.setBlockState(pos.add(-2,0,2),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.SMOOTH));
        //Layer y Right Top
        worldIn.setBlockState(pos.add(2,0,2),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.SMOOTH));
        //Layer y Middle
        if(BlockLoot.get(lootblock).equals(crystalblock))
        {
            StructureParts.spawnCrystal(worldIn,rand,pos,0,0,0,"UP");
        }
        else{
            StructureParts.spawnSpawnerPassive(worldIn,rand,pos,BlockLoot,lootblock,0,0,0,mobspawnerpicked);
            worldIn.setBlockState(pos.add(0,0,0),BlockLoot.get(lootblock));
        }

        //Layer y Left Bottom
        worldIn.setBlockState(pos.add(-2,0,-2),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.SMOOTH));
        //Layer y Left Bottom
        worldIn.setBlockState(pos.add(2,0,-2),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.SMOOTH));

        //Layer y+1 Left Top
        worldIn.setBlockState(pos.add(-2,1,2),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.SMOOTH));
        //Layer y+1 Right Top
        worldIn.setBlockState(pos.add(2,1,2),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.SMOOTH));
        //Layer y+1 Middle
        worldIn.setBlockState(pos.add(0,1,0),Blocks.FLOWING_WATER.getDefaultState());
        //Layer y+1 Left Bottom
        worldIn.setBlockState(pos.add(-2,1,-2),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.SMOOTH));
        //Layer y+1 Left Bottom
        worldIn.setBlockState(pos.add(2,1,-2),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.SMOOTH));

        //Layer y+2 Left Top
        worldIn.setBlockState(pos.add(-2,2,2),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.CHISELED));
        //Layer y+2 Right Top
        worldIn.setBlockState(pos.add(2,2,2),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.CHISELED));
        //Layer y+2 Left Bottom
        worldIn.setBlockState(pos.add(-2,2,-2),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.CHISELED));
        //Layer y+2 Left Bottom
        worldIn.setBlockState(pos.add(2,2,-2),Blocks.SANDSTONE.getDefaultState().withProperty(TYPE,BlockSandStone.EnumType.CHISELED));

        return true;
    }

}



//[-2+2][-1+2][+0+2][+1+2][+2+2]
//[-2+1][-1+1][+0+1][+1+1][+2+1]
//[-2+0][-1+0][+0+0][+1+0][+2+0]
//[-2-1][-1-1][+0-1][+1-1][+2-1]
//[-2-2][-1-2][+0-2][+1-2][+2-2]
