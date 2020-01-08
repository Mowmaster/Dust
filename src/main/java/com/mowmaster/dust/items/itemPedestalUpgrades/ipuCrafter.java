package com.mowmaster.dust.items.itemPedestalUpgrades;


import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.items.itemPedestalUpgrades.ipuBasic;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShulkerBoxRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuCrafter extends ipuBasic
{
    public int transferRate = 0;
    public int transferSpeed = 0;
    private int gridSize = 0;

    public ipuCrafter(String unlocName, String registryName, int sizeOfGrid)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
        this.gridSize = sizeOfGrid;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return super.isBookEnchantable(stack, book);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
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

    public int getTransferRate(ItemStack stack)
    {
        switch (getTransferRateModifier(stack))
        {
            case 0:
                transferSpeed = 20;//normal speed
                break;
            case 1:
                transferSpeed=10;//2x faster
                break;
            case 2:
                transferSpeed = 5;//4x faster
                break;
            case 3:
                transferSpeed = 3;//6x faster
                break;
            case 4:
                transferSpeed = 2;//10x faster
                break;
            case 5:
                transferSpeed=1;//20x faster
                break;
            default: transferSpeed=20;
        }

        return  transferSpeed;
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getTransferRate(coinInPedestal);

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
            IItemHandlerModifiable handler = (IItemHandlerModifiable) world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal));
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

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s2 = getItemTransferRate(stack);
        String s3 = "";

        switch (getTransferRate(stack))
        {
            case 1:
                s3 = "20x Faster";
                break;
            case 2:
                s3="10x Faster";
                break;
            case 3:
                s3 = "6x Faster";
                break;
            case 5:
                s3 = "4x Faster";
                break;
            case 10:
                s3 = "2x Faster";
                break;
            case 20:
                s3="Normal Speed";
                break;
            default: s3="Normal Speed";
        }


        String tr = "" + s2 + "";
        String trr = s3;
        tooltip.add(TextFormatting.GOLD + "Item Stack Crafter Upgrade - "+gridSize+"x"+gridSize);

        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("coineffect"))
            {
                tooltip.add("Bulk Crafting Rate: " + tr);
            }
            else
            {
                tooltip.add("Bulk Crafting Rate: 1");
            }

            if(stack.isItemEnchanted() && getTransferRate(stack) >0)
            {
                tooltip.add("Crafting Speed: " + trr);
            }
            else
            {
                tooltip.add("Crafting Speed: Normal Speed");
            }
        }
        else
        {
            tooltip.add("Bulk Crafting Rate: 1");
            tooltip.add("Crafting Speed: Normal Speed");
        }
    }

}
