package com.mowmaster.dust.Features.DustStorage;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.DustComposition.ElementNames;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// A Dust Collector's dust: up to CAPACITY in all, of any elements. Every WORK_TICKS it
//   gathers   dust from the Dust Piles on the 3x3 blocks centred over it (as much as it has room for)
//   sends     each element out along its tubes (DustNetwork.insert: each tube network takes only its own element)
//   passes    an element none of its tubes have anywhere for to a Dust Collector beside it that does
// A Dust Jar used on it draws dust into the jar: its own element, or (an empty jar) the element the collector holds most of.
// Broken, its dust comes out as element orbs.
public class DustCollectorBlockEntity extends CBSyncedBlockEntity
{
    public static final int CAPACITY = 100;
    private static final int WORK_TICKS = 10;
    private static final int PUSH_AMOUNT = 20;
    private static final Codec<Map<DustElement, Integer>> HELD_CODEC = Codec.unboundedMap(DustElements.CODEC, Codec.INT);

    private final Map<DustElement, Integer> held = new LinkedHashMap<>();

    public DustCollectorBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.DUST_COLLECTOR.get(), pos, state);
    }

    public int total()
    {
        int total = 0;
        for (int amount : held.values()) total += amount;
        return total;
    }

    // Puts up to amount in (as much as there's room for); returns how much went
    public int accept(DustElement element, int amount)
    {
        int moved = Math.min(amount, CAPACITY - total());
        if (moved <= 0) return 0;
        held.merge(element, moved, Integer::sum);
        sync();
        return moved;
    }

    private int remove(DustElement element, int amount)
    {
        int has = held.getOrDefault(element, 0);
        int taken = Math.min(has, amount);
        if (taken <= 0) return 0;
        if (has - taken <= 0) held.remove(element);
        else held.put(element, has - taken);
        sync();
        return taken;
    }

    public void serverTick(ServerLevel level)
    {
        if ((level.getGameTime() + worldPosition.asLong()) % WORK_TICKS != 0) return;
        gather(level);
        send(level);
    }

    // The piles on the 3x3 blocks centred over it
    private void gather(ServerLevel level)
    {
        BlockPos above = worldPosition.above();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (total() >= CAPACITY) return;
                BlockPos pos = above.offset(dx, 0, dz);
                if (!level.isLoaded(pos) || !(level.getBlockEntity(pos) instanceof DustPileBlockEntity pile)) continue;
                for (Map.Entry<DustElement, Integer> entry : List.copyOf(pile.held().entrySet())) {
                    int room = CAPACITY - total();
                    if (room <= 0) return;
                    int taken = pile.take(entry.getKey(), Math.min(room, entry.getValue()));
                    if (taken > 0) accept(entry.getKey(), taken);
                }
            }
        }
    }

    // Out along its tubes; what they've nowhere for, to a collector beside it that has
    private void send(ServerLevel level)
    {
        for (Map.Entry<DustElement, Integer> entry : List.copyOf(held.entrySet())) {
            DustElement element = entry.getKey();
            int amount = Math.min(PUSH_AMOUNT, entry.getValue());
            if (DustNetwork.routes(level, worldPosition, element)) {
                int went = DustNetwork.insert(level, worldPosition, element, amount);
                if (went > 0) remove(element, went);
                continue;
            }
            for (Direction side : Direction.values()) {
                BlockPos next = worldPosition.relative(side);
                if (!(level.getBlockEntity(next) instanceof DustCollectorBlockEntity other) || !DustNetwork.routes(level, next, element)) continue;
                int went = other.accept(element, amount);
                if (went > 0) remove(element, went);
                break;
            }
        }
    }

    // A Dust Jar in the hand: dust into it (its element, or the one held most of); a jar from a stack of them is split off
    public boolean fillJar(Player player, InteractionHand hand)
    {
        ItemStack jar = player.getItemInHand(hand);
        DustJarContents contents = jar.getOrDefault(DustComponentDataRegistry.DUST_JAR.get(), DustJarContents.EMPTY);
        DustElement element = contents.element().orElse(null);
        if (element == null) {
            int most = 0;
            for (Map.Entry<DustElement, Integer> entry : held.entrySet()) {
                if (entry.getValue() > most) {
                    most = entry.getValue();
                    element = entry.getKey();
                }
            }
        }
        if (element == null || held.getOrDefault(element, 0) <= 0) return false;
        int taken = remove(element, DustJarContents.CAPACITY - contents.amount());
        if (taken <= 0) return false;
        DustJarContents filled = new DustJarContents(Optional.of(element), contents.amount() + taken, contents.labels());
        if (jar.getCount() > 1) {
            ItemStack one = jar.split(1);
            one.set(DustComponentDataRegistry.DUST_JAR.get(), filled);
            if (!player.getInventory().add(one)) player.spawnAtLocation((ServerLevel) level, one);
        } else {
            jar.set(DustComponentDataRegistry.DUST_JAR.get(), filled);
        }
        level.playSound(null, worldPosition, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 0.6F, 1.2F);
        return true;
    }

    // "Dust Collector: 40/100 | 25 Stone, 15 Mud"
    public Component status(Player player)
    {
        MutableComponent line = Component.empty().append(getBlockState().getBlock().getName()).append(": " + total() + "/" + CAPACITY);
        boolean first = true;
        for (Map.Entry<DustElement, Integer> entry : held.entrySet()) {
            line.append(first ? " | " : ", ").append(entry.getValue() + " ").append(ElementNames.forViewer(entry.getKey(), player));
            first = false;
        }
        return line;
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.store("held", HELD_CODEC, held);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        held.clear();
        input.read("held", HELD_CODEC).ifPresent(held::putAll);
    }

    // Broken: its dust comes out as element orbs
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (!(level instanceof ServerLevel server)) return;
        held.forEach((element, amount) -> com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity.spawn(server, element, amount, Vec3.atCenterOf(pos)));
        held.clear();
    }
}
