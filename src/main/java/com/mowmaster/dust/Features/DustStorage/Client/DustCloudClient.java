package com.mowmaster.dust.Features.DustStorage.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustStorage.DustClouds;
import com.mowmaster.dust.Features.DustStorage.DustPileBlockEntity;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

// The dusty air round Dust Piles out in the open (DustClouds), as the player sees it:
//   motes   specks of a pile's colour drifting about in its cloud, more for a bigger pile (pileTick)
//   haze    with their eyes in a cloud, the view is washed over in its colour, thicker the more dust there is: half
//           (50% visibility) at DustClouds.IGNITE, the thickness at which a spark sets it off; at most MAX_HAZE. It fades
//           in and out (each tick, FADE of the way), drawn over the world just under the HUD as the lamps' hue is.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class DustCloudClient
{
    private static final float MAX_HAZE = 0.85F;
    private static final float FADE = 0.15F;
    private static final double MOTE_DISTANCE = 40;
    // Motes a tick: a pile's dust / MOTES_PER on average
    private static final float MOTES_PER = 300F;

    private static float haze;
    private static int hazeColor = 0xFFFFFF;

    private DustCloudClient() {}

    // A pile's client tick: motes in its cloud, if it's out in the open and the player is near
    public static void pileTick(DustPileBlockEntity pile)
    {
        Level level = pile.getLevel();
        Minecraft minecraft = Minecraft.getInstance();
        if (level == null || minecraft.player == null) return;
        BlockPos pos = pile.getBlockPos();
        if (minecraft.player.distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(pos)) > MOTE_DISTANCE * MOTE_DISTANCE || !pile.isOpen()) return;
        RandomSource random = level.getRandom();
        float motes = pile.total() / MOTES_PER;
        int count = (int) motes + (random.nextFloat() < motes - (int) motes ? 1 : 0);
        if (count <= 0) return;
        DustParticleOptions mote = new DustParticleOptions(pile.color(), 0.8F);
        double reach = DustClouds.RADIUS + 0.5;
        for (int i = 0; i < count; i++) {
            double x = pos.getX() + 0.5 + (random.nextDouble() * 2 - 1) * reach;
            double z = pos.getZ() + 0.5 + (random.nextDouble() * 2 - 1) * reach;
            double y = pos.getY() + random.nextDouble() * (DustClouds.RISE + 1);
            level.addParticle(mote, x, y, z, 0, 0.005, 0);
        }
    }

    // Each tick: how thick the air is round the player's eyes, and the haze fading towards that
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            haze = 0;
            return;
        }
        DustClouds.Cloud cloud = DustClouds.density(minecraft.level, BlockPos.containing(minecraft.player.getEyePosition()));
        float target = Math.min(MAX_HAZE, cloud.total() / (2F * DustClouds.IGNITE));
        if (cloud.total() > 0) hazeColor = cloud.color();
        haze += (target - haze) * FADE;
        if (haze < 0.002F) haze = 0;
    }

    @SubscribeEvent
    public static void registerLayer(RegisterGuiLayersEvent event)
    {
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dust_haze"), DustCloudClient::renderHaze);
    }

    private static void renderHaze(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker)
    {
        if (haze <= 0) return;
        int alpha = Math.round(haze * 255);
        graphics.fill(0, 0, graphics.guiWidth(), graphics.guiHeight(), (alpha << 24) | hazeColor);
    }
}
