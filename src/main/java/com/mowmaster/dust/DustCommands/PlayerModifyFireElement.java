package com.mowmaster.dust.DustCommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustElementAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public class PlayerModifyFireElement
{
    public PlayerModifyFireElement(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("modifyElementOfFire")
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
                        int added = DustElementAttachmentHelper.addToElementFire(player, amount, false);
                        source.sendSuccess(()-> Component.literal(added + " count " + EnumElement.FIRE.name() + " Element Added To: " + target.getPlainTextName()), false);
                        return 1;
                    }
                    else {
                        int removeAmount = Math.abs(amount);
                        int removed = DustElementAttachmentHelper.removeFromElementFire(player, removeAmount, false);
                        source.sendSuccess(()-> Component.literal(removed + " count "+ EnumElement.FIRE.name() + " Element Removed From: " + target.getPlainTextName()), false);
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
