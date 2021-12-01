package com.mowmaster.dust.Items.Upgrades.Pedestal;

import com.mowmaster.dust.Block.Pedestal.BasePedestalBlockEntity;
import com.mowmaster.dust.CreativeTabs.DustItemTabs;
import com.mowmaster.dust.Items.Filters.IPedestalFilter;
import com.mowmaster.dust.Util.PedestalUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static com.mowmaster.dust.Block.Pedestal.BasePedestalBlock.FACING;
import static com.mowmaster.dust.References.Constants.MODID;


public class ItemUpgradeBase extends Item implements IPedestalUpgrade
{
    public ItemUpgradeBase(Properties p_41383_) {
        super(new Properties().tab(DustItemTabs.TAB_ITEMS));
    }

    @Override
    public int getComparatorRedstoneLevel(Level worldIn, BlockPos pos) {
        return PedestalUtilities.getRedstoneLevelPedestal(worldIn, pos);
    }

    @Override
    public void updateAction(Level world, BasePedestalBlockEntity pedestal) {

    }

    public static void saveModeToNBT(ItemStack augment, int mode)
    {
        CompoundTag compound = new CompoundTag();
        if(augment.hasTag())
        {
            compound = augment.getTag();
        }
        compound.putInt(MODID+"_upgrade_mode",mode);
        augment.setTag(compound);
    }

    public static int readModeFromNBT(ItemStack augment) {
        if(augment.hasTag())
        {
            CompoundTag getCompound = augment.getTag();
            return getCompound.getInt(MODID+"_upgrade_mode");
        }
        return 0;
    }

    public static int getUpgradeMode(ItemStack stack) {

        return readModeFromNBT(stack);
    }

    /*
     * 0 - Items
     * 1 - Fluids
     * 2 - Energy
     * 3 - XP
     * 4 - Items Fluids
     * 5 - Items Energy
     * 6 - Items XP
     * 7 - Items Fluids Energy
     * 8 - Items Fluids XP
     * 9 - Items Energy XP
     * 10 - Items Fluids Energy XP
     * 11 - Fluids Energy
     * 12 - Fluids XP
     * 13 - Energy XP
     * 14 - Fluids Energy XP
     */

    @Override
    public boolean canTransferItems(ItemStack upgrade)
    {
        switch(getUpgradeMode(upgrade))
        {
            case 0: return true;
            case 1: return false;
            case 2: return false;
            case 3: return false;
            case 4: return true;
            case 5: return true;
            case 6: return true;
            case 7: return true;
            case 8: return true;
            case 9: return true;
            case 10: return true;
            case 11: return false;
            case 12: return false;
            case 13: return false;
            case 14: return false;
            default: return true;
        }
    }

    @Override
    public boolean canTransferFluids(ItemStack upgrade)
    {
        switch(getUpgradeMode(upgrade))
        {
            case 0: return false;
            case 1: return true;
            case 2: return false;
            case 3: return false;
            case 4: return true;
            case 5: return false;
            case 6: return false;
            case 7: return true;
            case 8: return true;
            case 9: return false;
            case 10: return true;
            case 11: return true;
            case 12: return true;
            case 13: return false;
            case 14: return true;
            default: return true;
        }
    }

    @Override
    public boolean canTransferEnergy(ItemStack upgrade)
    {
        switch(getUpgradeMode(upgrade))
        {
            case 0: return false;
            case 1: return false;
            case 2: return true;
            case 3: return false;
            case 4: return false;
            case 5: return true;
            case 6: return false;
            case 7: return true;
            case 8: return false;
            case 9: return true;
            case 10: return true;
            case 11: return true;
            case 12: return false;
            case 13: return true;
            case 14: return true;
            default: return true;
        }
    }

    @Override
    public boolean canTransferXP(ItemStack upgrade)
    {
        switch(getUpgradeMode(upgrade))
        {
            case 0: return false;
            case 1: return false;
            case 2: return false;
            case 3: return true;
            case 4: return false;
            case 5: return false;
            case 6: return true;
            case 7: return false;
            case 8: return true;
            case 9: return true;
            case 10: return true;
            case 11: return false;
            case 12: return true;
            case 13: return true;
            case 14: return true;
            default: return true;
        }
    }

    public BlockPos getPosOfBlockBelow(Level world, BlockPos posOfPedestal, int numBelow)
    {
        BlockState state = world.getBlockState(posOfPedestal);

        Direction enumfacing = (state.hasProperty(FACING))?(state.getValue(FACING)):(Direction.UP);
        BlockPos blockBelow = posOfPedestal;
        switch (enumfacing)
        {
            case UP:
                return blockBelow.offset(0,-numBelow,0);
            case DOWN:
                return blockBelow.offset(0,numBelow,0);
            case NORTH:
                return blockBelow.offset(0,0,numBelow);
            case SOUTH:
                return blockBelow.offset(0,0,-numBelow);
            case EAST:
                return blockBelow.offset(-numBelow,0,0);
            case WEST:
                return blockBelow.offset(numBelow,0,0);
            default:
                return blockBelow;
        }
    }

    public Direction getPedestalFacing(Level world, BlockPos posOfPedestal)
    {
        BlockState state = world.getBlockState(posOfPedestal);
        return state.getValue(FACING);
    }

    public int getItemTransferRate(ItemStack stack)
    {
        int transferRate = 1;
        int speed = 1;
        switch (speed)
        {
            case 0:
                transferRate = 1;
                break;
            case 1:
                transferRate=4;
                break;
            case 2:
                transferRate = 8;
                break;
            case 3:
                transferRate = 16;
                break;
            case 4:
                transferRate = 32;
                break;
            case 5:
                transferRate=64;
                break;
            default: transferRate=1;
        }

        return  transferRate;
    }

    public int getFluidTransferRate(ItemStack stack)
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

    public boolean isInventoryEmpty(LazyOptional<IItemHandler> cap)
    {
        if(cap.isPresent())
        {
            IItemHandler handler = cap.orElse(null);
            if(handler != null)
            {
                int range = handler.getSlots();

                ItemStack itemFromInv = ItemStack.EMPTY;
                itemFromInv = IntStream.range(0,range)//Int Range
                        .mapToObj((handler)::getStackInSlot)//Function being applied to each interval
                        .filter(itemStack -> !itemStack.isEmpty())
                        .findFirst().orElse(ItemStack.EMPTY);

                if(!itemFromInv.isEmpty())
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean doItemsMatch(ItemStack stackPedestal, ItemStack itemStackIn)
    {
        return ItemHandlerHelper.canItemStacksStack(stackPedestal,itemStackIn);
    }

    public int getNextSlotWithItemsCapFiltered(BasePedestalBlockEntity pedestal, LazyOptional<IItemHandler> cap, ItemStack stackInPedestal)
    {
        AtomicInteger slot = new AtomicInteger(-1);
        if(cap.isPresent()) {

            cap.ifPresent(itemHandler -> {
                int range = itemHandler.getSlots();
                for(int i=0;i<range;i++)
                {
                    ItemStack stackInSlot = itemHandler.getStackInSlot(i);
                    //find a slot with items
                    if(!stackInSlot.isEmpty())
                    {
                        //check if it could pull the item out or not
                        if(!itemHandler.extractItem(i,1 ,true ).equals(ItemStack.EMPTY))
                        {
                            //If pedestal is empty accept any items
                            if(passesItemFilter(pedestal,stackInSlot))
                            {
                                if(stackInPedestal.isEmpty())
                                {
                                    slot.set(i);
                                    break;
                                }
                                //if stack in pedestal matches items in slot
                                else if(doItemsMatch(stackInPedestal,stackInSlot))
                                {
                                    slot.set(i);
                                    break;
                                }
                            }
                        }
                    }
                }});


        }

        return slot.get();
    }

    public int getNextSlotEmptyOrMatching(LazyOptional<IItemHandler> cap, ItemStack stackInPedestal)
    {
        AtomicInteger slot = new AtomicInteger(-1);
        if(cap.isPresent())
        {
            IItemHandler handler = cap.orElse(null);
            if(handler != null)
            {
                int range = handler.getSlots();
                for(int i=0;i<range;i++)
                {
                    ItemStack stackInSlot = handler.getStackInSlot(i);
                    int maxSizeSlot = handler.getSlotLimit(i);
                    if(maxSizeSlot>0)
                    {
                        if(stackInSlot.getMaxStackSize()>1)
                        {
                            if(doItemsMatch(stackInSlot,stackInPedestal) && stackInSlot.getCount() < handler.getSlotLimit(i))
                            {
                                slot.set(i);
                                break;
                            }
                            else if(stackInSlot.isEmpty())
                            {
                                slot.set(i);
                                break;
                            }
                            //if chest is full
                            else if(i==range)
                            {
                                slot.set(i);
                            }
                        }
                    }
                }
            }
        }
        return slot.get();
    }

    public FluidStack getFluidStored(ItemStack stack)
    {
        if(stack.hasTag())
        {
            CompoundTag getCompound = stack.getTag();
            return FluidStack.loadFluidStackFromNBT(getCompound);
        }

        return FluidStack.EMPTY;
    }

    public boolean passesItemFilter(BasePedestalBlockEntity pedestal, ItemStack stackIn)
    {
        boolean returner = true;
        if(pedestal.hasFilter())
        {
            Item filterInPedestal = pedestal.getFilterInPedestal().getItem();
            if(filterInPedestal instanceof IPedestalFilter)
            {
                returner = ((IPedestalFilter) filterInPedestal).canAcceptItem(pedestal,stackIn);
            }

        }

        return returner;
    }


}
