package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.GlyphStoneBlock;
import com.mowmaster.dust.Features.DustComposition.DustFireBlock;
import com.mowmaster.dust.Features.DustComposition.ElementTesterBlock;
import com.mowmaster.dust.Features.Machines.ResearchCrusherBlock;
import com.mowmaster.dust.Features.Machines.ResearchCombinationBlock;
import com.mowmaster.dust.Features.Spells.pylons.PylonBlock;
import com.mowmaster.dust.Features.Spells.blocks.BlightBlock;
import com.mowmaster.dust.Features.Spells.blocks.CorruptedBlock;
import com.mowmaster.dust.Features.Spells.blocks.PoisonPoolBlock;
import com.mowmaster.dust.Features.Spells.blocks.PhantomBlock;
import com.mowmaster.dust.Features.Spells.blocks.SpellLightBlock;
import com.mowmaster.dust.Features.Spells.blocks.WardedGlassBlock;
import com.mowmaster.dust.Features.Spells.pylons.PylonType;
import com.mowmaster.dust.Features.Spells.pylons.SpellPylonBlock;
import com.mowmaster.dust.Features.Machines.ResearchDistillationBlock;
import com.mowmaster.dust.Features.Machines.ResearchMixingBlock;
import com.mowmaster.dust.Features.Machines.ResearchTransformationBlock;
import com.mowmaster.dust.Features.Machines.ResearchEvaporationBlock;
import com.mowmaster.dust.Features.Machines.ResearchSeparationBlock;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
//? if >=26.3
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class DustBlockRegistry {
    public static final DeferredRegister.Blocks DUSTBLOCKS = DeferredRegister.createBlocks(DustReferences.MODID);

    /*
    If a block doesnt need a loot table this way the datagen wont fail
    public static final DeferredBlock<Block> BLOCK_OF_DUST_RED = registerBlock("blockofdust_red", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops().noLootTable()));
     */
    public static final DeferredBlock<Block> BLOCK_OF_DUST_RED = registerBlock("blockofdust_red", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_DUST_GREEN = registerBlock("blockofdust_green", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_DUST_BLUE = registerBlock("blockofdust_blue", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_DUST_WHITE = registerBlock("blockofdust_white", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_DUST_BLACK = registerBlock("blockofdust_black", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops()));


    public static final DeferredBlock<Block> BLOCK_OF_CRYSTAL_RED = registerBlock("blockofcrystal_red", properties -> new Block(
            properties.sound(SoundType.AMETHYST).strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_CRYSTAL_GREEN = registerBlock("blockofcrystal_green", properties -> new Block(
            properties.sound(SoundType.AMETHYST).strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_CRYSTAL_BLUE = registerBlock("blockofcrystal_blue", properties -> new Block(
            properties.sound(SoundType.AMETHYST).strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_CRYSTAL_WHITE = registerBlock("blockofcrystal_white", properties -> new Block(
            properties.sound(SoundType.AMETHYST).strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_CRYSTAL_BLACK = registerBlock("blockofcrystal_black", properties -> new Block(
            properties.sound(SoundType.AMETHYST).strength(4f).requiresCorrectToolForDrops()));


    // Inert Crystal Ore: the crystal ore that generates; it slowly turns into a primal ore by its chunk's aura
    // (Features/Magic/InertCrystalOreBlock)
    public static final DeferredBlock<Block> INERT_CRYSTAL_ORE = registerBlock("block_inert_crystal_ore", properties -> new com.mowmaster.dust.Features.Magic.InertCrystalOreBlock(
            false, UniformInt.of(2,4), properties.sound(SoundType.STONE).strength(4f).requiresCorrectToolForDrops().randomTicks()));
    public static final DeferredBlock<Block> INERT_CRYSTAL_ORE_DEEPSLATE = registerBlock("block_inert_crystal_ore_deepslate", properties -> new com.mowmaster.dust.Features.Magic.InertCrystalOreBlock(
            true, UniformInt.of(2,4), properties.sound(SoundType.DEEPSLATE).strength(5f).requiresCorrectToolForDrops().randomTicks()));
    // The primal crystal ores it becomes (block_fire_crystal_ore, block_fire_crystal_ore_deepslate...), dropping that
    // primal's crystal. EnumMap: a map keyed by an enum, in the enum's order.
    public static final java.util.Map<com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement, DeferredBlock<Block>> PRIMAL_ORES = primalOres(false);
    public static final java.util.Map<com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement, DeferredBlock<Block>> PRIMAL_ORES_DEEPSLATE = primalOres(true);

    private static java.util.Map<com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement, DeferredBlock<Block>> primalOres(boolean deepslate)
    {
        var ores = new java.util.EnumMap<com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement, DeferredBlock<Block>>(
                com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement.class);
        for (var primal : com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement.values()) {
            String name = "block_" + primal.getSerializedName() + "_crystal_ore" + (deepslate ? "_deepslate" : "");
            ores.put(primal, registerBlock(name, properties -> new DropExperienceBlock(UniformInt.of(2, 4), properties
                    .sound(deepslate ? SoundType.DEEPSLATE : SoundType.STONE).strength(deepslate ? 5f : 4f).requiresCorrectToolForDrops())));
        }
        return ores;
    }


    public static final DeferredBlock<Block> BLOCK_FLOWER_WHINDWHEEL = registerBlock("flower_windwheel",
            properties -> new FlowerBlock(
                    MobEffects.WIND_CHARGED, 10F, properties
                    .mapColor(MapColor.PLANT)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.POPPED)
            ),
            // 26.3: composting is an item component now (replaces the COMPOSTABLES data map). COMPOSTABLE_LOW is
            // vanilla's 30% chance to add a layer.
            // 26.1.2 still uses NeoForge's data map: src/main/resources-26.1.2/data/neoforge/data_maps/item/compostables.json
            // (only the 26.1.2 build includes that folder; see build.gradle).
            //? if >=26.3 {
            itemProperties -> itemProperties.compostable(ContextIntProviders.COMPOSTABLE_LOW));
    //?} else {
    /*itemProperties -> itemProperties);
     *///?}

    // NeoForge links a full pot to its empty pot by itself when the block is registered, as long as the empty pot is
    // given here (passing null throws). So there's no addPlant call in common setup any more.

    public static final DeferredBlock<Block> BLOCK_POTTEDFLOWER_WINDWHEEL = DUSTBLOCKS.registerBlock("pottedflower_windwheel",
            properties -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, BLOCK_FLOWER_WHINDWHEEL,
                    properties
                            .noOcclusion()
                            .instabreak()
                            .pushReaction(PushReaction.POPPED)
            ));


    // Ruin walls: a stone with one ancient glyph carved on its front (see Features/AncientLanguage)
    public static final DeferredBlock<Block> GLYPH_STONE = registerBlock("glyph_stone", properties -> new GlyphStoneBlock(
            properties.mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5F, 6.0F).requiresCorrectToolForDrops()));

    // Dust composition (see Features/DustComposition).
    // The fire a Magical Flint and Steel lights, in the colour of what it burned. No item, nothing drops; like soul fire
    // it can be walked through, is put out by a punch, and pops (goes out) when a piston pushes it.
    public static final DeferredBlock<Block> DUST_FIRE = DUSTBLOCKS.registerBlock("dust_fire", properties -> new DustFireBlock(
            properties.mapColor(MapColor.FIRE).replaceable().noCollision().instabreak().lightLevel(state -> 12)
                    .sound(SoundType.WOOL).pushReaction(PushReaction.POPPED).noLootTable()));
    // The Element Tester workstation; with crystals on it, the Elemental Examiner. noOcclusion: not a full block, so
    // the blocks around it still draw their faces behind it.
    public static final DeferredBlock<Block> ELEMENT_TESTER = registerBlock("element_tester", properties -> new ElementTesterBlock(
            properties.mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5F, 6.0F).noOcclusion()));
    // The Element Crafter (Features/Crafting): element crafting recipes, with dust from its four jars
    public static final DeferredBlock<Block> ELEMENT_CRAFTER = registerBlock("element_crafter", properties -> new com.mowmaster.dust.Features.Crafting.ElementCrafterBlock(
            properties.mapColor(MapColor.STONE).sound(SoundType.STONE).strength(2.0F, 6.0F).noOcclusion()));
    // The Element Replicator (Features/Machines): copies an item a player has fully worked out, with dust from the jars
    // round it
    public static final DeferredBlock<Block> ELEMENT_REPLICATOR = registerBlock("element_replicator", properties -> new com.mowmaster.dust.Features.Machines.ElementReplicatorBlock(
            properties.mapColor(MapColor.STONE).sound(SoundType.STONE).strength(2.0F, 6.0F).noOcclusion()));

    // Dust storage (Features/DustStorage): the Dust Jar (1000 dust of one element; its own item class, for MowLib's place
    // key and its contents tooltip) and the Dust Grinder (grinds researched compounds, Alchemical Dust and crystals into
    // orbs or jars)
    public static final DeferredBlock<Block> DUST_JAR = DUSTBLOCKS.registerBlock("dust_jar", properties -> new com.mowmaster.dust.Features.DustStorage.DustJarBlock(
            properties.mapColor(MapColor.NONE).sound(SoundType.GLASS).strength(0.6F).noOcclusion()));
    static {
        DustItemRegistry.DUSTITEMS.registerItem("dust_jar", properties -> new com.mowmaster.dust.Features.DustStorage.DustJarItem(DUST_JAR.get(), properties.useBlockDescriptionPrefix()));
    }
    public static final DeferredBlock<Block> DUST_GRINDER = registerBlock("dust_grinder", properties -> new com.mowmaster.dust.Features.DustStorage.DustGrinderBlock(
            properties.mapColor(MapColor.STONE).sound(SoundType.STONE).strength(2.0F, 6.0F).noOcclusion()));
    // The Glass Dust Tube: carries element dust between Dust Jars and the alchemical apparatus (DustNetwork)
    public static final DeferredBlock<Block> DUST_TUBE = registerBlock("dust_tube", properties -> new com.mowmaster.dust.Features.DustStorage.DustTubeBlock(
            properties.mapColor(MapColor.NONE).sound(SoundType.GLASS).strength(0.5F).noOcclusion()));

    // A Dust Pile: dust spilled out of an open tube end (DustClouds). No item, nothing drops; walked through.
    public static final DeferredBlock<Block> DUST_PILE = DUSTBLOCKS.registerBlock("dust_pile", properties -> new com.mowmaster.dust.Features.DustStorage.DustPileBlock(
            properties.mapColor(MapColor.SAND).noCollision().strength(0.1F).sound(SoundType.SAND).noOcclusion()
                    .pushReaction(PushReaction.POPPED).noLootTable()));

    // Lamps (Features/Lamps): lanterns that burn element dust for an effect on what's round them. Set up as vanilla's
    // lantern is.
    public static final DeferredBlock<Block> GROWTH_LAMP = registerBlock("growth_lamp", properties -> new com.mowmaster.dust.Features.Lamps.GrowthLampBlock(
            lampProperties(properties)));
    public static final DeferredBlock<Block> LOVELY_LAMP = registerBlock("lovely_lamp", properties -> new com.mowmaster.dust.Features.Lamps.LovelyLampBlock(
            lampProperties(properties)));

    private static BlockBehaviour.Properties lampProperties(BlockBehaviour.Properties properties)
    {
        return properties.mapColor(MapColor.METAL).forceSolidOn().strength(3.5F).sound(SoundType.LANTERN).lightLevel(state -> 15).noOcclusion()
                .pushReaction(PushReaction.POPPED);
    }

    // The alchemical apparatus (Features/Apparatus): factory machines of copper, brass and iron, one for each research
    // station, that work without stopping and pour the dust they make along Glass Dust Tubes into Dust Jars
    public static final DeferredBlock<Block> CRUSHING_MILL = apparatus("crushing_mill", com.mowmaster.dust.Features.Apparatus.ApparatusShapes.CRUSHING_MILL,
            com.mowmaster.dust.Features.Apparatus.CrushingMillBlockEntity::new);
    public static final DeferredBlock<Block> ALCHEMICAL_SEPARATOR = apparatus("alchemical_separator", com.mowmaster.dust.Features.Apparatus.ApparatusShapes.SEPARATOR,
            com.mowmaster.dust.Features.Apparatus.SeparatorBlockEntity::new);
    public static final DeferredBlock<Block> EVAPORATING_KETTLE = apparatus("evaporating_kettle", com.mowmaster.dust.Features.Apparatus.ApparatusShapes.KETTLE,
            com.mowmaster.dust.Features.Apparatus.EvaporatingKettleBlockEntity::new);
    // The Still (Features/Apparatus/still: it replaced the Alembic), stacked: firebox, pot, towers, top
    public static final DeferredBlock<Block> STILL_FIREBOX = registerBlock("firebox_burnable", properties -> new com.mowmaster.dust.Features.Apparatus.still.StillFireboxBlock(
            properties.mapColor(MapColor.COLOR_RED).sound(SoundType.STONE).strength(3.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion()
                    .lightLevel(state -> state.getValue(com.mowmaster.dust.Features.Apparatus.still.StillFireboxBlock.LIT) ? 13 : 0)));
    public static final DeferredBlock<Block> STILL_POT = registerBlock("still_pot", properties -> new com.mowmaster.dust.Features.Apparatus.still.StillPotBlock(
            properties.mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).strength(3.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredBlock<Block> STILL_TOWER = registerBlock("still_tower", properties -> new com.mowmaster.dust.Features.Apparatus.still.StillTowerBlock(
            properties.mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).strength(3.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredBlock<Block> STILL_TOP = registerBlock("still_top", properties -> new com.mowmaster.dust.Features.Apparatus.still.StillTopBlock(
            properties.mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).strength(3.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion()));
    // A Still Pot's Dust Chute: lets the pot's dust out as Dust Piles (filtered: one element)
    public static final DeferredBlock<Block> DUST_CHUTE = registerBlock("dust_chute", properties -> new com.mowmaster.dust.Features.Apparatus.still.DustChuteBlock(
            properties.mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5F, 6.0F).noOcclusion()));
    // The Dust Collector: gathers Dust Piles off its top into its tubes
    public static final DeferredBlock<Block> DUST_COLLECTOR = registerBlock("dust_collector", properties -> new com.mowmaster.dust.Features.DustStorage.DustCollectorBlock(
            properties.mapColor(MapColor.STONE).sound(SoundType.STONE).strength(2.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion()));
    // The Dust Separator (a tumbling drum) and its extensions, and the Dust Mixer
    public static final DeferredBlock<Block> DUST_SEPARATOR = registerBlock("dust_seperator", properties -> new com.mowmaster.dust.Features.Apparatus.separator.DustSeparatorBlock(
            properties.mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).strength(3.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredBlock<Block> DUST_SEPARATOR_EXTENSION = registerBlock("dust_seperator_extension", properties -> new com.mowmaster.dust.Features.Apparatus.separator.DustSeparatorExtensionBlock(
            properties.mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).strength(3.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredBlock<Block> DUST_MIXER = registerBlock("dust_mixer", properties -> new com.mowmaster.dust.Features.Apparatus.mixer.DustMixerBlock(
            properties.mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).strength(3.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredBlock<Block> TRANSMUTER = apparatus("transmuter", com.mowmaster.dust.Features.Apparatus.ApparatusShapes.TRANSMUTER,
            com.mowmaster.dust.Features.Apparatus.TransmuterBlockEntity::new);
    public static final DeferredBlock<Block> CONJOINING_CRUCIBLE = apparatus("conjoining_crucible", com.mowmaster.dust.Features.Apparatus.ApparatusShapes.CRUCIBLE,
            com.mowmaster.dust.Features.Apparatus.ConjoiningCrucibleBlockEntity::new);
    public static final DeferredBlock<Block> MIXING_VAT = apparatus("mixing_vat", com.mowmaster.dust.Features.Apparatus.ApparatusShapes.MIXING_VAT,
            com.mowmaster.dust.Features.Apparatus.MixingVatBlockEntity::new);
    public static final DeferredBlock<Block> DISSOLVING_RETORT = apparatus("dissolving_retort", com.mowmaster.dust.Features.Apparatus.ApparatusShapes.RETORT,
            com.mowmaster.dust.Features.Apparatus.DissolvingRetortBlockEntity::new);

    // One apparatus: copper, brass and iron (mined with a pickaxe; tool needed for it to drop), its own shape and block
    // entity (ApparatusBlock)
    private static DeferredBlock<Block> apparatus(String name, net.minecraft.world.phys.shapes.VoxelShape shape,
                                                  java.util.function.BiFunction<net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState,
                                                          ? extends com.mowmaster.dust.Features.Apparatus.ApparatusBlockEntity> factory)
    {
        return registerBlock(name, properties -> new com.mowmaster.dust.Features.Apparatus.ApparatusBlock(properties.mapColor(MapColor.COLOR_ORANGE)
                .sound(SoundType.COPPER).strength(3.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion(), shape, factory));
    }

    // The Spell Caster (Features/Spells/caster): casts a scroll's spell on a redstone pulse, on dust and energy crystals
    public static final DeferredBlock<Block> SPELL_CASTER = registerBlock("spell_caster", properties -> new com.mowmaster.dust.Features.Spells.caster.SpellCasterBlock(
            properties.mapColor(MapColor.STONE).sound(SoundType.STONE).strength(3.5F).requiresCorrectToolForDrops()));

    // Research machines (Features/Machines): break materials down, step by step, for research
    public static final DeferredBlock<Block> RESEARCH_CRUSHER = registerBlock("research_crusher", properties -> new ResearchCrusherBlock(
            properties.mapColor(MapColor.STONE).sound(SoundType.STONE).strength(2.0F, 6.0F).noOcclusion()));
    public static final DeferredBlock<Block> RESEARCH_SEPARATION = registerBlock("research_separation", properties -> new ResearchSeparationBlock(
            properties.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).noOcclusion()));
    public static final DeferredBlock<Block> RESEARCH_EVAPORATION = registerBlock("research_evaporation", properties -> new ResearchEvaporationBlock(
            properties.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).noOcclusion()));
    public static final DeferredBlock<Block> RESEARCH_DISTILLATION = registerBlock("research_distalation", properties -> new ResearchDistillationBlock(
            properties.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).noOcclusion()));
    // Learning the elements: compounds into Alchemical Dust (and dust into crystals), then dusts put together
    public static final DeferredBlock<Block> RESEARCH_TRANSFORMATION = registerBlock("research_transformational", properties -> new ResearchTransformationBlock(
            properties.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).noOcclusion()));
    public static final DeferredBlock<Block> RESEARCH_COMBINATION = registerBlock("research_combination", properties -> new ResearchCombinationBlock(
            properties.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).noOcclusion()));
    public static final DeferredBlock<Block> RESEARCH_MIXING = registerBlock("research_mixing", properties -> new ResearchMixingBlock(
            properties.mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).noOcclusion()));

    // Spell crafting (Features/Spells/pylons): the Spell Pylon, and the pylons that grow its tree
    public static final DeferredBlock<Block> SPELL_PYLON = registerBlock("spell_pylon", properties -> new SpellPylonBlock(
            properties.mapColor(MapColor.STONE).sound(SoundType.AMETHYST).strength(2.0F, 6.0F).noOcclusion()));
    public static final DeferredBlock<Block> SHAPE_PYLON = pylon("shape_pylon", PylonType.SHAPE);
    public static final DeferredBlock<Block> NEUTRAL_PYLON = pylon("neutral_pylon", PylonType.NEUTRAL);
    public static final DeferredBlock<Block> THRIFTY_PYLON = pylon("thrifty_pylon", PylonType.THRIFTY);
    public static final DeferredBlock<Block> GIANT_PYLON = pylon("giant_pylon", PylonType.GIANT);
    public static final DeferredBlock<Block> EXPERIENCED_PYLON = pylon("experienced_pylon", PylonType.EXPERIENCED);
    public static final DeferredBlock<Block> DAMAGE_PYLON = pylon("damage_pylon", PylonType.DAMAGE);

    // Blocks spells leave behind (Features/Spells/blocks). No items: they only come from spells.
    // The Rune modifier's glyph on the ground (RuneBlock)
    public static final DeferredBlock<Block> RUNE = DUSTBLOCKS.registerBlock("rune", properties -> new com.mowmaster.dust.Features.Spells.blocks.RuneBlock(
            properties.mapColor(MapColor.NONE).noCollision().instabreak().sound(SoundType.AMETHYST).noLootTable().lightLevel(state -> 4)));
    public static final DeferredBlock<Block> POISON_POOL = DUSTBLOCKS.registerBlock("poison_pool", properties -> new PoisonPoolBlock(
            properties.mapColor(MapColor.COLOR_GREEN).noCollision().instabreak().sound(SoundType.SLIME_BLOCK).noLootTable()));
    public static final DeferredBlock<Block> BLIGHT = DUSTBLOCKS.registerBlock("blight", properties -> new BlightBlock(
            properties.mapColor(MapColor.COLOR_RED).noCollision().instabreak().sound(SoundType.SCULK).noLootTable()));
    public static final DeferredBlock<Block> CORRUPTED = DUSTBLOCKS.registerBlock("corrupted_block", properties -> new CorruptedBlock(
            properties.mapColor(MapColor.COLOR_PURPLE).strength(1.5F, 3.0F).sound(SoundType.SCULK).noLootTable()));

    public static final DeferredBlock<Block> SPELL_LIGHT = DUSTBLOCKS.registerBlock("spell_light", properties -> new SpellLightBlock(
            properties.mapColor(MapColor.NONE).noCollision().instabreak().lightLevel(state -> 15).noOcclusion().sound(SoundType.AMETHYST).noLootTable()));
    public static final DeferredBlock<Block> VEILED = DUSTBLOCKS.registerBlock("veiled_block", properties -> new PhantomBlock(
            properties.mapColor(MapColor.NONE).strength(-1.0F, 3600000.0F).noOcclusion().sound(SoundType.GLASS).noLootTable(), true));
    public static final DeferredBlock<Block> PHASED = DUSTBLOCKS.registerBlock("phased_block", properties -> new PhantomBlock(
            properties.mapColor(MapColor.NONE).strength(-1.0F, 3600000.0F).noCollision().noOcclusion().sound(SoundType.GLASS).noLootTable(), false));
    public static final DeferredBlock<Block> WARDED_GLASS = DUSTBLOCKS.registerBlock("warded_glass", properties -> new WardedGlassBlock(
            properties.mapColor(MapColor.COLOR_LIGHT_BLUE).strength(0.5F, 3600000.0F).noOcclusion().sound(SoundType.GLASS).noLootTable()
                    .isValidSpawn((state, level, pos, type) -> false).isRedstoneConductor((state, level, pos) -> false)
                    .isSuffocating((state, level, pos) -> false)));

    // Placed near a Spell Pylon, they show its blessing / corruption on their nodes (Features/Spells/pylons/CounterPylonBlock)
    public static final DeferredBlock<Block> PYLON_BLESSED = registerBlock("pylon_blessed", properties -> new com.mowmaster.dust.Features.Spells.pylons.CounterPylonBlock(
            properties.mapColor(MapColor.STONE).sound(SoundType.AMETHYST).strength(1.5F, 6.0F).noOcclusion(), true));
    public static final DeferredBlock<Block> PYLON_CORRUPTED = registerBlock("pylon_corrupted", properties -> new com.mowmaster.dust.Features.Spells.pylons.CounterPylonBlock(
            properties.mapColor(MapColor.STONE).sound(SoundType.AMETHYST).strength(1.5F, 6.0F).noOcclusion(), false));

    private static DeferredBlock<Block> pylon(String name, PylonType type)
    {
        return registerBlock(name, properties -> new PylonBlock(properties.mapColor(MapColor.STONE).sound(SoundType.AMETHYST)
                .strength(1.5F, 6.0F).noOcclusion(), type));
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function)
    {
        return registerBlock(name, function, UnaryOperator.identity());
    }

    // itemProperties: extra settings for the block's item (UnaryOperator<X> = a function from X to X)
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function,
                                                                    UnaryOperator<Item.Properties> itemProperties)
    {
        DeferredBlock<T> toReturn = DUSTBLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn, itemProperties);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block, UnaryOperator<Item.Properties> itemProperties)
    {
        DustItemRegistry.DUSTITEMS.registerItem(name, properties -> new BlockItem(block.get(), itemProperties.apply(properties.useBlockDescriptionPrefix())));
    }

    public static void register(IEventBus eventBus)
    {
        DUSTBLOCKS.register(eventBus);
    }
}
