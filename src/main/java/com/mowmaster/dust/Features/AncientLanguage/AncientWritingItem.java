package com.mowmaster.dust.Features.AncientLanguage;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

// Something with an ancient word written on it: the Ancient Tablet (found in chests and ruins) and the Transcription
// (rubbed off a glyph wall with Parchment). The word is the dust:ancient_word component.
// Right-click to decipher it (DecipherMenu, a game of hangman). Solving it teaches every glyph in the word; failing
// destroys it. If you already know every glyph, it just reads out.
public class AncientWritingItem extends Item
{
    public AncientWritingItem(Properties properties)
    {
        super(properties);
    }

    public static String getWord(ItemStack stack)
    {
        return stack.getOrDefault(DustComponentDataRegistry.ANCIENT_WORD.get(), "");
    }

    public static ItemStack withWord(ItemStack stack, String word)
    {
        stack.set(DustComponentDataRegistry.ANCIENT_WORD.get(), AncientWords.clean(word));
        return stack;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.SUCCESS;
        }

        String word = getWord(stack);
        if (word.isEmpty()) {
            // Tablets from the creative tab or /give have no word yet: pick one now
            word = AncientWords.random(level.getRandom());
            withWord(stack, word);
        }

        if (knowsAll(serverPlayer, word)) {
            // Nothing left to learn: it simply reads out (in the ancient font, which shows as plain letters to them)
            serverPlayer.sendSystemMessage(Component.translatable("message.dust.ancient.reads", AncientLanguage.write(word)));
            return InteractionResult.SUCCESS;
        }

        final String puzzle = word;
        // openMenu(provider, extra data): the server builds its menu from the provider, and the extra data is sent so the
        // client can build its copy (DecipherMenu's buffer constructor reads it back in the same order)
        serverPlayer.openMenu(new SimpleMenuProvider(
                        (containerId, inventory, p) -> new DecipherMenu(containerId, inventory, puzzle, hand),
                        Component.translatable("container.dust.decipher")),
                buf -> {
                    buf.writeUtf(puzzle);
                    buf.writeEnum(hand);
                });
        return InteractionResult.SUCCESS;
    }

    private static boolean knowsAll(Player player, String word)
    {
        for (char c : word.toCharArray()) {
            if (!AncientLanguage.knows(player, AncientLanguage.glyphIndex(c))) return false;
        }
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        String word = getWord(stack);
        if (!word.isEmpty()) {
            // Shown in the ancient font: each player sees the letters they know
            builder.accept(AncientLanguage.write(word).withStyle(ChatFormatting.GOLD));
        }
        builder.accept(Component.translatable(DustReferences.TOOLTIP + "ancient_writing").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, display, builder, flag);
    }
}
