package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.AncientLanguage;
import com.mowmaster.dust.Features.AncientLanguage.GlyphStoneBlock;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
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

        createGlyphStone(blockModels);

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

    // One "orientable" model per glyph (front = textures/block/glyph_stone/glyph_stone_<letter>, sides and top plain),
    // picked by the GLYPH property and turned by FACING. The item shows the 'a' glyph.
    private void createGlyphStone(BlockModelGenerators blockModels)
    {
        Block block = DustBlockRegistry.GLYPH_STONE.get();
        Material side = new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/glyph_stone/glyph_stone_side"));
        Material top = new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/glyph_stone/glyph_stone_top"));
        Identifier[] models = new Identifier[AncientLanguage.GLYPH_COUNT];
        for (int i = 0; i < models.length; i++) {
            String letter = String.valueOf((char) ('a' + i));
            TextureMapping textures = new TextureMapping()
                    .put(TextureSlot.FRONT, new Material(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/glyph_stone/glyph_stone_" + letter)))
                    .put(TextureSlot.SIDE, side)
                    .put(TextureSlot.TOP, top);
            models[i] = ModelTemplates.CUBE_ORIENTABLE.createWithSuffix(block, "_" + letter, textures, blockModels.modelOutput);
        }
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(GlyphStoneBlock.GLYPH).generate(glyph -> BlockModelGenerators.plainVariant(models[glyph])))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.registerSimpleItemModel(block, models[0]);
    }
}