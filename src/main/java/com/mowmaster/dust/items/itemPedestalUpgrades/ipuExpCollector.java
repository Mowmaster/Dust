package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;
import static net.minecraft.block.BlockDirectional.FACING;

public class ipuExpCollector extends ipuBasic
{
    public int rangeWidth = 0;
    public int operationalSpeed = 0;
    public int maxXP = 1395;

    public ipuExpCollector(String unlocName, String registryName)
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

    public int getRangeWidth(ItemStack stack)
    {
        int rW = getRangeModifier(stack);
        rangeWidth = ((rW)+1);
        return  rangeWidth;
    }

    public BlockPos getNegRangePos(World world,BlockPos posOfPedestal, int intWidth, int intHeight)
    {
        IBlockState state = world.getBlockState(posOfPedestal);
        EnumFacing enumfacing = state.getValue(FACING);
        BlockPos blockBelow = posOfPedestal;
        switch (enumfacing)
        {
            case UP:
                return blockBelow.add(-intWidth,0,-intWidth);
            case DOWN:
                return blockBelow.add(-intWidth,-intHeight,-intWidth);
            case NORTH:
                return blockBelow.add(-intWidth,-intWidth,-intHeight);
            case SOUTH:
                return blockBelow.add(-intWidth,-intWidth,0);
            case EAST:
                return blockBelow.add(0,-intWidth,-intWidth);
            case WEST:
                return blockBelow.add(-intHeight,-intWidth,-intWidth);
            default:
                return blockBelow;
        }
    }

    public BlockPos getPosRangePos(World world,BlockPos posOfPedestal, int intWidth, int intHeight)
    {
        IBlockState state = world.getBlockState(posOfPedestal);
        EnumFacing enumfacing = state.getValue(FACING);
        BlockPos blockBelow = posOfPedestal;
        switch (enumfacing)
        {
            case UP:
                return blockBelow.add(intWidth+1,intHeight,intWidth+1);
            case DOWN:
                return blockBelow.add(intWidth+1,0,intWidth+1);
            case NORTH:
                return blockBelow.add(intWidth+1,intWidth,0+1);
            case SOUTH:
                return blockBelow.add(intWidth+1,intWidth,intHeight+1);
            case EAST:
                return blockBelow.add(intHeight+1,intWidth,intWidth+1);
            case WEST:
                return blockBelow.add(0+1,intWidth,intWidth+1);
            default:
                return blockBelow;
        }
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
                upgradeAction(world, coinInPedestal, pedestalPos);
            }
        }
    }

    public void upgradeAction(World world, ItemStack coinInPedestal, BlockPos posOfPedestal)
    {
        int width = getRangeWidth(coinInPedestal);
        int height = (2*width)+1;
        BlockPos negBlockPos = getNegRangePos(world,posOfPedestal,width,height);
        BlockPos posBlockPos = getPosRangePos(world,posOfPedestal,width,height);

        AxisAlignedBB getBox = new AxisAlignedBB(negBlockPos,posBlockPos);

        List<EntityXPOrb> xpList = world.getEntitiesWithinAABB(EntityXPOrb.class,getBox);
        for(EntityXPOrb getXPFromList : xpList)
        {
            world.playSound((EntityPlayer)null, posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.5F, 1.0F);
            TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
            if(pedestalInv instanceof TilePedestal) {
                int currentlyStoredExp = ((TilePedestal) pedestalInv).getStoredValueForUpgrades();
                if(currentlyStoredExp < maxXP)
                {
                    int value = getXPFromList.getXpValue();
                    getXPFromList.setDead();
                    ((TilePedestal) pedestalInv).setStoredValueForUpgrades(currentlyStoredExp + value);
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
            EntityXPOrb getXPFromList = ((EntityXPOrb)entityIn);
            world.playSound((EntityPlayer)null, posPedestal.getX(), posPedestal.getY(), posPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.5F, 1.0F);
            int currentlyStoredExp = tilePedestal.getStoredValueForUpgrades();
            if(currentlyStoredExp < maxXP)
            {
                int value = getXPFromList.getXpValue();
                getXPFromList.setDead();
                tilePedestal.setStoredValueForUpgrades(currentlyStoredExp + value);
            }
        }
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s3 = getRangeWidth(stack);
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

        String tr = "" + (s3+s3+1) + "";

        tooltip.add(TextFormatting.GOLD + "Exp Collector Upgrade");


        if(s3>0)
        {
            tooltip.add("Effected Area: " + tr+"x"+tr+"x"+tr);
        }
        else
        {
            tooltip.add("Effected Are: " + tr+"x"+tr+"x"+tr);
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
