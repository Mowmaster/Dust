package com.mowmaster.dust.DustRegistries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustReferences;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class DustParticleRegistry
{
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, DustReferences.MODID);

    public static final Supplier<SimpleParticleType> DUSTPARTICLES_SPELLS_FIRE = PARTICLE_TYPES.register("dustparticles_spells_fire",
            () -> new SimpleParticleType(true));

    // The Still Top's smoke, tinted (white while boiling, an element's colour as it leaves): Features/Apparatus/still/StillSmokeParticle
    public static final Supplier<ParticleType<net.minecraft.core.particles.ColorParticleOption>> STILL_SMOKE = PARTICLE_TYPES.register("still_smoke",
            () -> new ParticleType<net.minecraft.core.particles.ColorParticleOption>(false) {
                @Override
                public MapCodec<net.minecraft.core.particles.ColorParticleOption> codec()
                {
                    return net.minecraft.core.particles.ColorParticleOption.codec(this);
                }

                @Override
                public net.minecraft.network.codec.StreamCodec<? super net.minecraft.network.RegistryFriendlyByteBuf, net.minecraft.core.particles.ColorParticleOption> streamCodec()
                {
                    return net.minecraft.core.particles.ColorParticleOption.streamCodec(this);
                }
            });

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
