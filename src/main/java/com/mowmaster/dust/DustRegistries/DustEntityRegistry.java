package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustEntities.baseOrbEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DustEntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.createEntities(DustReferences.MODID);

    public static final ResourceKey<EntityType<?>> BASE_ORB_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(DustReferences.MODID, "base_orb_key"));

    public static final DeferredHolder<EntityType<?>, EntityType<baseOrbEntity>> FIRE_ORB =
            ENTITY_TYPES.register("fire_orb", () ->
                    EntityType.Builder.of(baseOrbEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(BASE_ORB_KEY));
    public static final DeferredHolder<EntityType<?>, EntityType<baseOrbEntity>> EARTH_ORB =
            ENTITY_TYPES.register("earth_orb", () ->
                    EntityType.Builder.of(baseOrbEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(BASE_ORB_KEY));
    public static final DeferredHolder<EntityType<?>,   EntityType<baseOrbEntity>> WATER_ORB =
            ENTITY_TYPES.register("water_orb", () ->
                    EntityType.Builder.of(baseOrbEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(BASE_ORB_KEY));
    public static final DeferredHolder<EntityType<?>, EntityType<baseOrbEntity>> ORDER_ORB =
            ENTITY_TYPES.register("order_orb", () ->
                    EntityType.Builder.of(baseOrbEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(BASE_ORB_KEY));
    public static final DeferredHolder<EntityType<?>, EntityType<baseOrbEntity>> CHAOS_ORB =
            ENTITY_TYPES.register("chaos_orb", () ->
                    EntityType.Builder.of(baseOrbEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(BASE_ORB_KEY));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
