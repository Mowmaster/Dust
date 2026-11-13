package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// Small things the element effects share
final class EffectHelper
{
    private EffectHelper() {}

    // A mob effect for the spell's duration, at the spell's potency (living targets only)
    static void give(SpellCast cast, Entity target, Holder<MobEffect> effect)
    {
        if (target instanceof LivingEntity living) living.addEffect(new MobEffectInstance(effect, cast.stats().duration(), cast.stats().amplifier()), cast.caster());
    }

    // The container at pos (chests, barrels, hoppers...), or null
    static @Nullable Container container(SpellCast cast, BlockPos pos)
    {
        return cast.level().getBlockEntity(pos) instanceof Container container ? container : null;
    }

    // Every slot's stack, in slot order (empty slots included)
    static List<ItemStack> contents(Container container)
    {
        List<ItemStack> stacks = new ArrayList<>();
        for (int slot = 0; slot < container.getContainerSize(); slot++) stacks.add(container.getItem(slot));
        return stacks;
    }

    // Puts stacks back into the container's slots, in this order (setChanged: tells the block entity to save)
    static void refill(Container container, List<ItemStack> stacks)
    {
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            container.setItem(slot, slot < stacks.size() ? stacks.get(slot) : ItemStack.EMPTY);
        }
        container.setChanged();
    }
}
