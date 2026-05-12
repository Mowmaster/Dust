package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.DustReferences;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DustProviderBasicRecipe extends RecipeProvider {
    public DustProviderBasicRecipe(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }



    @Override
    protected void buildRecipes() {
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

        stairBuilder(DustBlockRegistry.BLOCK_STAIRS_GREEN.get(), Ingredient.of(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()))
                .group(DustReferences.MODID)
                .unlockedBy(getHasName(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()), has(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()))
                .save(output);
        slab(RecipeCategory.BUILDING_BLOCKS, DustBlockRegistry.BLOCK_SLAB_GREEN.get(), DustBlockRegistry.BLOCK_PLANKS_GREEN.get());

        buttonBuilder(DustBlockRegistry.BLOCK_BUTTON_GREEN.get(), Ingredient.of(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()))
                .group(DustReferences.MODID)
                .unlockedBy(getHasName(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()), has(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()))
                .save(output);
        pressurePlate(DustBlockRegistry.BLOCK_PRESSUREPLATE_GREEN.get(), DustBlockRegistry.BLOCK_PLANKS_GREEN.get());
        fenceBuilder(DustBlockRegistry.BLOCK_FENCE_GREEN.get(), Ingredient.of(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()))
                .group(DustReferences.MODID)
                .unlockedBy(getHasName(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()), has(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()))
                .save(output);
        fenceGateBuilder(DustBlockRegistry.BLOCK_FENCEGATE_GREEN.get(), Ingredient.of(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()))
                .group(DustReferences.MODID)
                .unlockedBy(getHasName(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()), has(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()))
                .save(output);
        //wALL AND TRAPDOOR HAVE SAME RECIPE...
        //wall(RecipeCategory.BUILDING_BLOCKS, DustBlockRegistry.BLOCK_WALL_GREEN.get(), DustBlockRegistry.BLOCK_PLANKS_GREEN.get());
        doorBuilder(DustBlockRegistry.BLOCK_DOOR_GREEN.get(), Ingredient.of(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()))
                .group(DustReferences.MODID)
                .unlockedBy(getHasName(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()), has(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()))
                .save(output);
        trapdoorBuilder(DustBlockRegistry.BLOCK_TRAPDOOR_GREEN.get(), Ingredient.of(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()))
                .group(DustReferences.MODID)
                .unlockedBy(getHasName(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()), has(DustBlockRegistry.BLOCK_PLANKS_GREEN.get()))
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

        shaped(RecipeCategory.COMBAT, DustItemRegistry.CORNBREAD.get())
                .pattern("CCC")
                .pattern("CAC")
                .pattern("CCC")
                .define('C', DustTags.Items.MAGICAL_DUST_ITEMS).define('A', Items.BREAD)
                .unlockedBy(getHasName(DustItemRegistry.DUST_RED.get()), has(DustItemRegistry.DUST_RED.get()))
                .save(output);
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

    public static class Runner extends RecipeProvider.Runner {
        protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new DustProviderBasicRecipe(provider,recipeOutput);
        }

        @Override
        public String getName() {
            return DustReferences.MODNAME + " Recipes";
        }
    }
}
