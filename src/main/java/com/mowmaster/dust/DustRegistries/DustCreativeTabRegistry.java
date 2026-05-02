package com.mowmaster.dust.DustRegistries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.mowmaster.dust.DustUtils.DustReferences.MODID;

public class DustCreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final Supplier<CreativeModeTab> DUSTTAB_ITEMS = CREATIVE_MODE_TABS.register("dusttab_items",
            () -> CreativeModeTab.builder()
                    .icon(()-> new ItemStack(DustItemRegistry.DUST_RED.get()))
                    .title(Component.translatable("creativetab.dust.dusttab_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(DustItemRegistry.DUST_RED.asItem());
                        output.accept(DustItemRegistry.DUST_GREEN.asItem());
                        output.accept(DustItemRegistry.DUST_BLUE.asItem());
                        output.accept(DustItemRegistry.DUST_WHITE.asItem());
                        output.accept(DustItemRegistry.DUST_BLACK.asItem());

                        output.accept(DustItemRegistry.CRYSTAL_RED.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_GREEN.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_BLUE.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_WHITE.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_BLACK.asItem());
                    }).build());

    public static final Supplier<CreativeModeTab> DUSTTAB_BLOCKS = CREATIVE_MODE_TABS.register("dusttab_blocks",
            () -> CreativeModeTab.builder()
                    .icon(()-> new ItemStack(DustBlockRegistry.BLOCK_OF_DUST_RED))
                    .title(Component.translatable("creativetab.dust.dusttab_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(DustBlockRegistry.BLOCK_OF_DUST_RED.asItem());
                        output.accept(DustBlockRegistry.BLOCK_OF_DUST_GREEN.asItem());
                        output.accept(DustBlockRegistry.BLOCK_OF_DUST_BLUE.asItem());
                        output.accept(DustBlockRegistry.BLOCK_OF_DUST_WHITE.asItem());
                        output.accept(DustBlockRegistry.BLOCK_OF_DUST_BLACK.asItem());

                        output.accept(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.asItem());
                        output.accept(DustBlockRegistry.BLOCK_OF_CRYSTAL_GREEN.asItem());
                        output.accept(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLUE.asItem());
                        output.accept(DustBlockRegistry.BLOCK_OF_CRYSTAL_WHITE.asItem());
                        output.accept(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLACK.asItem());

                        output.accept(DustBlockRegistry.INERT_CRYSTAL_ORE.asItem());
                        output.accept(DustBlockRegistry.INERT_CRYSTAL_ORE_DEEPSLATE.asItem());

                    }).build());
}
