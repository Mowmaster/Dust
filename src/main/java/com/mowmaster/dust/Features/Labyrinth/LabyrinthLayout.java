package com.mowmaster.dust.Features.Labyrinth;

import net.minecraft.util.RandomSource;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// The plan of a labyrinth run's maze, from its seed: rooms on a grid of cells (CELL blocks apart; columns -2..2, rows
// 1..ROWS ahead of the entrance hall), joined by corridors like Vault Hunters' rooms and tunnels:
//   1. a random walk from the first cell (in front of the hall) grows a tree of cells, as many as the run's level
//      asks for (more rooms at higher levels), reaching the far row (where the way to the arena leaves from)
//   2. a few extra corridors between neighbouring rooms make loops, so it's not just dead ends
//   3. each room gets a type by how far it is from the start: Heralds in the farthest dead ends, treasure in other dead
//      ends, then a puzzle or two, a challenge, a sanctuary; the rest are combat rooms (the first and the arena's
//      doorstep always are)
// LabyrinthBuilder builds it, each room from a template of its type (its size is the template's: at most 23 x 23, so
// rooms CELL apart leave room for a corridor).
public final class LabyrinthLayout
{
    public static final int CELL = 26;
    public static final int MIN_COLUMN = -2, MAX_COLUMN = 2, ROWS = 5;

    public record Cell(int x, int z)
    {
        Cell step(int dx, int dz)
        {
            return new Cell(x + dx, z + dz);
        }

        boolean inGrid()
        {
            return x >= MIN_COLUMN && x <= MAX_COLUMN && z >= 1 && z <= ROWS;
        }
    }

    // A corridor between two neighbouring cells (a before b: smaller z, or same z and smaller x)
    public record Link(Cell a, Cell b) {}

    public final Map<Cell, LabyrinthRoom.Type> types = new LinkedHashMap<>();
    public final Set<Link> links = new HashSet<>();
    public final Cell start = new Cell(0, 1);
    public Cell boss;

    private static final int[][] STEPS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    private LabyrinthLayout() {}

    public static LabyrinthLayout generate(RandomSource random, int bossLevel)
    {
        LabyrinthLayout layout = new LabyrinthLayout();
        int wanted = Math.min(20, 9 + bossLevel / 2);
        Set<Cell> cells = new HashSet<>();
        cells.add(layout.start);
        Deque<Cell> stack = new ArrayDeque<>();
        stack.push(layout.start);
        while (!stack.isEmpty() && cells.size() < wanted) {
            Cell current = stack.peek();
            List<Cell> free = new ArrayList<>();
            for (int[] step : STEPS) {
                Cell next = current.step(step[0], step[1]);
                if (next.inGrid() && !cells.contains(next)) free.add(next);
            }
            if (free.isEmpty()) {
                stack.pop();
                continue;
            }
            Cell next = free.get(random.nextInt(free.size()));
            layout.link(current, next);
            cells.add(next);
            stack.push(next);
        }
        // The far row must be reached: carry on straight ahead from the farthest cell
        Cell far = null;
        for (Cell cell : cells) if (far == null || cell.z > far.z) far = cell;
        while (far.z < ROWS) {
            Cell next = far.step(0, 1);
            layout.link(far, next);
            cells.add(next);
            far = next;
        }
        // A few loops
        List<Cell> ordered = new ArrayList<>(cells);
        ordered.sort((one, two) -> one.z != two.z ? Integer.compare(one.z, two.z) : Integer.compare(one.x, two.x));
        int loops = 0;
        for (Cell cell : ordered) {
            for (int[] step : new int[][] {{1, 0}, {0, 1}}) {
                Cell next = cell.step(step[0], step[1]);
                if (loops < 3 && cells.contains(next) && !layout.linked(cell, next) && random.nextInt(7) == 0) {
                    layout.link(cell, next);
                    loops++;
                }
            }
        }

        Map<Cell, Integer> distance = layout.distances(cells);
        // The way to the arena leaves from the far row's farthest room
        for (Cell cell : ordered) {
            if (cell.z == ROWS && (layout.boss == null || distance.get(cell) > distance.get(layout.boss))) layout.boss = cell;
        }
        layout.assign(ordered, distance, random);
        return layout;
    }

    private void link(Cell one, Cell two)
    {
        links.add(one.z < two.z || (one.z == two.z && one.x < two.x) ? new Link(one, two) : new Link(two, one));
    }

    public boolean linked(Cell one, Cell two)
    {
        return links.contains(new Link(one, two)) || links.contains(new Link(two, one));
    }

    private Map<Cell, Integer> distances(Set<Cell> cells)
    {
        Map<Cell, Integer> distance = new HashMap<>();
        Deque<Cell> queue = new ArrayDeque<>();
        distance.put(start, 0);
        queue.add(start);
        while (!queue.isEmpty()) {
            Cell cell = queue.poll();
            for (int[] step : STEPS) {
                Cell next = cell.step(step[0], step[1]);
                if (cells.contains(next) && !distance.containsKey(next) && linked(cell, next)) {
                    distance.put(next, distance.get(cell) + 1);
                    queue.add(next);
                }
            }
        }
        return distance;
    }

    private int neighbours(Cell cell)
    {
        int count = 0;
        for (int[] step : STEPS) if (linked(cell, cell.step(step[0], step[1]))) count++;
        return count;
    }

    private void assign(List<Cell> cells, Map<Cell, Integer> distance, RandomSource random)
    {
        for (Cell cell : cells) types.put(cell, LabyrinthRoom.Type.COMBAT);
        List<Cell> open = new ArrayList<>();
        for (Cell cell : cells) if (!cell.equals(start) && !cell.equals(boss)) open.add(cell);
        Collections.shuffle(open, new java.util.Random(random.nextLong()));
        // Dead ends first, the farthest first
        List<Cell> ends = new ArrayList<>();
        for (Cell cell : open) if (neighbours(cell) == 1) ends.add(cell);
        ends.sort((one, two) -> Integer.compare(distance.getOrDefault(two, 0), distance.getOrDefault(one, 0)));
        int heralds = cells.size() >= 8 ? 2 : 1;
        for (Cell cell : ends) {
            if (heralds > 0) {
                types.put(cell, LabyrinthRoom.Type.MINIBOSS);
                heralds--;
            } else if (types.get(cell) == LabyrinthRoom.Type.COMBAT) {
                types.put(cell, LabyrinthRoom.Type.TREASURE);
            }
        }
        // Not enough dead ends for the Heralds: the farthest other rooms
        if (heralds > 0) {
            List<Cell> byDistance = new ArrayList<>(open);
            byDistance.sort((one, two) -> Integer.compare(distance.getOrDefault(two, 0), distance.getOrDefault(one, 0)));
            for (Cell cell : byDistance) {
                if (heralds == 0) break;
                if (types.get(cell) == LabyrinthRoom.Type.MINIBOSS) continue;
                types.put(cell, LabyrinthRoom.Type.MINIBOSS);
                heralds--;
            }
        }
        // Then a puzzle or two, a challenge and a sanctuary among the rooms left, and treasure if no dead end got any
        boolean treasure = types.containsValue(LabyrinthRoom.Type.TREASURE);
        List<LabyrinthRoom.Type> extras = new ArrayList<>(List.of(LabyrinthRoom.Type.PUZZLE, LabyrinthRoom.Type.CHALLENGE, LabyrinthRoom.Type.SANCTUARY));
        if (!treasure) extras.addFirst(LabyrinthRoom.Type.TREASURE);
        if (cells.size() >= 12) extras.add(LabyrinthRoom.Type.PUZZLE);
        for (Cell cell : open) {
            if (extras.isEmpty()) break;
            if (types.get(cell) != LabyrinthRoom.Type.COMBAT) continue;
            types.put(cell, extras.removeFirst());
        }
    }
}
