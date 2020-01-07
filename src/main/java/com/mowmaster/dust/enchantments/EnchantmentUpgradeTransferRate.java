package com.mowmaster.dust.enchantments;

import com.mowmaster.dust.blocks.machines.BlockPedestal;
import com.mowmaster.dust.items.itemPedestalUpgrades.ipuBasic;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;


public class EnchantmentUpgradeTransferRate extends Enchantment
{
    public EnchantmentUpgradeTransferRate() {
        super(Rarity.COMMON, EnumEnchantmentType.ALL, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
        this.setName("upgradetransferrate");
        this.setRegistryName("upgradetransferrate");
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + 10 * (enchantmentLevel - 1);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 25;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return super.isAllowedOnBooks();
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof ipuBasic || Block.getBlockFromItem(stack.getItem()) instanceof BlockPedestal ? true : super.canApply(stack);
    }

    @Override
    public boolean canApplyTogether(Enchantment ench)
    {
        return super.canApplyTogether(ench)
                //&& ench == Enchantments.MENDING
                && ench == Enchantments.SILK_TOUCH || ench == Enchantments.MENDING || ench == Enchantments.FORTUNE || ench == EnchantmentRegistry.enchantmentRange;
                //&& ench == Enchantments.FORTUNE;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        if(stack.getItem() instanceof ipuBasic || Block.getBlockFromItem(stack.getItem()) instanceof BlockPedestal)
        {
            return super.canApplyAtEnchantingTable(stack);
        }
        else return false;
        //return stack.getItem() instanceof ipuBasic || Block.getBlockFromItem(stack.getItem()) instanceof BlockPedestal ? true : super.canApplyAtEnchantingTable(stack);
    }
}
