package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuEffectSower extends ipuBasicEffect
{
    public int rangeWidth = 0;

    public ipuEffectSower(String unlocName, String registryName)
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

        int width = getRangeWidth(coinInPedestal);
        int height = (2*width)+1;

        BlockPos negBlockPos = getNegRangePos(world,pedestalPos,width,height);
        BlockPos posBlockPos = getPosRangePos(world,pedestalPos,width,height);

        if(!world.isBlockPowered(pedestalPos)) {
            for (int x = negBlockPos.getX(); x <= posBlockPos.getX(); x++) {
                for (int z = negBlockPos.getZ(); z <= posBlockPos.getZ(); z++) {
                    for (int y = negBlockPos.getY(); y <= posBlockPos.getY(); y++) {
                        BlockPos posTargetBlock = new BlockPos(x, y, z);
                        IBlockState targetBlock = world.getBlockState(posTargetBlock);
                        if (tick%speed == 0) {
                            ticked++;
                        }

                        if(ticked > 84)
                        {
                            upgradeAction(world, itemInPedestal, pedestalPos, posTargetBlock, targetBlock);
                            ticked=0;
                        }
                        else
                        {
                            ticked++;
                        }
                    }
                }
            }
        }
    }

    public void upgradeAction(World world, ItemStack itemInPedestal, BlockPos posOfPedestal, BlockPos posTarget, IBlockState target)
    {
        Random rand = new Random();
        Item singleItemInPedestal = itemInPedestal.getItem();

        if(world.getBlockState(posTarget).getBlock().equals(Blocks.AIR))
        {
            if(singleItemInPedestal instanceof IPlantable)
            {
                IPlantable plantMe = (IPlantable)singleItemInPedestal;
                IBlockState soil = world.getBlockState(posTarget.down());
                IBlockState plantToPlant = plantMe.getPlant(world,posTarget);
                if(soil.getBlock().canSustainPlant(soil, world, posTarget.down(), net.minecraft.util.EnumFacing.UP, plantMe))
                {
                    //Place Seed???
                    this.removeFromPedestal(world,posOfPedestal,1);
                    world.setBlockState(posTarget,plantToPlant);
                    world.playSound((EntityPlayer)null, posTarget.getX(), posTarget.getY(), posTarget.getZ(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                }
            }
        }
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s3 = getRangeWidth(stack);
        String s5 = getOperationSpeedString(stack);
        String tr = "" + (s3+s3+1) + "";

        tooltip.add(TextFormatting.GOLD + "Planter Upgrade");

        if(stack.isItemEnchanted() && s3 > 0)
        {
            tooltip.add(TextFormatting.WHITE + "Effected Area: " + tr+"x"+tr+"x"+tr);
        }
        else
        {
            tooltip.add(TextFormatting.WHITE + "Effected Are: " + tr+"x"+tr+"x"+tr);
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
