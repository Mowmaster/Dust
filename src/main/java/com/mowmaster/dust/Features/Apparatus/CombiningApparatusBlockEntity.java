package com.mowmaster.dust.Features.Apparatus;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Machines.ElementCombining;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// The apparatus that put two elements' dust together into the element mixed from them (the Conjoining Crucible: two
// solids; the Mixing Vat: anything gaseous or aqueous in the pair, as at the Combination and Mixing Tables). The
// pattern is the element to make; each piece of work draws one of each of its two parts from the Dust Jars on the
// tube network (air: a fire and a water) and makes one of it, which goes back to the jars.
public abstract class CombiningApparatusBlockEntity extends DustApparatusBlockEntity
{
    protected CombiningApparatusBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    // Whether this apparatus puts these two together (true: both solid, the crucible; false: the vat)
    protected abstract boolean takesSolids();

    // The lang key suffix for a pattern the other apparatus makes
    protected abstract String otherApparatus();

    @Override
    protected @Nullable String patternProblem(ItemStack stack, DustElement element)
    {
        List<DustElement> parts = element.parts();
        if (parts.size() != 2) return "primal";
        if (ElementCombining.combinedBySolids(parts.get(0), parts.get(1)) != takesSolids()) return otherApparatus();
        return null;
    }

    @Override
    protected Map<DustElement, Integer> needs()
    {
        DustElement element = patternElement();
        if (element == null || element.parts().size() != 2) return Map.of();
        Map<DustElement, Integer> needs = new LinkedHashMap<>();
        for (DustElement part : element.parts()) needs.merge(part, 1, Integer::sum);
        return needs;
    }

    @Override
    protected boolean canStart()
    {
        return dustRoom() >= 1;
    }

    @Override
    protected void make(ServerLevel level)
    {
        DustElement element = patternElement();
        if (element != null) catchDust(level, List.of(element));
    }
}
