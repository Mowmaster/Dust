package com.mowmaster.dust.Features.Apparatus;

import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.DustComposition.DustCompositions;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Machines.Separation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// What the apparatus (the Still, the Dust Separator...) read an item as: its dust composition (DustCompositions: a
// compound its elements, Alchemical Dust and crystals their element, a process product the material it was made from,
// anything else its data or recipe), every element whole: industry doesn't break elements down into their parts the
// way research does. Blocks go in as they are (only the research stations need them crushed first).
public final class ApparatusElements
{
    private ApparatusElements() {}

    // The elements an item holds (repeats kept: 9 Trees in a compound is 9 Trees)
    public static List<DustElement> elementsOf(ItemStack stack)
    {
        DustComposition composition = DustCompositions.of(stack);
        return composition == null ? List.of() : composition.elements();
    }

    public static List<DustElement> solids(List<DustElement> elements)
    {
        return elements.stream().filter(Separation::isSolid).toList();
    }

    // Gaseous and aqueous
    public static List<DustElement> fluids(List<DustElement> elements)
    {
        return elements.stream().filter(element -> !Separation.isSolid(element)).toList();
    }
}
