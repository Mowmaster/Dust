package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.AncientLanguage;
import com.mowmaster.dust.Features.AncientLanguage.GlyphStoneBlock;
import com.mowmaster.dust.Features.AncientLanguage.Client.GlyphStoneTint;
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
        createTestingDisk(blockModels);

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

    // The Dust Testing Disk, for now a plain block made of vanilla textures (it gets its own model later).
    // Its item uses the block model (ModelProvider fills that in for block items without one).
    private void createTestingDisk(BlockModelGenerators blockModels)
    {
        Block block = DustBlockRegistry.TESTING_DISK.get();
        TextureMapping textures = new TextureMapping()
                .put(TextureSlot.TOP, new Material(Identifier.withDefaultNamespace("block/lodestone_top")))
                .put(TextureSlot.SIDE, new Material(Identifier.withDefaultNamespace("block/lodestone_side")))
                .put(TextureSlot.BOTTOM, new Material(Identifier.withDefaultNamespace("block/smooth_stone")));
        Identifier model = ModelTemplates.CUBE_BOTTOM_TOP.create(block, textures, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
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