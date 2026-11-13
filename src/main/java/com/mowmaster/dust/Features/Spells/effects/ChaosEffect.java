package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.Spells.api.ElementEffect;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

// Chaos
//   Self     Rage: strength, speed and stepping up blocks, with hunger, mining fatigue and nausea
//   Block    shuffles a container's items between its slots
//   Entity   acts as if it was attacked by the caster (no damage): it turns on them; a player's inventory is shuffled
public class ChaosEffect implements ElementEffect
{
    @Override
    public void onSelf(SpellCast cast, LivingEntity target)
    {
        EffectHelper.give(cast, target, DustEffectsRegistry.RAGE);
        EffectHelper.give(cast, target, MobEffects.STRENGTH);
        EffectHelper.give(cast, target, MobEffects.SPEED);
        EffectHelper.give(cast, target, MobEffects.HUNGER);
        EffectHelper.give(cast, target, MobEffects.MINING_FATIGUE);
        EffectHelper.give(cast, target, MobEffects.NAUSEA);
    }

    @Override
    public void onBlock(SpellCast cast, BlockPos pos, Direction face)
    {
        Container container = EffectHelper.container(cast, pos);
        if (container == null) return;
        List<ItemStack> slots = new ArrayList<>(EffectHelper.contents(container));
        // Util.shuffle: shuffles a list with Minecraft's own random source
        Util.shuffle(slots, cast.level().getRandom());
        EffectHelper.refill(container, slots);
    }

    @Override
    public void onEntity(SpellCast cast, Entity target)
    {
        if (target instanceof Player player) {
            // The main inventory and hotbar (not armour or the offhand)
            List<ItemStack> items = new ArrayList<>(player.getInventory().getNonEquipmentItems());
            Util.shuffle(items, cast.level().getRandom());
            for (int slot = 0; slot < items.size(); slot++) player.getInventory().getNonEquipmentItems().set(slot, items.get(slot));
            player.getInventory().setChanged();
            return;
        }
        LivingEntity caster = cast.caster();
        if (caster == null || !(target instanceof Mob mob)) return;
        mob.setLastHurtByMob(caster);
        mob.setTarget(caster);
        if (target instanceof NeutralMob neutral) {
            neutral.setPersistentAngerTarget(EntityReference.of(caster));
            neutral.startPersistentAngerTimer();
        }
    }
}
