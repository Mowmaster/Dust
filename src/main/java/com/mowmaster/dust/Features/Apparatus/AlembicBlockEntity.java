package com.mowmaster.dust.Features.Apparatus;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Machines.Compounds;
import com.mowmaster.dust.Features.Machines.ProductSource;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The Alembic (the Distillation Table's apparatus): boils the gaseous and aqueous elements out of an item and catches
// them as they come over, all of them, as dust for the Dust Jars on its tube network. The solid elements stay together
// in the flask and come out as an alchemical compound (for the Alchemical Separator). Items with nothing gaseous or
// aqueous in them, and blocks (crush them first in a Crushing Mill), pass straight through.
public class AlembicBlockEntity extends ItemApparatusBlockEntity
{
    public static final int WORK_TICKS = 20;

    public AlembicBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.ALEMBIC.get(), pos, state);
    }

    @Override
    protected int workTicks()
    {
        return WORK_TICKS;
    }

    @Override
    protected @Nullable Result process(ItemStack one)
    {
        if (isBlock(one)) return null;
        List<DustElement> elements = elementsOf(one);
        List<DustElement> fluids = fluids(elements);
        if (fluids.isEmpty()) return null;
        List<DustElement> solids = solids(elements);
        ItemStack rest = solids.isEmpty() ? ItemStack.EMPTY : Compounds.create(solids, ProductSource.original(one));
        return new Result(fluids, rest, List.of());
    }

    @Override
    protected String verb()
    {
        return "distilling";
    }

    @Override
    protected void started(ServerLevel level)
    {
        level.playSound(null, worldPosition, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.4F, 1.1F);
    }

    // Client: while it works, bubbles in the receiver, drops of the distillate falling from the neck, and flames under
    // the pot
    @Override
    protected void clientWork(net.minecraft.world.level.Level level)
    {
        super.clientWork(level);
        if (!isWorking()) return;
        net.minecraft.util.RandomSource random = level.getRandom();
        if (random.nextInt(3) == 0) {
            net.minecraft.world.phys.Vec3 bubble = at(6.5 + random.nextDouble() * 3, 6.2, 1.8 + random.nextDouble() * 1.8);
            level.addParticle(net.minecraft.core.particles.ParticleTypes.BUBBLE_POP, bubble.x, bubble.y, bubble.z, 0, 0.01, 0);
        }
        if (random.nextInt(6) == 0) {
            net.minecraft.world.phys.Vec3 drop = at(8, 8.3, 3);
            level.addParticle(new net.minecraft.core.particles.DustParticleOptions(0xB48CFF, 0.5F), drop.x, drop.y, drop.z, 0, -0.05, 0);
        }
        if (random.nextInt(10) == 0) {
            net.minecraft.world.phys.Vec3 fire = random.nextBoolean() ? at(2.9, 4.5, 6 + random.nextDouble() * 6) : at(13.1, 4.5, 6 + random.nextDouble() * 6);
            level.addParticle(net.minecraft.core.particles.ParticleTypes.SMALL_FLAME, fire.x, fire.y, fire.z, 0, 0.01, 0);
        }
    }
}
