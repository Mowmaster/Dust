package com.mowmaster.dust.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Movement for spell effects, in LivingEntity (players' own clients and the server both run this):
//   Slippery   the ground is as slick as ice (the friction of the block underfoot, read in travelInAir)
//   Gliding    can glide like with an elytra, with no elytra (canGlide)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMovementMixin
{
    // @ModifyExpressionValue (MixinExtras): takes the value a call returned and hands back a changed one. Here: the
    // friction of the block underfoot, raised to ice's for a Slippery entity.
    @ModifyExpressionValue(method = "travelInAir",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"))
    private float dust$slipperyFriction(float friction)
    {
        LivingEntity self = (LivingEntity) (Object) this;
        return self.hasEffect(DustEffectsRegistry.SLIPPERY) ? Math.max(friction, 0.98F) : friction;
    }

    // Gliding: in the air and not riding anything, it can glide whatever it's wearing
    @Inject(method = "canGlide()Z", at = @At("HEAD"), cancellable = true)
    private void dust$glideWithoutElytra(CallbackInfoReturnable<Boolean> result)
    {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.hasEffect(DustEffectsRegistry.GLIDING) && !self.onGround() && !self.isPassenger()) result.setReturnValue(true);
    }
}
