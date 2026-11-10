package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// The Element Tester's crystals (up to five, in the order they were put in) and the item on its plate.
// What's saved is also what clients get (CBSyncedBlockEntity), so the tint and renderer can read it there.
public class ElementTesterBlockEntity extends CBSyncedBlockEntity
{
    private final List<DustElement> crystals = new ArrayList<>();
    private ItemStack item = ItemStack.EMPTY;

    public ElementTesterBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.ELEMENT_TESTER.get(), pos, state);
    }

    public List<DustElement> crystals()
    {
        return List.copyOf(crystals);
    }

    public boolean hasCrystal(DustElement element)
    {
        return crystals.contains(element);
    }

    public void addCrystal(DustElement element)
    {
        crystals.add(element);
        sync();
    }

    // Takes the last crystal out (null if there are none)
    public @Nullable DustElement removeLastCrystal()
    {
        if (crystals.isEmpty()) return null;
        DustElement removed = crystals.removeLast();
        sync();
        return removed;
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

    // How much of the item on the plate is this element, as a whole percent: the share of the elements it's directly
    // made of (blue wool is creature + protection + water, so water is 33%). Only those elements count: wool's water
    // is there, but the water inside its creature and protection isn't. 0 with nothing on the plate.
    public int percent(DustElement element)
    {
        return percentOf(DustCompositions.of(item), element);
    }

    public static int percentOf(@Nullable DustComposition composition, DustElement element)
    {
        if (composition == null || composition.elements().isEmpty()) return 0;
        int count = 0;
        for (DustElement part : composition.elements()) {
            if (part == element) count++;
        }
        return count * 100 / composition.elements().size();
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (!crystals.isEmpty()) output.store("crystals", DustElements.CODEC.listOf(), List.copyOf(crystals));
        if (!item.isEmpty()) output.store("item", ItemStack.OPTIONAL_CODEC, item);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        crystals.clear();
        crystals.addAll(input.read("crystals", DustElements.CODEC.listOf()).orElse(List.of()));
        item = input.read("item", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        // On the client the block state may not have changed (a new item on the plate), so ask for the block to be
        // drawn again: its crystal colours and gauge lines come from here (Client/ElementTesterTint)
        if (level != null && level.isClientSide()) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    // Broken or replaced: drop the item and the crystals (26.x calls this on the block entity before removing it)
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level == null) return;
        if (!item.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, item);
        for (DustElement crystal : crystals) {
            Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, CrystalElements.stackOf(crystal));
        }
    }
}
