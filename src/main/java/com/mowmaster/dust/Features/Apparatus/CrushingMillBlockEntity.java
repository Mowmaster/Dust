package com.mowmaster.dust.Features.Apparatus;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.Machines.ResearchCrusherBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The Crushing Mill (the Research Crusher's apparatus): grinds blocks between its iron wheels into crushed pebbles that
// remember their block (as the Research Crusher's do), WORK_TICKS each, for the Separator, Kettle and Alembic, which
// don't take blocks. Anything that isn't a block passes straight through, so a farm's whole harvest can go through it
// first.
public class CrushingMillBlockEntity extends ItemApparatusBlockEntity
{
    public static final int WORK_TICKS = 20;

    public CrushingMillBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.CRUSHING_MILL.get(), pos, state);
    }

    @Override
    protected int workTicks()
    {
        return WORK_TICKS;
    }

    @Override
    protected @Nullable Result process(ItemStack one)
    {
        if (!isBlock(one)) return null;
        return new Result(List.of(), ResearchCrusherBlockEntity.crushed(one), List.of());
    }

    @Override
    protected String verb()
    {
        return "crushing";
    }

    @Override
    protected void started(ServerLevel level)
    {
        level.playSound(null, worldPosition, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.4F, 0.7F + level.getRandom().nextFloat() * 0.2F);
    }

    // Client: while it works, grit of the block being crushed thrown up from between the wheels
    @Override
    protected void clientWork(net.minecraft.world.level.Level level)
    {
        super.clientWork(level);
        if (!isWorking() || level.getRandom().nextInt(3) != 0 || !(working.getItem() instanceof net.minecraft.world.item.BlockItem blockItem)) return;
        net.minecraft.util.RandomSource random = level.getRandom();
        net.minecraft.world.phys.Vec3 from = at(5 + random.nextDouble() * 6, 14.5, 7 + random.nextDouble() * 2);
        level.addParticle(new net.minecraft.core.particles.BlockParticleOption(net.minecraft.core.particles.ParticleTypes.BLOCK,
                blockItem.getBlock().defaultBlockState()), from.x, from.y, from.z, 0, 0.08, 0);
    }
}
