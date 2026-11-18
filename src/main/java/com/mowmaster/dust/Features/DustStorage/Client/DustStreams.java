package com.mowmaster.dust.Features.DustStorage.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustStorage.DustStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;
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

// Dust flowing (DustStream): a ribbon of dust motes in the element's colour drifting from where it came from to where
// it's going, over a second or two:
//   the path      it arcs up and over (a curve with its peak above the middle), and winds round that curve as it goes
//                 (a spiral, widest halfway, nothing at either end), so it looks blown along rather than shot
//   the flow      motes are let out along the stretch the head has passed, thickest just behind it, each drifting on a
//                 little along the way; some fade from the element's colour to a paler one as they go
//   the ends      a puff as it lifts off, a little burst as it arrives
// More dust moved makes the stream last longer and run thicker.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class DustStreams
{
    // How long a stream takes (ticks): MIN_TICKS, plus a tick for each TICK_PER units, up to MAX_TICKS
    private static final int MIN_TICKS = 18;
    private static final int MAX_TICKS = 44;
    private static final int TICK_PER = 4;
    // How high the arc rises over its length (share of the length, at least a block), and how wide it winds (blocks)
    private static final double ARC = 0.25;
    private static final double WIND = 0.35;
    // How many times it winds round on the way
    private static final double TURNS = 2.5;

    private static final class Flow
    {
        final Vec3 from, to, up, side;
        final int color, pale, ticks, thickness;
        final double arc, phase;
        int age;

        Flow(DustStream stream, RandomSource random)
        {
            from = stream.from();
            to = stream.to();
            color = stream.color();
            pale = paler(stream.color());
            ticks = Mth.clamp(MIN_TICKS + stream.amount() / TICK_PER, MIN_TICKS, MAX_TICKS);
            thickness = Mth.clamp(2 + stream.amount() / 20, 2, 5);
            Vec3 way = to.subtract(from);
            arc = Math.max(1.0, way.length() * ARC);
            // Two directions square to the path, for the winding (any pair will do; the path's own up if it's level)
            Vec3 along = way.lengthSqr() < 1.0E-6 ? new Vec3(0, 1, 0) : way.normalize();
            Vec3 helper = Math.abs(along.y) > 0.9 ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);
            side = along.cross(helper).normalize();
            up = side.cross(along).normalize();
            phase = random.nextDouble() * Math.PI * 2;
        }

        // Where the stream is at t (0 at the start, 1 at the end): on the arc, wound round it
        Vec3 at(double t)
        {
            Vec3 straight = from.lerp(to, t);
            // A parabola: nothing at the ends, arc at the middle
            double lift = 4 * arc * t * (1 - t);
            double angle = phase + t * TURNS * Math.PI * 2;
            double radius = WIND * Math.sin(Math.PI * t);
            return straight.add(0, lift, 0).add(side.scale(Math.cos(angle) * radius)).add(up.scale(Math.sin(angle) * radius));
        }
    }

    private static final List<Flow> FLOWS = new ArrayList<>();

    private DustStreams() {}

    // The message: a new stream, and a puff where it starts
    public static void onStream(DustStream stream, IPayloadContext context)
    {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        Flow flow = new Flow(stream, level.getRandom());
        FLOWS.add(flow);
        RandomSource random = level.getRandom();
        for (int i = 0; i < 6; i++) {
            level.addParticle(new DustParticleOptions(flow.color, 0.8F), flow.from.x + (random.nextDouble() - 0.5) * 0.4,
                    flow.from.y + (random.nextDouble() - 0.5) * 0.4, flow.from.z + (random.nextDouble() - 0.5) * 0.4, 0, 0.04, 0);
        }
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
            flow.age++;
            double head = Math.min(1.0, flow.age / (double) flow.ticks);
            // Motes let out behind the head, more of them nearer it, each with a nudge on along the path
            for (int i = 0; i < flow.thickness; i++) {
                double t = Math.max(0, head - random.nextDouble() * random.nextDouble() * 0.35);
                Vec3 at = flow.at(t);
                Vec3 ahead = flow.at(Math.min(1.0, t + 0.04)).subtract(at).scale(0.35);
                double jitter = 0.08;
                // Some fade from the colour to a paler one as they drift (dust_color_transition), the rest stay the colour
                net.minecraft.core.particles.ParticleOptions mote = random.nextInt(3) == 0 ? new DustColorTransitionOptions(flow.color, flow.pale, 0.7F)
                        : new DustParticleOptions(flow.color, 0.55F + random.nextFloat() * 0.35F);
                level.addParticle(mote, at.x + (random.nextDouble() - 0.5) * jitter, at.y + (random.nextDouble() - 0.5) * jitter,
                        at.z + (random.nextDouble() - 0.5) * jitter, ahead.x, ahead.y, ahead.z);
            }
            if (flow.age >= flow.ticks) {
                // Arrived: a little burst at the far end
                for (int i = 0; i < 8; i++) {
                    level.addParticle(new DustColorTransitionOptions(flow.color, flow.pale, 0.9F), flow.to.x, flow.to.y, flow.to.z,
                            (random.nextDouble() - 0.5) * 0.15, (random.nextDouble() - 0.2) * 0.15, (random.nextDouble() - 0.5) * 0.15);
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
