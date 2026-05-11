package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.CrystalTools.Materials.DustMaterialArmor;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

public class DustProviderModel extends ModelProvider {
    public DustProviderModel(PackOutput output) {
        super(output, DustReferences.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        itemModels.generateFlatItem(DustItemRegistry.CORNBREAD.get(), ModelTemplates.FLAT_ITEM);


        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_CHISEL_IRON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_HALFSAW_IRON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CHARCOAL_WHITE.get(), ModelTemplates.FLAT_ITEM);


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



        blockModels.createTrivialCube(DustBlockRegistry.CRYSTAL_PATH_TIER1.get());
        blockModels.createTrivialCube(DustBlockRegistry.CRYSTAL_PATH_TIER2.get());
        blockModels.createTrivialCube(DustBlockRegistry.CRYSTAL_PATH_TIER3.get());
        blockModels.createTrivialCube(DustBlockRegistry.CRYSTAL_PATH_TIER4.get());


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

        blockModels.family(DustBlockRegistry.BLOCK_PLANKS_GREEN.get())
                .stairs(DustBlockRegistry.BLOCK_STAIRS_GREEN.get())
                .slab(DustBlockRegistry.BLOCK_SLAB_GREEN.get())
                .button(DustBlockRegistry.BLOCK_BUTTON_GREEN.get())
                .pressurePlate(DustBlockRegistry.BLOCK_PRESSUREPLATE_GREEN.get())
                .fence(DustBlockRegistry.BLOCK_FENCE_GREEN.get())
                .fenceGate(DustBlockRegistry.BLOCK_FENCEGATE_GREEN.get())
                .wall(DustBlockRegistry.BLOCK_WALL_GREEN.get())
                .door(DustBlockRegistry.BLOCK_DOOR_GREEN.get())
                .trapdoor(DustBlockRegistry.BLOCK_TRAPDOOR_GREEN.get());


        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateSpear(DustItemRegistry.CRYSTAL_SPEAR.get());
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_MATTOCK.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        itemModels.generateTrimmableItem(DustItemRegistry.CRYSTAL_HELMET.get(), DustMaterialArmor.DUST_ARMOR_MATERIAL_KEY, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModels.generateTrimmableItem(DustItemRegistry.CRYSTAL_CHESTPLATE.get(), DustMaterialArmor.DUST_ARMOR_MATERIAL_KEY, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModels.generateTrimmableItem(DustItemRegistry.CRYSTAL_LEGGINGS.get(), DustMaterialArmor.DUST_ARMOR_MATERIAL_KEY, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModels.generateTrimmableItem(DustItemRegistry.CRYSTAL_BOOTS.get(), DustMaterialArmor.DUST_ARMOR_MATERIAL_KEY, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);

        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_HORSE_ARMOR.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

    }
}
