package com.mowmaster.dust.Features.Apparatus.centrifuge;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.Apparatus.still.DustChutes;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.DustComposition.ElementNames;
import com.mowmaster.dust.Features.DustStorage.IDustHolder;
import com.mowmaster.dust.Features.Machines.Separation;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

// The Dust Centrifuge's cycle (the phase is sent to clients, which play it out: Client/DustCentrifugeRenderer):
//   FILLING    a gaseous or aqueous element made of two others, piped in at its back (it's an IDustHolder on its tube
//              network, taking dust in at that side only, and only now), fills its four vials, up to CAPACITY. Once
//              they're full (or nothing more has come for START_IDLE ticks with some in them), it spins
//   SPIN_UP    the arms and vials spin up, the vials swinging out as they go faster
//   SPINNING   full speed
//   SPIN_DOWN  slowing to a stop: the element has come apart, into as much of each of the two it's made of (two vials of
//              one, two of the other)
//   DRAINING   it drains out of the Dust Chutes on its sides (DRAIN_RATE a chute every DRAIN_TICKS; a filtered chute only
//              lets out its element), waiting while there's no chute for one; empty, it fills again
public class DustCentrifugeBlockEntity extends CBSyncedBlockEntity implements IDustHolder, com.mowmaster.dust.Features.Guide.GuideAnimated
{
    public static final int CAPACITY = 100;
    public static final int SPIN_UP_TICKS = 30;
    public static final int SPIN_TICKS = 80;
    public static final int SPIN_DOWN_TICKS = 40;
    // Degrees a tick at full speed, and how far the vials swing out
    public static final float MAX_SPEED = 36F;
    public static final float MAX_TILT = 75F;
    private static final int START_IDLE = 100;
    private static final int DRAIN_TICKS = 10;
    private static final int DRAIN_RATE = 5;

    public enum Phase { FILLING, SPIN_UP, SPINNING, SPIN_DOWN, DRAINING }

    private Phase phase = Phase.FILLING;
    private int phaseTicks;
    private @Nullable DustElement input;
    private int amount;
    private long lastInput;
    // What it came apart into, how much of each is left, and how much each began with (for the vials)
    private @Nullable DustElement first, second;
    private int firstLeft, secondLeft, made;
    // Client: how far round the arms have turned (degrees), last tick and now
    private float angle, lastAngle;

    public DustCentrifugeBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.DUST_CENTRIFUGE.get(), pos, state);
    }

    public Phase phase() { return phase; }
    public int phaseTicks() { return phaseTicks; }
    public @Nullable DustElement input() { return input; }
    public int amount() { return amount; }
    public @Nullable DustElement first() { return first; }
    public @Nullable DustElement second() { return second; }
    public int firstLeft() { return firstLeft; }
    public int secondLeft() { return secondLeft; }
    public int made() { return made; }

    // How far round the arms are (degrees), between ticks
    public float angle(float partialTick)
    {
        return lastAngle + (angle - lastAngle) * partialTick;
    }

    // How fast it's spinning now, 0-1 of full speed
    public float speedShare(float partialTick)
    {
        float t = phaseTicks + partialTick;
        return switch (phase) {
            case SPIN_UP -> Math.min(1, t / SPIN_UP_TICKS);
            case SPINNING -> 1;
            case SPIN_DOWN -> Math.max(0, 1 - t / SPIN_DOWN_TICKS);
            default -> 0;
        };
    }

    // Whether this element can be spun apart here: gaseous or aqueous, and made of two others
    public static boolean accepts(DustElement element)
    {
        return !Separation.isSolid(element) && element.parts().size() == 2;
    }

    // ---- dust coming in (only at its back, only while filling) ----

    @Override
    public boolean takesDustFrom(Direction side)
    {
        return side == getBlockState().getValue(DustCentrifugeBlock.FACING).getOpposite();
    }

    @Override
    public int insertDust(DustElement dust, int count, boolean simulate)
    {
        if (phase != Phase.FILLING || count <= 0 || !accepts(dust) || (input != null && !input.equals(dust))) return 0;
        int moved = Math.min(count, CAPACITY - amount);
        if (moved > 0 && !simulate) {
            input = dust;
            amount += moved;
            if (level != null) lastInput = level.getGameTime();
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
    public int extractDust(DustElement element, int count, boolean simulate)
    {
        return 0;
    }

    @Override
    public boolean prefersDust(DustElement element)
    {
        return element.equals(input);
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
        phaseTicks++;
        switch (phase) {
            case FILLING -> {
                if (input != null && (amount >= CAPACITY || (amount >= 4 && level.getGameTime() - lastInput >= START_IDLE))) {
                    enter(Phase.SPIN_UP);
                    level.playSound(null, worldPosition, SoundEvents.MINECART_RIDING, SoundSource.BLOCKS, 0.3F, 1.6F);
                }
            }
            case SPIN_UP -> {
                if (phaseTicks >= SPIN_UP_TICKS) enter(Phase.SPINNING);
            }
            case SPINNING -> {
                if (phaseTicks % 20 == 0) level.playSound(null, worldPosition, SoundEvents.MINECART_RIDING, SoundSource.BLOCKS, 0.2F, 1.8F);
                if (phaseTicks >= SPIN_TICKS) enter(Phase.SPIN_DOWN);
            }
            case SPIN_DOWN -> {
                if (phaseTicks < SPIN_DOWN_TICKS) return;
                if (input != null) {
                    first = input.parts().get(0);
                    second = input.parts().get(1);
                    firstLeft = secondLeft = made = amount;
                }
                input = null;
                amount = 0;
                enter(Phase.DRAINING);
            }
            case DRAINING -> {
                if (phaseTicks % DRAIN_TICKS != 0) return;
                boolean changed = false;
                if (first != null && firstLeft > 0) {
                    int landed = DustChutes.pour(level, worldPosition, first, DRAIN_RATE, firstLeft);
                    firstLeft -= landed;
                    changed |= landed > 0;
                }
                if (second != null && secondLeft > 0) {
                    int landed = DustChutes.pour(level, worldPosition, second, DRAIN_RATE, secondLeft);
                    secondLeft -= landed;
                    changed |= landed > 0;
                }
                if (firstLeft <= 0 && secondLeft <= 0) {
                    first = second = null;
                    firstLeft = secondLeft = made = 0;
                    enter(Phase.FILLING);
                } else if (changed) {
                    sync();
                }
            }
        }
    }

    // ---- the client ----

    public void clientTick()
    {
        phaseTicks++;
        lastAngle = angle;
        angle += speedShare(0) * MAX_SPEED;
        if (angle >= 360) {
            angle -= 360;
            lastAngle -= 360;
        }
    }

    // In a guide scene (Guide/GuideAnimated) nothing comes from the server, so once the scene starts it spinning (a
    // "data" step setting the phase to SPIN_UP) the cycle plays on by itself as the server runs it: up to speed, spinning,
    // down, then it drains as if through a chute for each element (DRAIN_RATE every DRAIN_TICKS), and fills again
    @Override
    public void guideTick()
    {
        clientTick();
        switch (phase) {
            case SPIN_UP -> {
                if (phaseTicks >= SPIN_UP_TICKS) guideEnter(Phase.SPINNING);
            }
            case SPINNING -> {
                if (phaseTicks >= SPIN_TICKS) guideEnter(Phase.SPIN_DOWN);
            }
            case SPIN_DOWN -> {
                if (phaseTicks < SPIN_DOWN_TICKS) return;
                if (input != null && input.parts().size() == 2) {
                    first = input.parts().get(0);
                    second = input.parts().get(1);
                    firstLeft = secondLeft = made = amount;
                }
                input = null;
                amount = 0;
                guideEnter(Phase.DRAINING);
            }
            case DRAINING -> {
                if (phaseTicks % DRAIN_TICKS != 0) return;
                firstLeft = Math.max(0, firstLeft - DRAIN_RATE);
                secondLeft = Math.max(0, secondLeft - DRAIN_RATE);
                if (firstLeft <= 0 && secondLeft <= 0) {
                    first = second = null;
                    made = 0;
                    guideEnter(Phase.FILLING);
                }
            }
            default -> { }
        }
    }

    private void guideEnter(Phase next)
    {
        phase = next;
        phaseTicks = 0;
    }

    // "Dust Centrifuge: filling 40/100 Air" / "spinning" / "draining 30 Fire, 30 Water"
    public Component status(Player player)
    {
        MutableComponent line = Component.empty().append(getBlockState().getBlock().getName()).append(": ");
        switch (phase) {
            case FILLING -> {
                if (input == null) line.append(Component.translatable("dust.centrifuge.empty"));
                else line.append(Component.translatable("dust.centrifuge.filling", amount, CAPACITY)).append(" ").append(ElementNames.forViewer(input, player));
            }
            case DRAINING -> {
                line.append(Component.translatable("dust.centrifuge.draining"));
                if (first != null) line.append(" " + firstLeft + " ").append(ElementNames.forViewer(first, player));
                if (second != null) line.append(", " + secondLeft + " ").append(ElementNames.forViewer(second, player));
                if (level instanceof ServerLevel server && ((first != null && firstLeft > 0 && !DustChutes.anyFor(server, worldPosition, first))
                        || (second != null && secondLeft > 0 && !DustChutes.anyFor(server, worldPosition, second)))) {
                    line.append(" | ").append(Component.translatable("dust.centrifuge.needs_chute"));
                }
            }
            default -> line.append(Component.translatable("dust.centrifuge.spinning"));
        }
        return line;
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.putString("phase", phase.name());
        output.putInt("phase_ticks", phaseTicks);
        if (input != null) output.store("input", DustElements.CODEC, input);
        output.putInt("amount", amount);
        if (first != null) output.store("first", DustElements.CODEC, first);
        if (second != null) output.store("second", DustElements.CODEC, second);
        output.putInt("first_left", firstLeft);
        output.putInt("second_left", secondLeft);
        output.putInt("made", made);
    }

    @Override
    protected void loadAdditional(ValueInput in)
    {
        super.loadAdditional(in);
        try {
            phase = Phase.valueOf(in.getStringOr("phase", Phase.FILLING.name()));
        } catch (IllegalArgumentException e) {
            phase = Phase.FILLING;
        }
        phaseTicks = in.getIntOr("phase_ticks", 0);
        input = in.read("input", DustElements.CODEC).orElse(null);
        amount = in.getIntOr("amount", 0);
        first = in.read("first", DustElements.CODEC).orElse(null);
        second = in.read("second", DustElements.CODEC).orElse(null);
        firstLeft = in.getIntOr("first_left", 0);
        secondLeft = in.getIntOr("second_left", 0);
        made = in.getIntOr("made", 0);
    }

    // Broken: what's in it comes out as element orbs
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (!(level instanceof ServerLevel server)) return;
        var at = net.minecraft.world.phys.Vec3.atCenterOf(pos);
        if (input != null && amount > 0) com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity.spawn(server, input, amount, at);
        if (first != null && firstLeft > 0) com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity.spawn(server, first, firstLeft, at);
        if (second != null && secondLeft > 0) com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity.spawn(server, second, secondLeft, at);
        input = first = second = null;
        amount = firstLeft = secondLeft = 0;
    }
}
