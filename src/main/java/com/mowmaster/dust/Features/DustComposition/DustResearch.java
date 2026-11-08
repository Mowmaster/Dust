package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import com.mowmaster.dust.Features.Research.EntryResearch;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchData;
import com.mowmaster.dust.Features.Research.ResearchUnlocks;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

import java.util.Map;

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
        ResearchUnlocks.check(player);
    }

    // They read its composition on a Dust Testing Disk
    public static void noteComposition(ServerPlayer player, @Nullable ResearchEntry entry, DustComposition composition)
    {
        if (entry == null || !Research.carriesNotes(player)) return;
        Research.observe(player, entry, player.blockPosition());
        Research.experiment(player, entry, research -> research.withComposition(composition));
        ResearchUnlocks.check(player);
    }

    // Whether the player has found something made only of this dust: burned something whose flame was that dust's
    // pure colour (only pure fire burns pure red, and so on), or read a pure composition on a Dust Testing Disk
    public static boolean hasFoundPure(ResearchData data, EnumElement element)
    {
        int pure = DustComposition.pureColor(element);
        for (Map<Identifier, EntryResearch> tab : data.tabs().values()) {
            for (EntryResearch research : tab.values()) {
                EntryResearch.Dust dust = research.dust();
                if (dust.burned() && dust.fireColor() == pure) return true;
                if (dust.composition().map(c -> c.isPure() && c.get(element) > 0).orElse(false)) return true;
            }
        }
        return false;
    }
}
