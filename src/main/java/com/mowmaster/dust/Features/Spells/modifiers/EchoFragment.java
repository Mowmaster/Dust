package com.mowmaster.dust.Features.Spells.modifiers;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.SpellScheduler;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellFragment;
import com.mowmaster.dust.Features.Spells.api.SpellShape;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

import java.util.Map;

// Echo (a fragment any element can get): the spell casts again by itself a second after, from wherever the caster is
// then, the way they're facing. Its aspects: Resonance (another echo for each) and Quickening (echoes come sooner).
// An echo is the bare spell: its shape again, without the cast's modifiers delivering (no extra Multishot spread).
public class EchoFragment implements SpellFragment
{
    private static final int DELAY = 20;
    private static final int MIN_DELAY = 5;

    @Override
    public @Nullable DustElement element() { return null; }

    @Override
    public Map<DustElement, Integer> cost(DustElement spellElement)
    {
        return Map.of(spellElement, 3);
    }

    @Override
    public boolean beforeDelivery(SpellCast cast)
    {
        long echoes = 1 + cast.spell().modifiers().stream().filter(SpellRegistries.ECHO_RESONANCE.getId()::equals).count();
        long quickening = cast.spell().modifiers().stream().filter(SpellRegistries.ECHO_QUICKENING.getId()::equals).count();
        int delay = (int) Math.max(MIN_DELAY, DELAY / (1 + quickening));
        SpellShape shape = cast.spell().shapeType();
        if (shape == null) return true;
        for (int i = 1; i <= echoes; i++) {
            SpellScheduler.later(cast.level().getServer(), delay * i, () -> {
                LivingEntity caster = cast.caster();
                if (caster != null && !caster.isAlive()) return;
                SpellCast echo = caster != null
                        ? new SpellCast(cast.level(), caster, cast.spell(), cast.stats(), caster.getEyePosition(), caster.getLookAngle())
                        : new SpellCast(cast.level(), null, cast.spell(), cast.stats(), cast.origin(), cast.direction());
                shape.cast(echo);
            });
        }
        // The cast itself goes ahead as normal
        return true;
    }
}
