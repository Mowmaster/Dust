package com.mowmaster.dust.Features.DustStorage.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustStorage.DustTubeFlow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustColorTransitionOptions;
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

// Dust running through Glass Dust Tubes (DustTubeFlow): a glowing head of the element's dust racing from block centre
// to block centre along the tubes at SPEED, leaving a trail of motes that hang in the glass and fade, some paling as
// they go; a little puff where it pours out at the far end.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class DustTubeFlows
{
    // Blocks a tick the head travels
    private static final double SPEED = 0.35;
    // Motes let out a tick along the stretch the head just covered, and how far they stray from the tube's middle
    private static final int MOTES = 3;
    private static final double JITTER = 0.06;
    // The most flows drawn at once (the oldest are dropped first)
    private static final int MAX_FLOWS = 256;

    private static final class Flow
    {
        final List<Vec3> points = new ArrayList<>();
        final int color, pale;
        final double length;
        double travelled;

        Flow(DustTubeFlow message)
        {
            for (BlockPos pos : message.path()) points.add(Vec3.atCenterOf(pos));
            color = message.color();
            pale = paler(message.color());
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

    private DustTubeFlows() {}

    public static void onFlow(DustTubeFlow message, IPayloadContext context)
    {
        if (Minecraft.getInstance().level == null || message.path().size() < 2) return;
        if (FLOWS.size() >= MAX_FLOWS) FLOWS.removeFirst();
        FLOWS.add(new Flow(message));
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            FLOWS.clear();
            return;
        }
        if (Minecraft.getInstance().isPaused()) return;
        RandomSource random = level.getRandom();
        Iterator<Flow> flows = FLOWS.iterator();
        while (flows.hasNext()) {
            Flow flow = flows.next();
            double from = flow.travelled;
            flow.travelled = Math.min(flow.length, flow.travelled + SPEED);
            // Motes along the stretch just covered, so the trail has no gaps
            for (int i = 0; i < MOTES; i++) {
                Vec3 at = flow.at(from + (flow.travelled - from) * random.nextDouble());
                boolean fading = random.nextInt(3) == 0;
                net.minecraft.core.particles.ParticleOptions mote = fading ? new DustColorTransitionOptions(flow.color, flow.pale, 0.5F)
                        : new DustParticleOptions(flow.color, 0.35F + random.nextFloat() * 0.25F);
                level.addParticle(mote, at.x + (random.nextDouble() - 0.5) * JITTER * 2, at.y + (random.nextDouble() - 0.5) * JITTER * 2,
                        at.z + (random.nextDouble() - 0.5) * JITTER * 2, 0, 0, 0);
            }
            if (flow.travelled >= flow.length) {
                Vec3 end = flow.points.getLast();
                for (int i = 0; i < 4; i++) {
                    level.addParticle(new DustParticleOptions(flow.color, 0.6F), end.x, end.y, end.z,
                            (random.nextDouble() - 0.5) * 0.06, (random.nextDouble() - 0.5) * 0.06, (random.nextDouble() - 0.5) * 0.06);
                }
                flows.remove();
            }
        }
    }

    // A paler shade of a colour: halfway to white
    private static int paler(int color)
    {
        int red = (color >> 16) & 0xFF, green = (color >> 8) & 0xFF, blue = color & 0xFF;
        return ((red + 255) / 2 << 16) | ((green + 255) / 2 << 8) | (blue + 255) / 2;
    }
}
