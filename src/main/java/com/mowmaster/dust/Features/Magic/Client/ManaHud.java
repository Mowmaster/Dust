package com.mowmaster.dust.Features.Magic.Client;

import com.mowmaster.dust.DustClientConfig;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
import com.mowmaster.dust.Features.Magic.PlayerMagicHelper;
import com.mowmaster.mowlib.client.HudAnchor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;

// The mana HUD: the mana bar in its frame, with the dust carried in a row above it (below it when pinned to the top of
// the screen). Where it goes is [mana_hud] in config/dust-client.toml (DustClientConfig):
//   follow_hotbar = true   just left of the hotbar (the default). When the offhand slot is showing on that side (it
//                          is whenever the offhand holds something, for right-handed players), it moves left of that.
//   follow_hotbar = false  pinned to anchor (one of MowLib's nine HudAnchor places)
// x / y move it from there either way.
// Both are drawn from DustClient's RegisterGuiLayersEvent handler as two layers; panel() works out the spot for both.
public final class ManaHud
{
    private ManaHud() {}

    // The whole panel: dust row (12 high) over the 116 x 20 mana frame
    private static final int WIDTH = 116;
    private static final int ROW_HEIGHT = 12;
    private static final int FRAME_HEIGHT = 20;
    private static final int HEIGHT = ROW_HEIGHT + FRAME_HEIGHT;
    // The vanilla hotbar is 182 wide, centred; the offhand slot sits 29 further out from it
    private static final int HOTBAR_HALF = 91;
    private static final int OFFHAND_SLOT = 29;
    private static final int GAP = 1;

    private static final Identifier BAR_BACK = Identifier.fromNamespaceAndPath(DustReferences.MODID, "aura_iconh_bg");
    private static final Identifier BAR = Identifier.fromNamespaceAndPath(DustReferences.MODID, "aura_iconh");
    private static final Identifier FRAME = Identifier.fromNamespaceAndPath(DustReferences.MODID, "magicmeter_overlay");

    // Top-left corner of the panel, and whether the dust row goes below the frame (pinned to the top of the screen)
    private record Panel(int x, int y, boolean rowBelow) {}

    private static Panel panel(GuiGraphicsExtractor graphics, LocalPlayer player)
    {
        int screenW = graphics.guiWidth(), screenH = graphics.guiHeight();
        int x, y;
        boolean rowBelow = false;
        if (DustClientConfig.MANA_FOLLOW_HOTBAR.get()) {
            int left = screenW / 2 - HOTBAR_HALF;
            // The offhand slot shows on the left when the main arm is the right one and the offhand holds something
            boolean offhandLeft = player.getMainArm() == HumanoidArm.RIGHT && !player.getOffhandItem().isEmpty();
            if (offhandLeft) left -= OFFHAND_SLOT + GAP;
            x = left - GAP - WIDTH;
            y = screenH - 8 - HEIGHT;
        } else {
            HudAnchor anchor = DustClientConfig.MANA_ANCHOR.get();
            x = anchor.x(screenW, WIDTH);
            y = anchor.y(screenH, HEIGHT);
            rowBelow = anchor.ordinal() / 3 == 0;
        }
        x = HudAnchor.keepOnScreen(x + DustClientConfig.MANA_X.get(), WIDTH, screenW);
        y = HudAnchor.keepOnScreen(y + DustClientConfig.MANA_Y.get(), HEIGHT, screenH);
        return new Panel(x, y, rowBelow);
    }

    // The mana bar: 100 one-pixel steps of the empty bar, as many full ones as the percent of mana left, then the frame
    public static void renderMana(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || player.isCreative() || player.isSpectator() || !DustMagicAttachmentHelper.hasUnlockedMana(player)) return;
        Panel panel = panel(graphics, player);
        int frameY = panel.rowBelow() ? panel.y() : panel.y() + ROW_HEIGHT;
        int barX = panel.x() + 8, barY = frameY + 8;
        int max = DustMagicAttachmentHelper.getManaMaximum(player);
        int percent = max <= 0 ? 0 : Math.round(100F * DustMagicAttachmentHelper.getManaLevel(player) / max);
        for (int i = 0; i < 100; i++) graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BAR_BACK, 1, 4, 0, 0, barX + i, barY, 1, 4);
        for (int i = 0; i < percent; i++) graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BAR, 1, 4, 0, 0, barX + i, barY, 1, 4);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FRAME, WIDTH, FRAME_HEIGHT, 0, 0, panel.x(), frameY, WIDTH, FRAME_HEIGHT);
    }

    // The dust carried (Features/Magic/PlayerMagic): each element's icon in its colour and how much, in a row, once the
    // player's magic has awakened
    public static void renderDust(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator() || !PlayerMagicHelper.isAwakened(player)) return;
        Panel panel = panel(graphics, player);
        Font font = Minecraft.getInstance().font;
        int x = panel.x();
        int y = panel.rowBelow() ? panel.y() + FRAME_HEIGHT + 2 : panel.y();
        for (var carried : PlayerMagicHelper.get(player).dust().entrySet()) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, carried.getKey().icon(), x, y, 9, 9, 0xFF000000 | carried.getKey().color());
            String amount = String.valueOf(carried.getValue());
            graphics.text(font, amount, x + 10, y + 1, 0xFFFFFFFF, true);
            x += 10 + font.width(amount) + 4;
        }
    }
}
