package com.mowmaster.dust.Features.Bosses;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.function.Predicate;

// What the Archon trinkets (TrinketItem) do while carried anywhere in a player's inventory. PlayerMagicHelper asks for
// the power, cost and regeneration; SpellCasting for a free cast.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class Trinkets
{
    // A Sigil: its element's spells this much stronger and their dust cost this much, and that element's harm this much
    public static final float SIGIL_POWER = 1.15F, SIGIL_COST = 0.9F, SIGIL_WARD = 0.75F;
    // Prism of the Archons: every spell this much stronger
    public static final float PRISM_POWER = 1.1F;
    // Void Locket: one cast in this many is free
    public static final int LOCKET_ODDS = 10;

    private Trinkets() {}

    private static boolean carries(Player player, Predicate<TrinketItem> which)
    {
        return player.getInventory().contains(stack -> stack.getItem() instanceof TrinketItem trinket && which.test(trinket));
    }

    public static boolean has(Player player, TrinketItem.Kind kind)
    {
        return carries(player, trinket -> trinket.kind() == kind);
    }

    public static boolean hasSigil(Player player, DustElement element)
    {
        return carries(player, trinket -> trinket.kind() == TrinketItem.Kind.SIGIL && trinket.set() != null && trinket.set().element().equals(element));
    }

    public static float powerFactor(Player player, DustElement element)
    {
        float factor = 1.0F;
        if (hasSigil(player, element)) factor *= SIGIL_POWER;
        if (has(player, TrinketItem.Kind.PRISM)) factor *= PRISM_POWER;
        return factor;
    }

    public static float costFactor(Player player, DustElement element)
    {
        return hasSigil(player, element) ? SIGIL_COST : 1.0F;
    }

    // Heart of Mana: an extra regeneration step every other tick
    public static int regenBonus(Player player)
    {
        return player.tickCount % 2 == 0 && has(player, TrinketItem.Kind.MANA_HEART) ? 1 : 0;
    }

    public static boolean freeCast(Player player)
    {
        return has(player, TrinketItem.Kind.VOID_LOCKET) && player.getRandom().nextInt(LOCKET_ODDS) == 0;
    }

    // A Sigil wards its carrier against its element (its spells, and burning for Fire, drowning for Water...: BossDamage.elementOf)
    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event)
    {
        if (!(event.getEntity() instanceof Player player)) return;
        DustElement element = BossDamage.elementOf(event.getSource());
        if (element != null && hasSigil(player, element)) event.setAmount(event.getAmount() * SIGIL_WARD);
    }
}
