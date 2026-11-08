package com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs;

import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.MoveSimulationType;
import org.jspecify.annotations.Nullable;

// A floating orb of one element's dust that flies to nearby players and is absorbed into their element store,
// like an experience orb. Each element has a small subclass that only says which element it is.
public abstract class ElementalOrbEntityBase extends Entity {

    private @Nullable Player followingPlayer;
    private static final EntityDataAccessor<Integer> VALUE =
            SynchedEntityData.defineId(ElementalOrbEntityBase.class, EntityDataSerializers.INT);

    // Unabsorbed dust dissipates back into the aura after this long
    private static final int BASE_DURATION = 600; // 30 seconds
    private int age;

    public ElementalOrbEntityBase(EntityType<? extends ElementalOrbEntityBase> type, Level level) {
        super(type, level);
    }

    // Which element store this orb fills
    public abstract EnumElement getElement();

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(VALUE, 1);
    }

    public int getValue() {
        return this.entityData.get(VALUE);
    }

    public void setValue(int value) {
        this.entityData.set(VALUE, Math.max(1, value));
    }

    @Override
    public void playerTouch(Player player) {
        // Only the server changes the player's dust. The client used to discard its copy of the orb here too, which
        // made it vanish on screen even when the server didn't absorb it.
        if (this.isRemoved() || !(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        if (!DustMagicAttachmentHelper.hasMana(serverPlayer)) {
            return;
        }

        // Prevent unreasonable values
        int orbValue = Math.clamp(this.getValue(), 1, 100);
        EnumElement element = this.getElement();
        if (element.addElement(serverPlayer, orbValue, true) > 0) {
            element.addElement(serverPlayer, orbValue, false);
            // Whatever didn't fit is lost (it dissipates into the aura)
            this.discard();
        }
    }

    private void setUnderwaterMovement() {
        Vec3 movement = this.getDeltaMovement();
        this.setDeltaMovement(movement.x * (double)0.99F, Math.min(movement.y + (double)5.0E-4F, (double)0.06F), movement.z * (double)0.99F);
    }

    @Override
    public final boolean hurtClient(DamageSource source) {
        return !this.isInvulnerableToBase(source);
    }

    @Override
    public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (this.isInvulnerableToBase(source)) {
            return false;
        } else {
            this.markHurt();
            this.discard();
            return true;
        }
    }

    // ValueOutput / ValueInput (26.x) replace reading and writing CompoundTags directly.
    // Without this the orb's value went back to 1 whenever the chunk reloaded.
    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt("Value", this.getValue());
        output.putInt("Age", this.age);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.setValue(input.getIntOr("Value", 1));
        this.age = input.getIntOr("Age", 0);
    }

    // 26.3: entities say which side runs their movement. Like vanilla's experience orb, an orb moves itself on both the
    // server and the client (the client predicts it between updates). The default only lets the authoritative side
    // move it, and calling move() on the client then fails (checked in the development environment).
    @Override
    public MoveSimulationType getMoveSimulationType() {
        return MoveSimulationType.SERVER_AND_CLIENT;
    }


    @Override
    protected double getDefaultGravity() {
        return 0.03;
    }

    // Movement copied from vanilla's ExperienceOrb: float, sink slowly in water, bounce out of lava, follow players.
    @Override
    public void tick() {
        // super.tick() used to be called twice per tick (once here and once below), doubling the entity's base tick
        if (this.firstTick && this.level().isClientSide()) {
            this.firstTick = false;
        } else {
            super.tick();
            boolean colliding = !this.level().noCollision(this.getBoundingBox());
            if (this.isEyeInFluid(FluidTags.WATER)) {
                this.setUnderwaterMovement();
            } else if (!colliding) {
                this.applyGravity();
            }

            if (this.level().getFluidState(this.blockPosition()).is(FluidTags.LAVA)) {
                this.setDeltaMovement((double)((this.random.nextFloat() - this.random.nextFloat()) * 0.2F), (double)0.2F, (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.2F));
            }

            this.followNearbyPlayer();
            if (this.followingPlayer == null && !this.level().isClientSide() && colliding) {
                boolean nextColliding = !this.level().noCollision(this.getBoundingBox().move(this.getDeltaMovement()));
                if (nextColliding) {
                    this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / (double)2.0F, this.getZ());
                    this.needsSync = true;
                }
            }

            double fallSpeed = this.getDeltaMovement().y;
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.applyEffectsFromBlocks();
            float friction = 0.98F;
            if (this.onGround()) {
                BlockPos pos = this.getBlockPosBelowThatAffectsMyMovement();
                friction = this.level().getBlockState(pos).getFriction(this.level(), pos, this) * 0.98F;
            }

            this.setDeltaMovement(this.getDeltaMovement().scale((double)friction));
            if (this.verticalCollisionBelow && fallSpeed < -this.getGravity()) {
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, -fallSpeed * 0.4, this.getDeltaMovement().z));
            }

            this.age++;
            if (!this.level().isClientSide() && this.age >= BASE_DURATION) {
                this.discard();
            }
        }
    }

    private void followNearbyPlayer() {
        if (this.followingPlayer == null || this.followingPlayer.isSpectator() || this.followingPlayer.distanceToSqr(this) > (double)64.0F) {
            Player nearestPlayer = this.level().getNearestPlayer(this,8.0F);
            if (nearestPlayer != null && !nearestPlayer.isSpectator() && !nearestPlayer.isDeadOrDying()) {
                this.followingPlayer = nearestPlayer;
            } else {
                this.followingPlayer = null;
            }
        }

        if (this.followingPlayer != null) {
            Vec3 delta = new Vec3(this.followingPlayer.getX() - this.getX(), this.followingPlayer.getY() + (double)this.followingPlayer.getEyeHeight() / (double)2.0F - this.getY(), this.followingPlayer.getZ() - this.getZ());
            double length = delta.lengthSqr();
            double power = (double)1.0F - Math.sqrt(length) / (double)8.0F;
            this.setDeltaMovement(this.getDeltaMovement().add(delta.normalize().scale(power * power * 0.1)));
        }
    }

    public int getIcon() {
        return 0;
    }
}
