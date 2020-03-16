package com.mowmaster.dust.crafting;

import com.google.common.collect.Maps;
import com.mowmaster.dust.blocks.BlockDustStone;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class SpellCraftingStone
{
    private static final SpellCraftingStone SPELL_CRAFTING_STONE = new SpellCraftingStone();

    private final Map<Integer, ItemStack> spellCraftingStoneList = Maps.<Integer, ItemStack>newHashMap();

    public static SpellCraftingStone instance()
    {
        return SPELL_CRAFTING_STONE;
    }

    private SpellCraftingStone()
    {
        this.addSpellCraftingStoneRecipe(0,new ItemStack(BlockDustStone.ITEM_STONE_000));
        this.addSpellCraftingStoneRecipe(85,new ItemStack(BlockDustStone.ITEM_STONE_001));
        this.addSpellCraftingStoneRecipe(170,new ItemStack(BlockDustStone.ITEM_STONE_002));
        this.addSpellCraftingStoneRecipe(255,new ItemStack(BlockDustStone.ITEM_STONE_003));
        this.addSpellCraftingStoneRecipe(21760,new ItemStack(BlockDustStone.ITEM_STONE_010));
        this.addSpellCraftingStoneRecipe(21845,new ItemStack(BlockDustStone.ITEM_STONE_011));
        this.addSpellCraftingStoneRecipe(21930,new ItemStack(BlockDustStone.ITEM_STONE_012));
        this.addSpellCraftingStoneRecipe(22015,new ItemStack(BlockDustStone.ITEM_STONE_013));
        this.addSpellCraftingStoneRecipe(43520,new ItemStack(BlockDustStone.ITEM_STONE_020));
        this.addSpellCraftingStoneRecipe(43605,new ItemStack(BlockDustStone.ITEM_STONE_021));
        this.addSpellCraftingStoneRecipe(43690,new ItemStack(BlockDustStone.ITEM_STONE_022));
        this.addSpellCraftingStoneRecipe(43775,new ItemStack(BlockDustStone.ITEM_STONE_023));
        this.addSpellCraftingStoneRecipe(65280,new ItemStack(BlockDustStone.ITEM_STONE_030));
        this.addSpellCraftingStoneRecipe(65365,new ItemStack(BlockDustStone.ITEM_STONE_031));
        this.addSpellCraftingStoneRecipe(65450,new ItemStack(BlockDustStone.ITEM_STONE_032));
        this.addSpellCraftingStoneRecipe(65535,new ItemStack(BlockDustStone.ITEM_STONE_033));

        this.addSpellCraftingStoneRecipe(5570560,new ItemStack(BlockDustStone.ITEM_STONE_100));
        this.addSpellCraftingStoneRecipe(5570645,new ItemStack(BlockDustStone.ITEM_STONE_101));
        this.addSpellCraftingStoneRecipe(5570730,new ItemStack(BlockDustStone.ITEM_STONE_102));
        this.addSpellCraftingStoneRecipe(5570815,new ItemStack(BlockDustStone.ITEM_STONE_103));
        this.addSpellCraftingStoneRecipe(5592320,new ItemStack(BlockDustStone.ITEM_STONE_110));
        this.addSpellCraftingStoneRecipe(5592405,new ItemStack(BlockDustStone.ITEM_STONE_111));
        this.addSpellCraftingStoneRecipe(5592490,new ItemStack(BlockDustStone.ITEM_STONE_112));
        this.addSpellCraftingStoneRecipe(5592575,new ItemStack(BlockDustStone.ITEM_STONE_113));
        this.addSpellCraftingStoneRecipe(5614080,new ItemStack(BlockDustStone.ITEM_STONE_120));
        this.addSpellCraftingStoneRecipe(5614165,new ItemStack(BlockDustStone.ITEM_STONE_121));
        this.addSpellCraftingStoneRecipe(5614250,new ItemStack(BlockDustStone.ITEM_STONE_122));
        this.addSpellCraftingStoneRecipe(5614335,new ItemStack(BlockDustStone.ITEM_STONE_123));
        this.addSpellCraftingStoneRecipe(5635840,new ItemStack(BlockDustStone.ITEM_STONE_130));
        this.addSpellCraftingStoneRecipe(5635925,new ItemStack(BlockDustStone.ITEM_STONE_131));
        this.addSpellCraftingStoneRecipe(5636010,new ItemStack(BlockDustStone.ITEM_STONE_132));
        this.addSpellCraftingStoneRecipe(5636095,new ItemStack(BlockDustStone.ITEM_STONE_133));

        this.addSpellCraftingStoneRecipe(11141120,new ItemStack(BlockDustStone.ITEM_STONE_200));
        this.addSpellCraftingStoneRecipe(11141205,new ItemStack(BlockDustStone.ITEM_STONE_201));
        this.addSpellCraftingStoneRecipe(11141290,new ItemStack(BlockDustStone.ITEM_STONE_202));
        this.addSpellCraftingStoneRecipe(11141375,new ItemStack(BlockDustStone.ITEM_STONE_203));
        this.addSpellCraftingStoneRecipe(11162880,new ItemStack(BlockDustStone.ITEM_STONE_210));
        this.addSpellCraftingStoneRecipe(11162965,new ItemStack(BlockDustStone.ITEM_STONE_211));
        this.addSpellCraftingStoneRecipe(11163050,new ItemStack(BlockDustStone.ITEM_STONE_212));
        this.addSpellCraftingStoneRecipe(11163135,new ItemStack(BlockDustStone.ITEM_STONE_213));
        this.addSpellCraftingStoneRecipe(11184640,new ItemStack(BlockDustStone.ITEM_STONE_220));
        this.addSpellCraftingStoneRecipe(11184725,new ItemStack(BlockDustStone.ITEM_STONE_221));
        this.addSpellCraftingStoneRecipe(11184810,new ItemStack(BlockDustStone.ITEM_STONE_222));
        this.addSpellCraftingStoneRecipe(11184895,new ItemStack(BlockDustStone.ITEM_STONE_223));
        this.addSpellCraftingStoneRecipe(11206400,new ItemStack(BlockDustStone.ITEM_STONE_230));
        this.addSpellCraftingStoneRecipe(11206485,new ItemStack(BlockDustStone.ITEM_STONE_231));
        this.addSpellCraftingStoneRecipe(11206570,new ItemStack(BlockDustStone.ITEM_STONE_232));
        this.addSpellCraftingStoneRecipe(11206655,new ItemStack(BlockDustStone.ITEM_STONE_233));

        this.addSpellCraftingStoneRecipe(16711680,new ItemStack(BlockDustStone.ITEM_STONE_300));
        this.addSpellCraftingStoneRecipe(16711765,new ItemStack(BlockDustStone.ITEM_STONE_301));
        this.addSpellCraftingStoneRecipe(16711850,new ItemStack(BlockDustStone.ITEM_STONE_302));
        this.addSpellCraftingStoneRecipe(16711935,new ItemStack(BlockDustStone.ITEM_STONE_303));
        this.addSpellCraftingStoneRecipe(16733440,new ItemStack(BlockDustStone.ITEM_STONE_310));
        this.addSpellCraftingStoneRecipe(16733525,new ItemStack(BlockDustStone.ITEM_STONE_311));
        this.addSpellCraftingStoneRecipe(16733610,new ItemStack(BlockDustStone.ITEM_STONE_312));
        this.addSpellCraftingStoneRecipe(16733695,new ItemStack(BlockDustStone.ITEM_STONE_313));
        this.addSpellCraftingStoneRecipe(16755200,new ItemStack(BlockDustStone.ITEM_STONE_320));
        this.addSpellCraftingStoneRecipe(16755285,new ItemStack(BlockDustStone.ITEM_STONE_321));
        this.addSpellCraftingStoneRecipe(16755370,new ItemStack(BlockDustStone.ITEM_STONE_322));
        this.addSpellCraftingStoneRecipe(16755455,new ItemStack(BlockDustStone.ITEM_STONE_323));
        this.addSpellCraftingStoneRecipe(16776960,new ItemStack(BlockDustStone.ITEM_STONE_330));
        this.addSpellCraftingStoneRecipe(16777045,new ItemStack(BlockDustStone.ITEM_STONE_331));
        this.addSpellCraftingStoneRecipe(16777130,new ItemStack(BlockDustStone.ITEM_STONE_332));
        this.addSpellCraftingStoneRecipe(16777215,new ItemStack(BlockDustStone.ITEM_STONE_333));
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
