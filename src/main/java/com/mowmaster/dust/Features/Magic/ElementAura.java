package com.mowmaster.dust.Features.Magic;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

// The chunk's aura (ChunkAura) as one element feels it: through its primals, each counted as many times as the element
// holds it (DustElement.primal: Light is 2 Fire and 1 Water). Used by what feeds on the aura or crystallizes it: budding
// crystals, elemental flowers, magical fruit.
public final class ElementAura
{
    private ElementAura() {}

    // How full the chunk is of the element's primals against its normal: 1 normal, 2 twice as full, 0.5 half
    public static float fullness(Level level, BlockPos pos, DustElement element)
    {
        ChunkAuraData aura = ChunkAura.of(level, pos);
        float[] normal = ChunkAura.baseline(level, ChunkPos.containing(pos));
        float sum = 0;
        int parts = 0;
        for (EnumElement primal : EnumElement.values()) {
            int count = element.primal(primal);
            if (count <= 0) continue;
            sum += count * aura.get(primal) / Math.max(1, normal[primal.ordinal()]);
            parts += count;
        }
        return parts == 0 ? 1 : sum / parts;
    }

    // How full the chunk is of one primal against its normal
    public static float fullness(Level level, BlockPos pos, EnumElement primal)
    {
        float normal = ChunkAura.baseline(level, ChunkPos.containing(pos))[primal.ordinal()];
        return ChunkAura.of(level, pos).get(primal) / Math.max(1, normal);
    }

    // Whether the chunk holds more of this primal than its normal
    public static boolean overNormal(Level level, BlockPos pos, EnumElement primal)
    {
        return fullness(level, pos, primal) > 1.0F;
    }

    // Takes this much of the element's dust out of the aura, split between its primals as the element is made of them
    public static void take(Level level, BlockPos pos, DustElement element, float amount)
    {
        int parts = 0;
        for (EnumElement primal : EnumElement.values()) parts += element.primal(primal);
        if (parts <= 0 || amount <= 0) return;
        for (EnumElement primal : EnumElement.values()) {
            int count = element.primal(primal);
            if (count > 0) ChunkAura.add(level, pos, primal, -amount * count / parts);
        }
    }

    // The primals the element is made of (each once)
    public static java.util.List<EnumElement> primalsOf(DustElement element)
    {
        java.util.List<EnumElement> out = new java.util.ArrayList<>();
        for (EnumElement primal : EnumElement.values()) if (element.primal(primal) > 0) out.add(primal);
        return out;
    }
}
