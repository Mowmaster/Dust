package com.mowmaster.dust.Features.AncientLanguage;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignApplicator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
//? if >=26.3
import net.minecraft.world.level.block.entity.SignTextSlot;

// Writes things in the ancient script.
// - Right-click a sign: the side you're facing is rewritten in glyphs (like glow ink). Vanilla's SignBlock handles
//   the click for any item that implements SignApplicator, and uses up one ink when this returns true.
//   Editing the sign's text again afterwards writes plain text, so it needs inking again.
// - Crafting: ink + a written book, or ink + an item with a custom name / lore (AncientInscribeRecipe).
public class AncientInkItem extends Item implements SignApplicator
{
    public AncientInkItem(Properties properties)
    {
        super(properties);
    }

    // Stonecutter: 26.3 rewrote signs (SignTextSlot for the side, a mutable copy to edit lines). 26.1.2's version
    // of both methods is in the else part below.
    //? if >=26.3 {
    @Override
    public boolean canApplyToSign(SignText text, ItemStack item, Player player)
    {
        // Only if there's text, and at least one line isn't already ancient
        boolean filtered = player.isTextFilteringEnabled();
        return text.hasMessage(filtered) && text.getMessages(filtered).stream()
                .anyMatch(line -> !line.getString().isEmpty() && !AncientLanguage.isAncient(line));
    }

    @Override
    public boolean tryApplyToSign(Level level, SignBlockEntity sign, SignTextSlot slot, ItemStack item, Player player)
    {
        // modifyLines changes the raw and the filtered copy of every line
        boolean changed = sign.updateText(text -> text.asMutable().modifyLines(AncientInkItem::inscribe).asImmutable(), slot);
        if (changed) {
            level.playSound(null, sign.getBlockPos(), SoundEvents.INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return changed;
    }
    //?} else {
    /*@Override
    public boolean canApplyToSign(SignText text, ItemStack item, Player player)
    {
        // Only if there's text, and at least one line isn't already ancient. getMessages gives an array here, so
        // Arrays.stream turns it into a Stream.
        boolean filtered = player.isTextFilteringEnabled();
        return text.hasMessage(player) && java.util.Arrays.stream(text.getMessages(filtered))
                .anyMatch(line -> !line.getString().isEmpty() && !AncientLanguage.isAncient(line));
    }

    @Override
    public boolean tryApplyToSign(Level level, SignBlockEntity sign, boolean isFrontText, ItemStack item, Player player)
    {
        boolean changed = sign.updateText(AncientInkItem::inscribeAll, isFrontText);
        if (changed) {
            level.playSound(null, sign.getBlockPos(), SoundEvents.INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return changed;
    }

    // SignText can't be changed in place here: setMessage returns a new copy with one line (raw and filtered) replaced
    private static SignText inscribeAll(SignText text)
    {
        for (int i = 0; i < SignText.LINES; i++) {
            text = text.setMessage(i, inscribe(text.getMessage(i, false)), inscribe(text.getMessage(i, true)));
        }
        return text;
    }
    *///?}

    private static Component inscribe(Component line)
    {
        return line.getString().isEmpty() ? line : AncientLanguage.inscribe(line);
    }
}
