package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;


public class TileLootBlock extends TileEntity implements ITickable
{
    boolean inDev = false;
    int chancetospawnloot = new Random().nextInt(99);



    @Override
    public void update() {
        World world = this.getWorld();
        BlockPos pos = this.getPos();


        ArrayList<IBlockState> LootBlock = new ArrayList<>();
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
        LootBlock.add(BlockRegistry.redCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.blueCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.yellowCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.purpleCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.greenCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.orangeCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.blackCrystalThree.getDefaultState());
        LootBlock.add(BlockRegistry.whiteCrystalTwo.getDefaultState());
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




        if(!world.isRemote)
        {
            if(inDev==false)
            {
                if(chancetospawnloot<=10)
                {
                    this.world.setBlockState(pos,LootBlock.get(lootchoice));
                }
                else this.world.setBlockToAir(pos);
            }
        }
    }
}
