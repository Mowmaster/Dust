package com.mowmaster.dust.recipes.crusher_recipes;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;


//Code directly taken from FurnaceRecipes
public class CrusherRecipes
{
    private static final CrusherRecipes CRUSHER_RECIPES = new CrusherRecipes();
    /** The list of smelting results. */
    private final Map<ItemStack, ItemStack> crusherList = Maps.<ItemStack, ItemStack>newHashMap();

    /**
     * Returns an instance of FurnaceRecipes.
     */
    public static CrusherRecipes instance()
    {
        return CRUSHER_RECIPES;
    }

    private CrusherRecipes()
    {
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,0),new ItemStack(BlockRegistry.redDust,8));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,1),new ItemStack(BlockRegistry.blueDust,8));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,2),new ItemStack(BlockRegistry.yellowDust,8));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,3),new ItemStack(BlockRegistry.purpleDust,8));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,4),new ItemStack(BlockRegistry.greenDust,8));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,5),new ItemStack(BlockRegistry.orangeDust,8));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,6),new ItemStack(BlockRegistry.whiteDust,8));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,7),new ItemStack(BlockRegistry.blackDust,8));

        this.addCrushingRecipeForBlock(Blocks.REDSTONE_ORE,new ItemStack(BlockRegistry.redstoneDust,4));
        this.addCrushingRecipeForBlock(Blocks.IRON_ORE,new ItemStack(BlockRegistry.ironDust,2));
        this.addCrushingRecipeForBlock(Blocks.GOLD_ORE,new ItemStack(BlockRegistry.goldDust,2));
        this.addCrushing(Items.REEDS,new ItemStack(BlockRegistry.sugarDust,2));
        this.addCrushing(Items.WHEAT,new ItemStack(BlockRegistry.wheatDust,1));
        this.addCrushing(Items.POTATO,new ItemStack(BlockRegistry.potatoDust,1));
        this.addCrushing(Items.POISONOUS_POTATO,new ItemStack(BlockRegistry.potatoDust,1));
        this.addCrushing(Items.BLAZE_ROD,new ItemStack(BlockRegistry.blazeDust,3));
    }

    /**
     * Adds a smelting recipe, where the input item is an instance of Block.
     */
    public void addCrushingRecipeForBlock(Block input, ItemStack stack)
    {
        this.addCrushing(Item.getItemFromBlock(input), stack);
    }

    /**
     * Adds a smelting recipe using an Item as the input item.
     */
    public void addCrushing(Item input, ItemStack stack)
    {
        this.addCrusherRecipe(new ItemStack(input, 1, 32767), stack);
    }

    /**
     * Adds a smelting recipe using an ItemStack as the input for the recipe.
     */
    public void addCrusherRecipe(ItemStack input, ItemStack stack)
    {
        if (getCrushingResult(input) != ItemStack.EMPTY) { net.minecraftforge.fml.common.FMLLog.log.info("Ignored crystal crushing recipe with conflicting input: {} = {}", input, stack); return; }
        this.crusherList.put(input, stack);
    }

    /**
     * Returns the smelting result of an item.
     */
    public ItemStack getCrushingResult(ItemStack stack)
    {
        for (Map.Entry<ItemStack, ItemStack> entry : this.crusherList.entrySet())
        {
            if (this.compareItemStacks(stack, entry.getKey()))
            {
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }

    /**
     * Compares two itemstacks to ensure that they are the same. This checks both the item and the metadata of the item.
     */
    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public Map<ItemStack, ItemStack> getCrushingList()
    {
        return this.crusherList;
    }

}
