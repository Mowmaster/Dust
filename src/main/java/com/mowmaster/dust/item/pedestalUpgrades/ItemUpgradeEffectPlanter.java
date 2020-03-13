package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemUpgradeEffectPlanter extends ItemUpgradeBase
{
    public ItemUpgradeEffectPlanter(Properties builder) {super(builder.group(dust.itemGroup));}

    @Override
    public Boolean canAcceptRange() {
        return true;
    }

    public int getRangeWidth(ItemStack stack)
    {
        int rangeWidth = 0;
        int rW = getRangeModifier(stack);
        rangeWidth = ((rW)+1);
        return  rangeWidth;
    }


    public int ticked = 0;

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);

        int width = getRangeWidth(coinInPedestal);
        int height = (2*width)+1;

        BlockPos negBlockPos = getNegRangePos(world,pedestalPos,width,height);
        BlockPos posBlockPos = getPosRangePos(world,pedestalPos,width,height);

        if(!world.isBlockPowered(pedestalPos)) {
            for (int x = negBlockPos.getX(); x <= posBlockPos.getX(); x++) {
                for (int z = negBlockPos.getZ(); z <= posBlockPos.getZ(); z++) {
                    for (int y = negBlockPos.getY(); y <= posBlockPos.getY(); y++) {
                        BlockPos posTargetBlock = new BlockPos(x, y, z);
                        BlockState targetBlock = world.getBlockState(posTargetBlock);
                        if (tick%speed == 0) {
                            ticked++;
                        }

                        if(ticked > 84)
                        {
                            upgradeAction(world, itemInPedestal, pedestalPos, posTargetBlock, targetBlock);
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

    public void upgradeAction(World world, ItemStack itemInPedestal, BlockPos posOfPedestal, BlockPos posTarget, BlockState target)
    {
        Random rand = new Random();
        Item singleItemInPedestal = itemInPedestal.getItem();

        if(world.getBlockState(posTarget).getBlock().equals(Blocks.AIR) && !singleItemInPedestal.equals(Items.AIR))
        {
            if(((BlockItem) singleItemInPedestal).getBlock() instanceof IPlantable)
            {
                if (!itemInPedestal.isEmpty() && itemInPedestal.getItem() instanceof BlockItem && ((BlockItem) itemInPedestal.getItem()).getBlock() instanceof IPlantable) {
                    Block block = ((BlockItem) itemInPedestal.getItem()).getBlock();


                    if (world.getBlockState(posTarget.down()).canSustainPlant(world,posTarget.down(),Direction.UP,(IPlantable) block)) {
                        if (world.setBlockState(posTarget, ((IPlantable) block).getPlant(world, posTarget))) {
                            this.removeFromPedestal(world,posOfPedestal,1);
                            world.setBlockState(posTarget,((IPlantable) block).getPlant(world, posTarget));
                            world.playSound((PlayerEntity) null, posTarget.getX(), posTarget.getY(), posTarget.getZ(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        int s3 = getRangeWidth(stack);
        String tr = "" + (s3+s3+1) + "";
        String s5 = getOperationSpeedString(stack);
        tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "Planter Effect Upgrade"));
        tooltip.add(new TranslationTextComponent(TextFormatting.WHITE + "Effected Area: " + tr+"x"+tr+"x"+tr));
        tooltip.add(new TranslationTextComponent(TextFormatting.RED + "Operational Speed: " + s5));
    }

    public static final Item PLANTER = new ItemUpgradeEffectPlanter(new Properties().maxStackSize(64).group(dust.itemGroup)).setRegistryName(new ResourceLocation(MODID, "coin/planter"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(PLANTER);
    }


}
