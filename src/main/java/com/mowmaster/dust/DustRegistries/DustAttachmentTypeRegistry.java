package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.Features.Magic.ManaTimers;
import com.mowmaster.dust.Features.Magic.PlayerMagic;
import com.mowmaster.dust.Features.Spells.SpellKnowledge;
import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.TranscribedStones;
import com.mowmaster.dust.Features.Research.ResearchData;
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


    // Ancient glyphs the player has learned: bit 0 = a ... bit 25 = z (see AncientLanguage).
    // Research knowledge, so it survives death. Synced ONLY to the player it belongs to (holder == to): other players'
    // clients never get it, and each client reveals ancient text using its own player's mask.
    public static final Supplier<AttachmentType<Integer>> ANCIENT_GLYPHS = ATTACHMENT_TYPES.register("ancient_glyphs",
            () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT.fieldOf("ancient_glyphs"))
                    .sync((holder, to) -> holder == to, ByteBufCodecs.VAR_INT)
                    .copyOnDeath()
                    .build());

    // Glyph stones this player has already transcribed with Parchment (one transcription per stone per player).
    // Synced only to its player, whose client draws those stones blank (see TranscribedStones).
    public static final Supplier<AttachmentType<TranscribedStones>> TRANSCRIBED_STONES = ATTACHMENT_TYPES.register("transcribed_stones",
            () -> AttachmentType.builder(() -> TranscribedStones.EMPTY)
                    .serialize(TranscribedStones.CODEC.fieldOf("transcribed_stones"))
                    .sync(TranscribedStones.SYNC)
                    .copyOnDeath()
                    .build());

    // Everything the player has researched (Features/Research). Survives death; synced only to its player.
    public static final Supplier<AttachmentType<ResearchData>> RESEARCH = ATTACHMENT_TYPES.register("research",
            () -> AttachmentType.builder(() -> ResearchData.EMPTY)
                    .serialize(ResearchData.CODEC.fieldOf("research"))
                    .sync((holder, to) -> holder == to, ResearchData.STREAM_CODEC)
                    .copyOnDeath()
                    .build());

    // A player's mana, dust store and attunements (Features/Magic/PlayerMagic). Survives death; synced to its player.
    public static final Supplier<AttachmentType<PlayerMagic>> PLAYER_MAGIC = ATTACHMENT_TYPES.register("player_magic",
            () -> AttachmentType.builder(() -> PlayerMagic.DEFAULT)
                    .serialize(PlayerMagic.CODEC.fieldOf("magic"))
                    .sync((holder, to) -> holder == to, PlayerMagic.STREAM_CODEC)
                    .copyOnDeath()
                    .build());
    // Its mana timers (Features/Magic/ManaTimers): saved, not synced
    public static final Supplier<AttachmentType<ManaTimers>> MANA_TIMERS = ATTACHMENT_TYPES.register("mana_timers",
            () -> AttachmentType.builder(() -> ManaTimers.ZERO)
                    .serialize(ManaTimers.CODEC.fieldOf("timers"))
                    .copyOnDeath()
                    .build());

    // What the player has learned about spells: each element's effects seen, and the shapes they can use
    // (Features/Spells/SpellKnowledge). Survives death; synced to its player (the book reads it).
    public static final Supplier<AttachmentType<SpellKnowledge>> SPELL_KNOWLEDGE = ATTACHMENT_TYPES.register("spell_knowledge",
            () -> AttachmentType.builder(() -> SpellKnowledge.EMPTY)
                    .serialize(SpellKnowledge.CODEC.fieldOf("knowledge"))
                    .sync((holder, to) -> holder == to, SpellKnowledge.STREAM_CODEC)
                    .copyOnDeath()
                    .build());

    // Where a player's Warp spells send things (Features/Spells/effects/UtilityParts): a place in some dimension, or none
    public static final Supplier<AttachmentType<java.util.Optional<net.minecraft.core.GlobalPos>>> WARP_MARK = ATTACHMENT_TYPES.register("warp_mark",
            () -> AttachmentType.builder(() -> java.util.Optional.<net.minecraft.core.GlobalPos>empty())
                    .serialize(net.minecraft.core.GlobalPos.CODEC.optionalFieldOf("pos"))
                    .copyOnDeath()
                    .build());

    // A chunk's warded blocks (Armor's block effect: Features/Spells/blocks/Warding)
    public static final Supplier<AttachmentType<com.mowmaster.dust.Features.Spells.blocks.Warding.Wards>> WARDS = ATTACHMENT_TYPES.register("wards",
            () -> AttachmentType.builder(() -> com.mowmaster.dust.Features.Spells.blocks.Warding.Wards.NONE)
                    .serialize(com.mowmaster.dust.Features.Spells.blocks.Warding.Wards.CODEC.fieldOf("wards"))
                    .build());

    // Soul (Features/Spells/soul). A player's soul anchor: saved, and copied on death (that's when it's used)
    public static final Supplier<AttachmentType<com.mowmaster.dust.Features.Spells.soul.SoulAnchor>> SOUL_ANCHOR = ATTACHMENT_TYPES.register("soul_anchor",
            () -> AttachmentType.builder(() -> com.mowmaster.dust.Features.Spells.soul.SoulAnchor.NONE)
                    .serialize(com.mowmaster.dust.Features.Spells.soul.SoulAnchor.CODEC.fieldOf("anchor"))
                    .copyOnDeath()
                    .build());
    // What ties a ghost or a soulless body to its soul: saved, so it holds across a reload
    public static final Supplier<AttachmentType<com.mowmaster.dust.Features.Spells.soul.SoulTether>> SOUL_TETHER = ATTACHMENT_TYPES.register("soul_tether",
            () -> AttachmentType.builder(() -> com.mowmaster.dust.Features.Spells.soul.SoulTether.NONE)
                    .serialize(com.mowmaster.dust.Features.Spells.soul.SoulTether.CODEC.fieldOf("tether"))
                    .build());

    // Magic's block effect: amplified blocks in a chunk (Features/Magic/AuraAmplifiers), saved with the chunk
    public static final Supplier<AttachmentType<com.mowmaster.dust.Features.Magic.AuraAmplifiers>> AURA_AMPLIFIERS = ATTACHMENT_TYPES.register("aura_amplifiers",
            () -> AttachmentType.builder(() -> com.mowmaster.dust.Features.Magic.AuraAmplifiers.NONE)
                    .serialize(com.mowmaster.dust.Features.Magic.AuraAmplifiers.CODEC.fieldOf("amplifiers"))
                    .build());

    // Life: a pet's ghost (Features/Spells/effects/PetGhosts), the game time it fades for good. Saved with the pet, and
    // synced to everyone who can see it, so their clients draw it see-through (Client/GhostRendering)
    public static final Supplier<AttachmentType<Long>> PET_GHOST = ATTACHMENT_TYPES.register("pet_ghost",
            () -> AttachmentType.builder(() -> 0L)
                    .serialize(Codec.LONG.fieldOf("until"))
                    .sync(ByteBufCodecs.VAR_LONG)
                    .build());

    // Radiance: who a player has fought and befriended (Features/Spells/radiance/RadianceRelations). Kept on death, and
    // synced to that player only (their client draws the red and green arrows)
    public static final Supplier<AttachmentType<com.mowmaster.dust.Features.Spells.radiance.RadianceRelations>> RADIANCE_RELATIONS = ATTACHMENT_TYPES.register("radiance_relations",
            () -> AttachmentType.builder(() -> com.mowmaster.dust.Features.Spells.radiance.RadianceRelations.NONE)
                    .serialize(com.mowmaster.dust.Features.Spells.radiance.RadianceRelations.CODEC.fieldOf("relations"))
                    .sync((holder, to) -> holder == to, com.mowmaster.dust.Features.Spells.radiance.RadianceRelations.STREAM_CODEC)
                    .copyOnDeath()
                    .build());

    // Slime's block effect: a chunk's slime patches (Features/Spells/blocks/StickyFaces), saved with the chunk and
    // synced to clients, who draw them (Client/StickyPatchRenderer)
    public static final Supplier<AttachmentType<com.mowmaster.dust.Features.Spells.blocks.StickyFaces>> STICKY_FACES = ATTACHMENT_TYPES.register("sticky_faces",
            () -> AttachmentType.builder(() -> com.mowmaster.dust.Features.Spells.blocks.StickyFaces.NONE)
                    .serialize(com.mowmaster.dust.Features.Spells.blocks.StickyFaces.CODEC.fieldOf("faces"))
                    .sync(com.mowmaster.dust.Features.Spells.blocks.StickyFaces.STREAM_CODEC)
                    .build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
