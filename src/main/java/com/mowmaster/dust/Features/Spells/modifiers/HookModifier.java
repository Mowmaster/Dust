package com.mowmaster.dust.Features.Spells.modifiers;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import com.mowmaster.dust.Features.Spells.api.SpellStats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

// A modifier that does its work through hooks rather than levels: the blessed (zodiac) and corrupted (sin) ones.
// Each is a knockout (one use: gone from the pool once drawn), with no upgrade nodes. Built with the with... methods:
//   stats      a change to the numbers, knowing the spell (BiFunction: spell and stats in, stats out)
//   against    the numbers for one creature (Pride)
//   onCast     something done as the spell is cast (a buff for the caster)
//   onImpact   something done where it landed (pulling items in, pets attacking...)
//   costChange / extraCost   what it does to the dust (and so mana) cost, in percent / extra dust
public class HookModifier implements SpellModifier
{
    // (cast, target, stats) -> stats, for one creature
    @FunctionalInterface
    public interface Against { SpellStats apply(SpellCast cast, Entity target, SpellStats stats); }

    // (cast, where it landed, the creature hit or null)
    @FunctionalInterface
    public interface Impact { void apply(SpellCast cast, Vec3 at, @Nullable Entity target); }

    private final ModifierTier tier;
    private BiFunction<Spell, SpellStats, SpellStats> stats = (spell, s) -> s;
    private @Nullable Against against;
    private @Nullable Consumer<SpellCast> onCast;
    private @Nullable Impact onImpact;
    private int costPercent;
    private int extraCost = 1;

    public HookModifier(ModifierTier tier)
    {
        this.tier = tier;
    }

    public HookModifier stats(BiFunction<Spell, SpellStats, SpellStats> stats) { this.stats = stats; return this; }
    public HookModifier against(Against against) { this.against = against; return this; }
    public HookModifier onCast(Consumer<SpellCast> onCast) { this.onCast = onCast; return this; }
    public HookModifier onImpact(Impact onImpact) { this.onImpact = onImpact; return this; }
    public HookModifier costChange(int percent) { this.costPercent = percent; return this; }
    public HookModifier extraCost(int dust) { this.extraCost = dust; return this; }

    @Override public ModifierTier tier() { return tier; }
    @Override public int uses() { return 1; }

    @Override
    public Map<DustElement, Integer> cost(DustElement spellElement)
    {
        return extraCost == 0 ? Map.of() : Map.of(spellElement, extraCost);
    }

    @Override
    public int costPercent(int levels)
    {
        return costPercent;
    }

    @Override
    public SpellStats modifyStats(Spell spell, SpellStats stats, int levels)
    {
        return this.stats.apply(spell, stats);
    }

    @Override
    public SpellStats statsAgainst(SpellCast cast, Entity target, SpellStats stats)
    {
        return against == null ? stats : against.apply(cast, target, stats);
    }

    @Override
    public boolean beforeDelivery(SpellCast cast)
    {
        if (onCast != null) onCast.accept(cast);
        return true;
    }

    @Override
    public void onImpact(SpellCast cast, Vec3 at, @Nullable Entity target)
    {
        if (onImpact != null) onImpact.apply(cast, at, target);
    }
}
