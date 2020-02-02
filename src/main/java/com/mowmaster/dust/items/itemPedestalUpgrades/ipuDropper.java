package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.enchantments.EnchantmentUpgradeRange;
import com.mowmaster.dust.enchantments.EnchantmentUpgradeTransferRate;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
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

    public int getTransferRate(ItemStack stack)
    {
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
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


    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world,pedestalPos,coinInPedestal);
            }
        }
    }

    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack coinOnPedestal)
    {
        int rate = getTransferRate(coinOnPedestal);
        int range = getRange(coinOnPedestal);
        if(!getStackInPedestal(world,posOfPedestal).isEmpty())//hasItem
        {
            ItemStack itemToSummon = getStackInPedestal(world,posOfPedestal).copy();
            itemToSummon.setCount(rate);
            EntityItem itemEntity = new EntityItem(world,getPosOfBlockBelow(world,posOfPedestal,-range).getX() + 0.5,getPosOfBlockBelow(world,posOfPedestal,-range).getY(),getPosOfBlockBelow(world,posOfPedestal,-range).getZ() + 0.5,itemToSummon);
            itemEntity.motionX = 0;
            itemEntity.motionY = 0;
            itemEntity.motionZ = 0;
            world.spawnEntity(itemEntity);
            this.removeFromPedestal(world,posOfPedestal,itemToSummon.getCount());
        }
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int range = getRange(stack);
        String tr = "" + getTransferRate(stack) + "";
        String trr = "" + range + "";
        String s5 = getOperationSpeedString(stack);


        tooltip.add(TextFormatting.GOLD + "Item Dropper Upgrade");
        if(stack.hasTagCompound())
        {
            if(getTransferRate(stack)>0)
            {
                tooltip.add(TextFormatting.GRAY + "Dropped Stack Size: " + tr);
            }
            else
            {
                tooltip.add(TextFormatting.GRAY + "Dropped Stack Size: 1");
            }

            if(stack.isItemEnchanted() && range >0)
            {
                tooltip.add(TextFormatting.WHITE + "Dropper Range: " + trr);
            }
            else
            {
                tooltip.add(TextFormatting.WHITE + "Dropper Range: " + trr);
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
        else
        {
            tooltip.add(TextFormatting.GRAY + "Dropped Stack Size: 1");
            tooltip.add(TextFormatting.WHITE + "Dropper Range: " + trr);
            tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
        }
    }


}
