package com.mowmaster.dust.Features.DustComposition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.network.chat.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// What something is made of: a list of elements (repeats count twice), e.g. rooted dirt = earth + trees.
// Everything comes down to the five primal dusts (get), which give the colour it burns with (color).
// Items get theirs from the dust:elements data map (DustDataMaps; see DustCompositions).
public record DustComposition(List<DustElement> elements)
{
    public static final DustComposition NONE = new DustComposition(List.of());

    // { "elements": ["dust:earth", "dust:trees"] }: the form used in the data map and saved in research
    public static final Codec<DustComposition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DustElements.CODEC.listOf().fieldOf("elements").forGetter(DustComposition::elements)
    ).apply(instance, DustComposition::new));

    public DustComposition
    {
        // A compact constructor: runs before the fields are set (here, making the list unchangeable)
        elements = List.copyOf(elements);
    }

    public static DustComposition of(DustElement... elements)
    {
        return new DustComposition(List.of(elements));
    }

    // How much of this primal dust, across all its elements
    public int get(EnumElement primal)
    {
        int total = 0;
        for (DustElement element : elements) total += element.primal(primal);
        return total;
    }

    public int total()
    {
        int total = 0;
        for (EnumElement primal : EnumElement.values()) total += get(primal);
        return total;
    }

    public boolean isEmpty()
    {
        return total() == 0;
    }

    // Exactly one kind of primal dust
    public boolean isPure()
    {
        int kinds = 0;
        for (EnumElement primal : EnumElement.values()) {
            if (get(primal) > 0) kinds++;
        }
        return kinds == 1;
    }

    // Each element once, with how many times it's listed, in order
    public Map<DustElement, Integer> counted()
    {
        Map<DustElement, Integer> counts = new LinkedHashMap<>();
        for (DustElement element : elements) counts.merge(element, 1, Integer::sum);
        return counts;
    }

    // The colour it burns with (see colorOf)
    public int color()
    {
        int[] primals = new int[EnumElement.values().length];
        for (EnumElement primal : EnumElement.values()) primals[primal.ordinal()] = get(primal);
        return colorOf(primals);
    }

    // A colour from amounts of primal dust (indexed by EnumElement#ordinal), 0xRRGGBB. HSV colour theory:
    //   fire, earth, water   the red, green and blue channels: the hue. C = the largest of the three.
    //   order                saturation = C / (C + order): more order, paler (towards white)
    //   chaos                value = C / (C + chaos): more chaos, darker (towards black)
    // So 1 fire + 1 order is red at 50% saturation, and 1 fire + 1 water + 2 chaos is magenta at 33% value. With no
    // fire, earth or water it's a grey: white for order, black for chaos, between for both. Nothing at all is white.
    public static int colorOf(int[] primals)
    {
        int fire = primals[EnumElement.FIRE.ordinal()];
        int water = primals[EnumElement.WATER.ordinal()];
        int earth = primals[EnumElement.EARTH.ordinal()];
        int order = primals[EnumElement.ORDER.ordinal()];
        int chaos = primals[EnumElement.CHAOS.ordinal()];
        int strongest = Math.max(fire, Math.max(water, earth));
        if (strongest == 0) {
            if (order + chaos == 0) return 0xFFFFFF;
            int grey = Math.round(255F * order / (order + chaos));
            return (grey << 16) | (grey << 8) | grey;
        }
        // The hue at full strength: the strongest channel at 1
        float red = (float) fire / strongest, green = (float) earth / strongest, blue = (float) water / strongest;
        // java.awt.Color converts between RGB and HSV ("HSB" there: hue, saturation, brightness)
        float[] hsv = java.awt.Color.RGBtoHSB(Math.round(red * 255), Math.round(green * 255), Math.round(blue * 255), null);
        float saturation = hsv[1] * strongest / (strongest + order);
        float value = (float) strongest / (strongest + chaos);
        return java.awt.Color.HSBtoRGB(hsv[0], saturation, value) & 0xFFFFFF;
    }

    // Each primal dust's own colour
    public static int pureColor(EnumElement primal)
    {
        return DustElements.of(primal).color();
    }

    // "Fire", "Water"...
    public static Component name(EnumElement primal)
    {
        return DustElements.of(primal).name();
    }
}
