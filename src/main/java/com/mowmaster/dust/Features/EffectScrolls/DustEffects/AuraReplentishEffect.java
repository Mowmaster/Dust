package com.mowmaster.dust.Features.EffectScrolls.DustEffects;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustRegistries.DustConsumeEffectsRegistry;
import com.mowmaster.dust.Features.EffectScrolls.Networking.DustAuraPacketHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

public class AuraReplentishEffect implements ConsumeEffect {
    public static final MapCodec<AuraReplentishEffect> CODEC = MapCodec.unit(AuraReplentishEffect::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, AuraReplentishEffect> STREAM_CODEC =
            StreamCodec.unit(new AuraReplentishEffect());

    @Override
    public Type<? extends ConsumeEffect> getType() {
        return DustConsumeEffectsRegistry.AURA_REPLENTISH_CONSUME_EFFECT.get();
    }

    @Override
    public boolean apply(Level level, ItemStack itemStack, LivingEntity livingEntity) {
        if (!level.isClientSide() && livingEntity instanceof ServerPlayer player)
        {
            //Bypasses the canAddAuraCheck to allow over consumption without overflowing the aura value
            DustAuraPacketHelper.addAura(player,8);
        }
        return true;
    }
}
