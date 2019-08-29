package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuDropper extends ipuBasic
{
    public int summonRate = 0;
    public int range = 0;

    public ipuDropper(String unlocName, String registryName)
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

    public int getTransferRate(ItemStack stack)
    {
        switch (getRateModifier(MobEffects.SPEED,stack))
        {
            case 0:
                summonRate = 1;
                break;
            case 1:
                summonRate=4;
                break;
            case 2:
                summonRate = 8;
                break;
            case 3:
                summonRate = 16;
                break;
            case 4:
                summonRate = 32;
                break;
            case 5:
                summonRate=64;
                break;
            default: summonRate=1;
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


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s2 = getTransferRate(stack);
        int s3 = getRange(stack);

        String tr = "" + s2 + "";
        String trr = "" + s3 + "";

        tooltip.add("Item Dropper Upgrade");
        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("coineffect"))
                tooltip.add("Dropped Stack Size: " + tr);
        }
        else
        {
            tooltip.add("Dropped Stack Size: 1");
        }

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
