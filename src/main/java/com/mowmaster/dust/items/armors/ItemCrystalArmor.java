package com.mowmaster.dust.items.armors;

import com.mowmaster.dust.references.Reference;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ItemCrystalArmor extends ItemArmor
{
    public ItemCrystalArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot entityEquipmentSlot, String unlocName, String registryName)
    {
        super(material,renderIndex,entityEquipmentSlot);
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setCreativeTab(DUSTTABS);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}
