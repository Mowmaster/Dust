package com.mowmaster.dust.DustCommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mowmaster.mowlib.storage.StorageInteractions;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.AncientLanguage.AncientLanguage;
import com.mowmaster.dust.Features.AncientLanguage.AncientWords;
import com.mowmaster.dust.Features.AncientLanguage.AncientWritingItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

// Testing / admin tool for the ancient language:
//   /ancientglyphs learn <players> <letter|all>
//   /ancientglyphs forget <players> <letter|all>
//   /ancientglyphs list <player>
//   /ancientglyphs say <text>      (broadcasts the text written in the ancient script)
//   /ancientglyphs tablet <word>   (gives you an Ancient Tablet with that word)
public class AncientGlyphsCommand
{
    private static final SimpleCommandExceptionType BAD_LETTER =
            new SimpleCommandExceptionType(Component.literal("Expected a single letter a-z, or 'all'"));

    public AncientGlyphsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ancientglyphs")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("learn")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("glyph", StringArgumentType.word())
                                        .executes(c -> learn(c.getSource(), EntityArgument.getPlayers(c, "targets"),
                                                StringArgumentType.getString(c, "glyph"))))))
                .then(Commands.literal("forget")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("glyph", StringArgumentType.word())
                                        .executes(c -> forget(c.getSource(), EntityArgument.getPlayers(c, "targets"),
                                                StringArgumentType.getString(c, "glyph"))))))
                .then(Commands.literal("list")
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(c -> list(c.getSource(), EntityArgument.getPlayer(c, "target")))))
                .then(Commands.literal("tablet")
                        .then(Commands.argument("word", StringArgumentType.word())
                                .executes(c -> tablet(c.getSource(), StringArgumentType.getString(c, "word")))))
                .then(Commands.literal("say")
                        .then(Commands.argument("text", StringArgumentType.greedyString())
                                .executes(c -> say(c.getSource(), StringArgumentType.getString(c, "text")))))
        );
    }

    // -1 = all glyphs, else 0-25
    private static int parseGlyph(String glyph) throws CommandSyntaxException {
        if (glyph.equalsIgnoreCase("all")) return -1;
        if (glyph.length() == 1) {
            int index = AncientLanguage.glyphIndex(glyph.charAt(0));
            if (index >= 0) return index;
        }
        throw BAD_LETTER.create();
    }

    private static int learn(CommandSourceStack source, Collection<ServerPlayer> targets, String glyph) throws CommandSyntaxException {
        int index = parseGlyph(glyph);
        for (ServerPlayer player : targets) {
            if (index < 0) AncientLanguage.setKnown(player, AncientLanguage.ALL_GLYPHS);
            else AncientLanguage.learn(player, index);
            source.sendSuccess(() -> Component.literal(player.getPlainTextName() + " knows "
                    + AncientLanguage.knownCount(player) + "/" + AncientLanguage.GLYPH_COUNT + " glyphs"), true);
        }
        return targets.size();
    }

    private static int forget(CommandSourceStack source, Collection<ServerPlayer> targets, String glyph) throws CommandSyntaxException {
        int index = parseGlyph(glyph);
        for (ServerPlayer player : targets) {
            if (index < 0) AncientLanguage.setKnown(player, 0);
            else AncientLanguage.forget(player, index);
            source.sendSuccess(() -> Component.literal(player.getPlainTextName() + " knows "
                    + AncientLanguage.knownCount(player) + "/" + AncientLanguage.GLYPH_COUNT + " glyphs"), true);
        }
        return targets.size();
    }

    private static int list(CommandSourceStack source, ServerPlayer player) {
        StringBuilder known = new StringBuilder();
        for (int i = 0; i < AncientLanguage.GLYPH_COUNT; i++) {
            known.append(AncientLanguage.knows(player, i) ? AncientLanguage.letter(i) : '-');
        }
        source.sendSuccess(() -> Component.literal(player.getPlainTextName() + ": " + known), false);
        return AncientLanguage.knownCount(player);
    }

    // Gives the command user an Ancient Tablet with this word, for testing the decipher game
    private static int tablet(CommandSourceStack source, String word) throws CommandSyntaxException {
        String clean = AncientWords.clean(word);
        if (clean.isEmpty()) throw BAD_LETTER.create();
        ServerPlayer player = source.getPlayerOrException();
        ItemStack tablet = AncientWritingItem.withWord(new ItemStack(DustItemRegistry.ANCIENT_TABLET.get()), clean);
        StorageInteractions.giveToPlayer(player, tablet);
        return 1;
    }

    private static int say(CommandSourceStack source, String text) {
        // Every player sees it with their own knowledge
        source.getServer().getPlayerList().broadcastSystemMessage(AncientLanguage.write(text), false);
        return 1;
    }
}
