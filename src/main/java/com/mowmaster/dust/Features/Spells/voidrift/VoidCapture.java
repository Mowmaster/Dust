package com.mowmaster.dust.Features.Spells.voidrift;

import com.mowmaster.dust.DustReferences;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Catches what a creature or player drops when it dies in a void rift: its drops and experience go into VoidStorage
// instead of onto the ground. The rift marks whatever it touches; a mark lasts a moment (MARK_TICKS), so someone who
// steps out of the rift and dies later to something else drops their things as normal.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class VoidCapture
{
    private static final long MARK_TICKS = 20;
    // Who was touched by a rift, and when (game time). Server only: the rift marks from its server tick.
    private static final Map<UUID, Long> MARKED = new HashMap<>();

    private VoidCapture() {}

    public static void mark(LivingEntity living)
    {
        long now = living.level().getGameTime();
        MARKED.put(living.getUUID(), now);
        // Forget old marks now and then, so the map doesn't grow
        if (MARKED.size() > 256) MARKED.values().removeIf(at -> now - at > MARK_TICKS);
    }

    private static boolean marked(LivingEntity living)
    {
        Long at = MARKED.get(living.getUUID());
        return at != null && living.level().getGameTime() - at <= MARK_TICKS;
    }

    // LOWEST: runs after every other drops handler (Butchered's extra drops, other mods' additions), so the void gets those too
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDrops(LivingDropsEvent event)
    {
        LivingEntity living = event.getEntity();
        if (!(living.level() instanceof ServerLevel level) || !marked(living)) return;
        VoidStorage storage = VoidStorage.get(level.getServer());
        for (ItemEntity drop : event.getDrops()) storage.add(drop.getItem());
        event.getDrops().clear();
        storage.addCreature(living.getType());
        MARKED.remove(living.getUUID());
    }

    // Experience is worked out just before the drops event fires (and only for kills a player had a hand in, as in
    // vanilla), so the mark is still there; it's cleared in onDrops
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onExperience(LivingExperienceDropEvent event)
    {
        LivingEntity living = event.getEntity();
        if (!(living.level() instanceof ServerLevel level) || !marked(living)) return;
        VoidStorage.get(level.getServer()).addExperience(event.getDroppedExperience());
        event.setDroppedExperience(0);
    }
}
