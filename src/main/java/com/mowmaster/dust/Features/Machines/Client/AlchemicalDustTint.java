package com.mowmaster.dust.Features.Machines.Client;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Machines.AlchemicalDust;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

// An item model tint: Alchemical Dust's element colour, for the heap. {"type": "dust:alchemical_dust"} in the item
// model. 0xAARRGGBB; white (-1) without an element.
public record AlchemicalDustTint() implements ItemTintSource
{
    public static final MapCodec<AlchemicalDustTint> MAP_CODEC = MapCodec.unit(new AlchemicalDustTint());

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity)
    {
        DustElement element = AlchemicalDust.of(stack);
        return element == null ? -1 : 0xFF000000 | element.color();
    }

    @Override
    public MapCodec<AlchemicalDustTint> type()
    {
        return MAP_CODEC;
    }
}
