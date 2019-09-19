package com.mowmaster.dust.item;

import com.mowmaster.dust.references.Reference;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemDust extends Item {
    public ItemDust(Properties builder) {
        super(builder);
    }

    private static final ResourceLocation RESLOC_DUST_RED = new ResourceLocation(Reference.MODID, "itemdustred");
    private static final ResourceLocation RESLOC_DUST_BLUE = new ResourceLocation(Reference.MODID, "itemdustblue");
    private static final ResourceLocation RESLOC_DUST_GREEN = new ResourceLocation(Reference.MODID, "itemdustgreen");
    private static final ResourceLocation RESLOC_DUST_WHITE = new ResourceLocation(Reference.MODID, "itemdustwhite");
    private static final ResourceLocation RESLOC_DUST_BLACK = new ResourceLocation(Reference.MODID, "itemdustblack");


    private static final ResourceLocation RESLOC_DUST_STONE = new ResourceLocation(Reference.MODID, "itemduststone");
    private static final ResourceLocation RESLOC_DUST_GOLD = new ResourceLocation(Reference.MODID, "itemdustgold");
    private static final ResourceLocation RESLOC_DUST_IRON = new ResourceLocation(Reference.MODID, "itemdustiron");
    private static final ResourceLocation RESLOC_DUST_FLOUR= new ResourceLocation(Reference.MODID, "itemdustflour");


    public static final Item DUST_RED = new Item(new Item.Properties()).setRegistryName(RESLOC_DUST_RED);
    public static final Item DUST_BLUE = new Item(new Item.Properties()).setRegistryName(RESLOC_DUST_BLUE);
    public static final Item DUST_GREEN = new Item(new Item.Properties()).setRegistryName(RESLOC_DUST_GREEN);
    public static final Item DUST_WHITE = new Item(new Item.Properties()).setRegistryName(RESLOC_DUST_WHITE);
    public static final Item DUST_BLACK= new Item(new Item.Properties()).setRegistryName(RESLOC_DUST_BLACK);

    public static final Item DUST_STONE = new Item(new Item.Properties()).setRegistryName(RESLOC_DUST_STONE);
    public static final Item DUST_GOLD = new Item(new Item.Properties()).setRegistryName(RESLOC_DUST_GOLD);
    public static final Item DUST_IRON = new Item(new Item.Properties()).setRegistryName(RESLOC_DUST_IRON);
    public static final Item DUST_FLOUR = new Item(new Item.Properties()).setRegistryName(RESLOC_DUST_FLOUR);




    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(DUST_RED);
        event.getRegistry().register(DUST_BLUE);
        event.getRegistry().register(DUST_GREEN);
        event.getRegistry().register(DUST_WHITE);
        event.getRegistry().register(DUST_BLACK);

        event.getRegistry().register(DUST_STONE);
        event.getRegistry().register(DUST_GOLD);
        event.getRegistry().register(DUST_IRON);
        event.getRegistry().register(DUST_FLOUR);
    }




}
