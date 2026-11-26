package com.mowmaster.dust.Features.Labyrinth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

// One labyrinth run: its slot (which region of the labyrinth dimension it's built in: Labyrinth.origin), a run number
// (never reused, unlike slots), its element and Archon level, the seed it was built from, and its state:
//   OPEN      built, players exploring; the Archon wakes when one reaches its arena
//   BOSS      the Archon is fighting
//   COMPLETE  the Archon is beaten (an exit portal stands in the arena)
//   CLOSED    everyone left: the region is free, and gets cleared and rebuilt for the next run that takes its slot
// The builder records what it built (LabyrinthBuilder): the inside of every room and corridor (interiors: the blocks
// round them are its walls, which can't be broken), where players arrive (spawn, facing spawnYaw), the arena and where
// the Archon wakes, the maze's rooms (LabyrinthRoom) and the seal over the arena's way in (arenaDoor: open once every
// Herald has fallen). Blocks players place are noted (placed), so they can take them back.
public final class LabyrinthInstance
{
    public enum State implements StringRepresentable
    {
        OPEN, BOSS, COMPLETE, CLOSED;

        public static final Codec<State> CODEC = StringRepresentable.fromEnum(State::values);

        @Override
        public String getSerializedName()
        {
            return name().toLowerCase(java.util.Locale.ROOT);
        }
    }

    public static final Codec<LabyrinthInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("slot").forGetter(run -> run.slot),
            Codec.LONG.fieldOf("run").forGetter(run -> run.run),
            ElementalArmorSet.CODEC.fieldOf("set").forGetter(run -> run.set),
            Codec.INT.fieldOf("boss_level").forGetter(run -> run.bossLevel),
            Codec.LONG.fieldOf("seed").forGetter(run -> run.seed),
            State.CODEC.fieldOf("state").forGetter(run -> run.state),
            Codec.LONG.optionalFieldOf("last_occupied", 0L).forGetter(run -> run.lastOccupied),
            BoundingBox.CODEC.listOf().optionalFieldOf("interiors", List.of()).forGetter(run -> run.interiors),
            Codec.LONG.listOf().optionalFieldOf("placed", List.of()).forGetter(run -> List.copyOf(run.placed)),
            UUIDUtil.CODEC.optionalFieldOf("boss").forGetter(run -> Optional.ofNullable(run.boss)),
            GlobalPos.CODEC.optionalFieldOf("keystone").forGetter(run -> Optional.ofNullable(run.keystone)),
            BlockPos.CODEC.optionalFieldOf("spawn", BlockPos.ZERO).forGetter(run -> run.spawn),
            Codec.FLOAT.optionalFieldOf("spawn_yaw", 0.0F).forGetter(run -> run.spawnYaw),
            BoundingBox.CODEC.optionalFieldOf("arena").forGetter(run -> Optional.ofNullable(run.arena)),
            BlockPos.CODEC.optionalFieldOf("boss_spawn", BlockPos.ZERO).forGetter(run -> run.bossSpawn),
            Progress.CODEC.optionalFieldOf("progress", Progress.NONE).forGetter(run -> new Progress(List.copyOf(run.rooms), Optional.ofNullable(run.arenaDoor), run.arenaOpen))
    ).apply(instance, LabyrinthInstance::new));

    // The rooms of the maze, the seal over the arena's way in and whether it's open (a record of its own: a codec
    // takes at most 16 fields)
    private record Progress(List<LabyrinthRoom> rooms, Optional<BoundingBox> arenaDoor, boolean arenaOpen)
    {
        static final Progress NONE = new Progress(List.of(), Optional.empty(), true);
        static final Codec<Progress> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                LabyrinthRoom.CODEC.listOf().optionalFieldOf("rooms", List.of()).forGetter(Progress::rooms),
                BoundingBox.CODEC.optionalFieldOf("arena_door").forGetter(Progress::arenaDoor),
                Codec.BOOL.optionalFieldOf("arena_open", true).forGetter(Progress::arenaOpen)
        ).apply(instance, Progress::new));
    }

    public final int slot;
    public final long run;
    public final ElementalArmorSet set;
    public final int bossLevel;
    public final long seed;
    State state;
    long lastOccupied;
    final List<BoundingBox> interiors = new ArrayList<>();
    final Set<Long> placed = new HashSet<>();
    UUID boss;
    GlobalPos keystone;
    BlockPos spawn = BlockPos.ZERO;
    float spawnYaw;
    BoundingBox arena;
    BlockPos bossSpawn = BlockPos.ZERO;
    final List<LabyrinthRoom> rooms = new ArrayList<>();
    BoundingBox arenaDoor;
    boolean arenaOpen = true;

    LabyrinthInstance(int slot, long run, ElementalArmorSet set, int bossLevel, long seed)
    {
        this.slot = slot;
        this.run = run;
        this.set = set;
        this.bossLevel = bossLevel;
        this.seed = seed;
        this.state = State.OPEN;
    }

    private LabyrinthInstance(int slot, long run, ElementalArmorSet set, int bossLevel, long seed, State state, long lastOccupied,
                              List<BoundingBox> interiors, List<Long> placed, Optional<UUID> boss, Optional<GlobalPos> keystone,
                              BlockPos spawn, float spawnYaw, Optional<BoundingBox> arena, BlockPos bossSpawn, Progress progress)
    {
        this(slot, run, set, bossLevel, seed);
        this.state = state;
        this.lastOccupied = lastOccupied;
        this.interiors.addAll(interiors);
        this.placed.addAll(placed);
        this.boss = boss.orElse(null);
        this.keystone = keystone.orElse(null);
        this.spawn = spawn;
        this.spawnYaw = spawnYaw;
        this.arena = arena.orElse(null);
        this.bossSpawn = bossSpawn;
        this.rooms.addAll(progress.rooms());
        this.arenaDoor = progress.arenaDoor().orElse(null);
        this.arenaOpen = progress.arenaOpen();
    }

    public List<LabyrinthRoom> rooms()
    {
        return rooms;
    }

    // The room whose inside holds pos, if any
    public LabyrinthRoom roomAt(BlockPos pos)
    {
        for (LabyrinthRoom room : rooms) if (room.interior.isInside(pos)) return room;
        return null;
    }

    public State state()
    {
        return state;
    }

    public BlockPos origin()
    {
        return Labyrinth.origin(slot);
    }

    // Whether pos is in one of its rooms or corridors (not their walls)
    public boolean inside(BlockPos pos)
    {
        for (BoundingBox box : interiors) if (box.isInside(pos)) return true;
        return false;
    }

    // Whether pos is part of the walls, floors or ceilings round its rooms (they can't be broken)
    public boolean isShell(BlockPos pos)
    {
        if (inside(pos)) return false;
        for (BoundingBox box : interiors) if (box.inflatedBy(1).isInside(pos)) return true;
        return false;
    }
}
