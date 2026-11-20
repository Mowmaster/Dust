package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import com.mowmaster.mowlib.keys.IPickupableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

// A Dust Jar's dust: up to DustJarContents.CAPACITY of one element. The first dust in sets the element (the lid takes
// its colour, Client/DustJarTint); an empty jar keeps it until it's cleared (DustJarBlock: sneak + right-click). A label
// only goes on a jar with an element, and locks it to that element: it can't be cleared until the labels are off.
// What's saved is also what the client gets (CBSyncedBlockEntity), for the lid colour.
// IPickupableBlock: MowLib's pick-up key (and the jar's own sneak-click) take it as an item, dust and all.
// IDustHolder: machines (the Dust Grinder, the alchemical apparatus through Glass Dust Tubes) pour dust in and draw it
// out (DustNetwork).
public class DustJarBlockEntity extends CBSyncedBlockEntity implements IPickupableBlock, IDustHolder
{
    private @Nullable DustElement element;
    private int amount;
    // The sides with a Jar Label (only the four around: north, east, south, west)
    private final java.util.EnumSet<net.minecraft.core.Direction> labels = java.util.EnumSet.noneOf(net.minecraft.core.Direction.class);
    private boolean pickedUp;

    public DustJarBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.DUST_JAR.get(), pos, state);
    }

    public @Nullable DustElement element() { return element; }
    public int amount() { return amount; }
    public int room() { return DustJarContents.CAPACITY - amount; }

    public DustJarContents contents()
    {
        return new DustJarContents(Optional.ofNullable(element), amount, List.copyOf(labels));
    }

    // ---- labels ----

    public java.util.Set<net.minecraft.core.Direction> labels()
    {
        return java.util.Collections.unmodifiableSet(labels);
    }

    // Sticks a label on this side; false if it isn't one of the four around, already has one, or the jar has no element
    // to label (a labelled jar is locked to its element: clearElement)
    public boolean addLabel(net.minecraft.core.Direction side)
    {
        if (element == null || !side.getAxis().isHorizontal() || !labels.add(side)) return false;
        changed();
        return true;
    }

    // Scrapes this side's label off; false if there wasn't one
    public boolean removeLabel(net.minecraft.core.Direction side)
    {
        if (!labels.remove(side)) return false;
        changed();
        return true;
    }

    // Whether this element's dust can go in: the jar has no element yet, or the same one, and has room
    public boolean accepts(DustElement dust)
    {
        return (element == null || element == dust) && amount < DustJarContents.CAPACITY;
    }

    @Override
    public int insertDust(DustElement dust, int count, boolean simulate)
    {
        if (count <= 0 || !accepts(dust)) return 0;
        int moved = Math.min(count, room());
        if (!simulate) {
            element = dust;
            amount += moved;
            changed();
        }
        return moved;
    }

    // ---- dust holder (IDustHolder: tubes and the apparatus) ----

    @Override
    public int dustAmount(DustElement dust)
    {
        return dust.equals(element) ? amount : 0;
    }

    @Override
    public int extractDust(DustElement dust, int count, boolean simulate)
    {
        if (!dust.equals(element)) return 0;
        int taken = Math.min(count, amount);
        if (!simulate && taken > 0) {
            amount -= taken;
            changed();
        }
        return taken;
    }

    // Set to this element: it keeps its element while empty, so tubes fill it before an empty, unset jar
    @Override
    public boolean prefersDust(DustElement dust)
    {
        return dust.equals(element);
    }

    // Takes up to count out (the element stays, so the jar keeps its colour when it runs dry); returns how much
    public int extract(int count)
    {
        int taken = Math.min(count, amount);
        if (taken > 0) {
            amount -= taken;
            changed();
        }
        return taken;
    }

    // Clears an empty jar's element (the lid goes plain again); false if it has dust in it, no element anyway, or a
    // label (locked to its element until every label is scraped off)
    public boolean clearElement()
    {
        if (amount > 0 || element == null || !labels.isEmpty()) return false;
        element = null;
        changed();
        return true;
    }

    private void changed()
    {
        setChanged();
        sync();
    }

    // ---- picking up (MowLib's IPickupableBlock) ----

    @Override
    public ItemStack toItem()
    {
        ItemStack jar = new ItemStack(DustBlockRegistry.DUST_JAR.get());
        if (!contents().isEmpty()) jar.set(DustComponentDataRegistry.DUST_JAR.get(), contents());
        return jar;
    }

    @Override
    public void markPickedUp()
    {
        pickedUp = true;
    }

    public boolean isPickedUp()
    {
        return pickedUp;
    }

    // ---- the item's data component ----
    // collectImplicitComponents: what the block's item gets when it's made from this block entity (the loot table's
    // copy_components, the pick-block). applyImplicitComponents: read back when the item is placed.

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components)
    {
        super.collectImplicitComponents(components);
        if (!contents().isEmpty()) components.set(DustComponentDataRegistry.DUST_JAR.get(), contents());
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components)
    {
        super.applyImplicitComponents(components);
        DustJarContents held = components.getOrDefault(DustComponentDataRegistry.DUST_JAR.get(), DustJarContents.EMPTY);
        element = held.element().orElse(null);
        amount = held.amount();
        labels.clear();
        labels.addAll(held.labels());
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.store("contents", DustJarContents.CODEC, contents());
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        DustElement before = element;
        DustJarContents saved = input.read("contents", DustJarContents.CODEC).orElse(DustJarContents.EMPTY);
        element = saved.element().orElse(null);
        amount = saved.amount();
        labels.clear();
        labels.addAll(saved.labels());
        // On the client the block state didn't change, so ask for the block to be drawn again when the lid's colour has
        if (level != null && level.isClientSide() && !Objects.equals(before, element)) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }
}
