package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.DustComposition.DustCompositions;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Research.ProcessRecord;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

// The Evaporation Table: boils the gaseous and aqueous elements out of something, leaving its solids (Evaporation).
//   idle       put the item in the pot (it shows as liquid in the colour of what will boil off), paper on its pad
//   stirring   started with a Magical Flint and Steel: the glyph spins over the pot, and the player stirs with the spoon,
//              one full turn per element in the item (each click is one turn; the liquid bubbles and goes down)
//   settling   the glyph sinks into the pot, and the solids stream across to the output pad
// What's left is decided at the start (paper is used then); whoever started it gets the step in their research.
// What boils off goes into the air (into the chunk's aura, once there is one).
public class ResearchEvaporationBlockEntity extends CBSyncedBlockEntity implements IStationSlots
{
    public static final int STIR_TICKS = 20;
    public static final int SETTLE_TICKS = 40;
    public static final int PAPER_LIMIT = 64;

    public enum Phase { IDLE, STIRRING, SETTLING }

    private ItemStack input = ItemStack.EMPTY;
    private ItemStack paper = ItemStack.EMPTY;
    private ItemStack output = ItemStack.EMPTY;
    private ItemStack pending = ItemStack.EMPTY;
    private Phase phase = Phase.IDLE;
    private int stirsNeeded;
    private int stirsDone;
    // Ticks into the turn being stirred (-1: not stirring right now), ticks settling, ticks since starting
    private int stirTicks = -1;
    private int settleTicks;
    private int time;
    private @Nullable UUID owner;

    public ResearchEvaporationBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.RESEARCH_EVAPORATION.get(), pos, state);
    }

    public ItemStack getInput() { return input; }
    public ItemStack getPaper() { return paper; }
    public ItemStack getOutput() { return output; }
    public ItemStack getPending() { return pending; }
    public Phase phase() { return phase; }
    public int stirsNeeded() { return stirsNeeded; }
    public int stirsDone() { return stirsDone; }
    public int time() { return time; }

    // 0-1 into the turn being stirred (0 when not stirring)
    public float stirProgress(float partialTick)
    {
        return stirTicks < 0 ? 0 : Math.min(1F, (stirTicks + partialTick) / STIR_TICKS);
    }

    // 0-1 into settling
    public float settleProgress(float partialTick)
    {
        return phase != Phase.SETTLING ? 0 : Math.min(1F, (settleTicks + partialTick) / SETTLE_TICKS);
    }

    // The colour of what will boil off (the liquid in the pot), 0xRRGGBB; -1 if nothing will
    public int liquidColor()
    {
        List<DustElement> boiling = Separation.escaping(input);
        return boiling.isEmpty() ? -1 : new DustComposition(boiling).color();
    }

    // ---- clicking (StationInteractions) ----

    @Override
    public List<StationSlot> slots()
    {
        return List.of(
                StationSlot.ofStack(EvaporationLayout.PAPER_AREA, stack -> stack.is(Items.PAPER), PAPER_LIMIT, () -> paper, stack -> paper = stack, this::sync),
                // Into the pot: one, and it stays while it's being worked
                StationSlot.ofStack(EvaporationLayout.POT_AREA, stack -> phase == Phase.IDLE, 1,
                        () -> phase == Phase.IDLE ? input : ItemStack.EMPTY, stack -> input = stack, this::sync),
                StationSlot.output(EvaporationLayout.OUTPUT_AREA, () -> output, stack -> output = stack, this::sync));
    }

    // ---- starting and stirring (ResearchEvaporationBlock) ----

    // Why it can't start now (a lang key suffix), or null if it can
    public @Nullable String cannotStart()
    {
        if (phase != Phase.IDLE) return "running";
        if (input.isEmpty()) return "empty";
        if (!output.isEmpty()) return "outputs";
        if (Separation.escaping(input).isEmpty()) return "nothing";
        if (Separation.solids(input).isEmpty()) return "no_solids";
        return null;
    }

    public void start(ServerLevel level, @Nullable Player player)
    {
        Separation.Output result = Separation.output(Separation.solids(input), ProductSource.original(input), paper.getCount());
        if (result.usesPaper()) paper.shrink(1);
        pending = result.stack();
        DustComposition composition = DustCompositions.of(input);
        stirsNeeded = composition == null ? 1 : Math.max(1, composition.elements().size());
        stirsDone = 0;
        stirTicks = -1;
        settleTicks = 0;
        time = 0;
        phase = Phase.STIRRING;
        owner = player == null ? null : player.getUUID();
        level.playSound(null, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.1F);
        sync();
    }

    public boolean isStirring()
    {
        return phase == Phase.STIRRING;
    }

    // One turn of the spoon (ignored while a turn is still going round)
    public void stir(Level level)
    {
        if (phase != Phase.STIRRING || stirTicks >= 0) return;
        stirTicks = 0;
        level.playSound(null, worldPosition, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.6F, 1.3F);
        sync();
    }

    // ---- ticking ----

    public static void serverTick(Level level, BlockPos pos, BlockState state, ResearchEvaporationBlockEntity table)
    {
        if (table.phase == Phase.IDLE) return;
        table.time++;
        if (table.phase == Phase.STIRRING && table.stirTicks >= 0 && ++table.stirTicks >= STIR_TICKS) {
            table.stirTicks = -1;
            table.stirsDone++;
            if (table.stirsDone >= table.stirsNeeded) {
                table.phase = Phase.SETTLING;
                table.settleTicks = 0;
                level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 0.9F);
            }
            table.sync();
            return;
        }
        if (table.phase == Phase.SETTLING && ++table.settleTicks >= SETTLE_TICKS) {
            table.finish((ServerLevel) level);
            return;
        }
        table.setChanged();
    }

    // Client side: count for the animation; bubbles while stirring, and the solids streaming to the output while settling
    public static void clientTick(Level level, BlockPos pos, BlockState state, ResearchEvaporationBlockEntity table)
    {
        if (table.phase == Phase.IDLE) return;
        table.time++;
        if (table.stirTicks >= 0 && table.stirTicks < STIR_TICKS) table.stirTicks++;
        if (table.phase == Phase.SETTLING && table.settleTicks < SETTLE_TICKS) table.settleTicks++;

        Vec3 surface = table.turned(new Vec3(EvaporationLayout.POT.x, table.liquidHeight(0) + 0.1, EvaporationLayout.POT.z));
        if (table.stirTicks >= 0) {
            int color = table.liquidColor();
            for (int i = 0; i < 2; i++) {
                double dx = (level.getRandom().nextDouble() - 0.5) * 0.15, dz = (level.getRandom().nextDouble() - 0.5) * 0.15;
                level.addParticle(ParticleTypes.BUBBLE_POP, pos.getX() + surface.x + dx, pos.getY() + surface.y, pos.getZ() + surface.z + dz, 0, 0.02, 0);
                if (color >= 0) {
                    level.addParticle(new DustParticleOptions(color, 0.7F), pos.getX() + surface.x + dx, pos.getY() + surface.y + 0.05,
                            pos.getZ() + surface.z + dz, 0, 0.06, 0);
                }
            }
        }
        if (table.phase == Phase.SETTLING) {
            DustComposition solids = DustCompositions.of(table.pending);
            int color = solids == null ? 0x333333 : solids.color();
            Vec3 to = table.turned(EvaporationLayout.OUTPUT);
            Vec3 at = surface.lerp(to, level.getRandom().nextDouble());
            Vec3 velocity = to.subtract(surface).scale(0.08);
            level.addParticle(new DustParticleOptions(color, 0.6F), pos.getX() + at.x, pos.getY() + at.y, pos.getZ() + at.z, velocity.x, velocity.y, velocity.z);
        }
    }

    // The liquid's surface, in pixels: half full when the item goes in, going down a step with each turn stirred, and
    // boiled away while settling
    public double liquidHeight(float partialTick)
    {
        double half = EvaporationLayout.LIQUID_HALF, bottom = EvaporationLayout.POT.y;
        if (phase == Phase.IDLE || stirsNeeded <= 0) return half;
        double stirred = (stirsDone + stirProgress(partialTick)) / stirsNeeded;
        double level = half - (half - bottom) * 0.7 * Math.min(1, stirred);
        if (phase == Phase.SETTLING) level -= (level - bottom) * settleProgress(partialTick);
        return level;
    }

    private void finish(ServerLevel level)
    {
        ItemStack evaporated = input;
        output = pending;
        pending = ItemStack.EMPTY;
        input = ItemStack.EMPTY;
        phase = Phase.IDLE;
        stirsDone = 0;
        stirsNeeded = 0;
        // The gaseous and aqueous elements are gone into the air. (They'll go into the chunk's aura once there is one.)
        Vec3 at = turned(EvaporationLayout.OUTPUT);
        if (output.getItem() == com.mowmaster.dust.DustRegistries.DustItemRegistry.ALCHEMICAL_SOOT.get()) {
            level.sendParticles(ParticleTypes.FLAME, worldPosition.getX() + at.x, worldPosition.getY() + at.y + 0.1, worldPosition.getZ() + at.z, 10, 0.08, 0.05, 0.08, 0.02);
        }
        level.playSound(null, worldPosition, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        noteResearch(level, evaporated);
        sync();
    }

    private void noteResearch(ServerLevel level, ItemStack evaporated)
    {
        if (owner == null) return;
        ServerPlayer player = level.getServer().getPlayerList().getPlayer(owner);
        if (player == null) return;
        ProcessRecord step = new ProcessRecord(BuiltInRegistries.ITEM.getKey(getBlockState().getBlock().asItem()),
                ItemStackTemplate.fromNonEmptyStack(evaporated, 1), List.of(ItemStackTemplate.fromNonEmptyStack(output, 1)));
        Research.noteProcess(player, ResearchTabs.entryForItem(ProductSource.original(evaporated)), step);
    }

    private Vec3 turned(Vec3 pixels)
    {
        return CrusherLayout.turned(pixels, getBlockState().getValue(ResearchEvaporationBlock.FACING));
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        store(output, "input", input);
        store(output, "paper", paper);
        store(output, "output", this.output);
        store(output, "pending", pending);
        output.putString("phase", phase.name());
        output.putInt("stirs_needed", stirsNeeded);
        output.putInt("stirs_done", stirsDone);
        output.putInt("stir_ticks", stirTicks);
        output.putInt("settle_ticks", settleTicks);
        if (owner != null) output.store("owner", UUIDUtil.CODEC, owner);
    }

    private static void store(ValueOutput output, String key, ItemStack stack)
    {
        if (!stack.isEmpty()) output.store(key, ItemStack.OPTIONAL_CODEC, stack);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.input = read(input, "input");
        this.paper = read(input, "paper");
        this.output = read(input, "output");
        this.pending = read(input, "pending");
        // The saved name back to the enum value (IDLE if it is missing or unknown)
        String saved = input.getStringOr("phase", Phase.IDLE.name());
        this.phase = java.util.Arrays.stream(Phase.values()).filter(p -> p.name().equals(saved)).findFirst().orElse(Phase.IDLE);
        this.stirsNeeded = input.getIntOr("stirs_needed", 0);
        this.stirsDone = input.getIntOr("stirs_done", 0);
        this.stirTicks = input.getIntOr("stir_ticks", -1);
        this.settleTicks = input.getIntOr("settle_ticks", 0);
        this.owner = input.read("owner", UUIDUtil.CODEC).orElse(null);
    }

    private static ItemStack read(ValueInput input, String key)
    {
        return input.read(key, ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
    }

    // Broken: everything in it drops (an evaporation in progress gives back its item)
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level == null) return;
        for (ItemStack stack : List.of(input, paper, output)) {
            if (!stack.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, stack);
        }
    }
}
