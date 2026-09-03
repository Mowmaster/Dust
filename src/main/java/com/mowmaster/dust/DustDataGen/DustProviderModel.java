package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.CrystalTools.Materials.DustMaterialArmor;
import com.mowmaster.dust.Features.DustyDelights.Block.BushFalloldBerryBlock;
import com.mowmaster.dust.Features.DustyDelights.Block.CropBlockLettuce;
import com.mowmaster.dust.Features.FocusedBooks.DataGenCustomItemModels;
import com.mowmaster.dust.Features.Pedestals.RegistryPedestalBlock;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.numeric.Count;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.List;

public class DustProviderModel extends ModelProvider {
    public DustProviderModel(PackOutput output) {
        super(output, DustReferences.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        itemModels.generateFlatItem(DustItemRegistry.CORNBREAD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.FOOD_LETTUCE.get(), ModelTemplates.FLAT_ITEM);


        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_CHISEL_IRON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.CRYSTAL_HALFSAW_IRON.get(), ModelTemplates.FLAT_ITEM);


        itemModels.generateFlatItem(DustItemRegistry.SPELL_WAND_FIRE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DustItemRegistry.SPELL_WAND_EARTH.get(), ModelTemplates.FLAT_ITEM);

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

        generateFocusedBookModels(itemModels.itemModelOutput, DustItemRegistry.FOCUSED_BOOK.get());

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

        blockModels.createCropBlock(DustBlockRegistry.CROP_LETTUCE.get(), CropBlockLettuce.AGE, 0, 1, 2, 3);

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

        itemModels.declareCustomModelItem(DustItemRegistry.FOCUSED_BOOK_BASE.get());


        blockModels.createPlantWithDefaultItem(
                DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL.get(),
                DustBlockRegistry.BLOCK_POTTEDFLOWER_WINDWHEEL.get(),
                BlockModelGenerators.PlantType.TINTED
        );

        blockModels.createTintedLeaves(DustBlockRegistry.CRYSTAL_STONE_RED.get(), TexturedModel.LEAVES, 16711680);
        blockModels.createTintedLeaves(DustBlockRegistry.CRYSTAL_STONE_GREEN.get(), TexturedModel.LEAVES, 65280);
        blockModels.createTintedLeaves(DustBlockRegistry.CRYSTAL_STONE_BLUE.get(), TexturedModel.LEAVES, 255);
        blockModels.createTintedLeaves(DustBlockRegistry.CRYSTAL_STONE_WHITE.get(), TexturedModel.LEAVES, 16777215);
        blockModels.createTintedLeaves(DustBlockRegistry.CRYSTAL_STONE_BLACK.get(), TexturedModel.LEAVES, 2763306);

        blockModels.createCropBlock(DustBlockRegistry.BLOCK_FALLOLDBERRY_BUSH.get(), BushFalloldBerryBlock.AGE, 0, 1, 2, 3);


        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(RegistryPedestalBlock.PEDESTAL_BLOCK.get(),
                BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/pedestal_block"))));
    }


    public void generateFocusedBookModels(ItemModelOutput itemModelOutput, Item item)
    {
        ItemModel.Unbaked focusedbookBase = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item));
        ItemModel.Unbaked anchor = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_anchor"));
        ItemModel.Unbaked animal = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_animal"));
        ItemModel.Unbaked armor = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_armor"));
        ItemModel.Unbaked bow = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_bow"));
        ItemModel.Unbaked breakable = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_breakable"));
        ItemModel.Unbaked chest = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_chest"));
        ItemModel.Unbaked crossbow = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_crossbow"));
        ItemModel.Unbaked base = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_default"));
        ItemModel.Unbaked digger = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "digger"));
        ItemModel.Unbaked feet = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_feet"));
        ItemModel.Unbaked fish = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_fish"));
        ItemModel.Unbaked gun = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_gun"));
        ItemModel.Unbaked hammer = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_hammer"));
        ItemModel.Unbaked helm = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_helm"));
        ItemModel.Unbaked hoe = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_hoe"));
        ItemModel.Unbaked knife = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_knife"));
        ItemModel.Unbaked legs = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_legs"));
        ItemModel.Unbaked pedestal = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_pedestal"));
        ItemModel.Unbaked trident = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_trident"));
        ItemModel.Unbaked weapon = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_weapon"));
        ItemModel.Unbaked wearable = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_wearable"));
        itemModelOutput.accept(
                item,
                ItemModelUtils.conditional(
                        ItemModelUtils.hasComponent(DustComponentDataRegistry.FOCUSEDBOOK_BOOKCOVER_TYPE.get()),
                        ItemModelUtils.rangeSelect(new Count(false),
                                List.of(
                                        ItemModelUtils.override(anchor, 1),
                                        ItemModelUtils.override(animal, 2),
                                        ItemModelUtils.override(armor, 3),
                                        ItemModelUtils.override(bow, 4),
                                        ItemModelUtils.override(breakable, 5),
                                        ItemModelUtils.override(chest, 6),
                                        ItemModelUtils.override(crossbow, 7),
                                        ItemModelUtils.override(base, 8),
                                        ItemModelUtils.override(digger, 9),
                                        ItemModelUtils.override(feet, 10),
                                        ItemModelUtils.override(fish, 11),
                                        ItemModelUtils.override(gun, 12),
                                        ItemModelUtils.override(hammer, 13),
                                        ItemModelUtils.override(helm, 14),
                                        ItemModelUtils.override(hoe, 15),
                                        ItemModelUtils.override(knife, 16),
                                        ItemModelUtils.override(legs, 17),
                                        ItemModelUtils.override(pedestal, 18),
                                        ItemModelUtils.override(trident, 19),
                                        ItemModelUtils.override(weapon, 20),
                                        ItemModelUtils.override(wearable, 21))), focusedbookBase));
    }
}