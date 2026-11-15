package com.mowmaster.dust.Features.DustStorage.Client;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.DustStorage.DustJarContents;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

// An item model tint: a Dust Jar item's lid in the colour of the element it carries (white with none).
// {"type": "dust:dust_jar"} in the item model.
public record DustJarItemTint() implements ItemTintSource
{
    public static final MapCodec<DustJarItemTint> MAP_CODEC = MapCodec.unit(new DustJarItemTint());

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity)
    {
        DustJarContents contents = stack.getOrDefault(DustComponentDataRegistry.DUST_JAR.get(), DustJarContents.EMPTY);
        return contents.element().map(element -> 0xFF000000 | element.color()).orElse(-1);
    }

    @Override
    public MapCodec<DustJarItemTint> type()
    {
        return MAP_CODEC;
    }
}
