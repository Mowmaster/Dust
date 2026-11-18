package com.mowmaster.dust.Features.Apparatus;

import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.ElementNames;
import com.mowmaster.dust.Features.DustStorage.DustNetwork;
import com.mowmaster.dust.Features.Machines.AlchemicalDust;
import com.mowmaster.dust.Features.Machines.StationSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// The apparatus that work on dust (the Transmuter, the Conjoining Crucible, the Mixing Vat):
//   pattern    what to make: an element's crystal, dust or Alchemical Dust, put on the pattern plate on the front by hand
//              (no hopper or pipe reaches it: StationSlot.Role.MANUAL), and only an element the owner has discovered.
//              It stays there (left-click takes it back while it isn't working).
//   a piece    once everything a piece of work needs is there (needs(): the dust, on the Dust Jars of its tube network
//              or right against it, and whatever else canStart() asks for), it draws it all at once (the dust runs
//              along the tubes to it) and works for workTicks(), then makes the piece (make)
public abstract class DustApparatusBlockEntity extends ApparatusBlockEntity
{
    protected ItemStack pattern = ItemStack.EMPTY;
    protected int progress = -1;

    protected DustApparatusBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    // Why this item can't be its pattern (a lang key suffix: dust.apparatus.pattern.<problem>), or null if it can
    protected abstract @Nullable String patternProblem(ItemStack stack, DustElement element);

    // The dust one piece of work takes, from the pattern
    protected abstract Map<DustElement, Integer> needs();

    // Whether a piece can start (room for what it makes, any supplies it uses), besides the dust
    protected abstract boolean canStart();

    // A piece starts (use up any supplies)
    protected void begin(ServerLevel level) {}

    // A piece is done: make it
    protected abstract void make(ServerLevel level);

    protected abstract int workTicks();

    // The status line's verb (dust.apparatus.<verb>: "making %s (%s%%)")
    protected abstract String verb();

    // Its other slots (a supply), before the pattern
    protected List<StationSlot> extraSlots()
    {
        return List.of();
    }

    public ItemStack getPattern() { return pattern; }
    public boolean isWorking() { return progress >= 0; }

    public float progress(float partialTick)
    {
        return progress < 0 ? -1 : Math.min(1F, (progress + partialTick) / workTicks());
    }

    // The element an item stands for as a pattern: a crystal's, an element dust's, Alchemical Dust's (null: none)
    public static @Nullable DustElement elementOf(ItemStack stack)
    {
        DustElement element = CrystalElements.of(stack);
        if (element == null) element = CrystalElements.ofDust(stack);
        if (element == null) element = AlchemicalDust.of(stack);
        return element;
    }

    public @Nullable DustElement patternElement()
    {
        return elementOf(pattern);
    }

    // ---- slots ----

    @Override
    protected List<StationSlot> machineSlots()
    {
        List<StationSlot> slots = new ArrayList<>(extraSlots());
        slots.add(new StationSlot(ApparatusLayout.PATTERN, stack -> pattern.isEmpty() && !isWorking(), this::putPattern,
                () -> isWorking() ? ItemStack.EMPTY : pattern, this::takePattern).manual());
        return slots;
    }

    // One of the held item as the pattern, if it stands for an element the owner knows and this apparatus can make;
    // otherwise the player is told why not and nothing goes in
    private int putPattern(ItemStack stack, @Nullable Player player)
    {
        if (player == null || isWorking() || !pattern.isEmpty()) return 0;
        DustElement element = elementOf(stack);
        String problem = element == null ? "not_element" : !knows(element) ? "unknown" : patternProblem(stack, element);
        if (problem != null) {
            player.sendOverlayMessage(element == null ? Component.translatable("dust.apparatus.pattern.not_element")
                    : Component.translatable("dust.apparatus.pattern." + problem, ElementNames.forViewer(element, player)));
            return 0;
        }
        pattern = stack.copyWithCount(1);
        sync();
        return 1;
    }

    private ItemStack takePattern(int max)
    {
        if (isWorking() || pattern.isEmpty() || max <= 0) return ItemStack.EMPTY;
        ItemStack taken = pattern;
        pattern = ItemStack.EMPTY;
        sync();
        return taken;
    }

    // ---- working ----

    @Override
    protected void work(ServerLevel level)
    {
        if (progress < 0) {
            tryStart(level);
            return;
        }
        if (++progress < workTicks()) {
            setChanged();
            return;
        }
        make(level);
        progress = -1;
        sync();
    }

    // Starts when the pattern's element is still known, there's room, and the network holds all the dust it needs
    private void tryStart(ServerLevel level)
    {
        DustElement element = patternElement();
        if (element == null || !knows(element) || !canStart()) return;
        Map<DustElement, Integer> needs = needs();
        if (needs.isEmpty()) return;
        for (Map.Entry<DustElement, Integer> need : needs.entrySet()) {
            if (DustNetwork.available(level, worldPosition, need.getKey()) < need.getValue()) return;
        }
        for (Map.Entry<DustElement, Integer> need : needs.entrySet()) DustNetwork.extract(level, worldPosition, need.getKey(), need.getValue());
        begin(level);
        progress = 0;
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
        DustElement element = patternElement();
        if (element == null) return Component.translatable("dust.apparatus.no_pattern");
        // The owner knows it (only known elements go on as patterns), so it's shown by name
        Component name = ElementNames.named(element, true);
        if (progress >= 0) return Component.translatable("dust.apparatus." + verb(), name, Math.round(progress(0) * 100));
        if (!knows(element)) return Component.translatable("dust.apparatus.pattern_unknown", name);
        return Component.translatable("dust.apparatus.waiting_dust", name);
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        store(output, "pattern", pattern);
        output.putInt("progress", progress);
    }

    @Override
    protected void loadAdditional(ValueInput in)
    {
        super.loadAdditional(in);
        pattern = read(in, "pattern");
        progress = in.getIntOr("progress", -1);
    }

    @Override
    protected List<ItemStack> dropsOnBreak()
    {
        List<ItemStack> drops = super.dropsOnBreak();
        drops.add(pattern);
        return drops;
    }
}
