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
//                against, -1 for the Spell Pylon itself). First the Shape Pylon (on any side of the Spell Pylon, no
//                level needed); after that one modifier pylon per level, on any open side of any pylon in the tree
//                (the Spell Pylon included). Placing against a pylon that already has one after it starts a branch,
//                which slightly favours the category of the pylon branched from (PylonConfig.BRANCH_BOOST).
//   excluded     for each spot, the modifiers rolled there and broken (they won't be rolled there again)
//   blessed, corruption   built up by keeping rolls / rerolling them; they let blessed / corrupted modifiers be rolled
//   boosts       how much each modifier category is currently favoured (category pylons add, every placement decays)
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

    // Levels not yet spent on a modifier pylon (the Shape Pylon is free)
    public int availableLevels()
    {
        return chain.isEmpty() ? 0 : level() - (chain.size() - 1);
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

    // The Shape Pylon's places (any side of the Spell Pylon, once it has an element), or the next modifier pylon's (any
    // open side of any pylon in the tree, while a level is available)
    public List<BlockPos> ghostPositions()
    {
        List<BlockPos> places = new ArrayList<>();
        if (level == null || element == null) return places;
        if (chain.isEmpty()) {
            for (Direction side : Direction.Plane.HORIZONTAL) addIfFree(places, worldPosition.relative(side));
            return places;
        }
        if (availableLevels() <= 0) return places;
        for (int index = -1; index < chain.size(); index++) {
            for (Direction side : Direction.Plane.HORIZONTAL) addIfFree(places, positionOf(index).relative(side));
        }
        return places;
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
        if (chain.isEmpty()) return type == PylonType.SHAPE && isBeside(pos, worldPosition);
        return type != PylonType.SHAPE && availableLevels() > 0 && parentFor(pos) != NO_PARENT;
    }

    private static final int NO_PARENT = -2;

    // The pylon a new one at pos is placed against: the most recently placed beside it, else the Spell Pylon (-1)
    private int parentFor(BlockPos pos)
    {
        for (int index = chain.size() - 1; index >= 0; index--) {
            if (isBeside(pos, chain.get(index))) return index;
        }
        return isBeside(pos, worldPosition) ? -1 : NO_PARENT;
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
            pylon.link(worldPosition, null, ModifierTier.NORMAL, false, defaultShape(placer));
            chain.add(pylon.getBlockPos());
            parents.add(-1);
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

        Roll roll = roll(pylon.getBlockPos(), parent, branchedFrom, placer);
        pylon.link(worldPosition, roll.modifier(), roll.tier(), roll.spellBreak(), null);
        chain.add(pylon.getBlockPos());
        parents.add(parent);
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
    private Roll roll(BlockPos pos, int parent, @Nullable ModifierCategory branchedFrom, @Nullable ServerPlayer placer)
    {
        List<Identifier> before = modifiersUpTo(parent);
        var random = level.getRandom();
        boolean afterBreak = parent >= 1 && level.getBlockEntity(chain.get(parent)) instanceof PylonBlockEntity last && last.isSpellBreak();
        if (before.size() >= PylonConfig.BREAK_MIN_MODIFIERS && !afterBreak && random.nextFloat() < PylonConfig.BREAK_CHANCE) {
            return new Roll(null, ModifierTier.NORMAL, true);
        }
        ModifierContext context = new ModifierContext(element, shape() == null ? SpellRegistries.SELF.getId() : shape(), before);
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
        ModifierTier tier = modifier == null ? ModifierTier.NORMAL : modifier.tier();
        if (tier == ModifierTier.BLESSED) blessed = Math.max(0, blessed - PylonConfig.TIER_USE);
        if (tier == ModifierTier.CORRUPTED) corruption = Math.max(0, corruption - PylonConfig.TIER_USE);
        return new Roll(chosen, tier, false);
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
            if (pylon.modifier() != null) modifiers.add(pylon.modifier());
        }
        if (pylonsOut != null && pylonsOut.length > 0) pylonsOut[0] = pylons;
        return new Spell(element, shape, Map.of(), modifiers);
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
        return Component.translatable("dust.pylon.modifier." + pylon.tier().getSerializedName(),
                Component.translatable("spell_modifier." + modifier.getNamespace() + "." + modifier.getPath()));
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
    }
}
