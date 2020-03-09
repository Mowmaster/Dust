package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import net.minecraft.block.BlockState;
import net.minecraft.block.LogBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemUpgradeChopper extends ItemUpgradeBase
{

    public int rangeWidth = 0;
    public int rangeHeight = 0;

    public ItemUpgradeChopper(Properties builder) {super(builder.group(dust.itemGroup));}

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
                        BlockState blockToChop = world.getBlockState(blockToChopPos);
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

    public void upgradeAction(World world, ItemStack itemInPedestal, ItemStack coinInPedestal, BlockPos blockToChopPos, BlockState blockToChop, BlockPos posOfPedestal)
    {
        if(!blockToChop.getBlock().isAir(blockToChop,world,blockToChopPos) && blockToChop.getBlock().isIn(BlockTags.LOGS) || blockToChop.getBlock().isIn(BlockTags.LEAVES))
        {
            FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(world.getServer().getWorld(world.getDimension().getType()));
            fakePlayer.setPosition(posOfPedestal.getX(),posOfPedestal.getY(),posOfPedestal.getZ());
            ItemStack choppingAxe = new ItemStack(Items.DIAMOND_AXE,1);
            if(!itemInPedestal.isEmpty())
            {
                fakePlayer.setHeldItem(Hand.MAIN_HAND,itemInPedestal);
            }
            else
            {
                if(EnchantmentHelper.getEnchantments(coinInPedestal).containsKey(Enchantments.SILK_TOUCH))
                {
                    choppingAxe.addEnchantment(Enchantments.SILK_TOUCH,1);
                    fakePlayer.setHeldItem(Hand.MAIN_HAND,choppingAxe);
                }
                else if (EnchantmentHelper.getEnchantments(coinInPedestal).containsKey(Enchantments.FORTUNE))
                {
                    int lvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE,coinInPedestal);
                    choppingAxe.addEnchantment(Enchantments.FORTUNE,lvl);
                    fakePlayer.setHeldItem(Hand.MAIN_HAND,choppingAxe);
                }
                else
                {
                    fakePlayer.setHeldItem(Hand.MAIN_HAND,choppingAxe);
                }
            }

            if(fakePlayer.canHarvestBlock(blockToChop))
            {
                blockToChop.getBlock().harvestBlock(world, fakePlayer, blockToChopPos, blockToChop, null, fakePlayer.getHeldItemMainhand());
            }
            blockToChop.getBlock().removedByPlayer(blockToChop,world,blockToChopPos,fakePlayer,false,null);
        }
    }

    /*@Override
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
    }*/

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "Tree Chopper Upgrade"));
    }

    public static final Item CHOPPER = new ItemUpgradeChopper(new Properties().maxStackSize(64).group(dust.itemGroup)).setRegistryName(new ResourceLocation(MODID, "coin/chopper"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(CHOPPER);
    }


}
