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

    public static final Item STONE = new ItemDust(new Properties().maxStackSize(64).group(dust.itemGroup)).setRegistryName(new ResourceLocation(MODID, "itemduststone"));
    public static final Item GOLD = new ItemDust(new Properties().maxStackSize(64).group(dust.itemGroup)).setRegistryName(new ResourceLocation(MODID, "itemdustgold"));
    public static final Item IRON = new ItemDust(new Properties().maxStackSize(64).group(dust.itemGroup)).setRegistryName(new ResourceLocation(MODID, "itemdustiron"));
    public static final Item FLOUR = new ItemDust(new Properties().maxStackSize(64).group(dust.itemGroup)).setRegistryName(new ResourceLocation(MODID, "itemdustflour"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(STONE);
        event.getRegistry().register(GOLD);
        event.getRegistry().register(IRON);
        event.getRegistry().register(FLOUR);
    }




}
