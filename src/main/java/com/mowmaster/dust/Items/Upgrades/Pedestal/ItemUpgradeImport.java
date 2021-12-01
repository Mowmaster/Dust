package com.mowmaster.dust.Items.Upgrades.Pedestal;

import com.mowmaster.dust.Block.Pedestal.BasePedestalBlockEntity;
import com.mowmaster.dust.Items.Filters.FilterRestricted;
import com.mowmaster.dust.Networking.DustPacketHandler;
import com.mowmaster.dust.Networking.DustPacketParticles;
import com.mowmaster.dust.Util.PedestalUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;
import java.util.stream.IntStream;

import static com.mowmaster.dust.References.Constants.MODID;
import static com.mowmaster.dust.Util.PedestalUtilities.findFluidHandlerAtPos;

public class ItemUpgradeImport extends ItemUpgradeBase
{
    public ItemUpgradeImport(Properties p_41383_) {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        Level world = p_41432_;
        Player player = p_41433_;
        InteractionHand hand = p_41434_;
        ItemStack stackInHand = player.getItemInHand(hand);
        //Build Color List from NBT

        HitResult result = player.pick(5,0,false);
        if(player.isCrouching())
        {
            if(result.getType().equals(HitResult.Type.MISS))
            {
                int mode = getUpgradeMode(stackInHand)+1;
                int setNewMode = (mode<=14)?(mode):(0);
                saveModeToNBT(stackInHand,setNewMode);
                player.setItemInHand(p_41434_,stackInHand);
            }
        }

        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public void updateAction(Level world, BasePedestalBlockEntity pedestal) {

        upgradeAction(pedestal, world,pedestal.getPos(),pedestal.getCoinOnPedestal());
        /*if (world.getGameTime()%20 == 0) {

        }*/
    }

    public void upgradeAction(BasePedestalBlockEntity pedestal, Level world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {
        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);

        if(canTransferItems(coinInPedestal))
        {
            int transferRate = getItemTransferRate(coinInPedestal);

            ItemStack itemFromInv = ItemStack.EMPTY;
            LazyOptional<IItemHandler> cap = PedestalUtilities.findItemHandlerAtPos(world,posInventory,getPedestalFacing(world, posOfPedestal),true);
            if(!isInventoryEmpty(cap))
            {
                if(cap.isPresent())
                {
                    IItemHandler handler = cap.orElse(null);
                    BlockEntity invToPullFrom = world.getBlockEntity(posInventory);
                    if(invToPullFrom instanceof BasePedestalBlockEntity) {
                        itemFromInv = ItemStack.EMPTY;

                    }
                    else {
                        if(handler != null)
                        {
                            int i = getNextSlotWithItemsCapFiltered(pedestal,cap,pedestal.getItemInPedestal());
                            if(i>=0)
                            {
                                int maxStackSizeAllowedInPedestal = 0;
                                int roomLeftInPedestal = 0;
                                itemFromInv = handler.getStackInSlot(i);
                                ItemStack itemFromPedestal = pedestal.getItemInPedestal();
                                //if there IS a valid item in the inventory to pull out
                                if(itemFromInv != null && !itemFromInv.isEmpty() && itemFromInv.getItem() != Items.AIR)
                                {
                                    //If pedestal is empty, if not then set max possible stack size for pedestal itemstack(64)
                                    if(itemFromPedestal.isEmpty() || itemFromPedestal.equals(ItemStack.EMPTY))
                                    {maxStackSizeAllowedInPedestal = 64;}
                                    else
                                    {maxStackSizeAllowedInPedestal = itemFromPedestal.getMaxStackSize();}
                                    //Get Room left in pedestal
                                    roomLeftInPedestal = maxStackSizeAllowedInPedestal-itemFromPedestal.getCount();
                                    //Get items stack count(from inventory)
                                    int itemCountInInv = itemFromInv.getCount();
                                    //Allowed transfer rate (from coin)
                                    int allowedTransferRate = transferRate;
                                    //Checks to see if pedestal can accept as many items as transferRate IF NOT it sets the new rate to what it can accept
                                    if(roomLeftInPedestal < transferRate) allowedTransferRate = roomLeftInPedestal;
                                    //Checks to see how many items are left in the slot IF ITS UNDER the allowedTransferRate then sent the max rate to that.
                                    if(itemCountInInv < allowedTransferRate) allowedTransferRate = itemCountInInv;

                                    //if(itemFromInv.maxStackSize() < allowedTransferRate) allowedTransferRate = itemFromInv.maxStackSize();

                                    ItemStack copyIncoming = itemFromInv.copy();
                                    copyIncoming.setCount(allowedTransferRate);
                                    BlockEntity pedestalInv = world.getBlockEntity(posOfPedestal);
                                    if(pedestalInv instanceof BasePedestalBlockEntity) {
                                        if(!handler.extractItem(i,allowedTransferRate ,true ).isEmpty() && ((BasePedestalBlockEntity) pedestalInv).addItem(copyIncoming, true))
                                        {
                                            handler.extractItem(i,allowedTransferRate ,false );
                                            ((BasePedestalBlockEntity) pedestalInv).addItem(copyIncoming, false);
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //Fluids
        if(canTransferFluids(coinInPedestal))
        {
            LazyOptional<IFluidHandler> cap = findFluidHandlerAtPos(world,posInventory,getPedestalFacing(world, posOfPedestal),true);

            BlockEntity invToPushTo = world.getBlockEntity(posInventory);
            if(cap.isPresent())
            {
                IFluidHandler handler = cap.orElse(null);
                if(handler != null)
                {
                    int tanks = handler.getTanks();
                    FluidStack fluidCheckedMatching = FluidStack.EMPTY;
                    fluidCheckedMatching = IntStream.range(0,tanks)//Int Range
                            .mapToObj((handler)::getFluidInTank)//Function being applied to each interval
                            .filter(fluidStack -> !fluidStack.isEmpty())
                            .findFirst().orElse(FluidStack.EMPTY);

                    if(!fluidCheckedMatching.isEmpty())
                    {
                        FluidStack fluidInPedestal = pedestal.getStoredFluid();
                        int spaceInCoin = pedestal.getFluidCapacity()-fluidInPedestal.getAmount();
                        if(tanks > 1)
                        {
                            if(!fluidInPedestal.isEmpty())
                            {
                                //Default grab from first tank
                                FluidStack fluidInTank = handler.getFluidInTank(0);
                                int amountIn = fluidInTank.getAmount();
                                int rate = getFluidTransferRate(coinInPedestal);
                                int actualCoinRate = (spaceInCoin>=rate)?(rate):(spaceInCoin);
                                int transferRate = (amountIn>=actualCoinRate)?(actualCoinRate):(amountIn);

                                if(spaceInCoin >= transferRate || getFluidStored(coinInPedestal).isEmpty())
                                {
                                    FluidStack estFluidToDrain = new FluidStack(fluidInTank,transferRate);
                                    FluidStack fluidToActuallyDrain = handler.drain(estFluidToDrain,IFluidHandler.FluidAction.SIMULATE);
                                    int amountCanAddToPedestal = pedestal.addFluid(fluidToActuallyDrain,IFluidHandler.FluidAction.SIMULATE);
                                    if(!fluidInTank.isEmpty() && amountCanAddToPedestal>0)
                                    {
                                        FluidStack fluidDrained = handler.drain(amountCanAddToPedestal,IFluidHandler.FluidAction.EXECUTE);
                                        pedestal.addFluid(fluidDrained,IFluidHandler.FluidAction.EXECUTE);
                                    }
                                }
                            }
                            else
                            {
                                FluidStack fluidMatching = FluidStack.EMPTY;
                                fluidMatching = IntStream.range(0,tanks)//Int Range
                                        .mapToObj((handler)::getFluidInTank)//Function being applied to each interval
                                        .filter(fluidStack -> fluidInPedestal.isFluidEqual(fluidStack))
                                        .findFirst().orElse(FluidStack.EMPTY);

                                if(!fluidMatching.isEmpty())
                                {
                                    int amountIn = fluidMatching.getAmount();
                                    int rate = getFluidTransferRate(coinInPedestal);
                                    int actualCoinRate = (spaceInCoin>=rate)?(rate):(spaceInCoin);
                                    int transferRate = (amountIn>=actualCoinRate)?(actualCoinRate):(amountIn);

                                    if(spaceInCoin >= transferRate || getFluidStored(coinInPedestal).isEmpty())
                                    {
                                        FluidStack estFluidToDrain = new FluidStack(fluidMatching,transferRate);
                                        FluidStack fluidToActuallyDrain = handler.drain(estFluidToDrain,IFluidHandler.FluidAction.SIMULATE);
                                        int amountCanAddToPedestal = pedestal.addFluid(fluidToActuallyDrain,IFluidHandler.FluidAction.SIMULATE);
                                        if(!fluidMatching.isEmpty() && amountCanAddToPedestal>0)
                                        {
                                            FluidStack fluidDrained = handler.drain(amountCanAddToPedestal,IFluidHandler.FluidAction.EXECUTE);
                                            pedestal.addFluid(fluidDrained,IFluidHandler.FluidAction.EXECUTE);
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            //should i just set this to zero???
                            FluidStack fluidInTank = handler.getFluidInTank(tanks-1);
                            if(fluidInPedestal.isEmpty() || fluidInPedestal.isFluidEqual(fluidInTank))
                            {
                                int amountIn = fluidInTank.getAmount();
                                int rate = getFluidTransferRate(coinInPedestal);
                                int actualCoinRate = (spaceInCoin>=rate)?(rate):(spaceInCoin);
                                int transferRate = (amountIn>=actualCoinRate)?(actualCoinRate):(amountIn);

                                if(spaceInCoin >= transferRate || getFluidStored(coinInPedestal).isEmpty())
                                {
                                    FluidStack estFluidToDrain = new FluidStack(fluidInTank,transferRate);
                                    FluidStack fluidToActuallyDrain = handler.drain(estFluidToDrain,IFluidHandler.FluidAction.SIMULATE);
                                    int amountCanAddToPedestal = pedestal.addFluid(fluidToActuallyDrain,IFluidHandler.FluidAction.SIMULATE);
                                    if(!fluidInTank.isEmpty() && amountCanAddToPedestal>0)
                                    {
                                        FluidStack fluidDrained = handler.drain(amountCanAddToPedestal,IFluidHandler.FluidAction.EXECUTE);
                                        pedestal.addFluid(fluidDrained,IFluidHandler.FluidAction.EXECUTE);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //Energy
        if(canTransferEnergy(coinInPedestal))
        {

        }
        //XP
        if(canTransferXP(coinInPedestal))
        {

        }
    }


    @Override
    public void actionOnCollideWithBlock(BasePedestalBlockEntity pedestal, Entity entityIn) {
        if(canTransferItems(pedestal.getCoinOnPedestal()))
        {
            if(entityIn instanceof ItemEntity)
            {
                ItemEntity itemEntity = ((ItemEntity) entityIn);
                ItemStack itemStack = itemEntity.getItem();
                ItemStack stackInPedestal = pedestal.getItemInPedestal();
                boolean stacksMatch = doItemsMatch(stackInPedestal,itemStack);
                if((!pedestal.hasItem() || stacksMatch) && passesItemFilter(pedestal,itemStack))
                {
                    int spaceInPed = stackInPedestal.getMaxStackSize()-stackInPedestal.getCount();
                    int filterAllowedSpace = getCountItemFilter(pedestal,itemStack);
                    int actualSpaceInPed = (filterAllowedSpace>spaceInPed)?(spaceInPed):(filterAllowedSpace);
                    if(actualSpaceInPed>0)
                    {
                        int itemInCount = itemStack.getCount();
                        int countToAdd = ( itemInCount<= actualSpaceInPed)?(itemInCount):(actualSpaceInPed);
                        ItemStack stackToAdd = itemStack.copy();
                        stackToAdd.setCount(countToAdd);
                        if(pedestal.addItem(stackToAdd,true))
                        {
                            itemEntity.getItem().setCount(itemInCount-countToAdd);
                            if(itemInCount<=countToAdd)itemEntity.remove(Entity.RemovalReason.DISCARDED);
                            pedestal.addItem(stackToAdd,false);
                            if(pedestal.canSpawnParticles()) DustPacketHandler.sendToNearby(pedestal.getLevel(),pedestal.getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,pedestal.getPos().getX(),pedestal.getPos().getY(),pedestal.getPos().getZ(),180,180,180));
                        }
                    }
                }
            }
            else if (entityIn instanceof Player)
            {
                Player player = ((Player) entityIn);
                ItemStack itemFromInv = ItemStack.EMPTY;

                itemFromInv = IntStream.range(0,(player.getInventory().items.size()))//Int Range
                        .mapToObj((player.getInventory().items)::get)//Function being applied to each interval
                        .filter(itemStack -> !itemStack.isEmpty())
                        .filter(itemStack -> passesItemFilter(pedestal,itemStack))
                        .findFirst().orElse(ItemStack.EMPTY);

                if(!itemFromInv.isEmpty())
                {
                    ItemStack itemStack = itemFromInv;
                    ItemStack stackInPedestal = pedestal.getItemInPedestal();
                    boolean stacksMatch = doItemsMatch(stackInPedestal,itemStack);
                    if((!pedestal.hasItem() || stacksMatch) && passesItemFilter(pedestal,itemStack))
                    {
                        int spaceInPed = stackInPedestal.getMaxStackSize()-stackInPedestal.getCount();
                        int filterAllowedSpace = getCountItemFilter(pedestal,itemStack);
                        int actualSpaceInPed = (filterAllowedSpace>spaceInPed)?(spaceInPed):(filterAllowedSpace);
                        if(actualSpaceInPed>0)
                        {
                            int itemInCount = itemStack.getCount();
                            int countToAdd = ( itemInCount<= actualSpaceInPed)?(itemInCount):(actualSpaceInPed);
                            ItemStack stackToAdd = itemStack.copy();
                            stackToAdd.setCount(countToAdd);
                            if(pedestal.addItem(stackToAdd,true))
                            {
                                ItemStack newStackInPlayer = (itemInCount>countToAdd)?(itemStack.copy()):(ItemStack.EMPTY);
                                if(!newStackInPlayer.isEmpty())newStackInPlayer.setCount(itemInCount-countToAdd);
                                int slot = player.getInventory().findSlotMatchingItem(itemStack);
                                player.getInventory().setItem(slot,newStackInPlayer);
                                pedestal.addItem(stackToAdd,false);
                                if(pedestal.canSpawnParticles()) DustPacketHandler.sendToNearby(pedestal.getLevel(),pedestal.getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,pedestal.getPos().getX(),pedestal.getPos().getY(),pedestal.getPos().getZ(),180,180,0));
                            }
                        }
                    }
                }
            }
        }
    }
}
