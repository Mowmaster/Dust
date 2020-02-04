package com.mowmaster.dust.items.itemPedestalUpgrades;


import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuCrafter extends ipuBasic
{
    public int transferRate = 0;
    public int gridSize = 0;

    public ipuCrafter(String unlocName, String registryName, int sizeOfGrid)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
        this.gridSize = sizeOfGrid;
    }

    public int getItemTransferRate(ItemStack stack)
    {
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                transferRate = 1;
                break;
            case 1:
                transferRate=2;
                break;
            case 2:
                transferRate = 4;
                break;
            case 3:
                transferRate = 8;
                break;
            case 4:
                transferRate = 12;
                break;
            case 5:
                transferRate=16;
                break;
            default: transferRate=1;
        }

        return  transferRate;
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);

        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world,pedestalPos,coinInPedestal);
            }
        }
    }


    //protected int counter = 0;
    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {
        ItemStack stackInPedestal = getStackInPedestal(world,posOfPedestal);
        int sizeAttemptToCraft = getItemTransferRate(coinInPedestal);
        int spaceInPedestal = 64 - stackInPedestal.getCount();
        ItemStack itemFromInv = ItemStack.EMPTY;
        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);
        InventoryCrafting craft = new InventoryCrafting(new Container()
        {
            @Override
            public boolean canInteractWith(@Nonnull EntityPlayer player) {
                return false;
            }
        }, gridSize, gridSize);

        int gridIterateSize = gridSize*gridSize;

        if(world.getTileEntity(posInventory).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal))) {
            IItemHandler handler = (IItemHandler) world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal));
            TileEntity invToPullFrom = world.getTileEntity(posInventory);
            if (invToPullFrom instanceof TilePedestal) {
                itemFromInv = ItemStack.EMPTY;
            }

            //Makes Sure the Slots available to us are big enough for our recipe
            if(handler.getSlots()>=gridIterateSize)
            {
                int itemInStackCount = 0;
                for(int i = 0; i < gridIterateSize; i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    //If has enough for a recipe, is empty, is a container item(potentially), or is a crafting placeholder(blank space)
                    if(stack.getCount()>=2 || stack.isEmpty() || stack.getItem().hasContainerItem(stack)  || stack.getItem().equals(ItemRegistry.craftingPlaceholder))
                    {
                        if(sizeAttemptToCraft > (stack.getCount()-1))
                        {
                            if(stack.getItem().hasContainerItem(stack)) {sizeAttemptToCraft = 1;}
                            else {sizeAttemptToCraft = stack.getCount()-1;}
                        }
                        itemInStackCount++;
                    }
                }
                //For loop runs and makes sure we can craft a recipe
                if(itemInStackCount>=gridIterateSize)
                {
                    for(int i = 0; i < gridIterateSize; i++) {
                        ItemStack stack = handler.getStackInSlot(i);

                        if(stack.isEmpty() || stack.getItem().equals(ItemRegistry.craftingPlaceholder))
                            continue;

                        craft.setInventorySlotContents(i, stack);
                    }

                    for(IRecipe recipe : ForgeRegistries.RECIPES)
                    {
                        //If recipe is valid and we can craft recipe and stick it in pedestal
                        if(recipe.matches(craft, world)) {
                            //Checks to make sure the stack we are about to return will fit in the pedestal
                            ItemStack stackOutput = recipe.getCraftingResult(craft);
                            while((stackOutput.getCount() * sizeAttemptToCraft) > spaceInPedestal) {
                                sizeAttemptToCraft--;
                            }
                            //since result will fit in pedestal, is the pedestal empty or do the items match?
                            if(stackInPedestal.equals(ItemStack.EMPTY) || doItemsMatch(stackInPedestal, stackOutput)  && sizeAttemptToCraft>0)
                            {
                                int sizeToCraft = stackOutput.getCount() * sizeAttemptToCraft;
                                stackOutput.setCount(sizeToCraft);
                                addToPedestal(world,posOfPedestal,stackOutput);

                                for(int i = 0; i < gridIterateSize; i++) {
                                    ItemStack stack = handler.getStackInSlot(i);
                                    if(stack.isEmpty()  || stack.getItem().equals(ItemRegistry.craftingPlaceholder))
                                        continue;

                                    if(stack.getItem().hasContainerItem(stack))
                                    {
                                        //From above, if we have a container item the sizeAttemptToCraft should be 1
                                        ItemStack container = stack.getItem().getContainerItem(stack);
                                        if(!world.isRemote)
                                        {
                                            world.spawnEntity(new EntityItem(world,getPosOfBlockBelow(world,posOfPedestal,-1).getX() + 0.5,getPosOfBlockBelow(world,posOfPedestal,-1).getY()+ 1,getPosOfBlockBelow(world,posOfPedestal,-1).getZ()+ 0.5,container));
                                        }
                                        stack.shrink(1);
                                    }
                                    else
                                    {
                                        stack.shrink(sizeAttemptToCraft);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*protected int counter = 0;
    public void crafter(World world, ItemStack itemInPedestal, ItemStack coinInPedestal, BlockPos posOfPedestal)//1x1, 2x2, 3x3
    {
        //Iterate size is 1,4,9 effectivly
        int gridIterateSize = gridSize*gridSize;
        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);
        ItemStack itemFromInv = ItemStack.EMPTY;

        //Setup crafting Container with grid size
        InventoryCrafting craft = new InventoryCrafting(new Container()
        {
            @Override
            public boolean canInteractWith(@Nonnull EntityPlayer player) {
                return false;
            }
        }, gridSize, gridSize);

        if(world.getTileEntity(posInventory) !=null)
        {
            if(world.getTileEntity(posInventory).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)))
            {
                IItemHandler handler = (IItemHandler) world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal));
                TileEntity invToPullFrom = world.getTileEntity(posInventory);
                if(invToPullFrom instanceof TilePedestal) {
                    itemFromInv = ItemStack.EMPTY;
                }
                else {
                    if(handler != null)
                    {
                        int intInventorySlots = handler.getSlots();
                        //int i = getNextSlotWithItems(invToPullFrom,getPedestalFacing(world, posOfPedestal),getStackInPedestal(world,posOfPedestal));

                        //If the container is the same size as the grid
                        if(intInventorySlots==gridIterateSize)
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
                            //Counter is the currently selected slot
                            int counted = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots()/gridIterateSize;
                            if(counter<counted)
                            {
                                if(!hasItem())
                                {
                                    // 0 1 2 3 4 5 6 7 8
                                    // 9 10 11 12 13 14 15 16 17
                                    // 18 19 20 21 22 23 24 25 26

                                    //Pretend we use a 3x3 grid and counter = 0 and so fin =8
                                    int fin = ((counter+1)*gridIterateSize)-1;
                                    int itemInStackCount = 0;
                                    //j = 8-(9-1) == 0
                                    for(int j = (fin-(gridIterateSize-1)); j <=fin; j++) {
                                        ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(j);
                                        //Checks to make sure we have a possible recipe in slots 0-8
                                        if(stack.getCount()>=2 || stack.isEmpty() || stack.getMaxStackSize()==1 || stack.getItem().equals(ItemRegistry.craftingPlaceholder))
                                        {
                                            itemInStackCount++;
                                        }
                                    }

                                    //IF we have a potential recipe available to craft
                                    if(itemInStackCount>=gridIterateSize)
                                    {
                                        //Iterating 0-8 again
                                        for(int i = (fin-(gridIterateSize-1)); i <=fin; i++) {
                                            ItemStack stack = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);

                                            if(stack.isEmpty() || stack.getItem().equals(ItemRegistry.craftingPlaceholder))
                                                continue;

                                            //Sets the craftign slots up with items (Should just make a crafting array first with a counter and then simplify this with a loop through array)
                                            int getCraftingSlot = i-(counter*gridIterateSize);
                                            craft.setInventorySlotContents(getCraftingSlot, stack);
                                        }

                                        //iterate through forge registered recipes
                                        for(IRecipe recipe : ForgeRegistries.RECIPES)
                                        {
                                            //if our recipe has a match
                                            if(recipe.matches(craft, world)) {
                                                //if pedestal is empty
                                                if(!hasItem())
                                                {
                                                    //add crafting result to pedestal
                                                    this.addItem(recipe.getCraftingResult(craft));

                                                    //deal with removing items, expelling container items, and keeping placeholders in place
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
                                                //if pedestal has items inside (Could skip this if we wanted to)
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
    }*/

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String s5 = getOperationSpeedString(stack);
        String tr = "" + getItemTransferRate(stack) + "";

        tooltip.add(TextFormatting.GOLD + "Item Stack Crafter Upgrade - "+gridSize+"x"+gridSize);

        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("coineffect"))
            {
                tooltip.add(TextFormatting.GRAY + "Bulk Crafting Rate: " + tr);
            }
            else
            {
                tooltip.add(TextFormatting.GRAY + "Bulk Crafting Rate: 1");
            }

            if(stack.isItemEnchanted() && getOperationSpeed(stack) >0)
            {
                tooltip.add(TextFormatting.RED + "Operational Speed: " + s5);
            }
            else
            {
                tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
            }
        }
        else
        {
            tooltip.add(TextFormatting.GRAY + "Bulk Crafting Rate: 1");
            tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
        }
    }

}
