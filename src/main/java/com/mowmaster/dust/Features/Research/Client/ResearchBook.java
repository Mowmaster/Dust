package com.mowmaster.dust.Features.Research.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.DustComposition.DustCompositions;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.Features.Machines.ElementCombining;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import com.mowmaster.dust.Features.Machines.AlchemicalDust;
import com.mowmaster.dust.Features.Machines.Compounds;
import com.mowmaster.dust.Features.Machines.ProductSource;
import com.mowmaster.dust.Features.Research.EntryResearch;
import com.mowmaster.dust.Features.Research.ProcessRecord;
import com.mowmaster.dust.Features.Research.RemoveProcessPayload;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import com.mowmaster.dust.Features.Research.RecipeRecord;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchData;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import com.mowmaster.dust.Features.Research.ResearchTab;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.entity.EquipmentSlot;
import com.mowmaster.dust.Features.Research.Tabs.EffectResearchTab;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

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
    //   OVERVIEW      the built-in notes, which the player can rewrite (Research Notes only)
    //   MADE_FROM     an element's combination path: each mix it takes, from the primals up, with the table that puts
    //                 the two together (discovered mixed elements only; as many pages as it takes)
    //   WHERE_FOUND   the biomes it's been seen in (once there are two or more)
    //   FAMILY        the other members of its group (block families), unfound ones faded
    //   CRAFTED_FROM  recipes seen making it: ingredients, station, result (as many pages as it takes)
    //   CRAFTED_INTO  recipes seen using it
    //   EXPERIMENTS   health, breeding food, brewing
    //   RESEARCH      its dust: the colour it burns with (Magical Flint and Steel), what it's made of (Elemental Examiner)
    //   PROCESSES     the steps the player has taken it through: input -> machine -> output, each with an "x" to cross
    //                 it out (as many pages as it takes)
    enum Kind { OVERVIEW, MADE_FROM, WHERE_FOUND, FAMILY, CRAFTED_FROM, CRAFTED_INTO, EXPERIMENTS, RESEARCH, PROCESSES }

    // One notes page: its kind, and for the crafting / process pages the recipes / steps that fit on it
    //   path: the elements whose mix is shown on a MADE_FROM page
    // A record can have extra constructors as long as they call the main one (this(...)): the three-argument one is
    // for every page but MADE_FROM.
    record NotesPage(Kind kind, List<RecipeRecord> recipes, List<ProcessRecord> processes, List<DustElement> path)
    {
        NotesPage(Kind kind, List<RecipeRecord> recipes, List<ProcessRecord> processes)
        {
            this(kind, recipes, processes, List.of());
        }
    }

    static List<NotesPage> notesPages(ResearchEntry entry, EntryResearch research)
    {
        List<NotesPage> pages = new ArrayList<>();
        pages.add(new NotesPage(Kind.OVERVIEW, List.of(), List.of()));
        // An element's combination path, PROCESS_ROWS mixes to a page
        DustElement element = entry.tab() == ResearchTabs.ELEMENTS ? DustElements.get(entry.id()) : null;
        if (element != null && !element.isPrimal()) {
            List<DustElement> path = combinationPath(element);
            for (int i = 0; i < path.size(); i += PROCESS_ROWS) {
                pages.add(new NotesPage(Kind.MADE_FROM, List.of(), List.of(), List.copyOf(path.subList(i, Math.min(path.size(), i + PROCESS_ROWS)))));
            }
        }
        if (research.biomes().size() >= 2) pages.add(new NotesPage(Kind.WHERE_FOUND, List.of(), List.of()));
        if (entry.group().size() > 1) pages.add(new NotesPage(Kind.FAMILY, List.of(), List.of()));
        addRecipePages(pages, Kind.CRAFTED_FROM, research.madeBy());
        addRecipePages(pages, Kind.CRAFTED_INTO, research.usedIn());
        if (research.maxHealth() > 0 || !research.breeding().isEmpty() || !research.madeFrom().isEmpty()
                || !research.makes().isEmpty() || !research.modifiers().isEmpty()
                || research.saturation() >= 0 || !research.foodEffects().isEmpty()
                || research.hits().known() || !research.levels().isEmpty()) {
            pages.add(new NotesPage(Kind.EXPERIMENTS, List.of(), List.of()));
        }
        if (entry.tab().showsDustPage()) pages.add(new NotesPage(Kind.RESEARCH, List.of(), List.of()));
        // Steps, PROCESS_ROWS to a page
        List<ProcessRecord> steps = research.processes();
        for (int i = 0; i < steps.size(); i += PROCESS_ROWS) {
            pages.add(new NotesPage(Kind.PROCESSES, List.of(), List.copyOf(steps.subList(i, Math.min(steps.size(), i + PROCESS_ROWS)))));
        }
        return pages;
    }

    // Splits recipes over as many pages as it takes: each recipe is as tall as its ingredient grid
    private static void addRecipePages(List<NotesPage> pages, Kind kind, List<RecipeRecord> recipes)
    {
        List<RecipeRecord> page = new ArrayList<>();
        int used = 0;
        for (RecipeRecord recipe : recipes) {
            int height = recipeHeight(recipe) + RECIPE_GAP;
            if (!page.isEmpty() && used + height > NOTES_HEIGHT) {
                pages.add(new NotesPage(kind, List.copyOf(page), List.of()));
                page.clear();
                used = 0;
            }
            page.add(recipe);
            used += height;
        }
        if (!page.isEmpty()) pages.add(new NotesPage(kind, List.copyOf(page), List.of()));
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

    // Is the open page an entry's Overview (where the screen shows its notes box)?
    static boolean onOverview(ResearchData data)
    {
        NotesPage page = currentNotesPage(data);
        return page != null && page.kind() == Kind.OVERVIEW;
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
            listIcon(graphics, tab, shown != null ? shown : group.getFirst(), cell[0], cell[1], shown != null);
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

    // One list cell's picture: the tab's list sprite if it has one (effects), otherwise the icon item; faded until found.
    // blitSprite draws a GUI atlas sprite at the given size (effect icons are 18x18, drawn at 16x16 like an item).
    private static void listIcon(GuiGraphicsExtractor graphics, ResearchTab tab, Identifier entry, int x, int y, boolean researched)
    {
        Identifier sprite = tab.listSprite(entry);
        if (sprite == null) graphics.item(tab.icon(entry), x + 1, y + 1);
        else graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x + 1, y + 1, 16, 16, tab.listTint(entry));
        if (!researched) Paper.fade(graphics, x, y);
    }

    // ---- entry view ----

    // Where notes pages start, and how much height they have before the footer
    private static final int NOTES_TOP = PAGE_TOP + 40;
    private static final int NOTES_HEIGHT = FOOTER_Y - 2 - NOTES_TOP;
    private static final int RECIPE_GAP = 4;
    // How many drop / source icons fit on the left page (two rows)
    private static final int LEFT_ICONS = 12;

    private static void drawEntry(GuiGraphicsExtractor graphics, Font font, ResearchData data, ResearchEntry entry, boolean editing)
    {
        EntryResearch research = data.get(entry);
        if (research == null) return;
        LINKS.clear();
        CROSSES.clear();

        // Left page: the entry itself, then its drops (or, for items, its sources), as linked icons
        drawSubject(graphics, entry, LEFT_X + 20, PAGE_TOP + 2, LEFT_X + PAGE_W - 20, PAGE_TOP + 62);
        boolean sources = entry.tab().showsSources();
        if (sources || entry.tab().showsDrops()) {
            Paper.ink(graphics, font, Component.translatable(sources ? "research.dust.sources" : "research.dust.drops"), LEFT_X + 4, PAGE_TOP + 68, INK);
            List<ResearchEntry> sourceEntries = sources ? data.sourcesOf(entry.id()) : List.of();
            int count = sources ? sourceEntries.size() : research.drops().size();
            if (count == 0) {
                String key = sources ? "research.dust.sources.unknown"
                        : entry.tab().displayEntity(entry.id()) != null ? "research.dust.drops.unknown_mob" : "research.dust.drops.unknown_block";
                drawLines(graphics, font, font.split(Component.translatable(key), PAGE_W - 8), LEFT_X + 4, PAGE_TOP + 80, FOOTER_Y - 2, FADED);
            }
            for (int i = 0; i < count && i < LEFT_ICONS; i++) {
                int x = LEFT_X + 3 + (i % 6) * CELL;
                int y = PAGE_TOP + 79 + (i / 6) * CELL;
                if (sources) linkedEntry(graphics, data, sourceEntries.get(i), x, y, false);
                else linkedItem(graphics, data, research.drops().get(i), x, y);
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
            Paper.ink(graphics, font, Component.translatable("research.dust.notes." + page.kind().name().toLowerCase(java.util.Locale.ROOT)), RIGHT_X + 4, PAGE_TOP + 28, INK);
            switch (page.kind()) {
                case FAMILY -> drawFamily(graphics, font, data, entry);
                case CRAFTED_FROM, CRAFTED_INTO -> {
                    int y = NOTES_TOP;
                    for (RecipeRecord recipe : page.recipes()) y += drawRecipe(graphics, font, data, recipe, RIGHT_X + 3, y) + RECIPE_GAP;
                }
                case EXPERIMENTS -> drawExperiments(graphics, font, data, entry, research);
                case PROCESSES -> drawProcesses(graphics, font, data, page.processes());
                case MADE_FROM -> drawCombinations(graphics, font, data, page.path());
                case RESEARCH -> drawDust(graphics, font, data, entry, research.dust());
                case OVERVIEW -> {
                    if (!editing) drawLines(graphics, font, font.split(notesText(entry, research, page.kind()), PAGE_W - 8), RIGHT_X + 4, NOTES_TOP, FOOTER_Y - 2, INK);
                }
                default -> drawLines(graphics, font, font.split(notesText(entry, research, page.kind()), PAGE_W - 8), RIGHT_X + 4, NOTES_TOP, FOOTER_Y - 2, INK);
            }
        }
        Paper.inkCentred(graphics, font, Component.literal((notesPage + 1) + "/" + pages.size()), rightCentre, FOOTER_Y + 2, FADED);
        if (notesPage > 0) Paper.ink(graphics, font, Component.literal("<"), RIGHT_X + 2, FOOTER_Y + 2, INK);
        if (notesPage < pages.size() - 1) Paper.ink(graphics, font, Component.literal(">"), RIGHT_X + PAGE_W - 8, FOOTER_Y + 2, INK);
    }

    // ---- linked icons ----
    // Every item shown on an entry's pages is an icon in a slot frame: normal if its entry has been researched (clicking
    // it opens that entry), faded if not. The icons drawn this frame are remembered for clicks and tooltips.

    // family: a member on the family page (clicking keeps the family page open; unresearched ones are "???")
    private record Link(int x, int y, @Nullable ResearchEntry entry, boolean researched, Component name, boolean family) {}
    private static final List<Link> LINKS = new ArrayList<>();

    private static void linkedItem(GuiGraphicsExtractor graphics, ResearchData data, Identifier item, int x, int y)
    {
        linkedItem(graphics, null, data, item, 1, x, y);
    }

    // An item icon with a stack count in the corner when count > 1 (font is only needed then)
    private static void linkedItem(GuiGraphicsExtractor graphics, @Nullable Font font, ResearchData data, Identifier item, int count, int x, int y)
    {
        ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.getValue(item));
        ResearchEntry entry = ResearchTabs.entryForItem(stack);
        boolean researched = entry != null && data.has(entry);
        drawIcon(graphics, stack, x, y, researched);
        // The count is passed as text rather than set on the stack, so it works for items that don't stack (a recipe
        // can use 3 buckets). Drawn after the ghost fade so it stays readable on unresearched items too.
        if (count > 1 && font != null) graphics.itemDecorations(font, stack, x + 1, y + 1, String.valueOf(count));
        LINKS.add(new Link(x, y, entry, researched, stack.getHoverName(), false));
    }

    private static void linkedEntry(GuiGraphicsExtractor graphics, ResearchData data, ResearchEntry entry, int x, int y, boolean family)
    {
        boolean researched = data.has(entry);
        drawIcon(graphics, entry.icon(), x, y, researched);
        LINKS.add(new Link(x, y, entry, researched, entry.name(), family));
    }

    private static void drawIcon(GuiGraphicsExtractor graphics, ItemStack stack, int x, int y, boolean researched)
    {
        Paper.slot(graphics, x, y);
        if (researched) graphics.item(stack, x + 1, y + 1);
        else Paper.ghost(graphics, stack, x, y);
    }

    private static @Nullable Link linkAt(int x, int y)
    {
        if (selected == null) return null;
        for (Link link : LINKS) {
            if (x >= link.x() && x < link.x() + CELL && y >= link.y() && y < link.y() + CELL) return link;
        }
        return null;
    }

    // ---- crafting pages ----

    // Ingredients per row on a crafting page (the station column starts after this many cells)
    private static final int INGREDIENT_COLS = 3;

    // Each different ingredient once, with how many times the recipe uses it, in the order they first appear.
    // LinkedHashMap is a HashMap that remembers insertion order; merge(key, 1, Integer::sum) puts 1 for a new key and
    // adds 1 to an existing one. Integer::sum is a method reference: shorthand for the lambda (a, b) -> a + b.
    private static List<Map.Entry<Identifier, Integer>> ingredients(RecipeRecord recipe)
    {
        Map<Identifier, Integer> counts = new LinkedHashMap<>();
        for (Identifier slot : recipe.slots()) {
            if (!slot.equals(RecipeRecord.EMPTY)) counts.merge(slot, 1, Integer::sum);
        }
        return List.copyOf(counts.entrySet());
    }

    private static int recipeHeight(RecipeRecord recipe)
    {
        int rows = (ingredients(recipe).size() + INGREDIENT_COLS - 1) / INGREDIENT_COLS;
        return Math.max(1, rows) * CELL;
    }

    // One recipe as  [ingredients]  [station]  ->  [result]. Each ingredient is shown once, with a count when it's used
    // more than once (4 planks -> crafting table is a single planks icon marked 4). Returns its height.
    private static int drawRecipe(GuiGraphicsExtractor graphics, Font font, ResearchData data, RecipeRecord recipe, int x, int y)
    {
        List<Map.Entry<Identifier, Integer>> ingredients = ingredients(recipe);
        for (int i = 0; i < ingredients.size(); i++) {
            int cx = x + (i % INGREDIENT_COLS) * CELL;
            int cy = y + (i / INGREDIENT_COLS) * CELL;
            Map.Entry<Identifier, Integer> ingredient = ingredients.get(i);
            linkedItem(graphics, font, data, ingredient.getKey(), ingredient.getValue(), cx, cy);
        }
        int height = recipeHeight(recipe);
        int middle = y + (height - CELL) / 2;
        // Fixed columns, so recipes of different sizes line up: ingredients up to 3 wide, then station, arrow, result
        recipe.station().ifPresent(station -> linkedItem(graphics, data, station, x + INGREDIENT_COLS * CELL + 4, middle));
        Paper.ink(graphics, font, Component.literal("\u2192"), x + 4 * CELL + 7, middle + 5, INK);
        linkedItem(graphics, data, recipe.output(), x + 4 * CELL + 18, middle);
        return height;
    }

    // ---- experiments page: health as text, everything else as rows of icons ----

    private static void drawExperiments(GuiGraphicsExtractor graphics, Font font, ResearchData data, ResearchEntry entry, EntryResearch research)
    {
        int y = NOTES_TOP;
        int bottom = FOOTER_Y - 2;
        if (research.maxHealth() > 0) {
            Paper.ink(graphics, font, Component.translatable(label(entry, "health"), Math.round(research.maxHealth()) / 2.0F), RIGHT_X + 4, y, INK);
            y += font.lineHeight + 3;
        }
        if (research.hits().known()) {
            EntryResearch.Hits hits = research.hits();
            // "1.5" for a single value seen, "1.5-3" once different hits have been taken
            String range = hits.min() == hits.max() ? hearts(hits.max()) : hearts(hits.min()) + "-" + hearts(hits.max());
            Paper.ink(graphics, font, Component.translatable(label(entry, "hits"), range), RIGHT_X + 4, y, INK);
            y += font.lineHeight + 3;
        }
        y = drawLevels(graphics, font, entry, research.levels(), y, bottom);
        if (research.saturation() >= 0) {
            // One decimal place (steak gives 12.8). Locale.ROOT so it's always a dot, whatever the system language.
            String saturation = String.format(java.util.Locale.ROOT, "%.1f", research.saturation());
            Paper.ink(graphics, font, Component.translatable(label(entry, "saturation"), saturation), RIGHT_X + 4, y, INK);
            y += font.lineHeight + 3;
        }
        y = effectRow(graphics, font, data, Component.translatable(label(entry, "food_effects"), ""), research.foodEffects(), y, bottom);
        y = iconRow(graphics, font, data, Component.translatable(label(entry, "breeding"), ""), research.breeding(), y, bottom);
        y = iconRow(graphics, font, data, Component.translatable(label(entry, "made_from"), ""), research.madeFrom(), y, bottom);
        y = iconRow(graphics, font, data, Component.translatable(label(entry, "makes"), ""), research.makes(), y, bottom);
        iconRow(graphics, font, data, Component.translatable(label(entry, "modifiers"), ""), research.modifiers(), y, bottom);
    }

    // A heading and its items as icons (6 to a row). Returns where the next one starts.
    private static int iconRow(GuiGraphicsExtractor graphics, Font font, ResearchData data, Component heading, List<Identifier> items, int y, int bottom)
    {
        if (items.isEmpty() || y + font.lineHeight + CELL > bottom) return y;
        Paper.ink(graphics, font, heading, RIGHT_X + 4, y, INK);
        y += font.lineHeight + 1;
        for (int i = 0; i < items.size(); i++) {
            int row = i / COLS;
            int cy = y + row * CELL;
            if (cy + CELL > bottom) break;
            linkedItem(graphics, data, items.get(i), RIGHT_X + 3 + (i % COLS) * CELL, cy);
        }
        return y + ((items.size() + COLS - 1) / COLS) * CELL + 3;
    }

    // Health points as hearts, with a half if needed and no ".0": 7 -> "3.5", 6 -> "3"
    private static String hearts(float health)
    {
        float hearts = Math.round(health) / 2.0F;
        return hearts == (int) hearts ? String.valueOf((int) hearts) : String.valueOf(hearts);
    }

    // An effect's or enchantment's levels that the player has had: "Speed II", then what one level does.
    //   Instant effects    the health one dose gave or took
    //   Lasting effects    the attribute changes, worked out from the effect: MobEffect#createModifiers(amplifier,
    //                      consumer) hands every (attribute, modifier) pair the effect applies at that level to the
    //                      consumer (a BiConsumer: a function taking two values and returning nothing)
    //   Enchantments       the attribute changes in the enchantment's "minecraft:attributes" effects (Efficiency,
    //                      Swift Sneak, Depth Strider, Respiration...). Other enchantment effects (extra damage, fire,
    //                      drops) are data with no general way to describe them, so only the level is shown for those.
    // NeoForge's Attribute#toComponent turns each attribute change into the same "+40% Speed" line tooltips show.
    private static int drawLevels(GuiGraphicsExtractor graphics, Font font, ResearchEntry entry, List<EntryResearch.EffectLevel> levels, int y, int bottom)
    {
        if (levels.isEmpty() || entry.tab() != ResearchTabs.EFFECTS) return y;
        boolean enchantment = EffectResearchTab.isEnchantment(entry.id());
        Optional<Holder.Reference<MobEffect>> effect = enchantment ? Optional.empty() : BuiltInRegistries.MOB_EFFECT.get(entry.id());
        Optional<Holder.Reference<Enchantment>> enchant = enchantment ? EffectResearchTab.enchantment(entry.id()) : Optional.empty();
        if (effect.isEmpty() && enchant.isEmpty()) return y;
        int step = font.lineHeight + 1;
        for (EntryResearch.EffectLevel level : levels) {
            if (y + step > bottom) break;
            List<Component> lines = new ArrayList<>();
            Component heading;
            if (enchant.isPresent()) {
                int enchantLevel = level.amplifier() + 1;
                heading = enchantmentName(enchant.get(), enchantLevel);
                for (EnchantmentAttributeEffect attribute : enchant.get().value().getEffects(EnchantmentEffectComponents.ATTRIBUTES)) {
                    lines.add(attribute.attribute().value().toComponent(attribute.getModifier(enchantLevel, EquipmentSlot.MAINHAND), TooltipFlag.NORMAL));
                }
            } else {
                // potion.withAmplifier: "Speed II" (level I is just "Speed")
                heading = PotionContents.getPotionDescription(effect.get(), level.amplifier());
                if (level.health() > 0) lines.add(Component.translatable("research.dust.experiment.heals", hearts(level.health())));
                if (level.health() < 0) lines.add(Component.translatable("research.dust.experiment.harms", hearts(-level.health())));
                effect.get().value().createModifiers(level.amplifier(), (attribute, modifier) -> lines.add(attribute.value().toComponent(modifier, TooltipFlag.NORMAL)));
            }
            Paper.ink(graphics, font, heading, RIGHT_X + 4, y, INK);
            y += step;
            for (Component line : lines) {
                for (FormattedCharSequence part : font.split(line, PAGE_W - 14)) {
                    if (y + step > bottom) return y;
                    graphics.text(font, part, RIGHT_X + 10, y, INK, false);
                    y += step;
                }
            }
            y += 2;
        }
        return y + 1;
    }

    // "Sharpness III" in plain ink. (Enchantment.getFullname colours it grey or red for tooltips, which is hard to
    // read on the paper.) Like vanilla, an enchantment that only has level I shows no numeral.
    private static Component enchantmentName(Holder<Enchantment> enchantment, int level)
    {
        MutableComponent name = enchantment.value().description().copy();
        if (level != 1 || enchantment.value().getMaxLevel() != 1) {
            name.append(" ").append(Component.translatable("enchantment.level." + level));
        }
        return name;
    }

    // A heading and effects as their status effect icons (the ones beside the inventory), 6 to a row, each linking to
    // its Effects entry. Returns where the next row starts.
    private static int effectRow(GuiGraphicsExtractor graphics, Font font, ResearchData data, Component heading, List<Identifier> effects, int y, int bottom)
    {
        if (effects.isEmpty() || y + font.lineHeight + CELL > bottom) return y;
        Paper.ink(graphics, font, heading, RIGHT_X + 4, y, INK);
        y += font.lineHeight + 1;
        for (int i = 0; i < effects.size(); i++) {
            int cy = y + (i / COLS) * CELL;
            if (cy + CELL > bottom) break;
            linkedEffect(graphics, data, effects.get(i), RIGHT_X + 3 + (i % COLS) * CELL, cy);
        }
        return y + ((effects.size() + COLS - 1) / COLS) * CELL + 3;
    }

    // An effect's icon in a slot frame (faded if its Effects entry isn't researched yet); clicking opens the entry.
    // Hud.getMobEffectSprite gives the sprite id of the effect's icon in the GUI atlas; blitSprite draws a sprite
    // from the atlas at the given size (the icons are 18x18, drawn at 16x16 here to fit the slot like an item).
    private static void linkedEffect(GuiGraphicsExtractor graphics, ResearchData data, Identifier effectId, int x, int y)
    {
        // registry.get(id) gives an Optional holder: empty if the effect's mod was removed since it was noted
        Optional<Holder.Reference<MobEffect>> effect = BuiltInRegistries.MOB_EFFECT.get(effectId);
        if (effect.isEmpty()) return;
        ResearchEntry entry = new ResearchEntry(ResearchTabs.EFFECTS, effectId);
        boolean researched = data.has(entry);
        Paper.slot(graphics, x, y);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Hud.getMobEffectSprite(effect.get()), x + 1, y + 1, 16, 16);
        if (!researched) Paper.fade(graphics, x, y);
        LINKS.add(new Link(x, y, entry, researched, effect.get().value().getDisplayName(), false));
    }

    // ---- processes pages ----

    private static final int PROCESS_ROW = 21;
    private static final int PROCESS_ROWS = 5;

    // The "x" boxes drawn on the open processes page, for clicks (filled each frame, like LINKS)
    private record Cross(int x, int y, ProcessRecord process) {}
    private static final List<Cross> CROSSES = new ArrayList<>();
    // Whether crossing out works: true for your own Research Notes, false for a Shared Research Guide (set by the screen)
    static boolean editable = true;

    // Each step as  [input] -> [machine] -> [outputs]  x   (icons link to their entries like everywhere else)
    private static void drawProcesses(GuiGraphicsExtractor graphics, Font font, ResearchData data, List<ProcessRecord> steps)
    {
        int y = NOTES_TOP;
        for (ProcessRecord step : steps) {
            int x = RIGHT_X + 1;
            if (!step.with().isEmpty()) {
                drawTwoInputStep(graphics, font, data, step, x, y);
                y += PROCESS_ROW;
                continue;
            }
            linkedStack(graphics, data, step.input().create(), x, y);
            Paper.ink(graphics, font, Component.literal("\u2192"), x + CELL, y + 5, INK);
            linkedItem(graphics, data, step.machine(), x + CELL + 6, y);
            Paper.ink(graphics, font, Component.literal("\u2192"), x + 2 * CELL + 6, y + 5, INK);
            // Up to three outputs side by side, touching (the distillation table makes three), clear of the "x"
            for (int i = 0; i < step.outputs().size() && i < 3; i++) {
                linkedStack(graphics, data, step.outputs().get(i).create(), x + 2 * CELL + 12 + i * (CELL - 1), y);
            }
            if (editable) {
                int crossX = RIGHT_X + PAGE_W - 12;
                Paper.ink(graphics, font, Component.literal("x"), crossX + 2, y + 5, FADED);
                CROSSES.add(new Cross(crossX, y + 3, step));
            }
            y += PROCESS_ROW;
        }
    }

    // ---- made-from pages: an element's combination path ----

    // Every mix it takes to make this element, the primals' first: a post-order walk of its parts (each part's own
    // mixes before the mix that uses it), each element once
    static List<DustElement> combinationPath(DustElement element)
    {
        List<DustElement> path = new ArrayList<>();
        addMixes(element, path);
        return path;
    }

    private static void addMixes(DustElement element, List<DustElement> path)
    {
        if (element.isPrimal() || path.contains(element)) return;
        for (DustElement part : element.parts()) addMixes(part, path);
        path.add(element);
    }

    // Each mix as  [part] + [part] -> [table] -> [element]  (the Combination Table for two solids, the Mixing Table
    // otherwise); the element icons link to their Elements entries, faded and named ??? until discovered
    private static void drawCombinations(GuiGraphicsExtractor graphics, Font font, ResearchData data, List<DustElement> path)
    {
        Identifier combination = BuiltInRegistries.ITEM.getKey(DustBlockRegistry.RESEARCH_COMBINATION.get().asItem());
        Identifier mixing = BuiltInRegistries.ITEM.getKey(DustBlockRegistry.RESEARCH_MIXING.get().asItem());
        int y = NOTES_TOP;
        for (DustElement element : path) {
            int x = RIGHT_X + 3;
            DustElement a = element.parts().get(0), b = element.parts().get(1);
            linkedElement(graphics, data, a, x, y);
            Paper.ink(graphics, font, Component.literal("+"), x + CELL + 1, y + 5, INK);
            linkedElement(graphics, data, b, x + CELL + 7, y);
            Paper.ink(graphics, font, Component.literal("\u2192"), x + 2 * CELL + 7, y + 5, INK);
            linkedItem(graphics, data, ElementCombining.combinedBySolids(a, b) ? combination : mixing, x + 2 * CELL + 14, y);
            Paper.ink(graphics, font, Component.literal("\u2192"), x + 3 * CELL + 14, y + 5, INK);
            linkedElement(graphics, data, element, x + 3 * CELL + 21, y);
            y += PROCESS_ROW;
        }
    }

    // An element's icon (its greyscale sprite tinted its colour) in a slot, linking to its Elements entry
    private static void linkedElement(GuiGraphicsExtractor graphics, ResearchData data, DustElement element, int x, int y)
    {
        ResearchEntry entry = new ResearchEntry(ResearchTabs.ELEMENTS, element.id());
        boolean discovered = data.has(entry);
        Paper.slot(graphics, x, y);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, element.icon(), x + 1, y + 1, 16, 16, 0xFF000000 | element.color());
        if (!discovered) Paper.fade(graphics, x, y);
        LINKS.add(new Link(x, y, entry, discovered, discovered ? element.name() : Component.translatable("research.dust.undiscovered"), false));
    }

    // A two-input step (the Combination and Mixing Tables):  [dust] + [dust] -> [table] -> [result]  x
    private static void drawTwoInputStep(GuiGraphicsExtractor graphics, Font font, ResearchData data, ProcessRecord step, int x, int y)
    {
        linkedStack(graphics, data, step.input().create(), x, y);
        Paper.ink(graphics, font, Component.literal("+"), x + CELL + 1, y + 5, INK);
        linkedStack(graphics, data, step.with().getFirst().create(), x + CELL + 7, y);
        Paper.ink(graphics, font, Component.literal("\u2192"), x + 2 * CELL + 7, y + 5, INK);
        linkedItem(graphics, data, step.machine(), x + 2 * CELL + 14, y);
        Paper.ink(graphics, font, Component.literal("\u2192"), x + 3 * CELL + 14, y + 5, INK);
        if (!step.outputs().isEmpty()) linkedStack(graphics, data, step.outputs().getFirst().create(), x + 3 * CELL + 21, y);
        if (editable) {
            int crossX = RIGHT_X + PAGE_W - 12;
            Paper.ink(graphics, font, Component.literal("x"), crossX + 2, y + 5, FADED);
            CROSSES.add(new Cross(crossX, y + 3, step));
        }
    }

    // An item stack (with its data: crushed stone shows as pebbles of stone) as a linked icon. Products link to, and
    // are faded by, the material they were made from. An alchemical compound's name says which elements are in it,
    // but only the ones the player has discovered: "Alchemical Compound (Fire, ???)".
    private static void linkedStack(GuiGraphicsExtractor graphics, ResearchData data, ItemStack stack, int x, int y)
    {
        // Alchemical Dust links to its element's entry: "Alchemical Dust (Stone)", or ??? until it's discovered
        DustElement dust = AlchemicalDust.of(stack);
        if (dust != null) {
            ResearchEntry element = new ResearchEntry(ResearchTabs.ELEMENTS, dust.id());
            boolean discovered = data.has(element);
            drawIcon(graphics, stack, x, y, true);
            Component known = discovered ? dust.name() : Component.translatable("research.dust.undiscovered");
            LINKS.add(new Link(x, y, element, discovered, Component.translatable("research.dust.process.compound", stack.getHoverName(), known), false));
            return;
        }
        ResearchEntry entry = ResearchTabs.entryForItem(ProductSource.original(stack));
        boolean researched = entry != null && data.has(entry);
        drawIcon(graphics, stack, x, y, researched);
        Component name = stack.getHoverName();
        DustComposition compound = Compounds.of(stack);
        if (compound != null) {
            Component elements = Compounds.describe(compound, element -> data.has(new ResearchEntry(ResearchTabs.ELEMENTS, element.id())));
            name = Component.translatable("research.dust.process.compound", name, elements);
        }
        LINKS.add(new Link(x, y, entry, researched, name, false));
    }

    private static @Nullable Cross crossAt(int x, int y)
    {
        for (Cross cross : CROSSES) {
            if (x >= cross.x() && x < cross.x() + 10 && y >= cross.y() && y < cross.y() + 12) return cross;
        }
        return null;
    }

    // ---- research page: its dust ----

    private static final Identifier FLAME_SPRITE = Identifier.fromNamespaceAndPath(DustReferences.MODID, "research/flame");

    // What's known of its dust:
    //   burned     "Burns with:" and a flame in that colour
    //   measured   "Made of:" and each element the Elemental Examiner found in it, with its icon and percent
    //              ("Water 33%")
    //   neither    a hint at how to find out
    // The sprites are greyscale; blitSprite's last argument tints them (0xAARRGGBB), the way a block tint colours a
    // texture.
    // Only elements the player has discovered are listed: the rest stay noted but hidden, and show up once they're in
    // the Elements tab.
    // Only the material's own elements are listed (readings of anything deeper, noted by older versions, are skipped).
    private static void drawDust(GuiGraphicsExtractor graphics, Font font, ResearchData data, ResearchEntry entry, EntryResearch.Dust dust)
    {
        DustComposition madeOf = DustCompositions.of(entry.icon());
        int x = RIGHT_X + 4;
        int y = NOTES_TOP;
        int bottom = FOOTER_Y - 2;
        // Only elements that are there (0% isn't noted; older notes may still have some, so they're skipped too) and
        // that the player has discovered
        List<Map.Entry<DustElement, Integer>> shown = dust.measured().entrySet().stream()
                .filter(measured -> measured.getValue() > 0 && data.has(new ResearchEntry(ResearchTabs.ELEMENTS, measured.getKey().id())))
                .filter(measured -> madeOf == null || madeOf.elements().contains(measured.getKey()))
                .toList();
        if (!dust.burned() && shown.isEmpty()) {
            drawLines(graphics, font, font.split(Component.translatable("research.dust.research.unknown"), PAGE_W - 8), x, y, bottom, INK);
            return;
        }
        if (dust.burned()) {
            Paper.ink(graphics, font, Component.translatable("research.dust.research.flame"), x, y + 8, INK);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FLAME_SPRITE, RIGHT_X + PAGE_W - 30, y, 24, 24, 0xFF000000 | dust.fireColor());
            y += 27;
        }
        if (shown.isEmpty()) {
            if (y + font.lineHeight <= bottom) drawLines(graphics, font, font.split(Component.translatable("research.dust.research.unread"), PAGE_W - 8), x, y, bottom, FADED);
            return;
        }
        Paper.ink(graphics, font, Component.translatable("research.dust.research.made_of"), x, y, INK);
        y += font.lineHeight + 2;
        for (Map.Entry<DustElement, Integer> measured : shown) {
            if (y + 10 > bottom) continue;
            DustElement element = measured.getKey();
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, element.icon(), x + 1, y - 1, 10, 10, 0xFF000000 | element.color());
            Paper.ink(graphics, font, Component.translatable("research.dust.research.percent", element.name(), measured.getValue()), x + 14, y, INK);
            y += 11;
        }
    }

    // ---- family page ----

    // "4 of 11 found", then every member as an icon (unfound ones faded). Clicking a found one opens it.
    private static void drawFamily(GuiGraphicsExtractor graphics, Font font, ResearchData data, ResearchEntry entry)
    {
        List<Identifier> group = entry.group();
        int found = 0;
        for (Identifier member : group) {
            if (data.has(new ResearchEntry(entry.tab(), member))) found++;
        }
        Paper.ink(graphics, font, Component.translatable("research.dust.family.found", found, group.size()), RIGHT_X + 4, NOTES_TOP, FADED);
        int gridLeft = RIGHT_X + (PAGE_W - COLS * CELL) / 2;
        for (int i = 0; i < group.size() && i < COLS * FAMILY_ROWS; i++) {
            linkedEntry(graphics, data, new ResearchEntry(entry.tab(), group.get(i)),
                    gridLeft + (i % COLS) * CELL, FAMILY_TOP + (i / COLS) * CELL, true);
        }
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
        // A tab's own sprite (an element's icon) if it has one, at three times size, else its item
        Identifier sprite = entry.tab().listSprite(entry.id());
        int left = (x0 + x1) / 2 - 24, top = (y0 + y1) / 2 - 24;
        if (sprite != null) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, left, top, 48, 48, entry.tab().listTint(entry.id()));
            return;
        }
        graphics.pose().pushMatrix();
        graphics.pose().translate(left, top);
        graphics.pose().scale(3.0F, 3.0F);
        graphics.item(entry.icon(), 0, 0);
        graphics.pose().popMatrix();
    }

    // The text of a text notes page
    private static Component notesText(ResearchEntry entry, EntryResearch research, Kind kind)
    {
        return switch (kind) {
            // The player's version if they've written one, otherwise the built-in notes
            case OVERVIEW -> research.notes().isEmpty() ? builtInNotes(entry) : Component.literal(research.notes());
            case WHERE_FOUND -> lines(Component.translatable("research.dust.where_found.intro"),
                    research.biomes().stream().map(ResearchBook::biomeName).toList());
            default -> Component.empty();   // drawn as icons
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

    // An experiment line's wording: research.<tab namespace>.experiment.<tab path>.<line> if the lang file has it (so a
    // tab can reword it: an effect is "Brewed with" rather than "Made from"), otherwise research.dust.experiment.<line>
    private static String label(ResearchEntry entry, String line)
    {
        Identifier tab = entry.tab().id();
        String own = "research." + tab.getNamespace() + ".experiment." + tab.getPath() + "." + line;
        return Language.getInstance().has(own) ? own : "research.dust.experiment." + line;
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
        // Linked icons: an item's name (things seen in recipes and drops are known by sight); unfound family members "???"
        if (crossAt(x, y) != null && editable) return Component.translatable("research.dust.process.remove");
        Link link = linkAt(x, y);
        if (link != null) return link.family() && !link.researched() ? Component.translatable("research.dust.undiscovered") : link.name();
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

        // An "x" on a processes page: cross that step out (the server removes it; the page updates when it syncs back)
        Cross cross = editable ? crossAt(x, y) : null;
        if (cross != null) {
            ClientPacketDistributor.sendToServer(new RemoveProcessPayload(selected.tab().id(), selected.id(), cross.process()));
            return true;
        }

        // A linked icon: open its entry if it's been researched (a family member keeps the family page open)
        Link link = linkAt(x, y);
        if (link != null && link.researched() && link.entry() != null) {
            ResearchEntry target = link.entry();
            tab = target.tab();
            selected = target;
            notesPage = 0;
            if (link.family()) {
                EntryResearch research = data.get(target);
                List<NotesPage> pages = research == null ? List.of() : notesPages(target, research);
                for (int i = 0; i < pages.size(); i++) {
                    if (pages.get(i).kind() == Kind.FAMILY) { notesPage = i; break; }
                }
            }
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
            fade(graphics, x, y);
        }

        // Fades whatever is drawn in the slot at (x, y)
        static void fade(GuiGraphicsExtractor graphics, int x, int y)
        {
            graphics.fill(x + 1, y + 1, x + 17, y + 17, SLOT_FADE);
        }
    }
}
