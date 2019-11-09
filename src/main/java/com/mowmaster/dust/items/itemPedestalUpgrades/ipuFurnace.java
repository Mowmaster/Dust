package com.mowmaster.dust.items.itemPedestalUpgrades;


import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuFurnace extends ipuBasic
{
    public int itemsPerSmelt = 0;
    public int smeltingSpeed = 0;
    public final int burnTimeCostPerItemSmelted = 200;

    public ipuFurnace(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    public int getItemTransferRate(ItemStack stack)
    {
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                itemsPerSmelt = 1;
                break;
            case 1:
                itemsPerSmelt=2;
                break;
            case 2:
                itemsPerSmelt = 4;
                break;
            case 3:
                itemsPerSmelt = 8;
                break;
            case 4:
                itemsPerSmelt = 12;
                break;
            case 5:
                itemsPerSmelt=16;
                break;
            default: itemsPerSmelt=1;
        }

        return  itemsPerSmelt;
    }

    public int getSmeltingSpeed(ItemStack stack)
    {
        switch (getTransferRateModifier(stack))
        {
            case 0:
                smeltingSpeed = 200;//normal speed
                break;
            case 1:
                smeltingSpeed=100;//2x faster
                break;
            case 2:
                smeltingSpeed = 50;//4x faster
                break;
            case 3:
                smeltingSpeed = 33;//6x faster
                break;
            case 4:
                smeltingSpeed = 20;//10x faster
                break;
            case 5:
                smeltingSpeed=10;//20x faster
                break;
            default: smeltingSpeed=200;
        }

        return  smeltingSpeed;
    }

    private int getNextSlotWithItems(TileEntity invBeingChecked, EnumFacing sideSlot, ItemStack stackInPedestal)
    {
        int slot = -1;

        if(invBeingChecked.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,sideSlot)) {
            IItemHandlerModifiable handler = (IItemHandlerModifiable) invBeingChecked.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, sideSlot);
            int range = handler.getSlots();
            for(int i=0;i<range;i++)
            {

                //Get Item In Slot
                ItemStack stackInSlot = handler.getStackInSlot(i);

                //find a slot with items
                if(!stackInSlot.isEmpty())
                {
                    //check if it could pull the item out or not
                    if(!handler.extractItem(i,1 ,true ).equals(ItemStack.EMPTY))
                    {
                        //Check if it can be smelted
                        if(!(FurnaceRecipes.instance().getSmeltingResult(stackInSlot).equals(ItemStack.EMPTY)))
                        {
                            //If pedestal is empty accept any items
                            if(stackInPedestal.isEmpty())
                            {
                                slot=i;
                                break;
                            }
                            //if stack in pedestal matches items in slot when smelted
                            else if(doItemsMatch(stackInPedestal,FurnaceRecipes.instance().getSmeltingResult(stackInSlot)))
                            {
                                slot=i;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return slot;
    }

    public int removeFuel(TilePedestal pedestal, int amountToRemove, boolean simulate)
    {
        int fuelLeft = pedestal.getStoredValueForUpgrades();
        int amountToSet = fuelLeft - amountToRemove;
        if(amountToRemove >= fuelLeft) amountToSet = -1;
        if(!simulate)
        {
            if(amountToSet == -1) amountToSet = 0;
            pedestal.setStoredValueForUpgrades(amountToSet);
        }

        return amountToSet;
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getSmeltingSpeed(coinInPedestal);

        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world,pedestalPos,coinInPedestal);
            }
        }
    }

    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {
        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);
        int itemsPerSmelt = getItemTransferRate(coinInPedestal);

            ItemStack itemFromInv = ItemStack.EMPTY;
            if(world.getTileEntity(posInventory) !=null)
            {
                if(world.getTileEntity(posInventory).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)))
                {
                    IItemHandlerModifiable handler = (IItemHandlerModifiable) world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal));
                    TileEntity invToPullFrom = world.getTileEntity(posInventory);
                    if(invToPullFrom instanceof TilePedestal) {
                        itemFromInv = ItemStack.EMPTY;

                    }
                    else {
                        if(handler != null)
                        {
                            int i = getNextSlotWithItems(invToPullFrom,getPedestalFacing(world, posOfPedestal),getStackInPedestal(world,posOfPedestal));
                            if(i>=0)
                            {
                                int maxInSlot = handler.getSlotLimit(i);
                                itemFromInv = handler.getStackInSlot(i);
                                //Should work without catch since we null check this in our GetNextSlotFunction
                                ItemStack smeltedItemResult = FurnaceRecipes.instance().getSmeltingResult(itemFromInv);
                                ItemStack itemFromPedestal = getStackInPedestal(world,posOfPedestal);

                                //Null check our slot again, which is probably redundant
                                if(handler.getStackInSlot(i) != null && !handler.getStackInSlot(i).isEmpty() && handler.getStackInSlot(i).getItem() != Items.AIR)
                                {
                                    int roomLeftInPedestal = 64-itemFromPedestal.getCount();
                                    if(itemFromPedestal.isEmpty() || itemFromPedestal.equals(ItemStack.EMPTY)) roomLeftInPedestal = 64;

                                    //Upgrade Determins amout of items to smelt, but space count is determined by how much the item smelts into
                                    int itemInputsPerSmelt = itemsPerSmelt;
                                    int itemsOutputWhenStackSmelted = (itemsPerSmelt*smeltedItemResult.getCount());
                                    //Checks to see if pedestal can accept as many items as will be returned on smelt, if not reduce items being smelted
                                    if(roomLeftInPedestal < itemsOutputWhenStackSmelted)
                                    {
                                        itemInputsPerSmelt = Math.floorDiv(roomLeftInPedestal, smeltedItemResult.getCount());
                                    }
                                    //Checks to see how many items are left in the slot IF ITS UNDER the allowedTransferRate then sent the max rate to that.
                                    if(itemFromInv.getCount() < itemInputsPerSmelt) itemInputsPerSmelt = itemFromInv.getCount();

                                    itemsOutputWhenStackSmelted = (itemsPerSmelt*smeltedItemResult.getCount());
                                    ItemStack copyIncoming = smeltedItemResult.copy();
                                    copyIncoming.setCount(itemsOutputWhenStackSmelted);
                                    int fuelToConsume = burnTimeCostPerItemSmelted * getItemTransferRate(coinInPedestal);
                                    TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                    if(pedestalInv instanceof TilePedestal) {
                                        TilePedestal ped = ((TilePedestal) pedestalInv);
                                        //Checks to make sure we have fuel to smelt everything
                                        if(removeFuel(ped,fuelToConsume,true)>=0)
                                        {
                                            handler.extractItem(i,itemInputsPerSmelt ,false );
                                            removeFuel(ped,fuelToConsume,false);
                                            ped.addItem(copyIncoming);
                                        }
                                        //If we done have enough fuel to smelt everything then reduce size of smelt
                                        else
                                        {
                                            //gets fuel left
                                            int fuelLeft = ped.getStoredValueForUpgrades();
                                            if(fuelLeft>0)
                                            {
                                                //this = a number over 1 unless fuelleft < burnTimeCostPeritemSmelted
                                                itemInputsPerSmelt = Math.floorDiv(fuelLeft,burnTimeCostPerItemSmelted );
                                                if(itemInputsPerSmelt >=1)
                                                {
                                                    System.out.println(itemInputsPerSmelt);
                                                    fuelToConsume = burnTimeCostPerItemSmelted * itemInputsPerSmelt;
                                                    itemsOutputWhenStackSmelted = (itemsPerSmelt*smeltedItemResult.getCount());
                                                    copyIncoming.setCount(itemsOutputWhenStackSmelted);

                                                    handler.extractItem(i,itemInputsPerSmelt ,false );
                                                    removeFuel(ped,fuelToConsume,false);
                                                    ped.addItem(copyIncoming);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

    }

    public static int getItemFuelBurnTime(ItemStack fuel)
    {
        if (fuel.isEmpty()) return 0;
        else
        {
            int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(fuel);
            if (burnTime >= 0) return burnTime;
            Item item = fuel.getItem();

            if (item == Item.getItemFromBlock(Blocks.WOODEN_SLAB)) return 150;
            else if (item == Item.getItemFromBlock(Blocks.WOOL)) return 100;
            else if (item == Item.getItemFromBlock(Blocks.CARPET)) return 67;
            else if (item == Item.getItemFromBlock(Blocks.LADDER)) return 300;
            else if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON)) return 100;
            else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD) return 300;
            else if (item == Item.getItemFromBlock(Blocks.COAL_BLOCK)) return 16000;
            else if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName())) return 200;
            else if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName())) return 200;
            else if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName())) return 200;
            else if (item == Items.STICK) return 100;
            else if (item != Items.BOW && item != Items.FISHING_ROD)
            {
                if (item == Items.SIGN) return 200;
                else if (item == Items.COAL) return 1600;
                else if (item == Items.LAVA_BUCKET) return 20000;
                else if (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL)
                {
                    if (item == Items.BLAZE_ROD) return 2400;
                    else if (item instanceof ItemDoor && item != Items.IRON_DOOR) return 200;
                    else return item instanceof ItemBoat ? 400 : 0;
                }
                else return 100;
            }
            else return 300;
        }
    }

    @Override
    public void actionOnColideWithBlock(World world, TilePedestal tilePedestal, BlockPos posPedestal, IBlockState state, Entity entityIn)
    {
        if(entityIn instanceof EntityItem)
        {
            ItemStack getItemStack = ((EntityItem) entityIn).getItem();
            if(getItemFuelBurnTime(getItemStack)>0)
            {
                int CurrentBurnTime = tilePedestal.getStoredValueForUpgrades();
                int getBurnTimeForStack = getItemFuelBurnTime(getItemStack) * getItemStack.getCount();
                tilePedestal.setStoredValueForUpgrades(CurrentBurnTime + getBurnTimeForStack);
                if(((EntityItem) entityIn).getItem().equals(Items.LAVA_BUCKET))
                {
                    ItemStack getReturned = new ItemStack(Items.BUCKET,getItemStack.getCount());
                    EntityItem items1 = new EntityItem(world, posPedestal.getX() + 0.5, posPedestal.getY() + 1.0, posPedestal.getZ() + 0.5, getReturned.copy());
                    entityIn.setDead();
                    world.spawnEntity(items1);
                }

                 entityIn.setDead();
            }
        }
    }

    @Override
    public void onRandomDisplayTick(TilePedestal pedestal, IBlockState stateIn, World world, BlockPos pos, Random rand)
    {
        if(!world.isBlockPowered(pos))
        {
            int fuelValue = pedestal.getStoredValueForUpgrades();

            double d0 = (double)getPosOfBlockBelow(world,pos,-1 ).getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
            double d1 = (double)getPosOfBlockBelow(world,pos,-1 ).getY() + 0.0D - (double)(rand.nextFloat() * 0.1F);
            double d2 = (double)getPosOfBlockBelow(world,pos,-1 ).getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
            double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);

            //If fuel is less then 8 normal charcoal
            if(fuelValue<=12800 && fuelValue>0)
            {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, new int[0]);
            }
            //If fuel has less then a stack of normal charcoal
            if(fuelValue>12800 && fuelValue<=102400)
            {
                world.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
                world.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, new int[0]);

            }
            //If fuel has less then 256 worth of normal charcoal
            if(fuelValue>102400 && fuelValue<=409600)
            {
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, new int[0]);
                world.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
                world.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, new int[0]);
            }
            //If fuel has less then 1024 worth of normal charcoal
            if(fuelValue>409600 && fuelValue<=1638400)
            {
                world.spawnParticle(EnumParticleTypes.LAVA, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
                world.spawnParticle(EnumParticleTypes.LAVA, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, new int[0]);
            }
            //If fuel has more then 1024 worth of normal charcoal
            if(fuelValue>1638400)
            {
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, new int[0]);
                world.spawnParticle(EnumParticleTypes.LAVA, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
                world.spawnParticle(EnumParticleTypes.LAVA, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, rand.nextGaussian() * 0.004D, new int[0]);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s2 = getItemTransferRate(stack);
        String s3 = "";

        switch (getSmeltingSpeed(stack))
        {
            case 200:
                s3 = "Normal Speed";
                break;
            case 100:
                s3="2x Faster";
                break;
            case 50:
                s3 = "46x Faster";
                break;
            case 33:
                s3 = "6x Faster";
                break;
            case 20:
                s3 = "10x Faster";
                break;
            case 10:
                s3="20x Faster";
                break;
            default: s3= "Normal Speed";
        }


        String tr = "" + s2 + "";
        String trr = s3;
        tooltip.add(TextFormatting.GOLD + "Furnace Upgrade");
        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("coineffect"))
            {
                tooltip.add("Items Per Smelt: " + tr);
            }
            else
            {
                tooltip.add("Items Per Smelt: 1");
            }

            if(stack.isItemEnchanted() && getSmeltingSpeed(stack) >0)
            {
                tooltip.add("Smelting Speed: " + trr);
            }
            else
            {
                tooltip.add("Smelting Speed: Normal Speed");
            }
        }
        else
        {
            tooltip.add("Items Per Smelt: 1");
            tooltip.add("Smelting Speed: Normal Speed");
        }
    }

}
