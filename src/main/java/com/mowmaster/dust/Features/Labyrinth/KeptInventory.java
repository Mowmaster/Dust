package com.mowmaster.dust.Features.Labyrinth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// What a player who died in a labyrinth had (every inventory slot, and their experience), handed back when they
// respawn outside (Labyrinth.keep / restore): nothing is dropped in a labyrinth that's about to be rebuilt.
public record KeptInventory(List<ItemStack> items, int level, float progress, int total)
{
    public static final KeptInventory NONE = new KeptInventory(List.of(), 0, 0.0F, 0);

    public static final Codec<KeptInventory> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.OPTIONAL_CODEC.listOf().fieldOf("items").forGetter(KeptInventory::items),
            Codec.INT.fieldOf("level").forGetter(KeptInventory::level),
            Codec.FLOAT.fieldOf("progress").forGetter(KeptInventory::progress),
            Codec.INT.fieldOf("total").forGetter(KeptInventory::total)
    ).apply(instance, KeptInventory::new));

    public boolean isEmpty()
    {
        return items.isEmpty() && level == 0 && total == 0;
    }
}
