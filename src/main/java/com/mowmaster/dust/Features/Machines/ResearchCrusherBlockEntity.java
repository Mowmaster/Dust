package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.Research.ProcessRecord;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import com.mowmaster.mowlib.util.CBPixelBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

// The Research Crusher's block being crushed, the pebbles in its tray, how far along it is, and who put the block in
// (their research notes the step). One block at a time, CRUSH_TICKS each.
// The client keeps its own count while a block is in (for the descent and the wheels), restarted whenever the server's
// copy arrives, so the server only syncs when the block or the tray changes, not every tick.
public class ResearchCrusherBlockEntity extends CBSyncedBlockEntity implements IStationSlots
{
    public static final int CRUSH_TICKS = 100;
    public static final int TRAY_LIMIT = 64;

    private ItemStack input = ItemStack.EMPTY;
    private ItemStack output = ItemStack.EMPTY;
    private int progress;
    private @Nullable UUID owner;

    public ResearchCrusherBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.RESEARCH_CRUSHER.get(), pos, state);
    }

    public ItemStack getInput() { return input; }
    public ItemStack getOutput() { return output; }
    public boolean isRunning() { return !input.isEmpty(); }

    // 0-1: how far the block has gone down into the wheels
    public float progress(float partialTick)
    {
        return input.isEmpty() ? 0 : Math.min(1F, (progress + partialTick) / CRUSH_TICKS);
    }

    // ---- clicking (StationInteractions) ----
    // Right-click the funnel with a block to drop one in; left-click the funnel to take it back, the tray for the
    // pebbles. Areas are model pixels, facing north (the tray side).
    private static final CBPixelBox FUNNEL = new CBPixelBox(0, 7, 6, 16, 16.5, 16.5);
    private static final CBPixelBox TRAY = new CBPixelBox(4, 0, -0.5, 12, 5, 7);

    @Override
    public List<StationSlot> slots()
    {
        StationSlot.Inserter dropIn = (stack, player) -> {
            if (!input.isEmpty()) return 0;
            input = stack.copyWithCount(1);
            owner = player == null ? null : player.getUUID();
            progress = 0;
            sync();
            return 1;
        };
        StationSlot.Extractor takeBack = max -> {
            ItemStack taken = input;
            input = ItemStack.EMPTY;
            progress = 0;
            sync();
            return taken;
        };
        return List.of(
                StationSlot.output(TRAY, () -> output, stack -> output = stack, this::sync),
                new StationSlot(FUNNEL, stack -> stack.getItem() instanceof BlockItem && input.isEmpty(), dropIn, () -> input, takeBack));
    }

    // Crushed <block>: pebbles that remember their block
    public static ItemStack crushed(ItemStack block)
    {
        return ProductSource.madeFrom(new ItemStack(DustItemRegistry.CRUSHED_BLOCK.get()), block);
    }

    // ---- ticking (ResearchCrusherBlock#getTicker) ----

    public static void serverTick(Level level, BlockPos pos, BlockState state, ResearchCrusherBlockEntity crusher)
    {
        if (crusher.input.isEmpty()) return;
        ItemStack result = crushed(crusher.input);
        // The tray takes the same pebbles up to a stack; anything else waits until it's emptied
        boolean fits = crusher.output.isEmpty()
                || (ItemStack.isSameItemSameComponents(crusher.output, result) && crusher.output.getCount() < Math.min(TRAY_LIMIT, crusher.output.getMaxStackSize()));
        if (!fits) return;

        crusher.progress++;
        if (crusher.progress % 20 == 1) level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.5F, 0.8F + level.getRandom().nextFloat() * 0.2F);
        if (crusher.progress < CRUSH_TICKS) {
            crusher.setChanged();
            return;
        }

        ItemStack block = crusher.input;
        if (crusher.output.isEmpty()) crusher.output = result;
        else crusher.output.grow(1);
        crusher.input = ItemStack.EMPTY;
        crusher.progress = 0;
        crusher.noteResearch(level, block, result);
        crusher.sync();
    }

    // Client side: just count, for the animation
    public static void clientTick(Level level, BlockPos pos, BlockState state, ResearchCrusherBlockEntity crusher)
    {
        if (!crusher.input.isEmpty() && crusher.progress < CRUSH_TICKS) crusher.progress++;
    }

    // The step goes into the research of whoever put the block in (if they're here, with their notes), on the block's
    // original material's entry
    private void noteResearch(Level level, ItemStack block, ItemStack result)
    {
        if (owner == null || !(level instanceof ServerLevel serverLevel)) return;
        ServerPlayer player = serverLevel.getServer().getPlayerList().getPlayer(owner);
        if (player == null) return;
        ProcessRecord step = new ProcessRecord(BuiltInRegistries.ITEM.getKey(getBlockState().getBlock().asItem()),
                ItemStackTemplate.fromNonEmptyStack(block, 1), List.of(ItemStackTemplate.fromNonEmptyStack(result, 1)));
        Research.noteProcess(player, ResearchTabs.entryForItem(ProductSource.original(block)), step);
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (!input.isEmpty()) output.store("input", ItemStack.OPTIONAL_CODEC, input);
        if (!this.output.isEmpty()) output.store("output", ItemStack.OPTIONAL_CODEC, this.output);
        output.putInt("progress", progress);
        // UUIDUtil.CODEC: a player's id as four numbers
        if (owner != null) output.store("owner", UUIDUtil.CODEC, owner);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.input = input.read("input", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        this.output = input.read("output", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        this.progress = input.getIntOr("progress", 0);
        this.owner = input.read("owner", UUIDUtil.CODEC).orElse(null);
    }

    // Broken: the block being crushed and the tray's pebbles drop
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level == null) return;
        if (!input.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, input);
        if (!output.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, output);
    }
}
