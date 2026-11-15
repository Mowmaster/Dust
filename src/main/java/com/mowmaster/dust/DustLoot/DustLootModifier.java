package com.mowmaster.dust.DustLoot;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.Optional;

// Base class for Dust's loot modifiers, so the version difference lives in this one file.
// 26.3: a LootModifier has ONE optional condition (Optional<Holder<LootItemCondition>>), saved as "condition".
// 26.1.2: it has an array of conditions (LootItemCondition[]), saved as "conditions".
// The loot modifier jsons in src/generated are made by 26.3's datagen and used by both versions, so on 26.1.2 this
// class reads the 26.3 "condition" field itself and hands NeoForge a one-entry array. The subclasses' constructors and
// codecs are the same on both versions.
public abstract class DustLootModifier extends LootModifier
{
    // The condition as the json had it; the codec writes it back from here
    protected final Optional<Holder<LootItemCondition>> dustCondition;

    protected DustLootModifier(Optional<Holder<LootItemCondition>> condition, int priority)
    {
        //? if >=26.3 {
        super(condition, priority);
        //?} else
        //super(condition.map(holder -> new LootItemCondition[]{holder.value()}).orElseGet(() -> new LootItemCondition[0]), priority);
        this.dustCondition = condition;
    }

    // Use this instead of LootModifier.codecStart: the "condition" and "priority" fields, in the 26.3 format on both versions.
    // Products.P2 is DataFixerUpper's "two fields so far" step of a RecordCodecBuilder; .and(...) adds more fields.
    protected static <T extends DustLootModifier> Products.P2<RecordCodecBuilder.Mu<T>, Optional<Holder<LootItemCondition>>, Integer> dustCodecStart(RecordCodecBuilder.Instance<T> instance)
    {
        return instance.group(
                LootItemCondition.CODEC.optionalFieldOf("condition").forGetter(modifier -> modifier.dustCondition),
                Codec.INT.optionalFieldOf("priority", 1000).forGetter(modifier -> modifier.priority));
    }
}
