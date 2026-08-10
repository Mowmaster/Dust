package com.mowmaster.dust.DustRegistries;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicData;
import com.mowmaster.dust.Features.EffectScrolls.Research.DustResearchData;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class DustAttachmentTypeRegistry
{
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, DustReferences.MODID);

    public static final Supplier<AttachmentType<Boolean>> DUSTMAGIC_UNLOCKED =
            ATTACHMENT_TYPES.register("dustmagic_unlocked", () ->
                    AttachmentType.builder(() -> Boolean.FALSE)
                            .serialize(Codec.BOOL.fieldOf("dustmagic_unlocked"))
                            .sync(ByteBufCodecs.BOOL)
                            .copyOnDeath()
                            .build()
            );

    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_MAXMANA = ATTACHMENT_TYPES.register("dustmagic_maxmana",
            () -> AttachmentType.builder(() -> 20).serialize(Codec.INT.fieldOf("dustmagic_maxmana")).sync(ByteBufCodecs.INT).copyOnDeath().build());
    //Tutorial Attachment Types (Module 3: #7)
    //This could be a custom data class as well
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_MANA = ATTACHMENT_TYPES.register("dustmagic_mana",
            // 0 is the default value
            () -> AttachmentType.builder(() -> 0)//.sync(ByteBufCodecs.INT) // this auto-syncs HOWEVER, I wanna teach Networking!
                    .serialize(Codec.INT.fieldOf("dustmagic_mana")).build());

    public static final Supplier<AttachmentType<DustMagicData>> DUSTMAGIC_ELEMENTS =
            ATTACHMENT_TYPES.register("dustmagic_elements", () ->
                    AttachmentType.builder(DustMagicData::new)
                            .serialize(DustMagicData.CODEC.fieldOf("dustmagic_elements"))
                            .sync(DustMagicData.STREAM_CODEC)
                            .copyOnDeath()
                            .build()
            );

    public static final Supplier<AttachmentType<DustResearchData>> DUSTMAGIC_RESEARCHED_ITEMS =
            ATTACHMENT_TYPES.register("dustmagic_researched_items",
            () -> AttachmentType.builder(DustResearchData::new)
                    .serialize(DustResearchData.CODEC.fieldOf("dustmagic_researched_items"))
                    .sync(DustResearchData.STREAM_CODEC)
                    .copyOnDeath()
                    .build());


    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
