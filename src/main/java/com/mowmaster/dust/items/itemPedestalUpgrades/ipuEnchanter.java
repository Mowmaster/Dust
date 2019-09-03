package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuEnchanter extends ipuBasic
{
    public int range = 0;

    public ipuEnchanter(String unlocName, String registryName)
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

    public float getEnchantmentPowerFromSorroundings(World world, BlockPos posOfPedestal)
    {
        float enchantPower = 0;

        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (i > -2 && i < 2 && j == -1) {
                    j = 2;
                }
                for (int k = 0; k <= 2; ++k) {
                    BlockPos blockpos = posOfPedestal.add(i, k, j);
                    Block blockNearBy = world.getBlockState(blockpos).getBlock();

                    if (blockNearBy.getEnchantPowerBonus(world, blockpos)>0)
                    {
                        enchantPower +=blockNearBy.getEnchantPowerBonus(world, blockpos);
                    }
                }
            }
        }
        return enchantPower;
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


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s3 = getRange(stack);

        String trr = "" + s3 + "";

        tooltip.add("Enchanter Upgrade");

        if(s3>0)
        {
            tooltip.add("Dropper Range: " + trr);
        }
        else
        {
            tooltip.add("Dropper Range: " + trr);
        }
    }


}
