package com.mowmaster.dust.mixin;

import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.item.component.ResolvableProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// Reaches a mannequin's private setters (Soul's ghosts and bodies are mannequins wearing the player's skin). An invoker
// mixin: like an accessor, but it calls a private method instead of reading a field. Cast any mannequin to this.
@Mixin(Mannequin.class)
public interface MannequinAccessor
{
    @Invoker("setProfile")
    void dust$setProfile(ResolvableProfile profile);

    @Invoker("setHideDescription")
    void dust$setHideDescription(boolean hide);

    @Invoker("setImmovable")
    void dust$setImmovable(boolean immovable);
}
