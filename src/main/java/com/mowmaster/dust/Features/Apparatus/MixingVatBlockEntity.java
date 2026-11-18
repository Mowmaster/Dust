package com.mowmaster.dust.Features.Apparatus;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;

// The Mixing Vat (the Mixing Table's apparatus): stirs two elements' dust together, at least one of them gaseous or
// aqueous, into the element mixed from them (CombiningApparatusBlockEntity), WORK_TICKS a dust
public class MixingVatBlockEntity extends CombiningApparatusBlockEntity
{
    public static final int WORK_TICKS = 10;

    public MixingVatBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.MIXING_VAT.get(), pos, state);
    }

    @Override
    protected boolean takesSolids()
    {
        return false;
    }

    @Override
    protected String otherApparatus()
    {
        return "conjoining_crucible";
    }

    @Override
    protected void begin(ServerLevel level)
    {
        if (level.getRandom().nextInt(4) == 0) level.playSound(null, worldPosition, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.25F, 1.4F);
    }

    @Override
    protected int workTicks()
    {
        return WORK_TICKS;
    }

    @Override
    protected String verb()
    {
        return "mixing";
    }

    // Client: while it works, bubbles on the liquid and motes of the element it's making rising off it
    @Override
    protected void clientWork(net.minecraft.world.level.Level level)
    {
        super.clientWork(level);
        if (!isWorking()) return;
        net.minecraft.util.RandomSource random = level.getRandom();
        net.minecraft.world.phys.Vec3 surface = at(4.5 + random.nextDouble() * 7, 10.7, 4.5 + random.nextDouble() * 7);
        if (random.nextInt(2) == 0) level.addParticle(net.minecraft.core.particles.ParticleTypes.BUBBLE_POP, surface.x, surface.y, surface.z, 0, 0.01, 0);
        com.mowmaster.dust.Features.DustComposition.DustElement element = patternElement();
        if (element != null && random.nextInt(3) == 0) {
            level.addParticle(new net.minecraft.core.particles.DustParticleOptions(element.color(), 0.5F), surface.x, surface.y + 0.05, surface.z, 0, 0.025, 0);
        }
    }
}
