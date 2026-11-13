package com.mowmaster.dust.Features.Spells.soul;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustReferences;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.List;

// Soul-bound blocks that were broken and are waiting to come back, per dimension (data/dust_soul_blocks.dat in each
// dimension's folder). Checked once a second; a block comes back when its time is up, its chunk is loaded and its space
// is free (if something's in the way, it tries again a little later). Block entity contents don't come back.
public class SoulBlockRespawns extends SavedData
{
    public record Pending(BlockPos pos, BlockState state, long at)
    {
        public static final Codec<Pending> CODEC = RecordCodecBuilder.create(i -> i.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(Pending::pos),
                BlockState.CODEC.fieldOf("state").forGetter(Pending::state),
                Codec.LONG.fieldOf("at").forGetter(Pending::at)
        ).apply(i, Pending::new));
    }

    private static final Codec<SoulBlockRespawns> CODEC = Pending.CODEC.listOf().xmap(SoulBlockRespawns::new, data -> data.pending);

    private static final SavedDataType<SoulBlockRespawns> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(DustReferences.MODID, "soul_blocks"), SoulBlockRespawns::new, CODEC);

    // Blocked: try again this much later
    private static final long RETRY_TICKS = 20 * 10;

    private final List<Pending> pending;

    public SoulBlockRespawns()
    {
        this(List.of());
    }

    private SoulBlockRespawns(List<Pending> pending)
    {
        this.pending = new ArrayList<>(pending);
    }

    // This dimension's own store (a ServerLevel's data storage is per dimension, unlike the server's)
    public static SoulBlockRespawns get(ServerLevel level)
    {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    public void schedule(BlockPos pos, BlockState state, long at)
    {
        pending.add(new Pending(pos.immutable(), state, at));
        setDirty();
    }

    public void tick(ServerLevel level)
    {
        if (pending.isEmpty()) return;
        long now = level.getGameTime();
        // Iterate over a copy, so the list can be changed inside the loop
        for (Pending entry : List.copyOf(pending)) {
            if (entry.at() > now || !level.isLoaded(entry.pos())) continue;
            pending.remove(entry);
            BlockPos pos = entry.pos();
            if (level.getBlockState(pos).canBeReplaced() && entry.state().canSurvive(level, pos)) {
                level.setBlockAndUpdate(pos, entry.state());
                level.sendParticles(ParticleTypes.SOUL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 12, 0.35, 0.35, 0.35, 0.02);
                level.playSound(null, pos, SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
            } else {
                pending.add(new Pending(pos, entry.state(), now + RETRY_TICKS));
            }
            setDirty();
        }
    }
}
