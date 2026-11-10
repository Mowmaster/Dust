package com.mowmaster.dust.Features.Research;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.Features.DustComposition.DustResearch;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.function.Supplier;

// The Research tab's entries: discoveries that are unlocked by what a player has learned rather than by finding
// something in the world (the Element Tester, once you've found all five pure dusts...). Each one shows faded in
// the book until it's unlocked, then unlocks with the usual toast. A dust recipe can require one (DustRecipe).
//
// Other code can add more (from a mod constructor, so they exist on both sides):
//     ResearchUnlocks.register(id, () -> new ItemStack(MY_ITEM.get()), data -> ...);
public final class ResearchUnlocks
{
    // id: the entry's id in the Research tab. icon: a Supplier, because items don't exist yet when this class loads.
    // condition: given everything the player has researched, whether it's unlocked.
    public record Unlock(Identifier id, Supplier<ItemStack> icon, Predicate<ResearchData> condition) {}

    private static final List<Unlock> UNLOCKS = new CopyOnWriteArrayList<>();

    // The Element Tester: found something of each pure dust (by its flame, or measured on an examiner)
    public static final Unlock ELEMENT_TESTER = register(Identifier.fromNamespaceAndPath(DustReferences.MODID, "element_tester"),
            () -> new ItemStack(DustBlockRegistry.ELEMENT_TESTER.get()),
            data -> {
                for (EnumElement element : EnumElement.values()) {
                    if (!DustResearch.hasFoundPure(data, element)) return false;
                }
                return true;
            });

    private ResearchUnlocks() {}

    public static Unlock register(Identifier id, Supplier<ItemStack> icon, Predicate<ResearchData> condition)
    {
        Unlock unlock = new Unlock(id, icon, condition);
        UNLOCKS.add(unlock);
        ResearchTabs.RESEARCH.clearCache();
        return unlock;
    }

    public static List<Unlock> all()
    {
        return List.copyOf(UNLOCKS);
    }

    public static Unlock byId(Identifier id)
    {
        for (Unlock unlock : UNLOCKS) {
            if (unlock.id().equals(id)) return unlock;
        }
        return null;
    }

    // Unlocks whatever the player now qualifies for. Call after anything that could complete a condition.
    public static void check(ServerPlayer player)
    {
        if (!Research.carriesNotes(player)) return;
        for (Unlock unlock : UNLOCKS) {
            ResearchEntry entry = new ResearchEntry(ResearchTabs.RESEARCH, unlock.id());
            // Research.get each time: an unlock just made could be part of the next one's condition
            ResearchData data = Research.get(player);
            if (!data.has(entry) && unlock.condition().test(data)) Research.observe(player, entry, player.blockPosition());
        }
    }
}
