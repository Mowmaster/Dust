package com.mowmaster.dust.Items.Readable.RepairScrolls;

import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.Util.DustItemHandling;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
        DustItemHandling.writeItemStackToNBT(scroll,list);
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

    //Set by the BlockEntity Machine
    public void setRepairItem(ItemStack scroll, List<ItemStack> repairItemList)
    {
        DustItemHandling.writeItemStackToNBT(scroll,repairItemList);
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

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);

    }
}
