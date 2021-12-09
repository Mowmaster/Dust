package com.mowmaster.dust.Block.BlockEntities.CustomDustBlock;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CustomPowderedBlockItemColor implements ItemColor {

    @Override
    public int getColor(ItemStack stack, int tintindex) {
        if (stack.getItem() instanceof CustomPowderedBlockItem) {
            if (!stack.hasTag() || !stack.getTag().contains("intColor")) {
                return 0;
            }
            CompoundTag compound = stack.getTag();
            if (compound.contains("intColor")) {
                return compound.getInt("intColor");
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }
}
