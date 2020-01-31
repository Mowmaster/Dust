package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuExpTank extends ipuBasicExpUpgrade
{
    public int operationalSpeed = 0;
    //10000 levels
    public int maxXP;


    public ipuExpTank(String unlocName, String registryName)
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

    public int getExpBuffer(ItemStack stack)
    {
        int value = 0;
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                value = 100;//30
                break;
            case 1:
                value=250;//44
                break;
            case 2:
                value = 500;//58
                break;
            case 3:
                value = 1000;//72
                break;
            case 4:
                value = 10000;//86
                break;
            case 5:
                value=100000;//100
                break;
            default: value=100;
        }

        return  value;
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
                upgradeAction(coinInPedestal);
                upgradeActionSendExp( world, coinInPedestal, pedestalPos);
            }
        }
    }

    public void upgradeAction(ItemStack coinInPedestal)
    {
        int maxXPLevel = getExpBuffer(coinInPedestal);
        setMaxXP(coinInPedestal,getExpCountByLevel(maxXPLevel) );
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        String tr = "";
        String s5 = "";
        int buffer = getExpBuffer(stack);

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

        tooltip.add(TextFormatting.AQUA + "Exp Storage Capacity: "+buffer+" Levels");

        if(getExpTransferRate(stack)>0)
        {
            tooltip.add("Exp Transfer Ammount: " + tr);
        }
        else
        {
            tooltip.add("Exp Transfer Ammount: 5 Levels");
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
