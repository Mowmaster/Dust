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
// corruption, and its deck (what pylons draw from) as a grid of its cards; its pool (what the deck is refilled from) only
// while the reader holds the Akashic. Read on the
// client from the synced block entity (SpellPylonBlockEntity.read). Looking at a Spell Pylon is enough to see it: no
// reading helmet needed (lookingAtSpellPylon, added to ReadingSight in Dust's constructor).
public final class SpellPylonReading
{
    private SpellPylonReading() {}

    // The ReadingSight condition: the player's crosshair is on a modifier pylon or a Blessed / Corrupted Pylon within
    // reach (the Spell Pylon has its own HUD: Client/SpellPylonHud) (pick: what they're looking
    // at, false: not fluids). Checked on both sides.
    public static boolean lookingAtSpellPylon(Player player)
    {
        HitResult hit = player.pick(player.blockInteractionRange(), 1.0F, false);
        return hit instanceof BlockHitResult block && hit.getType() == HitResult.Type.BLOCK
                && (player.level().getBlockState(block.getBlockPos()).getBlock() instanceof PylonBlock
                    || player.level().getBlockState(block.getBlockPos()).getBlock() instanceof CounterPylonBlock);
    }

    // A Research Notes / Shared Research Guide in either hand: its HUD (ResearchBookHud) is showing, so the pylon's
    // paper isn't (they'd overlap)
    public static boolean holdsResearchBook(Player player)
    {
        return player.getMainHandItem().getItem() instanceof com.mowmaster.dust.Features.Research.ResearchBookItem
                || player.getOffhandItem().getItem() instanceof com.mowmaster.dust.Features.Research.ResearchBookItem;
    }

    // reader: who's looking (the pool only shows while they hold the Akashic)
    public static List<Component> lines(SpellPylonBlockEntity pylon, SpellDeck deck, @Nullable Player reader)
    {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable("block.dust.spell_pylon").withStyle(ChatFormatting.BOLD));
        DustElement element = pylon.element();
        if (element == null) {
            lines.add(Component.translatable("dust.pylon.no_element"));
            return lines;
        }
        lines.add(Component.translatable("hud.dust.pylon.spell", SpellNames.spellForClient(element, pylon.fusion()), pylon.level()));
        lines.add(Component.translatable("hud.dust.pylon.counters", deck.blessed(), SpellDeck.BLESS_AT, deck.corruption(), SpellDeck.CORRUPT_AT));
        if (!deck.started()) {
            lines.add(Component.empty());
            lines.add(Component.translatable("hud.dust.pylon.not_started").withStyle(ChatFormatting.ITALIC));
            return lines;
        }

        // The deck is drawn as cards (deckPages); the special cards, which have none, are named under it
        MutableComponent specials = Component.empty();
        Map<Identifier, Integer> counted = new LinkedHashMap<>();
        for (Identifier card : deck.cards()) counted.merge(card, 1, Integer::sum);
        for (Map.Entry<Identifier, Integer> entry : counted.entrySet()) {
            if (!SpellDeck.isSpecial(entry.getKey())) continue;
            if (!specials.getSiblings().isEmpty()) specials.append(", ");
            MutableComponent name = cardName(entry.getKey(), pylon);
            if (entry.getValue() > 1) name.append(" x" + entry.getValue());
            specials.append(name);
        }
        lines.add(Component.empty());
        lines.add(Component.translatable("hud.dust.pylon.deck", deck.cards().size()).withStyle(ChatFormatting.UNDERLINE));
        if (!specials.getSiblings().isEmpty()) lines.add(Component.translatable("hud.dust.pylon.deck_specials", specials));

        // The pool only while the reader holds the Akashic: each modifier as its card (an inline picture), a knockout
        // modifier with its uses left; used-up ones left out
        if (reader != null && com.mowmaster.dust.Features.Spells.grimoire.AkashicItem.holds(reader)) {
            MutableComponent pool = Component.empty();
            int shown = 0;
            for (Map.Entry<Identifier, Integer> entry : deck.pool().entrySet()) {
                if (entry.getValue() == 0) continue;
                if (shown++ > 0) pool.append(" ");
                pool.append(cardPicture(entry.getKey()));
                if (entry.getValue() > 0) pool.append(String.valueOf(entry.getValue()));
            }
            lines.add(Component.empty());
            lines.add(Component.translatable("hud.dust.pylon.pool_akashic").withStyle(ChatFormatting.UNDERLINE));
            lines.add(pool);
        }
        return lines;
    }

    // The deck as cards for the reading HUD's slot grid: one slot per different card (a modifier's card item), with
    // how many of it there are. null before the deck has started.
    public static com.mowmaster.mowlib.paging.@Nullable PagedContents deckPages(SpellDeck deck)
    {
        if (!deck.started()) return null;
        Map<Identifier, Integer> counted = new LinkedHashMap<>();
        for (Identifier card : deck.cards()) {
            if (!SpellDeck.isSpecial(card)) counted.merge(card, 1, Integer::sum);
        }
        List<net.neoforged.neoforge.transfer.resource.ResourceStack<net.neoforged.neoforge.transfer.item.ItemResource>> stacks = new ArrayList<>();
        for (Map.Entry<Identifier, Integer> entry : counted.entrySet()) {
            net.minecraft.world.item.ItemStack card = com.mowmaster.dust.DustRegistries.DustItemRegistry.cardOf(entry.getKey());
            if (!card.isEmpty()) stacks.add(new net.neoforged.neoforge.transfer.resource.ResourceStack<>(net.neoforged.neoforge.transfer.item.ItemResource.of(card), entry.getValue()));
        }
        if (stacks.isEmpty()) return null;
        return new com.mowmaster.mowlib.paging.PagedContents(stacks.size(), 0, stacks);
    }

    // A modifier's card as a picture in a line of text: an "object" component showing its card texture from the items
    // atlas (textures/item/cards/card_<id>.png), the height of a letter
    private static Component cardPicture(Identifier modifier)
    {
        return Component.object(new net.minecraft.network.chat.contents.objects.AtlasSprite(net.minecraft.data.AtlasIds.ITEMS,
                Identifier.fromNamespaceAndPath(modifier.getNamespace(), "item/cards/card_" + modifier.getPath())));
    }

    // A card's name: the special cards by their own names, a modifier by its name for this spell (SpellNames), blessed
    // ones in purple and corrupted ones in dark red
    private static MutableComponent cardName(Identifier card, SpellPylonBlockEntity pylon)
    {
        if (card.equals(SpellDeck.SPELL_BREAK)) return Component.translatable("hud.dust.pylon.card.spell_break").withStyle(ChatFormatting.DARK_RED);
        if (card.equals(SpellDeck.BLESSING)) return Component.translatable("hud.dust.pylon.card.blessing").withStyle(ChatFormatting.DARK_PURPLE);
        if (card.equals(SpellDeck.NULL_CORRUPTION)) return Component.translatable("hud.dust.pylon.card.null_corruption").withStyle(ChatFormatting.DARK_RED);
        MutableComponent name = SpellNames.modifier(card, pylon.element(), pylon.shape(), pylon.fusion()).copy();
        SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(card);
        if (modifier != null && modifier.tier() == ModifierTier.BLESSED) name.withStyle(ChatFormatting.DARK_PURPLE);
        if (modifier != null && modifier.tier() == ModifierTier.CORRUPTED) name.withStyle(ChatFormatting.DARK_RED);
        return name;
    }
}
