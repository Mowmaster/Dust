package com.mowmaster.dust.Features.Research.Client;

import com.mowmaster.dust.Features.Research.EntryResearch;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchCategory;
import com.mowmaster.dust.Features.Research.ResearchData;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// Draws the research book, and says what a click on it hits. Used by the held view, the offhand panel
// (ResearchBookHud) and the clickable screen (ResearchBookScreen), so they all look the same. Everything is in "book
// pixels": (0, 0) is the book's top-left corner and it's WIDTH x HEIGHT; callers move and scale the drawing.
//
//   [tab][tab][tab][tab][tab]                  five tabs along the top
//   +-----------------+-----------------+
//   | left page       | right page      |      list view: a grid of entries over both pages
//   |                 |                 |      entry view: the entry and its drops on the left,
//   +-----------------+-----------------+                  its name, when it was found and notes on the right
//
// What's open (tab, list page, entry, notes page) is remembered here, so the held view shows whatever was last
// opened in the screen.
public final class ResearchBook
{
    public static final int WIDTH = 256;
    public static final int HEIGHT = 188;

    private static final int TAB_W = 28;
    private static final int TAB_GAP = 3;
    private static final int TABS_X = 12;
    // The tabs sit above the cover (icons end at y 20 at the lowest), so the cover never hides them
    private static final int COVER_TOP = 22;

    static final int PAGE_W = 114;
    static final int PAGE_TOP = COVER_TOP + 8;
    static final int PAGE_BOTTOM = HEIGHT - 8;
    static final int LEFT_X = 10;
    static final int RIGHT_X = WIDTH - 10 - PAGE_W;

    private static final int CELL = 18;
    private static final int COLS = 6;
    private static final int ROWS = 7;
    private static final int GRID_TOP = PAGE_TOP + 14;
    private static final int PER_PAGE = COLS * ROWS;
    private static final int PER_SPREAD = PER_PAGE * 2;
    // Where the clickable footer row (arrows, back) sits
    static final int FOOTER_Y = PAGE_BOTTOM - 12;

    private static final int COVER = 0xFF5B3A1E;
    private static final int COVER_EDGE = 0xFF3A2410;
    private static final int TAB_OFF = 0xFFB89A6A;
    private static final int TAB_ON = 0xFFEDE0BB;
    static final int INK = Paper.INK;
    static final int FADED = Paper.FADED_INK;

    // ---- what's open ----
    static ResearchCategory tab = ResearchCategory.BLOCKS;
    static int listPage = 0;
    static @Nullable ResearchEntry selected = null;
    static int notesPage = 0;

    private ResearchBook() {}

    // The notes pages an entry has, depending on how far its research has got
    // OVERVIEW starts as the built-in notes and the player can rewrite it (Research Notes only)
    enum NotesPage { OVERVIEW, WHERE_FOUND, EXPERIMENTS, RESEARCH }

    static List<NotesPage> notesPages(EntryResearch research)
    {
        List<NotesPage> pages = new ArrayList<>();
        pages.add(NotesPage.OVERVIEW);
        if (research.biomes().size() >= 2) pages.add(NotesPage.WHERE_FOUND);
        if (research.hasExperiments()) pages.add(NotesPage.EXPERIMENTS);
        pages.add(NotesPage.RESEARCH);
        return pages;
    }

    static @Nullable NotesPage currentNotesPage(ResearchData data)
    {
        if (selected == null) return null;
        EntryResearch research = data.get(selected);
        if (research == null) return null;
        List<NotesPage> pages = notesPages(research);
        notesPage = Math.clamp(notesPage, 0, pages.size() - 1);
        return pages.get(notesPage);
    }

    // ================= drawing =================

    // mouseX/mouseY in book pixels (or negative for no mouse); editing = the screen is showing its notes box
    public static void draw(GuiGraphicsExtractor graphics, Font font, ResearchData data, int mouseX, int mouseY, boolean editing)
    {
        drawTabs(graphics, mouseX, mouseY);
        graphics.fill(0, COVER_TOP, WIDTH, HEIGHT, COVER_EDGE);
        graphics.fill(2, COVER_TOP + 2, WIDTH - 2, HEIGHT - 2, COVER);
        Paper.paper(graphics, LEFT_X - 2, PAGE_TOP - 4, PAGE_W + 4, PAGE_BOTTOM - PAGE_TOP + 6);
        Paper.paper(graphics, RIGHT_X - 2, PAGE_TOP - 4, PAGE_W + 4, PAGE_BOTTOM - PAGE_TOP + 6);

        if (selected != null && data.has(selected)) {
            drawEntry(graphics, font, data, selected, editing);
        } else {
            selected = null;
            drawList(graphics, font, data);
        }
    }

    private static void drawTabs(GuiGraphicsExtractor graphics, int mouseX, int mouseY)
    {
        ResearchCategory[] categories = ResearchCategory.values();
        for (int i = 0; i < categories.length; i++) {
            int x = TABS_X + i * (TAB_W + TAB_GAP);
            boolean on = categories[i] == tab;
            int top = on ? 0 : 2;
            graphics.fill(x, top, x + TAB_W, COVER_TOP + 2, COVER_EDGE);
            graphics.fill(x + 1, top + 1, x + TAB_W - 1, COVER_TOP + 2, on ? TAB_ON : TAB_OFF);
            graphics.item(categories[i].tabIcon(), x + (TAB_W - 16) / 2, top + 2);
        }
    }

    // ---- list view ----

    private static void drawList(GuiGraphicsExtractor graphics, Font font, ResearchData data)
    {
        List<ResearchEntry> entries = tab.entries();
        int pages = Math.max(1, (entries.size() + PER_SPREAD - 1) / PER_SPREAD);
        listPage = Math.clamp(listPage, 0, pages - 1);

        Paper.inkCentred(graphics, font, tab.title(), LEFT_X + PAGE_W / 2, PAGE_TOP + 2, INK);
        Paper.inkCentred(graphics, font, Component.translatable("research.dust.page", listPage + 1, pages), RIGHT_X + PAGE_W / 2, PAGE_TOP + 2, FADED);

        int start = listPage * PER_SPREAD;
        for (int i = 0; i < PER_SPREAD && start + i < entries.size(); i++) {
            ResearchEntry entry = entries.get(start + i);
            int[] cell = cellPosition(i);
            Paper.slot(graphics, cell[0], cell[1]);
            ItemStack icon = entry.icon();
            if (data.has(entry)) graphics.item(icon, cell[0] + 1, cell[1] + 1);
            else Paper.ghost(graphics, icon, cell[0], cell[1]);
        }

        // "Discovered 12 / 340" across the bottom, page arrows in the corners
        Component count = Component.translatable("research.dust.discovered", data.count(tab), entries.size());
        Paper.inkCentred(graphics, font, count, WIDTH / 2, FOOTER_Y + 2, INK);
        if (listPage > 0) Paper.ink(graphics, font, Component.literal("<"), LEFT_X + 2, FOOTER_Y + 2, INK);
        if (listPage < pages - 1) Paper.ink(graphics, font, Component.literal(">"), RIGHT_X + PAGE_W - 8, FOOTER_Y + 2, INK);
    }

    // Top-left of the i-th cell on the open spread (the first PER_PAGE on the left page)
    private static int[] cellPosition(int i)
    {
        int pageX = i < PER_PAGE ? LEFT_X : RIGHT_X;
        int index = i % PER_PAGE;
        int gridLeft = pageX + (PAGE_W - COLS * CELL) / 2;
        return new int[] {gridLeft + (index % COLS) * CELL, GRID_TOP + (index / COLS) * CELL};
    }

    // ---- entry view ----

    private static void drawEntry(GuiGraphicsExtractor graphics, Font font, ResearchData data, ResearchEntry entry, boolean editing)
    {
        EntryResearch research = data.get(entry);
        if (research == null) return;

        // Left page: the entry itself, then its drops
        int centreX = LEFT_X + PAGE_W / 2;
        drawSubject(graphics, entry, LEFT_X + 20, PAGE_TOP + 2, LEFT_X + PAGE_W - 20, PAGE_TOP + 62);
        Paper.ink(graphics, font, Component.translatable("research.dust.drops"), LEFT_X + 4, PAGE_TOP + 68, INK);
        if (research.drops().isEmpty()) {
            List<FormattedCharSequence> lines = font.split(Component.translatable(entry.isEntity() ? "research.dust.drops.unknown_mob" : "research.dust.drops.unknown_block"), PAGE_W - 8);
            drawLines(graphics, font, lines, LEFT_X + 4, PAGE_TOP + 80, FOOTER_Y - 2, FADED);
        } else {
            for (int i = 0; i < research.drops().size() && i < 12; i++) {
                int x = LEFT_X + 3 + (i % 6) * CELL;
                int y = PAGE_TOP + 79 + (i / 6) * CELL;
                Paper.slot(graphics, x, y);
                graphics.item(new ItemStack(BuiltInRegistries.ITEM.getValue(research.drops().get(i))), x + 1, y + 1);
            }
        }
        Paper.ink(graphics, font, Component.translatable("research.dust.back"), LEFT_X + 2, FOOTER_Y + 2, INK);

        // Right page: name, when it was found, then the open notes page
        int rightCentre = RIGHT_X + PAGE_W / 2;
        drawCentredClipped(graphics, font, entry.name().copy().withStyle(ChatFormatting.BOLD), rightCentre, PAGE_TOP + 2, PAGE_W - 4, INK);
        Component found = Component.translatable("research.dust.found", research.day(), Research.clockText(research.time()),
                Component.translatable("research.dust.time." + Research.partOfDay(research.time())));
        drawCentredClipped(graphics, font, found, rightCentre, PAGE_TOP + 13, PAGE_W - 4, FADED);
        graphics.fill(RIGHT_X + 6, PAGE_TOP + 24, RIGHT_X + PAGE_W - 6, PAGE_TOP + 25, FADED);

        List<NotesPage> pages = notesPages(research);
        NotesPage page = currentNotesPage(data);
        if (page != null) {
            Paper.ink(graphics, font, Component.translatable("research.dust.notes." + page.name().toLowerCase(java.util.Locale.ROOT)), RIGHT_X + 4, PAGE_TOP + 28, INK);
            if (!(page == NotesPage.OVERVIEW && editing)) {
                drawLines(graphics, font, font.split(notesText(entry, research, page), PAGE_W - 8), RIGHT_X + 4, PAGE_TOP + 40, FOOTER_Y - 2, INK);
            }
        }
        Paper.inkCentred(graphics, font, Component.literal((notesPage + 1) + "/" + pages.size()), rightCentre, FOOTER_Y + 2, FADED);
        if (notesPage > 0) Paper.ink(graphics, font, Component.literal("<"), RIGHT_X + 2, FOOTER_Y + 2, INK);
        if (notesPage < pages.size() - 1) Paper.ink(graphics, font, Component.literal(">"), RIGHT_X + PAGE_W - 8, FOOTER_Y + 2, INK);
    }

    // The block as a big item, or the mob itself
    private static void drawSubject(GuiGraphicsExtractor graphics, ResearchEntry entry, int x0, int y0, int x1, int y1)
    {
        if (entry.isEntity()) {
            LivingEntity mob = ResearchClient.displayEntity(entry);
            if (mob != null) {
                float biggest = Math.max(mob.getBbWidth(), mob.getBbHeight());
                int size = (int) Math.max(4, Math.min(30, 48 / Math.max(1.0F, biggest)));
                try {
                    InventoryScreen.renderEntityInInventoryFollowsAngle(graphics, x0, y0, x1, y1, size, 0.0625F, 0.6F, 0.0F, mob);
                    return;
                } catch (RuntimeException ignored) {
                    // Some mobs can't be drawn outside a world: fall back to the egg
                }
            }
        }
        graphics.pose().pushMatrix();
        graphics.pose().translate((x0 + x1) / 2.0F - 24, (y0 + y1) / 2.0F - 24);
        graphics.pose().scale(3.0F, 3.0F);
        graphics.item(entry.icon(), 0, 0);
        graphics.pose().popMatrix();
    }

    // The text of a notes page
    private static Component notesText(ResearchEntry entry, EntryResearch research, NotesPage page)
    {
        return switch (page) {
            // The player's version if they've written one, otherwise the built-in notes
            case OVERVIEW -> research.notes().isEmpty() ? builtInNotes(entry) : Component.literal(research.notes());
            case WHERE_FOUND -> lines(Component.translatable("research.dust.where_found.intro"),
                    research.biomes().stream().map(ResearchBook::biomeName).toList());
            case EXPERIMENTS -> experiments(research);
            case RESEARCH -> Component.translatable("research.dust.research.unknown");
        };
    }

    // The built-in overview as plain text: what the notes box starts with
    static String builtInNotesText(ResearchEntry entry)
    {
        return builtInNotes(entry).getString();
    }

    // research.dust.note.<namespace>.<path> if the lang file has one, otherwise the tab's general note
    private static Component builtInNotes(ResearchEntry entry)
    {
        String key = entry.noteKey();
        return Language.getInstance().has(key)
                ? Component.translatable(key)
                : Component.translatable("research.dust.note.default." + entry.category().name().toLowerCase(java.util.Locale.ROOT), entry.name());
    }

    private static Component experiments(EntryResearch research)
    {
        MutableComponent text = Component.empty();
        if (research.maxHealth() > 0) {
            text.append(Component.translatable("research.dust.experiment.health", Math.round(research.maxHealth()) / 2.0F)).append("\n");
        }
        appendItems(text, "research.dust.experiment.breeding", research.breeding());
        appendItems(text, "research.dust.experiment.made_from", research.madeFrom());
        appendItems(text, "research.dust.experiment.makes", research.makes());
        return text;
    }

    private static void appendItems(MutableComponent text, String key, List<Identifier> items)
    {
        if (items.isEmpty()) return;
        MutableComponent names = Component.empty();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) names.append(", ");
            names.append(new ItemStack(BuiltInRegistries.ITEM.getValue(items.get(i))).getHoverName());
        }
        text.append(Component.translatable(key, names)).append("\n");
    }

    private static Component biomeName(Identifier biome)
    {
        return Component.translatable("biome." + biome.getNamespace() + "." + biome.getPath());
    }

    private static Component lines(Component first, List<Component> rest)
    {
        MutableComponent text = first.copy();
        for (Component line : rest) text.append("\n- ").append(line);
        return text;
    }

    // Draws wrapped lines down from y, stopping before maxY (text that doesn't fit is cut off)
    private static void drawLines(GuiGraphicsExtractor graphics, Font font, List<FormattedCharSequence> lines, int x, int y, int maxY, int color)
    {
        for (FormattedCharSequence line : lines) {
            if (y + font.lineHeight > maxY) break;
            graphics.text(font, line, x, y, color, false);
            y += font.lineHeight;
        }
    }

    private static void drawCentredClipped(GuiGraphicsExtractor graphics, Font font, Component text, int centreX, int y, int maxWidth, int color)
    {
        List<FormattedCharSequence> lines = font.split(text, maxWidth);
        if (lines.isEmpty()) return;
        FormattedCharSequence line = lines.getFirst();
        graphics.text(font, line, centreX - font.width(line) / 2, y, color, false);
    }

    // ================= clicks (book pixels) =================

    // What's under the mouse in the list view, for the tooltip; null if no entry there
    static @Nullable ResearchEntry entryAt(int x, int y)
    {
        if (selected != null) return null;
        List<ResearchEntry> entries = tab.entries();
        int start = listPage * PER_SPREAD;
        for (int i = 0; i < PER_SPREAD && start + i < entries.size(); i++) {
            int[] cell = cellPosition(i);
            if (x >= cell[0] && x < cell[0] + CELL && y >= cell[1] && y < cell[1] + CELL) return entries.get(start + i);
        }
        return null;
    }

    // The tab under the mouse, for its name as a tooltip; null if none
    static @Nullable ResearchCategory tabAt(int x, int y)
    {
        if (y < 0 || y >= COVER_TOP) return null;
        ResearchCategory[] categories = ResearchCategory.values();
        for (int i = 0; i < categories.length; i++) {
            int tx = TABS_X + i * (TAB_W + TAB_GAP);
            if (x >= tx && x < tx + TAB_W) return categories[i];
        }
        return null;
    }

    // Opens the book at this entry: the entry itself if it's been discovered, otherwise its tab at the page it's on.
    // null: back to the main page of the open tab.
    static void show(ResearchData data, @Nullable ResearchEntry entry)
    {
        selected = null;
        if (entry == null) return;
        tab = entry.category();
        if (data.has(entry)) {
            selected = entry;
            notesPage = 0;
        } else {
            int index = tab.entries().indexOf(entry);
            listPage = Math.max(0, index) / PER_SPREAD;
        }
    }

    // The page keys: the list's pages on the main page, the notes pages on an entry (clamped when drawn)
    static void turnPage(ResearchData data, int direction)
    {
        if (selected != null && data.has(selected)) notesPage = Math.max(0, notesPage + direction);
        else listPage = Math.max(0, listPage + direction);
    }

    // A click at (x, y). Returns true if it did something.
    static boolean click(ResearchData data, int x, int y)
    {
        // Tabs
        if (y >= 0 && y < COVER_TOP + 2) {
            ResearchCategory[] categories = ResearchCategory.values();
            for (int i = 0; i < categories.length; i++) {
                int tx = TABS_X + i * (TAB_W + TAB_GAP);
                if (x >= tx && x < tx + TAB_W) {
                    tab = categories[i];
                    listPage = 0;
                    selected = null;
                    return true;
                }
            }
            return false;
        }

        boolean footer = y >= FOOTER_Y && y < FOOTER_Y + 12;
        if (selected == null) {
            if (footer && x >= LEFT_X && x < LEFT_X + 16) { listPage--; return true; }
            if (footer && x >= RIGHT_X + PAGE_W - 16 && x < RIGHT_X + PAGE_W) { listPage++; return true; }
            ResearchEntry entry = entryAt(x, y);
            if (entry != null && data.has(entry)) {
                selected = entry;
                notesPage = 0;
                return true;
            }
            return false;
        }

        if (footer && x >= LEFT_X && x < LEFT_X + 40) { selected = null; return true; }
        if (footer && x >= RIGHT_X && x < RIGHT_X + 16) { notesPage = Math.max(0, notesPage - 1); return true; }
        if (footer && x >= RIGHT_X + PAGE_W - 16 && x < RIGHT_X + PAGE_W) { notesPage++; return true; }
        return false;
    }

    // Paper-style drawing: ink text without shadows, slot frames, faded "ghost" items. (Same look as MowLib's PaperGui,
    // which is newer than the MowLib version Dust builds against; switch to it once Dust's MowLib has it.)
    static final class Paper
    {
        static final int INK = 0xFF3B2A1A;
        static final int FADED_INK = 0x993B2A1A;
        private static final int PAPER = 0xFFEDE0BB;
        private static final int PAPER_EDGE = 0xFFC9B48A;
        private static final int SLOT_BORDER = 0xFFA08A5E;
        private static final int SLOT_FILL = 0xFFDCCCA0;
        private static final int SLOT_FADE = 0xB0DCCCA0;

        static void paper(GuiGraphicsExtractor graphics, int x, int y, int width, int height)
        {
            graphics.fill(x, y, x + width, y + height, PAPER_EDGE);
            graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, PAPER);
        }

        static void ink(GuiGraphicsExtractor graphics, Font font, Component text, int x, int y, int color)
        {
            graphics.text(font, text, x, y, color, false);
        }

        static void inkCentred(GuiGraphicsExtractor graphics, Font font, Component text, int x, int y, int color)
        {
            graphics.text(font, text, x - font.width(text) / 2, y, color, false);
        }

        // An 18x18 slot frame with its top-left at (x, y); the item goes at (x + 1, y + 1)
        static void slot(GuiGraphicsExtractor graphics, int x, int y)
        {
            graphics.fill(x, y, x + 18, y + 18, SLOT_BORDER);
            graphics.fill(x + 1, y + 1, x + 17, y + 17, SLOT_FILL);
        }

        // An item drawn faded in a slot frame
        static void ghost(GuiGraphicsExtractor graphics, ItemStack shown, int x, int y)
        {
            graphics.item(shown, x + 1, y + 1);
            graphics.fill(x + 1, y + 1, x + 17, y + 17, SLOT_FADE);
        }
    }
}
