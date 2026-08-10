package com.mowmaster.dust.DustCommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustElementAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.ElementEnum;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public class PlayerModifyEarthElement
{
    public PlayerModifyEarthElement(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("modifyElementOfEarth")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(Integer.MIN_VALUE, Integer.MAX_VALUE))
                                .executes(c->run(
                                        c.getSource(),
                                        EntityArgument.getEntities(c, "targets"),
                                        IntegerArgumentType.getInteger(c, "amount"))))
                        )
        );
    }

    private static int run(CommandSourceStack source, Collection<? extends Entity> targets, int amount) {

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity target) {
                if(target instanceof ServerPlayer player)
                {
                    if(amount >=0)
                    {
                        DustElementAttachmentHelper.addToElement(player, ElementEnum.EARTH, amount);
                        source.sendSuccess(()-> Component.literal(ElementEnum.EARTH.name() + " Element Added To: " + target.getPlainTextName()), false);
                        return 1;
                    }
                    else {
                        int removeAmount = Math.abs(amount);
                        DustElementAttachmentHelper.removeFromElement(player, ElementEnum.EARTH, removeAmount);
                        source.sendSuccess(()-> Component.literal(removeAmount + " "+ ElementEnum.EARTH.name() + " Element Removed From: " + target.getPlainTextName()), false);
                        return 1;
                    }

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
