package com.mowmaster.dust.Features.Spells;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Spells.api.Spell;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

// Spells waiting their turn: a Spell Stack's later spells, cast a few ticks after the first (SpellCasting.STACK_DELAY).
// Kept in memory only (a restart drops any still waiting); a player who has left or died by then misses theirs.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class DelayedCasts
{
    private record Waiting(UUID player, Spell spell, int[] ticksLeft) {}

    private static final List<Waiting> WAITING = new ArrayList<>();

    private DelayedCasts() {}

    public static void add(ServerPlayer player, Spell spell, int ticks)
    {
        WAITING.add(new Waiting(player.getUUID(), spell, new int[] {ticks}));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event)
    {
        if (WAITING.isEmpty()) return;
        // Casting may add more (a stacked spell with its own stack): those wait for the next tick
        List<Waiting> due = new ArrayList<>();
        for (Iterator<Waiting> it = WAITING.iterator(); it.hasNext(); ) {
            Waiting waiting = it.next();
            if (--waiting.ticksLeft()[0] <= 0) {
                due.add(waiting);
                it.remove();
            }
        }
        for (Waiting waiting : due) {
            ServerPlayer player = event.getServer().getPlayerList().getPlayer(waiting.player());
            if (player != null && player.isAlive()) SpellCasting.castStacked(player, waiting.spell());
        }
    }
}
