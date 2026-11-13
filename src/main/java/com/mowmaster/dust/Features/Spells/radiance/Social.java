package com.mowmaster.dust.Features.Spells.radiance;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

// Friends, allies and enemies between players (the /friend, /ally and /enemy commands: DustCommands/SocialCommands).
//   Friend  asked for with /friend <player>, answered with /friend accept|deny. Permanent, both ways. The request
//           waits for an offline player and is shown when they join.
//   Ally    the same with /ally, but only between players who are both online, and it ends when either leaves.
//   Enemy   /enemy <player>: one way and permanent; they always show as hostile. It ends any friendship or alliance
//           with them.
// Friends and allies are protected from each other: no damage from the other player or their pets, no harmful effects
// from them, and the other's pets won't go after them. Fighting still makes both players hostile for a while (Radiance),
// but friends and allies can't hurt each other to start one.
// The permanent lists are in SocialData; allies and ally requests are here, in memory. Each player's own view is copied
// to their RADIANCE_RELATIONS attachment (refresh), which their client reads for the arrows.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class Social
{
    private static final Map<UUID, Set<UUID>> ALLIES = new HashMap<>();
    // Ally requests waiting for an answer: target -> who asked
    private static final Map<UUID, Set<UUID>> ALLY_REQUESTS = new HashMap<>();

    private Social() {}

    private static Set<UUID> alliesOf(UUID player)
    {
        return ALLIES.getOrDefault(player, Set.of());
    }

    public static boolean areProtected(MinecraftServer server, UUID a, UUID b)
    {
        return !a.equals(b) && (alliesOf(a).contains(b) || SocialData.get(server).areFriends(a, b));
    }

    // Copies a player's friends, allies and enemies onto their attachment (which syncs to their client)
    public static void refresh(ServerPlayer player)
    {
        SocialData data = SocialData.get(player.level().getServer());
        UUID id = player.getUUID();
        player.setData(DustAttachmentTypeRegistry.RADIANCE_RELATIONS,
                player.getData(DustAttachmentTypeRegistry.RADIANCE_RELATIONS).withSocial(data.friendsOf(id), alliesOf(id), data.enemiesOf(id)));
    }

    private static void refresh(MinecraftServer server, UUID player)
    {
        ServerPlayer online = server.getPlayerList().getPlayer(player);
        if (online != null) refresh(online);
    }

    private static void tell(MinecraftServer server, UUID player, Component message)
    {
        ServerPlayer online = server.getPlayerList().getPlayer(player);
        if (online != null) online.sendSystemMessage(message);
    }

    // A clickable [Accept] or [Deny] that runs the command
    private static MutableComponent button(String key, ChatFormatting color, String command)
    {
        return Component.translatable(key).withStyle(style -> style.withColor(color)
                .withClickEvent(new ClickEvent.RunCommand(command))
                .withHoverEvent(new HoverEvent.ShowText(Component.literal(command))));
    }

    // "<name> wants to be your friend [Accept] [Deny]"
    private static Component requestMessage(String kind, String fromName)
    {
        String command = "/" + kind;
        return Component.translatable("dust.social." + kind + ".request", fromName).withStyle(ChatFormatting.YELLOW)
                .append(" ").append(button("dust.social.accept", ChatFormatting.GREEN, command + " accept " + fromName))
                .append(" ").append(button("dust.social.deny", ChatFormatting.RED, command + " deny " + fromName));
    }

    // ---- friends ----

    public static void requestFriend(ServerPlayer from, UUID target, String targetName)
    {
        MinecraftServer server = from.level().getServer();
        SocialData data = SocialData.get(server);
        UUID id = from.getUUID();
        if (target.equals(id)) {
            from.sendSystemMessage(Component.translatable("dust.social.self").withStyle(ChatFormatting.RED));
        } else if (data.areFriends(id, target)) {
            from.sendSystemMessage(Component.translatable("dust.social.friend.already", targetName));
        } else if (data.hasRequest(id, target)) {
            // They already asked you: asking back is a yes
            acceptFriend(from, target);
        } else {
            data.addRequest(target, id);
            from.sendSystemMessage(Component.translatable("dust.social.friend.sent", targetName).withStyle(ChatFormatting.GRAY));
            tell(server, target, requestMessage("friend", from.getPlainTextName()));
        }
    }

    public static void acceptFriend(ServerPlayer target, UUID from)
    {
        MinecraftServer server = target.level().getServer();
        SocialData data = SocialData.get(server);
        UUID id = target.getUUID();
        if (!data.removeRequest(id, from)) {
            target.sendSystemMessage(Component.translatable("dust.social.no_request", data.nameOf(from)).withStyle(ChatFormatting.RED));
            return;
        }
        data.removeRequest(from, id);
        // Friends aren't enemies
        data.removeEnemy(id, from);
        data.removeEnemy(from, id);
        data.addFriends(id, from);
        refresh(target);
        refresh(server, from);
        target.sendSystemMessage(Component.translatable("dust.social.friend.added", data.nameOf(from)).withStyle(ChatFormatting.GREEN));
        tell(server, from, Component.translatable("dust.social.friend.added", target.getPlainTextName()).withStyle(ChatFormatting.GREEN));
    }

    public static void denyFriend(ServerPlayer target, UUID from)
    {
        MinecraftServer server = target.level().getServer();
        SocialData data = SocialData.get(server);
        if (!data.removeRequest(target.getUUID(), from)) {
            target.sendSystemMessage(Component.translatable("dust.social.no_request", data.nameOf(from)).withStyle(ChatFormatting.RED));
            return;
        }
        target.sendSystemMessage(Component.translatable("dust.social.friend.denied", data.nameOf(from)).withStyle(ChatFormatting.GRAY));
        tell(server, from, Component.translatable("dust.social.friend.denied_by", target.getPlainTextName()).withStyle(ChatFormatting.GRAY));
    }

    public static void removeFriend(ServerPlayer player, UUID other)
    {
        MinecraftServer server = player.level().getServer();
        SocialData data = SocialData.get(server);
        if (!data.removeFriends(player.getUUID(), other)) {
            player.sendSystemMessage(Component.translatable("dust.social.friend.not", data.nameOf(other)).withStyle(ChatFormatting.RED));
            return;
        }
        refresh(player);
        refresh(server, other);
        player.sendSystemMessage(Component.translatable("dust.social.friend.removed", data.nameOf(other)));
        tell(server, other, Component.translatable("dust.social.friend.removed_by", player.getPlainTextName()));
    }

    // The oldest request waiting for this player (for /friend accept with no name), or null
    public static @Nullable UUID oldestFriendRequest(ServerPlayer target)
    {
        List<UUID> requests = SocialData.get(target.level().getServer()).requestsTo(target.getUUID());
        return requests.isEmpty() ? null : requests.getFirst();
    }

    // ---- allies ----

    public static void requestAlly(ServerPlayer from, ServerPlayer target)
    {
        UUID id = from.getUUID(), targetId = target.getUUID();
        if (targetId.equals(id)) {
            from.sendSystemMessage(Component.translatable("dust.social.self").withStyle(ChatFormatting.RED));
        } else if (alliesOf(id).contains(targetId)) {
            from.sendSystemMessage(Component.translatable("dust.social.ally.already", target.getPlainTextName()));
        } else if (ALLY_REQUESTS.getOrDefault(id, Set.of()).contains(targetId)) {
            acceptAlly(from, targetId);
        } else {
            ALLY_REQUESTS.computeIfAbsent(targetId, key -> new LinkedHashSet<>()).add(id);
            from.sendSystemMessage(Component.translatable("dust.social.ally.sent", target.getPlainTextName()).withStyle(ChatFormatting.GRAY));
            target.sendSystemMessage(requestMessage("ally", from.getPlainTextName()));
        }
    }

    public static void acceptAlly(ServerPlayer target, UUID from)
    {
        MinecraftServer server = target.level().getServer();
        UUID id = target.getUUID();
        ServerPlayer asker = server.getPlayerList().getPlayer(from);
        Set<UUID> waiting = ALLY_REQUESTS.getOrDefault(id, new LinkedHashSet<>());
        if (!waiting.remove(from) || asker == null) {
            target.sendSystemMessage(Component.translatable("dust.social.no_request", SocialData.get(server).nameOf(from)).withStyle(ChatFormatting.RED));
            return;
        }
        ALLY_REQUESTS.getOrDefault(from, new LinkedHashSet<>()).remove(id);
        // Allies aren't enemies
        SocialData.get(server).removeEnemy(id, from);
        SocialData.get(server).removeEnemy(from, id);
        ALLIES.computeIfAbsent(id, key -> new LinkedHashSet<>()).add(from);
        ALLIES.computeIfAbsent(from, key -> new LinkedHashSet<>()).add(id);
        refresh(target);
        refresh(asker);
        target.sendSystemMessage(Component.translatable("dust.social.ally.added", asker.getPlainTextName()).withStyle(ChatFormatting.GREEN));
        asker.sendSystemMessage(Component.translatable("dust.social.ally.added", target.getPlainTextName()).withStyle(ChatFormatting.GREEN));
    }

    public static void denyAlly(ServerPlayer target, UUID from)
    {
        MinecraftServer server = target.level().getServer();
        if (!ALLY_REQUESTS.getOrDefault(target.getUUID(), new LinkedHashSet<>()).remove(from)) {
            target.sendSystemMessage(Component.translatable("dust.social.no_request", SocialData.get(server).nameOf(from)).withStyle(ChatFormatting.RED));
            return;
        }
        target.sendSystemMessage(Component.translatable("dust.social.ally.denied", SocialData.get(server).nameOf(from)).withStyle(ChatFormatting.GRAY));
        tell(server, from, Component.translatable("dust.social.ally.denied_by", target.getPlainTextName()).withStyle(ChatFormatting.GRAY));
    }

    // Ends an alliance; false if there wasn't one
    private static boolean unally(MinecraftServer server, UUID a, UUID b)
    {
        boolean removed = ALLIES.getOrDefault(a, new LinkedHashSet<>()).remove(b) | ALLIES.getOrDefault(b, new LinkedHashSet<>()).remove(a);
        if (removed) {
            refresh(server, a);
            refresh(server, b);
        }
        return removed;
    }

    public static void removeAlly(ServerPlayer player, UUID other)
    {
        MinecraftServer server = player.level().getServer();
        String name = SocialData.get(server).nameOf(other);
        if (!unally(server, player.getUUID(), other)) {
            player.sendSystemMessage(Component.translatable("dust.social.ally.not", name).withStyle(ChatFormatting.RED));
            return;
        }
        player.sendSystemMessage(Component.translatable("dust.social.ally.removed", name));
        tell(server, other, Component.translatable("dust.social.ally.removed_by", player.getPlainTextName()));
    }

    // Who has asked target to be allies (for suggestions)
    public static Set<UUID> allyRequestsTo(UUID target)
    {
        return Set.copyOf(ALLY_REQUESTS.getOrDefault(target, Set.of()));
    }

    public static @Nullable UUID oldestAllyRequest(ServerPlayer target)
    {
        Set<UUID> waiting = ALLY_REQUESTS.getOrDefault(target.getUUID(), Set.of());
        return waiting.isEmpty() ? null : waiting.iterator().next();
    }

    // ---- enemies ----

    // Declares an enemy: any friendship, alliance or request between the two ends, and they show as hostile
    public static void declareEnemy(ServerPlayer player, UUID enemy, String enemyName)
    {
        MinecraftServer server = player.level().getServer();
        SocialData data = SocialData.get(server);
        UUID id = player.getUUID();
        if (enemy.equals(id)) {
            player.sendSystemMessage(Component.translatable("dust.social.self").withStyle(ChatFormatting.RED));
            return;
        }
        if (!data.addEnemy(id, enemy)) {
            player.sendSystemMessage(Component.translatable("dust.social.enemy.already", enemyName));
            return;
        }
        boolean wasFriend = data.removeFriends(id, enemy);
        boolean wasAlly = unally(server, id, enemy);
        data.removeRequest(id, enemy);
        data.removeRequest(enemy, id);
        ALLY_REQUESTS.getOrDefault(id, new LinkedHashSet<>()).remove(enemy);
        ALLY_REQUESTS.getOrDefault(enemy, new LinkedHashSet<>()).remove(id);
        refresh(player);
        refresh(server, enemy);
        player.sendSystemMessage(Component.translatable("dust.social.enemy.added", enemyName).withStyle(ChatFormatting.RED));
        if (wasFriend || wasAlly) {
            tell(server, enemy, Component.translatable("dust.social.enemy.broke", player.getPlainTextName()).withStyle(ChatFormatting.RED));
        }
    }

    public static void removeEnemy(ServerPlayer player, UUID enemy)
    {
        SocialData data = SocialData.get(player.level().getServer());
        if (!data.removeEnemy(player.getUUID(), enemy)) {
            player.sendSystemMessage(Component.translatable("dust.social.enemy.not", data.nameOf(enemy)).withStyle(ChatFormatting.RED));
            return;
        }
        refresh(player);
        player.sendSystemMessage(Component.translatable("dust.social.enemy.removed", data.nameOf(enemy)));
    }

    // ---- joining and leaving ----

    // Joining: remember their name, give them their lists, and show friend requests that came while they were away
    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        SocialData data = SocialData.get(player.level().getServer());
        data.rememberName(player.getUUID(), player.getPlainTextName());
        refresh(player);
        for (UUID from : data.requestsTo(player.getUUID())) player.sendSystemMessage(requestMessage("friend", data.nameOf(from)));
    }

    // Leaving: their alliances and ally requests end
    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        MinecraftServer server = player.level().getServer();
        UUID id = player.getUUID();
        for (UUID ally : Set.copyOf(alliesOf(id))) {
            unally(server, id, ally);
            tell(server, ally, Component.translatable("dust.social.ally.left", player.getPlainTextName()).withStyle(ChatFormatting.GRAY));
        }
        ALLIES.remove(id);
        ALLY_REQUESTS.remove(id);
        ALLY_REQUESTS.values().forEach(waiting -> waiting.remove(id));
    }

    // ---- protection ----

    // The player behind something: the player themself, or a pet's owner. (A projectile's or spell's damage source
    // already names its shooter or caster as the entity.)
    private static @Nullable UUID playerBehind(@Nullable Entity entity)
    {
        if (entity instanceof ServerPlayer player) return player.getUUID();
        if (entity instanceof OwnableEntity pet && pet.getOwnerReference() != null) return pet.getOwnerReference().getUUID();
        return null;
    }

    // No damage between friends or allies (their pets included). HIGH: before anything reacts to the hit.
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onIncomingDamage(LivingIncomingDamageEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer victim)) return;
        UUID attacker = playerBehind(event.getSource().getEntity());
        if (attacker != null && areProtected(victim.level().getServer(), victim.getUUID(), attacker)) event.setCanceled(true);
    }

    // No harmful effects from a friend or ally (splash potions, spells, lingering clouds all name who made them)
    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event)
    {
        if (!(event.getEntity() instanceof ServerPlayer target)) return;
        if (event.getEffectInstance().getEffect().value().getCategory() != MobEffectCategory.HARMFUL) return;
        UUID source = playerBehind(event.getEffectSource());
        if (source != null && areProtected(target.level().getServer(), target.getUUID(), source)) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    // A pet won't go after its owner's friends or allies
    @SubscribeEvent
    public static void onChangeTarget(LivingChangeTargetEvent event)
    {
        LivingEntity target = event.getNewAboutToBeSetTarget();
        if (!(target instanceof ServerPlayer player) || !(event.getEntity() instanceof OwnableEntity)) return;
        UUID owner = playerBehind(event.getEntity());
        if (owner != null && areProtected(player.level().getServer(), player.getUUID(), owner)) event.setCanceled(true);
    }
}
