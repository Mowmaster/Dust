package com.mowmaster.dust.Features.Magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// A player's magic (a data attachment: DustAttachmentTypeRegistry.PLAYER_MAGIC; PlayerMagicHelper has the rules):
//   mana, maxMana       what spells spend to activate (MagicConfig)
//   dust                the dust carried, by element (only the elements in it take up a type slot)
//   dustTypes           how many different elements can be carried at once
//   dustCapacity        how much of each element can be carried
//   attunements         the elements the player is attuned to (0 to 3; machines and research give these)
//   exhausted           they've paid for magic with their health (burning Alchemical Dust with no mana left) and
//                       need to sleep it off
//   awakened            they've slept after that and woken with their magic: mana regenerates, and the HUD shows
// Immutable: every change makes a new one (the with... methods), which is what setting the attachment syncs. The
// regeneration timers are kept apart (ManaTimers), so ticking doesn't send this to the client every tick.
public record PlayerMagic(int mana, int maxMana, Map<DustElement, Integer> dust, int dustTypes, int dustCapacity, List<DustElement> attunements,
                          boolean exhausted, boolean awakened)
{
    public static final PlayerMagic DEFAULT = new PlayerMagic(MagicConfig.START_MAX_MANA, MagicConfig.START_MAX_MANA,
            Map.of(), MagicConfig.START_DUST_TYPES, MagicConfig.START_DUST_CAPACITY, List.of(), false, false);

    public PlayerMagic
    {
        // LinkedHashMap keeps the order elements were first picked up (the HUD lists them that way)
        dust = java.util.Collections.unmodifiableMap(new LinkedHashMap<>(dust));
        attunements = List.copyOf(attunements);
    }

    public static final Codec<PlayerMagic> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("mana", MagicConfig.START_MAX_MANA).forGetter(PlayerMagic::mana),
            Codec.INT.optionalFieldOf("max_mana", MagicConfig.START_MAX_MANA).forGetter(PlayerMagic::maxMana),
            Codec.unboundedMap(DustElements.CODEC, Codec.INT).optionalFieldOf("dust", Map.of()).forGetter(PlayerMagic::dust),
            Codec.INT.optionalFieldOf("dust_types", MagicConfig.START_DUST_TYPES).forGetter(PlayerMagic::dustTypes),
            Codec.INT.optionalFieldOf("dust_capacity", MagicConfig.START_DUST_CAPACITY).forGetter(PlayerMagic::dustCapacity),
            DustElements.CODEC.listOf().optionalFieldOf("attunements", List.of()).forGetter(PlayerMagic::attunements),
            Codec.BOOL.optionalFieldOf("exhausted", false).forGetter(PlayerMagic::exhausted),
            Codec.BOOL.optionalFieldOf("awakened", false).forGetter(PlayerMagic::awakened)
    ).apply(instance, PlayerMagic::new));

    // Sent to the player's own client (the HUD reads it), through the same codec
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerMagic> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public int dustOf(DustElement element)
    {
        return dust.getOrDefault(element, 0);
    }

    public PlayerMagic withMana(int mana, int maxMana)
    {
        return new PlayerMagic(mana, maxMana, dust, dustTypes, dustCapacity, attunements, exhausted, awakened);
    }

    // The dust with this element set to amount (0 removes it, freeing its type slot)
    public PlayerMagic withDust(DustElement element, int amount)
    {
        Map<DustElement, Integer> changed = new LinkedHashMap<>(dust);
        if (amount <= 0) changed.remove(element);
        else changed.put(element, amount);
        return new PlayerMagic(mana, maxMana, changed, dustTypes, dustCapacity, attunements, exhausted, awakened);
    }

    public PlayerMagic withStoreSize(int dustTypes, int dustCapacity)
    {
        return new PlayerMagic(mana, maxMana, dust, dustTypes, dustCapacity, attunements, exhausted, awakened);
    }

    public PlayerMagic withAwakening(boolean exhausted, boolean awakened)
    {
        return new PlayerMagic(mana, maxMana, dust, dustTypes, dustCapacity, attunements, exhausted, awakened);
    }

    public PlayerMagic withAttunements(List<DustElement> attunements)
    {
        return new PlayerMagic(mana, maxMana, dust, dustTypes, dustCapacity, attunements, exhausted, awakened);
    }
}
