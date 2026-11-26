package com.mowmaster.dust.Features.Armor;

import com.google.common.collect.Maps;
import com.mowmaster.dust.DustDataGen.DustTags;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

// Registers the elemental armor sets' pieces (ElementalArmorSet): <set>_helmet, _chestplate, _leggings, _boots. Each set
// is its own armour material, as strong as netherite (and as hard to burn), repaired with its element's crystal blocks
// (tag dust:repairs/<set>), with its own equipment asset (dust:<set>; the model and texture are Client/ElementalArmorModels').
public final class ElementalArmor
{
    public static final List<ArmorType> PIECES = List.of(ArmorType.HELMET, ArmorType.CHESTPLATE, ArmorType.LEGGINGS, ArmorType.BOOTS);
    private static final ResourceKey<? extends Registry<EquipmentAsset>> EQUIPMENT_ASSETS = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));

    public static final Map<ElementalArmorSet, ArmorMaterial> MATERIALS;
    public static final Map<ElementalArmorSet, Map<ArmorType, DeferredItem<ElementalArmorItem>>> ITEMS;

    static {
        Map<ElementalArmorSet, ArmorMaterial> materials = new EnumMap<>(ElementalArmorSet.class);
        Map<ElementalArmorSet, Map<ArmorType, DeferredItem<ElementalArmorItem>>> items = new EnumMap<>(ElementalArmorSet.class);
        for (ElementalArmorSet set : ElementalArmorSet.values()) {
            ArmorMaterial material = new ArmorMaterial(37, Maps.newEnumMap(Map.of(ArmorType.BOOTS, 3, ArmorType.LEGGINGS, 6, ArmorType.CHESTPLATE, 8,
                    ArmorType.HELMET, 3, ArmorType.BODY, 11)), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F,
                    DustTags.Items.armorRepair(set), assetKey(set));
            materials.put(set, material);
            Map<ArmorType, DeferredItem<ElementalArmorItem>> pieces = new EnumMap<>(ArmorType.class);
            for (ArmorType type : PIECES) {
                pieces.put(type, DustItemRegistry.DUSTITEMS.registerItem(set.id + "_" + type.getSerializedName(),
                        properties -> new ElementalArmorItem(set, type, properties.humanoidArmor(material, type).rarity(Rarity.EPIC).fireResistant())));
            }
            items.put(set, Collections.unmodifiableMap(pieces));
        }
        MATERIALS = Collections.unmodifiableMap(materials);
        ITEMS = Collections.unmodifiableMap(items);
    }

    private ElementalArmor() {}

    // Loads the class (and so registers everything above) from the mod constructor (Dust)
    public static void init() {}

    public static ResourceKey<EquipmentAsset> assetKey(ElementalArmorSet set)
    {
        return ResourceKey.create(EQUIPMENT_ASSETS, Identifier.fromNamespaceAndPath(DustReferences.MODID, set.id));
    }

    public static ElementalArmorItem item(ElementalArmorSet set, ArmorType type)
    {
        return ITEMS.get(set).get(type).get();
    }
}
