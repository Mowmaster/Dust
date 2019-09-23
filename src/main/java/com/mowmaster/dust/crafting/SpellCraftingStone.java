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
        this.addSpellCraftingStoneRecipe(0,new ItemStack(BlockDustStone.ITEM_STONE_000,1));
        this.addSpellCraftingStoneRecipe(50,new ItemStack(BlockDustStone.ITEM_STONE_001,1));
        this.addSpellCraftingStoneRecipe(101,new ItemStack(BlockDustStone.ITEM_STONE_002,1));
        this.addSpellCraftingStoneRecipe(153,new ItemStack(BlockDustStone.ITEM_STONE_003,1));
        this.addSpellCraftingStoneRecipe(204,new ItemStack(BlockDustStone.ITEM_STONE_004,1));
        this.addSpellCraftingStoneRecipe(255,new ItemStack(BlockDustStone.ITEM_STONE_005,1));

        this.addSpellCraftingStoneRecipe(12851,new ItemStack(BlockDustStone.ITEM_STONE_010,1));
        this.addSpellCraftingStoneRecipe(12901,new ItemStack(BlockDustStone.ITEM_STONE_011,1));
        this.addSpellCraftingStoneRecipe(12952,new ItemStack(BlockDustStone.ITEM_STONE_012,1));
        this.addSpellCraftingStoneRecipe(13004,new ItemStack(BlockDustStone.ITEM_STONE_013,1));
        this.addSpellCraftingStoneRecipe(13055,new ItemStack(BlockDustStone.ITEM_STONE_014,1));
        this.addSpellCraftingStoneRecipe(13106,new ItemStack(BlockDustStone.ITEM_STONE_015,1));

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
