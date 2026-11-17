package com.mowmaster.dust.Features.Spells.pylons;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Spells.api.Spell;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

// Each spell tree's spell as it stands now, by the tree's id, saved with the world (data/dust_spell_tree_spells.dat),
// so scrolls and spell tools recorded from it keep up by themselves (SpellSync), even with the tree unloaded:
//   current   the spell its Spell Pylon compiles now, and how many pylons it used (the pylon writes it every second)
//   ended     trees whose Spell Pylon was broken: their scrolls lock (no further changes can come)
public class SpellTreeSpells extends SavedData
{
    public record Current(Spell spell, int pylons)
    {
        static final Codec<Current> CODEC = RecordCodecBuilder.create(i -> i.group(
                Spell.CODEC.fieldOf("spell").forGetter(Current::spell),
                Codec.INT.fieldOf("pylons").forGetter(Current::pylons)
        ).apply(i, Current::new));
    }

    private static final Codec<SpellTreeSpells> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.unboundedMap(UUIDUtil.STRING_CODEC, Current.CODEC).optionalFieldOf("current", Map.of()).forGetter(data -> data.current),
            UUIDUtil.STRING_CODEC.listOf().optionalFieldOf("ended", List.of()).forGetter(data -> List.copyOf(data.ended))
    ).apply(i, SpellTreeSpells::new));

    private static final SavedDataType<SpellTreeSpells> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(DustReferences.MODID, "spell_tree_spells"), SpellTreeSpells::new, CODEC);

    private final Map<UUID, Current> current;
    private final Set<UUID> ended;

    public SpellTreeSpells()
    {
        this(Map.of(), List.of());
    }

    private SpellTreeSpells(Map<UUID, Current> current, List<UUID> ended)
    {
        this.current = new HashMap<>(current);
        this.ended = new HashSet<>(ended);
    }

    // The server's copy (kept with the overworld's data)
    public static SpellTreeSpells get(MinecraftServer server)
    {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    public @Nullable Current current(UUID tree)
    {
        return current.get(tree);
    }

    public boolean ended(UUID tree)
    {
        return ended.contains(tree);
    }

    // The tree's spell now (only saved when it changed)
    public void publish(UUID tree, Spell spell, int pylons)
    {
        Current now = new Current(spell, pylons);
        if (now.equals(current.get(tree))) return;
        current.put(tree, now);
        setDirty();
    }

    // Its Spell Pylon was broken: its scrolls lock as they are
    public void end(UUID tree)
    {
        current.remove(tree);
        ended.add(tree);
        setDirty();
    }
}
