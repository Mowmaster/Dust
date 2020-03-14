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

public class ItemUpgradeExpTank extends ItemUpgradeBaseExp
{

    public ItemUpgradeExpTank(Properties builder) {super(builder.group(dust.ITEM_GROUP));}

    public int getExpBuffer(ItemStack stack)
    {
        int value = 100;
        /*switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                value = 100;//
                break;
            case 1:
                value=250;//
                break;
            case 2:
                value = 500;//
                break;
            case 3:
                value = 1000;//
                break;
            case 4:
                value = 10000;//
                break;
            case 5:
                value=100000;//
                break;
            default: value=100;
        }*/

        return  value;
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
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
        int maxXPLevel = getExpBuffer(coinInPedestal);
        setMaxXP(coinInPedestal,getExpCountByLevel(maxXPLevel));
    }


    /*@Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        String s5 = getOperationSpeedString(stack);

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
        String tr = getExpTransferRateString(stack);
        String xp = ""+ getExpLevelFromCount(getXPStored(stack)) +"";
        int buffer = getExpBuffer(stack);



        tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "Exp Tank Upgrade"));
        tooltip.add(new TranslationTextComponent(TextFormatting.GREEN + "Exp Levels Stored: "+xp));
        tooltip.add(new TranslationTextComponent(TextFormatting.AQUA + "Exp Storage Capacity: "+buffer+" Levels"));

        if(getExpTransferRate(stack)>0)
        {
            tooltip.add(new TranslationTextComponent(TextFormatting.GRAY + "Exp Transfer Ammount: " + tr));
        }
        else
        {
            tooltip.add(new TranslationTextComponent(TextFormatting.GRAY + "Exp Transfer Ammount: 5 Levels"));
        }
    }

    public static final Item XPTANK = new ItemUpgradeExpTank(new Properties().maxStackSize(64).group(dust.ITEM_GROUP)).setRegistryName(new ResourceLocation(MODID, "coin/xptank"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(XPTANK);
    }


}
