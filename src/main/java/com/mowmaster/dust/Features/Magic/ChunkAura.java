package com.mowmaster.dust.Features.Magic;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

// The aura of dust each chunk holds, as the five primals, slowly evened out with the chunks around it. Not built yet:
// for now dust released into it is simply gone. Everything that should feed it already calls release, so it'll start
// working everywhere at once when it exists.
public final class ChunkAura
{
    private ChunkAura() {}

    // How fast corruption spreads here (1 normally; slower where the aura is low, once aura exists)
    public static float spreadFactor(ServerLevel level, BlockPos pos)
    {
        // TODO chunk aura: scale by how full the chunk's aura is
        return 1.0F;
    }

    // Dust going back into the world at pos (orbs the player had no room for, spell leftovers...): it'll be broken
    // down into the primals it's made of and added to that chunk's aura
    public static void release(ServerLevel level, BlockPos pos, DustElement element, int amount)
    {
        if (amount <= 0) return;
        // TODO chunk aura: add element.primal(p) * amount of each primal to the chunk at pos
    }
}
