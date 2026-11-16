package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

// What every research station shares: automation.
//   Hoppers and pipes   from the top: what it works on (StationSlot.Role.INPUT slots)
//                       from the sides: what it uses up: paper, bottles, bowls, crystals, nether quartz (SUPPLY slots)
//                       out the bottom: what it made (OUTPUT slots)
//                       Each way in or out is a one-slot buffer (like the Spell Caster's): changing the station from
//                       inside a hopper's transaction isn't safe, so the buffers are emptied into the station (and the
//                       outputs moved into the bottom one) on the station's own tick (automationTick).
//   Starting it         a redstone pulse (StationAutomation.onNeighborChanged), a dispenser firing a Magical Flint and
//                       Steel into it, or a fire spell landing on it (a Spell Caster's, or a player's): activate. On a
//                       table that's being stirred, each one is a stir.
//   Research            a station started by a player notes its steps in their research. Started by automation, the
//                       notes go to whoever last put something in it by hand (lastUser), if they're online.
@EventBusSubscriber(modid = DustReferences.MODID)
public abstract class StationBlockEntity extends CBSyncedBlockEntity implements IStationSlots
{
    // Whether it was powered at the last neighbour update, so only a new pulse starts it (saved: a station that stays
    // powered through a reload doesn't start again)
    private boolean powered;
    private @Nullable UUID lastUser;

    // The automation buffers (see above). isValid: what a hopper may put in.
    private final ItemStacksResourceHandler topBuffer = buffer(StationSlot.Role.INPUT);
    private final ItemStacksResourceHandler sideBuffer = buffer(StationSlot.Role.SUPPLY);
    private final ItemStacksResourceHandler bottomBuffer = buffer(StationSlot.Role.OUTPUT);

    protected StationBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    private ItemStacksResourceHandler buffer(StationSlot.Role role)
    {
        return new ItemStacksResourceHandler(1)
        {
            @Override
            public boolean isValid(int index, ItemResource resource)
            {
                // Nothing goes into the output buffer from outside; the others take what one of their slots would
                return role != StationSlot.Role.OUTPUT && acceptsFromAutomation(role, resource.toStack(1));
            }

            @Override
            protected void onContentsChanged(int index, ItemStack previous)
            {
                setChanged();
            }
        };
    }

    // Every research station's block entity type, for the item capability below
    private static List<BlockEntityType<? extends StationBlockEntity>> types()
    {
        return List.of(DustBlockEntityRegistry.RESEARCH_CRUSHER.get(), DustBlockEntityRegistry.RESEARCH_SEPARATION.get(),
                DustBlockEntityRegistry.RESEARCH_EVAPORATION.get(), DustBlockEntityRegistry.RESEARCH_DISTILLATION.get(),
                DustBlockEntityRegistry.RESEARCH_TRANSFORMATION.get(), DustBlockEntityRegistry.RESEARCH_COMBINATION.get(),
                DustBlockEntityRegistry.RESEARCH_MIXING.get());
    }

    // Hoppers and pipes: the buffer for the side they're on (side is null when something asks without a side: the top)
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        for (BlockEntityType<? extends StationBlockEntity> type : types()) register(event, type);
    }

    // <T extends StationBlockEntity>: a generic method, so the lambda knows each type's block entity is a station
    private static <T extends StationBlockEntity> void register(RegisterCapabilitiesEvent event, BlockEntityType<T> type)
    {
        event.registerBlockEntity(Capabilities.Item.BLOCK, type, (station, side) -> station.bufferFor(side));
    }

    public ItemStacksResourceHandler bufferFor(@Nullable Direction side)
    {
        if (side == Direction.DOWN) return bottomBuffer;
        if (side == null || side == Direction.UP) return topBuffer;
        return sideBuffer;
    }

    // Whether a slot with this role would take the stack now
    private boolean acceptsFromAutomation(StationSlot.Role role, ItemStack stack)
    {
        for (StationSlot slot : slots()) {
            if (slot.role() == role && slot.accepts().test(stack)) return true;
        }
        return false;
    }

    // ---- the tick (each station's serverTick calls this first) ----

    protected void automationTick()
    {
        boolean moved = feed(topBuffer, StationSlot.Role.INPUT) | feed(sideBuffer, StationSlot.Role.SUPPLY);
        // The bottom buffer fills from the outputs once a hopper has emptied it
        if (bottomBuffer.copyToList().getFirst().isEmpty()) {
            for (StationSlot slot : slots()) {
                if (slot.role() != StationSlot.Role.OUTPUT || slot.contents().get().isEmpty()) continue;
                ItemStack taken = slot.extractor().extract(64);
                if (taken.isEmpty()) continue;
                bottomBuffer.set(0, ItemResource.of(taken), taken.getCount());
                moved = true;
                break;
            }
        }
        if (moved) setChanged();
    }

    // What's in a buffer into the station's slots of this role (as much as they take). True if any went in.
    private boolean feed(ItemStacksResourceHandler buffer, StationSlot.Role role)
    {
        ItemStack stack = buffer.copyToList().getFirst();
        if (stack.isEmpty()) return false;
        int before = stack.getCount();
        for (StationSlot slot : slots()) {
            if (stack.isEmpty()) break;
            if (slot.role() != role || !slot.accepts().test(stack)) continue;
            stack.shrink(slot.inserter().insert(stack, null));
        }
        if (stack.getCount() == before) return false;
        buffer.set(0, stack.isEmpty() ? ItemResource.EMPTY : ItemResource.of(stack), stack.getCount());
        return true;
    }

    // ---- starting it ----

    // Starts the station (or stirs it) from automation, or from a player's fire spell (by: that player, else null).
    // True if it did something.
    public boolean activate(ServerLevel level, @Nullable Player by)
    {
        return false;
    }

    // A new redstone pulse? Notes the power either way; true only when it has just come on.
    public boolean newPulse(boolean poweredNow)
    {
        boolean pulse = poweredNow && !powered;
        if (poweredNow != powered) {
            powered = poweredNow;
            setChanged();
        }
        return pulse;
    }

    // Whoever puts something in by hand (StationInteractions)
    public void noteUser(Player player)
    {
        if (!player.getUUID().equals(lastUser)) {
            lastUser = player.getUUID();
            setChanged();
        }
    }

    // The last player to put something in by hand, or null
    protected @Nullable UUID lastUserId()
    {
        return lastUser;
    }

    // Who gets the research for an automated start: by, else the last player to use it by hand if they're online
    protected @Nullable Player researcher(ServerLevel level, @Nullable Player by)
    {
        if (by != null) return by;
        return lastUser == null ? null : level.getServer().getPlayerList().getPlayer(lastUser);
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.putBoolean("powered", powered);
        if (lastUser != null) output.store("last_user", UUIDUtil.CODEC, lastUser);
        storeBuffer(output, "top_buffer", topBuffer);
        storeBuffer(output, "side_buffer", sideBuffer);
        storeBuffer(output, "bottom_buffer", bottomBuffer);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        powered = input.getBooleanOr("powered", false);
        lastUser = input.read("last_user", UUIDUtil.CODEC).orElse(null);
        readBuffer(input, "top_buffer", topBuffer);
        readBuffer(input, "side_buffer", sideBuffer);
        readBuffer(input, "bottom_buffer", bottomBuffer);
    }

    private static void storeBuffer(ValueOutput output, String key, ItemStacksResourceHandler buffer)
    {
        ItemStack stack = buffer.copyToList().getFirst();
        if (!stack.isEmpty()) output.store(key, ItemStack.OPTIONAL_CODEC, stack);
    }

    private static void readBuffer(ValueInput input, String key, ItemStacksResourceHandler buffer)
    {
        ItemStack stack = input.read(key, ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        buffer.set(0, stack.isEmpty() ? ItemResource.EMPTY : ItemResource.of(stack), stack.getCount());
    }

    // Broken: whatever is waiting in the buffers drops too
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        Level level = getLevel();
        if (level == null) return;
        for (ItemStacksResourceHandler buffer : List.of(topBuffer, sideBuffer, bottomBuffer)) {
            ItemStack stack = buffer.copyToList().getFirst();
            if (!stack.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, stack);
        }
    }
}
