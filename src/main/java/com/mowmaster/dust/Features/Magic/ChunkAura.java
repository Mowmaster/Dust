package com.mowmaster.dust.Features.Magic;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// The aura of dust each chunk holds, as the five primals (ChunkAuraData, attachment CHUNK_AURA).
//   Normal depends on the biome (baseline): BASELINE of each primal, with more of a biome's own primals (deserts Fire,
//   oceans Water, plains and forests Earth, snowy biomes Order and Chaos). A chunk nobody has touched stores nothing and
//   counts as exactly its normal.
//   Dust going back into the world (release: orbs nobody could carry, what an evaporation table boils off...) is broken
//   down into its primals and added. Things that feed on aura take from it (take).
//   It evens out: every UPDATE_TICKS, a chunk that isn't at normal shares SPREAD of the difference with each loaded
//   neighbour, and drifts DRIFT of the way back to normal. It takes days of steady change to move a chunk a long way.
//   Only chunks away from normal are worked on (the active set); one that was unloaded catches up on its drift when
//   it loads again.
// Richness: the chunk's total over its normal total (1 = normal). What it does at each level: AuraEffects.
// Amplification (Magic's block effect): an amplified block feels the richness further from normal, either way.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class ChunkAura
{
    public static final float BASELINE = 100;
    // A biome's own primal: this much instead of BASELINE (snowy biomes have two, a little less each)
    public static final float BIOME_PRIMAL = 200;
    public static final float BIOME_PAIR = 175;
    public static final int UPDATE_TICKS = 200;
    public static final float SPREAD = 0.01F;
    public static final float DRIFT = 0.002F;
    // Close enough to normal to stop working on it
    private static final float SETTLED = 0.05F;

    // Chunks away from normal, per dimension (chunk positions as longs)
    private static final Map<ResourceKey<Level>, Set<Long>> ACTIVE = new HashMap<>();

    private ChunkAura() {}

    private static Set<Long> active(Level level)
    {
        return ACTIVE.computeIfAbsent(level.dimension(), key -> new HashSet<>());
    }

    // ---- reading ----

    // A chunk's normal aura, from the biome at its middle (at sea level)
    public static float[] baseline(Level level, ChunkPos pos)
    {
        float[] values = new float[ChunkAuraData.COUNT];
        java.util.Arrays.fill(values, BASELINE);
        // Holder.is(tag): whether the biome is in a biome tag. c: tags (NeoForge's Tags.Biomes) cover modded biomes too.
        net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome> biome = level.getBiome(pos.getMiddleBlockPosition(level.getSeaLevel()));
        if (biome.is(net.neoforged.neoforge.common.Tags.Biomes.IS_DESERT)) values[EnumElement.FIRE.ordinal()] = BIOME_PRIMAL;
        if (biome.is(net.minecraft.tags.BiomeTags.IS_OCEAN)) values[EnumElement.WATER.ordinal()] = BIOME_PRIMAL;
        if (biome.is(net.neoforged.neoforge.common.Tags.Biomes.IS_PLAINS) || biome.is(net.minecraft.tags.BiomeTags.IS_FOREST)) {
            values[EnumElement.EARTH.ordinal()] = BIOME_PRIMAL;
        }
        if (biome.is(net.neoforged.neoforge.common.Tags.Biomes.IS_SNOWY)) {
            values[EnumElement.ORDER.ordinal()] = BIOME_PAIR;
            values[EnumElement.CHAOS.ordinal()] = BIOME_PAIR;
        }
        return values;
    }

    public static float baselineTotal(Level level, ChunkPos pos)
    {
        float total = 0;
        for (float value : baseline(level, pos)) total += value;
        return total;
    }

    // The chunk's aura (its normal, if it has none of its own)
    public static ChunkAuraData of(LevelChunk chunk)
    {
        ChunkAuraData data = chunk.getExistingDataOrNull(DustAttachmentTypeRegistry.CHUNK_AURA);
        return data != null ? data : new ChunkAuraData(baseline(chunk.getLevel(), chunk.getPos()), chunk.getLevel().getGameTime());
    }

    public static ChunkAuraData of(Level level, BlockPos pos)
    {
        return of(level.getChunkAt(pos));
    }

    // The chunk's richness: 1 normal, 2 twice as full, 0.5 half
    public static float richness(Level level, BlockPos pos)
    {
        return of(level, pos).total() / baselineTotal(level, ChunkPos.containing(pos));
    }

    // The richness as felt at one block: amplified blocks feel it further from normal
    public static float richnessAt(ServerLevel level, BlockPos pos)
    {
        float richness = richness(level, pos);
        return Math.max(0, 1 + (richness - 1) * amplification(level, pos));
    }

    // How fast corruption spreads here: as fast as normal at normal richness and above, slower the thinner it is
    // (down to a tenth)
    public static float spreadFactor(ServerLevel level, BlockPos pos)
    {
        return Math.max(0.1F, Math.min(1.0F, richnessAt(level, pos)));
    }

    // ---- writing ----

    private static void set(LevelChunk chunk, float[] primals)
    {
        for (int i = 0; i < primals.length; i++) primals[i] = Math.max(0, primals[i]);
        chunk.setData(DustAttachmentTypeRegistry.CHUNK_AURA, new ChunkAuraData(primals, chunk.getLevel().getGameTime()));
        chunk.markUnsaved();
        active(chunk.getLevel()).add(chunk.getPos().pack());
    }

    // Adds (or with a negative amount, takes) some of one primal
    public static void add(Level level, BlockPos pos, EnumElement primal, float amount)
    {
        if (amount == 0) return;
        LevelChunk chunk = level.getChunkAt(pos);
        float[] primals = of(chunk).copy();
        primals[primal.ordinal()] += amount;
        set(chunk, primals);
    }

    // Dust going back into the world at pos (orbs the player had no room for, evaporation...): broken down into the
    // primals it's made of, amount times over, and added to that chunk
    public static void release(ServerLevel level, BlockPos pos, DustElement element, int amount)
    {
        if (amount <= 0) return;
        LevelChunk chunk = level.getChunkAt(pos);
        float[] primals = of(chunk).copy();
        for (EnumElement primal : EnumElement.values()) primals[primal.ordinal()] += element.primal(primal) * amount;
        set(chunk, primals);
    }

    // Takes up to amount from the chunk, spread over the primals by how much of each it has. Returns how much it got.
    public static float take(Level level, BlockPos pos, float amount)
    {
        LevelChunk chunk = level.getChunkAt(pos);
        ChunkAuraData data = of(chunk);
        float total = data.total();
        if (total <= 0 || amount <= 0) return 0;
        float taken = Math.min(amount, total);
        float[] primals = data.copy();
        for (int i = 0; i < primals.length; i++) primals[i] -= taken * primals[i] / total;
        set(chunk, primals);
        return taken;
    }

    // Sets every primal of the chunk to the same amount (commands)
    public static void setAll(Level level, BlockPos pos, float each)
    {
        float[] primals = new float[ChunkAuraData.COUNT];
        java.util.Arrays.fill(primals, each);
        set(level.getChunkAt(pos), primals);
    }

    // ---- evening out ----

    // Drift back toward its normal for the time since it was last updated: DRIFT a step, compounded
    private static float[] drifted(ChunkAuraData data, float[] normal, long now)
    {
        float[] primals = data.copy();
        long steps = Math.max(0, (now - data.updated()) / UPDATE_TICKS);
        if (steps == 0) return primals;
        float keep = (float) Math.pow(1 - DRIFT, steps);
        for (int i = 0; i < primals.length; i++) primals[i] = normal[i] + (primals[i] - normal[i]) * keep;
        return primals;
    }

    private static boolean settled(float[] primals, float[] normal)
    {
        for (int i = 0; i < primals.length; i++) if (Math.abs(primals[i] - normal[i]) > SETTLED) return false;
        return true;
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event)
    {
        if (!(event.getLevel() instanceof ServerLevel level) || level.getGameTime() % UPDATE_TICKS != 0) return;
        Set<Long> active = active(level);
        if (active.isEmpty()) return;
        long now = level.getGameTime();
        for (long packed : List.copyOf(active)) {
            ChunkPos pos = ChunkPos.unpack(packed);
            LevelChunk chunk = level.getChunkSource().getChunkNow(pos.x(), pos.z());
            if (chunk == null) {
                active.remove(packed);
                continue;
            }
            float[] normal = baseline(level, pos);
            float[] primals = drifted(of(chunk), normal, now);
            // Share with each loaded neighbour: SPREAD of the difference moves across
            for (int[] step : new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
                LevelChunk next = level.getChunkSource().getChunkNow(pos.x() + step[0], pos.z() + step[1]);
                if (next == null) continue;
                float[] theirs = drifted(of(next), baseline(level, next.getPos()), now);
                boolean moved = false;
                for (int i = 0; i < primals.length; i++) {
                    float flow = (primals[i] - theirs[i]) * SPREAD;
                    if (Math.abs(flow) < 0.0001F) continue;
                    primals[i] -= flow;
                    theirs[i] += flow;
                    moved = true;
                }
                if (moved) set(next, theirs);
            }
            if (settled(primals, normal)) {
                chunk.removeData(DustAttachmentTypeRegistry.CHUNK_AURA);
                chunk.markUnsaved();
                active.remove(packed);
            } else {
                set(chunk, primals);
                AuraEffects.chunkTick(level, chunk, richness(level, pos.getMiddleBlockPosition(64)));
            }
        }
    }

    // A chunk with aura of its own loads: it catches up on its drift and joins the active set
    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event)
    {
        if (!(event.getLevel() instanceof ServerLevel) || !(event.getChunk() instanceof LevelChunk chunk)) return;
        if (chunk.hasData(DustAttachmentTypeRegistry.CHUNK_AURA)) active(chunk.getLevel()).add(chunk.getPos().pack());
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event)
    {
        if (event.getLevel() instanceof ServerLevel level) active(level).remove(event.getChunk().getPos().pack());
    }

    // ---- amplification (Magic's block effect) ----

    // How much aura effects at pos are amplified: 1 normally, more where Magic has amplified the block
    public static float amplification(ServerLevel level, BlockPos pos)
    {
        AuraAmplifiers.Amplifier amplifier = level.getChunkAt(pos).getData(DustAttachmentTypeRegistry.AURA_AMPLIFIERS).blocks().get(pos);
        return amplifier == null || amplifier.until() < level.getGameTime() ? 1.0F : amplifier.factor();
    }

    // Amplifies aura effects at pos by factor for ticks (a stronger one replaces a weaker one; recasting renews it)
    public static void amplify(ServerLevel level, BlockPos pos, float factor, int ticks)
    {
        LevelChunk chunk = level.getChunkAt(pos);
        AuraAmplifiers current = chunk.getData(DustAttachmentTypeRegistry.AURA_AMPLIFIERS);
        AuraAmplifiers.Amplifier old = current.blocks().get(pos);
        if (old != null && old.until() >= level.getGameTime()) factor = Math.max(factor, old.factor());
        chunk.setData(DustAttachmentTypeRegistry.AURA_AMPLIFIERS, current.with(pos, new AuraAmplifiers.Amplifier(factor, level.getGameTime() + ticks), level.getGameTime()));
        chunk.markUnsaved();
    }

    // Used by the commands
    public static @Nullable String describe(float richness)
    {
        return AuraEffects.stateOf(richness);
    }
}
