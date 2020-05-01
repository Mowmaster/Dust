package com.mowmaster.dust.crafting;

import com.google.common.collect.Maps;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;

import java.util.Map;

public class SpellCraftingEffectBad
{
    private static final SpellCraftingEffectBad SPELL_CRAFTING_EFFECT = new SpellCraftingEffectBad();

    private final Map<Integer, Effect> spellCraftingEffectList = Maps.<Integer, Effect>newHashMap();

    public static SpellCraftingEffectBad instance()
    {
        return SPELL_CRAFTING_EFFECT;
    }

    private SpellCraftingEffectBad()
    {
        this.addSpellCraftingEffectRecipe(0,Effects.INSTANT_DAMAGE);//000
        //this.addSpellCraftingEffectRecipe(85,new ItemStack(BlockDustStone.ITEM_STONE_001));
        //this.addSpellCraftingEffectRecipe(170,new ItemStack(BlockDustStone.ITEM_STONE_002));
        //this.addSpellCraftingEffectRecipe(255,Effects.WATER_BREATHING);//003
        //this.addSpellCraftingEffectRecipe(21760,new ItemStack(BlockDustStone.ITEM_STONE_010));
        //this.addSpellCraftingEffectRecipe(21845,new ItemStack(BlockDustStone.ITEM_STONE_011));
        //this.addSpellCraftingEffectRecipe(21930,new ItemStack(BlockDustStone.ITEM_STONE_012));
        //this.addSpellCraftingEffectRecipe(22015,new ItemStack(BlockDustStone.ITEM_STONE_013));

        //this.addSpellCraftingEffectRecipe(43520,new ItemStack(BlockDustStone.ITEM_STONE_020));
        //this.addSpellCraftingEffectRecipe(43605,new ItemStack(BlockDustStone.ITEM_STONE_021));
        //this.addSpellCraftingEffectRecipe(43690,new ItemStack(BlockDustStone.ITEM_STONE_022));
        //this.addSpellCraftingEffectRecipe(43775,new ItemStack(BlockDustStone.ITEM_STONE_023));
        this.addSpellCraftingEffectRecipe(65280,Effects.WITHER);//030
        //this.addSpellCraftingEffectRecipe(65365,new ItemStack(BlockDustStone.ITEM_STONE_031));
        //this.addSpellCraftingEffectRecipe(65450,new ItemStack(BlockDustStone.ITEM_STONE_032));
        this.addSpellCraftingEffectRecipe(65535,Effects.SLOWNESS);//033

        this.addSpellCraftingEffectRecipe(16777045,Effects.BAD_OMEN);//100
        this.addSpellCraftingEffectRecipe(16777045,Effects.BLINDNESS);//101
        //this.addSpellCraftingEffectRecipe(5570730,new ItemStack(BlockDustStone.ITEM_STONE_102));
        //this.addSpellCraftingEffectRecipe(5570815,new ItemStack(BlockDustStone.ITEM_STONE_103));
        this.addSpellCraftingEffectRecipe(16777045,Effects.NAUSEA);//110
        //this.addSpellCraftingEffectRecipe(16777045,Effects.CONDUIT_POWER);//111
        //this.addSpellCraftingEffectRecipe(5592490,new ItemStack(BlockDustStone.ITEM_STONE_112));
        //this.addSpellCraftingEffectRecipe(5592575,new ItemStack(BlockDustStone.ITEM_STONE_113));

        //this.addSpellCraftingEffectRecipe(5614080,new ItemStack(BlockDustStone.ITEM_STONE_120));
        this.addSpellCraftingEffectRecipe(16777045,Effects.POISON);//121
        //this.addSpellCraftingEffectRecipe(5614250,new ItemStack(BlockDustStone.ITEM_STONE_122));
        //this.addSpellCraftingEffectRecipe(5614335,new ItemStack(BlockDustStone.ITEM_STONE_123));
        //this.addSpellCraftingEffectRecipe(5635840,new ItemStack(BlockDustStone.ITEM_STONE_130));
        //this.addSpellCraftingEffectRecipe(16777045,Effects.JUMP_BOOST);//131
        //this.addSpellCraftingEffectRecipe(5636010,new ItemStack(BlockDustStone.ITEM_STONE_132));
        //this.addSpellCraftingEffectRecipe(5636095,new ItemStack(BlockDustStone.ITEM_STONE_133));

        //this.addSpellCraftingEffectRecipe(11141120,new ItemStack(BlockDustStone.ITEM_STONE_200));
        //this.addSpellCraftingEffectRecipe(11141205,new ItemStack(BlockDustStone.ITEM_STONE_201));
        //this.addSpellCraftingEffectRecipe(11141290,new ItemStack(BlockDustStone.ITEM_STONE_202));
        //this.addSpellCraftingEffectRecipe(11141375,new ItemStack(BlockDustStone.ITEM_STONE_203));
        this.addSpellCraftingEffectRecipe(11162880,Effects.UNLUCK);//210
        //this.addSpellCraftingEffectRecipe(11162965,new ItemStack(BlockDustStone.ITEM_STONE_211));
        //this.addSpellCraftingEffectRecipe(11163050,new ItemStack(BlockDustStone.ITEM_STONE_212));
        this.addSpellCraftingEffectRecipe(16777045,Effects.GLOWING);//213

        //this.addSpellCraftingEffectRecipe(11184640,new ItemStack(BlockDustStone.ITEM_STONE_220));
        //this.addSpellCraftingEffectRecipe(11184725,new ItemStack(BlockDustStone.ITEM_STONE_221));
        //this.addSpellCraftingEffectRecipe(11184810,new ItemStack(BlockDustStone.ITEM_STONE_222));
        //this.addSpellCraftingEffectRecipe(11184895,new ItemStack(BlockDustStone.ITEM_STONE_223));
        //this.addSpellCraftingEffectRecipe(11206400,new ItemStack(BlockDustStone.ITEM_STONE_230));
        //this.addSpellCraftingEffectRecipe(11206485,new ItemStack(BlockDustStone.ITEM_STONE_231));
        //this.addSpellCraftingEffectRecipe(11206570,new ItemStack(BlockDustStone.ITEM_STONE_232));
        //this.addSpellCraftingEffectRecipe(11206655,new ItemStack(BlockDustStone.ITEM_STONE_233));

        this.addSpellCraftingEffectRecipe(16711680,Effects.WEAKNESS);//300
        //this.addSpellCraftingEffectRecipe(16711765,new ItemStack(BlockDustStone.ITEM_STONE_301));
        //this.addSpellCraftingEffectRecipe(16711850,new ItemStack(BlockDustStone.ITEM_STONE_302));
        //this.addSpellCraftingEffectRecipe(16711935,new ItemStack(BlockDustStone.ITEM_STONE_303));
        //this.addSpellCraftingEffectRecipe(16777045,Effects.HEALTH_BOOST);//310
        //this.addSpellCraftingEffectRecipe(16733525,new ItemStack(BlockDustStone.ITEM_STONE_311));
        //this.addSpellCraftingEffectRecipe(16733610,new ItemStack(BlockDustStone.ITEM_STONE_312));
        //this.addSpellCraftingEffectRecipe(16733695,new ItemStack(BlockDustStone.ITEM_STONE_313));

        //this.addSpellCraftingEffectRecipe(16755200,new ItemStack(BlockDustStone.ITEM_STONE_320));
        this.addSpellCraftingEffectRecipe(16777045,Effects.MINING_FATIGUE);//321
        //this.addSpellCraftingEffectRecipe(16755370,new ItemStack(BlockDustStone.ITEM_STONE_322));
        //this.addSpellCraftingEffectRecipe(16755455,new ItemStack(BlockDustStone.ITEM_STONE_323));
        this.addSpellCraftingEffectRecipe(16776960,Effects.HUNGER);//330
        //this.addSpellCraftingEffectRecipe(16777045,Effects.ABSORPTION);//331
        //this.addSpellCraftingEffectRecipe(16777130,new ItemStack(BlockDustStone.ITEM_STONE_332));
        this.addSpellCraftingEffectRecipe(16777215,Effects.LEVITATION);//333
    }

    public void addSpellCraftingEffectRecipe(int colorIn, Effect effect)
    {
        if (getResult(colorIn) != Effects.UNLUCK) { return;}
        this.spellCraftingEffectList.put(colorIn, effect);
    }

    /**
     * Returns the smelting result of an item.
     */
    public Effect getResult(int colorIn)
    {
        for (Map.Entry<Integer, Effect> entry : this.spellCraftingEffectList.entrySet())
        {
            if (colorIn == entry.getKey())
            {
                return entry.getValue();
            }
        }

        //ToDo: "Default" effect
        return Effects.UNLUCK;
    }

    public Map<Integer, Effect> getEffectList()
    {
        return this.spellCraftingEffectList;
    }

}