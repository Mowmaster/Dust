package com.mowmaster.dust.Features.Research;

import com.mowmaster.dust.DustReferences;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Which icon the research book shows for each enchantment in the Effects tab: enchantment id -> GUI sprite id.
// A GUI sprite is a texture under assets/<namespace>/textures/gui/sprites/; its id leaves off that folder and ".png"
// (dust:enchantment/sharpness is assets/dust/textures/gui/sprites/enchantment/sharpness.png).
//
// Enchantments without an icon here show the enchanted book instead. To give another mod's enchantment an icon, add
// its texture and one line, either below or from that mod's constructor:
//     EnchantmentIcons.register(Identifier.fromNamespaceAndPath("othermod", "frostbite"),
//             Identifier.fromNamespaceAndPath(DustReferences.MODID, "enchantment/othermod/frostbite"));
public final class EnchantmentIcons
{
    // ConcurrentHashMap: safe if mods register while the book is reading it
    private static final Map<Identifier, Identifier> ICONS = new ConcurrentHashMap<>();

    static {
        // Vanilla: each texture is named after the enchantment
        for (String name : new String[] {
                "protection", "fire_protection", "feather_falling", "blast_protection", "projectile_protection",
                "respiration", "aqua_affinity", "thorns", "depth_strider", "frost_walker", "binding_curse",
                "soul_speed", "swift_sneak", "sharpness", "smite", "bane_of_arthropods", "knockback", "fire_aspect",
                "looting", "sweeping_edge", "efficiency", "silk_touch", "unbreaking", "fortune", "power", "punch",
                "flame", "infinity", "luck_of_the_sea", "lure", "loyalty", "impaling", "riptide", "channeling",
                "multishot", "quick_charge", "piercing", "density", "breach", "wind_burst", "lunge", "mending",
                "vanishing_curse"}) {
            register(Identifier.withDefaultNamespace(name), Identifier.fromNamespaceAndPath(DustReferences.MODID, "enchantment/" + name));
        }
    }

    private EnchantmentIcons() {}

    public static void register(Identifier enchantment, Identifier sprite)
    {
        ICONS.put(enchantment, sprite);
    }

    // The icon sprite for an enchantment, or null if it has none
    public static @Nullable Identifier sprite(Identifier enchantment)
    {
        return ICONS.get(enchantment);
    }
}
