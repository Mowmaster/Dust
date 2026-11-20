package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.Features.Apparatus.still.StillFireboxBlock;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.effects.SpellExplosions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
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
//   spilling  deposit: dust poured out of an open tube end falls to the ground and lands in a pile there (onto a pile
//             already there, of any elements, until it's full, then on top of it)
//   the cloud each pile out in the open (DustPileBlockEntity.isOpen) fills a 9x9 area round it (RADIUS), from a block
//             under it to RISE blocks over it, with its dust. How thick the air is at a place (density) is all the dust
//             of the piles whose cloud reaches it: the thicker, the harder to see through (Client/DustCloudClient: half
//             at IGNITE)
//   igniting  where the air holds IGNITE dust or more, a spark sets it off (spark): an open flame (fire, dust fire, a lit
//             campfire or firebox, lava) or a creeper in the cloud (checked by the piles: DustPileBlockEntity), a spell
//             cast or landing there, a Magical Flint and Steel struck there. Every pile whose cloud reaches the spark
//             burns up at once, and with it every pile in reach of one that burns (their clouds touch), on and on: a
//             chain. They go off as spell explosions (SpellExplosions) that break and drop blocks: one for each group of
//             piles near together (a cloud's size), its power growing with that group's dust (power).
public final class DustClouds
{
    public static final int RADIUS = 4;
    public static final int RISE = 3;
    public static final int IGNITE = 500;
    // Explosion power: BASE_POWER (twice TNT's) at IGNITE dust, growing with the square root of the dust, at most
    // MAX_POWER
    private static final float BASE_POWER = 8F;
    private static final float MAX_POWER = 16F;
    // The most piles one chain sets off
    private static final int MAX_CHAIN = 512;
    // How far dust falls from a tube's end looking for somewhere to land (blocks)
    private static final int MAX_FALL = 24;
    private static final int MAX_FLAMES = 4;

    private DustClouds() {}

    // ---- spilling ----

    // Pours up to amount of this element out at start: it falls to the ground and piles up there. Returns how much
    // landed (0: nowhere to land, as in water, over the void or against a solid block).
    public static int deposit(ServerLevel level, BlockPos start, DustElement element, int amount)
    {
        BlockPos.MutableBlockPos at = start.mutable();
        int left = amount;
        for (int step = 0; step <= MAX_FALL && left > 0; step++) {
            if (!level.isLoaded(at) || level.isOutsideBuildHeight(at)) break;
            BlockState state = level.getBlockState(at);
            if (level.getBlockEntity(at) instanceof DustPileBlockEntity pile) {
                left -= pile.add(element, left);
                // A full pile: the rest lands on top of it
                if (!pile.isFull()) break;
                at.move(0, 1, 0);
                continue;
            }
            if (!state.canBeReplaced() || !state.getFluidState().isEmpty()) break;
            if (DustPileBlock.supports(level, at.below())) {
                left -= placeNew(level, at.immutable(), element, left);
                break;
            }
            at.move(0, -1, 0);
        }
        return amount - left;
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
        // The piles whose cloud reaches here: up to RADIUS away to the side, from RISE below to one above
        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                for (int dy = -RISE; dy <= 1; dy++) {
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

    // A pile's check (out in the open): any open flame or creeper in its cloud, where the air is thick enough, sets it off
    public static void checkIgnition(ServerLevel level, BlockPos pile)
    {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        // At most MAX_FLAMES flames tried each check (a lava lake beside a pile has a great many)
        int flames = 0;
        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                for (int dy = -1; dy <= RISE && flames < MAX_FLAMES; dy++) {
                    pos.set(pile.getX() + dx, pile.getY() + dy, pile.getZ() + dz);
                    if (!level.isLoaded(pos) || !isFlame(level.getBlockState(pos))) continue;
                    flames++;
                    if (spark(level, pos.immutable(), null)) return;
                }
            }
        }
        AABB area = new AABB(pile.getX() - RADIUS, pile.getY() - 1, pile.getZ() - RADIUS, pile.getX() + RADIUS + 1, pile.getY() + RISE + 1, pile.getZ() + RADIUS + 1);
        for (Creeper creeper : level.getEntitiesOfClass(Creeper.class, area, Entity::isAlive)) {
            if (spark(level, creeper.blockPosition(), creeper)) return;
        }
    }

    // An open flame: fire of any kind (vanilla's, soul fire, dust fire), a lit campfire, a lit Still firebox, lava
    public static boolean isFlame(BlockState state)
    {
        if (state.getBlock() instanceof BaseFireBlock) return true;
        if (state.is(BlockTags.CAMPFIRES) && state.hasProperty(CampfireBlock.LIT) && state.getValue(CampfireBlock.LIT)) return true;
        if (state.getBlock() instanceof StillFireboxBlock && state.getValue(StillFireboxBlock.LIT)) return true;
        return state.getFluidState().is(FluidTags.LAVA);
    }

    // A spark at a place (a spell, a struck flint and steel, a flame, a creeper): if the air there holds IGNITE dust or
    // more, it goes up. Returns whether it did.
    public static boolean spark(Level level, Vec3 at, @Nullable Entity source)
    {
        return level instanceof ServerLevel server && spark(server, BlockPos.containing(at), source);
    }

    public static boolean spark(ServerLevel level, BlockPos at, @Nullable Entity source)
    {
        if (!level.isLoaded(at)) return false;
        Cloud cloud = density(level, at);
        if (cloud.total() < IGNITE) return false;
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
            for (int dx = -RADIUS; dx <= RADIUS; dx++) {
                for (int dz = -RADIUS; dz <= RADIUS; dz++) {
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
                if (Math.abs(pile.getX() - first.getX()) > RADIUS || Math.abs(pile.getZ() - first.getZ()) > RADIUS || Math.abs(pile.getY() - first.getY()) > RISE) continue;
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
        return Math.min(MAX_POWER, BASE_POWER * (float) Math.sqrt(dust / (double) IGNITE));
    }
}
