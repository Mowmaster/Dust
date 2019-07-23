package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class ipuBasic extends Item
{
    public ipuBasic() {}

    public boolean isFilter;

    private boolean hasEnchant;

    private boolean hasEffect;

    private int intRate = 0;



    public boolean hasEnchant(ItemStack stack)
    {
       hasEnchant =  stack.isItemEnchanted();
       return hasEnchant;
    }

    public int getRange(ItemStack stack)
    {
        int range = 0;
        if(hasEffect(stack))
        {
           range = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentRange,stack)+1;
        }

        return range;
    }

    public boolean hasEffect(ItemStack stack)
    {
        hasEffect = false;
        if(stack.hasTagCompound())
        {
            hasEffect = stack.getTagCompound().hasKey("coineffect");
        }
        return hasEffect;
    }

    public PotionEffect getEffect(ItemStack stack)
    {
        PotionEffect potionEffect = new PotionEffect(MobEffects.LUCK);


            if(hasEffect(stack))
            {
                potionEffect = PotionEffect.readCustomPotionEffectFromNBT(stack.getTagCompound().getCompoundTag("coineffect"));
            }

        return potionEffect;
    }

    public int getPotency(ItemStack stack)
    {
        return getEffect(stack).getAmplifier()+1;
    }

    public int getTransferRateModifier(ItemStack stack)
    {
        if(hasEffect(stack))
        {
            if(getEffect(stack).getPotion().equals(MobEffects.SPEED))
            {
                intRate = getPotency(stack);
            }
        }

        return intRate;
    }

    public ItemStack getStackInPedestal(World world, BlockPos posOfPedestal)
    {
        ItemStack stackInPedestal = ItemStack.EMPTY;
        TileEntity pedestalInventory = world.getTileEntity(posOfPedestal);
        if(pedestalInventory instanceof TilePedestal) {
            stackInPedestal = pedestalInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN).getStackInSlot(0);
        }

        return stackInPedestal;
    }


    PotionEffect potionEffect = new PotionEffect(MobEffects.LUCK);
    public PotionEffect getPotionEffectFromStack(ItemStack stack)
    {
        if(stack.hasTagCompound())
        {
            potionEffect = PotionEffect.readCustomPotionEffectFromNBT(stack.getTagCompound().getCompoundTag("coineffect"));
        }
        return potionEffect;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        String s1 = I18n.translateToLocal(getPotionEffectFromStack(stack).getEffectName()).trim();
        int s2 = getPotionEffectFromStack(stack).getAmplifier()+1;
        String count = "";
        switch (s2)
        {
            case 0:
                count = "I";
                break;
            case 1:
                count = "II";
                break;
            case 2:
                count = "III";
                break;
            case 3:
                count = "IV";
                break;
            case 4:
                count = "V";
                break;
            case 5:
                count = "VI";
                break;
            case 6:
                count = "VII";
                break;
            case 7:
                count = "VIII";
                break;
            case 8:
                count = "IX";
                break;
            case 9:
                count = "X";
                break;
        }
        if(stack.getItem().equals(ItemRegistry.filterUpgrade)) {tooltip.add("Filter Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.fuzzyFilterUpgrade)) {tooltip.add("Fuzzy Filter Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.filterModUpgrade)) {tooltip.add("Mod Filter Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.filterBlacklistUpgrade)) {tooltip.add("Blacklist Filter Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.fuzzyFilterBlacklistUpgrade)) {tooltip.add("Fuzzy Blacklist Filter Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.filterModBlacklistUpgrade)) {tooltip.add("Mod Blacklist Filter Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.effectUpgrade))
        {
            tooltip.add("Pedestal Effect Upgrade");
            if(stack.hasTagCompound())
            {
                if(stack.getTagCompound().hasKey("coineffect"))
                    //tooltip.add(s1 + " " + s2);
                    tooltip.add(s1);
            }
        }
        else if(stack.getItem().equals(ItemRegistry.enchantUpgrade)) {tooltip.add("XP Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.chopperUpgrade)) {tooltip.add("Tree Chopper Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.breakerUpgrade)) {tooltip.add("Block Breaker Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.placerUpgrade)) {tooltip.add("Block Placer Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.dropperUpgrade)) {tooltip.add("Item Dropper Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.importUpgrade)) { tooltip.add("Item Import Upgrade"); }
        else if(stack.getItem().equals(ItemRegistry.exportUpgrade)) {tooltip.add("Item Stack Export Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.singleExportUpgrade)) {tooltip.add("Export Restocking Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.crafter1Upgrade)) {tooltip.add("Crafter (1x1) Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.crafter4Upgrade)) {tooltip.add("Crafter (2x2) Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.crafter9Upgrade)) {tooltip.add("Crafter (3x3) Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.userUpgrade)) {tooltip.add("Auto Milk/Shearer Upgrade");}
    }
}
