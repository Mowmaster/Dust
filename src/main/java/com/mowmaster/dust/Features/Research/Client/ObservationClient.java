package com.mowmaster.dust.Features.Research.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Research.ObservationPayloads;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.Nullable;

// What the player sees and hears while observing:
// - a small icon + progress bar under the crosshair while something new is being observed
// - a quill scratching every so often while it fills (sounds played only for this player, on their client)
// - when it's done: a ding and a toast (ResearchToast) with what was found
// The server says how far along it is (ObservationPayloads.Progress); if it goes quiet the bar is hidden.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class ObservationClient
{
    private static final Identifier LAYER = Identifier.fromNamespaceAndPath(DustReferences.MODID, "observation");
    // Hide the bar if no progress has arrived for this many ticks (the server sends one every 4)
    private static final int TIMEOUT_TICKS = 12;
    private static final int SCRIBBLE_EVERY = 14;
    private static final int BAR_WIDTH = 34;

    // One sound per burst: crafting notes the result and every ingredient at once, and a discovery's own ding covers
    // anything noted with it. Ticks left before another "noted" sound may play.
    private static final int SOUND_COOLDOWN = 10;
    private static int soundCooldown;

    private static @Nullable ResearchEntry observing;
    private static float progress;
    private static int sinceUpdate;
    private static int ticksObserving;

    private ObservationClient() {}

    // ---- messages from the server (registered in DustClient) ----

    public static void onProgress(ObservationPayloads.Progress payload, IPayloadContext context)
    {
        ResearchEntry entry = payload.progress() < 0 ? null : Research.byKey(payload.tab(), payload.id());
        if (entry == null) {
            observing = null;
            return;
        }
        if (!entry.equals(observing)) ticksObserving = 0;
        observing = entry;
        progress = payload.progress();
        sinceUpdate = 0;
    }

    public static void onDiscovered(ObservationPayloads.Discovered payload, IPayloadContext context)
    {
        ResearchEntry entry = Research.byKey(payload.tab(), payload.id());
        if (entry == null) return;
        Minecraft minecraft = Minecraft.getInstance();
        observing = null;
        // A ding: the note block "chime", played as a UI sound (only this player, not positioned in the world)
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_CHIME.value(), payload.newBiome() ? 1.5F : 1.8F, 0.8F));
        soundCooldown = SOUND_COOLDOWN;
        minecraft.gui.toastManager().addToast(new ResearchToast(entry, payload.newBiome()));
    }

    // New information on a known entry: a ding for a recipe (crafted, smelted, cooked), a scribble for anything else
    public static void onNoted(ObservationPayloads.Noted payload, IPayloadContext context)
    {
        if (soundCooldown > 0) return;
        soundCooldown = SOUND_COOLDOWN;
        Minecraft minecraft = Minecraft.getInstance();
        if (payload.recipe()) {
            minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_CHIME.value(), 1.6F, 0.7F));
        } else {
            minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1.0F, 0.5F));
        }
    }

    // ---- scribbling while observing ----

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        if (soundCooldown > 0) soundCooldown--;
        if (observing == null) return;
        if (++sinceUpdate > TIMEOUT_TICKS) {
            observing = null;
            return;
        }
        if (ticksObserving++ % SCRIBBLE_EVERY == 0) {
            float pitch = 0.9F + Minecraft.getInstance().level.getRandom().nextFloat() * 0.3F;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, pitch, 0.35F));
        }
    }

    // ---- the progress bar ----

    @SubscribeEvent
    public static void registerLayer(RegisterGuiLayersEvent event)
    {
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, LAYER, ObservationClient::render);
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker)
    {
        ResearchEntry entry = observing;
        Minecraft minecraft = Minecraft.getInstance();
        if (entry == null || minecraft.gui.screen() != null) return;

        int centreX = graphics.guiWidth() / 2;
        int y = graphics.guiHeight() / 2 + 10;
        int left = centreX - (16 + 3 + BAR_WIDTH) / 2;

        graphics.fakeItem(entry.icon(), left, y);
        int barX = left + 19;
        int barY = y + 6;
        graphics.fill(barX - 1, barY - 1, barX + BAR_WIDTH + 1, barY + 4, 0xC0000000);
        graphics.fill(barX, barY, barX + Math.round(BAR_WIDTH * Math.min(1F, progress)), barY + 3, 0xFFE8C860);
    }
}
