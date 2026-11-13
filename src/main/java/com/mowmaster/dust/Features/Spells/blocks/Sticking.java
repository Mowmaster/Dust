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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

import java.util.HashMap;
import java.util.Map;

// Slime's block effect: makes the block sticky.
//   A falling block (sand, gravel, concrete powder, an anvil...) sticks where it is: it won't fall, even with nothing
//   under it (mixin/FallingBlockStickyMixin stops its fall check). For good, until it's broken.
//   Anything else goes through Slime's conversion table (a piston becomes a sticky piston).
// Sticky spots are kept with the chunk the same way wards are (attachment STICKY: the spot and the block stuck there,
// so a different block put there later isn't sticky). Breaking the block, or pushing it with a piston, ends it.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class Sticking
{
    private Sticking() {}

    public static void stick(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof FallingBlock)) {
            SpellConversions.convert(cast, pos, face);
            return;
        }
        LevelChunk chunk = level.getChunkAt(pos);
        Map<BlockPos, Block> blocks = new HashMap<>(chunk.getData(DustAttachmentTypeRegistry.STICKY).blocks());
        blocks.put(pos.immutable(), state.getBlock());
        chunk.setData(DustAttachmentTypeRegistry.STICKY, new Warding.Wards(blocks));
        chunk.markUnsaved();
        level.sendParticles(ParticleTypes.ITEM_SLIME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 15, 0.4, 0.4, 0.4, 0.05);
        level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    // Is the block at pos stuck? (Only if it's still the block that was made sticky.)
    public static boolean isStuck(Level level, BlockPos pos)
    {
        Block stuck = level.getChunkAt(pos).getData(DustAttachmentTypeRegistry.STICKY).blocks().get(pos);
        return stuck != null && level.getBlockState(pos).is(stuck);
    }

    // Broken: it's not sticky any more (a block put back there later doesn't stick)
    @SubscribeEvent
    public static void onBlockBroken(BreakBlockEvent event)
    {
        if (!(event.getLevel() instanceof Level level)) return;
        LevelChunk chunk = level.getChunkAt(event.getPos());
        Warding.Wards sticky = chunk.getData(DustAttachmentTypeRegistry.STICKY);
        if (!sticky.blocks().containsKey(event.getPos())) return;
        Map<BlockPos, Block> blocks = new HashMap<>(sticky.blocks());
        blocks.remove(event.getPos());
        chunk.setData(DustAttachmentTypeRegistry.STICKY, new Warding.Wards(blocks));
        chunk.markUnsaved();
    }
}
