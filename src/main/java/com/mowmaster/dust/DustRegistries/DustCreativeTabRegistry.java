package com.mowmaster.dust.DustRegistries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.mowmaster.dust.DustReferences.MODID;

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

                        output.accept(DustItemRegistry.CRYSTAL_INERT.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_RED.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_GREEN.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_BLUE.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_WHITE.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_BLACK.asItem());
                    }).build());

    public static final Supplier<CreativeModeTab> DUSTTAB_DECORATION = CREATIVE_MODE_TABS.register("dusttab_decor",
            () -> CreativeModeTab.builder()
                    .icon(()-> new ItemStack(DustItemRegistry.CRYSTAL_CHISEL_IRON.get()))
                    .title(Component.translatable("creativetab.dust.dusttab_decor"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(DustItemRegistry.CRYSTAL_CHISEL_IRON.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_HALFSAW_IRON.asItem());
                        output.accept(DustBlockRegistry.CRYSTAL_PATH_TIER1.asItem());
                        output.accept(DustBlockRegistry.CRYSTAL_PATH_TIER2.asItem());
                        output.accept(DustBlockRegistry.CRYSTAL_PATH_TIER3.asItem());
                        output.accept(DustBlockRegistry.CRYSTAL_PATH_TIER4.asItem());
                        output.accept(DustItemRegistry.CHARCOAL_WHITE.asItem());
                        output.accept(DustBlockRegistry.BLOCK_PLANKS_GREEN.asItem());
                        output.accept(DustBlockRegistry.BLOCK_STAIRS_GREEN.asItem());
                        output.accept(DustBlockRegistry.BLOCK_SLAB_GREEN.asItem());
                        output.accept(DustBlockRegistry.BLOCK_BUTTON_GREEN.asItem());
                        output.accept(DustBlockRegistry.BLOCK_PRESSUREPLATE_GREEN.asItem());
                        output.accept(DustBlockRegistry.BLOCK_FENCE_GREEN.asItem());
                        output.accept(DustBlockRegistry.BLOCK_FENCEGATE_GREEN.asItem());
                        output.accept(DustBlockRegistry.BLOCK_WALL_GREEN.asItem());
                        output.accept(DustBlockRegistry.BLOCK_DOOR_GREEN.asItem());
                        output.accept(DustBlockRegistry.BLOCK_TRAPDOOR_GREEN.asItem());
                        output.accept(DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL.asItem());
                        output.accept(DustBlockRegistry.CRYSTAL_STONE_RED.asItem());
                        output.accept(DustBlockRegistry.CRYSTAL_STONE_GREEN.asItem());
                        output.accept(DustBlockRegistry.CRYSTAL_STONE_BLUE.asItem());
                        output.accept(DustBlockRegistry.CRYSTAL_STONE_WHITE.asItem());
                        output.accept(DustBlockRegistry.CRYSTAL_STONE_BLACK.asItem());
                    }).build());

    public static final Supplier<CreativeModeTab> DUSTTAB_FOOD = CREATIVE_MODE_TABS.register("dusttab_food",
            () -> CreativeModeTab.builder()
                    .icon(()-> new ItemStack(DustItemRegistry.CORNBREAD.get()))
                    .title(Component.translatable("creativetab.dust.dusttab_food"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(DustItemRegistry.CORNBREAD.asItem());

                        output.accept(DustItemRegistry.FOOD_LETTUCE.asItem());
                        output.accept(DustItemRegistry.SEEDS_LETTUCE.asItem());
                        output.accept(DustItemRegistry.FOOD_FALLOLDBERRY.asItem());
                    }).build());

    public static final Supplier<CreativeModeTab> DUSTTAB_COMBAT = CREATIVE_MODE_TABS.register("dusttab_combat",
            () -> CreativeModeTab.builder()
                    .icon(()-> new ItemStack(DustItemRegistry.CRYSTAL_SWORD.get()))
                    .title(Component.translatable("creativetab.dust.dusttab_combat"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(DustItemRegistry.CRYSTAL_SWORD.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_PICKAXE.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_AXE.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_SHOVEL.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_HOE.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_SPEAR.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_MATTOCK.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_HAMMER.asItem());

                        output.accept(DustItemRegistry.CRYSTAL_HELMET.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_CHESTPLATE.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_LEGGINGS.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_BOOTS.asItem());
                        output.accept(DustItemRegistry.CRYSTAL_HORSE_ARMOR.asItem());

                        output.accept(DustItemRegistry.SPELL_WAND_FIRE.asItem());
                        output.accept(DustItemRegistry.SPELL_WAND_EARTH.asItem());

                        output.accept(DustItemRegistry.FOCUSED_BOOK_BASE.asItem());


                    }).build());

    public static final Supplier<CreativeModeTab> DUSTTAB_BOOKS = CREATIVE_MODE_TABS.register("dusttab_books",
            () -> CreativeModeTab.builder()
                    .icon(()-> new ItemStack(DustItemRegistry.FOCUSED_BOOK_BASE.get()))
                    .title(Component.translatable("creativetab.dust.dusttab_books"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(DustItemRegistry.FOCUSED_BOOK_BASE.asItem());
                        output.accept(DustItemRegistry.FOCUSED_BOOK.asItem());


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
