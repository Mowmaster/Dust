package com.mowmaster.dust.Features.Lamps.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Lamps.LampBlockEntity;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.util.HashMap;
import java.util.Map;

// A lit lamp's area, as the player sees it:
//   motes      a few small specks of its colour drifting about in its area all the time it's lit
//   the hue    standing in a lit lamp's area, the view takes on a faint wash of its colour (fading in and out; two
//              lamps of different colours mix), drawn over the world just under the HUD
// Lit lamps report themselves every tick they're lit (LampBlockEntity.clientTick); one not heard from for a moment
// (out of fuel, broken, out of range) drops out.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class LampClient
{
    // How strong the hue gets (0-1), how fast it fades in or out (each tick, this share of the way), how near the player
    // has to be for motes (blocks)
    private static final float HUE_STRENGTH = 0.10F;
    private static final float HUE_FADE = 0.12F;
    private static final double MOTE_DISTANCE = 48;
    // How long a lamp counts as lit after it last reported (ticks)
    private static final long FORGET_AFTER = 10;

    // The lit lamps heard from lately: their colour and the game time they last reported
    private record Lit(int color, long time) {}
    private static final Map<BlockPos, Lit> LIT = new HashMap<>();

    private static float hue;
    private static int hueColor = 0xFFFFFF;

    private LampClient() {}

    // A lit lamp's client tick: it's remembered as lit, and a mote or two drift up somewhere in its area
    public static void litTick(LampBlockEntity lamp)
    {
        Level level = lamp.getLevel();
        Minecraft minecraft = Minecraft.getInstance();
        if (level == null || minecraft.player == null) return;
        BlockPos pos = lamp.getBlockPos();
        LIT.put(pos, new Lit(lamp.color(), level.getGameTime()));
        Vec3 centre = Vec3.atCenterOf(pos);
        if (minecraft.player.distanceToSqr(centre) > MOTE_DISTANCE * MOTE_DISTANCE) return;
        RandomSource random = level.getRandom();
        double radius = LampBlockEntity.RADIUS;
        for (int i = 0; i < 2; i++) {
            // A point somewhere in the sphere: picked in the cube round it, kept only if it's inside
            double dx = (random.nextDouble() * 2 - 1) * radius, dy = (random.nextDouble() * 2 - 1) * radius, dz = (random.nextDouble() * 2 - 1) * radius;
            if (dx * dx + dy * dy + dz * dz > radius * radius) continue;
            level.addParticle(lamp.mote(0.6F), centre.x + dx, centre.y + dy, centre.z + dz, 0, 0.02, 0);
        }
    }

    // Each tick: whether the player stands in a lit lamp's area, and the hue fading towards that
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            LIT.clear();
            hue = 0;
            return;
        }
        long now = minecraft.level.getGameTime();
        LIT.values().removeIf(lit -> now - lit.time() > FORGET_AFTER || now < lit.time());
        Vec3 eye = minecraft.player.getEyePosition();
        double reach = LampBlockEntity.RADIUS * LampBlockEntity.RADIUS;
        int red = 0, green = 0, blue = 0, count = 0;
        for (Map.Entry<BlockPos, Lit> entry : LIT.entrySet()) {
            if (Vec3.atCenterOf(entry.getKey()).distanceToSqr(eye) > reach) continue;
            int color = entry.getValue().color();
            red += (color >> 16) & 0xFF;
            green += (color >> 8) & 0xFF;
            blue += color & 0xFF;
            count++;
        }
        if (count > 0) hueColor = ((red / count) << 16) | ((green / count) << 8) | (blue / count);
        float target = count > 0 ? HUE_STRENGTH : 0;
        hue += (target - hue) * HUE_FADE;
        if (hue < 0.002F) hue = 0;
    }

    // The hue: a see-through wash of colour over the whole view, just above vanilla's camera overlays (the pumpkin
    // blur, powder snow...) and under the rest of the HUD
    @SubscribeEvent
    public static void registerLayer(RegisterGuiLayersEvent event)
    {
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, Identifier.fromNamespaceAndPath(DustReferences.MODID, "lamp_hue"), LampClient::renderHue);
    }

    private static void renderHue(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker)
    {
        if (hue <= 0) return;
        int alpha = Math.round(hue * 255);
        graphics.fill(0, 0, graphics.guiWidth(), graphics.guiHeight(), (alpha << 24) | hueColor);
    }
}
