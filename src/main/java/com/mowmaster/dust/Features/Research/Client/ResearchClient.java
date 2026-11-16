package com.mowmaster.dust.Features.Research.Client;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Machines.AlchemicalDust;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import com.mowmaster.dust.Features.Research.ResearchBookItem;
import com.mowmaster.dust.Features.Research.ResearchData;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import com.mowmaster.dust.Features.Research.ResearchLook;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

// Client helpers for the research book
public final class ResearchClient
{
    private ResearchClient() {}

    // Opens the book screen. With an entry: straight to it if it's been discovered, otherwise to its tab at the page
    // it's on. Without: the main page of the tab that was open last.
    public static void openBook(ItemStack book, @Nullable ResearchEntry entry)
    {
        if (!(book.getItem() instanceof ResearchBookItem item)) return;
        ResearchBook.show(dataFor(book), entry);
        // 26.3: screens are opened through the Gui (Minecraft.gui), no longer Minecraft.setScreen
        Minecraft.getInstance().gui.setScreen(new ResearchBookScreen(book.copy(), item.isShared()));
    }

    // Sneak + right-click / the activation key: the entry for what the player is looking at (or the main page).
    // A dropped element crystal, element dust or Alchemical Dust opens its element's page instead of its own.
    public static void openAtLookedAt(ItemStack book)
    {
        Minecraft minecraft = Minecraft.getInstance();
        ResearchLook.Target target = minecraft.player == null ? null : ResearchLook.lookedAt(minecraft.player);
        ResearchEntry entry = target == null ? null : target.entry();
        if (target != null && target.entity() instanceof ItemEntity dropped) {
            DustElement element = elementOf(dropped.getItem());
            if (element != null) entry = new ResearchEntry(ResearchTabs.ELEMENTS, element.id());
        }
        openBook(book, entry);
    }

    // The one element a crystal, dust or Alchemical Dust is, or null for anything else
    private static @Nullable DustElement elementOf(ItemStack stack)
    {
        DustElement element = AlchemicalDust.of(stack);
        if (element == null) element = CrystalElements.of(stack);
        if (element == null) element = CrystalElements.ofDust(stack);
        return element;
    }

    // MowLib's page keys while the book is held (the side view)
    public static void turnPage(ItemStack book, int direction)
    {
        ResearchBook.turnPage(dataFor(book), direction);
    }

    // What a book shows: a guide shows the copy stored on it, Research Notes show your own (synced) research
    public static ResearchData dataFor(ItemStack book)
    {
        if (book.getItem() instanceof ResearchBookItem item && item.isShared()) {
            return book.getOrDefault(DustComponentDataRegistry.RESEARCH_SNAPSHOT.get(), ResearchData.EMPTY);
        }
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.player == null ? ResearchData.EMPTY : Research.get(minecraft.player);
    }

    // ---- mobs to draw on the entry page ----

    // One temporary mob per type, made on the client and never added to the world (just something to draw).
    // Cleared when the level changes (the mobs belong to a level).
    private static final Map<EntityType<?>, LivingEntity> DISPLAY = new HashMap<>();
    private static @Nullable Object displayLevel;

    public static @Nullable LivingEntity displayEntity(ResearchEntry entry)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return null;
        if (displayLevel != minecraft.level) {
            DISPLAY.clear();
            displayLevel = minecraft.level;
        }
        // Only tabs that show a mob (Creatures, Bosses) give an entity type
        EntityType<?> type = entry.tab().displayEntity(entry.id());
        if (type == null) return null;
        if (!DISPLAY.containsKey(type)) {
            Entity made = null;
            try {
                made = type.create(minecraft.level, EntitySpawnReason.LOAD);
            } catch (RuntimeException ignored) {
                // A mob that can't be made on its own: show its egg instead
            }
            DISPLAY.put(type, made instanceof LivingEntity living ? living : null);
        }
        return DISPLAY.get(type);
    }
}
