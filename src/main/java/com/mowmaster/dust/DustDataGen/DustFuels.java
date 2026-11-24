package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.Features.Trees.Charcoal;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootPredicates;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConditionalValue;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

// 26.3: an item's furnace burn time is a "context int provider" entry it names (Item.Properties.cookingFuel). Vanilla
// writes each as its burn time divided by vanilla's burn time factors (1, or 2 in a fast furnace: a blast furnace or
// smoker), and the charcoals' entries (Charcoal.fuelKey: dust:cooking/time_<colour>_charcoal) are written the same way.
// Registered from DustDataGenerator; 26.1.2 has the furnace fuel data map instead
// (src/main/resources-26.1.2/data/neoforge/data_maps/item/furnace_fuels.json).
public final class DustFuels
{
    private DustFuels() {}

    public static void bootstrap(BootstrapContext<ContextIntProvider> context)
    {
        HolderGetter<LootItemCondition> predicates = context.lookup(Registries.PREDICATE);
        HolderGetter<ContextIntProvider> providers = context.lookup(Registries.CONTEXT_INT_PROVIDER);
        Holder<ContextIntProvider> normal = providers.getOrThrow(ContextIntProviders.COOKING_NORMAL_BURN_TIME_REDUCTION_FACTOR);
        Holder<ContextIntProvider> fast = providers.getOrThrow(ContextIntProviders.COOKING_FAST_BURN_TIME_REDUCTION_FACTOR);
        for (Charcoal charcoal : Charcoal.values()) {
            ContextIntProvider divisor = new ConditionalValue(predicates.getOrThrow(LootPredicates.FAST_FURNACE), fast, normal);
            context.register(charcoal.fuelKey(), ContextIntProviders.div(ContextIntProviders.exactly(charcoal.burnTime), Holder.direct(divisor)).value());
        }
    }
}
