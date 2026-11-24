package com.mowmaster.dust.Features.Crystals;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

// Whether a tool has Silk Touch (crystals and elemental flowers keep their form with it, and give orbs without)
public final class CrystalDrops
{
    private CrystalDrops() {}

    public static boolean silkTouch(ServerLevel level, ItemStack tool)
    {
        if (tool.isEmpty()) return false;
        var silk = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.SILK_TOUCH);
        return silk.isPresent() && EnchantmentHelper.getItemEnchantmentLevel(silk.get(), tool) > 0;
    }
}
