package com.mowmaster.dust.Features.Guide.Client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Guide.GuideRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jspecify.annotations.Nullable;

import java.util.List;

// Opening the guide: hover an item that has guide scenes in any screen (inventory, chests, creative tabs, recipe
// viewers) and its tooltip says "Hold [G] for the guide"; hold the key and the words fill in gold, then the guide opens
// (GuideScreen), going back to that screen when it's closed.
// The key works inside screens only (KeyConflictContext.GUI), so it doesn't clash with keys used in the world. It's
// followed through NeoForge's screen key events (pressed / released): Minecraft only updates its own key mappings while
// no screen is open. Typing in a text box (the creative search) doesn't count.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class GuideClient
{
    // Since 1.21.9 a key's category is a KeyMapping.Category (an id), registered with the keys
    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(DustReferences.MODID, "guide"));
    public static final KeyMapping KEY = new KeyMapping("key.dust.guide", InputConstants.KEY_G, CATEGORY);
    // How long to hold it (ticks)
    private static final int HOLD_TICKS = 14;

    static {
        KEY.setKeyConflictContext(KeyConflictContext.GUI);
    }

    private static boolean held;
    private static int progress;
    private static long ticks;
    // The item whose tooltip was last shown, and when (ticks)
    private static ItemStack hovered = ItemStack.EMPTY;
    private static long hoveredAt = Long.MIN_VALUE / 2;

    private GuideClient() {}

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event)
    {
        event.registerCategory(CATEGORY);
        event.register(KEY);
    }

    private static @Nullable Screen screen()
    {
        //? if >=26.2 {
        return Minecraft.getInstance().gui.screen();
        //?} else {
        /*return Minecraft.getInstance().screen;
        *///?}
    }

    private static boolean matches(KeyEvent key)
    {
        return KEY.isActiveAndMatches(InputConstants.getKey(key));
    }

    private static boolean typing(Screen screen)
    {
        return screen.getFocused() instanceof EditBox || screen.getFocused() instanceof MultiLineEditBox;
    }

    @SubscribeEvent
    public static void onKeyPressed(ScreenEvent.KeyPressed.Pre event)
    {
        if (matches(event.getKeyEvent()) && !typing(event.getScreen())) held = true;
    }

    @SubscribeEvent
    public static void onKeyReleased(ScreenEvent.KeyReleased.Pre event)
    {
        if (matches(event.getKeyEvent())) held = false;
    }

    @SubscribeEvent
    public static void onScreenClosing(ScreenEvent.Closing event)
    {
        held = false;
        progress = 0;
    }

    // An item with guides: the hint line, filling in as the key is held. Tooltips are made every frame the item is
    // hovered, so this is also how the hovered item is known. (getEntity is null when tooltips are made for searching.)
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event)
    {
        Screen screen = screen();
        if (event.getEntity() == null || screen == null || screen instanceof GuideScreen) return;
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty() || !GuideRegistry.has(stack.getItem())) return;
        hovered = stack;
        hoveredAt = ticks;
        event.getToolTip().add(hint());
    }

    private static Component hint()
    {
        String text = Component.translatable("guide.dust.hold", KEY.getTranslatedKeyMessage()).getString();
        int filled = Math.round(text.length() * Math.min(1F, progress / (float) HOLD_TICKS));
        return Component.literal(text.substring(0, filled)).withStyle(ChatFormatting.GOLD)
                .append(Component.literal(text.substring(filled)).withStyle(ChatFormatting.DARK_GRAY));
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        ticks++;
        Screen screen = screen();
        boolean hovering = screen != null && !(screen instanceof GuideScreen) && ticks - hoveredAt <= 2
                && !hovered.isEmpty() && GuideRegistry.has(hovered.getItem());
        if (held && hovering) {
            if (++progress >= HOLD_TICKS) {
                progress = 0;
                held = false;
                open(hovered, screen);
            }
        } else if (progress > 0) {
            progress = Math.max(0, progress - 2);
        }
    }

    // Opens the guide for this item (any code can call this, e.g. a button in a book), coming back to previous
    public static void open(ItemStack stack, @Nullable Screen previous)
    {
        List<GuideRegistry.Entry> scenes = GuideRegistry.scenesFor(stack.getItem());
        if (scenes.isEmpty()) return;
        Minecraft.getInstance().gui.setScreen(new GuideScreen(stack, scenes, previous));
    }
}
