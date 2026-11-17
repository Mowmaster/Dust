package com.mowmaster.dust.Features.Spells.Client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.Features.Spells.FusedCrystalItem;
import com.mowmaster.dust.Features.Spells.modifiers.FusionModifier;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

// A Fused Crystal's colours: {"type": "dust:fused_crystal", "second": false} is its fusion's first element's colour,
// "second": true the second's (the item model's two layers). White without a fusion.
public record FusedCrystalTint(boolean second) implements ItemTintSource
{
    public static final MapCodec<FusedCrystalTint> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("second", false).forGetter(FusedCrystalTint::second)
    ).apply(instance, FusedCrystalTint::new));

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity)
    {
        FusionModifier fusion = FusedCrystalItem.modifierOf(stack);
        if (fusion == null) return -1;
        return 0xFF000000 | (second ? fusion.second() : fusion.first()).color();
    }

    @Override
    public MapCodec<FusedCrystalTint> type()
    {
        return MAP_CODEC;
    }
}
