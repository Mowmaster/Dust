package com.mowmaster.dust.world.structures.allbiomestructures;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.blocks.buildingblocks.BlockLootBlock;
import com.mowmaster.dust.enums.CrystalBlocks;
import com.mowmaster.dust.world.structures.structurebits.SpawnerTypesHostile;
import com.mowmaster.dust.world.structures.structurebits.StructureParts;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.ArrayList;
import java.util.Random;

import static net.minecraft.block.BlockStoneBrick.VARIANT;

public class SmallPiller extends WorldGenerator
{
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos) {

        Random rn = new Random();

        while (!(worldIn.getBlockState(pos).equals(Blocks.GRASS.getDefaultState()) || worldIn.getBlockState(pos).equals(Blocks.SAND.getDefaultState()))&& pos.getY() > -50)
        {
            pos = pos.down();
        }

        StructureParts.clearArea(worldIn,pos,1,4,-1,1,-1,1);

        IBlockState blockToSpawn = BlockLootBlock.lootblock.getDefaultState().withProperty(BlockLootBlock.TYPE, CrystalBlocks.CrystalLoot.CLUSTER);

        switch (rn.nextInt(5))
        {
            case 0:
            case 1:
            case 2:
                blockToSpawn = BlockLootBlock.lootblock.getDefaultState().withProperty(BlockLootBlock.TYPE, CrystalBlocks.CrystalLoot.PILLAR);
                break;
            case 3:
                blockToSpawn = BlockLootBlock.lootblock.getDefaultState().withProperty(BlockLootBlock.TYPE, CrystalBlocks.CrystalLoot.SPAWNH);
                break;
            case 4:
                blockToSpawn = BlockLootBlock.lootblock.getDefaultState().withProperty(BlockLootBlock.TYPE, CrystalBlocks.CrystalLoot.SPAWNP);
                break;
            default:
                blockToSpawn = BlockLootBlock.lootblock.getDefaultState().withProperty(BlockLootBlock.TYPE, CrystalBlocks.CrystalLoot.PILLAR);
                break;
        }

        StructureParts.buildLootPiller(worldIn, rand,pos,0,0,0);
        worldIn.setBlockState(pos.add(0,0,0),blockToSpawn);
        worldIn.setBlockState(pos.add(0,3,0),Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.CHISELED));
        worldIn.setBlockState(pos.add(0,4,0), BlockLootBlock.lootblock.getDefaultState().withProperty(BlockLootBlock.TYPE, CrystalBlocks.CrystalLoot.CLUSTER));


        return true;
    }
}
