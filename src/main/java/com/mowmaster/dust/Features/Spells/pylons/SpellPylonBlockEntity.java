package com.mowmaster.dust.Features.Spells.pylons;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Spells.SpellKnowledge;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.ModifierCategory;
import com.mowmaster.dust.Features.Spells.api.ModifierContext;
import com.mowmaster.dust.Features.Spells.SpellMastery;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import com.mowmaster.mowlib.blockentity.CBSyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// The Spell Pylon: the heart of a spell tree. It holds:
//   element      set by right-clicking it with an element crystal
//   tree         the tree's id: casts of its spells give it experience (SpellTreeData); its level is the spell's level
//   chain        its pylons in the order placed, each with its parent (parents: the index of the pylon it was placed
//                against, -1 for the Spell Pylon itself). First the Shape Pylon, on the Spell Pylon's connection side
//                (SpellPylonBlock.FACING; no level needed); it leads on to one modifier pylon, straight on or to
//                either side. After that one modifier pylon per level, on any of a modifier pylon's three free sides
//                (straight on, left, right: its back is its parent). Placing against a pylon that already has one
//                after it starts a branch. The pylons' models show the links (PylonLinks).
//   deck         the tree's pool and deck of modifiers, its blessing and corruption (SpellDeck): every modifier pylon
//                draws its modifier from the deck (drawFor)
// Each level can go on a new pylon or on an upgrade node of a pylon already there (sneak-right-click the pylon): a
// node adds levels of the pylon's modifier (its levels per node), and locks the pylon.
// Right-click it with a Spell Scroll to record the spell as it stands: every pylon in the tree, except those beyond a
// spell break without redstone power (that whole branch is off).
public class SpellPylonBlockEntity extends CBSyncedBlockEntity
{
    private @Nullable DustElement element;
    private UUID tree = UUID.randomUUID();
    private final List<BlockPos> chain = new ArrayList<>();
    private final List<Integer> parents = new ArrayList<>();
    private final SpellDeck deck = new SpellDeck();
    // The tree's experience, copied from SpellTreeData every second (so the client knows the level, for the ghosts)
    private int experience;
    // From a broken pylon's item: its element and kept experience, until an element is chosen
    private @Nullable PylonMemory memory;
    // While this pylon is being broken (its pylons are broken with it; they shouldn't report back)
    private boolean breaking;

    // ---- client only: the icons (element over this pylon, shape over the Shape Pylon) coming out like a genie from a
    // lamp while a player is near, and poofing away when they leave (clientTick; Client/SpellPylonRenderer draws them)
    public static final int APPEAR_TICKS = 20;
    public static final double NEAR_DISTANCE = 8.0;
    // The middle of the little dark square on top of the Spell Pylon and the Shape Pylon (where the icons come out)
    public static final double ICON_BASE_Y = 15.0 / 16.0;
    private boolean near;
    private int appear;
    private int lastAppear;
    // The ghosts (where the next pylon can go) work the same way from further off: they spin up out of the ground
    // while a player is within GHOST_DISTANCE, and poof away when they leave
    public static final double GHOST_DISTANCE = NEAR_DISTANCE * 4;
    public static final int GHOST_APPEAR_TICKS = 20;
    private int ghostAppear;
    private int lastGhostAppear;

    public SpellPylonBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.SPELL_PYLON.get(), pos, state);
    }

    public @Nullable DustElement element() { return element; }
    public List<BlockPos> chain() { return chain; }
    public int experience() { return experience; }
    public int blessed() { return deck.blessed(); }
    public int corruption() { return deck.corruption(); }

    public int level()
    {
        return SpellLevels.levelForXp(experience);
    }

    // Levels not yet spent on a modifier pylon or an upgrade node (the Shape Pylon is free)
    public int availableLevels()
    {
        if (chain.isEmpty()) return 0;
        int spent = chain.size() - 1;
        if (level != null) {
            for (int i = 1; i < chain.size(); i++) {
                if (level.getBlockEntity(chain.get(i)) instanceof PylonBlockEntity pylon) spent += pylon.nodes();
            }
        }
        return level() - spent;
    }

    // A pylon of this tree sneak-right-clicked: one of its upgrade nodes filled, for a level. It adds the modifier's
    // levels per node (a random number in its range) and locks the pylon (the first time: a lock, so blessing, and a
    // category pylon adds one of its category to the deck). The last node of a Neutral or Legendary pylon puts its
    // modifier back in the deck.
    public void upgrade(PylonBlockEntity pylon, ServerPlayer player)
    {
        if (!pylon.canUpgrade()) {
            player.sendOverlayMessage(Component.translatable(pylon.maxNodes() > 0 ? "dust.pylon.upgrade_full" : "dust.pylon.upgrade_none"));
            return;
        }
        if (availableLevels() <= 0) {
            player.sendOverlayMessage(Component.translatable("dust.pylon.upgrade_no_level"));
            return;
        }
        var random = level.getRandom();
        SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(pylon.modifier());
        int min = modifier == null ? 1 : modifier.minLevelsPerNode(), max = modifier == null ? 1 : Math.max(min, modifier.maxLevelsPerNode());
        boolean wasLocked = pylon.locked();
        pylon.upgrade(min + random.nextInt(max - min + 1));
        if (!wasLocked) locked(pylon);
        if (pylon.nodes() >= pylon.maxNodes() && pylon.card() != null && (pylon.type() == PylonType.NEUTRAL || pylon.legendary())) {
            deck.addCard(pylon.card());
        }
        deck.topUp(treeContext(null), random, categoryBonus(), doubleDraw());
        level.playSound(null, pylon.getBlockPos(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.8F);
        player.sendOverlayMessage(describe(pylon));
        sync();
    }

    // A pylon just locked: a step of blessing (two with Double Blessed Progress); a category pylon also puts another
    // modifier of its category from the pool into the deck
    private void locked(PylonBlockEntity pylon)
    {
        var random = level.getRandom();
        deck.bless(treeLevels().containsKey(SpellRegistries.DOUBLE_BLESSED.getId()) ? 2 : 1, random);
        ModifierCategory category = pylon.type().category();
        if (category != null && pylon.modifier() != null && pylon.tier() == ModifierTier.NORMAL) {
            deck.drawNormal(treeContext(null), random, categoryBonus(), doubleDraw(), modifier -> modifier.categories().contains(category));
        }
    }

    public @Nullable Identifier shape()
    {
        return !chain.isEmpty() && level != null && level.getBlockEntity(chain.getFirst()) instanceof PylonBlockEntity shapePylon ? shapePylon.shape() : null;
    }

    // The pylon's position in the tree (-1 for the Spell Pylon)
    private BlockPos positionOf(int index)
    {
        return index < 0 ? worldPosition : chain.get(index);
    }

    private boolean hasChildren(int index)
    {
        return parents.contains(index);
    }

    // ---- where the next pylon can go (shown as ghosts) ----

    // The Shape Pylon's place (the Spell Pylon's connection side, once it has an element), or the next modifier pylon's
    // (a free side of any pylon in the tree that can lead on, while a level is available)
    public List<BlockPos> ghostPositions()
    {
        List<BlockPos> places = new ArrayList<>();
        if (level == null || element == null) return places;
        if (chain.isEmpty()) {
            addIfFree(places, worldPosition.relative(connection()));
            return places;
        }
        if (availableLevels() <= 0) return places;
        for (int index = 0; index < chain.size(); index++) {
            if (!canLeadOn(index)) continue;
            for (Direction side : childSides(index)) addIfFree(places, chain.get(index).relative(side));
        }
        return places;
    }

    // The side of the Spell Pylon its connection arm points to (where the Shape Pylon goes)
    public Direction connection()
    {
        return getBlockState().getOptionalValue(SpellPylonBlock.FACING).orElse(Direction.NORTH);
    }

    // The way from a pylon of the tree to its parent
    private Direction parentSide(int index)
    {
        BlockPos at = chain.get(index), parent = positionOf(parents.get(index));
        Direction side = Direction.getNearest(parent.getX() - at.getX(), 0, parent.getZ() - at.getZ(), Direction.NORTH);
        return side.getAxis().isHorizontal() ? side : Direction.NORTH;
    }

    // The sides a pylon can lead on from: all but its parent's (a spell break: only straight on, as its model shows)
    private List<Direction> childSides(int index)
    {
        Direction back = parentSide(index);
        if (isBreak(index)) return List.of(back.getOpposite());
        List<Direction> sides = new ArrayList<>();
        for (Direction side : Direction.Plane.HORIZONTAL) if (side != back) sides.add(side);
        return sides;
    }

    // Whether another pylon can be placed after this one: the Shape Pylon and a spell break lead on to just one
    private boolean canLeadOn(int index)
    {
        // A null corruption's stop ends its branch
        if (index > 0 && level != null && level.getBlockEntity(chain.get(index)) instanceof PylonBlockEntity pylon && pylon.stop()) return false;
        return (index > 0 && !isBreak(index)) || !hasChildren(index);
    }

    private boolean isBreak(int index)
    {
        return index > 0 && level != null && level.getBlockEntity(chain.get(index)) instanceof PylonBlockEntity pylon && pylon.isSpellBreak();
    }

    private void addIfFree(List<BlockPos> places, BlockPos pos)
    {
        if (level != null && !places.contains(pos) && !pos.equals(worldPosition) && !chain.contains(pos) && level.getBlockState(pos).canBeReplaced()) {
            places.add(pos);
        }
    }

    // Whether a pylon of this type just placed at pos joins the tree
    public boolean accepts(BlockPos pos, PylonType type)
    {
        if (level == null || element == null || chain.contains(pos)) return false;
        if (chain.isEmpty()) return type == PylonType.SHAPE && pos.equals(worldPosition.relative(connection()));
        return type != PylonType.SHAPE && availableLevels() > 0 && parentFor(pos) != NO_PARENT;
    }

    private static final int NO_PARENT = -2;

    // The pylon a new one at pos is placed against: the most recently placed beside it that can lead on that way
    private int parentFor(BlockPos pos)
    {
        for (int index = chain.size() - 1; index >= 0; index--) {
            BlockPos at = chain.get(index);
            if (!isBeside(pos, at) || !canLeadOn(index)) continue;
            Direction side = Direction.getNearest(pos.getX() - at.getX(), 0, pos.getZ() - at.getZ(), Direction.NORTH);
            if (childSides(index).contains(side)) return index;
        }
        return NO_PARENT;
    }

    // ---- the pylons' looks (PylonBlock.PARENT / LINKS) ----

    // Turns a pylon's model to its parent and shows the arms to the pylons after it (straight on, left, right: seen
    // looking away from the parent; Direction.getClockWise turns a quarter right, seen from above)
    private void refreshLook(int index)
    {
        if (index < 0 || index >= chain.size() || !(level.getBlockEntity(chain.get(index)) instanceof PylonBlockEntity pylon)) return;
        Direction back = parentSide(index);
        Direction forward = back.getOpposite();
        boolean straight = false, left = false, right = false;
        for (int child = 0; child < chain.size(); child++) {
            if (parents.get(child) != index) continue;
            BlockPos at = chain.get(child), from = chain.get(index);
            Direction side = Direction.getNearest(at.getX() - from.getX(), 0, at.getZ() - from.getZ(), Direction.NORTH);
            if (side == forward) straight = true;
            else if (side == forward.getCounterClockWise()) left = true;
            else if (side == forward.getClockWise()) right = true;
        }
        pylon.updateLook(back, PylonLinks.of(straight, left, right));
    }

    private static boolean isBeside(BlockPos a, BlockPos b)
    {
        return a.getY() == b.getY() && a.distManhattan(b) == 1;
    }

    // ---- joining a pylon ----

    // A pylon placed in one of the ghost places: the Shape Pylon takes the placer's first known shape; a modifier
    // pylon draws from the deck (drawFor). The first modifier pylon fixes the shape, which fills the pool and deck.
    public void attach(PylonBlockEntity pylon, @Nullable ServerPlayer placer)
    {
        if (!(level instanceof ServerLevel server)) return;
        PylonType type = pylon.type();
        if (chain.isEmpty()) {
            pylon.link(worldPosition, null, null, ModifierTier.NORMAL, false, defaultShape(placer), 0, false, false);
            chain.add(pylon.getBlockPos());
            parents.add(-1);
            refreshLook(0);
            server.playSound(null, pylon.getBlockPos(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);
            sync();
            return;
        }
        var random = level.getRandom();
        int parent = parentFor(pylon.getBlockPos());
        if (placer != null && element != null) SpellMastery.note(placer, element, level());
        // The shape is fixed from now on: the Shape Pylon locks, and the pool and deck fill
        if (!deck.started()) deck.start(treeContext(placer), random, categoryBonus(), doubleDraw());
        if (parent == 0 && level.getBlockEntity(chain.getFirst()) instanceof PylonBlockEntity shapePylon) shapePylon.lock();
        deck.addDueBreaks(level());

        Draw draw = drawFor(type, pylon.getBlockPos(), context(parent, placer), placer);
        pylon.link(worldPosition, draw.card(), draw.modifier(), draw.tier(), draw.spellBreak(), null, draw.nodes(), draw.legendary(), draw.stop());
        chain.add(pylon.getBlockPos());
        parents.add(parent);
        refreshLook(chain.size() - 1);
        refreshLook(parent);
        if (pylon.locked()) locked(pylon);
        // A shape modifier brings its own modifiers into the pool
        SpellModifier modifier = draw.modifier() == null ? null : SpellRegistries.MODIFIER_REGISTRY.getValue(draw.modifier());
        if (modifier != null) deck.bringIn(modifier, random);
        deck.topUp(treeContext(placer), random, categoryBonus(), doubleDraw());

        if (placer != null && draw.modifier() != null && element != null) SpellKnowledge.unlockModifier(placer, element, draw.modifier());
        server.playSound(null, pylon.getBlockPos(), draw.spellBreak() ? SoundEvents.RESPAWN_ANCHOR_DEPLETE.value() : SoundEvents.AMETHYST_BLOCK_CHIME,
                SoundSource.BLOCKS, 1.0F, draw.tier() == ModifierTier.CORRUPTED || draw.stop() ? 0.6F : draw.tier() == ModifierTier.BLESSED || draw.legendary() ? 1.6F : 1.1F);
        if (placer != null) placer.sendOverlayMessage(describe(pylon));
        sync();
    }

    private Identifier defaultShape(@Nullable ServerPlayer placer)
    {
        if (placer != null) {
            for (Identifier known : SpellKnowledge.get(placer).shapes()) {
                if (SpellRegistries.SHAPE_REGISTRY.containsKey(known)) return known;
            }
        }
        return SpellRegistries.SELF.getId();
    }

    // What a new pylon drew: the card it took, its modifier (and tier), or a spell break, or a stop; how many upgrade
    // nodes it gets; whether it's Legendary. All empty: there was nothing it could draw.
    private record Draw(@Nullable Identifier card, @Nullable Identifier modifier, ModifierTier tier, boolean spellBreak,
                        boolean legendary, boolean stop, int nodes)
    {
        static final Draw NOTHING = new Draw(null, null, ModifierTier.NORMAL, false, false, false, 0);
    }

    // What a pylon of this kind at pos draws from the deck:
    //   a corrupted card in the deck is taken first, whatever the pylon (a NULL_CORRUPTION: a stop)
    //   a Neutral pylon takes any card that fits here: a blessed card makes it Blessed (or now and then Legendary), a
    //     BLESSING card may make it Legendary (otherwise it draws again), a spell break card makes it a spell break
    //   a category pylon takes a normal modifier of its category, or any normal modifier if the deck has none
    // The card leaves the deck (it goes back if the pylon is broken before it locks).
    private Draw drawFor(PylonType type, BlockPos pos, ModifierContext line, @Nullable ServerPlayer placer)
    {
        var random = level.getRandom();
        List<Identifier> cards = deck.cards();
        for (Identifier card : List.copyOf(cards)) {
            if (card.equals(SpellDeck.NULL_CORRUPTION)) {
                deck.removeCard(card);
                return new Draw(card, null, ModifierTier.CORRUPTED, false, false, true, 0);
            }
            SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(card);
            if (modifier != null && modifier.tier() == ModifierTier.CORRUPTED) {
                deck.removeCard(card);
                return new Draw(card, card, ModifierTier.CORRUPTED, false, false, false, 0);
            }
        }
        SpellKnowledge knowledge = placer == null ? SpellKnowledge.EMPTY : SpellKnowledge.get(placer);
        boolean afterBreak = lastParentIsBreak;
        List<Identifier> fits = new ArrayList<>();
        for (Identifier card : cards) {
            if (fitsHere(card, type, line, knowledge, afterBreak)) fits.add(card);
        }
        if (type.category() != null) {
            List<Identifier> ours = fits.stream().filter(card -> {
                SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(card);
                return modifier != null && modifier.categories().contains(type.category());
            }).toList();
            if (!ours.isEmpty()) fits = ours;
        }
        if (fits.isEmpty()) return Draw.NOTHING;
        Identifier card = fits.get(random.nextInt(fits.size()));
        deck.removeCard(card);

        if (card.equals(SpellDeck.SPELL_BREAK)) return new Draw(card, null, ModifierTier.NORMAL, true, false, false, 0);
        float legendaryChance = PylonConfig.LEGENDARY_CHANCE * (deck.blessingsLeft() ? 1 : 2);
        SpellModifier drawn = SpellRegistries.MODIFIER_REGISTRY.getValue(card);
        boolean blessedCard = card.equals(SpellDeck.BLESSING) || (drawn != null && drawn.tier() == ModifierTier.BLESSED);
        if (blessedCard && random.nextFloat() < legendaryChance) {
            Identifier legendary = deck.randomFor(line, random);
            if (legendary != null) {
                int nodes = PylonConfig.LEGENDARY_MIN_NODES + random.nextInt(PylonConfig.LEGENDARY_MAX_NODES - PylonConfig.LEGENDARY_MIN_NODES + 1);
                return new Draw(legendary, resolve(legendary, line), ModifierTier.NORMAL, false, true, false, nodes);
            }
        }
        if (card.equals(SpellDeck.BLESSING)) {
            // No Legendary: an ordinary draw instead, from what's left
            List<Identifier> normal = fits.stream().filter(c -> !c.equals(card) && !SpellDeck.isSpecial(c) && tierOf(c) == ModifierTier.NORMAL
                    && deck.cards().contains(c)).toList();
            if (normal.isEmpty()) return Draw.NOTHING;
            Identifier other = normal.get(random.nextInt(normal.size()));
            deck.removeCard(other);
            return normalDraw(other, type, pos, line);
        }
        if (blessedCard) return new Draw(card, card, ModifierTier.BLESSED, false, false, false, 0);
        return normalDraw(card, type, pos, line);
    }

    // A normal modifier card on a pylon of this kind: its nodes (fewer for each reroll at the spot), and a stand-in
    // (the Fragment slot) resolved into what it becomes
    private Draw normalDraw(Identifier card, PylonType type, BlockPos pos, ModifierContext line)
    {
        int[] range = type.nodeRange(deck.rerolls(pos));
        int nodes = Math.min(PylonConfig.MAX_NODES, range[0] + level.getRandom().nextInt(range[1] - range[0] + 1));
        Identifier modifier = resolve(card, line);
        return modifier == null ? Draw.NOTHING : new Draw(card, modifier, ModifierTier.NORMAL, false, false, false, nodes);
    }

    private @Nullable Identifier resolve(Identifier card, ModifierContext line)
    {
        SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(card);
        if (modifier == null) return null;
        Identifier resolved = modifier.resolveRoll(line, level.getRandom());
        return resolved != null && SpellRegistries.MODIFIER_REGISTRY.containsKey(resolved) ? resolved : card;
    }

    // Whether a card can go on a pylon of this kind here: a spell break or blessing only on a Neutral pylon (a spell
    // break never straight after another); a modifier if it suits the line and the placer has it unlocked (a blessed
    // one only on a Neutral pylon)
    private boolean fitsHere(Identifier card, PylonType type, ModifierContext line, SpellKnowledge knowledge, boolean afterBreak)
    {
        if (card.equals(SpellDeck.SPELL_BREAK)) return type == PylonType.NEUTRAL && !afterBreak;
        if (card.equals(SpellDeck.BLESSING)) return type == PylonType.NEUTRAL;
        if (card.equals(SpellDeck.NULL_CORRUPTION)) return false;
        SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(card);
        if (modifier == null || !modifier.canRoll(line)) return false;
        // A Neutral pylon passes over what only a category pylon can use (Category Weight)
        if (modifier.categoryPylonsOnly() && type.category() == null) return false;
        if (!modifier.startsUnlocked() && !knowledge.hasUnlocked(element, card)) return false;
        return modifier.tier() == ModifierTier.NORMAL || (modifier.tier() == ModifierTier.BLESSED && type == PylonType.NEUTRAL);
    }

    private static @Nullable ModifierTier tierOf(Identifier id)
    {
        SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(id);
        return modifier == null ? null : modifier.tier();
    }

    // Set by context(): whether that pylon's parent is a spell break
    private boolean lastParentIsBreak;

    // ---- what the modifiers see ----

    // The context for a pylon placed against parent: its line, the whole tree, the counts
    private ModifierContext context(int parent, @Nullable ServerPlayer placer)
    {
        lastParentIsBreak = parent >= 1 && level.getBlockEntity(chain.get(parent)) instanceof PylonBlockEntity last && last.isSpellBreak();
        return buildContext(modifiersUpTo(parent), breaksUpTo(parent), placer);
    }

    // The context for drawing into the deck, which isn't for any one line: the whole tree as the line, any number of
    // breaks
    private ModifierContext treeContext(@Nullable ServerPlayer placer)
    {
        return buildContext(treeModifiers(), 99, placer);
    }

    private ModifierContext buildContext(List<Identifier> line, int breaks, @Nullable ServerPlayer placer)
    {
        Identifier shape = shape() == null ? SpellRegistries.SELF.getId() : shape();
        Map<Identifier, Integer> levels = treeLevels();
        boolean[] types = effectTypes(shape);
        int reduction = levels.containsKey(SpellRegistries.REDUCED_REQUIREMENTS.getId()) ? PylonConfig.REDUCED_REQUIREMENT : 0;
        return new ModifierContext(element, shape, line, treeModifiers(), levels, level(), breaks,
                placer == null ? Map.of() : SpellMastery.get(placer).levels(), types[0], types[1], reduction,
                levels.getOrDefault(SpellRegistries.FRAGMENT_PROGRESS.getId(), 0));
    }

    // Whether the spell's effect, on what its shape lands on, is instant / lingering: {instant, dot}. Self lands on
    // the caster; the other shapes on a creature or a block.
    private boolean[] effectTypes(Identifier shape)
    {
        var effect = element == null ? null : SpellRegistries.ELEMENT_EFFECT_REGISTRY.getValue(element.id());
        if (effect == null) return new boolean[] {true, true};
        List<com.mowmaster.dust.Features.Spells.api.EffectType> types = shape.equals(SpellRegistries.SELF.getId())
                ? List.of(effect.type(com.mowmaster.dust.Features.Spells.api.SpellTarget.SELF))
                : List.of(effect.type(com.mowmaster.dust.Features.Spells.api.SpellTarget.ENTITY), effect.type(com.mowmaster.dust.Features.Spells.api.SpellTarget.BLOCK));
        return new boolean[] {types.contains(com.mowmaster.dust.Features.Spells.api.EffectType.INSTANT), types.contains(com.mowmaster.dust.Features.Spells.api.EffectType.DOT)};
    }

    // Every modifier in the tree, and how many levels of each (a Legendary pylon's count double)
    private List<Identifier> treeModifiers()
    {
        List<Identifier> modifiers = new ArrayList<>();
        if (level == null) return modifiers;
        for (int i = 1; i < chain.size(); i++) {
            if (level.getBlockEntity(chain.get(i)) instanceof PylonBlockEntity pylon && pylon.modifier() != null) modifiers.add(pylon.modifier());
        }
        return modifiers;
    }

    private Map<Identifier, Integer> treeLevels()
    {
        Map<Identifier, Integer> levels = new HashMap<>();
        if (level == null) return levels;
        for (int i = 1; i < chain.size(); i++) {
            if (level.getBlockEntity(chain.get(i)) instanceof PylonBlockEntity pylon && pylon.modifier() != null) {
                levels.merge(pylon.modifier(), pylon.levels() * (pylon.legendary() ? 2 : 1), Integer::sum);
            }
        }
        return levels;
    }

    // Category Weight: for each category, the levels of it on that category's pylons
    private Map<ModifierCategory, Integer> categoryBonus()
    {
        Map<ModifierCategory, Integer> bonus = new EnumMap<>(ModifierCategory.class);
        if (level == null) return bonus;
        Identifier weight = SpellRegistries.CATEGORY_WEIGHT.getId();
        for (int i = 1; i < chain.size(); i++) {
            if (level.getBlockEntity(chain.get(i)) instanceof PylonBlockEntity pylon && weight.equals(pylon.modifier()) && pylon.type().category() != null) {
                bonus.merge(pylon.type().category(), pylon.levels(), Integer::sum);
            }
        }
        return bonus;
    }

    // Double Draw: 5% a level
    private float doubleDraw()
    {
        return 0.05F * treeLevels().getOrDefault(SpellRegistries.DOUBLE_DRAW.getId(), 0);
    }

    // How many spell breaks are on the way from the Spell Pylon to this pylon, inclusive
    private int breaksUpTo(int index)
    {
        int breaks = 0;
        for (int i = index; i >= 1; i = parents.get(i)) {
            if (level.getBlockEntity(chain.get(i)) instanceof PylonBlockEntity pylon && pylon.isSpellBreak()) breaks++;
        }
        return breaks;
    }

    // The modifiers on the way from the Spell Pylon to this pylon, inclusive (preconditions look at its own branch)
    private List<Identifier> modifiersUpTo(int index)
    {
        List<Identifier> modifiers = new ArrayList<>();
        for (int i = index; i >= 1; i = parents.get(i)) {
            if (level.getBlockEntity(chain.get(i)) instanceof PylonBlockEntity pylon && pylon.modifier() != null) modifiers.addFirst(pylon.modifier());
        }
        return modifiers;
    }

    // The Shape Pylon right-clicked: the next shape the player knows (only before any modifier pylon: those may depend
    // on the shape)
    public void cycleShape(PylonBlockEntity shapePylon, ServerPlayer player)
    {
        if (chain.size() > 1) {
            player.sendOverlayMessage(Component.translatable("dust.pylon.shape_fixed"));
            return;
        }
        List<Identifier> known = SpellKnowledge.get(player).shapes().stream().filter(SpellRegistries.SHAPE_REGISTRY::containsKey).toList();
        if (known.isEmpty()) {
            player.sendOverlayMessage(Component.translatable("dust.pylon.no_shapes"));
            return;
        }
        int at = known.indexOf(shapePylon.shape());
        shapePylon.setShape(known.get((at + 1) % known.size()));
        player.sendOverlayMessage(describe(shapePylon));
        sync();
    }

    // ---- a pylon broken ----

    // A modifier pylon broken: a reroll (its modifier won't come up at that spot again; corruption), and everything
    // grown from it is cut off (those pylons fall dark until broken and placed again). The Shape Pylon broken: the
    // whole tree is cut off.
    void pylonRemoved(PylonBlockEntity pylon)
    {
        if (breaking) return;
        int index = chain.indexOf(pylon.getBlockPos());
        if (index < 0) return;
        BlockPos parentPos = parents.get(index) >= 0 ? chain.get(parents.get(index)) : null;
        var random = level.getRandom();
        if (index >= 1) {
            // A reroll: corruption, its card back in the deck (a spell break's isn't), fewer nodes at this spot next time
            deck.corrupt(random);
            returnCard(pylon);
            deck.rerolled(pylon.getBlockPos());
        } else {
            // The Shape Pylon (only while nothing follows it): the next shape starts the pool afresh
            deck.reset();
        }
        // It and everything grown from it (a pylon is in the subtree if its parent is)
        boolean[] removed = new boolean[chain.size()];
        removed[index] = true;
        // The Shape Pylon takes the whole tree with it (even branches placed against the Spell Pylon itself)
        if (index == 0) java.util.Arrays.fill(removed, true);
        for (int i = index + 1; i < chain.size(); i++) {
            if (parents.get(i) >= 0 && removed[parents.get(i)]) removed[i] = true;
        }
        // Rebuild the lists without them, renumbering the parents
        int[] newIndex = new int[chain.size()];
        List<BlockPos> keptChain = new ArrayList<>();
        List<Integer> keptParents = new ArrayList<>();
        for (int i = 0; i < chain.size(); i++) {
            if (removed[i]) {
                newIndex[i] = NO_PARENT;
                if (i != index && level.getBlockEntity(chain.get(i)) instanceof PylonBlockEntity cut) {
                    returnCard(cut);
                    cut.unlink();
                }
                continue;
            }
            newIndex[i] = keptChain.size();
            keptChain.add(chain.get(i));
            keptParents.add(parents.get(i) < 0 ? -1 : newIndex[parents.get(i)]);
        }
        chain.clear();
        chain.addAll(keptChain);
        parents.clear();
        parents.addAll(keptParents);
        // Its parent loses that arm
        if (parentPos != null) refreshLook(chain.indexOf(parentPos));
        deck.topUp(treeContext(null), random, categoryBonus(), doubleDraw());
        sync();
    }

    // An unlocked pylon leaving the tree: its card goes back in the deck (not a spell break's)
    private void returnCard(PylonBlockEntity pylon)
    {
        if (!pylon.locked() && pylon.card() != null && !pylon.card().equals(SpellDeck.SPELL_BREAK)) deck.addCard(pylon.card());
    }

    // ---- element, recording, info ----

    // An element crystal used on it (only while it has none). A remembered element that matches brings back the kept
    // experience.
    public boolean setElement(DustElement chosen)
    {
        if (element != null || !(level instanceof ServerLevel server)) return false;
        element = chosen;
        int start = memory != null && memory.element() == chosen ? memory.experience() : 0;
        memory = null;
        SpellTreeData.get(server.getServer()).setExperience(tree, start);
        experience = start;
        level.setBlockAndUpdate(worldPosition, getBlockState().setValue(SpellPylonBlock.ACTIVE, true));
        sync();
        return true;
    }

    public void setMemory(@Nullable PylonMemory memory)
    {
        this.memory = memory;
        setChanged();
    }

    // The spell as it stands: its element and shape, then the modifier of every pylon in the tree in the order placed,
    // except a spell break without redstone power and everything grown from it. Null without an element or shape.
    // pylonsOut gets how many pylons the spell uses.
    public @Nullable Spell compile(int[] pylonsOut)
    {
        Identifier shape = shape();
        if (element == null || shape == null || level == null) return null;
        boolean[] off = new boolean[chain.size()];
        List<Identifier> modifiers = new ArrayList<>();
        List<Integer> levels = new ArrayList<>();
        int pylons = 2;
        for (int i = 1; i < chain.size(); i++) {
            int parent = parents.get(i);
            if (parent >= 1 && off[parent]) {
                off[i] = true;
                continue;
            }
            if (!(level.getBlockEntity(chain.get(i)) instanceof PylonBlockEntity pylon)) {
                off[i] = true;
                continue;
            }
            if (pylon.isSpellBreak() && !level.hasNeighborSignal(pylon.getBlockPos())) {
                off[i] = true;
                continue;
            }
            pylons++;
            if (pylon.modifier() != null) {
                modifiers.add(pylon.modifier());
                levels.add(pylon.levels() * (pylon.legendary() ? 2 : 1));
            }
        }
        if (pylonsOut != null && pylonsOut.length > 0) pylonsOut[0] = pylons;
        return new Spell(element, shape, Map.of(), modifiers, levels);
    }

    // A Spell Scroll used on it: the spell as it stands is written onto the scroll, linked to this tree
    // A spell tool used on it (SpellTools): it takes the spell as it stands, like a scroll, if it's a Touch spell.
    // Returns null if it took it, otherwise why not (a translation key).
    public @Nullable String recordTool(ItemStack tool)
    {
        int[] pylons = new int[1];
        Spell spell = compile(pylons);
        if (spell == null) return "dust.pylon.cant_record";
        if (!spell.shape().equals(SpellRegistries.TOUCH.getId())) return "dust.pylon.tool_needs_touch";
        tool.set(DustComponentDataRegistry.SPELL.get(), spell);
        tool.set(DustComponentDataRegistry.SPELL_TREE.get(), new SpellTreeLink(tree, pylons[0]));
        return null;
    }

    public boolean record(ItemStack scroll)
    {
        int[] pylons = new int[1];
        Spell spell = compile(pylons);
        if (spell == null) return false;
        scroll.set(DustComponentDataRegistry.SPELL.get(), spell);
        scroll.set(DustComponentDataRegistry.SPELL_TREE.get(), new SpellTreeLink(tree, pylons[0]));
        return true;
    }

    // "Fire spell: level 3 (5/13), 1 level to spend; blessing 2, corruption 1"
    public Component info()
    {
        if (element == null) return Component.translatable("dust.pylon.no_element");
        int lvl = level();
        int into = experience - SpellLevels.xpForLevel(lvl);
        return Component.translatable("dust.pylon.info", element.name(), lvl, into, SpellLevels.xpForNextLevel(lvl), Math.max(0, availableLevels()),
                deck.blessed(), deck.corruption());
    }

    // What a modifier pylon holds, for its overlay message
    public static Component describe(PylonBlockEntity pylon)
    {
        if (pylon.main() == null) return Component.translatable("dust.pylon.inert");
        if (pylon.type() == PylonType.SHAPE) {
            Identifier shape = pylon.shape();
            return Component.translatable("dust.pylon.shape", shape == null ? Component.literal("?")
                    : Component.translatable("spell_shape." + shape.getNamespace() + "." + shape.getPath()));
        }
        if (pylon.isSpellBreak()) return Component.translatable(pylon.getLevel() != null && pylon.getLevel().hasNeighborSignal(pylon.getBlockPos())
                ? "dust.pylon.break_on" : "dust.pylon.break_off");
        if (pylon.stop()) return Component.translatable("dust.pylon.stop");
        Identifier modifier = pylon.modifier();
        if (modifier == null) return Component.translatable("dust.pylon.nothing");
        Component name = Component.translatable("dust.pylon.modifier." + pylon.tier().getSerializedName(),
                Component.translatable("spell_modifier." + modifier.getNamespace() + "." + modifier.getPath()));
        if (pylon.legendary()) name = Component.translatable("dust.pylon.legendary", name);
        Component line = pylon.maxNodes() == 0 ? name
                : Component.translatable("dust.pylon.with_nodes", name, pylon.nodes(), pylon.maxNodes(), pylon.levels() * (pylon.legendary() ? 2 : 1));
        return pylon.locked() ? Component.translatable("dust.pylon.locked_suffix", line) : line;
    }

    // ---- ticking: the tree's experience, from wherever its spells were cast ----

    public static void serverTick(Level level, BlockPos pos, BlockState state, SpellPylonBlockEntity pylon)
    {
        if (level.getGameTime() % 20 != 0 || !(level instanceof ServerLevel server)) return;
        int xp = SpellTreeData.get(server.getServer()).experience(pylon.tree);
        if (xp != pylon.experience) {
            boolean levelled = SpellLevels.levelForXp(xp) > pylon.level();
            pylon.experience = xp;
            if (levelled) level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.6F, 1.4F);
            pylon.sync();
        }
    }

    // ---- client: the icons, near a player ----

    // Whether the player is within this distance of any block of the tree (this pylon or any of its pylons)
    private boolean isNear(net.minecraft.world.entity.player.Player player, double distance)
    {
        double max = distance * distance;
        if (player.distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(worldPosition)) <= max) return true;
        for (BlockPos pos : chain) {
            if (player.distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(pos)) <= max) return true;
        }
        return false;
    }

    // Every tick on the client: the icons rise out (APPEAR_TICKS) while this computer's player is near, trailing dust;
    // when they leave, the icons vanish in a puff. isLocalPlayer: the player this game is being played by (only true on
    // their own client), so this works without touching client-only classes.
    public static void clientTick(Level level, BlockPos pos, BlockState state, SpellPylonBlockEntity pylon)
    {
        pylon.lastAppear = pylon.appear;
        pylon.lastGhostAppear = pylon.ghostAppear;
        if (pylon.element == null) {
            pylon.appear = 0;
            pylon.ghostAppear = 0;
            return;
        }
        boolean nowNear = false, ghostsNear = false;
        for (net.minecraft.world.entity.player.Player player : level.players()) {
            if (!player.isLocalPlayer() || player.isSpectator()) continue;
            if (pylon.isNear(player, NEAR_DISTANCE)) nowNear = true;
            if (pylon.isNear(player, GHOST_DISTANCE)) ghostsNear = true;
        }
        // The ghosts: up while near (only while there's somewhere to put a pylon), a puff at each when they go
        if (ghostsNear) {
            if (pylon.ghostAppear < GHOST_APPEAR_TICKS) pylon.ghostAppear++;
        } else if (pylon.ghostAppear > 0) {
            pylon.ghostPoof(level);
            pylon.ghostAppear = 0;
            pylon.lastGhostAppear = 0;
        }
        pylon.near = nowNear;
        if (nowNear) {
            if (pylon.appear < APPEAR_TICKS) {
                pylon.appear++;
                pylon.wisps(level);
            }
        } else if (pylon.appear > 0) {
            pylon.poof(level);
            pylon.appear = 0;
            pylon.lastAppear = 0;
        }
    }

    // The places the icons come out of: this pylon, and the Shape Pylon if there is one with a shape
    private List<BlockPos> iconPlaces()
    {
        List<BlockPos> places = new ArrayList<>();
        places.add(worldPosition);
        if (shape() != null) places.add(chain.getFirst());
        return places;
    }

    // Smoke curling out of the dark square while an icon rises, in the element's colour
    private void wisps(Level level)
    {
        var random = level.getRandom();
        float rise = appear / (float) APPEAR_TICKS;
        for (BlockPos place : iconPlaces()) {
            double y = place.getY() + ICON_BASE_Y + rise * 0.4;
            level.addParticle(new net.minecraft.core.particles.DustParticleOptions(element.color(), 0.5F),
                    place.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.25, y, place.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.25, 0, 0.03, 0);
        }
    }

    // The puff the icons vanish in
    private void poof(Level level)
    {
        var random = level.getRandom();
        for (BlockPos place : iconPlaces()) {
            double y = place.getY() + ICON_BASE_Y + ICON_RISE;
            for (int i = 0; i < 8; i++) {
                level.addParticle(net.minecraft.core.particles.ParticleTypes.POOF, place.getX() + 0.5, y, place.getZ() + 0.5,
                        (random.nextDouble() - 0.5) * 0.08, (random.nextDouble() - 0.5) * 0.08, (random.nextDouble() - 0.5) * 0.08);
            }
        }
    }

    // The puff each ghost vanishes in
    private void ghostPoof(Level level)
    {
        var random = level.getRandom();
        for (BlockPos ghost : ghostPositions()) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(net.minecraft.core.particles.ParticleTypes.POOF, ghost.getX() + 0.5, ghost.getY() + 0.6, ghost.getZ() + 0.5,
                        (random.nextDouble() - 0.5) * 0.1, random.nextDouble() * 0.06, (random.nextDouble() - 0.5) * 0.1);
            }
        }
    }

    // How far up the ghosts are, 0 (gone) to 1 (up), smoothed between ticks
    public float ghostAppear(float partialTick)
    {
        return net.minecraft.util.Mth.lerp(partialTick, lastGhostAppear, ghostAppear) / GHOST_APPEAR_TICKS;
    }

    // How far the icon rises above the dark square once it's all the way out (blocks)
    public static final double ICON_RISE = 0.55;

    // How far out the icons are, 0 (gone) to 1 (out), smoothed between ticks
    public float appear(float partialTick)
    {
        return net.minecraft.util.Mth.lerp(partialTick, lastAppear, appear) / APPEAR_TICKS;
    }

    // ---- broken ----

    // Every pylon of the tree breaks with it (dropping as items), and it drops itself remembering its element and a
    // tenth of its levels. Scrolls already recorded keep their spell, but it can't change any more.
    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        if (!(level instanceof ServerLevel server)) return;
        breaking = true;
        for (BlockPos pylon : List.copyOf(chain)) {
            if (level.getBlockEntity(pylon) instanceof PylonBlockEntity) level.destroyBlock(pylon, true);
        }
        ItemStack drop = new ItemStack(state.getBlock().asItem());
        if (element != null) {
            int kept = SpellLevels.xpForLevel((int) (level() * PylonConfig.KEPT_LEVELS));
            drop.set(DustComponentDataRegistry.PYLON_MEMORY.get(), new PylonMemory(element, kept));
        } else if (memory != null) {
            drop.set(DustComponentDataRegistry.PYLON_MEMORY.get(), memory);
        }
        Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
        SpellTreeData.get(server.getServer()).forget(tree);
    }

    // ---- saving (and the client's copy) ----

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (element != null) output.store("element", DustElements.CODEC, element);
        output.store("tree", UUIDUtil.CODEC, tree);
        output.store("chain", BlockPos.CODEC.listOf(), List.copyOf(chain));
        output.store("parents", Codec.INT.listOf(), List.copyOf(parents));
        output.store("deck", SpellDeck.Saved.CODEC, deck.save());
        output.putInt("experience", experience);
        if (memory != null) output.store("memory", PylonMemory.CODEC, memory);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        element = input.read("element", DustElements.CODEC).orElse(null);
        tree = input.read("tree", UUIDUtil.CODEC).orElse(tree);
        chain.clear();
        chain.addAll(input.read("chain", BlockPos.CODEC.listOf()).orElse(List.of()));
        parents.clear();
        parents.addAll(input.read("parents", Codec.INT.listOf()).orElse(List.of()));
        // Trees saved before branching were one line: each pylon's parent is the one before it
        while (parents.size() < chain.size()) parents.add(parents.size() - 1);
        input.read("deck", SpellDeck.Saved.CODEC).ifPresent(deck::load);
        experience = input.getIntOr("experience", 0);
        memory = input.read("memory", PylonMemory.CODEC).orElse(null);
        // On the client: the body's colour comes from the element (Client/SpellPylonTint), so draw it again
        if (level != null && level.isClientSide()) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), net.minecraft.world.level.block.Block.UPDATE_ALL);
    }
}
