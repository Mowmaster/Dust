package com.mowmaster.dust.Features.Magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

// How long the player's mana has been regenerating, kept low and kept high, in ticks (PlayerMagicHelper.onPlayerTick).
// Saved but never sent to the client (DustAttachmentTypeRegistry.MANA_TIMERS has no sync).
public record ManaTimers(int regen, int low, int high)
{
    public static final ManaTimers ZERO = new ManaTimers(0, 0, 0);

    public static final Codec<ManaTimers> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("regen", 0).forGetter(ManaTimers::regen),
            Codec.INT.optionalFieldOf("low", 0).forGetter(ManaTimers::low),
            Codec.INT.optionalFieldOf("high", 0).forGetter(ManaTimers::high)
    ).apply(instance, ManaTimers::new));
}
