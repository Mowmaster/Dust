package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.DustComposition.DustCompositions;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.mowlib.util.CBPixelBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

// What the research machines after the crusher (the Separation, Evaporation and Distillation Tables) take, and what
// they see in it:
//   - Items only: a block has to go through the Research Crusher first (its pebbles are an item and go in fine).
//   - Elements are taken out by kind: all of one element at once (a compound keeps how many there were).
//   - An item or pebbles holding a single element (mixed or primal) is first turned into that element's compound
//     (isSingleElement): by the Separation or Evaporation Table if the element is solid, by the Distillation Table if
//     it's gaseous or aqueous. (So oak planks, which is Trees, gives a Trees compound: its own element.)
//   - A compound (or Alchemical Dust) of a single mixed element is broken back into the elements it's mixed from
//     (trees: plants and earth), one step each time it goes through a machine, down to the primals. A compound of one
//     primal can't be broken down any further.
public final class ResearchInput
{
    private ResearchInput() {}

    public static boolean accepts(ItemStack stack)
    {
        return !(stack.getItem() instanceof BlockItem);
    }

    // The elements the machines work with (repeats kept), or an empty list if it has no dust
    public static List<DustElement> elements(ItemStack stack)
    {
        DustComposition composition = DustCompositions.of(stack);
        if (composition == null) return List.of();
        List<DustElement> elements = composition.elements();
        // A compound of one mixed element (any count) breaks into what it's mixed from, as many of each: 9 Trees is 9
        // Plants and 9 Earth
        var counted = composition.counted();
        if (isElemental(stack) && counted.size() == 1 && !elements.getFirst().isPrimal()) {
            int count = elements.size();
            List<DustElement> parts = new java.util.ArrayList<>();
            for (DustElement part : elements.getFirst().parts()) {
                for (int i = 0; i < count; i++) parts.add(part);
            }
            return parts;
        }
        return elements;
    }

    // An item or pebbles (not a compound or Alchemical Dust) holding a single element: the machines turn it into that
    // element's compound
    public static boolean isSingleElement(ItemStack stack)
    {
        if (isElemental(stack)) return false;
        DustComposition composition = DustCompositions.of(stack);
        // counted(): each element once, so 2 Stone is still a single element
        return composition != null && composition.counted().size() == 1;
    }

    // Already elements rather than a material: a compound or Alchemical Dust
    private static boolean isElemental(ItemStack stack)
    {
        return stack.has(DustComponentDataRegistry.COMPOUND.get()) || AlchemicalDust.is(stack);
    }

    // Right-clicking a machine's input with a block: tells the player to crush it first. True if it was a block there.
    public static boolean refuseBlock(ItemStack stack, Level level, BlockPos pos, Player player, BlockHitResult hit, CBPixelBox inputArea)
    {
        if (accepts(stack) || !StationInteractions.inArea(level, pos, hit, inputArea)) return false;
        if (!level.isClientSide()) player.sendOverlayMessage(Component.translatable("dust.research_input.block"));
        return true;
    }
}
