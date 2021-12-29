package com.mowmaster.dust.Recipes;

import com.google.gson.JsonObject;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
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

public class CrystalNodeRecipe implements Recipe<Container>
{
    @ObjectHolder(MODID + ":crystalnode")
    public static RecipeSerializer<?> SERIALIZER = new Serializer();

    public static RecipeType<CrystalNodeRecipe> CRYSTAL_NODE = RecipeType.register(MODID + ":crystalnode");

    private final String group;
    private final ResourceLocation id;
    @Nullable
    private final Ingredient input;
    private final ItemStack output;

    public CrystalNodeRecipe(ResourceLocation id, String group, @Nullable Ingredient input, ItemStack output)
    {
        this.group = group;
        this.id = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static Collection<CrystalNodeRecipe> getAllRecipes(Level world)
    {
        return world.getRecipeManager().getAllRecipesFor(CRYSTAL_NODE);
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

    @Override
    public ItemStack getResultItem()
    {
        return output;
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
        return CRYSTAL_NODE;
    }

    @Override
    public ItemStack getToastSymbol()
    {
        return new ItemStack(DeferredRegisterBlocks.CRYSTAL_NODE.get());
    }

    public Ingredient getPattern()
    {
        return input != null ? input : Ingredient.EMPTY;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<CrystalNodeRecipe>
    {
        protected CrystalNodeRecipe createRecipe(ResourceLocation recipeId, String group, Ingredient input, ItemStack result)
        {
            return new CrystalNodeRecipe(recipeId, group, input, result);
        }

        @Override
        public CrystalNodeRecipe fromJson(ResourceLocation recipeId, JsonObject json)
        {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient input = json.has("input") ? CraftingHelper.getIngredient(json.get("input")) : null;
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
            return createRecipe(recipeId, group, input, result);
        }

        @Override
        public CrystalNodeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            String group = buffer.readUtf(32767);
            boolean hasInput = buffer.readBoolean();
            Ingredient input = hasInput ? Ingredient.fromNetwork(buffer) : null;
            ItemStack result = buffer.readItem();
            return createRecipe(recipeId, group,  input, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrystalNodeRecipe recipe)
        {
            buffer.writeUtf(recipe.group);
            boolean hasInput = recipe.input != null;
            buffer.writeBoolean(hasInput);
            if (hasInput) recipe.input.toNetwork(buffer);
            buffer.writeItem(recipe.output);
        }
    }
}
