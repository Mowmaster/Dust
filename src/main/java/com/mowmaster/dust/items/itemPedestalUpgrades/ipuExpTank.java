package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuExpTank extends ipuBasicExpUpgrade
{
    public ipuExpTank(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    public int getExpBuffer(ItemStack stack)
    {
        int value = 0;
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                value = 100;//
                break;
            case 1:
                value=250;//
                break;
            case 2:
                value = 500;//
                break;
            case 3:
                value = 1000;//
                break;
            case 4:
                value = 10000;//
                break;
            case 5:
                value=100000;//
                break;
            default: value=100;
        }

        return  value;
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(coinInPedestal);
                upgradeActionSendExp( world, coinInPedestal, pedestalPos);
            }
        }
    }

    public void upgradeAction(ItemStack coinInPedestal)
    {
        int maxXPLevel = getExpBuffer(coinInPedestal);
        setMaxXP(coinInPedestal,getExpCountByLevel(maxXPLevel));
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        String tr = "";
        String s5 = getOperationSpeedString(stack);
        int buffer = getExpBuffer(stack);
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

        tooltip.add(TextFormatting.GOLD + "Exp Tank Upgrade");
        tooltip.add(TextFormatting.GREEN + "Exp Levels Stored: "+xp);

        tooltip.add(TextFormatting.AQUA + "Exp Storage Capacity: "+buffer+" Levels");


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
