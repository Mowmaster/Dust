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
// Creative players cast for free. Casting by blocks (dust and energy crystals, no mana) comes later.
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
            for (Map.Entry<DustElement, Integer> need : cost.entrySet()) {
                if (PlayerMagicHelper.dust(player, need.getKey()) < need.getValue()) {
                    player.sendOverlayMessage(Component.translatable("dust.spell.not_enough_dust", need.getKey().name(), need.getValue()));
                    return false;
                }
            }
        }

        SpellStats stats = spell.stats(PlayerMagicHelper.spellPower(player, spell.element()));
        SpellCast cast = SpellCast.by(player, spell, stats);
        boolean delivered = false;
        boolean shapeDelivers = true;
        for (SpellModifier modifier : spell.modifierList()) {
            if (!modifier.beforeDelivery(cast)) {
                shapeDelivers = false;
                delivered = true;
            }
        }
        if (shapeDelivers) delivered = shape.cast(cast);
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
            PlayerMagicHelper.spendMana(player, PlayerMagicHelper.manaForDust(total));
        }
        return true;
    }
}
