package com.mowmaster.dust.Util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

import static com.mowmaster.dust.References.Constants.MODID;

public class DustItemHandling
{
    public static void writeItemStackToNBT(ItemStack stackSaveToThis, List<ItemStack> listToStore)
    {
        CompoundTag compound = new CompoundTag();
        CompoundTag compoundStorage = new CompoundTag();
        if(stackSaveToThis.hasTag()){compound = stackSaveToThis.getTag();}

        ItemStackHandler handler = new ItemStackHandler();
        handler.setSize(listToStore.size());

        for(int i=0;i<handler.getSlots();i++) {handler.setStackInSlot(i,listToStore.get(i));}

        compoundStorage = handler.serializeNBT();
        compound.put(MODID + "itemStorage",compoundStorage);
        stackSaveToThis.setTag(compound);
    }

    public static List<ItemStack> readItemStackFromNBT(ItemStack storedOnThisStack)
    {
        List<ItemStack> stackList = new ArrayList<>();
        if(storedOnThisStack.hasTag())
        {
            CompoundTag getCompound = storedOnThisStack.getTag();
            if(getCompound.contains(MODID + "itemStorage"))
            {
                CompoundTag invTag = getCompound.getCompound(MODID + "itemStorage");
                ItemStackHandler handler = new ItemStackHandler();
                ((INBTSerializable<CompoundTag>) handler).deserializeNBT(invTag);

                for(int i=0;i<handler.getSlots();i++) {stackList.add(handler.getStackInSlot(i));}
            }
        }

        return stackList;
    }

    public static void writeListStackToNBT(ItemStack stackSaveToThis, List<ItemStack> listToStore, String path)
    {
        CompoundTag compound = new CompoundTag();
        CompoundTag compoundStorage = new CompoundTag();
        if(stackSaveToThis.hasTag()){compound = stackSaveToThis.getTag();}

        ItemStackHandler handler = new ItemStackHandler();
        handler.setSize(listToStore.size());

        for(int i=0;i<handler.getSlots();i++) {handler.setStackInSlot(i,listToStore.get(i));}

        compoundStorage = handler.serializeNBT();
        compound.put(path,compoundStorage);
        stackSaveToThis.setTag(compound);
    }

    public static List<ItemStack> readListStackFromNBT(ItemStack storedOnThisStack, String path)
    {
        List<ItemStack> stackList = new ArrayList<>();
        if(storedOnThisStack.hasTag())
        {
            CompoundTag getCompound = storedOnThisStack.getTag();
            if(getCompound.contains(path))
            {
                CompoundTag invTag = getCompound.getCompound(path);
                ItemStackHandler handler = new ItemStackHandler();
                ((INBTSerializable<CompoundTag>) handler).deserializeNBT(invTag);

                for(int i=0;i<handler.getSlots();i++) {stackList.add(handler.getStackInSlot(i));}
            }
        }

        return stackList;
    }
}
