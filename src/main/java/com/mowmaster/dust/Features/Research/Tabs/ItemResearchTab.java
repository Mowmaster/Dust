package com.mowmaster.dust.Features.Research.Tabs;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.DustComposition.CrystalElements;
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
// Element crystals and element dusts are one entry each (dust:crystal, dust:element_dust), drawn untinted: what each
// element is lives on its Elements page (sneak + right-click on one with the book opens that: ResearchClient).
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

    // The one entry for every element crystal, and the one for every element dust
    public static final Identifier CRYSTAL = Identifier.fromNamespaceAndPath(DustReferences.MODID, "crystal");
    public static final Identifier ELEMENT_DUST = Identifier.fromNamespaceAndPath(DustReferences.MODID, "element_dust");

    @Override
    protected List<Identifier> computeEntries()
    {
        List<Identifier> list = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            // The shared entry goes where the first crystal / dust would have been
            Identifier shared = sharedEntry(new ItemStack(item));
            if (shared != null) {
                if (!list.contains(shared)) list.add(shared);
            } else if (researchable(item)) {
                list.add(BuiltInRegistries.ITEM.getKey(item));
            }
        }
        return list;
    }

    private static boolean researchable(Item item)
    {
        return item != Items.AIR && !(item instanceof BlockItem) && !(item instanceof SpawnEggItem);
    }

    // CRYSTAL for an element crystal, ELEMENT_DUST for an element dust, null for anything else
    private static @Nullable Identifier sharedEntry(ItemStack stack)
    {
        if (CrystalElements.of(stack) != null) return CRYSTAL;
        if (CrystalElements.ofDust(stack) != null) return ELEMENT_DUST;
        return null;
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
        Identifier shared = sharedEntry(stack);
        if (shared != null) return shared;
        return researchable(stack.getItem()) ? BuiltInRegistries.ITEM.getKey(stack.getItem()) : null;
    }

    // Items don't drop anything; their page shows what drops them
    @Override
    public boolean showsSources()
    {
        return true;
    }

    // For toasts and the side view, which show items: the shared entries show the order (white) crystal / dust
    @Override
    public ItemStack icon(Identifier entry)
    {
        if (entry.equals(CRYSTAL)) return new ItemStack(DustItemRegistry.CRYSTAL_WHITE.get());
        if (entry.equals(ELEMENT_DUST)) return new ItemStack(DustItemRegistry.DUST_WHITE.get());
        return new ItemStack(BuiltInRegistries.ITEM.getValue(entry));
    }

    // In the list and on their page the shared entries are the plain (untinted) crystal and dust:
    // textures/gui/sprites/research/crystal.png and element_dust.png
    @Override
    public @Nullable Identifier listSprite(Identifier entry)
    {
        if (entry.equals(CRYSTAL)) return Identifier.fromNamespaceAndPath(DustReferences.MODID, "research/crystal");
        if (entry.equals(ELEMENT_DUST)) return Identifier.fromNamespaceAndPath(DustReferences.MODID, "research/element_dust");
        return null;
    }

    @Override
    public Component name(Identifier entry)
    {
        if (entry.equals(CRYSTAL)) return Component.translatable("item.dust.crystal");
        if (entry.equals(ELEMENT_DUST)) return Component.translatable("research.dust.entry.element_dust");
        return icon(entry).getHoverName();
    }
}
