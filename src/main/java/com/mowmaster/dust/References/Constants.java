package com.mowmaster.dust.References;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Constants {
    public static final String MODID = "dust";
    public static final String MODNAME = "Dust";


    public static Container blankContainer = new Container() {
        ItemStack stack = ItemStack.EMPTY;

        @Override
        public int getContainerSize() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return stack.isEmpty();
        }

        @Override
        public ItemStack getItem(int p_18941_) {
            return stack;
        }

        @Override
        public ItemStack removeItem(int p_18942_, int p_18943_) {
            return stack = ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItemNoUpdate(int p_18951_) {
            return stack = ItemStack.EMPTY;
        }

        @Override
        public void setItem(int p_18944_, ItemStack p_18945_) {
            stack = p_18945_;
        }

        @Override
        public void setChanged() {

        }

        @Override
        public boolean stillValid(Player p_18946_) {
            return stack.isEmpty();
        }

        @Override
        public void clearContent() {
            stack = ItemStack.EMPTY;
        }
    };
}
