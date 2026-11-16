package com.mowmaster.dust.Features.Spells;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Map;

// What a modifier does, for the pylon HUD (PylonReading): how much one level of it adds, and so how much a pylon's
// levels add in all. The amounts match what each modifier's levels do in SpellRegistries; the words are the lang key
// spell_modifier.<ns>.<id>.per_level ("+%s%% crit chance"; %s is the amount). Modifiers that don't add a number per
// level (shape modifiers, one-use ones, the signs and sins) have a description instead: spell_modifier.<ns>.<id>.desc.
public final class ModifierInfo
{
    private ModifierInfo() {}

    // How much one level adds, in the units its per_level line uses
    private static final Map<String, Double> PER_LEVEL = Map.ofEntries(
            Map.entry("cast_speed", 1.0), Map.entry("mana_reduction", 1.0), Map.entry("dust_reduction", 1.0),
            Map.entry("spell_xp", 10.0), Map.entry("fragment_progress", 1.0), Map.entry("damage", 0.5),
            Map.entry("crit_chance", 1.0), Map.entry("luck", 1.0), Map.entry("duration", 1.0), Map.entry("potency", 0.25),
            Map.entry("falloff", 1.0), Map.entry("velocity", 0.1), Map.entry("range", 1.0), Map.entry("impact_damage", 1.0),
            Map.entry("multishot", 1.0), Map.entry("shield_potency", 1.0), Map.entry("size", 0.25), Map.entry("target_count", 1.0),
            Map.entry("depth", 0.5), Map.entry("chain_count", 1.0), Map.entry("search_distance", 1.0), Map.entry("rune_duration", 20.0),
            Map.entry("wall_width", 0.5), Map.entry("wall_depth", 0.5), Map.entry("beam_distance", 1.0), Map.entry("efficiency", 1.0),
            Map.entry("casting_speed", 1.0), Map.entry("category_weight", 1.0), Map.entry("double_draw", 5.0));

    // One level's worth, or null if it has none (a description instead)
    public static @Nullable Double perLevel(Identifier modifier)
    {
        return PER_LEVEL.get(modifier.getPath());
    }

    // "+1% crit chance"
    public static Component perLevelLine(Identifier modifier)
    {
        Double amount = perLevel(modifier);
        return amount == null ? description(modifier) : line(modifier, amount);
    }

    // What this many levels add in all: "+3% crit chance" (null for a modifier with no amount)
    public static @Nullable Component total(Identifier modifier, int levels)
    {
        Double amount = perLevel(modifier);
        return amount == null ? null : line(modifier, amount * levels);
    }

    public static Component description(Identifier modifier)
    {
        return Component.translatable("spell_modifier." + modifier.getNamespace() + "." + modifier.getPath() + ".desc");
    }

    private static Component line(Identifier modifier, double amount)
    {
        return Component.translatable("spell_modifier." + modifier.getNamespace() + "." + modifier.getPath() + ".per_level", format(amount));
    }

    // 3 -> "3", 0.75 -> "0.75", 0.30000001 -> "0.3"
    public static String format(double amount)
    {
        String text = String.format(java.util.Locale.ROOT, "%.2f", amount);
        return text.contains(".") ? text.replaceAll("0+$", "").replaceAll("\\.$", "") : text;
    }
}
