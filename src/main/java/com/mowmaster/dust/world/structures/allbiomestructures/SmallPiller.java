package com.mowmaster.dust.world.structures.allbiomestructures;

import com.mowmaster.dust.blocks.BlockRegistry;
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
        IBlockState mobspawnerpicked = Blocks.MOB_SPAWNER.getDefaultState();
        ArrayList<IBlockState> BlockLoot = new ArrayList<>();
        BlockLoot.add(mobspawnerpicked);
        BlockLoot.add(Blocks.EMERALD_BLOCK.getDefaultState());
        BlockLoot.add(Blocks.DIAMOND_BLOCK.getDefaultState());
        BlockLoot.add(Blocks.GOLD_BLOCK.getDefaultState());
        BlockLoot.add(Blocks.IRON_BLOCK.getDefaultState());

        Random rn = new Random();

        int lootblock = Math.abs(rn.nextInt(BlockLoot.size() - 1));

        while (!(worldIn.getBlockState(pos).equals(Blocks.GRASS.getDefaultState()) || worldIn.getBlockState(pos).equals(Blocks.SAND.getDefaultState()))&& pos.getY() > -50)
        {
            pos = pos.down();
        }

        StructureParts.clearArea(worldIn,pos,1,4,-1,1,-1,1);
        //mobspawnerlocation
        int x = 0;
        int y = 0;
        int z = 0;

        StructureParts.buildLootPiller(worldIn, rand,pos,0,0,0);
        worldIn.setBlockState(pos.add(x,y,z),BlockLoot.get(lootblock));
        worldIn.setBlockState(pos.add(0,3,0),Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.CHISELED));
        StructureParts.spawnCrystal(worldIn,rand,pos,0,4,0,"UP");

        if(BlockLoot.get(lootblock).equals(mobspawnerpicked))
        {
            worldIn.setBlockState(pos.add(x,y,z), mobspawnerpicked, 2);
            TileEntity tileentity = worldIn.getTileEntity(pos.add(x,y,z));

            if (tileentity instanceof TileEntityMobSpawner)
            {
                ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityId(SpawnerTypesHostile.getRandomDungeonMob(rand));
            }
        }

        return true;
    }
}
