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

// A Still Pot. The bottom one (on the Burnable Firebox) runs the Still (StillLayout): what goes into any of its pots
// (hoppers and pipes from the top and sides, or right-click their tops) boils one at a time, WORK_TICKS each, while the
// firebox burns (it lights fuel as it's needed) and there's a Still Top on the stack:
//   solids     each pot holds one solid element as dust, up to POT_CAPACITY: a pot already holding it, else an empty
//              one (that its filter lets in). A solid no pot can take makes an extra Alchemical Soot, which the firebox
//              gives out with its own (StillFireboxBlockEntity.addSoot)
//   the rest   each gaseous or aqueous element rises into a heated tower (within StillLayout.HEAT_REACH of the firebox):
//              the tower already set to it, else an empty one (not one whose tubes can't take it:
//              StillTowerBlockEntity.isBlocked). What no tower takes (no tower for it, or full) leaves
//              through the top into the chunk's aura, as smoke in its colour. So a pot with only a top on it sends
//              everything into the aura.
//   unknown    an element the owner hasn't discovered always leaves through the top
// It only takes things with a gaseous or aqueous element in them (accepts): things of solids alone go to the Dust
// Separator, blocks to a Crushing Mill first. (Anything already in it that it can't boil passes through to its outputs,
// pushed into the containers beside the pot, a hopper feeding it last.)
// A pot's dust comes out of the Dust Chutes on its sides, as Dust Piles under them (DustChuteBlockEntity), CHUTE_RATE
// every CHUTE_TICKS. Its filter: a Jar Label on it (only on a pot holding an element: it's kept to that element, even
// empty), or a filtered chute on it; then it holds and lets out nothing else.
public class StillPotBlockEntity extends ApparatusBlockEntity
{
    public static final int WORK_TICKS = 20;
    public static final int INPUT_LIMIT = 64;
    public static final int SOOT_PUSH_TICKS = 10;

    public static final int POT_CAPACITY = 100;
    public static final int CHUTE_TICKS = 10;
    public static final int CHUTE_RATE = 5;

    private ItemStack input = ItemStack.EMPTY;
    private ItemStack working = ItemStack.EMPTY;
    private int progress = -1;
    // The solid element it holds, how much, and the sides with a Jar Label (any label keeps it to its element)
    private @Nullable DustElement stored;
    private int storedAmount;
    private final java.util.EnumSet<Direction> labels = java.util.EnumSet.noneOf(Direction.class);

    public StillPotBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.STILL_POT.get(), pos, state);
    }

    public boolean isWorking() { return progress >= 0; }
    public @Nullable DustElement stored() { return stored; }
    public int storedAmount() { return storedAmount; }
    public java.util.Set<Direction> labels() { return java.util.Collections.unmodifiableSet(labels); }

    // ---- the dust it holds ----

    // What it's kept to: its element while it has a label, else a filtered chute's element (null: anything)
    public @Nullable DustElement filter()
    {
        if (!labels.isEmpty() && stored != null) return stored;
        if (level == null) return null;
        for (Direction side : Direction.Plane.HORIZONTAL) {
            DustChuteBlockEntity chute = chute(level, side);
            if (chute != null && chute.filter() != null) return chute.filter();
        }
        return null;
    }

    // The chute on this side, if one is fixed to the pot there
    private @Nullable DustChuteBlockEntity chute(Level level, Direction side)
    {
        BlockPos pos = worldPosition.relative(side);
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof DustChuteBlock) || state.getValue(DustChuteBlock.FACING) != side) return null;
        return level.getBlockEntity(pos) instanceof DustChuteBlockEntity chute ? chute : null;
    }

    // Whether it would take this solid element: its filter lets it in, and it's empty or holding it already with room
    public boolean takes(DustElement element)
    {
        DustElement filter = filter();
        if (filter != null && !filter.equals(element)) return false;
        return (stored == null || stored.equals(element)) && storedAmount < POT_CAPACITY;
    }

    // Puts up to amount of it in; returns how much went
    public int store(DustElement element, int amount)
    {
        if (amount <= 0 || !takes(element)) return 0;
        int moved = Math.min(amount, POT_CAPACITY - storedAmount);
        stored = element;
        storedAmount += moved;
        sync();
        return moved;
    }

    // Takes up to amount out; an unlabelled pot forgets its element once it's empty
    public int take(int amount)
    {
        int taken = Math.min(amount, storedAmount);
        if (taken <= 0) return 0;
        storedAmount -= taken;
        if (storedAmount <= 0 && labels.isEmpty()) stored = null;
        sync();
        return taken;
    }

    // A Jar Label on this side: only on a pot holding an element (it's then kept to it)
    public boolean addLabel(Direction side)
    {
        if (stored == null || !side.getAxis().isHorizontal() || !labels.add(side)) return false;
        sync();
        return true;
    }

    public boolean removeLabel(Direction side)
    {
        if (!labels.remove(side)) return false;
        if (labels.isEmpty() && storedAmount <= 0) stored = null;
        sync();
        return true;
    }

    // Out of the chutes on its sides as Dust Piles under them
    private void pourChutes(ServerLevel level)
    {
        if (stored == null || storedAmount <= 0) return;
        for (Direction side : Direction.Plane.HORIZONTAL) {
            if (storedAmount <= 0 || stored == null) return;
            DustChuteBlockEntity chute = chute(level, side);
            if (chute == null || (chute.filter() != null && !chute.filter().equals(stored))) continue;
            int amount = Math.min(CHUTE_RATE, storedAmount);
            BlockPos under = worldPosition.relative(side).below();
            int landed = com.mowmaster.dust.Features.DustStorage.DustClouds.deposit(level, under, stored, amount, under.getY());
            if (landed > 0) {
                DustElement element = stored;
                take(landed);
                level.sendParticles(new net.minecraft.core.particles.DustParticleOptions(element.color(), 1.0F),
                        under.getX() + 0.5, under.getY() + 0.9, under.getZ() + 0.5, 4, 0.12, 0.2, 0.12, 0);
            }
        }
    }

    // Whether it's the bottom pot, the one running the still (on the firebox)
    private boolean isBottom(Level level)
    {
        return level.getBlockState(worldPosition.below()).getBlock() instanceof StillFireboxBlock;
    }

    // Takes the next thing to boil out of a pot's input (for the bottom pot, from the pots above)
    private ItemStack splitInput()
    {
        ItemStack one = input.split(1);
        if (input.isEmpty()) input = ItemStack.EMPTY;
        sync();
        return one;
    }

    @Override
    protected List<StationSlot> machineSlots()
    {
        return List.of(StationSlot.ofStack(ApparatusLayout.INTAKE, StillPotBlockEntity::accepts, INPUT_LIMIT, () -> input, stack -> input = stack, this::sync));
    }

    // Only things with a gaseous or aqueous element in them (not blocks: crush those first); things made of solids alone
    // are for the Dust Separator
    public static boolean accepts(ItemStack stack)
    {
        return !ItemApparatusBlockEntity.isBlock(stack) && !ItemApparatusBlockEntity.fluids(ItemApparatusBlockEntity.elementsOf(stack)).isEmpty();
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
        if ((level.getGameTime() + worldPosition.asLong()) % CHUTE_TICKS == 0) pourChutes(level);
        // Only the bottom pot runs the still (the others hold dust and take things in for it)
        if (!isBottom(level)) return;
        if (progress < 0) {
            tryStart(level);
            return;
        }
        // Only boils while it's hot (and while it's a still)
        StillFireboxBlockEntity firebox = firebox(level);
        StillLayout.Stack stack = StillLayout.above(level, worldPosition);
        if (firebox == null || stack.top() == null || !firebox.heat(level)) return;
        // Boiling: the towers show their colours
        for (BlockPos pos : stack.towers()) {
            if (level.getBlockEntity(pos) instanceof StillTowerBlockEntity tower) tower.keepActive(level.getGameTime());
        }
        if (++progress < WORK_TICKS) {
            setChanged();
            return;
        }
        finish(level);
    }

    private void tryStart(ServerLevel level)
    {
        // Its own input first, then the pots above's
        StillPotBlockEntity source = this;
        if (input.isEmpty()) {
            source = null;
            for (BlockPos pos : StillLayout.above(level, worldPosition).pots()) {
                if (level.getBlockEntity(pos) instanceof StillPotBlockEntity pot && !pot.input.isEmpty()) {
                    source = pot;
                    break;
                }
            }
            if (source == null) return;
        }
        source.startFrom(level, this);
    }

    // Starts boiling the next thing in this pot's input, in the bottom pot (or passes it through to this pot's outputs)
    private void startFrom(ServerLevel level, StillPotBlockEntity bottom)
    {
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
        StillFireboxBlockEntity firebox = bottom.firebox(level);
        if (firebox == null || StillLayout.above(level, bottom.worldPosition).top() == null || !firebox.heat(level)) return;
        bottom.working = splitInput();
        bottom.progress = 0;
        level.playSound(null, bottom.worldPosition, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.4F, 1.0F);
        bottom.sync();
    }

    private void finish(ServerLevel level)
    {
        StillLayout.Stack stack = StillLayout.above(level, worldPosition);
        if (stack.top() == null) return;
        List<DustElement> elements = ItemApparatusBlockEntity.elementsOf(working);
        settle(level, ItemApparatusBlockEntity.solids(elements), stack);
        rise(level, ItemApparatusBlockEntity.fluids(elements), stack);
        working = ItemStack.EMPTY;
        progress = -1;
        sync();
    }

    // The solid elements into the pots: one holding it already, then an empty one that lets it in. What no pot takes makes
    // one extra Alchemical Soot (the firebox gives it out); what the owner doesn't know leaves through the top.
    private void settle(ServerLevel level, List<DustElement> solids, StillLayout.Stack stack)
    {
        if (solids.isEmpty()) return;
        Map<DustElement, Integer> counts = new LinkedHashMap<>();
        for (DustElement element : solids) counts.merge(element, 1, Integer::sum);
        List<StillPotBlockEntity> pots = new ArrayList<>();
        pots.add(this);
        for (BlockPos pos : stack.pots()) if (level.getBlockEntity(pos) instanceof StillPotBlockEntity pot) pots.add(pot);
        boolean leftOver = false;
        for (Map.Entry<DustElement, Integer> entry : counts.entrySet()) {
            DustElement element = entry.getKey();
            int left = entry.getValue();
            if (!knows(element)) {
                release(level, stack.top(), element, left);
                continue;
            }
            for (int pass = 0; pass < 2 && left > 0; pass++) {
                for (StillPotBlockEntity pot : pots) {
                    if (left <= 0) break;
                    if ((pass == 0) != element.equals(pot.stored)) continue;
                    left -= pot.store(element, left);
                }
            }
            if (left > 0) leftOver = true;
        }
        StillFireboxBlockEntity firebox = firebox(level);
        if (leftOver && firebox != null) firebox.addSoot(level, 1);
    }

    // The gaseous and aqueous elements up into the towers, the rest out of the top
    private void rise(ServerLevel level, List<DustElement> fluids, StillLayout.Stack stack)
    {
        Map<DustElement, Integer> counts = new LinkedHashMap<>();
        for (DustElement element : fluids) counts.merge(element, 1, Integer::sum);
        List<StillTowerBlockEntity> towers = new ArrayList<>();
        for (BlockPos pos : stack.towers()) {
            // A blocked tower (its tubes reach jars of another element) takes nothing
            if (StillLayout.heated(worldPosition, pos) && level.getBlockEntity(pos) instanceof StillTowerBlockEntity tower && !tower.isBlocked()) towers.add(tower);
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

    // What's in its outputs into the containers beside it (each side's item handler, as much as each takes). Containers
    // that don't feed it come first (a chest), and only then a hopper pointing into it (it would only bring the soot
    // straight back)
    private void pushOutputs(ServerLevel level)
    {
        List<Direction> order = new ArrayList<>();
        List<Direction> feeders = new ArrayList<>();
        for (Direction side : Direction.Plane.HORIZONTAL) {
            if (StillPotBlock.feeds(level.getBlockState(worldPosition.relative(side)), side)) feeders.add(side);
            else order.add(side);
        }
        order.addAll(feeders);
        boolean moved = false;
        for (Direction side : order) {
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
        Component held = stored == null ? null : Component.translatable(labels.isEmpty() ? "dust.still.pot_holds" : "dust.still.pot_holds_labelled",
                storedAmount, POT_CAPACITY, stored.name());
        Component doing = doing();
        return held == null ? doing : Component.empty().append(doing).append(" | ").append(held);
    }

    private Component doing()
    {
        if (level != null && !isBottom(level)) return input.isEmpty() ? Component.translatable("dust.still.upper_pot")
                : Component.translatable("dust.apparatus.waiting", input.getCount(), input.getHoverName());
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
        if (stored != null) output.store("stored", com.mowmaster.dust.Features.DustComposition.DustElements.CODEC, stored);
        output.putInt("stored_amount", storedAmount);
        output.store("labels", Direction.CODEC.listOf(), List.copyOf(labels));
    }

    @Override
    protected void loadAdditional(ValueInput in)
    {
        super.loadAdditional(in);
        input = read(in, "input");
        working = read(in, "working");
        progress = in.getIntOr("progress", -1);
        stored = in.read("stored", com.mowmaster.dust.Features.DustComposition.DustElements.CODEC).orElse(null);
        storedAmount = in.getIntOr("stored_amount", 0);
        labels.clear();
        labels.addAll(in.read("labels", Direction.CODEC.listOf()).orElse(List.of()));
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
