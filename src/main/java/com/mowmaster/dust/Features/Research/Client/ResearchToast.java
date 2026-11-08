package com.mowmaster.dust.Features.Research.Client;

import com.mowmaster.dust.Features.Research.ResearchEntry;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

// The pop-up in the top right when an observation finishes: the entry's icon, "New research" (or "Noted somewhere
// new") and its name. Uses the advancement toast's background. A Toast is drawn by the game's ToastManager, which
// calls update() to ask whether it should still show, and extractRenderState() to draw it (0,0 = its top-left).
public class ResearchToast implements Toast
{
    private static final Identifier BACKGROUND = Identifier.withDefaultNamespace("toast/advancement");
    private static final long DISPLAY_MS = 5000L;
    private static final int TITLE_COLOR = 0xFFE8C860;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    private final ResearchEntry entry;
    private final Component title;
    private Visibility visibility = Visibility.SHOW;

    public ResearchToast(ResearchEntry entry, boolean newBiome)
    {
        this.entry = entry;
        this.title = Component.translatable(newBiome ? "toast.dust.research.new_biome" : "toast.dust.research.discovered");
    }

    @Override
    public Visibility getWantedVisibility()
    {
        return visibility;
    }

    // fullyVisibleForMs: how long it's been fully on screen. The player's notification time setting stretches it.
    @Override
    public void update(ToastManager manager, long fullyVisibleForMs)
    {
        visibility = fullyVisibleForMs >= DISPLAY_MS * manager.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, Font font, long fullyVisibleForMs)
    {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND, 0, 0, this.width(), this.height());
        graphics.text(font, title, 30, 7, TITLE_COLOR, false);
        List<FormattedCharSequence> name = font.split(entry.name(), this.width() - 34);
        if (!name.isEmpty()) graphics.text(font, name.getFirst(), 30, 18, TEXT_COLOR, false);
        graphics.fakeItem(entry.icon(), 8, 8);
    }
}
