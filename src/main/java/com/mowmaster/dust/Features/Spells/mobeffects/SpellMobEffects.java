package com.mowmaster.dust.Features.Spells.mobeffects;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

// What the spell effects that aren't just attribute changes do:
//   Calmed   (Order on a mob) it won't pick a target while it lasts
@EventBusSubscriber(modid = DustReferences.MODID)
public final class SpellMobEffects
{
    private SpellMobEffects() {}

    // LivingChangeTargetEvent: fired when a mob picks a new target; cancelling it keeps the old (none, once calmed)
    @SubscribeEvent
    public static void onChangeTarget(LivingChangeTargetEvent event)
    {
        LivingEntity mob = event.getEntity();
        if (event.getNewAboutToBeSetTarget() != null && mob.hasEffect(DustEffectsRegistry.CALMED)) event.setCanceled(true);
    }
}
