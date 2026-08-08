package com.mowmaster.dust.DustRegistries;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustReferences;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class DustAttachmentTypeRegistry
{
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, DustReferences.MODID);

    //This could be a custom data class as well
    public static final Supplier<AttachmentType<Integer>> DUST_AURA = ATTACHMENT_TYPES.register("aura",
            // 0 is the default value
            () -> AttachmentType.builder(() -> 0)//.sync(ByteBufCodecs.INT) // this auto-syncs HOWEVER, I wanna teach Networking!
                    .serialize(Codec.INT.fieldOf("aura")).build());

    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_FIRE = ATTACHMENT_TYPES.register("dustmagic_fire",
            () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("dustmagic_fire")).build());
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_WATER = ATTACHMENT_TYPES.register("dustmagic_water",
            () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("dustmagic_water")).build());
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_EARTH = ATTACHMENT_TYPES.register("dustmagic_earth",
            () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("dustmagic_earth")).build());
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_CHAOS = ATTACHMENT_TYPES.register("dustmagic_chaos",
            () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("dustmagic_chaos")).build());
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_ORDER = ATTACHMENT_TYPES.register("dustmagic_order",
            () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("dustmagic_order")).build());


    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
