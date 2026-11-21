package com.mowmaster.dust.Features.Apparatus.still;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Machines.AlchemicalDust;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

// A Dust Chute's filter: the one element it lets out (null: any). Sent to clients for the symbol on it.
public class DustChuteBlockEntity extends CBSyncedBlockEntity
{
    private @Nullable DustElement filter;

    public DustChuteBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.DUST_CHUTE.get(), pos, state);
    }

    public @Nullable DustElement filter() { return filter; }

    // Sets (or with null, clears) the filter; the block switches to its filtered look and back
    public void setFilter(@Nullable DustElement element)
    {
        filter = element;
        if (level != null) {
            BlockState state = getBlockState();
            boolean filtered = element != null;
            if (state.getValue(DustChuteBlock.FILTERED) != filtered) level.setBlock(worldPosition, state.setValue(DustChuteBlock.FILTERED, filtered), Block.UPDATE_ALL);
        }
        sync();
    }

    // The one element an element's crystal, dust or Alchemical Dust is (null: anything else)
    public static @Nullable DustElement elementOf(ItemStack stack)
    {
        DustElement element = AlchemicalDust.of(stack);
        if (element == null) element = CrystalElements.of(stack);
        if (element == null) element = CrystalElements.ofDust(stack);
        return element;
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (filter != null) output.store("filter", DustElements.CODEC, filter);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        DustElement before = filter;
        filter = input.read("filter", DustElements.CODEC).orElse(null);
        if (level != null && level.isClientSide() && !Objects.equals(before, filter)) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
}
