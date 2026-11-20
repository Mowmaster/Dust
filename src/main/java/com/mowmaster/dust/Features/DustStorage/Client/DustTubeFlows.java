package com.mowmaster.dust.Features.DustStorage.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustStorage.DustTubeFlow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Dust running through Glass Dust Tubes (DustTubeFlow), like sand poured down a pipe: a stream of the element's dust
// TRAIL blocks long running from block centre to block centre along the tubes at SPEED, the glass along it sprinkled with
// grains of its colour (each a little lighter or darker, as sand is), until its tail has run out at the far end with a
// little puff.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class DustTubeFlows
{
    // Blocks a tick the head travels
    private static final double SPEED = 0.35;
    // How long the stream is (blocks), grains a tick for each block of it, how far they stray from the tube's middle
    // (the glass is 5 pixels wide), and how much lighter or darker one can be
    private static final double TRAIL = 2.5;
    private static final double GRAINS_PER_BLOCK = 2;
    private static final double JITTER = 0.09;
    private static final float SHADE = 0.18F;
    // The most flows drawn at once (the oldest are dropped first)
    private static final int MAX_FLOWS = 128;

    private static final class Flow
    {
        final List<Vec3> points = new ArrayList<>();
        final int color;
        final double length;
        double travelled;

        Flow(DustTubeFlow message)
        {
            for (BlockPos pos : message.path()) points.add(Vec3.atCenterOf(pos));
            color = message.color();
            double total = 0;
            for (int i = 1; i < points.size(); i++) total += points.get(i).distanceTo(points.get(i - 1));
            length = total;
        }

        // Where the head is when it has gone this far along the way
        Vec3 at(double distance)
        {
            double left = distance;
            for (int i = 1; i < points.size(); i++) {
                Vec3 from = points.get(i - 1), to = points.get(i);
                double step = from.distanceTo(to);
                if (left <= step) return step <= 1.0E-6 ? to : from.lerp(to, left / step);
                left -= step;
            }
            return points.getLast();
        }
    }

    private static final List<Flow> FLOWS = new ArrayList<>();
    // The colour of the dust that last ran through each tube (its filled core's tint: DustTubeTint)
    private static final java.util.Map<BlockPos, Integer> COLORS = new java.util.HashMap<>();

    public static int colorAt(BlockPos pos)
    {
        return COLORS.getOrDefault(pos, 0xFFFFFF);
    }

    private DustTubeFlows() {}

    public static void onFlow(DustTubeFlow message, IPayloadContext context)
    {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || message.path().size() < 2) return;
        // A tube's core changes colour: it's drawn again
        for (BlockPos pos : message.path()) {
            Integer before = COLORS.put(pos.immutable(), message.color());
            if (before != null && before == message.color()) continue;
            net.minecraft.world.level.block.state.BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof com.mowmaster.dust.Features.DustStorage.DustTubeBlock) {
                level.sendBlockUpdated(pos, state, state, net.minecraft.world.level.block.Block.UPDATE_ALL);
            } else {
                COLORS.remove(pos);
            }
        }
        if (FLOWS.size() >= MAX_FLOWS) FLOWS.removeFirst();
        FLOWS.add(new Flow(message));
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            FLOWS.clear();
            COLORS.clear();
            return;
        }
        if (Minecraft.getInstance().isPaused()) return;
        RandomSource random = level.getRandom();
        Iterator<Flow> flows = FLOWS.iterator();
        while (flows.hasNext()) {
            Flow flow = flows.next();
            // The head runs on (stopping at the far end); the tail follows TRAIL behind
            flow.travelled += SPEED;
            double head = Math.min(flow.length, flow.travelled), tail = Math.max(0, flow.travelled - TRAIL);
            double stretch = head - tail;
            int grains = (int) (stretch * GRAINS_PER_BLOCK) + (random.nextDouble() < stretch * GRAINS_PER_BLOCK % 1 ? 1 : 0);
            for (int i = 0; i < grains; i++) {
                Vec3 at = flow.at(tail + stretch * random.nextDouble());
                DustParticleOptions grain = new DustParticleOptions(shade(flow.color, (random.nextFloat() * 2 - 1) * SHADE), 0.4F + random.nextFloat() * 0.25F);
                level.addParticle(grain, at.x + (random.nextDouble() - 0.5) * JITTER * 2, at.y + (random.nextDouble() - 0.5) * JITTER * 2,
                        at.z + (random.nextDouble() - 0.5) * JITTER * 2, 0, 0, 0);
            }
            if (flow.travelled - TRAIL >= flow.length) {
                Vec3 end = flow.points.getLast();
                for (int i = 0; i < 4; i++) {
                    level.addParticle(new DustParticleOptions(flow.color, 0.6F), end.x, end.y, end.z,
                            (random.nextDouble() - 0.5) * 0.06, (random.nextDouble() - 0.5) * 0.06, (random.nextDouble() - 0.5) * 0.06);
                }
                flows.remove();
            }
        }
    }

    // A colour made lighter (amount above 0: that share of the way to white) or darker (below 0: towards black)
    private static int shade(int color, float amount)
    {
        int red = (color >> 16) & 0xFF, green = (color >> 8) & 0xFF, blue = color & 0xFF;
        if (amount >= 0) {
            red += (int) ((255 - red) * amount);
            green += (int) ((255 - green) * amount);
            blue += (int) ((255 - blue) * amount);
        } else {
            red = (int) (red * (1 + amount));
            green = (int) (green * (1 + amount));
            blue = (int) (blue * (1 + amount));
        }
        return red << 16 | green << 8 | blue;
    }
}
