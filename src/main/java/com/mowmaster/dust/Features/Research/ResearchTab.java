package com.mowmaster.dust.Features.Research;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// One tab of the research book. Dust's are in Tabs/ (Blocks, Items, Creatures, Bosses); other mods can add their own:
// extend this and call ResearchTabs.register(new MyTab()) from your mod's constructor (on both sides).
//
// A tab owns a set of entries, each named by an Identifier (a block id, an entity type id, or anything the tab likes),
// and says how they look and what in the world observes them. Research for a tab is saved under the tab's id, so the id
// must never change.
public abstract class ResearchTab
{
    private final Identifier id;
    private final int sortOrder;
    private @Nullable List<Identifier> entries;
    private @Nullable List<List<Identifier>> groups;

    // sortOrder: where the tab goes along the top (lower = further left; Dust's use 100, 200, 300, 400)
    protected ResearchTab(Identifier id, int sortOrder)
    {
        this.id = id;
        this.sortOrder = sortOrder;
    }

    public final Identifier id() { return id; }
    public final int sortOrder() { return sortOrder; }

    // research.<namespace>.tab.<path>
    public Component title()
    {
        return Component.translatable("research." + id.getNamespace() + ".tab." + id.getPath());
    }

    public abstract ItemStack tabIcon();

    // ---- entries ----

    // Every entry, in the order the book lists them. Worked out on first use and kept until clearCache (tags reloading),
    // so it can depend on tags.
    protected abstract List<Identifier> computeEntries();

    // How entries are shown in the list: each group is one icon (all of its members are researched separately and shown
    // together on its page). Default: every entry on its own. Must cover every entry exactly once, in list order.
    protected List<List<Identifier>> computeGroups(List<Identifier> entries)
    {
        List<List<Identifier>> singles = new ArrayList<>();
        for (Identifier entry : entries) singles.add(List.of(entry));
        return singles;
    }

    public final List<Identifier> entries()
    {
        if (entries == null) entries = List.copyOf(computeEntries());
        return entries;
    }

    public final List<List<Identifier>> groups()
    {
        if (groups == null) groups = List.copyOf(computeGroups(entries()));
        return groups;
    }

    // The group an entry belongs to (just itself if it isn't grouped)
    public final List<Identifier> groupOf(Identifier entry)
    {
        for (List<Identifier> group : groups()) {
            if (group.contains(entry)) return group;
        }
        return List.of(entry);
    }

    public void clearCache()
    {
        entries = null;
        groups = null;
    }

    // ---- how an entry looks ----

    public abstract ItemStack icon(Identifier entry);

    public abstract Component name(Identifier entry);

    // A GUI sprite to show for the entry in the book's list instead of its icon item, or null for the item.
    // (A sprite is an image in the game's GUI atlas, named by its path under textures/gui/sprites/.) icon() is still
    // used everywhere else: the entry's own page, toasts, the side view.
    public @Nullable Identifier listSprite(Identifier entry)
    {
        return null;
    }

    // The colour listSprite is drawn in (0xAARRGGBB; white leaves the sprite as it is). Element icons are greyscale
    // and take their element's colour this way.
    public int listTint(Identifier entry)
    {
        return 0xFFFFFFFF;
    }

    // A mob to draw large on the entry's page instead of its icon, or null for the icon (client only)
    public @Nullable EntityType<?> displayEntity(Identifier entry)
    {
        return null;
    }

    // The entry's built-in overview notes: research.<entry namespace>.note.<entry path> if the lang file has it (checked
    // by the book), otherwise this
    public Component defaultNote(Identifier entry)
    {
        return Component.translatable("research." + id.getNamespace() + ".note.default." + id.getPath(), name(entry));
    }

    // false: entries have no Research page about their dust (the Research tab's own discoveries)
    public boolean showsDustPage()
    {
        return true;
    }

    // false: an entry's left page has no drops / sources section at all (Effects)
    public boolean showsDrops()
    {
        return true;
    }

    // true: an entry's left page lists its Sources (researched entries seen dropping it) instead of its own drops.
    // For tabs whose entries are things that get dropped (items) rather than things that drop.
    public boolean showsSources()
    {
        return false;
    }

    // ---- what observes it ----

    // The entry for a block looked at (or broken), or null if this tab doesn't cover it
    public @Nullable Identifier entryForBlock(BlockState state)
    {
        return null;
    }

    // The entry for an entity looked at, or null if this tab doesn't cover it. Default: a dropped item goes by its item
    // (entryForItem).
    public @Nullable Identifier entryForEntity(Entity entity)
    {
        return entity instanceof ItemEntity dropped ? entryForItem(dropped.getItem()) : null;
    }

    // The entry for an item (dropped in the world, or crafted / used in crafting), or null if this tab doesn't cover it
    public @Nullable Identifier entryForItem(ItemStack stack)
    {
        return null;
    }

    // How long something on this tab must be looked at to observe it, in ticks (20 = one second)
    public int observeTicks()
    {
        return 20;
    }
}
