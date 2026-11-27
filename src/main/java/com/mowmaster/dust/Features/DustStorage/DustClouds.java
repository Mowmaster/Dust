package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.DustConfig;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.Features.Apparatus.still.StillFireboxBlock;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.effects.SpellExplosions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// Spilled dust (Dust Piles) and the dusty air round them.
//   spilling  deposit: dust poured out of an open tube end falls to the ground and piles up there, slumping out into
//             a mound round where it falls (piles of any elements together), no higher than the tube's end
//   the cloud each pile out in the open (DustPileBlockEntity.isOpen) fills a 9x9 area round it (radius), from RISE
//             blocks under it to RISE over it (so a heap several blocks high counts whole), with its dust. How thick the air is at a place (density) is all the dust
//             of the piles whose cloud reaches it: the thicker, the harder to see through (Client/DustCloudClient: half
//             at ignite)
//   igniting  where the air holds ignite() dust or more, a spark sets it off (spark): an open flame (fire, dust fire, a lit
//             campfire or firebox, lava), a mob on fire or a creeper starting to explode in the cloud (checked by the
//             piles: DustPileBlockEntity), a spell
//             cast or landing there, a Magical Flint and Steel struck there. Every pile whose cloud reaches the spark
//             burns up at once, and with it every pile in reach of one that burns (their clouds touch), on and on: a
//             chain. They go off as spell explosions (SpellExplosions) that break and drop blocks: one for each group of
//             piles near together (a cloud's size), its power growing with that group's dust (power).
// The cloud's size, the dust it takes, the blasts' power and whether there are blasts at all are settings (DustConfig
// [dust_clouds]).
public final class DustClouds
{
    public static final int RISE = 3;
    // The most piles one chain sets off
    private static final int MAX_CHAIN = 512;
    // How far dust falls from a tube's end looking for somewhere to land (blocks)
    private static final int MAX_FALL = 24;
    private static final int MAX_FLAMES = 4;

    private DustClouds() {}

    // ---- settings (DustConfig [dust_clouds]; their defaults until it's loaded) ----

    // How far a pile's cloud reaches to each side (cloud_size 9: 4)
    public static int radius()
    {
        return config(DustConfig.DUST_CLOUD_SIZE, 9) / 2;
    }

    // How much dust in the air a spark sets off
    public static int ignite()
    {
        return config(DustConfig.DUST_TO_EXPLODE, 500);
    }

    public static boolean explosions()
    {
        return !DustConfig.SPEC.isLoaded() || DustConfig.DUST_EXPLOSIONS.getAsBoolean();
    }

    private static int config(net.neoforged.neoforge.common.ModConfigSpec.IntValue value, int fallback)
    {
        return DustConfig.SPEC.isLoaded() ? value.getAsInt() : fallback;
    }

    private static double config(net.neoforged.neoforge.common.ModConfigSpec.DoubleValue value, double fallback)
    {
        return DustConfig.SPEC.isLoaded() ? value.getAsDouble() : fallback;
    }

    // ---- spilling ----

    // Pours up to amount of this element out of a tube's end at start: it falls to the ground and piles up there, no
    // higher than the tube's end (then the tube backs up). Returns how much landed (0: nowhere to land, as in water,
    // over the void or against a solid block, or the pile has reached the tube).
    public static int deposit(ServerLevel level, BlockPos start, DustElement element, int amount)
    {
        return deposit(level, start, element, amount, start.getY());
    }

    // The same, piling no higher than maxY. A little at a time (CHUNK), each bit landing on the column under start,
    // then sliding down to the lowest column beside it while that's SLIDE or more lower (as sand slumps), up to SPREAD
    // columns out from start: the pile spreads out into a mound rather than standing up as a block.
    public static int deposit(ServerLevel level, BlockPos start, DustElement element, int amount, int maxY)
    {
        int left = amount;
        while (left > 0) {
            Surface at = surface(level, start.getX(), start.getZ(), start.getY());
            if (at == null) break;
            for (int step = 0; step < 4 * SPREAD; step++) {
                Surface lowest = null;
                for (Direction side : Direction.Plane.HORIZONTAL) {
                    int x = at.x() + side.getStepX(), z = at.z() + side.getStepZ();
                    if (Math.max(Math.abs(x - start.getX()), Math.abs(z - start.getZ())) > SPREAD) continue;
                    // Dust slides sideways at its own height and down, never up over a wall
                    Surface beside = surface(level, x, z, Math.min(at.y(), start.getY()));
                    if (beside != null && (lowest == null || beside.height() < lowest.height())) lowest = beside;
                }
                if (lowest == null || at.height() - lowest.height() < SLIDE) break;
                at = lowest;
            }
            int moved = addAt(level, at, element, Math.min(left, CHUNK), maxY);
            if (moved <= 0) break;
            left -= moved;
        }
        return amount - left;
    }

    // Dust falls in this much at a time, and slides on to a column this much lower (a quarter block: two layers), up to
    // SPREAD columns from where it fell
    private static final int CHUNK = 5;
    private static final int SLIDE = DustPileBlockEntity.CAPACITY / 4;
    private static final int SPREAD = 3;

    // The top of a column: where dust poured in at fromY ends up (a pile at y holding total, or an empty space at y
    // with total 0), and how high that is in dust (height: y * CAPACITY + total)
    private record Surface(int x, int y, int z, int total)
    {
        int height() { return y * DustPileBlockEntity.CAPACITY + total; }
    }

    private static @Nullable Surface surface(ServerLevel level, int x, int z, int fromY)
    {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, fromY, z);
        for (int fell = 0; fell <= MAX_FALL; fell++, pos.move(0, -1, 0)) {
            if (!level.isLoaded(pos) || level.isOutsideBuildHeight(pos)) return null;
            if (level.getBlockEntity(pos) instanceof DustPileBlockEntity pile) return new Surface(x, pos.getY(), z, pile.total());
            BlockState state = level.getBlockState(pos);
            if (!state.canBeReplaced() || !state.getFluidState().isEmpty()) return null;
            if (DustPileBlock.supports(level, pos.below())) return new Surface(x, pos.getY(), z, 0);
        }
        return null;
    }

    // Adds dust to the top of a column: to its pile (a full one: a new pile on top), or a new pile in its empty space;
    // never above maxY
    private static int addAt(ServerLevel level, Surface at, DustElement element, int amount, int maxY)
    {
        BlockPos pos = new BlockPos(at.x(), at.y(), at.z());
        if (at.total() >= DustPileBlockEntity.CAPACITY) pos = pos.above();
        if (pos.getY() > maxY) return 0;
        if (level.getBlockEntity(pos) instanceof DustPileBlockEntity pile) return pile.add(element, amount);
        return placeNew(level, pos, element, amount);
    }

    // A new pile here, if there's room for one (an empty, dry space on something it can lie on)
    private static int placeNew(ServerLevel level, BlockPos pos, DustElement element, int amount)
    {
        if (!level.isLoaded(pos) || level.isOutsideBuildHeight(pos)) return 0;
        BlockState state = level.getBlockState(pos);
        if (!state.canBeReplaced() || !state.getFluidState().isEmpty() || state.getBlock() instanceof DustPileBlock) return 0;
        BlockState pile = DustBlockRegistry.DUST_PILE.get().defaultBlockState();
        if (!pile.canSurvive(level, pos) || !level.setBlock(pos, pile, Block.UPDATE_ALL)) return 0;
        return level.getBlockEntity(pos) instanceof DustPileBlockEntity placed ? placed.add(element, amount) : 0;
    }

    // ---- the cloud ----

    // The dust in the air at a place: how much, its colour (weighed by amount) and the piles it comes from
    public record Cloud(int total, int color, List<BlockPos> piles) {}

    public static Cloud density(Level level, BlockPos at)
    {
        int total = 0;
        long red = 0, green = 0, blue = 0;
        List<BlockPos> piles = new ArrayList<>();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        // The piles whose cloud reaches here: up to radius() away to the side, RISE above or below
        for (int dx = -radius(); dx <= radius(); dx++) {
            for (int dz = -radius(); dz <= radius(); dz++) {
                for (int dy = -RISE; dy <= RISE; dy++) {
                    pos.set(at.getX() + dx, at.getY() + dy, at.getZ() + dz);
                    if (!level.isLoaded(pos) || !(level.getBlockState(pos).getBlock() instanceof DustPileBlock)) continue;
                    if (!(level.getBlockEntity(pos) instanceof DustPileBlockEntity pile) || pile.total() <= 0 || !pile.isOpen()) continue;
                    int amount = pile.total(), c = pile.color();
                    total += amount;
                    red += (long) ((c >> 16) & 0xFF) * amount;
                    green += (long) ((c >> 8) & 0xFF) * amount;
                    blue += (long) (c & 0xFF) * amount;
                    piles.add(pos.immutable());
                }
            }
        }
        int color = total <= 0 ? 0xFFFFFF : (int) ((red / total) << 16 | (green / total) << 8 | (blue / total));
        return new Cloud(total, color, piles);
    }

    // ---- igniting ----

    // A pile's check (out in the open): any open flame, burning mob or exploding creeper in its cloud, where the air is
    // thick enough, sets it off
    public static void checkIgnition(ServerLevel level, BlockPos pile)
    {
        if (!explosions()) return;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        // At most MAX_FLAMES flames tried each check (a lava lake beside a pile has a great many)
        int flames = 0;
        for (int dx = -radius(); dx <= radius(); dx++) {
            for (int dz = -radius(); dz <= radius(); dz++) {
                for (int dy = -RISE; dy <= RISE && flames < MAX_FLAMES; dy++) {
                    pos.set(pile.getX() + dx, pile.getY() + dy, pile.getZ() + dz);
                    if (!level.isLoaded(pos) || !isFlame(level.getBlockState(pos))) continue;
                    flames++;
                    if (spark(level, pos.immutable(), null)) return;
                }
            }
        }
        AABB area = new AABB(pile.getX() - radius(), pile.getY() - 1, pile.getZ() - radius(), pile.getX() + radius() + 1, pile.getY() + RISE + 1, pile.getZ() + radius() + 1);
        for (Mob mob : level.getEntitiesOfClass(Mob.class, area, DustClouds::sparks)) {
            if (spark(level, mob.blockPosition(), mob)) return;
        }
    }

    // A mob that sets a cloud off: one on fire, or a creeper starting to explode (not one just standing there)
    private static boolean sparks(Mob mob)
    {
        if (!mob.isAlive()) return false;
        if (mob.isOnFire()) return true;
        return mob instanceof Creeper creeper && (creeper.getSwellDir() > 0 || creeper.isIgnited());
    }

    // An open flame: fire of any kind (vanilla's, soul fire, dust fire), a lit campfire, a lit Still firebox, lava
    public static boolean isFlame(BlockState state)
    {
        if (state.getBlock() instanceof BaseFireBlock) return true;
        if (state.is(BlockTags.CAMPFIRES) && state.hasProperty(CampfireBlock.LIT) && state.getValue(CampfireBlock.LIT)) return true;
        if (state.getBlock() instanceof StillFireboxBlock && state.getValue(StillFireboxBlock.LIT)) return true;
        return state.getFluidState().is(FluidTags.LAVA);
    }

    // A spark at a place (a spell, a struck flint and steel, a flame, a creeper): if the air there holds ignite() dust or
    // more, it goes up. Returns whether it did.
    public static boolean spark(Level level, Vec3 at, @Nullable Entity source)
    {
        return level instanceof ServerLevel server && spark(server, BlockPos.containing(at), source);
    }

    public static boolean spark(ServerLevel level, BlockPos at, @Nullable Entity source)
    {
        if (!explosions() || !level.isLoaded(at)) return false;
        Cloud cloud = density(level, at);
        if (cloud.total() < ignite()) return false;
        // The chain: the piles in the cloud, then every pile out in the open in reach of one already caught
        java.util.LinkedHashMap<BlockPos, DustPileBlockEntity> caught = new java.util.LinkedHashMap<>();
        java.util.ArrayDeque<BlockPos> queue = new java.util.ArrayDeque<>();
        for (BlockPos pile : cloud.piles()) {
            if (level.getBlockEntity(pile) instanceof DustPileBlockEntity entity) {
                caught.put(pile, entity);
                queue.add(pile);
            }
        }
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        while (!queue.isEmpty() && caught.size() < MAX_CHAIN) {
            BlockPos from = queue.poll();
            for (int dx = -radius(); dx <= radius(); dx++) {
                for (int dz = -radius(); dz <= radius(); dz++) {
                    for (int dy = -RISE; dy <= RISE; dy++) {
                        pos.set(from.getX() + dx, from.getY() + dy, from.getZ() + dz);
                        if (caught.containsKey(pos) || !level.isLoaded(pos) || !(level.getBlockState(pos).getBlock() instanceof DustPileBlock)) continue;
                        if (!(level.getBlockEntity(pos) instanceof DustPileBlockEntity entity) || entity.total() <= 0 || !entity.isOpen()) continue;
                        BlockPos found = pos.immutable();
                        caught.put(found, entity);
                        queue.add(found);
                    }
                }
            }
        }
        // Grouped a cloud's size at a time, each group one explosion at its middle
        List<BlockPos> left = new ArrayList<>(caught.keySet());
        List<Blast> blasts = new ArrayList<>();
        while (!left.isEmpty()) {
            BlockPos first = left.getFirst();
            int dust = 0;
            double x = 0, y = 0, z = 0;
            int count = 0;
            for (java.util.Iterator<BlockPos> it = left.iterator(); it.hasNext(); ) {
                BlockPos pile = it.next();
                if (Math.abs(pile.getX() - first.getX()) > radius() || Math.abs(pile.getZ() - first.getZ()) > radius() || Math.abs(pile.getY() - first.getY()) > RISE) continue;
                DustPileBlockEntity entity = caught.get(pile);
                dust += entity.total();
                x += pile.getX() + 0.5;
                y += pile.getY() + 0.5;
                z += pile.getZ() + 0.5;
                count++;
                // A flash of its colour as it burns
                DustParticleOptions flash = new DustParticleOptions(entity.color(), 2.0F);
                level.sendParticles(flash, pile.getX() + 0.5, pile.getY() + 1.0, pile.getZ() + 0.5, 12, 2.0, 1.0, 2.0, 0);
                level.sendParticles(ParticleTypes.FLAME, pile.getX() + 0.5, pile.getY() + 1.0, pile.getZ() + 0.5, 10, 2.0, 1.0, 2.0, 0.05);
                it.remove();
            }
            blasts.add(new Blast(x / count, y / count, z / count, power(dust)));
        }
        // Every pile goes before any blast, so none is blown apart without burning
        for (BlockPos pile : caught.keySet()) level.removeBlock(pile, false);
        boolean firstBlast = true;
        for (Blast blast : blasts) {
            SpellExplosions.explode(level, firstBlast ? source : null, blast.x(), blast.y(), blast.z(), blast.power(), Level.ExplosionInteraction.TNT);
            firstBlast = false;
        }
        return true;
    }

    private record Blast(double x, double y, double z, float power) {}

    public static float power(int dust)
    {
        double power = config(DustConfig.DUST_EXPLOSION_POWER, 8.0) * Math.sqrt(dust / (double) ignite());
        return (float) Math.min(config(DustConfig.DUST_EXPLOSION_MAX_POWER, 16.0), power);
    }
}
