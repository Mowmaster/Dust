package com.mowmaster.dust.Features.Spells.voidrift;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustReferences;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Everything the void has swallowed, saved with the world (data/dust_void.dat): the items (merged, with counts that
// can go past a stack), how much experience, and how many of each kind of creature. Nothing comes back out yet; it's
// kept so research can look into it later.
public class VoidStorage extends SavedData
{
    // One kind of item and how many. The stack is kept at count 1 (its components say what kind it is); the count is
    // a long, so it can hold more than a stack ever could.
    public record Entry(ItemStack item, long count)
    {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(i -> i.group(
                ItemStack.CODEC.fieldOf("item").forGetter(Entry::item),
                Codec.LONG.fieldOf("count").forGetter(Entry::count)
        ).apply(i, Entry::new));
    }

    private static final Codec<VoidStorage> CODEC = RecordCodecBuilder.create(i -> i.group(
            Entry.CODEC.listOf().optionalFieldOf("items", List.of()).forGetter(data -> data.items),
            Codec.LONG.optionalFieldOf("experience", 0L).forGetter(data -> data.experience),
            Codec.unboundedMap(Identifier.CODEC, Codec.INT).optionalFieldOf("creatures", Map.of()).forGetter(data -> data.creatures)
    ).apply(i, VoidStorage::new));

    private static final SavedDataType<VoidStorage> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(DustReferences.MODID, "void"), VoidStorage::new, CODEC);

    private final List<Entry> items;
    private long experience;
    private final Map<Identifier, Integer> creatures;

    public VoidStorage()
    {
        this(List.of(), 0L, Map.of());
    }

    private VoidStorage(List<Entry> items, long experience, Map<Identifier, Integer> creatures)
    {
        // Copies: the codec hands over lists and maps that can't be changed
        this.items = new ArrayList<>(items);
        this.experience = experience;
        this.creatures = new HashMap<>(creatures);
    }

    public static VoidStorage get(MinecraftServer server)
    {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    // Adds a stack, merged with the same kind of item (same item, same components) if it's already here
    public void add(ItemStack stack)
    {
        if (stack.isEmpty()) return;
        for (int i = 0; i < items.size(); i++) {
            Entry entry = items.get(i);
            if (ItemStack.isSameItemSameComponents(entry.item(), stack)) {
                // Records can't be changed, so the entry is replaced with a new one
                items.set(i, new Entry(entry.item(), entry.count() + stack.getCount()));
                setDirty();
                return;
            }
        }
        items.add(new Entry(stack.copyWithCount(1), stack.getCount()));
        setDirty();
    }

    public void addExperience(int amount)
    {
        if (amount <= 0) return;
        experience += amount;
        setDirty();
    }

    public void addCreature(EntityType<?> type)
    {
        // Map.merge: put 1 if it's not there, otherwise add 1 to what's there
        creatures.merge(BuiltInRegistries.ENTITY_TYPE.getKey(type), 1, Integer::sum);
        setDirty();
    }

    public List<Entry> items() { return Collections.unmodifiableList(items); }
    public long experience() { return experience; }
    public Map<Identifier, Integer> creatures() { return Collections.unmodifiableMap(creatures); }

    public long totalItems()
    {
        return items.stream().mapToLong(Entry::count).sum();
    }
}
