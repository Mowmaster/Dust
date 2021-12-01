package com.mowmaster.dust.Block.Pedestal;

import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import com.mowmaster.dust.Items.Augments.AugmentRenderDiffuser;
import com.mowmaster.dust.Items.Filters.IPedestalFilter;
import com.mowmaster.dust.Items.Upgrades.Pedestal.IPedestalUpgrade;
import com.mowmaster.dust.Networking.DustPacketParticles;
import com.mowmaster.dust.Networking.DustPacketHandler;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.Block.Pedestal.BasePedestalBlock.*;

public class BasePedestalBlockEntity extends BlockEntity
{
    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandlerPedestal);
    private LazyOptional<IItemHandler> privateHandler = LazyOptional.of(this::createHandlerPedestalPrivate);
    private LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(this::createHandlerPedestalEnergy);
    private LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(this::createHandlerPedestalFluid);
    //private LazyOptional<IExperienceHandler> xpHandler = LazyOptional.of(this::createHandlerPedestalXp);
    private List<ItemStack> stacksList = new ArrayList<>();
    private FluidStack storedFluid = FluidStack.EMPTY;
    private int storedEnergy = 0;
    private int storedXP = 0;
    private boolean boolLight = false;
    private final List<BlockPos> storedLocations = new ArrayList<BlockPos>();
    private int storedValueForUpgrades = 0;
    public BlockPos getPos() { return this.worldPosition; }
    private BasePedestalBlockEntity getPedestal() { return this; }

    public BasePedestalBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(DeferredBlockEntityTypes.PEDESTAL.get(), p_155229_, p_155230_);
    }

    public void update()
    {
        BlockState state = level.getBlockState(worldPosition);
        this.level.sendBlockUpdated(worldPosition, state, state, 3);
        this.setChanged();
    }

    //9 slots, but only when it has the tank upgrade will we allow more then the first to be used.
    public IItemHandler createHandlerPedestal() {
        return new ItemStackHandler(1) {
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
                //Run filter checks here(slot==0)?(true):(false)
                IPedestalFilter filter = getIPedestalFilter();
                if(filter == null)return true;
                return filter.canAcceptItem(getPedestal(),stack,0);
            }

            @Override
            public int getSlots() {
                //maybe return less if there is no tank upgrade???
                return 1;
            }

            @Override
            protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
                //Run filter checks here
                IPedestalFilter filter = getIPedestalFilter();
                if(filter == null)return super.getStackLimit(slot, stack);

                return filter.canAcceptCount(getPedestal(),stack,0);
            }

            @Override
            public int getSlotLimit(int slot) {

                //Hopefully never mess with this again
                return super.getSlotLimit(slot);
            }

            @Nonnull
            @Override
            public ItemStack getStackInSlot(int slot) {
                //only allow slot 1 unless tank upgrade exists
                return super.getStackInSlot(slot);
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                //restrict to slot 1 unless it has the tank
                return super.insertItem(slot, stack, simulate);
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                //restrict to slot 1 unless it has the tank

                return super.extractItem(slot, amount, simulate);
            }
        };
    }

    private IItemHandler createHandlerPedestalPrivate() {
        //going from 5 to 11 slots to future proof things
        return new ItemStackHandler(10) {

            @Override
            protected void onLoad() {
                if(getSlots()<10)
                {
                    for(int i = 0; i < getSlots(); ++i) {
                        stacksList.add(i,getStackInSlot(i));
                    }
                    setSize(10);
                    for(int j = 0;j<stacksList.size();j++) {
                        setStackInSlot(j, stacksList.get(j));
                    }
                }

                super.onLoad();
            }

            @Override
            protected void onContentsChanged(int slot) {
                if(!(stacksList.size()>0))
                {
                    update();
                }
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 0 && stack.getItem() instanceof IPedestalUpgrade && !hasCoin()) return true;
                //if (slot == 1 && stack.getItem().equals(Items.GLOWSTONE_DUST) && getLightBrightness()<15) return true;
                if (slot == 1 && stack.getItem().equals(Items.GLOWSTONE) && !hasLight()) return true;
                if (slot == 2 && stack.getItem() instanceof IPedestalFilter && !(stack.getItem().equals(DeferredRegisterItems.FILTER_BASE.get())) && !hasFilter()) return true;
                if (slot == 3 && stack.getItem().equals(Items.REDSTONE) && getRedstonePowerNeeded()<15) return true;
                if (slot == 4 && stack.getItem().equals(DeferredRegisterItems.AUGMENT_PEDESTAL_ROUNDROBIN.get()) && !hasRRobin()) return true;
                if (slot == 5 && stack.getItem().equals(DeferredRegisterItems.AUGMENT_PEDESTAL_RENDERDIFFUSER.get()) && !hasRenderAugment()) return true;
                return false;
            }
        };
    }

    public IEnergyStorage createHandlerPedestalEnergy() {
        return new IEnergyStorage() {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {

                if(simulate)
                {
                    IPedestalFilter filter = getIPedestalFilter();
                    int spaceAvailable = getMaxEnergyStored()-getEnergyStored();
                    if(filter == null)return (maxReceive>spaceAvailable)?(spaceAvailable):(maxReceive);
                    return filter.canAcceptCount(getPedestal(),ItemStack.EMPTY,2);
                }
                else
                {
                    int currentEnergy = getEnergyStored();
                    int incomingEnergy = maxReceive;
                    int spaceAvailable = getMaxEnergyStored()-currentEnergy;
                    int newEnergy = currentEnergy + ((maxReceive>spaceAvailable)?(spaceAvailable):(maxReceive));
                    storedEnergy = newEnergy;
                    update();
                    return (incomingEnergy>spaceAvailable)?(spaceAvailable):(incomingEnergy);
                }
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {

                if(simulate)
                {
                    return (maxExtract>getEnergyStored())?(getEnergyStored()):(maxExtract);
                }
                else
                {
                    int currentEnergy = getEnergyStored();
                    int remaining = (maxExtract>getEnergyStored())?(0):(currentEnergy-maxExtract);
                    storedEnergy = remaining;
                    update();
                    return (maxExtract>currentEnergy)?(currentEnergy):(maxExtract);
                }
            }

            @Override
            public int getEnergyStored() {

                return storedEnergy;
            }

            @Override
            public int getMaxEnergyStored() {

                return 20000;
            }

            @Override
            public boolean canExtract() {

                if(hasEnergy())return true;
                return false;
            }

            @Override
            public boolean canReceive() {
                if(hasSpaceForEnergy())
                {
                    IPedestalFilter filter = getIPedestalFilter();
                    if(filter == null)return true;
                    return filter.canAcceptItem(getPedestal(),ItemStack.EMPTY,2);
                }

                return false;
            }
        };
    }

    public IFluidHandler createHandlerPedestalFluid() {
        return new IFluidHandler() {
            @Nonnull
            @Override
            public FluidStack getFluidInTank(int i) {
                return storedFluid;
            }

            @Override
            public int getTanks() {
                //Technically we dont use the tanks thing, but we'll pretend and hope it doesnt break...
                return 1;
            }

            @Nonnull
            @Override
            public FluidStack drain(FluidStack resource, FluidAction action) {

                FluidStack stored = storedFluid.copy();
                if(stored.isFluidEqual(resource))
                {
                    int storedAmount = stored.getAmount();
                    int receivingAmount = resource.getAmount();
                    if(action.simulate())
                    {
                        if(receivingAmount>storedAmount)
                        {
                            return new FluidStack(resource.getFluid(),storedAmount,resource.getTag());
                        }
                        return resource;
                    }
                    else if(action.execute())
                    {
                        if(receivingAmount>storedAmount)
                        {
                            storedFluid = FluidStack.EMPTY;
                            update();
                            return new FluidStack(resource.getFluid(),storedAmount,resource.getTag());
                        }

                        FluidStack newStack = storedFluid.copy();
                        newStack.setAmount(storedAmount-receivingAmount);
                        storedFluid = newStack;
                        update();
                        return resource;
                    }
                }

                return FluidStack.EMPTY;
            }

            @Nonnull
            @Override
            public FluidStack drain(int i, FluidAction fluidAction) {

                FluidStack stored = storedFluid.copy();
                int storedAmount = stored.getAmount();
                int receivingAmount = i;
                if(fluidAction.simulate())
                {
                    if(receivingAmount>storedAmount)
                    {
                        return stored;
                    }
                    return new FluidStack(stored.getFluid(),receivingAmount,stored.getTag());
                }
                else if(fluidAction.execute())
                {
                    if(receivingAmount>storedAmount)
                    {
                        storedFluid = FluidStack.EMPTY;
                        update();
                        return stored;
                    }

                    FluidStack newStack = stored.copy();
                    newStack.setAmount(storedAmount-receivingAmount);
                    storedFluid = newStack;
                    update();
                    return new FluidStack(stored.getFluid(),receivingAmount,stored.getTag());
                }

                return FluidStack.EMPTY;
            }

            @Override
            public int getTankCapacity(int i) {
                return 16000;
            }

            @Override
            public boolean isFluidValid(int i, @Nonnull FluidStack fluidStack) {

                if(hasFilter())
                {
                    IPedestalFilter filter = getIPedestalFilter();
                    Item item = fluidStack.getFluid().getBucket();
                    ItemStack incomingBucket = new ItemStack(item);
                    return filter.canAcceptItem(getPedestal(),incomingBucket,1);
                }
                else
                {
                    if(storedFluid.isEmpty())return true;
                    else if(storedFluid.isFluidEqual(fluidStack))return true;
                }

                return false;
            }

            @Override
            public int fill(FluidStack fluidStack, FluidAction fluidAction) {

                if(isFluidValid(0,fluidStack))
                {
                    FluidStack stored = storedFluid.copy();
                    int storedAmount = stored.getAmount();
                    int receivingAmount = fluidStack.getAmount();
                    int canReceive;
                    IPedestalFilter filter = getIPedestalFilter();
                    if(filter == null)return getTankCapacity(0)-storedAmount;
                    canReceive = filter.canAcceptCount(getPedestal(),ItemStack.EMPTY,1);

                    if(canReceive>0)
                    {
                        if(fluidAction.simulate())
                        {
                            if(receivingAmount>canReceive)return canReceive;
                            else return receivingAmount;
                        }
                        else if(fluidAction.execute())
                        {
                            if(receivingAmount>canReceive)
                            {
                                stored.setAmount(canReceive);
                                storedFluid = stored;
                                update();
                                return canReceive;
                            }
                            else
                            {
                                stored.setAmount(storedAmount+receivingAmount);
                                storedFluid = stored;
                                update();
                                return receivingAmount;
                            }
                        }
                    }
                }

                return 0;
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        if ((cap == CapabilityEnergy.ENERGY)) {
            return energyHandler.cast();
        }
        if ((cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
            return fluidHandler.cast();
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

    public void dropInventoryItemsPrivate(Level worldIn, BlockPos pos) {
        IItemHandler ph = privateHandler.orElse(null);
        for(int i = 0; i < ph.getSlots(); ++i) {
            /*if(i==0 && hasCoin())
            {
                IUpgradeBase coin = ((IUpgradeBase)ph.getStackInSlot(0).getItem());
                ItemStack actualCoin = ph.getStackInSlot(0);
                coin.removePlayerFromCoin(actualCoin);
                coin.removeWorkQueueFromCoin(actualCoin);
                coin.removeWorkQueueTwoFromCoin(actualCoin);
                coin.removeStoredIntFromCoin(actualCoin);
                coin.removeStoredIntTwoFromCoin(actualCoin);
                coin.removeFilterQueueHandler(actualCoin);
                coin.removeFilterBlock(actualCoin);
                coin.removeInventoryQueue(actualCoin);
                coin.removeCraftingQueue(actualCoin);
            }*/
            spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), ph.getStackInSlot(i));
        }
    }



    /*============================================================================
    ==============================================================================
    =========================    STORED VALUE START    ===========================
    ==============================================================================
    ============================================================================*/

    public int getStoredValueForUpgrades()
    {
        return storedValueForUpgrades;
    }

    public void setStoredValueForUpgrades(int value)
    {
        storedValueForUpgrades = value;
        update();
    }

    /*============================================================================
    ==============================================================================
    =========================     STORED VALUE END     ===========================
    ==============================================================================
    ============================================================================*/



    /*============================================================================
    ==============================================================================
    ===========================     LINKING START    =============================
    ==============================================================================
    ============================================================================*/

    public int getNumberOfStoredLocations() {return storedLocations.size();}

    public int getRange()
    {
        //Update if i ever do add back in range augments, or some sort of range increasing potion?
        // 0 = Default, scales to 5 which is 64 blocks currently
        return 0;
    }

    public boolean storeNewLocation(BlockPos pos)
    {
        boolean returner = false;
        if(getNumberOfStoredLocations() < 8)
        {
            storedLocations.add(pos);
            returner=true;
        }
        update();
        return returner;
    }

    public BlockPos getStoredPositionAt(int index)
    {
        BlockPos sendToPos = getPos();
        if(index<getNumberOfStoredLocations())
        {
            sendToPos = storedLocations.get(index);
        }

        return sendToPos;
    }

    public boolean removeLocation(BlockPos pos)
    {
        boolean returner = false;
        if(getNumberOfStoredLocations() >= 1)
        {
            storedLocations.remove(pos);
            returner=true;
        }
        update();

        return returner;
    }

    public boolean isAlreadyLinked(BlockPos pos) {
        return storedLocations.contains(pos);
    }

    public List<BlockPos> getLocationList()
    {
        return storedLocations;
    }

    public int getLinkingRange()
    {
        int range = 8;
        switch (getRange())
        {
            case 0:
                range = (getRange()>0)?(8):(8);//normal speed
                break;
            case 1:
                range=(getRange()>0)?(12):(8);//2x faster
                break;
            case 2:
                range = (getRange()>0)?(16):(8);;//4x faster
                break;
            case 3:
                range = (getRange()>0)?(32):(8);;//6x faster
                break;
            case 4:
                range = (getRange()>0)?(48):(8);;//10x faster
                break;
            case 5:
                range=(getRange()>0)?(64):(8);;//20x faster
                break;
            default: range=8;
        }
        return  range;
    }

    public boolean isPedestalInRange(BasePedestalBlockEntity pedestalCurrent, BlockPos pedestalToBeLinked)
    {
        int range = pedestalCurrent.getLinkingRange();
        int x = pedestalToBeLinked.getX();
        int y = pedestalToBeLinked.getY();
        int z = pedestalToBeLinked.getZ();
        int x1 = pedestalCurrent.getPos().getX();
        int y1 = pedestalCurrent.getPos().getY();
        int z1 = pedestalCurrent.getPos().getZ();
        int xF = Math.abs(Math.subtractExact(x,x1));
        int yF = Math.abs(Math.subtractExact(y,y1));
        int zF = Math.abs(Math.subtractExact(z,z1));

        if(xF>range || yF>range || zF>range)
        {
            return false;
        }
        else return true;
    }

    /*============================================================================
    ==============================================================================
    ===========================      LINKING END     =============================
    ==============================================================================
    ============================================================================*/



    /*============================================================================
    ==============================================================================
    ===========================     ITEM START       =============================
    ==============================================================================
    ============================================================================*/

    public boolean hasItem()
    {
        IItemHandler h = handler.orElse(null);
        if(h.getStackInSlot(0).isEmpty())
        {
            return false;
        }
        else  return true;
    }

    public ItemStack getItemInPedestal()
    {
        IItemHandler h = handler.orElse(null);
        if(hasItem())
        {
            return h.getStackInSlot(0);
        }
        else return ItemStack.EMPTY;
    }

    public int countAllowedForInsert(ItemStack stackIn) {
        if(hasItem())
        {
            if(stackIn.getItem().equals(getItemInPedestal().getItem()))
            {
                int allowedInsertCount = stackIn.getMaxStackSize() - getItemInPedestal().getCount();
                return allowedInsertCount;
            }
            else return 0;
        }
        else return stackIn.getMaxStackSize();

    }

    public ItemStack removeItem(int numToRemove) {
        IItemHandler h = handler.orElse(null);
        ItemStack stack = h.extractItem(0,numToRemove,false);
        //update();

        return stack;
    }

    public ItemStack removeItem() {
        IItemHandler h = handler.orElse(null);
        ItemStack stack = h.extractItem(0,getItemInPedestal().getMaxStackSize(),false);
        //update();

        return stack;
    }

    public boolean addItem(ItemStack itemFromBlock,boolean simulate)
    {
        IItemHandler h = handler.orElse(null);
        if(h.isItemValid(0,itemFromBlock))
        {
            if(hasItem())
            {
                if(ItemHandlerHelper.canItemStacksStack(getItemInPedestal(),itemFromBlock))
                {
                    if(!simulate)
                    {
                        h.insertItem(0, itemFromBlock.copy(), false);
                        update();
                    }
                    return true;
                }
            }
            else
            {
                if(!simulate)
                {
                    h.insertItem(0, itemFromBlock.copy(), false);
                    update();
                }
                return true;
            }
        }

        return false;
    }

    public int getItemTransferRate()
    {
        int itemRate = 4;
        /*switch (getCapacity())
        {
            case 0:
                itemRate = (getCapacity()>0)?(4):(4);
                break;
            case 1:
                itemRate= (getCapacity()>0)?(8):(4);
                break;
            case 2:
                itemRate = (getCapacity()>0)?(16):(4);
                break;
            case 3:
                itemRate = (getCapacity()>0)?(32):(4);
                break;
            case 4:
                itemRate = (getCapacity()>0)?(48):(4);
                break;
            case 5:
                itemRate=(getCapacity()>0)?(64):(4);
                break;
            default: itemRate=4;
        }*/

        return  itemRate;
    }

    public int getSlotSizeLimit()
    {
        IItemHandler h = handler.orElse(null);
        return (h != null)?(h.getSlotLimit(0)):(0);
    }

    public void collideWithPedestal(Level world, BasePedestalBlockEntity tilePedestal, BlockPos posPedestal, BlockState state, Entity entityIn)
    {
        //Handle items and o things like potions too.
        if(!world.isClientSide()) {
            /*if(entityIn instanceof ItemEntity)
            {
                if(tilePedestal.hasCoin())
                {
                    Item coinInPed = tilePedestal.getCoinOnPedestal().getItem();
                    if(coinInPed instanceof IUpgradeBase)
                    {
                        ((IUpgradeBase) coinInPed).actionOnCollideWithBlock(tilePedestal, ((ItemEntity)entityIn));
                    }
                }
            }*/
        }
    }

    /*============================================================================
    ==============================================================================
    ===========================      ITEM END        =============================
    ==============================================================================
    ============================================================================*/



    /*============================================================================
    ==============================================================================
    ===========================    FLUID  START      =============================
    ==============================================================================
    ============================================================================*/

    public boolean hasFluid()
    {
        IFluidHandler h = fluidHandler.orElse(null);
        if(!h.getFluidInTank(0).isEmpty())return true;
        return false;
    }

    public FluidStack getStoredFluid()
    {
        IFluidHandler h = fluidHandler.orElse(null);
        if(!h.getFluidInTank(0).isEmpty())return h.getFluidInTank(0);
        return FluidStack.EMPTY;
    }

    public int getFluidCapacity()
    {
        IFluidHandler h = fluidHandler.orElse(null);
        return h.getTankCapacity(0);
    }

    public int spaceForFluid()
    {
        return getFluidCapacity()-getStoredFluid().getAmount();
    }

    public boolean canAcceptFluid(FluidStack fluidStackIn)
    {
        IFluidHandler h = fluidHandler.orElse(null);
        return h.isFluidValid(0,fluidStackIn);
    }

    public FluidStack removeFluid(FluidStack fluidToRemove, IFluidHandler.FluidAction action)
    {
        IFluidHandler h = fluidHandler.orElse(null);
        return h.drain(fluidToRemove,action);
    }

    public FluidStack removeFluid(int fluidAmountToRemove, IFluidHandler.FluidAction action)
    {
        IFluidHandler h = fluidHandler.orElse(null);
        return h.drain(new FluidStack(getStoredFluid().getFluid(),fluidAmountToRemove,getStoredFluid().getTag()),action);
    }

    public int addFluid(FluidStack fluidStackIn, IFluidHandler.FluidAction fluidAction)
    {
        IFluidHandler h = fluidHandler.orElse(null);
        return h.fill(fluidStackIn,fluidAction);
    }

    public int getFluidTransferRate()
    {
        //im assuming # = rf value???
        int fluidTransferRate = 0;
        int modifier = 1;
        switch (modifier)
        {
            case 0:
                fluidTransferRate = 1000;//1x
                break;
            case 1:
                fluidTransferRate=2000;//2x
                break;
            case 2:
                fluidTransferRate = 4000;//4x
                break;
            case 3:
                fluidTransferRate = 6000;//6x
                break;
            case 4:
                fluidTransferRate = 10000;//10x
                break;
            case 5:
                fluidTransferRate=20000;//20x
                break;
            default: fluidTransferRate=2000;
        }

        return  fluidTransferRate;
    }


    /*============================================================================
    ==============================================================================
    ===========================     FLUID  END       =============================
    ==============================================================================
    ============================================================================*/



    /*============================================================================
    ==============================================================================
    ===========================    ENERGY START      =============================
    ==============================================================================
    ============================================================================*/

    public boolean hasEnergy()
    {
        IEnergyStorage h = energyHandler.orElse(null);
        if(h.getEnergyStored()>0)return true;

        return false;
    }

    public boolean hasSpaceForEnergy()
    {
        return getEnergyCapacity() - getStoredEnergy() > 0;
    }

    public int getEnergyCapacity()
    {
        IEnergyStorage h = energyHandler.orElse(null);
        return h.getMaxEnergyStored();
    }

    public int getStoredEnergy()
    {
        IEnergyStorage h = energyHandler.orElse(null);
        return h.getEnergyStored();
    }

    public int addEnergy(int amountIn, boolean simulate)
    {
        IEnergyStorage h = energyHandler.orElse(null);
        return h.receiveEnergy(amountIn,simulate);
    }

    public int removeEnergy(int amountOut, boolean simulate)
    {
        IEnergyStorage h = energyHandler.orElse(null);
        return h.extractEnergy(amountOut,simulate);
    }

    public boolean canAcceptEnergy()
    {
        IEnergyStorage h = energyHandler.orElse(null);
        return h.canReceive();
    }

    public boolean canSendEnergy()
    {
        IEnergyStorage h = energyHandler.orElse(null);
        return h.canExtract();
    }

    public int getEnergyTransferRate()
    {
        //im assuming # = rf value???
        int fluidTransferRate = 0;
        int modifier = 1;
        switch (modifier)
        {
            case 0:
                fluidTransferRate = 125;//1x
                break;
            case 1:
                fluidTransferRate=250;//2x
                break;
            case 2:
                fluidTransferRate = 500;//4x
                break;
            case 3:
                fluidTransferRate = 1000;//6x
                break;
            case 4:
                fluidTransferRate = 10000;//10x
                break;
            case 5:
                fluidTransferRate=20000;//20x
                break;
            default: fluidTransferRate=250;
        }

        return  fluidTransferRate;
    }

    /*============================================================================
    ==============================================================================
    ===========================     ENERGY END       =============================
    ==============================================================================
    ============================================================================*/



    /*============================================================================
    ==============================================================================
    ===========================      COIN START      =============================
    ==============================================================================
    ============================================================================*/

    public boolean hasCoin()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(ph.getStackInSlot(0).isEmpty())
        {
            return false;
        }
        else  return true;
    }

    public ItemStack getCoinOnPedestal()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(hasCoin())
        {
            return ph.getStackInSlot(0);
        }
        else return ItemStack.EMPTY;
    }

    public ItemStack removeCoin() {
        IItemHandler ph = privateHandler.orElse(null);
        ItemStack stack = ph.getStackInSlot(0);
        ph.extractItem(0,stack.getCount(),false);
        //update();

        return stack;
    }

    public boolean addCoin(Player player, ItemStack coinFromBlock, boolean simulate)
    {
        if(hasCoin())
        {
            return false;
        }
        else
        {
            IItemHandler ph = privateHandler.orElse(null);
            ItemStack coinItem = coinFromBlock.copy();
            coinItem.setCount(1);
            if(!hasCoin() && ph.isItemValid(0,coinItem))
            {
                if(!simulate)
                {
                    //((IPedestalUpgrade)coinFromBlock.getItem()).setPlayerOnCoin(coinFromBlock,player);
                    ph.insertItem(0,coinItem,false);
                }
                return true;
            }
            else return false;
        }
    }

    /*============================================================================
    ==============================================================================
    ===========================       COIN END       =============================
    ==============================================================================
    ============================================================================*/

    /*============================================================================
    ==============================================================================
    ===========================     SPEED START      =============================
    ==============================================================================
    ============================================================================*/

    /*public boolean addSpeed(ItemStack speedUpgrade)
    {
        IItemHandler ph = privateHandler.orElse(null);
        ItemStack itemFromBlock = speedUpgrade.copy();
        itemFromBlock.setCount(1);
        if(getSpeed() < 5)
        {
            ph.insertItem(2,itemFromBlock,false);
            //update();
            return true;
        }
        else return false;
    }

    public ItemStack removeSpeed(int count)
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(hasSpeed())
        {
            //update();
            return ph.extractItem(2,count,false);
        }
        else return ItemStack.EMPTY;
    }

    public ItemStack removeSpeed()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(hasSpeed())
        {
            //update();
            return ph.extractItem(2,ph.getStackInSlot(2).getCount(),false);
        }
        else return ItemStack.EMPTY;
    }

    public boolean hasSpeed()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(ph.getStackInSlot(2).isEmpty())
        {
            return false;
        }
        else  return true;
    }

    public int getSpeed()
    {
        IItemHandler ph = privateHandler.orElse(null);
        return ph.getStackInSlot(2).getCount();
    }

    public int getOperationSpeed()
    {
        int speed = 20;
        switch (getSpeed())
        {
            case 0:
                speed = (getSpeed()>0)?(20):(20);//normal speed
                break;
            case 1:
                speed=(getSpeed()>0)?(10):(20);//2x faster
                break;
            case 2:
                speed = (getSpeed()>0)?(5):(20);;//4x faster
                break;
            case 3:
                speed = (getSpeed()>0)?(3):(20);;//6x faster
                break;
            case 4:
                speed = (getSpeed()>0)?(2):(20);;//10x faster
                break;
            case 5:
                speed=(getSpeed()>0)?(1):(20);;//20x faster
                break;
            default: speed=20;
        }

        return  speed;
    }*/

    /*============================================================================
    ==============================================================================
    ===========================      SPEED END       =============================
    ==============================================================================
    ============================================================================*/



    /*============================================================================
    ==============================================================================
    ===========================    CAPACITY START    =============================
    ==============================================================================
    ============================================================================*/

    /*public boolean addCapacity(ItemStack capacityUpgrade)
    {
        IItemHandler ph = privateHandler.orElse(null);
        ItemStack itemFromBlock = capacityUpgrade.copy();
        itemFromBlock.setCount(1);
        if(getCapacity() < 5)
        {
            ph.insertItem(3,itemFromBlock,false);
            //update();
            return true;
        }
        else return false;
    }

    public ItemStack removeCapacity(int count)
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(hasCapacity())
        {
            //update();
            return ph.extractItem(3,count,false);
        }
        else return ItemStack.EMPTY;
    }

    public ItemStack removeCapacity()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(hasCapacity())
        {
            //update();
            return ph.extractItem(3,ph.getStackInSlot(3).getCount(),false);
        }
        else return ItemStack.EMPTY;
    }

    public boolean hasCapacity()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(ph.getStackInSlot(3).isEmpty())
        {
            return false;
        }
        else  return true;
    }
    public int getCapacity()
    {
        IItemHandler ph = privateHandler.orElse(null);
        return ph.getStackInSlot(3).getCount();
    }*/

    /*============================================================================
    ==============================================================================
    ===========================     CAPACITY END     =============================
    ==============================================================================
    ============================================================================*/



    /*============================================================================
    ==============================================================================
    ===========================      LIGHT START     =============================
    ==============================================================================
    ============================================================================*/

    private int slotLight = 1;

    public boolean addLight()
    {
        if(hasLight())
        {
            return false;
        }
        else
        {
            boolLight = true;
            IItemHandler ph = privateHandler.orElse(null);
            BlockState state = level.getBlockState(worldPosition);
            BlockState newstate = ColorReference.addColorToBlockState(DeferredRegisterTileBlocks.BLOCK_PEDESTAL.get().defaultBlockState(),ColorReference.getColorFromStateInt(state)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)).setValue(FACING, state.getValue(FACING)).setValue(LIT, Boolean.valueOf(true)).setValue(FILTER_STATUS, state.getValue(FILTER_STATUS));
            ph.insertItem(slotLight,new ItemStack(Items.GLOWSTONE,1),false);
            update();
            level.setBlock(worldPosition,newstate,3);
            return true;
        }
    }

    /*public boolean addLight()
    {
        BlockState state = level.getBlockState(worldPosition);
        BlockState newstate = ColorReference.addColorToBlockState(DeferredRegisterTileBlocks.BLOCK_PEDESTAL.get().defaultBlockState(),ColorReference.getColorFromStateInt(state)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)).setValue(FACING, state.getValue(FACING)).setValue(LIT, Boolean.valueOf(true)).setValue(FILTER_STATUS, state.getValue(FILTER_STATUS));
        if(hasLight())
        {
            if(getLightBrightness()>=15)
            {
                return false;
            }
            else
            {
                IItemHandler ph = privateHandler.orElse(null);
                ph.insertItem(slotLight,new ItemStack(Items.GLOWSTONE_DUST,1),false);
                state.updateNeighbourShapes(this.level,this.worldPosition,1,3);
                return true;
            }
        }
        else
        {
            boolLight = true;
            IItemHandler ph = privateHandler.orElse(null);
            ph.insertItem(slotLight,new ItemStack(Items.GLOWSTONE_DUST,1),false);
            update();
            level.setBlock(worldPosition,newstate,3);
            return true;
        }
    }*/

    public ItemStack removeLight()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(hasLight())
        {
            BlockState state = level.getBlockState(worldPosition);
            BlockState newstate = ColorReference.addColorToBlockState(DeferredRegisterTileBlocks.BLOCK_PEDESTAL.get().defaultBlockState(),ColorReference.getColorFromStateInt(state)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)).setValue(FACING, state.getValue(FACING)).setValue(LIT, Boolean.valueOf(false)).setValue(FILTER_STATUS, state.getValue(FILTER_STATUS));
            boolLight = true;
            ph.extractItem(slotLight,1,false);
            level.setBlock(worldPosition,newstate,3);
            update();
            return new ItemStack(Items.GLOWSTONE,1);

        }
        else return ItemStack.EMPTY;
    }

    /*public ItemStack removeLight()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(hasLight())
        {
            BlockState state = level.getBlockState(worldPosition);
            BlockState newstate = ColorReference.addColorToBlockState(DeferredRegisterTileBlocks.BLOCK_PEDESTAL.get().defaultBlockState(),ColorReference.getColorFromStateInt(state)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)).setValue(FACING, state.getValue(FACING)).setValue(LIT, Boolean.valueOf(false)).setValue(FILTER_STATUS, state.getValue(FILTER_STATUS));
            if(getLightBrightness()<=1)
            {
                boolLight = true;
                ph.extractItem(slotLight,1,false);
                level.setBlock(worldPosition,newstate,3);
                return new ItemStack(Items.GLOWSTONE_DUST,1);
            }
            else
            {
                ph.extractItem(slotLight,1,false);
                state.updateNeighbourShapes(this.level,this.worldPosition,1,3);
                return new ItemStack(Items.GLOWSTONE_DUST,1);
            }

        }
        else return ItemStack.EMPTY;
    }*/

    /*public ItemStack removeAllLight()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(hasLight())
        {
            BlockState state = level.getBlockState(worldPosition);
            BlockState newstate = ColorReference.addColorToBlockState(DeferredRegisterTileBlocks.BLOCK_PEDESTAL.get().defaultBlockState(),ColorReference.getColorFromStateInt(state)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)).setValue(FACING, state.getValue(FACING)).setValue(LIT, Boolean.valueOf(false)).setValue(FILTER_STATUS, state.getValue(FILTER_STATUS));
            int slotCount = ph.getStackInSlot(slotLight).getCount();
            ph.extractItem(slotLight,slotCount,false);
            level.setBlock(worldPosition,newstate,3);
            return new ItemStack(Items.GLOWSTONE_DUST,slotCount);
        }
        else return ItemStack.EMPTY;
    }*/

    /*public int getLightBrightness()
    {
        IItemHandler ph = privateHandler.orElse(null);
        return ph.getStackInSlot(slotLight).getCount();
    }*/


    public boolean hasLight()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(ph.getStackInSlot(slotLight).isEmpty())
        {
            return false;
        }
        else  return true;
    }

    /*============================================================================
    ==============================================================================
    ===========================       LIGHT END      =============================
    ==============================================================================
    ============================================================================*/



    /*============================================================================
    ==============================================================================
    ===========================     FILTER START     =============================
    ==============================================================================
    ============================================================================*/

    private int slotFilter = 2;
    public boolean hasFilter()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(ph.getStackInSlot(slotFilter).isEmpty())
        {
            return false;
        }
        else  return true;
    }

    public ItemStack getFilterInPedestal()
    {
        IItemHandler ph = privateHandler.orElse(null);
        return ph.getStackInSlot(slotFilter);
    }

    public IPedestalFilter getIPedestalFilter()
    {
        if(hasFilter())
        {
            if(getFilterInPedestal().getItem() instanceof IPedestalFilter)
            {
                return ((IPedestalFilter)getFilterInPedestal().getItem());
            }
        }

        return null;
    }

    public ItemStack removeFilter(boolean updateBlock) {
        IItemHandler ph = privateHandler.orElse(null);
        if(updateBlock)
        {
            BlockState state = level.getBlockState(worldPosition);
            BlockState newstate = ColorReference.addColorToBlockState(DeferredRegisterTileBlocks.BLOCK_PEDESTAL.get().defaultBlockState(),ColorReference.getColorFromStateInt(state)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)).setValue(FACING, state.getValue(FACING)).setValue(LIT, state.getValue(LIT)).setValue(FILTER_STATUS, 0);
            level.setBlock(worldPosition,newstate,3);
            update();
        }

        return ph.extractItem(slotFilter,ph.getStackInSlot(slotFilter).getCount(),false);
    }

    public boolean addFilter(ItemStack filter, boolean simulate)
    {
        if(hasFilter())
        {
            return false;
        }
        else
        {
            IItemHandler ph = privateHandler.orElse(null);
            if(!hasFilter() && ph.isItemValid(slotFilter,filter))
            {
                if(!simulate)
                {
                    ph.insertItem(slotFilter,filter.copy(),false);
                    BlockState state = level.getBlockState(worldPosition);
                    BlockState newstate = ColorReference.addColorToBlockState(DeferredRegisterTileBlocks.BLOCK_PEDESTAL.get().defaultBlockState(),ColorReference.getColorFromStateInt(state)).setValue(WATERLOGGED, state.getValue(WATERLOGGED)).setValue(FACING, state.getValue(FACING)).setValue(LIT, state.getValue(LIT)).setValue(FILTER_STATUS, (((IPedestalFilter) filter.getItem()).getFilterType(filter))?(2):(1));
                    level.setBlock(worldPosition,newstate,3);
                    update();
                }
                return true;
            }
            else return false;
        }
    }

    /*============================================================================
    ==============================================================================
    ===========================      FILTER END      =============================
    ==============================================================================
    ============================================================================*/

    /*============================================================================
    ==============================================================================
    ===========================    REDSTONE START    =============================
    ==============================================================================
    ============================================================================*/

    private int torchSlot = 3;
    public boolean hasRedstone()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(ph.getStackInSlot(torchSlot).isEmpty())
        {
            return false;
        }
        else  return true;
    }

    public boolean addRedstone()
    {
        IItemHandler ph = privateHandler.orElse(null);
        ItemStack itemFromBlock = new ItemStack(Items.REDSTONE);
        itemFromBlock.setCount(1);
        if(!hasRedstone() || getRedstonePowerNeeded()<15)
        {
            ph.insertItem(torchSlot,itemFromBlock,false);
            return true;
        }
        else return false;
    }

    public ItemStack removeRedstone()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(hasRedstone() && getRedstonePowerNeeded()>=1)
        {
            ph.extractItem(torchSlot,1,false);
            return new ItemStack(Items.REDSTONE,1);
        }
        return ItemStack.EMPTY;
    }

    public ItemStack removeAllRedstone()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(hasRedstone() && getRedstonePowerNeeded()>=1)
        {
            int slotCount = ph.getStackInSlot(torchSlot).getCount();
            ph.extractItem(torchSlot,slotCount,false);
            return new ItemStack(Items.REDSTONE,slotCount);
        }
        return ItemStack.EMPTY;
    }

    public int getRedstonePowerNeeded()
    {
        IItemHandler ph = privateHandler.orElse(null);
        return ph.getStackInSlot(torchSlot).getCount();
    }

    public boolean isPedestalBlockPowered()
    {
        if(hasRedstone())
        {
            //hasRedstone should mean if theres a signal, its off (reverse of normal)
            return (this.getLevel().hasNeighborSignal(this.getBlockPos()))?((getRedstonePower()>=getRedstonePowerNeeded())?(false):(true)):(true);
        }

        return getRedstonePower() > 0;
    }

    public int getRedstonePower() {
        return this.getLevel().getBestNeighborSignal(this.getBlockPos());
    }

    /*============================================================================
    ==============================================================================
    ===========================    REDSTONE END      =============================
    ==============================================================================
    ============================================================================*/



    /*============================================================================
    ==============================================================================
    ===========================     Robin START     ==============================
    ==============================================================================
    ============================================================================*/

    private int slotRobin = 4;
    public boolean addRRobin(ItemStack roundRobin)
    {
        IItemHandler ph = privateHandler.orElse(null);
        ItemStack itemFromBlock = roundRobin.copy();
        itemFromBlock.setCount(1);
        if(!hasRRobin())
        {
            ph.insertItem(slotRobin,itemFromBlock,false);
            //update();
            return true;
        }
        else return false;
    }

    public ItemStack removeRRobin()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(hasRRobin())
        {
            //update();
            return ph.extractItem(slotRobin,ph.getStackInSlot(slotRobin).getCount(),false);
        }
        else return ItemStack.EMPTY;
    }

    public boolean hasRRobin()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(ph.getStackInSlot(slotRobin).isEmpty())
        {
            return false;
        }
        else  return true;
    }

    /*============================================================================
    ==============================================================================
    ===========================      Robin END      ==============================
    ==============================================================================
    ============================================================================*/



    /*============================================================================
    ==============================================================================
    ============================   RENDER START    ===============================
    ==============================================================================
    ============================================================================*/

    private int slotRenderer = 5;
    public boolean addRenderAugment(ItemStack particle)
    {
        IItemHandler ph = privateHandler.orElse(null);
        ItemStack itemFromBlock = particle.copy();
        itemFromBlock.setCount(1);
        if(!hasRenderAugment())
        {
            //update();
            ph.insertItem(slotRenderer,itemFromBlock,false);
            return true;
        }
        else return false;
    }

    public ItemStack removeRenderAugment()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(hasRenderAugment())
        {
            //update();
            return ph.extractItem(slotRenderer,ph.getStackInSlot(slotRenderer).getCount(),false);
        }
        else return ItemStack.EMPTY;
    }

    public boolean hasRenderAugment()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(ph.getStackInSlot(slotRenderer).isEmpty())
        {
            return false;
        }
        else  return true;
    }

    public boolean canSpawnParticles()
    {
        switch (getRendererType())
        {
            case 0: return false;
            case 1: return true;
            case 2: return true;
            case 3: return false;
            case 4: return false;
            case 5: return true;
            case 6: return false;
            case 7: return true;
            default: return true;
        }
    }

    public int getRendererType()
    {
        // 0 - No Particles
        // 1 - No Render Item
        // 2 - No Render Upgrade
        // 3 - No Particles/No Render Item
        // 4 - No Particles/No Render Upgrade
        // 5 - No Render Item/No Render Upgrade
        // 6 - No Particles/No Render Item/No Render Upgrade
        // 7 - No Augment exists and thus all rendering is fine.
        IItemHandler ph = privateHandler.orElse(null);
        if(hasRenderAugment())
        {
            if(ph.getStackInSlot(slotRenderer).getItem() instanceof AugmentRenderDiffuser)
            {
                AugmentRenderDiffuser augment = ((AugmentRenderDiffuser)ph.getStackInSlot(slotRenderer).getItem());
                return augment.getAugmentMode(ph.getStackInSlot(slotRenderer));
            }
            else  return 0;
        }
        else  return 7;
    }

    /*============================================================================
    ==============================================================================
    ============================    RENDER END     ===============================
    ==============================================================================
    ============================================================================*/



    public boolean canSendItemInPedestal(BasePedestalBlockEntity pedestal)
    {
        if(pedestal.hasItem())return true;

        return false;
    }

    public boolean isSamePedestal(BlockPos pedestalToBeLinked)
    {
        BlockPos thisPedestal = this.getPos();

        if(thisPedestal.equals(pedestalToBeLinked))
        {
            return true;
        }

        return false;
    }

    //Checks when linking pedestals if the two being linked are the same block and within range
    public boolean canLinkToPedestalNetwork(BlockPos pedestalToBeLinked)
    {
        //Check to see if pedestal to be linked is a block pedestal
        if(level.getBlockState(pedestalToBeLinked).getBlock() instanceof BasePedestalBlock)
        {
            //isPedestalInRange(tileCurrent,pedestalToBeLinked);
            return true;
        }

        return false;
    }

    //Returns items available to be insert, 0 if false
    public int canAcceptItems(Level worldIn, BlockPos posPedestal, ItemStack itemsIncoming)
    {
        int canAccept = 0;
        int pedestalAccept = 0;

        if(getItemInPedestal().isEmpty() || getItemInPedestal().equals(ItemStack.EMPTY))
        {
            canAccept = itemsIncoming.getMaxStackSize();
        }
        else
        {
            if(ItemHandlerHelper.canItemStacksStack(getItemInPedestal(),itemsIncoming))
            {
                //Two buckets match but cant be stacked since max stack size is 1
                //BUT if its a tank, its cooler then this
                if(itemsIncoming.getMaxStackSize() > 1)
                {
                    //If i did this right, slot limit should default to stack max size, or custom allowed
                    int allowed = getSlotSizeLimit();
                    if(getItemInPedestal().getCount() < allowed)
                    {
                        canAccept = (allowed - getItemInPedestal().getCount());
                    }
                }
            }
        }

        if(hasFilter())
        {
            Item filterInPed = this.getFilterInPedestal().getItem();
            if(filterInPed instanceof IPedestalFilter)
            {
                pedestalAccept = ((IPedestalFilter) filterInPed).canAcceptCount(getPedestal(), itemsIncoming,0);
            }
        }

        if((canAccept > pedestalAccept) && hasFilter())
        {
            canAccept = pedestalAccept;
        }

        return canAccept;
    }

    public int fluidAmountToAccept(Level worldIn, BlockPos posPedestal, FluidStack fluidIncoming)
    {
        int spaceForFluid = spaceForFluid();
        int canAccept = (fluidIncoming.getAmount()>spaceForFluid)?(spaceForFluid):(fluidIncoming.getAmount());

        if(hasFilter())
        {
            Item filterInPed = this.getFilterInPedestal().getItem();
            if(filterInPed instanceof IPedestalFilter)
            {
                int filterCount = ((IPedestalFilter) filterInPed).canAcceptCount(getPedestal(), ItemStack.EMPTY,1);
                if(filterCount>0)canAccept = filterCount;
            }
        }

        return canAccept;
    }

    public int energyAmountToAccept(Level worldIn, BlockPos posPedestal, int energyIncoming)
    {
        int spaceForEnergy = getEnergyCapacity()-getStoredEnergy();
        int canAccept = (energyIncoming>spaceForEnergy)?(spaceForEnergy):(energyIncoming);

        if(hasFilter())
        {
            Item filterInPed = this.getFilterInPedestal().getItem();
            if(filterInPed instanceof IPedestalFilter)
            {
                int filterCount = ((IPedestalFilter) filterInPed).canAcceptCount(getPedestal(), ItemStack.EMPTY,2);
                if(filterCount>0)canAccept = filterCount;
            }
        }

        return canAccept;
    }

    public boolean hasFilter(BasePedestalBlockEntity pedestalSendingTo)
    {
        boolean returner = false;
        if(pedestalSendingTo.hasFilter())
        {
            Item filterInPedestal = pedestalSendingTo.getFilterInPedestal().getItem();
            if(filterInPedestal instanceof IPedestalFilter)
            {
                returner = true;
            }
        }

        return returner;
    }

    public static LazyOptional<IItemHandler> findItemHandlerPedestal(BasePedestalBlockEntity pedestal)
    {
        Level world = pedestal.getLevel();
        BlockPos pos = pedestal.getPos();
        BlockEntity neighbourTile = world.getBlockEntity(pos);
        if(neighbourTile!=null)
        {
            LazyOptional<IItemHandler> cap = neighbourTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
            if(cap.isPresent())
                return cap;
        }
        return LazyOptional.empty();
    }

    //Needed for filtered imports
    public boolean canSendToPedestal(BlockPos pedestalToSendTo)
    {
        //Method to check if we can send items FROM this pedestal???
        //Check if Block is Loaded in World
        if(level.isAreaLoaded(pedestalToSendTo,1))
        {
            //If block ISNT powered
            if(!isPedestalBlockPowered())
            {
                //Make sure its a pedestal before getting the tile
                if(level.getBlockState(pedestalToSendTo).getBlock() instanceof BasePedestalBlock)
                {
                    //Make sure it is still part of the right network
                    if(canLinkToPedestalNetwork(pedestalToSendTo))
                    {
                        return true;
                    }
                }
                else
                {
                    removeLocation(pedestalToSendTo);
                }
            }
        }

        return false;
    }


    //The actual transfer methods for items
    public void sendItemsToPedestal(BlockPos pedestalToSendTo, ItemStack itemStackIncoming)
    {
        if(hasItem())
        {
            if(level.getBlockEntity(pedestalToSendTo) instanceof BasePedestalBlockEntity)
            {
                BasePedestalBlockEntity tilePedestalToSendTo = (BasePedestalBlockEntity)level.getBlockEntity(pedestalToSendTo);

                //Checks if pedestal is empty or if not then checks if items match and how many can be insert
                if(tilePedestalToSendTo.canAcceptItems(level,pedestalToSendTo,itemStackIncoming) > 0)
                {
                    boolean filter = true;
                    //Check if it has filter, if not return true
                    if(tilePedestalToSendTo.hasFilter())
                    {
                        Item filterInPedestal = tilePedestalToSendTo.getFilterInPedestal().getItem();
                        if(filterInPedestal instanceof IPedestalFilter)
                        {
                            filter = ((IPedestalFilter) filterInPedestal).canAcceptItem(tilePedestalToSendTo,itemStackIncoming,0);
                        }
                    }

                    if(filter)
                    {
                        //Max that can be recieved
                        int countToSend = tilePedestalToSendTo.canAcceptItems(level,pedestalToSendTo,getItemInPedestal());
                        ItemStack copyStackToSend = getItemInPedestal().copy();
                        //Max that is available to send
                        if(copyStackToSend.getCount()<countToSend)
                        {
                            countToSend = copyStackToSend.getCount();
                        }
                        //Get max that can be sent
                        if(countToSend > getItemTransferRate())
                        {
                            countToSend = getItemTransferRate();
                        }


                        if(countToSend >=1)
                        {
                            //Send items
                            if(tilePedestalToSendTo.addItem(copyStackToSend,true))
                            {
                                copyStackToSend.setCount(countToSend);
                                removeItem(copyStackToSend.getCount());
                                tilePedestalToSendTo.addItem(copyStackToSend,false);
                                if(canSpawnParticles()) DustPacketHandler.sendToNearby(level,worldPosition,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR_BEAM,pedestalToSendTo.getX(),pedestalToSendTo.getY(),pedestalToSendTo.getZ(),worldPosition.getX(),worldPosition.getY(),worldPosition.getZ()));
                            }
                        }
                    }
                }
            }
        }
    }

    public void sendFluidsToPedestal(BlockPos pedestalToSendTo, FluidStack fluidStackIncoming)
    {
        if(hasFluid())
        {
            if(level.getBlockEntity(pedestalToSendTo) instanceof BasePedestalBlockEntity)
            {
                BasePedestalBlockEntity tilePedestalToSendTo = (BasePedestalBlockEntity)level.getBlockEntity(pedestalToSendTo);

                //Checks if pedestal is empty or if not then checks if items match and how many can be insert
                if(tilePedestalToSendTo.fluidAmountToAccept(level,pedestalToSendTo,fluidStackIncoming) > 0)
                {
                    if(tilePedestalToSendTo.canAcceptFluid(fluidStackIncoming))
                    {
                        //Max that can be recieved
                        int countToSend = tilePedestalToSendTo.spaceForFluid();
                        FluidStack currentFluid = getStoredFluid().copy();
                        //Max that is available to send
                        if(currentFluid.getAmount()<countToSend)
                        {
                            countToSend = currentFluid.getAmount();
                        }
                        //Get max that can be sent
                        if(countToSend > getFluidTransferRate())
                        {
                            countToSend = getItemTransferRate();
                        }


                        if(countToSend > 0)
                        {
                            //Send items
                            int countFluidToSend = tilePedestalToSendTo.addFluid(new FluidStack(fluidStackIncoming.getFluid(),countToSend,fluidStackIncoming.getTag()), IFluidHandler.FluidAction.SIMULATE);
                            if(countFluidToSend>0)
                            {
                                int finalFluidCountToSend = removeFluid(countFluidToSend, IFluidHandler.FluidAction.SIMULATE).getAmount();
                                if(finalFluidCountToSend>0)
                                {
                                    removeFluid(countFluidToSend, IFluidHandler.FluidAction.EXECUTE);
                                    tilePedestalToSendTo.addFluid(new FluidStack(fluidStackIncoming.getFluid(),finalFluidCountToSend,fluidStackIncoming.getTag()), IFluidHandler.FluidAction.SIMULATE);
                                    if(canSpawnParticles()) DustPacketHandler.sendToNearby(level,worldPosition,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR_BEAM,pedestalToSendTo.getX(),pedestalToSendTo.getY(),pedestalToSendTo.getZ(),worldPosition.getX(),worldPosition.getY(),worldPosition.getZ()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void sendEnergyToPedestal(BlockPos pedestalToSendTo, int energyIncoming)
    {
        if(hasEnergy())
        {
            if(level.getBlockEntity(pedestalToSendTo) instanceof BasePedestalBlockEntity)
            {
                BasePedestalBlockEntity tilePedestalToSendTo = (BasePedestalBlockEntity)level.getBlockEntity(pedestalToSendTo);

                //Checks if pedestal is empty or if not then checks if items match and how many can be insert
                if(tilePedestalToSendTo.energyAmountToAccept(level,pedestalToSendTo,energyIncoming) > 0)
                {
                    if(tilePedestalToSendTo.canAcceptEnergy())
                    {
                        //Max that can be recieved
                        int countToSend = tilePedestalToSendTo.getEnergyCapacity()-tilePedestalToSendTo.getStoredEnergy();

                        //Max that is available to send
                        if(energyIncoming<countToSend)
                        {
                            countToSend = energyIncoming;
                        }
                        //Get max that can be sent
                        if(countToSend > getEnergyTransferRate())
                        {
                            countToSend = getEnergyTransferRate();
                        }


                        if(countToSend > 0)
                        {
                            //Send items
                            int countEnergyToSend = tilePedestalToSendTo.addEnergy(countToSend,true);
                            if(countEnergyToSend>0)
                            {
                                int finalEnergyCountToSend = removeEnergy(countEnergyToSend,true);
                                if(finalEnergyCountToSend>0)
                                {
                                    removeEnergy(finalEnergyCountToSend,false);
                                    tilePedestalToSendTo.addEnergy(finalEnergyCountToSend,false);
                                    if(canSpawnParticles()) DustPacketHandler.sendToNearby(level,worldPosition,new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR_BEAM,pedestalToSendTo.getX(),pedestalToSendTo.getY(),pedestalToSendTo.getZ(),worldPosition.getX(),worldPosition.getY(),worldPosition.getZ()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void transferAction()
    {
        int locations = getNumberOfStoredLocations();
        if(locations > 0)
        {
            if(hasRRobin())
            {
                int robinCount = getStoredValueForUpgrades();
                if(robinCount >= locations)
                {
                    setStoredValueForUpgrades(0);
                    robinCount=0;
                }
                BlockPos posReceiver = getStoredPositionAt(robinCount);
                if(canSendToPedestal(posReceiver))
                {
                    sendItemsToPedestal(posReceiver,getItemInPedestal());
                    sendFluidsToPedestal(posReceiver,getStoredFluid());
                    sendEnergyToPedestal(posReceiver,getStoredEnergy());
                }

                robinCount++;
                setStoredValueForUpgrades(robinCount);
            }
            else
            {
                for(int i=0;i<locations;i++){
                    BlockPos posReceiver = getStoredPositionAt(i);
                    if(canSendToPedestal(posReceiver))
                    {
                        sendItemsToPedestal(posReceiver,getItemInPedestal());
                        sendFluidsToPedestal(posReceiver,getStoredFluid());
                        sendEnergyToPedestal(posReceiver,getStoredEnergy());
                        break;
                    }
                    else continue;
                }
            }
        }
    }






    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BasePedestalBlockEntity e) {
        e.tick();
    }

    public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, BasePedestalBlockEntity e) {
        e.tick();
    }

    int partTicker = 0;
    int impTicker = 0;
    int pedTicker = 0;

    public void tick() {

        if(!level.isClientSide() && level.isAreaLoaded(worldPosition,1))
        {
            if(getNumberOfStoredLocations() > 0 && !isPedestalBlockPowered() && hasItem())
            {
                pedTicker++;
                //if (pedTicker%getOperationSpeed() == 0) {
                if (pedTicker%20 == 0) {

                    transferAction();
                    //Eventually have Energy, Fluids, XP in here too???

                    if(pedTicker >=100){pedTicker=0;}
                }
            }
        }
        if(level.isAreaLoaded(worldPosition,1))
        {
            if(hasCoin() && !isPedestalBlockPowered())
            {
                Item coinInPed = getCoinOnPedestal().getItem();
                if(coinInPed instanceof IPedestalUpgrade)
                {
                    impTicker++;
                    ((IPedestalUpgrade) coinInPed).updateAction(level,this);
                    //Has to be bigger than our biggest ticker value for an upgrade, or itll reset the upgrade instance before the upgrade action can fire
                    if(impTicker >=Integer.MAX_VALUE-100){impTicker=0;}
                }
            }
        }
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        CompoundTag invTag = p_155245_.getCompound("inv");
        handler.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(invTag));
        CompoundTag invPrivateTag = p_155245_.getCompound("inv_private");
        privateHandler.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(invPrivateTag));


        this.storedValueForUpgrades = p_155245_.getInt("storedUpgradeValue");
        this.storedEnergy = p_155245_.getInt("storedEnergy");
        this.storedXP = p_155245_.getInt("storedXP");
        this.storedFluid = FluidStack.loadFluidStackFromNBT(p_155245_.getCompound("storedFluid"));


        int[] storedIX = p_155245_.getIntArray("intArrayXPos");
        int[] storedIY = p_155245_.getIntArray("intArrayYPos");
        int[] storedIZ = p_155245_.getIntArray("intArrayZPos");

        for(int i=0;i<storedIX.length;i++)
        {
            BlockPos gotPos = new BlockPos(storedIX[i],storedIY[i],storedIZ[i]);
            storedLocations.add(gotPos);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        handler.ifPresent(h -> {
            CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            p_187471_.put("inv", compound);
        });
        privateHandler.ifPresent(h -> {
            CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            p_187471_.put("inv_private", compound);
        });

        p_187471_.putInt("storedUpgradeValue",storedValueForUpgrades);
        p_187471_.putInt("storedEnergy",storedEnergy);
        p_187471_.putInt("storedXP",storedXP);
        p_187471_.put("storedFluid",storedFluid.writeToNBT(new CompoundTag()));

        List<Integer> storedX = new ArrayList<Integer>();
        List<Integer> storedY = new ArrayList<Integer>();
        List<Integer> storedZ = new ArrayList<Integer>();

        for(int i=0;i<getNumberOfStoredLocations();i++)
        {
            storedX.add(storedLocations.get(i).getX());
            storedY.add(storedLocations.get(i).getY());
            storedZ.add(storedLocations.get(i).getZ());
        }

        p_187471_.putIntArray("intArrayXPos",storedX);
        p_187471_.putIntArray("intArrayYPos",storedY);
        p_187471_.putIntArray("intArrayZPos",storedZ);
    }

    @Override
    public CompoundTag save(CompoundTag p_58888_) {
        super.save(p_58888_);
        handler.ifPresent(h -> {
            CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            p_58888_.put("inv", compound);
        });
        privateHandler.ifPresent(h -> {
            CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            p_58888_.put("inv_private", compound);
        });

        p_58888_.putInt("storedUpgradeValue",storedValueForUpgrades);
        p_58888_.putInt("storedEnergy",storedEnergy);
        p_58888_.putInt("storedXP",storedXP);
        p_58888_.put("storedFluid",storedFluid.writeToNBT(new CompoundTag()));

        List<Integer> storedX = new ArrayList<Integer>();
        List<Integer> storedY = new ArrayList<Integer>();
        List<Integer> storedZ = new ArrayList<Integer>();

        for(int i=0;i<getNumberOfStoredLocations();i++)
        {
            storedX.add(storedLocations.get(i).getX());
            storedY.add(storedLocations.get(i).getY());
            storedZ.add(storedLocations.get(i).getZ());
        }

        p_58888_.putIntArray("intArrayXPos",storedX);
        p_58888_.putIntArray("intArrayYPos",storedY);
        p_58888_.putIntArray("intArrayZPos",storedZ);

        return p_58888_;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        /*CompoundTag nbtTagCompound = new CompoundTag();
        save(nbtTagCompound);*/
        //super.getUpdatePacket();
        //return new ClientboundBlockEntityDataPacket(this.worldPosition,42,nbtTagCompound);
        return ClientboundBlockEntityDataPacket.create(this.getPedestal());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return save(new CompoundTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net,pkt);
        BlockState state = this.level.getBlockState(worldPosition);
        this.handleUpdateTag(pkt.getTag());
        this.level.sendBlockUpdated(worldPosition, state, state, 3);
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
        if(this.privateHandler != null) {
            this.privateHandler.invalidate();
        }
        if(this.energyHandler != null) {
            this.energyHandler.invalidate();
        }
        if(this.fluidHandler != null) {
            this.fluidHandler.invalidate();
        }
    }
}
