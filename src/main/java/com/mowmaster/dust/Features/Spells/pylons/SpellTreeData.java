package com.mowmaster.dust.Features.Spells.pylons;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustReferences;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Each spell tree's experience, by the tree's id, saved with the world (data/dust_spell_trees.dat). Casting a spell
// adds to it wherever the caster is, even with the tree's pylons unloaded; the main pylon reads it when it ticks.
// SavedData: a world-wide store Minecraft saves when it's marked dirty.
public class SpellTreeData extends SavedData
{
    private static final Codec<SpellTreeData> CODEC = Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.INT)
            .xmap(SpellTreeData::new, data -> data.experience);

    // SavedDataType: how to make, load and save this data, under its file id
    private static final SavedDataType<SpellTreeData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(DustReferences.MODID, "spell_trees"), SpellTreeData::new, CODEC);

    private final Map<UUID, Integer> experience;

    public SpellTreeData()
    {
        this(Map.of());
    }

    private SpellTreeData(Map<UUID, Integer> experience)
    {
        this.experience = new HashMap<>(experience);
    }

    // The server's copy (kept with the overworld's data)
    public static SpellTreeData get(MinecraftServer server)
    {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    public int experience(UUID tree)
    {
        return experience.getOrDefault(tree, 0);
    }

    public void setExperience(UUID tree, int xp)
    {
        experience.put(tree, Math.max(0, xp));
        setDirty();
    }

    public void addExperience(UUID tree, int xp)
    {
        if (xp > 0) setExperience(tree, experience(tree) + xp);
    }

    // A tree whose main pylon is gone
    public void forget(UUID tree)
    {
        if (experience.remove(tree) != null) setDirty();
    }
}
