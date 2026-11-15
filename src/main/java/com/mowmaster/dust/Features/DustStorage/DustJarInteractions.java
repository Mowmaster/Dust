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
//   right-click, empty hand          says what's in it
//   left-click                       takes 1% of how much of an element you can carry into your dust store
//   sneak + right-click, empty hand  takes 10%; an empty jar clears its element instead, and a plain empty jar is
//                                    picked up (MowLib's pick-up key picks any jar up, contents and all)
// An empty jar left-clicked is mined as normal.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class DustJarInteractions
{
    public static final int CLICK_PERCENT = 1;
    public static final int SNEAK_PERCENT = 10;

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

    // Takes percent of the player's capacity for one element (at least 1) from the jar into their dust store (server)
    public static void takeIntoStore(DustJarBlockEntity jar, Player player, int percent)
    {
        DustElement element = jar.element();
        if (element == null || jar.amount() <= 0) return;
        int wanted = Math.max(1, PlayerMagicHelper.dustCap(player) * percent / 100);
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
        takeIntoStore(jar, player, CLICK_PERCENT);
    }
}
