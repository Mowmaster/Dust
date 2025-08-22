package com.mowmaster.dust.creativemode;

import com.mowmaster.dust.Dust;
import com.mowmaster.dust.block.ModBlocks;
import com.mowmaster.dust.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DustCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Dust.MODID);

    public static final Supplier<CreativeModeTab> BISMUTH_ITEMS_TAB = CREATIVE_MODE_TAB.register("dusttab_items",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.DUST_RED.get()))
                    .title(Component.translatable("creativetab.dust.dust_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.DUST_RED);
                        output.accept(ModItems.WAND_BASIC_RED);
                    }).build());

    public static final Supplier<CreativeModeTab> BISMUTH_BLOCK_TAB = CREATIVE_MODE_TAB.register("dusttab_blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.DUSTBLOCK_RED))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(Dust.MODID, "dusttab_items"))
                    .title(Component.translatable("creativetab.dust.dust_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.DUSTBLOCK_RED);

                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }

}
