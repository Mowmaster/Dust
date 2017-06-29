package com.mowmaster.dust.world.structures;

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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.ArrayList;
import java.util.Random;

import static net.minecraft.block.BlockStoneBrick.VARIANT;

/**
 * Created by KingMowmaster on 6/29/2017.
 */
public class SmallPiller extends WorldGenerator
{
    private static final ResourceLocation[] SPAWNERTYPES = new ResourceLocation[] {EntityList.getKey(EntitySkeleton.class), EntityList.getKey(EntityZombie.class), EntityList.getKey(EntityZombie.class), EntityList.getKey(EntitySpider.class)};

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos) {
        //System.out.println(Blocks.GRASS.getDefaultState());
        //System.out.println(worldIn.getBlockState(pos));
        IBlockState mobspawnerpicked = Blocks.MOB_SPAWNER.getDefaultState();
        ArrayList<IBlockState> BlockLoot = new ArrayList<>();
        BlockLoot.add(BlockRegistry.machineBase.getDefaultState());
        BlockLoot.add(mobspawnerpicked);
        BlockLoot.add(Blocks.EMERALD_BLOCK.getDefaultState());
        BlockLoot.add(Blocks.DIAMOND_BLOCK.getDefaultState());
        BlockLoot.add(Blocks.GOLD_BLOCK.getDefaultState());
        BlockLoot.add(Blocks.IRON_BLOCK.getDefaultState());


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

        int lootblock = Math.abs(rn.nextInt(BlockLoot.size() - 1));
        int crystalcolor = Math.abs(rn.nextInt(BlockCrystalTop.size() - 1));


        while (!(worldIn.getBlockState(pos).equals(Blocks.GRASS.getDefaultState()) || worldIn.getBlockState(pos).equals(Blocks.SAND.getDefaultState()))&& pos.getY() > -50)
        {
            pos = pos.down();
        }

        for(int y=1;y<=4;y++)
        {
            for(int z=-1;z<=1;z++)
            {
                worldIn.setBlockState(pos.add(-1,y,z),Blocks.AIR.getDefaultState());
                worldIn.setBlockState(pos.add(0,y,z),Blocks.AIR.getDefaultState());
                worldIn.setBlockState(pos.add(1,y,z),Blocks.AIR.getDefaultState());
            }
        }
        //mobspawnerlocation
        int x = 0;
        int y = 0;
        int z = 0;

        worldIn.setBlockState(pos.add(x,y,z),BlockLoot.get(lootblock));
        worldIn.setBlockState(pos.add(0,1,0),Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.DEFAULT));
        worldIn.setBlockState(pos.add(0,2,0),Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.DEFAULT));
        worldIn.setBlockState(pos.add(0,3,0),Blocks.STONEBRICK.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.CHISELED));
        worldIn.setBlockState(pos.add(0,4,0),BlockCrystalTop.get(crystalcolor));



        if(BlockLoot.get(lootblock).equals(mobspawnerpicked))
        {
            worldIn.setBlockState(pos.add(x,y,z), mobspawnerpicked, 2);
            TileEntity tileentity = worldIn.getTileEntity(pos.add(x,y,z));

            if (tileentity instanceof TileEntityMobSpawner)
            {
                ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityId(this.pickMobSpawner(rand));
            }
        }

        return true;
    }

    private ResourceLocation pickMobSpawner(Random rand)
    {
        return net.minecraftforge.common.DungeonHooks.getRandomDungeonMob(rand);
    }

}
