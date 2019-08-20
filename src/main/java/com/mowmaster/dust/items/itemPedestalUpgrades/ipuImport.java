package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.references.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuImport extends ipuBasic
{
    public int transferRate = 0;

    public ipuImport(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    public int getTransferRate(ItemStack stack)
    {
        switch (getTransferRateModifier(stack))
        {
            case 0:
                transferRate = 1;
                break;
            case 1:
                transferRate=4;
                break;
            case 2:
                transferRate = 8;
                break;
            case 3:
                transferRate = 16;
                break;
            case 4:
                transferRate = 32;
                break;
            case 5:
                transferRate=64;
                break;
            default: transferRate=64;
        }

        return  transferRate;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s2 = getPotionEffectFromStack(stack).getAmplifier()+1;

        String tr = "";
        switch (s2)
        {
            case 0:
                tr = "1";
                break;
            case 1:
                tr = "4";
                break;
            case 2:
                tr = "8";
                break;
            case 3:
                tr = "16";
                break;
            case 4:
                tr = "32";
                break;
            default:
                tr = "64";
        }
        tooltip.add("Item Stack Import Upgrade");
        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("coineffect"))
                tooltip.add("Transfer Rate: " + tr);
        }
        else
        {
            tooltip.add("Transfer Rate: 1");
        }
    }

}
