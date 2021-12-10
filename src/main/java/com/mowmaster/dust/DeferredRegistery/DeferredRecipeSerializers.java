package com.mowmaster.dust.DeferredRegistery;

import com.mowmaster.dust.Recipes.CrusherRecipe;
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
}
