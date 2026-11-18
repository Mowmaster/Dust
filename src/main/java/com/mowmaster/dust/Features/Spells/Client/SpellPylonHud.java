package com.mowmaster.dust.Features.Spells.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.SpellNames;
import com.mowmaster.dust.Features.Spells.grimoire.AkashicItem;
import com.mowmaster.dust.Features.Spells.pylons.SpellDeck;
import com.mowmaster.dust.Features.Spells.pylons.SpellLevels;
import com.mowmaster.dust.Features.Spells.pylons.SpellPylonBlockEntity;
import com.mowmaster.dust.Features.Spells.pylons.SpellPylonReading;
import com.mowmaster.mowlib.client.HudAnchor;
import com.mowmaster.mowlib.client.MowLibClientConfig;
import com.mowmaster.mowlib.client.MowLibKeyMappings;
import com.mowmaster.mowlib.client.SlotGrid;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// The Spell Pylon's HUD, while it's looked at: a sheet of paper where MowLib's reading HUD goes (same paper, same place:
// MowLib's [paper] client config), laid out as
//   Spell Pylon
//   [icon] Fire  Lvl: 3  5/13 xp
//
//   Deck (12)
//   [the deck's cards, one slot each, with how many]
//   Spell Break x1, ...                 the special cards, which have no card picture
//   <the selected card's name>
//
//   Pool (20)                           only while the player holds the Akashic
//   [the pool's cards, with uses left]
//   <the selected card's name>
// MowLib's page keys (next / previous page) step through the cards, the deck's then the pool's; the selected one is
// ringed and named under its grid. Nothing is selected (no name) until a key is pressed, and looking away (or at
// another Spell Pylon) clears it. The keys are read here before MowLib's own tick handler gets them
// (ClientTickEvent.Pre comes first), and only while a Spell Pylon is looked at.
// (MowLib's reading paper only has room for one grid under its text, so this draws its own.)
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class SpellPylonHud
{
    private static final Identifier PAPER = Identifier.fromNamespaceAndPath("mowlib", "reading_paper");
    private static final int INK = 0xFF3B2A1A;
    private static final int FADED = 0xA03B2A1A;
    private static final int SLOT_FILL = 0x40553A1C;
    private static final int SLOT_EDGE = 0x80553A1C;
    private static final int PADDING = 10;
    private static final int LINE = 10;
    private static final int COLUMNS = 8;
    private static final int ICON = 9;

    // What's looked at, and which card is selected (-1: none; deck cards first, then pool cards)
    private static @Nullable BlockPos lookedAt;
    private static int selected = -1;

    private SpellPylonHud() {}

    // ---- what's looked at, and the keys ----

    private static @Nullable SpellPylonBlockEntity lookedAtPylon()
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) return null;
        // 26.2 moved the open screen to Minecraft.gui
        //? if >=26.2 {
        if (minecraft.gui.screen() != null) return null;
        //?} else {
        /*if (minecraft.screen != null) return null;
        *///?}
        HitResult hit = minecraft.hitResult;
        if (!(hit instanceof BlockHitResult block) || hit.getType() != HitResult.Type.BLOCK) return null;
        return minecraft.level.getBlockEntity(block.getBlockPos()) instanceof SpellPylonBlockEntity pylon ? pylon : null;
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event)
    {
        SpellPylonBlockEntity pylon = lookedAtPylon();
        BlockPos now = pylon == null ? null : pylon.getBlockPos();
        // Looked away, or at another one: start again with no card selected
        if (now == null || !now.equals(lookedAt)) selected = -1;
        lookedAt = now;
        if (pylon == null) return;
        int count = deckCards(pylon).size() + (showsPool() ? poolCards(pylon).size() : 0);
        // consumeClick: true once for each press since the last call; taking them here leaves none for MowLib
        while (MowLibKeyMappings.PAGE_NEXT.consumeClick()) step(1, count);
        while (MowLibKeyMappings.PAGE_PREVIOUS.consumeClick()) step(-1, count);
    }

    private static void step(int direction, int count)
    {
        if (count <= 0) return;
        if (selected < 0) selected = direction > 0 ? 0 : count - 1;
        else selected = Math.floorMod(selected + direction, count);
    }

    private static boolean showsPool()
    {
        var player = Minecraft.getInstance().player;
        return player != null && AkashicItem.holds(player);
    }

    // ---- the cards ----

    // A card and how many (-1: no limit, shown without a count); blocked: it can't go on this spell's pylons any more
    // (greyed out: SpellPylonBlockEntity.blockedCards)
    private record Entry(Identifier card, int count, boolean blocked) {}

    // The deck's modifier cards, each once with how many (in deck order)
    private static List<Entry> deckCards(SpellPylonBlockEntity pylon)
    {
        Map<Identifier, Integer> counted = new LinkedHashMap<>();
        for (Identifier card : pylon.deck().cards()) {
            if (!SpellDeck.isSpecial(card)) counted.merge(card, 1, Integer::sum);
        }
        List<Entry> entries = new ArrayList<>();
        counted.forEach((card, count) -> entries.add(new Entry(card, count, pylon.blockedCards().contains(card))));
        return entries;
    }

    // The pool's cards with their uses left (used-up ones left out)
    private static List<Entry> poolCards(SpellPylonBlockEntity pylon)
    {
        List<Entry> entries = new ArrayList<>();
        pylon.deck().pool().forEach((card, uses) -> {
            if (uses != 0) entries.add(new Entry(card, uses, pylon.blockedCards().contains(card)));
        });
        return entries;
    }

    // The selected card's name, with a note under it if it's greyed out
    private static void addSelected(List<Object> rows, Entry entry, SpellPylonBlockEntity pylon)
    {
        rows.add(cardName(entry.card(), pylon));
        if (entry.blocked()) rows.add(Component.translatable("hud.dust.pylon.card_blocked").withStyle(ChatFormatting.ITALIC));
    }

    // ---- drawing ----

    public static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker)
    {
        Minecraft minecraft = Minecraft.getInstance();
        SpellPylonBlockEntity pylon = lookedAtPylon();
        if (pylon == null || minecraft.player == null || SpellPylonReading.holdsResearchBook(minecraft.player)) return;
        // F1 hides the HUD (before 26.2: the hideGui option)
        //? if >=26.2 {
        if (minecraft.gui.hud.isHidden()) return;
        //?} else {
        /*if (minecraft.options.hideGui) return;
        *///?}
        Font font = minecraft.font;
        DustElement element = pylon.element();
        SpellDeck deck = pylon.deck();

        // What goes on the paper, top to bottom: text lines and card grids
        List<Object> rows = new ArrayList<>();
        rows.add(Component.translatable("block.dust.spell_pylon").withStyle(ChatFormatting.BOLD));
        if (element == null) {
            rows.add(Component.translatable("dust.pylon.no_element"));
        } else {
            int level = pylon.level();
            int into = pylon.experience() - SpellLevels.xpForLevel(level);
            // A fused spell goes by its fusion's name (SpellNames.spellForClient)
            rows.add(Component.translatable("hud.dust.pylon.title", SpellNames.spellForClient(element, pylon.fusion()), level, into, SpellLevels.xpForNextLevel(level)));
            if (!deck.started()) {
                rows.add(Component.empty());
                rows.add(Component.translatable("hud.dust.pylon.not_started").withStyle(ChatFormatting.ITALIC));
            } else {
                List<Entry> deckCards = deckCards(pylon);
                rows.add(Component.empty());
                rows.add(Component.translatable("hud.dust.pylon.deck", deck.cards().size()).withStyle(ChatFormatting.UNDERLINE));
                rows.add(new Grid(deckCards, 0));
                Component specials = specials(pylon);
                if (specials != null) rows.add(specials);
                if (selected >= 0 && selected < deckCards.size()) addSelected(rows, deckCards.get(selected), pylon);
                if (showsPool()) {
                    List<Entry> poolCards = poolCards(pylon);
                    rows.add(Component.empty());
                    rows.add(Component.translatable("hud.dust.pylon.pool", poolCards.size()).withStyle(ChatFormatting.UNDERLINE));
                    rows.add(new Grid(poolCards, deckCards.size()));
                    int inPool = selected - deckCards.size();
                    if (inPool >= 0 && inPool < poolCards.size()) addSelected(rows, poolCards.get(inPool), pylon);
                }
            }
        }

        // Size the paper to fit
        int width = COLUMNS * SlotGrid.SLOT;
        int height = 0;
        for (Object row : rows) {
            if (row instanceof Component text) {
                width = Math.max(width, font.width(text));
                height += LINE;
            } else if (row instanceof Grid grid) {
                height += Math.max(1, SlotGrid.rows(grid.entries().size(), COLUMNS)) * SlotGrid.SLOT + 2;
            }
        }
        width += 2 * PADDING;
        height += 2 * PADDING - 2;
        int screenWidth = graphics.guiWidth(), screenHeight = graphics.guiHeight();
        HudAnchor anchor = MowLibClientConfig.PAPER_ANCHOR.get();
        int x = HudAnchor.keepOnScreen(anchor.x(screenWidth, width) + MowLibClientConfig.PAPER_X.get(), width, screenWidth);
        int y = HudAnchor.keepOnScreen(anchor.y(screenHeight, height) + MowLibClientConfig.PAPER_Y.get(), height, screenHeight);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PAPER, x, y, width, height);

        int left = x + PADDING, top = y + PADDING;
        for (Object row : rows) {
            if (row instanceof Component text) {
                // The element's (or fusion's) icon is part of the title line, an inline picture: SpellNames.spellForClient
                graphics.text(font, text, left, top, INK, false);
                top += LINE;
            } else if (row instanceof Grid grid) {
                drawGrid(graphics, font, grid, left, top);
                top += Math.max(1, SlotGrid.rows(grid.entries().size(), COLUMNS)) * SlotGrid.SLOT + 2;
            }
        }
    }

    // A grid of cards; first: the selection index of its first card
    private record Grid(List<Entry> entries, int first) {}

    private static void drawGrid(GuiGraphicsExtractor graphics, Font font, Grid grid, int left, int top)
    {
        if (grid.entries().isEmpty()) {
            graphics.text(font, Component.translatable("hud.dust.pylon.none"), left, top + 5, FADED, false);
            return;
        }
        boolean anyBlocked = false;
        for (int i = 0; i < grid.entries().size(); i++) {
            int slotX = SlotGrid.slotX(left, i, COLUMNS);
            int slotY = SlotGrid.slotY(top, i, COLUMNS, 0);
            boolean isSelected = grid.first() + i == selected;
            SlotGrid.fill(graphics, slotX, slotY, SLOT_FILL);
            SlotGrid.outline(graphics, slotX, slotY, isSelected ? INK : SLOT_EDGE);
            Entry entry = grid.entries().get(i);
            ItemStack card = DustItemRegistry.cardOf(entry.card());
            if (!card.isEmpty()) SlotGrid.item(graphics, font, card, slotX, slotY, Math.max(1, entry.count()));
            anyBlocked |= entry.blocked();
        }
        if (!anyBlocked) return;
        // Greyed-out cards: a see-through grey over them. Items are drawn after everything else in a layer (a
        // "stratum"), so the cover goes in the next one to land on top of them.
        graphics.nextStratum();
        for (int i = 0; i < grid.entries().size(); i++) {
            if (grid.entries().get(i).blocked()) SlotGrid.fill(graphics, SlotGrid.slotX(left, i, COLUMNS), SlotGrid.slotY(top, i, COLUMNS, 0), BLOCKED_COVER);
        }
    }

    // Over a greyed-out card
    private static final int BLOCKED_COVER = 0xB0707078;

    // The special cards in the deck (they have no picture), or null if there are none: "Spell Break x1, Blessing x2"
    private static @Nullable Component specials(SpellPylonBlockEntity pylon)
    {
        Map<Identifier, Integer> counted = new LinkedHashMap<>();
        for (Identifier card : pylon.deck().cards()) {
            if (SpellDeck.isSpecial(card)) counted.merge(card, 1, Integer::sum);
        }
        if (counted.isEmpty()) return null;
        MutableComponent text = Component.empty();
        counted.forEach((card, count) -> {
            if (!text.getSiblings().isEmpty()) text.append(", ");
            text.append(Component.translatable("hud.dust.pylon.card." + card.getPath())).append(" x" + count);
        });
        return text;
    }

    // A card's name for this spell (SpellNames: Potency on a Fire bolt is "Scorch Stacks")
    private static Component cardName(Identifier card, SpellPylonBlockEntity pylon)
    {
        return SpellNames.modifier(card, pylon.element(), pylon.shape(), pylon.fusion()).copy().withStyle(ChatFormatting.ITALIC);
    }
}
