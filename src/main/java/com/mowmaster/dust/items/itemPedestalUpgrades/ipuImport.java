package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuImport extends ipuBasic
{
    public int transferRate;
    public ipuImport(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    private int getTransferRate(ItemStack stack)
    {
        int rate=0;
        switch (getTransferRateModifier(stack))
        {
            case 0:
                rate = 1;
                break;
            case 1:
                rate=4;
                break;
            case 2:
                rate = 8;
                break;
            case 3:
                rate = 16;
                break;
            case 4:
                rate = 32;
                break;
            case 5:
                rate=64;
                break;
            default: rate=64;
        }

        return  rate;
    }

    public void upgradeAction(World world, BlockPos posOfPedestal, BlockPos posOfInventory, ItemStack coinInPedestal)
    {
        //Only works when block is NOT powered
        if(!world.isBlockPowered(posOfPedestal))
        {
            ItemStack itemFromInv = ItemStack.EMPTY;
            if(world.getTileEntity(posOfInventory) !=null)
            {
                if(world.getTileEntity(posOfInventory).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))
                {

                    TileEntity invToPullFrom = world.getTileEntity(posOfInventory);
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
                                    if(itemFromInv.getCount()> getTransferRate(coinInPedestal))
                                    {
                                        ItemStack copyIncoming = itemFromInv.copy();
                                        copyIncoming.setCount(getTransferRate(coinInPedestal));
                                        TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                        if(pedestalInv instanceof TilePedestal) {
                                            ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                        }
                                        int counted = itemFromInv.getCount()-getTransferRate(coinInPedestal);
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
                                else if(getStackInPedestal(world,posOfPedestal) == itemFromInv)
                                {
                                    int leftTillFilled = 64 - getStackInPedestal(world,posOfPedestal).getCount();
                                    if(leftTillFilled > itemFromInv.getCount())
                                    {
                                        if(itemFromInv.getCount()> getTransferRate(coinInPedestal))
                                        {
                                            ItemStack copyIncoming = itemFromInv.copy();
                                            copyIncoming.setCount(getTransferRate(coinInPedestal));
                                            TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                            if(pedestalInv instanceof TilePedestal) {
                                                ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                            }
                                            int counted = itemFromInv.getCount()-getTransferRate(coinInPedestal);
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
                                        if(leftTillFilled >= getTransferRate(coinInPedestal))
                                        {
                                            ItemStack copyIncoming = itemFromInv.copy();
                                            copyIncoming.setCount(getTransferRate(coinInPedestal));
                                            TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                            if(pedestalInv instanceof TilePedestal) {
                                                ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                            }
                                            int counted = itemFromInv.getCount()-getTransferRate(coinInPedestal);
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
    }

    private int ticker = 0;
    public void update(World world, BlockPos posOfPedestal, BlockPos posOfInventory, ItemStack coinInPedestal)
    {
        ticker++;
        if(ticker>=10)
        {
            upgradeAction(world, posOfPedestal, posOfInventory, coinInPedestal);
            ticker=0;
        }
    }


    PotionEffect potionEffect = new PotionEffect(MobEffects.LUCK);
    public PotionEffect getPotionEffectFromStack(ItemStack stack)
    {
        if(stack.hasTagCompound())
        {
            potionEffect = PotionEffect.readCustomPotionEffectFromNBT(stack.getTagCompound().getCompoundTag("coineffect"));
        }
        return potionEffect;
    }
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s2 = getPotionEffectFromStack(stack).getAmplifier()+1;

        String tr = "";
        switch (s2)
        {
            case 0:
                tr = "1";
                break;
            case 1:
                tr = "4";
                break;
            case 2:
                tr = "8";
                break;
            case 3:
                tr = "16";
                break;
            case 4:
                tr = "32";
                break;
            default:
                tr = "64";
        }
        tooltip.add("Item Stack Import Upgrade");
        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("coineffect"))
                tooltip.add("Transfer Rate: " + tr);
        }
        else
        {
            tooltip.add("Transfer Rate: 1");
        }
    }


}
