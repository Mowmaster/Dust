package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustRegistries.DustPotionRegistry;
import com.mowmaster.dust.DustReferences;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.data.recipes.BrewingRecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public class DustProviderBasicRecipe extends RecipeProvider {
    // 26.3: recipes (and their advancements) are registry entries. The provider is built by the registry
    // bootstrap that create() returns, which DustDataGenerator hands to GatherDataRegistryEntriesEvent.
    protected DustProviderBasicRecipe(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput)
    {
        super(recipeOutput, advancementOutput);
    }

    public static MultiRegistryBootstrap create()
    {
        return RecipeProvider.asBootstrap(DustProviderBasicRecipe::new);
    }



    @Override
    protected void buildRecipes() {
        buildBrewingRecipes();

        List<ItemLike> DUST_SMELTABLES = List.of(DustItemRegistry.CRYSTAL_RED);

        oreSmelting(DUST_SMELTABLES,RecipeCategory.MISC, CookingBookCategory.BLOCKS, DustItemRegistry.DUST_RED.get(), 0.25f, 200, DustReferences.MODID);
        oreBlasting(DUST_SMELTABLES,RecipeCategory.MISC, CookingBookCategory.BLOCKS, DustItemRegistry.DUST_RED.get(), 0.25f, 100, DustReferences.MODID);


        shaped(RecipeCategory.BUILDING_BLOCKS, DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get())
                .pattern("CCC")
                .pattern("CCC")
                .pattern("CCC")
                .define('C', DustItemRegistry.CRYSTAL_RED.get())
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, DustItemRegistry.CRYSTAL_RED.get())
                .pattern("DDD")
                .pattern("DID")
                .pattern("DDD")
                .define('D', DustItemRegistry.DUST_RED.get())
                .define('I', DustItemRegistry.CRYSTAL_INERT.get())
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_INERT.get()), has(DustItemRegistry.CRYSTAL_INERT.get()))
                .save(output, DustReferences.MODID + ":" + "inertcrystal_dustcrafting_red");

        shapeless(RecipeCategory.MISC, DustItemRegistry.CRYSTAL_RED.get(),9)
                .requires(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get())
                .unlockedBy(getHasName(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get()), has(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get()))
                .save(output);

        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_SWORD.get())
                .pattern("C")
                .pattern("C")
                .pattern("S")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('S', Items.STICK)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_AXE.get())
                .pattern("CC")
                .pattern("CS")
                .pattern(" S")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('S', Items.STICK)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_PICKAXE.get())
                .pattern("CCC")
                .pattern(" S ")
                .pattern(" S ")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('S', Items.STICK)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_SHOVEL.get())
                .pattern(" C ")
                .pattern(" S ")
                .pattern(" S ")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('S', Items.STICK)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_HOE.get())
                .pattern("CC")
                .pattern(" S")
                .pattern(" S")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('S', Items.STICK)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_SPEAR.get())
                .pattern("  C")
                .pattern(" C ")
                .pattern("S  ")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('S', Items.STICK)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shapeless(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_MATTOCK.get())
                .requires(DustItemRegistry.CRYSTAL_HOE.get())
                .requires(DustItemRegistry.CRYSTAL_AXE.get())
                .requires(DustItemRegistry.CRYSTAL_SHOVEL.get())
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_AXE.get()), has(DustItemRegistry.CRYSTAL_AXE.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_HAMMER.get())
                .pattern("BBB")
                .pattern(" S ")
                .pattern(" S ")
                .define('B', DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.asItem()).define('S', Items.STICK)
                .unlockedBy(getHasName(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get()), has(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get()))
                .save(output);

        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_HELMET.get())
                .pattern("CCC")
                .pattern("CAC")
                .pattern("CCC")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('A', Items.LEATHER_HELMET)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_CHESTPLATE.get())
                .pattern("CCC")
                .pattern("CAC")
                .pattern("CCC")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('A', Items.LEATHER_CHESTPLATE)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_LEGGINGS.get())
                .pattern("CCC")
                .pattern("CAC")
                .pattern("CCC")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('A', Items.LEATHER_LEGGINGS)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_BOOTS.get())
                .pattern("CCC")
                .pattern("CAC")
                .pattern("CCC")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('A', Items.LEATHER_BOOTS)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, DustItemRegistry.CRYSTAL_HORSE_ARMOR.get())
                .pattern("CCC")
                .pattern("CAC")
                .pattern("CCC")
                .define('C', DustTags.Items.MAGICAL_CRYSTAL_ITEMS).define('A', Items.LEATHER_HORSE_ARMOR)
                .unlockedBy(getHasName(DustItemRegistry.CRYSTAL_RED.get()), has(DustItemRegistry.CRYSTAL_RED.get()))
                .save(output);
    }

    // 26.3: brewing is recipe data now (RegisterBrewingRecipesEvent is gone). A brewing recipe names the exact
    // container, so the mix needs one recipe per bottle type, and the splash/lingering conversions need their own
    // recipes for every potion (vanilla only makes them for its own potions).
    private void buildBrewingRecipes() {
        // Fire Resistance + Blaze Powder -> Scorch, in all three bottles
        for (Item container : List.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION)) {
            BrewingRecipeBuilder.brewingMix(container, Potions.FIRE_RESISTANCE, Items.BLAZE_POWDER, DustPotionRegistry.POTION_SCORCH)
                    .save(output, DustReferences.MODID + ":brewing/" + getItemName(container) + "_potion_scorch");
        }
        // Scorch potion -> splash (gunpowder) -> lingering (dragon's breath)
        BrewingRecipeBuilder.brewingContainerTransform(Items.POTION, DustPotionRegistry.POTION_SCORCH, Items.GUNPOWDER, Items.SPLASH_POTION)
                .save(output, DustReferences.MODID + ":brewing/potion_scorch_to_splash");
        BrewingRecipeBuilder.brewingContainerTransform(Items.SPLASH_POTION, DustPotionRegistry.POTION_SCORCH, Items.DRAGON_BREATH, Items.LINGERING_POTION)
                .save(output, DustReferences.MODID + ":brewing/potion_scorch_to_lingering");
    }

    @Override
    protected <T extends AbstractCookingRecipe> void oreCooking(AbstractCookingRecipe.Factory<T> factory, List<ItemLike> smeltables, RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemLike result, float experience, int cookingTime, String group, String fromDesc) {
        for (ItemLike item : smeltables) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(item), craftingCategory, cookingCategory, result, experience, cookingTime, factory)
                    .group(group)
                    .unlockedBy(getHasName(item), this.has(item))
                    .save(this.output, DustReferences.MODID + ":" + getItemName(result) + fromDesc + "_" + getItemName(item));
        }
    }

}
