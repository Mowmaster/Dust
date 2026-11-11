package com.mowmaster.dust.Features.DustComposition.Client;

import com.mowmaster.dust.Features.DustComposition.ElementTooltip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;

// Draws an ElementTooltip: for each element, its icon (the greyscale sprite tinted with the element's colour) and
// how many of it, all on one line.
public class ClientElementTooltip implements ClientTooltipComponent
{
    private static final int ICON = 9;
    private static final int GAP = 4;
    private static final int HEIGHT = 11;
    private static final int TEXT = 0xFFFFFFFF;

    private final ElementTooltip tooltip;

    public ClientElementTooltip(ElementTooltip tooltip)
    {
        this.tooltip = tooltip;
    }

    @Override
    public int getHeight(Font font)
    {
        return HEIGHT;
    }

    @Override
    public int getWidth(Font font)
    {
        int width = 0;
        for (ElementTooltip.Entry entry : tooltip.entries()) width += ICON + 1 + font.width(String.valueOf(entry.count())) + GAP;
        return Math.max(0, width - GAP);
    }

    // extractImage: 26.3's name for drawing a tooltip component's picture part (x, y: its top-left corner)
    @Override
    public void extractImage(Font font, int x, int y, int width, int height, GuiGraphicsExtractor graphics)
    {
        int dx = x;
        for (ElementTooltip.Entry entry : tooltip.entries()) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, entry.element().icon(), dx, y, ICON, ICON, 0xFF000000 | entry.element().color());
            String count = String.valueOf(entry.count());
            graphics.text(font, count, dx + ICON + 1, y + 1, TEXT, true);
            dx += ICON + 1 + font.width(count) + GAP;
        }
    }
}
