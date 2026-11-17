package com.mowmaster.dust.Features.Spells.pylons;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import com.mowmaster.dust.Features.Spells.modifiers.FusionModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// Linking two spells: a Fusion or Spell Stack card (drawn from LINK_LEVEL, on a Neutral pylon) waits on its pylon (the
// pylon_fusionstack model, diamond blue) for another spell. The next pylon beside it must be
//   a pylon of another spell (placed there, joining that spell), or
//   a new Spell Pylon placed there and given its element crystal
// and the two spells are linked. The tree with the Fusion / Stack pylon owns the link; the other is linked in (at its
// pylon or Spell Pylon that touches it, "via"). What the link becomes:
//   a Fusion card, and the two elements have a fusion (FusionModifier.joins): both spells become that fusion
//   otherwise (or a Spell Stack card): a Spell Stack, both spells cast one after the other, the owner's first
// Until it's linked nothing can follow the waiting pylon; after, pylons placed after it count for both spells
// (SpellPylonBlockEntity.compile). Each spell only gets the link while its own way to it is on (no unpowered spell
// break before it). Once linked, breaking any part of either spell breaks both (breakBoth); a fusion leaves a Fused
// Crystal of it.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class SpellLinks
{
    public static final int LINK_LEVEL = 50;

    private SpellLinks() {}

    // A link card as drawn (waiting on its pylon, or a Spell Stack): Fusion or Spell Stack
    public static boolean isDrawnLinkCard(@Nullable Identifier modifier)
    {
        return SpellRegistries.FUSION.getId().equals(modifier) || SpellRegistries.SPELL_STACK.getId().equals(modifier);
    }

    // A Fusion / Spell Stack pylon's modifier, waiting or linked (a fusion it became counts too)
    public static boolean isLinkCard(@Nullable Identifier modifier)
    {
        return isDrawnLinkCard(modifier) || (modifier != null && SpellRegistries.MODIFIER_REGISTRY.getValue(modifier) instanceof FusionModifier);
    }

    // The fusion of these two elements, if there is one
    public static @Nullable FusionModifier fusionOf(@Nullable DustElement a, @Nullable DustElement b)
    {
        if (a == null || b == null) return null;
        for (SpellModifier modifier : SpellRegistries.MODIFIER_REGISTRY) {
            if (modifier instanceof FusionModifier fusion && fusion.joins(a, b)) return fusion;
        }
        return null;
    }

    // A fusion's icon, drawn over its Spell Pylons and its link pylon in place of an element's (white: the colours are
    // in the picture): textures/gui/sprites/fusion/<id>.png
    public static Identifier fusionIcon(Identifier fusion)
    {
        return Identifier.fromNamespaceAndPath(fusion.getNamespace(), "textures/gui/sprites/fusion/" + fusion.getPath() + ".png");
    }

    public static @Nullable Identifier idOf(SpellModifier modifier)
    {
        return SpellRegistries.MODIFIER_REGISTRY.getKey(modifier);
    }

    // ---- making links ----

    // A pylon of tree just joined it at pos: if it's a waiting link pylon, a spell beside it is linked in; if it's beside
    // another tree's waiting link pylon, this tree is linked into that one
    public static void afterJoin(ServerLevel level, SpellPylonBlockEntity tree, PylonBlockEntity pylon, @Nullable ServerPlayer placer)
    {
        if (tree.isLinked()) return;
        BlockPos pos = pylon.getBlockPos();
        if (isDrawnLinkCard(pylon.modifier())) {
            for (Direction side : Direction.Plane.HORIZONTAL) {
                SpellPylonBlockEntity other = spellAt(level, pos.relative(side));
                if (other != null && other != tree && other.element() != null && !other.isLinked()) {
                    link(level, tree, pylon, other, pos.relative(side), placer);
                    return;
                }
            }
            return;
        }
        tryJoinWaiting(level, tree, pos, placer);
    }

    // A Spell Pylon just given its element: a waiting link pylon beside it links it in
    public static void afterElement(ServerLevel level, SpellPylonBlockEntity tree, @Nullable ServerPlayer placer)
    {
        if (!tree.isLinked()) tryJoinWaiting(level, tree, tree.getBlockPos(), placer);
    }

    // Another tree's waiting link pylon beside `via` (a block of `tree`): link tree into it
    private static void tryJoinWaiting(ServerLevel level, SpellPylonBlockEntity tree, BlockPos via, @Nullable ServerPlayer placer)
    {
        for (Direction side : Direction.Plane.HORIZONTAL) {
            if (!(level.getBlockEntity(via.relative(side)) instanceof PylonBlockEntity waiting) || !isDrawnLinkCard(waiting.modifier())
                    || waiting.main() == null || waiting.main().equals(tree.getBlockPos())) continue;
            if (!(level.getBlockEntity(waiting.main()) instanceof SpellPylonBlockEntity owner) || owner.isLinked()) continue;
            link(level, owner, waiting, tree, via, placer);
            return;
        }
    }

    // The tree a block belongs to: a Spell Pylon, or a pylon of one
    private static @Nullable SpellPylonBlockEntity spellAt(Level level, BlockPos pos)
    {
        if (level.getBlockEntity(pos) instanceof SpellPylonBlockEntity spellPylon) return spellPylon;
        if (level.getBlockEntity(pos) instanceof PylonBlockEntity pylon && pylon.main() != null
                && level.getBlockEntity(pylon.main()) instanceof SpellPylonBlockEntity spellPylon) return spellPylon;
        return null;
    }

    private static void link(ServerLevel level, SpellPylonBlockEntity owner, PylonBlockEntity linkPylon, SpellPylonBlockEntity other, BlockPos via,
                             @Nullable ServerPlayer placer)
    {
        Identifier result = SpellRegistries.SPELL_STACK.getId();
        if (SpellRegistries.FUSION.getId().equals(linkPylon.modifier())) {
            FusionModifier fusion = fusionOf(owner.element(), other.element());
            Identifier id = fusion == null ? null : idOf(fusion);
            if (id != null) result = id;
        }
        linkPylon.resolveLink(result);
        owner.linkAsOwner(linkPylon.getBlockPos(), other.getBlockPos());
        other.linkInto(owner.getBlockPos(), via);
        boolean fused = !result.equals(SpellRegistries.SPELL_STACK.getId());
        BlockPos at = linkPylon.getBlockPos();
        level.sendParticles(fused ? ParticleTypes.END_ROD : ParticleTypes.ENCHANT, at.getX() + 0.5, at.getY() + 1.2, at.getZ() + 0.5, 30, 0.3, 0.4, 0.3, 0.05);
        level.playSound(null, at, fused ? SoundEvents.BEACON_ACTIVATE : SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, 1.2F);
        if (placer != null) {
            placer.sendOverlayMessage(fused ? Component.translatable("dust.pylon.fused", Component.translatable("spell_modifier." + result.getNamespace() + "." + result.getPath()))
                    : Component.translatable("dust.pylon.stacked"));
            com.mowmaster.dust.Features.Research.Tabs.SpellCardResearchTab.unlock(placer, result);
        }
    }

    // ---- breaking ----

    // Spell Pylons to break at the start of the next tick (not straight away: they're asked for while a block of theirs
    // is still being removed)
    private static final List<Pending> TO_BREAK = new ArrayList<>();

    private record Pending(ServerLevel level, BlockPos pos) {}

    // A part of a linked spell broke: both Spell Pylons break (with all their pylons), next tick
    public static void breakBoth(ServerLevel level, BlockPos first, @Nullable BlockPos second)
    {
        TO_BREAK.add(new Pending(level, first));
        if (second != null) TO_BREAK.add(new Pending(level, second));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event)
    {
        if (TO_BREAK.isEmpty()) return;
        List<Pending> due = List.copyOf(TO_BREAK);
        TO_BREAK.clear();
        for (Pending pending : due) {
            if (pending.level().getBlockEntity(pending.pos()) instanceof SpellPylonBlockEntity) pending.level().destroyBlock(pending.pos(), true);
        }
    }
}
