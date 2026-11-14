package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.List;

// The blocks Metal's block effect has marked, kept on their chunk (attachment METAL_MARKS) so they last for good, saved
// with the world. A mark is used up when the block is broken (MetalParts.onBlockDrops). Only the server needs them.
public record MetalMarks(List<BlockPos> marked)
{
    public static final MetalMarks NONE = new MetalMarks(List.of());
    public static final Codec<MetalMarks> CODEC = BlockPos.CODEC.listOf().xmap(MetalMarks::new, MetalMarks::marked);

    public MetalMarks
    {
        marked = List.copyOf(marked);
    }

    public static void add(Level level, BlockPos pos)
    {
        LevelChunk chunk = level.getChunkAt(pos);
        MetalMarks marks = chunk.getData(DustAttachmentTypeRegistry.METAL_MARKS);
        if (marks.marked.contains(pos)) return;
        List<BlockPos> now = new ArrayList<>(marks.marked);
        now.add(pos.immutable());
        // setData marks the chunk as changed, so it's saved
        chunk.setData(DustAttachmentTypeRegistry.METAL_MARKS, new MetalMarks(now));
    }

    // Whether pos was marked; the mark is taken off either way
    public static boolean take(Level level, BlockPos pos)
    {
        LevelChunk chunk = level.getChunkAt(pos);
        MetalMarks marks = chunk.getData(DustAttachmentTypeRegistry.METAL_MARKS);
        if (!marks.marked.contains(pos)) return false;
        List<BlockPos> now = new ArrayList<>(marks.marked);
        now.remove(pos);
        chunk.setData(DustAttachmentTypeRegistry.METAL_MARKS, new MetalMarks(now));
        return true;
    }
}
