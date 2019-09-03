package com.mowmaster.dust.items.itemPedestalUpgrades;

import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ipuaEffectGrower extends ipuBasic {

    public ipuaEffectGrower()
    {

    }

    public void upgradeAction(World world, BlockPos posOfPedestal, int summonRange, int summonCount)
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

                                    if (block.getBlock() instanceof IGrowable) {
                                        if(((IGrowable) block.getBlock()).canGrow(world,posThis.add(x,y,z),block,false))
                                        {
                                            if(doItemsMatch(getItemInPedestal(),new ItemStack(Items.DYE,1,15)))
                                            {
                                                if(tickytick>=80)
                                                {
                                                    Random rand = new Random();
                                                    ((IGrowable) block.getBlock()).grow(world,rand,posThis.add(x,y,z),block);
                                                    stack.shrink(1);
                                                    tickytick=0;
                                                }
                                                else tickytick++;
                                            }
                                            else
                                            {
                                                if(ticky>=240)
                                                {
                                                    Random rand = new Random();
                                                    //((IGrowable) block.getBlock()).grow(world,rand,posThis.add(x,y,z),block);
                                                    block.getBlock().updateTick(world,posThis.add(x,y,z),block,rand);
                                                    ticky=0;
                                                }
                                                else ticky++;

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}
