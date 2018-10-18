package com.mowmaster.dust.items;

import com.mowmaster.dust.misc.AchievementHandler;
import com.mowmaster.dust.references.Reference;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//import static com.mowmaster.dust.misc.AchievementHandler.achievementScrollA;
import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class ItemScroll extends Item
{
    public ItemScroll(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add("[WIP] Will randomly give you a letter of the Ancient Alphabet.");
        tooltip.add("Not needed till next beta release(will also be reworked)");
    }

    //Use NBT to define which one and set item resources based on that, maybe also apply to coins...


}


