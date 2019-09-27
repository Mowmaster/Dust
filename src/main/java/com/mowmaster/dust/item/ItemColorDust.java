package com.mowmaster.dust.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.RandomObjectDescriptor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemColorDust extends Item {
    public static int color = 0;
    public ItemColorDust(Properties builder) {
        super(builder);
    }

    public static void handleItemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return getColorFromNBT(itemstack);} else {return -1;}},DUST);

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

            if(playerIn.isSneaking())
            {
                if(playerIn.getHeldItemMainhand().hasTag())
                {
                    if(playerIn.getHeldItemMainhand().getTag().contains(MODID+"color"))
                    {
                        System.out.println(getColorFromNBT(playerIn.getHeldItemMainhand()));
                    }
                }
                else setColorToNBT(playerIn.getHeldItemMainhand(),16777215 );

            }
            else
            {
                setColorToNBT(playerIn.getHeldItemMainhand(),65280 );
            }



        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public void setColorToNBT(ItemStack stack, int color)
    {
        CompoundNBT nbt;
        if (stack.hasTag())
        {
            nbt = stack.getTag();
        }
        else
        {
            nbt = new CompoundNBT();
        }

        if(nbt.contains(MODID+"color"))
        {
            nbt.putInt(MODID+"color",16777215 );
        }
        else
        {
            nbt.putInt(MODID+"color",255 );
        }

        /*//ItemStack stack = new ItemStack(this);
        nbt = stack.getOrCreateTag();
        nbt.putInt(MODID+"color", color);*/

    }

    public static int getColorFromNBT(ItemStack stack)
    {

        if(stack.hasTag())
        {
            if(stack.getTag().contains(MODID))
            {
                color = stack.getTag().getInt(MODID +"color");
            }
        }

        return color;
    }

    private static final ResourceLocation RESLOC_DUST = new ResourceLocation(MODID, "itemdust");


    public static final Item DUST = new ItemColorDust(new Properties().group(ItemGroup.MATERIALS)).setRegistryName(RESLOC_DUST);




    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(DUST);

    }




}
