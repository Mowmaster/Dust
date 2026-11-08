package com.mowmaster.dust.Features.Research.Client;

import com.mowmaster.dust.Features.Research.EntryResearch;
import com.mowmaster.dust.Features.Research.ResearchData;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import com.mowmaster.dust.Features.Research.ResearchNotesPayload;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jspecify.annotations.Nullable;

// The research book opened for clicking (sneak + right-click the book, or MowLib's activation key while holding it).
// While it's open the mouse works the book instead of the game: tabs, entries, page arrows, Back, and on an entry's
// Overview page a text box to rewrite its notes (Research Notes only; a Shared Research Guide is read-only). The box
// starts with the built-in notes; the last few characters before the limit are counted down above the page footer.
// The game keeps running behind it (isPauseScreen false).
public class ResearchBookScreen extends Screen
{
    private final ItemStack book;
    private final boolean readOnly;
    private int left;
    private int top;
    private @Nullable MultiLineEditBox notesBox;
    // Show the character count once this close to the limit
    private static final int COUNT_FROM = 20;

    // The entry the notes box is editing, its text when it was opened (to send only real changes), and its built-in notes
    private @Nullable ResearchEntry editing;
    private String editingStart = "";
    private String editingBuiltIn = "";

    public ResearchBookScreen(ItemStack book, boolean readOnly)
    {
        super(Component.translatable(readOnly ? "item.dust.shared_research_guide" : "item.dust.research_notes"));
        this.book = book;
        this.readOnly = readOnly;
    }

    private ResearchData data()
    {
        return ResearchClient.dataFor(book);
    }

    @Override
    protected void init()
    {
        left = (this.width - ResearchBook.WIDTH) / 2;
        top = (this.height - ResearchBook.HEIGHT) / 2;
        if (!readOnly) {
            notesBox = MultiLineEditBox.builder()
                    .setX(left + ResearchBook.RIGHT_X + 2)
                    .setY(top + ResearchBook.PAGE_TOP + 38)
                    .setTextColor(ResearchBook.INK)
                    .setCursorColor(ResearchBook.INK)
                    .setTextShadow(false)
                    .setShowBackground(false)
                    // No vanilla decorations: its character counter would sit on the page arrows; we draw our own
                    .setShowDecorations(false)
                    .setPlaceholder(Component.translatable("research.dust.notes.overview.placeholder"))
                    // Ends one line above the footer, leaving room for the countdown
                    .build(this.font, ResearchBook.PAGE_W - 4, ResearchBook.FOOTER_Y - ResearchBook.PAGE_TOP - 38 - 12, Component.translatable("research.dust.notes.overview"));
            notesBox.setCharacterLimit(EntryResearch.MAX_NOTES_LENGTH);
            this.addRenderableWidget(notesBox);
        }
        updateNotesBox();
    }

    // Shows the notes box on an entry's Overview page, filled with the player's notes (or the built-in ones);
    // saves when leaving it
    private void updateNotesBox()
    {
        if (notesBox == null) return;
        ResearchData data = data();
        boolean show = ResearchBook.currentNotesPage(data) == ResearchBook.NotesPage.OVERVIEW;
        ResearchEntry entry = show ? ResearchBook.selected : null;
        if (entry != editing) {
            saveNotes();
            editing = entry;
            EntryResearch research = entry == null ? null : data.get(entry);
            editingBuiltIn = entry == null ? "" : ResearchBook.builtInNotesText(entry);
            editingStart = research == null || research.notes().isEmpty() ? editingBuiltIn : research.notes();
            notesBox.setValue(editingStart);
        }
        notesBox.visible = show;
        notesBox.active = show;
        if (!show && getFocused() == notesBox) setFocused(null);
    }

    private void saveNotes()
    {
        if (notesBox == null || editing == null) return;
        String text = notesBox.getValue();
        if (!text.equals(editingStart)) {
            // Left as (or put back to) the built-in notes: store nothing, so they keep following the language file
            String stored = text.equals(editingBuiltIn) ? "" : text;
            ClientPacketDistributor.sendToServer(new ResearchNotesPayload(editing.isEntity(), editing.id(), stored));
            editingStart = text;
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a)
    {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.pose().pushMatrix();
        graphics.pose().translate(left, top);
        ResearchBook.draw(graphics, this.font, data(), mouseX - left, mouseY - top, notesBox != null && notesBox.visible);
        graphics.pose().popMatrix();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a)
    {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        drawCountdown(graphics);
        // Entry names on hover in the list ("???" until discovered)
        ResearchEntry hovered = ResearchBook.entryAt(mouseX - left, mouseY - top);
        if (hovered != null) {
            Component name = data().has(hovered) ? hovered.name() : Component.translatable("research.dust.undiscovered");
            graphics.setTooltipForNextFrame(this.font, name, mouseX, mouseY);
        }
    }

    // "243/256" just above the footer at the right page's right edge, clear of the arrows and page number, and only
    // within COUNT_FROM characters of the limit
    private void drawCountdown(GuiGraphicsExtractor graphics)
    {
        if (notesBox == null || !notesBox.visible) return;
        int length = notesBox.getValue().length();
        if (EntryResearch.MAX_NOTES_LENGTH - length > COUNT_FROM) return;
        Component count = Component.literal(length + "/" + EntryResearch.MAX_NOTES_LENGTH);
        int color = length >= EntryResearch.MAX_NOTES_LENGTH ? 0xFFAA2020 : ResearchBook.FADED;
        int x = left + ResearchBook.RIGHT_X + ResearchBook.PAGE_W - 4 - this.font.width(count);
        int y = top + ResearchBook.FOOTER_Y - 10;
        graphics.text(this.font, count, x, y, color, false);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick)
    {
        if (super.mouseClicked(event, doubleClick)) return true;   // the notes box
        if (ResearchBook.click(data(), (int) event.x() - left, (int) event.y() - top)) {
            updateNotesBox();
            return true;
        }
        return false;
    }

    @Override
    public void tick()
    {
        super.tick();
        updateNotesBox();
    }

    @Override
    public void removed()
    {
        saveNotes();
        super.removed();
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
