package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

public class ipuaCrafter extends ipuBasic {

    public ipuaCrafter()
    {

    }

    protected int counter = 0;
    public void upgradeAction(World world, BlockPos posOfPedestal, int gridSize)
    {
        InventoryCrafting craft = new InventoryCrafting(new Container()
        {
            @Override
            public boolean canInteractWith(@Nonnull EntityPlayer player) {
                return false;
            }
        }, gridSize, gridSize);

        int gridIterateSize = gridSize*gridSize;

        if(!world.isRemote)
        {
            if(!world.isBlockPowered(posOfPedestal))
            {
                if (world.getTileEntity(getPosOfBlockBelow(1)) != null) {
                    if (world.getTileEntity(getPosOfBlockBelow(1)).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
                        TileEntity invToPushTo = world.getTileEntity(getPosOfBlockBelow(1));
                        if (invToPushTo instanceof TilePedestal) {
                            return;
                        }

                        if(invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots()==gridIterateSize)
                        {
                            int itemInStackCount = 0;
                            for(int i = 0; i < gridIterateSize; i++) {
                                ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);
                                if(stack.getCount()>=2 || stack.isEmpty() || stack.getMaxStackSize()==1  || stack.getItem().equals(ItemRegistry.craftingPlaceholder))
                                {
                                    itemInStackCount++;
                                }
                            }

                            if(itemInStackCount>=gridIterateSize)
                            {
                                for(int i = 0; i < gridIterateSize; i++) {
                                    ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);

                                    if(stack.isEmpty() || stack.getItem().equals(ItemRegistry.craftingPlaceholder))
                                        continue;

                                    craft.setInventorySlotContents(i, stack);
                                }

                                for(IRecipe recipe : ForgeRegistries.RECIPES)
                                {
                                    if(recipe.matches(craft, world)) {
                                        if(!hasItem())
                                        {
                                            this.addItem(recipe.getCraftingResult(craft));

                                            for(int i = 0; i < gridIterateSize; i++) {
                                                ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);
                                                if(stack.isEmpty()  || stack.getItem().equals(ItemRegistry.craftingPlaceholder))
                                                    continue;

                                                if(stack.getItem().hasContainerItem(stack))
                                                {
                                                    System.out.println(stack.getDisplayName());
                                                    ItemStack container = stack.getItem().getContainerItem(stack);
                                                    if(!world.isRemote)
                                                    {
                                                        world.spawnEntity(new EntityItem(world,getPosOfBlockBelow(-1).getX() + 0.5,getPosOfBlockBelow(-1).getY(),getPosOfBlockBelow(-1).getZ()+ 0.5,container));
                                                    }
                                                    stack.shrink(1);
                                                }
                                                else
                                                {
                                                    stack.shrink(1);
                                                }

                                            }
                                        }
                                        else
                                        {
                                            if(doItemsMatch(recipe.getCraftingResult(craft)))
                                            {
                                                if(getItemInPedestal().getCount()<getMaxStackSize())
                                                {
                                                    this.addItem(recipe.getCraftingResult(craft));
                                                }


                                                for(int i = 0; i < gridIterateSize; i++) {
                                                    ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);
                                                    if(stack.isEmpty()  || stack.getItem().equals(ItemRegistry.craftingPlaceholder))
                                                        continue;

                                                    if(stack.getItem().hasContainerItem(stack))
                                                    {
                                                        System.out.println(stack.getDisplayName());
                                                        ItemStack container = stack.getItem().getContainerItem(stack);
                                                        if(!world.isRemote)
                                                        {
                                                            world.spawnEntity(new EntityItem(world,getPosOfBlockBelow(-1).getX() + 0.5,getPosOfBlockBelow(-1).getY(),getPosOfBlockBelow(-1).getZ()+ 0.5,container));
                                                        }
                                                        stack.shrink(1);
                                                    }
                                                    else
                                                    {
                                                        stack.shrink(1);
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                        else
                        {
                            int counted = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots()/gridIterateSize;
                            if(counter<counted)
                            {
                                if(!hasItem())
                                {
                                    // 0 1 2 3 4 5 6 7 8
                                    // 9 10 11 12 13 14 15 16 17
                                    // 18 19 20 21 22 23 24 25 26
                                    int fin = ((counter+1)*gridIterateSize)-1;
                                    int itemInStackCount = 0;
                                    for(int j = (fin-(gridIterateSize-1)); j <=fin; j++) {
                                        ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(j);
                                        if(stack.getCount()>=2 || stack.isEmpty() || stack.getMaxStackSize()==1 || stack.getItem().equals(ItemRegistry.craftingPlaceholder))
                                        {
                                            itemInStackCount++;
                                        }
                                    }

                                    if(itemInStackCount>=gridIterateSize)
                                    {
                                        for(int i = (fin-(gridIterateSize-1)); i <=fin; i++) {
                                            ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);

                                            if(stack.isEmpty() || stack.getItem().equals(ItemRegistry.craftingPlaceholder))
                                                continue;

                                            int getCraftingSlot = i-(counter*gridIterateSize);
                                            craft.setInventorySlotContents(getCraftingSlot, stack);
                                        }

                                        for(IRecipe recipe : ForgeRegistries.RECIPES)
                                        {
                                            if(recipe.matches(craft, world)) {
                                                if(!hasItem())
                                                {
                                                    this.addItem(recipe.getCraftingResult(craft));

                                                    for(int i = (fin-(gridIterateSize-1)); i <=fin; i++) {
                                                        ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);
                                                        if(stack.isEmpty()  || stack.getItem().equals(ItemRegistry.craftingPlaceholder))
                                                            continue;

                                                        if(stack.getItem().hasContainerItem(stack))
                                                        {
                                                            System.out.println(stack.getDisplayName());
                                                            ItemStack container = stack.getItem().getContainerItem(stack);
                                                            if(!world.isRemote)
                                                            {
                                                                world.spawnEntity(new EntityItem(world,getPosOfBlockBelow(-1).getX() + 0.5,getPosOfBlockBelow(-1).getY(),getPosOfBlockBelow(-1).getZ()+ 0.5,container));
                                                            }
                                                            stack.shrink(1);
                                                        }
                                                        else
                                                        {
                                                            stack.shrink(1);
                                                        }

                                                    }
                                                }
                                                else
                                                {
                                                    if(doItemsMatch(recipe.getCraftingResult(craft)))
                                                    {
                                                        if(getItemInPedestal().getCount()<getMaxStackSize())
                                                        {
                                                            this.addItem(recipe.getCraftingResult(craft));
                                                        }


                                                        for(int i = (fin-(gridIterateSize-1)); i <=fin; i++) {
                                                            ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);
                                                            if(stack.isEmpty()  || stack.getItem().equals(ItemRegistry.craftingPlaceholder))
                                                                continue;

                                                            if(stack.getItem().hasContainerItem(stack))
                                                            {
                                                                ItemStack container = stack.getItem().getContainerItem(stack);
                                                                if(!world.isRemote)
                                                                {
                                                                    world.spawnEntity(new EntityItem(world,getPosOfBlockBelow(-1).getX() + 0.5,getPosOfBlockBelow(-1).getY(),getPosOfBlockBelow(-1).getZ()+ 0.5,container));
                                                                }
                                                                stack.shrink(1);
                                                            }
                                                            else
                                                            {
                                                                stack.shrink(1);
                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }

                                    counter++;
                                }
                            }

                            if(counter>=counted)
                            {
                                counter=0;
                            }
                        }
                        //sets the items from the inventory as a crafting pattern WE WILL CHECK the below inv and set the first 9 slots as this
                    }
                }
            }
        }

    }

}
