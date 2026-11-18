package com.mowmaster.dust.Features.Apparatus;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Machines.AlchemicalDust;
import com.mowmaster.dust.Features.Machines.ResearchTransformationBlockEntity;
import com.mowmaster.dust.Features.Machines.StationSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

// The Transmuter (the Transformation Table's apparatus): makes items of an element out of its dust, one dust each,
// WORK_TICKS each. The pattern says which element and what to make:
//   a crystal          that element's crystal, from one dust and an inert crystal or nether quartz (fed in at the
//                      intake, or by hoppers and pipes from the top and sides; up to a stack)
//   Alchemical Dust    Alchemical Dust of it
//   element dust       the dust item (to carry, feed a Spell Caster or an Element Crafter by hand...)
// What it makes goes to the outputs.
public class TransmuterBlockEntity extends DustApparatusBlockEntity
{
    public static final int WORK_TICKS = 20;
    public static final int CRYSTAL_LIMIT = 64;

    private ItemStack crystals = ItemStack.EMPTY;

    public TransmuterBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.TRANSMUTER.get(), pos, state);
    }

    public ItemStack getCrystals() { return crystals; }

    // Inert crystals and nether quartz, at the intake
    @Override
    protected List<StationSlot> extraSlots()
    {
        return List.of(StationSlot.ofStack(ApparatusLayout.INTAKE, ResearchTransformationBlockEntity::isCrystal, CRYSTAL_LIMIT,
                () -> crystals, stack -> crystals = stack, this::sync));
    }

    // Any element's crystal, dust or Alchemical Dust will do
    @Override
    protected @Nullable String patternProblem(ItemStack stack, DustElement element)
    {
        return null;
    }

    private boolean makesCrystals()
    {
        return CrystalElements.of(pattern) != null;
    }

    // What one piece makes, from the pattern (the renderer shows it growing in the dome)
    public ItemStack product()
    {
        DustElement element = patternElement();
        if (element == null) return ItemStack.EMPTY;
        if (makesCrystals()) return CrystalElements.stackOf(element);
        if (AlchemicalDust.is(pattern)) return AlchemicalDust.create(element);
        return CrystalElements.dustOf(element, 1);
    }

    @Override
    protected Map<DustElement, Integer> needs()
    {
        DustElement element = patternElement();
        return element == null ? Map.of() : Map.of(element, 1);
    }

    @Override
    protected boolean canStart()
    {
        ItemStack made = product();
        return !made.isEmpty() && outputsFit(made) && (!makesCrystals() || !crystals.isEmpty());
    }

    @Override
    protected void begin(ServerLevel level)
    {
        if (makesCrystals()) {
            crystals.shrink(1);
            if (crystals.isEmpty()) crystals = ItemStack.EMPTY;
        }
        level.playSound(null, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.3F, 1.2F);
    }

    @Override
    protected void make(ServerLevel level)
    {
        addOutput(product());
        level.playSound(null, worldPosition, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.6F, 1.2F);
    }

    @Override
    protected int workTicks()
    {
        return WORK_TICKS;
    }

    @Override
    protected String verb()
    {
        return "transmuting";
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        store(output, "crystals", crystals);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        crystals = read(input, "crystals");
    }

    @Override
    protected List<ItemStack> dropsOnBreak()
    {
        List<ItemStack> drops = super.dropsOnBreak();
        drops.add(crystals);
        return drops;
    }

    // Client: while it works, enchanting glyphs drawn into the dome
    @Override
    protected void clientWork(net.minecraft.world.level.Level level)
    {
        super.clientWork(level);
        if (!isWorking() || level.getRandom().nextInt(2) != 0) return;
        net.minecraft.util.RandomSource random = level.getRandom();
        net.minecraft.world.phys.Vec3 from = at(4 + random.nextDouble() * 8, 14, 4 + random.nextDouble() * 8);
        net.minecraft.world.phys.Vec3 to = at(8, 10, 8);
        level.addParticle(net.minecraft.core.particles.ParticleTypes.ENCHANT, to.x, to.y, to.z, from.x - to.x, from.y - to.y, from.z - to.z);
    }
}
