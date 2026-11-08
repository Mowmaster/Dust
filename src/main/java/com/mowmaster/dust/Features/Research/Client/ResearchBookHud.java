package com.mowmaster.dust.Features.Research.Client;

import com.mowmaster.dust.DustClientConfig;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Research.ResearchBookItem;
import com.mowmaster.mowlib.client.HudAnchor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

// The research book drawn at the side of the screen while held in either hand (main hand first), so you can read it and
// keep moving. Where and how big: client config [research_book] (default: the middle of the left edge, 45%), placed with
// MowLib's HudAnchor (a screen anchor plus x/y offsets). Hidden while the book screen is open; clicking happens there.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class ResearchBookHud
{
    private static final Identifier LAYER = Identifier.fromNamespaceAndPath(DustReferences.MODID, "research_book");

    private ResearchBookHud() {}

    @SubscribeEvent
    public static void registerLayer(RegisterGuiLayersEvent event)
    {
        event.registerBelow(VanillaGuiLayers.CHAT, LAYER, ResearchBookHud::render);
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker)
    {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.gui.screen() instanceof ResearchBookScreen) return;

        ItemStack book = player.getMainHandItem().getItem() instanceof ResearchBookItem ? player.getMainHandItem() : player.getOffhandItem();
        if (book.getItem() instanceof ResearchBookItem) {
            draw(graphics, book, DustClientConfig.BOOK_ANCHOR.get(), DustClientConfig.BOOK_X.get(), DustClientConfig.BOOK_Y.get(), DustClientConfig.BOOK_SCALE.get());
        }
    }

    private static void draw(GuiGraphicsExtractor graphics, ItemStack book, HudAnchor anchor, int xOffset, int yOffset, int scalePercent)
    {
        float scale = scalePercent / 100.0F;
        int width = Math.round(ResearchBook.WIDTH * scale);
        int height = Math.round(ResearchBook.HEIGHT * scale);
        int x = HudAnchor.keepOnScreen(anchor.x(graphics.guiWidth(), width) + xOffset, width, graphics.guiWidth());
        int y = HudAnchor.keepOnScreen(anchor.y(graphics.guiHeight(), height) + yOffset, height, graphics.guiHeight());

        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().scale(scale, scale);
        ResearchBook.draw(graphics, Minecraft.getInstance().font, ResearchClient.dataFor(book), -1, -1, false);
        graphics.pose().popMatrix();
    }
}
