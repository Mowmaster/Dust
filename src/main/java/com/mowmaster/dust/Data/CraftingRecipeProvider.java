package com.mowmaster.dust.Data;

import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.common.data.ForgeRecipeProvider;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.mowmaster.dust.References.Constants.MODID;

public class CraftingRecipeProvider  extends RecipeProvider
{
    public CraftingRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public String getName() {
        return MODID + "_crafting_recipes";
    }

    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
    {

        /*ShapedRecipeBuilder.shaped(DeferredRegisterBlocks.CRYSTAL_BLOCK.get())
                .group(MODID + "_pedestal")
                .pattern("S S")
                .pattern(" B ")
                .pattern("BBB")
                .define('S', DeferredRegisterBlocks.CRYSTAL_STONE_SLAB.get())
                .define('B', DeferredRegisterBlocks.CRYSTAL_STONE.get())
                .unlockedBy("has_item",has(DeferredRegisterBlocks.CRYSTAL_STONE.get()))
                .save(consumer);*/

        /*for (Iterator<Integer> iter = ColorReference.ALL_COLORS.iterator(); iter.hasNext(); )
        {
            makePedestalRecipe(consumer,MODID + "_pedestal", iter.next());
        }*/

        /*ItemStack ing1 = ColorReference.addColorToItemStack(new ItemStack(DeferredRegisterBlocks.CRYSTAL_STONE_SLAB.get().asItem()),255);
        ItemStack ing2 = ColorReference.addColorToItemStack(new ItemStack(DeferredRegisterBlocks.CRYSTAL_STONE.get().asItem()),255);


        ShapedRecipeBuilder.shaped(DeferredRegisterBlocks.CRYSTAL_BLOCK.get())
                .group(MODID + "_pedestal")
                .pattern("S S")
                .pattern(" B ")
                .pattern("BBB")
                .define('S', NBTIngredient.of(ing1))
                .define('B', NBTIngredient.of(ing2))
                .unlockedBy("has_item",has(DeferredRegisterBlocks.CRYSTAL_STONE.get()))
                .save(consumer);*/
    }

    public void makeSimpleRecipe(Consumer<FinishedRecipe> consumer,String groupName, Item item)
    {
        /*ShapelessRecipeBuilder.shapeless(item)
                .group(MODID + groupName)
                .requires(DeferredRegisterBlocks.CRYSTAL_BLOCK)
                .requires(ItemFocus.FOCUS_BASE)
                .requires(((ItemEnchantableBook)item).getGetIngredient())
                .unlockedBy("has_item",has(ItemFocus.FOCUS_BASE))
                .save(consumer);*/
    }

    public void makePedestalRecipe(Consumer<FinishedRecipe> consumer,String groupName, int color)
    {
        /*BetterShapedRecipeBuilder.vanillaBuilder(DeferredRegisterBlocks.CRYSTAL_BLOCK.get()).addExtraData(json -> json.addProperty(MODID+"_color", color))
                .setGroup(MODID + groupName)
                .patternLine("S S")
                .patternLine(" B ")
                .patternLine("BBB")
                .key('S', DeferredRegisterBlocks.CRYSTAL_STONE_SLAB.get()).addExtraData(json -> json.addProperty(MODID+"_color", color))
                .key('B', DeferredRegisterBlocks.CRYSTAL_STONE.get()).addExtraData(json -> json.addProperty(MODID+"_color", color))
                .addCriterion("has_item",has(DeferredRegisterBlocks.CRYSTAL_STONE.get()))
                .build(consumer);*/


        ShapedRecipeBuilder.shaped(DeferredRegisterBlocks.CRYSTAL_BLOCK.get())
                .group(MODID + "_pedestal")
                .pattern("S S")
                .pattern(" B ")
                .pattern("BBB")
                .define('S', NBTIngredient.of(ColorReference.addColorToItemStack(new ItemStack(DeferredRegisterBlocks.CRYSTAL_STONE_SLAB.get().asItem()),color)))
                .define('B', NBTIngredient.of(ColorReference.addColorToItemStack(new ItemStack(DeferredRegisterBlocks.CRYSTAL_STONE.get().asItem()),color)))
                .unlockedBy("has_item",has(DeferredRegisterBlocks.CRYSTAL_STONE.get()))
                .save(consumer);
    }
}
