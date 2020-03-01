package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
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

public class ipuBreaker extends ipuBasic
{
    /*
    Breaks Block, at Range and Puts FIRST Block Result in pedestal slot(Spawn rest of drops list in world???)
     */
    public int range = 0;

    public ipuBreaker(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
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

        FakePlayer fakePlayer = FakePlayerFactory.getMinecraft((WorldServer) world);
        fakePlayer.setPosition(posOfPedestal.getX(),posOfPedestal.getY(),posOfPedestal.getZ());
        ItemStack pickaxe = new ItemStack(Items.DIAMOND_PICKAXE,1);
        BlockPos posOfBlock = getPosOfBlockBelow(world,posOfPedestal,range);
        IBlockState blockToBreak = world.getBlockState(posOfBlock);

        /*
        BREAKS BLOCKS AND DROPS THEM IN WORLD FOR PICKUP LATER
         */
        if(!blockToBreak.getBlock().isAir(blockToBreak,world,posOfBlock) && blockToBreak.getBlockHardness(world,posOfBlock) != -1.0F)
        {
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

        String s5 = getOperationSpeedString(stack);

        tooltip.add(TextFormatting.GOLD + "Block Breaker Upgrade");

        if(stack.isItemEnchanted() && s3 > 0)
        {
            tooltip.add(TextFormatting.WHITE + "Breaker Range: " + trr);
        }
        else
        {
            tooltip.add(TextFormatting.WHITE + "Breaker Range: " + trr);
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
