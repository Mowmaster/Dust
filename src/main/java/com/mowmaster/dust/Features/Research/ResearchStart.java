package com.mowmaster.dust.Features.Research;

import com.mowmaster.dust.DustReferences;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

import java.util.LinkedHashSet;
import java.util.Set;

// What every player knows from the start: anything vanilla's crafting table makes (a minecraft: crafting recipe,
// RecipeCatalog.vanillaCrafted) is already in their Blocks and Items tabs, recipe and all. Given quietly (no toasts) when
// a player joins, and to everyone when datapacks reload (recipes may have changed). EventPriority.LOW: after
// RecipeCatalog has rebuilt its lists for the reload.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class ResearchStart
{
    private ResearchStart() {}

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDatapackSync(OnDatapackSyncEvent event)
    {
        event.getRelevantPlayers().forEach(ResearchStart::grant);
    }

    public static void grant(ServerPlayer player)
    {
        Set<ResearchEntry> entries = new LinkedHashSet<>();
        for (Item item : RecipeCatalog.vanillaCrafted()) {
            ResearchEntry entry = ResearchTabs.entryForItem(new ItemStack(item));
            if (entry != null && (entry.tab() == ResearchTabs.BLOCKS || entry.tab() == ResearchTabs.ITEMS)) entries.add(entry);
        }
        Research.grantQuietly(player, entries);
    }
}
