package com.mowmaster.dust.Features.Guide.Client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mowmaster.dust.Features.Guide.GuideRegistry;
import com.mowmaster.dust.Features.Guide.GuideScene;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The guide viewer: an item's guide scenes (GuideRegistry), one at a time, playing as an animated diorama.
//   the view      the scene's blocks and items in 3D (GuideSceneRenderer), with its text boxes, outlines and click
//                 indicators drawn over it. Drag to turn it round, scroll to zoom; hover a block or item for its name.
//   the controls  previous scene, replay, pause / play, next scene (or Left, R, Space, Right), and the timeline: click
//                 or drag along it to jump; the marks on it are where each text box starts
//   Esc           back to the screen it was opened from (the inventory, a chest...)
public class GuideScreen extends Screen
{
    private static final int SIDE = 12;
    private static final int TOP = 30;
    private static final int BOTTOM = 42;
    private static final int BUTTON = 18;
    private static final int BUTTON_GAP = 6;
    // Colours (0xAARRGGBB)
    private static final int FRAME = 0xFF5C4C32;
    private static final int VIEW_BACK = 0xFF1C1A22;
    private static final int TEXT_BACK = 0xE8141018;
    private static final int TEXT_BORDER = 0xFFC8A060;
    private static final int TEXT = 0xFFF2E8D5;
    private static final int GOLD = 0xFFE8C068;

    private final ItemStack icon;
    private final List<GuideRegistry.Entry> scenes;
    private final @Nullable Screen previous;
    private int index;
    private GuidePlayback playback;
    // Ticks into the scene (the frame drawn adds the part of a tick since, while it's playing)
    private int time;
    private boolean paused;
    // How far the player has turned and zoomed the view (on top of the scene's own camera)
    private float turnYaw;
    private float turnPitch;
    private float zoom = 1;
    private boolean dragging;
    private boolean scrubbing;
    private ScreenRectangle viewport = new ScreenRectangle(0, 0, 1, 1);
    private @Nullable GuideCamera camera;

    public GuideScreen(ItemStack icon, List<GuideRegistry.Entry> scenes, @Nullable Screen previous)
    {
        super(scenes.getFirst().scene().title());
        this.icon = icon.copyWithCount(1);
        this.scenes = scenes;
        this.previous = previous;
        select(0);
    }

    private void select(int which)
    {
        index = Mth.clamp(which, 0, scenes.size() - 1);
        playback = new GuidePlayback(scenes.get(index).scene());
        time = 0;
        paused = false;
        turnYaw = 0;
        turnPitch = 0;
        zoom = 1;
    }

    private GuideScene scene()
    {
        return playback.scene();
    }

    @Override
    protected void init()
    {
        viewport = new ScreenRectangle(SIDE, TOP, Math.max(1, width - 2 * SIDE), Math.max(1, height - TOP - BOTTOM));
    }

    @Override
    public void tick()
    {
        if (!paused && time < playback.length()) time++;
    }

    private boolean finished()
    {
        return time >= playback.length();
    }

    private float now(float partialTick)
    {
        return Math.min(playback.length(), time + (paused || finished() ? 0 : partialTick));
    }

    // GUI pixels per block: the scene fits the view at zoom 1, kept inside the texture's depth range (±1000 pixels)
    private float scale()
    {
        float fit = Math.min(viewport.width(), viewport.height()) / (2 * playback.radius()) * 0.92F;
        int guiScale = Math.max(1, Minecraft.getInstance().getWindow().getGuiScale());
        return Math.min(fit * scene().camera().zoom() * zoom, 900F / (playback.radius() * guiScale));
    }

    // ---- drawing ----

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick)
    {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.fill(0, 0, width, height, 0xA0000000);
        graphics.fill(viewport.left() - 1, viewport.top() - 1, viewport.right() + 1, viewport.bottom() + 1, FRAME);
        graphics.fill(viewport.left(), viewport.top(), viewport.right(), viewport.bottom(), VIEW_BACK);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick)
    {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        GuideFrame frame = playback.at(now(partialTick));
        GuideScene.Camera base = scene().camera();
        float yaw = base.yaw() + frame.yaw() + turnYaw;
        float pitch = Mth.clamp(base.pitch() + turnPitch, -10, 89);
        float scale = scale();
        camera = GuideCamera.of(playback.centre(), yaw, pitch, scale, viewport);
        graphics.submitPictureInPictureRenderState(new GuideSceneRenderState(frame, camera.view(), yaw, viewport.left(), viewport.top(),
                viewport.right(), viewport.bottom(), scale, null));
        // A new layer, so what's drawn next goes over the scene's picture
        graphics.nextStratum();
        graphics.enableScissor(viewport.left(), viewport.top(), viewport.right(), viewport.bottom());
        for (GuideFrame.BoxDraw box : frame.boxes()) drawBox(graphics, box);
        for (GuideFrame.ClickDraw click : frame.clicks()) drawClick(graphics, click);
        int unanchored = 0;
        for (GuideFrame.TextDraw text : frame.texts()) unanchored = drawText(graphics, text, unanchored);
        graphics.disableScissor();
        drawChrome(graphics, mouseX, mouseY);
        tooltip(graphics, frame, mouseX, mouseY);
    }

    private static int withAlpha(int color, float alpha)
    {
        int a = Math.round(((color >>> 24) & 0xFF) * Mth.clamp(alpha, 0, 1));
        return (a << 24) | (color & 0xFFFFFF);
    }

    // A one-pixel line (runs along a row are drawn as one fill)
    private static void line(GuiGraphicsExtractor graphics, float fromX, float fromY, float toX, float toY, int color)
    {
        // Bresenham's line: steps one pixel at a time, choosing x, y or both by the running error
        int x0 = Math.round(fromX), y0 = Math.round(fromY), x1 = Math.round(toX), y1 = Math.round(toY);
        int dx = Math.abs(x1 - x0), dy = -Math.abs(y1 - y0), sx = x0 < x1 ? 1 : -1, sy = y0 < y1 ? 1 : -1, err = dx + dy;
        int runY = y0, runMin = x0, runMax = x0;
        for (int guard = 0; guard < 4096; guard++) {
            if (y0 != runY) {
                graphics.fill(runMin, runY, runMax + 1, runY + 1, color);
                runY = y0;
                runMin = x0;
                runMax = x0;
            } else {
                runMin = Math.min(runMin, x0);
                runMax = Math.max(runMax, x0);
            }
            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 >= dy) { err += dy; x0 += sx; }
            if (e2 <= dx) { err += dx; y0 += sy; }
        }
        graphics.fill(runMin, runY, runMax + 1, runY + 1, color);
    }

    private Vector2f project(Vec3 point)
    {
        return camera.project(point.x, point.y, point.z);
    }

    // A highlight: the box's twelve edges
    private void drawBox(GuiGraphicsExtractor graphics, GuideFrame.BoxDraw draw)
    {
        AABB box = draw.box();
        Vector2f[] corner = new Vector2f[8];
        for (int i = 0; i < 8; i++) {
            corner[i] = camera.project((i & 1) == 0 ? box.minX : box.maxX, (i & 2) == 0 ? box.minY : box.maxY, (i & 4) == 0 ? box.minZ : box.maxZ);
        }
        int color = withAlpha(draw.color(), draw.alpha());
        int[][] edges = {{0, 1}, {2, 3}, {4, 5}, {6, 7}, {0, 2}, {1, 3}, {4, 6}, {5, 7}, {0, 4}, {1, 5}, {2, 6}, {3, 7}};
        for (int[] edge : edges) line(graphics, corner[edge[0]].x, corner[edge[0]].y, corner[edge[1]].x, corner[edge[1]].y, color);
    }

    // A click: a ring pulsing out from the point, a mouse with the button lit, the held item and what to do
    private void drawClick(GuiGraphicsExtractor graphics, GuideFrame.ClickDraw click)
    {
        Vector2f at = project(click.point());
        int x = Math.round(at.x), y = Math.round(at.y);
        float pulse = (click.progress() * 3) % 1;
        float radius = 2 + pulse * 7;
        int ring = withAlpha(0xFFFFFFFF, 1 - pulse);
        for (int i = 0; i < 16; i++) {
            double a0 = Math.PI * 2 * i / 16, a1 = Math.PI * 2 * (i + 1) / 16;
            line(graphics, x + (float) Math.cos(a0) * radius, y + (float) Math.sin(a0) * radius, x + (float) Math.cos(a1) * radius, y + (float) Math.sin(a1) * radius, ring);
        }
        graphics.fill(x - 1, y - 1, x + 2, y + 2, GOLD);
        int mx = x + 10, my = y - 22;
        // The mouse: outline, the line between its buttons, the pressed button lit
        graphics.fill(mx, my, mx + 9, my + 12, 0xFF202028);
        graphics.fill(mx + 1, my + 1, mx + 8, my + 11, 0xFFD8D8E0);
        graphics.fill(mx + 1, my + 5, mx + 8, my + 6, 0xFF202028);
        graphics.fill(mx + 4, my + 1, mx + 5, my + 5, 0xFF202028);
        if (click.right()) graphics.fill(mx + 5, my + 1, mx + 8, my + 5, GOLD);
        else graphics.fill(mx + 1, my + 1, mx + 4, my + 5, GOLD);
        int labelX = mx + 12;
        if (!click.held().isEmpty()) {
            graphics.item(click.held(), labelX, my - 2);
            labelX += 18;
        }
        Component button = Component.translatable(click.right() ? "guide.dust.click.right" : "guide.dust.click.left");
        Component label = click.sneak() ? Component.translatable("guide.dust.click.sneak", button) : button;
        int w = font.width(label);
        graphics.fill(labelX - 2, my + 1, labelX + w + 2, my + 12, TEXT_BACK);
        graphics.text(font, label, labelX, my + 3, TEXT, false);
    }

    // A text box (pointing at its point, if it has one); returns how many unanchored boxes are stacked so far
    private int drawText(GuiGraphicsExtractor graphics, GuideFrame.TextDraw text, int unanchored)
    {
        if (text.alpha() < 0.05F) return unanchored;
        int maxWidth = Math.max(80, Math.min(170, viewport.width() / 2 - 20));
        List<FormattedCharSequence> lines = font.split(text.text(), maxWidth);
        int w = 8, h = lines.size() * 10 + 6;
        for (FormattedCharSequence line : lines) w = Math.max(w, font.width(line) + 8);
        int bx, by;
        Vector2f point = null;
        if (text.point() != null) {
            point = project(text.point());
            switch (text.side()) {
                case LEFT -> { bx = Math.round(point.x) - 28 - w; by = Math.round(point.y) - h / 2 - 10; }
                case ABOVE -> { bx = Math.round(point.x) - w / 2; by = Math.round(point.y) - h - 26; }
                case BELOW -> { bx = Math.round(point.x) - w / 2; by = Math.round(point.y) + 26; }
                default -> { bx = Math.round(point.x) + 28; by = Math.round(point.y) - h / 2 - 10; }
            }
        } else {
            bx = viewport.left() + 6;
            by = viewport.top() + 6 + unanchored * 34;
            unanchored++;
        }
        bx = Mth.clamp(bx, viewport.left() + 4, Math.max(viewport.left() + 4, viewport.right() - 4 - w));
        by = Mth.clamp(by, viewport.top() + 4, Math.max(viewport.top() + 4, viewport.bottom() - 4 - h));
        int border = withAlpha(TEXT_BORDER, text.alpha());
        if (point != null) {
            // A line from the point to the near side of the box, and a dot on the point
            float edgeX = point.x < bx ? bx : point.x > bx + w ? bx + w : point.x;
            float edgeY = point.y < by ? by : point.y > by + h ? by + h : by + h / 2F;
            line(graphics, point.x, point.y, edgeX, edgeY, border);
            graphics.fill(Math.round(point.x) - 1, Math.round(point.y) - 1, Math.round(point.x) + 2, Math.round(point.y) + 2, border);
        }
        graphics.fill(bx - 1, by - 1, bx + w + 1, by + h + 1, border);
        graphics.fill(bx, by, bx + w, by + h, withAlpha(TEXT_BACK, text.alpha()));
        int color = withAlpha(TEXT, text.alpha());
        for (int i = 0; i < lines.size(); i++) graphics.text(font, lines.get(i), bx + 4, by + 4 + i * 10, color, false);
        return unanchored;
    }

    // ---- the controls ----

    // 7x7 icons: previous scene, replay, pause, play, next scene
    private static final String[] PREVIOUS = {"#.....#", "#....##", "#...###", "#..####", "#...###", "#....##", "#.....#"};
    private static final String[] REPLAY = {"..###.#", ".#...##", "#...###", "#......", "#.....#", ".#...#.", "..###.."};
    private static final String[] PAUSE = {"##...##", "##...##", "##...##", "##...##", "##...##", "##...##", "##...##"};
    private static final String[] PLAY = {"##.....", "####...", "######.", "#######", "######.", "####...", "##....."};
    private static final String[] NEXT = {"#.....#", "##....#", "###...#", "####..#", "###...#", "##....#", "#.....#"};

    private int buttonX(int which)
    {
        int total = 4 * BUTTON + 3 * BUTTON_GAP;
        return width / 2 - total / 2 + which * (BUTTON + BUTTON_GAP);
    }

    private int buttonsY()
    {
        return height - BUTTON - 4;
    }

    private int buttonAt(double mouseX, double mouseY)
    {
        if (mouseY < buttonsY() || mouseY >= buttonsY() + BUTTON) return -1;
        for (int i = 0; i < 4; i++) if (mouseX >= buttonX(i) && mouseX < buttonX(i) + BUTTON) return i;
        return -1;
    }

    private boolean enabled(int button)
    {
        return switch (button) {
            case 0 -> index > 0;
            case 3 -> index < scenes.size() - 1;
            default -> true;
        };
    }

    private int timelineY()
    {
        return height - BOTTOM + 8;
    }

    private boolean onTimeline(double mouseX, double mouseY)
    {
        return mouseX >= viewport.left() && mouseX <= viewport.right() && mouseY >= timelineY() - 4 && mouseY <= timelineY() + 6;
    }

    private void drawChrome(GuiGraphicsExtractor graphics, int mouseX, int mouseY)
    {
        graphics.item(icon, SIDE, 7);
        graphics.text(font, scene().title(), SIDE + 22, 11, 0xFFFFFFFF, true);
        if (scenes.size() > 1) {
            Component count = Component.translatable("guide.dust.count", index + 1, scenes.size());
            graphics.text(font, count, width - SIDE - font.width(count), 11, 0xFFB0B0B0, true);
        }
        // The timeline: what's played, the marks where text boxes start, the handle
        int y = timelineY(), left = viewport.left(), right = viewport.right();
        graphics.fill(left, y, right, y + 3, 0xFF34303C);
        float done = playback.length() <= 0 ? 0 : (float) time / playback.length();
        graphics.fill(left, y, left + Math.round((right - left) * done), y + 3, GOLD);
        for (int key : playback.keyframes()) {
            int kx = left + Math.round((right - left) * (float) key / playback.length());
            graphics.fill(kx, y - 2, kx + 1, y + 5, 0xFFA8A0C0);
        }
        int hx = left + Math.round((right - left) * done);
        graphics.fill(hx - 1, y - 3, hx + 2, y + 6, 0xFFFFFFFF);
        // The buttons
        String[][] icons = {PREVIOUS, REPLAY, paused || finished() ? PLAY : PAUSE, NEXT};
        int hovered = buttonAt(mouseX, mouseY);
        for (int i = 0; i < 4; i++) {
            int bx = buttonX(i), by = buttonsY();
            boolean on = enabled(i);
            graphics.fill(bx, by, bx + BUTTON, by + BUTTON, on && hovered == i ? 0xFF6A5A3C : FRAME);
            graphics.fill(bx + 1, by + 1, bx + BUTTON - 1, by + BUTTON - 1, on && hovered == i ? 0xFF3A3248 : 0xFF26222E);
            int color = !on ? 0xFF55505E : i == 1 && finished() ? GOLD : 0xFFE0D8C8;
            String[] art = icons[i];
            for (int row = 0; row < art.length; row++) {
                for (int col = 0; col < art[row].length(); col++) {
                    if (art[row].charAt(col) == '#') graphics.fill(bx + 5 + col, by + 5 + row, bx + 6 + col, by + 6 + row, color);
                }
            }
        }
        // How to move the view, left of the buttons (when there's room)
        Component hint = Component.translatable("guide.dust.hint");
        if (SIDE + font.width(hint) + 8 < buttonX(0)) graphics.text(font, hint, SIDE, buttonsY() + 5, 0xFF8A8494, false);
    }

    private void tooltip(GuiGraphicsExtractor graphics, GuideFrame frame, int mouseX, int mouseY)
    {
        int button = buttonAt(mouseX, mouseY);
        if (button >= 0) {
            String key = switch (button) {
                case 0 -> "guide.dust.previous";
                case 1 -> "guide.dust.replay";
                case 2 -> paused || finished() ? "guide.dust.play" : "guide.dust.pause";
                default -> "guide.dust.next";
            };
            graphics.setTooltipForNextFrame(font, Component.translatable(key), mouseX, mouseY);
            return;
        }
        if (dragging || camera == null || !viewport.containsPoint(mouseX, mouseY)) return;
        // The nearest item or block to the mouse, if it's close (items first: they're smaller)
        Component name = null;
        float best = 10 * 10;
        for (GuideFrame.ItemDraw item : frame.items()) {
            Vector2f at = project(item.at());
            float d = (at.x - mouseX) * (at.x - mouseX) + (at.y - mouseY) * (at.y - mouseY);
            if (d < best) { best = d; name = item.stack().getHoverName(); }
        }
        if (name == null) {
            best = 12 * 12;
            for (GuideFrame.BlockDraw block : frame.blocks()) {
                // The floor isn't worth naming
                if (block.pos().getY() == 0 && scene().plate().width() > 0) continue;
                Vector2f at = camera.project(block.pos().getX() + 0.5, block.pos().getY() + 0.5 + block.lift(), block.pos().getZ() + 0.5);
                float d = (at.x - mouseX) * (at.x - mouseX) + (at.y - mouseY) * (at.y - mouseY);
                if (d < best) { best = d; name = block.state().getBlock().getName(); }
            }
        }
        if (name != null) graphics.setTooltipForNextFrame(font, name, mouseX, mouseY);
    }

    // ---- input ----

    private void press(int button)
    {
        if (!enabled(button)) return;
        switch (button) {
            case 0 -> select(index - 1);
            case 1 -> { time = 0; paused = false; }
            case 2 -> {
                if (finished()) { time = 0; paused = false; }
                else paused = !paused;
            }
            case 3 -> select(index + 1);
            default -> {}
        }
    }

    private void seek(double mouseX)
    {
        float along = Mth.clamp((float) (mouseX - viewport.left()) / Math.max(1, viewport.width()), 0, 1);
        time = Math.round(along * playback.length());
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick)
    {
        int button = buttonAt(event.x(), event.y());
        if (button >= 0) {
            press(button);
            return true;
        }
        if (onTimeline(event.x(), event.y())) {
            scrubbing = true;
            seek(event.x());
            return true;
        }
        if (viewport.containsPoint((int) event.x(), (int) event.y())) {
            dragging = true;
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY)
    {
        if (scrubbing) {
            seek(event.x());
            return true;
        }
        if (dragging) {
            turnYaw -= (float) dragX * 0.8F;
            // The same limits as the pitch drawn (extractRenderState), so dragging back never has a dead zone
            turnPitch = Mth.clamp(turnPitch + (float) dragY * 0.8F, -10 - scene().camera().pitch(), 89 - scene().camera().pitch());
            return true;
        }
        return super.mouseDragged(event, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event)
    {
        dragging = false;
        scrubbing = false;
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        if (viewport.containsPoint((int) mouseX, (int) mouseY)) {
            zoom = Mth.clamp(zoom * (float) Math.pow(1.15, scrollY), 0.4F, 3F);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(KeyEvent event)
    {
        switch (event.key()) {
            case InputConstants.KEY_LEFT -> { press(0); return true; }
            case InputConstants.KEY_RIGHT -> { press(3); return true; }
            case InputConstants.KEY_SPACE -> { press(2); return true; }
            case InputConstants.KEY_R -> { press(1); return true; }
            default -> { return super.keyPressed(event); }
        }
    }

    // Back to where it was opened from
    @Override
    public void onClose()
    {
        Minecraft.getInstance().gui.setScreen(previous);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
