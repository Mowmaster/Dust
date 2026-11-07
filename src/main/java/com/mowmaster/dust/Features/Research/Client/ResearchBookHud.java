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

// The research book drawn on the HUD while held, so you can read it and keep moving:
// - main hand: big, in the lower middle of the screen like a held map  (client config [research_book.main_hand])
// - offhand:   smaller, at the side                                    (client config [research_book.offhand])
// Positions use MowLib's HudAnchor (a screen anchor plus x/y offsets). Hidden while the book screen is open.
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

        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        if (main.getItem() instanceof ResearchBookItem) {
            draw(graphics, main, DustClientConfig.MAIN_ANCHOR.get(), DustClientConfig.MAIN_X.get(), DustClientConfig.MAIN_Y.get(), DustClientConfig.MAIN_SCALE.get());
        } else if (off.getItem() instanceof ResearchBookItem) {
            draw(graphics, off, DustClientConfig.OFF_ANCHOR.get(), DustClientConfig.OFF_X.get(), DustClientConfig.OFF_Y.get(), DustClientConfig.OFF_SCALE.get());
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
