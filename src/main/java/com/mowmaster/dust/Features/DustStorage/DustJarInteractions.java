package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Magic.PlayerMagicHelper;
import com.mowmaster.mowlib.util.LeftClickCooldown;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

// The Dust Jar's clicks:
//   right-click with element dust    pours it in (as much as fits)
//   right-click, empty hand          empties your dust store of the jar's element into it (as much as fits); an
//                                    empty jar with no element takes the first element you carry
//   left-click                       takes 1% of how much of an element you can carry into your dust store (at least 1)
//   sneak + left-click               takes 10% (at least 10)
//   sneak + right-click, empty hand  picks the jar up, dust and all; an empty jar with an element clears it first
//                                    (MowLib's pick-up key picks any jar up straight away, element and all)
// An empty jar left-clicked is mined as normal.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class DustJarInteractions
{
    public static final int CLICK_PERCENT = 1, CLICK_MINIMUM = 1;
    public static final int SNEAK_PERCENT = 10, SNEAK_MINIMUM = 10;

    private DustJarInteractions() {}

    // Element dust items into the jar. True if any went in.
    public static boolean pour(DustJarBlockEntity jar, ItemStack stack, Player player)
    {
        DustElement dust = CrystalElements.ofDust(stack);
        if (dust == null || !jar.accepts(dust)) return false;
        int moved = jar.insertDust(dust, stack.getCount(), false);
        if (moved <= 0) return false;
        stack.consume(moved, player); // creative players keep their stack
        player.level().playSound(null, jar.getBlockPos(), SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 0.6F, 1.4F);
        return true;
    }

    // Takes percent of the player's capacity for one element (at least minimum, as far as the jar holds that much and
    // the store has room) from the jar into their dust store (server)
    public static void takeIntoStore(DustJarBlockEntity jar, Player player, int percent, int minimum)
    {
        DustElement element = jar.element();
        if (element == null || jar.amount() <= 0) return;
        int wanted = Math.max(minimum, PlayerMagicHelper.dustCap(player) * percent / 100);
        int fits = PlayerMagicHelper.addDust(player, element, Math.min(wanted, jar.amount()), true);
        if (fits <= 0) {
            player.sendOverlayMessage(Component.translatable("dust.dust_jar.full_store", element.name()));
            return;
        }
        int taken = jar.extract(fits);
        PlayerMagicHelper.addDust(player, element, taken, false);
        player.level().playSound(null, jar.getBlockPos(), SoundEvents.SAND_BREAK, SoundSource.BLOCKS, 0.6F, 1.4F);
        showContents(jar, player);
    }

    // The player's dust of the jar's element into the jar, as much as fits (server). A jar with no element takes the
    // first element the player carries (the dust store keeps the order they were picked up in).
    public static void storeFromPlayer(DustJarBlockEntity jar, Player player)
    {
        DustElement element = jar.element();
        if (element == null) element = PlayerMagicHelper.get(player).dust().keySet().stream().findFirst().orElse(null);
        if (element == null || PlayerMagicHelper.dust(player, element) <= 0) {
            player.sendOverlayMessage(element == null ? Component.translatable("dust.dust_jar.nothing_carried")
                    : Component.translatable("dust.dust_jar.none_carried", element.name()));
            return;
        }
        int fits = jar.insertDust(element, PlayerMagicHelper.dust(player, element), true);
        if (fits <= 0) {
            showContents(jar, player);
            return;
        }
        int moved = PlayerMagicHelper.removeDust(player, element, fits, false);
        jar.insertDust(element, moved, false);
        player.level().playSound(null, jar.getBlockPos(), SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 0.6F, 1.4F);
        showContents(jar, player);
    }

    // "Fire: 512 / 1000" over the hotbar
    public static void showContents(DustJarBlockEntity jar, Player player)
    {
        Component text = jar.element() == null
                ? Component.translatable("dust.dust_jar.empty")
                : Component.translatable("dust.dust_jar.contents", jar.element().name(), jar.amount(), DustJarContents.CAPACITY);
        player.sendOverlayMessage(text);
    }

    // Left-click: the event fires on both sides; cancelling it (when the jar has dust) stops the jar being mined
    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.LeftClickBlock event)
    {
        if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START) return;
        Player player = event.getEntity();
        Level level = player.level();
        if (!(level.getBlockEntity(event.getPos()) instanceof DustJarBlockEntity jar) || jar.amount() <= 0) return;
        event.setCanceled(true);
        if (level.isClientSide() || !LeftClickCooldown.ready(player, level)) return;
        if (player.isShiftKeyDown()) takeIntoStore(jar, player, SNEAK_PERCENT, SNEAK_MINIMUM);
        else takeIntoStore(jar, player, CLICK_PERCENT, CLICK_MINIMUM);
    }
}
