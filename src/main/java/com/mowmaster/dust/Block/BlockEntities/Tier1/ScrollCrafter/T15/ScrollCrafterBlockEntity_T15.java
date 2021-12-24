package com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.T15;

import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import com.mowmaster.dust.Recipes.CrystalNodeRecipe;
import com.mowmaster.dust.Recipes.MachineBlockRepairItemsRecipe;
import com.mowmaster.dust.References.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ScrollCrafterBlockEntity_T15 extends BlockEntity {


    public ScrollCrafterBlockEntity_T15(BlockPos p_155229_, BlockState p_155230_) {
        super(DeferredBlockEntityTypes.CRAFTER_SCROLL_T15.get(), p_155229_, p_155230_);
    }

    @Nullable
    protected MachineBlockRepairItemsRecipe getRecipe(Level level, ItemStack stackIn) {
        Container cont = Constants.blankContainer;
        cont.setItem(0,stackIn);
        List<MachineBlockRepairItemsRecipe> recipes = level.getRecipeManager().getRecipesFor(MachineBlockRepairItemsRecipe.MACHINE_REPAIR_ITEMS,cont,level);
        return recipes.size() > 0 ? level.getRecipeManager().getRecipesFor(MachineBlockRepairItemsRecipe.MACHINE_REPAIR_ITEMS,cont,level).get(0) : null;
    }

    protected Collection<ItemStack> getProcessResults(MachineBlockRepairItemsRecipe recipe) {
        return (recipe == null)?(Arrays.asList(ItemStack.EMPTY)):(Collections.singleton(recipe.getResultItem()));
    }

    protected Collection<String> getProcessResultsTag(MachineBlockRepairItemsRecipe recipe) {
        return (recipe == null)?(Arrays.asList("")):(Collections.singleton(recipe.getResultTag()));
    }

    public void getRepairList()
    {
        Collection<ItemStack> jsonResults = getProcessResults(getRecipe(getLevel(),new ItemStack(DeferredRegisterTileBlocks.BLOCK_CRAFTER_SCROLL_T15.get())));
        System.out.println(jsonResults);
        System.out.println(jsonResults.size());
        for(ItemStack result : jsonResults)
        {
            if(result.getItem().equals(Blocks.BARRIER.asItem()))
            {
                System.out.println(getProcessResultsTag(getRecipe(getLevel(),new ItemStack(DeferredRegisterTileBlocks.BLOCK_CRAFTER_SCROLL_T15.get()))));
            }
            else
            {
                System.out.println(result);
            }

        }

    }












    public static void tick()
    {

    }
}
