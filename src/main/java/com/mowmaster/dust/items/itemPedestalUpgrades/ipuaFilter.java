package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.enums.FilterTypes;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import static net.minecraft.block.BlockDirectional.FACING;

public class ipuaFilter extends ipuBasic {

    public ipuaFilter()
    {

    }

    public Boolean areFilteredItemsEqual(TilePedestal recievingPedestal, ItemStack itemStackIn, FilterTypes enumType)
    {
        BlockPos filterInv = recievingPedestal.getPosOfBlockBelow(1);
        if(world.getTileEntity(filterInv).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))
        {
            TileEntity tile = world.getTileEntity(filterInv);
            ItemStack stackInFilter = ItemStack.EMPTY;

            for(int i=0;i<tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots();i++)
            {
                if(!tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i).equals(ItemStack.EMPTY))
                {
                    stackInFilter = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);

                    if(enumType == FilterTypes.FUZZY)
                    {
                        if(itemStackIn.getItem().equals(stackInFilter.getItem()))
                        {
                            return true;
                        }
                    }
                    if(enumType == FilterTypes.MOD)
                    {
                        if(itemStackIn.getItem().getRegistryName().getResourceDomain()==stackInFilter.getItem().getRegistryName().getResourceDomain())
                        {
                            return true;
                        }
                    }
                    if(enumType == FilterTypes.NORMAL)
                    {
                        if(itemStackIn.getHasSubtypes())
                        {
                            if(itemStackIn.getItem().equals(stackInFilter.getItem()) && itemStackIn.getMetadata()==stackInFilter.getMetadata())
                            {
                                return true;
                            }
                        }
                        else if(itemStackIn.hasTagCompound())
                        {
                            NBTTagCompound itemIn = itemStackIn.getTagCompound();
                            NBTTagCompound itemStored = stackInFilter.getTagCompound();
                            if(itemIn.equals(itemStored) && itemStackIn.getItem().equals(stackInFilter.getItem()))
                            {
                                return true;
                            }
                        }
                        else
                        {
                            if(itemStackIn.getItem().equals(stackInFilter.getItem()))
                            {
                                return true;
                            }
                        }
                    }

                    else return false;
                }
            }
        }

        return false;
    }


    public boolean hasFilterableInventoryBelow()
    {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        EnumFacing enumfacing = state.getValue(FACING);

        switch (enumfacing)
        {
            case UP:
                if(isBlockUnder(0,-1,0)) {return true;}
                return false;
            case DOWN:
                if(isBlockUnder(0,1,0)) {return true;}
                return false;
            case NORTH:
                if(isBlockUnder(0,0,1)) {return true;}
                return false;
            case SOUTH:
                if(isBlockUnder(0,0,-1)) {return true;}
                return false;
            case EAST:
                if(isBlockUnder(-1,0,0)) {return true;}
                return false;
            case WEST:
                if(isBlockUnder(1,0,0)) {return true;}
                return false;
        }
        return false;
    }

    public void upgradeAction(World world, BlockPos posOfPedestal, int summonRange, int summonCount)
    {
        //Range comes from enchant
        //ammount comes from...speed?

        if(!world.isRemote)
        {
            if(!world.isBlockPowered(posOfPedestal))
            {
                if(!getStackInPedestal(world,posOfPedestal).isEmpty())//hasItem
                {
                    ItemStack itemToSummon = getStackInPedestal(world,posOfPedestal).copy();
                    itemToSummon.setCount(summonCount);
                    EntityItem itemEntity = new EntityItem(world,getPosOfBlockBelow(world,posOfPedestal,-summonRange).getX() + 0.5,getPosOfBlockBelow(world,posOfPedestal,-summonRange).getY(),getPosOfBlockBelow(world,posOfPedestal,-summonRange).getZ() + 0.5,itemToSummon);
                    itemEntity.motionX = 0;
                    itemEntity.motionY = 0;
                    itemEntity.motionZ = 0;
                    world.spawnEntity(itemEntity);
                    this.removeFromPedestal(world,posOfPedestal,itemToSummon.getCount());
                }
            }
        }

    }

}
