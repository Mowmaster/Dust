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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// Telekinesis's block (effects/Telekinesis): the block it takes hold of, wrapped in a jelly cube, like a 26.3 sulfur cube
// that has swallowed a block. Unlike a sulfur cube it isn't a mob, so nothing it hits or falls onto can kill it.
//   held        no gravity: Telekinesis steers it each tick (hold)
//   let go      it falls (gravity), slides to a stop on the ground and hurts what it hits at speed, as the thrower's
//   at rest     on the ground and still for SETTLE_TICKS: it turns back into the block, where it lies
// Like a sulfur cube holding a block, it always sits square: its yaw snaps to the nearest quarter turn every tick (vanilla
// SulfurCube's look control does the same with Mth.wrapDegrees90 while it has a block). Held, it turns with the caster
// in quarter turns; when it's placed back the block is rotated by those quarter turns (BlockState.rotate), into the
// block space its middle is in.
// Punch it and it's knocked the way you face, as a sulfur cube is.
public class TelekinesisCubeEntity extends Entity implements CarriesBlock
{
    private static final EntityDataAccessor<BlockState> BLOCK = SynchedEntityData.defineId(TelekinesisCubeEntity.class, EntityDataSerializers.BLOCK_STATE);
    private static final double GRAVITY = 0.1;
    private static final double PUNCH = 0.6;
    // Faster than this (blocks a tick), what it runs into is hurt
    private static final double HURT_SPEED = 0.4;
    // Still on the ground this long (ticks): it becomes a block again
    private static final int SETTLE_TICKS = 10;
    // On the ground or in water this long without settling (rolling about, bobbing in water): it's placed or dropped where it is (time in the air doesn't count)
    // Let go this long without settling (stuck in water, rolling about): it's placed or dropped wherever it is
    private static final int MAX_FREE_TICKS = 100;

    // heldAt when nothing holds it. Not Long.MIN_VALUE: "now - MIN_VALUE" overflows to a negative number, which would
    // read as just held
    private static final long NOT_HELD = -1;

    private @Nullable Entity thrower;
    // When Telekinesis last held it (game time); held this tick or last: no gravity
    private long heldAt = NOT_HELD;
    private int still;
    private int free;
    // The caster's yaw when it was taken, and its own then: held, it turns as far as they've turned since
    private float casterYawAtGrab;
    private float yawAtGrab;

    public TelekinesisCubeEntity(EntityType<? extends TelekinesisCubeEntity> type, Level level)
    {
        super(type, level);
    }

    // Takes the block at pos into a cube (not air, a fluid, one with contents or an unbreakable one): null if it can't
    public static @Nullable TelekinesisCubeEntity lift(ServerLevel level, BlockPos pos, @Nullable Entity by)
    {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || !state.getFluidState().isEmpty() || level.getBlockEntity(pos) != null || state.getDestroySpeed(level, pos) < 0) return null;
        TelekinesisCubeEntity cube = DustEntityRegistry.TELEKINESIS_CUBE.get().create(level, EntitySpawnReason.TRIGGERED);
        if (cube == null) return null;
        cube.entityData.set(BLOCK, state);
        cube.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        cube.setYRot(0);
        cube.thrower = by;
        cube.heldAt = level.getGameTime();
        if (by != null) cube.casterYawAtGrab = by.getYRot();
        level.removeBlock(pos, false);
        level.playSound(null, pos, state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 0.8F, 1.2F);
        level.addFreshEntity(cube);
        return cube;
    }

    // ---- Telekinesis ----

    // Held this tick, moving at motion; it turns with the holder in quarter turns
    public void hold(Entity by, Vec3 motion)
    {
        if (thrower != by) {
            thrower = by;
            casterYawAtGrab = by.getYRot();
            yawAtGrab = getYRot();
        }
        heldAt = level().getGameTime();
        setDeltaMovement(motion);
        setYRot(square(yawAtGrab + by.getYRot() - casterYawAtGrab));
        still = 0;
        free = 0;
    }

    // Sent flying (a push or a throw), or just let go (Vec3.ZERO): from now on it falls
    public void launch(@Nullable Entity by, Vec3 motion)
    {
        thrower = by;
        heldAt = NOT_HELD;
        setDeltaMovement(motion);
        still = 0;
    }

    private boolean held()
    {
        return heldAt != NOT_HELD && level().getGameTime() - heldAt <= 1;
    }

    // To the nearest quarter turn (0, 90, 180, 270), as a sulfur cube with a block squares itself
    private static float square(float yaw)
    {
        return Math.round(yaw / 90F) * 90F;
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

    // ---- each tick ----

    @Override
    public void tick()
    {
        super.tick();
        setYRot(square(getYRot()));
        // 26.3: only the side simulating an entity may move it (the server here); clients show where it's sent
        if (!(level() instanceof ServerLevel level)) return;
        boolean held = held();
        Vec3 motion = getDeltaMovement();
        if (!held) motion = motion.add(0, -GRAVITY, 0);
        move(MoverType.SELF, motion);
        double speed = motion.length();
        if (speed > HURT_SPEED) hurtWhatItHits(level, speed);
        if (held) {
            // Telekinesis sets its motion again next tick; between, it slows (so a missed tick doesn't fling it)
            setDeltaMovement(getDeltaMovement().scale(0.6));
            return;
        }
        // In the air it keeps most of its speed; on the ground it slides to a stop on what it's on (ice: far)
        double drag = isInWater() ? 0.8 : 0.98;
        Vec3 after = getDeltaMovement().scale(drag);
        if (onGround()) {
            BlockPos below = getBlockPosBelowThatAffectsMyMovement();
            // NeoForge's getFriction(level, pos, entity): a block may change its slipperiness by where it is / who's on it
            float slip = level.getBlockState(below).getFriction(level, below, this) * 0.91F;
            after = new Vec3(after.x * slip, after.y < 0 ? 0 : after.y, after.z * slip);
        }
        setDeltaMovement(after);
        if (onGround() || isInWater()) free++;
        still = onGround() && after.horizontalDistanceSqr() < 1.0E-4 ? still + 1 : 0;
        if (still >= SETTLE_TICKS || free >= MAX_FREE_TICKS) settle(level);
    }

    // Whatever it runs into at speed is hurt (kinetic damage, as the thrower's), and it slows
    private void hurtWhatItHits(ServerLevel level, double speed)
    {
        for (LivingEntity hit : level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(0.1), living -> living != thrower && living.isAlive())) {
            hit.hurtServer(level, DustDamageTypes.create(level, DustDamageTypes.KINETIC_KEY, thrower), (float) (speed * 8));
            setDeltaMovement(getDeltaMovement().scale(0.3));
        }
    }

    // Back to a block: the block space its middle is in, turned by its quarter turns; if that's taken, the one above;
    // if that's taken too, it drops as an item
    private void settle(ServerLevel level)
    {
        BlockState state = carriedBlock().rotate(rotation());
        BlockPos at = BlockPos.containing(getX(), getY() + getBbHeight() / 2, getZ());
        discard();
        for (BlockPos pos : new BlockPos[] {at, at.above()}) {
            if (level.getBlockState(pos).canBeReplaced()) {
                level.setBlockAndUpdate(pos, state);
                level.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 0.9F);
                return;
            }
        }
        spawnAtLocation(level, new ItemStack(state.getBlock()));
    }

    // Its yaw as a block rotation. Minecraft's yaw turns clockwise seen from above, as Rotation.CLOCKWISE_90 does.
    private Rotation rotation()
    {
        int quarters = Math.floorMod(Math.round(getYRot() / 90F), 4);
        return switch (quarters) {
            case 1 -> Rotation.CLOCKWISE_90;
            case 2 -> Rotation.CLOCKWISE_180;
            case 3 -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };
    }

    // Punched: knocked the way the puncher faces (a little up), and isn't harmed
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount)
    {
        Entity puncher = source.getEntity();
        if (puncher == null || held()) return false;
        launch(puncher, getDeltaMovement().add(puncher.getLookAngle().scale(PUNCH)).add(0, 0.2, 0));
        level.playSound(null, blockPosition(), carriedBlock().getSoundType().getHitSound(), SoundSource.BLOCKS, 1.0F, 0.8F);
        return false;
    }

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
        output.putInt("free", free);
    }

    // Loaded again (it was saved mid-flight): it just falls from where it was
    @Override
    protected void readAdditionalSaveData(ValueInput input)
    {
        entityData.set(BLOCK, input.read("block", BlockState.CODEC).orElse(Blocks.STONE.defaultBlockState()));
        free = input.getIntOr("free", 0);
    }
}
