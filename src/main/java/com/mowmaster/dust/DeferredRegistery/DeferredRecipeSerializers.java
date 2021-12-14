package com.mowmaster.dust.DeferredRegistery;

import com.mowmaster.dust.Recipes.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.mowmaster.dust.References.Constants.MODID;

public final class DeferredRecipeSerializers
{
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

    public static final RegistryObject<RecipeSerializer<?>> CRUSHING = RECIPES.register(MODID + "_crushing", () ->
            CrusherRecipe.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<?>> CLUSTER_FUEL = RECIPES.register(MODID + "_cluster_fuel", () ->
            CrystalClusterFuelRecipe.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<?>> CLUSTER_MODIFIER = RECIPES.register(MODID + "_cluster_modifier", () ->
            CrystalClusterModifiers.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<?>> CLUSTER_FILTER = RECIPES.register(MODID + "_entity_filter", () ->
            BaseBlockEntityFilter.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<?>> MOB_EFFECT_COLOR = RECIPES.register(MODID + "_mobeffect_color", () ->
            MobEffectColorRecipe.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<?>> MOB_EFFECTCORRUPTED_COLOR = RECIPES.register(MODID + "_mobeffectcorrupted_color", () ->
            MobEffectColorRecipeCorrupted.SERIALIZER);
}
