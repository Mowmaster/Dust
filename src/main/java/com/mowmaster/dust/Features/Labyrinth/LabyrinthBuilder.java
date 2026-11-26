package com.mowmaster.dust.Features.Labyrinth;

import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Sanctums.ArcanePortalBlock;
import com.mowmaster.dust.Features.Sanctums.ArcaneSpawnerBlock;
import com.mowmaster.dust.Features.Sanctums.Build;
import com.mowmaster.dust.Features.Sanctums.SanctumPalette;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Builds a labyrinth run in its slot's region from its plan (LabyrinthLayout) and element palette (SanctumPalette),
// facing +Z from the slot's middle (Labyrinth.origin):
//   the entrance hall   where players arrive, with an exit portal in its back wall (the way out, always open)
//   the maze            rooms on a grid of cells joined by corridors (3 wide, 4 high), each room fitted out for its type
//                       (LabyrinthRoom): an Arcane Spawner, a Herald's ring, wall runes, a pit with stepping stones, chests
//                       or a mending font
//   the arena           beyond the far row, 31 x 31 and 12 high; its way in sealed until every Herald has fallen. The
//                       Archon wakes when a player steps in (Labyrinth.wake); an exit portal opens where it fell.
// Rooms go up in passes (every wall, then every floor, then every inside emptied), so where corridors meet rooms they
// open into each other. Only the walls' surfaces are set and only blocks that aren't already air are emptied, so even a
// big maze builds quickly. What's built is recorded in the run for the rules about breaking blocks, the rooms' logic
// (LabyrinthRooms) and clearing the slot for its next run (clear).
public final class LabyrinthBuilder
{
    private static final int CELL = LabyrinthLayout.CELL;
    private static final int CORRIDOR_HEIGHT = 4;
    private static final int ARENA_HALF = 15, ARENA_HEIGHT = 12, ARENA_DEPTH = 31;
    private static final int ARENA_Z0 = (LabyrinthLayout.ROWS + 1) * CELL - 8;

    private LabyrinthBuilder() {}

    // A space's inside, in local coordinates (corners inclusive)
    private record Space(int x0, int y0, int z0, int x1, int y1, int z1) {}

    private static Build frame(ServerLevel level, BlockPos origin)
    {
        return new Build(level, origin, Direction.SOUTH);
    }

    private static BoundingBox box(Build build, Space space)
    {
        return BoundingBox.fromCorners(build.at(space.x0, space.y0, space.z0), build.at(space.x1, space.y1, space.z1));
    }

    // The top of a room's inside, by type (a challenge room's floor is 4 lower: its pit)
    private static int top(LabyrinthRoom.Type type)
    {
        return switch (type) {
            case COMBAT -> 7;
            case MINIBOSS -> 9;
            case PUZZLE, SANCTUARY -> 6;
            case CHALLENGE -> 4;
            case TREASURE -> 5;
        };
    }

    public static void build(ServerLevel level, LabyrinthInstance run, @Nullable LabyrinthInstance previous)
    {
        if (previous != null) clear(level, previous);
        run.interiors.clear();
        run.placed.clear();
        run.rooms.clear();
        SanctumPalette palette = SanctumPalette.of(run.set);
        RandomSource random = RandomSource.create(run.seed);
        Build build = frame(level, run.origin());
        LabyrinthLayout layout = LabyrinthLayout.generate(random, run.bossLevel);

        List<Space> spaces = new ArrayList<>();
        Space hall = new Space(-5, 0, -5, 5, 5, 5);
        spaces.add(hall);
        Map<LabyrinthLayout.Cell, Space> rooms = new HashMap<>();
        Map<LabyrinthLayout.Cell, List<Space>> doors = new HashMap<>();
        for (Map.Entry<LabyrinthLayout.Cell, LabyrinthRoom.Type> entry : layout.types.entrySet()) {
            LabyrinthLayout.Cell cell = entry.getKey();
            int h = layout.half.get(cell), cx = cell.x() * CELL, cz = cell.z() * CELL;
            Space room = new Space(cx - h, entry.getValue() == LabyrinthRoom.Type.CHALLENGE ? -4 : 0, cz - h, cx + h, top(entry.getValue()), cz + h);
            rooms.put(cell, room);
            doors.put(cell, new ArrayList<>());
            spaces.add(room);
        }
        // The corridors between rooms, with a doorway at each end (the corridor's cross-section in each room's wall)
        for (LabyrinthLayout.Link link : layout.links) {
            Space a = rooms.get(link.a()), b = rooms.get(link.b());
            if (link.a().z() == link.b().z()) {
                int zc = link.a().z() * CELL, x0 = a.x1 + 1, x1 = b.x0 - 1;
                spaces.add(new Space(x0, 0, zc - 1, x1, CORRIDOR_HEIGHT - 1, zc + 1));
                doors.get(link.a()).add(new Space(x0, 0, zc - 1, x0, CORRIDOR_HEIGHT - 1, zc + 1));
                doors.get(link.b()).add(new Space(x1, 0, zc - 1, x1, CORRIDOR_HEIGHT - 1, zc + 1));
            } else {
                int xc = link.a().x() * CELL, z0 = a.z1 + 1, z1 = b.z0 - 1;
                spaces.add(new Space(xc - 1, 0, z0, xc + 1, CORRIDOR_HEIGHT - 1, z1));
                doors.get(link.a()).add(new Space(xc - 1, 0, z0, xc + 1, CORRIDOR_HEIGHT - 1, z0));
                doors.get(link.b()).add(new Space(xc - 1, 0, z1, xc + 1, CORRIDOR_HEIGHT - 1, z1));
            }
        }
        // From the hall to the first room, and from the far room to the arena
        Space first = rooms.get(layout.start);
        spaces.add(new Space(-1, 0, hall.z1 + 1, 1, CORRIDOR_HEIGHT - 1, first.z0 - 1));
        doors.get(layout.start).add(new Space(-1, 0, first.z0 - 1, 1, CORRIDOR_HEIGHT - 1, first.z0 - 1));
        Space last = rooms.get(layout.boss);
        int bx = layout.boss.x() * CELL;
        spaces.add(new Space(bx - 1, 0, last.z1 + 1, bx + 1, CORRIDOR_HEIGHT - 1, ARENA_Z0 - 1));
        doors.get(layout.boss).add(new Space(bx - 1, 0, last.z1 + 1, bx + 1, CORRIDOR_HEIGHT - 1, last.z1 + 1));
        Space arena = new Space(bx - ARENA_HALF, 0, ARENA_Z0, bx + ARENA_HALF, ARENA_HEIGHT - 1, ARENA_Z0 + ARENA_DEPTH - 1);
        spaces.add(arena);

        // The passes: walls, floors, insides
        for (Space space : spaces) shell(build, space, palette, random);
        for (Space space : spaces) build.fill(space.x0, space.y0 - 1, space.z0, space.x1, space.y0 - 1, space.z1, palette.floor());
        for (Space space : spaces) {
            carve(build, space);
            run.interiors.add(box(build, space));
        }

        // The entrance hall: lights, the exit portal in its back wall
        build.set(-6, 3, 0, palette.light());
        build.set(6, 3, 0, palette.light());
        build.set(0, 6, 0, palette.light());
        portal(build, run.set, palette, -6);
        run.spawn = build.at(0, 0, -1);
        run.spawnYaw = Direction.SOUTH.toYRot();

        // Corridor lights, every five blocks along their ceilings
        for (Space space : spaces) {
            if (space.y1 != CORRIDOR_HEIGHT - 1 || space == hall) continue;
            boolean alongX = space.x1 - space.x0 > space.z1 - space.z0;
            int length = alongX ? space.x1 - space.x0 : space.z1 - space.z0;
            for (int along = 2; along < length - 1; along += 5) {
                if (alongX) build.set(space.x0 + along, CORRIDOR_HEIGHT, (space.z0 + space.z1) / 2, palette.light());
                else build.set((space.x0 + space.x1) / 2, CORRIDOR_HEIGHT, space.z0 + along, palette.light());
            }
        }

        // The rooms
        for (Map.Entry<LabyrinthLayout.Cell, LabyrinthRoom.Type> entry : layout.types.entrySet()) {
            Space space = rooms.get(entry.getKey());
            LabyrinthRoom.Type type = entry.getValue();
            int cx = entry.getKey().x() * CELL, cz = entry.getKey().z() * CELL;
            LabyrinthRoom room = new LabyrinthRoom(type, box(build, space), build.at(cx, 0, cz));
            for (Space door : doors.get(entry.getKey())) room.doors.add(box(build, door));
            roomLights(build, space, palette);
            fitOut(build, run, room, space, cx, cz, palette, random);
            run.rooms.add(room);
        }

        // The arena: pillars, lights along its walls, the crystal ring where the Archon wakes; its way in sealed while
        // Heralds remain
        int az = ARENA_Z0 + ARENA_DEPTH / 2;
        for (int x : new int[] {bx - 9, bx + 9}) for (int z : new int[] {ARENA_Z0 + 6, ARENA_Z0 + ARENA_DEPTH - 7}) build.pillar(x, z, 0, ARENA_HEIGHT, palette);
        for (int z = ARENA_Z0 + 2; z < ARENA_Z0 + ARENA_DEPTH - 1; z += 6) {
            for (int y : new int[] {3, 8}) {
                build.set(arena.x0 - 1, y, z, palette.light());
                build.set(arena.x1 + 1, y, z, palette.light());
            }
        }
        for (int x = arena.x0 + 3; x <= arena.x1 - 3; x += 6) build.set(x, 5, arena.z1 + 1, palette.light());
        ring(build, bx, az, 4.2, 5.2, palette.accent());
        run.arena = BoundingBox.fromCorners(build.at(arena.x0, 0, ARENA_Z0 + 2), build.at(arena.x1, ARENA_HEIGHT - 1, arena.z1));
        run.bossSpawn = build.at(bx, 0, az);
        run.arenaDoor = box(build, new Space(bx - 1, 0, ARENA_Z0 - 1, bx + 1, CORRIDOR_HEIGHT - 1, ARENA_Z0 - 1));
        boolean heralds = false;
        for (LabyrinthRoom room : run.rooms) if (room.type == LabyrinthRoom.Type.MINIBOSS) heralds = true;
        run.arenaOpen = !heralds;
        if (heralds) LabyrinthRooms.setSeal(level, run, run.arenaDoor, true);
    }

    // The surface of a space's box with its walls (its inside grown by one), in the palette's wall blocks
    private static void shell(Build build, Space space, SanctumPalette palette, RandomSource random)
    {
        int x0 = space.x0 - 1, y0 = space.y0 - 1, z0 = space.z0 - 1, x1 = space.x1 + 1, y1 = space.y1 + 1, z1 = space.z1 + 1;
        for (int x = x0; x <= x1; x++) {
            for (int y = y0; y <= y1; y++) {
                for (int z = z0; z <= z1; z++) {
                    if (x != x0 && x != x1 && y != y0 && y != y1 && z != z0 && z != z1) continue;
                    build.set(x, y, z, palette.wall(random));
                }
            }
        }
    }

    // Empties a space's inside where it isn't already empty
    private static void carve(Build build, Space space)
    {
        BlockState air = Blocks.AIR.defaultBlockState();
        for (int x = space.x0; x <= space.x1; x++) {
            for (int y = space.y0; y <= space.y1; y++) {
                for (int z = space.z0; z <= space.z1; z++) {
                    BlockPos pos = build.at(x, y, z);
                    if (!build.level.getBlockState(pos).isAir()) build.level.setBlock(pos, air, Build.FLAGS);
                }
            }
        }
    }

    // Lights in a room's walls, near the corners (doorways are in the middle of the walls)
    private static void roomLights(Build build, Space space, SanctumPalette palette)
    {
        int y = 3;
        build.set(space.x0 - 1, y, space.z0 + 1, palette.light());
        build.set(space.x0 - 1, y, space.z1 - 1, palette.light());
        build.set(space.x1 + 1, y, space.z0 + 1, palette.light());
        build.set(space.x1 + 1, y, space.z1 - 1, palette.light());
        build.set(space.x0 + 1, y, space.z0 - 1, palette.light());
        build.set(space.x1 - 1, y, space.z0 - 1, palette.light());
        build.set(space.x0 + 1, y, space.z1 + 1, palette.light());
        build.set(space.x1 - 1, y, space.z1 + 1, palette.light());
    }

    // A ring of blocks in the floor (between two radii) round (cx, cz)
    private static void ring(Build build, int cx, int cz, double inner, double outer, BlockState state)
    {
        int reach = (int) Math.ceil(outer);
        for (int x = -reach; x <= reach; x++) {
            for (int z = -reach; z <= reach; z++) {
                double distance = Math.sqrt(x * x + z * z);
                if (distance >= inner && distance <= outer) build.set(cx + x, -1, cz + z, state);
            }
        }
    }

    // What makes each kind of room what it is (LabyrinthRoom)
    private static void fitOut(Build build, LabyrinthInstance run, LabyrinthRoom room, Space space, int cx, int cz, SanctumPalette palette, RandomSource random)
    {
        int h = cx - space.x0;
        int height = space.y1 + 1;
        switch (room.type) {
            case COMBAT -> {
                build.set(cx, 0, cz, com.mowmaster.dust.Features.Sanctums.Sanctums.ARCANE_SPAWNER.get().defaultBlockState().setValue(ArcaneSpawnerBlock.SET, run.set));
                if (h >= 6) for (int dx : new int[] {-(h - 2), h - 2}) for (int dz : new int[] {-(h - 2), h - 2}) build.pillar(cx + dx, cz + dz, 0, height, palette);
            }
            case MINIBOSS -> {
                ring(build, cx, cz, 2.5, 3.4, palette.accent());
                for (int dx : new int[] {-(h - 1), h - 1}) for (int dz : new int[] {-(h - 1), h - 1}) build.pillar(cx + dx, cz + dz, 0, height, palette);
            }
            case PUZZLE -> {
                build.set(cx, -1, cz, palette.accent());
                int[][] spots = {{cx - (h - 2), cz + h + 1}, {cx + (h - 2), cz + h + 1}, {cx - (h - 2), cz - h - 1}, {cx + (h - 2), cz - h - 1},
                        {cx + h + 1, cz - (h - 2)}, {cx + h + 1, cz + (h - 2)}, {cx - h - 1, cz - (h - 2)}, {cx - h - 1, cz + (h - 2)}};
                List<int[]> free = new ArrayList<>(List.of(spots));
                List<ElementalArmorSet> elements = new ArrayList<>(List.of(ElementalArmorSet.PYREBORN, ElementalArmorSet.TIDEWOVEN,
                        ElementalArmorSet.ROOTBOUND, ElementalArmorSet.LUMEN, ElementalArmorSet.RIFTBORN));
                if (!elements.contains(run.set)) elements.add(run.set);
                for (int i = 0; i < 3; i++) {
                    int[] spot = free.remove(random.nextInt(free.size()));
                    ElementalArmorSet element = elements.remove(random.nextInt(elements.size()));
                    build.set(spot[0], 2, spot[1], LabyrinthBlocks.RUNE.get().defaultBlockState().setValue(LabyrinthRuneBlock.SET, element));
                    room.runes.add(build.at(spot[0], 2, spot[1]));
                }
            }
            case CHALLENGE -> {
                // A ledge round the walls (every doorway opens onto it), an island in the middle with a chest, and
                // stepping stones out to it two blocks apart
                for (int x = space.x0; x <= space.x1; x++) {
                    for (int z = space.z0; z <= space.z1; z++) {
                        boolean edge = x == space.x0 || x == space.x1 || z == space.z0 || z == space.z1;
                        boolean island = Math.abs(x - cx) <= 1 && Math.abs(z - cz) <= 1;
                        if (edge || island) build.set(x, -1, z, palette.floor());
                    }
                }
                for (int z = space.z0 + 3; z < cz - 1; z += 3) build.set(cx + random.nextInt(3) - 1, -1, z, palette.accent());
                LabyrinthLoot.chest(build.level, build.at(cx, 0, cz), build.forward.getOpposite(), run, 2);
                room.ledge = build.at(cx, 0, space.z0);
            }
            case TREASURE -> {
                LabyrinthLoot.chest(build.level, build.at(space.x1 - 1, 0, space.z1 - 1), build.forward.getOpposite(), run, 0);
                LabyrinthLoot.chest(build.level, build.at(space.x0 + 1, 0, space.z1 - 1), build.forward.getOpposite(), run, 0);
                if (random.nextBoolean()) LabyrinthLoot.chest(build.level, build.at(space.x1 - 1, 0, space.z0 + 1), build.forward, run, 0);
                build.set(cx, -1, cz, palette.accent());
            }
            case SANCTUARY -> {
                build.set(cx, -1, cz, palette.light());
                ring(build, cx, cz, 0.9, 1.5, palette.accent());
                for (int dx : new int[] {-(h - 1), h - 1}) for (int dz : new int[] {-(h - 1), h - 1}) build.pillar(cx + dx, cz + dz, 0, height, palette);
            }
        }
    }

    // An exit portal two blocks wide and three high, its frame of the element's crystal, across the build's x: in the
    // entrance hall's back wall (z: the wall's line), or standing where the Archon fell
    private static void portal(Build build, ElementalArmorSet set, SanctumPalette palette, int z)
    {
        for (int y = -1; y <= 3; y++) {
            build.set(-2, y, z, palette.accent());
            build.set(1, y, z, palette.accent());
        }
        for (int x = -1; x <= 0; x++) {
            build.set(x, -1, z, palette.accent());
            build.set(x, 3, z, palette.accent());
        }
        BlockState sheet = ArcanePortalBlock.state(build.right.getAxis(), set, true);
        for (int x = -1; x <= 0; x++) for (int y = 0; y <= 2; y++) build.set(x, y, z, sheet);
    }

    // The Archon has fallen: the way out opens where it woke
    public static void openExit(ServerLevel level, LabyrinthInstance run)
    {
        Build build = frame(level, run.bossSpawn);
        portal(build, run.set, SanctumPalette.of(run.set), 0);
        Vec3 middle = Vec3.atCenterOf(build.at(0, 1, 0));
        level.sendParticles(new DustParticleOptions(run.set.element().color(), 2.0F), middle.x, middle.y, middle.z, 60, 1.0, 1.5, 1.0, 0);
        level.playSound(null, build.at(0, 1, 0), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0F, 0.7F);
    }

    // Empties what a slot's last run built (its rooms and their walls) and what was left lying in it
    static void clear(ServerLevel level, LabyrinthInstance previous)
    {
        AABB built = Labyrinth.builtBox(previous);
        if (built != null) for (Entity entity : level.getEntitiesOfClass(Entity.class, built, entity -> !(entity instanceof Player))) entity.discard();
        BlockState air = Blocks.AIR.defaultBlockState();
        for (BoundingBox box : previous.interiors) {
            BoundingBox shell = box.inflatedBy(1);
            for (BlockPos pos : BlockPos.betweenClosed(shell.minX(), shell.minY(), shell.minZ(), shell.maxX(), shell.maxY(), shell.maxZ())) {
                if (!level.getBlockState(pos).isAir()) level.setBlock(pos, air, Build.FLAGS);
            }
        }
    }
}
