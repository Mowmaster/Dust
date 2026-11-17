package com.mowmaster.dust.Features.Spells.modifiers;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.api.ModifierCategory;
import com.mowmaster.dust.Features.Spells.api.ModifierContext;
import com.mowmaster.dust.Features.Spells.api.ModifierFit;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import com.mowmaster.dust.Features.Spells.api.SpellStats;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;

// Most modifiers: a change to the spell's numbers for each level it has, plus the rules for drawing it. Built in
// SpellRegistries with the with... methods, each returning this so they chain:
//   new LeveledModifier((stats, levels) -> stats.withBonusDamage(stats.bonusDamage() + 0.5F * levels))
//       .categories(ModifierCategory.DAMAGE).fit(ModifierFit.INSTANT)
// Its id is set by SpellRegistries (named(id)), so rules like "only one per branch" can count it.
//   change          what its levels do to the stats (BiFunction: a function of two arguments)
//   costPerLevel    the whole dust cost's change per level, in percent (-1: 1% cheaper a level)
//   everyLevels     from its unlock level, one more of it allowed in the tree every this many levels (0: no limit)
//   oncePerBranch   at most one in any line
//   shapes          only for these shapes (empty: any)
//   requires        any other precondition
public class LeveledModifier implements SpellModifier
{
    private final BiFunction<SpellStats, Integer, SpellStats> change;
    private Set<ModifierCategory> categories = Set.of();
    private ModifierTier tier = ModifierTier.NORMAL;
    private ModifierFit fit = ModifierFit.ANY;
    private int weight = 10;
    private int unlockLevel;
    private int minPerNode = 1;
    private int maxPerNode = 1;
    private int uses;
    private int costPerLevel;
    private int extraCost = 1;
    private int everyLevels;
    private boolean oncePerBranch;
    private boolean categoryOnly;
    private boolean oncePerSpell;
    private boolean singleLevel;
    private Set<Identifier> shapes = Set.of();
    private List<Identifier> addsToPool = List.of();
    private Predicate<ModifierContext> requires = context -> true;
    private Predicate<ModifierContext> inPlay = context -> true;
    private java.util.function.@org.jspecify.annotations.Nullable Consumer<SpellCast> onCast;
    private Identifier id = Identifier.withDefaultNamespace("unnamed");

    public LeveledModifier(BiFunction<SpellStats, Integer, SpellStats> change)
    {
        this.change = change;
    }

    // A modifier whose effect is read elsewhere (by its id) rather than through the stats
    public static LeveledModifier marker()
    {
        return new LeveledModifier((stats, levels) -> stats);
    }

    // ---- building ----

    // Set as it's registered (SpellRegistries.modifier)
    public LeveledModifier named(Identifier id) { this.id = id; return this; }
    public LeveledModifier categories(ModifierCategory... categories) { this.categories = Set.of(categories); return this; }
    public LeveledModifier tier(ModifierTier tier) { this.tier = tier; return this; }
    public LeveledModifier fit(ModifierFit fit) { this.fit = fit; return this; }
    public LeveledModifier weight(int weight) { this.weight = weight; return this; }
    public LeveledModifier unlock(int level) { this.unlockLevel = level; return this; }
    public LeveledModifier perNode(int min, int max) { this.minPerNode = min; this.maxPerNode = max; return this; }
    public LeveledModifier uses(int uses) { this.uses = uses; return this; }
    public LeveledModifier costPerLevel(int percent) { this.costPerLevel = percent; return this; }
    public LeveledModifier extraCost(int dust) { this.extraCost = dust; return this; }
    public LeveledModifier everyLevels(int levels) { this.everyLevels = levels; return this; }
    public LeveledModifier oncePerBranch() { this.oncePerBranch = true; return this; }
    // Only a category pylon can take it (SpellModifier.categoryPylonsOnly)
    public LeveledModifier categoryOnly() { this.categoryOnly = true; return this; }
    // Only one per spell, ever (SpellModifier.oncePerSpell): also a one-use knockout in the pool
    public LeveledModifier onlyOne() { this.oncePerSpell = true; this.uses = 1; return this; }
    // Never more than one level: its pylon gets no upgrade nodes
    public LeveledModifier oneLevel() { this.singleLevel = true; return this; }
    public LeveledModifier shapes(Identifier... shapes) { this.shapes = Set.of(shapes); return this; }
    public LeveledModifier addsToPool(Identifier... modifiers) { this.addsToPool = List.of(modifiers); return this; }
    public LeveledModifier requires(Predicate<ModifierContext> requires) { this.requires = requires; return this; }
    // Only in the pool (and drawable) while something in the spell uses it (SpellModifier.inPlay)
    public LeveledModifier onlyWhen(Predicate<ModifierContext> inPlay) { this.inPlay = inPlay; return this; }
    // Something it does as the spell is cast (before the shape delivers it), e.g. Overshield's Resistance
    public LeveledModifier onCast(java.util.function.Consumer<SpellCast> onCast) { this.onCast = onCast; return this; }

    // ---- for the book (Research/Tabs/SpellCardResearchTab) ----
    public int everyLevelsCount() { return everyLevels; }
    public Set<Identifier> allowedShapes() { return shapes; }
    public boolean branchOnly() { return oncePerBranch; }
    public int costPercentPerLevel() { return costPerLevel; }

    // ---- SpellModifier ----

    @Override public Set<ModifierCategory> categories() { return categories; }
    @Override public ModifierTier tier() { return tier; }
    @Override public ModifierFit fit() { return fit; }
    @Override public int weight() { return weight; }
    @Override public int unlockLevel() { return unlockLevel; }
    @Override public int minLevelsPerNode() { return minPerNode; }
    @Override public int maxLevelsPerNode() { return maxPerNode; }
    @Override public int uses() { return uses; }
    @Override public boolean categoryPylonsOnly() { return categoryOnly; }
    @Override public boolean oncePerSpell() { return oncePerSpell; }
    @Override public boolean singleLevel() { return singleLevel; }
    @Override public List<Identifier> addsToPool() { return addsToPool; }
    @Override public boolean inPlay(ModifierContext context) { return inPlay.test(context); }

    @Override
    public boolean canRoll(ModifierContext context)
    {
        if ((!context.reached(unlockLevel) && !context.unlockedEarly(id)) || !fit.suits(context.instant(), context.dot())) return false;
        if (!shapes.isEmpty() && !shapes.contains(context.shape())) return false;
        if (oncePerBranch && context.has(id)) return false;
        if (everyLevels > 0) {
            int from = context.unlockedEarly(id) ? 0 : unlockLevel;
            long allowed = 1 + Math.max(0, context.level() + context.unlockReduction() - from) / everyLevels;
            if (context.countInTree(id) >= allowed) return false;
        }
        return inPlay.test(context) && requires.test(context);
    }

    @Override
    public Map<DustElement, Integer> cost(DustElement spellElement)
    {
        return extraCost == 0 ? Map.of() : Map.of(spellElement, extraCost);
    }

    @Override
    public int costPercent(int levels)
    {
        return costPerLevel * levels;
    }

    @Override
    public SpellStats modifyStats(SpellStats stats, int levels)
    {
        return change.apply(stats, levels);
    }

    @Override
    public boolean beforeDelivery(SpellCast cast)
    {
        if (onCast != null) onCast.accept(cast);
        return true;
    }
}
