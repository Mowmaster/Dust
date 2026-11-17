package com.mowmaster.dust.Features.Guide;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

// An item in a guide scene: written as just its id ("minecraft:paper"), or {"id": ..., "count": 3, "components": {...}}
// (components as in /give, e.g. {"dust:alchemical_element": "dust:fire"}).
// The ItemStack is only made when it's drawn (stack()): scene files load with the client's resources, before items can
// be made into stacks. An unknown id (a mod that isn't installed) draws as nothing.
public record GuideItem(Identifier id, int count, DataComponentPatch components)
{
    public GuideItem(Item item, int count)
    {
        this(BuiltInRegistries.ITEM.getKey(item), count, DataComponentPatch.EMPTY);
    }

    private static final Codec<GuideItem> FULL = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("id").forGetter(GuideItem::id),
            Codec.INT.optionalFieldOf("count", 1).forGetter(GuideItem::count),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(GuideItem::components)
    ).apply(instance, GuideItem::new));

    public static final Codec<GuideItem> CODEC = Codec.either(Identifier.CODEC, FULL)
            .xmap(either -> either.map(id -> new GuideItem(id, 1, DataComponentPatch.EMPTY), item -> item), Either::right);

    public ItemStack stack()
    {
        Item item = BuiltInRegistries.ITEM.getValue(id);
        if (item == null || item == Items.AIR) return ItemStack.EMPTY;
        return new ItemStack(BuiltInRegistries.ITEM.wrapAsHolder(item), Math.max(1, count), components);
    }
}
