package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.Machines.IStationSlots;
import com.mowmaster.dust.Features.Machines.StationSlot;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import com.mowmaster.mowlib.util.CBPixelBox;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
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
public class ElementTesterBlockEntity extends CBSyncedBlockEntity implements IStationSlots
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
        crystalsChanged();
    }

    // ---- clicking (StationInteractions) ----
    //   the plate              right-click with an item to examine it (once there's a crystal); left-click takes it
    //   each crystal           left-click takes that crystal off
    //   anywhere on the top    right-click with a crystal to set it on (up to five, one of each element)
    private static final CBPixelBox TABLE_TOP = new CBPixelBox(0, 10.5, 0, 16, 16.5, 16);

    @Override
    public List<StationSlot> slots()
    {
        List<StationSlot> slots = new ArrayList<>();
        ExaminerLayout layout = ExaminerLayout.of(crystals.size());
        if (layout != null) {
            Vec3 plate = layout.plate();
            StationSlot.Inserter examine = (stack, player) -> {
                if (!item.isEmpty()) return 0;
                setItem(stack.copyWithCount(1));
                if (player instanceof ServerPlayer serverPlayer) ElementTesterBlock.report(serverPlayer, this, item);
                return 1;
            };
            slots.add(new StationSlot(new CBPixelBox(plate.x - 2.5, 10.5, plate.z - 2.5, plate.x + 2.5, 13.5, plate.z + 2.5),
                    stack -> item.isEmpty(), examine, () -> item, max -> {
                        ItemStack taken = item;
                        setItem(ItemStack.EMPTY);
                        return taken;
                    }));
            for (int i = 0; i < crystals.size(); i++) {
                final int index = i;
                Vec3 at = layout.crystals()[i];
                slots.add(new StationSlot(new CBPixelBox(at.x - 1.75, 10.5, at.z - 1.75, at.x + 1.75, 15.5, at.z + 1.75),
                        stack -> false, (stack, player) -> 0, () -> CrystalElements.stackOf(crystals.get(index)), max -> removeCrystal(index)));
            }
        }
        StationSlot.Inserter install = (stack, player) -> {
            DustElement crystal = CrystalElements.of(stack);
            if (crystal == null || crystals.size() >= ExaminerLayout.MAX_CRYSTALS || crystals.contains(crystal)) return 0;
            addCrystal(crystal);
            return 1;
        };
        slots.add(new StationSlot(TABLE_TOP, stack -> CrystalElements.of(stack) != null, install, () -> ItemStack.EMPTY, max -> ItemStack.EMPTY));
        return slots;
    }

    // Takes a crystal off (the ones after it move up); returns it as an item
    public ItemStack removeCrystal(int index)
    {
        if (index < 0 || index >= crystals.size()) return ItemStack.EMPTY;
        DustElement removed = crystals.remove(index);
        crystalsChanged();
        return CrystalElements.stackOf(removed);
    }

    // The block's model follows how many crystals there are (ElementTesterBlock.CRYSTALS)
    private void crystalsChanged()
    {
        sync();
        if (level != null && !level.isClientSide()) {
            level.setBlock(worldPosition, getBlockState().setValue(ElementTesterBlock.CRYSTALS, crystals.size()), Block.UPDATE_ALL);
            level.playSound(null, worldPosition, SoundEvents.AMETHYST_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
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
