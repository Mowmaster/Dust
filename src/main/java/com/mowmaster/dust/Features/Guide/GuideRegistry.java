package com.mowmaster.dust.Features.Guide;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Every guide scene the client knows (client only: guides are shown, never sent):
//   from code       registered once at startup (RegisterGuidesEvent)
//   from resources  assets/<namespace>/guides/**.json (GuideLoader), loaded again on every resource reload (F3+T).
//                   A file with the same id as a code scene replaces it, so a resource pack can rewrite any scene.
public final class GuideRegistry
{
    // A scene and its id
    public record Entry(Identifier id, GuideScene scene) {}

    private static final Map<Identifier, GuideScene> CODE = new LinkedHashMap<>();
    private static Map<Identifier, GuideScene> resources = Map.of();
    // Item -> its scenes, in order (rebuilt when either changes)
    private static Map<Item, List<Entry>> byItem = Map.of();

    private GuideRegistry() {}

    // A scene made in code (RegisterGuidesEvent.register)
    static void registerCode(Identifier id, GuideScene scene)
    {
        CODE.put(id, scene);
        rebuild();
    }

    // The scenes read from resources (GuideLoader)
    static void setResources(Map<Identifier, GuideScene> loaded)
    {
        resources = Map.copyOf(loaded);
        rebuild();
    }

    private static void rebuild()
    {
        Map<Identifier, GuideScene> all = new LinkedHashMap<>(CODE);
        all.putAll(resources);
        Map<Item, List<Entry>> items = new HashMap<>();
        all.forEach((id, scene) -> {
            for (Identifier itemId : scene.items()) {
                // An item of a mod that isn't installed: skipped
                Item item = BuiltInRegistries.ITEM.getValue(itemId);
                if (item == null || item == Items.AIR) continue;
                items.computeIfAbsent(item, key -> new ArrayList<>()).add(new Entry(id, scene));
            }
        });
        items.values().forEach(list -> list.sort(Comparator.comparingInt((Entry entry) -> entry.scene().order()).thenComparing(entry -> entry.id().toString())));
        byItem = items;
    }

    // The scenes for this item, in order (empty if none)
    public static List<Entry> scenesFor(Item item)
    {
        return byItem.getOrDefault(item, List.of());
    }

    public static boolean has(Item item)
    {
        return byItem.containsKey(item);
    }
}
