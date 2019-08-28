package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.block.BlockDirectional.FACING;

public class ipuBasic extends Item
{
    public ipuBasic() {}

    public boolean isFilter;

    public boolean hasEnchant;

    public boolean hasEffect;

    public int intRate = 0;


    public boolean hasEnchant(ItemStack stack)
    {
       hasEnchant =  stack.isItemEnchanted();
       return hasEnchant;
    }

    public int getRangeModifier(ItemStack stack)
    {
        int range = 0;
        if(stack.isItemEnchanted())
        {
           range = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentRange,stack);
        }
        return range;
    }

    public boolean hasCoinEffect(ItemStack stack)
    {
        hasEffect = false;
        if(stack.hasTagCompound())
        {
            hasEffect = stack.getTagCompound().hasKey("coineffect");
        }
        return hasEffect;
    }

    public PotionEffect getCoinEffect(ItemStack stack)
    {
        PotionEffect potionEffect = new PotionEffect(MobEffects.LUCK);


            if(hasCoinEffect(stack))
            {
                potionEffect = PotionEffect.readCustomPotionEffectFromNBT(stack.getTagCompound().getCompoundTag("coineffect"));
            }

        return potionEffect;
    }

    /*
    PotionEffect potionEffect = new PotionEffect(MobEffects.LUCK);
    public PotionEffect getPotionEffectFromStack(ItemStack stack)
    {
        if(stack.hasTagCompound())
        {
            potionEffect = PotionEffect.readCustomPotionEffectFromNBT(stack.getTagCompound().getCompoundTag("coineffect"));
        }
        return potionEffect;
    }
     */

    public int getPotency(ItemStack stack)
    {
        return getCoinEffect(stack).getAmplifier()+1;
    }

    public int getRateModifier(Potion effect, ItemStack stack)
    {
        if(hasCoinEffect(stack))
        {
            if(getCoinEffect(stack).getPotion().equals(effect))
            {
                intRate = getPotency(stack);
            }
        }
        else intRate = 0;

        return intRate;
    }

    public ItemStack getStackInPedestal(World world, BlockPos posOfPedestal)
    {
        ItemStack stackInPedestal = ItemStack.EMPTY;
        TileEntity pedestalInventory = world.getTileEntity(posOfPedestal);
        if(pedestalInventory instanceof TilePedestal) {
            stackInPedestal = pedestalInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN).getStackInSlot(0);
        }

        return stackInPedestal;
    }

    public void removeFromPedestal(World world, BlockPos posOfPedestal,int count)
    {
        ItemStack stackInPedestal = ItemStack.EMPTY;
        TileEntity pedestalInventory = world.getTileEntity(posOfPedestal);
        if(pedestalInventory instanceof TilePedestal) {
            ((TilePedestal) pedestalInventory).removeItem(count);
        }
    }

    public boolean doItemsMatch(ItemStack stackPedestal, ItemStack itemStackIn)
    {
        if(!stackPedestal.equals(ItemStack.EMPTY))
        {
            if(itemStackIn.getHasSubtypes())
            {
                if(itemStackIn.getItem().equals(stackPedestal.getItem()) && itemStackIn.getMetadata()==stackPedestal.getMetadata())
                {
                    return true;
                }
                else return false;
            }
            else if(itemStackIn.hasTagCompound())
            {
                NBTTagCompound itemIn = itemStackIn.getTagCompound();
                NBTTagCompound itemStored = stackPedestal.getTagCompound();
                if(itemIn.equals(itemStored) && itemStackIn.getItem().equals(stackPedestal.getItem()))
                {
                    return true;
                }
                else return false;
            }
            else
            {
                if(itemStackIn.getItem().equals(stackPedestal.getItem()))
                {
                    return true;
                }
            }
        }
        else{return true;}


        return false;
    }

    public BlockPos getPosOfBlockBelow(World world,BlockPos posOfPedestal, int numBelow)
    {
        IBlockState state = world.getBlockState(posOfPedestal);
        EnumFacing enumfacing = state.getValue(FACING);
        BlockPos blockBelow = posOfPedestal;
        switch (enumfacing)
        {
            case UP:
                return blockBelow.add(0,-numBelow,0);
            case DOWN:
                return blockBelow.add(0,numBelow,0);
            case NORTH:
                return blockBelow.add(0,0,numBelow);
            case SOUTH:
                return blockBelow.add(0,0,-numBelow);
            case EAST:
                return blockBelow.add(-numBelow,0,0);
            case WEST:
                return blockBelow.add(numBelow,0,0);
            default:
                return blockBelow;
        }
    }
}
