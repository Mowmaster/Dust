package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.GlyphStoneBlock;
import com.mowmaster.dust.Features.DustComposition.DustFireBlock;
import com.mowmaster.dust.Features.DustComposition.ElementTesterBlock;
import com.mowmaster.dust.Features.Machines.ResearchCrusherBlock;
import com.mowmaster.dust.Features.Machines.ResearchCombinationBlock;
import com.mowmaster.dust.Features.Spells.pylons.PylonBlock;
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


    public static final DeferredBlock<Block> INERT_CRYSTAL_ORE = registerBlock("block_inert_crystal_ore", properties -> new DropExperienceBlock(
            UniformInt.of(2,4), properties.sound(SoundType.STONE).strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> INERT_CRYSTAL_ORE_DEEPSLATE = registerBlock("block_inert_crystal_ore_deepslate", properties -> new DropExperienceBlock(
            UniformInt.of(2,4), properties.sound(SoundType.DEEPSLATE).strength(5f).requiresCorrectToolForDrops()));


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
