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

// The Evaporating Kettle (the Evaporation Table's apparatus): boils the gaseous and aqueous elements out of an item into
// the air, where they feed the chunk's aura (ChunkAura: a rich aura grows crops faster and quickens mana; an
// overcharged one brings empowered monsters). The solid elements stay together in the kettle and come out as an
// alchemical compound (for the Alchemical Separator). It makes no dust: it's for working the land's aura. Items with
// nothing gaseous or aqueous in them, and blocks (crush them first in a Crushing Mill), pass straight through.
public class EvaporatingKettleBlockEntity extends ItemApparatusBlockEntity
{
    public static final int WORK_TICKS = 20;

    public EvaporatingKettleBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.EVAPORATING_KETTLE.get(), pos, state);
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
        return new Result(List.of(), rest, fluids);
    }

    @Override
    protected String verb()
    {
        return "evaporating";
    }

    @Override
    protected void finished(ServerLevel level, Result result)
    {
        level.playSound(null, worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.25F, 1.6F);
    }

    // Client: while it works, steam rising from the neck and now and then a flame licking out of the firebox
    @Override
    protected void clientWork(net.minecraft.world.level.Level level)
    {
        super.clientWork(level);
        if (!isWorking()) return;
        net.minecraft.util.RandomSource random = level.getRandom();
        net.minecraft.world.phys.Vec3 neck = at(8 + random.nextGaussian() * 0.6, 16, 8 + random.nextGaussian() * 0.6);
        level.addParticle(net.minecraft.core.particles.ParticleTypes.WHITE_SMOKE, neck.x, neck.y, neck.z, 0, 0.03 + random.nextDouble() * 0.03, 0);
        if (random.nextInt(8) == 0) {
            net.minecraft.world.phys.Vec3 fire = at(4 + random.nextDouble() * 8, 4.5, 1.9);
            level.addParticle(net.minecraft.core.particles.ParticleTypes.SMALL_FLAME, fire.x, fire.y, fire.z, 0, 0.01, 0);
        }
    }
}
