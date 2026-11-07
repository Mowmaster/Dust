package com.mowmaster.dust.Features.AncientLanguage.Client;

import com.mowmaster.dust.Features.AncientLanguage.AncientLanguage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GlyphSource;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.RandomSource;

// What the "dust:ancient" font hands out, glyph by glyph: the normal letter if the local player has learned it,
// otherwise the ancient glyph. Asked every time text is drawn, so learning a glyph shows up immediately everywhere.
// A GlyphSource is what Font asks for "the picture of character X in this font".
public record AncientGlyphSource(GlyphSource ancient, GlyphSource plain) implements GlyphSource
{
    @Override
    public BakedGlyph getGlyph(int codepoint)
    {
        int glyph = AncientLanguage.glyphIndex(codepoint);
        if (glyph < 0 || knows(glyph)) {
            return plain.getGlyph(codepoint);
        }
        return ancient.getGlyph(codepoint);
    }

    // Used by obfuscated (§k) text: just scramble with normal letters
    @Override
    public BakedGlyph getRandomGlyph(RandomSource random, int width)
    {
        return plain.getRandomGlyph(random, width);
    }

    private static boolean knows(int glyph)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        // No player (title screen, loading): nothing is known
        return player != null && AncientLanguage.knows(player, glyph);
    }
}
