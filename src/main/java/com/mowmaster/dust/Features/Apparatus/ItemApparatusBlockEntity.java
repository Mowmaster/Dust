package com.mowmaster.dust.Features.Apparatus;

import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.DustComposition.DustCompositions;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Machines.Separation;
import com.mowmaster.dust.Features.Machines.StationSlot;
import com.mowmaster.dust.Features.Magic.ChunkAura;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The apparatus that work through items one at a time (the Crushing Mill, the Alchemical Separator, the Evaporating
// Kettle, the Alembic):
//   input      a stack of one kind (up to INPUT_LIMIT), fed in at the intake (right-click it, or hoppers and pipes from
//              the top and sides)
//   working    one item taken from the input, workTicks() long. What it makes (process) is worked out at the start,
//              and it only starts if there's room for it all (the outputs, and the dust waiting to go); else it waits.
//   passing    anything it can't work on (process gives null) goes straight to the outputs, a stack at a time
// What an item holds is read as its dust composition (DustCompositions: a compound its elements, Alchemical Dust and
// crystals their element, a process product the material it was made from, anything else its data or recipe), every
// element whole: industry doesn't break elements down into their parts the way research does.
public abstract class ItemApparatusBlockEntity extends ApparatusBlockEntity
{
    public static final int INPUT_LIMIT = 64;

    // What a piece of work makes: dust (one for each element listed; what the owner doesn't know slips away into the
    // aura), what's left of the item (handed on to the outputs), and elements boiled off into the aura
    protected record Result(List<DustElement> dust, ItemStack remainder, List<DustElement> toAura) {}

    protected ItemStack input = ItemStack.EMPTY;
    protected ItemStack working = ItemStack.EMPTY;
    protected int progress = -1;

    protected ItemApparatusBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    // How long one item takes (ticks)
    protected abstract int workTicks();

    // What it makes of one item, or null if it can't work on it (it's passed straight through)
    protected abstract @Nullable Result process(ItemStack one);

    // The status line's verb, a lang key: dust.apparatus.<verb> "crushing %s (%s%%)"
    protected abstract String verb();

    // A piece of work started / finished (sounds, particles)
    protected void started(ServerLevel level) {}

    protected void finished(ServerLevel level, Result result) {}

    public ItemStack getInput() { return input; }
    public ItemStack getWorking() { return working; }
    public boolean isWorking() { return progress >= 0; }

    // 0-1 through the item being worked, or -1 when idle
    public float progress(float partialTick)
    {
        return progress < 0 ? -1 : Math.min(1F, (progress + partialTick) / workTicks());
    }

    // ---- reading items ----

    // The elements an item holds, as the apparatus read them (repeats kept: 9 Trees in a compound is 9 Trees)
    public static List<DustElement> elementsOf(ItemStack stack)
    {
        DustComposition composition = DustCompositions.of(stack);
        return composition == null ? List.of() : composition.elements();
    }

    public static List<DustElement> solids(List<DustElement> elements)
    {
        return elements.stream().filter(Separation::isSolid).toList();
    }

    // Gaseous and aqueous
    public static List<DustElement> fluids(List<DustElement> elements)
    {
        return elements.stream().filter(element -> !Separation.isSolid(element)).toList();
    }

    // Blocks go through the Crushing Mill first (as at the research stations); the other apparatus pass them on
    public static boolean isBlock(ItemStack stack)
    {
        return stack.getItem() instanceof BlockItem;
    }

    // ---- slots ----

    @Override
    protected List<StationSlot> machineSlots()
    {
        return List.of(StationSlot.ofStack(ApparatusLayout.INTAKE, stack -> true, INPUT_LIMIT, () -> input, stack -> input = stack, this::sync));
    }

    // ---- working ----

    @Override
    protected void work(ServerLevel level)
    {
        if (progress < 0) {
            tryStart(level);
            return;
        }
        if (progress < workTicks()) {
            progress++;
            setChanged();
            if (progress < workTicks()) return;
        }
        finish(level);
    }

    private void tryStart(ServerLevel level)
    {
        if (input.isEmpty()) return;
        Result result = process(input.copyWithCount(1));
        if (result == null) {
            // Passed straight through: as many as fit
            int moving = 0;
            while (moving < input.getCount() && outputsFit(input.copyWithCount(moving + 1))) moving++;
            if (moving <= 0) return;
            addOutput(input.split(moving));
            if (input.isEmpty()) input = ItemStack.EMPTY;
            sync();
            return;
        }
        if (!fits(result)) return;
        working = input.split(1);
        if (input.isEmpty()) input = ItemStack.EMPTY;
        progress = 0;
        started(level);
        sync();
    }

    private boolean fits(Result result)
    {
        return outputsFit(result.remainder()) && knownCount(result.dust()) <= dustRoom();
    }

    // Done: the dust caught, anything boiled off released, what's left handed on. (If there's no room after all, it
    // waits here.)
    private void finish(ServerLevel level)
    {
        Result result = process(working);
        if (result != null && !fits(result)) return;
        if (result != null) {
            catchDust(level, result.dust());
            for (DustElement element : result.toAura()) ChunkAura.release(level, worldPosition, element, 1);
            if (!result.remainder().isEmpty()) addOutput(result.remainder());
            finished(level, result);
        }
        else addOutput(working);
        working = ItemStack.EMPTY;
        progress = -1;
        sync();
    }

    @Override
    protected void clientWork(Level level)
    {
        if (progress >= 0 && progress < workTicks()) progress++;
    }

    @Override
    protected Component activity()
    {
        if (progress >= 0) return Component.translatable("dust.apparatus." + verb(), working.getHoverName(), Math.round(progress(0) * 100));
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
