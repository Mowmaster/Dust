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

public class BaseBlockEntityFilter implements Recipe<Container>
{
    @ObjectHolder(MODID + ":entity_filter")
    public static RecipeSerializer<?> SERIALIZER = new Serializer();

    public static RecipeType<BaseBlockEntityFilter> BLOCKENTITYFILTER = RecipeType.register(MODID + ":entity_filter");

    private final String group;
    private final ResourceLocation id;
    @Nullable
    private final Ingredient input;
    private final String entityString;
    private final int type;
    private final boolean isbaby;

    public BaseBlockEntityFilter(ResourceLocation id, String group, @Nullable Ingredient input, String entityString, int type, boolean isbaby)
    {
        this.group = group;
        this.id = id;
        this.input = input;
        this.entityString = entityString;
        this.type = type;
        this.isbaby = isbaby;
    }

    public static Collection<BaseBlockEntityFilter> getAllRecipes(Level world)
    {
        return world.getRecipeManager().getAllRecipesFor(BLOCKENTITYFILTER);
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

    public String getResultEntityString()
    {
        return getEntityString();
    }

    public int getResultMobType()
    {
        return getMobType();
    }

    public boolean getResultBaby()
    {
        return getBaby();
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
        return BLOCKENTITYFILTER;
    }

    @Override
    public ItemStack getToastSymbol()
    {
        return new ItemStack(Items.WITHER_SKELETON_SKULL.asItem());
    }

    public String getEntityString()
    {
        return entityString;
    }

    public int getMobType()
    {
        return type;
    }

    public boolean getBaby()
    {
        return isbaby;
    }

    public Ingredient getPattern()
    {
        return input != null ? input : Ingredient.EMPTY;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<BaseBlockEntityFilter>
    {
        protected BaseBlockEntityFilter createRecipe(ResourceLocation recipeId, String group, Ingredient input, String entityString, int type, boolean isbaby)
        {
            return new BaseBlockEntityFilter(recipeId, group, input, entityString, type, isbaby);
        }

        @Override
        public BaseBlockEntityFilter fromJson(ResourceLocation recipeId, JsonObject json)
        {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient input = json.has("input") ? CraftingHelper.getIngredient(json.get("input")) : null;
            String entityString = json.has("entityString") ? GsonHelper.getAsString(json,"entityString") : "";
            //most people will be setting up mobtype and not mob category so default to 1
            int mobType = json.has("mobType") ? GsonHelper.getAsInt(json,"mobType") : (1);
            boolean isBaby = json.has("isBaby") ? GsonHelper.getAsBoolean(json,"isBaby") : false;
            return createRecipe(recipeId, group, input, entityString, mobType, isBaby);
        }

        @Override
        public BaseBlockEntityFilter fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            String group = buffer.readUtf(32767);
            boolean hasInput = buffer.readBoolean();
            Ingredient input = hasInput ? Ingredient.fromNetwork(buffer) : null;
            String entityString = buffer.readUtf(32767);
            int mobType = buffer.readInt();
            boolean isBaby = buffer.readBoolean();
            return createRecipe(recipeId, group,  input, entityString, mobType, isBaby);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BaseBlockEntityFilter recipe)
        {
            buffer.writeUtf(recipe.group);
            boolean hasInput = recipe.input != null;
            buffer.writeBoolean(hasInput);
            if (hasInput) recipe.input.toNetwork(buffer);
            buffer.writeUtf(recipe.entityString);
            buffer.writeInt(recipe.type);
            buffer.writeBoolean(recipe.isbaby);
        }
    }
}
