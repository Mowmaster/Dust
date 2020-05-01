package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemUpgradePlacer extends ItemUpgradeBase
{
    public ItemUpgradePlacer(Properties builder) {super(builder.group(dust.ITEM_GROUP));}

    @Override
    public Boolean canAcceptRange() {
        return true;
    }

    public BlockState getState(Block getBlock, ItemStack itemForBlock)
    {
        BlockState stated = Blocks.AIR.getDefaultState();

        //Redstone
        if(itemForBlock.getItem() == Items.REDSTONE)
        {
            stated = Blocks.REDSTONE_WIRE.getDefaultState();
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

            Item singleItemInPedestal = itemInPedestal.getItem();

            if(blockBelow.equals(Blocks.AIR) && !singleItemInPedestal.equals(Items.AIR)) {
                if(singleItemInPedestal instanceof BlockItem)
                {
                    if (((BlockItem) singleItemInPedestal).getBlock() instanceof Block)
                    {
                        if (!itemInPedestal.isEmpty() && itemInPedestal.getItem() instanceof BlockItem && ((BlockItem) itemInPedestal.getItem()).getBlock() instanceof Block) {
                            Block block = ((BlockItem) itemInPedestal.getItem()).getBlock();

                            world.setBlockState(blockPosBelow,block.getDefaultState());
                            this.removeFromPedestal(world,posOfPedestal,1);
                            world.playSound((PlayerEntity) null, blockPosBelow.getX(), blockPosBelow.getY(), blockPosBelow.getZ(), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                        }
                    }
                }
            }

            /*if(blockBelow.isAir(blockBelow.getDefaultState(),world,blockPosBelow))
            {
                FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(world.getServer().getWorld(world.getDimension().getType()));
                fakePlayer.setPosition(posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ());
                fakePlayer.setPosition(posOfPedestal.getX(),posOfPedestal.getY(),posOfPedestal.getZ());
                fakePlayer.setHeldItem(Hand.MAIN_HAND,itemInPedestal.copy());
                //, blockPosBelow, getPedestalFacing(world,posOfPedestal).getOpposite(), 0.5F, 0.5F, 0.5F
                if(fakePlayer.interactionManager.processRightClick(fakePlayer, world, fakePlayer.getHeldItemMainhand(), Hand.MAIN_HAND).equals(ActionResultType.SUCCESS))
                {
                    this.removeFromPedestal(world,posOfPedestal,1);
                    world.playSound((PlayerEntity) null, blockPosBelow.getX(), blockPosBelow.getY(), blockPosBelow.getZ(), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                }
            }*/
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        TranslationTextComponent range = new TranslationTextComponent(getTranslationKey() + ".tooltip_range");
        range.appendText("" + getRange(stack) + "");
        TranslationTextComponent speed = new TranslationTextComponent(getTranslationKey() + ".tooltip_speed");
        speed.appendText(getOperationSpeedString(stack));

        range.applyTextStyle(TextFormatting.WHITE);
        speed.applyTextStyle(TextFormatting.RED);

        tooltip.add(range);
        tooltip.add(speed);
    }

    public static final Item PLACER = new ItemUpgradePlacer(new Properties().maxStackSize(64).group(dust.ITEM_GROUP)).setRegistryName(new ResourceLocation(MODID, "coin/placer"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(PLACER);
    }


}