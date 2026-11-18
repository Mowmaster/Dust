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

// The Alchemical Separator (the Separation Table's apparatus): spins the solid elements (crystalline and amorphous) out
// of an item as dust, all of them, into the Dust Jars on its tube network. Its gaseous and aqueous elements stay
// together and come out as an alchemical liquid or gas (a compound, for the Alembic or the Evaporating Kettle). Items
// with nothing solid in them, and blocks (crush them first in a Crushing Mill), pass straight through.
public class SeparatorBlockEntity extends ItemApparatusBlockEntity
{
    public static final int WORK_TICKS = 20;

    public SeparatorBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.ALCHEMICAL_SEPARATOR.get(), pos, state);
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
        List<DustElement> solids = solids(elements);
        if (solids.isEmpty()) return null;
        List<DustElement> fluids = fluids(elements);
        ItemStack rest = fluids.isEmpty() ? ItemStack.EMPTY : Compounds.create(fluids, ProductSource.original(one), Compounds.Kind.of(fluids));
        return new Result(solids, rest, List.of());
    }

    @Override
    protected String verb()
    {
        return "separating";
    }

    @Override
    protected void started(ServerLevel level)
    {
        level.playSound(null, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.3F, 1.4F);
    }

    // Client: while it works, motes in the colours of the item's solid elements flung off the spinning drum
    @Override
    protected void clientWork(net.minecraft.world.level.Level level)
    {
        super.clientWork(level);
        if (!isWorking()) return;
        List<DustElement> solids = solids(elementsOf(working));
        if (solids.isEmpty()) return;
        net.minecraft.util.RandomSource random = level.getRandom();
        double angle = random.nextDouble() * Math.PI * 2;
        net.minecraft.world.phys.Vec3 from = at(8 + Math.cos(angle) * 3.2, 5 + random.nextDouble() * 5, 8 + Math.sin(angle) * 3.2);
        int color = solids.get(random.nextInt(solids.size())).color();
        level.addParticle(new net.minecraft.core.particles.DustParticleOptions(color, 0.45F), from.x, from.y, from.z,
                Math.cos(angle) * 0.02, 0.005, Math.sin(angle) * 0.02);
    }
}
