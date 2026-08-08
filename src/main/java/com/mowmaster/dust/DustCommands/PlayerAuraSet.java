package com.mowmaster.dust.DustCommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.EffectScrolls.Networking.DustAuraPacketHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Collection;

public class PlayerAuraSet
{
    public PlayerAuraSet(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("setAura")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0,10))
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
                    DustAuraPacketHelper.setAura(player,0,amount);
                    source.sendSuccess(()-> Component.literal(target.getPlainTextName() + "'s Aura Set To: " + amount), false);
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
