package com.mowmaster.dust.Features.Apparatus.mixer;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.DustComposition.ElementNames;
import com.mowmaster.dust.Features.DustStorage.DustClouds;
import com.mowmaster.dust.Features.DustStorage.IDustHolder;
import com.mowmaster.dust.Features.Machines.ElementCombining;
import com.mowmaster.dust.Features.Machines.Separation;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// The Dust Mixer's cycle (the phase is sent to clients, which play it out: Client/DustMixerRenderer):
//   OPEN       the grate is open: dust piped in falls in, two solid elements that mix into another (ElementCombining),
//              up to SLOT_CAPACITY of each (it's an IDustHolder on its tube networks, taking dust only now). It gives
//              the tubes time to fill it: once both are full (and a moment more: FULL_SETTLE_TICKS), or once it has
//              some of both and nothing has come in for SETTLE_TICKS, the grate closes
//   CLOSING    the grate swings shut (GRATE_TICKS); it takes an even amount of each (as much as it has of the less)
//   MIXING     the paddles turn, MIX_TICKS_PER_DUST for each dust it'll make
//   OPENING    the grate swings open again
//   TIPPING    the bowl tips over TIP_DEGREES toward the front (TIP_TICKS)
//   DUMPING    what it made pours out in front of it as a Dust Pile (DustClouds.deposit), DUMP_RATE a tick, waiting
//              while there's nowhere for it to land
//   RETURNING  the bowl tips back, and it's OPEN again (what's left over of either element waits for the next batch)
// A redstone signal stops it: mixing stops (it makes as much as it's mixed so far; the rest of the batch comes out as
// it went in), it tips out everything it has, and stays tipped (HELD) until the signal goes.
public class DustMixerBlockEntity extends CBSyncedBlockEntity implements IDustHolder
{
    public static final int SLOT_CAPACITY = 100;
    public static final int MIX_TICKS_PER_DUST = 2;
    public static final int GRATE_TICKS = 10;
    public static final int TIP_TICKS = 20;
    public static final float TIP_DEGREES = 105F;
    // Nothing coming in for this long (tubes bring dust every 10 ticks), it starts; full of both, a moment after
    private static final int SETTLE_TICKS = 30;
    private static final int FULL_SETTLE_TICKS = 10;
    private static final int DUMP_RATE = 10;
    private static final Codec<Map<DustElement, Integer>> AMOUNTS_CODEC = Codec.unboundedMap(DustElements.CODEC, Codec.INT);

    public enum Phase { OPEN, CLOSING, MIXING, OPENING, TIPPING, DUMPING, HELD, RETURNING }

    private Phase phase = Phase.OPEN;
    // Ticks into the phase (the client counts on by itself between updates)
    private int phaseTicks;
    private @Nullable DustElement first, second;
    private int firstAmount, secondAmount;
    // The batch being mixed: how much of each, and the mixing ticks so far
    private int batch;
    private int mixed;
    private final Map<DustElement, Integer> dumping = new LinkedHashMap<>();
    private long lastInput;
    // When the first dust of this load came in (-1: none yet)
    private long fillStart = -1;
    private boolean powered;

    public DustMixerBlockEntity(BlockPos pos, BlockState state)
    {
        this(DustBlockEntityRegistry.DUST_MIXER.get(), pos, state);
    }

    // For a mixer of another kind (the Wet Dust Mixer: DustWetMixerBlockEntity)
    protected DustMixerBlockEntity(net.minecraft.world.level.block.entity.BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    // Whether this element can go in at all (the first of a pair): this one only mixes solids
    protected boolean takesAlone(DustElement dust)
    {
        return Separation.isSolid(dust);
    }

    // Whether these two go together in it: they mix into an element (both solid, as takesAlone says)
    protected boolean pairs(DustElement first, DustElement second)
    {
        return ElementCombining.combine(first, second) != null;
    }

    public Phase phase() { return phase; }
    public int phaseTicks() { return phaseTicks; }
    public @Nullable DustElement first() { return first; }
    public @Nullable DustElement second() { return second; }
    public int firstAmount() { return firstAmount; }
    public int secondAmount() { return secondAmount; }
    public int batch() { return batch; }
    public Map<DustElement, Integer> dumping() { return dumping; }

    // ---- dust coming in (IDustHolder: it's a store on its tube networks, but only takes, and only while open) ----

    @Override
    public int insertDust(DustElement dust, int count, boolean simulate)
    {
        if (phase != Phase.OPEN || powered || count <= 0 || !takesAlone(dust)) return 0;
        int moved;
        if (dust.equals(first) || (first == null && !dust.equals(second))) {
            moved = Math.min(count, SLOT_CAPACITY - firstAmount);
            if (moved > 0 && !simulate) {
                first = dust;
                firstAmount += moved;
            }
        } else if (dust.equals(second) || second == null) {
            DustElement other = first;
            if (other == null || !pairs(other, dust)) return 0;
            moved = Math.min(count, SLOT_CAPACITY - secondAmount);
            if (moved > 0 && !simulate) {
                second = dust;
                secondAmount += moved;
            }
        } else {
            return 0;
        }
        if (moved > 0 && !simulate) {
            if (level != null) {
                lastInput = level.getGameTime();
                if (fillStart < 0) fillStart = lastInput;
            }
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
        return element.equals(first) || element.equals(second);
    }

    // ---- the cycle ----

    private void enter(Phase next)
    {
        phase = next;
        phaseTicks = 0;
        sync();
    }

    public void serverTick(ServerLevel level)
    {
        boolean signal = level.hasNeighborSignal(worldPosition);
        if (signal != powered) {
            powered = signal;
            if (powered) stop();
            setChanged();
        }
        phaseTicks++;
        switch (phase) {
            case OPEN -> {
                if (firstAmount > 0 && secondAmount > 0 && first != null && second != null
                        && level.getGameTime() - lastInput >= ((firstAmount >= SLOT_CAPACITY && secondAmount >= SLOT_CAPACITY) ? FULL_SETTLE_TICKS : SETTLE_TICKS)) {
                    enter(Phase.CLOSING);
                    level.playSound(null, worldPosition, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 0.5F, 1.2F);
                }
            }
            case CLOSING -> {
                if (phaseTicks < GRATE_TICKS) return;
                fillStart = -1;
                batch = Math.min(firstAmount, secondAmount);
                firstAmount -= batch;
                secondAmount -= batch;
                mixed = 0;
                enter(Phase.MIXING);
            }
            case MIXING -> {
                mixed++;
                if (mixed % 10 == 0) level.playSound(null, worldPosition, SoundEvents.GRAVEL_STEP, SoundSource.BLOCKS, 0.4F, 0.9F);
                if (mixed < batch * MIX_TICKS_PER_DUST) {
                    setChanged();
                    return;
                }
                finishBatch(batch);
                enter(Phase.OPENING);
                level.playSound(null, worldPosition, SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS, 0.5F, 1.2F);
            }
            case OPENING -> {
                if (phaseTicks >= GRATE_TICKS) enter(Phase.TIPPING);
            }
            case TIPPING -> {
                if (phaseTicks >= TIP_TICKS) enter(Phase.DUMPING);
            }
            case DUMPING, HELD -> {
                dump(level);
                if (phase == Phase.DUMPING && dumping.isEmpty()) enter(powered ? Phase.HELD : Phase.RETURNING);
                else if (phase == Phase.HELD && !powered && dumping.isEmpty()) enter(Phase.RETURNING);
            }
            case RETURNING -> {
                if (phaseTicks >= TIP_TICKS) enter(Phase.OPEN);
            }
        }
    }

    // Mixing done: the batch into what's made of the two
    private void finishBatch(int made)
    {
        DustElement child = first == null || second == null ? null : ElementCombining.combine(first, second);
        if (child != null && made > 0) dumping.merge(child, made, Integer::sum);
        batch = 0;
        mixed = 0;
        forgetEmpty();
    }

    // A redstone signal: what's been mixed so far is made, the rest comes out as it went in, and it tips it all out
    private void stop()
    {
        switch (phase) {
            case MIXING, CLOSING -> {
                int made = phase == Phase.MIXING ? Math.min(batch, mixed / MIX_TICKS_PER_DUST) : 0;
                int unmixed = batch - made;
                if (first != null) firstAmount += unmixed;
                if (second != null) secondAmount += unmixed;
                finishBatch(made);
                spillSlots();
                enter(Phase.OPENING);
            }
            case OPEN -> {
                spillSlots();
                enter(Phase.TIPPING);
            }
            default -> { }
        }
    }

    // Everything waiting to be mixed, tipped out as it is
    private void spillSlots()
    {
        if (first != null && firstAmount > 0) dumping.merge(first, firstAmount, Integer::sum);
        if (second != null && secondAmount > 0) dumping.merge(second, secondAmount, Integer::sum);
        firstAmount = 0;
        secondAmount = 0;
        fillStart = -1;
        forgetEmpty();
    }

    private void forgetEmpty()
    {
        if (firstAmount <= 0) first = null;
        if (secondAmount <= 0) second = null;
        // An element left on its own moves to the first slot
        if (first == null && second != null) {
            first = second;
            firstAmount = secondAmount;
            second = null;
            secondAmount = 0;
        }
    }

    // Out in front of it as a Dust Pile
    private void dump(ServerLevel level)
    {
        if (dumping.isEmpty()) return;
        BlockPos front = worldPosition.relative(getBlockState().getValue(DustMixerBlock.FACING));
        Map.Entry<DustElement, Integer> entry = dumping.entrySet().iterator().next();
        int landed = DustClouds.deposit(level, front, entry.getKey(), Math.min(DUMP_RATE, entry.getValue()), front.getY());
        if (landed <= 0) return;
        if (entry.getValue() - landed <= 0) dumping.remove(entry.getKey());
        else dumping.put(entry.getKey(), entry.getValue() - landed);
        level.sendParticles(new net.minecraft.core.particles.DustParticleOptions(entry.getKey().color(), 1.0F),
                front.getX() + 0.5, front.getY() + 0.6, front.getZ() + 0.5, 5, 0.2, 0.2, 0.2, 0);
        // Sent to clients: the dust in the bowl goes down as it pours out
        sync();
    }

    // ---- the client ----

    public void clientTick()
    {
        phaseTicks++;
    }

    // "Dust Mixer: mixing (40%) | 60 Stone, 60 Fire"
    public Component status(Player player)
    {
        MutableComponent line = Component.empty().append(getBlockState().getBlock().getName()).append(": ");
        line.append(switch (phase) {
            case OPEN -> Component.translatable("dust.mixer.open");
            case MIXING -> Component.translatable("dust.mixer.mixing", batch <= 0 ? 0 : 100 * mixed / (batch * MIX_TICKS_PER_DUST));
            case HELD -> Component.translatable("dust.mixer.held");
            case DUMPING -> Component.translatable("dust.mixer.dumping");
            default -> Component.translatable("dust.mixer.moving");
        });
        if (first != null) line.append(" | " + firstAmount + " ").append(ElementNames.forViewer(first, player));
        if (second != null) line.append(", " + secondAmount + " ").append(ElementNames.forViewer(second, player));
        return line;
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.putString("phase", phase.name());
        output.putInt("phase_ticks", phaseTicks);
        if (first != null) output.store("first", DustElements.CODEC, first);
        if (second != null) output.store("second", DustElements.CODEC, second);
        output.putInt("first_amount", firstAmount);
        output.putInt("second_amount", secondAmount);
        output.putInt("batch", batch);
        output.putInt("mixed", mixed);
        output.store("dumping", AMOUNTS_CODEC, dumping);
        output.putBoolean("powered", powered);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        try {
            phase = Phase.valueOf(input.getStringOr("phase", Phase.OPEN.name()));
        } catch (IllegalArgumentException e) {
            phase = Phase.OPEN;
        }
        phaseTicks = input.getIntOr("phase_ticks", 0);
        first = input.read("first", DustElements.CODEC).orElse(null);
        second = input.read("second", DustElements.CODEC).orElse(null);
        firstAmount = input.getIntOr("first_amount", 0);
        secondAmount = input.getIntOr("second_amount", 0);
        batch = input.getIntOr("batch", 0);
        mixed = input.getIntOr("mixed", 0);
        dumping.clear();
        input.read("dumping", AMOUNTS_CODEC).ifPresent(dumping::putAll);
        powered = input.getBooleanOr("powered", false);
    }

    // Broken: what's in it comes out as element orbs
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (!(level instanceof ServerLevel server)) return;
        if (batch > 0) {
            if (first != null) firstAmount += batch;
            if (second != null) secondAmount += batch;
            batch = 0;
        }
        spillSlots();
        for (Map.Entry<DustElement, Integer> entry : List.copyOf(dumping.entrySet())) {
            com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity.spawn(server, entry.getKey(), entry.getValue(), net.minecraft.world.phys.Vec3.atCenterOf(pos));
        }
        dumping.clear();
    }
}
