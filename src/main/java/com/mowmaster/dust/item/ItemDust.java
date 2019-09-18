package com.mowmaster.dust.item;

import com.mowmaster.dust.references.Reference;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemDust extends Item {
    public ItemDust(Properties builder) {
        super(builder);
    }

    private static final ResourceLocation RES_DUST_RED = new ResourceLocation(Reference.MODID, "dustred");

}
