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

    public ipuShearer(String unlocName, String registryName)
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
        BlockPos negBlockPos = getNegRangePosEntity(world,pedestalPos,width,rangeHeight);
        BlockPos posBlockPos = getPosRangePosEntity(world,pedestalPos,width,rangeHeight);

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
        String tr = "" + (s3+s3+1) + "";
        String s5 = getOperationSpeedString(stack);

        tooltip.add(TextFormatting.GOLD + "Shearing Upgrade");


        if(stack.isItemEnchanted() && s3 > 0)
        {
            tooltip.add(TextFormatting.WHITE + "Effected Area: " + tr+"x"+"2x"+tr);
        }
        else
        {
            tooltip.add(TextFormatting.WHITE + "Effected Are: " + tr+"x"+"2x"+tr);
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
