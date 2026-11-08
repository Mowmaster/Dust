package com.mowmaster.dust.Features.Research.Tabs;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Research.ResearchTab;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Items that aren't blocks (those are under Blocks) or spawn eggs: tools, food, ingots, dust...
// Observed by looking at one dropped in the world (an item entity).
// Colour variants are listed as one family, like blocks: the 16 dyes, the bundles, the harnesses, the eggs...
// (names that are the same once a colour at the front is ignored: red_dye, blue_dye -> "dye"; bundle, red_bundle).
// They're grouped within a namespace, so another mod's red_dye isn't mixed with vanilla's.
public class ItemResearchTab extends ResearchTab
{
    public ItemResearchTab()
    {
        super(Identifier.fromNamespaceAndPath(DustReferences.MODID, "items"), 200);
    }

    @Override
    public ItemStack tabIcon()
    {
        return new ItemStack(Items.IRON_INGOT);
    }

    @Override
    protected List<Identifier> computeEntries()
    {
        List<Identifier> list = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            if (researchable(item)) list.add(BuiltInRegistries.ITEM.getKey(item));
        }
        return list;
    }

    private static boolean researchable(Item item)
    {
        return item != Items.AIR && !(item instanceof BlockItem) && !(item instanceof SpawnEggItem);
    }

    @Override
    protected List<List<Identifier>> computeGroups(List<Identifier> entries)
    {
        // LinkedHashMap keeps the order families are first met in, so each is listed where its first member would be
        Map<String, List<Identifier>> families = new LinkedHashMap<>();
        for (Identifier entry : entries) {
            String key = entry.getNamespace() + ":" + ColorVariants.stripColors(entry.getPath());
            families.computeIfAbsent(key, k -> new ArrayList<>()).add(entry);
        }
        return new ArrayList<>(families.values());
    }

    @Override
    public @Nullable Identifier entryForItem(ItemStack stack)
    {
        return researchable(stack.getItem()) ? BuiltInRegistries.ITEM.getKey(stack.getItem()) : null;
    }

    // Items don't drop anything; their page shows what drops them
    @Override
    public boolean showsSources()
    {
        return true;
    }

    @Override
    public ItemStack icon(Identifier entry)
    {
        return new ItemStack(BuiltInRegistries.ITEM.getValue(entry));
    }

    @Override
    public Component name(Identifier entry)
    {
        return icon(entry).getHoverName();
    }
}
