package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustReferences;
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

}