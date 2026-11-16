package com.mowmaster.dust.Features.Machines;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.ArrayList;
import java.util.List;

// A research station's own slots of one role, as NeoForge's item capability (hoppers and pipes use it directly, so
// nothing is hidden from the player: an output stays on its pad until a hopper takes it):
//   INPUT, SUPPLY   can be put into, nothing taken out
//   OUTPUT          can be taken from, nothing put in
// Index i is the station's i-th slot of that role (StationBlockEntity.slots(), in order).
//
// Transactions: a hopper asks "how much would go in" inside a transaction that may be rolled back (cancelled). The
// station's slots change straight away when used (and sync to clients), so nothing is changed during the transaction:
// each insert / extract is written down as pending, the answers count what's already pending, and only when the
// outermost transaction commits (onRootCommit) are they done for real through the slots' inserter / extractor. A
// SnapshotJournal does the bookkeeping: it saves a copy of the pending list whenever a (nested) transaction first
// touches it, and puts that copy back if the transaction is cancelled.
public class StationSlotHandler implements ResourceHandler<ItemResource>
{
    private final StationBlockEntity station;
    private final StationSlot.Role role;

    // One pending change: insert (or take out) amount of resource at the role's index-th slot
    private record Pending(int index, boolean insert, ItemResource resource, int amount) {}

    private final Journal journal = new Journal();

    public StationSlotHandler(StationBlockEntity station, StationSlot.Role role)
    {
        this.station = station;
        this.role = role;
    }

    private List<StationSlot> slots()
    {
        return station.slots().stream().filter(slot -> slot.role() == role).toList();
    }

    private StationSlot slot(int index)
    {
        return slots().get(index);
    }

    // What the slot holds once the pending changes are counted (a copy)
    private ItemStack view(int index)
    {
        ItemStack stack = slot(index).contents().get().copy();
        for (Pending change : journal.pending) {
            if (change.index() != index) continue;
            if (change.insert()) {
                if (stack.isEmpty()) stack = change.resource().toStack(change.amount());
                else stack.grow(change.amount());
            } else {
                stack.shrink(change.amount());
            }
        }
        return stack;
    }

    @Override
    public int size()
    {
        return slots().size();
    }

    @Override
    public ItemResource getResource(int index)
    {
        ItemStack stack = view(index);
        return stack.isEmpty() ? ItemResource.EMPTY : ItemResource.of(stack);
    }

    @Override
    public long getAmountAsLong(int index)
    {
        return view(index).getCount();
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource)
    {
        return slot(index).limit();
    }

    @Override
    public boolean isValid(int index, ItemResource resource)
    {
        return role != StationSlot.Role.OUTPUT && !resource.isEmpty() && slot(index).accepts().test(resource.toStack());
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction)
    {
        if (amount <= 0 || index < 0 || index >= size() || !isValid(index, resource)) return 0;
        ItemStack held = view(index);
        if (!held.isEmpty() && !resource.matches(held)) return 0;
        int room = Math.min(slot(index).limit(), resource.toStack().getMaxStackSize()) - held.getCount();
        int moved = Math.min(room, amount);
        if (moved <= 0) return 0;
        // updateSnapshots: saves the pending list for this transaction before it's first changed in it
        journal.updateSnapshots(transaction);
        journal.pending.add(new Pending(index, true, resource, moved));
        return moved;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction)
    {
        if (role != StationSlot.Role.OUTPUT || amount <= 0 || index < 0 || index >= size() || resource.isEmpty()) return 0;
        ItemStack held = view(index);
        if (held.isEmpty() || !resource.matches(held)) return 0;
        int moved = Math.min(amount, held.getCount());
        journal.updateSnapshots(transaction);
        journal.pending.add(new Pending(index, false, resource, moved));
        return moved;
    }

    private final class Journal extends SnapshotJournal<List<Pending>>
    {
        private List<Pending> pending = new ArrayList<>();

        @Override
        protected List<Pending> createSnapshot()
        {
            return new ArrayList<>(pending);
        }

        @Override
        protected void revertToSnapshot(List<Pending> snapshot)
        {
            pending = snapshot;
        }

        // The outermost transaction committed: now do it all for real (no player: automation)
        @Override
        protected void onRootCommit(List<Pending> originalState)
        {
            List<Pending> done = pending;
            pending = new ArrayList<>();
            for (Pending change : done) {
                StationSlot slot = slot(change.index());
                if (change.insert()) slot.inserter().insert(change.resource().toStack(change.amount()), null);
                else slot.extractor().extract(change.amount());
            }
        }
    }
}
