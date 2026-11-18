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
                        output.accept(DustItemRegistry.GRIMOIRE.asItem());
                        output.accept(DustItemRegistry.AKASHIC.asItem());
                        // A Fused Crystal of each fusion
                        for (var modifier : com.mowmaster.dust.Features.Spells.SpellRegistries.MODIFIER_REGISTRY) {
                            if (modifier instanceof com.mowmaster.dust.Features.Spells.modifiers.FusionModifier) {
                                var id = com.mowmaster.dust.Features.Spells.SpellRegistries.MODIFIER_REGISTRY.getKey(modifier);
                                if (id != null) output.accept(com.mowmaster.dust.Features.Spells.FusedCrystalItem.of(id));
                            }
                        }
                        DustItemRegistry.CARDS.values().forEach(card -> output.accept(card.get()));
                        // A crystal and a dust for every element, in element order
                        DustItemRegistry.CRYSTALS.values().forEach(item -> output.accept(item.get()));
                        DustItemRegistry.DUSTS.values().forEach(item -> output.accept(item.get()));
                        output.accept(DustBlockRegistry.ELEMENT_TESTER.asItem());
                        output.accept(DustBlockRegistry.ELEMENT_CRAFTER.asItem());
                        output.accept(DustBlockRegistry.ELEMENT_REPLICATOR.asItem());
                        output.accept(DustBlockRegistry.DUST_JAR.asItem());
                        output.accept(DustItemRegistry.JAR_LABEL.asItem());
                        output.accept(DustBlockRegistry.DUST_GRINDER.asItem());
                        output.accept(DustBlockRegistry.DUST_TUBE.asItem());
                        output.accept(DustBlockRegistry.CRUSHING_MILL.asItem());
                        output.accept(DustBlockRegistry.ALCHEMICAL_SEPARATOR.asItem());
                        output.accept(DustBlockRegistry.EVAPORATING_KETTLE.asItem());
                        output.accept(DustBlockRegistry.ALEMBIC.asItem());
                        output.accept(DustBlockRegistry.TRANSMUTER.asItem());
                        output.accept(DustBlockRegistry.CONJOINING_CRUCIBLE.asItem());
                        output.accept(DustBlockRegistry.MIXING_VAT.asItem());
                        output.accept(DustBlockRegistry.GROWTH_LAMP.asItem());
                        output.accept(DustBlockRegistry.LOVELY_LAMP.asItem());
                        output.accept(DustBlockRegistry.SPELL_CASTER.asItem());
                        output.accept(DustBlockRegistry.RESEARCH_CRUSHER.asItem());
                        output.accept(DustBlockRegistry.RESEARCH_SEPARATION.asItem());
                        output.accept(DustBlockRegistry.RESEARCH_EVAPORATION.asItem());
                        output.accept(DustBlockRegistry.RESEARCH_DISTILLATION.asItem());
                        output.accept(DustBlockRegistry.RESEARCH_TRANSFORMATION.asItem());
                        output.accept(DustBlockRegistry.RESEARCH_COMBINATION.asItem());
                        output.accept(DustBlockRegistry.RESEARCH_MIXING.asItem());
                        output.accept(DustBlockRegistry.SPELL_PYLON.asItem());
                        output.accept(DustBlockRegistry.SHAPE_PYLON.asItem());
                        output.accept(DustBlockRegistry.NEUTRAL_PYLON.asItem());
                        output.accept(DustBlockRegistry.PYLON_BLESSED.asItem());
                        output.accept(DustBlockRegistry.PYLON_CORRUPTED.asItem());
                        output.accept(DustBlockRegistry.THRIFTY_PYLON.asItem());
                        output.accept(DustBlockRegistry.GIANT_PYLON.asItem());
                        output.accept(DustBlockRegistry.DAMAGE_PYLON.asItem());
                        output.accept(DustBlockRegistry.EXPERIENCED_PYLON.asItem());
                        output.accept(DustItemRegistry.SPELL_SCROLL.get());
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
                        output.accept(DustItemRegistry.SPELL_WAND_ICE.asItem());
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
                        DustBlockRegistry.PRIMAL_ORES.values().forEach(ore -> output.accept(ore.asItem()));
                        DustBlockRegistry.PRIMAL_ORES_DEEPSLATE.values().forEach(ore -> output.accept(ore.asItem()));

                        output.accept(DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL.asItem());
                        output.accept(DustBlockRegistry.GLYPH_STONE.asItem());
                    }).build());
}
