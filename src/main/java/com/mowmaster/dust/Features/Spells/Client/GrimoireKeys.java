package com.mowmaster.dust.Features.Spells.Client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Spells.grimoire.GrimoireItem;
import com.mowmaster.dust.Features.Spells.grimoire.GrimoirePayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

// The grimoire keys (Options > Controls > Key Binds, under "Dust: Grimoire"). They work with a Grimoire anywhere in
// your inventory (GrimoireItem.find):
//   cast spell (R)                 cast the selected spell
//   next / previous spell (Z / -)  turn the book to the next / previous spell, named over the hotbar
//   cast spell 1-9 (unbound)       cast that page's spell directly, for those who want a key per spell
// Each press is sent to the server (GrimoirePayload), which does the work.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class GrimoireKeys
{
    // Since 1.21.9 a key's category is a KeyMapping.Category (an id), registered with the keys
    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(DustReferences.MODID, "grimoire"));

    public static final KeyMapping CAST = new KeyMapping("key.dust.grimoire_cast", InputConstants.KEY_R, CATEGORY);
    public static final KeyMapping NEXT = new KeyMapping("key.dust.grimoire_next", InputConstants.KEY_Z, CATEGORY);
    public static final KeyMapping PREVIOUS = new KeyMapping("key.dust.grimoire_previous", InputConstants.UNKNOWN.getValue(), CATEGORY);
    public static final KeyMapping[] PAGES = new KeyMapping[GrimoireItem.PAGES];

    static {
        for (int i = 0; i < PAGES.length; i++) {
            PAGES[i] = new KeyMapping("key.dust.grimoire_page_" + (i + 1), InputConstants.UNKNOWN.getValue(), CATEGORY);
        }
    }

    private GrimoireKeys() {}

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event)
    {
        event.registerCategory(CATEGORY);
        event.register(CAST);
        event.register(NEXT);
        event.register(PREVIOUS);
        for (KeyMapping page : PAGES) event.register(page);
    }

    // Key presses are counted up and read once a tick (consumeClick is true once per press). Only in the world, with a
    // grimoire to use.
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.gui.screen() != null) return;
        boolean has = !GrimoireItem.find(minecraft.player).isEmpty();
        while (CAST.consumeClick()) if (has) send(GrimoirePayload.CAST, GrimoirePayload.SELECTED);
        while (NEXT.consumeClick()) if (has) send(GrimoirePayload.TURN, 1);
        while (PREVIOUS.consumeClick()) if (has) send(GrimoirePayload.TURN, -1);
        for (int i = 0; i < PAGES.length; i++) {
            while (PAGES[i].consumeClick()) if (has) send(GrimoirePayload.CAST, i);
        }
    }

    private static void send(int action, int value)
    {
        ClientPacketDistributor.sendToServer(new GrimoirePayload(action, value));
    }
}
