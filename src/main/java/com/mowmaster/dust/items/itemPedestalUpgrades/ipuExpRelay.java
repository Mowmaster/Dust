package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuExpRelay extends ipuBasicExpUpgrade
{
    public int operationalSpeed = 0;
    public int maxXP = 1395;


    public ipuExpRelay(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return super.isBookEnchantable(stack, book);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxXP() {
        return maxXP;
    }

    public int getOperationSpeed(ItemStack stack)
    {
        switch (getTransferRateModifier(stack))
        {
            case 0:
                operationalSpeed = 20;//normal speed
                break;
            case 1:
                operationalSpeed=10;//2x faster
                break;
            case 2:
                operationalSpeed = 5;//4x faster
                break;
            case 3:
                operationalSpeed = 3;//6x faster
                break;
            case 4:
                operationalSpeed = 2;//10x faster
                break;
            case 5:
                operationalSpeed=1;//20x faster
                break;
            default: operationalSpeed=20;
        }

        return  operationalSpeed;
    }

    public int ticked = 0;

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeActionSendExp( world, coinInPedestal, pedestalPos);
            }
        }
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
                                    Item coinOnStoredPedestal = ((TilePedestal) storedPedestal).getCoinOnPedestal().getItem();
                                    TilePedestal storedPed = ((TilePedestal) storedPedestal);
                                    //Check if pedestal to send to can even be sent exp
                                    if(coinOnStoredPedestal instanceof ipuBasicExpUpgrade)
                                    {
                                        int coinExpMax = ((ipuBasicExpUpgrade)coinOnStoredPedestal).getMaxXP();
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
                                                world.playSound((EntityPlayer)null, posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.BLOCKS, 0.5F, 1.0F);
                                                ((TilePedestal) pedestalInv).setStoredValueForUpgrades(getExpLeftInPedestal);
                                                world.playSound((EntityPlayer)null, getStoredPedestalPos.getX(), getStoredPedestalPos.getY(), getStoredPedestalPos.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.5F, 1.0F);
                                                storedPed.setStoredValueForUpgrades(getSetValueforStoredPed);
                                            }
                                            else
                                            {
                                                //If we have less then 5 levels, just send them all.
                                                int getExpLeftInPedestal = 0;
                                                int getExpInStoredPed = storedPed.getStoredValueForUpgrades();
                                                int getSetValueforStoredPed = getExpInStoredPed + expInPedestal;
                                                world.playSound((EntityPlayer)null, posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.BLOCKS, 0.5F, 1.0F);
                                                ((TilePedestal) pedestalInv).setStoredValueForUpgrades(getExpLeftInPedestal);
                                                world.playSound((EntityPlayer)null, getStoredPedestalPos.getX(), getStoredPedestalPos.getY(), getStoredPedestalPos.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.5F, 1.0F);
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

    @Override
    public void actionOnColideWithBlock(World world, TilePedestal tilePedestal, BlockPos posPedestal, IBlockState state, Entity entityIn)
    {
        if(entityIn instanceof EntityXPOrb)
        {
            EntityXPOrb getXPFromList = ((EntityXPOrb)entityIn);
            world.playSound((EntityPlayer)null, posPedestal.getX(), posPedestal.getY(), posPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.5F, 1.0F);
            int currentlyStoredExp = tilePedestal.getStoredValueForUpgrades();
            if(currentlyStoredExp < getMaxXP())
            {
                int value = getXPFromList.getXpValue();
                getXPFromList.setDead();
                tilePedestal.setStoredValueForUpgrades(currentlyStoredExp + value);
            }
        }
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        String tr = "";
        String s5 = "";

        switch (getOperationSpeed(stack))
        {
            case 1:
                s5 = "20x Faster";
                break;
            case 2:
                s5="10x Faster";
                break;
            case 3:
                s5 = "6x Faster";
                break;
            case 5:
                s5 = "4x Faster";
                break;
            case 10:
                s5 = "2x Faster";
                break;
            case 20:
                s5="Normal Speed";
                break;
            default: s5="Normal Speed";
        }

        switch (getExpTransferRate(stack))
        {
            case 55:
                tr = "5 Levels";
                break;
            case 160:
                tr="10 Levels";
                break;
            case 315:
                tr = "15 Levels";
                break;
            case 550:
                tr = "20 Levels";
                break;
            case 910:
                tr = "25 Levels";
                break;
            case 1395:
                tr="30 Levels";
                break;
            default: tr="5 Levels";
        }

        tooltip.add(TextFormatting.GOLD + "Exp Relay Upgrade");

        if(getExpTransferRate(stack)>0)
        {
            tooltip.add("Exp Transfer Ammount: " + tr);
        }
        else
        {
            tooltip.add("Exp Transfer Ammount: 5 Levels");
        }

        if(stack.isItemEnchanted() && getOperationSpeed(stack) >0)
        {
            tooltip.add("Operational Speed: " + s5);
        }
        else
        {
            tooltip.add("Operational Speed: Normal Speed");
        }
    }



}
