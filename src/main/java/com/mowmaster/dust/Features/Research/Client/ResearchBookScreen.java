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
// While it's open the mouse works the book instead of the game: tabs, entries, page arrows, Back, and on "My notes"
// a text box for your own notes (Research Notes only; a Shared Research Guide is read-only).
// The game keeps running behind it (isPauseScreen false).
public class ResearchBookScreen extends Screen
{
    private final ItemStack book;
    private final boolean readOnly;
    private int left;
    private int top;
    private @Nullable MultiLineEditBox notesBox;
    // The entry the notes box is editing, and its text when it was opened (to send only real changes)
    private @Nullable ResearchEntry editing;
    private String editingStart = "";

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
                    .setPlaceholder(Component.translatable("research.dust.notes.my_notes.placeholder"))
                    .build(this.font, ResearchBook.PAGE_W - 4, ResearchBook.FOOTER_Y - ResearchBook.PAGE_TOP - 40, Component.translatable("research.dust.notes.my_notes"));
            notesBox.setCharacterLimit(EntryResearch.MAX_NOTES_LENGTH);
            this.addRenderableWidget(notesBox);
        }
        updateNotesBox();
    }

    // Shows the notes box on an entry's "My notes" page, filled with that entry's notes; saves when leaving it
    private void updateNotesBox()
    {
        if (notesBox == null) return;
        ResearchData data = data();
        boolean show = ResearchBook.currentNotesPage(data) == ResearchBook.NotesPage.MY_NOTES;
        ResearchEntry entry = show ? ResearchBook.selected : null;
        if (entry != editing) {
            saveNotes();
            editing = entry;
            EntryResearch research = entry == null ? null : data.get(entry);
            editingStart = research == null ? "" : research.notes();
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
            ClientPacketDistributor.sendToServer(new ResearchNotesPayload(editing.isEntity(), editing.id(), text));
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
        // Entry names on hover in the list ("???" until discovered)
        ResearchEntry hovered = ResearchBook.entryAt(mouseX - left, mouseY - top);
        if (hovered != null) {
            Component name = data().has(hovered) ? hovered.name() : Component.translatable("research.dust.undiscovered");
            graphics.setTooltipForNextFrame(this.font, name, mouseX, mouseY);
        }
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
