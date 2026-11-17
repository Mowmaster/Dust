package com.mowmaster.dust.Features.Spells;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Magic.PlayerMagicHelper;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import com.mowmaster.dust.Features.Spells.api.SpellShape;
import com.mowmaster.dust.Features.Spells.api.SpellStats;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import java.util.function.Predicate;
import java.util.LinkedHashMap;
import java.util.Map;

// A player casting a spell by hand (from a Spell Scroll now, a grimoire later):
//   1. The dust it costs (Spell.dustCost, less for elements they're attuned to) must all be in their dust store.
//   2. The shape delivers it (a modifier can deliver it instead). Nothing to cast at (Touch at thin air): nothing spent.
//   3. The dust is used, and mana: 1 per 10 dust, rounded up. Mana they don't have costs half a heart a point.
// Creative players cast for free. Blocks cast too, on dust and energy crystals with no mana (caster/SpellCasterBlockEntity).
public final class SpellCasting
{
    private SpellCasting() {}

    // ---- the Akashic (grimoire/AkashicItem): a cast from it costs no dust or mana, and its spell's tree gets ten times
    // the experience (SpellScrollItem.giveExperience); switched on just around that cast ----
    public static final int AKASHIC_XP = 10;
    // ThreadLocal: a value of its own for each thread (the server's main thread is the one casting)
    private static final ThreadLocal<Boolean> AKASHIC = ThreadLocal.withInitial(() -> false);

    // Whether the cast going on now is from the Akashic
    public static boolean isAkashic()
    {
        return AKASHIC.get();
    }

    // Runs a cast as the Akashic's
    public static boolean akashic(java.util.function.BooleanSupplier cast)
    {
        boolean before = AKASHIC.get();
        AKASHIC.set(true);
        try {
            return cast.getAsBoolean();
        } finally {
            AKASHIC.set(before);
        }
    }

    // How long after a spell each spell stacked on it is cast (ticks)
    public static final int STACK_DELAY = 5;

    // A stacked spell's own turn (DelayedCasts): cast as the player's, already paid for. True if it landed.
    public static boolean castStacked(ServerPlayer player, Spell spell)
    {
        SpellShape shape = spell.shapeType();
        if (shape == null) return false;
        SpellStats stats = spell.stats(PlayerMagicHelper.spellPower(player, spell.element()));
        return deliver(SpellCast.by(player, spell, stats), shape);
    }

    // True if it was cast
    public static boolean cast(ServerPlayer player, Spell spell)
    {
        return cast(player, spell, 0);
    }

    // costReduction: percent off the dust and mana (a channelled beam's Efficiency)
    public static boolean cast(ServerPlayer player, Spell spell, int costReduction)
    {
        return perform(player, spell, costReduction, null, null, null);
    }

    // A spell tool's cast (SpellTools): it lands where the tool struck (landing: on the creature or block hit; true if it
    // did), and the tool pays the mana in durability (one point per mana, at least 1). The dust is paid as usual.
    public static boolean castWithTool(ServerPlayer player, Spell spell, ItemStack tool, EquipmentSlot slot, Predicate<SpellCast> landing)
    {
        return perform(player, spell, 0, tool, slot, landing);
    }

    private static boolean perform(ServerPlayer player, Spell spell, int costReduction, @Nullable ItemStack tool, @Nullable EquipmentSlot slot,
                                   @Nullable Predicate<SpellCast> landing)
    {
        SpellShape shape = spell.shapeType();
        if (shape == null && landing == null) {
            player.sendOverlayMessage(Component.translatable("dust.spell.unknown_shape", spell.shape().toString()));
            return false;
        }
        if (com.mowmaster.dust.Features.Spells.mobeffects.SpellMobEffects.isSilenced(player)) {
            player.sendOverlayMessage(Component.translatable("dust.spell.silenced"));
            return false;
        }
        // Creative players, and casts from the Akashic, pay nothing
        boolean free = player.hasInfiniteMaterials() || AKASHIC.get();

        // What it costs this player
        Map<DustElement, Integer> cost = new LinkedHashMap<>();
        // A stacked spell's dust is paid with the first (totalDustCost: the whole stack's)
        spell.totalDustCost().forEach((element, amount) -> cost.put(element, Math.max(1, PlayerMagicHelper.dustCost(player, element, amount) * (100 - costReduction) / 100)));
        if (!free) {
            // Short of an element: with Transmutation, its parent elements make up the difference, one of each per
            // missing dust (fire + water for air); otherwise it can't be cast
            boolean transmute = spell.modifiers().contains(SpellRegistries.TRANSMUTATION.getId());
            Map<DustElement, Integer> pay = new LinkedHashMap<>();
            for (Map.Entry<DustElement, Integer> need : cost.entrySet()) {
                int have = PlayerMagicHelper.dust(player, need.getKey()) - pay.getOrDefault(need.getKey(), 0);
                int shortBy = need.getValue() - Math.max(0, have);
                pay.merge(need.getKey(), Math.min(need.getValue(), Math.max(0, have)), Integer::sum);
                if (shortBy <= 0) continue;
                if (transmute && !need.getKey().parts().isEmpty()) {
                    for (DustElement parent : need.getKey().parts()) pay.merge(parent, shortBy, Integer::sum);
                } else {
                    player.sendOverlayMessage(Component.translatable("dust.spell.not_enough_dust", com.mowmaster.dust.Features.DustComposition.ElementNames.forViewer(need.getKey(), player), need.getValue()));
                    return false;
                }
            }
            for (Map.Entry<DustElement, Integer> part : pay.entrySet()) {
                if (PlayerMagicHelper.dust(player, part.getKey()) < part.getValue()) {
                    player.sendOverlayMessage(Component.translatable("dust.spell.not_enough_dust", com.mowmaster.dust.Features.DustComposition.ElementNames.forViewer(part.getKey(), player), part.getValue()));
                    return false;
                }
            }
            cost.clear();
            cost.putAll(pay);
        }

        SpellStats stats = spell.stats(PlayerMagicHelper.spellPower(player, spell.element()));
        // A critical hit (Crit Chance): double damage this cast
        if (stats.critChance() > 0 && player.getRandom().nextFloat() < stats.critChance()) {
            stats = stats.withDamageMultiplier(stats.damageMultiplier() * 2);
            player.level().playSound(null, player.blockPosition(), net.minecraft.sounds.SoundEvents.PLAYER_ATTACK_CRIT, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.2F);
        }
        SpellCast cast = SpellCast.by(player, spell, stats);
        boolean delivered;
        if (landing != null) {
            // The modifiers' cast-time parts (Overshield...), then where the tool struck
            for (SpellModifier modifier : spell.modifierList()) modifier.beforeDelivery(cast);
            delivered = landing.test(cast);
        } else {
            delivered = deliver(cast, shape);
        }
        if (!delivered) {
            player.sendOverlayMessage(Component.translatable("dust.spell.no_target"));
            return false;
        }
        // A Spell Stack: the spells stacked after it follow, STACK_DELAY ticks apart (already paid for)
        for (int i = 0; i < spell.stack().size(); i++) DelayedCasts.add(player, spell.stack().get(i), STACK_DELAY * (i + 1));
        // Every modifier in a spell the player casts counts as had in a spell (their Spell Cards entries)
        for (net.minecraft.resources.Identifier modifier : spell.modifiers()) com.mowmaster.dust.Features.Research.Tabs.SpellCardResearchTab.unlock(player, modifier);

        if (!free) {
            int total = 0;
            for (Map.Entry<DustElement, Integer> need : cost.entrySet()) {
                PlayerMagicHelper.removeDust(player, need.getKey(), need.getValue(), false);
                total += need.getValue();
            }
            // Mana Reduction: percent off (int division rounds the saving down)
            int mana = PlayerMagicHelper.manaForDust(total);
            // Permanent (a rune that pays its own way after): 4x the mana to cast
            if (spell.modifiers().contains(SpellRegistries.PERMANENT.getId())) mana *= com.mowmaster.dust.Features.Spells.api.SpellConfig.PERMANENT_MANA;
            mana = mana * (100 - costReduction) / 100;
            mana = Math.max(0, mana + mana * stats.manaPercent() / 100);
            // A spell tool pays in durability instead
            if (tool != null && slot != null) tool.hurtAndBreak(Math.max(1, mana), player, slot);
            else PlayerMagicHelper.spendMana(player, mana);
        }
        return true;
    }

    // Delivers a cast: each modifier first (one may deliver it itself, e.g. Multishot's spread), then the shape unless a
    // modifier already did. True if it landed on something. Shared by players and Spell Casters (blocks).
    public static boolean deliver(SpellCast cast, SpellShape shape)
    {
        boolean delivered = false;
        boolean shapeDelivers = true;
        for (SpellModifier modifier : cast.spell().modifierList()) {
            if (!modifier.beforeDelivery(cast)) {
                shapeDelivers = false;
                delivered = true;
            }
        }
        // Beam: each pulse is a ray, whatever the shape
        if (shapeDelivers) delivered = Channeling.channels(cast.spell()) ? com.mowmaster.dust.Features.Spells.shapes.BeamRay.fire(cast) : shape.cast(cast);
        return delivered;
    }
}
