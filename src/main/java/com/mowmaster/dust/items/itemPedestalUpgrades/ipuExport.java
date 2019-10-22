package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuExport extends ipuBasic
{
    public int transferRate = 0;
    public int transferSpeed = 0;

    public ipuExport(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        if(stack.getItem().equals(ItemRegistry.importUpgrade))
        {
            return super.isBookEnchantable(stack, book);
        }

        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    public int getTransferRate(ItemStack stack)
    {
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
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
        }

        return  transferRate;
    }

    public int getTransferSpeed(ItemStack stack)
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
        int speed = getTransferSpeed(coinInPedestal);
        if (tick%speed == 0) {
            upgradeAction(world,pedestalPos,coinInPedestal);
        }
    }

    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {
        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);
        int transferRate = getTransferRate(coinInPedestal);

            ItemStack itemFromInv = ItemStack.EMPTY;
            if(world.getTileEntity(posInventory) !=null)
            {
                if(world.getTileEntity(posInventory).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))
                {

                    TileEntity invToPullFrom = world.getTileEntity(posInventory);
                    if(invToPullFrom instanceof TilePedestal) {
                        itemFromInv = ItemStack.EMPTY;

                    }
                    else {
                        for(int i =0;i<invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots();i++)
                        {
                            if(!invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i).equals(ItemStack.EMPTY))
                            {
                                itemFromInv = invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);

                                if(getStackInPedestal(world,posOfPedestal) == ItemStack.EMPTY)
                                {
                                    if(itemFromInv.getCount() > transferRate)
                                    {
                                        ItemStack copyIncoming = itemFromInv.copy();
                                        copyIncoming.setCount(transferRate);
                                        TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                        if(pedestalInv instanceof TilePedestal) {
                                            ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                        }
                                        int counted = itemFromInv.getCount()-transferRate;
                                        itemFromInv.setCount(counted);
                                    }
                                    else
                                    {
                                        ItemStack copyIncoming = itemFromInv.copy();
                                        TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                        if(pedestalInv instanceof TilePedestal) {
                                            ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                        }
                                        itemFromInv.setCount(0);
                                    }
                                }
                                else if(doItemsMatch(getStackInPedestal(world,posOfPedestal),itemFromInv))
                                {
                                    int leftTillFilled = 64 - getStackInPedestal(world,posOfPedestal).getCount();
                                    if(leftTillFilled > itemFromInv.getCount())
                                    {
                                        if(itemFromInv.getCount()> transferRate)
                                        {
                                            ItemStack copyIncoming = itemFromInv.copy();
                                            copyIncoming.setCount(transferRate);
                                            TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                            if(pedestalInv instanceof TilePedestal) {
                                                ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                            }
                                            int counted = itemFromInv.getCount()-transferRate;
                                            itemFromInv.setCount(counted);
                                        }
                                        else
                                        {
                                            ItemStack copyIncoming = itemFromInv.copy();
                                            TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                            if(pedestalInv instanceof TilePedestal) {
                                                ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                            }
                                            itemFromInv.setCount(0);
                                        }
                                    }
                                    else
                                    {
                                        if(leftTillFilled >= transferRate)
                                        {
                                            ItemStack copyIncoming = itemFromInv.copy();
                                            copyIncoming.setCount(transferRate);
                                            TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                            if(pedestalInv instanceof TilePedestal) {
                                                ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                            }
                                            int counted = itemFromInv.getCount()-transferRate;
                                            itemFromInv.setCount(counted);
                                        }
                                        else
                                        {
                                            ItemStack copyIncoming = itemFromInv.copy();
                                            copyIncoming.setCount(leftTillFilled);
                                            TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                            if(pedestalInv instanceof TilePedestal) {
                                                ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                            }
                                            int counted = itemFromInv.getCount()-leftTillFilled;
                                            itemFromInv.setCount(counted);
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
        int s2 = getTransferRate(stack);
        String s3 = "";

        switch (getTransferSpeed(stack))
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
        tooltip.add("Item Stack Export Upgrade");
        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("coineffect"))
            {
                tooltip.add("Transfer Rate: " + tr);
            }
            else
            {
                tooltip.add("Transfer Rate: 1");
            }

            if(stack.isItemEnchanted() && getTransferRate(stack) >0)
            {
                tooltip.add("Transfer Speed: " + trr);
            }
            else
            {
                tooltip.add("Transfer Speed: Normal Speed");
            }
        }
        else
        {
            tooltip.add("Transfer Rate: 1");
            tooltip.add("Transfer Speed: Normal Speed");
        }
    }

}
