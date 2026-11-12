package com.mowmaster.dust.Features.Machines;

import com.mowmaster.mowlib.util.CBPixelBox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

// One clickable place on a research station (StationInteractions): an area of the model, what may be put there, and
// how things go in and come out.
//   area       where it is on the model, in pixels with the model facing north (turned to the block's facing for clicks)
//   accepts    whether the held stack can go in (right-click)
//   inserter   puts some of the held stack in; returns how many it took (the stack itself isn't changed)
//   contents   what's there now (left-click takes from it, when not empty)
//   extractor  takes up to this many out
public record StationSlot(CBPixelBox area, Predicate<ItemStack> accepts, Inserter inserter, Supplier<ItemStack> contents, Extractor extractor)
{
    @FunctionalInterface
    public interface Inserter
    {
        // player: who's putting it in (null for automation)
        int insert(ItemStack stack, @Nullable Player player);
    }

    @FunctionalInterface
    public interface Extractor
    {
        ItemStack extract(int max);
    }

    // A slot holding one ItemStack (get / set): takes up to limit of an accepted item (only the same item if not
    // empty), gives back what's there. changed runs after anything goes in or out (to save and sync).
    public static StationSlot ofStack(CBPixelBox area, Predicate<ItemStack> accepts, int limit,
                                      Supplier<ItemStack> get, Consumer<ItemStack> set, Runnable changed)
    {
        Inserter inserter = (stack, player) -> {
            ItemStack held = get.get();
            if (!held.isEmpty() && !ItemStack.isSameItemSameComponents(held, stack)) return 0;
            int room = Math.min(limit, stack.getMaxStackSize()) - held.getCount();
            int moved = Math.min(room, stack.getCount());
            if (moved <= 0) return 0;
            if (held.isEmpty()) set.accept(stack.copyWithCount(moved));
            else held.grow(moved);
            changed.run();
            return moved;
        };
        Extractor extractor = max -> {
            ItemStack held = get.get();
            if (held.isEmpty()) return ItemStack.EMPTY;
            ItemStack taken = held.split(Math.min(max, held.getCount()));
            if (held.isEmpty()) set.accept(ItemStack.EMPTY);
            changed.run();
            return taken;
        };
        return new StationSlot(area, accepts, inserter, get, extractor);
    }

    // A slot that only gives things out (an output)
    public static StationSlot output(CBPixelBox area, Supplier<ItemStack> get, Consumer<ItemStack> set, Runnable changed)
    {
        return ofStack(area, stack -> false, 0, get, set, changed);
    }
}
