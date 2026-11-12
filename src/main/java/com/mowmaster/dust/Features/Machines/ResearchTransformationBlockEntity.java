package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Research.ProcessRecord;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.DustParticleOptions;
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

// The Transformation Table: an alchemical compound of one element the player has discovered becomes Alchemical Dust of
// that element; Alchemical Dust with an inert crystal or nether quartz on the pad behind becomes a crystal of its
// element (one crystal used). Started with a Magical Flint and Steel; the glyph rises off the scroll spinning, the item
// riding above it, and the result streams down to the output (like the Separation Table).
public class ResearchTransformationBlockEntity extends CBSyncedBlockEntity implements IStationSlots
{
    public static final int TRANSFORM_TICKS = 100;
    public static final int CRYSTAL_LIMIT = 64;

    private ItemStack input = ItemStack.EMPTY;
    private ItemStack crystals = ItemStack.EMPTY;
    private ItemStack output = ItemStack.EMPTY;
    private ItemStack pending = ItemStack.EMPTY;
    private int progress = -1;
    private @Nullable UUID owner;

    public ResearchTransformationBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.RESEARCH_TRANSFORMATION.get(), pos, state);
    }

    public ItemStack getInput() { return input; }
    public ItemStack getCrystals() { return crystals; }
    public ItemStack getOutput() { return output; }
    public boolean isRunning() { return progress >= 0; }

    public float progress(float partialTick)
    {
        return progress < 0 ? -1 : Math.min(TRANSFORM_TICKS, progress + partialTick);
    }

    // What the crystal pad takes
    public static boolean isCrystal(ItemStack stack)
    {
        return stack.is(DustItemRegistry.CRYSTAL_INERT.get()) || stack.is(Items.QUARTZ);
    }

    // What the scroll takes: alchemical compounds and Alchemical Dust
    public static boolean isTransformable(ItemStack stack)
    {
        return Compounds.of(stack) != null || AlchemicalDust.is(stack);
    }

    // ---- clicking (StationInteractions) ----

    @Override
    public List<StationSlot> slots()
    {
        return List.of(
                StationSlot.ofStack(TransformationLayout.CRYSTAL_AREA, ResearchTransformationBlockEntity::isCrystal, CRYSTAL_LIMIT, () -> crystals, stack -> crystals = stack, this::sync),
                StationSlot.ofStack(TransformationLayout.SCROLL_AREA, stack -> !isRunning() && isTransformable(stack), 1,
                        () -> isRunning() ? ItemStack.EMPTY : input, stack -> input = stack, this::sync),
                StationSlot.output(TransformationLayout.OUTPUT_AREA, () -> output, stack -> output = stack, this::sync));
    }

    // ---- starting ----

    // Why this player can't start it now (a lang key suffix), or null if they can
    public @Nullable String cannotStart(Player player)
    {
        if (isRunning()) return "running";
        if (input.isEmpty()) return "empty";
        if (!output.isEmpty()) return "outputs";
        if (AlchemicalDust.is(input)) return crystals.isEmpty() ? "no_crystal" : null;
        DustComposition compound = Compounds.of(input);
        if (compound == null || compound.isEmpty()) return "nothing";
        if (compound.counted().size() != 1) return "single";
        if (!Compounds.knows(player, compound.elements().getFirst())) return "unknown";
        return null;
    }

    public void start(ServerLevel level, Player player)
    {
        DustElement dust = AlchemicalDust.of(input);
        if (dust != null) {
            pending = CrystalElements.stackOf(dust);
            crystals.shrink(1);
        } else {
            pending = AlchemicalDust.create(Compounds.of(input).elements().getFirst());
        }
        owner = player.getUUID();
        progress = 0;
        level.playSound(null, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        sync();
    }

    // ---- ticking ----

    public static void serverTick(Level level, BlockPos pos, BlockState state, ResearchTransformationBlockEntity table)
    {
        if (table.progress < 0) return;
        table.progress++;
        if (table.progress < TRANSFORM_TICKS) {
            if (table.progress % 25 == 0) level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.8F, 1.0F);
            table.setChanged();
            return;
        }
        table.finish((ServerLevel) level);
    }

    // Client side: count for the animation; the result streams from the glyph down to the output in its colour
    public static void clientTick(Level level, BlockPos pos, BlockState state, ResearchTransformationBlockEntity table)
    {
        if (table.progress < 0) return;
        if (table.progress < TRANSFORM_TICKS) table.progress++;
        if (table.progress < 30) return;
        Vec3 from = table.turned(TransformationLayout.ARRAY).add(0, TransformationLayout.ARRAY_LIFT / 16 + 0.15, 0);
        Vec3 to = table.turned(TransformationLayout.OUTPUT);
        Vec3 at = from.lerp(to, level.getRandom().nextDouble());
        Vec3 velocity = to.subtract(from).scale(0.08);
        level.addParticle(new DustParticleOptions(table.resultColor(), 0.6F), pos.getX() + at.x, pos.getY() + at.y, pos.getZ() + at.z,
                velocity.x, velocity.y, velocity.z);
    }

    // The colour of the element being transformed
    private int resultColor()
    {
        DustElement dust = AlchemicalDust.of(input);
        if (dust != null) return dust.color();
        DustComposition compound = Compounds.of(input);
        return compound == null ? 0xFFFFFF : compound.color();
    }

    private void finish(ServerLevel level)
    {
        ItemStack transformed = input;
        output = pending;
        pending = ItemStack.EMPTY;
        input = ItemStack.EMPTY;
        progress = -1;
        level.playSound(null, worldPosition, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1.0F, 1.2F);
        noteResearch(level, transformed);
        sync();
    }

    // A compound made into dust goes into the research of whoever started it, on its element's entry (how to make the
    // element's dust). Dust made into a crystal isn't noted.
    private void noteResearch(ServerLevel level, ItemStack transformed)
    {
        if (owner == null || output.isEmpty() || Compounds.of(transformed) == null) return;
        ServerPlayer player = level.getServer().getPlayerList().getPlayer(owner);
        if (player == null) return;
        ProcessRecord step = new ProcessRecord(BuiltInRegistries.ITEM.getKey(getBlockState().getBlock().asItem()),
                ItemStackTemplate.fromNonEmptyStack(transformed, 1), List.of(ItemStackTemplate.fromNonEmptyStack(output, 1)));
        Research.noteProcess(player, new ResearchEntry(ResearchTabs.ELEMENTS, AlchemicalDust.of(output).id()), step);
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
        StationSaving.store(out, "input", input);
        StationSaving.store(out, "crystals", crystals);
        StationSaving.store(out, "output", output);
        StationSaving.store(out, "pending", pending);
        out.putInt("progress", progress);
        if (owner != null) out.store("owner", UUIDUtil.CODEC, owner);
    }

    @Override
    protected void loadAdditional(ValueInput in)
    {
        super.loadAdditional(in);
        input = StationSaving.read(in, "input");
        crystals = StationSaving.read(in, "crystals");
        output = StationSaving.read(in, "output");
        pending = StationSaving.read(in, "pending");
        progress = in.getIntOr("progress", -1);
        owner = in.read("owner", UUIDUtil.CODEC).orElse(null);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level == null) return;
        for (ItemStack stack : List.of(input, crystals, output)) {
            if (!stack.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, stack);
        }
    }
}
