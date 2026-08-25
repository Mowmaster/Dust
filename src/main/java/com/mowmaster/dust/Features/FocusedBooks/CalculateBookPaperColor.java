package com.mowmaster.dust.Features.FocusedBooks;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/*public class CalculateBookPaperColor implements ItemTintSource {
    public static final MapCodec<CalculateBookPaperColor> MAP_CODEC = MapCodec.unit(new CalculateBookPaperColor());

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        int color = stack.get(DustComponentDataRegistry.FOCUSEDBOOK_BOOKPAGE_COLOR.get());
        return color != 0 ? color : 0xFFFFFF; // Default white
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return null;
    }
}*/

