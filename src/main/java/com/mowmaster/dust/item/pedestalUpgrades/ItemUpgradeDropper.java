package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemUpgradeDropper extends ItemUpgradeBase
{
    public ItemUpgradeDropper(Properties builder) {super(builder.group(dust.itemGroup));}

    @Override
    public Boolean canAcceptRange() {
        return true;
    }

    @Override
    public Boolean canAcceptCapacity() {
        return true;
    }

    public int getTransferRate(ItemStack stack)
    {
        int summonRate = 1;
        switch (getCapacityModifier(stack))
        {
            case 0:
                summonRate = 1;
                break;
            case 1:
                summonRate=4;
                break;
            case 2:
                summonRate = 8;
                break;
            case 3:
                summonRate = 16;
                break;
            case 4:
                summonRate = 32;
                break;
            case 5:
                summonRate=64;
                break;
            default: summonRate=1;
        }
        return  summonRate;
    }

    public int getRange(ItemStack stack)
    {
        int range = 1;
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


    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal, BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world,pedestalPos,coinInPedestal);
            }
        }
    }

    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack coinOnPedestal)
    {
        int rate = getTransferRate(coinOnPedestal);
        int range = getRange(coinOnPedestal);
        if(!getStackInPedestal(world,posOfPedestal).isEmpty())//hasItem
        {
            ItemStack itemToSummon = getStackInPedestal(world,posOfPedestal).copy();
            itemToSummon.setCount(rate);
            ItemEntity itemEntity = new ItemEntity(world,getPosOfBlockBelow(world,posOfPedestal,-range).getX() + 0.5,getPosOfBlockBelow(world,posOfPedestal,-range).getY(),getPosOfBlockBelow(world,posOfPedestal,-range).getZ() + 0.5,itemToSummon);
            itemEntity.setMotion(0,0,0);
            world.addEntity(itemEntity);
            this.removeFromPedestal(world,posOfPedestal,itemToSummon.getCount());
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        int range = getRange(stack);
        String tr = "" + getTransferRate(stack) + "";
        String trr = "" + range + "";
        String s5 = getOperationSpeedString(stack);

        tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "Item Dropper Upgrade"));
        tooltip.add(new TranslationTextComponent(TextFormatting.GRAY + "Dropped Stack Size: " + tr));
        tooltip.add(new TranslationTextComponent(TextFormatting.WHITE + "Dropper Range: " + trr));
        tooltip.add(new TranslationTextComponent(TextFormatting.RED + "Operational Speed: " + s5));
    }

    public static final Item DROPPER = new ItemUpgradeDropper(new Properties().maxStackSize(64).group(dust.itemGroup)).setRegistryName(new ResourceLocation(MODID, "coin/dropper"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(DROPPER);
    }


}
