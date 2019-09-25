package com.mowmaster.dust.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.mowmaster.dust.references.Reference.MODID;

public class ItemColorDust extends Item {
    public ItemColorDust(Properties builder) {
        super(builder);
    }

    public static void handleItemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().register((itemstack, tintIndex) -> {if (tintIndex == 1){return getColorFromNBT(itemstack);} else {return -1;}},DUST);

    }

    /*public static int getColor(ItemStack stack, int tintIndex) {
        if (stack == null || stack.isEmpty()) {
            return -1;
        }
        CompoundNBT tag = stack.getTag();
        return tag != null && tag.contains("color", Constants.NBT.TAG_INT) ? tag.getInt("color") : 8606770;
    }*/

    public void setColorToNBT(int color)
    {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putInt(MODID+"color", color);
    }

    public static int getColorFromNBT(ItemStack stack)
    {
        int color = 0;
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
