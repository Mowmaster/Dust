package com.mowmaster.dust.Block.BlockEntities.CustomDustBlock;

import com.mowmaster.dust.Capabilities.Experience.CapabilityExperience;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.Items.Filters.IPedestalFilter;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.References.Constants.MODID;

public class CustomPowderedBlockEntity extends BlockEntity
{
    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandlerDrops);
    private int blockColor =  0;
    public BlockPos getPos() { return this.worldPosition; }

    public CustomPowderedBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(DeferredBlockEntityTypes.DUST.get(), p_155229_, p_155230_);
    }

    public void setColor(int color)
    {
        this.blockColor = color;
    }

    public int getColor()
    {
        return this.blockColor;
    }

    public void update()
    {
        BlockState state = level.getBlockState(getPos());
        this.level.sendBlockUpdated(getPos(), state, state, 3);
        this.setChanged();
    }

    public IItemHandler createHandlerDrops() {
        return new ItemStackHandler(27) {
            @Override
            protected void onLoad() {
                super.onLoad();
            }

            @Override
            protected void onContentsChanged(int slot) {
                update();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }

            @Override
            public int getSlots() {
                //maybe return less if there is no tank upgrade???
                return 27;
            }

            @Override
            protected int getStackLimit(int slot, @NotNull ItemStack stack) {
                return super.getStackLimit(slot, stack);
            }

            @Override
            public int getSlotLimit(int slot) {
                return super.getSlotLimit(slot);
            }

            @Nonnull
            @Override
            public ItemStack getStackInSlot(int slot) {
                return super.getStackInSlot(slot);
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return super.insertItem(slot, stack, simulate);
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return super.extractItem(slot, amount, simulate);
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return null;
        }
        return super.getCapability(cap, side);
    }

    public void spawnItemStack(Level worldIn, double x, double y, double z, ItemStack stack) {
        Random RANDOM = new Random();
        double d0 = (double) EntityType.ITEM.getWidth();
        double d1 = 1.0D - d0;
        double d2 = d0 / 2.0D;
        double d3 = Math.floor(x) + RANDOM.nextDouble() * d1 + d2;
        double d4 = Math.floor(y) + RANDOM.nextDouble() * d1;
        double d5 = Math.floor(z) + RANDOM.nextDouble() * d1 + d2;

        while(!stack.isEmpty()) {
            ItemEntity itementity = new ItemEntity(worldIn, d3, d4, d5, stack.split(RANDOM.nextInt(21) + 10));
            float f = 0.05F;
            itementity.lerpMotion(RANDOM.nextGaussian() * 0.05000000074505806D, RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D, RANDOM.nextGaussian() * 0.05000000074505806D);
            worldIn.addFreshEntity(itementity);
        }
    }

    public void dropInventoryItems(Level worldIn, BlockPos pos) {
        IItemHandler h = handler.orElse(null);
        for(int i = 0; i < h.getSlots(); ++i) {
            spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(i));
        }
    }

    public void addItem(List<ItemStack> itemFromBlock)
    {
        IItemHandler h = handler.orElse(null);
        for (int i = 0; i<itemFromBlock.size();i++)
        {
            if(h.isItemValid(i,itemFromBlock.get(i)))
            {
                h.insertItem(i, itemFromBlock.get(i).copy(), false);
            }
        }
        update();
    }

    public boolean addItem(ItemStack itemFromBlock)
    {
        IItemHandler h = handler.orElse(null);
        for (int i = 0; i<h.getSlots();i++)
        {
            if(h.getStackInSlot(i).isEmpty())
            {
                h.insertItem(i, itemFromBlock.copy(), false);
                return true;
            }
        }

        return false;
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        CompoundTag invTag = p_155245_.getCompound("inv");
        handler.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(invTag));
        this.blockColor = p_155245_.getInt("currentColor");
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        save(p_187471_);
    }

    @Override
    public CompoundTag save(CompoundTag p_58888_) {

        super.save(p_58888_);
        handler.ifPresent(h -> {
            CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            p_58888_.put("inv", compound);
        });
        p_58888_.putInt("currentColor",getColor());

        return p_58888_;
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
        if(this.handler != null) {
            this.handler.invalidate();
        }
    }
}
