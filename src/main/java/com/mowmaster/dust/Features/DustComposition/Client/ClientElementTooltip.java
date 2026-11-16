package com.mowmaster.dust.Features.DustComposition.Client;

import com.mowmaster.dust.Features.DustComposition.ElementTooltip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;

// Draws an ElementTooltip: for each element, its icon (the greyscale sprite tinted with the element's colour) and
// how many of it and its name ([icon] 9 Growth), all on one line. (Only shown once every element is discovered.)
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
        for (ElementTooltip.Entry entry : tooltip.entries()) width += ICON + 1 + font.width(label(entry)) + GAP;
        return Math.max(0, width - GAP);
    }

    // extractImage: 26.3's name for drawing a tooltip component's picture part (x, y: its top-left corner)
    @Override
    public void extractImage(Font font, int x, int y, int width, int height, GuiGraphicsExtractor graphics)
    {
        int dx = x;
        for (ElementTooltip.Entry entry : tooltip.entries()) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, entry.element().icon(), dx, y, ICON, ICON, 0xFF000000 | entry.element().color());
            net.minecraft.network.chat.Component label = label(entry);
            graphics.text(font, label, dx + ICON + 1, y + 1, TEXT, true);
            dx += ICON + 1 + font.width(label) + GAP;
        }
    }

    // "9 Growth"
    private static net.minecraft.network.chat.Component label(ElementTooltip.Entry entry)
    {
        return net.minecraft.network.chat.Component.literal(entry.count() + " ").append(entry.element().name());
    }
}
