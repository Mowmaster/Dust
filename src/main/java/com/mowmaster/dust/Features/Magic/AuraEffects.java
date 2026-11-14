package com.mowmaster.dust.Features.Magic;

import com.mowmaster.dust.DustReferences;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;
import org.jspecify.annotations.Nullable;

// What a chunk's aura does, by its richness (ChunkAura: 1 is normal). Amplified blocks (Magic) feel it further from
// normal, so they get more of whichever side they're on.
//   rich         (1.5+)  crops grow faster; players' mana regenerates faster (PlayerMagicHelper)
//   overcharged  (3+)    monsters spawning here may come out empowered (more health and damage) or with a twin. Each one
//                        feeds on the aura, which is how an overcharged chunk calms down. (Rifts come later.)
//   thin         (0.5-)  crops struggle to grow
//   barren       (0.25-) crops can wither and die; surface water evaporates; grass dries to dirt, dirt to coarse dirt
// Corruption also spreads more slowly the thinner the aura is (ChunkAura.spreadFactor).
@EventBusSubscriber(modid = DustReferences.MODID)
public final class AuraEffects
{
    public static final float RICH = 1.5F;
    public static final float OVERCHARGED = 3.0F;
    public static final float THIN = 0.5F;
    public static final float BARREN = 0.25F;
    // How much aura an empowered or extra monster feeds on
    public static final float SPAWN_COST = 25;
    // Surface spots looked at per barren chunk per update
    private static final int BARREN_SPOTS = 3;

    private static final Identifier EMPOWERED_HEALTH = Identifier.fromNamespaceAndPath(DustReferences.MODID, "aura_empowered_health");
    private static final Identifier EMPOWERED_DAMAGE = Identifier.fromNamespaceAndPath(DustReferences.MODID, "aura_empowered_damage");

    private AuraEffects() {}

    // A name for the richness (null when it's ordinary)
    public static @Nullable String stateOf(float richness)
    {
        if (richness >= OVERCHARGED) return "overcharged";
        if (richness >= RICH) return "rich";
        if (richness <= BARREN) return "barren";
        if (richness <= THIN) return "thin";
        return null;
    }

    // ---- players ----

    // Extra steps of mana regeneration a tick for a player standing here (PlayerMagicHelper.onPlayerTick)
    public static int regenBonus(ServerLevel level, BlockPos pos)
    {
        return ChunkAura.richnessAt(level, pos) >= RICH ? 1 : 0;
    }

    // ---- crops ----

    // Rich: a chance to grow when it otherwise wouldn't. Thin: a chance not to. Barren: sometimes it dies.
    @SubscribeEvent
    public static void onCropGrow(CropGrowEvent.Pre event)
    {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        BlockPos pos = event.getPos();
        float richness = ChunkAura.richnessAt(level, pos);
        RandomSource random = level.getRandom();
        if (richness >= RICH) {
            if (random.nextFloat() < Math.min(0.5F, (richness - 1) * 0.25F)) event.setResult(CropGrowEvent.Pre.Result.GROW);
        } else if (richness <= THIN) {
            if (richness <= BARREN && random.nextFloat() < 0.1F) {
                wither(level, pos);
                event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
            } else if (random.nextFloat() < (THIN - richness) / THIN) {
                event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
            }
        }
    }

    // A crop dies: a dead bush where one can stand, nothing where it can't
    private static void wither(ServerLevel level, BlockPos pos)
    {
        BlockState dead = Blocks.DEAD_BUSH.defaultBlockState();
        level.setBlockAndUpdate(pos, dead.canSurvive(level, pos) ? dead : Blocks.AIR.defaultBlockState());
        level.sendParticles(ParticleTypes.ASH, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8, 0.3, 0.3, 0.3, 0);
    }

    // ---- monsters ----

    // A monster spawning (naturally, from a spawner, a spawn egg...) in an overcharged chunk may be empowered, or bring
    // a twin; either way it feeds on the aura
    @SubscribeEvent
    public static void onSpawn(FinalizeSpawnEvent event)
    {
        Mob mob = event.getEntity();
        if (!(mob instanceof Enemy)) return;
        EntitySpawnReason reason = event.getSpawnType();
        // Not twins (EVENT), and not mobs placed while the world generates: then the level is a worldgen region on
        // another thread, where looking up chunks or adding entities isn't safe
        if (reason == EntitySpawnReason.EVENT || reason == EntitySpawnReason.CHUNK_GENERATION || reason == EntitySpawnReason.STRUCTURE) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        BlockPos pos = BlockPos.containing(event.getX(), event.getY(), event.getZ());
        float richness = ChunkAura.richnessAt(level, pos);
        if (richness < OVERCHARGED) return;
        RandomSource random = level.getRandom();
        float chance = Math.min(0.6F, 0.15F + (richness - OVERCHARGED) * 0.15F);
        if (random.nextFloat() < chance) {
            empower(mob);
            ChunkAura.take(level, pos, SPAWN_COST);
        }
        if (random.nextFloat() < chance * 0.5F) {
            // EVENT: so the twin doesn't roll for a twin of its own
            Mob twin = (Mob) mob.getType().create(level, EntitySpawnReason.EVENT);
            if (twin != null) {
                twin.snapTo(event.getX(), event.getY(), event.getZ(), random.nextFloat() * 360, 0);
                level.addFreshEntity(twin);
                ChunkAura.take(level, pos, SPAWN_COST);
            }
        }
    }

    // +50% health, +30% damage, for good
    private static void empower(Mob mob)
    {
        AttributeInstance health = mob.getAttribute(Attributes.MAX_HEALTH);
        if (health != null && !health.hasModifier(EMPOWERED_HEALTH)) {
            health.addPermanentModifier(new AttributeModifier(EMPOWERED_HEALTH, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            mob.setHealth(mob.getMaxHealth());
        }
        AttributeInstance damage = mob.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damage != null && !damage.hasModifier(EMPOWERED_DAMAGE)) {
            damage.addPermanentModifier(new AttributeModifier(EMPOWERED_DAMAGE, 0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    // ---- the land ----

    // Every update, for a chunk away from normal (ChunkAura): a barren one dries out a little at a few surface spots
    public static void chunkTick(ServerLevel level, LevelChunk chunk, float richness)
    {
        if (richness > BARREN) return;
        RandomSource random = level.getRandom();
        int baseX = chunk.getPos().getMinBlockX(), baseZ = chunk.getPos().getMinBlockZ();
        for (int i = 0; i < BARREN_SPOTS; i++) {
            int x = baseX + random.nextInt(16), z = baseZ + random.nextInt(16);
            // The top block here (the heightmap gives the space above it)
            BlockPos top = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, new BlockPos(x, 0, z)).below();
            BlockState state = level.getBlockState(top);
            if (random.nextFloat() >= (BARREN - richness) / BARREN + 0.1F) continue;
            Block dried = null;
            if (state.getFluidState().is(Fluids.WATER) && state.getFluidState().isSource() && state.is(Blocks.WATER)) {
                dried = Blocks.AIR;
                level.playSound(null, top, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.3F, 1.8F);
                level.sendParticles(ParticleTypes.CLOUD, x + 0.5, top.getY() + 1, z + 0.5, 4, 0.3, 0.1, 0.3, 0.01);
            } else if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.FARMLAND)) {
                dried = Blocks.DIRT;
            } else if (state.is(Blocks.DIRT)) {
                dried = Blocks.COARSE_DIRT;
            }
            if (dried != null) level.setBlockAndUpdate(top, dried.defaultBlockState());
        }
    }
}
