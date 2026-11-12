package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.ElementPhase;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

// The rules of alchemical separation (the Alchemical Separation Table):
//   - Only solids separate: something must hold at least two solid elements (crystalline or amorphous; counting an
//     element listed twice as two). Gaseous and aqueous elements in it hiss or bubble away.
//   - One solid element, picked at random, goes left; the rest of the solids go right, together.
//   - Each side comes out as:
//       with paper in the table    an alchemical compound (one paper each). Its tooltip names the elements the
//                                  player has discovered, and the material it came from.
//       no paper                   crystalline remains as a compound; with anything amorphous in it, it burns away
//                                  to alchemical soot
public final class Separation
{
    private Separation() {}

    public static boolean isSolid(DustElement element)
    {
        return element.phase() == ElementPhase.CRYSTALLINE || element.phase() == ElementPhase.AMORPHOUS;
    }

    // The solid elements in it (repeats kept; as ResearchInput reads it), or an empty list if it has no dust
    public static List<DustElement> solids(ItemStack stack)
    {
        return ResearchInput.elements(stack).stream().filter(Separation::isSolid).toList();
    }

    // The elements that escape (gaseous and aqueous)
    public static List<DustElement> escaping(ItemStack stack)
    {
        return ResearchInput.elements(stack).stream().filter(element -> !isSolid(element)).toList();
    }

    // Two or more solids, or a single solid primal (an item of it, made into its compound: ResearchInput.isPrimal)
    public static boolean canSeparate(ItemStack stack)
    {
        List<DustElement> solids = solids(stack);
        return solids.size() >= 2 || (ResearchInput.isPrimal(stack) && solids.size() == 1);
    }

    // The two sides of a split: the element picked out, and the rest
    public record Split(List<DustElement> left, List<DustElement> right) {}

    public static Split split(ItemStack stack, RandomSource random)
    {
        List<DustElement> rest = new ArrayList<>(solids(stack));
        DustElement picked = rest.remove(random.nextInt(rest.size()));
        return new Split(List.of(picked), List.copyOf(rest));
    }

    // What one side becomes. paper: how much paper there is (one is used if the result needs it; usesPaper says).
    public record Output(ItemStack stack, boolean usesPaper, boolean burned) {}

    public static Output output(List<DustElement> elements, ItemStack original, int paper)
    {
        // Nothing on this side (a primal made into its compound goes all to the left)
        if (elements.isEmpty()) return new Output(ItemStack.EMPTY, false, false);
        if (paper > 0) return new Output(Compounds.create(elements, original), true, false);
        boolean amorphous = elements.stream().anyMatch(element -> element.phase() == ElementPhase.AMORPHOUS);
        if (!amorphous) return new Output(Compounds.create(elements, original), false, false);
        return new Output(new ItemStack(DustItemRegistry.ALCHEMICAL_SOOT.get()), false, true);
    }
}
