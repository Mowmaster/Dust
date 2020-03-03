package com.mowmaster.dust.item;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.references.Reference;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemDust extends Item {
    public ItemDust(Properties builder) {
        super(builder.group(dust.itemGroup));
    }

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(new ItemDust(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(new ResourceLocation(MODID, "itemduststone")));
        event.getRegistry().register(new ItemDust(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(new ResourceLocation(MODID, "itemdustgold")));
        event.getRegistry().register(new ItemDust(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(new ResourceLocation(MODID, "itemdustiron")));
        event.getRegistry().register(new ItemDust(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(new ResourceLocation(MODID, "itemdustflour")));
    }




}
