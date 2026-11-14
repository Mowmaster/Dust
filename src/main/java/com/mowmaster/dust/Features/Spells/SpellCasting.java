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

    // True if it was cast
    public static boolean cast(ServerPlayer player, Spell spell)
    {
        SpellShape shape = spell.shapeType();
        if (shape == null) {
            player.sendOverlayMessage(Component.translatable("dust.spell.unknown_shape", spell.shape().toString()));
            return false;
        }
        if (com.mowmaster.dust.Features.Spells.mobeffects.SpellMobEffects.isSilenced(player)) {
            player.sendOverlayMessage(Component.translatable("dust.spell.silenced"));
            return false;
        }
        boolean free = player.hasInfiniteMaterials();

        // What it costs this player
        Map<DustElement, Integer> cost = new LinkedHashMap<>();
        spell.dustCost().forEach((element, amount) -> cost.put(element, PlayerMagicHelper.dustCost(player, element, amount)));
        if (!free) {
            // Short of an element: with Transmutation, its parent elements make up the difference at double the amount
            // (fire + water for air); otherwise it can't be cast
            boolean transmute = spell.modifiers().contains(SpellRegistries.TRANSMUTATION.getId());
            Map<DustElement, Integer> pay = new LinkedHashMap<>();
            for (Map.Entry<DustElement, Integer> need : cost.entrySet()) {
                int have = PlayerMagicHelper.dust(player, need.getKey()) - pay.getOrDefault(need.getKey(), 0);
                int shortBy = need.getValue() - Math.max(0, have);
                pay.merge(need.getKey(), Math.min(need.getValue(), Math.max(0, have)), Integer::sum);
                if (shortBy <= 0) continue;
                if (transmute && !need.getKey().parts().isEmpty()) {
                    for (DustElement parent : need.getKey().parts()) pay.merge(parent, shortBy * 2, Integer::sum);
                } else {
                    player.sendOverlayMessage(Component.translatable("dust.spell.not_enough_dust", need.getKey().name(), need.getValue()));
                    return false;
                }
            }
            for (Map.Entry<DustElement, Integer> part : pay.entrySet()) {
                if (PlayerMagicHelper.dust(player, part.getKey()) < part.getValue()) {
                    player.sendOverlayMessage(Component.translatable("dust.spell.not_enough_dust", part.getKey().name(), part.getValue()));
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
        boolean delivered = deliver(cast, shape);
        if (!delivered) {
            player.sendOverlayMessage(Component.translatable("dust.spell.no_target"));
            return false;
        }

        if (!free) {
            int total = 0;
            for (Map.Entry<DustElement, Integer> need : cost.entrySet()) {
                PlayerMagicHelper.removeDust(player, need.getKey(), need.getValue(), false);
                total += need.getValue();
            }
            // Mana Reduction: percent off (int division rounds the saving down)
            int mana = PlayerMagicHelper.manaForDust(total);
            PlayerMagicHelper.spendMana(player, Math.max(0, mana + mana * stats.manaPercent() / 100));
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
        if (shapeDelivers) delivered = shape.cast(cast);
        return delivered;
    }
}
