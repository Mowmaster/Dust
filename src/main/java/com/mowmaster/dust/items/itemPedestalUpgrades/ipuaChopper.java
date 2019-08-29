package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class ipuaChopper extends ipuBasic{


    public ipuaChopper(){}

    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack coinInPedestal, BlockPos blockToChopPos, IBlockState blockToChop)
    {
            WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
            FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(worldServer);
            ItemStack choppingAxe = new ItemStack(Items.DIAMOND_AXE,1);
            if(EnchantmentHelper.getEnchantments(coinInPedestal).containsKey(Enchantments.SILK_TOUCH))
            {
                choppingAxe.addEnchantment(Enchantments.SILK_TOUCH,1);
                fakePlayer.setHeldItem(EnumHand.MAIN_HAND,choppingAxe);
            }
            else if (EnchantmentHelper.getEnchantments(coinInPedestal).containsKey(Enchantments.FORTUNE))
            {
                int lvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE,coinInPedestal);
                choppingAxe.addEnchantment(Enchantments.FORTUNE,lvl);
                fakePlayer.setHeldItem(EnumHand.MAIN_HAND,choppingAxe);
            }
            else
            {
                fakePlayer.setHeldItem(EnumHand.MAIN_HAND,choppingAxe);
            }




        if(blockToChop.getMaterial().equals(Material.WOOD) && blockToChop.getBlock().getUnlocalizedName().toLowerCase().contains("log")
                || blockToChop.getMaterial().equals(Material.LEAVES) && blockToChop.getBlock().getUnlocalizedName().toLowerCase().contains("leaves")
                || blockToChop.getMaterial().equals(Material.VINE) && blockToChop.getBlock().getUnlocalizedName().toLowerCase().contains("vine"))
        {
            blockToChop.getBlock().harvestBlock(world, fakePlayer, blockToChopPos, blockToChop, null, fakePlayer.getHeldItemMainhand());
            world.setBlockToAir(blockToChopPos);
        }
    }
}