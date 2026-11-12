package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.DustComposition.DustCompositions;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.DustComposition.ElementPhase;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// The Distillation Table (Distillation has the rules): the item in the flask, containers on three pads (glass bottles,
// bowls, paper for the soot), and three outputs (soot from the solids, the gas / liquid that stayed in the flask, what boiled over into
// the jar).
//   idle         the item in the flask shows as liquid, a quarter full, in the colour of what can boil
//   boiling      started with a Magical Flint and Steel (BOIL_TICKS): the glyph rises off the scroll into the flask,
//                spinning; the liquid boils away, and vapour fills the flask and runs down the neck into the jar
//   transfer     (TRANSFER_TICKS) what's left in the flask streams to its outputs, the jar's to its own
// The split and what each part becomes are decided at the start (containers are used then). Gases and liquids
// without their container escape when it finishes (puffs and bubbles); the solids burn to soot (a flame), which blows
// away (smoke) without paper to catch it.
public class ResearchDistillationBlockEntity extends CBSyncedBlockEntity implements IStationSlots
{
    public static final int BOIL_TICKS = 100;
    public static final int TRANSFER_TICKS = 30;
    public static final int CONTAINER_LIMIT = 64;

    private ItemStack input = ItemStack.EMPTY;
    private ItemStack bottles = ItemStack.EMPTY;
    private ItemStack bowls = ItemStack.EMPTY;
    private ItemStack paper = ItemStack.EMPTY;
    private ItemStack flaskOut = ItemStack.EMPTY;
    private ItemStack jarOut = ItemStack.EMPTY;
    private ItemStack sootOut = ItemStack.EMPTY;
    private ItemStack pendingFlask = ItemStack.EMPTY;
    private ItemStack pendingJar = ItemStack.EMPTY;
    private ItemStack pendingSoot = ItemStack.EMPTY;
    // The element boiling over (for the jar's colour), and what escapes at the end without a container
    private @Nullable DustElement over;
    private List<DustElement> lost = List.of();
    private boolean burned;
    private boolean sootBlown;
    private int progress = -1;
    private @Nullable UUID owner;

    public ResearchDistillationBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.RESEARCH_DISTILLATION.get(), pos, state);
    }

    public ItemStack getInput() { return input; }
    public ItemStack getBottles() { return bottles; }
    public ItemStack getBowls() { return bowls; }
    public ItemStack getPaper() { return paper; }
    public ItemStack getFlaskOut() { return flaskOut; }
    public ItemStack getJarOut() { return jarOut; }
    public ItemStack getSootOut() { return sootOut; }
    public boolean isRunning() { return progress >= 0; }

    public float progress(float partialTick)
    {
        return progress < 0 ? -1 : Math.min(BOIL_TICKS + TRANSFER_TICKS, progress + partialTick);
    }

    // The colour of what can boil (the flask's liquid), or of the element boiling over (the jar's), 0xRRGGBB; -1: none
    public int flaskColor()
    {
        List<DustElement> boiling = Separation.escaping(input);
        return boiling.isEmpty() ? -1 : new DustComposition(boiling).color();
    }

    public int jarColor()
    {
        return over == null ? -1 : over.color();
    }

    // ---- clicking (StationInteractions) ----

    @Override
    public List<StationSlot> slots()
    {
        return List.of(
                StationSlot.ofStack(DistillationLayout.BOTTLE_AREA, stack -> stack.is(Items.GLASS_BOTTLE), CONTAINER_LIMIT, () -> bottles, stack -> bottles = stack, this::sync),
                StationSlot.ofStack(DistillationLayout.BOWL_AREA, stack -> stack.is(Items.BOWL), CONTAINER_LIMIT, () -> bowls, stack -> bowls = stack, this::sync),
                StationSlot.ofStack(DistillationLayout.PAPER_AREA, stack -> stack.is(Items.PAPER), CONTAINER_LIMIT, () -> paper, stack -> paper = stack, this::sync),
                StationSlot.ofStack(DistillationLayout.FLASK_AREA, stack -> !isRunning() && ResearchInput.accepts(stack), 1, () -> isRunning() ? ItemStack.EMPTY : input, stack -> input = stack, this::sync),
                StationSlot.output(DistillationLayout.SOOT_OUTPUT_AREA, () -> sootOut, stack -> sootOut = stack, this::sync),
                StationSlot.output(DistillationLayout.FLASK_OUTPUT_AREA, () -> flaskOut, stack -> flaskOut = stack, this::sync),
                StationSlot.output(DistillationLayout.JAR_OUTPUT_AREA, () -> jarOut, stack -> jarOut = stack, this::sync));
    }

    // ---- starting ----

    // Why it can't start now (a lang key suffix), or null if it can
    public @Nullable String cannotStart()
    {
        if (isRunning()) return "running";
        if (input.isEmpty()) return "empty";
        if (!flaskOut.isEmpty() || !jarOut.isEmpty() || !sootOut.isEmpty()) return "outputs";
        if (!Distillation.canDistill(input)) return "nothing";
        return null;
    }

    public void start(ServerLevel level, @Nullable Player player)
    {
        ItemStack original = ProductSource.original(input);
        Distillation.Split split = Distillation.split(input, level.getRandom());
        over = split.jar().getFirst();
        Distillation.Output jar = Distillation.output(split.jar(), original, this::hasContainer);
        if (jar.usesContainer()) useContainer(Compounds.Kind.of(split.jar()).container());
        Distillation.Output flask = Distillation.output(split.flask(), original, this::hasContainer);
        if (flask.usesContainer()) useContainer(Compounds.Kind.of(split.flask().stream().filter(element -> !Separation.isSolid(element)).toList()).container());
        pendingJar = jar.compound();
        pendingFlask = flask.compound();
        if (flask.usesPaper()) useContainer(Items.PAPER);
        pendingSoot = flask.soot();
        burned = split.flask().stream().anyMatch(Separation::isSolid);
        sootBlown = flask.sootBlown();
        List<DustElement> escaping = new ArrayList<>(jar.lost());
        escaping.addAll(flask.lost());
        lost = List.copyOf(escaping);
        owner = player == null ? null : player.getUUID();
        progress = 0;
        level.playSound(null, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.2F);
        sync();
    }

    private boolean hasContainer(Item container)
    {
        return containerStack(container).getCount() > 0;
    }

    private void useContainer(Item container)
    {
        containerStack(container).shrink(1);
    }

    private ItemStack containerStack(Item container)
    {
        if (container == Items.GLASS_BOTTLE) return bottles;
        if (container == Items.BOWL) return bowls;
        if (container == Items.PAPER) return paper;
        return ItemStack.EMPTY;
    }

    // ---- ticking ----

    public static void serverTick(Level level, BlockPos pos, BlockState state, ResearchDistillationBlockEntity table)
    {
        if (table.progress < 0) return;
        table.progress++;
        if (table.progress % 20 == 0 && table.progress < BOIL_TICKS) level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.5F, 1.4F);
        if (table.progress >= BOIL_TICKS + TRANSFER_TICKS) {
            table.finish((ServerLevel) level);
            return;
        }
        table.setChanged();
    }

    // Client side: count for the animation, and the vapour / streams
    public static void clientTick(Level level, BlockPos pos, BlockState state, ResearchDistillationBlockEntity table)
    {
        if (table.progress < 0) return;
        if (table.progress < BOIL_TICKS + TRANSFER_TICKS) table.progress++;
        var random = level.getRandom();
        if (table.progress < BOIL_TICKS) {
            int liquid = table.flaskColor();
            int vapour = table.jarColor();
            // Bubbles on the boiling liquid, and vapour filling the flask
            Vec3 surface = table.turned(new Vec3(DistillationLayout.FLASK.x, table.flaskLiquid(0), DistillationLayout.FLASK.z));
            level.addParticle(ParticleTypes.BUBBLE_POP, pos.getX() + surface.x + (random.nextDouble() - 0.5) * 0.1, pos.getY() + surface.y,
                    pos.getZ() + surface.z + (random.nextDouble() - 0.5) * 0.1, 0, 0.01, 0);
            Vec3 inFlask = table.turned(DistillationLayout.FLASK.add((random.nextDouble() - 0.5) * DistillationLayout.FLASK_WIDTH,
                    random.nextDouble() * DistillationLayout.FLASK_HEIGHT, (random.nextDouble() - 0.5) * DistillationLayout.FLASK_DEPTH));
            if (liquid >= 0) level.addParticle(new DustParticleOptions(liquid, 0.4F), pos.getX() + inFlask.x, pos.getY() + inFlask.y, pos.getZ() + inFlask.z, 0, 0.01, 0);
            // Down the neck into the jar
            if (vapour >= 0 && table.progress > 15) {
                Vec3 from = table.turned(DistillationLayout.NECK_START), to = table.turned(DistillationLayout.NECK_END);
                stream(level, pos, from, to, vapour, 0.4F);
                Vec3 jarTop = table.turned(new Vec3(DistillationLayout.JAR.x, DistillationLayout.JAR.y + DistillationLayout.JAR_HEIGHT, DistillationLayout.JAR.z));
                stream(level, pos, to, jarTop.add(0, -0.1, 0), vapour, 0.4F);
            }
        } else {
            // The flask's remains to their outputs, the jar's to its own
            Vec3 flask = table.turned(DistillationLayout.FLASK.add(0, 0.5, 0)), jar = table.turned(DistillationLayout.JAR.add(0, 0.6, 0));
            stream(level, pos, flask, table.turned(DistillationLayout.FLASK_OUTPUT), colorOf(table.pendingFlask), 0.6F);
            if (!table.pendingSoot.isEmpty()) stream(level, pos, flask, table.turned(DistillationLayout.SOOT_OUTPUT), 0x2A2A2A, 0.6F);
            stream(level, pos, jar, table.turned(DistillationLayout.JAR_OUTPUT), colorOf(table.pendingJar), 0.6F);
        }
    }

    private static void stream(Level level, BlockPos pos, Vec3 from, Vec3 to, int color, float size)
    {
        if (color < 0) return;
        Vec3 at = from.lerp(to, level.getRandom().nextDouble());
        Vec3 velocity = to.subtract(from).scale(0.08);
        level.addParticle(new DustParticleOptions(color, size), pos.getX() + at.x, pos.getY() + at.y, pos.getZ() + at.z, velocity.x, velocity.y, velocity.z);
    }

    // An output's colour while it streams (nothing: none)
    private static int colorOf(ItemStack stack)
    {
        if (stack.isEmpty()) return -1;
        DustComposition composition = DustCompositions.of(stack);
        return composition == null ? 0x333333 : composition.color();
    }

    // The flask's liquid surface, in pixels: a quarter full, boiling down to nothing over BOIL_TICKS
    public double flaskLiquid(float partialTick)
    {
        double bottom = DistillationLayout.FLASK.y, quarter = bottom + DistillationLayout.FLASK_HEIGHT / 4;
        if (progress < 0) return quarter;
        double boiled = Math.min(1, (progress + partialTick) / BOIL_TICKS);
        return quarter - (quarter - bottom) * boiled;
    }

    // The jar's liquid surface: filling to a quarter as it boils over, while there's something to boil over
    public double jarLiquid(float partialTick)
    {
        double bottom = DistillationLayout.JAR.y;
        if (progress < 0 || over == null) return bottom;
        double filled = Math.min(1, Math.max(0, (progress + partialTick - 15) / (BOIL_TICKS - 15)));
        double level = bottom + DistillationLayout.JAR_HEIGHT / 4 * filled;
        // Emptied into the output during the transfer
        if (progress > BOIL_TICKS) level -= (level - bottom) * Math.min(1, (progress + partialTick - BOIL_TICKS) / TRANSFER_TICKS);
        return level;
    }

    private void finish(ServerLevel level)
    {
        ItemStack distilled = input;
        flaskOut = pendingFlask;
        jarOut = pendingJar;
        sootOut = pendingSoot;
        pendingFlask = ItemStack.EMPTY;
        pendingJar = ItemStack.EMPTY;
        pendingSoot = ItemStack.EMPTY;
        input = ItemStack.EMPTY;
        progress = -1;

        // What had no container escapes: gases in a puff of their colour, liquids bubbling away; soot from a flame
        Vec3 flask = turned(DistillationLayout.FLASK.add(0, 1, 0));
        double x = worldPosition.getX() + flask.x, y = worldPosition.getY() + flask.y, z = worldPosition.getZ() + flask.z;
        for (DustElement element : lost) {
            level.sendParticles(new DustParticleOptions(element.color(), 1.0F), x, y, z, 8, 0.1, 0.1, 0.1, 0.02);
            if (element.phase() == ElementPhase.AQUEOUS) level.sendParticles(ParticleTypes.BUBBLE_POP, x, y, z, 6, 0.1, 0.05, 0.1, 0.01);
        }
        if (burned) level.sendParticles(ParticleTypes.FLAME, x, y, z, 8, 0.08, 0.05, 0.08, 0.02);
        if (sootBlown) level.sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 10, 0.1, 0.1, 0.1, 0.03);
        lost = List.of();
        burned = false;
        sootBlown = false;
        over = null;
        level.playSound(null, worldPosition, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        noteResearch(level, distilled);
        sync();
    }

    // The step goes into the research of whoever started it (outputs that came to nothing aren't shown)
    private void noteResearch(ServerLevel level, ItemStack distilled)
    {
        if (owner == null) return;
        ServerPlayer player = level.getServer().getPlayerList().getPlayer(owner);
        if (player == null) return;
        List<ItemStackTemplate> outputs = new ArrayList<>();
        for (ItemStack out : List.of(sootOut, flaskOut, jarOut)) {
            if (!out.isEmpty()) outputs.add(ItemStackTemplate.fromNonEmptyStack(out, 1));
        }
        ProcessRecord step = new ProcessRecord(BuiltInRegistries.ITEM.getKey(getBlockState().getBlock().asItem()),
                ItemStackTemplate.fromNonEmptyStack(distilled, 1), outputs);
        Research.noteProcess(player, ResearchTabs.entryForItem(ProductSource.original(distilled)), step);
    }

    private Vec3 turned(Vec3 pixels)
    {
        return CrusherLayout.turned(pixels, getBlockState().getValue(ResearchDistillationBlock.FACING));
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        store(output, "input", input);
        store(output, "bottles", bottles);
        store(output, "bowls", bowls);
        store(output, "paper", paper);
        store(output, "flask_out", flaskOut);
        store(output, "jar_out", jarOut);
        store(output, "soot_out", sootOut);
        store(output, "pending_flask", pendingFlask);
        store(output, "pending_jar", pendingJar);
        store(output, "pending_soot", pendingSoot);
        if (over != null) output.store("over", DustElements.CODEC, over);
        if (!lost.isEmpty()) output.store("lost", DustElements.CODEC.listOf(), lost);
        output.putBoolean("burned", burned);
        output.putBoolean("soot_blown", sootBlown);
        output.putInt("progress", progress);
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
        this.bottles = read(input, "bottles");
        this.bowls = read(input, "bowls");
        this.paper = read(input, "paper");
        this.flaskOut = read(input, "flask_out");
        this.jarOut = read(input, "jar_out");
        this.sootOut = read(input, "soot_out");
        this.pendingFlask = read(input, "pending_flask");
        this.pendingJar = read(input, "pending_jar");
        this.pendingSoot = read(input, "pending_soot");
        this.over = input.read("over", DustElements.CODEC).orElse(null);
        this.lost = input.read("lost", DustElements.CODEC.listOf()).orElse(List.of());
        this.burned = input.getBooleanOr("burned", false);
        this.sootBlown = input.getBooleanOr("soot_blown", false);
        this.progress = input.getIntOr("progress", -1);
        this.owner = input.read("owner", UUIDUtil.CODEC).orElse(null);
    }

    private static ItemStack read(ValueInput input, String key)
    {
        return input.read(key, ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
    }

    // Broken: everything in it drops (a distillation in progress gives back its item)
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level == null) return;
        for (ItemStack stack : List.of(input, bottles, bowls, paper, flaskOut, jarOut, sootOut)) {
            if (!stack.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, stack);
        }
    }
}
