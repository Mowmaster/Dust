package com.mowmaster.dust.Features.Research;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.UnaryOperator;

// The research system's server-side API. Research is only recorded while the player carries Research Notes
// (anywhere in the inventory).
//   Observation     looking at something (ResearchObserver): starts its entry, and notes each biome it's seen in
//   Experimentation breaking / defeating / breeding / crafting (ResearchObserver's events): drops, health, breeding
//                   food, what it's made from and what it makes. Only for entries already started.
//   Research        Dust-specific (composition etc.): to come
public final class Research
{
    private Research() {}

    public static ResearchData get(Player player)
    {
        return player.getData(DustAttachmentTypeRegistry.RESEARCH);
    }

    public static boolean carriesNotes(Player player)
    {
        return player.getInventory().contains(stack -> stack.is(DustItemRegistry.RESEARCH_NOTES.get()));
    }

    // ---- observation ----

    // The player has watched this entry at this position long enough. Starts the entry, or notes a new biome.
    // Their client then plays a ding and shows a toast (Client/ObservationClient).
    public static void observe(ServerPlayer player, ResearchEntry entry, BlockPos where)
    {
        Level level = player.level();
        Identifier biome = biomeAt(level, where);
        EntryResearch before = get(player).get(entry);
        if (before == null) {
            long clock = level.getOverworldClockTime();
            set(player, get(player).update(entry, old -> EntryResearch.firstSeen(day(clock), timeOfDay(clock), biome)));
            PacketDistributor.sendToPlayer(player, new ObservationPayloads.Discovered(entry.tab().id(), entry.id(), false));
        } else if (!before.biomes().contains(biome)) {
            set(player, get(player).update(entry, old -> old.withBiome(biome)));
            PacketDistributor.sendToPlayer(player, new ObservationPayloads.Discovered(entry.tab().id(), entry.id(), true));
        }
    }

    // Would observing this entry here teach the player anything (a new entry, or a new biome for it)?
    public static boolean wouldLearn(Player player, ResearchEntry entry, BlockPos where)
    {
        EntryResearch known = get(player).get(entry);
        return known == null || !known.biomes().contains(biomeAt(player.level(), where));
    }

    private static Identifier biomeAt(Level level, BlockPos pos)
    {
        return level.getBiome(pos).unwrapKey().map(key -> key.identifier()).orElse(Biomes.PLAINS.identifier());
    }

    // ---- experimentation (only adds to entries that are already started) ----

    public static void experiment(ServerPlayer player, ResearchEntry entry, UnaryOperator<EntryResearch> change)
    {
        EntryResearch before = get(player).get(entry);
        if (before == null) return;
        EntryResearch after = change.apply(before);
        if (!after.equals(before)) {
            set(player, get(player).update(entry, old -> after));
            // Tell the player's client something was noted: a ding for a new recipe, a scribble for anything else.
            // (Edits to their own notes stay quiet.)
            boolean newRecipe = after.madeBy().size() > before.madeBy().size() || after.usedIn().size() > before.usedIn().size();
            boolean onlyNotes = after.withNotes(before.notes()).equals(before);
            if (!onlyNotes) PacketDistributor.sendToPlayer(player, new ObservationPayloads.Noted(newRecipe));
        }
    }

    public static List<Identifier> itemIds(List<ItemStack> stacks)
    {
        return stacks.stream().filter(stack -> !stack.isEmpty())
                .map(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem())).distinct().toList();
    }

    // ---- the player's own notes (from the book screen) ----

    public static void setNotes(ServerPlayer player, ResearchEntry entry, String notes)
    {
        experiment(player, entry, old -> old.withNotes(notes));
    }

    // ---- process steps (Features/Machines) ----

    // A machine turned input into output for this player: the step is noted on the input's original material (crushed
    // stone's steps go on stone), once. Making the output counts as finding the material, if it wasn't already.
    // An element is only ever discovered on the Elemental Examiner (or by its pure flame / crystal), so a step on an
    // element the player hasn't discovered isn't noted (it would discover it).
    public static void noteProcess(ServerPlayer player, @Nullable ResearchEntry entry, ProcessRecord process)
    {
        if (entry == null || !carriesNotes(player)) return;
        if (entry.tab() == ResearchTabs.ELEMENTS && !get(player).has(entry)) return;
        observe(player, entry, player.blockPosition());
        experiment(player, entry, old -> old.withProcess(process));
    }

    // The player crossed a step out in the book (quietly: no scribble for taking something out)
    public static void removeProcess(ServerPlayer player, ResearchEntry entry, ProcessRecord process)
    {
        EntryResearch before = get(player).get(entry);
        if (before == null || !before.processes().contains(process)) return;
        set(player, get(player).update(entry, old -> old.withoutProcess(process)));
    }

    private static void set(Player player, ResearchData data)
    {
        // Setting a new value is what syncs it to the player's client
        player.setData(DustAttachmentTypeRegistry.RESEARCH, data);
    }

    // ---- time ----

    // World day number, counting from day 1
    public static long day(long clock)
    {
        return clock / 24000L + 1;
    }

    public static int timeOfDay(long clock)
    {
        return (int) (clock % 24000L);
    }

    // "07:30": tick 0 is 6 in the morning
    public static String clockText(int timeOfDay)
    {
        int hours = (timeOfDay / 1000 + 6) % 24;
        int minutes = (timeOfDay % 1000) * 60 / 1000;
        return String.format("%02d:%02d", hours, minutes);
    }

    // dawn / morning / noon / afternoon / dusk / night, as a lang key suffix
    public static String partOfDay(int timeOfDay)
    {
        if (timeOfDay < 1000 || timeOfDay >= 23000) return "dawn";
        if (timeOfDay < 5000) return "morning";
        if (timeOfDay < 7000) return "noon";
        if (timeOfDay < 12000) return "afternoon";
        if (timeOfDay < 13500) return "dusk";
        return "night";
    }

    // An entry from a tab id and entry id (sent in packets); null if the tab doesn't exist
    public static @Nullable ResearchEntry byKey(Identifier tab, Identifier id)
    {
        return ResearchTabs.entry(tab, id);
    }
}
