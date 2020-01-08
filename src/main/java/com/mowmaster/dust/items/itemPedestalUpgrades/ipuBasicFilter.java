package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import com.mowmaster.dust.enchantments.EnchantmentUpgradeTransferRate;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Random;

import static net.minecraft.block.BlockDirectional.FACING;

public class ipuBasicFilter extends ipuBasic
{
    public ipuBasicFilter() {}

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public int getItemEnchantability()
    {
        return 0;
    }

}
