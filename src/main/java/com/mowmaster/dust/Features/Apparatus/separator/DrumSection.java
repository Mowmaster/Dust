package com.mowmaster.dust.Features.Apparatus.separator;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

// One drum section of a Dust Separator (the separator itself, or an extension): the one solid element it catches, how
// much it holds (up to CAPACITY, sent down its tubes), and whether a Jar Label under it keeps it to that element
public final class DrumSection
{
    public static final int CAPACITY = 100;

    @Nullable DustElement element;
    int amount;
    boolean labelled;

    public @Nullable DustElement element() { return element; }
    public int amount() { return amount; }
    public boolean labelled() { return labelled; }

    // Whether it catches this element now (it's set to it, with room), or could be set to it (empty, unlabelled)
    boolean holds(DustElement dust)
    {
        return dust.equals(element) && amount < CAPACITY;
    }

    boolean free()
    {
        return element == null && !labelled;
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
        if (amount <= 0 && !labelled) element = null;
        return taken;
    }

    // A label only goes on a section with an element; it then keeps it, even empty
    boolean label()
    {
        if (element == null || labelled) return false;
        labelled = true;
        return true;
    }

    boolean unlabel()
    {
        if (!labelled) return false;
        labelled = false;
        if (amount <= 0) element = null;
        return true;
    }

    void save(ValueOutput output)
    {
        if (element != null) output.store("section_element", DustElements.CODEC, element);
        output.putInt("section_amount", amount);
        output.putBoolean("section_labelled", labelled);
    }

    void load(ValueInput input)
    {
        element = input.read("section_element", DustElements.CODEC).orElse(null);
        amount = input.getIntOr("section_amount", 0);
        labelled = input.getBooleanOr("section_labelled", false);
    }
}
