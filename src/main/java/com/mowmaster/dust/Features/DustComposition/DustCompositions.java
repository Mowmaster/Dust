package com.mowmaster.dust.Features.DustComposition;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.Machines.ProductSource;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

import static com.mowmaster.dust.Features.DustComposition.DustElements.*;

// What things are made of, for burning (Magical Flint and Steel), measuring (Elemental Examiner) and dust recipes.
//   Items     the dust:elements item data map (data/<namespace>/data_maps/item/elements.json), so datapacks and other
//             mods can add or change items. Dust's own covers every vanilla item.
//   Rules     for items whose dust depends on their data (a water bottle is a potion item holding water); checked first
//   Elements  an element's crystal or dust is that element alone
//   Compounds an alchemical compound is the elements separated into it
//   Products  a process product (crushed stone) is made of what it came from
//   Fluids    for fluids burned where they lie (lava)
// "import static ...DustElements.*" lets the code below write WATER instead of DustElements.WATER.
public final class DustCompositions
{
    private static final List<StackRule> STACK_RULES = new CopyOnWriteArrayList<>();
    private static final Map<Fluid, DustComposition> FLUIDS = new ConcurrentHashMap<>();

    // A rule checked before the data map: if test matches the stack, it has this composition
    private record StackRule(Predicate<ItemStack> test, DustComposition composition) {}

    static {
        // A water bottle is pure water (other potions are the potion item's entry in the data map)
        registerRule(DustCompositions::isWaterBottle, DustComposition.of(WATER));
        registerFluid(Fluids.LAVA, DustComposition.of(FIRE, EXPLOSION));
        registerFluid(Fluids.WATER, DustComposition.of(WATER));
    }

    private DustCompositions() {}

    public static void registerRule(Predicate<ItemStack> test, DustComposition composition)
    {
        STACK_RULES.add(new StackRule(test, composition));
    }

    public static void registerFluid(Fluid fluid, DustComposition composition)
    {
        FLUIDS.put(fluid, composition);
    }

    // The item's dust, or null if it has none that can be read
    public static @Nullable DustComposition of(ItemStack stack)
    {
        if (stack.isEmpty()) return null;
        for (StackRule rule : STACK_RULES) {
            if (rule.test().test(stack)) return rule.composition();
        }
        // Alchemical Dust is its one element
        DustElement alchemical = stack.get(DustComponentDataRegistry.ALCHEMICAL_ELEMENT.get());
        if (alchemical != null) return DustComposition.of(alchemical);
        // An alchemical compound is exactly the elements separated into it
        DustComposition compound = stack.get(DustComponentDataRegistry.COMPOUND.get());
        if (compound != null) return compound.isEmpty() ? null : compound;
        // A process product (crushed stone) is still what it was made from
        ItemStack original = ProductSource.original(stack);
        if (original != stack) return of(original);
        // An element's crystal or dust is that element and nothing else
        DustElement element = CrystalElements.of(stack);
        if (element == null) element = CrystalElements.ofDust(stack);
        if (element != null) return DustComposition.of(element);
        // typeHolder(): the item's registry holder, which is what data maps are attached to. getData gives null for
        // items the data map doesn't list.
        DustComposition composition = stack.typeHolder().getData(DustDataMaps.ELEMENTS);
        return composition == null || composition.isEmpty() ? null : composition;
    }

    // A fluid's dust (still or flowing), or null
    public static @Nullable DustComposition of(FluidState fluid)
    {
        if (fluid.isEmpty()) return null;
        for (Map.Entry<Fluid, DustComposition> entry : FLUIDS.entrySet()) {
            // isSame: the still and flowing forms of a fluid count as the same fluid
            if (fluid.getType().isSame(entry.getKey())) return entry.getValue();
        }
        return null;
    }

    // A bottle of plain water: a potion item whose potion is minecraft:water
    private static boolean isWaterBottle(ItemStack stack)
    {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        return stack.is(Items.POTION) && contents != null && contents.is(Potions.WATER);
    }
}
