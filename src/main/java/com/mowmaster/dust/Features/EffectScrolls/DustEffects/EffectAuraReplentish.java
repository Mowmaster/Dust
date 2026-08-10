package com.mowmaster.dust.Features.EffectScrolls.DustEffects;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustRegistries.DustConsumeEffectsRegistry;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

public class EffectAuraReplentish implements ConsumeEffect {
    public static final MapCodec<EffectAuraReplentish> CODEC = MapCodec.unit(EffectAuraReplentish::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, EffectAuraReplentish> STREAM_CODEC =
            StreamCodec.unit(new EffectAuraReplentish());

    @Override
    public Type<? extends ConsumeEffect> getType() {
        return DustConsumeEffectsRegistry.AURA_REPLENTISH_CONSUME_EFFECT.get();
    }

    @Override
    public boolean apply(Level level, ItemStack itemStack, LivingEntity livingEntity) {
        if (!level.isClientSide() && livingEntity instanceof ServerPlayer player)
        {
            //Bypasses the canAddAuraCheck to allow over consumption without overflowing the aura value
            DustMagicAttachmentHelper.addMana(player,5);
        }
        return true;
    }
}
