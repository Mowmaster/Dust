package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ipuBasicExpUpgrade extends ipuBasic
{
    private int summonRate;
    public ipuBasicExpUpgrade() {}

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

    public void upgradeActionSendExp(World world, ItemStack coinInPedestal, BlockPos posOfPedestal)
    {
        TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
        if(pedestalInv instanceof TilePedestal) {
            //If this Pedestal has any Exp
            int expInPedestal = ((TilePedestal) pedestalInv).getStoredValueForUpgrades();
            if(expInPedestal>0)
            {
                //Grab the connected pedestals to send to
                if(((TilePedestal) pedestalInv).getNumberOfStoredLocations()>0)
                {
                    for(int i=0; i<((TilePedestal) pedestalInv).getNumberOfStoredLocations();i++)
                    {
                        BlockPos getStoredPedestalPos = ((TilePedestal) pedestalInv).getStoredPositionAt(i);
                        //Make sure pedestal ISNOT powered and IS loaded in world
                        if(!world.isBlockPowered(getStoredPedestalPos) && world.isBlockLoaded(getStoredPedestalPos))
                        {
                            if(getStoredPedestalPos != posOfPedestal)
                            {
                                TileEntity storedPedestal = world.getTileEntity(getStoredPedestalPos);
                                if(storedPedestal instanceof TilePedestal) {
                                    ItemStack coinOnStoredPedestal = ((TilePedestal) storedPedestal).getCoinOnPedestal();
                                    TilePedestal storedPed = ((TilePedestal) storedPedestal);
                                    //Check if pedestal to send to can even be sent exp
                                    if(coinOnStoredPedestal.getItem() instanceof ipuBasicExpUpgrade)
                                    {
                                        int coinExpMax = ((ipuBasicExpUpgrade)coinOnStoredPedestal.getItem()).readMaxXpFromNBT(coinInPedestal);
                                        int storedExp = storedPed.getStoredValueForUpgrades();
                                        //if Stored Pedestal has room for exp (will be lazy sending exp here)
                                        if(storedExp < coinExpMax)
                                        {
                                            int transferRate = getExpTransferRate(coinInPedestal);
                                            //If we have more then 5 levels in the pedestal we're sending from
                                            if(((TilePedestal) pedestalInv).getStoredValueForUpgrades() >= transferRate)
                                            {
                                                int getExpLeftInPedestal = expInPedestal - transferRate;
                                                int getExpInStoredPed = storedPed.getStoredValueForUpgrades();
                                                int getSetValueforStoredPed = getExpInStoredPed + transferRate;
                                                world.playSound((EntityPlayer)null, posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.BLOCKS, 0.25F, 1.0F);
                                                ((TilePedestal) pedestalInv).setStoredValueForUpgrades(getExpLeftInPedestal);
                                                world.playSound((EntityPlayer)null, getStoredPedestalPos.getX(), getStoredPedestalPos.getY(), getStoredPedestalPos.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.25F, 1.0F);
                                                storedPed.setStoredValueForUpgrades(getSetValueforStoredPed);
                                            }
                                            else
                                            {
                                                //If we have less then 5 levels, just send them all.
                                                int getExpLeftInPedestal = 0;
                                                int getExpInStoredPed = storedPed.getStoredValueForUpgrades();
                                                int getSetValueforStoredPed = getExpInStoredPed + expInPedestal;
                                                world.playSound((EntityPlayer)null, posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.BLOCKS, 0.25F, 1.0F);
                                                ((TilePedestal) pedestalInv).setStoredValueForUpgrades(getExpLeftInPedestal);
                                                world.playSound((EntityPlayer)null, getStoredPedestalPos.getX(), getStoredPedestalPos.getY(), getStoredPedestalPos.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.25F, 1.0F);
                                                storedPed.setStoredValueForUpgrades(getSetValueforStoredPed);
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
        int maths = 0;
        int i = 0;
        int j = 0;

        if(value > 0 && value <= 352)
        {
            maths = (int)Math.sqrt(Math.addExact(36, Math.addExact(4,value )));
            i = (int)(Math.addExact(-6 , maths) / 2);
            /*j = (int)(Math.subtractExact(-6, maths) / 2);

            if(i>j)
            {
                level = Math.abs(i);
            }
            else {
                level = Math.abs(j);
            }*/
        }
        if(value > 352 && value <= 1507)
        {
            maths = (int)Math.sqrt(Math.addExact(164025, Math.addExact(360000,Math.multiplyExact(1000,value ) )));
            i = (int)(Math.addExact(405 , maths) / 50);
            /*j = (int)(Math.subtractExact(405, maths) / 50);

            if(i>j)
            {
                level = Math.abs(i);
            }
            else {
                level = Math.abs(j);
            }*/
        }
        if(value > 1507)
        {

            maths = (int)Math.sqrt(Math.addExact(2640625,Math.multiplyExact(180, Math.addExact(22200,Math.multiplyExact(10,value)))));
            i = (int)(Math.addExact(1625 , maths) / 90);
            /*j = (int)(Math.subtractExact(1625, maths) / 90);

            if(i>j)
            {
                level = Math.abs(i);
            }
            else {
                level = Math.abs(j);
            }*/
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
