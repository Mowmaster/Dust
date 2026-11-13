package com.mowmaster.dust.Features.Spells.Client;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.Spells.api.Spell;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

// An item model tint: a Spell Scroll's seal in its spell's element colour ({"type": "dust:spell"}); grey when blank
public record SpellTint() implements ItemTintSource
{
    public static final MapCodec<SpellTint> MAP_CODEC = MapCodec.unit(new SpellTint());

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity)
    {
        Spell spell = stack.get(DustComponentDataRegistry.SPELL.get());
        return spell == null ? 0xFF808080 : 0xFF000000 | spell.element().color();
    }

    @Override
    public MapCodec<SpellTint> type()
    {
        return MAP_CODEC;
    }
}
