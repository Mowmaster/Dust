package com.mowmaster.dust.Recipes;

import com.google.gson.JsonObject;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
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

public class MachineBlockRepairItemsRecipe implements Recipe<Container>
{
    @ObjectHolder(MODID + ":machine_repair_items")
    public static RecipeSerializer<?> SERIALIZER = new Serializer();

    public static RecipeType<MachineBlockRepairItemsRecipe> MACHINE_REPAIR_ITEMS = RecipeType.register(MODID + ":machine_repair_items");

    private final String group;
    private final ResourceLocation id;
    @Nullable
    private final Ingredient input;
    private final ItemStack output;
    private final String tagname;

    public MachineBlockRepairItemsRecipe(ResourceLocation id, String group, @Nullable Ingredient input, ItemStack output, String tagname)
    {
        this.group = group;
        this.id = id;
        this.input = input;
        this.output = output;
        this.tagname = tagname;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static Collection<MachineBlockRepairItemsRecipe> getAllRecipes(Level world)
    {
        return world.getRecipeManager().getAllRecipesFor(MACHINE_REPAIR_ITEMS);
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

    public String getResultTag() {return tagname;}

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
        return MACHINE_REPAIR_ITEMS;
    }

    @Override
    public ItemStack getToastSymbol()
    {
        return new ItemStack(DeferredRegisterBlocks.BASE_MACHINE_BLOCK.get());
    }

    public Ingredient getPattern()
    {
        return input != null ? input : Ingredient.EMPTY;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<MachineBlockRepairItemsRecipe>
    {
        protected MachineBlockRepairItemsRecipe createRecipe(ResourceLocation recipeId, String group, Ingredient input, ItemStack result, String tagname)
        {
            return new MachineBlockRepairItemsRecipe(recipeId, group, input, result, tagname);
        }

        @Override
        public MachineBlockRepairItemsRecipe fromJson(ResourceLocation recipeId, JsonObject json)
        {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient input = json.has("input") ? CraftingHelper.getIngredient(json.get("input")) : null;
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
            String tagname = json.has("tagname") ? GsonHelper.getAsString(json,"tagname") : "";
            return createRecipe(recipeId, group, input, result, tagname);
        }

        @Override
        public MachineBlockRepairItemsRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            String group = buffer.readUtf(32767);
            boolean hasInput = buffer.readBoolean();
            Ingredient input = hasInput ? Ingredient.fromNetwork(buffer) : null;
            ItemStack result = buffer.readItem();
            String tagname = buffer.readUtf(32767);
            return createRecipe(recipeId, group,  input, result, tagname);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, MachineBlockRepairItemsRecipe recipe)
        {
            buffer.writeUtf(recipe.group);
            boolean hasInput = recipe.input != null;
            buffer.writeBoolean(hasInput);
            if (hasInput) recipe.input.toNetwork(buffer);
            buffer.writeItem(recipe.output);
            buffer.writeUtf(recipe.tagname);
        }
    }
}
