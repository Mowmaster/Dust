package com.mowmaster.dust.Features.Sanctums;

import com.mowmaster.dust.DustConfig;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Bosses.LootBags;
import com.mowmaster.dust.Features.Crystals.ElementCrystals;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Magic.PlayerMagicHelper;
import com.mowmaster.mowlib.display.ISpinningItemHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

// What an Arcane Spawner (ArcaneSpawnerBlock) does, in its PHASE:
//   WAITING   every half second, looks for a player (not creative or spectating) within DETECT blocks it can see; then
//             it starts a wave for everyone near (start)
//   ACTIVE    sends its mobs one at a time, SPAWN_EVERY ticks apart, never more than maxAlive at once, until it has sent
//             them all; when they're all gone, every player who was near during the fight gets a reward (complete). If
//             nobody comes near for GIVE_UP ticks, it gives up and waits again (its mobs stay).
//   COOLDOWN  rests for the configured time (half an hour by default, as a trial spawner), then waits again
// The wave: 4 mobs, 2 more for each other player, and one more for every two tiers (at most 14); up to 2 at once, more
// at higher tiers and for groups. The tier: the strongest nearby player's Magic Level / 10 (SanctumMobs).
// A reward, for each player who fought: dust and crystals of its element, experience, and a chance of a loot bag of its
// element's Archon (better at higher tiers).
// Its heart, drawn by MowLib's SpinningItemRenderer (ISpinningItemHolder): its element's crystal floating in the cage,
// turning slowly while it waits and fast while it fights; gone while it rests. All of it from its block state, which
// the client has.
public class ArcaneSpawnerBlockEntity extends BlockEntity implements ISpinningItemHolder
{
    public static final double DETECT = 12.0;
    private static final double TRACK = 24.0;
    private static final int SPAWN_EVERY = 40;
    private static final int GIVE_UP = 20 * 30;
    private static final int SPAWN_RADIUS = 4;

    private int tier;
    private int toSpawn;
    private int maxAlive = 2;
    private long nextSpawn;
    private long cooldownUntil;
    private int idle;
    private final Set<UUID> alive = new HashSet<>();
    private final Set<UUID> fighters = new HashSet<>();

    public ArcaneSpawnerBlockEntity(BlockPos pos, BlockState state)
    {
        super(Sanctums.ARCANE_SPAWNER_ENTITY.get(), pos, state);
    }

    private static final java.util.Map<ElementalArmorSet, ItemStack> CRYSTALS = new java.util.EnumMap<>(ElementalArmorSet.class);

    @Override
    public ItemStack getDisplayedItem()
    {
        if (phase() == ArcaneSpawnerBlock.Phase.COOLDOWN) return ItemStack.EMPTY;
        return CRYSTALS.computeIfAbsent(set(), key -> new ItemStack(DustItemRegistry.CRYSTALS.get(key.element()).get()));
    }

    @Override
    public double getDisplayHeight()
    {
        return 0.4;
    }

    @Override
    public float getDisplayScale()
    {
        return 0.9F;
    }

    @Override
    public float getSpinSpeed()
    {
        return phase() == ArcaneSpawnerBlock.Phase.ACTIVE ? 9.0F : 2.0F;
    }

    public ArcaneSpawnerBlock.Phase phase()
    {
        return getBlockState().getValue(ArcaneSpawnerBlock.PHASE);
    }

    private ElementalArmorSet set()
    {
        return getBlockState().getValue(ArcaneSpawnerBlock.SET);
    }

    // Back to waiting at once (a finished labyrinth run asks its sanctum's spawners to be fought again: SanctumKeystone)
    public void reset(ServerLevel level)
    {
        alive.clear();
        fighters.clear();
        toSpawn = 0;
        idle = 0;
        setPhase(level, ArcaneSpawnerBlock.Phase.WAITING);
    }

    void serverTick(ServerLevel level, BlockState state)
    {
        long now = level.getGameTime();
        switch (state.getValue(ArcaneSpawnerBlock.PHASE)) {
            case WAITING -> {
                if (now % 10 != 0) return;
                List<ServerPlayer> near = playersNear(level, DETECT, true);
                if (!near.isEmpty()) start(level, near);
            }
            case ACTIVE -> {
                alive.removeIf(id -> {
                    Entity entity = level.getEntity(id);
                    return !(entity instanceof LivingEntity living) || !living.isAlive();
                });
                List<ServerPlayer> near = playersNear(level, TRACK, false);
                for (ServerPlayer player : near) fighters.add(player.getUUID());
                idle = near.isEmpty() ? idle + 1 : 0;
                if (idle > GIVE_UP) {
                    reset(level);
                    return;
                }
                if (toSpawn > 0 && alive.size() < maxAlive && now >= nextSpawn) {
                    if (spawnOne(level, near)) toSpawn--;
                    nextSpawn = now + SPAWN_EVERY;
                }
                if (toSpawn <= 0 && alive.isEmpty()) complete(level);
            }
            case COOLDOWN -> {
                if (now >= cooldownUntil) setPhase(level, ArcaneSpawnerBlock.Phase.WAITING);
            }
        }
    }

    // Players (not creative or spectating) within range; seen: only those it has a clear line to
    private List<ServerPlayer> playersNear(ServerLevel level, double range, boolean seen)
    {
        Vec3 centre = Vec3.atCenterOf(worldPosition);
        List<ServerPlayer> near = new ArrayList<>();
        for (ServerPlayer player : level.players()) {
            if (player.isSpectator() || player.isCreative() || !player.isAlive()) continue;
            if (player.distanceToSqr(centre) > range * range) continue;
            if (seen && level.clip(new ClipContext(centre, player.getEyePosition(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player))
                    .getType() == HitResult.Type.BLOCK) continue;
            near.add(player);
        }
        return near;
    }

    private void start(ServerLevel level, List<ServerPlayer> players)
    {
        int best = 0;
        for (ServerPlayer player : players) best = Math.max(best, PlayerMagicHelper.magicLevel(player));
        tier = SanctumMobs.tier(best);
        int extra = players.size() - 1;
        toSpawn = Math.min(14, 4 + 2 * extra + tier / 2);
        maxAlive = Math.min(5, 2 + tier / 3 + extra / 2);
        fighters.clear();
        for (ServerPlayer player : players) fighters.add(player.getUUID());
        alive.clear();
        idle = 0;
        nextSpawn = level.getGameTime() + 20;
        setPhase(level, ArcaneSpawnerBlock.Phase.ACTIVE);
        level.playSound(null, worldPosition, SoundEvents.TRIAL_SPAWNER_DETECT_PLAYER, SoundSource.BLOCKS, 1.0F, 0.8F);
    }

    // One mob, somewhere clear near the spawner, set on the nearest fighter
    private boolean spawnOne(ServerLevel level, List<ServerPlayer> near)
    {
        RandomSource random = level.getRandom();
        EntityType<?> type = SanctumMobs.pick(set(), tier, random);
        if (type == null) return true;
        for (int attempt = 0; attempt < 12; attempt++) {
            double x = worldPosition.getX() + 0.5 + (random.nextDouble() * 2 - 1) * SPAWN_RADIUS;
            double z = worldPosition.getZ() + 0.5 + (random.nextDouble() * 2 - 1) * SPAWN_RADIUS;
            BlockPos at = BlockPos.containing(x, worldPosition.getY(), z);
            // Stand it on the floor near the spawner's level
            for (int dy = 1; dy >= -1; dy--) {
                BlockPos feet = at.above(dy);
                if (!level.getBlockState(feet.below()).isFaceSturdy(level, feet.below(), net.minecraft.core.Direction.UP)) continue;
                AABB box = type.getSpawnAABB(x, feet.getY(), z);
                if (!level.noCollision(box)) continue;
                Entity entity = type.create(level, EntitySpawnReason.SPAWNER);
                if (!(entity instanceof Mob mob)) return true;
                mob.snapTo(x, feet.getY(), z, random.nextFloat() * 360.0F, 0.0F);
                mob.finalizeSpawn(level, level.getCurrentDifficultyAt(feet), EntitySpawnReason.SPAWNER, null);
                mob.setPersistenceRequired();
                SanctumMobs.buff(mob, set(), tier);
                com.mowmaster.dust.Features.Labyrinth.Labyrinth.claim(level, mob);
                level.addFreshEntityWithPassengers(mob);
                ServerPlayer target = nearest(mob, near);
                if (target != null) mob.setTarget(target);
                alive.add(mob.getUUID());
                DustParticleOptions dust = new DustParticleOptions(set().element().color(), 1.4F);
                level.sendParticles(dust, x, feet.getY() + 0.8, z, 20, 0.35, 0.6, 0.35, 0);
                level.sendParticles(ParticleTypes.SMOKE, x, feet.getY() + 0.5, z, 8, 0.3, 0.4, 0.3, 0.02);
                level.playSound(null, feet, SoundEvents.TRIAL_SPAWNER_SPAWN_MOB, SoundSource.HOSTILE, 1.0F, 1.0F);
                return true;
            }
        }
        return false;
    }

    private static ServerPlayer nearest(Mob mob, List<ServerPlayer> players)
    {
        ServerPlayer best = null;
        for (ServerPlayer player : players) if (best == null || mob.distanceToSqr(player) < mob.distanceToSqr(best)) best = player;
        return best;
    }

    // The wave is beaten: a reward for each fighter, then a rest
    private void complete(ServerLevel level)
    {
        RandomSource random = level.getRandom();
        int rewards = Math.max(1, fighters.size());
        DustElement element = set().element();
        Vec3 top = Vec3.atBottomCenterOf(worldPosition).add(0, 1.1, 0);
        for (int i = 0; i < rewards; i++) {
            eject(level, top, new ItemStack(DustItemRegistry.DUSTS.get(element).get(), 6 + random.nextInt(11)));
            eject(level, top, new ItemStack(DustItemRegistry.CRYSTALS.get(element).get(), 1 + random.nextInt(3)));
            if (random.nextInt(4) == 0) eject(level, top, new ItemStack(ElementCrystals.SMALL_ITEMS.get(element).get()));
            if (random.nextFloat() < Math.min(0.3F, 0.08F + 0.02F * tier)) eject(level, top, LootBags.bag(set(), 1 + tier));
            ExperienceOrb.award(level, top, 5 + 3 * tier);
        }
        level.playSound(null, worldPosition, SoundEvents.TRIAL_SPAWNER_EJECT_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.sendParticles(new DustParticleOptions(element.color(), 1.6F), top.x, top.y, top.z, 30, 0.4, 0.4, 0.4, 0);
        alive.clear();
        fighters.clear();
        cooldownUntil = level.getGameTime() + DustConfig.SPAWNER_COOLDOWN.get();
        setPhase(level, ArcaneSpawnerBlock.Phase.COOLDOWN);
    }

    private static void eject(ServerLevel level, Vec3 at, ItemStack stack)
    {
        RandomSource random = level.getRandom();
        ItemEntity item = new ItemEntity(level, at.x, at.y, at.z, stack);
        item.setDeltaMovement((random.nextDouble() - 0.5) * 0.2, 0.3 + random.nextDouble() * 0.1, (random.nextDouble() - 0.5) * 0.2);
        item.setDefaultPickUpDelay();
        level.addFreshEntity(item);
    }

    private void setPhase(ServerLevel level, ArcaneSpawnerBlock.Phase phase)
    {
        BlockState state = getBlockState();
        if (state.getValue(ArcaneSpawnerBlock.PHASE) != phase) level.setBlock(worldPosition, state.setValue(ArcaneSpawnerBlock.PHASE, phase), Block.UPDATE_ALL);
        setChanged();
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        output.putInt("tier", tier);
        output.putInt("to_spawn", toSpawn);
        output.putInt("max_alive", maxAlive);
        output.putLong("next_spawn", nextSpawn);
        output.putLong("cooldown_until", cooldownUntil);
        output.store("alive", UUIDUtil.CODEC.listOf(), List.copyOf(alive));
        output.store("fighters", UUIDUtil.CODEC.listOf(), List.copyOf(fighters));
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        tier = input.getIntOr("tier", 0);
        toSpawn = input.getIntOr("to_spawn", 0);
        maxAlive = input.getIntOr("max_alive", 2);
        nextSpawn = input.getLongOr("next_spawn", 0L);
        cooldownUntil = input.getLongOr("cooldown_until", 0L);
        alive.clear();
        input.read("alive", UUIDUtil.CODEC.listOf()).ifPresent(alive::addAll);
        fighters.clear();
        input.read("fighters", UUIDUtil.CODEC.listOf()).ifPresent(fighters::addAll);
    }
}
