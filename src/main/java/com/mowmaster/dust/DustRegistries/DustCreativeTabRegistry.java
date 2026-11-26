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

                        output.accept(DustItemRegistry.ANCIENT_INK.asItem());
                        output.accept(DustItemRegistry.PARCHMENT.asItem());
                        output.accept(DustItemRegistry.ANCIENT_TABLET.asItem());
                        output.accept(DustItemRegistry.RESEARCH_NOTES.asItem());
                        output.accept(DustItemRegistry.MAGICAL_FLINT_AND_STEEL.asItem());
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
                        output.accept(DustBlockRegistry.STILL_FIREBOX.asItem());
                        output.accept(DustBlockRegistry.STILL_POT.asItem());
                        output.accept(DustBlockRegistry.STILL_TOWER.asItem());
                        output.accept(DustBlockRegistry.STILL_TOP.asItem());
                        output.accept(DustBlockRegistry.DUST_CHUTE.asItem());
                        output.accept(DustBlockRegistry.DUST_COLLECTOR.asItem());
                        output.accept(DustBlockRegistry.DUST_SEPARATOR.asItem());
                        output.accept(DustBlockRegistry.DUST_SEPARATOR_EXTENSION.asItem());
                        output.accept(DustBlockRegistry.DUST_MIXER.asItem());
                        output.accept(DustBlockRegistry.DUST_MIXER_WET.asItem());
                        output.accept(DustBlockRegistry.DUST_CYCLONE.asItem());
                        output.accept(DustBlockRegistry.DUST_CYCLONE_HOPPER.asItem());
                        output.accept(DustBlockRegistry.DUST_CENTRIFUGE.asItem());
                        output.accept(DustBlockRegistry.TRANSMUTER.asItem());
                        output.accept(DustBlockRegistry.CONJOINING_CRUCIBLE.asItem());
                        output.accept(DustBlockRegistry.MIXING_VAT.asItem());
                        output.accept(DustBlockRegistry.DISSOLVING_RETORT.asItem());
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
                        output.accept(DustItemRegistry.ALCHEMICAL_SOOT.asItem());
                    }).build());

    // Spells: the Akashic, grimoires and scrolls, a Fused Crystal of each fusion, and every modifier card
    public static final Supplier<CreativeModeTab> DUSTTAB_SPELLS = CREATIVE_MODE_TABS.register("dusttab_spells",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(DustItemRegistry.AKASHIC.get()))
                    .title(Component.translatable("creativetab.dust.dusttab_spells"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(DustItemRegistry.AKASHIC.asItem());
                        output.accept(DustItemRegistry.GRIMOIRE.asItem());
                        output.accept(DustItemRegistry.SPELL_SCROLL.get());
                        for (var modifier : com.mowmaster.dust.Features.Spells.SpellRegistries.MODIFIER_REGISTRY) {
                            if (modifier instanceof com.mowmaster.dust.Features.Spells.modifiers.FusionModifier) {
                                var id = com.mowmaster.dust.Features.Spells.SpellRegistries.MODIFIER_REGISTRY.getKey(modifier);
                                if (id != null) output.accept(com.mowmaster.dust.Features.Spells.FusedCrystalItem.of(id));
                            }
                        }
                        DustItemRegistry.CARDS.values().forEach(card -> output.accept(card.get()));
                    }).build());

    // Relics: the elemental armor sets (Features/Armor)
    public static final Supplier<CreativeModeTab> DUSTTAB_RELICS = CREATIVE_MODE_TABS.register("dusttab_relics",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(com.mowmaster.dust.Features.Armor.ElementalArmor.item(com.mowmaster.dust.Features.Armor.ElementalArmorSet.PYREBORN,
                            net.minecraft.world.item.equipment.ArmorType.HELMET)))
                    .title(Component.translatable("creativetab.dust.dusttab_relics"))
                    .displayItems((itemDisplayParameters, output) -> {
                        for (var set : com.mowmaster.dust.Features.Armor.ElementalArmorSet.values()) {
                            com.mowmaster.dust.Features.Armor.ElementalArmor.ITEMS.get(set).values().forEach(piece -> output.accept(piece.get()));
                        }
                        // The Archons (Features/Bosses): their trinkets, loot bags and spawn eggs
                        com.mowmaster.dust.Features.Bosses.Archons.LEGENDARY.forEach(trinket -> output.accept(trinket.get()));
                        com.mowmaster.dust.Features.Bosses.Archons.SIGILS.values().forEach(sigil -> output.accept(sigil.get()));
                        com.mowmaster.dust.Features.Bosses.Archons.BAGS.values().forEach(bag -> output.accept(bag.get()));
                        com.mowmaster.dust.Features.Bosses.Archons.EGGS.values().forEach(egg -> output.accept(egg.get()));
                        // The sanctums' Arcane Spawner (Features/Sanctums)
                        output.accept(com.mowmaster.dust.Features.Sanctums.Sanctums.ARCANE_SPAWNER_ITEM.get());
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

    // Dusts and crystals: every element's dust and alchemical crystal (and the inert crystal), in element order; then the
    // Crystal Display and every element's crystal blocks (small, medium and large crystals, crystal block)
    public static final Supplier<CreativeModeTab> DUSTTAB_CRYSTALS = CREATIVE_MODE_TABS.register("dusttab_crystals",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(com.mowmaster.dust.Features.Crystals.ElementCrystals.LARGE_ITEMS.get(com.mowmaster.dust.Features.DustComposition.DustElements.MAGIC).get()))
                    .title(Component.translatable("creativetab.dust.dusttab_crystals"))
                    .displayItems((itemDisplayParameters, output) -> {
                        DustItemRegistry.DUSTS.values().forEach(item -> output.accept(item.get()));
                        output.accept(DustItemRegistry.CRYSTAL_INERT.asItem());
                        DustItemRegistry.CRYSTALS.values().forEach(item -> output.accept(item.get()));
                        output.accept(DustBlockRegistry.CRYSTAL_DISPLAY.asItem());
                        for (var element : com.mowmaster.dust.Features.DustComposition.DustElements.all()) {
                            output.accept(com.mowmaster.dust.Features.Crystals.ElementCrystals.SMALL_ITEMS.get(element).get());
                            output.accept(com.mowmaster.dust.Features.Crystals.ElementCrystals.MEDIUM_ITEMS.get(element).get());
                            output.accept(com.mowmaster.dust.Features.Crystals.ElementCrystals.LARGE_ITEMS.get(element).get());
                            output.accept(com.mowmaster.dust.Features.Crystals.ElementCrystals.BLOCK_ITEMS.get(element).get());
                        }
                    }).build());

    // The elemental flowers and woods (Features/Flora, Features/Trees): flowers, then each wood's sapling, leaves, logs,
    // planks family, chest and fruit, then the charcoals
    public static final Supplier<CreativeModeTab> DUSTTAB_NATURE = CREATIVE_MODE_TABS.register("dusttab_nature",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(com.mowmaster.dust.Features.Trees.ElementalWoods.set(com.mowmaster.dust.Features.Trees.ElementalWood.EMBERWOOD).sapling().get()))
                    .title(Component.translatable("creativetab.dust.dusttab_nature"))
                    .displayItems((itemDisplayParameters, output) -> {
                        for (var flower : com.mowmaster.dust.Features.Flora.ElementalFlower.values()) {
                            output.accept(com.mowmaster.dust.Features.Flora.ElementalFlowers.FLOWERS.get(flower).get().asItem());
                        }
                        for (var set : com.mowmaster.dust.Features.Trees.ElementalWoods.all()) {
                            output.accept(set.sapling().get().asItem());
                            output.accept(set.leaves().get().asItem());
                            for (var block : set.itemBlocks()) {
                                if (block != set.sapling() && block != set.leaves()) output.accept(block.get().asItem());
                            }
                            output.accept(set.fruit().get());
                        }
                        // Fruit Medleys: one kind (one affinity for 30 minutes), two kinds, three kinds
                        for (var fruits : java.util.List.of(
                                java.util.Map.of(com.mowmaster.dust.Features.Trees.ElementalWood.EMBERWOOD, 6),
                                java.util.Map.of(com.mowmaster.dust.Features.Trees.ElementalWood.TIDEWOOD, 4, com.mowmaster.dust.Features.Trees.ElementalWood.LOAMWOOD, 2),
                                java.util.Map.of(com.mowmaster.dust.Features.Trees.ElementalWood.EMBERWOOD, 2, com.mowmaster.dust.Features.Trees.ElementalWood.TIDEWOOD, 2,
                                        com.mowmaster.dust.Features.Trees.ElementalWood.LOAMWOOD, 2))) {
                            ItemStack medley = new ItemStack(com.mowmaster.dust.Features.Trees.ElementalWoods.MEDLEY.get());
                            medley.set(DustComponentDataRegistry.FRUIT_MEDLEY.get(), new com.mowmaster.dust.Features.Trees.FruitMedley(fruits));
                            output.accept(medley);
                        }
                        com.mowmaster.dust.Features.Trees.ElementalWoods.CHARCOAL.values().forEach(charcoal -> output.accept(charcoal.get()));
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
