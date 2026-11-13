package com.mowmaster.dust.Features.Spells.conversions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.List;

// A block in a conversion table, or a block tag when it starts with '#': "minecraft:gravel", "#minecraft:saplings".
// As a "from", a tag matches any block in it; as a "to", a tag picks one of its blocks at random.
public record BlockRef(@Nullable Block block, @Nullable TagKey<Block> tag)
{
    public static final Codec<BlockRef> CODEC = Codec.STRING.comapFlatMap(BlockRef::parse, BlockRef::toString);

    public static BlockRef of(Block block)
    {
        return new BlockRef(block, null);
    }

    public static BlockRef of(TagKey<Block> tag)
    {
        return new BlockRef(null, tag);
    }

    private static DataResult<BlockRef> parse(String text)
    {
        if (text.startsWith("#")) {
            Identifier id = Identifier.tryParse(text.substring(1));
            return id == null ? DataResult.error(() -> "Bad block tag: " + text) : DataResult.success(of(TagKey.create(Registries.BLOCK, id)));
        }
        Identifier id = Identifier.tryParse(text);
        if (id == null || !BuiltInRegistries.BLOCK.containsKey(id)) return DataResult.error(() -> "Unknown block: " + text);
        return DataResult.success(of(BuiltInRegistries.BLOCK.getValue(id)));
    }

    public boolean matches(BlockState state)
    {
        return block != null ? state.is(block) : tag != null && state.is(tag);
    }

    // The block this stands for (a random one of the tag's)
    public @Nullable Block pick(RandomSource random)
    {
        if (block != null) return block;
        if (tag == null) return null;
        // getTagOrEmpty gives an Iterable; StreamSupport turns that into a stream to collect
        List<Holder<Block>> blocks = java.util.stream.StreamSupport.stream(BuiltInRegistries.BLOCK.getTagOrEmpty(tag).spliterator(), false).toList();
        return blocks.isEmpty() ? null : blocks.get(random.nextInt(blocks.size())).value();
    }

    @Override
    public String toString()
    {
        return block != null ? BuiltInRegistries.BLOCK.getKey(block).toString() : "#" + tag.location();
    }
}
