package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

// The rules of distillation (the Distillation Table):
//   - Something with at least two elements, one of them gaseous or aqueous.
//   - One gaseous or aqueous element, picked at random, boils over into the jar; everything else stays in the flask.
//   - The gaseous and aqueous parts come out as a compound of their kind (Compounds.Kind: gas if anything in it is
//     gaseous, else liquid), if its container is on the table (a glass bottle or a bowl; one used). Without it, they
//     escape (hiss / bubble away).
//   - Any solid elements left in the flask always burn to alchemical soot (getting solids out whole is the Evaporation
//     and Separation Tables' job). The soot needs paper to catch it (one used); without it, it blows away.
public final class Distillation
{
    private Distillation() {}

    // As ResearchInput reads it (a single-element compound: the elements it's mixed from)
    public static List<DustElement> elements(ItemStack stack)
    {
        return ResearchInput.elements(stack);
    }

    // Two or more elements with one gaseous or aqueous, or a single gaseous / aqueous primal (an item of it, which
    // all boils over into the jar and comes out as its compound: ResearchInput.isPrimal)
    public static boolean canDistill(ItemStack stack)
    {
        List<DustElement> elements = elements(stack);
        boolean anyFluid = elements.stream().anyMatch(element -> !Separation.isSolid(element));
        return anyFluid && (elements.size() >= 2 || ResearchInput.isPrimal(stack));
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

    // What one part becomes:
    //   compound        the gaseous / aqueous elements as a gas or liquid compound (empty: none, or no container)
    //   usesContainer   its container gets used up
    //   lost            the gaseous / aqueous elements that escaped for want of a container (for the particles)
    //   soot            alchemical soot if there were any solid elements and paper to catch it (empty if not)
    //   usesPaper       the paper gets used up (the soot was caught)
    //   sootBlown       there were solids, but no paper: the soot blew away (for the particles)
    public record Output(ItemStack compound, boolean usesContainer, List<DustElement> lost, ItemStack soot, boolean usesPaper, boolean sootBlown) {}

    public static Output output(List<DustElement> elements, ItemStack original, Predicate<Item> hasContainer)
    {
        List<DustElement> fluids = elements.stream().filter(element -> !Separation.isSolid(element)).toList();
        boolean anySolid = elements.stream().anyMatch(Separation::isSolid);
        boolean caught = anySolid && hasContainer.test(Items.PAPER);
        ItemStack soot = caught ? new ItemStack(DustItemRegistry.ALCHEMICAL_SOOT.get()) : ItemStack.EMPTY;
        boolean blown = anySolid && !caught;
        if (fluids.isEmpty()) return new Output(ItemStack.EMPTY, false, List.of(), soot, caught, blown);
        Compounds.Kind kind = Compounds.Kind.of(fluids);
        if (hasContainer.test(kind.container())) return new Output(Compounds.create(fluids, original, kind), true, List.of(), soot, caught, blown);
        return new Output(ItemStack.EMPTY, false, fluids, soot, caught, blown);
    }
}
