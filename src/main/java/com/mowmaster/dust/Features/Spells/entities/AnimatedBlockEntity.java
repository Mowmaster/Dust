package com.mowmaster.dust.Features.Spells.entities;

import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

// Creature's block effect: the block comes alive and scurries about (and runs when hit). Killed, it drops the block;
// when its time is up it settles down where it is, as the block again (or drops it, if there's no room).
// Its health goes with how hard the block is.
public class AnimatedBlockEntity extends PathfinderMob implements CarriesBlock
{
    private static final EntityDataAccessor<BlockState> BLOCK = SynchedEntityData.defineId(AnimatedBlockEntity.class, EntityDataSerializers.BLOCK_STATE);

    private int lifetime = 20 * 30;

    public AnimatedBlockEntity(EntityType<? extends AnimatedBlockEntity> type, Level level)
    {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return PathfinderMob.createMobAttributes().add(Attributes.MAX_HEALTH, 10).add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    // Brings the block at pos to life (not air, not one with contents, not an unbreakable one)
    public static boolean animate(ServerLevel level, BlockPos pos, int lifetime)
    {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || !state.getFluidState().isEmpty() || level.getBlockEntity(pos) != null || state.getDestroySpeed(level, pos) < 0) return false;
        AnimatedBlockEntity animated = DustEntityRegistry.ANIMATED_BLOCK.get().create(level, EntitySpawnReason.TRIGGERED);
        if (animated == null) return false;
        animated.entityData.set(BLOCK, state);
        animated.lifetime = lifetime;
        float health = Math.clamp(state.getDestroySpeed(level, pos) * 4, 4, 40);
        animated.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
        animated.setHealth(health);
        animated.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        level.removeBlock(pos, false);
        level.addFreshEntity(animated);
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
        super.defineSynchedData(builder);
        builder.define(BLOCK, Blocks.STONE.defaultBlockState());
    }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, 1.6));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.2));
        goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick()
    {
        super.tick();
        if (level() instanceof ServerLevel level && tickCount >= lifetime && isAlive()) settle(level);
    }

    // Back to being a block where it stands
    private void settle(ServerLevel level)
    {
        BlockPos at = blockPosition();
        if (level.getBlockState(at).canBeReplaced()) level.setBlockAndUpdate(at, carriedBlock());
        else spawnAtLocation(level, new ItemStack(carriedBlock().getBlock()));
        discard();
    }

    // Killed: it drops the block
    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit)
    {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        ItemStack block = new ItemStack(carriedBlock().getBlock());
        if (!block.isEmpty()) spawnAtLocation(level, block);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return carriedBlock().getSoundType().getHitSound();
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return carriedBlock().getSoundType().getBreakSound();
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output)
    {
        super.addAdditionalSaveData(output);
        output.store("block", BlockState.CODEC, carriedBlock());
        output.putInt("lifetime", lifetime);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input)
    {
        super.readAdditionalSaveData(input);
        entityData.set(BLOCK, input.read("block", BlockState.CODEC).orElse(Blocks.STONE.defaultBlockState()));
        lifetime = input.getIntOr("lifetime", 20 * 30);
    }
}
