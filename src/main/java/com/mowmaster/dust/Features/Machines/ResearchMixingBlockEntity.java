package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import com.mowmaster.dust.Features.Research.ProcessRecord;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
import java.util.UUID;

// The Mixing Table: two Alchemical Dusts, one on each side pad, at least one of them gaseous or aqueous, become
// Alchemical Dust of the element mixed from them (ElementCombining), if there is one. Like the Evaporation Table:
//   idle         the dusts on their pads
//   stirring     started with a Magical Flint and Steel: both dusts pour into the pot (half full, their colours mixed)
//                and the glyph spins over it; right-click the pot to stir: STIRS full turns of the spoon, the liquid
//                turning to the new element's colour
//   settling     (SETTLE_TICKS) the glyph sinks into the pot, and the new dust streams to the output
public class ResearchMixingBlockEntity extends CBSyncedBlockEntity implements IStationSlots
{
    public static final int STIRS = 2;
    public static final int STIR_TICKS = 20;
    public static final int SETTLE_TICKS = 40;
    // Ticks the dusts take to pour from their pads into the pot
    public static final int POUR_TICKS = 10;

    private ItemStack left = ItemStack.EMPTY;
    private ItemStack right = ItemStack.EMPTY;
    private ItemStack output = ItemStack.EMPTY;
    private ItemStack pending = ItemStack.EMPTY;
    // Who started it (the step goes in their research)
    private @Nullable UUID owner;
    private ResearchEvaporationBlockEntity.Phase phase = ResearchEvaporationBlockEntity.Phase.IDLE;
    private int stirsDone;
    private int stirTicks = -1;
    private int settleTicks;
    private int time;

    public ResearchMixingBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.RESEARCH_MIXING.get(), pos, state);
    }

    private boolean idle()
    {
        return phase == ResearchEvaporationBlockEntity.Phase.IDLE;
    }

    // The dusts as drawn: on their pads until they've poured into the pot
    public ItemStack getLeft() { return idle() || time < POUR_TICKS ? left : ItemStack.EMPTY; }
    public ItemStack getRight() { return idle() || time < POUR_TICKS ? right : ItemStack.EMPTY; }
    public ItemStack getOutput() { return output; }
    public ResearchEvaporationBlockEntity.Phase phase() { return phase; }
    public int stirsDone() { return stirsDone; }
    public int time() { return time; }
    public boolean isStirring() { return phase == ResearchEvaporationBlockEntity.Phase.STIRRING; }

    public float stirProgress(float partialTick)
    {
        return stirTicks < 0 ? 0 : Math.min(1F, (stirTicks + partialTick) / STIR_TICKS);
    }

    public float settleProgress(float partialTick)
    {
        return phase != ResearchEvaporationBlockEntity.Phase.SETTLING ? 0 : Math.min(1F, (settleTicks + partialTick) / SETTLE_TICKS);
    }

    // The liquid's colour (0xRRGGBB; -1 when the pot's empty): the two dusts' colours mixed, turning to the new element's
    // as it's stirred
    public int liquidColor(float partialTick)
    {
        DustElement a = AlchemicalDust.of(left), b = AlchemicalDust.of(right), made = AlchemicalDust.of(pending);
        if (idle() || a == null || b == null) return -1;
        int mixed = blend(a.color(), b.color(), 0.5F);
        if (made == null) return mixed;
        float stirred = phase == ResearchEvaporationBlockEntity.Phase.SETTLING ? 1F : (stirsDone + stirProgress(partialTick)) / STIRS;
        return blend(mixed, made.color(), stirred);
    }

    // Between two 0xRRGGBB colours, t 0-1 (each of red, green and blue moved t of the way)
    private static int blend(int from, int to, float t)
    {
        int out = 0;
        for (int shift = 0; shift <= 16; shift += 8) {
            int a = (from >> shift) & 0xFF, b = (to >> shift) & 0xFF;
            out |= (Math.round(a + (b - a) * t) & 0xFF) << shift;
        }
        return out;
    }

    // The liquid's surface, in pixels: filling to half as the dusts pour in, gone down into the output while settling
    public double liquidHeight(float partialTick)
    {
        double half = EvaporationLayout.LIQUID_HALF, bottom = EvaporationLayout.POT.y;
        if (idle()) return bottom;
        double level = bottom + (half - bottom) * Math.min(1, (time + partialTick) / POUR_TICKS);
        if (phase == ResearchEvaporationBlockEntity.Phase.SETTLING) level -= (level - bottom) * settleProgress(partialTick);
        return level;
    }

    // ---- clicking (StationInteractions) ----

    @Override
    public List<StationSlot> slots()
    {
        return List.of(
                StationSlot.ofStack(MixingLayout.LEFT_AREA, stack -> idle() && AlchemicalDust.is(stack), 1,
                        () -> idle() ? left : ItemStack.EMPTY, stack -> left = stack, this::sync),
                StationSlot.ofStack(MixingLayout.RIGHT_AREA, stack -> idle() && AlchemicalDust.is(stack), 1,
                        () -> idle() ? right : ItemStack.EMPTY, stack -> right = stack, this::sync),
                StationSlot.output(MixingLayout.OUTPUT_AREA, () -> output, stack -> output = stack, this::sync));
    }

    // ---- starting and stirring (ResearchMixingBlock) ----

    // Why it can't start now (a lang key suffix), or null if it can
    public @Nullable String cannotStart()
    {
        if (!idle()) return "running";
        DustElement a = AlchemicalDust.of(left), b = AlchemicalDust.of(right);
        if (a == null || b == null) return "empty";
        if (!output.isEmpty()) return "outputs";
        if (ElementCombining.combinedBySolids(a, b)) return "no_fluid";
        if (ElementCombining.combine(a, b) == null) return "no_element";
        return null;
    }

    public void start(ServerLevel level, @Nullable Player player)
    {
        owner = player == null ? null : player.getUUID();
        DustElement made = ElementCombining.combine(AlchemicalDust.of(left), AlchemicalDust.of(right));
        pending = made == null ? ItemStack.EMPTY : AlchemicalDust.create(made);
        phase = ResearchEvaporationBlockEntity.Phase.STIRRING;
        stirsDone = 0;
        stirTicks = -1;
        settleTicks = 0;
        time = 0;
        level.playSound(null, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.2F);
        sync();
    }

    // One turn of the spoon (ignored while a turn is still going round, or while the dusts are still pouring in)
    public void stir(Level level)
    {
        if (!isStirring() || stirTicks >= 0 || time < POUR_TICKS) return;
        stirTicks = 0;
        level.playSound(null, worldPosition, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.6F, 1.3F);
        sync();
    }

    // ---- ticking ----

    public static void serverTick(Level level, BlockPos pos, BlockState state, ResearchMixingBlockEntity table)
    {
        if (table.idle()) return;
        table.time++;
        if (table.isStirring() && table.stirTicks >= 0 && ++table.stirTicks >= STIR_TICKS) {
            table.stirTicks = -1;
            table.stirsDone++;
            if (table.stirsDone >= STIRS) {
                table.phase = ResearchEvaporationBlockEntity.Phase.SETTLING;
                table.settleTicks = 0;
                level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 0.9F);
            }
            table.sync();
            return;
        }
        if (table.phase == ResearchEvaporationBlockEntity.Phase.SETTLING && ++table.settleTicks >= SETTLE_TICKS) {
            table.finish((ServerLevel) level);
            return;
        }
        table.setChanged();
    }

    // Client side: count for the animation; the dusts pouring in, bubbles while stirring, the new dust streaming out
    public static void clientTick(Level level, BlockPos pos, BlockState state, ResearchMixingBlockEntity table)
    {
        if (table.idle()) return;
        table.time++;
        if (table.stirTicks >= 0 && table.stirTicks < STIR_TICKS) table.stirTicks++;
        if (table.phase == ResearchEvaporationBlockEntity.Phase.SETTLING && table.settleTicks < SETTLE_TICKS) table.settleTicks++;

        Vec3 potTop = table.turned(new Vec3(EvaporationLayout.POT.x, EvaporationLayout.LIQUID_HALF + 1, EvaporationLayout.POT.z));
        Vec3 surface = table.turned(new Vec3(EvaporationLayout.POT.x, table.liquidHeight(0) + 0.1, EvaporationLayout.POT.z));
        if (table.time < POUR_TICKS) {
            ResearchCombinationBlockEntity.stream(level, pos, table.turned(MixingLayout.LEFT_INPUT), potTop, AlchemicalDust.of(table.left));
            ResearchCombinationBlockEntity.stream(level, pos, table.turned(MixingLayout.RIGHT_INPUT), potTop, AlchemicalDust.of(table.right));
        }
        if (table.stirTicks >= 0) {
            int color = table.liquidColor(0);
            double dx = (level.getRandom().nextDouble() - 0.5) * 0.15, dz = (level.getRandom().nextDouble() - 0.5) * 0.15;
            level.addParticle(ParticleTypes.BUBBLE_POP, pos.getX() + surface.x + dx, pos.getY() + surface.y, pos.getZ() + surface.z + dz, 0, 0.02, 0);
            if (color >= 0) level.addParticle(new DustParticleOptions(color, 0.7F), pos.getX() + surface.x - dx, pos.getY() + surface.y + 0.05,
                    pos.getZ() + surface.z - dz, 0, 0.05, 0);
        }
        if (table.phase == ResearchEvaporationBlockEntity.Phase.SETTLING) {
            ResearchCombinationBlockEntity.stream(level, pos, surface, table.turned(MixingLayout.OUTPUT), AlchemicalDust.of(table.pending));
        }
    }

    private void finish(ServerLevel level)
    {
        noteResearch(level, left, right, pending);
        output = pending;
        pending = ItemStack.EMPTY;
        left = ItemStack.EMPTY;
        right = ItemStack.EMPTY;
        phase = ResearchEvaporationBlockEntity.Phase.IDLE;
        stirsDone = 0;
        level.playSound(null, worldPosition, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        sync();
    }

    // The step goes on the made element's entry, in the research of whoever started it (if they've discovered the
    // element: Research.noteProcess won't note it otherwise)
    private void noteResearch(ServerLevel level, ItemStack first, ItemStack second, ItemStack made)
    {
        DustElement element = AlchemicalDust.of(made);
        if (owner == null || element == null || first.isEmpty() || second.isEmpty()) return;
        ServerPlayer player = level.getServer().getPlayerList().getPlayer(owner);
        if (player == null) return;
        ProcessRecord step = new ProcessRecord(BuiltInRegistries.ITEM.getKey(getBlockState().getBlock().asItem()),
                ItemStackTemplate.fromNonEmptyStack(first, 1), List.of(ItemStackTemplate.fromNonEmptyStack(made, 1)),
                List.of(ItemStackTemplate.fromNonEmptyStack(second, 1)));
        Research.noteProcess(player, new ResearchEntry(ResearchTabs.ELEMENTS, element.id()), step);
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
        out.putString("phase", phase.name());
        out.putInt("stirs_done", stirsDone);
        out.putInt("stir_ticks", stirTicks);
        out.putInt("settle_ticks", settleTicks);
        out.putInt("time", time);
        if (owner != null) out.store("owner", UUIDUtil.CODEC, owner);
    }

    @Override
    protected void loadAdditional(ValueInput in)
    {
        super.loadAdditional(in);
        left = StationSaving.read(in, "left");
        right = StationSaving.read(in, "right");
        output = StationSaving.read(in, "output");
        pending = StationSaving.read(in, "pending");
        try {
            phase = ResearchEvaporationBlockEntity.Phase.valueOf(in.getStringOr("phase", "IDLE"));
        } catch (IllegalArgumentException unknown) {
            phase = ResearchEvaporationBlockEntity.Phase.IDLE;
        }
        stirsDone = in.getIntOr("stirs_done", 0);
        stirTicks = in.getIntOr("stir_ticks", -1);
        settleTicks = in.getIntOr("settle_ticks", 0);
        time = in.getIntOr("time", 0);
        owner = in.read("owner", UUIDUtil.CODEC).orElse(null);
    }

    // Broken: everything in it drops (a mix in progress gives back its dusts)
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
