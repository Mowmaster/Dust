package com.mowmaster.dust.Features.AncientLanguage;

import net.minecraft.util.RandomSource;

import java.util.List;

// Words found on tablets and ruin walls. Lowercase a-z only, at most DecipherMenu.MAX_LENGTH letters.
// Between them they use all 26 letters, so every glyph can be learned.
public final class AncientWords
{
    public static final List<String> WORDS = List.of(
            "dust", "aura", "ash", "ember", "stone", "river", "ancient", "glyph", "ruin", "crystal",
            "order", "chaos", "flame", "earth", "water", "wind", "star", "moon", "shadow", "light",
            "spirit", "mana", "vessel", "tablet", "seeker", "origin", "return", "echo", "memory", "quartz",
            "zephyr", "jade", "vortex", "kindle", "oxide", "wax", "jinx", "frozen", "quiver", "mystic",
            "sky", "beyond", "awaken", "journey", "forgotten", "zenith", "quest", "knowledge", "vanish", "wizard",
            "hex", "rune", "scribe", "oracle", "temple", "tomb", "cipher", "keeper", "relic", "dream",
            "void", "spark", "bloom", "harvest", "wyvern", "kin", "vow", "jewel", "muse", "tranquil");

    private AncientWords() {}

    public static String random(RandomSource random)
    {
        return WORDS.get(random.nextInt(WORDS.size()));
    }

    // Keeps only a-z (lowercased), cut to the longest word the decipher screen can show
    public static String clean(String text)
    {
        StringBuilder out = new StringBuilder();
        for (char c : text.toLowerCase(java.util.Locale.ROOT).toCharArray()) {
            if (c >= 'a' && c <= 'z' && out.length() < DecipherMenu.MAX_LENGTH) out.append(c);
        }
        return out.toString();
    }
}
