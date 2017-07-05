package com.mowmaster.dust.world.structures;

import com.mowmaster.dust.blocks.BlockCrystal;
import com.mowmaster.dust.blocks.BlockRegistry;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.ArrayList;
import java.util.Random;

import static com.mowmaster.dust.world.structures.StructureParts.*;
import static net.minecraft.block.BlockStoneBrick.VARIANT;

/**
 * Created by KingMowmaster on 6/30/2017.
 */
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
        worldIn.setBlockState(pos.add(0,1,0),BlockRegistry.machineBase.getDefaultState());
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
