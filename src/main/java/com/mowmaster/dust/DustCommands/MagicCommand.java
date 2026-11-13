package com.mowmaster.dust.DustCommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Magic.PlayerMagic;
import com.mowmaster.dust.Features.Magic.PlayerMagicHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.function.BiConsumer;

// Testing the player's magic (Features/Magic) until machines and research do it:
//   /dustmagic show <players>
//   /dustmagic dust <players> <element> <amount>        add (a negative amount takes away); ignores the type limit
//   /dustmagic dust <players> clear
//   /dustmagic store <players> <types> <capacity>       how many elements can be carried, and how much of each
//   /dustmagic mana <players> <amount>                  set mana (up to max)
//   /dustmagic maxmana <players> <amount>
//   /dustmagic attune <players> <element>   /dustmagic unattune <players> <element>
// Elements are ids, with suggestions: dust:fire, dust:knowledge...
public class MagicCommand
{
    // Brigadier suggestion provider: offers every element id as the player types
    private static final SuggestionProvider<CommandSourceStack> ELEMENTS =
            (context, builder) -> SharedSuggestionProvider.suggestResource(DustElements.all().stream().map(DustElement::id), builder);

    public MagicCommand(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("dustmagic")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("show")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(c -> show(c.getSource(), EntityArgument.getPlayers(c, "targets")))))
                .then(Commands.literal("dust")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.literal("clear")
                                        .executes(c -> each(c, (source, player) -> {
                                            PlayerMagic magic = PlayerMagicHelper.get(player);
                                            for (DustElement element : magic.dust().keySet()) PlayerMagicHelper.setDust(player, element, 0);
                                        })))
                                .then(Commands.argument("element", IdentifierArgument.id()).suggests(ELEMENTS)
                                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                .executes(c -> {
                                                    DustElement element = element(c);
                                                    int amount = IntegerArgumentType.getInteger(c, "amount");
                                                    return each(c, (source, player) ->
                                                            PlayerMagicHelper.setDust(player, element, PlayerMagicHelper.dust(player, element) + amount));
                                                })))))
                .then(Commands.literal("store")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("types", IntegerArgumentType.integer(0))
                                        .then(Commands.argument("capacity", IntegerArgumentType.integer(0))
                                                .executes(c -> {
                                                    int types = IntegerArgumentType.getInteger(c, "types");
                                                    int capacity = IntegerArgumentType.getInteger(c, "capacity");
                                                    return each(c, (source, player) ->
                                                            PlayerMagicHelper.set(player, PlayerMagicHelper.get(player).withStoreSize(types, capacity)));
                                                })))))
                .then(Commands.literal("mana")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .executes(c -> {
                                            int amount = IntegerArgumentType.getInteger(c, "amount");
                                            return each(c, (source, player) -> PlayerMagicHelper.setMana(player, amount));
                                        }))))
                .then(Commands.literal("maxmana")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                        .executes(c -> {
                                            int amount = IntegerArgumentType.getInteger(c, "amount");
                                            return each(c, (source, player) -> {
                                                PlayerMagic magic = PlayerMagicHelper.get(player);
                                                PlayerMagicHelper.set(player, magic.withMana(Math.min(magic.mana(), amount), amount));
                                            });
                                        }))))
                .then(Commands.literal("attune")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("element", IdentifierArgument.id()).suggests(ELEMENTS)
                                        .executes(c -> {
                                            DustElement element = element(c);
                                            return each(c, (source, player) -> PlayerMagicHelper.attune(player, element));
                                        }))))
                .then(Commands.literal("unattune")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("element", IdentifierArgument.id()).suggests(ELEMENTS)
                                        .executes(c -> {
                                            DustElement element = element(c);
                                            return each(c, (source, player) -> PlayerMagicHelper.unattune(player, element));
                                        })))));
    }

    private static DustElement element(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
    {
        Identifier id = IdentifierArgument.getId(context, "element");
        DustElement element = DustElements.get(id);
        if (element == null) throw new SimpleCommandExceptionType(Component.literal("Unknown element: " + id)).create();
        return element;
    }

    // Runs a change for each target, then shows them how they stand
    private static int each(CommandContext<CommandSourceStack> context, BiConsumer<CommandSourceStack, ServerPlayer> change) throws CommandSyntaxException
    {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        for (ServerPlayer player : targets) change.accept(context.getSource(), player);
        return show(context.getSource(), targets);
    }

    private static int show(CommandSourceStack source, Collection<ServerPlayer> targets)
    {
        for (ServerPlayer player : targets) {
            PlayerMagic magic = PlayerMagicHelper.get(player);
            StringBuilder dust = new StringBuilder();
            magic.dust().forEach((element, amount) -> dust.append(dust.isEmpty() ? "" : ", ").append(element.id().getPath()).append(' ').append(amount));
            StringBuilder attuned = new StringBuilder();
            magic.attunements().forEach(element -> attuned.append(attuned.isEmpty() ? "" : ", ").append(element.id().getPath()));
            String text = player.getPlainTextName() + ": mana " + magic.mana() + "/" + magic.maxMana()
                    + " | dust (" + magic.dust().size() + "/" + magic.dustTypes() + " types, up to " + magic.dustCapacity() + " each): "
                    + (dust.isEmpty() ? "none" : dust) + " | attuned: " + (attuned.isEmpty() ? "none" : attuned);
            source.sendSuccess(() -> Component.literal(text), false);
        }
        return targets.size();
    }
}
