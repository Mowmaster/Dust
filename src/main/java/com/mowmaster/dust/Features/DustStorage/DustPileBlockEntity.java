package com.mowmaster.dust.Features.DustStorage;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.LinkedHashMap;
import java.util.Map;

// What a Dust Pile holds: so much of each element (any mix), up to CAPACITY in all (a full block's height). Its height
// grows with it, a layer for every eighth of CAPACITY (started layers count); its colour the mix of its elements, weighed by how much of each (Client/DustPileTint). Sent to
// clients (they tint it and work out the cloud from it: DustClouds, Client/DustCloudClient).
//   server  every CHECK_TICKS (each pile at its own moment), out in the open: a flame, or a creeper, in its cloud where
//           the cloud is thick enough sets the dust off (DustClouds.checkIgnition)
//   client  out in the open: motes of its colour drift about in its cloud, more for a bigger pile
public class DustPileBlockEntity extends CBSyncedBlockEntity
{
    public static final int CAPACITY = 100;
    private static final int CHECK_TICKS = 10;
    private static final Codec<Map<DustElement, Integer>> HELD_CODEC = Codec.unboundedMap(DustElements.CODEC, Codec.INT);

    private final Map<DustElement, Integer> held = new LinkedHashMap<>();
    private int total;
    private int color = 0xFFFFFF;

    public DustPileBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.DUST_PILE.get(), pos, state);
    }

    // What a pile saved (its "held"): for a falling pile's blockData
    public static Map<DustElement, Integer> readHeld(ServerLevel level, net.minecraft.nbt.CompoundTag saved)
    {
        net.minecraft.nbt.Tag tag = saved.get("held");
        if (tag == null) return Map.of();
        return HELD_CODEC.parse(level.registryAccess().createSerializationContext(net.minecraft.nbt.NbtOps.INSTANCE), tag).result().orElse(Map.of());
    }

    public int total() { return total; }
    public int color() { return color; }
    public Map<DustElement, Integer> held() { return held; }

    // Adds up to amount of this element (as much as fits); returns how much went in. The pile grows to match.
    public int add(DustElement element, int amount)
    {
        int moved = Math.min(amount, CAPACITY - total);
        if (moved <= 0) return 0;
        held.merge(element, moved, Integer::sum);
        recount();
        if (level != null) {
            int layers = layers(total);
            BlockState state = getBlockState();
            if (state.getValue(DustPileBlock.LAYERS) != layers) level.setBlock(worldPosition, state.setValue(DustPileBlock.LAYERS, layers), Block.UPDATE_ALL);
        }
        sync();
        return moved;
    }

    public boolean isFull()
    {
        return total >= CAPACITY;
    }

    public static int layers(int total)
    {
        return Math.clamp((total * 8 + CAPACITY - 1) / CAPACITY, 1, 8);
    }

    // Whether it's out in the open: nothing solid lies on it (air, plants, fire... above), so its dust is in the air
    public boolean isOpen()
    {
        if (level == null) return false;
        BlockPos above = worldPosition.above();
        return level.getBlockState(above).getCollisionShape(level, above).isEmpty();
    }

    private void recount()
    {
        total = 0;
        long red = 0, green = 0, blue = 0;
        for (Map.Entry<DustElement, Integer> entry : held.entrySet()) {
            int amount = entry.getValue(), c = entry.getKey().color();
            total += amount;
            red += (long) ((c >> 16) & 0xFF) * amount;
            green += (long) ((c >> 8) & 0xFF) * amount;
            blue += (long) (c & 0xFF) * amount;
        }
        color = total <= 0 ? 0xFFFFFF : (int) ((red / total) << 16 | (green / total) << 8 | (blue / total));
    }

    public void serverTick(ServerLevel level)
    {
        if ((level.getGameTime() + worldPosition.asLong()) % CHECK_TICKS != 0 || total <= 0 || !isOpen()) return;
        DustClouds.checkIgnition(level, worldPosition);
    }

    // Client: motes drifting in its cloud (Client/DustCloudClient)
    public void clientTick()
    {
        if (total > 0) com.mowmaster.dust.Features.DustStorage.Client.DustCloudClient.pileTick(this);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.store("held", HELD_CODEC, held);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        int before = color;
        held.clear();
        input.read("held", HELD_CODEC).ifPresent(held::putAll);
        recount();
        // The colour changed: draw it again (on the client the block state may have stayed the same)
        if (level != null && level.isClientSide() && before != color) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
}
