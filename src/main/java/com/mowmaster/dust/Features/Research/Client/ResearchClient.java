package com.mowmaster.dust.Features.Research.Client;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchBookItem;
import com.mowmaster.dust.Features.Research.ResearchData;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

// Client helpers for the research book
public final class ResearchClient
{
    private ResearchClient() {}

    public static void openBook(ItemStack book)
    {
        if (book.getItem() instanceof ResearchBookItem item) {
            // 26.3: screens are opened through the Gui (Minecraft.gui), no longer Minecraft.setScreen
            Minecraft.getInstance().gui.setScreen(new ResearchBookScreen(book.copy(), item.isShared()));
        }
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
        EntityType<?> type = entry.entityType();
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
