package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuExpAnvil extends ipuBasicExpUpgrade
{
    public int operationalSpeed = 0;


    public ipuExpAnvil(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    public int getExpBuffer(ItemStack stack)
    {
        int value = 0;
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                value = 30;//30
                break;
            case 1:
                value=44;//44
                break;
            case 2:
                value = 58;//58
                break;
            case 3:
                value = 72;//72
                break;
            case 4:
                value = 86;//86
                break;
            case 5:
                value=100;//100
                break;
            default: value=30;
        }

        return  value;
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

    public float getEnchantmentPowerFromSorroundings(World world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {

        float enchantPower = 0;
        int getMaxEnchantLevel = getExpBuffer(coinInPedestal);

        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (i > -2 && i < 2 && j == -1) {
                    j = 2;
                }
                for (int k = 0; k <= 2; ++k) {
                    BlockPos blockpos = posOfPedestal.add(i, k, j);
                    Block blockNearBy = world.getBlockState(blockpos).getBlock();
                    if (blockNearBy.getEnchantPowerBonus(world, blockpos)>0)
                    {
                        enchantPower +=blockNearBy.getEnchantPowerBonus(world, blockpos);
                    }
                }
            }
        }

        if((int)(enchantPower*2) > getMaxEnchantLevel)
        {
            enchantPower = (float)(getMaxEnchantLevel/2);
        }

        return enchantPower;
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world, itemInPedestal, coinInPedestal, pedestalPos);
            }
        }
    }

    public void upgradeAction(World world, ItemStack itemInPedestal, ItemStack coinInPedestal, BlockPos posOfPedestal)
    {
        int getMaxXpValue = getExpCountByLevel(getExpBuffer(coinInPedestal));
        setMaxXP(coinInPedestal, getMaxXpValue);
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
                            TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                            if(pedestalInv instanceof TilePedestal) {
                                if(!((TilePedestal) pedestalInv).hasItem())
                                {
                                    if(itemFromInv.isItemEnchantable() || itemFromInv.getItem().equals(Items.BOOK))
                                    {
                                        //This is Book Shelf Enchanting level, not enchantment level (15 bookshelfves = 30 levels of enchantability)
                                        float level = getEnchantmentPowerFromSorroundings(world,posOfPedestal,coinInPedestal);
                                        int currentlyStoredExp = ((TilePedestal) pedestalInv).getStoredValueForUpgrades();
                                        int expNeeded = getExpCountByLevel((int)(level * 2));
                                        if(currentlyStoredExp >= expNeeded)
                                        {
                                            //Enchanting Code Here
                                            Random rand = new Random();
                                            ItemStack itemToEnchant = itemFromInv.copy();
                                            itemToEnchant.setCount(1);
                                            ItemStack stackToReturn = EnchantmentHelper.addRandomEnchantment(rand,itemToEnchant ,(int)(level * 2) ,true );
                                            if(!stackToReturn.isEmpty())
                                            {
                                                int getExpLeftInPedestal = currentlyStoredExp - expNeeded;
                                                ((TilePedestal) pedestalInv).setStoredValueForUpgrades(getExpLeftInPedestal);
                                                handler.extractItem(i,stackToReturn.getCount() ,false );
                                                world.playSound((EntityPlayer)null, posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 0.25F, 1.0F);
                                                ((TilePedestal) pedestalInv).addItem(stackToReturn);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        ItemStack toReturn = itemFromInv.copy();
                                        handler.extractItem(i,toReturn.getCount() ,false );
                                        ((TilePedestal) pedestalInv).addItem(toReturn);
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
        String s5 = "";
        int buffer = getExpBuffer(stack);
        String xp = ""+ getExpLevelFromCount(getXPStored(stack)) +"";
        tooltip.add(TextFormatting.GREEN + "Exp Levels Stored: "+xp);

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

        tooltip.add(TextFormatting.GOLD + "Enchanter Upgrade");
        tooltip.add(TextFormatting.GREEN + "Exp Levels Stored: "+xp);

        tooltip.add(TextFormatting.AQUA + "Exp Buffer Capacity: " + buffer + " Levels");


        if(stack.hasTagCompound())
        {
            if(stack.isItemEnchanted())
            {
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
                tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
            }
        }
        else
        {
            tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
        }
    }



    @Override
    public void onRandomDisplayTick(TilePedestal pedestal, IBlockState stateIn, World world, BlockPos pos, Random rand)
    {
        if(!world.isBlockPowered(pos))
        {
            for (int i = -2; i <= 2; ++i)
            {
                for (int j = -2; j <= 2; ++j)
                {
                    if (i > -2 && i < 2 && j == -1)
                    {
                        j = 2;
                    }

                    if (rand.nextInt(16) == 0)
                    {
                        for (int k = 0; k <= 2; ++k)
                        {
                            BlockPos blockpos = pos.add(i, k, j);

                            if (net.minecraftforge.common.ForgeHooks.getEnchantPower(world, blockpos) > 0)
                            {
                                if (!world.isAirBlock(pos.add(i / 2, 0, j / 2)))
                                {
                                    break;
                                }

                                world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, (double)pos.getX() + 0.5D, (double)pos.getY() + 2.0D, (double)pos.getZ() + 0.5D, (double)((float)i + rand.nextFloat()) - 0.5D, (double)((float)k - rand.nextFloat() - 1.0F), (double)((float)j + rand.nextFloat()) - 0.5D);
                            }
                        }
                    }
                }
            }
        }
    }

}
