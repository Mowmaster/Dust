package com.mowmaster.dust.DustCommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

// /unlockMana <players>
public class PlayerUnlockMana
{
    public PlayerUnlockMana(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("unlockMana")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(c -> run(c.getSource(), EntityArgument.getPlayers(c, "targets"))))
        );
    }

    private static int run(CommandSourceStack source, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            DustMagicAttachmentHelper.unlockMana(player);
            source.sendSuccess(() -> Component.literal("Mana Unlocked for: " + player.getPlainTextName()), false);
        }
        return targets.size();
    }
}
