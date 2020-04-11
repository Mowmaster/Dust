package com.mowmaster.dust.recipes.crusher_recipes;

import com.google.common.collect.Maps;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.Map;


//Code directly taken from FurnaceRecipes
public class CrusherUpgradeRecipes
{
    private static final CrusherUpgradeRecipes CRUSHER_RECIPES = new CrusherUpgradeRecipes();
    /** The list of smelting results. */
    private final Map<ItemStack, ItemStack> crusherList = Maps.<ItemStack, ItemStack>newHashMap();

    /**
     * Returns an instance of FurnaceRecipes.
     */
    public static CrusherUpgradeRecipes instance()
    {
        return CRUSHER_RECIPES;
    }

    private boolean thaumcraft=(Loader.isModLoaded("thaumcraft"))?(true):(false);

    private CrusherUpgradeRecipes()
    {
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,0),new ItemStack(ItemRegistry.dust,32,0));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,1),new ItemStack(ItemRegistry.dust,32,1));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,2),new ItemStack(ItemRegistry.dust,32,2));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,3),new ItemStack(ItemRegistry.dust,32,3));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,4),new ItemStack(ItemRegistry.dust,32,4));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,5),new ItemStack(ItemRegistry.dust,32,5));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,6),new ItemStack(ItemRegistry.dust,32,6));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,7),new ItemStack(ItemRegistry.dust,32,7));


        this.addCrushing(Items.REEDS,new ItemStack(Items.SUGAR,2));
        this.addCrushing(Items.WHEAT_SEEDS,new ItemStack(Blocks.GRASS,1));
        this.addCrushing(Items.BEETROOT_SEEDS,new ItemStack(Blocks.GRASS,1));
        this.addCrushing(Items.MELON_SEEDS,new ItemStack(Blocks.VINE,1));
        this.addCrushing(Items.PUMPKIN_SEEDS,new ItemStack(Blocks.VINE,1));
        this.addCrushing(Items.POTATO,new ItemStack(Items.POTATO,1));
        this.addCrushing(Items.POISONOUS_POTATO,new ItemStack(Items.POTATO,1));

        this.addCrushingRecipeForBlock(Blocks.COBBLESTONE,new ItemStack(Blocks.GRAVEL,1));
        this.addCrushingRecipeForBlock(Blocks.GRAVEL,new ItemStack(Blocks.SAND,1));
        this.addCrushingRecipeForBlock(Blocks.SANDSTONE,new ItemStack(Blocks.SAND,4,0));
        this.addCrushingRecipeForBlock(Blocks.RED_SANDSTONE,new ItemStack(Blocks.SAND,4,1));

        for(int i =0;i<16;i++)
        {
            this.addCrusherRecipe(new ItemStack(Blocks.CONCRETE,1,i),new ItemStack(Blocks.CONCRETE_POWDER,1,i));
        }

        this.addCrushingRecipeForOreBlock("rodBlaze",new ItemStack(Items.BLAZE_POWDER,3));
        this.addCrushingRecipeForOreBlock("bone",new ItemStack(Items.DYE,4,15));
        this.addCrushingRecipeForOreBlock("oreCoal",new ItemStack(Items.COAL,4));
        this.addCrushingRecipeForOreBlock("oreLapis",new ItemStack(Items.DYE,4,4));
        this.addCrushingRecipeForOreBlock("oreDiamond",new ItemStack(Items.DIAMOND,4));
        this.addCrushingRecipeForOreBlock("oreRedstone",new ItemStack(Items.REDSTONE,4));
        this.addCrushingRecipeForOreBlock("oreEmerald",new ItemStack(Items.EMERALD,4));
        this.addCrushingRecipeForOreBlock("oreQuartz",new ItemStack(Items.QUARTZ,4));
        this.addCrushingRecipeForOreBlock("oreIron",new ItemStack(ItemRegistry.dust,2,9));
        this.addCrushingRecipeForOreBlock("oreGold",new ItemStack(ItemRegistry.dust,2,10));

        this.addCrushingRecipeForOreBlock("oreCopper",new ItemStack(ItemRegistry.dust_compat,2,0));
        this.addCrushingRecipeForOreBlock("oreAluminum",new ItemStack(ItemRegistry.dust_compat,2,1));
        this.addCrushingRecipeForOreBlock("oreAluminium",new ItemStack(ItemRegistry.dust_compat,2,1));
        this.addCrushingRecipeForOreBlock("oreLead",new ItemStack(ItemRegistry.dust_compat,2,2));
        this.addCrushingRecipeForOreBlock("oreSilver",new ItemStack(ItemRegistry.dust_compat,2,3));
        this.addCrushingRecipeForOreBlock("oreNickel",new ItemStack(ItemRegistry.dust_compat,2,4));
        this.addCrushingRecipeForOreBlock("oreUranium",new ItemStack(ItemRegistry.dust_compat,2,5));
        this.addCrushingRecipeForOreBlock("oreTin",new ItemStack(ItemRegistry.dust_compat,2,6));

        if(thaumcraft){
            this.addCrushingRecipeForOreBlock("oreCinnabar",new ItemStack(Item.getByNameOrId("thaumcraft:quicksilver"),2));
            this.addCrushingRecipeForOreBlock("oreAmber",new ItemStack(Item.getByNameOrId("thaumcraft:amber"),2));
        }
    }

    /**
     * Adds a smelting recipe, where the input item is an instance of Block.
     */
    public void addCrushingRecipeForOreBlock(String oreString, ItemStack stack)
    {
        for(ItemStack input : OreDictionary.getOres(oreString))
        {
            if(input != null)
            {
                if (getCrushingResult(input) != ItemStack.EMPTY) { net.minecraftforge.fml.common.FMLLog.log.info("Ignored crushing recipe with conflicting input: {} = {}", input, stack); return; }
                this.crusherList.put(input, stack);
            }
        }
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
