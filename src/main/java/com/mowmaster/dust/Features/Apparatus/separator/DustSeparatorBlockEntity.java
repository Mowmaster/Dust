package com.mowmaster.dust.Features.Apparatus.separator;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.Apparatus.ApparatusBlockEntity;
import com.mowmaster.dust.Features.Apparatus.ApparatusLayout;
import com.mowmaster.dust.Features.Apparatus.ItemApparatusBlockEntity;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.DustStorage.DustClouds;
import com.mowmaster.dust.Features.DustStorage.DustNetwork;
import com.mowmaster.dust.Features.Machines.Separation;
import com.mowmaster.dust.Features.Machines.StationSlot;
import com.mowmaster.dust.Features.Magic.ChunkAura;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// The Dust Separator's working: things fed in at its intake side (only things made of solid elements alone, not blocks:
// crush blocks first; hoppers and pipes reach it there only: slotsFor) tumble in the
// drum one at a time, WORK_TICKS each, and fall apart into their elements, every element whole:
//   solids     each drum section (this one, then each extension out to the open end: up to
//              DustSeparatorBlock.MAX_EXTENSIONS) catches one solid element, up to DrumSection.CAPACITY: a section set
//              to it, else a free one (empty and unlabelled). Each section sends its dust down the tubes under it
//              (DustNetwork.insert, PUSH_AMOUNT every PUSH_TICKS)
//   the rest   everything else (and solids no section can take) falls out of the open end of the drum as Dust Piles
//              (DustClouds.deposit; no higher than the drum). While EXCESS_LIMIT of it is waiting there (nowhere to
//              land), it stops taking things in
//   unknown    an element the owner hasn't discovered slips away into the aura
// The drum turns while it works (spinning, sent to clients; the extensions turn with it).
public class DustSeparatorBlockEntity extends ApparatusBlockEntity implements SectionHolder
{
    public static final int WORK_TICKS = 20;
    public static final int INPUT_LIMIT = 64;
    private static final int PUSH_AMOUNT = 20;
    private static final int EXCESS_LIMIT = 100;
    private static final int SPIN_LINGER = 20;
    private static final Codec<Map<DustElement, Integer>> EXCESS_CODEC = Codec.unboundedMap(DustElements.CODEC, Codec.INT);

    private ItemStack input = ItemStack.EMPTY;
    private ItemStack working = ItemStack.EMPTY;
    private int progress = -1;
    private final DrumSection section = new DrumSection();
    private final Map<DustElement, Integer> excess = new LinkedHashMap<>();
    private boolean spinning;
    private int spinLeft;

    public DustSeparatorBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.DUST_SEPARATOR.get(), pos, state);
    }

    @Override public DrumSection section() { return section; }
    @Override public void sectionChanged() { sync(); }
    public boolean isSpinning() { return spinning; }

    @Override
    protected List<StationSlot> machineSlots()
    {
        return List.of(StationSlot.ofStack(ApparatusLayout.INTAKE, DustSeparatorBlockEntity::accepts, INPUT_LIMIT, () -> input, stack -> input = stack, this::sync));
    }

    // Hoppers and pipes reach it from its intake side only (the blue crystal, to the left of its front: the right as it
    // was placed); every other side has no slots for them
    @Override
    public com.mowmaster.dust.Features.Machines.StationSlotHandler slotsFor(@org.jspecify.annotations.Nullable Direction side)
    {
        if (side == null || side == intakeSide()) return super.slotsFor(Direction.UP);
        return null;
    }

    public Direction intakeSide()
    {
        return getBlockState().getValue(com.mowmaster.dust.Features.Machines.StationBlock.FACING).getCounterClockWise();
    }

    // Only things made of solid elements alone (not a block: crush those first); anything with a gaseous or aqueous
    // element in it is for the Still
    public static boolean accepts(ItemStack stack)
    {
        if (ItemApparatusBlockEntity.isBlock(stack)) return false;
        List<DustElement> elements = ItemApparatusBlockEntity.elementsOf(stack);
        return !elements.isEmpty() && elements.stream().allMatch(Separation::isSolid);
    }

    // ---- the drum ----

    // Its sections in order: this one, then each extension out along the open side
    public List<SectionHolder> sections(Level level)
    {
        List<SectionHolder> sections = new ArrayList<>();
        sections.add(this);
        Direction open = DustSeparatorBlock.openSide(getBlockState());
        BlockPos at = worldPosition.relative(open);
        for (int i = 0; i < DustSeparatorBlock.MAX_EXTENSIONS; i++) {
            BlockState state = level.getBlockState(at);
            if (!(state.getBlock() instanceof DustSeparatorExtensionBlock) || state.getValue(DustSeparatorExtensionBlock.FACING) != getBlockState().getValue(com.mowmaster.dust.Features.Machines.StationBlock.FACING)) break;
            if (!(level.getBlockEntity(at) instanceof SectionHolder holder)) break;
            sections.add(holder);
            at = at.relative(open);
        }
        return sections;
    }

    // Where what's left falls out: the block past the last section
    private BlockPos openEnd(Level level, List<SectionHolder> sections)
    {
        BlockPos last = ((net.minecraft.world.level.block.entity.BlockEntity) sections.getLast()).getBlockPos();
        return last.relative(DustSeparatorBlock.openSide(getBlockState()));
    }

    // ---- working ----

    @Override
    protected void work(ServerLevel level)
    {
        long time = level.getGameTime() + worldPosition.asLong();
        List<SectionHolder> sections = sections(level);
        if (time % PUSH_TICKS == 0) {
            pushSections(level, sections);
            dropExcess(level, sections);
        }
        if (progress < 0) {
            if (!input.isEmpty() && excessTotal() < EXCESS_LIMIT) {
                working = input.split(1);
                if (input.isEmpty()) input = ItemStack.EMPTY;
                progress = 0;
                setSpinning(true);
                sync();
            } else if (spinning && --spinLeft <= 0) {
                setSpinning(false);
            }
            return;
        }
        if (++progress < WORK_TICKS) {
            setChanged();
            return;
        }
        separate(level, sections, ItemApparatusBlockEntity.elementsOf(working));
        working = ItemStack.EMPTY;
        progress = -1;
        spinLeft = SPIN_LINGER;
        level.playSound(null, worldPosition, SoundEvents.GRAVEL_HIT, SoundSource.BLOCKS, 0.4F, 0.8F);
        sync();
    }

    private void setSpinning(boolean spin)
    {
        if (spinning == spin) return;
        spinning = spin;
        sync();
    }

    private void separate(ServerLevel level, List<SectionHolder> sections, List<DustElement> elements)
    {
        Map<DustElement, Integer> counts = new LinkedHashMap<>();
        for (DustElement element : elements) counts.merge(element, 1, Integer::sum);
        for (Map.Entry<DustElement, Integer> entry : counts.entrySet()) {
            DustElement element = entry.getKey();
            int left = entry.getValue();
            if (!knows(element)) {
                ChunkAura.release(level, worldPosition, element, left);
                continue;
            }
            if (Separation.isSolid(element)) {
                for (int pass = 0; pass < 2 && left > 0; pass++) {
                    for (SectionHolder holder : sections) {
                        if (left <= 0) break;
                        DrumSection drum = holder.section();
                        if (pass == 0 ? !drum.holds(element) : !drum.free()) continue;
                        int moved = drum.add(element, left);
                        if (moved > 0) {
                            left -= moved;
                            holder.sectionChanged();
                        }
                    }
                }
            }
            if (left > 0) excess.merge(element, left, Integer::sum);
        }
    }

    // Each section's dust down the tubes under it
    private void pushSections(ServerLevel level, List<SectionHolder> sections)
    {
        for (SectionHolder holder : sections) {
            DrumSection drum = holder.section();
            if (drum.element() == null || drum.amount() <= 0) continue;
            BlockPos pos = ((net.minecraft.world.level.block.entity.BlockEntity) holder).getBlockPos();
            int went = DustNetwork.insert(level, pos, drum.element(), Math.min(PUSH_AMOUNT, drum.amount()));
            if (went > 0) {
                drum.take(went);
                holder.sectionChanged();
            }
        }
    }

    // What no section caught, out of the open end as Dust Piles
    private void dropExcess(ServerLevel level, List<SectionHolder> sections)
    {
        if (excess.isEmpty()) return;
        BlockPos end = openEnd(level, sections);
        boolean changed = false;
        for (Map.Entry<DustElement, Integer> entry : List.copyOf(excess.entrySet())) {
            int landed = DustClouds.deposit(level, end, entry.getKey(), Math.min(PUSH_AMOUNT, entry.getValue()), end.getY());
            if (landed <= 0) continue;
            changed = true;
            if (entry.getValue() - landed <= 0) excess.remove(entry.getKey());
            else excess.put(entry.getKey(), entry.getValue() - landed);
            level.sendParticles(new net.minecraft.core.particles.DustParticleOptions(entry.getKey().color(), 1.0F),
                    end.getX() + 0.5, end.getY() + 0.5, end.getZ() + 0.5, 4, 0.15, 0.2, 0.15, 0);
        }
        if (changed) setChanged();
    }

    private int excessTotal()
    {
        int total = 0;
        for (int amount : excess.values()) total += amount;
        return total;
    }

    @Override
    protected Component activity()
    {
        Component doing;
        if (progress >= 0) doing = Component.translatable("dust.apparatus.separating", working.getHoverName(), Math.round(Math.min(1F, progress / (float) WORK_TICKS) * 100));
        else if (excessTotal() >= EXCESS_LIMIT) doing = Component.translatable("dust.separator.end_blocked");
        else if (!input.isEmpty()) doing = Component.translatable("dust.apparatus.waiting", input.getCount(), input.getHoverName());
        else doing = Component.translatable("dust.apparatus.idle");
        if (level == null) return doing;
        int count = sections(level).size();
        return Component.empty().append(doing).append(" | ").append(Component.translatable("dust.separator.sections", count));
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        store(output, "input", input);
        store(output, "working", working);
        output.putInt("progress", progress);
        section.save(output);
        output.store("excess", EXCESS_CODEC, excess);
        output.putBoolean("spinning", spinning);
    }

    @Override
    protected void loadAdditional(ValueInput in)
    {
        super.loadAdditional(in);
        input = read(in, "input");
        working = read(in, "working");
        progress = in.getIntOr("progress", -1);
        section.load(in);
        excess.clear();
        in.read("excess", EXCESS_CODEC).ifPresent(excess::putAll);
        spinning = in.getBooleanOr("spinning", false);
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
