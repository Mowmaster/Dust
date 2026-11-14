package com.mowmaster.dust.DustRegistries;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.AncientInscribeRecipe;
import com.mowmaster.dust.Features.DustComposition.DustRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DustRecipeRegistry
{
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, DustReferences.MODID);

    // The recipe has no settings, so both codecs just produce the single instance
    public static final Supplier<RecipeSerializer<AncientInscribeRecipe>> ANCIENT_INSCRIBE = RECIPE_SERIALIZERS.register("ancient_inscribe",
            () -> new RecipeSerializer<>(MapCodec.unit(AncientInscribeRecipe.INSTANCE), StreamCodec.unit(AncientInscribeRecipe.INSTANCE)));

    // Dust recipes: ingredients by the dust in them, and a research entry the crafter needs (Features/DustComposition)
    public static final Supplier<RecipeSerializer<DustRecipe>> DUST_CRAFTING = RECIPE_SERIALIZERS.register("dust_crafting",
            () -> new RecipeSerializer<>(DustRecipe.MAP_CODEC, DustRecipe.STREAM_CODEC));

    // The Element Crafter's recipes (Features/Crafting): their own recipe type, so the vanilla table never uses them
    public static final Supplier<RecipeSerializer<com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe>> ELEMENT_CRAFTING = RECIPE_SERIALIZERS.register("element_crafting",
            () -> new RecipeSerializer<>(com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe.MAP_CODEC, com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe.STREAM_CODEC));

    // Recipe types: what a recipe is for. RecipeType.simple names one by its id.
    public static final DeferredRegister<net.minecraft.world.item.crafting.RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, DustReferences.MODID);
    public static final Supplier<net.minecraft.world.item.crafting.RecipeType<com.mowmaster.dust.Features.Crafting.ElementCraftingRecipe>> ELEMENT_CRAFTING_TYPE =
            RECIPE_TYPES.register("element_crafting", id -> net.minecraft.world.item.crafting.RecipeType.simple(id));

    public static void register(IEventBus eventBus)
    {
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}
