package com.mowmaster.dust.Features.Apparatus;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;

// The Conjoining Crucible (the Combination Table's apparatus): melts two solid elements' dust together into the
// element mixed from them (CombiningApparatusBlockEntity), WORK_TICKS a dust
public class ConjoiningCrucibleBlockEntity extends CombiningApparatusBlockEntity
{
    public static final int WORK_TICKS = 10;

    public ConjoiningCrucibleBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.CONJOINING_CRUCIBLE.get(), pos, state);
    }

    @Override
    protected boolean takesSolids()
    {
        return true;
    }

    @Override
    protected String otherApparatus()
    {
        return "mixing_vat";
    }

    @Override
    protected void begin(ServerLevel level)
    {
        if (level.getRandom().nextInt(4) == 0) level.playSound(null, worldPosition, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.3F, 1.2F);
    }

    @Override
    protected int workTicks()
    {
        return WORK_TICKS;
    }

    @Override
    protected String verb()
    {
        return "conjoining";
    }

    // Client: while it works, the melt pops and spits, and motes of the two elements going in rise off it
    @Override
    protected void clientWork(net.minecraft.world.level.Level level)
    {
        super.clientWork(level);
        if (!isWorking()) return;
        net.minecraft.util.RandomSource random = level.getRandom();
        net.minecraft.world.phys.Vec3 melt = at(5 + random.nextDouble() * 6, 11, 5 + random.nextDouble() * 6);
        if (random.nextInt(12) == 0) level.addParticle(net.minecraft.core.particles.ParticleTypes.LAVA, melt.x, melt.y, melt.z, 0, 0, 0);
        com.mowmaster.dust.Features.DustComposition.DustElement element = patternElement();
        if (element != null && random.nextInt(2) == 0) {
            int color = element.parts().isEmpty() ? element.color() : element.parts().get(random.nextInt(element.parts().size())).color();
            level.addParticle(new net.minecraft.core.particles.DustParticleOptions(color, 0.5F), melt.x, melt.y + 0.05, melt.z, 0, 0.03, 0);
        }
    }
}
