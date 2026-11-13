package com.mowmaster.dust.Features.Spells.modifiers;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.ModifierCategory;
import com.mowmaster.dust.Features.Spells.api.ModifierContext;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

// Multishot (projectile spells only): fires a spread of bolts instead of one, two more for each Multishot in the spell,
// 10 degrees apart. Costs 2 dust.
public class MultishotModifier implements SpellModifier
{
    private static final float SPREAD_DEGREES = 10;

    @Override
    public ModifierCategory category() { return ModifierCategory.CHANGE; }

    @Override
    public int weight() { return 6; }

    @Override
    public boolean canRoll(ModifierContext context)
    {
        return context.shape().equals(SpellRegistries.PROJECTILE.getId());
    }

    @Override
    public Map<DustElement, Integer> cost(DustElement spellElement)
    {
        return Map.of(spellElement, 2);
    }

    @Override
    public boolean beforeDelivery(SpellCast cast)
    {
        Identifier id = SpellRegistries.MODIFIER_REGISTRY.getKey(this);
        if (!cast.spell().shape().equals(SpellRegistries.PROJECTILE.getId()) || id == null) return true;
        // Each Multishot in the spell runs this: the first fires the whole spread, the rest find it done
        if (cast.delivered()) return false;
        long extra = cast.spell().modifiers().stream().filter(id::equals).count();
        for (long i = -extra; i <= extra; i++) {
            // yRot: turns the direction about the vertical axis (radians)
            Vec3 direction = cast.direction().yRot((float) Math.toRadians(i * SPREAD_DEGREES));
            SpellRegistries.PROJECTILE.get().cast(new SpellCast(cast.level(), cast.caster(), cast.spell(), cast.stats(), cast.origin(), direction));
        }
        cast.markDelivered();
        return false;
    }
}
