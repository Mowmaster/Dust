package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
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

public class ipuExpDropper extends ipuBasicExpUpgrade
{
    public int range = 0;
    public int summonRate = 7;
    public int operationalSpeed = 0;


    public ipuExpDropper(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    public int getTransferRate(ItemStack stack)
    {
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                summonRate = 7;//1
                break;
            case 1:
                summonRate=16;//2
                break;
            case 2:
                summonRate = 40;//4
                break;
            case 3:
                summonRate = 72;//6
                break;
            case 4:
                summonRate = 112;//8
                break;
            case 5:
                summonRate=160;//10
                break;
            default: summonRate=7;
        }

        return  summonRate;
    }

    public int getRange(ItemStack stack)
    {
        switch (getRangeModifier(stack))
        {
            case 0:
                range = 1;
                break;
            case 1:
                range=2;
                break;
            case 2:
                range = 4;
                break;
            case 3:
                range = 8;
                break;
            case 4:
                range = 12;
                break;
            case 5:
                range=16;
                break;
            default: range=1;
        }

        return  range;
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


    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world, coinInPedestal, pedestalPos);
            }
        }
    }

    public void upgradeAction(World world, ItemStack coinInPedestal, BlockPos posOfPedestal)
    {
        setMaxXP(coinInPedestal,getExpCountByLevel(10) );
        int rate = getTransferRate(coinInPedestal);
        int range = getRange(coinInPedestal);


        TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
        if(pedestalInv instanceof TilePedestal) {
            int currentlyStoredExp = getXPStored(coinInPedestal);
            if(currentlyStoredExp > 0)
            {
                if(currentlyStoredExp < rate)
                {
                    rate = currentlyStoredExp;
                }

                EntityXPOrb expEntity = new EntityXPOrb(world,getPosOfBlockBelow(world,posOfPedestal,-range).getX() + 0.5,getPosOfBlockBelow(world,posOfPedestal,-range).getY(),getPosOfBlockBelow(world,posOfPedestal,-range).getZ() + 0.5,rate);
                expEntity.motionX = 0;
                expEntity.motionY = 0;
                expEntity.motionZ = 0;

                int getExpLeftInPedestal = currentlyStoredExp - rate;
                world.playSound((EntityPlayer)null, posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.BLOCKS, 0.25F, 1.0F);
                setXPStored(coinInPedestal,getExpLeftInPedestal);
                world.playSound((EntityPlayer)null, expEntity.getPosition().getX(), expEntity.getPosition().getY(), expEntity.getPosition().getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.25F, 1.0F);
                world.spawnEntity(expEntity);
            }
        }
    }

    @Override
    public void actionOnColideWithBlock(World world, TilePedestal tilePedestal, BlockPos posPedestal, IBlockState state, Entity entityIn)
    {

    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s2 = getTransferRate(stack);
        int s3 = getRange(stack);

        String trr = "" + s3 + "";

        String tr = "";
        String s5 = "";
        String xp = ""+ getExpLevelFromCount(getXPStored(stack)) +"";


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

        switch (getTransferRate(stack))
        {
            case 7:
                tr = "1 Level";
                break;
            case 16:
                tr="2 Levels";
                break;
            case 40:
                tr = "4 Levels";
                break;
            case 72:
                tr = "6 Levels";
                break;
            case 112:
                tr = "8 Levels";
                break;
            case 160:
                tr="10 Levels";
                break;
            default: tr="1 Level";
        }

        tooltip.add(TextFormatting.GOLD + "Exp Dropper Upgrade");
        tooltip.add(TextFormatting.GREEN + "Exp Levels Stored: "+xp);

        tooltip.add(TextFormatting.AQUA + "Exp Buffer Capacity: 10 Levels");


        if(stack.hasTagCompound())
        {
            if(getTransferRate(stack)>0)
            {
                tooltip.add("Exp Dropped Ammount: " + tr);
            }
            else
            {
                tooltip.add("Exp Dropped Ammount: 1 Level");
            }

            if(stack.isItemEnchanted())
            {
                if(getRange(stack) >0)
                {
                    tooltip.add("Dropper Range: " + trr);
                }
                else
                {
                    tooltip.add("Dropper Range: " + trr);
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
            else
            {
                tooltip.add("Dropper Range: " + trr);
                tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
            }
        }
        else
        {
            tooltip.add("Exp Dropped Ammount: 1 Level");
            tooltip.add("Dropper Range: " + trr);
            tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
        }
    }



}
