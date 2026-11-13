package com.mowmaster.dust.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mowmaster.dust.Features.Spells.Client.GhostRendering;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// How see-through a translucent body is drawn. Vanilla hard-codes 654311423 (0x26FFFFFF: white at about 15% opacity,
// the colour a spectator's invisible mobs are tinted with) in LivingEntityRenderer.submit. For pets' ghosts it's
// swapped for GhostRendering.GHOST_COLOR; everything else keeps vanilla's.
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererGhostMixin
{
    // At CONSTANT: the place the number 654311423 is loaded. @Local(argsOnly = true) hands over the method's own
    // argument of that type: the render state being drawn.
    @ModifyExpressionValue(method = "submit", at = @At(value = "CONSTANT", args = "intValue=654311423"))
    private int dust$ghostAlpha(int original, @Local(argsOnly = true) LivingEntityRenderState state)
    {
        return Boolean.TRUE.equals(state.getRenderData(GhostRendering.GHOST)) ? GhostRendering.GHOST_COLOR : original;
    }
}
