package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
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

    public ipuPlacer(String unlocName, String registryName)
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

    public IBlockState getState(Block getBlock, ItemStack itemForBlock)
    {
        IBlockState stated = Blocks.AIR.getDefaultState();


        //Redstone
        if(itemForBlock.getItem() == Items.REDSTONE)
        {
            stated = Blocks.REDSTONE_WIRE.getDefaultState();
        }
        else if(itemForBlock.getHasSubtypes())
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
             */
            BlockPos blockPosBelow = getPosOfBlockBelow(world,posOfPedestal,range);//Check if its replaceable instead of checking for air
            Block blockBelow = world.getBlockState(blockPosBelow).getBlock();

            if(blockBelow.isReplaceable(world,blockPosBelow) || blockBelow.isAir(blockBelow.getDefaultState(),world,blockPosBelow))
            {
                FakePlayer fake = FakePlayerFactory.getMinecraft((WorldServer) world);
                fake.setHeldItem(EnumHand.MAIN_HAND,itemInPedestal.copy());

                if(fake.interactionManager.processRightClickBlock(fake, world, fake.getHeldItemMainhand(), EnumHand.MAIN_HAND, blockPosBelow, getPedestalFacing(world,posOfPedestal).getOpposite(), 0.5F, 0.5F, 0.5F).equals(EnumActionResult.SUCCESS))
                {
                    this.removeFromPedestal(world,posOfPedestal,1);
                    world.playSound((EntityPlayer)null, blockPosBelow.getX(), blockPosBelow.getY(), blockPosBelow.getZ(), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                }
            }
        }
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s3 = getRange(stack);
        String trr = "" + s3 + "";
        String s5 = getOperationSpeedString(stack);

        tooltip.add(TextFormatting.GOLD + "Block Placer Upgrade");

        if(stack.isItemEnchanted() && getRange(stack) > 0)
        {
            tooltip.add(TextFormatting.WHITE + "Placer Range: " + trr);
        }
        else
        {
            tooltip.add(TextFormatting.WHITE + "Placer Range: " + trr);
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
