package com.mowmaster.dust.Features.Spells.blocks;

import com.mowmaster.dust.Features.Magic.ChunkAura;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

// Abyss's block effect: corruption. A corrupted block remembers what it was (CorruptedBlockEntity) and slowly corrupts
// the blocks around it (random ticks; slower where the chunk's aura is low, once aura exists). Purifying it (a
// Healing spell) brings back what it was. Broken, it drops nothing.
public class CorruptedBlock extends Block implements EntityBlock
{
    public static final float SPREAD_CHANCE = 0.2F;

    public CorruptedBlock(Properties properties)
    {
        super(properties.randomTicks());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new CorruptedBlockEntity(pos, state);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if (random.nextFloat() >= SPREAD_CHANCE * ChunkAura.spreadFactor(level, pos)) return;
        BlockPos next = pos.relative(net.minecraft.core.Direction.getRandom(random));
        SpellBlocks.corrupt(level, next);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (random.nextInt(5) == 0) {
            level.addParticle(ParticleTypes.PORTAL, pos.getX() + random.nextDouble(), pos.getY() + 1.05, pos.getZ() + random.nextDouble(), 0, 0.05, 0);
        }
    }
}
