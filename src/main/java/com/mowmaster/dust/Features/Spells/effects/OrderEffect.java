package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.Spells.api.ElementEffect;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Order
//   Self     dispels every harmful effect, and lifts the curses off the items in hand
//   Block    sorts a container's items by name (empty slots at the end)
//   Entity   calms it: it forgets its target and anger, and won't pick a new target while Calmed lasts
public class OrderEffect implements ElementEffect
{
    @Override
    public void onSelf(SpellCast cast, LivingEntity target)
    {
        // A copy of the list, so removing while going through it is safe
        for (MobEffectInstance active : new ArrayList<>(target.getActiveEffects())) {
            if (active.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) target.removeEffect(active.getEffect());
        }
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack held = target.getItemInHand(hand);
            if (!held.isEmpty()) EnchantmentHelper.updateEnchantments(held, enchantments -> enchantments.removeIf(enchantment -> enchantment.is(EnchantmentTags.CURSE)));
        }
    }

    @Override
    public void onBlock(SpellCast cast, BlockPos pos, Direction face)
    {
        Container container = EffectHelper.container(cast, pos);
        if (container == null) return;
        List<ItemStack> items = new ArrayList<>(EffectHelper.contents(container).stream().filter(stack -> !stack.isEmpty()).toList());
        // By name, then the bigger stack first. Comparator.comparing builds a comparator from a key; thenComparing
        // breaks ties with another.
        items.sort(Comparator.comparing((ItemStack stack) -> stack.getHoverName().getString())
                .thenComparing(ItemStack::getCount, Comparator.reverseOrder()));
        EffectHelper.refill(container, items);
    }

    @Override
    public void onEntity(SpellCast cast, Entity target)
    {
        if (target instanceof NeutralMob neutral) neutral.stopBeingAngry();
        if (target instanceof Mob mob) {
            mob.setTarget(null);
            mob.setLastHurtByMob(null);
        }
        EffectHelper.give(cast, target, DustEffectsRegistry.CALMED);
    }
}
