package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.item.ItemCraftingPlaceholder;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.crafting.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.data.ForgeRecipeProvider;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemUpgradeCrafter extends ItemUpgradeBase
{
    public ItemUpgradeCrafter(Properties builder) {super(builder.group(dust.ITEM_GROUP));}

    public int getGridSize(ItemStack itemStack)
    {
        int gridSize = 0;
        if(itemStack.getItem().equals(ItemUpgradeCrafter.CRAFTER_ONE)){gridSize = 1;}
        else if(itemStack.getItem().equals(ItemUpgradeCrafter.CRAFTER_TWO)){gridSize = 2;}
        else if(itemStack.getItem().equals(ItemUpgradeCrafter.CRAFTER_THREE)){gridSize = 3;}
        else{gridSize = 1;}

        return gridSize;
    }

    public int getItemTransferRate(ItemStack stack)
    {
        int transferRate = 1;
        switch (getCapacityModifier(stack))
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

    public static IRecipe<CraftingInventory> findRecipe(CraftingInventory inv, World world) {
        return world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, inv, world).orElse(null);
    }

    public void upgradeAction(World world, ItemStack itemInPedestal, ItemStack coinInPedestal, BlockPos posOfPedestal)
    {
        int gridSize = getGridSize(coinInPedestal);
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
                CraftingInventory craft = new CraftingInventory(new Container(null, -1) {
                    @Override
                    public boolean canInteractWith(PlayerEntity playerIn) {
                        return false;
                    }
                }, gridSize, gridSize);

                //Get Inventory Below
                if(world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)).isPresent()) {
                    IItemHandler handler = (IItemHandler) world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)).orElse(null);
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
                                    if(stackItemInSlot.isEmpty() ||stackItemInSlot.getItem().equals(ItemCraftingPlaceholder.PLACEHOLDER))
                                    {
                                        craft.setInventorySlotContents(intCraftingSlot,ItemStack.EMPTY);
                                        intCraftingSlot++;
                                    }
                                    else if(stackItemInSlot.getMaxStackSize()==1 || stackItemInSlot.isDamageable())
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

                                    IRecipe recipe = findRecipe(craft,world);
                                    //for(IRecipe recipes : ForgeRegistries.RECIPE)
                                    //{
                                        //If recipe is valid and we can craft recipe and stick it in pedestal

                                        if(recipe  != null &&  recipe.matches(craft, world)) {
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

                                                if(stackInRecipe.isEmpty()  || stackInRecipe.getItem().equals(ItemCraftingPlaceholder.PLACEHOLDER))
                                                    continue;

                                                if(stackInRecipe.getItem().hasContainerItem(stackInRecipe))
                                                {
                                                    //System.out.println(stackInRecipe.getDisplayName());
                                                    ItemStack container = stackInRecipe.getItem().getContainerItem(stackInRecipe);
                                                    if(!world.isRemote)
                                                    {
                                                        world.addEntity(new ItemEntity(world,getPosOfBlockBelow(world,posOfPedestal,-1).getX() + 0.5,getPosOfBlockBelow(world,posOfPedestal,-1).getY()+ 0.5,getPosOfBlockBelow(world,posOfPedestal,-1).getZ()+ 0.5,container));
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
                                    //}
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        String s5 = getOperationSpeedString(stack);
        String tr = "" + getItemTransferRate(stack) + "";

        tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "Crafter Upgrade"));
        tooltip.add(new TranslationTextComponent(TextFormatting.GRAY + "Recipes Crafted Per Action: " + tr));
        tooltip.add(new TranslationTextComponent(TextFormatting.RED + "Operational Speed: " + s5));
    }

    public static final Item CRAFTER_ONE = new ItemUpgradeCrafter(new Properties().maxStackSize(64).group(dust.ITEM_GROUP)).setRegistryName(new ResourceLocation(MODID, "coin/crafter1"));
    public static final Item CRAFTER_TWO = new ItemUpgradeCrafter(new Properties().maxStackSize(64).group(dust.ITEM_GROUP)).setRegistryName(new ResourceLocation(MODID, "coin/crafter2"));
    public static final Item CRAFTER_THREE = new ItemUpgradeCrafter(new Properties().maxStackSize(64).group(dust.ITEM_GROUP)).setRegistryName(new ResourceLocation(MODID, "coin/crafter3"));


    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(CRAFTER_ONE);
        event.getRegistry().register(CRAFTER_TWO);
        event.getRegistry().register(CRAFTER_THREE);
    }


}
