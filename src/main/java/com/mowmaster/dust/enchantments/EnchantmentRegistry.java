package com.mowmaster.dust.enchantments;

import com.mowmaster.dust.blocks.machines.BlockPedestal;
import com.mowmaster.dust.dust;
import com.mowmaster.dust.items.itemPedestalUpgrades.ipuBasic;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class EnchantmentRegistry
{

    public static EnchantmentDigger enchantDigger = new EnchantmentDigger();
    //public static EnchantmentVamperic enchantVamperic = new EnchantmentVamperic(Enchantment.Rarity.RARE,new ResourceLocation("dust", "enchantVamperic"));
    public static EnchantmentLastResort enchantLastResort = new EnchantmentLastResort(Enchantment.Rarity.RARE,new ResourceLocation("dust", "enchantLastResort"));
    public static EnchantmentSteadfast enchantSteadfast = new EnchantmentSteadfast(Enchantment.Rarity.RARE,new ResourceLocation("dust", "enchantSteadfast"));
    public static EnchantmentStepAssist enchantmentStepAssist = new EnchantmentStepAssist(Enchantment.Rarity.RARE,new ResourceLocation("dust", "enchantStepAssist"));
    public static EnchantmentQuickPace enchantmentQuickPace = new EnchantmentQuickPace(Enchantment.Rarity.RARE,new ResourceLocation("dust", "enchantQuickPace"));
    public static EnchantmentFlight enchantmentFlight = new EnchantmentFlight(Enchantment.Rarity.RARE,new ResourceLocation("dust", "enchantFlight"));
    //public static EnchantmentSmelt enchantmentSmelter = new EnchantmentSmelt();
    public static EnchantmentUpgradeRange enchantmentRange = new EnchantmentUpgradeRange();
    public static EnchantmentUpgradeTransferRate enchantmentTransferRate = new EnchantmentUpgradeTransferRate();

    public static final EnumEnchantmentType UPGRADES = EnumHelper.addEnchantmentType("upgrades", (item)->(item instanceof ipuBasic));
    public static final EnumEnchantmentType PEDESTALS = EnumHelper.addEnchantmentType("pedestals", (item)->(Block.getBlockFromItem(item) instanceof BlockPedestal));

    public static void Init(){
        //GameRegistry.register(enchantDigger, );
        //ForgeRegistries.ENCHANTMENTS.register(enchantVamperic);
        //ForgeRegistries.ENCHANTMENTS.register(enchantLastResort);
        //ForgeRegistries.ENCHANTMENTS.register(enchantSteadfast);
        //ForgeRegistries.ENCHANTMENTS.register(enchantmentQuickPace);
        //ForgeRegistries.ENCHANTMENTS.register(new EnchantmentCopy());
        ForgeRegistries.ENCHANTMENTS.register(enchantDigger);
        ForgeRegistries.ENCHANTMENTS.register(enchantmentStepAssist);
        //ForgeRegistries.ENCHANTMENTS.register(enchantmentFlight);
        //ForgeRegistries.ENCHANTMENTS.register(enchantmentSmelter);
        ForgeRegistries.ENCHANTMENTS.register(enchantmentRange);
        ForgeRegistries.ENCHANTMENTS.register(enchantmentTransferRate);


    }




}
