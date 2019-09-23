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
    private final Map<Integer, ItemStack> spellCraftingStoneList = Maps.<Integer, ItemStack>newHashMap();

    /**
     * Returns an instance of FurnaceRecipes.
     */
    public static SpellCraftingStone instance()
    {
        return SPELL_CRAFTING_STONE;
    }

    private SpellCraftingStone()
    {
        this.addSpellCraftingStoneRecipe(16711680,new ItemStack(BlockDustStone.ITEM_STONE_RED,1));
        this.addSpellCraftingStoneRecipe(2752512,new ItemStack(BlockDustStone.ITEM_STONE_RED1,1));
        this.addSpellCraftingStoneRecipe(5505024,new ItemStack(BlockDustStone.ITEM_STONE_RED2,1));
        this.addSpellCraftingStoneRecipe(8323072,new ItemStack(BlockDustStone.ITEM_STONE_RED3,1));
        this.addSpellCraftingStoneRecipe(11141120,new ItemStack(BlockDustStone.ITEM_STONE_RED4,1));
        this.addSpellCraftingStoneRecipe(13893632,new ItemStack(BlockDustStone.ITEM_STONE_RED5,1));

        this.addSpellCraftingStoneRecipe(65280,new ItemStack(BlockDustStone.ITEM_STONE_GREEN,1));
        this.addSpellCraftingStoneRecipe(255,new ItemStack(BlockDustStone.ITEM_STONE_BLUE,1));
        this.addSpellCraftingStoneRecipe(16777215,new ItemStack(BlockDustStone.ITEM_STONE_WHITE,1));
        this.addSpellCraftingStoneRecipe(2105376,new ItemStack(BlockDustStone.ITEM_STONE_BLACK,1));
    }



    /**
     * Adds a smelting recipe using an Item as the input item.
     */
    public void addColorRecipe(int colorIn, ItemStack stack)
    {
        this.addSpellCraftingStoneRecipe(colorIn, stack);
    }

    /**
     * Adds a smelting recipe using an ItemStack as the input for the recipe.
     */
    public void addSpellCraftingStoneRecipe(int colorIn, ItemStack stack)
    {
        if (getResult(colorIn) != ItemStack.EMPTY) { return;}
        this.spellCraftingStoneList.put(colorIn, stack);
    }

    /**
     * Returns the smelting result of an item.
     */
    public ItemStack getResult(int colorIn)
    {
        for (Map.Entry<Integer, ItemStack> entry : this.spellCraftingStoneList.entrySet())
        {
            if (colorIn == entry.getKey())
            {
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }

    public Map<Integer, ItemStack> getCrushingList()
    {
        return this.spellCraftingStoneList;
    }

}
