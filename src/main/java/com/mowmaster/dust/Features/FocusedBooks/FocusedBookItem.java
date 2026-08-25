package com.mowmaster.dust.Features.FocusedBooks;


import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.List;

public class FocusedBookItem extends EnchantableBookItem {
    public FocusedBookItem(Properties properties) {
        super(properties);
    }


    public static int getBookColor(ItemStack stack)
    {
        if(DustComponentDataRegistry.FOCUSEDBOOK_BOOK_COLOR.get() != null)
        {
            return stack.get(DustComponentDataRegistry.FOCUSEDBOOK_BOOK_COLOR.get());
        }

        return 0;
    }

    /*@Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return super.supportsEnchantment(stack, enchantment);
    }

    @Override
    public int getEnchantmentLevel(ItemInstance stack, Holder<Enchantment> enchantment) {
        return super.getEnchantmentLevel(stack, enchantment);
    }

    @Override
    public ItemStack applyEnchantments(ItemStack stack, List<EnchantmentInstance> enchantments) {
        return super.applyEnchantments(stack, enchantments);
    }

    @Override
    public ItemEnchantments getAllEnchantments(ItemStack stack, HolderLookup.RegistryLookup<Enchantment> lookup) {
        return super.getAllEnchantments(stack, lookup);
    }*/


}
