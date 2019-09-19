package com.mowmaster.dust.crafting;

import com.google.common.collect.Maps;
import com.mowmaster.dust.blocks.BlockDustStone;
import com.mowmaster.dust.item.ItemDust;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class SpellCraftingStone
{
    private static final SpellCraftingStone SPELL_CRAFTING_STONE = new SpellCraftingStone();
    /** The list of smelting results. */
    private final Map<ItemStack, ItemStack> spellCraftingStoneList = Maps.<ItemStack, ItemStack>newHashMap();

    /**
     * Returns an instance of FurnaceRecipes.
     */
    public static SpellCraftingStone instance()
    {
        return SPELL_CRAFTING_STONE;
    }

    private SpellCraftingStone()
    {

        this.addSpellCraftingStoneRecipe(new ItemStack(ItemDust.DUST_RED,2),new ItemStack(BlockDustStone.ITEM_STONE_RED,1));
        this.addSpellCraftingStoneRecipe(new ItemStack(ItemDust.DUST_GREEN,2),new ItemStack(BlockDustStone.ITEM_STONE_GREEN,1));
        this.addSpellCraftingStoneRecipe(new ItemStack(ItemDust.DUST_BLUE,2),new ItemStack(BlockDustStone.ITEM_STONE_BLUE,1));
        this.addSpellCraftingStoneRecipe(new ItemStack(ItemDust.DUST_WHITE,2),new ItemStack(BlockDustStone.ITEM_STONE_WHITE,1));
        this.addSpellCraftingStoneRecipe(new ItemStack(ItemDust.DUST_BLACK,2),new ItemStack(BlockDustStone.ITEM_STONE_BLACK,1));

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
        this.addSpellCraftingStoneRecipe(new ItemStack(input), stack);
    }

    /**
     * Adds a smelting recipe using an ItemStack as the input for the recipe.
     */
    public void addSpellCraftingStoneRecipe(ItemStack input, ItemStack stack)
    {
        if (getResult(input) != ItemStack.EMPTY) { return;}
        this.spellCraftingStoneList.put(input, stack);
    }

    /**
     * Returns the smelting result of an item.
     */
    public ItemStack getResult(ItemStack stack)
    {
        for (Map.Entry<ItemStack, ItemStack> entry : this.spellCraftingStoneList.entrySet())
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
        return stack2.getItem() == stack1.getItem();
    }

    public Map<ItemStack, ItemStack> getCrushingList()
    {
        return this.spellCraftingStoneList;
    }

}
