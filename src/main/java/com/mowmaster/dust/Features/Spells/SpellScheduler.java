package com.mowmaster.dust.Features.Spells;

import com.mowmaster.dust.DustReferences;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.List;

// Spell work to do a few ticks from now (an Echo's repeat...). In memory only: anything still waiting when the server
// stops is dropped.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class SpellScheduler
{
    private record Task(long due, Runnable work) {}

    private static final List<Task> TASKS = new ArrayList<>();

    private SpellScheduler() {}

    public static void later(MinecraftServer server, int ticks, Runnable work)
    {
        TASKS.add(new Task(server.getTickCount() + Math.max(1, ticks), work));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event)
    {
        if (TASKS.isEmpty()) return;
        long now = event.getServer().getTickCount();
        // Taken out before running, so work that schedules more doesn't upset the list being gone through
        List<Task> due = new ArrayList<>();
        TASKS.removeIf(task -> {
            if (task.due() > now) return false;
            due.add(task);
            return true;
        });
        for (Task task : due) task.work().run();
    }
}
