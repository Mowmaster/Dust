package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import com.mowmaster.mowlib.display.ISpinningItemHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

// The Dust Testing Disk's one item, shown turning slowly on top with a beam for each dust it holds
// (Client/TestingDiskRenderer). CBSyncedBlockEntity (MowLib) sends what's saved to the clients on sync().
public class TestingDiskBlockEntity extends CBSyncedBlockEntity implements ISpinningItemHolder
{
    private ItemStack item = ItemStack.EMPTY;

    public TestingDiskBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.TESTING_DISK.get(), pos, state);
    }

    public ItemStack getItem()
    {
        return item;
    }

    public void setItem(ItemStack stack)
    {
        item = stack;
        sync();
    }

    // The composition of what's on the disk (null if it's empty or has no readable dust)
    public @Nullable DustComposition composition()
    {
        return DustCompositions.of(item);
    }

    // ---- ISpinningItemHolder ----

    @Override
    public ItemStack getDisplayedItem()
    {
        return item;
    }

    @Override
    public double getDisplayHeight()
    {
        return 1.25;
    }

    @Override
    public float getDisplayScale()
    {
        return 0.75F;
    }

    @Override
    public float getSpinSpeed()
    {
        return 1.0F;
    }

    // ---- saving ----
    // ValueOutput / ValueInput: 26.x's save interface (in place of CompoundTag). ItemStack.OPTIONAL_CODEC allows an
    // empty stack, which the plain codec refuses.

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (!item.isEmpty()) output.store("item", ItemStack.OPTIONAL_CODEC, item);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        item = input.read("item", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
    }

    // The block is being broken or replaced: drop the item (26.x calls this on the block entity before removing it)
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level != null && !item.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, item);
    }
}
