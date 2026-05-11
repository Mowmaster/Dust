package com.mowmaster.dust.Features.CrystalTools.Materials;

import com.google.common.collect.Maps;
import com.mowmaster.dust.DustDataGen.DustTags;
import com.mowmaster.dust.DustReferences;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.Map;

public class DustMaterialArmor {
    private static ResourceKey<? extends Registry<EquipmentAsset>> ROOT_ID =
            ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));

    public static ResourceKey<EquipmentAsset> DUST_ARMOR_MATERIAL_KEY = ResourceKey.create(ROOT_ID,
            Identifier.fromNamespaceAndPath(DustReferences.MODID, "crystal_armor_material"));

    public static final ArmorMaterial CRYSTAL_ARMOR_MATERIAL = new ArmorMaterial(35,
            makeDefense(5, 7, 9, 5, 11), 27, SoundEvents.ARMOR_EQUIP_DIAMOND,
            2f, 0.1f, DustTags.Items.CRYSTAL_REPAIRABLES, DUST_ARMOR_MATERIAL_KEY);


    private static Map<ArmorType, Integer> makeDefense(int boots, int legs, int chest, int helm, int body) {
        return Maps.newEnumMap(
                Map.of(ArmorType.BOOTS, boots, ArmorType.LEGGINGS, legs, ArmorType.CHESTPLATE, chest, ArmorType.HELMET, helm, ArmorType.BODY, body)
        );
    }
}
