package com.mowmaster.dust.crafting;

import com.mowmaster.dust.item.ItemColorDust;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider
{
    public Recipes(DataGenerator genIn)
    {
        super(genIn);
    }


    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {

        ShapelessRecipeBuilder.shapelessRecipe(ItemColorDust.DUST)
                .addIngredient(Items.STICK)
                .addIngredient(Items.RED_DYE)
                .build(consumer);
    }
}
