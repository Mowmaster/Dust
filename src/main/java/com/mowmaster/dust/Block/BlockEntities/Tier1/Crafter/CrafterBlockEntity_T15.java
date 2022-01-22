package com.mowmaster.dust.Block.BlockEntities.Tier1.Crafter;

import com.mowmaster.dust.Block.BlockEntities.Tier1.Tier1BaseBlockEntity;
import com.mowmaster.dust.Capabilities.Dust.DustMagic;
import com.mowmaster.dust.Capabilities.Dust.IDustHandler;
import com.mowmaster.dust.Configs.DustConfig;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import com.mowmaster.dust.Items.Scrolls.EffectItemBase;
import com.mowmaster.dust.Items.Scrolls.ScrollBase;
import com.mowmaster.dust.Recipes.CrusherRecipe;
import com.mowmaster.dust.Recipes.CrystalClusterModifiers;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.References.Constants;
import com.mowmaster.dust.References.EffectPickerReference;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mowmaster.dust.References.Constants.MODID;

/*public class CrafterBlockEntity_T15 extends Tier1BaseBlockEntity {

    private LazyOptional<IItemHandler> tableItemHandler = LazyOptional.of(this::createTableItemHandler);
    private List<ItemStack> stacksListTable = new ArrayList<>();
    private ItemStack getCraftingOutput = ItemStack.EMPTY;

    public CrafterBlockEntity_T15(BlockPos p_155229_, BlockState p_155230_) {
        super(DeferredBlockEntityTypes.CRAFTER_T15.get(), p_155229_, p_155230_);
    }

    *//*============================================================================
    ==============================================================================
    =====================   BASE CLASS OVERRIDES - START   =======================
    ==============================================================================
    ============================================================================*//*

    @Override
    public void update()
    {
        calcCrafterRecipe();
        BlockState state = level.getBlockState(getPos());
        this.level.sendBlockUpdated(getPos(), state, state, 3);
        this.setChanged();
    }

    @Override
    public int getRepairSlotsForRepairs()
    {
        return DustConfig.COMMON.repairitemsToCraft_Crafter_T15.get();
    }

    @Override
    public Block getBlockForThisBlockEntity()
    {
        return DeferredRegisterTileBlocks.BLOCK_CRAFTER_T15.get();
    }

    *//*============================================================================
    ==============================================================================
    =====================    BASE CLASS OVERRIDES - END    =======================
    ==============================================================================
    ============================================================================*//*



    *//*============================================================================
    ==============================================================================
    ===========================     ITEM START       =============================
    ==============================================================================
    ============================================================================*//*

    private IItemHandler createTableItemHandler() {
        *//*
         * CONFIG IS USED TO SET THIS, USERS MUST DEFINE RECIPES AND CHANGE CONFIG TO MAKE CHANGES TO MACHINES NOW
         *//*

        return new ItemStackHandler(9) {

            @Override
            protected void onLoad() {

                if(getSlots()<3)
                {
                    for(int i = 0; i < getSlots(); ++i) {
                        stacksListTable.add(i,getStackInSlot(i));
                    }
                    setSize(getRepairListStacks().size());
                    for(int j = 0;j<stacksListTable.size();j++) {
                        setStackInSlot(j, stacksListTable.get(j));
                    }
                }

                super.onLoad();
            }

            @Override
            protected int getStackLimit(int slot, @NotNull ItemStack stack) {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot) {
                if(!(stacksListTable.size()>0))
                {
                    update();
                }
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return getStackInSlot(slot).isEmpty();
            }
        };
    }

    public int getSlotForItemStackInTable(ItemStack stackIncoming)
    {
        IItemHandler h = tableItemHandler.orElse(null);
        if(h!=null)
        {
            for(int i=0;i<h.getSlots();i++)
            {
                if(h.isItemValid(i,stackIncoming))return i;
            }
        }

        return -1;
    }

    public boolean isItemAllowedInTable(ItemStack stackIncoming)
    {
        IItemHandler h = tableItemHandler.orElse(null);
        if(h!=null)
        {
            for(int i=0;i<h.getSlots();i++)
            {
                if(h.isItemValid(i,stackIncoming))return true;
            }
        }

        return false;
    }

    public boolean hasItemInTable(int slot)
    {
        IItemHandler h = tableItemHandler.orElse(null);
        if(h!=null)
        {
            if(!(h.getStackInSlot(slot).isEmpty()))return true;

        }
        return false;
    }

    public ItemStack getItemInTable(int slot)
    {
        IItemHandler h = tableItemHandler.orElse(null);
        if(h!=null)
        {
            if(hasItemInTable(slot))return h.getStackInSlot(slot);
        }
        return ItemStack.EMPTY;
    }

    public ItemStack removeItemInTable(int slot, int numToRemove) {
        IItemHandler h = tableItemHandler.orElse(null);
        ItemStack stack = ItemStack.EMPTY;
        if(h!=null)
        {
            stack = h.extractItem(slot,numToRemove,false);
        }

        return stack;
    }

    public ItemStack removeItemInTable(int slot) {
        IItemHandler h = tableItemHandler.orElse(null);
        ItemStack stack = ItemStack.EMPTY;
        if(h!=null)
        {
            stack = h.extractItem(0,getItemInTable(slot).getMaxStackSize(),false);
        }

        return stack;
    }

    public ItemStack addItemInTable(ItemStack stackIncoming ,boolean simulate)
    {
        ItemStack returner = stackIncoming.copy();
        if(isItemAllowedInTable(stackIncoming))
        {
            IItemHandler h = tableItemHandler.orElse(null);
            int slot = getSlotForItemStackInTable(stackIncoming);
            if(h.isItemValid(slot,stackIncoming))
            {
                if(hasItemInTable(slot))
                {
                    if(ItemHandlerHelper.canItemStacksStack(getItemInTable(slot),stackIncoming))
                    {
                        returner = h.insertItem(slot, stackIncoming.copy(), simulate);
                        if(!simulate) {update();}
                        return returner;
                    }
                }
                else
                {
                    returner = h.insertItem(slot, stackIncoming.copy(), simulate);
                    if(!simulate) {update();}
                    return returner;
                }
            }
        }

        return returner;
    }

    public int getSlotSizeLimitInTable(int slot)
    {
        IItemHandler h = tableItemHandler.orElse(null);
        return (h != null)?(h.getSlotLimit(slot)):(0);
    }

    public int getLastUsedSlot()
    {
        IItemHandler h = tableItemHandler.orElse(null);
        List<Integer> getSlotsFilled = new ArrayList<Integer>();
        if(h != null)
        {
            for(int i=0;i<h.getSlots();i++)
            {
                if(!h.getStackInSlot(i).isEmpty())getSlotsFilled.add(i);
            }
        }

        if(getSlotsFilled.size()>0)return getSlotsFilled.get(getSlotsFilled.size()-1);

        return -1;
    }

    public ItemStack removeLastItemFromTable()
    {
        int lastSlot = getLastUsedSlot();
        if(lastSlot != -1)
        {
            ItemStack returnerItem = removeItemInTable(lastSlot);
            if(returnerItem.getItem().equals(Items.BARRIER))return ItemStack.EMPTY;
            else return returnerItem;
        }
        return ItemStack.EMPTY;
    }

    public ItemStack addNextItemToTable(ItemStack stackIn)
    {
        ItemStack incomingStack = stackIn.copy();
        if(incomingStack.isEmpty())incomingStack = new ItemStack(Items.BARRIER,1);
        if(addItemInTable(incomingStack,true).getCount() != incomingStack.getCount())
        {
            return addItemInTable(incomingStack,false);
        }

        return stackIn;
    }

    public List<ItemStack> getStacksInTable()
    {
        IItemHandler h = tableItemHandler.orElse(null);
        List<ItemStack> stacks = new ArrayList<>();
        if(h != null)
        {
            for(int i=0;i<h.getSlots();i++)
            {
                if(!h.getStackInSlot(i).isEmpty())stacks.add(h.getStackInSlot(i));
            }
        }

        return stacks;
    }



    *//*============================================================================
    ==============================================================================
    ===========================      ITEM END        =============================
    ==============================================================================
    ============================================================================*//*





    *//*============================================================================
    ==============================================================================
    ===========================   CRAFTING START     =============================
    ==============================================================================
    ============================================================================*//*

    @Nullable
    public CraftingRecipe getRecipe(Level world) {
        if (world == null) return null;

        List<ItemStack> stackListed = getStacksInTable();

        CraftingContainer cont = Constants.getContainerCrafting(1,1);
        if(stackListed.size()>1 && stackListed.size()<=4)cont = Constants.getContainerCrafting(2,2);
        if(stackListed.size()>4)cont = Constants.getContainerCrafting(3,3);
        for(int i=0; i<stackListed.size();i++)
        {
            ItemStack stackToSet = (stackListed.get(i).getItem().equals(Items.BARRIER))?(ItemStack.EMPTY):(stackListed.get(i));
            cont.setItem(-1,stackToSet);
        }

        RecipeManager recipeManager = world.getRecipeManager();
        Optional<CraftingRecipe> optional = recipeManager.getRecipeFor(RecipeType.CRAFTING,cont,getLevel());
        return getLevel() != null ? (optional.isPresent())?(optional.stream().findFirst().get()):(null) : null;

    }

    public ItemStack getProcessResults() {
        CraftingRecipe recipe = getRecipe(getLevel());
        return (recipe == null)?(ItemStack.EMPTY):(recipe.getResultItem());
    }

    private void calcCrafterRecipe()
    {
        ItemStack craftedResult = getProcessResults();
        this.getCraftingOutput = craftedResult;
    }

    public ItemStack getCraftingOutput()
    {
        return this.getCraftingOutput;
    }

    *//*============================================================================
    ==============================================================================
    ===========================    CRAFTING END      =============================
    ==============================================================================
    ============================================================================*//*



    public void dropInventoryItemsPrivate(Level worldIn, BlockPos pos) {
        IItemHandler ph = tableItemHandler.orElse(null);
        for(int i = 0; i < ph.getSlots(); ++i) {
            spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), ph.getStackInSlot(i));
        }
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);

        CompoundTag invTableTag = p_155245_.getCompound("inv_table");
        tableItemHandler.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(invTableTag));

        if(p_155245_.contains("stackCrafterOutput"))
        {
            CompoundTag invTag = p_155245_.getCompound("stackCrafterOutput");
            ItemStackHandler handler = new ItemStackHandler();
            ((INBTSerializable<CompoundTag>) handler).deserializeNBT(invTag);
            this.getCraftingOutput = handler.getStackInSlot(0);
        }
    }

    @Override
    public CompoundTag save(CompoundTag p_58888_) {
        super.save(p_58888_);

        tableItemHandler.ifPresent(h -> {
            CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            p_58888_.put("inv_table", compound);
        });

        CompoundTag compoundStorage = new CompoundTag();
        ItemStackHandler handler = new ItemStackHandler();
        handler.setSize(1);
        handler.setStackInSlot(0,this.getCraftingOutput);
        compoundStorage = handler.serializeNBT();
        p_58888_.put("stackCrafterOutput",compoundStorage);

        return p_58888_;
    }
}*/
