package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.chaosDust.ChaosOrbEntityBase;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.earthDust.EarthOrbEntityBase;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.fireDust.FireOrbEntityBase;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.orderDust.OrderOrbEntityBase;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.waterDust.WaterOrbEntityBase;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DustEntityRegistry {
    public static final DeferredRegister.Entities ENTITY_TYPES =
            DeferredRegister.createEntities(DustReferences.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<FireOrbEntityBase>> FIRE_ORB = registerOrb("fire_orb", FireOrbEntityBase::new);
    public static final DeferredHolder<EntityType<?>, EntityType<EarthOrbEntityBase>> EARTH_ORB = registerOrb("earth_orb", EarthOrbEntityBase::new);
    public static final DeferredHolder<EntityType<?>, EntityType<WaterOrbEntityBase>> WATER_ORB = registerOrb("water_orb", WaterOrbEntityBase::new);
    public static final DeferredHolder<EntityType<?>, EntityType<OrderOrbEntityBase>> ORDER_ORB = registerOrb("order_orb", OrderOrbEntityBase::new);
    public static final DeferredHolder<EntityType<?>, EntityType<ChaosOrbEntityBase>> CHAOS_ORB = registerOrb("chaos_orb", ChaosOrbEntityBase::new);

    // registerEntityType builds the EntityType with its own registry key (dust:fire_orb, ...). Before, every orb was
    // built with the same made-up key "dust:base_orb_key", so they all shared one description id / loot table id.
    // Tracking range and update interval match vanilla's experience orb.
    private static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> registerOrb(String name, EntityType.EntityFactory<E> factory) {
        return ENTITY_TYPES.registerEntityType(name, factory, MobCategory.MISC,
                (entityFactory, category) -> EntityType.Builder.of(entityFactory, category)
                        .sized(0.5F, 0.5F)
                        .clientTrackingRange(6)
                        .updateInterval(20));
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
