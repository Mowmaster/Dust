package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.Features.AncientLanguage.AncientLanguage;
import com.mowmaster.dust.Features.Machines.Compounds;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.objects.AtlasSprite;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

import java.util.function.Predicate;

// How an element is named wherever it's listed (tooltips, the book, jars, the Element Crafter, pylons):
//   discovered      its icon and its name: [icon] Fire
//   not yet         its name in the ancient script, no icon (letters the reader knows show plainly: AncientLanguage)
// The icon is an "object" text component: a sprite from the GUI atlas drawn inline like a letter (the element icons
// are GUI sprites, textures/gui/sprites/element/). The text colour tints an object, so it's given white: the icon shows
// in its own colours.
public final class ElementNames
{
    // Whether the local player has discovered an element, for text made without a player to hand (item names). Set by
    // the client (DustClient); on a server it stays "no" for nothing, so plain names are used.
    private static @Nullable Predicate<DustElement> clientKnows;

    private ElementNames() {}

    public static void setClientKnows(Predicate<DustElement> knows)
    {
        clientKnows = knows;
    }

    // The element's icon as a piece of text, in its colour
    public static MutableComponent icon(DustElement element)
    {
        return Component.object(new AtlasSprite(AtlasIds.GUI, element.icon())).withColor(0xFFFFFF);
    }

    // [icon] Name, or the ancient name
    public static MutableComponent named(DustElement element, boolean known)
    {
        if (!known) return AncientLanguage.inscribe(element.name());
        return Component.empty().append(icon(element)).append(" ").append(element.name());
    }

    // As this player knows it (no player: plain, with its icon)
    public static MutableComponent forViewer(DustElement element, @Nullable Player viewer)
    {
        return named(element, viewer == null || Compounds.knows(viewer, element));
    }

    // Just the name (no icon), as the local player knows it: for item names
    public static Component plainForClient(DustElement element)
    {
        Predicate<DustElement> knows = clientKnows;
        return knows == null || knows.test(element) ? element.name() : AncientLanguage.inscribe(element.name());
    }

    // [icon] Name as the local player knows it (on a server: known)
    public static MutableComponent forClient(DustElement element)
    {
        Predicate<DustElement> knows = clientKnows;
        return named(element, knows == null || knows.test(element));
    }
}
