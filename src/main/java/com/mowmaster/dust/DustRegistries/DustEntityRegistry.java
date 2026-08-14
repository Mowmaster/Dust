package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.chaosDust.ChaosOrbEntityBase;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.earthDust.EarthOrbEntityBase;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.fireDust.FireOrbEntityBase;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.orderDust.OrderOrbEntityBase;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.waterDust.WaterOrbEntityBase;
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

    public static final DeferredHolder<EntityType<?>, EntityType<FireOrbEntityBase>> FIRE_ORB =
            ENTITY_TYPES.register("fire_orb", () ->
                    EntityType.Builder.of(FireOrbEntityBase::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(BASE_ORB_KEY));
    public static final DeferredHolder<EntityType<?>, EntityType<EarthOrbEntityBase>> EARTH_ORB =
            ENTITY_TYPES.register("earth_orb", () ->
                    EntityType.Builder.of(EarthOrbEntityBase::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(BASE_ORB_KEY));
    public static final DeferredHolder<EntityType<?>,   EntityType<WaterOrbEntityBase>> WATER_ORB =
            ENTITY_TYPES.register("water_orb", () ->
                    EntityType.Builder.of(WaterOrbEntityBase::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(BASE_ORB_KEY));
    public static final DeferredHolder<EntityType<?>, EntityType<OrderOrbEntityBase>> ORDER_ORB =
            ENTITY_TYPES.register("order_orb", () ->
                    EntityType.Builder.of(OrderOrbEntityBase::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(BASE_ORB_KEY));
    public static final DeferredHolder<EntityType<?>, EntityType<ChaosOrbEntityBase>> CHAOS_ORB =
            ENTITY_TYPES.register("chaos_orb", () ->
                    EntityType.Builder.of(ChaosOrbEntityBase::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(BASE_ORB_KEY));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
