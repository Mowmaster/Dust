package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.references.Reference.MODID;

public class ImportItem extends UpgradeBase
{
    public int transferRate = 1;

    public ImportItem(Properties builder) {super(builder.group(dust.itemGroup));}


    public int getItemTransferRate(ItemStack stack)
    {
        /*switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
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
        }*/

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

    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {
        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);
        int transferRate = getItemTransferRate(coinInPedestal);

        ItemStack itemFromInv = ItemStack.EMPTY;
        if(world.getTileEntity(posInventory) !=null)
        {
            if(world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)).isPresent())
            {
                IItemHandler handler = (IItemHandler) world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)).orElse(null);
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
                            int maxStackSizeAllowedInPedestal = 0;
                            int roomLeftInPedestal = 0;
                            itemFromInv = handler.getStackInSlot(i);
                            ItemStack itemFromPedestal = getStackInPedestal(world,posOfPedestal);
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

                                ItemStack copyIncoming = itemFromInv.copy();
                                copyIncoming.setCount(allowedTransferRate);
                                TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                if(pedestalInv instanceof TilePedestal) {
                                    handler.extractItem(i,allowedTransferRate ,false );
                                    ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    public void actionOnColideWithBlock(World world, TilePedestal tilePedestal, BlockPos posPedestal, BlockState state, Entity entityIn)
    {
        if(entityIn instanceof ItemEntity)
        {
            ItemStack getItemStack = ((ItemEntity) entityIn).getItem();
            ItemStack itemFromPedestal = getStackInPedestal(world,posPedestal);
            if(itemFromPedestal.isEmpty())
            {
                TileEntity pedestalInv = world.getTileEntity(posPedestal);
                if(pedestalInv instanceof TilePedestal) {
                    entityIn.remove();
                    ((TilePedestal) pedestalInv).addItem(getItemStack);
                }
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "Import Upgrade"));
    }

    /*@Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s2 = getItemTransferRate(stack);
        String s5 = getOperationSpeedString(stack);
        String tr = "" + s2 + "";

        tooltip.add(TextFormatting.GOLD + "Item Stack Import Upgrade");

        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("coineffect"))
            {
                tooltip.add(TextFormatting.GRAY + "Transfer Rate: " + tr);
            }
            else
            {
                tooltip.add(TextFormatting.GRAY + "Transfer Rate: 1");
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
            tooltip.add(TextFormatting.GRAY + "Transfer Rate: 1");
            tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
        }
    }*/




    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(new ImportItem(new Properties().maxStackSize(64).group(dust.itemGroup)).setRegistryName(new ResourceLocation(MODID, "coin/import")));
    }


}
