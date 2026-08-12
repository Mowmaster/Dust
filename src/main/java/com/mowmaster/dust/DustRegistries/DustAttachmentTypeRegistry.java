package com.mowmaster.dust.DustRegistries;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustReferences;
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

    //Tutorial Attachment Types (Module 3: #7)
    //This could be a custom data class as well
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_MANA = ATTACHMENT_TYPES.register("dustmagic_mana",
            // 0 is the default value
            () -> AttachmentType.builder(() -> 0)//.sync(ByteBufCodecs.INT) // this auto-syncs HOWEVER, I wanna teach Networking!
                    .serialize(Codec.INT.fieldOf("dustmagic_mana")).build());
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_MAXMANA = ATTACHMENT_TYPES.register("dustmagic_maxmana",
            () -> AttachmentType.builder(() -> 20).serialize(Codec.INT.fieldOf("dustmagic_maxmana")).sync(ByteBufCodecs.INT).copyOnDeath().build());
    //Multiplier is for artifact like equipment, multipliers are additive???
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_MANA_CAPACITYMULTIPLIER = ATTACHMENT_TYPES.register("dustmagic_mana_capacitymultiplier",
            () -> AttachmentType.builder(() -> 0).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("dustmagic_mana_capacitymultiplier")).build());
    //Incremental Capacity increase, from levelups, equipped gear, etc
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_MANA_CAPACITYINCREASE = ATTACHMENT_TYPES.register("dustmagic_mana_capacityincrease",
            () -> AttachmentType.builder(() -> 0).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("dustmagic_mana_capacityincrease")).build());
    //Mana regen base rate, amount per second???
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_MANA_REGENERATION_RATE = ATTACHMENT_TYPES.register("dustmagic_mana_regeneration_rate",
            () -> AttachmentType.builder(() -> 0).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("dustmagic_mana_regeneration_rate")).build());


    //Having an affinity will reduce the dust cost and increase the dust gains for 1 or 2 elements (benefits are worse for 2 tho)
    public static final Supplier<AttachmentType<String>> DUSTMAGIC_AFFINITY =
            ATTACHMENT_TYPES.register("dustmagic_affinity", () ->
                    AttachmentType.builder(() -> "none")
                            .serialize(Codec.STRING.fieldOf("dustmagic_affinity"))
                            .sync(ByteBufCodecs.STRING_UTF8)
                            .copyOnDeath()
                            .build()
            );

    //Multiplier is for artifact like equipment, multipliers are additive???
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_ELEMENT_CAPACITYMULTIPLIER = ATTACHMENT_TYPES.register("dustmagic_element_capacitymultiplier",
            () -> AttachmentType.builder(() -> 0).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("dustmagic_element_capacitymultiplier")).build());
    //Incremental Capacity increase, from levelups, equipped storage vessles, etc
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_ELEMENT_CAPACITYINCREASE = ATTACHMENT_TYPES.register("dustmagic_element_capacityincrease",
            () -> AttachmentType.builder(() -> 0).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("dustmagic_element_capacityincrease")).build());
    //FIRE
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_ELEMENTCOUNT_FIRE = ATTACHMENT_TYPES.register("dustmagic_elementcount_fire",
            () -> AttachmentType.builder(() -> 0).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("dustmagic_elementcount_fire")).build());
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_ELEMENTMAX_FIRE = ATTACHMENT_TYPES.register("dustmagic_elementmax_fire",
            () -> AttachmentType.builder(() -> 10).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("dustmagic_elementmax_fire")).build());
    //WATER
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_ELEMENTCOUNT_WATER = ATTACHMENT_TYPES.register("dustmagic_elementcount_water",
            () -> AttachmentType.builder(() -> 0).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("dustmagic_elementcount_water")).build());
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_ELEMENTMAX_WATER = ATTACHMENT_TYPES.register("dustmagic_elementmax_water",
            () -> AttachmentType.builder(() -> 10).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("dustmagic_elementmax_water")).build());
    //EARTH
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_ELEMENTCOUNT_EARTH = ATTACHMENT_TYPES.register("dustmagic_elementcount_earth",
            () -> AttachmentType.builder(() -> 0).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("dustmagic_elementcount_earth")).build());
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_ELEMENTMAX_EARTH = ATTACHMENT_TYPES.register("dustmagic_elementmax_earth",
            () -> AttachmentType.builder(() -> 10).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("dustmagic_elementmax_earth")).build());
    //CHAOS
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_ELEMENTCOUNT_CHAOS = ATTACHMENT_TYPES.register("dustmagic_elementcount_chaos",
            () -> AttachmentType.builder(() -> 0).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("dustmagic_elementcount_chaos")).build());
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_ELEMENTMAX_CHAOS = ATTACHMENT_TYPES.register("dustmagic_elementmax_chaos",
            () -> AttachmentType.builder(() -> 10).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("dustmagic_elementmax_chaos")).build());
    //ORDER
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_ELEMENTCOUNT_ORDER = ATTACHMENT_TYPES.register("dustmagic_elementcount_order",
            () -> AttachmentType.builder(() -> 0).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("dustmagic_elementcount_order")).build());
    public static final Supplier<AttachmentType<Integer>> DUSTMAGIC_ELEMENTMAX_ORDER = ATTACHMENT_TYPES.register("dustmagic_elementmax_order",
            () -> AttachmentType.builder(() -> 10).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("dustmagic_elementmax_order")).build());

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
