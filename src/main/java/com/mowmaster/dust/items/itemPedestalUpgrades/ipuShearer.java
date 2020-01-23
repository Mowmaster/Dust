package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;
import static net.minecraft.block.BlockDirectional.FACING;

public class ipuShearer extends ipuBasic
{
    public int rangeWidth = 0;
    public int rangeHeight = 1;
    public int operationalSpeed = 0;

    public ipuShearer(String unlocName, String registryName)
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
                return blockBelow.add(-intWidth,intHeight,-intWidth);
            case DOWN:
                return blockBelow.add(-intWidth,0,-intWidth);
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
                return blockBelow.add(intWidth+1,intHeight+1,intWidth+1);
            case DOWN:
                return blockBelow.add(intWidth+1,intHeight,intWidth+1);
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
                upgradeAction(world, itemInPedestal,pedestalPos, coinInPedestal);
            }
        }
    }

    //Note: Could use getEntitieswithingAABB one method that nulls entity and just needs aabb box
    public void upgradeAction(World world, ItemStack itemInPedestal,BlockPos pedestalPos, ItemStack coinInPedestal)
    {
        int width = getRangeWidth(coinInPedestal);
        BlockPos negBlockPos = getNegRangePos(world,pedestalPos,width,rangeHeight);
        BlockPos posBlockPos = getPosRangePos(world,pedestalPos,width,rangeHeight);

        AxisAlignedBB getBox = new AxisAlignedBB(negBlockPos,posBlockPos);
        //Entity Creature could be used to cover creepers for better with mods and such
        List<EntityLivingBase> baa = world.getEntitiesWithinAABB(EntityLivingBase.class,getBox);
        for(EntityLivingBase baaaaaa : baa)
        {
            if(baaaaaa instanceof IShearable)
            {
                IShearable baabaa = (IShearable)baaaaaa;
                if (baabaa.isShearable(new ItemStack(Items.SHEARS),world,baaaaaa.getPosition()))
                {
                    if(getStackInPedestal(world,pedestalPos).equals(ItemStack.EMPTY))
                    {
                        Random rando = new Random();
                        int i = 1 + rando.nextInt(3);
                        List<ItemStack> drops = baabaa.onSheared(new ItemStack(Items.SHEARS),world,baaaaaa.getPosition(),0);

                        for (int j = 0; j < i; ++j)
                        {
                            if(drops.size()>0)
                            {
                                for (int d=0;d<drops.size();d++)
                                {
                                    if(itemInPedestal.isEmpty() || drops.get(d).equals(new ItemStack(itemInPedestal.getItem(),1,itemInPedestal.getMetadata())))
                                    {
                                        addToPedestal(world,pedestalPos,drops.get(d));
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

        tooltip.add(TextFormatting.GOLD + "Shearing Upgrade");


        if(s3>0)
        {
            tooltip.add("Effected Area: " + tr+"x"+"2x"+tr);
        }
        else
        {
            tooltip.add("Effected Are: " + tr+"x"+"2x"+tr);
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
