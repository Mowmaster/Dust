package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustReferences;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// The mod's own sound events. Each one's sounds (and their volume) are listed in assets/dust/sounds.json under its
// name. createVariableRangeEvent: heard further away the louder it's played, like most sounds.
public final class DustSoundRegistry
{
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, DustReferences.MODID);

    // A spell's explosion: vanilla's explosion sounds, played quieter. Explosions are played at volume 4.0 (which sets
    // how far they're heard); the sound's volume in sounds.json multiplies that, and the result is capped at 1.0 (full
    // loudness), so it has to be under 0.25 to be quieter at all: 0.1 makes 40%.
    public static final DeferredHolder<SoundEvent, SoundEvent> SPELL_EXPLOSION = SOUND_EVENTS.register("spell_explosion",
            id -> SoundEvent.createVariableRangeEvent(id));

    private DustSoundRegistry() {}

    public static void register(IEventBus eventBus)
    {
        SOUND_EVENTS.register(eventBus);
    }
}
