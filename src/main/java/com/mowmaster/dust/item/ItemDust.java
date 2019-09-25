package com.mowmaster.dust.item;

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
        super(builder);
    }

    private static final ResourceLocation RESLOC_DUST_STONE = new ResourceLocation(MODID, "itemduststone");
    private static final ResourceLocation RESLOC_DUST_GOLD = new ResourceLocation(MODID, "itemdustgold");
    private static final ResourceLocation RESLOC_DUST_IRON = new ResourceLocation(MODID, "itemdustiron");
    private static final ResourceLocation RESLOC_DUST_FLOUR= new ResourceLocation(MODID, "itemdustflour");

    public static final Item DUST_STONE = new ItemDust(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(RESLOC_DUST_STONE);
    public static final Item DUST_GOLD = new ItemDust(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(RESLOC_DUST_GOLD);
    public static final Item DUST_IRON = new ItemDust(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(RESLOC_DUST_IRON);
    public static final Item DUST_FLOUR = new ItemDust(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(RESLOC_DUST_FLOUR);




    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(DUST_STONE);
        event.getRegistry().register(DUST_GOLD);
        event.getRegistry().register(DUST_IRON);
        event.getRegistry().register(DUST_FLOUR);
    }




}
