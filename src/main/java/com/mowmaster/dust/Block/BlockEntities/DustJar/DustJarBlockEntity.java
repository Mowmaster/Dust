package com.mowmaster.dust.Block.BlockEntities.DustJar;

import com.mowmaster.dust.Capabilities.Dust.DustMagic;
import com.mowmaster.dust.Capabilities.Dust.IDustHandler;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.mowmaster.dust.References.Constants.MODID;

public class DustJarBlockEntity extends BlockEntity {

    private LazyOptional<IDustHandler> dustHandler = LazyOptional.of(this::createhandlerDust);
    private DustMagic dustMagic = new DustMagic(-1, 0);
    private int dustCapacity = 1000;
    public BlockPos getPos() { return this.worldPosition; }

    public DustJarBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(DeferredBlockEntityTypes.DUST_JAR.get(), p_155229_, p_155230_);
    }

    public void update()
    {
        BlockState state = level.getBlockState(getPos());
        this.level.sendBlockUpdated(getPos(), state, state, 3);
        this.setChanged();
    }

    public IDustHandler createhandlerDust() {
        return new IDustHandler() {

            protected void onContentsChanged()
            {
                update();
            }

            @Override
            public int getTanks() {
                return 1;
            }

            @NotNull
            @Override
            public DustMagic getDustMagicInTank(int tank) {
                return dustMagic;
            }

            @Override
            public int getTankCapacity(int tank) {
                return dustCapacity;
            }

            @Override
            public boolean isDustValid(int tank, @NotNull DustMagic dustIn) {
                return dustMagic.isDustEqualOrEmpty(dustIn);
            }

            @Override
            public int fill(DustMagic dust, DustAction action) {
                if (dust.isEmpty() || !isDustValid(0,dust))
                {
                    return 0;
                }
                if (action.simulate())
                {
                    if (dustMagic.isEmpty())
                    {
                        return Math.min(getTankCapacity(0), dust.getDustAmount());
                    }
                    if (!dustMagic.isDustEqual(dust))
                    {
                        return 0;
                    }
                    return Math.min(getTankCapacity(0) - dustMagic.getDustAmount(), dust.getDustAmount());
                }
                if (dustMagic.isEmpty())
                {
                    dustMagic = new DustMagic(dust.getDustColor(), Math.min(getTankCapacity(0), dust.getDustAmount()));
                    onContentsChanged();
                    return dustMagic.getDustAmount();
                }
                if (!dustMagic.isDustEqual(dust))
                {
                    return 0;
                }
                int filled = getTankCapacity(0) - dustMagic.getDustAmount();

                if (dust.getDustAmount() < filled)
                {
                    dustMagic.grow(dust.getDustAmount());
                    filled = dust.getDustAmount();
                }
                else
                {
                    dustMagic.setDustAmount(getTankCapacity(0));
                }
                if (filled > 0)
                    onContentsChanged();
                return filled;
            }

            @NotNull
            @Override
            public DustMagic drain(DustMagic dust, DustAction action) {
                if (dust.isEmpty() || !dust.isDustEqual(dustMagic))
                {
                    return new DustMagic(-1, 0);
                }
                return drain(dust.getDustAmount(), action);
            }

            @NotNull
            @Override
            public DustMagic drain(int maxDrain, DustAction action) {
                int drained = maxDrain;
                if (dustMagic.getDustAmount() < drained)
                {
                    drained = dustMagic.getDustAmount();
                }
                DustMagic magic = new DustMagic(dustMagic.getDustColor(), drained);
                if (action.execute() && drained > 0)
                {
                    dustMagic.shrink(drained);
                    onContentsChanged();
                }
                return magic;
            }
        };
    }


    /*============================================================================
    ==============================================================================
    ===========================    DUST   START      =============================
    ==============================================================================
    ============================================================================*/

    public boolean hasDust()
    {
        IDustHandler h = dustHandler.orElse(null);
        if(!h.getDustMagicInTank(0).isEmpty())return true;
        return false;
    }

    public DustMagic getStoredDust()
    {
        IDustHandler h = dustHandler.orElse(null);
        if(!h.getDustMagicInTank(0).isEmpty())return h.getDustMagicInTank(0);
        return new DustMagic(-1, 0);
    }

    private void setDustCapacity(int capacity)
    {
        this.dustCapacity = capacity;
    }

    public int getDustCapacity()
    {
        IDustHandler h = dustHandler.orElse(null);
        return h.getTankCapacity(0);
    }

    public int spaceForDust()
    {
        return getDustCapacity()-getStoredDust().getDustAmount();
    }

    public boolean canAcceptDust(DustMagic dustMagicIn)
    {
        IDustHandler h = dustHandler.orElse(null);
        return h.isDustValid(0,dustMagicIn);
    }

    public DustMagic removeDust(DustMagic dustMagicToRemove, IDustHandler.DustAction action)
    {
        IDustHandler h = dustHandler.orElse(null);
        update();
        return h.drain(dustMagicToRemove,action);
    }

    public DustMagic removeDust(int dustAmountToRemove, IDustHandler.DustAction action)
    {
        IDustHandler h = dustHandler.orElse(null);
        update();
        return h.drain(new DustMagic(getStoredDust().getDustColor(),dustAmountToRemove),action);
    }

    public int addDust(DustMagic dustMagicIn, IDustHandler.DustAction action)
    {
        IDustHandler h = dustHandler.orElse(null);
        update();
        return h.fill(dustMagicIn,action);
    }

    /*============================================================================
    ==============================================================================
    ===========================     DUST   END       =============================
    ==============================================================================
    ============================================================================*/


    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        this.dustCapacity = p_155245_.getInt(MODID + "_dustCapacity");
        this.dustMagic = DustMagic.getDustMagicInTag(p_155245_);
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        save(p_187471_);
    }

    @Override
    public CompoundTag save(CompoundTag p_58888_) {
        super.save(p_58888_);
        p_58888_.putInt(MODID + "_dustCapacity", this.getDustCapacity());
        return DustMagic.setDustMagicInTag(p_58888_,this.dustMagic);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return save(new CompoundTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net,pkt);
        BlockState state = this.level.getBlockState(getPos());
        this.handleUpdateTag(pkt.getTag());
        this.level.sendBlockUpdated(getPos(), state, state, 3);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if(this.dustHandler != null) {
            this.dustHandler.invalidate();
        }
    }


}
