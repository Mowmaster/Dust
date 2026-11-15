package com.mowmaster.dust.Features.Spells.pylons;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.SpellNames;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// The Spell Pylon on MowLib's reading HUD (the paper on the left of the screen): the spell, its blessing and
// corruption, its deck (what pylons draw from) and, below, its pool (what the deck is refilled from). Read on the
// client from the synced block entity (SpellPylonBlockEntity.read). Looking at a Spell Pylon is enough to see it: no
// reading helmet needed (lookingAtSpellPylon, added to ReadingSight in Dust's constructor).
public final class SpellPylonReading
{
    private SpellPylonReading() {}

    // The ReadingSight condition: the player's crosshair is on a Spell Pylon within reach (pick: what they're looking
    // at, false: not fluids). Checked on both sides.
    public static boolean lookingAtSpellPylon(Player player)
    {
        HitResult hit = player.pick(player.blockInteractionRange(), 1.0F, false);
        return hit instanceof BlockHitResult block && hit.getType() == HitResult.Type.BLOCK
                && player.level().getBlockState(block.getBlockPos()).getBlock() instanceof SpellPylonBlock;
    }

    public static List<Component> lines(SpellPylonBlockEntity pylon, SpellDeck deck)
    {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable("block.dust.spell_pylon").withStyle(ChatFormatting.BOLD));
        DustElement element = pylon.element();
        if (element == null) {
            lines.add(Component.translatable("dust.pylon.no_element"));
            return lines;
        }
        lines.add(Component.translatable("hud.dust.pylon.spell", element.name(), pylon.level()));
        lines.add(Component.translatable("hud.dust.pylon.counters", deck.blessed(), SpellDeck.BLESS_AT, deck.corruption(), SpellDeck.CORRUPT_AT));
        if (!deck.started()) {
            lines.add(Component.empty());
            lines.add(Component.translatable("hud.dust.pylon.not_started").withStyle(ChatFormatting.ITALIC));
            return lines;
        }

        // The deck: each card once, with how many there are
        Map<Identifier, Integer> counted = new LinkedHashMap<>();
        for (Identifier card : deck.cards()) counted.merge(card, 1, Integer::sum);
        lines.add(Component.empty());
        lines.add(Component.translatable("hud.dust.pylon.deck", deck.cards().size()).withStyle(ChatFormatting.UNDERLINE));
        for (Map.Entry<Identifier, Integer> entry : counted.entrySet()) {
            MutableComponent name = cardName(entry.getKey(), pylon);
            if (entry.getValue() > 1) name.append(" x" + entry.getValue());
            lines.add(name);
        }

        // The pool: one list, wrapped by the HUD; a knockout modifier shows its uses left; used-up ones are left out
        MutableComponent pool = Component.empty();
        int shown = 0;
        for (Map.Entry<Identifier, Integer> entry : deck.pool().entrySet()) {
            if (entry.getValue() == 0) continue;
            if (shown++ > 0) pool.append(", ");
            MutableComponent name = cardName(entry.getKey(), pylon);
            if (entry.getValue() > 0) name.append(" (" + entry.getValue() + ")");
            pool.append(name);
        }
        lines.add(Component.empty());
        lines.add(Component.translatable("hud.dust.pylon.pool", shown).withStyle(ChatFormatting.UNDERLINE));
        lines.add(pool);
        return lines;
    }

    // A card's name: the special cards by their own names, a modifier by its name for this spell (SpellNames), blessed
    // ones in purple and corrupted ones in dark red
    private static MutableComponent cardName(Identifier card, SpellPylonBlockEntity pylon)
    {
        if (card.equals(SpellDeck.SPELL_BREAK)) return Component.translatable("hud.dust.pylon.card.spell_break").withStyle(ChatFormatting.DARK_RED);
        if (card.equals(SpellDeck.BLESSING)) return Component.translatable("hud.dust.pylon.card.blessing").withStyle(ChatFormatting.DARK_PURPLE);
        if (card.equals(SpellDeck.NULL_CORRUPTION)) return Component.translatable("hud.dust.pylon.card.null_corruption").withStyle(ChatFormatting.DARK_RED);
        MutableComponent name = SpellNames.modifier(card, pylon.element(), pylon.shape()).copy();
        SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(card);
        if (modifier != null && modifier.tier() == ModifierTier.BLESSED) name.withStyle(ChatFormatting.DARK_PURPLE);
        if (modifier != null && modifier.tier() == ModifierTier.CORRUPTED) name.withStyle(ChatFormatting.DARK_RED);
        return name;
    }
}
