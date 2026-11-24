package com.mowmaster.dust.Features.Magic;

import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

// The primals a biome is innately rich in: how much of each its chunks hold when nobody has touched them (their normal
// aura, ChunkAura.baseline), indexed by EnumElement#ordinal. Every primal starts at ChunkAura.BASELINE; a biome's own
// primals have more (RICH, or PAIR when a biome has two). The richest tag wins for each primal.
//   Fire          deserts, badlands (RICH); savannas (PAIR)
//   Water         oceans (RICH); rivers, beaches (PAIR)
//   Earth         plains, forests (RICH); taigas (PAIR)
//   Order, Chaos  snowy biomes (both PAIR, as before)
//   Order         mountain peaks and slopes (PAIR)
//   Chaos         spooky biomes: dark forests and the like (PAIR)
//   pairs         jungles and lush caves Earth + Water; swamps Water + Chaos; mushroom fields Earth + Chaos
// What grows where follows these: element geodes, flowers and trees (Features/Worldgen) pick their element from the
// biome's rich primals, so a desert's geodes are Fire's family, a snowy biome's the ones that don't fit Fire, Water or
// Earth (where the white and black crystal ores form).
public final class BiomeElements
{
    public static final float RICH = 200;
    public static final float PAIR = 175;

    private BiomeElements() {}

    public static float[] innate(Holder<Biome> biome)
    {
        float[] values = new float[EnumElement.values().length];
        java.util.Arrays.fill(values, ChunkAura.BASELINE);
        rich(values, biome, Tags.Biomes.IS_DESERT, RICH, EnumElement.FIRE);
        rich(values, biome, Tags.Biomes.IS_BADLANDS, RICH, EnumElement.FIRE);
        rich(values, biome, Tags.Biomes.IS_SAVANNA, PAIR, EnumElement.FIRE);
        rich(values, biome, BiomeTags.IS_OCEAN, RICH, EnumElement.WATER);
        rich(values, biome, Tags.Biomes.IS_RIVER, PAIR, EnumElement.WATER);
        rich(values, biome, Tags.Biomes.IS_BEACH, PAIR, EnumElement.WATER);
        rich(values, biome, Tags.Biomes.IS_PLAINS, RICH, EnumElement.EARTH);
        rich(values, biome, BiomeTags.IS_FOREST, RICH, EnumElement.EARTH);
        rich(values, biome, Tags.Biomes.IS_TAIGA, PAIR, EnumElement.EARTH);
        rich(values, biome, Tags.Biomes.IS_SNOWY, PAIR, EnumElement.ORDER, EnumElement.CHAOS);
        rich(values, biome, Tags.Biomes.IS_MOUNTAIN_PEAK, PAIR, EnumElement.ORDER);
        rich(values, biome, Tags.Biomes.IS_MOUNTAIN_SLOPE, PAIR, EnumElement.ORDER);
        rich(values, biome, Tags.Biomes.IS_SPOOKY, PAIR, EnumElement.CHAOS);
        rich(values, biome, Tags.Biomes.IS_JUNGLE, PAIR, EnumElement.EARTH, EnumElement.WATER);
        rich(values, biome, Tags.Biomes.IS_LUSH, PAIR, EnumElement.EARTH, EnumElement.WATER);
        rich(values, biome, Tags.Biomes.IS_SWAMP, PAIR, EnumElement.WATER, EnumElement.CHAOS);
        rich(values, biome, Tags.Biomes.IS_MUSHROOM, PAIR, EnumElement.EARTH, EnumElement.CHAOS);
        return values;
    }

    private static void rich(float[] values, Holder<Biome> biome, TagKey<Biome> tag, float amount, EnumElement... primals)
    {
        if (!biome.is(tag)) return;
        for (EnumElement primal : primals) values[primal.ordinal()] = Math.max(values[primal.ordinal()], amount);
    }

    // How much richer than ordinary the biome is in this primal (0: not at all)
    public static float richness(Holder<Biome> biome, EnumElement primal)
    {
        return Math.max(0, innate(biome)[primal.ordinal()] - ChunkAura.BASELINE);
    }

    // Whether the biome is innately rich in this primal
    public static boolean isRich(Holder<Biome> biome, EnumElement primal)
    {
        return richness(biome, primal) > 0;
    }
}
