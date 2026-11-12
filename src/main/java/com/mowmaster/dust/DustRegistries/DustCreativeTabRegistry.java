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

                        output.accept(DustItemRegistry.CRYSTAL_INERT.asItem());

                        output.accept(DustItemRegistry.ANCIENT_INK.asItem());
                        output.accept(DustItemRegistry.PARCHMENT.asItem());
                        output.accept(DustItemRegistry.ANCIENT_TABLET.asItem());
                        output.accept(DustItemRegistry.RESEARCH_NOTES.asItem());
                        output.accept(DustItemRegistry.MAGICAL_FLINT_AND_STEEL.asItem());
                        // A crystal and a dust for every element, in element order
                        DustItemRegistry.CRYSTALS.values().forEach(item -> output.accept(item.get()));
                        DustItemRegistry.DUSTS.values().forEach(item -> output.accept(item.get()));
                        output.accept(DustBlockRegistry.ELEMENT_TESTER.asItem());
                        output.accept(DustBlockRegistry.RESEARCH_CRUSHER.asItem());
                        output.accept(DustBlockRegistry.RESEARCH_SEPARATION.asItem());
                        output.accept(DustBlockRegistry.RESEARCH_EVAPORATION.asItem());
                        output.accept(DustBlockRegistry.RESEARCH_DISTILLATION.asItem());
                        output.accept(DustBlockRegistry.RESEARCH_TRANSFORMATION.asItem());
                        output.accept(DustBlockRegistry.RESEARCH_COMBINATION.asItem());
                        output.accept(DustBlockRegistry.RESEARCH_MIXING.asItem());
                        output.accept(DustItemRegistry.ALCHEMICAL_SOOT.asItem());
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

                        output.accept(DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL.asItem());
                        output.accept(DustBlockRegistry.GLYPH_STONE.asItem());
                    }).build());
}
