package com.mowmaster.dust.Features.Crystals.crystalizer;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.Crystals.CrystalNodeBlock;
import com.mowmaster.dust.Features.Crystals.CrystalSize;
import com.mowmaster.dust.Features.Crystals.ElementCrystals;
import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.DustStorage.DustNetwork;
import com.mowmaster.dust.Features.DustStorage.IDustHolder;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity;
import com.mowmaster.dust.Features.Machines.IStationSlots;
import com.mowmaster.dust.Features.Machines.StationSlot;
import com.mowmaster.dust.Features.Machines.StationSlotHandler;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import com.mowmaster.mowlib.util.CBPixelBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The Dust Crystalizer grows a crystal of the element dust piped into it (Glass Dust Tubes: dust poured along its network,
// or drawn from the Dust Jars on it once a second). No items go in. Each stage, in order:
//   stage 1  Crystal          STAGE_TICKS[0] = 100 ticks, costs 1 dust   the element's crystal item, drawn on top
//   stage 2  Small Crystal    100 more ticks, no dust                    the small crystal block, placed on top of it
//   stage 3  Medium Crystal   300 more ticks, costs 3 more dust          the block on top grows to the medium one
//   stage 4  Large Crystal    1200 more ticks, costs 12 more dust        then the large one
// A stage's dust is used when the stage starts; it waits (keeping its progress) for the dust, and for the space on top to
// be clear before the small crystal can form. It stops at the Large Crystal until the crystal is taken. The crystal can be
// taken at any stage (the item of that stage, and the block on top goes): by hand (an empty hand, or left-clicking the
// top), or by automation from any side (a hopper below, pipes). Breaking the crystal on top also takes it (dropping as
// that crystal does), and the crystalizer starts again. In all a small crystal takes 1 dust, a medium 4 and a large 16
// (each worth what it gives back when broken: CrystalSize.value). The element is set by the first dust in, and kept
// until the crystalizer is empty with nothing grown; it holds at most the dust still to be used (16 at the start).
// Comparators read the stage (0-4). It never gives out redstone itself, so a hopper below can be locked by its own
// redstone without the crystalizer turning it off.
@EventBusSubscriber(modid = DustReferences.MODID)
public class DustCrystalizerBlockEntity extends CBSyncedBlockEntity implements IDustHolder, IStationSlots
{
    public static final int[] STAGE_TICKS = {100, 100, 300, 1200};
    public static final int[] STAGE_COST = {1, 0, 3, 12};
    public static final int LAST_STAGE = 4;
    private static final int DRAW_TICKS = 20;
    // The top, where the crystal sits (left-clicking it takes the crystal)
    private static final CBPixelBox TOP = new CBPixelBox(4, 12, 4, 12, 16.5, 12);

    private final StationSlotHandler output = new StationSlotHandler(this, StationSlot.Role.OUTPUT);

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        event.registerBlockEntity(Capabilities.Item.BLOCK, DustBlockEntityRegistry.DUST_CRYSTALIZER.get(), (crystalizer, side) -> crystalizer.output);
    }

    private @Nullable DustElement element;
    private int stored;
    private int stage;
    // Ticks into the next stage, or -1 while it hasn't started (waiting for dust or space)
    private int progress = -1;

    public DustCrystalizerBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.DUST_CRYSTALIZER.get(), pos, state);
    }

    public @Nullable DustElement element() { return element; }
    public int stage() { return stage; }
    public int stored() { return stored; }

    // ---- the crystal ----

    // The crystal it holds as an item (empty at stage 0)
    public ItemStack crystal()
    {
        if (element == null || stage <= 0) return ItemStack.EMPTY;
        return switch (stage) {
            case 1 -> CrystalElements.stackOf(element);
            case 2 -> new ItemStack(ElementCrystals.SMALL_ITEMS.get(element).get());
            case 3 -> new ItemStack(ElementCrystals.MEDIUM_ITEMS.get(element).get());
            default -> new ItemStack(ElementCrystals.LARGE_ITEMS.get(element).get());
        };
    }

    // Takes the crystal (its item; the block on top goes without dropping anything) and starts again
    public ItemStack take()
    {
        ItemStack crystal = crystal();
        if (crystal.isEmpty() || level == null) return ItemStack.EMPTY;
        if (stage >= 2 && grownAbove()) level.setBlock(worldPosition.above(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        restart();
        return crystal;
    }

    // Back to nothing grown (the element is kept while it holds dust)
    private void restart()
    {
        stage = 0;
        progress = -1;
        if (stored <= 0) element = null;
        changed();
    }

    // The crystal block of a stage (2-4)
    private static CrystalSize size(int stage)
    {
        return stage == 2 ? CrystalSize.SMALL : stage == 3 ? CrystalSize.MEDIUM : CrystalSize.LARGE;
    }

    // Whether the block on top is this crystalizer's crystal for its stage
    private boolean grownAbove()
    {
        if (level == null || element == null || stage < 2) return false;
        BlockState above = level.getBlockState(worldPosition.above());
        return above.getBlock() instanceof CrystalNodeBlock node && node.element().equals(element) && node.size() == size(stage);
    }

    private void placeCrystal(Level level)
    {
        if (element == null) return;
        BlockState crystal = ElementCrystals.node(element, size(stage)).defaultBlockState().setValue(AmethystClusterBlock.FACING, Direction.UP);
        level.setBlock(worldPosition.above(), crystal, Block.UPDATE_ALL);
    }

    // ---- growing ----

    public static void serverTick(Level level, BlockPos pos, BlockState state, DustCrystalizerBlockEntity crystalizer)
    {
        if (level instanceof ServerLevel server) crystalizer.tick(server);
    }

    private void tick(ServerLevel level)
    {
        // The crystal on top was broken (or pushed, or replaced): start again
        if (stage >= 2 && !grownAbove()) {
            restart();
            return;
        }
        // Once a second, spread by position, draw what it still needs from the jars on its tubes
        if ((level.getGameTime() + worldPosition.asLong()) % DRAW_TICKS == 0) draw(level);
        if (stage >= LAST_STAGE || element == null) return;
        if (progress < 0) {
            if (stored < STAGE_COST[stage]) return;
            stored -= STAGE_COST[stage];
            progress = 0;
            changed();
        }
        if (progress < STAGE_TICKS[stage]) {
            progress++;
            setChanged();
            return;
        }
        // The small crystal needs the space on top clear: it waits there, finished, until it is
        if (stage == 1 && !level.getBlockState(worldPosition.above()).canBeReplaced()) return;
        stage++;
        progress = -1;
        if (stage >= 2) placeCrystal(level);
        level.playSound(null, worldPosition, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.8F, 0.8F + 0.1F * stage);
        changed();
    }

    // Dust still to be used to reach the Large Crystal from here
    private int needed()
    {
        int need = 0;
        for (int s = stage; s < LAST_STAGE; s++) {
            if (s == stage && progress >= 0) continue;
            need += STAGE_COST[s];
        }
        return need;
    }

    private int room()
    {
        return Math.max(0, needed() - stored);
    }

    // Draws what it still needs (of its element, or the element its network's jars are set to) out of its network's jars
    private void draw(ServerLevel level)
    {
        int room = room();
        if (room <= 0) return;
        DustElement want = element;
        if (want == null) {
            DustNetwork.Holds holds = DustNetwork.networkElement(level, worldPosition);
            if (holds.mixed() || holds.element() == null) return;
            want = holds.element();
        }
        int taken = DustNetwork.extract(level, worldPosition, want, room);
        if (taken <= 0) return;
        element = want;
        stored += taken;
        changed();
    }

    // Saved, synced, and comparators told
    private void changed()
    {
        sync();
        if (level != null) level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
    }

    // ---- tubes: dust in (its own element, or any while it has none), nothing out ----

    @Override
    public int insertDust(DustElement dust, int amount, boolean simulate)
    {
        if (amount <= 0 || (element != null && !element.equals(dust))) return 0;
        int added = Math.min(amount, room());
        if (added <= 0 || simulate) return Math.max(0, added);
        element = dust;
        stored += added;
        changed();
        return added;
    }

    @Override
    public int dustAmount(DustElement dust)
    {
        return 0;
    }

    @Override
    public int extractDust(DustElement dust, int amount, boolean simulate)
    {
        return 0;
    }

    @Override
    public boolean prefersDust(DustElement dust)
    {
        return element != null && element.equals(dust) && room() > 0;
    }

    @Override
    public boolean takesDustFrom(Direction side)
    {
        return side != Direction.UP;
    }

    // ---- automation and left-clicks: the crystal comes out (StationSlotHandler, StationInteractions) ----

    @Override
    public List<StationSlot> slots()
    {
        StationSlot.Inserter nothingIn = (stack, player) -> 0;
        StationSlot.Extractor takeOut = max -> max <= 0 ? ItemStack.EMPTY : take();
        return List.of(new StationSlot(TOP, stack -> false, nothingIn, this::crystal, takeOut, StationSlot.Role.OUTPUT, 1));
    }

    // ---- saving ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (element != null) output.store("element", DustElements.CODEC, element);
        output.putInt("stored", stored);
        output.putInt("stage", stage);
        output.putInt("progress", progress);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        element = input.read("element", DustElements.CODEC).orElse(null);
        stored = input.getIntOr("stored", 0);
        stage = input.getIntOr("stage", 0);
        progress = input.getIntOr("progress", -1);
    }

    // Broken: a first-stage crystal drops, and the dust it held comes out as an orb (a grown crystal on top stays, and
    // pops off once its support is gone)
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (!(level instanceof ServerLevel server) || element == null) return;
        if (stage == 1) Block.popResource(server, pos.above(), crystal());
        if (stored > 0) ElementOrbEntity.spawn(server, element, stored, Vec3.atCenterOf(pos));
        stored = 0;
        stage = 0;
    }
}
