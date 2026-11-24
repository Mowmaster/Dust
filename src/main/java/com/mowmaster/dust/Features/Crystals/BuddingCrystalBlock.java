package com.mowmaster.dust.Features.Crystals;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Magic.ElementAura;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

// A Budding Crystal (lines element geodes): like budding amethyst, now and then (random ticks) it grows a crystal of its
// element on one of its faces: a small one where there's room, then medium, then large. Its growth feeds on the chunk's
// aura (ChunkAura): the chance scales with how full the chunk is of its element's primals (ElementAura.fullness, up to
// MAX_SPEEDUP times as fast in a rich chunk, slower in a thin one), and each growth takes the dust it adds out of the
// aura (1 for a small crystal, 3 more for a medium, 12 more for a large), so geodes help an over-full chunk settle.
// Drops nothing, even with Silk Touch (as budding amethyst).
public class BuddingCrystalBlock extends AmethystBlock implements ElementCrystal
{
    // On a random tick, a 1 in GROW_ODDS chance of growing (at a normal aura)
    private static final int GROW_ODDS = 5;
    private static final float MAX_SPEEDUP = 3;

    private final DustElement element;

    public BuddingCrystalBlock(DustElement element, Properties properties)
    {
        super(properties);
        this.element = element;
    }

    @Override
    public DustElement element()
    {
        return element;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        float chance = Math.min(MAX_SPEEDUP, ElementAura.fullness(level, pos, element)) / GROW_ODDS;
        if (random.nextFloat() >= chance) return;
        Direction face = Direction.values()[random.nextInt(Direction.values().length)];
        BlockPos at = pos.relative(face);
        BlockState there = level.getBlockState(at);
        CrystalSize grow = null;
        int before = 0;
        if (BuddingAmethystBlock.canClusterGrowAtState(there)) {
            grow = CrystalSize.SMALL;
        } else if (there.getBlock() instanceof CrystalNodeBlock node && node.element().equals(element)
                && there.getValue(AmethystClusterBlock.FACING) == face) {
            grow = node.size().next();
            before = node.size().value;
        }
        if (grow == null) return;
        BlockState into = ElementCrystals.node(element, grow).defaultBlockState()
                .setValue(AmethystClusterBlock.FACING, face)
                .setValue(AmethystClusterBlock.WATERLOGGED, there.getFluidState().getType() == Fluids.WATER);
        level.setBlockAndUpdate(at, into);
        ElementAura.take(level, pos, element, grow.value - before);
    }
}
