package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import com.mowmaster.dust.Features.Research.EntryResearch;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchData;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import com.mowmaster.dust.Features.Research.ResearchUnlocks;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import com.mowmaster.dust.DustReferences;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

import java.util.Map;

// What dust research adds to a player's Research Notes (only while they carry them, like all research)
@EventBusSubscriber(modid = DustReferences.MODID)
public final class DustResearch
{
    private DustResearch() {}

    // They burned it and saw the colour of its flames. Burning something counts as finding it.
    public static void noteBurned(ServerPlayer player, @Nullable ResearchEntry entry, int color)
    {
        if (entry == null || !Research.carriesNotes(player)) return;
        Research.observe(player, entry, player.blockPosition());
        Research.experiment(player, entry, research -> research.withFireColor(color));
        // A pure flame shows that primal dust on its own
        for (EnumElement primal : EnumElement.values()) {
            if (color == DustComposition.pureColor(primal)) discoverElement(player, DustElements.of(primal));
        }
        ResearchUnlocks.check(player);
    }

    // They measured it on the Elemental Examiner: each crystal's element and how much of the item it is. An item that's
    // all one crystal's element discovers that element.
    public static void noteMeasured(ServerPlayer player, @Nullable ResearchEntry entry, Map<DustElement, Integer> measured)
    {
        if (!Research.carriesNotes(player)) return;
        for (Map.Entry<DustElement, Integer> crystal : measured.entrySet()) {
            if (crystal.getValue() >= 100) discoverElement(player, crystal.getKey());
        }
        if (entry != null) {
            Research.observe(player, entry, player.blockPosition());
            Research.experiment(player, entry, research -> research.withMeasured(measured));
        }
        ResearchUnlocks.check(player);
    }

    // Picking up a primal crystal (from its ore) discovers its primal element. Crystals of mixed elements are discovered
    // on the Elemental Examiner instead (an item that's all that element).
    @SubscribeEvent
    public static void onPickUpCrystal(ItemEntityPickupEvent.Post event)
    {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;
        DustElement element = CrystalElements.of(event.getOriginalStack());
        if (element != null && element.isPrimal()) discoverElement(player, element);
    }

    // The element's entry in the Elements tab (the usual toast the first time)
    public static void discoverElement(ServerPlayer player, DustElement element)
    {
        if (!Research.carriesNotes(player)) return;
        Research.observe(player, new ResearchEntry(ResearchTabs.ELEMENTS, element.id()), player.blockPosition());
    }

    // Whether the player has found something made only of this dust: burned something whose flame was that dust's
    // pure colour (only pure fire burns pure red, and so on), or measured something that's all that element
    public static boolean hasFoundPure(ResearchData data, EnumElement element)
    {
        int pure = DustComposition.pureColor(element);
        DustElement primal = DustElements.of(element);
        for (Map<Identifier, EntryResearch> tab : data.tabs().values()) {
            for (EntryResearch research : tab.values()) {
                EntryResearch.Dust dust = research.dust();
                if (dust.burned() && dust.fireColor() == pure) return true;
                if (dust.measured().getOrDefault(primal, 0) >= 100) return true;
            }
        }
        return false;
    }
}
