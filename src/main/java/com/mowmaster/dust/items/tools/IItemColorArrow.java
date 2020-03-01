package com.mowmaster.dust.items.tools;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IItemColorArrow implements IItemColor {
    public static final IItemColor INSTANCE = new IItemColorArrow();

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        return getColorFromNBT(stack);
    }

    public static int getColorFromNBT(ItemStack stack)
    {
        if(!stack.hasTagCompound())
        {
            return 0;
        }
        if(!stack.getTagCompound().hasKey("CustomPotionColor"))
        {
            return 0;
        }
        return stack.getTagCompound().getInteger("CustomPotionColor");
    }

    /*public static void registerArrowColors()
    {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(INSTANCE, ItemRegistry.dustTippedArrow);
    }*/
}
