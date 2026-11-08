package com.mowmaster.dust.Features.Research.Tabs;

import net.minecraft.world.item.DyeColor;

import java.util.ArrayList;
import java.util.List;

// Grouping names that differ only by a colour: red_dye, blue_dye -> "dye"; red_bundle and bundle -> "bundle".
// Used by the Blocks and Items tabs to put colour variants in one family.
public final class ColorVariants
{
    // The 16 dye colours plus "_", longest first so light_gray_ is removed before gray_ could half-match it
    public static final List<String> COLOR_PREFIXES = buildColorPrefixes();

    private ColorVariants() {}

    // The name with any of these prefixes removed from the front (repeatedly: waxed_exposed_x -> x)
    public static String stripPrefixes(String path, List<String> prefixes)
    {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String prefix : prefixes) {
                if (path.startsWith(prefix) && path.length() > prefix.length()) {
                    path = path.substring(prefix.length());
                    changed = true;
                }
            }
        }
        return path;
    }

    public static String stripColors(String path)
    {
        return stripPrefixes(path, COLOR_PREFIXES);
    }

    private static List<String> buildColorPrefixes()
    {
        List<String> prefixes = new ArrayList<>();
        for (DyeColor color : DyeColor.values()) prefixes.add(color.getSerializedName() + "_");
        prefixes.sort((a, b) -> b.length() - a.length());
        return List.copyOf(prefixes);
    }
}
