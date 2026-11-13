package com.mowmaster.dust.Features.Spells.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

// What an element does: one effect for the caster, one for a block, one for another entity. Registered in
// SpellRegistries.ELEMENT_EFFECTS under the element's own id (dust:fire's effect is dust:fire). Each method is called
// once per thing affected: a spell with size calls onBlock for every block in reach, and onEntity for every entity.
// Other mods register effects for their own elements the same way (a DeferredRegister on the registry key).
// The methods run on the server only.
public interface ElementEffect
{
    // The caster (or whoever the dust burned next to)
    default void onSelf(SpellCast cast, LivingEntity target) {}

    // A block, hit on face (the side it was hit from: where something placed "on" it goes)
    default void onBlock(SpellCast cast, BlockPos pos, Direction face) {}

    // Another entity (never the caster)
    default void onEntity(SpellCast cast, Entity target) {}

    // An element with nothing registered yet
    ElementEffect NONE = new ElementEffect() {};
}
