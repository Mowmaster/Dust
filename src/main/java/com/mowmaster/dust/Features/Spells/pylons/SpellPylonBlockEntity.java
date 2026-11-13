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
//   chain        its pylons in order: first the Shape Pylon (on any side of it, no level needed), then one modifier
//                pylon per level, each on one of the three free sides of the one before (never back the way it came)
//   excluded     for each place in the chain, the modifiers rolled there and broken (they won't be rolled there again)
//   blessed, corruption   built up by keeping rolls / rerolling them; they let blessed / corrupted modifiers be rolled
//   boosts       how much each modifier category is currently favoured (category pylons add, every placement decays)
// Right-click it with a Spell Scroll to record the spell as it stands (spell breaks without redstone end it there).
public class SpellPylonBlockEntity extends CBSyncedBlockEntity
{
    private @Nullable DustElement element;
    private UUID tree = UUID.randomUUID();
    private final List<BlockPos> chain = new ArrayList<>();
    private final Map<Integer, List<Identifier>> excluded = new HashMap<>();
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

    // ---- where the next pylon can go (shown as ghosts) ----

    // The Shape Pylon's places (any side of this, once it has an element), or the next modifier pylon's (the free
    // sides of the last pylon, except back the way the line came, while a level is available)
    public List<BlockPos> ghostPositions()
    {
        List<BlockPos> places = new ArrayList<>();
        if (level == null || element == null) return places;
        if (chain.isEmpty()) {
            for (Direction side : Direction.Plane.HORIZONTAL) addIfFree(places, worldPosition.relative(side));
            return places;
        }
        if (availableLevels() <= 0) return places;
        BlockPos tip = chain.getLast();
        BlockPos before = chain.size() >= 2 ? chain.get(chain.size() - 2) : worldPosition;
        for (Direction side : Direction.Plane.HORIZONTAL) {
            BlockPos next = tip.relative(side);
            if (!next.equals(before)) addIfFree(places, next);
        }
        return places;
    }

    private void addIfFree(List<BlockPos> places, BlockPos pos)
    {
        if (level != null && level.getBlockState(pos).canBeReplaced() && !chain.contains(pos)) places.add(pos);
    }

    // Whether a pylon of this type just placed at pos joins the tree
    public boolean accepts(BlockPos pos, PylonType type)
    {
        if (!acceptsPlaced(pos)) return false;
        return chain.isEmpty() ? type == PylonType.SHAPE : type != PylonType.SHAPE;
    }

    // The same places as ghostPositions, but checked with the pylon already standing there
    private boolean acceptsPlaced(BlockPos pos)
    {
        if (level == null || element == null || chain.contains(pos)) return false;
        if (chain.isEmpty()) return pos.distManhattan(worldPosition) == 1 && pos.getY() == worldPosition.getY();
        if (availableLevels() <= 0) return false;
        BlockPos tip = chain.getLast();
        BlockPos before = chain.size() >= 2 ? chain.get(chain.size() - 2) : worldPosition;
        return pos.distManhattan(tip) == 1 && pos.getY() == tip.getY() && !pos.equals(before);
    }

    // ---- joining a pylon ----

    // A pylon placed in one of the ghost places: the Shape Pylon takes the placer's first known shape; a modifier
    // pylon keeps the last roll (blessing), shifts the category boosts, and rolls
    public void attach(PylonBlockEntity pylon, @Nullable ServerPlayer placer)
    {
        if (!(level instanceof ServerLevel server)) return;
        PylonType type = pylon.type();
        if (chain.isEmpty()) {
            pylon.link(worldPosition, null, ModifierTier.NORMAL, false, defaultShape(placer));
            chain.add(pylon.getBlockPos());
            server.playSound(null, pylon.getBlockPos(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);
            sync();
            return;
        }
        // Placing on after a modifier pylon means its roll was kept
        if (chain.size() >= 2) blessed += PylonConfig.KEEP_BLESSING;
        boosts.replaceAll((category, boost) -> Math.max(0, boost - PylonConfig.BOOST_DECAY));
        if (type.category() != null) boosts.merge(type.category(), PylonConfig.CATEGORY_BOOST, Float::sum);

        int index = chain.size();
        Roll roll = roll(index, placer);
        pylon.link(worldPosition, roll.modifier(), roll.tier(), roll.spellBreak(), null);
        chain.add(pylon.getBlockPos());
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

    // A weighted random pick for the pylon at this place in the chain
    private Roll roll(int index, @Nullable ServerPlayer placer)
    {
        List<Identifier> before = modifiersBefore(index);
        var random = level.getRandom();
        boolean afterBreak = index >= 2 && level.getBlockEntity(chain.get(index - 1)) instanceof PylonBlockEntity last && last.isSpellBreak();
        if (before.size() >= PylonConfig.BREAK_MIN_MODIFIERS && !afterBreak && random.nextFloat() < PylonConfig.BREAK_CHANCE) {
            return new Roll(null, ModifierTier.NORMAL, true);
        }
        ModifierContext context = new ModifierContext(element, shape() == null ? SpellRegistries.SELF.getId() : shape(), before);
        List<Identifier> skip = excluded.getOrDefault(index, List.of());
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
            float weight = modifier.weight() * (1 + boosts.getOrDefault(modifier.category(), 0F)) * tierFactor;
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

    // The modifiers in the line before this place (ignoring spell breaks' power: preconditions look at the whole line)
    private List<Identifier> modifiersBefore(int index)
    {
        List<Identifier> modifiers = new ArrayList<>();
        for (int i = 1; i < index && i < chain.size(); i++) {
            if (level.getBlockEntity(chain.get(i)) instanceof PylonBlockEntity pylon && pylon.modifier() != null) modifiers.add(pylon.modifier());
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

    // The last pylon broken: a reroll (its modifier won't come up there again; corruption). Any other: the line is cut
    // there, and the pylons after it fall dark. The Shape Pylon broken: the whole line is cut.
    void pylonRemoved(PylonBlockEntity pylon)
    {
        if (breaking) return;
        int index = chain.indexOf(pylon.getBlockPos());
        if (index < 0) return;
        if (index >= 1) {
            corruption += PylonConfig.RESET_CORRUPTION;
            if (pylon.tier() == ModifierTier.BLESSED) blessed = Math.max(0, blessed - PylonConfig.BLESSED_LOSS);
            if (pylon.tier() == ModifierTier.CORRUPTED) corruption += PylonConfig.CORRUPTED_GAIN;
            if (pylon.modifier() != null) excluded.computeIfAbsent(index, i -> new ArrayList<>()).add(pylon.modifier());
        }
        // Everything after it is cut off
        for (int i = chain.size() - 1; i > index; i--) {
            if (level.getBlockEntity(chain.get(i)) instanceof PylonBlockEntity after) after.unlink();
            excluded.remove(i);
            chain.remove(i);
        }
        chain.remove(index);
        if (index == 0) excluded.clear();
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

    // The spell as it stands: its element and shape, then each modifier along the line until a spell break without
    // redstone power. Null without an element or shape. pylonsOut gets how many pylons the spell uses.
    public @Nullable Spell compile(int[] pylonsOut)
    {
        Identifier shape = shape();
        if (element == null || shape == null || level == null) return null;
        List<Identifier> modifiers = new ArrayList<>();
        int pylons = 2;
        for (int i = 1; i < chain.size(); i++) {
            if (!(level.getBlockEntity(chain.get(i)) instanceof PylonBlockEntity pylon)) break;
            if (pylon.isSpellBreak() && !level.hasNeighborSignal(pylon.getBlockPos())) break;
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

    private static final Codec<Map<Integer, List<Identifier>>> EXCLUDED_CODEC =
            Codec.unboundedMap(Codec.STRING.xmap(Integer::parseInt, String::valueOf), Identifier.CODEC.listOf());
    private static final Codec<Map<ModifierCategory, Float>> BOOSTS_CODEC = Codec.unboundedMap(ModifierCategory.CODEC, Codec.FLOAT);

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (element != null) output.store("element", DustElements.CODEC, element);
        output.store("tree", UUIDUtil.CODEC, tree);
        output.store("chain", BlockPos.CODEC.listOf(), List.copyOf(chain));
        if (!excluded.isEmpty()) output.store("excluded", EXCLUDED_CODEC, excluded);
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
        excluded.clear();
        input.read("excluded", EXCLUDED_CODEC).ifPresent(map -> map.forEach((index, ids) -> excluded.put(index, new ArrayList<>(ids))));
        blessed = input.getIntOr("blessed", 0);
        corruption = input.getIntOr("corruption", 0);
        boosts.clear();
        input.read("boosts", BOOSTS_CODEC).ifPresent(boosts::putAll);
        experience = input.getIntOr("experience", 0);
        memory = input.read("memory", PylonMemory.CODEC).orElse(null);
    }
}
