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
            itemProperties -> itemProperties.compostable(ContextIntProviders.COMPOSTABLE_LOW));
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
    public static final DeferredBlock<Block> CHANGE_PYLON = pylon("change_pylon", PylonType.CHANGE);
    public static final DeferredBlock<Block> COST_PYLON = pylon("cost_pylon", PylonType.COST);
    public static final DeferredBlock<Block> COUNT_PYLON = pylon("count_pylon", PylonType.COUNT);
    public static final DeferredBlock<Block> DAMAGE_PYLON = pylon("damage_pylon", PylonType.DAMAGE);

    // Blocks spells leave behind (Features/Spells/blocks). No items: they only come from spells.
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
            properties.mapColor(MapColor.COLOR_LIGHT_BLUE).strength(0.5F, 6.0F).noOcclusion().sound(SoundType.GLASS).noLootTable()
                    .isValidSpawn((state, level, pos, type) -> false).isRedstoneConductor((state, level, pos) -> false)
                    .isSuffocating((state, level, pos) -> false)));

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
