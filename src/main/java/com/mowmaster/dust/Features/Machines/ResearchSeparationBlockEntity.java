package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.DustComposition.DustCompositions;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Research.ProcessRecord;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import com.mowmaster.mowlib.util.CBPixelBox;
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
import java.util.stream.Stream;
import java.util.UUID;

// The Alchemical Separation Table's item (on the scroll), paper (on the pad behind it), and the two outputs; put in and
// taken out by clicking where they sit (slots, StationInteractions). Started with a Magical Flint and Steel
// (ResearchSeparationBlock): the split is decided at the start (Separation), so the client can colour the streams of
// dust running down to each output, and the outputs arrive when it finishes. Whoever started it gets the step in
// their research.
public class ResearchSeparationBlockEntity extends CBSyncedBlockEntity implements IStationSlots
{
    public static final int SEPARATE_TICKS = 100;
    public static final int PAPER_LIMIT = 64;

    private ItemStack input = ItemStack.EMPTY;
    private ItemStack paper = ItemStack.EMPTY;
    private ItemStack left = ItemStack.EMPTY;
    private ItemStack right = ItemStack.EMPTY;
    // While running: what the outputs will be
    private ItemStack pendingLeft = ItemStack.EMPTY;
    private ItemStack pendingRight = ItemStack.EMPTY;
    private int progress = -1;
    private @Nullable UUID owner;

    public ResearchSeparationBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.RESEARCH_SEPARATION.get(), pos, state);
    }

    public ItemStack getInput() { return input; }
    public ItemStack getPaper() { return paper; }
    public ItemStack getLeft() { return left; }
    public ItemStack getRight() { return right; }
    public ItemStack getPendingLeft() { return pendingLeft; }
    public ItemStack getPendingRight() { return pendingRight; }
    public boolean isRunning() { return progress >= 0; }

    // Ticks into the separation (with the partial tick), or -1 when idle
    public float progress(float partialTick)
    {
        return progress < 0 ? -1 : Math.min(SEPARATE_TICKS, progress + partialTick);
    }

    // ---- clicking (StationInteractions) ----
    // Right-click a slot to put things in, left-click to take them out. Areas are model pixels, facing north.
    private static final CBPixelBox PAPER_PAD = new CBPixelBox(6, 15, 1, 10, 17, 5);
    static final CBPixelBox SCROLL = new CBPixelBox(4.5, 15, 5, 11.5, 17, 11);
    private static final CBPixelBox LEFT_PAD = new CBPixelBox(5, 15, 11, 8, 17, 14.5);
    private static final CBPixelBox RIGHT_PAD = new CBPixelBox(8, 15, 11, 11, 17, 14.5);

    @Override
    public List<StationSlot> slots()
    {
        return List.of(
                // Paper, to catch compounds (up to a stack)
                StationSlot.ofStack(PAPER_PAD, stack -> stack.is(Items.PAPER), PAPER_LIMIT, () -> paper, stack -> paper = stack, this::sync),
                // The item to separate, on the scroll: one, and it stays put while separating
                StationSlot.ofStack(SCROLL, stack -> !isRunning() && ResearchInput.accepts(stack), 1, () -> isRunning() ? ItemStack.EMPTY : input, stack -> input = stack, this::sync),
                StationSlot.output(LEFT_PAD, () -> left, stack -> left = stack, this::sync),
                StationSlot.output(RIGHT_PAD, () -> right, stack -> right = stack, this::sync));
    }

    // Why it can't start now, or null if it can
    public @Nullable String cannotStart()
    {
        if (isRunning()) return "running";
        if (input.isEmpty()) return "empty";
        if (!left.isEmpty() || !right.isEmpty()) return "outputs";
        if (!Separation.canSeparate(input)) return "unsplittable";
        return null;
    }

    // Starts separating for this player: picks the split and what each side becomes (using paper now)
    public void start(ServerLevel level, @Nullable Player player)
    {
        ItemStack original = ProductSource.original(input);
        Separation.Split split = Separation.split(input, level.getRandom());
        Separation.Output leftOut = Separation.output(split.left(), original, paper.getCount());
        if (leftOut.usesPaper()) paper.shrink(1);
        Separation.Output rightOut = Separation.output(split.right(), original, paper.getCount());
        if (rightOut.usesPaper()) paper.shrink(1);
        pendingLeft = leftOut.stack();
        pendingRight = rightOut.stack();
        owner = player == null ? null : player.getUUID();
        progress = 0;

        // The gaseous and aqueous elements hiss and bubble away straight off, in their colours
        Vec3 at = turned(SeparationLayout.SCROLL);
        for (DustElement element : Separation.escaping(input)) {
            level.sendParticles(new DustParticleOptions(element.color(), 1.2F), at.x, at.y + 0.1, at.z, 12, 0.1, 0.05, 0.1, 0.02);
        }
        level.playSound(null, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 0.9F);
        sync();
    }

    // ---- ticking ----

    public static void serverTick(Level level, BlockPos pos, BlockState state, ResearchSeparationBlockEntity table)
    {
        if (table.progress < 0) return;
        table.progress++;
        if (table.progress < SEPARATE_TICKS) {
            if (table.progress % 25 == 0) level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.8F, 1.2F);
            table.setChanged();
            return;
        }
        table.finish((ServerLevel) level);
    }

    // Client side: count for the animation, and stream dust from the array down to each output in its colour
    public static void clientTick(Level level, BlockPos pos, BlockState state, ResearchSeparationBlockEntity table)
    {
        if (table.progress < 0) return;
        if (table.progress < SEPARATE_TICKS) table.progress++;
        if (table.progress < 30) return;
        Vec3 from = table.turned(SeparationLayout.ARRAY).add(0, SeparationLayout.ARRAY_LIFT / 16 + 0.15, 0);
        stream(level, pos, from, table.turned(SeparationLayout.LEFT_OUTPUT), colorOf(table.pendingLeft));
        stream(level, pos, from, table.turned(SeparationLayout.RIGHT_OUTPUT), colorOf(table.pendingRight));
    }

    private static void stream(Level level, BlockPos pos, Vec3 from, Vec3 to, int color)
    {
        double t = level.getRandom().nextDouble();
        Vec3 at = from.lerp(to, t);
        Vec3 velocity = to.subtract(from).scale(0.08);
        level.addParticle(new DustParticleOptions(color, 0.6F), pos.getX() + at.x, pos.getY() + at.y, pos.getZ() + at.z, velocity.x, velocity.y, velocity.z);
    }

    // The colour an output streams in: its elements' colour (soot: dark grey)
    private static int colorOf(ItemStack stack)
    {
        DustComposition composition = DustCompositions.of(stack);
        return composition == null ? 0x333333 : composition.color();
    }

    private void finish(ServerLevel level)
    {
        ItemStack separated = input;
        left = pendingLeft;
        right = pendingRight;
        pendingLeft = ItemStack.EMPTY;
        pendingRight = ItemStack.EMPTY;
        input = ItemStack.EMPTY;
        progress = -1;

        // Soot comes out of a little burst of flame
        for (ItemStack out : List.of(left, right)) {
            if (out.getItem() != com.mowmaster.dust.DustRegistries.DustItemRegistry.ALCHEMICAL_SOOT.get()) continue;
            Vec3 at = turned(out == left ? SeparationLayout.LEFT_OUTPUT : SeparationLayout.RIGHT_OUTPUT);
            level.sendParticles(ParticleTypes.FLAME, worldPosition.getX() + at.x, worldPosition.getY() + at.y + 0.1,
                    worldPosition.getZ() + at.z, 10, 0.08, 0.05, 0.08, 0.02);
        }
        level.playSound(null, worldPosition, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        noteResearch(level, separated);
        sync();
    }

    // The step goes into the research of whoever started it, on the material's original entry
    private void noteResearch(ServerLevel level, ItemStack separated)
    {
        if (owner == null) return;
        ServerPlayer player = level.getServer().getPlayerList().getPlayer(owner);
        if (player == null) return;
        ProcessRecord step = new ProcessRecord(BuiltInRegistries.ITEM.getKey(getBlockState().getBlock().asItem()),
                ItemStackTemplate.fromNonEmptyStack(separated, 1),
                Stream.of(left, right).filter(out -> !out.isEmpty()).map(out -> ItemStackTemplate.fromNonEmptyStack(out, 1)).toList());
        Research.noteProcess(player, ResearchTabs.entryForItem(ProductSource.original(separated)), step);
    }

    // A point on the model, turned to this table's facing, in block units
    private Vec3 turned(Vec3 pixels)
    {
        return CrusherLayout.turned(pixels, getBlockState().getValue(ResearchSeparationBlock.FACING));
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        store(output, "input", input);
        store(output, "paper", paper);
        store(output, "left", left);
        store(output, "right", right);
        store(output, "pending_left", pendingLeft);
        store(output, "pending_right", pendingRight);
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
        this.paper = read(input, "paper");
        this.left = read(input, "left");
        this.right = read(input, "right");
        this.pendingLeft = read(input, "pending_left");
        this.pendingRight = read(input, "pending_right");
        this.progress = input.getIntOr("progress", -1);
        this.owner = input.read("owner", UUIDUtil.CODEC).orElse(null);
    }

    private static ItemStack read(ValueInput input, String key)
    {
        return input.read(key, ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
    }

    // Broken: everything in it drops (a separation in progress gives back its item)
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level == null) return;
        for (ItemStack stack : List.of(input, paper, left, right)) {
            if (!stack.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, stack);
        }
    }
}
