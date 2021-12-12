package com.mowmaster.dust.Recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import static com.mowmaster.dust.References.Constants.MODID;

public class CrystalClusterFuelRecipe implements Recipe<Container>
{
    @ObjectHolder(MODID + ":cluster_fuel")
    public static RecipeSerializer<?> SERIALIZER = new Serializer();

    public static RecipeType<CrystalClusterFuelRecipe> CRYSTALCLUSTERFUEL = RecipeType.register(MODID + ":cluster_fuel");

    private final String group;
    private final ResourceLocation id;
    @Nullable
    private final Ingredient input;
    private final int fuelReturn;
    private final int potencyLimit;

    public CrystalClusterFuelRecipe(ResourceLocation id, String group, @Nullable Ingredient input, int fuelReturn, int potencyLimit)
    {
        this.group = group;
        this.id = id;
        this.input = input;
        this.fuelReturn = fuelReturn;
        this.potencyLimit = potencyLimit;
    }

    public static Collection<CrystalClusterFuelRecipe> getAllRecipes(Level world)
    {
        return world.getRecipeManager().getAllRecipesFor(CRYSTALCLUSTERFUEL);
    }

    @Override
    public String getGroup()
    {
        return group;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(Items.BARRIER);
    }

    @Override
    public NonNullList<Ingredient> getIngredients()
    {
        NonNullList<Ingredient> allIngredients = NonNullList.create();
        allIngredients.add(input != null ? input : Ingredient.EMPTY);
        return allIngredients;
    }

    @Override
    public boolean matches(Container inv, Level worldIn)
    {
        ItemStack inputStack = inv.getItem(0);
        return input.test(inputStack);
    }

    @Override
    public ItemStack assemble(Container inv)
    {
        return getResultItem().copy();
    }

    public int getResultFuelValue()
    {
        return getFuelValue();
    }


    public int getResultPotencyCap()
    {
        return getPotencyCap();
    }

    @Override
    public ResourceLocation getId()
    {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType()
    {
        return CRYSTALCLUSTERFUEL;
    }

    @Override
    public ItemStack getToastSymbol()
    {
        return new ItemStack(Items.BLAZE_POWDER.asItem());
    }

    public int getFuelValue()
    {
        return fuelReturn >= 0 ? fuelReturn : 0;
    }

    public int getPotencyCap()
    {
        return potencyLimit >= 0 ? potencyLimit : 0;
    }

    public Ingredient getPattern()
    {
        return input != null ? input : Ingredient.EMPTY;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<CrystalClusterFuelRecipe>
    {
        protected CrystalClusterFuelRecipe createRecipe(ResourceLocation recipeId, String group, Ingredient input, int fuelReturn, int potencyCap)
        {
            return new CrystalClusterFuelRecipe(recipeId, group, input, fuelReturn, potencyCap);
        }

        @Override
        public CrystalClusterFuelRecipe fromJson(ResourceLocation recipeId, JsonObject json)
        {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient input = json.has("input") ? CraftingHelper.getIngredient(json.get("input")) : null;
            int fuelReturn = json.has("fuelReturn") ? GsonHelper.getAsInt(json,"fuelReturn") : 0;
            int potencyCap = json.has("potencyCap") ? GsonHelper.getAsInt(json,"potencyCap") : 0;
            return createRecipe(recipeId, group, input, fuelReturn, potencyCap);
        }

        @Override
        public CrystalClusterFuelRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            String group = buffer.readUtf(32767);
            boolean hasInput = buffer.readBoolean();
            Ingredient input = hasInput ? Ingredient.fromNetwork(buffer) : null;
            int fuelReturn = buffer.readInt();
            int potencyCap = buffer.readInt();
            return createRecipe(recipeId, group,  input, fuelReturn, potencyCap);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrystalClusterFuelRecipe recipe)
        {
            buffer.writeUtf(recipe.group);
            boolean hasInput = recipe.input != null;
            buffer.writeBoolean(hasInput);
            if (hasInput) recipe.input.toNetwork(buffer);
            buffer.writeInt(recipe.fuelReturn);
            buffer.writeInt(recipe.potencyLimit);
        }
    }
}
