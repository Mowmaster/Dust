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
//                after it starts a branch, which slightly favours the category of the pylon branched from
//                (PylonConfig.BRANCH_BOOST). The pylons' models show the links (PylonLinks).
//   excluded     for each spot, the modifiers rolled there and broken (they won't be rolled there again)
//   blessed, corruption   built up by keeping rolls / rerolling them; they let blessed / corrupted modifiers be rolled
//   boosts       how much each modifier category is currently favoured (category pylons add, every placement decays)
// Each level can go on a new pylon or on an upgrade node of a pylon already there (sneak-right-click the pylon): a
// node adds potency to the spell (PylonConfig.nodePoints), and 1 dust per point to its cost.
// Right-click it with a Spell Scroll to record the spell as it stands: every pylon in the tree, except those beyond a
// spell break without redstone power (that whole branch is off).
public class SpellPylonBlockEntity extends CBSyncedBlockEntity
{
    private @Nullable DustElement element;
    private UUID tree = UUID.randomUUID();
    private final List<BlockPos> chain = new ArrayList<>();
    private final List<Integer> parents = new ArrayList<>();
    private final Map<BlockPos, List<Identifier>> excluded = new HashMap<>();
    private int blessed;
    private int corruption;
    private final Map<ModifierCategory, Float> boosts = new EnumMap<>(ModifierCategory.class);
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

    public SpellPylonBlockEntity(BlockPos pos, BlockState state)
    {
        super(DustBlockEntityRegistry.SPELL_PYLON.get(), pos, state);
    }

    public @Nullable DustElement element() { return element; }
    public List<BlockPos> chain() { return chain; }
    public int experience() { return experience; }
    public int blessed() { return blessed; }
    public int corruption() { return corruption; }

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

    // A pylon of this tree sneak-right-clicked: one of its upgrade nodes filled, for a level
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
        pylon.upgrade(PylonConfig.nodePoints(level()));
        level.playSound(null, pylon.getBlockPos(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.8F);
        player.sendOverlayMessage(describe(pylon));
        sync();
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
    // pylon keeps its parent's roll (blessing, the first time something follows it), shifts the category boosts, and
    // rolls (a branch slightly favouring its parent's category)
    public void attach(PylonBlockEntity pylon, @Nullable ServerPlayer placer)
    {
        if (!(level instanceof ServerLevel server)) return;
        PylonType type = pylon.type();
        if (chain.isEmpty()) {
            pylon.link(worldPosition, null, ModifierTier.NORMAL, false, defaultShape(placer), 0, 0);
            chain.add(pylon.getBlockPos());
            parents.add(-1);
            refreshLook(0);
            server.playSound(null, pylon.getBlockPos(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);
            sync();
            return;
        }
        int parent = parentFor(pylon.getBlockPos());
        PylonBlockEntity parentPylon = parent >= 1 && level.getBlockEntity(chain.get(parent)) instanceof PylonBlockEntity p ? p : null;
        boolean branch = hasChildren(parent);
        // Placing on after a modifier pylon for the first time means its roll was kept
        if (parentPylon != null && !branch) blessed += PylonConfig.KEEP_BLESSING;
        boosts.replaceAll((category, boost) -> Math.max(0, boost - PylonConfig.BOOST_DECAY));
        if (type.category() != null) boosts.merge(type.category(), PylonConfig.CATEGORY_BOOST, Float::sum);
        ModifierCategory branchedFrom = branch && parentPylon != null ? parentPylon.type().category() : null;

        if (placer != null && element != null) SpellMastery.note(placer, element, level());
        Roll roll = roll(pylon.getBlockPos(), parent, branchedFrom, placer, type);
        // A normal modifier gets upgrade nodes; a blessed / corrupted one a strength instead (and is locked by link)
        SpellModifier rolled = roll.modifier() == null ? null : SpellRegistries.MODIFIER_REGISTRY.getValue(roll.modifier());
        boolean usesStrength = rolled != null && rolled.usesStrength();
        int nodes = rolled == null || usesStrength ? 0 : PylonConfig.MIN_NODES + level.getRandom().nextInt(PylonConfig.MAX_NODES - PylonConfig.MIN_NODES + 1);
        int strength = usesStrength ? PylonConfig.MIN_STRENGTH + level.getRandom().nextInt(PylonConfig.MAX_STRENGTH - PylonConfig.MIN_STRENGTH + 1) : 0;
        pylon.link(worldPosition, roll.modifier(), roll.tier(), roll.spellBreak(), null, nodes, strength);
        chain.add(pylon.getBlockPos());
        parents.add(parent);
        refreshLook(chain.size() - 1);
        refreshLook(parent);
        // The Shape Pylon is fixed once a pylon follows it
        if (parent == 0 && level.getBlockEntity(chain.getFirst()) instanceof PylonBlockEntity shapePylon) shapePylon.lock();
        if (placer != null && roll.modifier() != null && element != null) SpellKnowledge.unlockModifier(placer, element, roll.modifier());
        server.playSound(null, pylon.getBlockPos(), roll.spellBreak() ? SoundEvents.RESPAWN_ANCHOR_DEPLETE.value() : SoundEvents.AMETHYST_BLOCK_CHIME,
                SoundSource.BLOCKS, 1.0F, roll.tier() == ModifierTier.CORRUPTED ? 0.6F : roll.tier() == ModifierTier.BLESSED ? 1.6F : 1.1F);
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

    // What a roll gave: a modifier (with its tier), a spell break, or nothing (nothing left that could be rolled here)
    private record Roll(@Nullable Identifier modifier, ModifierTier tier, boolean spellBreak) {}

    // A weighted random pick for a new pylon at pos, placed against parent. branchedFrom: the category of the pylon a
    // branch grows from (slightly favoured), or null.
    private Roll roll(BlockPos pos, int parent, @Nullable ModifierCategory branchedFrom, @Nullable ServerPlayer placer, PylonType type)
    {
        List<Identifier> before = modifiersUpTo(parent);
        var random = level.getRandom();
        boolean afterBreak = parent >= 1 && level.getBlockEntity(chain.get(parent)) instanceof PylonBlockEntity last && last.isSpellBreak();
        // A spell break: from the spell's BREAK_MIN_LEVEL, only on a kind that can break, never right after another
        if (type.canBreak() && level() >= PylonConfig.BREAK_MIN_LEVEL && !afterBreak && random.nextFloat() < PylonConfig.BREAK_CHANCE) {
            return new Roll(null, ModifierTier.NORMAL, true);
        }
        ModifierContext context = new ModifierContext(element, shape() == null ? SpellRegistries.SELF.getId() : shape(), before, level(),
                breaksUpTo(parent), placer == null ? Map.of() : SpellMastery.get(placer).levels());
        List<Identifier> skip = excluded.getOrDefault(pos, List.of());
        SpellKnowledge knowledge = placer == null ? SpellKnowledge.EMPTY : SpellKnowledge.get(placer);

        Map<Identifier, Float> weights = new LinkedHashMap<>();
        for (var entry : SpellRegistries.MODIFIER_REGISTRY.entrySet()) {
            Identifier id = entry.getKey().identifier();
            SpellModifier modifier = entry.getValue();
            if (skip.contains(id) || !modifier.canRoll(context)) continue;
            if (!modifier.startsUnlocked() && !knowledge.hasUnlocked(element, id)) continue;
            float tierFactor = switch (modifier.tier()) {
                case NORMAL -> 1;
                case BLESSED -> blessed >= PylonConfig.TIER_MIN ? blessed / (float) PylonConfig.TIER_MIN : 0;
                case CORRUPTED -> corruption >= PylonConfig.TIER_MIN ? corruption / (float) PylonConfig.TIER_MIN : 0;
            };
            float boost = boosts.getOrDefault(modifier.category(), 0F) + (modifier.category() == branchedFrom ? PylonConfig.BRANCH_BOOST : 0);
            float weight = modifier.weight() * (1 + boost) * tierFactor;
            if (weight > 0) weights.put(id, weight);
        }
        if (weights.isEmpty()) return new Roll(null, ModifierTier.NORMAL, false);

        float pick = random.nextFloat() * weights.values().stream().reduce(0F, Float::sum);
        Identifier chosen = null;
        for (Map.Entry<Identifier, Float> weight : weights.entrySet()) {
            chosen = weight.getKey();
            pick -= weight.getValue();
            if (pick <= 0) break;
        }
        SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(chosen);
        // A stand-in (the Fragment slot) becomes the modifier it picks
        Identifier resolved = modifier == null ? null : modifier.resolveRoll(context, random);
        if (resolved != null && SpellRegistries.MODIFIER_REGISTRY.containsKey(resolved)) {
            chosen = resolved;
            modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(chosen);
        }
        ModifierTier tier = modifier == null ? ModifierTier.NORMAL : modifier.tier();
        if (tier == ModifierTier.BLESSED) blessed = Math.max(0, blessed - PylonConfig.TIER_USE);
        if (tier == ModifierTier.CORRUPTED) corruption = Math.max(0, corruption - PylonConfig.TIER_USE);
        return new Roll(chosen, tier, false);
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
        if (index >= 1) {
            corruption += PylonConfig.RESET_CORRUPTION;
            if (pylon.tier() == ModifierTier.BLESSED) blessed = Math.max(0, blessed - PylonConfig.BLESSED_LOSS);
            if (pylon.tier() == ModifierTier.CORRUPTED) corruption += PylonConfig.CORRUPTED_GAIN;
            if (pylon.modifier() != null) excluded.computeIfAbsent(pylon.getBlockPos(), p -> new ArrayList<>()).add(pylon.modifier());
        } else {
            excluded.clear();
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
                if (i != index && level.getBlockEntity(chain.get(i)) instanceof PylonBlockEntity cut) cut.unlink();
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
        sync();
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
        List<Integer> strengths = new ArrayList<>();
        int points = 0;
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
                strengths.add(pylon.strength());
            }
            points += pylon.points();
        }
        if (pylonsOut != null && pylonsOut.length > 0) pylonsOut[0] = pylons;
        // The upgrade nodes' points are potency levels: +1 potency and +1 dust each (Spell.stats / dustCost)
        Map<Identifier, Integer> attributes = points > 0 ? Map.of(SpellRegistries.POTENCY.getId(), points) : Map.of();
        return new Spell(element, shape, attributes, modifiers, strengths);
    }

    // A Spell Scroll used on it: the spell as it stands is written onto the scroll, linked to this tree
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
                blessed, corruption);
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
        Identifier modifier = pylon.modifier();
        if (modifier == null) return Component.translatable("dust.pylon.nothing");
        Component name = Component.translatable("dust.pylon.modifier." + pylon.tier().getSerializedName(),
                Component.translatable("spell_modifier." + modifier.getNamespace() + "." + modifier.getPath()));
        Component line = pylon.strength() > 0 ? Component.translatable("dust.pylon.with_strength", name, pylon.strength())
                : Component.translatable("dust.pylon.with_nodes", name, pylon.nodes(), pylon.maxNodes(), pylon.points());
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

    // Whether the player is within NEAR_DISTANCE of any block of the tree (this pylon or any of its pylons)
    private boolean isNear(net.minecraft.world.entity.player.Player player)
    {
        double max = NEAR_DISTANCE * NEAR_DISTANCE;
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
        if (pylon.element == null) {
            pylon.appear = 0;
            return;
        }
        boolean nowNear = false;
        for (net.minecraft.world.entity.player.Player player : level.players()) {
            if (player.isLocalPlayer() && !player.isSpectator() && pylon.isNear(player)) nowNear = true;
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

    // The exclusions are saved as a list of (spot, modifiers): a map's keys have to be text, and a BlockPos isn't
    private record Exclusion(BlockPos pos, List<Identifier> modifiers)
    {
        static final Codec<Exclusion> CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(instance -> instance.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(Exclusion::pos),
                Identifier.CODEC.listOf().fieldOf("modifiers").forGetter(Exclusion::modifiers)
        ).apply(instance, Exclusion::new));
    }
    private static final Codec<Map<ModifierCategory, Float>> BOOSTS_CODEC = Codec.unboundedMap(ModifierCategory.CODEC, Codec.FLOAT);

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (element != null) output.store("element", DustElements.CODEC, element);
        output.store("tree", UUIDUtil.CODEC, tree);
        output.store("chain", BlockPos.CODEC.listOf(), List.copyOf(chain));
        output.store("parents", Codec.INT.listOf(), List.copyOf(parents));
        if (!excluded.isEmpty()) {
            output.store("excluded", Exclusion.CODEC.listOf(), excluded.entrySet().stream().map(e -> new Exclusion(e.getKey(), e.getValue())).toList());
        }
        output.putInt("blessed", blessed);
        output.putInt("corruption", corruption);
        if (!boosts.isEmpty()) output.store("boosts", BOOSTS_CODEC, boosts);
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
        excluded.clear();
        input.read("excluded", Exclusion.CODEC.listOf()).ifPresent(list -> list.forEach(e -> excluded.put(e.pos(), new ArrayList<>(e.modifiers()))));
        blessed = input.getIntOr("blessed", 0);
        corruption = input.getIntOr("corruption", 0);
        boosts.clear();
        input.read("boosts", BOOSTS_CODEC).ifPresent(boosts::putAll);
        experience = input.getIntOr("experience", 0);
        memory = input.read("memory", PylonMemory.CODEC).orElse(null);
        // On the client: the body's colour comes from the element (Client/SpellPylonTint), so draw it again
        if (level != null && level.isClientSide()) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), net.minecraft.world.level.block.Block.UPDATE_ALL);
    }
}
