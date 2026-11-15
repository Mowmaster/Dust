package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity;
import com.mowmaster.dust.Features.Machines.AlchemicalDust;
import com.mowmaster.dust.Features.Machines.Compounds;
import com.mowmaster.dust.Features.Machines.IStationSlots;
import com.mowmaster.dust.Features.Machines.StationSlot;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import com.mowmaster.mowlib.util.CBPixelBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.List;

// The Dust Grinder's input (a stack of one grindable item, in the hole in the middle of the top) and its turning.
// Grindable: an alchemical compound, Alchemical Dust or an element crystal, every element of which the player putting
// it in has discovered. Each full turn of the top (TURN_TICKS: a right-click, or a redstone pulse) grinds one of
// them: one dust of each element in it goes into a Dust Jar next to the grinder that takes it (any side), and any no
// jar takes comes out as an element orb on top, worth 1.
// The client keeps its own count while turning (for the top's animation), restarted whenever the server's copy
// arrives, so the server only syncs when a turn starts or ends and when the input changes.
public class DustGrinderBlockEntity extends CBSyncedBlockEntity implements IStationSlots
{
    public static final int TURN_TICKS = 40;
    public static final int INPUT_LIMIT = 64;

    private ItemStack input = ItemStack.EMPTY;
    // Ticks into the turn, or -1 when still
    private int turn = -1;

    public DustGrinderBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.DUST_GRINDER.get(), pos, state);
    }

    public ItemStack getInput() { return input; }
    public boolean isTurning() { return turn >= 0; }

    // Degrees the top has turned (0-360) this turn, with the partial tick
    public float angle(float partialTick)
    {
        return turn < 0 ? 0 : Math.min(1F, (turn + partialTick) / TURN_TICKS) * 360F;
    }

    // ---- what it grinds ----

    // The elements in an item, one per element it holds (a compound of fire and fire gives fire twice), or an empty list
    // if it isn't grindable
    public static List<DustElement> elementsOf(ItemStack stack)
    {
        DustElement single = AlchemicalDust.of(stack);
        if (single == null) single = CrystalElements.of(stack);
        if (single != null) return List.of(single);
        DustComposition compound = Compounds.of(stack);
        return compound == null ? List.of() : compound.elements();
    }

    public static boolean isGrindable(ItemStack stack)
    {
        return !elementsOf(stack).isEmpty();
    }

    // Fully researched: the player has discovered every element in it
    public static boolean researched(Player player, ItemStack stack)
    {
        List<DustElement> elements = elementsOf(stack);
        return !elements.isEmpty() && elements.stream().allMatch(element -> Compounds.knows(player, element));
    }

    // ---- clicking (StationInteractions: right-click the hole to put in, left-click it to take out) ----
    // The hole in the middle of the top, in model pixels (the grinder doesn't turn with facing)
    public static final CBPixelBox HOLE = new CBPixelBox(5, 12, 5, 11, 16.5, 11);

    @Override
    public List<StationSlot> slots()
    {
        StationSlot.Inserter putIn = (stack, player) -> {
            if (player != null && !researched(player, stack)) {
                player.sendOverlayMessage(Component.translatable("dust.dust_grinder.unresearched"));
                return 0;
            }
            if (!input.isEmpty() && !ItemStack.isSameItemSameComponents(input, stack)) return 0;
            int moved = Math.min(stack.getCount(), Math.min(INPUT_LIMIT, stack.getMaxStackSize()) - input.getCount());
            if (moved <= 0) return 0;
            if (input.isEmpty()) input = stack.copyWithCount(moved);
            else input.grow(moved);
            sync();
            return moved;
        };
        StationSlot.Extractor takeOut = max -> {
            if (isTurning() || input.isEmpty()) return ItemStack.EMPTY;
            ItemStack taken = input.split(Math.min(max, input.getCount()));
            sync();
            return taken;
        };
        return List.of(new StationSlot(HOLE, DustGrinderBlockEntity::isGrindable, putIn, () -> isTurning() ? ItemStack.EMPTY : input, takeOut));
    }

    // ---- turning ----

    // Starts a turn if there's something to grind and it isn't already turning. True if it started.
    public boolean startTurn(Level level)
    {
        if (isTurning() || input.isEmpty()) return false;
        turn = 0;
        level.playSound(null, worldPosition, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.6F, 0.9F + level.getRandom().nextFloat() * 0.2F);
        sync();
        return true;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DustGrinderBlockEntity grinder)
    {
        if (grinder.turn < 0) return;
        grinder.turn++;
        if (grinder.turn < TURN_TICKS) {
            grinder.setChanged();
            return;
        }
        grinder.turn = -1;
        grinder.grindOne((ServerLevel) level);
        grinder.sync();
    }

    // Client side: just count, for the animation
    public static void clientTick(Level level, BlockPos pos, BlockState state, DustGrinderBlockEntity grinder)
    {
        if (grinder.turn >= 0 && grinder.turn < TURN_TICKS) grinder.turn++;
    }

    // One item ground: each of its elements into a jar next to it, or out as an orb on top
    private void grindOne(ServerLevel level)
    {
        if (input.isEmpty()) return;
        List<DustElement> elements = elementsOf(input);
        input.shrink(1);
        if (input.isEmpty()) input = ItemStack.EMPTY;
        Vec3 top = Vec3.atBottomCenterOf(worldPosition).add(0, 1.15, 0);
        for (DustElement element : elements) {
            if (!intoJar(level, element)) ElementOrbEntity.spawn(level, element, 1, top);
        }
        level.playSound(null, worldPosition, SoundEvents.SAND_BREAK, SoundSource.BLOCKS, 0.7F, 1.2F);
    }

    // One dust of this element into the first dust receiver round the grinder that takes it; false if none does
    private boolean intoJar(ServerLevel level, DustElement element)
    {
        for (Direction side : Direction.values()) {
            if (level.getBlockEntity(worldPosition.relative(side)) instanceof IDustReceiver receiver
                    && receiver.insertDust(element, 1, false) > 0) return true;
        }
        return false;
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (!input.isEmpty()) output.store("input", ItemStack.OPTIONAL_CODEC, input);
        output.putInt("turn", turn);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.input = input.read("input", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        this.turn = input.getIntOr("turn", -1);
    }

    // Broken: the items in the hole drop
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (level != null && !input.isEmpty()) Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, input);
    }
}
