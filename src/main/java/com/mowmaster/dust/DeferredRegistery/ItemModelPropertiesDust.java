package com.mowmaster.dust.DeferredRegistery;

import com.mowmaster.dust.Items.Augments.AugmentRenderDiffuser;
import com.mowmaster.dust.Items.Filters.BaseFilter;
import com.mowmaster.dust.Items.Filters.FilterItem;
import com.mowmaster.dust.Items.Upgrades.Pedestal.ItemUpgradeImport;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import static com.mowmaster.dust.References.Constants.MODID;

public class ItemModelPropertiesDust
{
    public static void dustItemModes(Item item)
    {
        ItemProperties.register(item, new ResourceLocation(MODID + ":upgrade_import_mode"),(p_174625_, p_174626_, p_174627_, p_174628_) -> {
        return ItemUpgradeImport.getUpgradeMode(p_174625_);});

        ItemProperties.register(item, new ResourceLocation(MODID + ":filter_mode"),(p_174625_, p_174626_, p_174627_, p_174628_) -> {
            return BaseFilter.getFilterMode(p_174625_);});
        ItemProperties.register(item, new ResourceLocation(MODID + ":filter_mode"),(p_174625_, p_174626_, p_174627_, p_174628_) -> {
            return FilterItem.getFilterMode(p_174625_);});

        ItemProperties.register(item, new ResourceLocation(MODID + ":augment_mode"),(p_174625_, p_174626_, p_174627_, p_174628_) -> {
            return AugmentRenderDiffuser.getAugmentMode(p_174625_);});
    }
}
