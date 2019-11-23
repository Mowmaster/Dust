package com.mowmaster.dust.items;

import com.mowmaster.dust.references.Reference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentFishingSpeed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

//import static com.mowmaster.dust.misc.AchievementHandler.achievementScrollA;


public class ItemCoin extends Item
{
    PotionEffect potionEffect = new PotionEffect(MobEffects.LUCK);
    public ItemCoin(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        if(stack.getItem().equals(ItemRegistry.breakerUpgrade) || stack.getItem().equals(ItemRegistry.enchantUpgrade) ||
                stack.getItem().equals(ItemRegistry.placerUpgrade)|| stack.getItem().equals(ItemRegistry.effectUpgrade)||
                stack.getItem().equals(ItemRegistry.chopperUpgrade) || stack.getItem().equals(ItemRegistry.userUpgrade))
        {
            return super.isBookEnchantable(stack, book);
        }

        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

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
        /*if(stack.getItem().equals(ItemRegistry.filterUpgrade)) {tooltip.add("Filter Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.fuzzyFilterUpgrade)) {tooltip.add("Fuzzy Filter Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.filterModUpgrade)) {tooltip.add("Mod Filter Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.filterBlacklistUpgrade)) {tooltip.add("Blacklist Filter Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.fuzzyFilterBlacklistUpgrade)) {tooltip.add("Fuzzy Blacklist Filter Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.filterModBlacklistUpgrade)) {tooltip.add("Mod Blacklist Filter Upgrade");}
        else */
        if(stack.getItem().equals(ItemRegistry.effectUpgrade))
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
        //else if(stack.getItem().equals(ItemRegistry.dropperUpgrade)) {tooltip.add("Item Dropper Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.importUpgrade)) {tooltip.add("Item Stack Import Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.exportUpgrade)) {tooltip.add("Item Stack Export Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.singleExportUpgrade)) {tooltip.add("Export Restocking Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.crafter1Upgrade)) {tooltip.add("Crafter (1x1) Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.crafter4Upgrade)) {tooltip.add("Crafter (2x2) Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.crafter9Upgrade)) {tooltip.add("Crafter (3x3) Upgrade");}
        else if(stack.getItem().equals(ItemRegistry.userUpgrade)) {tooltip.add("Auto Milk/Shearer Upgrade");}
    }
}


