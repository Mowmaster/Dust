package com.mowmaster.dust.Features.Spells.blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// A chunk's sticky faces (Slime's block effect: Sticking), kept on the chunk (attachment STICKY_FACES) and synced to
// clients so they can draw the slime patches. By position: the block that was made sticky (a different block put there
// later isn't sticky) and which of its faces are, as bits (1 << Direction.get3DDataValue(): down, up, north, south,
// west, east).
public record StickyFaces(Map<BlockPos, Entry> blocks)
{
    public record Entry(Block block, int faces)
    {
        public boolean has(Direction face)
        {
            return (faces & bit(face)) != 0;
        }

        static final Codec<Entry> CODEC = RecordCodecBuilder.create(i -> i.group(
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(Entry::block),
                Codec.INT.fieldOf("faces").forGetter(Entry::faces)
        ).apply(i, Entry::new));

        // ByteBufCodecs.registry: a registry entry sent by its network id
        static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.registry(Registries.BLOCK), Entry::block,
                ByteBufCodecs.VAR_INT, Entry::faces,
                Entry::new);
    }

    public static int bit(Direction face)
    {
        return 1 << face.get3DDataValue();
    }

    // Saved and sent as a list of (pos, entry): map keys in codecs have to be text
    private record Pair(BlockPos pos, Entry entry)
    {
        static final Codec<Pair> CODEC = RecordCodecBuilder.create(i -> i.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(Pair::pos),
                Entry.CODEC.fieldOf("entry").forGetter(Pair::entry)
        ).apply(i, Pair::new));

        static final StreamCodec<RegistryFriendlyByteBuf, Pair> STREAM_CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, Pair::pos,
                Entry.STREAM_CODEC, Pair::entry,
                Pair::new);
    }

    private static StickyFaces fromPairs(List<Pair> pairs)
    {
        Map<BlockPos, Entry> blocks = new HashMap<>();
        pairs.forEach(pair -> blocks.put(pair.pos(), pair.entry()));
        return new StickyFaces(blocks);
    }

    private List<Pair> toPairs()
    {
        return blocks.entrySet().stream().map(e -> new Pair(e.getKey(), e.getValue())).toList();
    }

    public static final Codec<StickyFaces> CODEC = Pair.CODEC.listOf().xmap(StickyFaces::fromPairs, StickyFaces::toPairs);
    // .apply(ByteBufCodecs.list()): a codec for one thing turned into one for a list of them
    public static final StreamCodec<RegistryFriendlyByteBuf, StickyFaces> STREAM_CODEC =
            Pair.STREAM_CODEC.apply(ByteBufCodecs.list()).map(StickyFaces::fromPairs, StickyFaces::toPairs);

    public static final StickyFaces NONE = new StickyFaces(Map.of());
}
