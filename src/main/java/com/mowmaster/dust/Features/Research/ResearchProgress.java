package com.mowmaster.dust.Features.Research;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.List;
import java.util.function.Supplier;

// The Research tab's machine discoveries, and what unlocks them. Each is a ResearchUnlocks entry that only events grant
// (its condition is never met by itself); the gated ones are what their recipes ask for ("research" in the recipe):
//   a research station crafted for the first time    its own entry (Crusher, Separation, Evaporation, Distillation,
//                                                    Element Crafter)
//   the Element Crafter crafted                       Transformation, Combination and Mixing Tables (their recipes need it)
//   the first crystal made (Transformation Table)     Spell Pylon and Shape Pylon (Element Crafter recipes)
//   the first Spell Scroll clicked on a Spell Pylon    the Dust Grinder
//   the Neutral Pylon crafted                         the Damage, Experienced, Thrifty and Giant Pylons
// Granted whether or not the player carries Research Notes (a locked recipe would otherwise be lost for good).
@EventBusSubscriber(modid = DustReferences.MODID)
public final class ResearchProgress
{
    private ResearchProgress() {}

    // id dust:unlock_<name>, its icon the machine (separate from the machine's own Blocks entry, dust:<name>)
    private static ResearchUnlocks.Unlock machine(String name, Supplier<? extends ItemLike> item)
    {
        return ResearchUnlocks.register(Identifier.fromNamespaceAndPath(DustReferences.MODID, "unlock_" + name),
                () -> new ItemStack(item.get()), data -> false);
    }

    public static final ResearchUnlocks.Unlock CRUSHER = machine("research_crusher", DustBlockRegistry.RESEARCH_CRUSHER);
    public static final ResearchUnlocks.Unlock SEPARATION = machine("research_separation", DustBlockRegistry.RESEARCH_SEPARATION);
    public static final ResearchUnlocks.Unlock EVAPORATION = machine("research_evaporation", DustBlockRegistry.RESEARCH_EVAPORATION);
    public static final ResearchUnlocks.Unlock DISTILLATION = machine("research_distillation", DustBlockRegistry.RESEARCH_DISTILLATION);
    public static final ResearchUnlocks.Unlock ELEMENT_CRAFTER = machine("element_crafter", DustBlockRegistry.ELEMENT_CRAFTER);
    public static final ResearchUnlocks.Unlock TRANSFORMATION = machine("research_transformation", DustBlockRegistry.RESEARCH_TRANSFORMATION);
    public static final ResearchUnlocks.Unlock COMBINATION = machine("research_combination", DustBlockRegistry.RESEARCH_COMBINATION);
    public static final ResearchUnlocks.Unlock MIXING = machine("research_mixing", DustBlockRegistry.RESEARCH_MIXING);
    public static final ResearchUnlocks.Unlock SPELL_PYLON = machine("spell_pylon", DustBlockRegistry.SPELL_PYLON);
    public static final ResearchUnlocks.Unlock SHAPE_PYLON = machine("shape_pylon", DustBlockRegistry.SHAPE_PYLON);
    public static final ResearchUnlocks.Unlock DUST_GRINDER = machine("dust_grinder", DustBlockRegistry.DUST_GRINDER);
    public static final ResearchUnlocks.Unlock DAMAGE_PYLON = machine("damage_pylon", DustBlockRegistry.DAMAGE_PYLON);
    public static final ResearchUnlocks.Unlock EXPERIENCED_PYLON = machine("experienced_pylon", DustBlockRegistry.EXPERIENCED_PYLON);
    public static final ResearchUnlocks.Unlock THRIFTY_PYLON = machine("thrifty_pylon", DustBlockRegistry.THRIFTY_PYLON);
    public static final ResearchUnlocks.Unlock GIANT_PYLON = machine("giant_pylon", DustBlockRegistry.GIANT_PYLON);

    // Makes sure the class (and so its unlocks) is loaded on both sides from the mod constructor (DustMain)
    public static void init() {}

    // Unlocks it for this player (with the usual toast), if they haven't already
    public static void grant(ServerPlayer player, ResearchUnlocks.Unlock unlock)
    {
        ResearchEntry entry = new ResearchEntry(ResearchTabs.RESEARCH, unlock.id());
        if (!Research.get(player).has(entry)) Research.observe(player, entry, player.blockPosition());
    }

    // ---- the events ----

    // Something crafted, at a crafting table (ItemCraftedEvent) or an Element Crafter (ElementCrafterBlockEntity.craft)
    public static void onCrafted(Player player, ItemStack result)
    {
        if (!(player instanceof ServerPlayer serverPlayer) || result.isEmpty()) return;
        Item item = result.getItem();
        if (item == DustBlockRegistry.RESEARCH_CRUSHER.get().asItem()) grant(serverPlayer, CRUSHER);
        else if (item == DustBlockRegistry.RESEARCH_SEPARATION.get().asItem()) grant(serverPlayer, SEPARATION);
        else if (item == DustBlockRegistry.RESEARCH_EVAPORATION.get().asItem()) grant(serverPlayer, EVAPORATION);
        else if (item == DustBlockRegistry.RESEARCH_DISTILLATION.get().asItem()) grant(serverPlayer, DISTILLATION);
        else if (item == DustBlockRegistry.ELEMENT_CRAFTER.get().asItem()) {
            for (ResearchUnlocks.Unlock unlock : List.of(ELEMENT_CRAFTER, TRANSFORMATION, COMBINATION, MIXING)) grant(serverPlayer, unlock);
        } else if (item == DustBlockRegistry.NEUTRAL_PYLON.get().asItem()) {
            for (ResearchUnlocks.Unlock unlock : List.of(DAMAGE_PYLON, EXPERIENCED_PYLON, THRIFTY_PYLON, GIANT_PYLON)) grant(serverPlayer, unlock);
        }
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event)
    {
        onCrafted(event.getEntity(), event.getCrafting());
    }

    // The Transformation Table made a crystal for this player
    public static void onCrystalMade(ServerPlayer player)
    {
        grant(player, SPELL_PYLON);
        grant(player, SHAPE_PYLON);
    }

    // A Spell Scroll clicked on a Spell Pylon
    public static void onScrollOnPylon(ServerPlayer player)
    {
        grant(player, DUST_GRINDER);
    }
}
