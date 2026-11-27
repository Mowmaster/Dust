package com.mowmaster.dust.Features.Labyrinth;

import com.mojang.logging.LogUtils;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Sanctums.ArcanePortalBlock;
import com.mowmaster.dust.Features.Sanctums.Build;
import com.mowmaster.dust.Features.Sanctums.SanctumPalette;
import com.mowmaster.dust.Features.Sanctums.SanctumProcessor;
import com.mowmaster.dust.Features.Sanctums.SanctumTemplates;
import com.mowmaster.dust.Features.Sanctums.Sanctums;
import com.mowmaster.dust.Features.Sanctums.TemplateScan;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

// Builds a labyrinth run in its slot's region from its plan (LabyrinthLayout), out of structure templates
// (data/dust/structure/labyrinth/; SANCTUMS.md says how to make them) with their stand-in blocks swapped for the run's
// element's (SanctumProcessor), facing south (+Z) from the slot's middle (Labyrinth.origin):
//   hall       where players arrive (its "spawn" marker), its "centre" marker on the slot's middle, the maze through its
//              south wall; the 2 x 3 opening at its "exit" marker becomes the exit portal (the way out, always open)
//   rooms      a template of its type on each cell of the maze (combat, miniboss, puzzle, challenge, treasure,
//              sanctuary), turned a random way, its "centre" marker on the cell's middle
//   corridors  the corridor template laid end to end between rooms (the last one cut short); its inside sets the size
//              of the doorways
//   arena      past the far row: its north wall on ARENA_Z, its "boss" marker (where the Archon wakes) in line with the
//              far room; its way in sealed until every Herald has fallen
// A template's outer layer is its walls: doorways are cut through them where corridors meet (from the outside in, until
// the inside is reached), in line with the "centre" markers, so keep the middle of each wall clear. Templates of the
// run's element (labyrinth/<set>/<name>) are used before the shared ones (labyrinth/<name>), and each can have numbered
// variants (<name>_2, <name>_3...), picked at random. In a room: Arcane Spawners fight (LabyrinthRooms), chests and
// barrels left empty get the run's loot (LabyrinthLoot; richer in a challenge), Labyrinth Runes are each given an
// element (different ones while there are enough), Arcane Vaults take the element, and a challenge's "ledge" marker is
// where a fall puts you back.
// What's built is recorded in the run for the rules about breaking blocks (the templates' outer layers can't be broken),
// the rooms' logic (LabyrinthRooms) and clearing the slot for its next run (clear).
public final class LabyrinthBuilder
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int CELL = LabyrinthLayout.CELL;
    // The arena's north wall (local z): past the far row, with room for a corridor
    private static final int ARENA_Z = (LabyrinthLayout.ROWS + 1) * CELL - 9;
    // How deep a doorway may be cut (the walls' thickness)
    private static final int MAX_DOOR_DEPTH = 4;
    public static final String FOLDER = "labyrinth/", HALL = "hall", ARENA = "arena", CORRIDOR = "corridor";
    public static final String CENTRE = "centre", SPAWN = "spawn", EXIT = "exit", BOSS = "boss", LEDGE = "ledge";

    private LabyrinthBuilder() {}

    private static Build frame(ServerLevel level, BlockPos origin)
    {
        return new Build(level, origin, Direction.SOUTH);
    }

    // A template placed: which, how, where (its box: all of it), and what's in it
    private record Placed(Identifier id, StructureTemplate template, StructurePlaceSettings settings, BlockPos position, BoundingBox box, TemplateScan scan)
    {
        BlockPos world(BlockPos local)
        {
            return StructureTemplate.calculateRelativePosition(settings, local).offset(position);
        }

        BlockPos marker(String name)
        {
            BlockPos local = scan.marker(name);
            return world(local == null ? middle(scan) : local);
        }

        List<BlockPos> blocks(Block block)
        {
            List<BlockPos> found = new ArrayList<>();
            for (BlockPos local : TemplateScan.blocks(template, block)) found.add(world(local));
            return found;
        }

        // Inside its outer layer
        BoundingBox inside()
        {
            return new BoundingBox(box.minX() + 1, box.minY() + 1, box.minZ() + 1, box.maxX() - 1, box.maxY() - 1, box.maxZ() - 1);
        }
    }

    // A template's "centre" marker, or the middle of its floor
    private static BlockPos centre(TemplateScan scan)
    {
        BlockPos marker = scan.marker(CENTRE);
        return marker == null ? middle(scan) : marker;
    }

    // The middle of a template, standing on its bottom layer
    private static BlockPos middle(TemplateScan scan)
    {
        return new BlockPos(scan.size().getX() / 2, 1, scan.size().getZ() / 2);
    }

    // The corridor template and the size of its inside (its outer layer is walls, floor and ceiling)
    private record Corridor(Identifier id, StructureTemplate template, int width, int height, int length)
    {
        int outerWidth()
        {
            return width + 2;
        }

        int outerHeight()
        {
            return height + 2;
        }
    }

    public static void build(ServerLevel level, LabyrinthInstance run, @Nullable LabyrinthInstance previous)
    {
        if (previous != null) clear(level, previous);
        run.interiors.clear();
        run.placed.clear();
        run.rooms.clear();
        RandomSource random = RandomSource.create(run.seed);
        Build build = frame(level, run.origin());
        LabyrinthLayout layout = LabyrinthLayout.generate(random, run.bossLevel);
        StructureTemplateManager templates = level.getStructureTemplateManager();
        Corridor corridor = corridor(templates, run, random);
        int doorWidth = corridor == null ? 3 : corridor.width(), doorHeight = corridor == null ? 4 : corridor.height();

        // The hall, rooms and arena
        BlockPos origin = build.at(0, 0, 0);
        Placed hall = place(level, run, templates, HALL, Rotation.NONE, LabyrinthBuilder::centre, origin, random);
        Map<LabyrinthLayout.Cell, Placed> rooms = new HashMap<>();
        for (Map.Entry<LabyrinthLayout.Cell, LabyrinthRoom.Type> entry : layout.types.entrySet()) {
            LabyrinthLayout.Cell cell = entry.getKey();
            Placed room = place(level, run, templates, entry.getValue().getSerializedName(), Rotation.getRandom(random), LabyrinthBuilder::centre,
                    build.at(cell.x() * CELL, 0, cell.z() * CELL), random);
            if (room != null) rooms.put(cell, room);
        }
        Placed arena = place(level, run, templates, ARENA, Rotation.NONE, scan -> {
            BlockPos boss = scan.marker(BOSS);
            if (boss == null) boss = middle(scan);
            return new BlockPos(boss.getX(), boss.getY(), 0);
        }, build.at(layout.boss.x() * CELL, 0, ARENA_Z), random);

        // The corridors, with their doorways
        Map<LabyrinthLayout.Cell, List<BoundingBox>> doors = new HashMap<>();
        Placed first = rooms.get(layout.start);
        if (hall != null && first != null) {
            BoundingBox[] ends = connect(level, run, corridor, hall, origin, first, doorWidth, doorHeight, random);
            doors.computeIfAbsent(layout.start, cell -> new ArrayList<>()).add(ends[1]);
        }
        for (LabyrinthLayout.Link link : layout.links) {
            Placed a = rooms.get(link.a()), b = rooms.get(link.b());
            if (a == null || b == null) continue;
            BoundingBox[] ends = connect(level, run, corridor, a, a.marker(CENTRE), b, doorWidth, doorHeight, random);
            doors.computeIfAbsent(link.a(), cell -> new ArrayList<>()).add(ends[0]);
            doors.computeIfAbsent(link.b(), cell -> new ArrayList<>()).add(ends[1]);
        }
        Placed last = rooms.get(layout.boss);
        BoundingBox arenaDoor = null;
        if (last != null && arena != null) {
            BoundingBox[] ends = connect(level, run, corridor, last, last.marker(CENTRE), arena, doorWidth, doorHeight, random);
            doors.computeIfAbsent(layout.boss, cell -> new ArrayList<>()).add(ends[0]);
            arenaDoor = ends[1];
        }

        // The hall: where players arrive, the exit portal
        run.spawn = origin;
        if (hall != null) {
            run.interiors.add(hall.inside());
            run.spawn = hall.marker(SPAWN);
            exitPortal(level, run, hall);
        }
        run.spawnYaw = Direction.SOUTH.toYRot();

        // The rooms
        for (Map.Entry<LabyrinthLayout.Cell, Placed> entry : rooms.entrySet()) {
            Placed placed = entry.getValue();
            LabyrinthRoom room = new LabyrinthRoom(layout.types.get(entry.getKey()), placed.inside(), placed.marker(CENTRE));
            room.doors.addAll(doors.getOrDefault(entry.getKey(), List.of()));
            run.interiors.add(placed.inside());
            fitOut(level, run, room, placed, random);
            run.rooms.add(room);
        }

        // The arena: the Archon wakes when a player is well inside (past the doorway); its way in sealed while Heralds
        // remain
        if (arena != null) {
            BoundingBox inside = arena.inside();
            run.interiors.add(inside);
            run.arena = new BoundingBox(inside.minX(), inside.minY(), Math.min(inside.maxZ(), inside.minZ() + 2), inside.maxX(), inside.maxY(), inside.maxZ());
            run.bossSpawn = arena.marker(BOSS);
        }
        run.arenaDoor = arenaDoor;
        boolean heralds = false;
        for (LabyrinthRoom room : run.rooms) if (room.type == LabyrinthRoom.Type.MINIBOSS) heralds = true;
        run.arenaOpen = !heralds || arenaDoor == null;
        if (!run.arenaOpen) LabyrinthRooms.setSeal(level, run, arenaDoor, true);
    }

    // A template of this kind for the run (one of its element's own if there are any); null, with a warning, if there's none
    private static @Nullable Identifier pick(StructureTemplateManager templates, LabyrinthInstance run, String kind, RandomSource random)
    {
        List<Identifier> choices = TemplateScan.variants(templates, FOLDER + run.set.id + "/" + kind);
        if (choices.isEmpty()) choices = TemplateScan.variants(templates, FOLDER + kind);
        if (choices.isEmpty()) {
            LOGGER.error("No labyrinth template for {} (data/dust/structure/{}{}.nbt): that part of the labyrinth is left out", kind, FOLDER, kind);
            return null;
        }
        return choices.get(random.nextInt(choices.size()));
    }

    private static @Nullable Corridor corridor(StructureTemplateManager templates, LabyrinthInstance run, RandomSource random)
    {
        Identifier id = pick(templates, run, CORRIDOR, random);
        if (id == null) return null;
        StructureTemplate template = templates.getOrCreate(id);
        TemplateScan scan = TemplateScan.of(id, template);
        return new Corridor(id, template, Math.max(1, scan.size().getX() - 2), Math.max(1, scan.size().getY() - 2), Math.max(1, scan.size().getZ()));
    }

    // Places a template of this kind turned by rotation, its point anchor (its own coordinates, from its scan) at at
    private static @Nullable Placed place(ServerLevel level, LabyrinthInstance run, StructureTemplateManager templates, String kind, Rotation rotation,
                                          Function<TemplateScan, BlockPos> anchor, BlockPos at, RandomSource random)
    {
        Identifier id = pick(templates, run, kind, random);
        return id == null ? null : place(level, run, id, templates.getOrCreate(id), rotation, anchor, at, null, random);
    }

    // Places a template turned by rotation, its point anchor at at; only what falls in clip, if there is one
    private static Placed place(ServerLevel level, LabyrinthInstance run, Identifier id, StructureTemplate template, Rotation rotation,
                                Function<TemplateScan, BlockPos> anchor, BlockPos at, @Nullable BoundingBox clip, RandomSource random)
    {
        TemplateScan scan = TemplateScan.of(id, template);
        StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(rotation).setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING)
                .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).addProcessor(new SanctumProcessor(run.set));
        if (clip != null) settings.setBoundingBox(clip);
        BlockPos position = at.subtract(StructureTemplate.calculateRelativePosition(settings, anchor.apply(scan)));
        template.placeInWorld(level, position, position, settings, random, Build.FLAGS);
        return new Placed(id, template, settings, position, template.getBoundingBox(settings, position), scan);
    }

    // The corridor from a's wall to b's, on the line through a's centre, and a doorway through each wall; returns the
    // doorways (a's, b's)
    private static BoundingBox[] connect(ServerLevel level, LabyrinthInstance run, @Nullable Corridor corridor, Placed a, BlockPos aCentre, Placed b,
                                         int doorWidth, int doorHeight, RandomSource random)
    {
        BlockPos bCentre = b.marker(CENTRE);
        Direction way = aCentre.getX() != bCentre.getX() && aCentre.getZ() == bCentre.getZ()
                ? (bCentre.getX() > aCentre.getX() ? Direction.EAST : Direction.WEST)
                : (bCentre.getZ() > aCentre.getZ() ? Direction.SOUTH : Direction.NORTH);
        Direction.Axis axis = way.getAxis();
        int aFace = face(a.box(), way), bFace = face(b.box(), way.getOpposite());
        int length = Math.abs(bFace - aFace) - 1;
        if (corridor != null && length > 0) {
            BlockPos start = with(aCentre, axis, aFace).relative(way);
            Direction across = way.getClockWise();
            int reach = corridor.outerWidth() + corridor.outerHeight();
            BoundingBox clip = BoundingBox.fromCorners(start.relative(across, -reach).below(reach), start.relative(way, length - 1).relative(across, reach).above(reach));
            Rotation rotation = rotation(way);
            for (int along = 0; along < length; along += corridor.length()) {
                place(level, run, corridor.id(), corridor.template(), rotation, scan -> new BlockPos(scan.size().getX() / 2, 1, 0), start.relative(way, along), clip, random);
            }
            int half = corridor.width() / 2;
            run.interiors.add(BoundingBox.fromCorners(start.relative(across, -half),
                    start.relative(way, length - 1).relative(across, corridor.width() - 1 - half).above(corridor.height() - 1)));
        }
        BoundingBox aDoor = doorway(level, a.box(), aCentre, way, doorWidth, doorHeight);
        BoundingBox bDoor = doorway(level, b.box(), with(aCentre, axis, bFace), way.getOpposite(), doorWidth, doorHeight);
        run.interiors.add(aDoor);
        run.interiors.add(bDoor);
        return new BoundingBox[] {aDoor, bDoor};
    }

    // The turn that points a template's south (+Z) along way
    private static Rotation rotation(Direction way)
    {
        return switch (way) {
            case WEST -> Rotation.CLOCKWISE_90;
            case NORTH -> Rotation.CLOCKWISE_180;
            case EAST -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };
    }

    // Where a box's side facing way is (along way's axis)
    private static int face(BoundingBox box, Direction way)
    {
        boolean positive = way.getAxisDirection() == Direction.AxisDirection.POSITIVE;
        return switch (way.getAxis()) {
            case X -> positive ? box.maxX() : box.minX();
            case Y -> positive ? box.maxY() : box.minY();
            case Z -> positive ? box.maxZ() : box.minZ();
        };
    }

    private static BlockPos with(BlockPos pos, Direction.Axis axis, int value)
    {
        return switch (axis) {
            case X -> new BlockPos(value, pos.getY(), pos.getZ());
            case Y -> new BlockPos(pos.getX(), value, pos.getZ());
            case Z -> new BlockPos(pos.getX(), pos.getY(), value);
        };
    }

    // Cuts a doorway (width across, height up from the floor at line's height) through a template's wall facing way, on
    // the line through line: from the outer face in, until it reaches the inside (at most MAX_DOOR_DEPTH deep); returns it
    private static BoundingBox doorway(ServerLevel level, BoundingBox box, BlockPos line, Direction way, int width, int height)
    {
        Direction.Axis axis = way.getAxis();
        Direction across = way.getClockWise();
        int half = width / 2;
        BlockPos outer = with(line, axis, face(box, way));
        BlockPos layer = outer;
        int depth = 0;
        while (depth < MAX_DOOR_DEPTH) {
            if (depth > 0 && level.getBlockState(layer).isAir()) break;
            for (int side = -half; side <= width - 1 - half; side++) {
                for (int up = 0; up < height; up++) {
                    BlockPos pos = layer.relative(across, side).above(up);
                    if (!level.getBlockState(pos).isAir()) level.setBlock(pos, Blocks.AIR.defaultBlockState(), Build.FLAGS);
                }
            }
            depth++;
            layer = layer.relative(way.getOpposite());
        }
        BlockPos inner = outer.relative(way.getOpposite(), depth - 1);
        return BoundingBox.fromCorners(outer.relative(across, -half), inner.relative(across, width - 1 - half).above(height - 1));
    }

    // The hall's exit portal: the 2 x 3 opening at its "exit" marker, filled with exit portal (always open)
    private static void exitPortal(ServerLevel level, LabyrinthInstance run, Placed hall)
    {
        BlockPos local = hall.scan().marker(EXIT);
        if (local == null) {
            LOGGER.warn("Labyrinth hall template {} has no \"exit\" marker, so its runs have no way out by the entrance", hall.id());
            return;
        }
        SanctumTemplates.Opening opening = SanctumTemplates.opening(local, hall.scan());
        Direction.Axis axis = hall.settings().getRotation().rotate(Direction.fromAxisAndDirection(opening.axis(), Direction.AxisDirection.POSITIVE)).getAxis();
        BlockState sheet = ArcanePortalBlock.state(axis, run.set, true);
        for (BlockPos bottom : List.of(hall.world(opening.first()), hall.world(opening.second()))) {
            for (int up = 0; up < 3; up++) {
                BlockPos pos = bottom.above(up);
                if (level.getBlockState(pos).canBeReplaced()) level.setBlock(pos, sheet, Build.FLAGS);
            }
        }
    }

    // What makes a room work: its spawners, its loot, its runes, its ledge
    private static void fitOut(ServerLevel level, LabyrinthInstance run, LabyrinthRoom room, Placed placed, RandomSource random)
    {
        room.spawners.addAll(placed.blocks(Sanctums.ARCANE_SPAWNER.get()));
        int richness = room.type == LabyrinthRoom.Type.CHALLENGE ? 2 : 0;
        for (BlockPos local : SanctumTemplates.containers(placed.template())) {
            BlockEntity entity = level.getBlockEntity(placed.world(local));
            if (!(entity instanceof ChestBlockEntity) && !(entity instanceof BarrelBlockEntity)) continue;
            RandomizableContainerBlockEntity container = (RandomizableContainerBlockEntity) entity;
            if (container.getLootTable() == null && container.isEmpty()) LabyrinthLoot.fill(container, run.set, run.bossLevel, random, richness);
        }
        List<ElementalArmorSet> elements = new ArrayList<>();
        for (BlockPos pos : placed.blocks(LabyrinthBlocks.RUNE.get())) {
            if (elements.isEmpty()) {
                elements.addAll(List.of(ElementalArmorSet.PYREBORN, ElementalArmorSet.TIDEWOVEN, ElementalArmorSet.ROOTBOUND, ElementalArmorSet.LUMEN, ElementalArmorSet.RIFTBORN));
                if (!elements.contains(run.set)) elements.add(run.set);
            }
            ElementalArmorSet element = elements.remove(random.nextInt(elements.size()));
            level.setBlock(pos, LabyrinthBlocks.RUNE.get().defaultBlockState().setValue(LabyrinthRuneBlock.SET, element), Build.FLAGS);
            room.runes.add(pos);
        }
        if (room.type == LabyrinthRoom.Type.CHALLENGE) room.ledge = placed.scan().marker(LEDGE) == null ? room.centre : placed.marker(LEDGE);
    }

    // An exit portal two blocks wide and three high, its frame of the element's crystal, across the build's x, standing
    // where the Archon fell (z: 0)
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

    // Empties what a slot's last run built (its templates, corridors and doorways) and what was left lying in it
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
