package com.mowmaster.dust.Features.Spells.pylons;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.ModifierInfo;
import com.mowmaster.dust.Features.Spells.SpellNames;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// A modifier pylon on MowLib's reading HUD, while looked at: the modifier it holds (by its name for the spell's element),
// what one level of it adds, how many levels it gives (and nodes filled), and what that comes to in all (ModifierInfo).
// Or what else it is: a spell break, a null corruption, the Shape Pylon's shape, nothing drawn, or not in a spell.
public final class PylonReading
{
    private PylonReading() {}

    public static List<Component> lines(PylonBlockEntity pylon)
    {
        List<Component> lines = new ArrayList<>();
        lines.add(pylon.getBlockState().getBlock().getName().copy().withStyle(ChatFormatting.BOLD));
        if (pylon.main() == null || pylon.getLevel() == null) {
            lines.add(Component.translatable("hud.dust.pylon.unlinked").withStyle(ChatFormatting.ITALIC));
            return lines;
        }
        SpellPylonBlockEntity tree = pylon.getLevel().getBlockEntity(pylon.main()) instanceof SpellPylonBlockEntity found ? found : null;
        DustElement element = tree == null ? null : tree.element();
        Identifier shape = tree == null ? null : tree.shape();
        if (pylon.shape() != null) {
            lines.add(Component.translatable("hud.dust.pylon.shape", Component.translatable("spell_shape." + pylon.shape().getNamespace() + "." + pylon.shape().getPath())));
        }
        if (pylon.isSpellBreak()) {
            lines.add(Component.translatable("hud.dust.pylon.card.spell_break").withStyle(ChatFormatting.DARK_RED));
            lines.add(Component.translatable(pylon.powered() ? "hud.dust.pylon.spell_break_powered" : "hud.dust.pylon.spell_break_unpowered"));
            return lines;
        }
        if (pylon.stop()) {
            lines.add(Component.translatable("hud.dust.pylon.card.null_corruption").withStyle(ChatFormatting.DARK_RED));
            lines.add(Component.translatable("hud.dust.pylon.null_corruption"));
            return lines;
        }
        Identifier modifier = pylon.modifier();
        if (modifier == null) {
            if (pylon.shape() == null) lines.add(Component.translatable("hud.dust.pylon.empty").withStyle(ChatFormatting.ITALIC));
            return lines;
        }
        MutableComponent name = SpellNames.modifier(modifier, element, shape, tree == null ? null : tree.fusion()).copy();
        if (pylon.tier() == ModifierTier.BLESSED) name.withStyle(ChatFormatting.DARK_PURPLE);
        else if (pylon.tier() == ModifierTier.CORRUPTED) name.withStyle(ChatFormatting.DARK_RED);
        lines.add(name.withStyle(ChatFormatting.UNDERLINE));
        if (pylon.tier() != ModifierTier.NORMAL) lines.add(Component.translatable("hud.dust.pylon.tier." + pylon.tier().getSerializedName()).withStyle(
                pylon.tier() == ModifierTier.BLESSED ? ChatFormatting.DARK_PURPLE : ChatFormatting.DARK_RED));
        if (pylon.legendary()) lines.add(Component.translatable("hud.dust.pylon.legendary").withStyle(ChatFormatting.GOLD));
        lines.add(ModifierInfo.perLevel(modifier) == null ? ModifierInfo.description(modifier)
                : Component.translatable("hud.dust.pylon.per_level", ModifierInfo.perLevelLine(modifier)));
        lines.add(Component.translatable("hud.dust.pylon.levels", pylon.levels(), pylon.nodes(), pylon.maxNodes()));
        // A Legendary pylon's levels count double
        Component total = ModifierInfo.total(modifier, pylon.levels() * (pylon.legendary() ? 2 : 1));
        if (total != null) lines.add(Component.translatable("hud.dust.pylon.total", total).withStyle(ChatFormatting.BOLD));
        return lines;
    }

    // The texture of a modifier's card (textures/item/cards/card_<id>.png), for drawing it as a quad
    public static @Nullable Identifier cardTexture(@Nullable Identifier modifier)
    {
        if (modifier == null) return null;
        return com.mowmaster.dust.Features.Spells.ModifierCardItem.texture(modifier);
    }
}
