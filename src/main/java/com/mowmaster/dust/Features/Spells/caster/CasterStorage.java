package com.mowmaster.dust.Features.Spells.caster;

import com.mowmaster.dust.Features.DustStorage.IDustReceiver;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementOrbEntity;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.mowlib.transport.ExperienceTransportType;
import com.mowmaster.mowlib.transport.TransportType;
import com.mowmaster.mowlib.transport.TransportTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

// A Spell Caster's hands and pockets. A player's spell takes from and gives to the player; one a Spell Caster casts uses
// the containers around it instead (every block touching it, edges and corners too, the six it shares a face with
// first):
//   items        anything holding items (chests, barrels, hoppers, other mods' storage), through MowLib's item
//                transport type
//   experience   anything holding experience through MowLib's experience handler (mowlib:experience_handler)
//   dust         Dust Jars, and anything else that takes element dust (IDustReceiver)
// What uses it: Plants on a block plants what's in the containers (seeds, saplings, flowers...) instead of a wild
// flower (UtilityParts.plantFlower), and Vacuum on a block puts what it pulls in into them: items, experience orbs, and
// element orbs into the jars (MobParts.vacuumBlock).
public final class CasterStorage
{
    private final ServerLevel level;
    private final BlockPos caster;

    private CasterStorage(ServerLevel level, BlockPos caster)
    {
        this.level = level;
        this.caster = caster;
    }

    // The storage around the Spell Caster that cast this; null for a spell a creature cast, or when the Spell Caster
    // that cast it isn't there any more (a Vacuum still pulling after it was broken)
    public static @Nullable CasterStorage of(SpellCast cast)
    {
        BlockPos at = cast.casterBlock();
        if (at == null || !cast.level().isLoaded(at) || !(cast.level().getBlockEntity(at) instanceof SpellCasterBlockEntity)) return null;
        return new CasterStorage(cast.level(), at);
    }

    // What a Vacuum can put in: items, experience orbs, element orbs
    public static boolean takes(Entity entity)
    {
        return entity instanceof ItemEntity || entity instanceof ExperienceOrb || entity instanceof ElementOrbEntity;
    }

    // ---- where ----

    // The blocks touching it, nearest first: faces, then edges, then corners
    private List<BlockPos> around()
    {
        List<BlockPos> blocks = new ArrayList<>();
        for (BlockPos at : BlockPos.betweenClosed(caster.offset(-1, -1, -1), caster.offset(1, 1, 1))) {
            // betweenClosed reuses one mutable position: copied before it's kept
            if (!at.equals(caster) && level.isLoaded(at)) blocks.add(at.immutable());
        }
        blocks.sort(Comparator.comparingInt(at -> at.distManhattan(caster)));
        return blocks;
    }

    // The side of a block next to the Spell Caster that faces it, or null for one only touching an edge or a corner
    // (then its storage is asked for as a whole)
    private @Nullable Direction sideFacingCaster(BlockPos pos)
    {
        int dx = caster.getX() - pos.getX(), dy = caster.getY() - pos.getY(), dz = caster.getZ() - pos.getZ();
        if (Math.abs(dx) + Math.abs(dy) + Math.abs(dz) != 1) return null;
        return Direction.getNearest(dx, dy, dz, Direction.UP);
    }

    // Each block's storage of this transport type (items, experience...), nearest first
    private <R extends Resource> List<ResourceHandler<R>> handlers(TransportType<R> type)
    {
        List<ResourceHandler<R>> found = new ArrayList<>();
        for (BlockPos pos : around()) {
            ResourceHandler<R> handler = type.getBlockHandler(level, pos, sideFacingCaster(pos));
            if (handler != null) found.add(handler);
        }
        return found;
    }

    // Whether anything holding items is around it
    public boolean hasItems()
    {
        return !handlers(TransportTypes.ITEMS.get()).isEmpty();
    }

    // ---- taking ----

    // Uses up one item from the containers: the first one `which` accepts that `use` succeeds with. `use` is handed a
    // stack of one to do something with (plant it...) and says whether it did. The item only comes out of its container
    // if it did: it's taken out inside a transaction, which is committed only then (closing a transaction that wasn't
    // committed puts everything back). False if nothing was used.
    public boolean useOne(Predicate<ItemStack> which, Predicate<ItemStack> use)
    {
        // Each kind of item is only tried once: if one wheat seed won't plant here, the next won't either
        Set<Item> tried = new HashSet<>();
        for (ResourceHandler<ItemResource> handler : handlers(TransportTypes.ITEMS.get())) {
            for (int slot = 0; slot < handler.size(); slot++) {
                ItemResource resource = handler.getResource(slot);
                if (resource.isEmpty() || handler.getAmountAsInt(slot) <= 0) continue;
                ItemStack stack = resource.toStack(1);
                if (!tried.add(stack.getItem()) || !which.test(stack)) continue;
                // try-with-resources: the transaction is closed however the block is left (aborted unless committed)
                try (Transaction transaction = Transaction.openRoot()) {
                    if (handler.extract(slot, resource, 1, transaction) < 1 || !use.test(stack)) continue;
                    transaction.commit();
                    return true;
                }
            }
        }
        return false;
    }

    // ---- putting in ----

    // Puts a loose thing in: an item into the containers holding items, an experience orb into one holding experience,
    // an element orb into the dust holders. Whatever doesn't fit stays where it is. True if any of it went in.
    public boolean absorb(Entity entity)
    {
        if (entity.isRemoved()) return false;
        Vec3 from = entity.position();
        Vec3 to = Vec3.atCenterOf(caster);
        if (entity instanceof ElementOrbEntity orb) {
            if (!absorbOrb(orb)) return false;
            level.sendParticles(new DustParticleOptions(orb.getElement().color(), 1.0F), from.x, from.y, from.z, 6, 0.15, 0.15, 0.15, 0.02);
            return true;
        }
        if (entity instanceof ExperienceOrb) {
            if (!absorbInto(TransportTypes.EXPERIENCE.get(), entity)) return false;
            TransportTypes.EXPERIENCE.get().sendTransferParticles(level, from, to, ExperienceTransportType.EXPERIENCE);
            return true;
        }
        if (entity instanceof ItemEntity item) {
            ItemResource moved = ItemResource.of(item.getItem());
            if (!absorbInto(TransportTypes.ITEMS.get(), entity)) return false;
            TransportTypes.ITEMS.get().sendTransferParticles(level, from, to, moved);
            return true;
        }
        return false;
    }

    // Into each storage of the type in turn until it's all in (MowLib's absorbEntity takes what fits, and removes the
    // entity once it's all gone)
    private <R extends Resource> boolean absorbInto(TransportType<R> type, Entity entity)
    {
        boolean any = false;
        for (ResourceHandler<R> handler : handlers(type)) {
            if (entity.isRemoved()) break;
            if (type.absorbEntity(level, entity, handler, Integer.MAX_VALUE)) any = true;
        }
        return any;
    }

    // An element orb's dust into the dust holders around it, as much as fits; the orb is worth what's left (gone if
    // none is)
    private boolean absorbOrb(ElementOrbEntity orb)
    {
        int value = orb.getValue();
        int left = value;
        for (BlockPos pos : around()) {
            if (left <= 0) break;
            if (level.getBlockEntity(pos) instanceof IDustReceiver receiver) left -= receiver.insertDust(orb.getElement(), left, false);
        }
        if (left == value) return false;
        if (left <= 0) orb.discard();
        else orb.setValue(left);
        return true;
    }
}
