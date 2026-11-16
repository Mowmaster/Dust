package com.mowmaster.dust.Features.Magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

// The player's mana regeneration in progress (PlayerMagicHelper.onPlayerTick):
//   regen     ticks towards the next step (MagicConfig.REGEN_TICKS)
//   fraction  mana regained so far that isn't a whole point yet, in hundredths (each step adds max mana x REGEN_PERCENT)
// Saved but never sent to the client (DustAttachmentTypeRegistry.MANA_TIMERS has no sync). Old saves' "low" / "high"
// timers (max mana creeping up and down, now replaced by the Magic Level) are ignored.
public record ManaTimers(int regen, int fraction)
{
    public static final ManaTimers ZERO = new ManaTimers(0, 0);

    public static final Codec<ManaTimers> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("regen", 0).forGetter(ManaTimers::regen),
            Codec.INT.optionalFieldOf("fraction", 0).forGetter(ManaTimers::fraction)
    ).apply(instance, ManaTimers::new));
}
