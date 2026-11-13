package com.mowmaster.dust.mixin;

import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Spirit Form: a player passes through blocks like a spectator. Player.tick sets noPhysics from isSpectator() every
// tick; right after it does, this turns it on for a player in Spirit Form (noPhysics: no collisions, no suffocating,
// and the server doesn't call it "moving wrongly").
@Mixin(Player.class)
public abstract class PlayerSpiritMixin
{
    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Player;noPhysics:Z", opcode = org.objectweb.asm.Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void dust$spiritPassesThrough(CallbackInfo info)
    {
        Player self = (Player) (Object) this;
        if (self.hasEffect(DustEffectsRegistry.SPIRIT_FORM)) self.noPhysics = true;
    }
}
