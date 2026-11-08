package com.mowmaster.dust.Features.DustComposition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.network.chat.Component;

// How much of each kind of dust something is made of (see DustCompositions for who has what).
//   fire   red      water  blue      earth  green      chaos  black      order  white
//
// The record's fields follow EnumElement's order. with(...) builds one up readably:
//     DustComposition.NONE.with(EnumElement.FIRE, 2).with(EnumElement.EARTH, 2).with(EnumElement.CHAOS, 1)
public record DustComposition(int fire, int water, int earth, int chaos, int order)
{
    public static final DustComposition NONE = new DustComposition(0, 0, 0, 0, 0);

    // Saved in research (what a player read on a Dust Testing Disk). Kinds with none are left out.
    public static final Codec<DustComposition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("fire", 0).forGetter(DustComposition::fire),
            Codec.INT.optionalFieldOf("water", 0).forGetter(DustComposition::water),
            Codec.INT.optionalFieldOf("earth", 0).forGetter(DustComposition::earth),
            Codec.INT.optionalFieldOf("chaos", 0).forGetter(DustComposition::chaos),
            Codec.INT.optionalFieldOf("order", 0).forGetter(DustComposition::order)
    ).apply(instance, DustComposition::new));

    public int get(EnumElement element)
    {
        // A switch expression (Java 14+): each case gives a value, and the compiler checks every enum value is covered
        return switch (element) {
            case FIRE -> fire;
            case WATER -> water;
            case EARTH -> earth;
            case CHAOS -> chaos;
            case ORDER -> order;
        };
    }

    // A copy with this element's amount added to
    public DustComposition with(EnumElement element, int amount)
    {
        return new DustComposition(
                fire + (element == EnumElement.FIRE ? amount : 0),
                water + (element == EnumElement.WATER ? amount : 0),
                earth + (element == EnumElement.EARTH ? amount : 0),
                chaos + (element == EnumElement.CHAOS ? amount : 0),
                order + (element == EnumElement.ORDER ? amount : 0));
    }

    public int total()
    {
        return fire + water + earth + chaos + order;
    }

    public boolean isEmpty()
    {
        return total() == 0;
    }

    // Exactly one kind of dust
    public boolean isPure()
    {
        int kinds = 0;
        for (EnumElement element : EnumElement.values()) {
            if (get(element) > 0) kinds++;
        }
        return kinds == 1;
    }

    // The colour it burns with: each kind of dust's pure colour, weighted by its share of the whole.
    //   1 water                     -> pure blue
    //   1 fire + 1 water            -> 50% red + 50% blue = purple
    //   2 fire + 2 earth + 1 chaos  -> 40% red + 40% green + 20% black (the black darkens the mix by 20%)
    // Order (white) lightens the mix the same way chaos (black) darkens it. 0xRRGGBB; white if there's no dust.
    public int color()
    {
        int total = total();
        if (total == 0) return 0xFFFFFF;
        float red = 0, green = 0, blue = 0;
        for (EnumElement element : EnumElement.values()) {
            float share = (float) get(element) / total;
            int pure = pureColor(element);
            red += share * ((pure >> 16) & 0xFF);
            green += share * ((pure >> 8) & 0xFF);
            blue += share * (pure & 0xFF);
        }
        return (Math.round(red) << 16) | (Math.round(green) << 8) | Math.round(blue);
    }

    // Each kind of dust's own colour
    public static int pureColor(EnumElement element)
    {
        return switch (element) {
            case FIRE -> 0xFF0000;
            case WATER -> 0x0000FF;
            case EARTH -> 0x00FF00;
            case CHAOS -> 0x000000;
            case ORDER -> 0xFFFFFF;
        };
    }

    // "Fire" (dust.element.fire), "Water"...
    public static Component name(EnumElement element)
    {
        return Component.translatable("dust.element." + element.getSerializedName());
    }
}
