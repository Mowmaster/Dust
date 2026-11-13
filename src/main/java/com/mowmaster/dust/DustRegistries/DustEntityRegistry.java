package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.Features.Spells.shapes.SpellProjectileEntity;
import com.mowmaster.dust.Features.Spells.entities.AnimatedBlockEntity;
import com.mowmaster.dust.Features.Spells.entities.FloatingBlockEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity;

import com.mowmaster.dust.DustReferences;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DustEntityRegistry {
    public static final DeferredRegister.Entities ENTITY_TYPES =
            DeferredRegister.createEntities(DustReferences.MODID);

    // One orb type for every element: the element is the orb's synced data (ElementOrbEntity)
    public static final DeferredHolder<EntityType<?>, EntityType<ElementOrbEntity>> ELEMENT_ORB = registerOrb("element_orb", ElementOrbEntity::new);
    // A spell in flight (Features/Spells/shapes): small, tracked from a distance like an arrow
    public static final DeferredHolder<EntityType<?>, EntityType<SpellProjectileEntity>> SPELL_PROJECTILE = ENTITY_TYPES.registerEntityType("spell_projectile",
            SpellProjectileEntity::new, MobCategory.MISC, (factory, category) -> EntityType.Builder.of(factory, category)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10));

    // Blocks brought to life by spells (Features/Spells/entities): floating (Flight) and scurrying (Creature)
    public static final DeferredHolder<EntityType<?>, EntityType<FloatingBlockEntity>> FLOATING_BLOCK = ENTITY_TYPES.registerEntityType("floating_block",
            FloatingBlockEntity::new, MobCategory.MISC, (factory, category) -> EntityType.Builder.of(factory, category)
                    .sized(0.98F, 0.98F).clientTrackingRange(8).updateInterval(2));
    public static final DeferredHolder<EntityType<?>, EntityType<AnimatedBlockEntity>> ANIMATED_BLOCK = ENTITY_TYPES.registerEntityType("animated_block",
            AnimatedBlockEntity::new, MobCategory.MISC, (factory, category) -> EntityType.Builder.of(factory, category)
                    .sized(0.9F, 0.9F).clientTrackingRange(8));

    // registerEntityType builds the EntityType with its own registry key (dust:element_orb). Before, every orb was
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
