package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The Combination Table: two solid (crystalline / amorphous) Alchemical Dusts, one on each side pad, become Alchemical
// Dust of the element mixed from them (ElementCombining). Two that don't make an element won't start. Started with a
// Magical Flint and Steel: the glyph rises off the scroll spinning, both dusts are pulled off their pads into it
// (trailing their colours), and the new element streams from the glyph down to the output. (What an element is made from shows in its Elements entry once it's discovered.)
public class ResearchCombinationBlockEntity extends CBSyncedBlockEntity implements IStationSlots
{
    public static final int COMBINE_TICKS = 100;
    // The inputs are pulled into the glyph from PULL_START (once it's up) to DRAW_IN_TICKS; after that the result
    // streams out (CombinationLayout has the animation)
    public static final int PULL_START = 20;
    public static final int DRAW_IN_TICKS = 60;

    private ItemStack left = ItemStack.EMPTY;
    private ItemStack right = ItemStack.EMPTY;
    private ItemStack output = ItemStack.EMPTY;
    private ItemStack pending = ItemStack.EMPTY;
    private int progress = -1;

    public ResearchCombinationBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.RESEARCH_COMBINATION.get(), pos, state);
    }

    // The inputs as drawn: gone once they've been pulled into the glyph
    public ItemStack getLeft() { return progress >= DRAW_IN_TICKS ? ItemStack.EMPTY : left; }
    public ItemStack getRight() { return progress >= DRAW_IN_TICKS ? ItemStack.EMPTY : right; }
    public ItemStack getOutput() { return output; }
    public boolean isRunning() { return progress >= 0; }

    public float progress(float partialTick)
    {
        return progress < 0 ? -1 : Math.min(COMBINE_TICKS, progress + partialTick);
    }

    // ---- clicking (StationInteractions) ----

    @Override
    public List<StationSlot> slots()
    {
        return List.of(
                StationSlot.ofStack(CombinationLayout.LEFT_AREA, stack -> !isRunning() && AlchemicalDust.is(stack), 1,
                        () -> isRunning() ? ItemStack.EMPTY : left, stack -> left = stack, this::sync),
                StationSlot.ofStack(CombinationLayout.RIGHT_AREA, stack -> !isRunning() && AlchemicalDust.is(stack), 1,
                        () -> isRunning() ? ItemStack.EMPTY : right, stack -> right = stack, this::sync),
                StationSlot.output(CombinationLayout.OUTPUT_AREA, () -> output, stack -> output = stack, this::sync));
    }

    // ---- starting ----

    // Why it can't start now (a lang key suffix), or null if it can
    public @Nullable String cannotStart()
    {
        if (isRunning()) return "running";
        DustElement a = AlchemicalDust.of(left), b = AlchemicalDust.of(right);
        if (a == null || b == null) return "empty";
        if (!output.isEmpty()) return "outputs";
        if (!ElementCombining.combinedBySolids(a, b)) return "not_solid";
        if (ElementCombining.combine(a, b) == null) return "no_element";
        return null;
    }

    public void start(ServerLevel level)
    {
        DustElement made = ElementCombining.combine(AlchemicalDust.of(left), AlchemicalDust.of(right));
        pending = made == null ? ItemStack.EMPTY : AlchemicalDust.create(made);
        progress = 0;
        level.playSound(null, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 0.8F);
        sync();
    }

    // ---- ticking ----

    public static void serverTick(Level level, BlockPos pos, BlockState state, ResearchCombinationBlockEntity table)
    {
        if (table.progress < 0) return;
        table.progress++;
        if (table.progress == DRAW_IN_TICKS) {
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);
            table.sync();
        }
        if (table.progress < COMBINE_TICKS) {
            table.setChanged();
            return;
        }
        table.finish((ServerLevel) level);
    }

    // Client side: count for the animation; a trail behind each dust as it's pulled in, a flash of the new element
    // when they meet, then the new element streaming from the glyph to the output
    public static void clientTick(Level level, BlockPos pos, BlockState state, ResearchCombinationBlockEntity table)
    {
        if (table.progress < 0) return;
        if (table.progress < COMBINE_TICKS) table.progress++;
        float lift = CombinationLayout.lift(table.progress);
        Vec3 glyph = table.turned(CombinationLayout.glyphCentre(lift));
        if (table.progress < DRAW_IN_TICKS) {
            if (table.progress < PULL_START) return;
            float t = CombinationLayout.pull(table.progress);
            trail(level, pos, table.turned(CombinationLayout.pulled(CombinationLayout.LEFT_INPUT, t, lift)), AlchemicalDust.of(table.left));
            trail(level, pos, table.turned(CombinationLayout.pulled(CombinationLayout.RIGHT_INPUT, t, lift)), AlchemicalDust.of(table.right));
            return;
        }
        DustElement made = AlchemicalDust.of(table.pending);
        if (table.progress == DRAW_IN_TICKS && made != null) {
            for (int i = 0; i < 12; i++) {
                var random = level.getRandom();
                level.addParticle(new DustParticleOptions(made.color(), 0.8F), pos.getX() + glyph.x, pos.getY() + glyph.y, pos.getZ() + glyph.z,
                        (random.nextDouble() - 0.5) * 0.15, (random.nextDouble() - 0.5) * 0.1, (random.nextDouble() - 0.5) * 0.15);
            }
        }
        stream(level, pos, glyph, table.turned(CombinationLayout.OUTPUT), made);
    }

    // A puff left behind where a dust is (it hangs there as the dust moves on)
    private static void trail(Level level, BlockPos pos, Vec3 at, @Nullable DustElement element)
    {
        if (element == null) return;
        level.addParticle(new DustParticleOptions(element.color(), 0.5F), pos.getX() + at.x, pos.getY() + at.y, pos.getZ() + at.z, 0, 0, 0);
    }

    static void stream(Level level, BlockPos pos, Vec3 from, Vec3 to, @Nullable DustElement element)
    {
        if (element == null) return;
        Vec3 at = from.lerp(to, level.getRandom().nextDouble());
        Vec3 velocity = to.subtract(from).scale(0.08);
        level.addParticle(new DustParticleOptions(element.color(), 0.6F), pos.getX() + at.x, pos.getY() + at.y, pos.getZ() + at.z,
                velocity.x, velocity.y, velocity.z);
    }

    private void finish(ServerLevel level)
    {
        output = pending;
        pending = ItemStack.EMPTY;
        left = ItemStack.EMPTY;
        right = ItemStack.EMPTY;
        progress = -1;
        level.playSound(null, worldPosition, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1.0F, 1.1F);
        sync();
    }

    private Vec3 turned(Vec3 pixels)
    {
        return CrusherLayout.turned(pixels, getBlockState().getValue(StationBlock.FACING));
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput out)
    {
        super.saveAdditional(out);
        StationSaving.store(out, "left", left);
        StationSaving.store(out, "right", right);
        StationSaving.store(out, "output", output);
        StationSaving.store(out, "pending", pending);
        out.putInt("progress", progress);
    }

    @Override
    protected void loadAdditional(ValueInput in)
    {
        super.loadAdditional(in);
        left = StationSaving.read(in, "left");
        right = StationSaving.read(in, "right");
        output = StationSaving.read(in, "output");
        pending = StationSaving.read(in, "pending");
        progress = in.getIntOr("progress", -1);
    }

    // Broken: everything in it drops (a combination in progress gives back its dusts)
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level == null) return;
        for (ItemStack stack : List.of(left, right, output)) {
            if (!stack.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, stack);
        }
    }
}
