package com.mowmaster.dust.items.itemPedestalUpgrades;

import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ipuaEffectHarvester extends ipuBasic {

    public ipuaEffectHarvester()
    {

    }

    /*public void upgradeAction(World world, BlockPos posOfPedestal, int summonRange, int summonCount)
    {
        //Range comes from enchant
        //ammount comes from...speed?

        if(!world.isRemote)
        {
            if(!world.isBlockPowered(posOfPedestal))
            {
                World world = this.world;
                BlockPos posThis = this.getPos();
                IBlockState block = world.getBlockState(posThis);
                ItemStack stack = getItemInPedestal();
                WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
                FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(worldServer);

                int increase = rangeIncrease;

                if(!world.isRemote)
                {
                    if(!world.isBlockPowered(posThis))
                    {
                        for(int x=-(1+rangeIncrease);x<=(1+rangeIncrease);x++)
                        {
                            for(int z=-(1+rangeIncrease);z<=(1+rangeIncrease);z++)
                            {
                                for(int y=-(rangeIncrease);y<=(rangeIncrease);y++) {
                                    block = world.getBlockState(posThis.add(x, y, z));
                                    if(tickHarvest>39)
                                    {
                                        if (block.getBlock() instanceof IGrowable) {
                                            if(!((IGrowable) block.getBlock()).canGrow(world,posThis.add(x,y,z),block,false))
                                            {
                                                block.getBlock().harvestBlock(world,fakePlayer,posThis.add(x,y,z),block,null,getItemInPedestal());
                                                world.setBlockToAir(posThis.add(x,y,z));
                                                tickHarvest=0;
                                            }
                                        }
                                    }
                                    else tickHarvest++;
                                }
                            }
                        }

                        getItemEntitiesNearby(rangeIncrease);
                    }
                }
            }
        }

    }*/

}
