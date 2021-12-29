package com.mowmaster.dust.Recipes;

import com.google.gson.JsonObject;
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

public class CrusherRecipe implements Recipe<Container>
{
    @ObjectHolder(MODID + ":crushing")
    public static RecipeSerializer<?> SERIALIZER = new Serializer();

    public static RecipeType<CrusherRecipe> CRUSHING = RecipeType.register(MODID + ":crushing");

    private final String group;
    private final ResourceLocation id;
    @Nullable
    private final Ingredient input;
    private final int color;
    private final ItemStack output;

    public CrusherRecipe(ResourceLocation id, String group, @Nullable Ingredient input, @Nullable int color, ItemStack output)
    {
        this.group = group;
        this.id = id;
        this.input = input;
        this.color = color;
        this.output = output;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static Collection<CrusherRecipe> getAllRecipes(Level world)
    {
        return world.getRecipeManager().getAllRecipesFor(CRUSHING);
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

    public int getResultColor()
    {
        return getColor();
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
        return CRUSHING;
    }

    @Override
    public ItemStack getToastSymbol()
    {
        return new ItemStack(DeferredRegisterItems.COLORED_CRYSTAL_DUST.get());
    }

    public int getColor()
    {
        return color >= 0 ? color : 16777215;
    }

    public Ingredient getPattern()
    {
        return input != null ? input : Ingredient.EMPTY;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<CrusherRecipe>
    {
        protected CrusherRecipe createRecipe(ResourceLocation recipeId, String group, Ingredient input, int color, ItemStack result)
        {
            return new CrusherRecipe(recipeId, group, input, color, result);
        }

        @Override
        public CrusherRecipe fromJson(ResourceLocation recipeId, JsonObject json)
        {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient input = json.has("input") ? CraftingHelper.getIngredient(json.get("input")) : null;
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
            int color = json.has("color") ? GsonHelper.getAsInt(json,"color") : 16777215;
            return createRecipe(recipeId, group, input, color, result);
        }

        @Override
        public CrusherRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            String group = buffer.readUtf(32767);
            boolean hasInput = buffer.readBoolean();
            Ingredient input = hasInput ? Ingredient.fromNetwork(buffer) : null;
            ItemStack result = buffer.readItem();
            int color = buffer.readInt();
            return createRecipe(recipeId, group,  input, color, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrusherRecipe recipe)
        {
            buffer.writeUtf(recipe.group);
            boolean hasInput = recipe.input != null;
            buffer.writeBoolean(hasInput);
            if (hasInput) recipe.input.toNetwork(buffer);
            buffer.writeItem(recipe.output);
            buffer.writeInt(recipe.color);
        }
    }
}
