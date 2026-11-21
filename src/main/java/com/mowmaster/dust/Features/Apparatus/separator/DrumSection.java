package com.mowmaster.dust.Features.Apparatus.separator;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

// One drum section of a Dust Separator (the separator itself, or an extension): the solid element it's holding and how
// much (up to CAPACITY, on its way down its tubes). Which element it takes is up to the tubes under it
// (DustSeparatorBlockEntity: a network whose jars are set to an element, labelled or not, only takes that one).
public final class DrumSection
{
    public static final int CAPACITY = 100;

    @Nullable DustElement element;
    int amount;

    public @Nullable DustElement element() { return element; }
    public int amount() { return amount; }

    // Whether it can take this element now: it's empty, or holding it already with room
    boolean takes(DustElement dust)
    {
        return (element == null || dust.equals(element)) && amount < CAPACITY;
    }

    int add(DustElement dust, int count)
    {
        if (element == null) element = dust;
        int moved = Math.min(count, CAPACITY - amount);
        amount += moved;
        return moved;
    }

    int take(int count)
    {
        int taken = Math.min(count, amount);
        amount -= taken;
        if (amount <= 0) element = null;
        return taken;
    }

    void save(ValueOutput output)
    {
        if (element != null) output.store("section_element", DustElements.CODEC, element);
        output.putInt("section_amount", amount);
    }

    void load(ValueInput input)
    {
        element = input.read("section_element", DustElements.CODEC).orElse(null);
        amount = input.getIntOr("section_amount", 0);
    }
}
