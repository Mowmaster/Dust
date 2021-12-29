package com.mowmaster.dust.Items.Readable.RepairScrolls;

import com.google.common.collect.Maps;
import com.mowmaster.dust.Items.Filters.FilterRestricted;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.Util.DustItemHandling;
import com.mowmaster.dust.Util.TooltipUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.IntStream;

import static com.mowmaster.dust.References.Constants.MODID;

public class BaseRepairScroll extends Item {
    public BaseRepairScroll(Properties p_41383_) {
        super(p_41383_);
    }

    public int getColor(ItemStack stack)
    {
        return ColorReference.getColorFromItemStackInt(stack);
    }

    public int getColorRibbon(ItemStack stack)
    {
        return ColorReference.getSecondColorFromItemStackInt(stack);
    }

    public int getType(ItemStack stack)
    {
        if(!getRepairItem(stack).isEmpty())return 1;
        return 0;
    }

    public String getHintTitle(ItemStack stack)
    {
        CompoundTag tag = new CompoundTag();
        if(stack.hasTag()){tag = stack.getTag();}
        if(tag.contains(MODID + "_repairTitle"))return tag.getString(MODID + "_repairTitle");
        return "";
    }

    public String getHintDescription(ItemStack stack)
    {
        CompoundTag tag = new CompoundTag();
        if(stack.hasTag()){tag = stack.getTag();}
        if(tag.contains(MODID + "_repairDescription"))return tag.getString(MODID + "_repairDescription");
        return "";
    }

    //Determines if we need to grab the localization or just use the raw string
    public boolean getHintLocalization(ItemStack stack)
    {
        CompoundTag tag = new CompoundTag();
        if(stack.hasTag()){tag = stack.getTag();}
        if(tag.contains(MODID + "_repairLocalized"))return tag.getBoolean(MODID + "_repairLocalized");
        return false;
    }

    public void setHintTitle(ItemStack stack, String titleString)
    {
        CompoundTag tag = new CompoundTag();
        if(stack.hasTag()){tag = stack.getTag();}
        tag.putString(MODID + "_repairTitle", titleString);
        stack.setTag(tag);
    }

    public void setHintDescription(ItemStack stack, String titleDescription)
    {
        CompoundTag tag = new CompoundTag();
        if(stack.hasTag()){tag = stack.getTag();}
        tag.putString(MODID + "_repairDescription", titleDescription);
        stack.setTag(tag);
    }

    public void setHintLocalization(ItemStack stack, boolean isLocalized)
    {
        CompoundTag tag = new CompoundTag();
        if(stack.hasTag()){tag = stack.getTag();}
        tag.putBoolean(MODID + "_repairLocalized", isLocalized);
        stack.setTag(tag);
    }

    public void setItemFound(ItemStack stack, boolean isLocalized)
    {
        CompoundTag tag = new CompoundTag();
        if(stack.hasTag()){tag = stack.getTag();}
        tag.putBoolean(MODID + "_itemFound", isLocalized);
        stack.setTag(tag);
    }

    public boolean getItemFound(ItemStack stack)
    {
        CompoundTag tag = new CompoundTag();
        if(stack.hasTag()){tag = stack.getTag();}
        if(tag.contains(MODID + "_itemFound"))return tag.getBoolean(MODID + "_itemFound");
        return false;
    }



    public void setMachineBlock(ItemStack scroll, Block machineBlock)
    {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(machineBlock));
        DustItemHandling.writeListStackToNBT(scroll,list,MODID + "_machineBlock");
    }

    public ItemStack getMachineBlock(ItemStack scroll)
    {
        List<ItemStack> list = DustItemHandling.readListStackFromNBT(scroll, MODID + "_machineBlock");
        if(list.size()>0)
        {
            return list.stream().findFirst().get();
        }

        return ItemStack.EMPTY;
    }

    //Set by the BlockEntity Machine
    public void setRepairItem(ItemStack scroll, ItemStack repairItem)
    {
        List<ItemStack> list = new ArrayList<>();
        list.add(repairItem);
        setRepairItem(scroll, list);
    }

    //Set by the BlockEntity Machine
    public void setRepairItem(ItemStack scroll, List<ItemStack> repairItemList)
    {
        if(!scroll.isEnchanted())scroll.enchant(Enchantments.UNBREAKING,-1);
        DustItemHandling.writeItemStackToNBT(scroll,repairItemList);
    }

    //Needed for the Work Order Display
    public ItemStack getRepairItem(ItemStack scroll)
    {
        List<ItemStack> list = getRepairItemList(scroll);
        if(list.size()>0)
        {
            return list.stream().findFirst().get();
        }

        return ItemStack.EMPTY;
    }

    //Needed for the Work Order Display
    public List<ItemStack> getRepairItemList(ItemStack scroll)
    {
        List<ItemStack> list = DustItemHandling.readItemStackFromNBT(scroll);
        if(list.size()>0)
        {
            return list;
        }

        return list;
    }

    public boolean isItemRepairItem(ItemStack scroll, ItemStack incomingItem)
    {
        List<ItemStack> repairList = getRepairItemList(scroll);
        ItemStack itemFound = ItemStack.EMPTY;
        itemFound = IntStream.range(0,repairList.size())
                .mapToObj((repairList)::get)
                .filter(itemStack -> itemStack.getItem().equals(incomingItem.getItem()))
                .findFirst().orElse(ItemStack.EMPTY);

        return !itemFound.isEmpty();
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
        if(p_41406_ instanceof Player)
        {
            Player player = (Player)p_41406_;
            Inventory inv = player.getInventory();
            ItemStack itemFound = ItemStack.EMPTY;
            itemFound = IntStream.range(0,inv.getContainerSize())
                    .mapToObj((inv)::getItem)//Function being applied to each interval
                    .filter(itemStack -> isItemRepairItem(p_41404_,itemStack))
                    .findFirst().orElse(ItemStack.EMPTY);

            if(!itemFound.isEmpty())
            {
                if(p_41404_.isEnchanted())
                {
                    Map<Enchantment, Integer> enchantsNone = Maps.<Enchantment, Integer>newLinkedHashMap();
                    EnchantmentHelper.setEnchantments(enchantsNone,p_41404_);
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        ItemStack repairItem = getRepairItem(p_41421_);

        if(!repairItem.isEmpty())
        {
            boolean localized = getHintLocalization(p_41421_);
            if(localized)
            {
                TooltipUtils.addTooltipMessageWithStyle(p_41423_,new TranslatableComponent(getHintTitle(p_41421_)), ChatFormatting.LIGHT_PURPLE);
            }
            else
            {
                TooltipUtils.addTooltipMessageWithStyle(p_41423_,getHintTitle(p_41421_), ChatFormatting.LIGHT_PURPLE);
            }

            if(localized)
            {
                TooltipUtils.addTooltipMessageWithStyle(p_41423_,new TranslatableComponent(getHintDescription(p_41421_)), ChatFormatting.WHITE);
            }
            else
            {
                TooltipUtils.addTooltipMessageWithStyle(p_41423_,getHintDescription(p_41421_), ChatFormatting.WHITE);
            }

        }

    }
}
