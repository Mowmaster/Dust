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
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddRandomItemStackModifier extends LootModifier
{
    public static final MapCodec<AddRandomItemStackModifier> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    LootItemCondition.DIRECT_CODEC.listOf()
                            .fieldOf("conditions")
                            .forGetter(modifier -> List.of(modifier.conditions)),

                    BuiltInRegistries.ITEM.holderByNameCodec()
                            .fieldOf("item")
                            .forGetter(modifier -> modifier.item),

                    Codec.INT.fieldOf("min_count")
                            .forGetter(modifier -> modifier.minCount),

                    Codec.INT.fieldOf("max_count")
                            .forGetter(modifier -> modifier.maxCount)

            ).apply(instance, (conditions, item, minCount, maxCount) ->
                    new AddRandomItemStackModifier(
                            conditions.toArray(new LootItemCondition[0]),
                            item,
                            minCount,
                            maxCount
                    )));

                    /*.apply(instance,
                    (lootItemConditions, item, minCount, maxCount) -> new AddRandomItemStackModifier(lootItemConditions, item, minCount, maxCount)));*/

    private final Holder<Item> item;
    private final int minCount;
    private final int maxCount;

    public AddRandomItemStackModifier(
            LootItemCondition[] conditions,
            Holder<Item> item,
            int minCount,
            int maxCount
    ) {
        super(conditions, 1000);
        this.item = item;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
    {
        int count = context.getRandom().nextInt(maxCount - minCount + 1) + minCount;
        generatedLoot.add(new ItemStack(item.value(), count));
        return generatedLoot;
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
