package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class ipuaEffectPlanter extends ipuBasic {

    public ipuaEffectPlanter()
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
                IBlockState blockAbove = world.getBlockState(posThis.add(0,1,0));
                ItemStack stack = getItemInPedestal();

                if(!world.isRemote)
                {
                    if(!world.isBlockPowered(posThis))
                    {
                        if(this.hasItem())
                        {
                            if(tickPlanter>30)
                            {
                                for(int x=-(1+rangeIncrease);x<=(1+rangeIncrease);x++)
                                {
                                    for(int z=-(1+rangeIncrease);z<=(1+rangeIncrease);z++)
                                    {
                                        for(int y=-(rangeIncrease);y<=(rangeIncrease);y++) {
                                            block = world.getBlockState(posThis.add(x, y, z));
                                            blockAbove = world.getBlockState(posThis.add(x,y+1,z));


                                            if(getItemInPedestal().getItem() instanceof IPlantable)
                                            {
                                                if (block.getBlock().canSustainPlant(block,world,posThis.add(x,y,z), EnumFacing.UP,(IPlantable)getItemInPedestal().getItem()))
                                                {
                                                    if(blockAbove.getBlock().isAir(blockAbove,world,posThis.add(x,y+1,z)))
                                                    {
                                                        int oldCount = stack.getCount();
                                                        if(stack.getItem().getRegistryName().getResourceDomain().contains("harvestcraft") && stack.getItem().getUnlocalizedName().contains("sunflowerseedsitem")
                                                                || stack.getItem().getUnlocalizedName().contains("roastedpumpkinseedsitem")
                                                                || stack.getItem().getUnlocalizedName().contains("toastedsesameseedsitem")
                                                                || stack.getItem().getUnlocalizedName().contains("saltedsunflowerseedsitem")) {}
                                                        else if(stack.getItem().getRegistryName().getResourceDomain().contains("harvestcraft") && stack.getItem().getUnlocalizedName().contains("teaseeditem"))
                                                        {
                                                            world.setBlockState(posThis.add(x,y+1,z), Block.getBlockFromName("harvestcraft:pamtealeafcrop").getDefaultState());
                                                            stack.setCount(oldCount-1);
                                                        }
                                                        else if(stack.getItem().getRegistryName().getResourceDomain().contains("harvestcraft") && stack.getItem().getUnlocalizedName().contains("sesameseedsitem") || stack.getItem().getUnlocalizedName().contains("sesameseedsseeditem"))
                                                        {
                                                            world.setBlockState(posThis.add(x,y+1,z),Block.getBlockFromName("harvestcraft:pamsesameseedscrop").getDefaultState());
                                                            stack.setCount(oldCount-1);
                                                        }
                                                        else if(stack.getItem().getRegistryName().getResourceDomain().contains("harvestcraft") && stack.getItem().getUnlocalizedName().contains("coffeeseeditem"))
                                                        {
                                                            world.setBlockState(posThis.add(x,y+1,z),Block.getBlockFromName("harvestcraft:pamcoffeebeancrop").getDefaultState());
                                                            stack.setCount(oldCount-1);
                                                        }
                                                        else if(stack.getItem().getRegistryName().getResourceDomain().contains("harvestcraft") && stack.getItem().getUnlocalizedName().contains("mustardseeditem") || stack.getItem().getUnlocalizedName().contains("mustardseedsitem"))
                                                        {
                                                            world.setBlockState(posThis.add(x,y+1,z),Block.getBlockFromName("harvestcraft:pammustardseedscrop").getDefaultState());
                                                            stack.setCount(oldCount-1);
                                                        }
                                                        else if(stack.getItem().getRegistryName().getResourceDomain().contains("harvestcraft") && stack.getItem().getUnlocalizedName().contains("seeditem"))
                                                        {
                                                            world.setBlockState(posThis.add(x,y+1,z),Block.getBlockFromName(stack.getItem().getUnlocalizedName().replace("item.","harvestcraft:pam").replace("seeditem","crop")).getDefaultState());
                                                            stack.setCount(oldCount-1);

                                                        }
                                                        else if(stack.getItem().getRegistryName().getResourceDomain().contains("immersiveengineering") && stack.getItem().getUnlocalizedName().contains("seed"))
                                                        {
                                                            world.setBlockState(posThis.add(x,y+1,z),Block.getBlockFromName("immersiveengineering:hemp").getDefaultState());
                                                            stack.setCount(oldCount-1);
                                                        }
                                                        else if(stack.getItem().equals(Items.WHEAT_SEEDS))
                                                        {
                                                            world.setBlockState(posThis.add(x,y+1,z),Blocks.WHEAT.getDefaultState());
                                                            stack.setCount(oldCount-1);
                                                        }
                                                        else if(stack.getItem().equals(Items.MELON_SEEDS))
                                                        {
                                                            world.setBlockState(posThis.add(x,y+1,z),Blocks.MELON_STEM.getDefaultState());
                                                            stack.setCount(oldCount-1);
                                                        }
                                                        else if(stack.getItem().equals(Items.PUMPKIN_SEEDS))
                                                        {
                                                            world.setBlockState(posThis.add(x,y+1,z),Blocks.PUMPKIN_STEM.getDefaultState());
                                                            stack.setCount(oldCount-1);
                                                        }
                                                        else if(stack.getItem().equals(Items.BEETROOT_SEEDS))
                                                        {
                                                            world.setBlockState(posThis.add(x,y+1,z),Blocks.BEETROOTS.getDefaultState());
                                                            stack.setCount(oldCount-1);
                                                        }
                                                        else if(stack.getItem().equals(Items.POTATO))
                                                        {
                                                            world.setBlockState(posThis.add(x,y+1,z),Blocks.POTATOES.getDefaultState());
                                                            stack.setCount(oldCount-1);
                                                        }
                                                        else if(stack.getItem().equals(Items.CARROT))
                                                        {
                                                            world.setBlockState(posThis.add(x,y+1,z),Blocks.CARROTS.getDefaultState());
                                                            stack.setCount(oldCount-1);
                                                        }
                                                    }
                                                }
                                            }


                                        }

                                    }
                                }

                                tickPlanter=0;
                            }
                            else
                            {
                                tickPlanter++;
                            }
                        }
                    }



            *//*

             *//*
                }
            }
        }

    }*/

}
