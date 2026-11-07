package com.mowmaster.dust.DustCommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

// /addMana <players> <amount>
public class PlayerManaAdd
{
    public PlayerManaAdd(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("addMana")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                .executes(c -> run(
                                        c.getSource(),
                                        EntityArgument.getPlayers(c, "targets"),
                                        IntegerArgumentType.getInteger(c, "amount"))))
                )
        );
    }

    // Returns how many players were changed (a command's result is its "success count")
    private static int run(CommandSourceStack source, Collection<ServerPlayer> targets, int amount) {
        for (ServerPlayer player : targets) {
            int addedAmount = DustMagicAttachmentHelper.addManaWithConsumedCount(player, amount);
            source.sendSuccess(() -> Component.literal(addedAmount + " Mana Added To: " + player.getPlainTextName()), false);
        }
        return targets.size();
    }
}
