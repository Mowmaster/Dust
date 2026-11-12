package com.mowmaster.dust.Features.Machines.Client;

import com.mowmaster.dust.Features.Machines.AlchemicalDustTooltip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

// Draws an AlchemicalDustTooltip: [icon] Fire - Amorphous (the name is ??? until discovered; the phase always shows)
public class ClientAlchemicalDustTooltip implements ClientTooltipComponent
{
    private static final int ICON = 9;
    private static final int HEIGHT = 11;
    private static final int NAME = 0xFFFFFFFF;
    private static final int PHASE = 0xFFAAAAAA;

    private final AlchemicalDustTooltip tooltip;

    public ClientAlchemicalDustTooltip(AlchemicalDustTooltip tooltip)
    {
        this.tooltip = tooltip;
    }

    private Component name()
    {
        return tooltip.known() ? tooltip.element().name() : Component.translatable("research.dust.undiscovered");
    }

    private Component phase()
    {
        return Component.translatable("item.dust.alchemical_dust.phase",
                Component.translatable("dust.phase." + tooltip.element().phase().getSerializedName()));
    }

    @Override
    public int getHeight(Font font)
    {
        return HEIGHT;
    }

    @Override
    public int getWidth(Font font)
    {
        return ICON + 2 + font.width(name()) + font.width(phase());
    }

    @Override
    public void extractImage(Font font, int x, int y, int width, int height, GuiGraphicsExtractor graphics)
    {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, tooltip.element().icon(), x, y, ICON, ICON, 0xFF000000 | tooltip.element().color());
        int textX = x + ICON + 2;
        graphics.text(font, name(), textX, y + 1, NAME, true);
        graphics.text(font, phase(), textX + font.width(name()), y + 1, PHASE, true);
    }
}
