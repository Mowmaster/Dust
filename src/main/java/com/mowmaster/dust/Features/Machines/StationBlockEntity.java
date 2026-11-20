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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

// What every research station (and the Element Replicator) shares: automation.
//   Hoppers and pipes   from the top: what it works on (StationSlot.Role.INPUT slots)
//                       from the sides: what it uses up: paper, bottles, bowls, crystals, nether quartz (SUPPLY slots)
//                       out the bottom: what it made (OUTPUT slots)
//                       They reach the station's own slots (StationSlotHandler), so outputs stay on their pads for
//                       the player until a hopper takes them. MANUAL slots (the Element Replicator's item to copy)
//                       they don't reach at all.
//   Starting it         a dispenser firing a Magical Flint and Steel into it, or a fire spell landing on it (a Spell
//                       Caster's, or a player's): activate. On a table that's being stirred, each one is a stir.
//   Redstone            a pulse (StationAutomation.onNeighborChanged) only stirs a table that's being stirred: pulse.
//                       It never starts a station: that takes a Magical Flint and Steel or a fire spell.
//   Research            a station started by a player notes its steps in their research. Started by automation, the
//                       notes go to whoever last put something in it by hand (lastUser), if they're online.
@EventBusSubscriber(modid = DustReferences.MODID)
public abstract class StationBlockEntity extends CBSyncedBlockEntity implements IStationSlots
{
    // Whether it was powered at the last neighbour update, so only a new pulse counts (saved: a station that stays
    // powered through a reload doesn't count it again)
    private boolean powered;
    private @Nullable UUID lastUser;

    // What hoppers and pipes reach, by side (see above)
    private final StationSlotHandler inputs = new StationSlotHandler(this, StationSlot.Role.INPUT);
    private final StationSlotHandler supplies = new StationSlotHandler(this, StationSlot.Role.SUPPLY);
    private final StationSlotHandler outputs = new StationSlotHandler(this, StationSlot.Role.OUTPUT);
    // Items left in the hopper buffers an earlier version had (top_buffer, side_buffer, bottom_buffer): kept until the
    // station is broken, then dropped
    private static final List<String> OLD_BUFFERS = List.of("top_buffer", "side_buffer", "bottom_buffer");
    private final java.util.Map<String, ItemStack> leftovers = new java.util.HashMap<>();

    protected StationBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    // Every station's block entity type (the research stations, the Element Replicator and the alchemical apparatus),
    // for the item capability below
    private static List<BlockEntityType<? extends StationBlockEntity>> types()
    {
        return List.of(DustBlockEntityRegistry.RESEARCH_CRUSHER.get(), DustBlockEntityRegistry.RESEARCH_SEPARATION.get(),
                DustBlockEntityRegistry.RESEARCH_EVAPORATION.get(), DustBlockEntityRegistry.RESEARCH_DISTILLATION.get(),
                DustBlockEntityRegistry.RESEARCH_TRANSFORMATION.get(), DustBlockEntityRegistry.RESEARCH_COMBINATION.get(),
                DustBlockEntityRegistry.RESEARCH_MIXING.get(), DustBlockEntityRegistry.ELEMENT_REPLICATOR.get(),
                // The alchemical apparatus (Features/Apparatus)
                DustBlockEntityRegistry.CRUSHING_MILL.get(), DustBlockEntityRegistry.ALCHEMICAL_SEPARATOR.get(),
                DustBlockEntityRegistry.EVAPORATING_KETTLE.get(), DustBlockEntityRegistry.STILL_POT.get(), DustBlockEntityRegistry.STILL_FIREBOX.get(), DustBlockEntityRegistry.TRANSMUTER.get(),
                DustBlockEntityRegistry.CONJOINING_CRUCIBLE.get(), DustBlockEntityRegistry.MIXING_VAT.get(), DustBlockEntityRegistry.DISSOLVING_RETORT.get());
    }

    // Hoppers and pipes: the slots for the side they're on (side is null when something asks without a side: the top)
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        for (BlockEntityType<? extends StationBlockEntity> type : types()) register(event, type);
    }

    // <T extends StationBlockEntity>: a generic method, so the lambda knows each type's block entity is a station
    private static <T extends StationBlockEntity> void register(RegisterCapabilitiesEvent event, BlockEntityType<T> type)
    {
        event.registerBlockEntity(Capabilities.Item.BLOCK, type, (station, side) -> station.slotsFor(side));
    }

    public StationSlotHandler slotsFor(@Nullable Direction side)
    {
        if (side == Direction.DOWN) return outputs;
        if (side == null || side == Direction.UP) return inputs;
        return supplies;
    }

    // ---- starting it ----

    // Starts the station (or stirs it) from a dispenser's Magical Flint and Steel, or from a fire spell (by: the player
    // who cast it, else null). True if it did something.
    public boolean activate(ServerLevel level, @Nullable Player by)
    {
        return false;
    }

    // A redstone pulse (StationAutomation.onNeighborChanged): stirs a station that's being stirred (the stations that
    // stir override this). It never starts one. True if it did something.
    public boolean pulse(ServerLevel level)
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
        leftovers.forEach((key, stack) -> output.store(key, ItemStack.OPTIONAL_CODEC, stack));
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        powered = input.getBooleanOr("powered", false);
        lastUser = input.read("last_user", UUIDUtil.CODEC).orElse(null);
        leftovers.clear();
        for (String key : OLD_BUFFERS) input.read(key, ItemStack.OPTIONAL_CODEC).filter(stack -> !stack.isEmpty()).ifPresent(stack -> leftovers.put(key, stack));
    }

    // Broken: anything left in an old buffer drops
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level == null) return;
        for (ItemStack stack : leftovers.values()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, stack);
        leftovers.clear();
    }
}
