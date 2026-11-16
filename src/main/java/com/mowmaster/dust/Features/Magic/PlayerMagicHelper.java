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
//   Mana        max mana comes from the Magic Level (maxManaFor). It only regenerates once the player has awakened to
//               their magic (Awakening): 1% of max mana every 120 ticks. Sleeping through the night fills it. A spell
//               needing more mana than the player has takes what's there and hurts half a heart per missing point.
//   Magic Level experience on the same curve as spells (SpellLevels): 1 for each level a spell tree gains from their
//               casting (taken back if that spell is broken: MagicCredits), and 1 for each item they fully research
//               (every element in it found and discovered).
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

    // Sets mana, up to the cap (manaCap: higher while Crystallized)
    public static void setMana(Player player, int amount)
    {
        PlayerMagic magic = get(player);
        set(player, magic.withMana(Math.clamp(amount, 0, manaCap(player)), magic.maxMana()));
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

    // ---- Magic Level ----

    public static int magicLevel(Player player)
    {
        return com.mowmaster.dust.Features.Spells.pylons.SpellLevels.levelForXp(get(player).magicXp());
    }

    // Max mana at a Magic Level: START_MAX_MANA, plus (reached level / 10) + 1 for every level reached
    // (levels 1-9: +1 each, 10-19: +2, 20-29: +3...). Level 10: 10 + 9 + 2 = 21.
    public static int maxManaFor(int level)
    {
        int max = MagicConfig.START_MAX_MANA;
        for (int reached = 1; reached <= level; reached++) max += reached / 10 + 1;
        return max;
    }

    // Adds (or with a negative amount, takes away) Magic Level experience; max mana follows the level, and mana over
    // a lowered max is lost
    public static void addMagicXp(Player player, int amount)
    {
        if (amount == 0) return;
        PlayerMagic magic = get(player);
        int xp = Math.max(0, magic.magicXp() + amount);
        set(player, withLevelMana(magic.withMagicXp(xp, magic.workedOut())));
    }

    // The same magic with max mana set from its level (and mana kept within it)
    private static PlayerMagic withLevelMana(PlayerMagic magic)
    {
        int max = maxManaFor(com.mowmaster.dust.Features.Spells.pylons.SpellLevels.levelForXp(magic.magicXp()));
        return magic.withMana(Math.min(magic.mana(), max), max);
    }

    // The player has fully researched this many items in all: a point of experience for each one not counted yet
    public static void noteWorkedOut(Player player, int count)
    {
        PlayerMagic magic = get(player);
        if (count <= magic.workedOut()) return;
        int gained = count - magic.workedOut();
        set(player, withLevelMana(magic.withMagicXp(magic.magicXp() + gained, count)));
    }

    // ---- ticking: regeneration (only once awakened) ----

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        PlayerMagic magic = get(player);
        // Max mana follows the Magic Level (also fixes saves from when max mana crept up and down by itself)
        int levelMax = maxManaFor(com.mowmaster.dust.Features.Spells.pylons.SpellLevels.levelForXp(magic.magicXp()));
        if (magic.maxMana() != levelMax) {
            magic = magic.withMana(Math.min(magic.mana(), levelMax), levelMax);
            set(player, magic);
        }
        if (!magic.awakened()) return;
        ManaTimers timers = player.getData(DustAttachmentTypeRegistry.MANA_TIMERS);
        int mana = magic.mana(), max = magic.maxMana();
        // Mana Surge (Magic on yourself): regeneration runs twice as fast, faster still at higher levels
        MobEffectInstance surge = player.getEffect(DustEffectsRegistry.MANA_SURGE);
        int step = surge == null ? 1 : 2 + surge.getAmplifier();
        // A rich chunk's aura (ChunkAura) speeds it up too
        step += AuraEffects.regenBonus(player.level(), player.blockPosition());
        int regen = timers.regen() + step, fraction = timers.fraction();

        // Regenerates up to the cap (higher while Crystallized): every REGEN_TICKS, REGEN_PERCENT of max mana, kept in
        // hundredths until it makes a whole point
        if (mana < manaCap(player)) {
            while (regen >= MagicConfig.REGEN_TICKS) {
                regen -= MagicConfig.REGEN_TICKS;
                fraction += max * MagicConfig.REGEN_PERCENT;
            }
            while (fraction >= 100 && mana < manaCap(player)) {
                fraction -= 100;
                mana++;
            }
        } else {
            regen = 0;
            fraction = 0;
        }
        // The timers aren't synced, so setting them every tick costs nothing; mana only syncs when it changes
        player.setData(DustAttachmentTypeRegistry.MANA_TIMERS, new ManaTimers(regen, fraction));
        if (mana != magic.mana()) set(player, magic.withMana(mana, max));
    }

    // Sleeping through the night: everyone asleep wakes with full mana (SleepFinishedTimeEvent: the night was skipped,
    // before the sleepers get up)
    @SubscribeEvent
    public static void onNightSkipped(net.neoforged.neoforge.event.level.SleepFinishedTimeEvent event)
    {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        for (ServerPlayer player : level.players()) {
            if (player.isSleeping() && isAwakened(player)) setMana(player, manaCap(player));
        }
    }

    // Experience taken away by spells broken while the player was offline (MagicCredits)
    @SubscribeEvent
    public static void onLogIn(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.getEntity() instanceof ServerPlayer player) MagicCredits.get(player.level().getServer()).applyPending(player);
    }
}
