package com.mowmaster.dust.Features.Apparatus.still;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustRegistries.DustParticleRegistry;
import com.mowmaster.dust.Features.Apparatus.ApparatusBlockEntity;
import com.mowmaster.dust.Features.Apparatus.ApparatusLayout;
import com.mowmaster.dust.Features.Apparatus.ItemApparatusBlockEntity;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Machines.StationSlot;
import com.mowmaster.dust.Features.Magic.ChunkAura;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// The Still Pot: runs the Still (StillLayout). What goes in (hoppers and pipes from the top and sides, or right-click its
// top) boils one at a time, WORK_TICKS each, while the Burnable Firebox under it burns (it lights fuel as it's needed) and
// there's a Still Top on the stack:
//   solids     anything solid in it burns to one Alchemical Soot, pushed every PUSH_TICKS into the containers beside
//              the pot (and kept in its outputs while none has room; it waits when they're full)
//   the rest   each gaseous or aqueous element rises into a heated tower (within StillLayout.HEAT_REACH of the firebox):
//              the tower already set to it, else an empty one. What no tower takes (no tower for it, or full) leaves
//              through the top into the chunk's aura, as smoke in its colour. So a pot with only a top on it sends
//              everything into the aura.
//   unknown    an element the owner hasn't discovered always leaves through the top
// Blocks (crush them in a Crushing Mill first) and things with no elements pass straight through to the outputs.
public class StillPotBlockEntity extends ApparatusBlockEntity
{
    public static final int WORK_TICKS = 20;
    public static final int INPUT_LIMIT = 64;
    public static final int SOOT_PUSH_TICKS = 10;

    private ItemStack input = ItemStack.EMPTY;
    private ItemStack working = ItemStack.EMPTY;
    private int progress = -1;

    public StillPotBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.STILL_POT.get(), pos, state);
    }

    public boolean isWorking() { return progress >= 0; }

    @Override
    protected List<StationSlot> machineSlots()
    {
        return List.of(StationSlot.ofStack(ApparatusLayout.INTAKE, stack -> true, INPUT_LIMIT, () -> input, stack -> input = stack, this::sync));
    }

    private @Nullable StillFireboxBlockEntity firebox(Level level)
    {
        return level.getBlockEntity(worldPosition.below()) instanceof StillFireboxBlockEntity firebox ? firebox : null;
    }

    // ---- working ----

    @Override
    protected void work(ServerLevel level)
    {
        if ((level.getGameTime() + worldPosition.asLong()) % SOOT_PUSH_TICKS == 0) pushOutputs(level);
        if (progress < 0) {
            tryStart(level);
            return;
        }
        // Only boils while it's hot (and while it's a still)
        StillFireboxBlockEntity firebox = firebox(level);
        if (firebox == null || StillLayout.above(level, worldPosition).top() == null || !firebox.heat(level)) return;
        if (++progress < WORK_TICKS) {
            setChanged();
            return;
        }
        finish(level);
    }

    private void tryStart(ServerLevel level)
    {
        if (input.isEmpty()) return;
        ItemStack one = input.copyWithCount(1);
        List<DustElement> elements = ItemApparatusBlockEntity.elementsOf(one);
        if (ItemApparatusBlockEntity.isBlock(one) || elements.isEmpty()) {
            // Passed straight through: as many as fit
            int moving = 0;
            while (moving < input.getCount() && outputsFit(input.copyWithCount(moving + 1))) moving++;
            if (moving <= 0) return;
            addOutput(input.split(moving));
            if (input.isEmpty()) input = ItemStack.EMPTY;
            sync();
            return;
        }
        if (!ItemApparatusBlockEntity.solids(elements).isEmpty() && !outputsFit(soot())) return;
        StillFireboxBlockEntity firebox = firebox(level);
        if (firebox == null || StillLayout.above(level, worldPosition).top() == null || !firebox.heat(level)) return;
        working = input.split(1);
        if (input.isEmpty()) input = ItemStack.EMPTY;
        progress = 0;
        level.playSound(null, worldPosition, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.4F, 1.0F);
        sync();
    }

    private static ItemStack soot()
    {
        return new ItemStack(DustItemRegistry.ALCHEMICAL_SOOT.get());
    }

    private void finish(ServerLevel level)
    {
        StillLayout.Stack stack = StillLayout.above(level, worldPosition);
        if (stack.top() == null) return;
        List<DustElement> elements = ItemApparatusBlockEntity.elementsOf(working);
        if (!ItemApparatusBlockEntity.solids(elements).isEmpty()) {
            if (!outputsFit(soot())) return;
            addOutput(soot());
        }
        rise(level, ItemApparatusBlockEntity.fluids(elements), stack);
        working = ItemStack.EMPTY;
        progress = -1;
        sync();
    }

    // The gaseous and aqueous elements up into the towers, the rest out of the top
    private void rise(ServerLevel level, List<DustElement> fluids, StillLayout.Stack stack)
    {
        Map<DustElement, Integer> counts = new LinkedHashMap<>();
        for (DustElement element : fluids) counts.merge(element, 1, Integer::sum);
        List<StillTowerBlockEntity> towers = new ArrayList<>();
        for (BlockPos pos : stack.towers()) {
            if (StillLayout.heated(worldPosition, pos) && level.getBlockEntity(pos) instanceof StillTowerBlockEntity tower) towers.add(tower);
        }
        for (Map.Entry<DustElement, Integer> entry : counts.entrySet()) {
            DustElement element = entry.getKey();
            int left = entry.getValue();
            if (knows(element)) {
                // The towers set to it, then empty unset ones, then empty ones set to something else
                for (int pass = 0; pass < 3 && left > 0; pass++) {
                    for (StillTowerBlockEntity tower : towers) {
                        if (left <= 0) break;
                        boolean fits = switch (pass) {
                            case 0 -> element.equals(tower.element());
                            case 1 -> tower.element() == null;
                            default -> tower.isEmpty();
                        };
                        if (fits) left -= tower.add(element, left);
                    }
                }
            }
            if (left > 0) release(level, stack.top(), element, left);
        }
    }

    // Out of the top into the chunk's aura, as smoke in the element's colour
    private void release(ServerLevel level, BlockPos top, DustElement element, int amount)
    {
        ChunkAura.release(level, top, element, amount);
        level.sendParticles(ColorParticleOption.create(DustParticleRegistry.STILL_SMOKE.get(), 0xFF000000 | element.color()),
                top.getX() + 0.5, top.getY() + 1.0, top.getZ() + 0.5, Math.min(8, 2 + amount), 0.08, 0.05, 0.08, 0.01);
    }

    // What's in its outputs into the containers beside it (each side's item handler, as much as each takes)
    private void pushOutputs(ServerLevel level)
    {
        boolean moved = false;
        for (Direction side : Direction.Plane.HORIZONTAL) {
            ResourceHandler<ItemResource> handler = level.getCapability(Capabilities.Item.BLOCK, worldPosition.relative(side), side.getOpposite());
            if (handler == null) continue;
            for (int i = 0; i < OUTPUT_SLOTS; i++) {
                if (outputs[i].isEmpty()) continue;
                // try-with-resources: the transaction closes however this is left, undone unless committed
                try (Transaction transaction = Transaction.openRoot()) {
                    int went = ResourceHandlerUtil.insertStacking(handler, ItemResource.of(outputs[i]), outputs[i].getCount(), transaction);
                    if (went <= 0) continue;
                    transaction.commit();
                    outputs[i].shrink(went);
                    if (outputs[i].isEmpty()) outputs[i] = ItemStack.EMPTY;
                    moved = true;
                }
            }
        }
        if (moved) sync();
    }

    // ---- the client ----

    // Counting on for the status; while it boils, white smoke out of the top
    @Override
    protected void clientWork(Level level)
    {
        if (progress < 0) return;
        if (progress < WORK_TICKS) progress++;
        if (level.getRandom().nextInt(3) != 0) return;
        BlockPos top = StillLayout.above(level, worldPosition).top();
        if (top == null) return;
        net.minecraft.util.RandomSource random = level.getRandom();
        level.addParticle(ColorParticleOption.create(DustParticleRegistry.STILL_SMOKE.get(), 0xFFFFFFFF), top.getX() + 0.5 + random.nextGaussian() * 0.05,
                top.getY() + 1.0, top.getZ() + 0.5 + random.nextGaussian() * 0.05, 0, 0.03, 0);
    }

    @Override
    protected Component activity()
    {
        if (progress >= 0) return Component.translatable("dust.apparatus.evaporating", working.getHoverName(), Math.round(Math.min(1F, progress / (float) WORK_TICKS) * 100));
        if (level == null) return Component.translatable("dust.apparatus.idle");
        StillFireboxBlockEntity firebox = firebox(level);
        if (firebox == null) return Component.translatable("dust.still.no_firebox");
        if (StillLayout.above(level, worldPosition).top() == null) return Component.translatable("dust.still.no_top");
        if (!input.isEmpty() && !firebox.canBurn()) return Component.translatable("dust.still.no_fuel_below");
        if (!input.isEmpty()) return Component.translatable("dust.apparatus.waiting", input.getCount(), input.getHoverName());
        return Component.translatable("dust.apparatus.idle");
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        store(output, "input", input);
        store(output, "working", working);
        output.putInt("progress", progress);
    }

    @Override
    protected void loadAdditional(ValueInput in)
    {
        super.loadAdditional(in);
        input = read(in, "input");
        working = read(in, "working");
        progress = in.getIntOr("progress", -1);
    }

    @Override
    protected List<ItemStack> dropsOnBreak()
    {
        List<ItemStack> drops = super.dropsOnBreak();
        drops.add(input);
        drops.add(working);
        return drops;
    }
}
