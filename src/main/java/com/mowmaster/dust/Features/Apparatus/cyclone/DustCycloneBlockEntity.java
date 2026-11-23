package com.mowmaster.dust.Features.Apparatus.cyclone;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.DustComposition.ElementNames;
import com.mowmaster.dust.Features.DustComposition.ElementPhase;
import com.mowmaster.dust.Features.DustStorage.DustClouds;
import com.mowmaster.dust.Features.DustStorage.DustNetwork;
import com.mowmaster.dust.Features.DustStorage.IDustHolder;
import com.mowmaster.dust.Features.Machines.Separation;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// The Dust Cyclone's working: a solid element made of two others (Separation.isSolid; not a primal) piped in at its back
// (up to CAPACITY of one element at a time; it's an IDustHolder on its tube network, taking dust in at that side only)
// spins apart, one dust every WORK_TICKS, into one of each of the two it's made of:
//   the lighter  (gaseous, then aqueous, amorphous, crystalline: the first of the two if they're alike) goes out along
//                the tubes at its front (DustNetwork.insertFrom)
//   the heavier  spirals down the Dust Cyclone Hopper under it and drops out of its spout as a Dust Pile
// Each waits up to OUT_CAPACITY while there's nowhere for it; it stops while either is full, or with no hopper under it.
// While it works, dust spins round inside it in a cyclone, and spirals down the hopper (clientTick).
public class DustCycloneBlockEntity extends CBSyncedBlockEntity implements IDustHolder
{
    public static final int CAPACITY = 100;
    private static final int OUT_CAPACITY = 100;
    private static final int WORK_TICKS = 4;
    private static final int PUSH_TICKS = 10;
    private static final int PUSH_AMOUNT = 20;
    private static final Codec<Map<DustElement, Integer>> AMOUNTS_CODEC = Codec.unboundedMap(DustElements.CODEC, Codec.INT);

    private @Nullable DustElement input;
    private int inputAmount;
    private final Map<DustElement, Integer> light = new LinkedHashMap<>();
    private final Map<DustElement, Integer> heavy = new LinkedHashMap<>();
    // Whether it's spinning (sent to clients, for the cyclone)
    private boolean working;

    public DustCycloneBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.DUST_CYCLONE.get(), pos, state);
    }

    private Direction facing()
    {
        return getBlockState().getValue(DustCycloneBlock.FACING);
    }

    // Whether this element can be spun apart here: solid, and made of two others
    public static boolean accepts(DustElement element)
    {
        return Separation.isSolid(element) && element.parts().size() == 2;
    }

    // ---- dust coming in (only at its back) ----

    @Override
    public boolean takesDustFrom(Direction side)
    {
        return side == facing().getOpposite();
    }

    @Override
    public int insertDust(DustElement dust, int count, boolean simulate)
    {
        if (count <= 0 || !accepts(dust) || (input != null && !input.equals(dust))) return 0;
        int moved = Math.min(count, CAPACITY - inputAmount);
        if (moved > 0 && !simulate) {
            input = dust;
            inputAmount += moved;
            sync();
        }
        return Math.max(0, moved);
    }

    @Override
    public int dustAmount(DustElement element)
    {
        return 0;
    }

    @Override
    public int extractDust(DustElement element, int amount, boolean simulate)
    {
        return 0;
    }

    @Override
    public boolean prefersDust(DustElement element)
    {
        return element.equals(input);
    }

    // ---- working ----

    private boolean hasHopper(Level level)
    {
        return level.getBlockState(worldPosition.below()).getBlock() instanceof DustCycloneHopperBlock;
    }

    public void serverTick(ServerLevel level)
    {
        long time = level.getGameTime() + worldPosition.asLong();
        boolean hopper = hasHopper(level);
        if (time % PUSH_TICKS == 0) {
            pushLight(level);
            if (hopper) dropHeavy(level);
        }
        boolean canWork = hopper && input != null && inputAmount > 0 && total(light) < OUT_CAPACITY && total(heavy) < OUT_CAPACITY;
        if (canWork && time % WORK_TICKS == 0) spin();
        if (canWork != working) {
            working = canWork;
            sync();
        }
    }

    // One dust apart into one of each of its two parts
    private void spin()
    {
        if (input == null) return;
        List<DustElement> parts = input.parts();
        DustElement a = parts.get(0), b = parts.get(1);
        boolean aLighter = weight(a) <= weight(b);
        light.merge(aLighter ? a : b, 1, Integer::sum);
        heavy.merge(aLighter ? b : a, 1, Integer::sum);
        inputAmount--;
        if (inputAmount <= 0) {
            inputAmount = 0;
            input = null;
            // Clients only need to know what's spinning (for the cyclone's colour)
            sync();
        } else {
            setChanged();
        }
    }

    // How heavy an element's dust is: crystalline the heaviest, gaseous the lightest
    private static int weight(DustElement element)
    {
        ElementPhase phase = element.phase();
        if (phase == ElementPhase.CRYSTALLINE) return 3;
        if (phase == ElementPhase.AMORPHOUS) return 2;
        if (phase == ElementPhase.AQUEOUS) return 1;
        return 0;
    }

    // The lighter part out along the tubes at its front
    private void pushLight(ServerLevel level)
    {
        for (Map.Entry<DustElement, Integer> entry : List.copyOf(light.entrySet())) {
            int went = DustNetwork.insertFrom(level, worldPosition, facing(), entry.getKey(), Math.min(PUSH_AMOUNT, entry.getValue()));
            if (went <= 0) continue;
            take(light, entry.getKey(), went);
        }
    }

    // The heavier part out of the hopper's spout, a Dust Pile under it
    private void dropHeavy(ServerLevel level)
    {
        BlockPos under = worldPosition.below(2);
        for (Map.Entry<DustElement, Integer> entry : List.copyOf(heavy.entrySet())) {
            int landed = DustClouds.deposit(level, under, entry.getKey(), Math.min(PUSH_AMOUNT, entry.getValue()), under.getY());
            if (landed <= 0) continue;
            take(heavy, entry.getKey(), landed);
            level.sendParticles(new DustParticleOptions(entry.getKey().color(), 1.0F), under.getX() + 0.5, under.getY() + 0.9, under.getZ() + 0.5, 4, 0.1, 0.2, 0.1, 0);
        }
    }

    private void take(Map<DustElement, Integer> from, DustElement element, int amount)
    {
        int left = from.getOrDefault(element, 0) - amount;
        if (left <= 0) from.remove(element);
        else from.put(element, left);
        setChanged();
    }

    private static int total(Map<DustElement, Integer> amounts)
    {
        int total = 0;
        for (int amount : amounts.values()) total += amount;
        return total;
    }

    // ---- the client: the cyclone ----

    public void clientTick()
    {
        Level level = this.level;
        if (level == null || !working || input == null) return;
        RandomSource random = level.getRandom();
        double time = level.getGameTime() * 0.55;
        double cx = worldPosition.getX() + 0.5, cz = worldPosition.getZ() + 0.5;
        // Round and round inside the barrel, rising and falling
        for (int i = 0; i < 3; i++) {
            double angle = time + i * (Math.PI * 2 / 3) + random.nextDouble() * 0.3;
            double radius = 0.12 + random.nextDouble() * 0.2;
            double y = worldPosition.getY() + 0.1 + random.nextDouble() * 0.85;
            double x = cx + Math.cos(angle) * radius, z = cz + Math.sin(angle) * radius;
            level.addParticle(new DustParticleOptions(input.color(), 0.7F), x, y, z, -Math.sin(angle) * 0.05, 0, Math.cos(angle) * 0.05);
        }
        // The heavier part spiralling down the hopper, narrower as it goes, and out of the spout
        List<DustElement> parts = input.parts();
        DustElement heavier = weight(parts.get(0)) <= weight(parts.get(1)) ? parts.get(1) : parts.get(0);
        double depth = random.nextDouble();
        double angle = time * 1.3 + random.nextDouble() * Math.PI * 2;
        double radius = 0.05 + 0.25 * (1 - depth);
        double y = worldPosition.getY() - depth * 0.95;
        level.addParticle(new DustParticleOptions(heavier.color(), 0.6F), cx + Math.cos(angle) * radius, y, cz + Math.sin(angle) * radius,
                -Math.sin(angle) * 0.04, -0.03, Math.cos(angle) * 0.04);
    }

    // "Dust Cyclone: spinning 40 Metal | 12 Fire out front, 12 Earth down"
    public Component status(Player player)
    {
        MutableComponent line = Component.empty().append(getBlockState().getBlock().getName()).append(": ");
        if (level != null && !hasHopper(level)) return line.append(Component.translatable("dust.cyclone.no_hopper"));
        if (input == null) line.append(Component.translatable("dust.cyclone.idle"));
        else line.append(Component.translatable("dust.cyclone.spinning", inputAmount)).append(" ").append(ElementNames.forViewer(input, player));
        for (Map.Entry<DustElement, Integer> entry : light.entrySet()) {
            line.append(" | ").append(Component.translatable("dust.cyclone.out_front", entry.getValue(), ElementNames.forViewer(entry.getKey(), player)));
        }
        for (Map.Entry<DustElement, Integer> entry : heavy.entrySet()) {
            line.append(" | ").append(Component.translatable("dust.cyclone.down", entry.getValue(), ElementNames.forViewer(entry.getKey(), player)));
        }
        return line;
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (input != null) output.store("input", DustElements.CODEC, input);
        output.putInt("input_amount", inputAmount);
        output.store("light", AMOUNTS_CODEC, light);
        output.store("heavy", AMOUNTS_CODEC, heavy);
        output.putBoolean("working", working);
    }

    @Override
    protected void loadAdditional(ValueInput in)
    {
        super.loadAdditional(in);
        input = in.read("input", DustElements.CODEC).orElse(null);
        inputAmount = in.getIntOr("input_amount", 0);
        light.clear();
        in.read("light", AMOUNTS_CODEC).ifPresent(light::putAll);
        heavy.clear();
        in.read("heavy", AMOUNTS_CODEC).ifPresent(heavy::putAll);
        working = in.getBooleanOr("working", false);
    }

    // Broken: what's in it comes out as element orbs
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (!(level instanceof ServerLevel server)) return;
        var at = net.minecraft.world.phys.Vec3.atCenterOf(pos);
        if (input != null && inputAmount > 0) com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity.spawn(server, input, inputAmount, at);
        light.forEach((element, amount) -> com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity.spawn(server, element, amount, at));
        heavy.forEach((element, amount) -> com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity.spawn(server, element, amount, at));
        input = null;
        inputAmount = 0;
        light.clear();
        heavy.clear();
    }
}
