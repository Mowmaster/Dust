package com.mowmaster.dust.Items.Upgrades.Pedestal;

import com.mowmaster.dust.Block.Pedestal.BasePedestalBlockEntity;
import com.mowmaster.dust.Util.PedestalUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.stream.IntStream;

import static com.mowmaster.dust.Util.PedestalUtilities.findFluidHandlerAtPos;

public class ItemUpgradeExport extends ItemUpgradeBase
{
    public ItemUpgradeExport(Properties p_41383_) {
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

            ItemStack stackInPedestal = pedestal.getItemInPedestal();
            ItemStack itemFromInv = ItemStack.EMPTY;
            LazyOptional<IItemHandler> cap = PedestalUtilities.findItemHandlerAtPos(world,posInventory,getPedestalFacing(world, posOfPedestal),true);

            if(!stackInPedestal.isEmpty() && !stackInPedestal.equals(ItemStack.EMPTY))
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
                            //gets next empty or partially filled matching slot
                            int i = getNextSlotEmptyOrMatching(cap, stackInPedestal);
                            if(handler != null)
                            {
                                if(i>=0)
                                {
                                    if(handler.isItemValid(i, stackInPedestal))
                                    {
                                        stackInPedestal = pedestal.getItemInPedestal().copy();
                                        ItemStack itemFromInventory = handler.getStackInSlot(i);
                                        int spaceInInventoryStack = handler.getSlotLimit(i) - itemFromInventory.getCount();

                                        //if inv slot is empty it should be able to handle as much as we can give it
                                        int allowedTransferRate = transferRate;
                                        //checks allowed slot size amount and sets it if its lower then transfer rate
                                        if(handler.getSlotLimit(i) <= allowedTransferRate) allowedTransferRate = handler.getSlotLimit(i);
                                        //never have to check to see if pedestal and stack match because the slot checker does it for us
                                        //if our transfer rate is bigger then what can go in the slot if its partially full we set the transfer size to what can fit
                                        //Otherwise if space is bigger then rate we know it can accept as much as we're putting in
                                        if(allowedTransferRate> spaceInInventoryStack) allowedTransferRate = spaceInInventoryStack;
                                        //IF items in pedestal are less then the allowed transfer amount then set it as the amount
                                        if(allowedTransferRate > stackInPedestal.getCount()) allowedTransferRate = stackInPedestal.getCount();

                                        //After all calculations for transfer rate, set stack size to transfer and transfer the items
                                        stackInPedestal.setCount(allowedTransferRate);

                                        if(ItemHandlerHelper.insertItem(handler,stackInPedestal,true).equals(ItemStack.EMPTY)){
                                            pedestal.removeItem(allowedTransferRate);
                                            ItemHandlerHelper.insertItem(handler,stackInPedestal,false);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

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
                    FluidStack fluidInPedestal = pedestal.getStoredFluid();
                    if(tanks > 1)
                    {
                        FluidStack fluidCheckedMatching = FluidStack.EMPTY;
                        fluidCheckedMatching = IntStream.range(0,tanks)//Int Range
                                .mapToObj((handler)::getFluidInTank)//Function being applied to each interval
                                .filter(fluidStack -> fluidStack.isFluidEqual(fluidInPedestal))
                                .findFirst().orElse(FluidStack.EMPTY);

                        //There is a matching fluid in a tank to fill
                        if(!fluidCheckedMatching.isEmpty())
                        {
                            FluidStack matchedFluid = fluidCheckedMatching;
                            int value = 0;
                            for(int location=0;location<tanks;location++)
                            {
                                if(handler.getFluidInTank(location).isFluidEqual(matchedFluid)){
                                    value = location;
                                    break;
                                }
                            }

                            int getTankCapacity = handler.getTankCapacity(value);
                            int tankCurrentlyStored = matchedFluid.getAmount();
                            int spaceInTank = getTankCapacity-tankCurrentlyStored;
                            int amountInCoin = fluidInPedestal.getAmount();

                            int rate = getFluidTransferRate(coinInPedestal);
                            int actualCoinRate = (spaceInTank>=rate)?(rate):(spaceInTank);
                            int transferRate = (amountInCoin>=actualCoinRate)?(actualCoinRate):(amountInCoin);

                            if(spaceInTank >= transferRate)
                            {
                                FluidStack estFluidToFill = new FluidStack(fluidInPedestal.getFluid(),transferRate,fluidInPedestal.getTag());
                                int fluidToActuallyFill = handler.fill(estFluidToFill,IFluidHandler.FluidAction.SIMULATE);
                                FluidStack fluidToRemove = pedestal.removeFluid(fluidToActuallyFill, IFluidHandler.FluidAction.SIMULATE);
                                if(fluidToActuallyFill>0 && fluidToRemove.getAmount()>0)
                                {
                                    estFluidToFill = new FluidStack(fluidInPedestal.getFluid(),fluidToRemove.getAmount(),fluidInPedestal.getTag());
                                    int fluidDrained = handler.fill(estFluidToFill,IFluidHandler.FluidAction.EXECUTE);
                                    pedestal.removeFluid(fluidDrained,IFluidHandler.FluidAction.EXECUTE);
                                }
                            }
                        }
                        else
                        {
                            int value = 0;
                            for(int location=0;location<tanks;location++)
                            {
                                if(handler.getFluidInTank(location).isEmpty()){
                                    value = location;
                                    break;
                                }
                            }
                            FluidStack emptyTank = handler.getFluidInTank(value);
                            int getTankCapacity = handler.getTankCapacity(value);
                            int tankCurrentlyStored = emptyTank.getAmount();
                            int spaceInTank = getTankCapacity-tankCurrentlyStored;
                            int amountInCoin = fluidInPedestal.getAmount();

                            int rate = getFluidTransferRate(coinInPedestal);
                            int actualCoinRate = (spaceInTank>=rate)?(rate):(spaceInTank);
                            int transferRate = (amountInCoin>=actualCoinRate)?(actualCoinRate):(amountInCoin);

                            if(spaceInTank >= transferRate)
                            {
                                FluidStack estFluidToFill = new FluidStack(fluidInPedestal.getFluid(),transferRate,fluidInPedestal.getTag());
                                int fluidToActuallyFill = handler.fill(estFluidToFill,IFluidHandler.FluidAction.SIMULATE);
                                FluidStack fluidToRemove = pedestal.removeFluid(fluidToActuallyFill, IFluidHandler.FluidAction.SIMULATE);
                                if(fluidToActuallyFill>0 && fluidToRemove.getAmount()>0)
                                {
                                    estFluidToFill = new FluidStack(fluidInPedestal.getFluid(),fluidToRemove.getAmount(),fluidInPedestal.getTag());
                                    int fluidDrained = handler.fill(estFluidToFill,IFluidHandler.FluidAction.EXECUTE);
                                    pedestal.removeFluid(fluidDrained,IFluidHandler.FluidAction.EXECUTE);
                                }

                            }
                        }
                    }
                    else
                    {
                        //should i just set this to zero???
                        FluidStack fluidInTank = handler.getFluidInTank(tanks-1);
                        if(fluidInTank.isEmpty() || fluidInPedestal.isFluidEqual(fluidInTank))
                        {
                            int getTankCapacity = handler.getTankCapacity(tanks-1);
                            int tankCurrentlyStored = fluidInTank.getAmount();
                            int spaceInTank = getTankCapacity-tankCurrentlyStored;
                            int amountInCoin = fluidInPedestal.getAmount();

                            int rate = getFluidTransferRate(coinInPedestal);
                            int actualCoinRate = (spaceInTank>=rate)?(rate):(spaceInTank);
                            int transferRate = (amountInCoin>=actualCoinRate)?(actualCoinRate):(amountInCoin);
                            if(spaceInTank >= transferRate)
                            {
                                FluidStack estFluidToFill = new FluidStack(fluidInPedestal.getFluid(),transferRate,fluidInPedestal.getTag());
                                int fluidToActuallyFill = handler.fill(estFluidToFill,IFluidHandler.FluidAction.SIMULATE);
                                FluidStack fluidToRemove = pedestal.removeFluid(fluidToActuallyFill, IFluidHandler.FluidAction.SIMULATE);
                                if(fluidToActuallyFill>0 && fluidToRemove.getAmount()>0)
                                {
                                    estFluidToFill = new FluidStack(fluidInPedestal.getFluid(),fluidToRemove.getAmount(),fluidInPedestal.getTag());
                                    int fluidDrained = handler.fill(estFluidToFill,IFluidHandler.FluidAction.EXECUTE);
                                    pedestal.removeFluid(fluidDrained,IFluidHandler.FluidAction.EXECUTE);
                                }
                            }
                        }
                    }
                }
            }
        }
        if(canTransferEnergy(coinInPedestal))
        {

        }
        if(canTransferXP(coinInPedestal))
        {

        }
    }
}
