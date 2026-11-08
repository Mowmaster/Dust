package com.mowmaster.dust.Features.Research;

import com.mowmaster.dust.Features.Research.Tabs.BlockResearchTab;
import com.mowmaster.dust.Features.Research.Tabs.CreatureResearchTab;
import com.mowmaster.dust.Features.Research.Tabs.EffectResearchTab;
import com.mowmaster.dust.Features.Research.Tabs.ItemResearchTab;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// The research book's tabs. Dust registers its own here; other mods call register() from their mod constructor (it
// must run on both client and server, so the same tabs exist on both). The tabs are shown by sortOrder, then id.
//
// When something in the world could belong to several tabs, the first tab (in that order) that claims it wins.
public final class ResearchTabs
{
    // CopyOnWriteArrayList: safe to add to during mod loading while something else reads it
    private static final List<ResearchTab> TABS = new CopyOnWriteArrayList<>();

    public static final ResearchTab BLOCKS = register(new BlockResearchTab());
    public static final ResearchTab ITEMS = register(new ItemResearchTab());
    public static final ResearchTab CREATURES = register(new CreatureResearchTab(false));
    public static final ResearchTab BOSSES = register(new CreatureResearchTab(true));
    public static final EffectResearchTab EFFECTS = register(new EffectResearchTab());

    private ResearchTabs() {}

    public static <T extends ResearchTab> T register(T tab)
    {
        if (byId(tab.id()) != null) throw new IllegalArgumentException("Research tab registered twice: " + tab.id());
        TABS.add(tab);
        return tab;
    }

    // All tabs, in display order
    public static List<ResearchTab> all()
    {
        List<ResearchTab> sorted = new ArrayList<>(TABS);
        sorted.sort(Comparator.comparingInt(ResearchTab::sortOrder).thenComparing(tab -> tab.id().toString()));
        return sorted;
    }

    public static @Nullable ResearchTab byId(Identifier id)
    {
        for (ResearchTab tab : TABS) {
            if (tab.id().equals(id)) return tab;
        }
        return null;
    }

    public static @Nullable ResearchEntry entry(Identifier tabId, Identifier entryId)
    {
        ResearchTab tab = byId(tabId);
        return tab == null ? null : new ResearchEntry(tab, entryId);
    }

    public static @Nullable ResearchEntry entryForBlock(BlockState state)
    {
        for (ResearchTab tab : all()) {
            Identifier id = tab.entryForBlock(state);
            if (id != null) return new ResearchEntry(tab, id);
        }
        return null;
    }

    public static @Nullable ResearchEntry entryForEntity(Entity entity)
    {
        for (ResearchTab tab : all()) {
            Identifier id = tab.entryForEntity(entity);
            if (id != null) return new ResearchEntry(tab, id);
        }
        return null;
    }

    public static @Nullable ResearchEntry entryForItem(ItemStack stack)
    {
        for (ResearchTab tab : all()) {
            Identifier id = tab.entryForItem(stack);
            if (id != null) return new ResearchEntry(tab, id);
        }
        return null;
    }

    // Tags reloaded: tabs rebuild their entry lists (they may depend on tags)
    public static void clearCaches()
    {
        for (ResearchTab tab : TABS) tab.clearCache();
    }
}
