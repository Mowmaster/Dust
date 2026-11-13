package com.mowmaster.dust.Features.Spells.conversions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.Optional;

// One rule in a spell conversion table (SpellConversions). Every field is optional; which ones are set decides what
// kind of rule it is:
//   from + to          the block becomes one of to (random if several; a "#tag" picks one of its blocks)
//   from + drops       it drops these items instead (and is removed, unless "to" says what's left)
//   to_deep            what to becomes below y 0 instead (deepslate ores)
//   chance / fail      it only works this often (0-1); otherwise it becomes fail (e.g. "minecraft:air"), or stays
//   near_fluid         only next to this fluid (a fluid tag: "minecraft:water")
//   source_only        only a source block of a fluid (still water, not flowing)
//   spread             the block (from) spreads onto any of these blocks next to it (grass onto dirt)
//   cycle              a block in this list becomes the next one in it (the last back to the first)
// Properties with the same name (facing, waterlogged...) carry over to the new block.
public record BlockConversion(Optional<BlockRef> from, List<BlockRef> to, Optional<BlockRef> toDeep, List<Drop> drops, float chance,
                              Optional<BlockRef> fail, Optional<TagKey<Fluid>> nearFluid, boolean sourceOnly, List<BlockRef> spread,
                              List<BlockRef> cycle)
{
    // An item dropped, and how many
    public record Drop(Item item, int count)
    {
        public static final Codec<Drop> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(Drop::item),
                Codec.INT.optionalFieldOf("count", 1).forGetter(Drop::count)
        ).apply(instance, Drop::new));
    }

    // compactListCodec: a single value or a list of them ("to": "minecraft:stone" or "to": ["a", "b"])
    public static final Codec<BlockConversion> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockRef.CODEC.optionalFieldOf("from").forGetter(BlockConversion::from),
            ExtraCodecs.compactListCodec(BlockRef.CODEC).optionalFieldOf("to", List.of()).forGetter(BlockConversion::to),
            BlockRef.CODEC.optionalFieldOf("to_deep").forGetter(BlockConversion::toDeep),
            Drop.CODEC.listOf().optionalFieldOf("drops", List.of()).forGetter(BlockConversion::drops),
            Codec.floatRange(0, 1).optionalFieldOf("chance", 1F).forGetter(BlockConversion::chance),
            BlockRef.CODEC.optionalFieldOf("fail").forGetter(BlockConversion::fail),
            TagKey.codec(Registries.FLUID).optionalFieldOf("near_fluid").forGetter(BlockConversion::nearFluid),
            Codec.BOOL.optionalFieldOf("source_only", false).forGetter(BlockConversion::sourceOnly),
            BlockRef.CODEC.listOf().optionalFieldOf("spread", List.of()).forGetter(BlockConversion::spread),
            BlockRef.CODEC.listOf().optionalFieldOf("cycle", List.of()).forGetter(BlockConversion::cycle)
    ).apply(instance, BlockConversion::new));

    // ---- building rules (for datagen) ----

    public static BlockConversion convert(BlockRef from, BlockRef to)
    {
        return new BlockConversion(Optional.of(from), List.of(to), Optional.empty(), List.of(), 1, Optional.empty(), Optional.empty(), false, List.of(), List.of());
    }

    public static BlockConversion cycleOf(List<BlockRef> cycle)
    {
        return new BlockConversion(Optional.empty(), List.of(), Optional.empty(), List.of(), 1, Optional.empty(), Optional.empty(), false, List.of(), cycle);
    }

    public static BlockConversion spreadOnto(BlockRef from, List<BlockRef> onto)
    {
        return new BlockConversion(Optional.of(from), List.of(), Optional.empty(), List.of(), 1, Optional.empty(), Optional.empty(), false, onto, List.of());
    }

    public static BlockConversion dropping(BlockRef from, List<Drop> drops)
    {
        return new BlockConversion(Optional.of(from), List.of(), Optional.empty(), drops, 1, Optional.empty(), Optional.empty(), false, List.of(), List.of());
    }

    public BlockConversion to(List<BlockRef> to) { return new BlockConversion(from, to, toDeep, drops, chance, fail, nearFluid, sourceOnly, spread, cycle); }
    public BlockConversion deep(BlockRef deep) { return new BlockConversion(from, to, Optional.of(deep), drops, chance, fail, nearFluid, sourceOnly, spread, cycle); }
    public BlockConversion chance(float chance, BlockRef fail) { return new BlockConversion(from, to, toDeep, drops, chance, Optional.of(fail), nearFluid, sourceOnly, spread, cycle); }
    public BlockConversion near(TagKey<Fluid> fluid) { return new BlockConversion(from, to, toDeep, drops, chance, fail, Optional.of(fluid), sourceOnly, spread, cycle); }
    public BlockConversion onlySources() { return new BlockConversion(from, to, toDeep, drops, chance, fail, nearFluid, true, spread, cycle); }
}
