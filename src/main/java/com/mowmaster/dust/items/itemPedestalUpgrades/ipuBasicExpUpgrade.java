package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ipuBasicExpUpgrade extends ipuBasic
{
    private int summonRate;
    public ipuBasicExpUpgrade() {}

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return super.isBookEnchantable(stack, book);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    public void setMaxXP(ItemStack stack, int value)
    {
        writeMaxXpToNBT(stack, value);
    }

    public int getExpTransferRate(ItemStack stack)
    {
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                summonRate = 55;//5
                break;
            case 1:
                summonRate=160;//10
                break;
            case 2:
                summonRate = 315;//15
                break;
            case 3:
                summonRate = 550;//20
                break;
            case 4:
                summonRate = 910;//25
                break;
            case 5:
                summonRate=1395;//30
                break;
            default: summonRate=55;
        }

        return  summonRate;
    }

    public static int removeXp(EntityPlayer player, int amount) {
        //Someday consider using player.addExpierence()
        int startAmount = amount;
        while(amount > 0) {
            int barCap = player.xpBarCap();
            int barXp = (int) (barCap * player.experience);
            int removeXp = Math.min(barXp, amount);
            int newBarXp = barXp - removeXp;
            amount -= removeXp;//amount = amount-removeXp

            player.experienceTotal -= removeXp;
            if(player.experienceTotal < 0) {
                player.experienceTotal = 0;
            }
            if(newBarXp == 0 && amount > 0) {
                player.experienceLevel--;
                if(player.experienceLevel < 0) {
                    player.experienceLevel = 0;
                    player.experienceTotal = 0;
                    player.experience = 0;
                    break;
                } else {
                    player.experience = 1.0F;
                }
            } else {
                player.experience = newBarXp / (float) barCap;
            }
        }
        return startAmount - amount;
    }

    public void upgradeActionSendExp(World world, ItemStack coinMainPedestal, BlockPos posMainPedestal)
    {
        TileEntity pedestalInv = world.getTileEntity(posMainPedestal);
        if(pedestalInv instanceof TilePedestal) {
            TilePedestal tileMainPedestal = ((TilePedestal) pedestalInv);
            //If this Pedestal has any Exp
            int xpMainPedestal = getXPStored(coinMainPedestal);
            if(xpMainPedestal>0)
            {
                //Grab the connected pedestals to send to
                if(tileMainPedestal.getNumberOfStoredLocations()>0)
                {
                    for(int i=0; i<tileMainPedestal.getNumberOfStoredLocations();i++)
                    {
                        BlockPos posStoredPedestal = tileMainPedestal.getStoredPositionAt(i);
                        //Make sure pedestal ISNOT powered and IS loaded in world
                        if(!world.isBlockPowered(posStoredPedestal) && world.isBlockLoaded(posStoredPedestal))
                        {
                            if(posStoredPedestal != posMainPedestal)
                            {
                                TileEntity storedPedestal = world.getTileEntity(posStoredPedestal);
                                if(storedPedestal instanceof TilePedestal) {
                                    TilePedestal tileStoredPedestal = ((TilePedestal) storedPedestal);
                                    ItemStack coinStoredPedestal = tileStoredPedestal.getCoinOnPedestal();
                                    //Check if pedestal to send to can even be sent exp
                                    if(coinStoredPedestal.getItem() instanceof ipuBasicExpUpgrade)
                                    {
                                        int xpMaxStoredPedestal = ((ipuBasicExpUpgrade)coinStoredPedestal.getItem()).readMaxXpFromNBT(coinStoredPedestal);
                                        int xpStoredPedestal = getXPStored(coinStoredPedestal);
                                        //if Stored Pedestal has room for exp (will be lazy sending exp here)
                                        if(xpStoredPedestal < xpMaxStoredPedestal)
                                        {
                                            int transferRate = getExpTransferRate(coinMainPedestal);
                                            //If we have more then X levels in the pedestal we're sending from
                                            if(xpMainPedestal >= transferRate)
                                            {
                                                int xpRemainingMainPedestal = xpMainPedestal - transferRate;
                                                int xpRemainingStoredPedestal = xpStoredPedestal + transferRate;
                                                world.playSound((EntityPlayer)null, posMainPedestal.getX(), posMainPedestal.getY(), posMainPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.BLOCKS, 0.15F, 1.0F);
                                                setXPStored(coinMainPedestal,xpRemainingMainPedestal);
                                                world.playSound((EntityPlayer)null, posStoredPedestal.getX(), posStoredPedestal.getY(), posStoredPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.15F, 1.0F);
                                                setXPStored(coinStoredPedestal,xpRemainingStoredPedestal);
                                            }
                                            else
                                            {
                                                //If we have less then X levels, just send them all.
                                                int xpRemainingMainPedestal = 0;
                                                int xpRemainingStoredPedestal = xpStoredPedestal + xpMainPedestal;
                                                world.playSound((EntityPlayer)null, posMainPedestal.getX(), posMainPedestal.getY(), posMainPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.BLOCKS, 0.15F, 1.0F);
                                                setXPStored(coinMainPedestal,xpRemainingMainPedestal);
                                                world.playSound((EntityPlayer)null, posStoredPedestal.getX(), posStoredPedestal.getY(), posStoredPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.15F, 1.0F);
                                                setXPStored(coinStoredPedestal,xpRemainingStoredPedestal);
                                            }

                                            break;
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

    public void actionOnColideWithBlock(World world, TilePedestal tilePedestal, BlockPos posPedestal, IBlockState state, Entity entityIn)
    {
        if(entityIn instanceof EntityXPOrb)
        {
            ItemStack coin = tilePedestal.getCoinOnPedestal();
            EntityXPOrb getXPFromList = ((EntityXPOrb)entityIn);
            world.playSound((EntityPlayer)null, posPedestal.getX(), posPedestal.getY(), posPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.15F, 1.0F);
            int currentlyStoredExp = getXPStored(coin);
            if(currentlyStoredExp < readMaxXpFromNBT(coin))
            {
                int value = getXPFromList.getXpValue();
                getXPFromList.setDead();
                setXPStored(coin, currentlyStoredExp + value);
            }
        }
    }

    public int getExpCountByLevel(int level)
    {
        int expUsed = 0;

        if(level <= 16)
        {
            expUsed = (level*level) + (6 * level);
        }
        else if(level > 16 && level <=31)
        {
            expUsed = (int)(((2.5 * (level*level)) - (40.5 * level))+360);
        }
        else if(level > 31)
        {
            expUsed = (int)(((4.5 * (level*level)) - (162.5 * level))+2220);
        }

        return expUsed;
    }

    public int getExpLevelFromCount(int value)
    {
        int level = 0;
        long maths = 0;
        int i = 0;
        int j = 0;

        if(value > 0 && value <= 352)
        {
            maths = (long)Math.sqrt(Math.addExact((long)36, Math.addExact((long)4,(long)value )));
            i = (int)(Math.addExact((long)-6 , maths) / 2);
        }
        if(value > 352 && value <= 1507)
        {
            maths = (long)Math.sqrt(Math.subtractExact((long)164025, Math.multiplyExact((long)100,Math.subtractExact((long)3600,Math.multiplyExact((long)10,(long)value)))));

            i = (int)(Math.addExact((long)405 , maths) / 50);
        }
        if(value > 1507)
        {

            maths = (long)Math.sqrt(Math.subtractExact((long)2640625,Math.multiplyExact((long)180, Math.subtractExact((long)22200,Math.multiplyExact((long)10,(long)value)))));
            i = (int)(Math.addExact((long)1625 , maths) / 90);
        }

        return Math.abs(i);
    }

    public void setXPStored(ItemStack stack, int value)
    {
        NBTTagCompound compound = new NBTTagCompound();
        if(stack.hasTagCompound())
        {
            compound = stack.getTagCompound();
        }

        compound.setInteger("xpstored",value);
        stack.setTagCompound(compound);
    }

    public int getXPStored(ItemStack stack)
    {
        int storedxp = 0;
        if(stack.hasTagCompound())
        {
            NBTTagCompound getCompound = stack.getTagCompound();
            storedxp = getCompound.getInteger("xpstored");
        }
        return storedxp;
    }

    public void writeMaxXpToNBT(ItemStack stack, int value)
    {
        NBTTagCompound compound = new NBTTagCompound();
        if(stack.hasTagCompound())
        {
            compound = stack.getTagCompound();
        }

        compound.setInteger("maxxp",value);
        stack.setTagCompound(compound);
    }

    public int readMaxXpFromNBT(ItemStack stack)
    {
        int maxxp = 0;
        if(stack.hasTagCompound())
        {
            NBTTagCompound getCompound = stack.getTagCompound();
            maxxp = getCompound.getInteger("maxxp");
        }
        return maxxp;
    }
}
