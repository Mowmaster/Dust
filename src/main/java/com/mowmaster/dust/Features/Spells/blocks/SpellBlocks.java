package com.mowmaster.dust.Features.Spells.blocks;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.conversions.SpellConversions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;

// The block effects that leave a spell block behind, and purifying them:
//   Poison   a pool of poison on top of the block
//   Flesh    blight on top of the block (it spreads)
//   Abyss    the block becomes corrupted (it spreads)
//   Healing  purifies: a corrupted block becomes what it was, blight and poison are cleared; anything else is mended
//            (its conversion table)
public final class SpellBlocks
{
    private SpellBlocks() {}

    public static void poisonPool(SpellCast cast, BlockPos pos, Direction face)
    {
        layer(cast.level(), pos, (SpellLayerBlock) DustBlockRegistry.POISON_POOL.get());
    }

    public static void blight(SpellCast cast, BlockPos pos, Direction face)
    {
        layer(cast.level(), pos, (SpellLayerBlock) DustBlockRegistry.BLIGHT.get());
    }

    private static void layer(ServerLevel level, BlockPos pos, SpellLayerBlock layer)
    {
        BlockPos above = pos.above();
        if (layer.canLieAt(level, above)) level.setBlockAndUpdate(above, layer.defaultBlockState());
    }

    public static void corruptBlock(SpellCast cast, BlockPos pos, Direction face)
    {
        corrupt(cast.level(), pos);
    }

    // Corrupts the block at pos, if it's a whole, plain block: not air or fluid, not one with contents, not unbreakable,
    // not already corrupted
    public static boolean corrupt(ServerLevel level, BlockPos pos)
    {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || !state.getFluidState().isEmpty() || level.getBlockEntity(pos) != null || state.getDestroySpeed(level, pos) < 0
                || state.is(DustBlockRegistry.CORRUPTED.get()) || !state.isCollisionShapeFullBlock(level, pos)) {
            return false;
        }
        level.setBlockAndUpdate(pos, DustBlockRegistry.CORRUPTED.get().defaultBlockState());
        if (level.getBlockEntity(pos) instanceof CorruptedBlockEntity corrupted) corrupted.setOriginal(state);
        level.playSound(null, pos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 0.6F, 0.7F);
        return true;
    }

    // Healing's block effect: purify if there's anything to purify, else mend (its conversion table)
    public static void purifyOrMend(SpellCast cast, BlockPos pos, Direction face)
    {
        if (!purify(cast.level(), pos)) SpellConversions.convert(cast, pos, face);
    }

    // A corrupted block back to what it was; slime patches, blight or a poison pool on it (or at it) cleared. True if
    // anything was.
    public static boolean purify(ServerLevel level, BlockPos pos)
    {
        boolean purified = false;
        if (level.getBlockEntity(pos) instanceof CorruptedBlockEntity corrupted) {
            BlockState original = corrupted.original();
            level.setBlockAndUpdate(pos, original);
            purified = true;
        }
        // Slime patches washed off
        if (Sticking.clear(level, pos)) purified = true;
        for (BlockPos at : new BlockPos[] {pos, pos.above()}) {
            BlockState state = level.getBlockState(at);
            if (state.is(DustBlockRegistry.BLIGHT.get()) || state.is(DustBlockRegistry.POISON_POOL.get())) {
                level.removeBlock(at, false);
                purified = true;
            }
        }
        if (purified) {
            level.sendParticles(ParticleTypes.END_ROD, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 10, 0.3, 0.3, 0.3, 0.02);
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.6F);
        }
        return purified;
    }
}
