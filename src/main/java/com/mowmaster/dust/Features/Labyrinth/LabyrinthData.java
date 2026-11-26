package com.mowmaster.dust.Features.Labyrinth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

// Every labyrinth run there's been, by slot (data/dust_labyrinth.dat): a new run takes the lowest closed slot (or a
// new one), so the labyrinth dimension reuses its regions. Also the sanctums whose spawners are to be set waiting again
// once their keystone is loaded (a run from there was won: SanctumKeystoneBlockEntity), and the next run number.
public class LabyrinthData extends SavedData
{
    private record Stored(List<LabyrinthInstance> runs, long nextRun, List<GlobalPos> resets)
    {
        static final Codec<Stored> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                LabyrinthInstance.CODEC.listOf().optionalFieldOf("runs", List.of()).forGetter(Stored::runs),
                Codec.LONG.optionalFieldOf("next_run", 0L).forGetter(Stored::nextRun),
                GlobalPos.CODEC.listOf().optionalFieldOf("resets", List.of()).forGetter(Stored::resets)
        ).apply(instance, Stored::new));
    }

    private static final Codec<LabyrinthData> CODEC = Stored.CODEC.xmap(LabyrinthData::new,
            data -> new Stored(List.copyOf(data.slots.values()), data.nextRun, List.copyOf(data.resets)));
    private static final SavedDataType<LabyrinthData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(DustReferences.MODID, "labyrinth"), LabyrinthData::new, CODEC);

    private final Map<Integer, LabyrinthInstance> slots = new TreeMap<>();
    private final Set<GlobalPos> resets = new HashSet<>();
    private long nextRun;

    public LabyrinthData()
    {
    }

    private LabyrinthData(Stored stored)
    {
        for (LabyrinthInstance run : stored.runs()) slots.put(run.slot, run);
        nextRun = stored.nextRun();
        resets.addAll(stored.resets());
    }

    public static LabyrinthData get(MinecraftServer server)
    {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    // A new run in the lowest free slot. The caller builds it (LabyrinthBuilder clears what the slot's last run left).
    // previous: that slot's last run, for what to clear (null if the slot is new)
    public record NewRun(LabyrinthInstance run, @Nullable LabyrinthInstance previous) {}

    public NewRun create(ElementalArmorSet set, int bossLevel, long seed)
    {
        int slot = 0;
        while (slots.containsKey(slot) && slots.get(slot).state != LabyrinthInstance.State.CLOSED) slot++;
        LabyrinthInstance previous = slots.get(slot);
        LabyrinthInstance run = new LabyrinthInstance(slot, nextRun++, set, bossLevel, seed);
        slots.put(slot, run);
        setDirty();
        return new NewRun(run, previous);
    }

    public @Nullable LabyrinthInstance get(int slot)
    {
        return slots.get(slot);
    }

    // The run whose region holds pos (in the labyrinth dimension), if its slot has one
    public @Nullable LabyrinthInstance at(BlockPos pos)
    {
        int slot = Labyrinth.slotAt(pos);
        return slot < 0 ? null : slots.get(slot);
    }

    // The runs not yet closed
    public Collection<LabyrinthInstance> active()
    {
        List<LabyrinthInstance> active = new ArrayList<>();
        for (LabyrinthInstance run : slots.values()) if (run.state != LabyrinthInstance.State.CLOSED) active.add(run);
        return active;
    }

    public void setState(LabyrinthInstance run, LabyrinthInstance.State state)
    {
        run.state = state;
        setDirty();
    }

    // The sanctum at keystone is to be fought through again
    public void requestReset(GlobalPos keystone)
    {
        resets.add(keystone);
        setDirty();
    }

    // Whether the sanctum at keystone was due to be reset (and forgets it)
    public boolean takeReset(GlobalPos keystone)
    {
        if (!resets.remove(keystone)) return false;
        setDirty();
        return true;
    }
}
