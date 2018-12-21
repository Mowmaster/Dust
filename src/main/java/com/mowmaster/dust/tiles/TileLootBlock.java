package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.blocks.buildingblocks.BlockCrate;
import com.mowmaster.dust.blocks.buildingblocks.BlockLootBlock;
import com.mowmaster.dust.blocks.buildingblocks.BlockPot;
import com.mowmaster.dust.blocks.crystal.BlockCrystal;
import com.mowmaster.dust.world.structures.structurebits.SpawnerTypesHostile;
import com.mowmaster.dust.world.structures.structurebits.SpawnerTypesPassive;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

import static com.mowmaster.dust.misc.DustConfigurationFile.devBlocks;
import static com.mowmaster.dust.misc.DustConfigurationFile.funhaters;


public class TileLootBlock extends TileEntity implements ITickable
{
    public static Boolean inDev = devBlocks;
    int chancetospawnloot = new Random().nextInt(99);

    private IBlockState commonLoot()
    {
        ArrayList<IBlockState> LootBlock = new ArrayList<>();
        LootBlock.add(BlockCrystal.redCrystalTwo.getDefaultState());
        LootBlock.add(BlockCrystal.blueCrystalTwo.getDefaultState());
        LootBlock.add(BlockCrystal.yellowCrystalTwo.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());
        LootBlock.add(BlockCrystal.redCrystalOne.getDefaultState());
        LootBlock.add(BlockCrystal.blueCrystalOne.getDefaultState());
        LootBlock.add(BlockCrystal.yellowCrystalOne.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());

        Random random = new Random();
        int lootchoice = Math.abs(random.nextInt(LootBlock.size()-1));

        return LootBlock.get(lootchoice);
    }

    private IBlockState uncommonLoot()
    {
        ArrayList<IBlockState> LootBlock = new ArrayList<>();
        LootBlock.add(BlockCrystal.redCrystalTwo.getDefaultState());
        LootBlock.add(BlockCrystal.blueCrystalTwo.getDefaultState());
        LootBlock.add(BlockCrystal.yellowCrystalTwo.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());
        LootBlock.add(BlockCrystal.redCrystalOne.getDefaultState());
        LootBlock.add(BlockCrystal.blueCrystalOne.getDefaultState());
        LootBlock.add(BlockCrystal.yellowCrystalOne.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());
        LootBlock.add(BlockCrystal.redCrystalOne.getDefaultState());
        LootBlock.add(BlockCrystal.blueCrystalOne.getDefaultState());
        LootBlock.add(BlockCrystal.yellowCrystalOne.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());

        Random random = new Random();
        int lootchoice = Math.abs(random.nextInt(LootBlock.size()-1));

        return LootBlock.get(lootchoice);
    }

    private IBlockState rareLoot()
    {
        ArrayList<IBlockState> LootBlock = new ArrayList<>();

        LootBlock.add(BlockCrystal.redCrystalFour.getDefaultState());
        LootBlock.add(BlockCrystal.blueCrystalFour.getDefaultState());
        LootBlock.add(BlockCrystal.yellowCrystalFour.getDefaultState());
        LootBlock.add(BlockCrystal.purpleCrystalFour.getDefaultState());
        LootBlock.add(BlockCrystal.greenCrystalFour.getDefaultState());
        LootBlock.add(BlockCrystal.orangeCrystalFour.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());
        LootBlock.add(BlockCrystal.redCrystalThree.getDefaultState());
        LootBlock.add(BlockCrystal.blueCrystalThree.getDefaultState());
        LootBlock.add(BlockCrystal.yellowCrystalThree.getDefaultState());
        LootBlock.add(BlockCrystal.purpleCrystalThree.getDefaultState());
        LootBlock.add(BlockCrystal.greenCrystalThree.getDefaultState());
        LootBlock.add(BlockCrystal.orangeCrystalThree.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());
        LootBlock.add(BlockCrystal.redCrystalTwo.getDefaultState());
        LootBlock.add(BlockCrystal.blueCrystalTwo.getDefaultState());
        LootBlock.add(BlockCrystal.yellowCrystalTwo.getDefaultState());
        LootBlock.add(BlockCrystal.purpleCrystalTwo.getDefaultState());
        LootBlock.add(BlockCrystal.greenCrystalTwo.getDefaultState());
        LootBlock.add(BlockCrystal.orangeCrystalTwo.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());
        LootBlock.add(BlockCrystal.redCrystalOne.getDefaultState());
        LootBlock.add(BlockCrystal.blueCrystalOne.getDefaultState());
        LootBlock.add(BlockCrystal.yellowCrystalOne.getDefaultState());
        LootBlock.add(BlockCrystal.purpleCrystalOne.getDefaultState());
        LootBlock.add(BlockCrystal.greenCrystalOne.getDefaultState());
        LootBlock.add(BlockCrystal.orangeCrystalOne.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());

        Random random = new Random();
        int lootchoice = Math.abs(random.nextInt(LootBlock.size()-1));

        return LootBlock.get(lootchoice);
    }

    private IBlockState legendaryLoot()
    {
        ArrayList<IBlockState> LootBlock = new ArrayList<>();

        LootBlock.add(BlockLootBlock.lootblockpassivespawner.getDefaultState());
        LootBlock.add(BlockCrystal.redCrystalFour.getDefaultState());
        LootBlock.add(BlockCrystal.blueCrystalFour.getDefaultState());
        LootBlock.add(BlockCrystal.yellowCrystalFour.getDefaultState());
        LootBlock.add(BlockCrystal.purpleCrystalFour.getDefaultState());
        LootBlock.add(BlockCrystal.greenCrystalFour.getDefaultState());
        LootBlock.add(BlockCrystal.orangeCrystalFour.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());
        LootBlock.add(BlockCrystal.redCrystalThree.getDefaultState());
        LootBlock.add(BlockCrystal.blueCrystalThree.getDefaultState());
        LootBlock.add(BlockCrystal.yellowCrystalThree.getDefaultState());
        LootBlock.add(BlockCrystal.purpleCrystalThree.getDefaultState());
        LootBlock.add(BlockCrystal.greenCrystalThree.getDefaultState());
        LootBlock.add(BlockCrystal.orangeCrystalThree.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());
        LootBlock.add(BlockCrystal.blackCrystalTwo.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());
        LootBlock.add(BlockCrystal.whiteCrystalOne.getDefaultState());
        LootBlock.add(BlockCrystal.blackCrystalOne.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());

        Random random = new Random();
        int lootchoice = Math.abs(random.nextInt(LootBlock.size()-1));

        return LootBlock.get(lootchoice);
    }

    private IBlockState exoticLoot()
    {
        ArrayList<IBlockState> LootBlock = new ArrayList<>();

        LootBlock.add(BlockLootBlock.lootblockhostilespawner.getDefaultState());
        LootBlock.add(BlockCrystal.redCrystalFive.getDefaultState());
        LootBlock.add(BlockCrystal.blueCrystalFive.getDefaultState());
        LootBlock.add(BlockCrystal.yellowCrystalFive.getDefaultState());
        LootBlock.add(BlockCrystal.purpleCrystalFive.getDefaultState());
        LootBlock.add(BlockCrystal.greenCrystalFive.getDefaultState());
        LootBlock.add(BlockCrystal.orangeCrystalFive.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());
        LootBlock.add(BlockCrystal.redCrystalFour.getDefaultState());
        LootBlock.add(BlockCrystal.blueCrystalFour.getDefaultState());
        LootBlock.add(BlockCrystal.yellowCrystalFour.getDefaultState());
        LootBlock.add(BlockCrystal.purpleCrystalFour.getDefaultState());
        LootBlock.add(BlockCrystal.greenCrystalFour.getDefaultState());
        LootBlock.add(BlockCrystal.orangeCrystalFour.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());
        LootBlock.add(BlockCrystal.whiteCrystalThree.getDefaultState());
        LootBlock.add(BlockCrystal.blackCrystalThree.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());
        LootBlock.add(BlockCrystal.whiteCrystalTwo.getDefaultState());
        LootBlock.add(BlockCrystal.blackCrystalTwo.getDefaultState());
        LootBlock.add(BlockCrate.crate1.getDefaultState());
        LootBlock.add(BlockPot.pot1.getDefaultState());

        Random random = new Random();
        int lootchoice = Math.abs(random.nextInt(LootBlock.size()-1));

        return LootBlock.get(lootchoice);
    }

    private IBlockState pillarLoot()
    {
        ArrayList<IBlockState> LootBlock = new ArrayList<>();

        LootBlock.add(BlockLootBlock.lootblockhostilespawner.getDefaultState());
        LootBlock.add(BlockLootBlock.lootblockpassivespawner.getDefaultState());
        LootBlock.add(Blocks.DIAMOND_BLOCK.getDefaultState());
        LootBlock.add(Blocks.EMERALD_BLOCK.getDefaultState());
        LootBlock.add(Blocks.GLOWSTONE.getDefaultState());
        LootBlock.add(Blocks.GOLD_BLOCK.getDefaultState());
        LootBlock.add(Blocks.IRON_BLOCK.getDefaultState());
        LootBlock.add(Blocks.REDSTONE_BLOCK.getDefaultState());
        LootBlock.add(Blocks.COAL_BLOCK.getDefaultState());
        LootBlock.add(Blocks.LAPIS_BLOCK.getDefaultState());
        LootBlock.add(Blocks.DIAMOND_BLOCK.getDefaultState());
        LootBlock.add(Blocks.EMERALD_BLOCK.getDefaultState());
        LootBlock.add(Blocks.GLOWSTONE.getDefaultState());
        LootBlock.add(Blocks.GOLD_BLOCK.getDefaultState());
        LootBlock.add(Blocks.IRON_BLOCK.getDefaultState());
        LootBlock.add(Blocks.REDSTONE_BLOCK.getDefaultState());
        LootBlock.add(Blocks.COAL_BLOCK.getDefaultState());
        LootBlock.add(Blocks.LAPIS_BLOCK.getDefaultState());


        Random random = new Random();
        int lootchoice = Math.abs(random.nextInt(LootBlock.size()-1));

        return LootBlock.get(lootchoice);
    }

    private IBlockState crystalLoot()
    {
        ArrayList<IBlockState> LootBlock = new ArrayList<>();

        LootBlock.add(BlockCrystal.redCrystalFive.getDefaultState());
        LootBlock.add(BlockCrystal.blueCrystalFive.getDefaultState());
        LootBlock.add(BlockCrystal.yellowCrystalFive.getDefaultState());
        LootBlock.add(BlockCrystal.purpleCrystalFive.getDefaultState());
        LootBlock.add(BlockCrystal.greenCrystalFive.getDefaultState());
        LootBlock.add(BlockCrystal.orangeCrystalFive.getDefaultState());
        LootBlock.add(BlockCrystal.whiteCrystalFive.getDefaultState());
        LootBlock.add(BlockCrystal.blackCrystalFive.getDefaultState());

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
                if(this.getBlockType().equals(BlockLootBlock.lootblockaircommon))
                {
                    if(chancetospawnloot<=10)
                    {
                        this.world.setBlockState(pos,commonLoot());
                    }
                    else this.world.setBlockToAir(pos);
                }
                if(this.getBlockType().equals(BlockLootBlock.lootblockairuncommon))
                {
                    if(chancetospawnloot<=10)
                    {
                        this.world.setBlockState(pos,uncommonLoot());
                    }
                    else this.world.setBlockToAir(pos);
                }
                if(this.getBlockType().equals(BlockLootBlock.lootblockairrare))
                {
                    if(chancetospawnloot<=10)
                    {
                        this.world.setBlockState(pos,rareLoot());
                    }
                    else this.world.setBlockToAir(pos);
                }
                if(this.getBlockType().equals(BlockLootBlock.lootblockairlegendary))
                {
                    if(chancetospawnloot<=10)
                    {
                        this.world.setBlockState(pos,legendaryLoot());
                    }
                    else this.world.setBlockToAir(pos);
                }
                if(this.getBlockType().equals(BlockLootBlock.lootblockairexotic))
                {
                    if(chancetospawnloot<=10)
                    {
                        this.world.setBlockState(pos,exoticLoot());
                    }
                    else this.world.setBlockToAir(pos);
                }



                if(this.getBlockType().equals(BlockLootBlock.lootblockcommon))
                {
                    this.world.setBlockState(pos,commonLoot());
                }
                if(this.getBlockType().equals(BlockLootBlock.lootblockuncommon))
                {
                    this.world.setBlockState(pos,uncommonLoot());
                }
                if(this.getBlockType().equals(BlockLootBlock.lootblockrare))
                {
                    this.world.setBlockState(pos,rareLoot());
                }
                if(this.getBlockType().equals(BlockLootBlock.lootblocklegendary))
                {
                    this.world.setBlockState(pos,legendaryLoot());
                }
                if(this.getBlockType().equals(BlockLootBlock.lootblockexotic))
                {
                    this.world.setBlockState(pos,exoticLoot());
                }


                if(this.getBlockType().equals(BlockLootBlock.lootblockhostilespawner))
                {
                    spawnSpawnerHostile(this.world,pos, Blocks.MOB_SPAWNER.getDefaultState());
                    this.world.setBlockState(pos,Blocks.MOB_SPAWNER.getDefaultState());
                }

                if(this.getBlockType().equals(BlockLootBlock.lootblockpassivespawner))
                {
                    spawnSpawnerPassive(this.world,pos, Blocks.MOB_SPAWNER.getDefaultState());
                    this.world.setBlockState(pos,Blocks.MOB_SPAWNER.getDefaultState());
                }


                if(this.getBlockType().equals(BlockLootBlock.lootblockpillar))
                {
                    this.world.setBlockState(pos,pillarLoot());
                }

                if(this.getBlockType().equals(BlockLootBlock.lootblockcrystalcluster))
                {
                    this.world.setBlockState(pos,crystalLoot());
                }

            }
        }
    }
}
