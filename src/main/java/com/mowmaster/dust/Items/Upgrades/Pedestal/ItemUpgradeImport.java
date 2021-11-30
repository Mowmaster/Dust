package com.mowmaster.dust.Items.Upgrades.Pedestal;

import com.mowmaster.dust.Block.Pedestal.BasePedestalBlockEntity;
import com.mowmaster.dust.Util.PedestalUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

import static com.mowmaster.dust.References.Constants.MODID;

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

        if (world.getGameTime()%20 == 0) {
            upgradeAction(pedestal, world,pedestal.getPos(),pedestal.getCoinOnPedestal());
        }
    }

    public void upgradeAction(BasePedestalBlockEntity pedestal, Level world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {
        if(canTransferItems(coinInPedestal))
        {
            BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);
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
        if(canTransferFluids(coinInPedestal))
        {

        }
        if(canTransferEnergy(coinInPedestal))
        {

        }
        if(canTransferXP(coinInPedestal))
        {

        }
    }
}
