package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
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

public class ipuExpCollector extends ipuBasicExpUpgrade
{
    public int rangeWidth = 0;
    public int suckiRate = 0;

    public ipuExpCollector(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    public int getRangeWidth(ItemStack stack)
    {
        int rW = getRangeModifier(stack);
        rangeWidth = ((rW)+1);
        return  rangeWidth;
    }

    public int getSuckiRate(ItemStack stack)
    {
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                suckiRate = 7;//1
                break;
            case 1:
                suckiRate=16;//2
                break;
            case 2:
                suckiRate = 27;//3
                break;
            case 3:
                suckiRate = 40;//4
                break;
            case 4:
                suckiRate = 55;//5
                break;
            case 5:
                suckiRate=160;//10
                break;
            default: suckiRate=7;
        }

        return  suckiRate;
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world, coinInPedestal, pedestalPos);
                upgradeActionSendExp(world, coinInPedestal,pedestalPos);
            }
        }
    }

    public void upgradeAction(World world, ItemStack coinInPedestal, BlockPos posOfPedestal)
    {
        setMaxXP(coinInPedestal,getExpCountByLevel(30) );
        int width = getRangeWidth(coinInPedestal);
        int height = (2*width)+1;
        BlockPos negBlockPos = getNegRangePosEntity(world,posOfPedestal,width,height);
        BlockPos posBlockPos = getPosRangePosEntity(world,posOfPedestal,width,height);

        AxisAlignedBB getBox = new AxisAlignedBB(negBlockPos,posBlockPos);

        List<EntityXPOrb> xpList = world.getEntitiesWithinAABB(EntityXPOrb.class,getBox);
        for(EntityXPOrb getXPFromList : xpList)
        {
            world.playSound((EntityPlayer)null, posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.15F, 1.0F);
            TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
            if(pedestalInv instanceof TilePedestal) {
                int currentlyStoredExp = getXPStored(coinInPedestal);
                if(currentlyStoredExp < readMaxXpFromNBT(coinInPedestal))
                {
                    int value = getXPFromList.getXpValue();
                    getXPFromList.setDead();
                    setXPStored(coinInPedestal, currentlyStoredExp + value);
                }
            }
            break;
        }
    }

    @Override
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

        if(entityIn instanceof EntityPlayer)
        {
            EntityPlayer getPlayer = ((EntityPlayer)entityIn);
            ItemStack coin = tilePedestal.getCoinOnPedestal();
            if(!getPlayer.isSneaking())
            {
                int currentlyStoredExp = getXPStored(coin);
                if(currentlyStoredExp < readMaxXpFromNBT(coin))
                {
                    int transferRate = getSuckiRate(coin);
                    int value = removeXp(getPlayer, transferRate);
                    if(value > 0)
                    {
                        world.playSound((EntityPlayer)null, posPedestal.getX(), posPedestal.getY(), posPedestal.getZ(), SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.BLOCKS, 0.15F, 1.0F);
                        setXPStored(coin, currentlyStoredExp + value);
                    }
                }
            }
        }
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s3 = getRangeWidth(stack);
        String s5 = getOperationSpeedString(stack);
        String tr = "";
        String sr = "";
        String xp = ""+ getExpLevelFromCount(getXPStored(stack)) +"";

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

        /*switch (getSuckiRate(stack))
        {
            case 7:
                sr = "1 Level";
                break;
            case 16:
                sr="2 Levels";
                break;
            case 27:
                sr = "3 Levels";
                break;
            case 40:
                sr = "4 Levels";
                break;
            case 55:
                sr = "5 Levels";
                break;
            case 160:
                sr="10 Levels";
                break;
            default: sr="1 Level";
        }*/

        String trr = "" + (s3+s3+1) + "";

        tooltip.add(TextFormatting.GOLD + "Exp Collector Upgrade");
        tooltip.add(TextFormatting.GREEN + "Exp Levels Stored: "+xp);
        tooltip.add(TextFormatting.AQUA + "Exp Buffer Capacity: 30 Levels");


        /*if(stack.hasTagCompound()) {
            if (getSuckiRate(stack) > 0) {
                tooltip.add("???: " + sr);
            } else {
                tooltip.add("???: 1 Level");
            }
        }*/

        if(stack.isItemEnchanted() && s3 > 0)
        {
            tooltip.add(TextFormatting.WHITE + "Effected Area: " + trr+"x"+trr+"x"+trr);
        }
        else
        {
            tooltip.add(TextFormatting.WHITE + "Effected Area: " + trr+"x"+trr+"x"+trr);
        }

        if(getExpTransferRate(stack)>0)
        {
            tooltip.add(TextFormatting.GRAY + "Exp Transfer Ammount: " + tr);
        }
        else
        {
            tooltip.add(TextFormatting.GRAY + "Exp Transfer Ammount: 5 Levels");
        }

        if(stack.isItemEnchanted() && getOperationSpeed(stack) >0)
        {
            tooltip.add(TextFormatting.RED + "Operational Speed: " + s5);
        }
        else
        {
            tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
        }
    }



}
