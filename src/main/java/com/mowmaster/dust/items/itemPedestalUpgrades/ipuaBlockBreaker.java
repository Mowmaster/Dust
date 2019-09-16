package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ipuaBlockBreaker extends ipuBasic {

    public ipuaBlockBreaker()
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
                int rangeOfBreak = 1;
                if(hasCoin())
                {
                    if(getCoinOnPedestal().isItemEnchanted())
                    {
                        rangeOfBreak = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentRange,getCoinOnPedestal())+1;
                    }
                }
                int fortune = 0;
                Random rn = new Random();
                IBlockState blocky = world.getBlockState(getPosOfBlockBelow(rangeOfBreak));
                ItemStack getDrops = ItemStack.EMPTY;



                if(!world.getBlockState(getPosOfBlockBelow(rangeOfBreak)).getBlock().equals(Blocks.AIR))
                {
                    if(hasCoin())
                    {
                        if(coin.getStackInSlot(0).getItem().equals(ItemRegistry.breakerUpgrade))
                        {
                            if(coin.getStackInSlot(0).isItemEnchanted())
                            {
                                if(EnchantmentHelper.getEnchantments(coin.getStackInSlot(0)).containsKey(Enchantments.SILK_TOUCH))
                                {
                                    getDrops = new ItemStack(blocky.getBlock());
                                }
                                else if(EnchantmentHelper.getEnchantments(coin.getStackInSlot(0)).containsKey(Enchantments.FORTUNE))
                                {
                                    fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE,coin.getStackInSlot(0)) + 1;
                                    Item dropItem = blocky.getBlock().getItemDropped(blocky,rn,fortune);
                                    int metaDropped = blocky.getBlock().damageDropped(blocky);
                                    int countDropped = blocky.getBlock().quantityDropped(blocky,fortune,rn);
                                    if(blocky.getBlock().getItemDropped(blocky,rn,fortune)!=null)
                                    {
                                        if(dropItem.getHasSubtypes())
                                        {
                                            getDrops = new ItemStack(dropItem,countDropped,metaDropped);
                                        }
                                        else getDrops = new ItemStack(dropItem,countDropped);
                                    }
                                }
                                else
                                {
                                    Item dropItem = blocky.getBlock().getItemDropped(blocky,rn,0);
                                    int metaDropped = blocky.getBlock().damageDropped(blocky);
                                    int countDropped = blocky.getBlock().quantityDropped(blocky,0,rn);
                                    if(blocky.getBlock().getItemDropped(blocky,rn,0)!=null)
                                    {

                                        if(dropItem.getHasSubtypes())
                                        {
                                            getDrops = new ItemStack(dropItem,countDropped,metaDropped);
                                        }
                                        else getDrops = new ItemStack(dropItem,countDropped);
                                    }
                                }
                            }
                            else
                            {
                                Item dropItem = blocky.getBlock().getItemDropped(blocky,rn,0);
                                int metaDropped = blocky.getBlock().damageDropped(blocky);
                                int countDropped = blocky.getBlock().quantityDropped(blocky,0,rn);
                                if(blocky.getBlock().getItemDropped(blocky,rn,0)!=null)
                                {
                                    if(dropItem.getHasSubtypes())
                                    {
                                        getDrops = new ItemStack(dropItem,countDropped,metaDropped);
                                    }
                                    else getDrops = new ItemStack(dropItem,countDropped);
                                }
                            }


                            if(hasItem())
                            {
                                if(doItemsMatch(getDrops))
                                {
                                    if(getItemInPedestal().getCount() + getDrops.getCount() <= getMaxStackSize())
                                    {
                                        addItem(getDrops);
                                        world.setBlockToAir(getPosOfBlockBelow(rangeOfBreak));
                                    }
                                }

                            }
                            else
                            {
                                addItem(getDrops);
                                world.setBlockToAir(getPosOfBlockBelow(rangeOfBreak));
                            }
                        }
                    }
                }
            }
        }

    }*/

}
