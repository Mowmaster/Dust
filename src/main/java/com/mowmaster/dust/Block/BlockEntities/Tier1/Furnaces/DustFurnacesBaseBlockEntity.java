package com.mowmaster.dust.Block.BlockEntities.Tier1.Furnaces;

import com.mowmaster.dust.Block.BlockEntities.DustJar.DustJarBlockItem;
import com.mowmaster.dust.Block.BlockEntities.Tier1.Tier1BaseBlock;
import com.mowmaster.dust.Block.BlockEntities.Tier1.Tier1BaseBlockEntity;
import com.mowmaster.dust.Capabilities.Dust.DustMagic;
import com.mowmaster.dust.Configs.DustConfig;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import com.mowmaster.dust.Networking.DustPacketHandler;
import com.mowmaster.dust.Networking.DustPacketParticles;
import com.mowmaster.dust.Recipes.MachineBlockRenderItemsRecipe;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.References.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DustFurnacesBaseBlockEntity extends Tier1BaseBlockEntity
{
    private LazyOptional<IItemHandler> allowedInputsHandler = LazyOptional.of(this::createAllowedInputsHandler);
    private List<ItemStack> stacksListAllowedInputsHandler = new ArrayList<>();
    private boolean isLit = false;

    public DustFurnacesBaseBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    /*============================================================================
    ==============================================================================
    =====================   BASE CLASS OVERRIDES - START   =======================
    ==============================================================================
    ============================================================================*/

    @Override
    public void update()
    {
        BlockState state = level.getBlockState(getPos());
        this.level.sendBlockUpdated(getPos(), state, state, 3);
        this.setChanged();
    }

    /*============================================================================
    ==============================================================================
    =====================    BASE CLASS OVERRIDES - END    =======================
    ==============================================================================
    ============================================================================*/

    public RecipeType getRecipeTypeForBlock()
    {
        return RecipeType.SMELTING;
    }


    private IItemHandler createAllowedInputsHandler() {
        /*
         * CONFIG IS USED TO SET THIS, USERS MUST DEFINE RECIPES AND CHANGE CONFIG TO MAKE CHANGES TO MACHINES NOW
         */
        int slots = 2;
        return new ItemStackHandler(slots) {

            @Override
            protected void onLoad() {

                if(getSlots()<slots)
                {
                    for(int i = 0; i < getSlots(); ++i) {
                        stacksListAllowedInputsHandler.add(i,getStackInSlot(i));
                    }
                    setSize(getRepairListStacks().size());
                    for(int j = 0;j<stacksListAllowedInputsHandler.size();j++) {
                        setStackInSlot(j, stacksListAllowedInputsHandler.get(j));
                    }
                }

                super.onLoad();
            }

            @Override
            protected void onContentsChanged(int slot) {
                if(!(stacksListAllowedInputsHandler.size()>0))
                {
                    update();
                }
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return isAllowedInputItemForMachineSlot(slot,stack);
            }
        };
    }

    @Nullable
    protected AbstractCookingRecipe getRecipe(Level world, ItemStack stackIn) {
        if (world == null) return null;

        Container inv = Constants.getContainer(1);
        inv.setItem(-1,stackIn);

        RecipeManager recipeManager = world.getRecipeManager();
        if(getRecipeTypeForBlock() == RecipeType.BLASTING)
        {
            List<BlastingRecipe> optional = recipeManager.getRecipesFor(RecipeType.BLASTING, inv, world);
            return getLevel() != null ? (optional.size() > 0)?(optional.stream().findFirst().get()):(null) : null;
        }
        else if(getRecipeTypeForBlock() == RecipeType.SMOKING)
        {
            List<SmokingRecipe> optional2 = recipeManager.getRecipesFor(RecipeType.SMOKING, inv, world);
            return getLevel() != null ? (optional2.size() > 0)?(optional2.stream().findFirst().get()):(null) : null;
        }
        else if(getRecipeTypeForBlock() == RecipeType.SMELTING)
        {
            List<SmeltingRecipe> optional1 = recipeManager.getRecipesFor(RecipeType.SMELTING, inv, world);
            return getLevel() != null ? (optional1.size() > 0)?(optional1.stream().findFirst().get()):(null) : null;
        }
        else return null;
    }

    protected ItemStack getProcessResults(AbstractCookingRecipe recipe, ItemStack stackIn) {
        return (recipe == null)?(ItemStack.EMPTY):(recipe.getResultItem());
    }

    protected float getProcessResultsXP(AbstractCookingRecipe recipe) {
        return (recipe == null)?(0.0f):(recipe.getExperience());
    }

    public boolean isAllowedInputItemForMachineSlot(int slot, ItemStack stack)
    {
        if(slot == 0 && !stack.getItem().equals(Items.LAVA_BUCKET) && ForgeHooks.getBurnTime(stack, getRecipeTypeForBlock())>0)return true;
        else if(slot == 1 && getRecipe(getLevel(), stack) !=null)return true;
        else return false;
    }

    public int getAllowedInputSlotForMachine(ItemStack stack)
    {
        if(!stack.getItem().equals(Items.LAVA_BUCKET) && ForgeHooks.getBurnTime(stack, getRecipeTypeForBlock())>0)return true;
        else if(slot == 1 && getRecipe(getLevel(), stack) !=null)return true;
        else return false;
    }

    /*============================================================================
    ==============================================================================
    ===========================  INPUT ITEM START    =============================
    ==============================================================================
    ============================================================================*/

    public boolean addRepairItem(Player player, ItemStack inputItemFromBlock, boolean simulate)
    {
        IItemHandler aih = allowedInputsHandler.orElse(null);
        ItemStack insertedItem = inputItemFromBlock.copy();
        if(aih!=null)
        {
            int slot = getAllowedSlot(insertedItem);
            if(rh.isItemValid(slot,insertedItem))
            {
                if(slot<rh.getSlots())
                {
                    if(!simulate)
                    {
                        rh.insertItem(slot,insertedItem,false);
                        if(insertedItem.getItem() instanceof DustJarBlockItem)
                        {
                            dustMagic = DustMagic.getDustMagicInItemStack(insertedItem);
                        }
                    }
                    return true;
                }
            }
        }

        return false;
    }

    public List<ItemStack> getListOfInsertedItemsToDisplay()
    {
        List<ItemStack> stacksList = new ArrayList<>();
        IItemHandler rh = allowedInputsHandler.orElse(null);
        if(rh!=null)
        {
            for(int i=0; i < rh.getSlots();i++)
            {
                if(getRenderAllowed(rh.getStackInSlot(i))) stacksList.add(rh.getStackInSlot(i));
            }
        }

        return stacksList;
    }

    public ItemStack getStoredRepairItemStack(int slot)
    {
        IItemHandler rh = allowedInputsHandler.orElse(null);
        if(rh!=null)
        {
            return rh.getStackInSlot(slot);
        }

        return ItemStack.EMPTY;
    }

    /*============================================================================
    ==============================================================================
    ===========================    INPUT ITEM END    =============================
    ==============================================================================
    ============================================================================*/







    public boolean isFurnaceLit()
    {
        return this.isLit;
    }

    public void dropInputsItems(Level worldIn, BlockPos pos) {
        IItemHandler h = allowedInputsHandler.orElse(null);
        for(int i = 0; i < h.getSlots(); ++i) {
            spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(i));
        }
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);

        this.isLit = p_155245_.getBoolean("isLit");
    }

    @Override
    public CompoundTag save(CompoundTag p_58888_) {
        super.save(p_58888_);
        p_58888_.putBoolean("isLit",isLit);

        return p_58888_;
    }
}
