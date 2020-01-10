package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuBreaker extends ipuBasic
{
    /*
    Breaks Block, at Range and Puts FIRST Block Result in pedestal slot(Spawn rest of drops list in world???)
     */
    public int range = 0;
    public int operationalSpeed = 0;

    public ipuBreaker(String unlocName, String registryName)
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

    public int getRange(ItemStack stack)
    {
        switch (getRangeModifier(stack))
        {
            case 0:
                range = 1;
                break;
            case 1:
                range = 2;
                break;
            case 2:
                range = 4;
                break;
            case 3:
                range = 8;
                break;
            case 4:
                range = 12;
                break;
            case 5:
                range = 16;
                break;
            default: range = 1;
        }

        return  range;
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

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world,pedestalPos,itemInPedestal,coinInPedestal);
            }
        }
    }

    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack itemInPedestal, ItemStack coinOnPedestal)
    {
        int range = getRange(coinOnPedestal);

        WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
        FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(worldServer);
        ItemStack pickaxe = new ItemStack(Items.DIAMOND_PICKAXE,1);
        BlockPos posOfBlock = getPosOfBlockBelow(world,posOfPedestal,range);
        IBlockState blockToBreak = world.getBlockState(posOfBlock);

        /*
        BREAKS BLOCKS AND DROPS THEM IN WORLD FOR PICKUP LATER
         */

        if(itemInPedestal.getItem() instanceof ItemPickaxe)
        {
            fakePlayer.setHeldItem(EnumHand.MAIN_HAND,itemInPedestal);
        }
        else
        {
            if(EnchantmentHelper.getEnchantments(coinOnPedestal).containsKey(Enchantments.SILK_TOUCH))
            {
                pickaxe.addEnchantment(Enchantments.SILK_TOUCH,1);
                fakePlayer.setHeldItem(EnumHand.MAIN_HAND,pickaxe);
            }
            else if (EnchantmentHelper.getEnchantments(coinOnPedestal).containsKey(Enchantments.FORTUNE))
            {
                int lvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE,coinOnPedestal);
                pickaxe.addEnchantment(Enchantments.FORTUNE,lvl);
                fakePlayer.setHeldItem(EnumHand.MAIN_HAND,pickaxe);
            }
            else
            {
                fakePlayer.setHeldItem(EnumHand.MAIN_HAND,pickaxe);
            }
        }

        if(blockToBreak.getBlockHardness(world,posOfBlock) != -1.0F)
        {
            if(fakePlayer.canHarvestBlock(blockToBreak))
            {
                blockToBreak.getBlock().harvestBlock(world, fakePlayer, posOfBlock, blockToBreak, null, fakePlayer.getHeldItemMainhand());
            }
            blockToBreak.getBlock().removedByPlayer(blockToBreak,world,posOfBlock,fakePlayer,false);
        }

        /*if(itemInPedestal.isEmpty())//If pedestal inv is empty
        {
            int fortune = 0;
            Random rn = new Random();
            IBlockState blocky = world.getBlockState(getPosOfBlockBelow(rangeOfBreak));
            ItemStack getDrops = ItemStack.EMPTY;



            if(!world.getBlockState(getPosOfBlockBelow(rangeOfBreak)).getBlock().equals(Blocks.AIR))
            {
                if(hasCoin())
                {
                    if(coin.getStackInSlot(0).getItem().equals(ItemRegistry.breakerUpgrade))
                    {
                        if(coin.getStackInSlot(0).isItemEnchanted())
                        {
                            if(EnchantmentHelper.getEnchantments(coin.getStackInSlot(0)).containsKey(Enchantments.SILK_TOUCH))
                            {
                                getDrops = new ItemStack(blocky.getBlock());
                            }
                            else if(EnchantmentHelper.getEnchantments(coin.getStackInSlot(0)).containsKey(Enchantments.FORTUNE))
                            {
                                fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE,coin.getStackInSlot(0)) + 1;
                                Item dropItem = blocky.getBlock().getItemDropped(blocky,rn,fortune);
                                int metaDropped = blocky.getBlock().damageDropped(blocky);
                                int countDropped = blocky.getBlock().quantityDropped(blocky,fortune,rn);
                                if(blocky.getBlock().getItemDropped(blocky,rn,fortune)!=null)
                                {
                                    if(dropItem.getHasSubtypes())
                                    {
                                        getDrops = new ItemStack(dropItem,countDropped,metaDropped);
                                    }
                                    else getDrops = new ItemStack(dropItem,countDropped);
                                }
                            }
                            else
                            {
                                Item dropItem = blocky.getBlock().getItemDropped(blocky,rn,0);
                                int metaDropped = blocky.getBlock().damageDropped(blocky);
                                int countDropped = blocky.getBlock().quantityDropped(blocky,0,rn);
                                if(blocky.getBlock().getItemDropped(blocky,rn,0)!=null)
                                {

                                    if(dropItem.getHasSubtypes())
                                    {
                                        getDrops = new ItemStack(dropItem,countDropped,metaDropped);
                                    }
                                    else getDrops = new ItemStack(dropItem,countDropped);
                                }
                            }
                        }
                        else
                        {
                            Item dropItem = blocky.getBlock().getItemDropped(blocky,rn,0);
                            int metaDropped = blocky.getBlock().damageDropped(blocky);
                            int countDropped = blocky.getBlock().quantityDropped(blocky,0,rn);
                            if(blocky.getBlock().getItemDropped(blocky,rn,0)!=null)
                            {
                                if(dropItem.getHasSubtypes())
                                {
                                    getDrops = new ItemStack(dropItem,countDropped,metaDropped);
                                }
                                else getDrops = new ItemStack(dropItem,countDropped);
                            }
                        }


                        if(hasItem())
                        {
                            if(doItemsMatch(getDrops))
                            {
                                if(getItemInPedestal().getCount() + getDrops.getCount() <= getMaxStackSize())
                                {
                                    addItem(getDrops);
                                    world.setBlockToAir(getPosOfBlockBelow(rangeOfBreak));
                                }
                            }

                        }
                        else
                        {
                            addItem(getDrops);
                            world.setBlockToAir(getPosOfBlockBelow(rangeOfBreak));
                        }
                    }
                }
            }
        }*/
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s3 = getRange(stack);

        String trr = "" + s3 + "";

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

        tooltip.add(TextFormatting.GOLD + "Block Breaker Upgrade");
        if(stack.hasTagCompound())
        {

            if(stack.isItemEnchanted())
            {
                if(getRange(stack) >0)
                {
                    tooltip.add("Breaker Range: " + trr);
                }
                else
                {
                    tooltip.add("Breaker Range: " + trr);
                }

                if(getOperationSpeed(stack) >0)
                {
                    tooltip.add("Operation Speed: " + s5);
                }
                else
                {
                    tooltip.add("Operation Speed: Normal Speed");
                }
            }
            else
            {
                tooltip.add("Breaker Range: " + trr);
                tooltip.add("Operation Speed: Normal Speed");
            }
        }
        else
        {
            tooltip.add("Breaker Range: " + trr);
            tooltip.add("Operation Speed: Normal Speed");
        }
    }


}
