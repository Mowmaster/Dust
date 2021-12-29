package com.mowmaster.dust.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.mowmaster.dust.References.Constants.MODID;

//https://www.youtube.com/watch?v=uOAeHJtZMZM
public class TooltipUtils
{

    public static void addTooltipMessage(List<Component> componentList, ItemStack stack, TranslatableComponent translatableComponent)
    {
        TranslatableComponent base = translatableComponent;
        componentList.add(base);
    }

    public static void addTooltipMessageWithStyle(List<Component> componentList, String localizationString, ChatFormatting chatFormatting)
    {
        TranslatableComponent base = new TranslatableComponent(localizationString);
        base.withStyle(chatFormatting);
        componentList.add(base);
    }

    public static void addTooltipShiftMessage(List<Component> componentList, ItemStack stack, TranslatableComponent translatableComponent)
    {
        if(!Screen.hasShiftDown())
        {
            TranslatableComponent base = new TranslatableComponent(MODID + ".description_shift");
            base.withStyle(ChatFormatting.WHITE);
            componentList.add(base);
        }
        else {
            TranslatableComponent base = translatableComponent;
            componentList.add(base);
        }
    }

    public static void addTooltipShiftMessageWithStyle(List<Component> componentList, String localizationString, ChatFormatting chatFormatting)
    {
        if(!Screen.hasShiftDown())
        {
            TranslatableComponent base = new TranslatableComponent(MODID + ".description_shift");
            base.withStyle(ChatFormatting.WHITE);
            componentList.add(base);
        }
        else {
            TranslatableComponent base = new TranslatableComponent(localizationString);
            base.withStyle(chatFormatting);
            componentList.add(base);
        }
    }

    public static void addTooltipShiftMessageWithStyle(List<Component> componentList, TranslatableComponent translatableComponent, ChatFormatting chatFormatting)
    {
        if(!Screen.hasShiftDown())
        {
            TranslatableComponent base = new TranslatableComponent(MODID + ".description_shift");
            base.withStyle(ChatFormatting.WHITE);
            componentList.add(base);
        }
        else {
            TranslatableComponent base = translatableComponent;
            base.withStyle(chatFormatting);
            componentList.add(base);
        }
    }

    public static void addTooltipAltMessage(List<Component> componentList, ItemStack stack, TranslatableComponent translatableComponent)
    {
        if(!Screen.hasAltDown())
        {
            TranslatableComponent base = new TranslatableComponent(MODID + ".description_alt");
            base.withStyle(ChatFormatting.WHITE);
            componentList.add(base);
        }
        else {
            TranslatableComponent base = translatableComponent;
            componentList.add(base);
        }
    }

    public static void addTooltipAltMessageWithStyle(List<Component> componentList, String localizationString, ChatFormatting chatFormatting)
    {
        if(!Screen.hasAltDown())
        {
            TranslatableComponent base = new TranslatableComponent(MODID + ".description_alt");
            base.withStyle(ChatFormatting.WHITE);
            componentList.add(base);
        }
        else {
            TranslatableComponent base = new TranslatableComponent(localizationString);
            base.withStyle(chatFormatting);
            componentList.add(base);
        }
    }

    public static void addTooltipAltMessageWithStyle(List<Component> componentList, TranslatableComponent translatableComponent, ChatFormatting chatFormatting)
    {
        if(!Screen.hasAltDown())
        {
            TranslatableComponent base = new TranslatableComponent(MODID + ".description_alt");
            base.withStyle(ChatFormatting.WHITE);
            componentList.add(base);
        }
        else {
            TranslatableComponent base = translatableComponent;
            base.withStyle(chatFormatting);
            componentList.add(base);
        }
    }
}
