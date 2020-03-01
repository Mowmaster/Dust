package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.*;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuChopper extends ipuBasic
{
    public int rangeWidth = 0;
    public int rangeHeight = 0;

    public ipuChopper(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    public int getRangeWidth(ItemStack stack)
    {
        int rW = getRangeModifier(stack);
        rangeWidth = ((rW)+1);
        return  rangeWidth;
    }

    public int getRangeHeight(ItemStack stack)
    {
        int rH = getRangeModifier(stack);
        rangeHeight = ((rH*6)+4);
        return rangeHeight;
    }

    public int ticked = 0;

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int rangeWidth = getRangeWidth(coinInPedestal);
        int rangeHeight = getRangeHeight(coinInPedestal);
        int speed = getOperationSpeed(coinInPedestal);

        BlockPos negNums = getNegRangePos(world,pedestalPos,rangeWidth,rangeHeight);
        BlockPos posNums = getPosRangePos(world,pedestalPos,rangeWidth,rangeHeight);

        if(!world.isBlockPowered(pedestalPos)) {
            for (int x = negNums.getX(); x <= posNums.getX(); x++) {
                for (int z = negNums.getZ(); z <= posNums.getZ(); z++) {
                    for (int y = negNums.getY(); y <= posNums.getY(); y++) {
                        BlockPos blockToChopPos = new BlockPos(x, y, z);
                        //BlockPos blockToChopPos = this.getPos().add(x, y, z);
                        IBlockState blockToChop = world.getBlockState(blockToChopPos);
                        if (tick%speed == 0) {
                            ticked++;
                        }

                        if(ticked > 84)
                        {
                            upgradeAction(world, itemInPedestal, coinInPedestal, blockToChopPos, blockToChop, pedestalPos);
                            ticked=0;
                        }
                        else
                        {
                            ticked++;
                        }
                    }
                }
            }
        }
    }

    public void upgradeAction(World world, ItemStack itemInPedestal, ItemStack coinInPedestal, BlockPos blockToChopPos, IBlockState blockToChop, BlockPos posOfPedestal)
    {
        if(!blockToChop.getBlock().isAir(blockToChop,world,blockToChopPos) && blockToChop.getBlock().isWood(world,blockToChopPos) || blockToChop.getBlock().isLeaves(blockToChop,world,blockToChopPos))
        {
            FakePlayer fakePlayer = FakePlayerFactory.getMinecraft((WorldServer) world);
            fakePlayer.setPosition(posOfPedestal.getX(),posOfPedestal.getY(),posOfPedestal.getZ());
            ItemStack choppingAxe = new ItemStack(Items.DIAMOND_AXE,1);
            if(!itemInPedestal.isEmpty())
            {
                fakePlayer.setHeldItem(EnumHand.MAIN_HAND,itemInPedestal);
            }
            else
            {
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
            }

            if(fakePlayer.canHarvestBlock(blockToChop))
            {
                blockToChop.getBlock().harvestBlock(world, fakePlayer, blockToChopPos, blockToChop, null, fakePlayer.getHeldItemMainhand());
            }
            blockToChop.getBlock().removedByPlayer(blockToChop,world,blockToChopPos,fakePlayer,false);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s3 = getRangeWidth(stack);
        int s4 = getRangeHeight(stack);
        String s5 = getOperationSpeedString(stack);

        String tr = "" + (s3+s3+1) + "";
        String trr = "" + (s4+1) + "";

        tooltip.add(TextFormatting.GOLD + "Chopper Upgrade");

        if(stack.isItemEnchanted() && s3>0)
        {
            tooltip.add(TextFormatting.WHITE + "Effected Area: " + tr+"x"+tr+"x"+trr);
        }
        else
        {
            tooltip.add(TextFormatting.WHITE + "Effected Area: " + tr+"x"+tr+"x"+trr);
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


}
