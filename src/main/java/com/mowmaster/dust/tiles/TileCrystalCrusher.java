package com.mowmaster.dust.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;


public class TileCrystalCrusher extends TileEntityLockable implements ISidedInventory
{
    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return null;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
         return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {

    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return null;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public String getGuiID() {
        return null;
    }
}
