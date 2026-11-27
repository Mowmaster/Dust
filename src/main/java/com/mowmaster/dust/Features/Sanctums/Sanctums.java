package com.mowmaster.dust.Features.Sanctums;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

// Registers the sanctums' blocks and keys (SanctumStructure places the sanctums themselves, from templates: SanctumPiece):
//   dust:arcane_spawner     the trial-spawner-like spawner (with an item, for building your own arena or a template)
//   dust:arcane_vault       the trial-vault-like vault, opened with a Vault Key of its element (with an item, as the
//                           spawner's)
//   dust:arcane_portal      the portal sheet into a labyrinth (and out of one); no item
//   dust:sanctum_keystone   the stone under a sanctum's portal that keeps its records and opens it (with an item, for
//                           building sanctum templates: placed by hand it does nothing)
//   dust:<set>_vault_key    a key to the vaults of one element (VaultKeyItem), from its Arcane Spawners
// None of the blocks drop anything; the portal and keystone can't be broken at all. Pistons move none of them
// (unbreakable blocks and blocks with block entities stay put).
public final class Sanctums
{
    public static final DeferredBlock<ArcaneSpawnerBlock> ARCANE_SPAWNER = DustBlockRegistry.DUSTBLOCKS.registerBlock("arcane_spawner",
            properties -> new ArcaneSpawnerBlock(properties.mapColor(MapColor.STONE).sound(SoundType.TRIAL_SPAWNER).strength(50.0F, 1200.0F)
                    .noLootTable().noOcclusion()
                    .lightLevel(state -> state.getValue(ArcaneSpawnerBlock.PHASE) == ArcaneSpawnerBlock.Phase.ACTIVE ? 12 : 4)));
    public static final DeferredItem<BlockItem> ARCANE_SPAWNER_ITEM = DustItemRegistry.DUSTITEMS.registerItem("arcane_spawner",
            properties -> new BlockItem(ARCANE_SPAWNER.get(), properties.useBlockDescriptionPrefix().rarity(Rarity.RARE)));
    public static final DeferredBlock<ArcaneVaultBlock> ARCANE_VAULT = DustBlockRegistry.DUSTBLOCKS.registerBlock("arcane_vault",
            properties -> new ArcaneVaultBlock(properties.mapColor(MapColor.STONE).sound(SoundType.VAULT).strength(50.0F, 1200.0F)
                    .noLootTable().noOcclusion()
                    .lightLevel(state -> state.getValue(ArcaneVaultBlock.STATE).lit() ? 6 : 2)));
    public static final DeferredItem<BlockItem> ARCANE_VAULT_ITEM = DustItemRegistry.DUSTITEMS.registerItem("arcane_vault",
            properties -> new BlockItem(ARCANE_VAULT.get(), properties.useBlockDescriptionPrefix().rarity(Rarity.RARE)));
    public static final DeferredBlock<ArcanePortalBlock> ARCANE_PORTAL = DustBlockRegistry.DUSTBLOCKS.registerBlock("arcane_portal",
            properties -> new ArcanePortalBlock(properties.mapColor(MapColor.NONE).sound(SoundType.GLASS).strength(-1.0F, 3600000.0F)
                    .noLootTable().noCollision().noOcclusion().lightLevel(state -> 11)));
    public static final DeferredBlock<SanctumKeystoneBlock> SANCTUM_KEYSTONE = DustBlockRegistry.DUSTBLOCKS.registerBlock("sanctum_keystone",
            properties -> new SanctumKeystoneBlock(properties.mapColor(MapColor.COLOR_BLACK).sound(SoundType.DEEPSLATE_TILES).strength(-1.0F, 3600000.0F)
                    .noLootTable().lightLevel(state -> 7)));
    public static final DeferredItem<BlockItem> SANCTUM_KEYSTONE_ITEM = DustItemRegistry.DUSTITEMS.registerItem("sanctum_keystone",
            properties -> new BlockItem(SANCTUM_KEYSTONE.get(), properties.useBlockDescriptionPrefix().rarity(Rarity.EPIC)));

    public static final Supplier<BlockEntityType<ArcaneSpawnerBlockEntity>> ARCANE_SPAWNER_ENTITY = DustBlockEntityRegistry.BLOCK_ENTITIES.register("arcane_spawner",
            () -> new BlockEntityType<>(ArcaneSpawnerBlockEntity::new, Set.of(ARCANE_SPAWNER.get())));
    public static final Supplier<BlockEntityType<ArcaneVaultBlockEntity>> ARCANE_VAULT_ENTITY = DustBlockEntityRegistry.BLOCK_ENTITIES.register("arcane_vault",
            () -> new BlockEntityType<>(ArcaneVaultBlockEntity::new, Set.of(ARCANE_VAULT.get())));
    public static final Supplier<BlockEntityType<SanctumKeystoneBlockEntity>> KEYSTONE_ENTITY = DustBlockEntityRegistry.BLOCK_ENTITIES.register("sanctum_keystone",
            () -> new BlockEntityType<>(SanctumKeystoneBlockEntity::new, Set.of(SANCTUM_KEYSTONE.get())));

    public static final Map<ElementalArmorSet, DeferredItem<VaultKeyItem>> KEYS;

    static {
        Map<ElementalArmorSet, DeferredItem<VaultKeyItem>> keys = new EnumMap<>(ElementalArmorSet.class);
        for (ElementalArmorSet set : ElementalArmorSet.values()) {
            keys.put(set, DustItemRegistry.DUSTITEMS.registerItem(set.id + "_vault_key", properties -> new VaultKeyItem(set, properties.rarity(Rarity.UNCOMMON))));
        }
        KEYS = Collections.unmodifiableMap(keys);
    }

    private Sanctums() {}

    // Loads the class (and so registers everything above) from the mod constructor (Dust)
    public static void init() {}
}
