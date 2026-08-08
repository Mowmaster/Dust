package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.baseOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.chaosDust.ChaosOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.earthDust.EarthOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.fireDust.FireOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.orderDust.OrderOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.waterDust.WaterOrbEntity;
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

    public static final DeferredHolder<EntityType<?>, EntityType<FireOrbEntity>> FIRE_ORB =
            ENTITY_TYPES.register("fire_orb", () ->
                    EntityType.Builder.of(FireOrbEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(BASE_ORB_KEY));
    public static final DeferredHolder<EntityType<?>, EntityType<EarthOrbEntity>> EARTH_ORB =
            ENTITY_TYPES.register("earth_orb", () ->
                    EntityType.Builder.of(EarthOrbEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(BASE_ORB_KEY));
    public static final DeferredHolder<EntityType<?>,   EntityType<WaterOrbEntity>> WATER_ORB =
            ENTITY_TYPES.register("water_orb", () ->
                    EntityType.Builder.of(WaterOrbEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(BASE_ORB_KEY));
    public static final DeferredHolder<EntityType<?>, EntityType<OrderOrbEntity>> ORDER_ORB =
            ENTITY_TYPES.register("order_orb", () ->
                    EntityType.Builder.of(OrderOrbEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(BASE_ORB_KEY));
    public static final DeferredHolder<EntityType<?>, EntityType<ChaosOrbEntity>> CHAOS_ORB =
            ENTITY_TYPES.register("chaos_orb", () ->
                    EntityType.Builder.of(ChaosOrbEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(BASE_ORB_KEY));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
