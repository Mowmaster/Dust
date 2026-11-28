package com.mowmaster.dust.Features.Spells.entities;

import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// A block the Flight + Chaos fusion turned the gravity of (effects/GravityInversion.liftBlock): it falls upward, faster
// and faster (lift a tick, more with potency, up to the speed drag allows), until something stops it: a ceiling, or the
// top of the world. Then it's placed where it stopped (the space it's in, else the one below; if neither is free it
// drops as an item). A block entity goes with it: its saved data (a chest's contents...) is put back into the placed
// block. Drawn as the block (CarriedBlockRenderer); a block entity's own renderer (a chest's lid) isn't drawn on the way.
public class RisingBlockEntity extends Entity implements CarriesBlock
{
    private static final EntityDataAccessor<BlockState> BLOCK = SynchedEntityData.defineId(RisingBlockEntity.class, EntityDataSerializers.BLOCK_STATE);
    private static final double DRAG = 0.98;
    // Up this long (ticks) without stopping (somehow): placed where it is
    private static final int MAX_TICKS = 1200;

    private double lift = 0.04;
    private @Nullable CompoundTag blockData;
    private int age;

    public RisingBlockEntity(EntityType<? extends RisingBlockEntity> type, Level level)
    {
        super(type, level);
    }

    // Lifts the block at pos (not air, a fluid or an unbreakable block), with its block entity's data: null if it can't
    public static @Nullable RisingBlockEntity lift(ServerLevel level, BlockPos pos, double lift)
    {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || !state.getFluidState().isEmpty() || state.getDestroySpeed(level, pos) < 0) return null;
        if (pos.getY() >= level.getMaxY()) return null;
        RisingBlockEntity rising = DustEntityRegistry.RISING_BLOCK.get().create(level, EntitySpawnReason.TRIGGERED);
        if (rising == null) return null;
        rising.entityData.set(BLOCK, state);
        rising.lift = lift;
        rising.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null) {
            rising.blockData = blockEntity.saveCustomOnly(level.registryAccess());
            // Taken away first, so it doesn't spill its contents as the block goes (they ride along in blockData)
            level.removeBlockEntity(pos);
        }
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
        level.addFreshEntity(rising);
        return rising;
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
        // Only the server moves it; clients are shown where it goes
        if (!(level() instanceof ServerLevel level)) return;
        age++;
        Vec3 motion = getDeltaMovement().add(0, lift, 0);
        move(MoverType.SELF, motion);
        setDeltaMovement(getDeltaMovement().scale(DRAG));
        boolean stopped = (verticalCollision && !verticalCollisionBelow) || getY() + getBbHeight() >= level.getMaxY() || age >= MAX_TICKS;
        if (stopped) place(level);
    }

    // Placed where it stopped: the space its middle is in, or the one below; else it drops
    private void place(ServerLevel level)
    {
        BlockState state = carriedBlock();
        BlockPos at = BlockPos.containing(getX(), Math.min(getY() + getBbHeight() / 2, level.getMaxY()), getZ());
        discard();
        for (BlockPos pos : new BlockPos[] {at, at.below()}) {
            if (!level.getBlockState(pos).canBeReplaced()) continue;
            level.setBlockAndUpdate(pos, state);
            if (blockData != null) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity != null) {
                    blockEntity.loadCustomOnly(TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), blockData));
                    blockEntity.setChanged();
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                }
            }
            level.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 0.8F);
            return;
        }
        spawnAtLocation(level, new ItemStack(state.getBlock()));
    }

    @Override
    public boolean hurtServer(ServerLevel level, net.minecraft.world.damagesource.DamageSource source, float amount)
    {
        return false;
    }

    @Override
    public boolean isPickable()
    {
        return false;
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
        output.putDouble("lift", lift);
        output.putInt("age", age);
        if (blockData != null) output.store("block_data", CompoundTag.CODEC, blockData);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input)
    {
        entityData.set(BLOCK, input.read("block", BlockState.CODEC).orElse(Blocks.STONE.defaultBlockState()));
        lift = input.getDoubleOr("lift", 0.04);
        age = input.getIntOr("age", 0);
        blockData = input.read("block_data", CompoundTag.CODEC).orElse(null);
    }
}
