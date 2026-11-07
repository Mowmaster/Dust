package com.mowmaster.dust.DustCommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

// /modifyPlayerElementCount <players> <fire|water|earth|chaos|order> <amount>   (negative amount removes)
public class PlayerModifyElement
{
    public PlayerModifyElement(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("modifyPlayerElementCount")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("type", StringArgumentType.string())
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(c -> run(
                                                c.getSource(),
                                                EntityArgument.getPlayers(c, "targets"),
                                                StringArgumentType.getString(c, "type"),
                                                IntegerArgumentType.getInteger(c, "amount"))))
                        )
                )
        );
    }

    private static int run(CommandSourceStack source, Collection<ServerPlayer> targets, String type, int amount) {
        EnumElement element = EnumElement.safeValueOf(type);
        if (element == null) {
            source.sendFailure(Component.literal("Unknown element: " + type));
            return 0;
        }

        for (ServerPlayer player : targets) {
            if (amount >= 0) {
                int added = element.addElement(player, amount, false);
                source.sendSuccess(() -> Component.literal(added + " " + element.getSerializedName() + " added to: " + player.getPlainTextName()), false);
            } else {
                int removed = element.removeElement(player, Math.abs(amount), false);
                source.sendSuccess(() -> Component.literal(removed + " " + element.getSerializedName() + " removed from: " + player.getPlainTextName()), false);
            }
        }
        return targets.size();
    }
}
