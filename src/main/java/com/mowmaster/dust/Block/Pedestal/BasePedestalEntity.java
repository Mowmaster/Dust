package com.mowmaster.dust.Block.Pedestal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
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

public class BasePedestalEntity extends BlockEntity
{
    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandlerPedestal);
    private LazyOptional<IItemHandler> privateHandler = LazyOptional.of(this::createHandlerPedestalPrivate);
    private LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(this::createHandlerPedestalEnergy);
    private LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(this::createHandlerPedestalFluid);
    //private LazyOptional<IExperienceHandler> xpHandler = LazyOptional.of(this::createHandlerPedestalXp);
    private List<ItemStack> stacksList = new ArrayList<>();
    private boolean boolLight = false;
    private final List<BlockPos> storedLocations = new ArrayList<BlockPos>();
    public BlockPos getPos() { return this.worldPosition; }

    public BasePedestalEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    public void update()
    {
        this.update();
        this.level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),2);
        //do this for pedestals that do inv buffers
        this.level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),1);
        /*public static final int NOTIFY_NEIGHBORS = 1;
        public static final int BLOCK_UPDATE = 2;
        public static final int NO_RERENDER = 4;
        public static final int RERENDER_MAIN_THREAD = 8;
        public static final int UPDATE_NEIGHBORS = 16;
        public static final int NO_NEIGHBOR_DROPS = 32;
        public static final int IS_MOVING = 64;
        public static final int DEFAULT = 3;
        public static final int DEFAULT_AND_RERENDER = 11;*/
    }

    //9 slots, but only when it has the tank upgrade will we allow more then the first to be used.
    public IItemHandler createHandlerPedestal() {
        return new ItemStackHandler(9) {
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
                //Run filter checks here

                return (slot==0)?(true):(false);
            }

            @Override
            public int getSlots() {
                //maybe return less if there is no tank upgrade???
                return 9;
            }

            @Override
            protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
                //Run filter checks here

                return super.getStackLimit(slot, stack);
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
        return new ItemStackHandler(12) {

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
                //if (slot == 0 && stack.getItem() instanceof IUpgradeBase && !hasCoin() && ((hasFilter())?(!FILTER_BROKE_UPGRADES.contains(stack.getItem())):(true))) return true;
                // (slot == 1 && stack.getItem().equals(Items.GLOWSTONE) && !hasLight()) return true;
                //if (slot == 2 && stack.getItem() instanceof IFilterBase && !stack.getItem().equals(ItemFilterBase.BASEFILTER) && !hasFilter() && ((hasCoin())?(!FILTER_BROKE_UPGRADES.contains(getCoinOnPedestal().getItem())):(true))) return true;
                //if (slot == 3 && stack.getItem().equals(Items.REDSTONE_TORCH) && !hasTorch()) return true;
                //if (slot == 4 && stack.getItem().equals(ItemPedestalUpgrades.ROUNDROBIN) && !hasRRobin()) return true;
                //if (slot == 5 && stack.getItem().equals(ItemPedestalUpgrades.PARTICLEDIFFUSER)) return true;
                //if (slot == 6 && stack.getItem().equals(ItemPedestalRenderAugment.RENDERAUGMENT)) return true;
                return false;
            }
        };
    }

    public IEnergyStorage createHandlerPedestalEnergy() {
        return new IEnergyStorage() {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                return 0;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {

                return 0;
            }

            @Override
            public int getEnergyStored() {

                return 0;
            }

            @Override
            public int getMaxEnergyStored() {

                return 0;
            }

            @Override
            public boolean canExtract() {
                return false;
            }

            @Override
            public boolean canReceive() {
                return false;
            }
        };
    }

    public IFluidHandler createHandlerPedestalFluid() {
        return new IFluidHandler() {
            @Nonnull
            @Override
            public FluidStack getFluidInTank(int i) {
                return FluidStack.EMPTY;
            }

            @Override
            public int getTanks() {
                //Technically we dont use the tanks thing, but we'll pretend and hope it doesnt break...
                return 1;
            }

            @Nonnull
            @Override
            public FluidStack drain(int i, FluidAction fluidAction) {

                return FluidStack.EMPTY;
            }

            @Nonnull
            @Override
            public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {

                return FluidStack.EMPTY;
            }

            @Override
            public int getTankCapacity(int i) {
                return 0;
            }

            @Override
            public boolean isFluidValid(int i, @Nonnull FluidStack fluidStack) {


                return false;
            }

            @Override
            public int fill(FluidStack fluidStack, FluidAction fluidAction) {

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
            spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(-1));
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

    public boolean isPedestalInRange(BasePedestalEntity pedestalCurrent, BlockPos pedestalToBeLinked)
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
        if(hasItem())
        {
            if(ItemHandlerHelper.canItemStacksStack(getItemInPedestal(),itemFromBlock))
            {
                if(!simulate)
                {
                    h.insertItem(0, itemFromBlock.copy(), false);
                    //update();
                }
                return true;
            }
        }
        else
        {
            if(!simulate)
            {
                h.insertItem(0, itemFromBlock.copy(), false);
                //update();
            }
            return true;
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

    public void collideWithPedestal(Level world, BasePedestalEntity tilePedestal, BlockPos posPedestal, BlockState state, Entity entityIn)
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
    ===========================      COIN START      =============================
    ==============================================================================
    ============================================================================*/

    /*public boolean hasCoin()
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
        return ph.getStackInSlot(0);
        *//*if(hasCoin())
        {
            return ph.getStackInSlot(0);
        }
        else return ItemStack.EMPTY;*//*
    }

    public ItemStack removeCoin() {
        IItemHandler ph = privateHandler.orElse(null);
        ItemStack stack = ph.getStackInSlot(0);
        if(stack.getItem() instanceof IUpgradeBase){
            IUpgradeBase coin = (IUpgradeBase)stack.getItem();
            coin.removePlayerFromCoin(stack);
            coin.removeWorkQueueFromCoin(stack);
            coin.removeWorkQueueTwoFromCoin(stack);
            coin.removeStoredIntFromCoin(stack);
            coin.removeStoredIntTwoFromCoin(stack);
            coin.removeFilterQueueHandler(stack);
            coin.removeFilterBlock(stack);
            coin.removeInventoryQueue(stack);
            coin.removeCraftingQueue(stack);
            coin.removeOutputIngredientMap(stack);
            coin.removeFilterChangeUpdated(stack);
            coin.removeToolChangeUpdated(stack);
        }
        ph.extractItem(0,stack.getCount(),false);
        //update();

        return stack;
    }

    public boolean addCoin(PlayerEntity player, ItemStack coinFromBlock,boolean simulate)
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
                    ((IUpgradeBase)coinFromBlock.getItem()).setPlayerOnCoin(coinFromBlock,player);
                    ph.insertItem(0,coinItem,false);


                }
                return true;
            }
            else return false;
        }
    }*/

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

    /*public boolean addLight()
    {
        if(hasLight())
        {
            return false;
        }
        else
        {
            boolLight = true;
            BlockState state = world.getBlockState(pos);
            boolean watered = state.get(PedestalBlock.WATERLOGGED);
            Direction dir = state.get(PedestalBlock.FACING);
            int filterState = state.get(PedestalBlock.FILTER_STATUS);
            BlockState newstate = state.with(PedestalBlock.FACING,dir).with(PedestalBlock.WATERLOGGED,watered).with(PedestalBlock.LIT,true).with(PedestalBlock.FILTER_STATUS,filterState);
            IItemHandler ph = privateHandler.orElse(null);
            ph.insertItem(1,new ItemStack(Items.GLOWSTONE,1),false);
            world.notifyBlockUpdate(pos,state,newstate,3);
            world.setBlockState(pos,newstate,3);
            world.markBlockRangeForRenderUpdate(pos,state,newstate);
            //update();
            return true;
        }
    }

    public boolean hasLight()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(ph.getStackInSlot(1).isEmpty())
        {
            return false;
        }
        else  return true;
    }*/

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

    /*public boolean hasFilter()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(ph.getStackInSlot(6).isEmpty())
        {
            return false;
        }
        else  return true;
    }

    public ItemStack getFilterInPedestal()
    {
        IItemHandler ph = privateHandler.orElse(null);
        return ph.getStackInSlot(6);
    }

    public ItemStack removeFilter(boolean updateBlock) {
        IItemHandler ph = privateHandler.orElse(null);
        if(updateBlock)
        {
            BlockState state = world.getBlockState(pos);
            int filterState = 0;
            boolean watered = state.get(PedestalBlock.WATERLOGGED);
            Direction dir = state.get(PedestalBlock.FACING);
            boolean lit = state.get(PedestalBlock.LIT);
            BlockState newstate = state.with(PedestalBlock.FACING,dir).with(PedestalBlock.WATERLOGGED,watered).with(PedestalBlock.LIT,lit).with(PedestalBlock.FILTER_STATUS,filterState);
            world.notifyBlockUpdate(pos,state,newstate,3);
            world.setBlockState(pos,newstate,3);
            world.markBlockRangeForRenderUpdate(pos,state,newstate);
        }

        if(hasCoin())
        {
            if(getCoinOnPedestal().getItem() instanceof IUpgradeBase)
            {
                IUpgradeBase coin = ((IUpgradeBase)getCoinOnPedestal().getItem());
                coin.setFilterChangeUpdate(getCoinOnPedestal());
            }
        }
        return ph.extractItem(6,ph.getStackInSlot(6).getCount(),false);
    }

    public boolean addFilter(ItemStack filter,boolean simulate)
    {
        if(hasFilter())
        {
            return false;
        }
        else
        {
            BlockState state = world.getBlockState(pos);
            boolean watered = state.get(PedestalBlock.WATERLOGGED);
            Direction dir = state.get(PedestalBlock.FACING);
            boolean lit = state.get(PedestalBlock.LIT);
            int filterState = state.get(PedestalBlock.FILTER_STATUS);
            if(filter.getItem() instanceof IFilterBase)
            {
                //Blacklist = 2 , whitelist = 1
                filterState = (((IFilterBase) filter.getItem()).getFilterTypeFromNBT(filter))?(2):(1);
            }
            BlockState newstate = state.with(PedestalBlock.FACING,dir).with(PedestalBlock.WATERLOGGED,watered).with(PedestalBlock.LIT,lit).with(PedestalBlock.FILTER_STATUS,filterState);

            IItemHandler ph = privateHandler.orElse(null);
            ItemStack filterItem = filter.copy();
            filterItem.setCount(1);
            if(!hasFilter() && ph.isItemValid(6,filterItem))
            {
                if(!simulate)
                {
                    ph.insertItem(6,filterItem,false);
                    world.notifyBlockUpdate(pos,state,newstate,3);
                    world.setBlockState(pos,newstate,3);
                    world.markBlockRangeForRenderUpdate(pos,state,newstate);

                    if(hasCoin())
                    {
                        if(getCoinOnPedestal().getItem() instanceof IUpgradeBase)
                        {
                            IUpgradeBase coin = ((IUpgradeBase)getCoinOnPedestal().getItem());
                            coin.setFilterChangeUpdate(getCoinOnPedestal());
                        }
                    }
                }
                return true;
            }
            else return false;
        }
    }

    public boolean updateFilter(ItemStack filter,boolean updateBlock)
    {
        if(hasFilter())
        {
            //old filter needing updated
            ItemStack oldStack = getFilterInPedestal().copy();

            BlockState state = world.getBlockState(pos);
            int filterState = state.get(PedestalBlock.FILTER_STATUS);
            int newFilterState = filterState;
            if(filter.getItem() instanceof IFilterBase)
            {
                //Blacklist = 2 , whitelist = 1
                newFilterState = (((IFilterBase) filter.getItem()).getFilterTypeFromNBT(filter))?(2):(1);

                if(filter.hasTag())
                {
                    oldStack.setTag(filter.getTag());

                    if(hasCoin())
                    {
                        if(getCoinOnPedestal().getItem() instanceof IUpgradeBase)
                        {
                            IUpgradeBase coin = ((IUpgradeBase)getCoinOnPedestal().getItem());
                            coin.setFilterChangeUpdate(getCoinOnPedestal());
                        }
                    }
                }
            }
            IItemHandler ph = privateHandler.orElse(null);
            if(ph.isItemValid(6,filter))
            {
                if(updateBlock) {
                    removeFilter(false);
                    ph.insertItem(6, oldStack, false);
                    if(newFilterState != filterState)
                    {
                        boolean watered = state.get(PedestalBlock.WATERLOGGED);
                        Direction dir = state.get(PedestalBlock.FACING);
                        boolean lit = state.get(PedestalBlock.LIT);
                        BlockState newstate = state.with(PedestalBlock.FACING,dir).with(PedestalBlock.WATERLOGGED,watered).with(PedestalBlock.LIT,lit).with(PedestalBlock.FILTER_STATUS,newFilterState);
                        world.notifyBlockUpdate(pos,state,newstate,3);
                        world.setBlockState(pos,newstate,3);
                        world.markBlockRangeForRenderUpdate(pos,state,newstate);

                        if(hasCoin())
                        {
                            if(getCoinOnPedestal().getItem() instanceof IUpgradeBase)
                            {
                                IUpgradeBase coin = ((IUpgradeBase)getCoinOnPedestal().getItem());
                                coin.setFilterChangeUpdate(getCoinOnPedestal());
                            }
                        }
                    }
                }
                return true;
            }
        }

        return false;
    }*/

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

    /*public boolean hasTorch()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(ph.getStackInSlot(7).isEmpty())
        {
            return false;
        }
        else  return true;
    }

    public ItemStack removeTorch() {
        IItemHandler ph = privateHandler.orElse(null);
        return ph.extractItem(7,ph.getStackInSlot(7).getCount(),false);
    }

    public boolean addTorch()
    {
        IItemHandler ph = privateHandler.orElse(null);

        ItemStack itemFromBlock = new ItemStack(Items.REDSTONE_TORCH);
        itemFromBlock.setCount(1);
        if(!hasTorch() && ph.isItemValid(7,itemFromBlock))
        {
            ph.insertItem(7,itemFromBlock,false);
            return true;
        }
        else return false;
    }

    public boolean isPedestalBlockPowered(World world,BlockPos pos)
    {
        boolean returner = world.isBlockPowered(pos);
        if(hasTorch())
        {
            return !returner;
        }

        return returner;
    }

    public boolean isPowered() {
        return this.getLevel().hasNeighborSignal(this.getBlockPos());
    }

    public int getRedstonePower() {
        return this.getLevel().getBestNeighborSignal(this.getBlockPos());
    }*/

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

    /*public boolean addRRobin(ItemStack roundRobin)
    {
        IItemHandler ph = privateHandler.orElse(null);
        ItemStack itemFromBlock = roundRobin.copy();
        itemFromBlock.setCount(1);
        if(!hasRRobin())
        {
            ph.insertItem(8,itemFromBlock,false);
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
            return ph.extractItem(8,ph.getStackInSlot(8).getCount(),false);
        }
        else return ItemStack.EMPTY;
    }

    public boolean hasRRobin()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(ph.getStackInSlot(8).isEmpty())
        {
            return false;
        }
        else  return true;
    }*/

    /*============================================================================
    ==============================================================================
    ===========================      Robin END      ==============================
    ==============================================================================
    ============================================================================*/



    /*============================================================================
    ==============================================================================
    ===========================   PARTICLE START    ==============================
    ==============================================================================
    ============================================================================*/

    /*public ItemStack addParticleDiffuser(ItemStack particle)
    {
        IItemHandler ph = privateHandler.orElse(null);
        ItemStack itemFromBlock = particle.copy();
        itemFromBlock.setCount(1);
        if(!hasParticleDiffuser())
        {
            //update();
            return ph.insertItem(10,itemFromBlock,false);
        }
        else return ItemStack.EMPTY;
    }

    public ItemStack removeParticleDiffuser()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(hasParticleDiffuser())
        {
            //update();
            return ph.extractItem(10,ph.getStackInSlot(10).getCount(),false);
        }
        else return ItemStack.EMPTY;
    }

    public boolean hasParticleDiffuser()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(ph.getStackInSlot(10).isEmpty())
        {
            return false;
        }
        else  return true;
    }*/

    /*============================================================================
    ==============================================================================
    ===========================    PARTICLE END     ==============================
    ==============================================================================
    ============================================================================*/



    /*============================================================================
    ==============================================================================
    ============================   RENDER START    ===============================
    ==============================================================================
    ============================================================================*/

    /*public ItemStack addRenderAugment(ItemStack particle)
    {
        IItemHandler ph = privateHandler.orElse(null);
        ItemStack itemFromBlock = particle.copy();
        itemFromBlock.setCount(1);
        if(!hasRenderAugment())
        {
            //update();
            return ph.insertItem(11,itemFromBlock,false);
        }
        else return ItemStack.EMPTY;
    }

    public ItemStack removeRenderAugment()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(hasRenderAugment())
        {
            //update();
            return ph.extractItem(11,ph.getStackInSlot(11).getCount(),false);
        }
        else return ItemStack.EMPTY;
    }

    public boolean hasRenderAugment()
    {
        IItemHandler ph = privateHandler.orElse(null);
        if(ph.getStackInSlot(11).isEmpty())
        {
            return false;
        }
        else  return true;
    }

    public int getRenderAugmentType()
    {
        // 0 = BOTH
        // 1 = UPGRADE
        // 2 = ITEM
        // 3 = NORMAL RENDER
        IItemHandler ph = privateHandler.orElse(null);
        if(hasRenderAugment())
        {
            if(ph.getStackInSlot(11).getItem() instanceof ItemPedestalRenderAugment)
            {
                ItemPedestalRenderAugment augment = ((ItemPedestalRenderAugment)ph.getStackInSlot(11).getItem());
                return augment.getAugmentType(ph.getStackInSlot(11));
            }
            else  return 0;
        }
        else  return 3;
    }*/

    /*============================================================================
    ==============================================================================
    ============================    RENDER END     ===============================
    ==============================================================================
    ============================================================================*/



    /*public boolean canSendItemInPedestal(PedestalTileEntity pedestal)
    {
        if(pedestal.hasItem())
        {
            if(hasCoin())
            {
                Item coin = getCoinOnPedestal().getItem();
                if(coin instanceof IUpgradeBase)
                {
                    return ((IUpgradeBase)coin).canSendItem(pedestal);
                }
            }
            else return true;
        }

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
        if(world.getBlockState(pedestalToBeLinked).getBlock() instanceof PedestalBlock)
        {
            //isPedestalInRange(tileCurrent,pedestalToBeLinked);
            return true;
        }

        return false;
    }

    //Returns items available to be insert, 0 if false
    public int canAcceptItems(World worldIn, BlockPos posPedestal, ItemStack itemsIncoming)
    {
        int canAccept = 0;
        int pedestalAccept = 0;
        boolean isTank = false;

        if(hasCoin())
        {
            Item coinInPed = this.getCoinOnPedestal().getItem();
            if(coinInPed instanceof IUpgradeBase)
            {
                pedestalAccept = ((IUpgradeBase) coinInPed).canAcceptCount(worldIn, posPedestal, getItemInPedestal(), itemsIncoming);
            }

            if(coinInPed instanceof ItemUpgradeItemTank)
            {
                if(((ItemUpgradeItemTank) coinInPed).availableStorageSpace(this) > 0)isTank = true;
            }
        }

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
                if(itemsIncoming.getMaxStackSize() > 1 || isTank)
                {
                    //If i did this right, slot limit should default to stack max size, or custom allowed
                    int allowed = getSlotSizeLimit();
                    if(allowed> itemsIncoming.getMaxStackSize() && !isTank)allowed=itemsIncoming.getMaxStackSize();
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
            if(filterInPed instanceof IFilterBase)
            {
                pedestalAccept = ((IFilterBase) filterInPed).canAcceptCount(getTile(), itemsIncoming);
            }
        }

        if((canAccept > pedestalAccept) && (hasFilter() || hasCoin()))
        {
            canAccept = pedestalAccept;
        }

        return canAccept;
    }

    public boolean hasFilter(PedestalTileEntity pedestalSendingTo)
    {
        boolean returner = false;
        if(pedestalSendingTo.hasFilter())
        {
            Item filterInPedestal = pedestalSendingTo.getFilterInPedestal().getItem();
            if(filterInPedestal instanceof IFilterBase)
            {
                returner = true;
            }
        }

        return returner;
    }

    //Checks to see if we can send an item from the pedestal, like in the case of the import energy/fluid upgrades
    public Boolean canSendItemInPedestal()
    {
        boolean returner = true;

        if(hasCoin())
        {
            Item coinInPedestal = getCoinOnPedestal().getItem();
            if(coinInPedestal instanceof IUpgradeBase)
            {
                return ((IUpgradeBase) coinInPedestal).canSendItem(this);
            }
        }

        return returner;
    }

    public static LazyOptional<IItemHandler> findItemHandlerPedestal(PedestalTileEntity pedestal)
    {
        World world = pedestal.getWorld();
        BlockPos pos = pedestal.getPos();
        TileEntity neighbourTile = world.getTileEntity(pos);
        if(neighbourTile!=null)
        {
            LazyOptional<IItemHandler> cap = neighbourTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
            if(cap.isPresent())
                return cap;
        }
        return LazyOptional.empty();
    }

    //Needed for filtered imports
    public boolean canSendToPedestal(BlockPos pedestalToSendTo, ItemStack itemStackIncoming)
    {
        boolean returner = false;

        //Method to check if we can send items FROM this pedestal???
        if(canSendItemInPedestal())
        {
            //Check if Block is Loaded in World
            if(world.isAreaLoaded(pedestalToSendTo,1))
            {
                //If block ISNT powered
                if(!world.isBlockPowered(pedestalToSendTo))
                {
                    //Make sure its a pedestal before getting the tile
                    if(world.getBlockState(pedestalToSendTo).getBlock() instanceof PedestalBlock)
                    {
                        //Make sure it is still part of the right network
                        if(canLinkToPedestalNetwork(pedestalToSendTo))
                        {
                            //Get the tile before checking other things
                            if(world.getTileEntity(pedestalToSendTo) instanceof PedestalTileEntity)
                            {
                                PedestalTileEntity tilePedestalToSendTo = (PedestalTileEntity)world.getTileEntity(pedestalToSendTo);

                                //Checks if pedestal is empty or if not then checks if items match and how many can be insert
                                if(tilePedestalToSendTo.canAcceptItems(world,pedestalToSendTo,itemStackIncoming) > 0)
                                {
                                    boolean filter = true;
                                    boolean coin = true;
                                    //Check if it has filter, if not return true
                                    if(tilePedestalToSendTo.hasFilter())
                                    {
                                        Item filterInPedestal = tilePedestalToSendTo.getFilterInPedestal().getItem();
                                        if(filterInPedestal instanceof IFilterBase)
                                        {
                                            filter = ((IFilterBase) filterInPedestal).canAcceptItem(tilePedestalToSendTo,itemStackIncoming);
                                        }
                                    }

                                    if(tilePedestalToSendTo.hasCoin())
                                    {
                                        Item coinInPedestal = tilePedestalToSendTo.getCoinOnPedestal().getItem();
                                        if(coinInPedestal instanceof IUpgradeBase)
                                        {
                                            coin = ((IUpgradeBase) coinInPedestal).canAcceptItem(getWorld(),pedestalToSendTo,itemStackIncoming);
                                        }
                                    }

                                    //Should return true by default, or fals eif a filter or coin blocks it???
                                    returner = filter && coin;
                                }
                            }
                        }
                    }
                    else
                    {
                        removeLocation(pedestalToSendTo);
                    }
                }
            }
        }

        return returner;
    }




    //The actual transfer methods for items
    public void sendItemsToPedestal(BlockPos pedestalToSendTo)
    {
        if(world.getTileEntity(pedestalToSendTo) instanceof PedestalTileEntity)
        {
            PedestalTileEntity tileToSendTo = ((PedestalTileEntity)world.getTileEntity(pedestalToSendTo));

            //Max that can be recieved
            int countToSend = tileToSendTo.canAcceptItems(world,pedestalToSendTo,getItemInPedestal());
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
                copyStackToSend.setCount(countToSend);
                removeItem(copyStackToSend.getCount());
                tileToSendTo.addItem(copyStackToSend);
                if(!hasParticleDiffuser())PacketHandler.sendToNearby(world,pos,new PacketParticles(PacketParticles.EffectType.ANY_COLOR_BEAM,pedestalToSendTo.getX(),pedestalToSendTo.getY(),pedestalToSendTo.getZ(),pos.getX(),pos.getY(),pos.getZ()));
            }
        }
    }

    public void transferActionItems()
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
                if(canSendToPedestal(posReceiver,getItemInPedestal()))
                {
                    sendItemsToPedestal(posReceiver);
                }

                robinCount++;
                setStoredValueForUpgrades(robinCount);
            }
            else
            {
                for(int i=0;i<locations;i++){
                    BlockPos posReceiver = getStoredPositionAt(i);
                    if(canSendToPedestal(posReceiver,getItemInPedestal()))
                    {
                        sendItemsToPedestal(posReceiver);
                        break;
                    }
                    else continue;
                }
            }
        }
    }*/






    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BasePedestalEntity e) {
        e.tick();
    }

    public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, BasePedestalEntity e) {
        e.tick();
    }

    int partTicker = 0;
    int impTicker = 0;
    int pedTicker = 0;

    public void tick() {

        /*if(!level.isClientSide() && level.isAreaLoaded(worldPosition,1))
        {
            if(getNumberOfStoredLocations() >0 && !isPedestalBlockPowered(getWorld(),getPos()) && hasItem())
            {
                pedTicker++;
                if (pedTicker%getOperationSpeed() == 0) {

                    transferActionItems();
                    //Eventually have Energy, Fluids, XP in here too???

                    if(pedTicker >=20){pedTicker=0;}
                }
            }
        }
        if(level.isAreaLoaded(worldPosition,1))
        {
            if(hasCoin())
            {
                Item coinInPed = getCoinOnPedestal().getItem();
                if(coinInPed instanceof IUpgradeBase)
                {
                    impTicker++;
                    ((IUpgradeBase) coinInPed).updateAction(world,this);
                    //Has to be bigger than our biggest ticker value for an upgrade, or itll reset the upgrade instance before the upgrade action can fire
                    if(impTicker >=Integer.MAX_VALUE-100){impTicker=0;}
                }
            }
        }*/
    }

    @Override
    public CompoundTag getUpdateTag() {
        //thanks http://www.minecraftforge.net/forum/index.php?topic=39162.0
        CompoundTag syncData = new CompoundTag();
        this.save(syncData); //this calls writeInternal
        int tileEntityType = 42;
        return syncData;
    }

    @Override
    public void onDataPacket(net.minecraft.network.Connection net, net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
        BlockState state = this.level.getBlockState(this.worldPosition);
        this.handleUpdateTag(pkt.getTag());
        this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
        super.onDataPacket(net, pkt);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 1, getUpdateTag());
    }

    @Override
    public void load(CompoundTag tag) {

        super.load(tag);
        handler.ifPresent(h -> {
            CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            tag.put("inv", compound);
        });

        privateHandler.ifPresent(ph -> {
            CompoundTag compound = ((INBTSerializable<CompoundTag>) ph).serializeNBT();
            tag.put("invp", compound);
        });

        tag.putBoolean("boollight", boolLight);

        List<Integer> storedX = new ArrayList<Integer>();
        List<Integer> storedY = new ArrayList<Integer>();
        List<Integer> storedZ = new ArrayList<Integer>();

        for(int i=0;i<getNumberOfStoredLocations();i++)
        {
            storedX.add(storedLocations.get(i).getX());
            storedY.add(storedLocations.get(i).getY());
            storedZ.add(storedLocations.get(i).getZ());
        }

        tag.putIntArray("intArrayXPos",storedX);
        tag.putIntArray("intArrayYPos",storedY);
        tag.putIntArray("intArrayZPos",storedZ);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {

        CompoundTag invTag = tag.getCompound("inv");
        CompoundTag invTagP = tag.getCompound("invp");
        handler.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(invTag));
        privateHandler.ifPresent(ph -> ((INBTSerializable<CompoundTag>) ph).deserializeNBT(invTagP));

        this.boolLight = tag.getBoolean("boollight");

        int[] storedIX = tag.getIntArray("intArrayXPos");
        int[] storedIY = tag.getIntArray("intArrayYPos");
        int[] storedIZ = tag.getIntArray("intArrayZPos");

        for(int i=0;i<storedIX.length;i++)
        {
            BlockPos gotPos = new BlockPos(storedIX[i],storedIY[i],storedIZ[i]);
            storedLocations.add(gotPos);
        }
        return super.save(tag);
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
