package com.mowmaster.dust.Features.Apparatus.still;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.DustComposition.ElementNames;
import com.mowmaster.dust.Features.DustStorage.DustNetwork;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

// A Still Tower's dust: one element (set by the first that rises into it; it keeps it, so its window keeps the colour,
// until it's empty and another element needs a tower), up to CAPACITY. Every PUSH_TICKS it pours what it holds into the
// Dust Jars on its Glass Dust Tube network (DustNetwork.insert). It isn't a store the network draws on: it only gives.
// What's saved is also what clients get (CBSyncedBlockEntity), for the window's colour.
public class StillTowerBlockEntity extends CBSyncedBlockEntity
{
    public static final int CAPACITY = 100;
    public static final int PUSH_TICKS = 10;

    private @Nullable DustElement element;
    private int amount;

    public StillTowerBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.STILL_TOWER.get(), pos, state);
    }

    public @Nullable DustElement element() { return element; }
    public int amount() { return amount; }

    // How much of this element it would take now (0: it's set to another element, or full)
    public int room(DustElement dust)
    {
        if (element != null && !element.equals(dust)) return 0;
        return CAPACITY - amount;
    }

    // Empty: it can be set to another element
    public boolean isEmpty()
    {
        return amount <= 0;
    }

    // Takes up to count of this element (setting the tower to it if it's empty); returns how much went in
    public int add(DustElement dust, int count)
    {
        if (amount <= 0 && !dust.equals(element)) {
            element = dust;
            sync();
        }
        int moved = Math.min(count, room(dust));
        if (moved <= 0) return 0;
        amount += moved;
        setChanged();
        return moved;
    }

    public void serverTick(ServerLevel level)
    {
        if (element == null || amount <= 0 || (level.getGameTime() + worldPosition.asLong()) % PUSH_TICKS != 0) return;
        int poured = DustNetwork.insert(level, worldPosition, element, amount);
        if (poured <= 0) return;
        amount -= poured;
        setChanged();
    }

    // "Still Tower: 40/100 Fire dust"
    public Component status(Player player)
    {
        if (element == null) return Component.translatable("dust.still.tower_empty");
        return Component.translatable("dust.lamp.fuel", getBlockState().getBlock().getName(), amount, CAPACITY, ElementNames.forViewer(element, player));
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (element != null) output.store("element", DustElements.CODEC, element);
        output.putInt("amount", amount);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        DustElement before = element;
        element = input.read("element", DustElements.CODEC).orElse(null);
        amount = input.getIntOr("amount", 0);
        // The window's colour changed: draw the block again (on the client the block state stayed the same)
        if (level != null && level.isClientSide() && !Objects.equals(before, element)) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }
}
