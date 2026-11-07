package com.mowmaster.dust.Features.AncientLanguage.Client;

import com.mowmaster.dust.Features.AncientLanguage.AncientLanguage;
import com.mowmaster.dust.Features.AncientLanguage.DecipherMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

// The hangman screen for DecipherMenu. Drawn with plain rectangles and text (no GUI texture yet).
//
// 26.3 renamed the screen drawing methods: render -> extractRenderState, and GuiGraphics -> GuiGraphicsExtractor
// (screens now "extract" what to draw, and the game draws it afterwards). extractBackground draws in screen
// coordinates; extractLabels runs with the origin moved to the panel's top-left corner.
public class DecipherScreen extends AbstractContainerScreen<DecipherMenu>
{
    private static final int WIDTH = 210;
    private static final int HEIGHT = 158;
    private static final int KEY_SIZE = 14;
    private static final int KEY_GAP = 1;

    private static final int PAPER = 0xFFE9DCB5;
    private static final int PAPER_EDGE = 0xFF8A6B45;
    private static final int INK = 0xFF3B2A1A;
    private static final int FADED = 0xFF9C8A6E;

    private final Button[] keys = new Button[AncientLanguage.GLYPH_COUNT];

    public DecipherScreen(DecipherMenu menu, Inventory inventory, Component title)
    {
        super(menu, inventory, title, WIDTH, HEIGHT);
    }

    @Override
    protected void init()
    {
        super.init();
        // Two rows of 13 letter keys along the bottom
        int rowWidth = 13 * (KEY_SIZE + KEY_GAP) - KEY_GAP;
        int x0 = this.leftPos + (WIDTH - rowWidth) / 2;
        int y0 = this.topPos + HEIGHT - 2 * (KEY_SIZE + KEY_GAP) - 6;
        for (int i = 0; i < AncientLanguage.GLYPH_COUNT; i++) {
            final int glyph = i;
            int x = x0 + (i % 13) * (KEY_SIZE + KEY_GAP);
            int y = y0 + (i / 13) * (KEY_SIZE + KEY_GAP);
            keys[i] = this.addRenderableWidget(Button.builder(Component.literal(String.valueOf(AncientLanguage.letter(i))), b -> guess(glyph))
                    .bounds(x, y, KEY_SIZE, KEY_SIZE)
                    .build());
        }
        updateKeys();
    }

    // Sends the guess to the server's menu (DecipherMenu#clickMenuButton). The result comes back through the menu's
    // synced DataSlots.
    private void guess(int glyph)
    {
        if (this.minecraft != null && this.minecraft.gameMode != null && keys[glyph].active) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, glyph);
        }
    }

    private void updateKeys()
    {
        boolean playing = this.menu.getState() == DecipherMenu.PLAYING;
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] != null) keys[i].active = playing && !this.menu.isGuessed(i);
        }
    }

    @Override
    protected void containerTick()
    {
        super.containerTick();
        updateKeys();
    }

    // Typing a letter on the keyboard guesses it too
    @Override
    public boolean charTyped(CharacterEvent event)
    {
        int glyph = AncientLanguage.glyphIndex(event.codepoint());
        if (glyph >= 0) {
            guess(glyph);
            return true;
        }
        return super.charTyped(event);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a)
    {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int x = this.leftPos;
        int y = this.topPos;
        graphics.fill(x - 2, y - 2, x + WIDTH + 2, y + HEIGHT + 2, PAPER_EDGE);
        graphics.fill(x, y, x + WIDTH, y + HEIGHT, PAPER);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY)
    {
        graphics.centeredText(this.font, this.title, WIDTH / 2, 6, INK);
        drawGallows(graphics, 14, 20, this.menu.getWrong());
        drawWord(graphics);
        drawStatus(graphics);
    }

    // The word: revealed letters in normal text, the rest as their glyph, each over a line
    private void drawWord(GuiGraphicsExtractor graphics)
    {
        String word = this.menu.getWord();
        int cell = 12;
        int areaLeft = 70;
        int areaWidth = WIDTH - areaLeft - 8;
        int x0 = areaLeft + (areaWidth - word.length() * cell) / 2;
        int y = 46;
        for (int i = 0; i < word.length(); i++) {
            int cx = x0 + i * cell;
            char c = word.charAt(i);
            Component text = this.menu.isRevealed(i)
                    ? Component.literal(String.valueOf(Character.toUpperCase(c)))
                    // GLYPHS is the raw glyph font, never revealed: an unsolved letter always shows its glyph here
                    : Component.literal(String.valueOf(c)).withStyle(style -> style.withFont(AncientLanguage.GLYPHS));
            graphics.centeredText(this.font, text, cx + cell / 2, y, this.menu.isRevealed(i) ? INK : FADED);
            graphics.fill(cx + 1, y + 10, cx + cell - 1, y + 11, INK);
        }
    }

    private void drawStatus(GuiGraphicsExtractor graphics)
    {
        Component status = switch (this.menu.getState()) {
            case DecipherMenu.WON -> Component.translatable("container.dust.decipher.won");
            case DecipherMenu.LOST -> Component.translatable("container.dust.decipher.lost", this.menu.getWord().toUpperCase(java.util.Locale.ROOT));
            default -> Component.translatable("container.dust.decipher.mistakes", this.menu.getWrong(), DecipherMenu.MAX_WRONG);
        };
        graphics.centeredText(this.font, status, 70 + (WIDTH - 78) / 2, 74, INK);
    }

    // Classic hangman: gallows, then head, body, arms and legs, one part per mistake
    private static void drawGallows(GuiGraphicsExtractor graphics, int x, int y, int wrong)
    {
        // gallows
        graphics.fill(x, y + 62, x + 40, y + 64, INK);          // base
        graphics.fill(x + 6, y, x + 8, y + 62, INK);            // post
        graphics.fill(x + 6, y, x + 30, y + 2, INK);            // beam
        graphics.fill(x + 28, y, x + 30, y + 8, INK);           // rope
        int cx = x + 29;
        if (wrong >= 1) graphics.outline(cx - 5, y + 8, 10, 10, INK);                 // head
        if (wrong >= 2) graphics.fill(cx - 1, y + 18, cx + 1, y + 36, INK);           // body
        if (wrong >= 3) diagonal(graphics, cx, y + 21, -1, 9);                         // left arm
        if (wrong >= 4) diagonal(graphics, cx, y + 21, 1, 9);                          // right arm
        if (wrong >= 5) diagonal(graphics, cx, y + 36, -1, 10);                        // left leg
        if (wrong >= 6) diagonal(graphics, cx, y + 36, 1, 10);                         // right leg
    }

    // A 2px-wide 45 degree line going down and left (dir -1) or right (dir 1)
    private static void diagonal(GuiGraphicsExtractor graphics, int x, int y, int dir, int length)
    {
        for (int i = 0; i < length; i++) {
            int px = x + dir * i;
            graphics.fill(px - 1, y + i, px + 1, y + i + 1, INK);
        }
    }
}
