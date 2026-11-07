package com.mowmaster.dust.DustLoot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustRegistries.DustLootRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.Optional;

// Adds between min_count and max_count of an item to the loot of every table its condition matches.
public class AddRandomItemStackModifier extends LootModifier
{
    public static final MapCodec<AddRandomItemStackModifier> CODEC =
            RecordCodecBuilder.mapCodec(instance -> LootModifier.codecStart(instance)
                    .and(BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item").forGetter(modifier -> modifier.item))
                    .and(Codec.INT.fieldOf("min_count").forGetter(modifier -> modifier.minCount))
                    .and(Codec.INT.fieldOf("max_count").forGetter(modifier -> modifier.maxCount))
                    .apply(instance, AddRandomItemStackModifier::new));

    private final Holder<Item> item;
    private final int minCount;
    private final int maxCount;

    public AddRandomItemStackModifier(Optional<Holder<LootItemCondition>> condition, int priority,
                                      Holder<Item> item, int minCount, int maxCount) {
        super(condition, priority);
        this.item = item;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    // Shortcut for datagen: a single condition at the default priority
    public AddRandomItemStackModifier(LootItemCondition condition, Holder<Item> item, int minCount, int maxCount) {
        this(Optional.of(Holder.direct(condition)), IGlobalLootModifier.DEFAULT_PRIORITY, item, minCount, maxCount);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
    {
        int min = Math.min(minCount, maxCount);
        int max = Math.max(minCount, maxCount);
        int count = context.getRandom().nextInt(max - min + 1) + min;
        if (count > 0) {
            generatedLoot.add(new ItemStack(item, count));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return DustLootRegistry.ADD_RANDOM_ITEM_STACK.get();
    }
}
