package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityCow;
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
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuExpBottler extends ipuBasicExpUpgrade
{
    public int operationalSpeed = 0;
    public int maxXP = 160;
    public int bottlingRate = 1;


    public ipuExpBottler(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return super.isBookEnchantable(stack, book);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxXP() {
        return maxXP;
    }

    public int getTransferRate(ItemStack stack)
    {
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

    public int getOperationSpeed(ItemStack stack)
    {
        switch (getTransferRateModifier(stack))
        {
            case 0:
                operationalSpeed = 20;//normal speed
                break;
            case 1:
                operationalSpeed=10;//2x faster
                break;
            case 2:
                operationalSpeed = 5;//4x faster
                break;
            case 3:
                operationalSpeed = 3;//6x faster
                break;
            case 4:
                operationalSpeed = 2;//10x faster
                break;
            case 5:
                operationalSpeed=1;//20x faster
                break;
            default: operationalSpeed=20;
        }

        return  operationalSpeed;
    }



    public int ticked = 0;

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
        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);

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
                                            int currentlyStoredExp = ((TilePedestal) pedestalInv).getStoredValueForUpgrades();
                                            if(currentlyStoredExp > 0)
                                            {
                                                if(currentlyStoredExp >= rate)
                                                {
                                                    int getExpLeftInPedestal = currentlyStoredExp - rate;
                                                    world.playSound((EntityPlayer)null, posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ(), SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.BLOCKS, 0.5F, 1.0F);
                                                    ((TilePedestal) pedestalInv).setStoredValueForUpgrades(getExpLeftInPedestal);
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
    public void actionOnColideWithBlock(World world, TilePedestal tilePedestal, BlockPos posPedestal, IBlockState state, Entity entityIn)
    {
        if(entityIn instanceof EntityXPOrb)
        {
            EntityXPOrb getXPFromList = ((EntityXPOrb)entityIn);
            world.playSound((EntityPlayer)null, posPedestal.getX(), posPedestal.getY(), posPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.5F, 1.0F);
            int currentlyStoredExp = tilePedestal.getStoredValueForUpgrades();
            if(currentlyStoredExp < getMaxXP())
            {
                int value = getXPFromList.getXpValue();
                getXPFromList.setDead();
                tilePedestal.setStoredValueForUpgrades(currentlyStoredExp + value);
            }
        }
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int trr = getTransferRate(stack);
        String tr = ""+ trr +"";
        String s5 = "";

        switch (getOperationSpeed(stack))
        {
            case 1:
                s5 = "20x Faster";
                break;
            case 2:
                s5="10x Faster";
                break;
            case 3:
                s5 = "6x Faster";
                break;
            case 5:
                s5 = "4x Faster";
                break;
            case 10:
                s5 = "2x Faster";
                break;
            case 20:
                s5="Normal Speed";
                break;
            default: s5="Normal Speed";
        }



        tooltip.add(TextFormatting.GOLD + "Exp Bottler Upgrade");

        tooltip.add(TextFormatting.AQUA + "Exp Buffer Capacity: 10 Levels");

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

            if(stack.isItemEnchanted())
            {
                if(stack.isItemEnchanted() && getOperationSpeed(stack) >0)
                {
                    tooltip.add("Operational Speed: " + s5);
                }
                else
                {
                    tooltip.add("Operational Speed: Normal Speed");
                }
            }
            else
            {
                tooltip.add("Operational Speed: Normal Speed");
            }
        }
        else
        {
            tooltip.add("Bottled per Opperation: 1");
            tooltip.add("Operational Speed: Normal Speed");
        }
    }



}
