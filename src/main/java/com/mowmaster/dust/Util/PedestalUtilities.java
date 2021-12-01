package com.mowmaster.dust.Util;

import com.mowmaster.dust.Block.Pedestal.BasePedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.extensions.IForgeAbstractMinecart;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public class PedestalUtilities
{
    public static int getRedstoneLevelPedestal(Level worldIn, BlockPos pos)
    {
        int hasItem=0;
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if(blockEntity instanceof BasePedestalBlockEntity) {
            BasePedestalBlockEntity pedestal = ((BasePedestalBlockEntity) blockEntity);
            ItemStack itemstack = pedestal.getItemInPedestal();
            ItemStack coin = pedestal.getCoinOnPedestal();
            /*if(coin.getItem() instanceof IPedestalUpgrade)
            {
                return ((IPedestalUpgrade)coin.getItem()).getComparatorRedstoneLevel(worldIn,pos);
            }*/
            if(!itemstack.isEmpty())
            {
                float f = (float)itemstack.getCount()/(float)itemstack.getMaxStackSize();
                hasItem = (int)Math.floor(f*14.0F)+1;
            }
        }

        return hasItem;
    }

    public static LazyOptional<IItemHandler> findItemHandlerAtPos(Level world, BlockPos pos, boolean allowCart)
    {
        BlockEntity neighbourTile = world.getBlockEntity(pos);
        if(neighbourTile!=null)
        {
            LazyOptional<IItemHandler> cap = neighbourTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
            if(cap.isPresent())
                return cap;
        }
        if(allowCart)
        {
            if(RailBlock.isRail(world, pos))
            {
                List<Entity> list = world.getEntitiesOfClass(null, new AABB(pos), entity -> entity instanceof IForgeAbstractMinecart);
                if(!list.isEmpty())
                {
                    LazyOptional<IItemHandler> cap = list.get(world.random.nextInt(list.size())).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                    if(cap.isPresent())
                        return cap;
                }
            }
            else
            {
                //Added for quark boats with inventories (i hope)
                List<Entity> list = world.getEntitiesOfClass(null, new AABB(pos), entity -> entity instanceof Boat);
                if(!list.isEmpty())
                {
                    LazyOptional<IItemHandler> cap = list.get(world.random.nextInt(list.size())).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                    if(cap.isPresent())
                        return cap;
                }
            }
        }
        return LazyOptional.empty();
    }

    public static LazyOptional<IItemHandler> findItemHandlerAtPos(Level world, BlockPos pos, Direction side, boolean allowCart)
    {
        BlockEntity neighbourTile = world.getBlockEntity(pos);
        if(neighbourTile!=null)
        {
            LazyOptional<IItemHandler> cap = neighbourTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
            if(cap.isPresent())
                return cap;
        }
        if(allowCart)
        {
            if(RailBlock.isRail(world, pos))
            {
                List<Entity> list = world.getEntitiesOfClass(Entity.class, new AABB(pos), entity -> entity instanceof IForgeAbstractMinecart);
                if(!list.isEmpty())
                {
                    LazyOptional<IItemHandler> cap = list.get(world.random.nextInt(list.size())).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                    if(cap.isPresent())
                        return cap;
                }
            }
            else
            {
                //Added for quark boats with inventories (i hope)
                List<Entity> list = world.getEntitiesOfClass(Entity.class, new AABB(pos), entity -> entity instanceof Boat);
                if(!list.isEmpty())
                {
                    LazyOptional<IItemHandler> cap = list.get(world.random.nextInt(list.size())).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                    if(cap.isPresent())
                        return cap;
                }
            }
        }
        return LazyOptional.empty();
    }

    public static LazyOptional<IFluidHandler> findFluidHandlerAtPos(Level world, BlockPos pos, Direction side, boolean allowCart)
    {
        BlockEntity neighbourTile = world.getBlockEntity(pos);
        if(neighbourTile!=null)
        {
            LazyOptional<IFluidHandler> cap = neighbourTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
            if(cap.isPresent())
                return cap;
        }
        if(allowCart)
        {
            if(RailBlock.isRail(world, pos))
            {
                List<Entity> list = world.getEntitiesOfClass(Entity.class, new AABB(pos), entity -> entity instanceof IForgeAbstractMinecart);
                if(!list.isEmpty())
                {
                    LazyOptional<IFluidHandler> cap = list.get(world.random.nextInt(list.size())).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
                    if(cap.isPresent())
                        return cap;
                }
            }
            else
            {
                //Added for quark boats with inventories (i hope)
                List<Entity> list = world.getEntitiesOfClass(Entity.class, new AABB(pos), entity -> entity instanceof Boat);
                if(!list.isEmpty())
                {
                    LazyOptional<IFluidHandler> cap = list.get(world.random.nextInt(list.size())).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
                    if(cap.isPresent())
                        return cap;
                }
            }
        }
        return LazyOptional.empty();
    }
}
