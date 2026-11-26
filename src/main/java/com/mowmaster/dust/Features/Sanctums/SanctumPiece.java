package com.mowmaster.dust.Features.Sanctums;

import com.mowmaster.dust.DustRegistries.DustFeatureRegistry;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

// The whole of a sanctum as one structure piece: where it is (origin: the middle of its rooms' floor), which way it
// faces, its element, and how high the shrine's ground is above the rooms (top). Minecraft builds a structure one chunk
// at a time, so postProcess builds only what falls in that chunk (SanctumBuilder with the Build clipped to it). Saved
// with the chunk's structure data, so every chunk builds the same sanctum.
public class SanctumPiece extends StructurePiece
{
    private final BlockPos origin;
    private final Direction forward;
    private final ElementalArmorSet set;
    private final int top;

    public SanctumPiece(BlockPos origin, Direction forward, ElementalArmorSet set, int top)
    {
        super(DustFeatureRegistry.SANCTUM_PIECE.get(), 0, bounds(origin, forward, top));
        this.origin = origin;
        this.forward = forward;
        this.set = set;
        this.top = top;
    }

    public SanctumPiece(StructurePieceSerializationContext context, CompoundTag tag)
    {
        super(DustFeatureRegistry.SANCTUM_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getIntOr("origin_x", 0), tag.getIntOr("origin_y", 0), tag.getIntOr("origin_z", 0));
        this.forward = Direction.from2DDataValue(tag.getIntOr("facing", 0));
        ElementalArmorSet stored = ElementalArmorSet.PYREBORN;
        for (ElementalArmorSet candidate : ElementalArmorSet.values()) if (candidate.id.equals(tag.getStringOr("set", ""))) stored = candidate;
        this.set = stored;
        this.top = tag.getIntOr("top", SanctumBuilder.DEPTH);
    }

    // Everything SanctumBuilder can build round this origin, facing this way
    private static BoundingBox bounds(BlockPos origin, Direction forward, int top)
    {
        Direction right = forward.getClockWise();
        BlockPos one = origin.relative(right, -SanctumBuilder.BOUNDS_X).relative(forward, -SanctumBuilder.BOUNDS_Z_BACK).above(SanctumBuilder.BOUNDS_BELOW);
        BlockPos two = origin.relative(right, SanctumBuilder.BOUNDS_X).relative(forward, SanctumBuilder.BOUNDS_Z_FRONT).above(top + SanctumBuilder.BOUNDS_ABOVE);
        return BoundingBox.fromCorners(one, two);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
    {
        tag.putInt("origin_x", origin.getX());
        tag.putInt("origin_y", origin.getY());
        tag.putInt("origin_z", origin.getZ());
        tag.putInt("facing", forward.get2DDataValue());
        tag.putString("set", set.id);
        tag.putInt("top", top);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structures, ChunkGenerator generator, RandomSource random, BoundingBox chunkBox,
                            ChunkPos chunkPos, BlockPos pivot)
    {
        SanctumBuilder.build(new Build(level, origin, forward).clip(chunkBox), set, random, top);
    }
}
