package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.Features.Research.EntryResearch;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import com.mowmaster.dust.Features.Machines.AlchemicalDust;
import com.mowmaster.dust.Features.Machines.Compounds;
import com.mowmaster.dust.Features.Machines.ProductSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// The row of element icons under an item's name, once the player has worked out everything it's made of: each
// element's icon in its colour and how many times it's listed ("Water 1, Creature 1, Protection 1" for blue wool).
// A TooltipComponent is the data; Client/ClientElementTooltip draws it (registered in DustClient).
public record ElementTooltip(List<Entry> entries) implements TooltipComponent
{
    public record Entry(DustElement element, int count) {}

    // The tooltip for this item, or null if this player hasn't fully worked it out: they've researched it, found every
    // element it's made of (on the Elemental Examiner, or in compounds a machine broke out of it), and discovered each
    // of those elements (an undiscovered one stays hidden)
    public static @Nullable ElementTooltip forPlayer(ItemStack stack, Player player)
    {
        // Compounds and Alchemical Dust say what's in them in their own tooltip line
        if (Compounds.of(stack) != null || AlchemicalDust.is(stack)) return null;
        DustComposition composition = DustCompositions.of(stack);
        // A product (crushed pebbles) is researched as what it was made from, like everywhere else
        ResearchEntry entry = ResearchTabs.entryForItem(ProductSource.original(stack));
        if (composition == null || entry == null) return null;
        EntryResearch research = Research.get(player).get(entry);
        if (research == null) return null;
        Map<DustElement, Integer> measured = research.dust().measured();
        List<Entry> entries = new ArrayList<>();
        for (Map.Entry<DustElement, Integer> element : composition.counted().entrySet()) {
            if (measured.getOrDefault(element.getKey(), 0) <= 0) return null;
            if (!Research.get(player).has(new ResearchEntry(ResearchTabs.ELEMENTS, element.getKey().id()))) return null;
            entries.add(new Entry(element.getKey(), element.getValue()));
        }
        return new ElementTooltip(entries);
    }
}
