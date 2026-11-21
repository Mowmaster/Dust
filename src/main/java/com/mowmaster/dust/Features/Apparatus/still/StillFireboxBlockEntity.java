package com.mowmaster.dust.Features.Apparatus.still;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.Machines.StationBlockEntity;
import com.mowmaster.dust.Features.Machines.StationSaving;
import com.mowmaster.dust.Features.Machines.StationSlot;
import com.mowmaster.dust.Features.Machines.StationSlotHandler;
import com.mowmaster.mowlib.util.CBPixelBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.List;

// A Burnable Firebox's fuel: a stack of one furnace fuel (FUEL_LIMIT; not one that leaves something behind, like a lava
// bucket), and how long the piece burning has left. It only lights a new piece when the still above wants heat
// (heat(), from the pot), and then burns it out like a furnace does. Lit while it burns (StillFireboxBlock.LIT).
// Every piece it burns leaves an Alchemical Soot (and the pots add one for any solid none of them could take: addSoot).
// Soot goes into the containers round it (any side but the top; never a hopper, which would only bring it back); what
// they can't take waits in a stack of its own, and a full stack goes into the void (VoidStorage): nothing is wasted.
public class StillFireboxBlockEntity extends StationBlockEntity
{
    public static final int FUEL_LIMIT = 64;
    private static final CBPixelBox ANYWHERE = new CBPixelBox(0, 0, 0, 16, 16, 16);

    private static final int SOOT_PUSH_TICKS = 20;

    private ItemStack fuel = ItemStack.EMPTY;
    private ItemStack soot = ItemStack.EMPTY;
    private int burnTime;
    private int burnTotal;

    public StillFireboxBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.STILL_FIREBOX.get(), pos, state);
    }

    public boolean isBurning() { return burnTime > 0; }

    // How full it is, 0-1: the stack and what's left of the piece burning, out of a full stack (the flame's colour)
    public float fuelShare()
    {
        float burning = burnTotal > 0 ? (float) burnTime / burnTotal : 0;
        return Math.min(1F, (fuel.getCount() + burning) / FUEL_LIMIT);
    }

    // Whether it burns: something with a burn time, that leaves nothing behind. (26.3 keeps fuel as an item component,
    // cooking_fuel; before, the level's fuel table.)
    private boolean isFuel(ItemStack stack)
    {
        if (level == null || stack.getCraftingRemainder() != null) return false;
        //? if >=26.3 {
        return stack.has(net.minecraft.core.component.DataComponents.COOKING_FUEL);
        //?} else {
        /*return level.fuelValues().burnDuration(stack) > 0;
        *///?}
    }

    // How long one of it burns (ticks). 26.3: the cooking_fuel component's burn time, worked out the way a furnace does
    // (it can depend on where it burns: getLootContext)
    private int burnDuration(ServerLevel level, ItemStack stack)
    {
        //? if >=26.3 {
        // The context a furnace gives (the block, where it is, the container: here one holding just this fuel)
        net.minecraft.world.level.storage.loot.LootContext context = new net.minecraft.world.level.storage.loot.LootContext.Builder(
                new net.minecraft.world.level.storage.loot.LootParams.Builder(level)
                        .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_STATE, getBlockState())
                        .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_ENTITY, this)
                        .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN, net.minecraft.world.phys.Vec3.atCenterOf(worldPosition))
                        .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.CONTAINER, new net.minecraft.world.SimpleContainer(stack.copyWithCount(1)))
                        .create(net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.CONTAINER_PROCESS))
                .create(java.util.Optional.empty());
        return net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt.getFromItem(stack, net.minecraft.core.component.DataComponents.COOKING_FUEL,
                net.minecraft.world.item.component.CookingFuel::burnTime, context, 0);
        //?} else {
        /*return level.fuelValues().burnDuration(stack);
        *///?}
    }

    @Override
    public List<StationSlot> slots()
    {
        return List.of(StationSlot.ofStack(ANYWHERE, this::isFuel, FUEL_LIMIT, () -> fuel, stack -> fuel = stack, this::sync));
    }

    // Fuel goes in from every side
    @Override
    public StationSlotHandler slotsFor(@Nullable Direction side)
    {
        return super.slotsFor(Direction.UP);
    }

    // Whether it's burning or has fuel to burn (for the pot's status line)
    public boolean canBurn()
    {
        return burnTime > 0 || (!fuel.isEmpty() && isFuel(fuel));
    }

    // The still wants heat: true if it's burning, lighting the next piece of fuel if it needs to
    public boolean heat(ServerLevel level)
    {
        if (burnTime > 0) return true;
        if (fuel.isEmpty() || !isFuel(fuel)) return false;
        burnTotal = burnTime = burnDuration(level, fuel);
        if (burnTime <= 0) return false;
        fuel.shrink(1);
        if (fuel.isEmpty()) fuel = ItemStack.EMPTY;
        setLit(level, true);
        addSoot(level, 1);
        sync();
        return true;
    }

    // ---- soot ----

    public int sootHeld() { return soot.getCount(); }

    // Soot to give out: into the containers round it, the rest kept; a full stack kept goes into the void
    public void addSoot(ServerLevel level, int count)
    {
        if (count <= 0) return;
        if (soot.isEmpty()) soot = new ItemStack(com.mowmaster.dust.DustRegistries.DustItemRegistry.ALCHEMICAL_SOOT.get(), count);
        else soot.grow(count);
        pushSoot(level);
    }

    private void pushSoot(ServerLevel level)
    {
        if (soot.isEmpty()) return;
        for (Direction side : Direction.values()) {
            if (soot.isEmpty() || side == Direction.UP) break;
            BlockPos pos = worldPosition.relative(side);
            BlockState state = level.getBlockState(pos);
            // Not a hopper, and not the still itself
            if (state.getBlock() instanceof net.minecraft.world.level.block.HopperBlock || state.getBlock() instanceof StillPotBlock) continue;
            net.neoforged.neoforge.transfer.ResourceHandler<net.neoforged.neoforge.transfer.item.ItemResource> handler =
                    level.getCapability(net.neoforged.neoforge.capabilities.Capabilities.Item.BLOCK, pos, side.getOpposite());
            if (handler == null) continue;
            try (net.neoforged.neoforge.transfer.transaction.Transaction transaction = net.neoforged.neoforge.transfer.transaction.Transaction.openRoot()) {
                int went = net.neoforged.neoforge.transfer.ResourceHandlerUtil.insertStacking(handler,
                        net.neoforged.neoforge.transfer.item.ItemResource.of(soot), soot.getCount(), transaction);
                if (went <= 0) continue;
                transaction.commit();
                soot.shrink(went);
            }
        }
        if (soot.getCount() >= soot.getMaxStackSize()) {
            com.mowmaster.dust.Features.Spells.voidrift.VoidStorage.get(level.getServer()).add(soot.copy());
            soot = ItemStack.EMPTY;
        }
        if (soot.isEmpty()) soot = ItemStack.EMPTY;
        setChanged();
    }

    public void serverTick(ServerLevel level)
    {
        if (!soot.isEmpty() && (level.getGameTime() + worldPosition.asLong()) % SOOT_PUSH_TICKS == 0) pushSoot(level);
        if (burnTime <= 0) return;
        if (--burnTime <= 0) {
            burnTime = 0;
            setLit(level, false);
            sync();
        }
        else setChanged();
    }

    private void setLit(Level level, boolean lit)
    {
        BlockState state = getBlockState();
        if (state.getValue(StillFireboxBlock.LIT) != lit) level.setBlock(worldPosition, state.setValue(StillFireboxBlock.LIT, lit), 3);
    }

    // "Burnable Firebox: burning (45%) | 12 Coal"
    public Component status()
    {
        Component burning = burnTime > 0 ? Component.translatable("dust.still.burning", 100 * burnTime / Math.max(1, burnTotal))
                : Component.translatable("dust.still.cold");
        Component fuelText = fuel.isEmpty() ? Component.translatable("dust.still.no_fuel") : Component.literal(fuel.getCount() + " ").append(fuel.getHoverName());
        Component line = Component.empty().append(getBlockState().getBlock().getName()).append(": ").append(burning).append(" | ").append(fuelText);
        return soot.isEmpty() ? line : line.copy().append(" | ").append(Component.translatable("dust.still.soot_held", soot.getCount()));
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        StationSaving.store(output, "fuel", fuel);
        StationSaving.store(output, "soot", soot);
        output.putInt("burn_time", burnTime);
        output.putInt("burn_total", burnTotal);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        fuel = StationSaving.read(input, "fuel");
        soot = StationSaving.read(input, "soot");
        burnTime = input.getIntOr("burn_time", 0);
        burnTotal = input.getIntOr("burn_total", 0);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level != null && !fuel.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, fuel);
        if (level != null && !soot.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, soot);
    }
}
