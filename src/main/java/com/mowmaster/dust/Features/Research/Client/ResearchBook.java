package com.mowmaster.dust.Features.Research.Client;

import com.mowmaster.dust.Features.Research.EntryResearch;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchData;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import com.mowmaster.dust.Features.Research.ResearchTab;
import com.mowmaster.dust.Features.Research.ResearchTabs;
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

// Draws the research book, and says what a click on it hits. Used by the side view (ResearchBookHud) and the clickable
// screen (ResearchBookScreen), so they look the same. Everything is in "book pixels": (0, 0) is the book's top-left
// corner and it's WIDTH x HEIGHT; callers move and scale the drawing.
//
//   [tab][tab][tab][tab]                       one tab per registered ResearchTab, along the top
//   +-----------------+-----------------+
//   | left page       | right page      |      list view: one icon per group (block family) over both pages
//   |                 |                 |      entry view: the entry and its drops on the left,
//   +-----------------+-----------------+                  its name, when it was found and notes on the right
//
// What's open (tab, list page, entry, notes page) is remembered here, so the side view shows whatever was last
// opened in the screen.
public final class ResearchBook
{
    public static final int WIDTH = 256;
    public static final int HEIGHT = 188;

    private static final int TAB_MAX_W = 28;
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
    // The family page's grid of members, on the right page under the heading
    private static final int FAMILY_TOP = PAGE_TOP + 52;
    private static final int FAMILY_ROWS = 5;
    // Where the clickable footer row (arrows, back) sits
    static final int FOOTER_Y = PAGE_BOTTOM - 12;

    private static final int COVER = 0xFF5B3A1E;
    private static final int COVER_EDGE = 0xFF3A2410;
    private static final int TAB_OFF = 0xFFB89A6A;
    private static final int TAB_ON = 0xFFEDE0BB;
    static final int INK = Paper.INK;
    static final int FADED = Paper.FADED_INK;

    // ---- what's open ----
    private static @Nullable ResearchTab tab;
    static int listPage = 0;
    static @Nullable ResearchEntry selected = null;
    static int notesPage = 0;

    private ResearchBook() {}

    static ResearchTab tab()
    {
        if (tab == null || ResearchTabs.byId(tab.id()) == null) tab = ResearchTabs.all().getFirst();
        return tab;
    }

    // The notes pages an entry has, depending on how far its research has got
    //   OVERVIEW     the built-in notes, which the player can rewrite (Research Notes only)
    //   WHERE_FOUND  the biomes it's been seen in (once there are two or more)
    //   FAMILY       the other members of its group (block families), unfound ones faded
    //   EXPERIMENTS  drops, health, breeding, crafting
    //   RESEARCH     Dust composition (to come)
    enum NotesPage { OVERVIEW, WHERE_FOUND, FAMILY, EXPERIMENTS, RESEARCH }

    static List<NotesPage> notesPages(ResearchEntry entry, EntryResearch research)
    {
        List<NotesPage> pages = new ArrayList<>();
        pages.add(NotesPage.OVERVIEW);
        if (research.biomes().size() >= 2) pages.add(NotesPage.WHERE_FOUND);
        if (entry.group().size() > 1) pages.add(NotesPage.FAMILY);
        if (research.hasExperiments()) pages.add(NotesPage.EXPERIMENTS);
        pages.add(NotesPage.RESEARCH);
        return pages;
    }

    static @Nullable NotesPage currentNotesPage(ResearchData data)
    {
        if (selected == null) return null;
        EntryResearch research = data.get(selected);
        if (research == null) return null;
        List<NotesPage> pages = notesPages(selected, research);
        notesPage = Math.clamp(notesPage, 0, pages.size() - 1);
        return pages.get(notesPage);
    }

    // ================= drawing =================

    // mouseX/mouseY in book pixels (or negative for no mouse); editing = the screen is showing its notes box
    public static void draw(GuiGraphicsExtractor graphics, Font font, ResearchData data, int mouseX, int mouseY, boolean editing)
    {
        drawTabs(graphics);
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

    // Tabs share the width: up to TAB_MAX_W each, narrower when other mods add more
    private static int tabWidth(int count)
    {
        return Math.min(TAB_MAX_W, (WIDTH - 2 * TABS_X - (count - 1) * TAB_GAP) / Math.max(1, count));
    }

    private static void drawTabs(GuiGraphicsExtractor graphics)
    {
        List<ResearchTab> tabs = ResearchTabs.all();
        int width = tabWidth(tabs.size());
        for (int i = 0; i < tabs.size(); i++) {
            int x = TABS_X + i * (width + TAB_GAP);
            boolean on = tabs.get(i) == tab();
            int top = on ? 0 : 2;
            graphics.fill(x, top, x + width, COVER_TOP + 2, COVER_EDGE);
            graphics.fill(x + 1, top + 1, x + width - 1, COVER_TOP + 2, on ? TAB_ON : TAB_OFF);
            graphics.item(tabs.get(i).tabIcon(), x + (width - 16) / 2, top + 2);
        }
    }

    // ---- list view ----

    private static void drawList(GuiGraphicsExtractor graphics, Font font, ResearchData data)
    {
        ResearchTab tab = tab();
        List<List<Identifier>> groups = tab.groups();
        int pages = Math.max(1, (groups.size() + PER_SPREAD - 1) / PER_SPREAD);
        listPage = Math.clamp(listPage, 0, pages - 1);

        Paper.inkCentred(graphics, font, tab.title(), LEFT_X + PAGE_W / 2, PAGE_TOP + 2, INK);
        Paper.inkCentred(graphics, font, Component.translatable("research.dust.page", listPage + 1, pages), RIGHT_X + PAGE_W / 2, PAGE_TOP + 2, FADED);

        int start = listPage * PER_SPREAD;
        for (int i = 0; i < PER_SPREAD && start + i < groups.size(); i++) {
            List<Identifier> group = groups.get(start + i);
            int[] cell = cellPosition(i);
            Paper.slot(graphics, cell[0], cell[1]);
            // A group shows its first researched member, or (faded) its first member until one is found
            Identifier shown = data.firstResearched(tab, group);
            if (shown != null) graphics.item(tab.icon(shown), cell[0] + 1, cell[1] + 1);
            else Paper.ghost(graphics, tab.icon(group.getFirst()), cell[0], cell[1]);
        }

        // "Discovered 12 / 340" across the bottom (counting list icons), page arrows in the corners
        Component count = Component.translatable("research.dust.discovered", data.countGroups(tab), groups.size());
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

        // Left page: the entry itself, then its drops (or, for items, its sources). Both are icons that open their entry.
        drawSubject(graphics, entry, LEFT_X + 20, PAGE_TOP + 2, LEFT_X + PAGE_W - 20, PAGE_TOP + 62);
        boolean sources = entry.tab().showsSources();
        boolean leftSection = sources || entry.tab().showsDrops();
        if (leftSection) {
            Paper.ink(graphics, font, Component.translatable(sources ? "research.dust.sources" : "research.dust.drops"), LEFT_X + 4, PAGE_TOP + 68, INK);
            List<ItemStack> icons = leftIcons(data, entry).stream().map(ResearchEntry::icon).toList();
            if (icons.isEmpty()) {
                String key = sources ? "research.dust.sources.unknown"
                        : entry.tab().displayEntity(entry.id()) != null ? "research.dust.drops.unknown_mob" : "research.dust.drops.unknown_block";
                drawLines(graphics, font, font.split(Component.translatable(key), PAGE_W - 8), LEFT_X + 4, PAGE_TOP + 80, FOOTER_Y - 2, FADED);
            } else {
                for (int i = 0; i < icons.size() && i < LEFT_ICONS; i++) {
                    int[] cell = leftCell(i);
                    Paper.slot(graphics, cell[0], cell[1]);
                    graphics.item(icons.get(i), cell[0] + 1, cell[1] + 1);
                }
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

        List<NotesPage> pages = notesPages(entry, research);
        NotesPage page = currentNotesPage(data);
        if (page != null) {
            Paper.ink(graphics, font, Component.translatable("research.dust.notes." + page.name().toLowerCase(java.util.Locale.ROOT)), RIGHT_X + 4, PAGE_TOP + 28, INK);
            if (page == NotesPage.FAMILY) {
                drawFamily(graphics, font, data, entry);
            } else if (!(page == NotesPage.OVERVIEW && editing)) {
                drawLines(graphics, font, font.split(notesText(entry, research, page), PAGE_W - 8), RIGHT_X + 4, PAGE_TOP + 40, FOOTER_Y - 2, INK);
            }
        }
        Paper.inkCentred(graphics, font, Component.literal((notesPage + 1) + "/" + pages.size()), rightCentre, FOOTER_Y + 2, FADED);
        if (notesPage > 0) Paper.ink(graphics, font, Component.literal("<"), RIGHT_X + 2, FOOTER_Y + 2, INK);
        if (notesPage < pages.size() - 1) Paper.ink(graphics, font, Component.literal(">"), RIGHT_X + PAGE_W - 8, FOOTER_Y + 2, INK);
    }

    // How many drop / source icons fit on the left page (two rows)
    private static final int LEFT_ICONS = 12;

    // The left page's icons as entries: an item's Sources (researched entries seen dropping it), or an entry's drops
    // (each drop as the entry it belongs to: its block, or the item)
    private static List<ResearchEntry> leftIcons(ResearchData data, ResearchEntry entry)
    {
        EntryResearch research = data.get(entry);
        if (research == null) return List.of();
        if (entry.tab().showsSources()) return data.sourcesOf(entry.id());
        if (!entry.tab().showsDrops()) return List.of();
        List<ResearchEntry> drops = new ArrayList<>();
        for (Identifier item : research.drops()) {
            ResearchEntry drop = ResearchTabs.entryForItem(new ItemStack(BuiltInRegistries.ITEM.getValue(item)));
            if (drop != null && !drops.contains(drop)) drops.add(drop);
        }
        return drops;
    }

    private static int[] leftCell(int i)
    {
        return new int[] {LEFT_X + 3 + (i % 6) * CELL, PAGE_TOP + 79 + (i / 6) * CELL};
    }

    // The drop / source icon under the mouse on an entry's left page, or null
    private static @Nullable ResearchEntry leftIconAt(ResearchData data, int x, int y)
    {
        if (selected == null) return null;
        List<ResearchEntry> icons = leftIcons(data, selected);
        for (int i = 0; i < icons.size() && i < LEFT_ICONS; i++) {
            int[] cell = leftCell(i);
            if (x >= cell[0] && x < cell[0] + CELL && y >= cell[1] && y < cell[1] + CELL) return icons.get(i);
        }
        return null;
    }

    // The family page: "4 of 11 found", then every member as an icon (unfound ones faded). Clicking a found one opens it.
    private static void drawFamily(GuiGraphicsExtractor graphics, Font font, ResearchData data, ResearchEntry entry)
    {
        List<Identifier> group = entry.group();
        int found = 0;
        for (Identifier member : group) {
            if (data.has(new ResearchEntry(entry.tab(), member))) found++;
        }
        Paper.ink(graphics, font, Component.translatable("research.dust.family.found", found, group.size()), RIGHT_X + 4, PAGE_TOP + 40, FADED);
        for (int i = 0; i < group.size() && i < COLS * FAMILY_ROWS; i++) {
            int[] cell = familyCell(i);
            ResearchEntry member = new ResearchEntry(entry.tab(), group.get(i));
            Paper.slot(graphics, cell[0], cell[1]);
            if (data.has(member)) graphics.item(member.icon(), cell[0] + 1, cell[1] + 1);
            else Paper.ghost(graphics, member.icon(), cell[0], cell[1]);
        }
    }

    private static int[] familyCell(int i)
    {
        int gridLeft = RIGHT_X + (PAGE_W - COLS * CELL) / 2;
        return new int[] {gridLeft + (i % COLS) * CELL, FAMILY_TOP + (i / COLS) * CELL};
    }

    // The block as a big item, or the mob itself
    private static void drawSubject(GuiGraphicsExtractor graphics, ResearchEntry entry, int x0, int y0, int x1, int y1)
    {
        LivingEntity mob = ResearchClient.displayEntity(entry);
        if (mob != null) {
            float biggest = Math.max(mob.getBbWidth(), mob.getBbHeight());
            int size = (int) Math.max(4, Math.min(30, 48 / Math.max(1.0F, biggest)));
            try {
                InventoryScreen.renderEntityInInventoryFollowsAngle(graphics, x0, y0, x1, y1, size, 0.0625F, 0.6F, 0.0F, mob);
                return;
            } catch (RuntimeException ignored) {
                // Some mobs can't be drawn outside a world: fall back to the icon
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
            case FAMILY -> Component.empty();   // drawn as icons (drawFamily)
            case EXPERIMENTS -> experiments(entry, research);
            case RESEARCH -> Component.translatable("research.dust.research.unknown");
        };
    }

    // The built-in overview as plain text: what the notes box starts with
    static String builtInNotesText(ResearchEntry entry)
    {
        return builtInNotes(entry).getString();
    }

    // research.<namespace>.note.<path> if the lang file has one, otherwise the tab's general note
    private static Component builtInNotes(ResearchEntry entry)
    {
        String key = entry.noteKey();
        return Language.getInstance().has(key) ? Component.translatable(key) : entry.tab().defaultNote(entry.id());
    }

    private static Component experiments(ResearchEntry entry, EntryResearch research)
    {
        MutableComponent text = Component.empty();
        if (research.maxHealth() > 0) {
            text.append(Component.translatable(label(entry, "health"), Math.round(research.maxHealth()) / 2.0F)).append("\n");
        }
        appendItems(text, label(entry, "breeding"), research.breeding());
        appendItems(text, label(entry, "made_from"), research.madeFrom());
        appendItems(text, label(entry, "makes"), research.makes());
        appendItems(text, label(entry, "modifiers"), research.modifiers());
        return text;
    }

    // An experiment line's wording: research.<tab namespace>.experiment.<tab path>.<line> if the lang file has it (so a
    // tab can reword it: an effect is "Brewed with" rather than "Made from"), otherwise research.dust.experiment.<line>
    private static String label(ResearchEntry entry, String line)
    {
        Identifier tab = entry.tab().id();
        String own = "research." + tab.getNamespace() + ".experiment." + tab.getPath() + "." + line;
        return Language.getInstance().has(own) ? own : "research.dust.experiment." + line;
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

    // ================= what's under the mouse (book pixels) =================

    private static @Nullable ResearchTab tabAt(int x, int y)
    {
        if (y < 0 || y >= COVER_TOP) return null;
        List<ResearchTab> tabs = ResearchTabs.all();
        int width = tabWidth(tabs.size());
        for (int i = 0; i < tabs.size(); i++) {
            int tx = TABS_X + i * (width + TAB_GAP);
            if (x >= tx && x < tx + width) return tabs.get(i);
        }
        return null;
    }

    // The list group under the mouse, or null
    private static @Nullable List<Identifier> groupAt(int x, int y)
    {
        if (selected != null) return null;
        List<List<Identifier>> groups = tab().groups();
        int start = listPage * PER_SPREAD;
        for (int i = 0; i < PER_SPREAD && start + i < groups.size(); i++) {
            int[] cell = cellPosition(i);
            if (x >= cell[0] && x < cell[0] + CELL && y >= cell[1] && y < cell[1] + CELL) return groups.get(start + i);
        }
        return null;
    }

    // The family member under the mouse on the family page, or null
    private static @Nullable ResearchEntry familyMemberAt(ResearchData data, int x, int y)
    {
        if (selected == null || currentNotesPage(data) != NotesPage.FAMILY) return null;
        List<Identifier> group = selected.group();
        for (int i = 0; i < group.size() && i < COLS * FAMILY_ROWS; i++) {
            int[] cell = familyCell(i);
            if (x >= cell[0] && x < cell[0] + CELL && y >= cell[1] && y < cell[1] + CELL) return new ResearchEntry(selected.tab(), group.get(i));
        }
        return null;
    }

    // Tooltip for whatever is under the mouse: a tab's name, an entry's name ("???" until researched), or null
    static @Nullable Component tooltipAt(ResearchData data, int x, int y)
    {
        ResearchTab hoveredTab = tabAt(x, y);
        if (hoveredTab != null) return hoveredTab.title();
        List<Identifier> group = groupAt(x, y);
        if (group != null) {
            Identifier shown = data.firstResearched(tab(), group);
            return shown == null ? Component.translatable("research.dust.undiscovered") : tab().name(shown);
        }
        ResearchEntry member = familyMemberAt(data, x, y);
        if (member != null) return data.has(member) ? member.name() : Component.translatable("research.dust.undiscovered");
        // Drops and sources were seen happening, so their names are always known
        ResearchEntry icon = leftIconAt(data, x, y);
        if (icon != null) return icon.name();
        return null;
    }

    // ================= opening and turning =================

    // What the book opens to:
    //   null                    whatever the side view is showing now (an entry, or the main page)
    //   a discovered entry      that entry
    //   an undiscovered entry   the main page of the open tab
    static void show(ResearchData data, @Nullable ResearchEntry entry)
    {
        if (entry == null) return;
        if (data.has(entry)) {
            tab = entry.tab();
            selected = entry;
            notesPage = 0;
        } else {
            selected = null;
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
        ResearchTab clickedTab = tabAt(x, y);
        if (clickedTab != null) {
            tab = clickedTab;
            listPage = 0;
            selected = null;
            return true;
        }

        boolean footer = y >= FOOTER_Y && y < FOOTER_Y + 12;
        if (selected == null) {
            if (footer && x >= LEFT_X && x < LEFT_X + 16) { listPage--; return true; }
            if (footer && x >= RIGHT_X + PAGE_W - 16 && x < RIGHT_X + PAGE_W) { listPage++; return true; }
            List<Identifier> group = groupAt(x, y);
            Identifier open = group == null ? null : data.firstResearched(tab(), group);
            if (open != null) {
                selected = new ResearchEntry(tab(), open);
                notesPage = 0;
                return true;
            }
            return false;
        }

        // A drop or source icon: open its entry (if it's been researched)
        ResearchEntry linked = leftIconAt(data, x, y);
        if (linked != null && data.has(linked)) {
            tab = linked.tab();
            selected = linked;
            notesPage = 0;
            return true;
        }

        // A found family member: open it, staying on its family page
        ResearchEntry member = familyMemberAt(data, x, y);
        if (member != null && data.has(member)) {
            selected = member;
            EntryResearch research = data.get(member);
            notesPage = research == null ? 0 : Math.max(0, notesPages(member, research).indexOf(NotesPage.FAMILY));
            return true;
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
