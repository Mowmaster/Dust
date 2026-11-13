package com.mowmaster.dust.Features.Spells;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Research.ObservationPayloads;
import com.mowmaster.dust.Features.Spells.api.SpellTarget;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// What a player has learned about spells (a data attachment, DustAttachmentTypeRegistry.SPELL_KNOWLEDGE):
//   effects   for each element, which of its three effects (self / block / entity) they've seen: from burning its
//             Alchemical Dust with a Magical Flint and Steel, or casting it. Shown on the element's research entry.
//   shapes    the spell shapes they can use when making spells: learned by being hit by a spell of that shape, or
//             experiencing it themselves (any element: a shape learned once works for every element)
public record SpellKnowledge(Map<DustElement, List<SpellTarget>> effects, List<Identifier> shapes)
{
    public static final SpellKnowledge EMPTY = new SpellKnowledge(Map.of(), List.of());

    public SpellKnowledge
    {
        Map<DustElement, List<SpellTarget>> copy = new LinkedHashMap<>();
        effects.forEach((element, targets) -> copy.put(element, List.copyOf(targets)));
        effects = java.util.Collections.unmodifiableMap(copy);
        shapes = List.copyOf(shapes);
    }

    public static final Codec<SpellKnowledge> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(DustElements.CODEC, SpellTarget.CODEC.listOf()).optionalFieldOf("effects", Map.of()).forGetter(SpellKnowledge::effects),
            Identifier.CODEC.listOf().optionalFieldOf("shapes", List.of()).forGetter(SpellKnowledge::shapes)
    ).apply(instance, SpellKnowledge::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellKnowledge> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public List<SpellTarget> effectsOf(DustElement element)
    {
        return effects.getOrDefault(element, List.of());
    }

    // ---- the player's ----

    public static SpellKnowledge get(Player player)
    {
        return player.getData(DustAttachmentTypeRegistry.SPELL_KNOWLEDGE);
    }

    public static boolean knowsShape(Player player, Identifier shape)
    {
        return get(player).shapes().contains(shape);
    }

    // They've seen this element's effect on this kind of target. New: noted on the element's research entry (the
    // book's scribble sound), and true.
    public static boolean noteEffect(ServerPlayer player, DustElement element, SpellTarget target)
    {
        SpellKnowledge knowledge = get(player);
        List<SpellTarget> seen = knowledge.effectsOf(element);
        if (seen.contains(target)) return false;
        List<SpellTarget> more = new ArrayList<>(seen);
        more.add(target);
        Map<DustElement, List<SpellTarget>> effects = new LinkedHashMap<>(knowledge.effects());
        effects.put(element, more);
        player.setData(DustAttachmentTypeRegistry.SPELL_KNOWLEDGE, new SpellKnowledge(effects, knowledge.shapes()));
        PacketDistributor.sendToPlayer(player, new ObservationPayloads.Noted(false));
        return true;
    }

    // They can now use this shape (true if it's new to them)
    public static boolean unlockShape(ServerPlayer player, Identifier shape)
    {
        SpellKnowledge knowledge = get(player);
        if (knowledge.shapes().contains(shape)) return false;
        List<Identifier> shapes = new ArrayList<>(knowledge.shapes());
        shapes.add(shape);
        player.setData(DustAttachmentTypeRegistry.SPELL_KNOWLEDGE, new SpellKnowledge(knowledge.effects(), shapes));
        return true;
    }
}
