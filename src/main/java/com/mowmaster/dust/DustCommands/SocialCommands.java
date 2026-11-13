package com.mowmaster.dust.DustCommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mowmaster.dust.Features.Spells.radiance.Social;
import com.mowmaster.dust.Features.Spells.radiance.SocialData;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

// Survival commands for friends, allies and enemies (Features/Spells/radiance/Social). Anyone can use them (no
// .requires). Names in accept / deny / remove are matched against your own requests or lists, and suggested from them.
//   /friend <player>              ask to be friends (they're told now, or when they next join)
//   /friend accept|deny [player]  answer a request (no name: the oldest one)
//   /friend remove <player>       stop being friends
//   /friend list                  your friends, allies and enemies
//   /ally <player>                ask to be allies while you're both online
//   /ally accept|deny [player]    /ally remove <player>
//   /enemy <player>               declare an enemy (ends any friendship or alliance)
//   /enemy remove <player>
public class SocialCommands
{
    private static final SimpleCommandExceptionType NO_REQUESTS = new SimpleCommandExceptionType(Component.translatable("dust.social.no_requests"));
    private static final SimpleCommandExceptionType ONE_PLAYER = new SimpleCommandExceptionType(Component.translatable("dust.social.one_player"));

    public SocialCommands(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        // friend
        dispatcher.register(Commands.literal("friend")
                .then(Commands.literal("accept")
                        .executes(c -> answer(c, null, Social::oldestFriendRequest, Social::acceptFriend))
                        .then(Commands.argument("player", StringArgumentType.word()).suggests(FRIEND_REQUESTS)
                                .executes(c -> answer(c, "player", Social::oldestFriendRequest, Social::acceptFriend))))
                .then(Commands.literal("deny")
                        .executes(c -> answer(c, null, Social::oldestFriendRequest, Social::denyFriend))
                        .then(Commands.argument("player", StringArgumentType.word()).suggests(FRIEND_REQUESTS)
                                .executes(c -> answer(c, "player", Social::oldestFriendRequest, Social::denyFriend))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("player", StringArgumentType.word()).suggests(FRIENDS)
                                .executes(c -> byName(c, Social::removeFriend))))
                .then(Commands.literal("list").executes(SocialCommands::list))
                .then(Commands.argument("target", GameProfileArgument.gameProfile())
                        .executes(c -> {
                            NameAndId target = single(c);
                            Social.requestFriend(c.getSource().getPlayerOrException(), target.id(), target.name());
                            return 1;
                        })));

        // ally
        dispatcher.register(Commands.literal("ally")
                .then(Commands.literal("accept")
                        .executes(c -> answer(c, null, Social::oldestAllyRequest, Social::acceptAlly))
                        .then(Commands.argument("player", StringArgumentType.word()).suggests(ALLY_REQUESTS)
                                .executes(c -> answer(c, "player", Social::oldestAllyRequest, Social::acceptAlly))))
                .then(Commands.literal("deny")
                        .executes(c -> answer(c, null, Social::oldestAllyRequest, Social::denyAlly))
                        .then(Commands.argument("player", StringArgumentType.word()).suggests(ALLY_REQUESTS)
                                .executes(c -> answer(c, "player", Social::oldestAllyRequest, Social::denyAlly))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("player", StringArgumentType.word()).suggests(ALLIES)
                                .executes(c -> byName(c, Social::removeAlly))))
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(c -> {
                            Social.requestAlly(c.getSource().getPlayerOrException(), EntityArgument.getPlayer(c, "target"));
                            return 1;
                        })));

        // enemy
        dispatcher.register(Commands.literal("enemy")
                .then(Commands.literal("remove")
                        .then(Commands.argument("player", StringArgumentType.word()).suggests(ENEMIES)
                                .executes(c -> byName(c, Social::removeEnemy))))
                .then(Commands.argument("target", GameProfileArgument.gameProfile())
                        .executes(c -> {
                            NameAndId target = single(c);
                            Social.declareEnemy(c.getSource().getPlayerOrException(), target.id(), target.name());
                            return 1;
                        })));
    }

    // GameProfileArgument allows selectors (@a) and gives a collection: these commands take exactly one player
    private static NameAndId single(CommandContext<CommandSourceStack> c) throws CommandSyntaxException
    {
        Collection<NameAndId> targets = GameProfileArgument.getGameProfiles(c, "target");
        if (targets.size() != 1) throw ONE_PLAYER.create();
        return targets.iterator().next();
    }

    private static SocialData data(CommandContext<CommandSourceStack> c)
    {
        return SocialData.get(c.getSource().getServer());
    }

    // A name typed back into a UUID, using the names the server has seen; null if it's not a known player
    private static @Nullable UUID idOf(CommandContext<CommandSourceStack> c, String name)
    {
        ServerPlayer online = c.getSource().getServer().getPlayerList().getPlayerByName(name);
        if (online != null) return online.getUUID();
        SocialData data = data(c);
        for (ServerPlayer p : c.getSource().getServer().getPlayerList().getPlayers()) data.rememberName(p.getUUID(), p.getPlainTextName());
        // Search the player's own lists for an offline name
        ServerPlayer self = c.getSource().getPlayer();
        if (self == null) return null;
        var relations = self.getData(DustAttachmentTypeRegistry.RADIANCE_RELATIONS);
        java.util.Set<UUID> known = new java.util.HashSet<>();
        known.addAll(relations.friends());
        known.addAll(relations.enemies());
        known.addAll(data.requestsTo(self.getUUID()));
        for (UUID id : known) if (data.nameOf(id).equalsIgnoreCase(name)) return id;
        return null;
    }

    // accept / deny: the named request, or the oldest one
    private static int answer(CommandContext<CommandSourceStack> c, @Nullable String argument,
                              Function<ServerPlayer, @Nullable UUID> oldest, BiConsumer<ServerPlayer, UUID> action) throws CommandSyntaxException
    {
        ServerPlayer player = c.getSource().getPlayerOrException();
        UUID from = argument == null ? oldest.apply(player) : idOf(c, StringArgumentType.getString(c, argument));
        if (from == null) throw NO_REQUESTS.create();
        action.accept(player, from);
        return 1;
    }

    // remove: the named player from one of your lists
    private static int byName(CommandContext<CommandSourceStack> c, BiConsumer<ServerPlayer, UUID> action) throws CommandSyntaxException
    {
        ServerPlayer player = c.getSource().getPlayerOrException();
        String name = StringArgumentType.getString(c, "player");
        UUID other = idOf(c, name);
        if (other == null) {
            c.getSource().sendFailure(Component.translatable("dust.social.unknown", name));
            return 0;
        }
        action.accept(player, other);
        return 1;
    }

    private static int list(CommandContext<CommandSourceStack> c) throws CommandSyntaxException
    {
        ServerPlayer player = c.getSource().getPlayerOrException();
        SocialData data = data(c);
        var relations = player.getData(DustAttachmentTypeRegistry.RADIANCE_RELATIONS);
        Function<Collection<UUID>, String> names = ids -> ids.isEmpty() ? "-" : String.join(", ", ids.stream().map(data::nameOf).sorted().toList());
        player.sendSystemMessage(Component.translatable("dust.social.list.friends", names.apply(relations.friends())));
        player.sendSystemMessage(Component.translatable("dust.social.list.allies", names.apply(relations.allies())));
        player.sendSystemMessage(Component.translatable("dust.social.list.enemies", names.apply(relations.enemies())));
        player.sendSystemMessage(Component.translatable("dust.social.list.requests", names.apply(data.requestsTo(player.getUUID()))));
        return 1;
    }

    // ---- suggestions: names from your own requests and lists ----

    private static SuggestionProvider<CommandSourceStack> suggest(Function<CommandContext<CommandSourceStack>, Collection<UUID>> ids)
    {
        return (c, builder) -> SharedSuggestionProvider.suggest(ids.apply(c).stream().map(data(c)::nameOf), builder);
    }

    private static Collection<UUID> own(CommandContext<CommandSourceStack> c, Function<com.mowmaster.dust.Features.Spells.radiance.RadianceRelations, Collection<UUID>> pick)
    {
        ServerPlayer player = c.getSource().getPlayer();
        return player == null ? java.util.List.of() : pick.apply(player.getData(DustAttachmentTypeRegistry.RADIANCE_RELATIONS));
    }

    private static final SuggestionProvider<CommandSourceStack> FRIEND_REQUESTS = suggest(c -> {
        ServerPlayer player = c.getSource().getPlayer();
        return player == null ? java.util.List.of() : data(c).requestsTo(player.getUUID());
    });
    private static final SuggestionProvider<CommandSourceStack> ALLY_REQUESTS = suggest(c -> {
        ServerPlayer player = c.getSource().getPlayer();
        return player == null ? java.util.List.of() : Social.allyRequestsTo(player.getUUID());
    });
    private static final SuggestionProvider<CommandSourceStack> FRIENDS = suggest(c -> own(c, r -> r.friends()));
    private static final SuggestionProvider<CommandSourceStack> ALLIES = suggest(c -> own(c, r -> r.allies()));
    private static final SuggestionProvider<CommandSourceStack> ENEMIES = suggest(c -> own(c, r -> r.enemies()));
}
