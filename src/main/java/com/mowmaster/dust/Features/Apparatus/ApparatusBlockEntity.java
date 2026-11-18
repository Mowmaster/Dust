package com.mowmaster.dust.Features.Apparatus;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.DustStorage.DustNetwork;
import com.mowmaster.dust.Features.Machines.Compounds;
import com.mowmaster.dust.Features.Machines.StationBlockEntity;
import com.mowmaster.dust.Features.Machines.StationSaving;
import com.mowmaster.dust.Features.Machines.StationSlot;
import com.mowmaster.dust.Features.Magic.ChunkAura;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

// What every alchemical apparatus shares (the factory machines, one for each research station: ApparatusBlock):
//   owner      whoever placed it (or, if no one did, the first player to use it). It only catches the elements they've
//              discovered: the rest slip away into the chunk's aura (ChunkAura) instead of becoming dust. Which they
//              know is noted every KNOWN_REFRESH ticks while they're online, and saved, so it keeps working while
//              they're away.
//   dust out   dust it has made, waiting to go: every PUSH_TICKS it pours into the Dust Jars on its Glass Dust Tube
//              network, or right against it (DustNetwork.insert). Up to DUST_BUFFER waits; while it's full the
//              apparatus waits too, so nothing is lost when the jars are full.
//   outputs    OUTPUT_SLOTS slots for what it hands on (anything it can't work on passes straight through to them).
//              Hoppers and pipes take from the bottom; left-click the spout takes from them (sneak: a stack).
//   redstone   a redstone signal pauses it
//   status     right-click it with an empty hand: what it's doing (status)
// Hoppers, pipes and clicks reach its slots as a research station's (StationBlockEntity, StationSlotHandler): from the
// top what it works on (INPUT), from the sides what it uses up (SUPPLY; an apparatus that uses nothing up takes its
// input from the sides too), out the bottom what it made (OUTPUT). Slots for players' hands only (its pattern) are
// MANUAL.
public abstract class ApparatusBlockEntity extends StationBlockEntity
{
    public static final int OUTPUT_SLOTS = 4;
    public static final int DUST_BUFFER = 64;
    public static final int PUSH_TICKS = 10;
    public static final int KNOWN_REFRESH = 100;

    protected final ItemStack[] outputs = new ItemStack[OUTPUT_SLOTS];
    protected final Map<DustElement, Integer> dustOut = new LinkedHashMap<>();
    private @Nullable UUID owner;
    private final Set<DustElement> known = new HashSet<>();
    // A redstone signal reaching it (checked when a neighbour changes; worked out on its first tick after loading)
    private boolean paused;
    private boolean pausedChecked;

    protected ApparatusBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        Arrays.fill(outputs, ItemStack.EMPTY);
    }

    // ---- what each apparatus does ----

    // Its own slots (what it works on, a pattern, a supply); the outputs are added after them (slots)
    protected abstract List<StationSlot> machineSlots();

    // A tick of work (server, not paused)
    protected abstract void work(ServerLevel level);

    // A tick on the client (the animation: count on between syncs)
    protected void clientWork(Level level) {}

    // What it's doing, for the status line ("crushing Stone (45%)", "idle")
    protected abstract Component activity();

    // Whether it uses anything up from the sides (SUPPLY slots); if not, the sides feed its input too
    protected boolean usesSupplies()
    {
        return false;
    }

    // ---- slots ----

    @Override
    public final List<StationSlot> slots()
    {
        List<StationSlot> slots = new ArrayList<>(machineSlots());
        // The spout: one slot for clicks standing for all the outputs (MANUAL: automation uses the real ones below),
        // then each output for hoppers and pipes (never clicked: NOWHERE)
        slots.add(new StationSlot(ApparatusLayout.SPOUT, stack -> false, (stack, player) -> 0, this::firstOutput, this::takeOutput,
                StationSlot.Role.MANUAL, 64));
        for (int i = 0; i < OUTPUT_SLOTS; i++) {
            final int slot = i;
            slots.add(StationSlot.output(ApparatusLayout.NOWHERE, () -> outputs[slot], stack -> outputs[slot] = stack, this::sync));
        }
        return slots;
    }

    // Hoppers and pipes on its sides feed its input when it has no supplies to take there
    @Override
    public com.mowmaster.dust.Features.Machines.StationSlotHandler slotsFor(net.minecraft.core.@Nullable Direction side)
    {
        if (side != null && side.getAxis().isHorizontal() && !usesSupplies()) return super.slotsFor(net.minecraft.core.Direction.UP);
        return super.slotsFor(side);
    }

    private ItemStack firstOutput()
    {
        for (ItemStack stack : outputs) {
            if (!stack.isEmpty()) return stack;
        }
        return ItemStack.EMPTY;
    }

    private ItemStack takeOutput(int max)
    {
        for (int i = 0; i < OUTPUT_SLOTS; i++) {
            if (outputs[i].isEmpty()) continue;
            ItemStack taken = outputs[i].split(Math.min(max, outputs[i].getCount()));
            if (outputs[i].isEmpty()) outputs[i] = ItemStack.EMPTY;
            sync();
            return taken;
        }
        return ItemStack.EMPTY;
    }

    // Whether all of this would fit in the outputs (stacking onto the same item first, then empty slots)
    protected boolean outputsFit(ItemStack stack)
    {
        if (stack.isEmpty()) return true;
        int left = stack.getCount();
        for (ItemStack held : outputs) {
            if (held.isEmpty()) left -= stack.getMaxStackSize();
            else if (ItemStack.isSameItemSameComponents(held, stack)) left -= held.getMaxStackSize() - held.getCount();
            if (left <= 0) return true;
        }
        return false;
    }

    // Puts it in the outputs (check outputsFit first: what doesn't fit is dropped on top rather than lost)
    protected void addOutput(ItemStack stack)
    {
        ItemStack left = stack.copy();
        for (int i = 0; i < OUTPUT_SLOTS && !left.isEmpty(); i++) {
            if (!outputs[i].isEmpty() && ItemStack.isSameItemSameComponents(outputs[i], left)) {
                int moved = Math.min(left.getCount(), outputs[i].getMaxStackSize() - outputs[i].getCount());
                outputs[i].grow(moved);
                left.shrink(moved);
            }
        }
        for (int i = 0; i < OUTPUT_SLOTS && !left.isEmpty(); i++) {
            if (outputs[i].isEmpty()) {
                outputs[i] = left.split(Math.min(left.getCount(), left.getMaxStackSize()));
            }
        }
        if (!left.isEmpty() && level != null) Containers.dropItemStack(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.0, worldPosition.getZ() + 0.5, left);
        setChanged();
    }

    // ---- dust ----

    // How much more dust can wait to go
    protected int dustRoom()
    {
        int total = 0;
        for (int amount : dustOut.values()) total += amount;
        return DUST_BUFFER - total;
    }

    // Dust made: what the owner knows waits to go to the jars; what they don't slips away into the aura
    protected void catchDust(ServerLevel level, List<DustElement> elements)
    {
        for (DustElement element : elements) {
            if (knows(element)) dustOut.merge(element, 1, Integer::sum);
            else ChunkAura.release(level, worldPosition, element, 1);
        }
        setChanged();
    }

    // How many of these the owner knows (the dust that would wait to go)
    protected int knownCount(List<DustElement> elements)
    {
        int count = 0;
        for (DustElement element : elements) {
            if (knows(element)) count++;
        }
        return count;
    }

    // Pours what's waiting into the jars on its network; what no jar takes keeps waiting
    private void pushDust(ServerLevel level)
    {
        if (dustOut.isEmpty()) return;
        boolean moved = false;
        for (Map.Entry<DustElement, Integer> waiting : dustOut.entrySet()) {
            int poured = DustNetwork.insert(level, worldPosition, waiting.getKey(), waiting.getValue());
            if (poured <= 0) continue;
            waiting.setValue(waiting.getValue() - poured);
            moved = true;
        }
        dustOut.values().removeIf(amount -> amount <= 0);
        if (moved) setChanged();
    }

    // ---- the owner ----

    public void setOwner(Player player)
    {
        owner = player.getUUID();
        if (player instanceof ServerPlayer serverPlayer) noteKnown(serverPlayer);
        setChanged();
    }

    public boolean hasOwner()
    {
        return owner != null;
    }

    // Whether the owner had discovered this element when last noted
    public boolean knows(DustElement element)
    {
        return known.contains(element);
    }

    private void noteKnown(ServerPlayer player)
    {
        known.clear();
        for (DustElement element : DustElements.all()) {
            if (Compounds.knows(player, element)) known.add(element);
        }
    }

    // ---- ticking (ApparatusBlock's ticker) ----

    public void tick(Level level)
    {
        if (!(level instanceof ServerLevel server)) {
            clientWork(level);
            return;
        }
        if (!pausedChecked) {
            paused = level.hasNeighborSignal(worldPosition);
            pausedChecked = true;
        }
        long time = level.getGameTime() + worldPosition.asLong();
        if (owner != null && time % KNOWN_REFRESH == 0) {
            ServerPlayer player = server.getServer().getPlayerList().getPlayer(owner);
            if (player != null) noteKnown(player);
        }
        if (time % PUSH_TICKS == 0) pushDust(server);
        if (!paused) work(server);
    }

    // A neighbour changed (ApparatusBlock): a redstone signal pauses it
    public void checkPower(Level level)
    {
        boolean powered = level.hasNeighborSignal(worldPosition);
        pausedChecked = true;
        if (powered != paused) {
            paused = powered;
            sync();
        }
    }

    public boolean isPaused()
    {
        return paused;
    }

    // A point on the model (pixels, facing north) in the world, turned with the block (for particles)
    protected net.minecraft.world.phys.Vec3 at(double x, double y, double z)
    {
        net.minecraft.world.phys.Vec3 turned = com.mowmaster.dust.Features.Machines.CrusherLayout.turned(new net.minecraft.world.phys.Vec3(x, y, z),
                getBlockState().getValue(com.mowmaster.dust.Features.Machines.StationBlock.FACING));
        return turned.add(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
    }

    // ---- the player's view ----

    // "Crushing Mill: crushing Stone (45%) | dust waiting: 6"
    public Component status()
    {
        MutableComponent line = Component.empty().append(getBlockState().getBlock().getName()).append(": ");
        if (owner == null) line.append(Component.translatable("dust.apparatus.no_owner"));
        else if (paused) line.append(Component.translatable("dust.apparatus.paused"));
        else line.append(activity());
        int waiting = DUST_BUFFER - dustRoom();
        if (waiting > 0) line.append(" | ").append(Component.translatable(dustRoom() <= 0 ? "dust.apparatus.dust_full" : "dust.apparatus.dust_waiting", waiting));
        if (outputsFull()) line.append(" | ").append(Component.translatable("dust.apparatus.outputs_full"));
        return line;
    }

    // Every output slot holds a full stack
    private boolean outputsFull()
    {
        for (ItemStack held : outputs) {
            if (held.isEmpty() || held.getCount() < held.getMaxStackSize()) return false;
        }
        return true;
    }

    // ---- saving (and what clients get) ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.store("outputs", ItemStack.OPTIONAL_CODEC.listOf(), Arrays.asList(outputs));
        if (!dustOut.isEmpty()) output.store("dust_out", Codec.unboundedMap(DustElements.CODEC, Codec.INT), Map.copyOf(dustOut));
        if (owner != null) output.store("owner", UUIDUtil.CODEC, owner);
        if (!known.isEmpty()) output.store("known", DustElements.CODEC.listOf(), List.copyOf(known));
        output.putBoolean("paused", paused);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        List<ItemStack> saved = input.read("outputs", ItemStack.OPTIONAL_CODEC.listOf()).orElse(List.of());
        for (int i = 0; i < OUTPUT_SLOTS; i++) outputs[i] = i < saved.size() ? saved.get(i) : ItemStack.EMPTY;
        dustOut.clear();
        dustOut.putAll(input.read("dust_out", Codec.unboundedMap(DustElements.CODEC, Codec.INT)).orElse(Map.of()));
        owner = input.read("owner", UUIDUtil.CODEC).orElse(null);
        known.clear();
        known.addAll(input.read("known", DustElements.CODEC.listOf()).orElse(List.of()));
        paused = input.getBooleanOr("paused", false);
    }

    // Broken: everything in it drops (dust waiting to go is lost)
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level == null) return;
        for (ItemStack stack : dropsOnBreak()) {
            if (!stack.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, stack);
        }
    }

    // What drops when it's broken: the outputs, and each apparatus's own (overridden to add them)
    protected List<ItemStack> dropsOnBreak()
    {
        return new ArrayList<>(Arrays.asList(outputs));
    }

    // Saving helper for subclasses (an empty stack isn't written)
    protected static void store(ValueOutput output, String key, ItemStack stack)
    {
        StationSaving.store(output, key, stack);
    }

    protected static ItemStack read(ValueInput input, String key)
    {
        return StationSaving.read(input, key);
    }
}
