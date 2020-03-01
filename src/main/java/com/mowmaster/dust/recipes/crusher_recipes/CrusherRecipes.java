package com.mowmaster.dust.recipes.crusher_recipes;

import com.mowmaster.dust.blocks.machines.BlockDustCloud;
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
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,0),new ItemStack(BlockDustCloud.redDust,32));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,1),new ItemStack(BlockDustCloud.blueDust,32));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,2),new ItemStack(BlockDustCloud.yellowDust,32));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,3),new ItemStack(BlockDustCloud.purpleDust,32));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,4),new ItemStack(BlockDustCloud.greenDust,32));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,5),new ItemStack(BlockDustCloud.orangeDust,32));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,6),new ItemStack(BlockDustCloud.whiteDust,32));
        this.addCrusherRecipe(new ItemStack(ItemRegistry.crystal,1,7),new ItemStack(BlockDustCloud.blackDust,32));

        this.addCrushingRecipeForBlock(Blocks.REDSTONE_ORE,new ItemStack(BlockDustCloud.redstoneDust,4));
        this.addCrushingRecipeForBlock(Blocks.IRON_ORE,new ItemStack(BlockDustCloud.ironDust,2));
        this.addCrushingRecipeForBlock(Blocks.GOLD_ORE,new ItemStack(BlockDustCloud.goldDust,2));
        this.addCrushing(Items.REEDS,new ItemStack(BlockDustCloud.sugarDust,2));
        //this.addCrushing(Items.WHEAT,new ItemStack(BlockDustCloud.wheatDust,1));
        //this.addCrushing(Items.POTATO,new ItemStack(BlockDustCloud.potatoDust,1));
        //this.addCrushing(Items.POISONOUS_POTATO,new ItemStack(BlockDustCloud.potatoDust,1));
        this.addCrushing(Items.BLAZE_ROD,new ItemStack(BlockDustCloud.blazeDust,3));


        this.addCrushingRecipeForBlock(Blocks.COBBLESTONE,new ItemStack(Blocks.GRAVEL,1));
        this.addCrushingRecipeForBlock(Blocks.GRAVEL,new ItemStack(Blocks.SAND,1));
        this.addCrushingRecipeForBlock(Blocks.SANDSTONE,new ItemStack(Blocks.SAND,4,0));
        this.addCrushingRecipeForBlock(Blocks.RED_SANDSTONE,new ItemStack(Blocks.SAND,4,1));

        for(int i =0;i<16;i++)
        {
            this.addCrusherRecipe(new ItemStack(Blocks.CONCRETE,1,i),new ItemStack(Blocks.GRAVEL,1));
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
