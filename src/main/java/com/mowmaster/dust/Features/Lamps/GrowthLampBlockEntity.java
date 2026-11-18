package com.mowmaster.dust.Features.Lamps;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

// The Growth Lamp: burns Growth dust. While lit, blocks it can see within RADIUS that grow by random ticks (crops,
// saplings, grass, vines, cactus, copper ageing...) get extra random ticks: it tries blocks at random each tick
// (GrowthBoost: how many), and a block that grows or changes is ticked just as vanilla ticks it.
//   a plant moving on a stage   green growth sparkles on it (as bone meal shows)
//   anything else it ticks      a little spark now and then (as Motion's Quickening shows on a sped-up furnace)
public class GrowthLampBlockEntity extends LampBlockEntity
{
    public GrowthLampBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.GROWTH_LAMP.get(), pos, state);
    }

    @Override public DustElement element() { return DustElements.GROWTH; }
    @Override public int color() { return 0x6CE07A; }

    @Override
    protected void work(ServerLevel level)
    {
        RandomSource random = level.getRandom();
        int tries = GrowthBoost.share(level);
        for (int i = 0; i < tries; i++) {
            BlockPos target = randomInRange(random);
            // isLoaded: never reach into a chunk that isn't loaded (getBlockState would load it)
            if (target == null || !level.isLoaded(target)) continue;
            BlockState state = level.getBlockState(target);
            if (!state.isRandomlyTicking() || !sees(level, Vec3.atCenterOf(target), target)) continue;
            state.randomTick(level, target, random);
            BlockState after = level.getBlockState(target);
            double x = target.getX() + 0.5, y = target.getY() + 0.5, z = target.getZ() + 0.5;
            if (isPlant(state.getBlock())) {
                if (after != state) level.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 6, 0.3, 0.3, 0.3, 0);
            } else if (random.nextInt(3) == 0) {
                level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y + 0.3, z, 1, 0.3, 0.2, 0.3, 0);
            }
        }
    }

    // Plants: crops, saplings, flowers, vines, bushes, nether wart (VegetationBlock), and anything bone meal grows
    private static boolean isPlant(Block block)
    {
        return block instanceof VegetationBlock || block instanceof BonemealableBlock;
    }
}
