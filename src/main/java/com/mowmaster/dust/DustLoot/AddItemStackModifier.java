package com.mowmaster.dust.DustLoot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustRegistries.DustLootRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.Optional;

// Adds one fixed stack to the loot of every table its condition matches.
// 26.3: a LootModifier has ONE optional condition (Optional<Holder<LootItemCondition>>) instead of an array.
// Combine several with condA.and(condB) (an AllOfCondition) before building. LootModifier.apply() already
// checks the condition before doApply runs, so doApply doesn't test it again.
public class AddItemStackModifier extends LootModifier
{
    public static final MapCodec<AddItemStackModifier> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
            LootModifier.codecStart(instance)
                    .and(ItemStackTemplate.CODEC.fieldOf("stack").forGetter(inst -> inst.itemStack))
                    .apply(instance, AddItemStackModifier::new));
    private final ItemStackTemplate itemStack;

    public AddItemStackModifier(Optional<Holder<LootItemCondition>> condition, int priority, ItemStackTemplate itemStackTemplate) {
        super(condition, priority);
        this.itemStack = itemStackTemplate;
    }

    // Shortcut for datagen: a single condition at the default priority
    public AddItemStackModifier(LootItemCondition condition, ItemStackTemplate itemStackTemplate) {
        this(Optional.of(Holder.direct(condition)), IGlobalLootModifier.DEFAULT_PRIORITY, itemStackTemplate);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return DustLootRegistry.ADD_ITEMSTACK.get();
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(itemStack.create());
        return generatedLoot;
    }
}
