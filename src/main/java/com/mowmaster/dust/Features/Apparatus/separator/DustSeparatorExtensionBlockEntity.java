package com.mowmaster.dust.Features.Apparatus.separator;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.Machines.StationBlock;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

// A Dust Separator Extension's drum section (filled and emptied by the separator it belongs to). Its dust is lost if
// it's broken: what's in a drum is still being sorted.
public class DustSeparatorExtensionBlockEntity extends CBSyncedBlockEntity implements SectionHolder
{
    private final DrumSection section = new DrumSection();

    public DustSeparatorExtensionBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.DUST_SEPARATOR_EXTENSION.get(), pos, state);
    }

    @Override public DrumSection section() { return section; }
    @Override public void sectionChanged() { sync(); }

    // The separator this extension's drum belongs to (back along the drum toward its intake), or null
    public @Nullable DustSeparatorBlockEntity separator(BlockGetter level)
    {
        Direction facing = getBlockState().getValue(DustSeparatorExtensionBlock.FACING);
        Direction back = facing.getCounterClockWise();
        BlockPos at = worldPosition.relative(back);
        for (int i = 0; i < DustSeparatorBlock.MAX_EXTENSIONS; i++) {
            BlockState state = level.getBlockState(at);
            if (state.getBlock() instanceof DustSeparatorBlock && state.getValue(StationBlock.FACING) == facing) {
                return level.getBlockEntity(at) instanceof DustSeparatorBlockEntity separator ? separator : null;
            }
            if (!(state.getBlock() instanceof DustSeparatorExtensionBlock) || state.getValue(DustSeparatorExtensionBlock.FACING) != facing) return null;
            at = at.relative(back);
        }
        return null;
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        section.save(output);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        section.load(input);
    }
}
