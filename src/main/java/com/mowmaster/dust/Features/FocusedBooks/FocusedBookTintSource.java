package com.mowmaster.dust.Features.FocusedBooks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.MapColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record FocusedBookTintSource() implements ItemTintSource {
    public static final MapCodec<FocusedBookTintSource> MAP_CODEC = MapCodec.unit(FocusedBookTintSource::new);

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        return FocusedBookItem.getBookColor(itemStack);
    }

    @Override
    public MapCodec<FocusedBookTintSource> type() {
        return MAP_CODEC;
    }

    public int defaultColor() {
        return 0;
    }
}
