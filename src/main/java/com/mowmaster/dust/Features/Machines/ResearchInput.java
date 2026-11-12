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
//   - An alchemical compound of a single mixed element is broken back into the elements it's mixed from (air: fire
//     and water), one step each time it goes through a machine, down to the primals. A compound of one primal can't
//     be broken down any further.
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
        boolean compound = stack.has(DustComponentDataRegistry.COMPOUND.get());
        if (compound && elements.size() == 1 && !elements.getFirst().isPrimal()) return elements.getFirst().parts();
        return elements;
    }

    // Right-clicking a machine's input with a block: tells the player to crush it first. True if it was a block there.
    public static boolean refuseBlock(ItemStack stack, Level level, BlockPos pos, Player player, BlockHitResult hit, CBPixelBox inputArea)
    {
        if (accepts(stack) || !StationInteractions.inArea(level, pos, hit, inputArea)) return false;
        if (!level.isClientSide()) player.sendOverlayMessage(Component.translatable("dust.research_input.block"));
        return true;
    }
}
