package com.mowmaster.dust.Features.Apparatus.mixer.Client;

import com.mowmaster.dust.Features.Apparatus.mixer.DustMixerBlockEntity;
import com.mowmaster.dust.Features.Apparatus.mixer.DustMixerBlockEntity.Phase;
import com.mowmaster.dust.Features.Machines.Client.StationRendering;
import com.mowmaster.dust.Features.Machines.ElementCombining;

// What's in a mixer's bowl or pot to draw, for its phase (both Dust Mixers): how much on its left and right halves and
// their colours (0xAARRGGBB)
//   filling    the two elements side by side, each as much as has come in
//   mixing     both halves the batch, turning from their own colours to the colour of what they make
//   tipping    what it made (or tips out), one colour, going down as it pours
public record MixerContents(int left, int right, int leftColor, int rightColor)
{
    public static final MixerContents EMPTY = new MixerContents(0, 0, 0xFFFFFFFF, 0xFFFFFFFF);

    public static MixerContents of(DustMixerBlockEntity mixer, float ticksIntoPhase)
    {
        var first = mixer.first();
        var second = mixer.second();
        Phase phase = mixer.phase();
        switch (phase) {
            case OPEN, CLOSING, RETURNING -> {
                return new MixerContents(first == null ? 0 : mixer.firstAmount(), second == null ? 0 : mixer.secondAmount(),
                        first == null ? 0xFFFFFFFF : 0xFF000000 | first.color(), second == null ? 0xFFFFFFFF : 0xFF000000 | second.color());
            }
            case MIXING -> {
                int total = Math.max(1, mixer.batch() * DustMixerBlockEntity.MIX_TICKS_PER_DUST);
                float share = Math.min(1, ticksIntoPhase / total);
                var child = first == null || second == null ? null : ElementCombining.combine(first, second);
                int target = child == null ? 0xFFFFFF : child.color();
                return new MixerContents(mixer.batch(), mixer.batch(),
                        0xFF000000 | StationRendering.lerpColor(first == null ? target : first.color(), target, share),
                        0xFF000000 | StationRendering.lerpColor(second == null ? target : second.color(), target, share));
            }
            default -> {
                int amount = 0;
                long red = 0, green = 0, blue = 0;
                for (var entry : mixer.dumping().entrySet()) {
                    int c = entry.getKey().color(), n = entry.getValue();
                    amount += n;
                    red += (long) ((c >> 16) & 0xFF) * n;
                    green += (long) ((c >> 8) & 0xFF) * n;
                    blue += (long) (c & 0xFF) * n;
                }
                if (amount <= 0) return EMPTY;
                int color = 0xFF000000 | (int) ((red / amount) << 16 | (green / amount) << 8 | (blue / amount));
                return new MixerContents(amount, amount, color, color);
            }
        }
    }

    // How high this much stands, from floor up to full at SLOT_CAPACITY (a thin layer at least; floor: none)
    public static double height(int amount, double floor, double full)
    {
        if (amount <= 0) return floor;
        return floor + Math.max(0.3, (full - floor) * Math.min(1.0, amount / (double) DustMixerBlockEntity.SLOT_CAPACITY));
    }
}
