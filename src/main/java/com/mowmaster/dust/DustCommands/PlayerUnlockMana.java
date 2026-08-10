package com.mowmaster.dust.DustCommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public class PlayerUnlockMana
{
    public PlayerUnlockMana(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("unlockMana")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(c->run(
                                c.getSource(),
                                EntityArgument.getEntities(c, "targets")))

                )
        );
    }

    private static int run(CommandSourceStack source, Collection<? extends Entity> targets) {

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity target) {
                if(target instanceof ServerPlayer player)
                {
                    DustMagicAttachmentHelper.unlockMana(player);
                    source.sendSuccess(()-> Component.literal("Mana Unlocked for: " + target.getPlainTextName()), false);
                    return 1;
                }

            }
            else if (targets.size() == 1) {
                source.sendFailure(Component.literal("Target Unknown"));
                return -1;
            }
        }
        return -1;
    }

}




/*
dispatcher.register(
            Commands.literal("enchant")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(
                    Commands.argument("targets", EntityArgument.entities())
                        .then(
                            Commands.argument("enchantment", ResourceArgument.resource(context, Registries.ENCHANTMENT))
                                .executes(
                                    c -> enchant(c.getSource(), EntityArgument.getEntities(c, "targets"), ResourceArgument.getEnchantment(c, "enchantment"), 1)
                                )
                                .then(
                                    Commands.argument("level", IntegerArgumentType.integer(0))
                                        .executes(
                                            c -> enchant(
                                                c.getSource(),
                                                EntityArgument.getEntities(c, "targets"),
                                                ResourceArgument.getEnchantment(c, "enchantment"),
                                                IntegerArgumentType.getInteger(c, "level")
                                            )
                                        )
                                )
                        )
                )
        );
 */
