package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import net.minecraft.client.util.ITooltipFlag;
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

public class ItemUpgradeExpRelay extends ItemUpgradeBaseExp
{

    public ItemUpgradeExpRelay(Properties builder) {super(builder.group(dust.ITEM_GROUP));}

    @Override
    public Boolean canAcceptCapacity() {
        return super.canAcceptCapacity();
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal, BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(coinInPedestal);
                upgradeActionSendExp( world, coinInPedestal, pedestalPos);
            }
        }
    }

    public void upgradeAction(ItemStack coinInPedestal)
    {
        if(!hasMaxXpSet(coinInPedestal)) {setMaxXP(coinInPedestal,getExpCountByLevel(30));}
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        String tr = getExpTransferRateString(stack);
        String xp = ""+ getExpLevelFromCount(getXPStored(stack)) +"";
        String s5 = getOperationSpeedString(stack);

        tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "Exp Relay Upgrade"));
        tooltip.add(new TranslationTextComponent(TextFormatting.GREEN + "Exp Levels Stored: "+xp));
        tooltip.add(new TranslationTextComponent(TextFormatting.AQUA + "Exp Buffer Capacity: 30 Levels"));
        tooltip.add(new TranslationTextComponent(TextFormatting.GRAY + "Exp Transfer Ammount: " + tr));
        tooltip.add(new TranslationTextComponent(TextFormatting.RED + "Operational Speed: " + s5));
    }

    public static final Item XPRELAY = new ItemUpgradeExpRelay(new Properties().maxStackSize(64).group(dust.ITEM_GROUP)).setRegistryName(new ResourceLocation(MODID, "coin/xprelay"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(XPRELAY);
    }


}