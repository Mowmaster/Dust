package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;
import static net.minecraft.block.BlockDirectional.FACING;

public class ipuChopper extends ipuBasic
{
    public int rangeWidth = 0;
    public int rangeHeight = 0;

    public ipuChopper(String unlocName, String registryName)
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
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    public int getRangeWidth(ItemStack stack)
    {
        int rW = getRangeModifier(stack);
        rangeWidth = ((rW)+1);
        return  rangeWidth;
    }

    public int getRangeHeight(ItemStack stack)
    {
        int rH = getRangeModifier(stack);
        rangeHeight = ((rH*6)+4);
        return rangeHeight;
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
                return blockBelow.add(intWidth,intHeight,intWidth);
            case DOWN:
                return blockBelow.add(intWidth,0,intWidth);
            case NORTH:
                return blockBelow.add(intWidth,intWidth,0);
            case SOUTH:
                return blockBelow.add(intWidth,intWidth,intHeight);
            case EAST:
                return blockBelow.add(intHeight,intWidth,intWidth);
            case WEST:
                return blockBelow.add(0,intWidth,intWidth);
            default:
                return blockBelow;
        }
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s3 = getRangeWidth(stack);
        int s4 = getRangeHeight(stack);

        String tr = "" + (s3+s3+1) + "";
        String trr = "" + (s4+1) + "";

        tooltip.add("Chopper Upgrade");


        if(s3>0)
        {
            tooltip.add("Effected Area: " + tr+"x"+tr+"x"+trr);
        }
        else
        {
            tooltip.add("Effected Are: " + tr+"x"+tr+"x"+trr);
        }
    }


}
