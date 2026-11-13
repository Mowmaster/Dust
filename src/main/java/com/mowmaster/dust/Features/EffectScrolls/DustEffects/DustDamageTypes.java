package com.mowmaster.dust.Features.EffectScrolls.DustEffects;

import com.mowmaster.dust.DustReferences;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class DustDamageTypes {

    public static final ResourceKey<DamageType> SCORCH_KEY = ResourceKey.create(Registries.DAMAGE_TYPE,
            Identifier.fromNamespaceAndPath(DustReferences.MODID, "damagetype_scorch"));
    public static final ResourceKey<DamageType> IGNITION_KEY = ResourceKey.create(Registries.DAMAGE_TYPE,
            Identifier.fromNamespaceAndPath(DustReferences.MODID, "damagetype_ignition"));

    // Spells (Features/Spells): kinetic is a physical blow from magic (Stone, and later Crystal and Gem)
    public static final ResourceKey<DamageType> KINETIC_KEY = ResourceKey.create(Registries.DAMAGE_TYPE,
            Identifier.fromNamespaceAndPath(DustReferences.MODID, "damagetype_kinetic"));

    public static void bootstrap(BootstrapContext<DamageType> context)
    {
        context.register(KINETIC_KEY, new DamageType("kinetic", 0.1f, DamageEffects.HURT));
        //exhaustion removes hunger
        context.register(SCORCH_KEY, new DamageType("scorch", 0.1f, DamageEffects.BURNING));
        context.register(IGNITION_KEY, new DamageType("ignition", 0.0f, DamageEffects.HURT));
    }


    public static DamageSource create(Level level, ResourceKey<DamageType> key)
    {
        return new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key));
    }

    // From an attacker (credited with the kill; null for none)
    public static DamageSource create(Level level, ResourceKey<DamageType> key, net.minecraft.world.entity.@org.jspecify.annotations.Nullable Entity attacker)
    {
        return new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key), attacker);
    }
}
