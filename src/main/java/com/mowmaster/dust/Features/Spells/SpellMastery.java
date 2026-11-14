package com.mowmaster.dust.Features.Spells;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

// The highest level a player has taken a spell of each element to (attachment SPELL_MASTERY, kept on death). Updated
// whenever they cast a scroll from a tree or place a pylon on one. Fusions need two elements mastered this far
// (PylonConfig.FUSION_LEVEL).
public record SpellMastery(Map<DustElement, Integer> levels)
{
    public static final SpellMastery NONE = new SpellMastery(Map.of());
    public static final Codec<SpellMastery> CODEC = Codec.unboundedMap(DustElements.CODEC, Codec.INT).xmap(SpellMastery::new, SpellMastery::levels);

    public SpellMastery
    {
        levels = Map.copyOf(levels);
    }

    public static SpellMastery get(Player player)
    {
        return player.getData(DustAttachmentTypeRegistry.SPELL_MASTERY);
    }

    // A spell of this element seen at this level: kept if it's their best
    public static void note(ServerPlayer player, DustElement element, int level)
    {
        SpellMastery mastery = get(player);
        if (mastery.levels().getOrDefault(element, 0) >= level) return;
        Map<DustElement, Integer> levels = new HashMap<>(mastery.levels());
        levels.put(element, level);
        player.setData(DustAttachmentTypeRegistry.SPELL_MASTERY, new SpellMastery(levels));
    }
}
