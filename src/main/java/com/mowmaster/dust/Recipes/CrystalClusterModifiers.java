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

public class CrystalClusterModifiers implements Recipe<Container>
{
    @ObjectHolder(MODID + ":cluster_modifier")
    public static RecipeSerializer<?> SERIALIZER = new Serializer();

    public static RecipeType<CrystalClusterModifiers> CRYSTALCLUSTERMODIFIER = RecipeType.register(MODID + ":cluster_modifier");

    private final String group;
    private final ResourceLocation id;
    @Nullable
    private final Ingredient input;
    private final double durationModifier;
    private final double potencyModifier;

    public CrystalClusterModifiers(ResourceLocation id, String group, @Nullable Ingredient input, double durationModifier, double potencyModifier)
    {
        this.group = group;
        this.id = id;
        this.input = input;
        this.durationModifier = durationModifier;
        this.potencyModifier = potencyModifier;
    }

    public static Collection<CrystalClusterModifiers> getAllRecipes(Level world)
    {
        return world.getRecipeManager().getAllRecipesFor(CRYSTALCLUSTERMODIFIER);
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

    public double getResultDurationModifier()
    {
        return getDurationModifier();
    }


    public double getResultPotencyModifier()
    {
        return getPotencyModifier();
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
        return CRYSTALCLUSTERMODIFIER;
    }

    @Override
    public ItemStack getToastSymbol()
    {
        return new ItemStack(Items.NETHER_STAR.asItem());
    }

    //Can Be Negative
    public double getDurationModifier()
    {
        return durationModifier;
    }

    //Can Be Negative
    public double getPotencyModifier()
    {
        return potencyModifier;
    }

    public Ingredient getPattern()
    {
        return input != null ? input : Ingredient.EMPTY;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<CrystalClusterModifiers>
    {
        protected CrystalClusterModifiers createRecipe(ResourceLocation recipeId, String group, Ingredient input, double durationModifier, double potencyCap)
        {
            return new CrystalClusterModifiers(recipeId, group, input, durationModifier, potencyCap);
        }

        @Override
        public CrystalClusterModifiers fromJson(ResourceLocation recipeId, JsonObject json)
        {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient input = json.has("input") ? CraftingHelper.getIngredient(json.get("input")) : null;
            double durationModifier = json.has("durationModifier") ? GsonHelper.getAsDouble(json,"durationModifier") : 1;
            double potencyCap = json.has("potencyModifier") ? GsonHelper.getAsDouble(json,"potencyModifier") : 1;
            return createRecipe(recipeId, group, input, durationModifier, potencyCap);
        }

        @Override
        public CrystalClusterModifiers fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            String group = buffer.readUtf(32767);
            boolean hasInput = buffer.readBoolean();
            Ingredient input = hasInput ? Ingredient.fromNetwork(buffer) : null;
            double durationModifier = buffer.readDouble();
            double potencyCap = buffer.readDouble();
            return createRecipe(recipeId, group,  input, durationModifier, potencyCap);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrystalClusterModifiers recipe)
        {
            buffer.writeUtf(recipe.group);
            boolean hasInput = recipe.input != null;
            buffer.writeBoolean(hasInput);
            if (hasInput) recipe.input.toNetwork(buffer);
            buffer.writeDouble(recipe.durationModifier);
            buffer.writeDouble(recipe.potencyModifier);
        }
    }
}
