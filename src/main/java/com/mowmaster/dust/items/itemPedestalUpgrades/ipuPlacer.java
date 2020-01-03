package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuPlacer extends ipuBasic
{
    /*
    Places blocks at a distance using items inside inventory.
    Places blocks below the base of the pedestal. (Big part of the pedestal)
     */
    public int range = 0;
    public int operationalSpeed = 0;

    public ipuPlacer(String unlocName, String registryName)
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
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    public int getRange(ItemStack stack)
    {
        switch (getRangeModifier(stack))
        {
            case 0:
                range = 1;
                break;
            case 1:
                range=2;
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
                range=16;
                break;
            default: range=1;
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

    public IBlockState getState(Block getBlock, ItemStack itemForBlock)
    {
        IBlockState stated = Blocks.AIR.getDefaultState();

        if(itemForBlock.getHasSubtypes())
        {
            stated = getBlock.getStateFromMeta(itemForBlock.getMetadata());
        }
        else
        {
            stated = getBlock.getDefaultState();
        }

        return stated;
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
        if(!itemInPedestal.isEmpty())//hasItem
        {
            /*
            Block Placing stuff here!
            Also should place saplings!
            And Buckets! (Water, Lava, Etc!) [For now] Maybe?
             */
            BlockPos blockPosBelow = getPosOfBlockBelow(world,posOfPedestal,range);//Check if its replaceable instead of checking for air
            Block blockBelow = world.getBlockState(blockPosBelow).getBlock();

            Block block = Block.getBlockFromItem(itemInPedestal.getItem());
            String itemName = itemInPedestal.getUnlocalizedName();
            String displayName = itemInPedestal.getDisplayName();
            String domainName = itemInPedestal.getItem().getRegistryName().getResourceDomain();
            Block block2 = Block.getBlockFromName(itemName.replace("item.",domainName + ":"));
            Block block3 = Block.getBlockFromName(domainName + ":" + displayName.replace(" ","_").toLowerCase());
            IBlockState stated = Blocks.AIR.getDefaultState();
            Block getBlockToUse = Blocks.AIR;
            if(block != Blocks.AIR){
                getBlockToUse = block;
                stated = getState(block,itemInPedestal);
            }
            else if(block2 != Blocks.AIR)
            {
                getBlockToUse = block2;
                stated = getState(block2,itemInPedestal);
            }
            else if(block3 != Blocks.AIR)
            {
                getBlockToUse = block3;
                stated = getState(block3,itemInPedestal);
            }

            //If We have a block
            if(getBlockToUse != Blocks.AIR)
            {
                if(blockBelow.isReplaceable(world,blockPosBelow))
                {
                    //Redstone causes error LOL
                    //Redstone
                    if (replaceable && stack.getItem() == Items.REDSTONE) {
                        world.setBlockState(offsetPos, Blocks.REDSTONE_WIRE.getDefaultState(), 2);
                        return StackUtil.shrink(stack, 1);
                    }

                    if(getBlockToUse instanceof IGrowable)
                    {
                        if(blockBelow.canPlaceBlockAt(world,blockPosBelow))
                        {
                            //Place Sapling???
                            this.removeFromPedestal(world,posOfPedestal,1);
                            world.setBlockState(blockPosBelow,stated);
                            world.playSound((EntityPlayer)null, blockPosBelow.getX(), blockPosBelow.getY(), blockPosBelow.getZ(), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                        }
                    }
                    else
                    {
                        this.removeFromPedestal(world,posOfPedestal,1);
                        world.setBlockState(blockPosBelow,stated);
                        world.playSound((EntityPlayer)null, blockPosBelow.getX(), blockPosBelow.getY(), blockPosBelow.getZ(), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                    }
                }
            }
        }
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

        tooltip.add(TextFormatting.GOLD + "Block Placer Upgrade");
        if(stack.hasTagCompound())
        {

            if(stack.isItemEnchanted())
            {
                if(getRange(stack) >0)
                {
                    tooltip.add("Placer Range: " + trr);
                }
                else
                {
                    tooltip.add("Placer Range: " + trr);
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
                tooltip.add("Placer Range: " + trr);
                tooltip.add("Operation Speed: Normal Speed");
            }
        }
        else
        {
            tooltip.add("Placer Range: " + trr);
            tooltip.add("Operation Speed: Normal Speed");
        }
    }


}
