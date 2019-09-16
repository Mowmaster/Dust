package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class ipuaEnchanter extends ipuBasic {

    public ipuaEnchanter()
    {

    }



    /*public void getXPOrbEntitiesNearby(int rangeIncrease)
    {
        World world = this.world;
        ItemStack stack = ItemStack.EMPTY;
        int increase = rangeIncrease;
        if(!world.isRemote)
        {
            List<EntityXPOrb> orbs = world.getEntitiesWithinAABB(EntityXPOrb.class,new AxisAlignedBB(this.pos.getX() - (1 + increase), this.pos.getY() - (1 + increase),
                    this.pos.getZ() - (1 + increase), this.pos.getX() + (2 + increase), this.pos.getY() + (1 + increase), this.pos.getZ() + (2 + increase)));

            for (EntityXPOrb orbo : orbs) {
                onEntitiesCollidWithBlock(orbo);
            }

        }

        *//*
        List<EntityItem> items = worldIn.getEntitiesWithinAABB(EntityItem.class,new AxisAlignedBB(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 2, pos.getY() + 1, pos.getZ() + 2));
            List<EntityXPOrb> orbs = worldIn.getEntitiesWithinAABB(EntityXPOrb.class,new AxisAlignedBB(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 2, pos.getY() + 1, pos.getZ() + 2));
            for (EntityXPOrb orbo : orbs) {
                double orbox = pos.getX()-orbo.getPosition().getX();
                double orboy = pos.getY()-orbo.getPosition().getY();
                double orboz = pos.getZ()-orbo.getPosition().getZ();

                if(orbox<0)
                {
                    orbo.motionX=-0.1;
                }
                else
                {
                    orbo.motionX=0.1;
                }

                if(orboy<0)
                {
                    orbo.motionY=-0.1;
                }
                else
                {
                    orbo.motionY=0.1;
                }

                if(orboz<0)
                {
                    orbo.motionZ=-0.1;
                }
                else
                {
                    orbo.motionZ=0.1;
                }
            }

            for (EntityItem item : items) {
                double itemx = pos.getX()-item.getPosition().getX();
                double itemy = pos.getY()-item.getPosition().getY();
                double itemz = pos.getZ()-item.getPosition().getZ();

                if(itemx<0)
                {
                    item.motionX=-0.1;
                }
                else
                {
                    item.motionX=0.1;
                }

                if(itemy<0)
                {
                    item.motionY=-0.1;
                }
                else
                {
                    item.motionY=0.1;
                }

                if(itemz<0)
                {
                    item.motionZ=-0.1;
                }
                else
                {
                    item.motionZ=0.1;
                }
            }
         *//*
    }

    public void upgradeAction(World world, BlockPos posOfPedestal,BlockPos blockBelow, ItemStack itemInPedestal, ItemStack coinInPedestal, float enchantPower, int expInPedestal)
    {
        if(!world.isRemote)
        {
            if(!world.isBlockPowered(posOfPedestal))
            {
                float getEnchantPower = enchantPower;
                ItemStack itemFromInv = ItemStack.EMPTY;
                BlockPos posOfInvBelow = getPosOfBlockBelow(world,posOfPedestal,1);
                ItemStack stackInPedestal = getStackInPedestal(world,posOfPedestal);
                if(world.getTileEntity(posOfInvBelow) !=null)
                {
                    if(world.getTileEntity(posOfInvBelow).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))
                    {

                        TileEntity invToPullFrom = world.getTileEntity(posOfInvBelow);
                        if(invToPullFrom instanceof TilePedestal) {
                            itemFromInv = ItemStack.EMPTY;

                        }
                        else {
                            itemFromInv = IntStream.range(0,invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots())//Int Range
                                    .mapToObj((invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN))::getStackInSlot)//Function being applied to each interval
                                    .filter(itemStack -> !itemStack.isEmpty())
                                    .findFirst().orElse(ItemStack.EMPTY);

                            if(!itemFromInv.equals(ItemStack.EMPTY))
                            {
                                if(stackInPedestal.isEmpty())
                                {
                                    if(itemFromInv.getItem().equals(Items.GLASS_BOTTLE))
                                    {
                                        if(expInPedestal>=10)
                                        {
                                            itemFromInv.shrink(1);
                                            removeExpFromPedestal(world,posOfPedestal,10);
                                        }
                                    }
                                    else if(itemFromInv.getItem().equals(Items.BOOK))
                                    {
                                        Random rn = new Random();
                                        ItemStack enchantedBook = itemFromInv.copy();
                                        enchantedBook.setCount(1);

                                        if(getEnchantPower<16.0f)
                                        {
                                            getEnchantPower = enchantPower;
                                        }
                                        else getEnchantPower = 15.0f;

                                        if(expInPedestal>=((int)((getEnchantPower*2.5)*(getEnchantPower*2.5)))+7)
                                        {
                                            int level = (int)getEnchantPower*2;

                                            ItemStack bookEnchanted = EnchantmentHelper.addRandomEnchantment(rn,enchantedBook,level,true);
                                            addToPedestal(world,posOfPedestal,bookEnchanted);
                                            itemFromInv.shrink(1);
                                            removeExpFromPedestal(world,posOfPedestal,((int)((getEnchantPower*2.5)*(getEnchantPower*2.5)))+7);
                                        }
                                    }
                                    else if(itemFromInv.getItem().isDamageable() && itemFromInv.getItem().getDamage(itemFromInv)>0)
                                    {

                                        if(coinInPedestal.isItemEnchanted())
                                        {
                                            if(EnchantmentHelper.getEnchantments(coinInPedestal).containsKey(Enchantments.MENDING))
                                            {
                                                if(expInPedestal>=1)
                                                {
                                                    int getDamage = itemFromInv.getItemDamage();
                                                    if(getDamage>1)
                                                    {
                                                        itemFromInv.setItemDamage(getDamage-1);
                                                        removeExpFromPedestal(world,posOfPedestal,1);
                                                    }
                                                    else
                                                    {
                                                        ItemStack itemFromInvCopy = itemFromInv.copy();
                                                        itemFromInvCopy.setItemDamage(0);
                                                        addToPedestal(world,posOfPedestal,itemFromInvCopy);
                                                        itemFromInv.shrink(1);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else if(!itemFromInv.isItemEnchanted())
                                    {
                                        if(itemFromInv.isItemEnchantable())
                                        {
                                            Random rn = new Random();
                                            ItemStack itemFromInvCopy = itemFromInv.copy();
                                            itemFromInvCopy.setCount(1);

                                            if(getEnchantPower<16.0f)
                                            {
                                                getEnchantPower = enchantPower;
                                            }
                                            else getEnchantPower = 15.0f;

                                            if(expInPedestal>=((int)((getEnchantPower*2.5)*(getEnchantPower*2.5)))+7)
                                            {
                                                int level = (int)getEnchantPower*2;

                                                EnchantmentHelper.addRandomEnchantment(rn,itemFromInvCopy,level,true);
                                                addToPedestal(world,posOfPedestal,itemFromInvCopy);
                                                itemFromInv.shrink(1);
                                                removeExpFromPedestal(world,posOfPedestal,((int)((getEnchantPower*2.5)*(getEnchantPower*2.5)))+7);
                                            }
                                        }
                                    }

                                }
                                else if(itemInPedestal.getItem().equals(Items.EXPERIENCE_BOTTLE))
                                {
                                    if(expInPedestal>=10)
                                    {
                                        if(itemInPedestal.getCount() <= itemInPedestal.getMaxStackSize()-1)
                                        {
                                            itemFromInv.shrink(1);
                                            addToPedestal(world,posOfPedestal,new ItemStack(Items.EXPERIENCE_BOTTLE,1));
                                            removeExpFromPedestal(world,posOfPedestal,10);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }*/

}
