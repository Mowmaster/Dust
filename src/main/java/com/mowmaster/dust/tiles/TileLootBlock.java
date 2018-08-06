package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.world.structures.structurebits.SpawnerTypesHostile;
import com.mowmaster.dust.world.structures.structurebits.SpawnerTypesPassive;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

import static net.minecraft.block.BlockStoneBrick.VARIANT;


public class TileLootBlock extends TileEntity implements ITickable
{
    boolean inDev = false;
    int chancetospawnloot = new Random().nextInt(99);

    private IBlockState commonLoot()
    {
        ArrayList<IBlockState> LootBlock = new ArrayList<>();
        LootBlock.add(BlockRegistry.redCrystalTwo.getDefaultState());
        LootBlock.add(BlockRegistry.blueCrystalTwo.getDefaultState());
        LootBlock.add(BlockRegistry.yellowCrystalTwo.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());
        LootBlock.add(BlockRegistry.redCrystalOne.getDefaultState());
        LootBlock.add(BlockRegistry.blueCrystalOne.getDefaultState());
        LootBlock.add(BlockRegistry.yellowCrystalOne.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());

        Random random = new Random();
        int lootchoice = Math.abs(random.nextInt(LootBlock.size()-1));

        return LootBlock.get(lootchoice);
    }

    private IBlockState uncommonLoot()
    {
        ArrayList<IBlockState> LootBlock = new ArrayList<>();
        LootBlock.add(BlockRegistry.redCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.blueCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.yellowCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());
        LootBlock.add(BlockRegistry.redCrystalTwo.getDefaultState());
        LootBlock.add(BlockRegistry.blueCrystalTwo.getDefaultState());
        LootBlock.add(BlockRegistry.yellowCrystalTwo.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());
        LootBlock.add(BlockRegistry.redCrystalOne.getDefaultState());
        LootBlock.add(BlockRegistry.blueCrystalOne.getDefaultState());
        LootBlock.add(BlockRegistry.yellowCrystalOne.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());

        Random random = new Random();
        int lootchoice = Math.abs(random.nextInt(LootBlock.size()-1));

        return LootBlock.get(lootchoice);
    }

    private IBlockState rareLoot()
    {
        ArrayList<IBlockState> LootBlock = new ArrayList<>();

        LootBlock.add(BlockRegistry.redCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.blueCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.yellowCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.purpleCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.greenCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.orangeCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());
        LootBlock.add(BlockRegistry.redCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.blueCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.yellowCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.purpleCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.greenCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.orangeCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());
        LootBlock.add(BlockRegistry.redCrystalTwo.getDefaultState());
        LootBlock.add(BlockRegistry.blueCrystalTwo.getDefaultState());
        LootBlock.add(BlockRegistry.yellowCrystalTwo.getDefaultState());
        LootBlock.add(BlockRegistry.purpleCrystalTwo.getDefaultState());
        LootBlock.add(BlockRegistry.greenCrystalTwo.getDefaultState());
        LootBlock.add(BlockRegistry.orangeCrystalTwo.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());
        LootBlock.add(BlockRegistry.redCrystalOne.getDefaultState());
        LootBlock.add(BlockRegistry.blueCrystalOne.getDefaultState());
        LootBlock.add(BlockRegistry.yellowCrystalOne.getDefaultState());
        LootBlock.add(BlockRegistry.purpleCrystalOne.getDefaultState());
        LootBlock.add(BlockRegistry.greenCrystalOne.getDefaultState());
        LootBlock.add(BlockRegistry.orangeCrystalOne.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());

        Random random = new Random();
        int lootchoice = Math.abs(random.nextInt(LootBlock.size()-1));

        return LootBlock.get(lootchoice);
    }

    private IBlockState legendaryLoot()
    {
        ArrayList<IBlockState> LootBlock = new ArrayList<>();

        LootBlock.add(BlockRegistry.lootblockpassivespawner.getDefaultState());
        LootBlock.add(BlockRegistry.redCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.blueCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.yellowCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.purpleCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.greenCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.orangeCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());
        LootBlock.add(BlockRegistry.redCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.blueCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.yellowCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.purpleCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.greenCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.orangeCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());
        LootBlock.add(BlockRegistry.blackCrystalTwo.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());
        LootBlock.add(BlockRegistry.whiteCrystalOne.getDefaultState());
        LootBlock.add(BlockRegistry.blackCrystalOne.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());

        Random random = new Random();
        int lootchoice = Math.abs(random.nextInt(LootBlock.size()-1));

        return LootBlock.get(lootchoice);
    }

    private IBlockState exoticLoot()
    {
        ArrayList<IBlockState> LootBlock = new ArrayList<>();

        LootBlock.add(BlockRegistry.lootblockhostilespawner.getDefaultState());
        LootBlock.add(BlockRegistry.redCrystalFive.getDefaultState());
        LootBlock.add(BlockRegistry.blueCrystalFive.getDefaultState());
        LootBlock.add(BlockRegistry.yellowCrystalFive.getDefaultState());
        LootBlock.add(BlockRegistry.purpleCrystalFive.getDefaultState());
        LootBlock.add(BlockRegistry.greenCrystalFive.getDefaultState());
        LootBlock.add(BlockRegistry.orangeCrystalFive.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());
        LootBlock.add(BlockRegistry.redCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.blueCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.yellowCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.purpleCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.greenCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.orangeCrystalFour.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());
        LootBlock.add(BlockRegistry.whiteCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.blackCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());
        LootBlock.add(BlockRegistry.whiteCrystalTwo.getDefaultState());
        LootBlock.add(BlockRegistry.blackCrystalTwo.getDefaultState());
        LootBlock.add(BlockRegistry.crate1.getDefaultState());
        LootBlock.add(BlockRegistry.pot1.getDefaultState());

        Random random = new Random();
        int lootchoice = Math.abs(random.nextInt(LootBlock.size()-1));

        return LootBlock.get(lootchoice);
    }

    public static void spawnSpawnerHostile(World worldIn, BlockPos pos, IBlockState state)
    {
        Random random = new Random();
        worldIn.setBlockState(pos, state, 2);
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityMobSpawner) {
            ((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setEntityId(SpawnerTypesHostile.getRandomDungeonMob(random));
        }
    }

    public static void spawnSpawnerPassive(World worldIn, BlockPos pos, IBlockState state)
    {
        Random random = new Random();
        worldIn.setBlockState(pos, state, 2);
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityMobSpawner) {
            ((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setEntityId(SpawnerTypesPassive.getRandomDungeonMob(random));
        }
    }





    @Override
    public void update() {
        World world = this.getWorld();
        BlockPos pos = this.getPos();







        if(!world.isRemote)
        {
            if(inDev==false)
            {
                if(this.getBlockType().equals(BlockRegistry.lootblockaircommon))
                {
                    if(chancetospawnloot<=10)
                    {
                        this.world.setBlockState(pos,commonLoot());
                    }
                    else this.world.setBlockToAir(pos);
                }
                if(this.getBlockType().equals(BlockRegistry.lootblockairuncommon))
                {
                    if(chancetospawnloot<=10)
                    {
                        this.world.setBlockState(pos,uncommonLoot());
                    }
                    else this.world.setBlockToAir(pos);
                }
                if(this.getBlockType().equals(BlockRegistry.lootblockairrare))
                {
                    if(chancetospawnloot<=10)
                    {
                        this.world.setBlockState(pos,rareLoot());
                    }
                    else this.world.setBlockToAir(pos);
                }
                if(this.getBlockType().equals(BlockRegistry.lootblockairlegendary))
                {
                    if(chancetospawnloot<=10)
                    {
                        this.world.setBlockState(pos,legendaryLoot());
                    }
                    else this.world.setBlockToAir(pos);
                }
                if(this.getBlockType().equals(BlockRegistry.lootblockairexotic))
                {
                    if(chancetospawnloot<=10)
                    {
                        this.world.setBlockState(pos,exoticLoot());
                    }
                    else this.world.setBlockToAir(pos);
                }



                if(this.getBlockType().equals(BlockRegistry.lootblockcommon))
                {
                    this.world.setBlockState(pos,commonLoot());
                }
                if(this.getBlockType().equals(BlockRegistry.lootblockuncommon))
                {
                    this.world.setBlockState(pos,uncommonLoot());
                }
                if(this.getBlockType().equals(BlockRegistry.lootblockrare))
                {
                    this.world.setBlockState(pos,rareLoot());
                }
                if(this.getBlockType().equals(BlockRegistry.lootblocklegendary))
                {
                    this.world.setBlockState(pos,legendaryLoot());
                }
                if(this.getBlockType().equals(BlockRegistry.lootblockexotic))
                {
                    this.world.setBlockState(pos,exoticLoot());
                }


                if(this.getBlockType().equals(BlockRegistry.lootblockhostilespawner))
                {
                    spawnSpawnerHostile(this.world,pos, Blocks.MOB_SPAWNER.getDefaultState());
                    this.world.setBlockState(pos,Blocks.MOB_SPAWNER.getDefaultState());
                }

                if(this.getBlockType().equals(BlockRegistry.lootblockpassivespawner))
                {
                    spawnSpawnerPassive(this.world,pos, Blocks.MOB_SPAWNER.getDefaultState());
                    this.world.setBlockState(pos,Blocks.MOB_SPAWNER.getDefaultState());
                }

            }
        }
    }
}
