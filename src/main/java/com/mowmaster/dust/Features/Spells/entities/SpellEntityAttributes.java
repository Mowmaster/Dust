package com.mowmaster.dust.Features.Spells.entities;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

// Every living entity type needs its attributes (health, speed...) registered; this is when (a mod bus event: the
// subscriber picks the right bus by the event's type)
@EventBusSubscriber(modid = DustReferences.MODID)
public final class SpellEntityAttributes
{
    private SpellEntityAttributes() {}

    @SubscribeEvent
    public static void onAttributes(EntityAttributeCreationEvent event)
    {
        event.put(DustEntityRegistry.ANIMATED_BLOCK.get(), AnimatedBlockEntity.createAttributes().build());
    }
}
