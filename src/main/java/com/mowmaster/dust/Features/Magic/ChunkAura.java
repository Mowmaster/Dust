package com.mowmaster.dust.Features.Magic;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;

// The aura of dust each chunk holds, as the five primals, slowly evened out with the chunks around it. Not built yet:
// for now dust released into it is simply gone. Everything that should feed it already calls release, so it'll start
// working everywhere at once when it exists.
// Amplification (Magic's block effect): an aura effect at an amplified block is that many times as strong, good or
// bad. Every aura effect should go through amplification(level, pos); corruption spread already does.
public final class ChunkAura
{
    private ChunkAura() {}

    // How fast corruption spreads here (1 normally; slower where the aura is low, once aura exists)
    public static float spreadFactor(ServerLevel level, BlockPos pos)
    {
        // TODO chunk aura: scale by how full the chunk's aura is
        return 1.0F * amplification(level, pos);
    }

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

    // Dust going back into the world at pos (orbs the player had no room for, spell leftovers...): it'll be broken
    // down into the primals it's made of and added to that chunk's aura
    public static void release(ServerLevel level, BlockPos pos, DustElement element, int amount)
    {
        if (amount <= 0) return;
        // TODO chunk aura: add element.primal(p) * amount of each primal to the chunk at pos
    }
}
