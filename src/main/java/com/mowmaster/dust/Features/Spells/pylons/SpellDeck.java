package com.mowmaster.dust.Features.Spells.pylons;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.ModifierCategory;
import com.mowmaster.dust.Features.Spells.api.ModifierContext;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.dust.Features.Spells.api.SpellFragment;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

// A spell tree's pool and deck (held by its SpellPylonBlockEntity):
//   pool     every modifier the spell could ever get, with how many more times each can come out (-1: no limit; a
//            "knockout" modifier has a limited number). Filled when the shape is fixed (the first modifier pylon); a
//            shape modifier on a pylon adds its own modifiers (SpellModifier.addsToPool)
//   deck     the cards pylons draw from: START_CARDS drawn from the pool to begin with, topped back up to MIN_CARDS
//            whenever it runs low. Besides modifiers, three special cards:
//              SPELL_BREAK      a spell break, for a Neutral pylon (one at level 30, another every 20 levels)
//              BLESSING         a blessing with no blessed modifier left: a Neutral pylon taking it may turn Legendary
//              NULL_CORRUPTION  corruption with no corrupted modifier left: the pylon taking it ends its branch
//   blessed, corruption   the counters: a pylon locking adds to blessed (after paying corruption back to 0); at
//            BLESS_AT a blessed card goes in the deck. A pylon broken adds to corruption; at CORRUPT_AT a corrupted card.
//   rerolls  how many times a pylon has been broken and replaced at each spot (fewer upgrade nodes each time)
// The drawing itself (which card a pylon takes) is SpellPylonBlockEntity's, which knows the pylon and its line.
public class SpellDeck
{
    public static final int START_CARDS = 10;
    public static final int MIN_CARDS = 5;
    public static final int BLESS_AT = 10;
    public static final int CORRUPT_AT = 5;

    public static final Identifier SPELL_BREAK = Identifier.fromNamespaceAndPath(DustReferences.MODID, "spell_break");
    public static final Identifier BLESSING = Identifier.fromNamespaceAndPath(DustReferences.MODID, "blessing");
    public static final Identifier NULL_CORRUPTION = Identifier.fromNamespaceAndPath(DustReferences.MODID, "null_corruption");

    private boolean started;
    private final Map<Identifier, Integer> pool = new LinkedHashMap<>();
    private final List<Identifier> deck = new ArrayList<>();
    private int blessed;
    private int corruption;
    private int breakCards;
    private final Map<BlockPos, Integer> rerolls = new HashMap<>();
    // The Prevent cards (SpellRegistries.PREVENT_BLOCK / PREVENT_ATTACK): gone for good (a pylon holding it was broken),
    // and the one the next Neutral pylon must take (it came back when the other was broken)
    private final Set<Identifier> gone = new HashSet<>();
    private @Nullable Identifier forced;

    public boolean started() { return started; }
    public List<Identifier> cards() { return deck; }
    // Every modifier in the pool and how many more times it can come out (-1: no limit), read-only
    public Map<Identifier, Integer> pool() { return java.util.Collections.unmodifiableMap(pool); }
    public int blessed() { return blessed; }
    public int corruption() { return corruption; }

    public static boolean isSpecial(Identifier card)
    {
        return card.equals(SPELL_BREAK) || card.equals(BLESSING) || card.equals(NULL_CORRUPTION);
    }

    // ---- starting, and the pool ----

    // The shape is fixed: every modifier goes in the pool (but those another modifier brings in, and fragments, which
    // only come through the Fragment slot), and START_CARDS are drawn into the deck
    public void start(ModifierContext tree, RandomSource random, Map<ModifierCategory, Integer> categoryBonus, float doubleDraw)
    {
        reset();
        started = true;
        Set<Identifier> broughtIn = new HashSet<>();
        for (SpellModifier modifier : SpellRegistries.MODIFIER_REGISTRY) broughtIn.addAll(modifier.addsToPool());
        for (var entry : SpellRegistries.MODIFIER_REGISTRY.entrySet()) {
            Identifier id = entry.getKey().identifier();
            SpellModifier modifier = entry.getValue();
            if (broughtIn.contains(id) || modifier instanceof SpellFragment) continue;
            pool.put(id, modifier.uses() > 0 ? modifier.uses() : -1);
        }
        for (int i = 0; i < START_CARDS; i++) drawNormal(tree, random, categoryBonus, doubleDraw, modifier -> true);
    }

    // Back to nothing (the Shape Pylon was broken: the next shape fills it again)
    public void reset()
    {
        started = false;
        pool.clear();
        deck.clear();
        blessed = 0;
        corruption = 0;
        breakCards = 0;
        rerolls.clear();
        gone.clear();
        forced = null;
    }

    // A shape modifier went on a pylon (or its card was laid on the Spell Pylon): its own modifiers join the pool, and
    // with withCard, one of them the deck too
    public void bringIn(SpellModifier modifier, RandomSource random, boolean withCard)
    {
        List<Identifier> children = new ArrayList<>();
        for (Identifier id : modifier.addsToPool()) {
            SpellModifier brought = SpellRegistries.MODIFIER_REGISTRY.getValue(id);
            if (brought == null) continue;
            pool.putIfAbsent(id, brought.uses() > 0 ? brought.uses() : -1);
            if (pool.get(id) != 0) children.add(id);
        }
        if (withCard && !children.isEmpty()) take(children.get(random.nextInt(children.size())));
    }

    // ---- the deck ----

    // Draws a normal modifier from the pool into the deck, weighted (its weight, more for a category with Category
    // Weight), among those that suit the tree now and pass filter. doubleDraw: the chance it comes twice. False if
    // there was nothing to draw.
    public boolean drawNormal(ModifierContext tree, RandomSource random, Map<ModifierCategory, Integer> categoryBonus, float doubleDraw,
                              Predicate<SpellModifier> filter)
    {
        Identifier chosen = pick(random, id -> {
            SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(id);
            return modifier != null && modifier.tier() == ModifierTier.NORMAL && filter.test(modifier) && modifier.canRoll(tree);
        }, id -> {
            SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(id);
            int bonus = 0;
            for (ModifierCategory category : modifier.categories()) bonus = Math.max(bonus, categoryBonus.getOrDefault(category, 0));
            return modifier.weight() * (1F + bonus);
        });
        if (chosen == null) return false;
        take(chosen);
        // Double Draw: the same again (a knockout modifier only if it has a use left)
        if (doubleDraw > 0 && random.nextFloat() < doubleDraw && pool.getOrDefault(chosen, 0) != 0) take(chosen);
        return true;
    }

    // Tops the deck back up to MIN_CARDS (as far as the pool allows)
    public void topUp(ModifierContext tree, RandomSource random, Map<ModifierCategory, Integer> categoryBonus, float doubleDraw)
    {
        if (!started) return;
        // Copies of a once-per-spell modifier the spell already has can never be drawn: out they go
        deck.removeIf(card -> {
            SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(card);
            return modifier != null && modifier.oncePerSpell() && tree.countInTree(card) > 0;
        });
        while (deck.size() < MIN_CARDS && drawNormal(tree, random, categoryBonus, doubleDraw, modifier -> true)) {}
    }

    // A modifier straight into the deck, from the pool (one use of a knockout modifier)
    private void take(Identifier id)
    {
        int left = pool.getOrDefault(id, -1);
        if (left == 0) return;
        if (left > 0) pool.put(id, left - 1);
        deck.add(id);
    }

    // A modifier into the deck, using one of its uses in the pool if it has a limited number (a modifier card's)
    public void addFromPool(Identifier id)
    {
        int left = pool.getOrDefault(id, -1);
        if (left > 0) pool.put(id, left - 1);
        deck.add(id);
    }

    // A card put (back) in the deck as it is: a broken pylon's, or a maxed-out pylon's copy
    public void addCard(Identifier card)
    {
        deck.add(card);
    }

    public void removeCard(Identifier card)
    {
        deck.remove(card);
    }

    // ---- the Prevent cards ----

    // Out of the deck and pool for this spell. True if any copy was in the deck.
    public boolean knockOut(Identifier card)
    {
        pool.put(card, 0);
        return deck.removeIf(card::equals);
    }

    // A pylon holding this Prevent card was broken: it's gone for good, and the other one (unless it's gone too) comes
    // back into the deck, for the next Neutral pylon to take
    public void breakPrevent(Identifier card)
    {
        knockOut(card);
        gone.add(card);
        if (forced != null && forced.equals(card)) forced = null;
        Identifier other = SpellRegistries.otherPrevent(card);
        if (gone.contains(other)) return;
        knockOut(other);
        deck.add(other);
        forced = other;
    }

    public @Nullable Identifier forced() { return forced; }

    public void clearForced()
    {
        forced = null;
    }

    // A weighted random pick from the pool among the ids passing which (null if none do)
    private @Nullable Identifier pick(RandomSource random, Predicate<Identifier> which, java.util.function.ToDoubleFunction<Identifier> weight)
    {
        Map<Identifier, Double> weights = new LinkedHashMap<>();
        for (Map.Entry<Identifier, Integer> entry : pool.entrySet()) {
            if (entry.getValue() == 0 || !which.test(entry.getKey())) continue;
            double w = weight.applyAsDouble(entry.getKey());
            if (w > 0) weights.put(entry.getKey(), w);
        }
        if (weights.isEmpty()) return null;
        double roll = random.nextDouble() * weights.values().stream().mapToDouble(Double::doubleValue).sum();
        Identifier chosen = null;
        for (Map.Entry<Identifier, Double> entry : weights.entrySet()) {
            chosen = entry.getKey();
            roll -= entry.getValue();
            if (roll <= 0) break;
        }
        return chosen;
    }

    // A random normal modifier from the pool that fits this pylon (a Legendary pylon's), without taking it out of
    // the pool (null if none fits)
    public @Nullable Identifier randomFor(ModifierContext line, RandomSource random)
    {
        return pick(random, id -> {
            SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(id);
            return modifier != null && modifier.tier() == ModifierTier.NORMAL && modifier.canRoll(line);
        }, id -> SpellRegistries.MODIFIER_REGISTRY.getValue(id).weight());
    }

    // ---- spell breaks ----

    // A spell break card at PylonConfig.BREAK_MIN_LEVEL, and another every BREAK_EVERY levels after
    public void addDueBreaks(int level)
    {
        if (!started) return;
        int due = level < PylonConfig.BREAK_MIN_LEVEL ? 0 : 1 + (level - PylonConfig.BREAK_MIN_LEVEL) / PylonConfig.BREAK_EVERY;
        while (breakCards < due) {
            deck.add(SPELL_BREAK);
            breakCards++;
        }
    }

    // ---- the counters ----

    // A pylon locked: blessing (step: 2 with Double Blessed Progress), after corruption is paid back to 0. At BLESS_AT, a
    // blessed modifier from the pool goes in the deck (one use), or a BLESSING card if none is left.
    public void bless(int step, RandomSource random)
    {
        if (corruption > 0) {
            corruption = Math.max(0, corruption - step);
            return;
        }
        blessed += step;
        if (blessed < BLESS_AT) return;
        blessed = 0;
        Identifier card = pick(random, id -> tierOf(id) == ModifierTier.BLESSED, id -> 1);
        if (card != null) take(card);
        else deck.add(BLESSING);
    }

    // A pylon broken: corruption. At CORRUPT_AT, a corrupted modifier from the pool goes in the deck (one use), or a
    // NULL_CORRUPTION card if none is left.
    public void corrupt(RandomSource random)
    {
        corruption++;
        if (corruption < CORRUPT_AT) return;
        corruption = 0;
        Identifier card = pick(random, id -> tierOf(id) == ModifierTier.CORRUPTED, id -> 1);
        if (card != null) take(card);
        else deck.add(NULL_CORRUPTION);
    }

    // Whether any blessed modifier is left in the pool (none: a Legendary is twice as likely)
    public boolean blessingsLeft()
    {
        return pool.entrySet().stream().anyMatch(e -> e.getValue() != 0 && tierOf(e.getKey()) == ModifierTier.BLESSED);
    }

    private static @Nullable ModifierTier tierOf(Identifier id)
    {
        SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(id);
        return modifier == null ? null : modifier.tier();
    }

    // ---- rerolls ----

    public int rerolls(BlockPos pos)
    {
        return rerolls.getOrDefault(pos, 0);
    }

    public void rerolled(BlockPos pos)
    {
        rerolls.merge(pos.immutable(), 1, Integer::sum);
    }

    // ---- saving ----

    private record PoolEntry(Identifier id, int uses)
    {
        static final Codec<PoolEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
                Identifier.CODEC.fieldOf("id").forGetter(PoolEntry::id),
                Codec.INT.fieldOf("uses").forGetter(PoolEntry::uses)
        ).apply(i, PoolEntry::new));
    }

    private record Reroll(BlockPos pos, int count)
    {
        static final Codec<Reroll> CODEC = RecordCodecBuilder.create(i -> i.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(Reroll::pos),
                Codec.INT.fieldOf("count").forGetter(Reroll::count)
        ).apply(i, Reroll::new));
    }

    // Everything as one record, for the block entity's save (and the client's copy, though it doesn't show it yet)
    public record Saved(boolean started, List<PoolEntry> pool, List<Identifier> deck, int blessed, int corruption, int breakCards, List<Reroll> rerolls,
                        List<Identifier> gone, java.util.Optional<Identifier> forced)
    {
        public static final Codec<Saved> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.BOOL.optionalFieldOf("started", false).forGetter(Saved::started),
                PoolEntry.CODEC.listOf().optionalFieldOf("pool", List.of()).forGetter(Saved::pool),
                Identifier.CODEC.listOf().optionalFieldOf("deck", List.of()).forGetter(Saved::deck),
                Codec.INT.optionalFieldOf("blessed", 0).forGetter(Saved::blessed),
                Codec.INT.optionalFieldOf("corruption", 0).forGetter(Saved::corruption),
                Codec.INT.optionalFieldOf("break_cards", 0).forGetter(Saved::breakCards),
                Reroll.CODEC.listOf().optionalFieldOf("rerolls", List.of()).forGetter(Saved::rerolls),
                Identifier.CODEC.listOf().optionalFieldOf("gone", List.of()).forGetter(Saved::gone),
                Identifier.CODEC.optionalFieldOf("forced").forGetter(Saved::forced)
        ).apply(i, Saved::new));
    }

    public Saved save()
    {
        return new Saved(started, pool.entrySet().stream().map(e -> new PoolEntry(e.getKey(), e.getValue())).toList(), List.copyOf(deck),
                blessed, corruption, breakCards, rerolls.entrySet().stream().map(e -> new Reroll(e.getKey(), e.getValue())).toList(),
                List.copyOf(gone), java.util.Optional.ofNullable(forced));
    }

    public void load(Saved saved)
    {
        reset();
        started = saved.started();
        saved.pool().forEach(e -> pool.put(e.id(), e.uses()));
        deck.addAll(saved.deck());
        blessed = saved.blessed();
        corruption = saved.corruption();
        breakCards = saved.breakCards();
        saved.rerolls().forEach(r -> rerolls.put(r.pos(), r.count()));
        gone.addAll(saved.gone());
        forced = saved.forced().orElse(null);
    }
}
