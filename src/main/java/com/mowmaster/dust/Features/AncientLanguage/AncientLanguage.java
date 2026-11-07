package com.mowmaster.dust.Features.AncientLanguage;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

// The "language of the ancients": 26 glyphs, one per letter a-z.
//
// How it works:
// - Ancient writing is ordinary text with the font "dust:ancient" (a bitmap font made from the glyph textures,
//   assets/dust/font/ancient.json). It works anywhere text has a style: item names and lore, written books, signs,
//   chat, /tellraw, datapack text: {"text":"hello","font":"dust:ancient"}.
// - Each player has a set of glyphs they have learned (the ANCIENT_GLYPHS attachment, a 26-bit mask: bit 0 = a).
// - On each client, text in that font is drawn letter by letter: letters the LOCAL player knows use the normal font,
//   the rest stay glyphs (Client/AncientGlyphSource, hooked in by mixin/client/CachedFontProviderMixin). So two
//   players looking at the same sign each see it with their own knowledge, and it updates the moment one learns more.
// - Anything that isn't a-z / A-Z (spaces, digits, punctuation) is always drawn normally.
//
// Note: the real letters are still in the text data, so a hacked client could read them. That's fine for lore; it
// isn't a security boundary.
public final class AncientLanguage
{
    // The font ancient writing uses (revealed per letter as the viewer learns it)
    public static final Identifier FONT_ID = Identifier.fromNamespaceAndPath(DustReferences.MODID, "ancient");
    public static final FontDescription FONT = new FontDescription.Resource(FONT_ID);
    // The raw glyphs, never revealed: for showing a glyph on purpose (e.g. "you learned <glyph> = A")
    public static final Identifier GLYPHS_ID = Identifier.fromNamespaceAndPath(DustReferences.MODID, "ancient_glyphs");
    public static final FontDescription GLYPHS = new FontDescription.Resource(GLYPHS_ID);

    public static final int GLYPH_COUNT = 26;
    public static final int ALL_GLYPHS = (1 << GLYPH_COUNT) - 1;

    private AncientLanguage() {}

    // ---- writing ----

    // Plain text written in the ancient script
    public static MutableComponent write(String text)
    {
        return Component.literal(text).withStyle(style -> style.withFont(FONT));
    }

    // Any component (translatable lore, a book page...) written in the ancient script. Parts of it that set their own
    // font keep it.
    public static MutableComponent inscribe(Component component)
    {
        return component.copy().withStyle(style -> style.withFont(FONT));
    }

    public static boolean isAncient(Style style)
    {
        return FONT.equals(style.getFont());
    }

    public static boolean isAncient(Component component)
    {
        return isAncient(component.getStyle());
    }

    // ---- glyphs ----

    // 0-25 for a-z / A-Z, -1 for anything else
    public static int glyphIndex(int codepoint)
    {
        if (codepoint >= 'a' && codepoint <= 'z') return codepoint - 'a';
        if (codepoint >= 'A' && codepoint <= 'Z') return codepoint - 'A';
        return -1;
    }

    public static char letter(int glyphIndex)
    {
        return (char) ('A' + glyphIndex);
    }

    // The glyph itself, always shown as a glyph: "<glyph> (A)"
    public static MutableComponent describeGlyph(int glyphIndex)
    {
        String lower = String.valueOf(Character.toLowerCase(letter(glyphIndex)));
        return Component.literal(lower).withStyle(style -> style.withFont(GLYPHS))
                .append(Component.literal(" (" + letter(glyphIndex) + ")").withStyle(style -> style.withFont(FontDescription.DEFAULT)));
    }

    // ---- what a player knows ----

    public static int knownMask(Player player)
    {
        return player.getData(DustAttachmentTypeRegistry.ANCIENT_GLYPHS);
    }

    public static boolean knows(Player player, int glyphIndex)
    {
        return glyphIndex >= 0 && glyphIndex < GLYPH_COUNT && (knownMask(player) & (1 << glyphIndex)) != 0;
    }

    public static int knownCount(Player player)
    {
        return Integer.bitCount(knownMask(player));
    }

    // Teaches one glyph (0-25). Returns false if they already knew it. Call on the server; the attachment syncs the
    // new mask to that player only, and their client starts revealing the letter straight away.
    public static boolean learn(ServerPlayer player, int glyphIndex)
    {
        if (glyphIndex < 0 || glyphIndex >= GLYPH_COUNT || knows(player, glyphIndex)) return false;
        player.setData(DustAttachmentTypeRegistry.ANCIENT_GLYPHS, knownMask(player) | (1 << glyphIndex));
        player.sendSystemMessage(Component.translatable("message.dust.ancient.learned", describeGlyph(glyphIndex))
                .withStyle(ChatFormatting.GOLD));
        return true;
    }

    public static boolean forget(ServerPlayer player, int glyphIndex)
    {
        if (!knows(player, glyphIndex)) return false;
        player.setData(DustAttachmentTypeRegistry.ANCIENT_GLYPHS, knownMask(player) & ~(1 << glyphIndex));
        return true;
    }

    // Sets the whole mask at once (no messages): learn all, forget all
    public static void setKnown(ServerPlayer player, int mask)
    {
        player.setData(DustAttachmentTypeRegistry.ANCIENT_GLYPHS, mask & ALL_GLYPHS);
    }
}
