package com.mowmaster.dust.Features.Magic;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.effect.MobEffectInstance;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;

import java.util.ArrayList;
import java.util.List;

// The rules for a player's magic (PlayerMagic): mana, the dust store and attunement.
//   Mana        starts at 10. It only regenerates (1 a minute) once the player has awakened to their magic (Awakening). Kept under 30% for 10 minutes straight, max mana goes up 1;
//               kept at 90% or more for an hour straight, it goes down 1. A spell needing more mana than the player has
//               takes what's there and hurts half a heart per missing point.
//   Dust store  whole elements, a limited number of different ones (dustTypes) and of each (dustCapacity). An element
//               not carried yet needs a free type slot.
//   Attunement  0-3 elements: more dust from orbs, cheaper and (for 1-2) stronger spells of those elements
//               (MagicConfig.ATTUNEMENT). Only machines and research attune a player.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class PlayerMagicHelper
{
    private PlayerMagicHelper() {}

    public static PlayerMagic get(Player player)
    {
        return player.getData(DustAttachmentTypeRegistry.PLAYER_MAGIC);
    }

    // Setting the attachment saves it and syncs it to the player's client
    public static void set(Player player, PlayerMagic magic)
    {
        player.setData(DustAttachmentTypeRegistry.PLAYER_MAGIC, magic);
    }

    // ---- mana ----

    public static int mana(Player player) { return get(player).mana(); }
    public static int maxMana(Player player) { return get(player).maxMana(); }

    // Crystallized (Crystal on yourself): mana and dust hold half again as much a level, for as long as it lasts. These
    // are the caps for filling up; what's over the normal cap when it ends stays, it just can't be topped up.
    public static int manaCap(Player player)
    {
        return boosted(player, get(player).maxMana());
    }

    public static int dustCap(Player player)
    {
        return boosted(player, get(player).dustCapacity());
    }

    private static int boosted(Player player, int base)
    {
        MobEffectInstance crystal = player.getEffect(DustEffectsRegistry.CRYSTALLIZED);
        return crystal == null ? base : base + base * (crystal.getAmplifier() + 1) / 2;
    }

    // Mana back, up to the cap (manaCap). Returns how much was actually added.
    public static int addMana(Player player, int amount)
    {
        PlayerMagic magic = get(player);
        int added = Math.max(0, Math.min(amount, manaCap(player) - magic.mana()));
        if (added > 0) set(player, magic.withMana(magic.mana() + added, magic.maxMana()));
        return added;
    }

    public static void setMana(Player player, int amount)
    {
        PlayerMagic magic = get(player);
        set(player, magic.withMana(Math.clamp(amount, 0, magic.maxMana()), magic.maxMana()));
    }

    // Spends mana for a spell. Whatever's missing is paid in health: half a heart per point (magic damage, so armour
    // doesn't help). Returns false if the player died paying it.
    public static boolean spendMana(ServerPlayer player, int amount)
    {
        PlayerMagic magic = get(player);
        int paid = Math.min(amount, magic.mana());
        set(player, magic.withMana(magic.mana() - paid, magic.maxMana()));
        int missing = amount - paid;
        if (missing > 0) {
            ServerLevel level = player.level();
            player.hurtServer(level, level.damageSources().magic(), missing * MagicConfig.DAMAGE_PER_MISSING_MANA);
        }
        return player.isAlive();
    }

    // The mana a spell using this much dust costs: 1 per 10 dust, rounded up (10 dust: 1, 11: 2)
    public static int manaForDust(int dust)
    {
        return (dust + MagicConfig.DUST_PER_MANA - 1) / MagicConfig.DUST_PER_MANA;
    }

    // ---- dust store ----

    public static int dust(Player player, DustElement element)
    {
        return get(player).dustOf(element);
    }

    // Puts dust in: as much as fits (the element needs a free type slot if it isn't carried yet, and each element is
    // capped at dustCap: dustCapacity, more while Crystallized). Returns how much went in; simulate only works it out.
    public static int addDust(Player player, DustElement element, int amount, boolean simulate)
    {
        if (amount <= 0) return 0;
        PlayerMagic magic = get(player);
        int have = magic.dustOf(element);
        if (have == 0 && magic.dust().size() >= magic.dustTypes()) return 0;
        int added = Math.min(amount, dustCap(player) - have);
        if (added <= 0) return 0;
        if (!simulate) set(player, magic.withDust(element, have + added));
        return added;
    }

    // Takes dust out (an element at 0 frees its type slot). Returns how much came out.
    public static int removeDust(Player player, DustElement element, int amount, boolean simulate)
    {
        if (amount <= 0) return 0;
        PlayerMagic magic = get(player);
        int have = magic.dustOf(element);
        int removed = Math.min(amount, have);
        if (removed > 0 && !simulate) set(player, magic.withDust(element, have - removed));
        return removed;
    }

    // Sets an element's amount directly (capped at dustCapacity; ignores the type limit: for commands and old code)
    public static void setDust(Player player, DustElement element, int amount)
    {
        PlayerMagic magic = get(player);
        set(player, magic.withDust(element, Math.clamp(amount, 0, magic.dustCapacity())));
    }

    // ---- attunement ----

    public static boolean isAttuned(Player player, DustElement element)
    {
        return get(player).attunements().contains(element);
    }

    // Attunes the player to one more element (up to 3). False if already attuned to it or to three.
    public static boolean attune(Player player, DustElement element)
    {
        PlayerMagic magic = get(player);
        if (magic.attunements().contains(element) || magic.attunements().size() >= MagicConfig.ATTUNEMENT.length - 1) return false;
        List<DustElement> attuned = new ArrayList<>(magic.attunements());
        attuned.add(element);
        set(player, magic.withAttunements(attuned));
        return true;
    }

    public static boolean unattune(Player player, DustElement element)
    {
        PlayerMagic magic = get(player);
        if (!magic.attunements().contains(element)) return false;
        List<DustElement> attuned = new ArrayList<>(magic.attunements());
        attuned.remove(element);
        set(player, magic.withAttunements(attuned));
        return true;
    }

    // [gain bonus, cost reduction, power bonus] for an element: zero unless the player is attuned to it
    private static float[] attunementBonus(Player player, DustElement element)
    {
        List<DustElement> attuned = get(player).attunements();
        if (!attuned.contains(element)) return MagicConfig.ATTUNEMENT[0];
        return MagicConfig.ATTUNEMENT[Math.min(attuned.size(), MagicConfig.ATTUNEMENT.length - 1)];
    }

    // How much dust an orb of this element worth value gives the player (more if attuned: 50% / 20% / 10%)
    public static int orbGain(Player player, DustElement element, int value)
    {
        return Math.round(value * (1 + attunementBonus(player, element)[0]));
    }

    // A spell's dust cost of this element for this player (less if attuned; never below 1 when something's needed)
    public static int dustCost(Player player, DustElement element, int cost)
    {
        if (cost <= 0) return 0;
        return Math.max(1, Math.round(cost * (1 - attunementBonus(player, element)[1])));
    }

    // How much stronger this player's spells of this element are (1.25 for one attunement, 1.1 for two, else 1)
    public static float spellPower(Player player, DustElement element)
    {
        return 1 + attunementBonus(player, element)[2];
    }

    public static boolean isAwakened(Player player)
    {
        return get(player).awakened();
    }

    // ---- ticking: regeneration, and max mana creeping up or down (only once awakened) ----

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        PlayerMagic magic = get(player);
        if (!magic.awakened()) return;
        ManaTimers timers = player.getData(DustAttachmentTypeRegistry.MANA_TIMERS);
        int mana = magic.mana(), max = magic.maxMana();
        // Mana Surge (Magic on yourself): regeneration runs twice as fast, faster still at higher levels
        MobEffectInstance surge = player.getEffect(DustEffectsRegistry.MANA_SURGE);
        int step = surge == null ? 1 : 2 + surge.getAmplifier();
        // A rich chunk's aura (ChunkAura) speeds it up too
        step += AuraEffects.regenBonus(player.level(), player.blockPosition());
        int regen = timers.regen() + step, low = timers.low(), high = timers.high();

        // Regenerates up to the cap (higher while Crystallized)
        if (mana < manaCap(player)) {
            if (regen >= MagicConfig.REGEN_TICKS) {
                mana++;
                regen = 0;
            }
        } else {
            regen = 0;
        }
        float fraction = max <= 0 ? 0 : (float) mana / max;
        low = fraction < MagicConfig.LOW_FRACTION ? low + 1 : 0;
        high = fraction >= MagicConfig.HIGH_FRACTION ? high + 1 : 0;
        if (low >= MagicConfig.LOW_TICKS) {
            max++;
            low = 0;
        }
        if (high >= MagicConfig.HIGH_TICKS) {
            max = Math.max(MagicConfig.MIN_MAX_MANA, max - 1);
            mana = Math.min(mana, max);
            high = 0;
        }
        // The timers aren't synced, so setting them every tick costs nothing; mana only syncs when it changes
        player.setData(DustAttachmentTypeRegistry.MANA_TIMERS, new ManaTimers(regen, low, high));
        if (mana != magic.mana() || max != magic.maxMana()) set(player, magic.withMana(mana, max));
    }
}
