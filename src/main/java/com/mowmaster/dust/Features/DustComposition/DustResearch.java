package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

// What dust research adds to a player's Research Notes (only while they carry them, like all research)
public final class DustResearch
{
    private DustResearch() {}

    // They burned it and saw the colour of its flames. Burning something counts as finding it.
    public static void noteBurned(ServerPlayer player, @Nullable ResearchEntry entry, int color)
    {
        if (entry == null || !Research.carriesNotes(player)) return;
        Research.observe(player, entry, player.blockPosition());
        Research.experiment(player, entry, research -> research.withFireColor(color));
    }

    // They read its composition on a Dust Testing Disk
    public static void noteComposition(ServerPlayer player, @Nullable ResearchEntry entry, DustComposition composition)
    {
        if (entry == null || !Research.carriesNotes(player)) return;
        Research.observe(player, entry, player.blockPosition());
        Research.experiment(player, entry, research -> research.withComposition(composition));
    }
}
