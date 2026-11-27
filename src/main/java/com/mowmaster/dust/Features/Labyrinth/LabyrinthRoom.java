package com.mowmaster.dust.Features.Labyrinth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// One room of a labyrinth run (LabyrinthLayout places them, LabyrinthBuilder builds them, LabyrinthRooms runs them):
//   COMBAT     Arcane Spawners; its doors seal while a wave is on and players are in the room
//   MINIBOSS   a Herald (a lesser Archon of the run's element) wakes when a player comes in; the doors seal until it
//              falls. Every Herald must fall before the arena opens.
//   PUZZLE     runes in the walls, each of an element: strike every one with a spell holding its element and a chest
//              appears
//   CHALLENGE  a pit with a ledge round it and stepping stones out to an island with a chest; falling in sends you
//              back to the ledge
//   TREASURE   chests and Arcane Vaults
//   SANCTUARY  a font that mends whoever stands in the room
// state: DORMANT (untouched), ACTIVE (its encounter is on), CLEARED (done). centre: the middle of its floor (its
// template's "centre" marker: where a Herald wakes or a solved puzzle's chest appears). doors: the openings in its walls
// (sealed with Labyrinth Seals: sealed). spawners: its Arcane Spawners. runes: its puzzle's runes. ledge: where a fall
// in the challenge pit puts you back.
public final class LabyrinthRoom
{
    public enum Type implements StringRepresentable
    {
        COMBAT, MINIBOSS, PUZZLE, CHALLENGE, TREASURE, SANCTUARY;

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        @Override
        public String getSerializedName()
        {
            return name().toLowerCase(java.util.Locale.ROOT);
        }
    }

    public enum State implements StringRepresentable
    {
        DORMANT, ACTIVE, CLEARED;

        public static final Codec<State> CODEC = StringRepresentable.fromEnum(State::values);

        @Override
        public String getSerializedName()
        {
            return name().toLowerCase(java.util.Locale.ROOT);
        }
    }

    public static final Codec<LabyrinthRoom> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Type.CODEC.fieldOf("type").forGetter(room -> room.type),
            BoundingBox.CODEC.fieldOf("interior").forGetter(room -> room.interior),
            BoundingBox.CODEC.listOf().optionalFieldOf("doors", List.of()).forGetter(room -> room.doors),
            State.CODEC.optionalFieldOf("state", State.DORMANT).forGetter(room -> room.state),
            BlockPos.CODEC.fieldOf("centre").forGetter(room -> room.centre),
            UUIDUtil.CODEC.optionalFieldOf("mob").forGetter(room -> Optional.ofNullable(room.mob)),
            BlockPos.CODEC.listOf().optionalFieldOf("runes", List.of()).forGetter(room -> room.runes),
            Codec.BOOL.optionalFieldOf("sealed", false).forGetter(room -> room.sealed),
            BlockPos.CODEC.optionalFieldOf("ledge").forGetter(room -> Optional.ofNullable(room.ledge)),
            BlockPos.CODEC.listOf().optionalFieldOf("spawners", List.of()).forGetter(room -> room.spawners)
    ).apply(instance, LabyrinthRoom::new));

    public final Type type;
    public final BoundingBox interior;
    final List<BoundingBox> doors = new ArrayList<>();
    State state = State.DORMANT;
    public final BlockPos centre;
    UUID mob;
    final List<BlockPos> runes = new ArrayList<>();
    final List<BlockPos> spawners = new ArrayList<>();
    boolean sealed;
    BlockPos ledge;

    LabyrinthRoom(Type type, BoundingBox interior, BlockPos centre)
    {
        this.type = type;
        this.interior = interior;
        this.centre = centre;
    }

    private LabyrinthRoom(Type type, BoundingBox interior, List<BoundingBox> doors, State state, BlockPos centre, Optional<UUID> mob,
                          List<BlockPos> runes, boolean sealed, Optional<BlockPos> ledge, List<BlockPos> spawners)
    {
        this(type, interior, centre);
        this.doors.addAll(doors);
        this.state = state;
        this.mob = mob.orElse(null);
        this.runes.addAll(runes);
        this.sealed = sealed;
        this.ledge = ledge.orElse(null);
        this.spawners.addAll(spawners);
    }

    public State state()
    {
        return state;
    }
}
