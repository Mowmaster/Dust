package com.mowmaster.dust.Items.Upgrades.Pedestal;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import static com.mowmaster.dust.References.Constants.MODID;

public class ItemUpgradeModelProperties
{
    public static void importUpgradeModes(Item item)
    {
        ItemProperties.register(item, new ResourceLocation(MODID + ":upgrade_import_mode"),(p_174625_, p_174626_, p_174627_, p_174628_) -> {
        return ItemUpgradeImport.getUpgradeMode(p_174625_);});
    }
}
