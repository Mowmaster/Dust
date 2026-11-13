package com.mowmaster.dust.Features.Spells.entities;

import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import com.mowmaster.dust.Features.EffectScrolls.DustEffects.DustDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// Flight's block effect: the block lifts out and floats. Punch it and it drifts away from you (it doesn't break); it
// stops at walls (it collides with blocks) and moves through water; whatever it runs into at speed takes kinetic
// damage. Players and mobs pass through it. When its time is up it falls, landing as a block, and hurts what it lands
// on like an anvil.
public class FloatingBlockEntity extends Entity implements CarriesBlock
{
    private static final EntityDataAccessor<BlockState> BLOCK = SynchedEntityData.defineId(FloatingBlockEntity.class, EntityDataSerializers.BLOCK_STATE);
    private static final double PUNCH = 0.8;

    private int lifetime = 20 * 20;
    private @Nullable Entity lastPuncher;

    public FloatingBlockEntity(EntityType<? extends FloatingBlockEntity> type, Level level)
    {
        super(type, level);
    }

    // Lifts the block at pos out into a floating block (not air, not one with contents, not an unbreakable one)
    public static boolean lift(ServerLevel level, BlockPos pos, int lifetime)
    {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || !state.getFluidState().isEmpty() || level.getBlockEntity(pos) != null || state.getDestroySpeed(level, pos) < 0) return false;
        FloatingBlockEntity floating = DustEntityRegistry.FLOATING_BLOCK.get().create(level, EntitySpawnReason.TRIGGERED);
        if (floating == null) return false;
        floating.entityData.set(BLOCK, state);
        floating.lifetime = lifetime;
        floating.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        level.removeBlock(pos, false);
        level.addFreshEntity(floating);
        return true;
    }

    @Override
    public BlockState carriedBlock()
    {
        return entityData.get(BLOCK);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder)
    {
        builder.define(BLOCK, Blocks.STONE.defaultBlockState());
    }

    @Override
    public void tick()
    {
        super.tick();
        // 26.3: only the side simulating an entity may move it (Entity.move throws otherwise). For this that's the
        // server; clients just show where the server says it is.
        if (!(level() instanceof ServerLevel level)) return;
        Vec3 motion = getDeltaMovement();
        move(MoverType.SELF, motion);
        double drag = isInWater() ? 0.8 : 0.9;
        setDeltaMovement(motion.scale(drag));
        double speed = motion.length();
        if (speed > 0.25) {
            for (LivingEntity hit : level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(0.1), living -> living != lastPuncher && living.isAlive())) {
                hit.hurtServer(level, DustDamageTypes.create(level, DustDamageTypes.KINETIC_KEY, lastPuncher), (float) (speed * 8));
                setDeltaMovement(getDeltaMovement().scale(0.3));
            }
        }
        if (tickCount >= lifetime) drop(level);
    }

    // Time's up: it falls as a falling block, hurting what it lands on like an anvil
    private void drop(ServerLevel level)
    {
        BlockPos at = blockPosition();
        BlockState state = carriedBlock();
        discard();
        if (level.getBlockState(at).canBeReplaced()) {
            FallingBlockEntity falling = FallingBlockEntity.fall(level, at, state);
            falling.setHurtsEntities(2.0F, 40);
        } else {
            // No room where it is: it drops as an item instead
            spawnAtLocation(level, new net.minecraft.world.item.ItemStack(state.getBlock()));
        }
    }

    // Punched: it drifts off the way the puncher was facing, and isn't harmed
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount)
    {
        Entity puncher = source.getEntity();
        if (puncher == null) return false;
        lastPuncher = puncher;
        Vec3 push = puncher.getLookAngle().scale(PUNCH);
        setDeltaMovement(getDeltaMovement().add(push));
        level.playSound(null, blockPosition(), carriedBlock().getSoundType().getHitSound(), SoundSource.BLOCKS, 1.0F, 0.8F);
        return false;
    }

    // It can be hit, but nothing bumps into it or stands on it
    @Override
    public boolean isPickable()
    {
        return !isRemoved();
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output)
    {
        output.store("block", BlockState.CODEC, carriedBlock());
        output.putInt("lifetime", lifetime);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input)
    {
        entityData.set(BLOCK, input.read("block", BlockState.CODEC).orElse(Blocks.STONE.defaultBlockState()));
        lifetime = input.getIntOr("lifetime", 20 * 20);
    }
}
