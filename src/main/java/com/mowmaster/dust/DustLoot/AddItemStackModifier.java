package com.mowmaster.dust.DustLoot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class AddItemStackModifier extends LootModifier
{
    public static final MapCodec<AddItemStackModifier> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
            LootModifier.codecStart(instance)
                    .and(ItemStackTemplate.CODEC.fieldOf("stack").forGetter(inst -> inst.itemStack))
                    .apply(instance, (lootItemConditions, integer, itemStackTemplate) -> new AddItemStackModifier(lootItemConditions, itemStackTemplate)));
    private final ItemStackTemplate itemStack;

    public AddItemStackModifier(LootItemCondition[] conditionsIn, ItemStackTemplate itemStackTemplate) {
        super(conditionsIn, 1000);
        this.itemStack = itemStackTemplate;
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // generatedLoot is all the loot being generated
        for(LootItemCondition condition : this.conditions) {
            if(!condition.test(context)) {
                return generatedLoot;
            }
        }

        //if our condition passes we add our single item stack to the total loot being dropped
        generatedLoot.add(itemStack.create());
        return generatedLoot;
    }
}
