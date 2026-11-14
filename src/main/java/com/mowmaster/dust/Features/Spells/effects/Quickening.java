package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// Motion's block effect: a machine works faster for a while (the spell's duration x DURATION_SCALE: a minute at the
// default). Any block whose block entity ticks counts (furnaces, smokers, brewing stands, hoppers, other mods'
// machines): it's simply ticked extra times each tick, one more per point of potency. Kept in memory only (like the
// other short marks): a quickened machine goes back to normal when the server stops.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class Quickening
{
    public static final int DURATION_SCALE = 12;

    // Which machine: its dimension and position
    private record Key(ResourceKey<Level> dimension, BlockPos pos) {}
    // Until when (game time), and how many extra ticks a tick
    private record Boost(long until, int extra) {}

    private static final Map<Key, Boost> QUICKENED = new HashMap<>();

    private Quickening() {}

    public static void quicken(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        BlockEntity machine = level.getBlockEntity(pos);
        if (machine == null || ticker(level, pos, level.getBlockState(pos), machine) == null) return;
        int extra = Math.max(1, Math.round(cast.stats().potency()));
        QUICKENED.put(new Key(level.dimension(), pos.immutable()), new Boost(level.getGameTime() + (long) cast.stats().duration() * DURATION_SCALE, extra));
        level.sendParticles(ParticleTypes.ELECTRIC_SPARK, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, 12, 0.35, 0.35, 0.35, 0.05);
    }

    // After each world tick: every quickened machine there ticks its extra times (or is forgotten once its time is up,
    // or it's gone)
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event)
    {
        if (!(event.getLevel() instanceof ServerLevel level) || QUICKENED.isEmpty()) return;
        long now = level.getGameTime();
        Iterator<Map.Entry<Key, Boost>> entries = QUICKENED.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Key, Boost> entry = entries.next();
            if (entry.getKey().dimension() != level.dimension()) continue;
            BlockPos pos = entry.getKey().pos();
            if (now > entry.getValue().until()) {
                entries.remove();
                continue;
            }
            // Not loaded: it waits (its time still runs out)
            if (!level.isLoaded(pos)) continue;
            BlockEntity machine = level.getBlockEntity(pos);
            BlockState state = level.getBlockState(pos);
            BlockEntityTicker<BlockEntity> ticker = machine == null ? null : ticker(level, pos, state, machine);
            if (ticker == null) {
                entries.remove();
                continue;
            }
            for (int i = 0; i < entry.getValue().extra(); i++) ticker.tick(level, pos, state, machine);
            if (now % 10 == 0) level.sendParticles(ParticleTypes.ELECTRIC_SPARK, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 1, 0.3, 0.1, 0.3, 0);
        }
    }

    // The block's own ticker for its block entity (what the game runs once a tick), or null if it doesn't tick.
    // getTicker is generic in the block entity's class; the cast is safe because the type came from this very block
    // entity (@SuppressWarnings quiets the compiler's warning about it).
    @SuppressWarnings("unchecked")
    private static BlockEntityTicker<BlockEntity> ticker(Level level, BlockPos pos, BlockState state, BlockEntity machine)
    {
        return state.getTicker(level, (BlockEntityType<BlockEntity>) machine.getType());
    }
}
