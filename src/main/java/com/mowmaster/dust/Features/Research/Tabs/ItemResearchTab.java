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
import java.util.List;

// Items that aren't blocks (those are under Blocks) or spawn eggs: tools, food, ingots, dust...
// Observed by looking at one dropped in the world (an item entity).
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
    public @Nullable Identifier entryForItem(ItemStack stack)
    {
        return researchable(stack.getItem()) ? BuiltInRegistries.ITEM.getKey(stack.getItem()) : null;
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
