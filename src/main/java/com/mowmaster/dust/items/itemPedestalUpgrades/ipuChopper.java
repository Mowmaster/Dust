package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import com.mowmaster.dust.enchantments.EnchantmentUpgradeRange;
import com.mowmaster.dust.enchantments.EnchantmentUpgradeTransferRate;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.*;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;
import static net.minecraft.block.BlockDirectional.FACING;

public class ipuChopper extends ipuBasic
{
    public int rangeWidth = 0;
    public int rangeHeight = 0;
    public int transferSpeed = 0;

    public ipuChopper(String unlocName, String registryName)
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
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return !Enchantments.FORTUNE.equals(enchantment) && super.canApplyAtEnchantingTable(stack, enchantment);
        //return !EnchantmentRegistry.UPGRADES.equals(enchantment.type) || enchantment.equals(Enchantments.FORTUNE) || enchantment.equals(Enchantments.SILK_TOUCH) && super.canApplyAtEnchantingTable(stack, enchantment);
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

    public BlockPos getNegRangePos(World world,BlockPos posOfPedestal, int intWidth, int intHeight)
    {
        IBlockState state = world.getBlockState(posOfPedestal);
        EnumFacing enumfacing = state.getValue(FACING);
        BlockPos blockBelow = posOfPedestal;
        switch (enumfacing)
        {
            case UP:
                return blockBelow.add(-intWidth,0,-intWidth);
            case DOWN:
                return blockBelow.add(-intWidth,-intHeight,-intWidth);
            case NORTH:
                return blockBelow.add(-intWidth,-intWidth,-intHeight);
            case SOUTH:
                return blockBelow.add(-intWidth,-intWidth,0);
            case EAST:
                return blockBelow.add(0,-intWidth,-intWidth);
            case WEST:
                return blockBelow.add(-intHeight,-intWidth,-intWidth);
            default:
                return blockBelow;
        }
    }

    public BlockPos getPosRangePos(World world,BlockPos posOfPedestal, int intWidth, int intHeight)
    {
        IBlockState state = world.getBlockState(posOfPedestal);
        EnumFacing enumfacing = state.getValue(FACING);
        BlockPos blockBelow = posOfPedestal;
        switch (enumfacing)
        {
            case UP:
                return blockBelow.add(intWidth,intHeight,intWidth);
            case DOWN:
                return blockBelow.add(intWidth,0,intWidth);
            case NORTH:
                return blockBelow.add(intWidth,intWidth,0);
            case SOUTH:
                return blockBelow.add(intWidth,intWidth,intHeight);
            case EAST:
                return blockBelow.add(intHeight,intWidth,intWidth);
            case WEST:
                return blockBelow.add(0,intWidth,intWidth);
            default:
                return blockBelow;
        }
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


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s3 = getRangeWidth(stack);
        int s4 = getRangeHeight(stack);
        String s5 = "";

        switch (getTransferSpeed(stack))
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

        String tr = "" + (s3+s3+1) + "";
        String trr = "" + (s4+1) + "";

        tooltip.add(TextFormatting.GOLD + "Chopper Upgrade");


        if(s3>0)
        {
            tooltip.add("Effected Area: " + tr+"x"+tr+"x"+trr);
        }
        else
        {
            tooltip.add("Effected Are: " + tr+"x"+tr+"x"+trr);
        }

        if(stack.isItemEnchanted() && getTransferSpeed(stack) >0)
        {
            tooltip.add("Transfer Speed: " + s5);
        }
        else
        {
            tooltip.add("Transfer Speed: Normal Speed");
        }
    }

    public int ticked = 0;

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int rangeWidth = getRangeWidth(coinInPedestal);
        int rangeHeight = getRangeHeight(coinInPedestal);
        int speed = getTransferSpeed(coinInPedestal);

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
                            upgradeAction(world, itemInPedestal, coinInPedestal, blockToChopPos, blockToChop);
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

    public void upgradeAction(World world, ItemStack itemInPedestal, ItemStack coinInPedestal, BlockPos blockToChopPos, IBlockState blockToChop)
    {
        WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
        FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(worldServer);
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

        if(blockToChop.getBlock().isWood(world,blockToChopPos) || blockToChop.getBlock().isLeaves(blockToChop,world,blockToChopPos))
        {
            if(fakePlayer.canHarvestBlock(blockToChop))
            {
                blockToChop.getBlock().harvestBlock(world, fakePlayer, blockToChopPos, blockToChop, null, fakePlayer.getHeldItemMainhand());
            }
            blockToChop.getBlock().removedByPlayer(blockToChop,world,blockToChopPos,fakePlayer,false);
        }
    }


}
