package com.mowmaster.dust.DustDataGen;

import net.minecraft.core.registries.BuiltInRegistries;
import java.util.List;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.AncientLanguage;
import com.mowmaster.dust.Features.AncientLanguage.GlyphStoneBlock;
import com.mowmaster.dust.Features.AncientLanguage.Client.GlyphStoneTint;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.DustComposition.ElementTesterBlock;
import com.mowmaster.dust.Features.Machines.Client.AlchemicalDustTint;
import com.mowmaster.dust.Features.Machines.Client.CompoundTint;
import com.mowmaster.dust.Features.Machines.Client.SourceBlockTint;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelLocationUtils;

import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

import java.util.Map;
import java.util.Optional;

public class DustProviderModel extends ModelProvider {
    public DustProviderModel(PackOutput output) {
        super(output, DustReferences.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        itemModels.generateFlatItem(DustItemRegistry.SPELL_WAND_FIRE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.SPELL_WAND_EARTH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.ANCIENT_INK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.ANCIENT_TABLET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.TRANSCRIPTION.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.PARCHMENT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.RESEARCH_NOTES.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.SHARED_RESEARCH_GUIDE.get(), ModelTemplates.FLAT_ITEM);

        createGlyphStone(blockModels);

        // Dust composition
        itemModels.generateFlatItem(DustItemRegistry.MAGICAL_FLINT_AND_STEEL.get(), ModelTemplates.FLAT_ITEM);
        createDustFire(blockModels);
        createElementTester(blockModels);
        createElementItems(itemModels);
        createResearchCrusher(blockModels, itemModels);
        createResearchSeparation(blockModels, itemModels);
        createResearchEvaporation(blockModels);
        createResearchDistillation(blockModels, itemModels);
        createElementStations(blockModels, itemModels);
        createSpellScroll(itemModels);
        createPylons(blockModels);
        // Blocks spells leave behind: their models are in models/block/spell (no items)
        for (Block block : List.of(DustBlockRegistry.POISON_POOL.get(), DustBlockRegistry.BLIGHT.get(), DustBlockRegistry.CORRUPTED.get())) {
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(
                    Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/spell/" + BuiltInRegistries.BLOCK.getKey(block).getPath()))));
        }

        itemModels.generateFlatItem(DustItemRegistry.DUST_RED.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.DUST_GREEN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.DUST_BLUE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.DUST_WHITE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.DUST_BLACK.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_INERT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_RED.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_GREEN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_BLUE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_WHITE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_BLACK.get(), ModelTemplates.FLAT_ITEM);

        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_DUST_RED.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_DUST_GREEN.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_DUST_BLUE.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_DUST_WHITE.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_DUST_BLACK.get());

        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_CRYSTAL_GREEN.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLUE.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_CRYSTAL_WHITE.get());
        blockModels.createTrivialCube(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLACK.get());

        blockModels.createTrivialCube(DustBlockRegistry.INERT_CRYSTAL_ORE.get());
        blockModels.createTrivialCube(DustBlockRegistry.INERT_CRYSTAL_ORE_DEEPSLATE.get());

        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateSpear(DustItemRegistry.CRYSTAL_SPEAR.get());
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_MATTOCK.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Armor icons with one model per vanilla trim material. Deprecated in 26.3 but still works;
        // Map.of() = no trim palette replacements.
        itemModels.generateTrimmableItem(DustItemRegistry.CRYSTAL_HELMET.get(), ItemModelGenerators.TRIM_PREFIX_HELMET, false, Map.of());
        itemModels.generateTrimmableItem(DustItemRegistry.CRYSTAL_CHESTPLATE.get(), ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false, Map.of());
        itemModels.generateTrimmableItem(DustItemRegistry.CRYSTAL_LEGGINGS.get(), ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false, Map.of());
        itemModels.generateTrimmableItem(DustItemRegistry.CRYSTAL_BOOTS.get(), ItemModelGenerators.TRIM_PREFIX_BOOTS, false, Map.of());

        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_HORSE_ARMOR.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        blockModels.createPlantWithDefaultItem(
                DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL.get(),
                DustBlockRegistry.BLOCK_POTTEDFLOWER_WINDWHEEL.get(),
                BlockModelGenerators.PlantType.TINTED
        );

    }

    // The dust fire: shaped like soul fire (a floor fire plus four sides, each picking one of a few models at random),
    // but from Dust's own templates (models/block/template_dust_fire_*.json, hand-written), which are vanilla's with
    // "tintindex": 0 on every face so MowLib's tint source colours them. Textures: block/dust_fire_0 and _1 (greyscale).
    private static final ModelTemplate DUST_FIRE_FLOOR = dustTemplate("template_dust_fire_floor", TextureSlot.FIRE);
    private static final ModelTemplate DUST_FIRE_SIDE = dustTemplate("template_dust_fire_side", TextureSlot.FIRE);
    private static final ModelTemplate DUST_FIRE_SIDE_ALT = dustTemplate("template_dust_fire_side_alt", TextureSlot.FIRE);

    private static ModelTemplate dustTemplate(String name, TextureSlot... slots)
    {
        return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/" + name)), Optional.empty(), slots);
    }

    private void createDustFire(BlockModelGenerators blockModels)
    {
        Block block = DustBlockRegistry.DUST_FIRE.get();
        // TextureMapping.fire0 / fire1: the block's texture with "_0" / "_1" on the end
        TextureMapping fire0 = TextureMapping.fire0(block);
        TextureMapping fire1 = TextureMapping.fire1(block);
        MultiVariant floor = BlockModelGenerators.variants(
                BlockModelGenerators.plainModel(DUST_FIRE_FLOOR.create(ModelLocationUtils.getModelLocation(block, "_floor0"), fire0, blockModels.modelOutput)),
                BlockModelGenerators.plainModel(DUST_FIRE_FLOOR.create(ModelLocationUtils.getModelLocation(block, "_floor1"), fire1, blockModels.modelOutput)));
        MultiVariant sides = BlockModelGenerators.variants(
                BlockModelGenerators.plainModel(DUST_FIRE_SIDE.create(ModelLocationUtils.getModelLocation(block, "_side0"), fire0, blockModels.modelOutput)),
                BlockModelGenerators.plainModel(DUST_FIRE_SIDE.create(ModelLocationUtils.getModelLocation(block, "_side1"), fire1, blockModels.modelOutput)),
                BlockModelGenerators.plainModel(DUST_FIRE_SIDE_ALT.create(ModelLocationUtils.getModelLocation(block, "_side_alt0"), fire0, blockModels.modelOutput)),
                BlockModelGenerators.plainModel(DUST_FIRE_SIDE_ALT.create(ModelLocationUtils.getModelLocation(block, "_side_alt1"), fire1, blockModels.modelOutput)));
        // A multipart blockstate: every part with no condition is always drawn
        blockModels.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
                .with(floor)
                .with(sides)
                .with(sides.with(BlockModelGenerators.Y_ROT_90))
                .with(sides.with(BlockModelGenerators.Y_ROT_180))
                .with(sides.with(BlockModelGenerators.Y_ROT_270)));
    }

    // Every element's crystal and dust (not the five primals, which have their own textures): one shared flat model
    // per kind, from a greyscale texture (item/element_crystal, item/element_dust), tinted with the element's colour.
    // tintedModel + Constant: an item model whose layer 0 is always drawn in that colour (0xAARRGGBB).
    private void createElementItems(ItemModelGenerators itemModels)
    {
        Identifier crystalModel = ModelTemplates.FLAT_ITEM.create(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/element_crystal"),
                TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/element_crystal"))), itemModels.modelOutput);
        Identifier dustModel = ModelTemplates.FLAT_ITEM.create(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/element_dust"),
                TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/element_dust"))), itemModels.modelOutput);
        for (DustElement element : DustElements.all()) {
            if (element.isPrimal()) continue;
            int color = 0xFF000000 | element.color();
            itemModels.itemModelOutput.accept(DustItemRegistry.CRYSTALS.get(element).get(), ItemModelUtils.tintedModel(crystalModel, new Constant(color)));
            itemModels.itemModelOutput.accept(DustItemRegistry.DUSTS.get(element).get(), ItemModelUtils.tintedModel(dustModel, new Constant(color)));
        }
    }

    // The Research Crusher: the block shows its frame and funnel (models/block/station/research_crusher_body.json); its
    // wheels turn in Client/ResearchCrusherRenderer. Turned by FACING (the model's tray faces north). The item shows the
    // whole thing (research_crusher.json, wheels and all).
    // Crushed blocks: a greyscale pebbles texture tinted with their block's colour (Client/SourceBlockTint).
    private void createResearchCrusher(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        Block block = DustBlockRegistry.RESEARCH_CRUSHER.get();
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block,
                        BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_crusher_body")))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(block.asItem(),
                ItemModelUtils.plainModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_crusher")));

        Identifier crushedModel = ModelTemplates.FLAT_ITEM.create(DustItemRegistry.CRUSHED_BLOCK.get(), TextureMapping.layer0(DustItemRegistry.CRUSHED_BLOCK.get()), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(DustItemRegistry.CRUSHED_BLOCK.get(), ItemModelUtils.tintedModel(crushedModel, new SourceBlockTint()));
    }

    // The Alchemical Separation Table: research_separation.json for both the block and the item (the glyph that rises
    // off the scroll while separating is drawn by Client/ResearchSeparationRenderer). Turned by FACING (the outputs
    // face the player).
    // Alchemical compounds: paper (layer 0, as it is) with a dust pile (layer 1) tinted the compound's colour.
    private void createResearchSeparation(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        Block block = DustBlockRegistry.RESEARCH_SEPARATION.get();
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block,
                        BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_separation")))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(block.asItem(),
                ItemModelUtils.plainModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_separation")));

        Identifier compoundModel = ModelTemplates.TWO_LAYERED_ITEM.create(DustItemRegistry.ALCHEMICAL_COMPOUND.get(),
                TextureMapping.layered(new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/alchemical_compound_paper")),
                        new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/alchemical_compound_dust"))),
                itemModels.modelOutput);
        // One tint per layer: the paper as it is (constant white), the pile in the compound's colour
        itemModels.itemModelOutput.accept(DustItemRegistry.ALCHEMICAL_COMPOUND.get(),
                ItemModelUtils.tintedModel(compoundModel, new Constant(-1), new CompoundTint()));
        itemModels.generateFlatItem(DustItemRegistry.ALCHEMICAL_SOOT.get(), ModelTemplates.FLAT_ITEM);
    }

    // The Evaporation Table: the block shows everything but the spoon (research_evaporation_body.json), which stirs in
    // Client/ResearchEvaporationRenderer. Turned by FACING (the output faces the player). The item shows the whole thing.
    private void createResearchEvaporation(BlockModelGenerators blockModels)
    {
        Block block = DustBlockRegistry.RESEARCH_EVAPORATION.get();
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block,
                        BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_evaporation_body")))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(block.asItem(),
                ItemModelUtils.plainModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_evaporation")));
    }

    // The Distillation Table: nothing in the model moves (the glyph, liquid and items are drawn by
    // Client/ResearchDistillationRenderer), so the block and the item both use the whole model. Turned by FACING.
    // Its compounds: a gas in a bottle (vanilla's potion: the liquid overlay, layer 0, tinted the compound's colour; the
    // bottle, layer 1, as it is) and a liquid in a bowl (the bowl as it is, layer 0; its contents tinted, layer 1).
    private void createResearchDistillation(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        Block block = DustBlockRegistry.RESEARCH_DISTILLATION.get();
        Identifier model = Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_distalation");
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(model))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));

        Identifier gasModel = ModelTemplates.TWO_LAYERED_ITEM.create(DustItemRegistry.ALCHEMICAL_GAS.get(),
                TextureMapping.layered(new Material(Identifier.withDefaultNamespace("item/potion_overlay")),
                        new Material(Identifier.withDefaultNamespace("item/potion"))),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(DustItemRegistry.ALCHEMICAL_GAS.get(),
                ItemModelUtils.tintedModel(gasModel, new CompoundTint(), new Constant(-1)));

        Identifier liquidModel = ModelTemplates.TWO_LAYERED_ITEM.create(DustItemRegistry.ALCHEMICAL_LIQUID.get(),
                TextureMapping.layered(new Material(Identifier.withDefaultNamespace("item/bowl")),
                        new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/alchemical_compound_bowl_contents"))),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(DustItemRegistry.ALCHEMICAL_LIQUID.get(),
                ItemModelUtils.tintedModel(liquidModel, new Constant(-1), new CompoundTint()));
    }

    // The Transformation and Combination Tables: nothing in the model moves (the glyphs and items are drawn by their
    // renderers), so block and item use the whole model. The Mixing Table's block leaves out the spoon
    // (research_mixing_body.json), which stirs in Client/ResearchMixingRenderer; its item shows the whole thing.
    // Alchemical Dust: the heap (layer 0) tinted the element's colour, its sparkles (layer 1) as they are.
    private void createElementStations(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        station(blockModels, DustBlockRegistry.RESEARCH_TRANSFORMATION.get(), "research_transformational", "research_transformational");
        station(blockModels, DustBlockRegistry.RESEARCH_COMBINATION.get(), "research_combination", "research_combination");
        station(blockModels, DustBlockRegistry.RESEARCH_MIXING.get(), "research_mixing_body", "research_mixing");

        Identifier dustModel = ModelTemplates.TWO_LAYERED_ITEM.create(DustItemRegistry.ALCHEMICAL_DUST.get(),
                TextureMapping.layered(new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/alchemical_dust")),
                        new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/alchemical_dust_sparkle"))),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(DustItemRegistry.ALCHEMICAL_DUST.get(),
                ItemModelUtils.tintedModel(dustModel, new AlchemicalDustTint(), new Constant(-1)));
    }

    // A Spell Scroll: the parchment (layer 0) as it is, its seal (layer 1) in the spell's element colour
    private void createSpellScroll(ItemModelGenerators itemModels)
    {
        Identifier model = ModelTemplates.TWO_LAYERED_ITEM.create(DustItemRegistry.SPELL_SCROLL.get(),
                TextureMapping.layered(new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/spell_scroll")),
                        new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "item/spell_scroll_seal"))),
                itemModels.modelOutput);
        itemModels.itemModelOutput.accept(DustItemRegistry.SPELL_SCROLL.get(),
                ItemModelUtils.tintedModel(model, new Constant(-1), new com.mowmaster.dust.Features.Spells.Client.SpellTint()));
    }

    // The spell pylons: placeholder models (models/block/pylon/<name>.json) until the real ones are made. Every state
    // uses the same model for now; the blockstate already lists them so each can get its own model later (the
    // Spell Pylon's active=true/false, the modifier pylons' state=inert/linked/blessed/corrupted/break_on/break_off).
    private void createPylons(BlockModelGenerators blockModels)
    {
        pylonModel(blockModels, DustBlockRegistry.SPELL_PYLON.get(), "spell_pylon");
        pylonModel(blockModels, DustBlockRegistry.SHAPE_PYLON.get(), "shape_pylon");
        pylonModel(blockModels, DustBlockRegistry.CHANGE_PYLON.get(), "change_pylon");
        pylonModel(blockModels, DustBlockRegistry.COST_PYLON.get(), "cost_pylon");
        pylonModel(blockModels, DustBlockRegistry.COUNT_PYLON.get(), "count_pylon");
        pylonModel(blockModels, DustBlockRegistry.DAMAGE_PYLON.get(), "damage_pylon");
    }

    private void pylonModel(BlockModelGenerators blockModels, Block block, String name)
    {
        Identifier model = Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/pylon/" + name);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(model)));
        blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
    }

    // A station turned by FACING (the models face north), with its block and item models
    private void station(BlockModelGenerators blockModels, Block block, String blockModel, String itemModel)
    {
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block,
                        BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/" + blockModel)))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(block.asItem(),
                ItemModelUtils.plainModel(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/" + itemModel)));
    }

    // The Element Tester: one hand-made model per number of crystals (models/block/station/element_tester_<n>.json),
    // turned to face the way it was placed (ROTATION_HORIZONTAL_FACING: the models face north). The item shows the bare
    // workstation.
    private static final String[] TESTER_MODELS = {"default", "one", "two", "three", "four", "five"};

    private void createElementTester(BlockModelGenerators blockModels)
    {
        Block block = DustBlockRegistry.ELEMENT_TESTER.get();
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(ElementTesterBlock.CRYSTALS).generate(count -> BlockModelGenerators.plainVariant(testerModel(count))))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(testerModel(0)));
    }

    private static Identifier testerModel(int crystals)
    {
        return Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/element_tester_" + TESTER_MODELS[crystals]);
    }

    // The glyph stone's own model shape (models/block/glyph_stone_template.json, hand-written): a stone cube plus the
    // glyph as an overlay on the front with tint index 0, so Client/GlyphStoneTint can colour it per player.
    private static final TextureSlot GLYPH_SLOT = TextureSlot.create("glyph");
    private static final ModelTemplate GLYPH_STONE_TEMPLATE = new ModelTemplate(
            Optional.of(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/glyph_stone_template")), Optional.empty(),
            TextureSlot.FRONT, TextureSlot.SIDE, TextureSlot.TOP, GLYPH_SLOT);

    // One model per glyph, picked by the GLYPH property and turned by FACING. The item shows the 'a' glyph, tinted
    // with the carved colour (item models need their own tint, the block tint isn't used for items).
    private void createGlyphStone(BlockModelGenerators blockModels)
    {
        Block block = DustBlockRegistry.GLYPH_STONE.get();
        Material front = new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/glyph_stone/glyph_stone_front"));
        Material side = new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/glyph_stone/glyph_stone_side"));
        Material top = new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/glyph_stone/glyph_stone_top"));
        Identifier[] models = new Identifier[AncientLanguage.GLYPH_COUNT];
        for (int i = 0; i < models.length; i++) {
            String letter = String.valueOf((char) ('a' + i));
            TextureMapping textures = new TextureMapping()
                    .put(TextureSlot.FRONT, front)
                    .put(TextureSlot.SIDE, side)
                    .put(TextureSlot.TOP, top)
                    .put(GLYPH_SLOT, new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/glyph_stone/glyph_stone_" + letter)));
            models[i] = GLYPH_STONE_TEMPLATE.createWithSuffix(block, "_" + letter, textures, blockModels.modelOutput);
        }
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(GlyphStoneBlock.GLYPH).generate(glyph -> BlockModelGenerators.plainVariant(models[glyph])))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.tintedModel(models[0], new Constant(GlyphStoneTint.CARVED)));
    }
}