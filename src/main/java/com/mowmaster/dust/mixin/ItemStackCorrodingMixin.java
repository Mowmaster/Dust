package com.mowmaster.dust.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mowmaster.dust.Features.Spells.effects.RustParts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// Corroding (Rust on a mob): the things it wears and holds wear faster. Every way an item loses durability (armour hit,
// a tool used, a weapon swung) goes through this one hurtAndBreak; the others all call it. The amount is multiplied
// before Unbreaking and the item's own handling get their say.
@Mixin(ItemStack.class)
public abstract class ItemStackCorrodingMixin
{
    // @ModifyVariable at HEAD with argsOnly: changes one of the method's arguments as it starts. ordinal = 0: the first
    // int argument (the amount). @Local(argsOnly) hands over the owner argument (who's holding or wearing it; may be null).
    @ModifyVariable(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int dust$corrode(int amount, @Local(argsOnly = true) @Nullable LivingEntity owner)
    {
        return owner == null || amount <= 0 ? amount : amount * RustParts.wearMultiplier(owner);
    }
}
