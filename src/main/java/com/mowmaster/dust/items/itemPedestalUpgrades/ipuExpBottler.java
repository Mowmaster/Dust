package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuExpBottler extends ipuBasicExpUpgrade
{
    public ipuExpBottler(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    public int getTransferRate(ItemStack stack)
    {
        int bottlingRate = 1;
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                bottlingRate = 1;
                break;
            case 1:
                bottlingRate=2;
                break;
            case 2:
                bottlingRate = 4;
                break;
            case 3:
                bottlingRate = 8;
                break;
            case 4:
                bottlingRate = 12;
                break;
            case 5:
                bottlingRate=16;
                break;
            default: bottlingRate=1;
        }

        return  bottlingRate;
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world, coinInPedestal, pedestalPos);
            }
        }
    }

    public void upgradeAction(World world, ItemStack coinInPedestal, BlockPos posOfPedestal)
    {
        setMaxXP(coinInPedestal,getExpCountByLevel(15) );
        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);
        ItemStack itemFromInv = ItemStack.EMPTY;

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
                        int i = getNextSlotWithItems(invToPullFrom,getPedestalFacing(world, posOfPedestal),getStackInPedestal(world,posOfPedestal));
                        if(i>=0)
                        {
                            itemFromInv = handler.getStackInSlot(i);
                            int slotCount = itemFromInv.getCount();
                            if(itemFromInv.getItem().equals(Items.GLASS_BOTTLE))
                            {
                                //BottlingCodeHere
                                //11 exp per bottle
                                int modifier = getTransferRate(coinInPedestal);

                                //If we can extract the correct amount of bottles(If it returns empty then it CANT work)
                                if(!(handler.extractItem(i,modifier ,true ).equals(ItemStack.EMPTY)))
                                {
                                    int rate = (modifier * 11);
                                    ItemStack getBottle = new ItemStack(Items.EXPERIENCE_BOTTLE,modifier);
                                    TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                    if(pedestalInv instanceof TilePedestal) {
                                        if(((TilePedestal) pedestalInv).canAcceptItems(getBottle)>=rate)
                                        {
                                            int currentlyStoredExp = getXPStored(coinInPedestal);
                                            if(currentlyStoredExp > 0)
                                            {
                                                if(currentlyStoredExp >= rate)
                                                {
                                                    int getExpLeftInPedestal = currentlyStoredExp - rate;
                                                    world.playSound((EntityPlayer)null, posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ(), SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.BLOCKS, 0.25F, 1.0F);
                                                    setXPStored(coinInPedestal,getExpLeftInPedestal);
                                                    handler.extractItem(i,modifier ,false );
                                                    ((TilePedestal) pedestalInv).addItem(getBottle);
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
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String tr = "" + getTransferRate(stack) + "";
        String s5 = getOperationSpeedString(stack);
        String xp = "" + getExpLevelFromCount(getXPStored(stack)) + "";

        tooltip.add(TextFormatting.GOLD + "Exp Bottler Upgrade");
        tooltip.add(TextFormatting.GREEN + "Exp Levels Stored: "+xp);
        tooltip.add(TextFormatting.AQUA + "Exp Buffer Capacity: 15 Levels");

        if(stack.hasTagCompound())
        {
            if(getTransferRate(stack)>0)
            {
                tooltip.add("Bottled per Opperation: " + tr);
            }
            else
            {
                tooltip.add("Bottled per Opperation: 1");
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
            tooltip.add("Bottled per Opperation: 1");
            tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
        }
    }



}
