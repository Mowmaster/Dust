package com.mowmaster.dust.Data;


import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("WeakerAccess")
public class BetterShapedRecipeBuilder
{
    private final RecipeSerializer<?> serializer;
    private final Collection<Consumer<JsonObject>> extraData = new ArrayList<>();
    private final Item result;
    private final int count;
    private final List<String> pattern = new ArrayList<>();
    private final Map<Character, Ingredient> key = new LinkedHashMap<>();
    private final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();
    private boolean hasAdvancementCriterion = false;
    private String group = "";

    private BetterShapedRecipeBuilder(RecipeSerializer<?> serializer, ItemLike result, int count) {
        this.serializer = serializer;
        this.result = result.asItem();
        this.count = count;
    }

    public static BetterShapedRecipeBuilder builder(RecipeSerializer<?> serializer, ItemLike result) {
        return builder(serializer, result, 1);
    }

    public static BetterShapedRecipeBuilder builder(RecipeSerializer<?> serializer, ItemLike result, int count) {
        return new BetterShapedRecipeBuilder(serializer, result, count);
    }

    public static BetterShapedRecipeBuilder vanillaBuilder(ItemLike result) {
        return vanillaBuilder(result, 1);
    }

    public static BetterShapedRecipeBuilder vanillaBuilder(ItemLike result, int count) {
        return new BetterShapedRecipeBuilder(RecipeSerializer.SHAPED_RECIPE, result, count);
    }

    /**
     * Override to quickly add additional data to serialization
     *
     * @param json The recipe JSON
     */
    protected void serializeExtra(JsonObject json) {
        this.extraData.forEach(consumer -> consumer.accept(json));
    }

    /**
     * Allows extra data to be quickly appended for simple serializers. For more complex
     * serializers, consider extending this class and overriding {@link #serializeExtra(JsonObject)}
     * instead.
     *
     * @param extraDataIn Changes to make to the recipe JSON (called after base JSON is generated)
     * @return The recipe builder
     */
    public BetterShapedRecipeBuilder addExtraData(Consumer<JsonObject> extraDataIn) {
        this.extraData.add(extraDataIn);
        return this;
    }

    public BetterShapedRecipeBuilder key(Character symbol, Tag<Item> tagIn) {
        return this.key(symbol, Ingredient.of(tagIn));
    }

    public BetterShapedRecipeBuilder key(Character symbol, ItemLike itemIn) {
        return this.key(symbol, Ingredient.of(itemIn));
    }

    public BetterShapedRecipeBuilder key(Character symbol, Ingredient ingredientIn) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        } else if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(symbol, ingredientIn);
            return this;
        }
    }

    public BetterShapedRecipeBuilder patternLine(String patternIn) {
        if (!this.pattern.isEmpty() && patternIn.length() != this.pattern.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.pattern.add(patternIn);
            return this;
        }
    }

    public BetterShapedRecipeBuilder addCriterion(String name, CriterionTriggerInstance criterionIn) {
        this.advancementBuilder.addCriterion(name, criterionIn);
        this.hasAdvancementCriterion = true;
        return this;
    }

    public BetterShapedRecipeBuilder setGroup(String groupIn) {
        this.group = groupIn;
        return this;
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        build(consumer,checkNotNull(this.result.getRegistryName()));
    }

    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        this.validate(id);
        if (this.hasAdvancementCriterion && !this.advancementBuilder.getCriteria().isEmpty()) {
            this.advancementBuilder.parent(new ResourceLocation("recipes/root"))
                    .addCriterion("has_the_recipe", new RecipeUnlockedTrigger.TriggerInstance(EntityPredicate.Composite.ANY, id))
                    .rewards(AdvancementRewards.Builder.recipe(id))
                    .requirements(RequirementsStrategy.OR);
        }
        ResourceLocation advancementId = new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + id.getPath());
        consumer.accept(new Result(id, this, advancementId));
    }

    private void validate(ResourceLocation id) {
        // Basically the same as ShapedRecipeBuilder, but doesn't fail if advancement is missing
        if (this.pattern.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + id + "!");
        } else {
            Set<Character> set = Sets.newHashSet(this.key.keySet());
            set.remove(' ');

            for (String s : this.pattern) {
                for (int i = 0; i < s.length(); ++i) {
                    char c0 = s.charAt(i);
                    if (!this.key.containsKey(c0) && c0 != ' ') {
                        throw new IllegalStateException("Pattern in recipe " + id + " uses undefined symbol '" + c0 + "'");
                    }

                    set.remove(c0);
                }
            }

            if (!set.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + id);
            } else if (this.pattern.size() == 1 && this.pattern.get(0).length() == 1) {
                throw new IllegalStateException("Shaped recipe " + id + " only takes in a single item - should it be a shapeless recipe instead?");
            }
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final BetterShapedRecipeBuilder builder;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, BetterShapedRecipeBuilder builder, ResourceLocation advancementId) {
            this.id = id;
            this.builder = builder;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (!builder.group.isEmpty()) {
                json.addProperty("group", builder.group);
            }

            JsonArray pattern = new JsonArray();
            builder.pattern.forEach(pattern::add);
            json.add("pattern", pattern);

            JsonObject key = new JsonObject();
            builder.key.forEach((c, ingredient) -> key.add(String.valueOf(c), ingredient.toJson()));
            json.add("key", key);

            JsonObject result = new JsonObject();
            result.addProperty("item", checkNotNull(builder.result.getRegistryName()).toString());
            if (builder.count > 1) {
                result.addProperty("count", builder.count);
            }
            json.add("result", result);

            builder.serializeExtra(json);
        }

        @Override
        public RecipeSerializer<?> getType() {
            return builder.serializer;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return builder.hasAdvancementCriterion ? builder.advancementBuilder.serializeToJson() : null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return builder.hasAdvancementCriterion ? advancementId : null;
        }
    }
}
