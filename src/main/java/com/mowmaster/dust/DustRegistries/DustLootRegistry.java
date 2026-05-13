package com.mowmaster.dust.DustRegistries;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustLoot.AddItemStackModifier;
import com.mowmaster.dust.DustReferences;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class DustLootRegistry
{
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, DustReferences.MODID);

    public static final Supplier<MapCodec<AddItemStackModifier>> ADD_ITEMSTACK =
            LOOT_MODIFIERS.register("add_itemstack", () -> AddItemStackModifier.CODEC);


    public static void register(IEventBus eventBus) {
        LOOT_MODIFIERS.register(eventBus);
    }
}
