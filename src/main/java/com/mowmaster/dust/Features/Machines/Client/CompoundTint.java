package com.mowmaster.dust.Features.Machines.Client;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.Machines.Compounds;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

// An item model tint: an alchemical compound's colour (its elements mixed: DustComposition#color), for the dust pile on
// the paper. {"type": "dust:compound"} in the item model. 0xAARRGGBB; white (-1) when it holds nothing.
public record CompoundTint() implements ItemTintSource
{
    public static final MapCodec<CompoundTint> MAP_CODEC = MapCodec.unit(new CompoundTint());

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity)
    {
        DustComposition compound = Compounds.of(stack);
        return compound == null || compound.isEmpty() ? -1 : 0xFF000000 | compound.color();
    }

    @Override
    public MapCodec<CompoundTint> type()
    {
        return MAP_CODEC;
    }
}
