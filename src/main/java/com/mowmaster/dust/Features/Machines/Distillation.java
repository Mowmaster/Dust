package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.DustComposition.DustCompositions;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

// The rules of distillation (the Distillation Table):
//   - Something with at least two elements, one of them gaseous or aqueous.
//   - One gaseous or aqueous element, picked at random, boils over into the jar; everything else stays in the flask.
//   - Each comes out as a compound of its kind (Compounds.Kind: gas if anything in it is gaseous, else liquid if
//     anything is aqueous, else solid), if its container is on the table (a glass bottle, a bowl, paper; one used).
//   - Without its container, the gaseous and aqueous parts escape (hiss / bubble away), and what's left of the solids
//     remains as with no paper: crystalline as a compound, anything amorphous burned to soot; nothing if there are none.
public final class Distillation
{
    private Distillation() {}

    public static List<DustElement> elements(ItemStack stack)
    {
        DustComposition composition = DustCompositions.of(stack);
        return composition == null ? List.of() : composition.elements();
    }

    public static boolean canDistill(ItemStack stack)
    {
        List<DustElement> elements = elements(stack);
        return elements.size() >= 2 && elements.stream().anyMatch(element -> !Separation.isSolid(element));
    }

    // The two parts: what boils over (one element) and what stays in the flask
    public record Split(List<DustElement> jar, List<DustElement> flask) {}

    public static Split split(ItemStack stack, RandomSource random)
    {
        List<DustElement> rest = new ArrayList<>(elements(stack));
        List<Integer> volatileAt = new ArrayList<>();
        for (int i = 0; i < rest.size(); i++) {
            if (!Separation.isSolid(rest.get(i))) volatileAt.add(i);
        }
        int picked = volatileAt.get(random.nextInt(volatileAt.size()));
        DustElement over = rest.remove(picked);
        return new Split(List.of(over), List.copyOf(rest));
    }

    // What one part becomes. hasContainer: whether its kind's container is on the table (it's used if so).
    //   lost: the gaseous / aqueous elements that escaped (for the particles)
    public record Output(ItemStack stack, boolean usesContainer, List<DustElement> lost, boolean burned) {}

    public static Output output(List<DustElement> elements, ItemStack original, Predicate<Item> hasContainer)
    {
        if (elements.isEmpty()) return new Output(ItemStack.EMPTY, false, List.of(), false);
        Compounds.Kind kind = Compounds.Kind.of(elements);
        if (hasContainer.test(kind.container())) return new Output(Compounds.create(elements, original, kind), true, List.of(), false);
        List<DustElement> lost = elements.stream().filter(element -> !Separation.isSolid(element)).toList();
        List<DustElement> solids = elements.stream().filter(Separation::isSolid).toList();
        if (solids.isEmpty()) return new Output(ItemStack.EMPTY, false, lost, false);
        // The solids as with no paper: crystalline remains, anything amorphous burns
        Separation.Output remains = Separation.output(solids, original, 0);
        return new Output(remains.stack(), false, lost, remains.burned());
    }
}
