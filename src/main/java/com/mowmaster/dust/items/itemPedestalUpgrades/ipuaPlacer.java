package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ipuaPlacer extends ipuBasic {

    public ipuaPlacer()
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
                if(hasItem())
                {

                    Block block = Block.getBlockFromItem(item.getStackInSlot(0).getItem());
                    String itemName = getItemInPedestal().getUnlocalizedName();
                    String displayName = getItemInPedestal().getDisplayName();
                    String domainName = getItemInPedestal().getItem().getRegistryName().getResourceDomain();
                    Block block2 = Block.getBlockFromName(itemName.replace("item.",domainName + ":"));
                    Block block3 = Block.getBlockFromName(domainName + ":" + displayName.replace(" ","_").toLowerCase());
                    IBlockState stated = block.getDefaultState();
                    IBlockState stated2 = block.getDefaultState();
                    IBlockState stated3 = block.getDefaultState();

                    int rangeOfPlace = 1;
                    if(hasCoin())
                    {
                        if(getCoinOnPedestal().isItemEnchanted())
                        {
                            rangeOfPlace = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentRange,getCoinOnPedestal())+1;
                        }
                    }


                    if(!getItemInPedestal().hasTagCompound())
                    {
                        if(block!= Blocks.AIR)
                        {
                            if(getItemInPedestal().getHasSubtypes()) {
                                stated = block.getStateFromMeta(getItemInPedestal().getMetadata());
                            }

                            if(world.getBlockState(getPosOfBlockBelow(rangeOfPlace)).getBlock().equals(Blocks.AIR))
                            {
                                if(block instanceof IGrowable)
                                {
                                    if(world.getBlockState(getPosOfBlockBelow(rangeOfPlace).add(0,-1,0)).getBlock().equals(Blocks.DIRT) || world.getBlockState(getPosOfBlockBelow(rangeOfPlace).add(0,-1,0)).getBlock().equals(Blocks.GRASS))
                                    {
                                        if(getItemInPedestal().getHasSubtypes())
                                        {
                                            world.setBlockState(getPosOfBlockBelow(rangeOfPlace),stated);
                                        }
                                        else world.setBlockState(getPosOfBlockBelow(rangeOfPlace),block.getDefaultState());


                                        world.playSound((EntityPlayer)null, getPos().getX(), getPos().getY(), getPos().getZ(), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                                        getItemInPedestal().shrink(1);
                                    }
                                }
                                else {

                                    if(getItemInPedestal().getHasSubtypes())
                                    {
                                        world.setBlockState(getPosOfBlockBelow(rangeOfPlace),stated);
                                    }
                                    else world.setBlockState(getPosOfBlockBelow(rangeOfPlace),block.getDefaultState());
                                    world.playSound((EntityPlayer)null, getPos().getX(), getPos().getY(), getPos().getZ(), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                                    getItemInPedestal().shrink(1);
                                }

                            }
                        }
                        else if(block2!=null)
                        {
                            if(getItemInPedestal().getHasSubtypes()) {
                                stated2 = block2.getStateFromMeta(getItemInPedestal().getMetadata());
                            }

                            if(world.getBlockState(getPosOfBlockBelow(rangeOfPlace)).getBlock().equals(Blocks.AIR))
                            {
                                if(block2 instanceof IGrowable)
                                {
                                    if(world.getBlockState(getPosOfBlockBelow(rangeOfPlace).add(0,-1,0)).getBlock().equals(Blocks.DIRT) || world.getBlockState(getPosOfBlockBelow(rangeOfPlace).add(0,-1,0)).getBlock().equals(Blocks.GRASS))
                                    {
                                        if(getItemInPedestal().getHasSubtypes())
                                        {
                                            world.setBlockState(getPosOfBlockBelow(rangeOfPlace),stated2);
                                        }
                                        else world.setBlockState(getPosOfBlockBelow(rangeOfPlace),block2.getDefaultState());
                                        getItemInPedestal().shrink(1);
                                    }
                                }
                                else {

                                    if(getItemInPedestal().getHasSubtypes())
                                    {
                                        world.setBlockState(getPosOfBlockBelow(rangeOfPlace),stated2);
                                    }
                                    else world.setBlockState(getPosOfBlockBelow(rangeOfPlace),block2.getDefaultState());
                                    getItemInPedestal().shrink(1);
                                }
                            }
                        }
                        else if(block3!=null)
                        {
                            if(getItemInPedestal().getHasSubtypes())
                            {
                                stated3 = block3.getStateFromMeta(getItemInPedestal().getMetadata());
                            }

                            if(world.getBlockState(getPosOfBlockBelow(rangeOfPlace)).getBlock().equals(Blocks.AIR))
                            {
                                if(block3 instanceof IGrowable)
                                {
                                    if(world.getBlockState(getPosOfBlockBelow(rangeOfPlace).add(0,-1,0)).getBlock().equals(Blocks.DIRT) || world.getBlockState(getPosOfBlockBelow(rangeOfPlace).add(0,-1,0)).getBlock().equals(Blocks.GRASS))
                                    {
                                        if(getItemInPedestal().getHasSubtypes())
                                        {
                                            world.setBlockState(getPosOfBlockBelow(rangeOfPlace),stated3);
                                        }
                                        else world.setBlockState(getPosOfBlockBelow(rangeOfPlace),block3.getDefaultState());
                                        getItemInPedestal().shrink(1);
                                    }
                                }
                                else {
                                    if(getItemInPedestal().getHasSubtypes())
                                    {
                                        world.setBlockState(getPosOfBlockBelow(rangeOfPlace),stated3);
                                    }
                                    else world.setBlockState(getPosOfBlockBelow(rangeOfPlace),block3.getDefaultState());
                                    getItemInPedestal().shrink(1);
                                }
                            }

                        }
                    }
                }
            }
        }

    }

}
