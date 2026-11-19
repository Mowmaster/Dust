package com.mowmaster.dust.Features.Apparatus;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

// The Dissolving Retort: the Crucible and Vat run backwards. The pattern is a mixed element; each piece of work draws
// one of it from the Dust Jars on its tube network and dissolves it into one of each of the two elements it's mixed
// from (air: a fire and a water), which go back to the jars, WORK_TICKS a dust. (Research breaks elements down the same
// way: 9 Trees is 9 Plants and 9 Earth.) A part the owner hasn't discovered slips away into the aura, as everywhere.
public class DissolvingRetortBlockEntity extends DustApparatusBlockEntity
{
    public static final int WORK_TICKS = 10;

    public DissolvingRetortBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.DISSOLVING_RETORT.get(), pos, state);
    }

    // Only a mixed element: a primal has nothing to break into
    @Override
    protected @Nullable String patternProblem(ItemStack stack, DustElement element)
    {
        return element.parts().isEmpty() ? "primal" : null;
    }

    @Override
    protected Map<DustElement, Integer> needs()
    {
        DustElement element = patternElement();
        return element == null || element.parts().isEmpty() ? Map.of() : Map.of(element, 1);
    }

    // Room for both parts in the dust waiting to go
    @Override
    protected boolean canStart()
    {
        DustElement element = patternElement();
        return element != null && dustRoom() >= element.parts().size();
    }

    @Override
    protected void begin(ServerLevel level)
    {
        if (level.getRandom().nextInt(4) == 0) level.playSound(null, worldPosition, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.25F, 0.8F);
    }

    @Override
    protected void make(ServerLevel level)
    {
        DustElement element = patternElement();
        if (element != null) catchDust(level, List.copyOf(element.parts()));
    }

    @Override
    protected int workTicks()
    {
        return WORK_TICKS;
    }

    @Override
    protected String verb()
    {
        return "dissolving";
    }

    // Client: while it works, motes of the two parts rising out of the retort's mouth, one colour each
    @Override
    protected void clientWork(Level level)
    {
        super.clientWork(level);
        DustElement element = patternElement();
        if (!isWorking() || element == null || element.parts().isEmpty() || level.getRandom().nextInt(2) != 0) return;
        net.minecraft.util.RandomSource random = level.getRandom();
        DustElement part = element.parts().get(random.nextInt(element.parts().size()));
        net.minecraft.world.phys.Vec3 mouth = at(8 + random.nextGaussian() * 0.5, 15.5, 8 + random.nextGaussian() * 0.5);
        level.addParticle(new net.minecraft.core.particles.DustParticleOptions(part.color(), 0.5F), mouth.x, mouth.y, mouth.z,
                random.nextGaussian() * 0.01, 0.03, random.nextGaussian() * 0.01);
        if (random.nextInt(8) == 0) {
            net.minecraft.world.phys.Vec3 fire = at(4 + random.nextDouble() * 8, 4.5, 2.4);
            level.addParticle(net.minecraft.core.particles.ParticleTypes.SMALL_FLAME, fire.x, fire.y, fire.z, 0, 0.01, 0);
        }
    }
}
