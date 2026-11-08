package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

import static com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement.*;

// Who's made of which dust. Burning something in a Magical Flint and Steel's fire shows its colour; a Dust Testing Disk
// shows the amounts. Things not listed have no dust that can be read (yet).
//
// "import static ...EnumElement.*" lets the table below write FIRE instead of EnumElement.FIRE.
//
// Other code (or other mods, from their constructor) can add more:
//     DustCompositions.register(Items.APPLE, DustComposition.NONE.with(EnumElement.EARTH, 1).with(EnumElement.WATER, 1));
public final class DustCompositions
{
    // By item; and rules for items whose dust depends on their data (a water bottle is a potion item holding water)
    private static final Map<Item, DustComposition> ITEMS = new ConcurrentHashMap<>();
    private static final List<StackRule> STACK_RULES = new CopyOnWriteArrayList<>();
    // Fluids in the world (lava can be burned where it lies). Keyed by the still fluid; flowing lava counts too.
    private static final Map<Fluid, DustComposition> FLUIDS = new ConcurrentHashMap<>();

    // A rule checked before the item table: if test matches the stack, it has this composition
    private record StackRule(Predicate<ItemStack> test, DustComposition composition) {}

    static {
        // The five pure dusts
        register(Items.DIRT, of(EARTH, 1));
        register(Items.FIRE_CHARGE, of(FIRE, 1));
        registerRule(DustCompositions::isWaterBottle, of(WATER, 1));
        register(Items.FLINT, of(ORDER, 1));
        register(Items.SAND, of(CHAOS, 1));

        // Mixes of two
        register(Items.STONE, of(EARTH, 1).with(ORDER, 1));
        register(Items.FEATHER, of(FIRE, 1).with(WATER, 1));
        register(Items.IRON_INGOT, of(FIRE, 1).with(EARTH, 1));
        register(Items.ICE, of(WATER, 1).with(ORDER, 1));
        register(Items.TORCH, of(FIRE, 1).with(ORDER, 1));
        register(Items.REDSTONE, of(WATER, 1).with(CHAOS, 1));
        register(Items.GUNPOWDER, of(EARTH, 1).with(CHAOS, 1));
        registerFluid(Fluids.LAVA, of(FIRE, 1).with(CHAOS, 1));

        // Lava in an iron bucket
        register(Items.LAVA_BUCKET, of(FIRE, 2).with(CHAOS, 1).with(EARTH, 2));
    }

    private DustCompositions() {}

    public static DustComposition of(EnumElement element, int amount)
    {
        return DustComposition.NONE.with(element, amount);
    }

    public static void register(ItemLike item, DustComposition composition)
    {
        ITEMS.put(item.asItem(), composition);
    }

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
        return ITEMS.get(stack.getItem());
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
