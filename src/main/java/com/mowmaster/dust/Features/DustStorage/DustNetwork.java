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

    private record Remembered(int revision, List<Endpoint> endpoints) {}

    // One store on the network: where it is, its block entity, how many tubes away it is (0: right against the origin),
    // and the way there: the origin first, then each tube, the store last
    public record Endpoint(BlockPos pos, IDustHolder holder, int distance, List<BlockPos> path) {}

    private DustNetwork() {}

    // A tube or a block tubes join on was placed, changed or removed: networks have to be found again
    public static void changed(Level level)
    {
        if (!level.isClientSide()) REVISION.incrementAndGet();
    }

    // ---- moving dust ----

    // Pours up to amount of this element from origin into the network's stores; returns how much went in
    public static int insert(ServerLevel level, BlockPos origin, DustElement element, int amount)
    {
        if (amount <= 0) return 0;
        int left = amount;
        List<Endpoint> endpoints = endpoints(level, origin);
        // Two passes: the stores set to this element, then any others that will take it
        for (int pass = 0; pass < 2 && left > 0; pass++) {
            boolean wantPreferred = pass == 0;
            for (Endpoint end : endpoints) {
                if (left <= 0) break;
                if (end.holder().prefersDust(element) != wantPreferred) continue;
                int moved = end.holder().insertDust(element, left, false);
                if (moved <= 0) continue;
                left -= moved;
                showFlow(level, end.path(), element.color());
            }
        }
        return amount - left;
    }

    // Draws up to amount of this element out of the network's stores to origin; returns how much came out
    public static int extract(ServerLevel level, BlockPos origin, DustElement element, int amount)
    {
        if (amount <= 0) return 0;
        int left = amount;
        for (Endpoint end : endpoints(level, origin)) {
            if (left <= 0) break;
            int taken = end.holder().extractDust(element, left, false);
            if (taken <= 0) continue;
            left -= taken;
            List<BlockPos> back = new ArrayList<>(end.path());
            Collections.reverse(back);
            showFlow(level, back, element.color());
        }
        return amount - left;
    }

    // How much of this element the network's stores hold
    public static int available(ServerLevel level, BlockPos origin, DustElement element)
    {
        int total = 0;
        for (Endpoint end : endpoints(level, origin)) total += end.holder().dustAmount(element);
        return total;
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
        Map<BlockPos, Remembered> byOrigin = REMEMBERED.computeIfAbsent(level, key -> new HashMap<>());
        Remembered remembered = byOrigin.get(origin);
        int revision = REVISION.get();
        if (remembered != null && remembered.revision() == revision && stillThere(level, remembered.endpoints())) return remembered.endpoints();
        List<Endpoint> found = search(level, origin);
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
    private static List<Endpoint> search(ServerLevel level, BlockPos origin)
    {
        List<Endpoint> found = new ArrayList<>();
        Set<BlockPos> storesSeen = new HashSet<>();
        // Each tube reached, and the position it was reached from
        Map<BlockPos, BlockPos> cameFrom = new HashMap<>();
        Map<BlockPos, Integer> depth = new HashMap<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();

        // Right round the origin: stores against it, and the tubes joined on to it
        for (Direction side : Direction.values()) {
            BlockPos next = origin.relative(side);
            if (!level.isLoaded(next)) continue;
            BlockState state = level.getBlockState(next);
            if (state.getBlock() instanceof DustTubeBlock) {
                if (!DustTubeBlock.isJoined(state, side.getOpposite())) continue;
                cameFrom.put(next, origin);
                depth.put(next, 1);
                queue.add(next);
            }
            else if (state.getBlock() instanceof DustTubeConnectable connectable && connectable.connectsDustTube(state, side.getOpposite())) {
                addStore(level, next, 0, List.of(origin.immutable(), next), found, storesSeen);
            }
        }

        while (!queue.isEmpty() && cameFrom.size() <= MAX_TUBES) {
            BlockPos tube = queue.poll();
            BlockState state = level.getBlockState(tube);
            if (!(state.getBlock() instanceof DustTubeBlock)) continue;
            for (Direction side : Direction.values()) {
                if (!DustTubeBlock.isJoined(state, side)) continue;
                BlockPos next = tube.relative(side);
                if (next.equals(origin) || cameFrom.containsKey(next) || storesSeen.contains(next) || !level.isLoaded(next)) continue;
                if (level.getBlockState(next).getBlock() instanceof DustTubeBlock) {
                    cameFrom.put(next, tube);
                    depth.put(next, depth.get(tube) + 1);
                    queue.add(next);
                }
                else {
                    List<BlockPos> path = pathTo(tube, cameFrom, origin);
                    path.add(next);
                    addStore(level, next, depth.get(tube), path, found, storesSeen);
                }
            }
        }
        found.sort(Comparator.comparingInt(Endpoint::distance));
        return List.copyOf(found);
    }

    private static void addStore(ServerLevel level, BlockPos pos, int distance, List<BlockPos> path, List<Endpoint> found, Set<BlockPos> storesSeen)
    {
        if (!storesSeen.add(pos.immutable())) return;
        if (level.getBlockEntity(pos) instanceof IDustHolder holder) found.add(new Endpoint(pos.immutable(), holder, distance, List.copyOf(path)));
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
        List<BlockPos> shown = path.size() > MAX_FLOW_PATH ? path.subList(0, MAX_FLOW_PATH) : path;
        BlockPos middle = shown.get(shown.size() / 2);
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayersNear(level, null, middle.getX() + 0.5, middle.getY() + 0.5, middle.getZ() + 0.5,
                SEEN_FROM, new DustTubeFlow(List.copyOf(shown), color));
    }
}
