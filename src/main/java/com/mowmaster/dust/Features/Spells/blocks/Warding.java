package com.mowmaster.dust.Features.Spells.blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDestroyBlockEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

import java.util.HashMap;
import java.util.Map;

// Armor's block effect: a warded block. It keeps its look, but breaks as slowly as obsidian, shrugs off explosions, and
// withers and the ender dragon can't break it. Wards are kept with the chunk (a chunk data attachment: the ward's spot
// and the block it was put on, so a different block put there later isn't warded). Breaking the block removes its ward.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class Warding
{
    // Obsidian's hardness: a warded block takes as long to break as obsidian would
    private static final float OBSIDIAN = 50.0F;

    private Warding() {}

    // A chunk's wards: spot -> the block warded there
    public record Wards(Map<BlockPos, Block> blocks)
    {
        public static final Wards NONE = new Wards(Map.of());

        private record Entry(BlockPos pos, Block block)
        {
            static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    BlockPos.CODEC.fieldOf("pos").forGetter(Entry::pos),
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(Entry::block)
            ).apply(instance, Entry::new));
        }

        // Saved as a list of (pos, block): map keys have to be text
        public static final Codec<Wards> CODEC = Entry.CODEC.listOf().xmap(
                entries -> {
                    Map<BlockPos, Block> map = new HashMap<>();
                    entries.forEach(entry -> map.put(entry.pos(), entry.block()));
                    return new Wards(map);
                },
                wards -> wards.blocks().entrySet().stream().map(entry -> new Entry(entry.getKey(), entry.getValue())).toList());
    }

    private static Wards wardsAt(Level level, BlockPos pos)
    {
        return level.getChunkAt(pos).getData(DustAttachmentTypeRegistry.WARDS);
    }

    private static void setWards(Level level, BlockPos pos, Wards wards)
    {
        LevelChunk chunk = level.getChunkAt(pos);
        chunk.setData(DustAttachmentTypeRegistry.WARDS, wards);
        chunk.markUnsaved();
    }

    public static boolean isWarded(Level level, BlockPos pos)
    {
        Block warded = wardsAt(level, pos).blocks().get(pos);
        return warded != null && level.getBlockState(pos).is(warded);
    }

    public static boolean ward(ServerLevel level, BlockPos pos)
    {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || !state.getFluidState().isEmpty() || state.getDestroySpeed(level, pos) < 0) return false;
        Map<BlockPos, Block> blocks = new HashMap<>(wardsAt(level, pos).blocks());
        blocks.put(pos.immutable(), state.getBlock());
        setWards(level, pos, new Wards(blocks));
        level.sendParticles(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 20, 0.5, 0.5, 0.5, 0.5);
        return true;
    }

    private static void unward(Level level, BlockPos pos)
    {
        Wards wards = wardsAt(level, pos);
        if (!wards.blocks().containsKey(pos)) return;
        Map<BlockPos, Block> blocks = new HashMap<>(wards.blocks());
        blocks.remove(pos);
        setWards(level, pos, new Wards(blocks));
    }

    // ---- what a ward does ----

    // As slow to break as obsidian
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        if (event.getPosition().isEmpty()) return;
        BlockPos pos = event.getPosition().get();
        Level level = event.getEntity().level();
        if (!isWarded(level, pos)) return;
        float hardness = Math.max(0.1F, event.getState().getDestroySpeed(level, pos));
        event.setNewSpeed(event.getNewSpeed() * hardness / OBSIDIAN);
    }

    // Explosions pass it by
    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event)
    {
        event.getAffectedBlocks().removeIf(pos -> isWarded(event.getLevel(), pos));
    }

    // Withers and the dragon can't break it
    @SubscribeEvent
    public static void onMobDestroysBlock(LivingDestroyBlockEvent event)
    {
        if (isWarded(event.getEntity().level(), event.getPos())) event.setCanceled(true);
    }

    // Broken after all: the ward goes with it
    @SubscribeEvent
    public static void onBlockBroken(net.neoforged.neoforge.event.level.block.BreakBlockEvent event)
    {
        if (event.getLevel() instanceof Level level) unward(level, event.getPos());
    }
}
