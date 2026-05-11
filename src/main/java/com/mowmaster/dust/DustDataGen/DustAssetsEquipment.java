package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.CrystalTools.Materials.DustMaterialArmor;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class DustAssetsEquipment implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    public DustAssetsEquipment(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    private static void bootstrap(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
        output.accept(DustMaterialArmor.DUST_ARMOR_MATERIAL_KEY, EquipmentClientInfo.builder()
                .addHumanoidLayers(Identifier.fromNamespaceAndPath(DustReferences.MODID, "crystal"), false)
                        .addLayers(EquipmentClientInfo.LayerType.HORSE_BODY,
                                new EquipmentClientInfo.Layer(Identifier.fromNamespaceAndPath(DustReferences.MODID, "crystal")))
                .build());
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> equipmentAssets = new HashMap<>();
        bootstrap((id, asset) -> {
            if (equipmentAssets.putIfAbsent(id, asset) != null) {
                throw new IllegalStateException("Tried to register equipment asset twice for id: " + id);
            }
        });
        return DataProvider.saveAll(cache, EquipmentClientInfo.CODEC, this.pathProvider::json, equipmentAssets);
    }

    @Override
    public String getName() {
        return DustReferences.MODNAME +  " Equipment Definitions";
    }
}
