package com.mowmaster.dust.items.itemPedestalUpgrades;


import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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
                upgradeAction(world,itemInPedestal,coinInPedestal,pedestalPos);
            }
        }
    }


    public void upgradeAction(World world, ItemStack itemInPedestal, ItemStack coinInPedestal, BlockPos posOfPedestal)
    {
        int intBatchCraftingSize = getItemTransferRate(coinInPedestal);
        ItemStack itemFromInv = ItemStack.EMPTY;
        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);
        int intGridCount = gridSize*gridSize;

        //Dont bother unless pedestal is empty
        //Yes i'm being lazy here...

        if(itemInPedestal.isEmpty())
        {
            if(world.getTileEntity(posInventory) != null)
            {
                //Setup new Container for our Crafting Grid Size
                InventoryCrafting craft = new InventoryCrafting(new Container()
                {
                    @Override
                    public boolean canInteractWith(@Nonnull EntityPlayer player) {
                        return false;
                    }
                }, gridSize, gridSize);

                //Get Inventory Below
                if(world.getTileEntity(posInventory).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal))) {
                    IItemHandler handler = (IItemHandler) world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal));
                    TileEntity invToPullFrom = world.getTileEntity(posInventory);
                    int intInventorySlotCount = handler.getSlots();//normal chests return value of 1-27
                    if (invToPullFrom instanceof TilePedestal) {
                        itemFromInv = ItemStack.EMPTY;
                    }
                    else
                    {
                        if(handler != null)
                        {
                            //Makes sure we have more slots then the recipe requires
                            if(intInventorySlotCount>=intGridCount)
                            {
                                // Get Next iteration to craft
                                int intGetNextIteration = getIntValueFromPedestal(world,posOfPedestal);//Default value is 0
                                if(intGetNextIteration == 0)
                                {
                                    intGetNextIteration = 1;
                                }
                                int intSlotToStartFrom = (intGetNextIteration*intGridCount)-intGridCount;//use int i= intSlotToStartFrom in for-loop
                                //If starting slot will be bigger then our inventory size
                                int intSlotToEndBefore = (intGetNextIteration*intGridCount);//use i < intSlotToEndBefore in for-loop
                                //if ending slot is > then total slots then it would error, so reset things
                                if(intSlotToEndBefore > intInventorySlotCount)
                                {
                                    //reset back to 1
                                    intGetNextIteration = 1;
                                    intSlotToStartFrom = (intGetNextIteration*intGridCount)-intGridCount;//use int i= intSlotToStartFrom in for-loop
                                    intSlotToEndBefore = (intGetNextIteration*intGridCount);//use i < intSlotToEndBefore in for-loop
                                }

                                int intCraftingSlot = 0;
                                for(int i = intSlotToStartFrom; i < intSlotToEndBefore; i++) {
                                    ItemStack stackItemInSlot = handler.getStackInSlot(i);

                                    //If the item Stack has enough items to craft with
                                    //stack.getCount()>=2 ||  stack.getMaxStackSize()==1 ||
                                    if(stackItemInSlot.isEmpty() ||stackItemInSlot.getItem().equals(ItemRegistry.craftingPlaceholder))
                                    {
                                        craft.setInventorySlotContents(intCraftingSlot,ItemStack.EMPTY);
                                        intCraftingSlot++;
                                    }
                                    else if(stackItemInSlot.getMaxStackSize()==1 || stackItemInSlot.isItemStackDamageable())
                                    {
                                        //Since recipe has a container item we have to limit it to 1 craft
                                        intBatchCraftingSize = 1;
                                        craft.setInventorySlotContents(intCraftingSlot,stackItemInSlot);
                                        intCraftingSlot++;
                                    }
                                    //the +1 makes sure to leave 1 item in the inv as a placeholder
                                    else if(stackItemInSlot.getCount() > (intBatchCraftingSize))
                                    {
                                        craft.setInventorySlotContents(intCraftingSlot,stackItemInSlot);
                                        intCraftingSlot++;
                                    }
                                }

                                //Checks to make sure we have enough slots set for out recipe
                                if(craft.getSizeInventory() >= intGridCount)
                                {
                                    for(IRecipe recipe : ForgeRegistries.RECIPES)
                                    {
                                        //If recipe is valid and we can craft recipe and stick it in pedestal
                                        if(recipe.matches(craft, world)) {
                                            //Set ItemStack with recipe result
                                            ItemStack stackRecipeResult = recipe.getCraftingResult(craft);
                                            int intRecipeResultCount = stackRecipeResult.getCount();
                                            int intBatchCraftedAmount = stackRecipeResult.getCount() * intBatchCraftingSize;

                                            //Check if pedestal can hold the crafting result, if not then set the batch to be small enough that it can fit
                                            if(intBatchCraftedAmount > 64)
                                            {
                                                intBatchCraftingSize = 64/intRecipeResultCount;
                                            }

                                            //Loop through inventory again to remove crafted materials used
                                            for(int i = 0; i < craft.getSizeInventory(); i++) {


                                                ItemStack stackInRecipe = craft.getStackInSlot(i);
                                                int intGetActualSlot = ((intGetNextIteration*intGridCount)-intGridCount)+i;
                                                ItemStack stackItemInSlot = handler.getStackInSlot(intGetActualSlot);

                                    /*if(stackInRecipe.getItem().hasContainerItem(stackInRecipe))
                                    {
                                        //Clear slot and spawn item in world
                                        ItemStack container = stackInRecipe.getItem().getContainerItem(stackInRecipe);
                                        handler.extractItem(intGetActualSlot,stackInRecipe.getCount(),false);
                                        if(!world.isRemote)
                                        {
                                            //Eject Container Item
                                            world.spawnEntity(new EntityItem(world,getPosOfBlockBelow(world,posOfPedestal,-1).getX() + 0.5,getPosOfBlockBelow(world,posOfPedestal,-1).getY()+ 1,getPosOfBlockBelow(world,posOfPedestal,-1).getZ()+ 0.5,container));
                                        }
                                    }
                                    else if(!stackInRecipe.isEmpty())
                                    {
                                        handler.extractItem(intGetActualSlot,intBatchCraftingSize,false);
                                    }
                                    else
                                    {
                                        continue;
                                    }*/
                                                if(stackInRecipe.isEmpty()  || stackInRecipe.getItem().equals(ItemRegistry.craftingPlaceholder))
                                                    continue;

                                                if(stackInRecipe.getItem().hasContainerItem(stackInRecipe))
                                                {
                                                    //System.out.println(stackInRecipe.getDisplayName());
                                                    ItemStack container = stackInRecipe.getItem().getContainerItem(stackInRecipe);
                                                    if(!world.isRemote)
                                                    {
                                                        world.spawnEntity(new EntityItem(world,getPosOfBlockBelow(world,posOfPedestal,-1).getX() + 0.5,getPosOfBlockBelow(world,posOfPedestal,-1).getY()+ 0.5,getPosOfBlockBelow(world,posOfPedestal,-1).getZ()+ 0.5,container));
                                                    }
                                                    handler.extractItem(intGetActualSlot,intBatchCraftingSize,false);
                                                }
                                                else
                                                {
                                                    handler.extractItem(intGetActualSlot,intBatchCraftingSize,false);
                                                }
                                            }

                                            stackRecipeResult.setCount(intBatchCraftedAmount);
                                            addToPedestal(world,posOfPedestal,stackRecipeResult);
                                        }
                                    }
                                }
                                setIntValueToPedestal(world,posOfPedestal,(intGetNextIteration+1));
                            }
                        }
                    }
                }
            }
        }
    }

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
