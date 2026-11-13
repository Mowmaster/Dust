package com.mowmaster.dust.Features.Spells.blocks;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.conversions.SpellConversions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Slime's block effect: the face that was hit gets a slime patch (StickyFaces, drawn by Client/StickyPatchRenderer).
//   Two blocks are stuck together when either one has a patch on the face between them.
//   A falling block (sand, gravel, an anvil...) that's stuck to anything doesn't fall (mixin/FallingBlockStickyMixin).
//   A piston moving a block drags the blocks stuck to it along, like a slime block (mixin/PistonStructureResolverMixin),
//   and the patches move with their blocks (mixin/PistonBaseBlockStickyMixin).
//   A wind charge blast, purifying the block (Healing), or breaking it takes the patches off.
// A piston itself becomes a sticky piston instead (Slime's conversion table).
@EventBusSubscriber(modid = DustReferences.MODID)
public final class Sticking
{
    private Sticking() {}

    public static void stick(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof PistonBaseBlock) {
            SpellConversions.convert(cast, pos, face);
            return;
        }
        if (state.isAir() || !state.getFluidState().isEmpty()) return;
        StickyFaces.Entry entry = entry(level, pos);
        int faces = (entry == null ? 0 : entry.faces()) | StickyFaces.bit(face);
        set(level, pos, new StickyFaces.Entry(state.getBlock(), faces));
        Vec3 at = Vec3.atCenterOf(pos).add(face.getStepX() * 0.55, face.getStepY() * 0.55, face.getStepZ() * 0.55);
        level.sendParticles(ParticleTypes.ITEM_SLIME, at.x, at.y, at.z, 10, 0.25, 0.25, 0.25, 0.05);
        level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    // ---- reading ----

    // The block at pos's patches, if it's still the block they were put on
    public static StickyFaces.Entry entry(Level level, BlockPos pos)
    {
        StickyFaces.Entry entry = level.getChunkAt(pos).getData(DustAttachmentTypeRegistry.STICKY_FACES).blocks().get(pos);
        return entry != null && level.getBlockState(pos).is(entry.block()) ? entry : null;
    }

    // Is there a patch between pos and the block next to it on that side? (On either block's facing side.)
    public static boolean stuckTogether(Level level, BlockPos pos, Direction side)
    {
        StickyFaces.Entry own = entry(level, pos);
        if (own != null && own.has(side)) return true;
        StickyFaces.Entry other = entry(level, pos.relative(side));
        return other != null && other.has(side.getOpposite());
    }

    // A falling block held up: stuck to a block on any side (not to air or a fluid)
    public static boolean isStuck(Level level, BlockPos pos)
    {
        for (Direction side : Direction.values()) {
            BlockState next = level.getBlockState(pos.relative(side));
            if (!next.isAir() && next.getFluidState().isEmpty() && stuckTogether(level, pos, side)) return true;
        }
        return false;
    }

    // ---- writing ----

    private static void set(Level level, BlockPos pos, StickyFaces.Entry entry)
    {
        LevelChunk chunk = level.getChunkAt(pos);
        Map<BlockPos, StickyFaces.Entry> blocks = new HashMap<>(chunk.getData(DustAttachmentTypeRegistry.STICKY_FACES).blocks());
        if (entry == null) {
            if (blocks.remove(pos) == null) return;
        } else {
            blocks.put(pos.immutable(), entry);
        }
        chunk.setData(DustAttachmentTypeRegistry.STICKY_FACES, new StickyFaces(blocks));
        chunk.markUnsaved();
    }

    // Takes every patch off the block at pos; true if it had any. Falling blocks around it get a chance to fall.
    public static boolean clear(Level level, BlockPos pos)
    {
        StickyFaces.Entry had = level.getChunkAt(pos).getData(DustAttachmentTypeRegistry.STICKY_FACES).blocks().get(pos);
        if (had == null) return false;
        set(level, pos, null);
        wake(level, pos);
        return true;
    }

    // A falling block that may have just lost what held it checks again (its own and its neighbours' fall ticks)
    private static void wake(Level level, BlockPos pos)
    {
        for (BlockPos at : new BlockPos[] {pos, pos.above(), pos.below(), pos.north(), pos.south(), pos.east(), pos.west()}) {
            BlockState state = level.getBlockState(at);
            if (state.getBlock() instanceof FallingBlock) level.scheduleTick(at, state.getBlock(), 2);
        }
    }

    // A piston is about to move these blocks one step: their patches go with them (called from PistonBaseBlockStickyMixin).
    // All are lifted first, then put down, so a block moving into another's old spot doesn't lose its patches.
    public static void moveWith(Level level, List<BlockPos> moving, Direction direction)
    {
        List<BlockPos> from = new ArrayList<>();
        List<StickyFaces.Entry> entries = new ArrayList<>();
        for (BlockPos pos : moving) {
            StickyFaces.Entry entry = entry(level, pos);
            if (entry == null) continue;
            from.add(pos);
            entries.add(entry);
        }
        for (BlockPos pos : from) set(level, pos, null);
        for (int i = 0; i < from.size(); i++) set(level, from.get(i).relative(direction), entries.get(i));
    }

    // ---- what takes patches off ----

    @SubscribeEvent
    public static void onBlockBroken(BreakBlockEvent event)
    {
        if (event.getLevel() instanceof Level level) clear(level, event.getPos());
    }

    // A wind charge blasts the patches off everything it reaches
    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event)
    {
        if (!(event.getExplosion().getDirectSourceEntity() instanceof AbstractWindCharge)) return;
        Level level = event.getLevel();
        Vec3 centre = event.getExplosion().center();
        int reach = (int) Math.ceil(event.getExplosion().radius()) + 1;
        BlockPos middle = BlockPos.containing(centre);
        for (BlockPos pos : BlockPos.betweenClosed(middle.offset(-reach, -reach, -reach), middle.offset(reach, reach, reach))) {
            if (Vec3.atCenterOf(pos).distanceTo(centre) <= reach && clear(level, pos) && level instanceof ServerLevel server) {
                server.sendParticles(ParticleTypes.ITEM_SLIME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 6, 0.4, 0.4, 0.4, 0.1);
            }
        }
    }
}
