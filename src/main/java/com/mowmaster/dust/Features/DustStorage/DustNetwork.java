package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// Dust Tube networks: what the alchemical apparatus (and the Dust Grinder, and Spell Casters) reach through the Glass
// Dust Tubes joined on to them. Asked from a block (the origin), a network is every dust store (IDustHolder: the Dust
// Jars) at the end of a tube joined to it, plus any store right against it (no tube needed), each with how many tubes
// away it is and the way there.
//   insert    pours dust in: first into stores already set to that element, then into any that will take it (an
//             empty jar takes the first element it's given and keeps it), nearest first each time
//   extract   draws dust out of the stores holding it, nearest first
//   available how much of an element the network's stores hold together
// One element to a network: tubes won't join networks or blocks set to different elements (DustTubeBlock). A machine
// can have several separate tube networks on it (parts): each only takes its own element from it (one whose jars are
// set to it, or one with none set yet). A part whose jars are set to two different elements (mixed) moves nothing.
// networkElement says which a network is.
// A part with no stores at all spills what's poured in (and no part with stores took) out of its open tube ends (a tube joined on one side only,
// its other end not pointing up): it falls out and piles up on the ground (DustClouds.deposit: Dust Piles).
// Each move is shown as the element's dust running along the tubes (DustTubeFlow, drawn by Client/DustTubeFlows).
// Finding a network walks the tubes (DustTubeBlock's joins; never into chunks that aren't loaded; at most MAX_TUBES
// tubes). It's remembered per origin until a tube or a block tubes join on is placed or removed anywhere (changed),
// or one of the stores it found is gone (an unloaded chunk, a broken jar).
public final class DustNetwork
{
    private static final int MAX_TUBES = 1024;
    // The most positions a flow sent to clients follows (a longer way is cut short: only the look)
    private static final int MAX_FLOW_PATH = 512;
    // Players this near the middle of a move see its flow (blocks)
    private static final double SEEN_FROM = 48;

    // Bumped whenever a tube, or a block tubes join on, is placed or removed: every remembered network is thrown away.
    // AtomicInteger: a number that's safe to change from more than one thread (an integrated server's thread and the
    // client's both place blocks).
    private static final AtomicInteger REVISION = new AtomicInteger();
    // Remembered networks by level, then by origin. WeakHashMap: a level's entry goes once nothing else holds the level
    // (it was unloaded), so nothing is kept for levels that are gone.
    private static final Map<Level, Map<BlockPos, Remembered>> REMEMBERED = new WeakHashMap<>();

    private record Remembered(int revision, Found found) {}

    // What a walk found: the stores, nearest first, and the open tube ends, nearest first
    private record Found(List<Endpoint> endpoints, List<OpenEnd> openEnds) {}

    // A tube's open end: the tube, the way its open end points, and the way there from the origin (the tube last)
    public record OpenEnd(BlockPos tube, Direction out, int distance, List<BlockPos> path, int part) {}

    // One store on the network: where it is, its block entity, how many tubes away it is (0: right against the origin),
    // and the way there: the origin first, then each tube, the store last; part: which of the origin's separate tube
    // networks it's on (each set of tubes joined on to the origin that don't meet, and each store right against it, is a
    // part of its own)
    public record Endpoint(BlockPos pos, IDustHolder holder, int distance, List<BlockPos> path, int part) {}

    private DustNetwork() {}

    // A tube or a block tubes join on was placed, changed or removed: networks have to be found again
    public static void changed(Level level)
    {
        if (!level.isClientSide()) REVISION.incrementAndGet();
    }

    // ---- moving dust ----

    // Pours up to amount of this element from origin into the network's stores; returns how much went in. Each part
    // (a separate tube network joined on to the origin) only takes its own element: one whose jars are set to it, then
    // one with no jars set yet; a mixed part takes nothing. What's left spills out of parts with no stores at all.
    public static int insert(ServerLevel level, BlockPos origin, DustElement element, int amount)
    {
        if (amount <= 0) return 0;
        int left = amount;
        Found found = network(level, origin);
        List<Endpoint> endpoints = found.endpoints();
        Map<Integer, Holds> parts = partHolds(endpoints);
        // Three passes: stores set to this element in parts set to it, then any store that'll take it in those, then in
        // parts with nothing set yet
        for (int pass = 0; pass < 3 && left > 0; pass++) {
            for (Endpoint end : endpoints) {
                if (left <= 0) break;
                Holds part = parts.get(end.part());
                if (part.mixed()) continue;
                boolean partSet = element.equals(part.element());
                if (pass < 2 ? !partSet : part.element() != null) continue;
                if (pass == 0 && !end.holder().prefersDust(element)) continue;
                int moved = end.holder().insertDust(element, left, false);
                if (moved <= 0) continue;
                left -= moved;
                showFlow(level, end.path(), element.color());
            }
        }
        if (left > 0) {
            List<OpenEnd> spillable = new ArrayList<>();
            for (OpenEnd end : found.openEnds()) if (!parts.containsKey(end.part())) spillable.add(end);
            left -= spill(level, spillable, element, left);
        }
        return amount - left;
    }

    // Draws up to amount of this element out of the network's stores to origin (not from a mixed part); returns how much
    // came out
    public static int extract(ServerLevel level, BlockPos origin, DustElement element, int amount)
    {
        if (amount <= 0) return 0;
        int left = amount;
        List<Endpoint> endpoints = endpoints(level, origin);
        Map<Integer, Holds> parts = partHolds(endpoints);
        for (Endpoint end : endpoints) {
            if (left <= 0) break;
            if (parts.get(end.part()).mixed()) continue;
            int taken = end.holder().extractDust(element, left, false);
            if (taken <= 0) continue;
            left -= taken;
            List<BlockPos> back = new ArrayList<>(end.path());
            Collections.reverse(back);
            showFlow(level, back, element.color());
        }
        return amount - left;
    }

    // How much of this element the network's stores hold (not counting a mixed part)
    public static int available(ServerLevel level, BlockPos origin, DustElement element)
    {
        List<Endpoint> endpoints = endpoints(level, origin);
        Map<Integer, Holds> parts = partHolds(endpoints);
        int total = 0;
        for (Endpoint end : endpoints) if (!parts.get(end.part()).mixed()) total += end.holder().dustAmount(element);
        return total;
    }

    // Whether this element could go anywhere from origin: a part set to it or with nothing set (or no stores at all,
    // when it would spill)
    public static boolean takes(ServerLevel level, BlockPos origin, DustElement element)
    {
        List<Endpoint> endpoints = endpoints(level, origin);
        if (endpoints.isEmpty()) return true;
        for (Holds part : partHolds(endpoints).values()) {
            if (!part.mixed() && (part.element() == null || part.element().equals(element))) return true;
        }
        return false;
    }

    // The element of the tubes joined on to a tube (walking out from it, not along skip): what the blocks they join
    // hold or give out (DustTubeConnectable.tubeElement: jars, Still Towers). Not remembered (asked as tubes are placed).
    public static Holds elementFrom(net.minecraft.world.level.LevelReader level, BlockPos start, @org.jspecify.annotations.Nullable Direction skip)
    {
        DustElement seen = null;
        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        visited.add(start);
        queue.add(start);
        while (!queue.isEmpty() && visited.size() <= MAX_TUBES) {
            BlockPos tube = queue.poll();
            BlockState state = level.getBlockState(tube);
            if (!(state.getBlock() instanceof DustTubeBlock)) continue;
            for (Direction side : Direction.values()) {
                if ((tube.equals(start) && side == skip) || !DustTubeBlock.isJoined(state, side)) continue;
                BlockPos next = tube.relative(side);
                if (!level.hasChunkAt(next) || !visited.add(next)) continue;
                BlockState nextState = level.getBlockState(next);
                if (nextState.getBlock() instanceof DustTubeBlock) {
                    if (DustTubeBlock.isJoined(nextState, side.getOpposite())) queue.add(next);
                    else visited.remove(next);
                    continue;
                }
                if (!(nextState.getBlock() instanceof DustTubeConnectable connectable)) continue;
                DustElement element = connectable.tubeElement(level, next);
                if (element == null) continue;
                if (seen == null) seen = element;
                else if (!seen.equals(element)) return new Holds(null, true);
            }
        }
        return new Holds(seen, false);
    }

    // Whether this element has stores to go to from origin: a part with stores that's set to it or to nothing yet (not
    // just tubes that would spill it)
    public static boolean routes(ServerLevel level, BlockPos origin, DustElement element)
    {
        List<Endpoint> endpoints = endpoints(level, origin);
        if (endpoints.isEmpty()) return false;
        for (Holds part : partHolds(endpoints).values()) {
            if (!part.mixed() && (part.element() == null || part.element().equals(element))) return true;
        }
        return false;
    }

    // What each part holds (only the parts with stores)
    private static Map<Integer, Holds> partHolds(List<Endpoint> endpoints)
    {
        Map<Integer, List<Endpoint>> byPart = new HashMap<>();
        for (Endpoint end : endpoints) byPart.computeIfAbsent(end.part(), key -> new ArrayList<>()).add(end);
        Map<Integer, Holds> holds = new HashMap<>();
        byPart.forEach((part, ends) -> holds.put(part, holds(ends)));
        return holds;
    }

    // No stores: pours out of the open ends, nearest first, as much as lands
    private static int spill(ServerLevel level, List<OpenEnd> openEnds, DustElement element, int amount)
    {
        int left = amount;
        for (OpenEnd end : openEnds) {
            if (left <= 0) break;
            BlockPos outlet = end.tube().relative(end.out());
            int landed = DustClouds.deposit(level, outlet, element, left);
            if (landed <= 0) continue;
            left -= landed;
            List<BlockPos> path = new ArrayList<>(end.path());
            path.add(outlet);
            showFlow(level, path, element.color());
            level.sendParticles(new net.minecraft.core.particles.DustParticleOptions(element.color(), 1.0F),
                    outlet.getX() + 0.5, outlet.getY() + 0.5, outlet.getZ() + 0.5, 4, 0.15, 0.3, 0.15, 0);
        }
        return amount - left;
    }

    // Which element a network is for: the one its jars are set to (null: none set yet), or mixed (set to two different
    // elements: it moves nothing)
    public record Holds(@org.jspecify.annotations.Nullable DustElement element, boolean mixed) {}

    public static Holds networkElement(ServerLevel level, BlockPos origin)
    {
        return holds(endpoints(level, origin));
    }

    private static Holds holds(List<Endpoint> endpoints)
    {
        DustElement seen = null;
        for (Endpoint end : endpoints) {
            if (!(end.holder() instanceof DustJarBlockEntity jar) || jar.element() == null) continue;
            if (seen == null) seen = jar.element();
            else if (!seen.equals(jar.element())) return new Holds(null, true);
        }
        return new Holds(seen, false);
    }

    // Whether origin has any store on a network (or right against it)
    public static boolean hasStores(ServerLevel level, BlockPos origin)
    {
        return !endpoints(level, origin).isEmpty();
    }

    // ---- finding the network ----

    // The stores on origin's network, nearest first (remembered; see above)
    public static List<Endpoint> endpoints(ServerLevel level, BlockPos origin)
    {
        return network(level, origin).endpoints();
    }

    private static Found network(ServerLevel level, BlockPos origin)
    {
        Map<BlockPos, Remembered> byOrigin = REMEMBERED.computeIfAbsent(level, key -> new HashMap<>());
        Remembered remembered = byOrigin.get(origin);
        int revision = REVISION.get();
        if (remembered != null && remembered.revision() == revision && stillThere(level, remembered.found().endpoints())) return remembered.found();
        Found found = search(level, origin);
        // Thrown away together when anything changes, so this never holds more than the origins asking since then
        if (remembered != null && remembered.revision() != revision) byOrigin.clear();
        byOrigin.put(origin.immutable(), new Remembered(revision, found));
        return found;
    }

    // Every store found is still the block entity at its place (not unloaded, broken or replaced)
    private static boolean stillThere(ServerLevel level, List<Endpoint> endpoints)
    {
        for (Endpoint end : endpoints) {
            if (!(end.holder() instanceof BlockEntity blockEntity) || blockEntity.isRemoved() || level.getBlockEntity(end.pos()) != blockEntity) return false;
        }
        return true;
    }

    // A breadth-first walk out from origin along the tubes' joins (breadth-first: every tube one step away before any
    // two away, so the first way found to each store is a shortest one)
    private static Found search(ServerLevel level, BlockPos origin)
    {
        List<Endpoint> found = new ArrayList<>();
        List<OpenEnd> openEnds = new ArrayList<>();
        Set<BlockPos> storesSeen = new HashSet<>();
        // Each tube reached, and the position it was reached from
        Map<BlockPos, BlockPos> cameFrom = new HashMap<>();
        Map<BlockPos, Integer> depth = new HashMap<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();

        // Right round the origin: stores against it (each a part of its own), and the tubes joined on to it, each walked
        // out in turn (a part of its own, unless an earlier walk already reached it)
        int part = 0;
        for (Direction side : Direction.values()) {
            BlockPos next = origin.relative(side);
            if (!level.isLoaded(next)) continue;
            BlockState state = level.getBlockState(next);
            if (state.getBlock() instanceof DustTubeBlock) {
                if (!DustTubeBlock.isJoined(state, side.getOpposite()) || cameFrom.containsKey(next)) continue;
                cameFrom.put(next, origin);
                depth.put(next, 1);
                queue.add(next);
                walk(level, origin, queue, cameFrom, depth, found, openEnds, storesSeen, part++);
            }
            else if (state.getBlock() instanceof DustTubeConnectable connectable && connectable.connectsDustTube(state, side.getOpposite())) {
                addStore(level, next, 0, List.of(origin.immutable(), next), found, storesSeen, part++);
            }
        }
        found.sort(Comparator.comparingInt(Endpoint::distance));
        openEnds.sort(Comparator.comparingInt(OpenEnd::distance));
        return new Found(List.copyOf(found), List.copyOf(openEnds));
    }

    // One part's walk: breadth-first from the tubes queued, along the tubes' joins
    private static void walk(ServerLevel level, BlockPos origin, ArrayDeque<BlockPos> queue, Map<BlockPos, BlockPos> cameFrom, Map<BlockPos, Integer> depth,
                             List<Endpoint> found, List<OpenEnd> openEnds, Set<BlockPos> storesSeen, int part)
    {
        while (!queue.isEmpty() && cameFrom.size() <= MAX_TUBES) {
            BlockPos tube = queue.poll();
            BlockState state = level.getBlockState(tube);
            if (!(state.getBlock() instanceof DustTubeBlock)) continue;
            Direction open = openEnd(state);
            if (open != null) openEnds.add(new OpenEnd(tube, open, depth.get(tube), pathTo(tube, cameFrom, origin), part));
            for (Direction side : Direction.values()) {
                if (!DustTubeBlock.isJoined(state, side)) continue;
                BlockPos next = tube.relative(side);
                if (next.equals(origin) || cameFrom.containsKey(next) || storesSeen.contains(next) || !level.isLoaded(next)) continue;
                BlockState nextState = level.getBlockState(next);
                if (nextState.getBlock() instanceof DustTubeBlock) {
                    // Both tubes have to be joined to each other (a waxed tube may keep an arm to one cut off from it)
                    if (!DustTubeBlock.isJoined(nextState, side.getOpposite())) continue;
                    cameFrom.put(next, tube);
                    depth.put(next, depth.get(tube) + 1);
                    queue.add(next);
                }
                else {
                    List<BlockPos> path = pathTo(tube, cameFrom, origin);
                    path.add(next);
                    addStore(level, next, depth.get(tube), path, found, storesSeen, part);
                }
            }
        }
        queue.clear();
    }

    // A tube joined on one side only is an open end, pointing the other way (null: not one, or it points up, where
    // dust wouldn't come out)
    private static @org.jspecify.annotations.Nullable Direction openEnd(BlockState tube)
    {
        Direction joined = null;
        for (Direction side : Direction.values()) {
            if (!DustTubeBlock.isJoined(tube, side)) continue;
            if (joined != null) return null;
            joined = side;
        }
        if (joined == null || joined.getOpposite() == Direction.UP) return null;
        return joined.getOpposite();
    }

    private static void addStore(ServerLevel level, BlockPos pos, int distance, List<BlockPos> path, List<Endpoint> found, Set<BlockPos> storesSeen, int part)
    {
        if (!storesSeen.add(pos.immutable())) return;
        if (level.getBlockEntity(pos) instanceof IDustHolder holder) found.add(new Endpoint(pos.immutable(), holder, distance, List.copyOf(path), part));
    }

    // The way from origin to this tube: origin first, the tube last
    private static List<BlockPos> pathTo(BlockPos tube, Map<BlockPos, BlockPos> cameFrom, BlockPos origin)
    {
        List<BlockPos> path = new ArrayList<>();
        BlockPos at = tube;
        while (at != null && !at.equals(origin)) {
            path.add(at);
            at = cameFrom.get(at);
        }
        path.add(origin.immutable());
        Collections.reverse(path);
        return path;
    }

    // ---- the look ----

    // The element's dust running along this way, for the players nearby
    private static void showFlow(ServerLevel level, List<BlockPos> path, int color)
    {
        if (path.size() < 2) return;
        DustTubeBlock.filled(level, path);
        List<BlockPos> shown = path.size() > MAX_FLOW_PATH ? path.subList(0, MAX_FLOW_PATH) : path;
        BlockPos middle = shown.get(shown.size() / 2);
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayersNear(level, null, middle.getX() + 0.5, middle.getY() + 0.5, middle.getZ() + 0.5,
                SEEN_FROM, new DustTubeFlow(List.copyOf(shown), color));
    }
}
