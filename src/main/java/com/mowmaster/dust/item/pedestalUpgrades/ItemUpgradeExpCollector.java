package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
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

public class ItemUpgradeExpCollector extends ItemUpgradeBaseExp
{
    public int rangeWidth = 0;
    public int suckiRate = 7;

    public ItemUpgradeExpCollector(Properties builder) {super(builder.group(dust.itemGroup));}

    public int getRangeWidth(ItemStack stack)
    {
        int rW = getRangeModifier(stack);
        rangeWidth = ((rW)+1);
        return  rangeWidth;
    }

    public int getSuckiRate(ItemStack stack)
    {
        /*switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                suckiRate = 7;//1
                break;
            case 1:
                suckiRate=16;//2
                break;
            case 2:
                suckiRate = 27;//3
                break;
            case 3:
                suckiRate = 40;//4
                break;
            case 4:
                suckiRate = 55;//5
                break;
            case 5:
                suckiRate=160;//10
                break;
            default: suckiRate=7;
        }*/

        return  suckiRate;
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);
        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world, coinInPedestal, pedestalPos);
                upgradeActionSendExp(world, coinInPedestal,pedestalPos);
            }
        }
    }

    public void upgradeAction(World world, ItemStack coinInPedestal, BlockPos posOfPedestal)
    {
        setMaxXP(coinInPedestal,getExpCountByLevel(30) );
        int width = getRangeWidth(coinInPedestal);
        int height = (2*width)+1;
        BlockPos negBlockPos = getNegRangePosEntity(world,posOfPedestal,width,height);
        BlockPos posBlockPos = getPosRangePosEntity(world,posOfPedestal,width,height);

        AxisAlignedBB getBox = new AxisAlignedBB(negBlockPos,posBlockPos);

        List<ExperienceOrbEntity> xpList = world.getEntitiesWithinAABB(ExperienceOrbEntity.class,getBox);
        for(ExperienceOrbEntity getXPFromList : xpList)
        {
            world.playSound((PlayerEntity) null, posOfPedestal.getX(), posOfPedestal.getY(), posOfPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.15F, 1.0F);
            TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
            if(pedestalInv instanceof TilePedestal) {
                int currentlyStoredExp = getXPStored(coinInPedestal);
                if(currentlyStoredExp < readMaxXpFromNBT(coinInPedestal))
                {
                    int value = getXPFromList.getXpValue();
                    getXPFromList.remove();
                    setXPStored(coinInPedestal, currentlyStoredExp + value);
                }
            }
            break;
        }
    }

    @Override
    public void actionOnColideWithBlock(World world, TilePedestal tilePedestal, BlockPos posPedestal, BlockState state, Entity entityIn)
    {
        if(entityIn instanceof ExperienceOrbEntity)
        {
            ItemStack coin = tilePedestal.getCoinOnPedestal();
            ExperienceOrbEntity getXPFromList = ((ExperienceOrbEntity)entityIn);
            world.playSound((PlayerEntity) null, posPedestal.getX(), posPedestal.getY(), posPedestal.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.15F, 1.0F);
            int currentlyStoredExp = getXPStored(coin);
            if(currentlyStoredExp < readMaxXpFromNBT(coin))
            {
                int value = getXPFromList.getXpValue();
                getXPFromList.remove();
                setXPStored(coin, currentlyStoredExp + value);
            }
        }

        if(entityIn instanceof PlayerEntity)
        {
            PlayerEntity getPlayer = ((PlayerEntity)entityIn);
            ItemStack coin = tilePedestal.getCoinOnPedestal();
            if(!getPlayer.isCrouching())
            {
                int currentlyStoredExp = getXPStored(coin);
                if(currentlyStoredExp < readMaxXpFromNBT(coin))
                {
                    int transferRate = getSuckiRate(coin);
                    int value = removeXp(getPlayer, transferRate);
                    if(value > 0)
                    {
                        world.playSound((PlayerEntity)null, posPedestal.getX(), posPedestal.getY(), posPedestal.getZ(), SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.BLOCKS, 0.15F, 1.0F);
                        setXPStored(coin, currentlyStoredExp + value);
                    }
                }
            }
        }
    }

    /*@Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        String s5 = getOperationSpeedString(stack);

        String sr = "";

        *//*switch (getSuckiRate(stack))
        {
            case 7:
                sr = "1 Level";
                break;
            case 16:
                sr="2 Levels";
                break;
            case 27:
                sr = "3 Levels";
                break;
            case 40:
                sr = "4 Levels";
                break;
            case 55:
                sr = "5 Levels";
                break;
            case 160:
                sr="10 Levels";
                break;
            default: sr="1 Level";
        }*//*

        *//*if(stack.hasTagCompound()) {
            if (getSuckiRate(stack) > 0) {
                tooltip.add("???: " + sr);
            } else {
                tooltip.add("???: 1 Level");
            }
        }*//*

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
        int s3 = getRangeWidth(stack);
        String trr = "" + (s3+s3+1) + "";
        String tr = getExpTransferRateString(stack);
        String xp = ""+ getExpLevelFromCount(getXPStored(stack)) +"";

        tooltip.add(new TranslationTextComponent(TextFormatting.GOLD + "Exp Magnet Upgrade"));
        tooltip.add(new TranslationTextComponent(TextFormatting.GREEN + "Exp Levels Stored: "+xp));
        tooltip.add(new TranslationTextComponent(TextFormatting.AQUA + "Exp Buffer Capacity: 30 Levels"));

        if(stack.isEnchanted() && s3 > 0)
        {
            tooltip.add(new TranslationTextComponent(TextFormatting.WHITE + "Effected Area: " + trr+"x"+trr+"x"+trr));
        }
        else
        {
            tooltip.add(new TranslationTextComponent(TextFormatting.WHITE + "Effected Area: " + trr+"x"+trr+"x"+trr));
        }

        if(getExpTransferRate(stack)>0)
        {
            tooltip.add(new TranslationTextComponent(TextFormatting.GRAY + "Exp Transfer Ammount: " + tr));
        }
        else
        {
            tooltip.add(new TranslationTextComponent(TextFormatting.GRAY + "Exp Transfer Ammount: 5 Levels"));
        }
    }

    public static final Item XPMAGNET = new ItemUpgradeExpCollector(new Properties().maxStackSize(64).group(dust.itemGroup)).setRegistryName(new ResourceLocation(MODID, "coin/xpmagnet"));

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(XPMAGNET);
    }


}
