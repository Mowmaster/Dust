package com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.T15;

import com.mowmaster.dust.Block.BlockEntities.DustJar.DustJarBlockItem;
import com.mowmaster.dust.Block.BlockEntities.Pedestal.BasePedestalBlockEntity;
import com.mowmaster.dust.Capabilities.Dust.DustMagic;
import com.mowmaster.dust.Capabilities.Dust.IDustHandler;
import com.mowmaster.dust.Configs.DustConfig;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import com.mowmaster.dust.Items.Filters.IPedestalFilter;
import com.mowmaster.dust.Items.Upgrades.Pedestal.IPedestalUpgrade;
import com.mowmaster.dust.Networking.DustPacketHandler;
import com.mowmaster.dust.Networking.DustPacketParticles;
import com.mowmaster.dust.Recipes.MachineBlockRenderItemsRecipe;
import com.mowmaster.dust.Recipes.MachineBlockRepairItemsRecipe;
import com.mowmaster.dust.References.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.mowmaster.dust.References.Constants.MODID;

public class ScrollCrafterBlockEntity_T15 extends BlockEntity {

    private LazyOptional<IItemHandler> repairItemsHandler = LazyOptional.of(this::createRepairItemsHandler);
    private LazyOptional<IItemHandler> tableItemHandler = LazyOptional.of(this::createTableItemHandler);
    private LazyOptional<IDustHandler> dustHandler = LazyOptional.of(this::createhandlerDust);
    private DustMagic dustMagic = new DustMagic(-1, 0);
    private int dustCapacity = 1000;
    private List<ItemStack> stacksList = new ArrayList<>();
    private List<ItemStack> stacksListTable = new ArrayList<>();
    private List<ItemStack> repairStackList = new ArrayList<>();
    private List<String> repairTagList = new ArrayList<>();
    private boolean isRepaired = false;
    public BlockPos getPos() { return this.worldPosition; }

    public ScrollCrafterBlockEntity_T15(BlockPos p_155229_, BlockState p_155230_) {
        super(DeferredBlockEntityTypes.CRAFTER_SCROLL_T15.get(), p_155229_, p_155230_);
    }

    protected List<ItemStack> getListofRepairs(Level level, ItemStack stackIn) {
        if(!level.isClientSide())
        {
            Container cont = Constants.blankContainer;
            cont.setItem(0,stackIn);
            List<MachineBlockRepairItemsRecipe> recipes = level.getRecipeManager().getRecipesFor(MachineBlockRepairItemsRecipe.MACHINE_REPAIR_ITEMS,cont,level);
            List<ItemStack> stackie = new ArrayList<>();
            for(MachineBlockRepairItemsRecipe recipe : recipes)
            {
                if(!recipe.getResultItem().isEmpty()) stackie.add(recipe.getResultItem());
            }
            return recipes.size() > 0 ? stackie : new ArrayList<>();
        }
        return new ArrayList<>();
    }

    protected List<String> getListofRepairTags(Level level, ItemStack stackIn) {
        Container cont = Constants.blankContainer;
        cont.setItem(0,stackIn);
        List<MachineBlockRepairItemsRecipe> recipes = level.getRecipeManager().getRecipesFor(MachineBlockRepairItemsRecipe.MACHINE_REPAIR_ITEMS,cont,level);
        List<String> stackie = new ArrayList<>();
        for(MachineBlockRepairItemsRecipe recipe : recipes)
        {
            if(!recipe.getResultItem().isEmpty()) stackie.add(recipe.getResultTag());
        }
        return recipes.size() > 0 ? stackie : new ArrayList<>();
    }

    public List<ItemStack> getRepairListStacks()
    {
        if(repairStackList.size() == 0)
        {
            return getListofRepairs(getLevel(),new ItemStack(DeferredRegisterTileBlocks.BLOCK_CRAFTER_SCROLL_T15.get()));
        }
        else
        {
            return repairStackList;
        }
    }

    public List<String> getRepairListTags()
    {
        if(repairTagList.size() == 0)
        {
            return getListofRepairTags(getLevel(),new ItemStack(DeferredRegisterTileBlocks.BLOCK_CRAFTER_SCROLL_T15.get()));
        }
        else
        {
            return repairTagList;
        }
    }

    private boolean isStackAllowed(ItemStack stack)
    {
        List<ItemStack> allowedStacks = getRepairListStacks();
        List<String> allowedTags = getRepairListTags();

        if(allowedStacks.size()>0)
        {
            for(int i=0; i< allowedStacks.size();i++)
            {
                if(allowedStacks.get(i).getItem().equals(stack.getItem()))
                {
                    return true;
                }
                else if(allowedStacks.get(i).getItem().equals(Items.BARRIER))
                {
                    if(i<allowedTags.size())
                    {
                        if(allowedTags.get(i).length()>0)
                        {
                            String[] parts = allowedTags.get(i).split(":");
                            Tag<Item> GETTAGS = ItemTags.getAllTags().getTag(new ResourceLocation(parts[0], parts[1]));
                            List<Item> items = GETTAGS.getValues();
                            if(items.contains(stack.getItem()))return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean allowItemToBeInsert(ItemStack stack)
    {
        List<ItemStack> allowedStacks = getRepairListStacks();
        List<String> allowedTags = getRepairListTags();
        IItemHandler rh = repairItemsHandler.orElse(null);
        if(rh!=null)
        {
            if(isStackAllowed(stack))
            {
                for(int i=0;i<rh.getSlots();i++)
                {
                    if(i<allowedStacks.size())
                    {
                        if(allowedStacks.get(i).getItem().equals(stack.getItem()) && rh.getStackInSlot(i).isEmpty())
                        {
                            return true;
                        }
                        else if(allowedStacks.get(i).getItem().equals(Items.BARRIER) && rh.getStackInSlot(i).isEmpty())
                        {
                            String[] parts = allowedTags.get(i).split(":");
                            Tag<Item> GETTAGS = ItemTags.getAllTags().getTag(new ResourceLocation(parts[0], parts[1]));
                            List<Item> items = GETTAGS.getValues();
                            if(items.contains(stack.getItem()))return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private int getAllowedSlot(ItemStack stack)
    {
        List<ItemStack> allowedStacks = getRepairListStacks();
        List<String> allowedTags = getRepairListTags();
        IItemHandler rh = repairItemsHandler.orElse(null);
        if(rh!=null)
        {
            if(isStackAllowed(stack))
            {
                for(int i=0;i<rh.getSlots();i++)
                {
                    if(i<allowedStacks.size())
                    {
                        if(allowedStacks.get(i).getItem().equals(stack.getItem()))
                        {
                            return i;
                        }
                        else if(allowedStacks.get(i).getItem().equals(Items.BARRIER))
                        {
                            if(allowedTags.get(i).length()>0)
                            {
                                String[] parts = allowedTags.get(i).split(":");
                                Tag<Item> GETTAGS = ItemTags.getAllTags().getTag(new ResourceLocation(parts[0], parts[1]));
                                List<Item> items = GETTAGS.getValues();
                                if(items.contains(stack.getItem()))return i;
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }


    public void update()
    {
        BlockState state = level.getBlockState(getPos());
        this.level.sendBlockUpdated(getPos(), state, state, 3);
        this.setChanged();
    }

    private IItemHandler createRepairItemsHandler() {
        /*
        * CONFIG IS USED TO SET THIS, USERS MUST DEFINE RECIPES AND CHANGE CONFIG TO MAKE CHANGES TO MACHINES NOW
         */
        int slots = DustConfig.COMMON.repairitemsToCraft_ScrollCrafter_T15.get();
        return new ItemStackHandler(slots) {

            @Override
            protected void onLoad() {

                if(getSlots()<slots)
                {
                    for(int i = 0; i < getSlots(); ++i) {
                        stacksList.add(i,getStackInSlot(i));
                    }
                    setSize(getRepairListStacks().size());
                    for(int j = 0;j<stacksList.size();j++) {
                        setStackInSlot(j, stacksList.get(j));
                    }
                }

                super.onLoad();
            }

            @Override
            protected void onContentsChanged(int slot) {
                if(!(stacksList.size()>0))
                {
                    update();
                }
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return (slot == getAllowedSlot(stack)) && allowItemToBeInsert(stack);
            }
        };
    }

    public boolean isValidItem(ItemStack stack)
    {
        IItemHandler rh = repairItemsHandler.orElse(null);
        if(rh!=null)
        {
            int slot = getAllowedSlot(stack);
            if(slot != -1)return rh.isItemValid(slot,stack);
        }
        return false;
    }

    public boolean isFullyRepaired()
    {
        IItemHandler rh = repairItemsHandler.orElse(null);
        if(isRepaired)
        {
            return isRepaired;
        }
        else {
            if(rh!=null)
            {
                for(int i=0;i<rh.getSlots();i++)
                {
                    isRepaired = false;
                    if(rh.getStackInSlot(i).isEmpty())return false;
                }

                isRepaired = true;
                return true;
            }
        }

        return isRepaired;
    }

    public List<ItemStack> getNextRepairItem()
    {
        if(!isRepaired)
        {
            List<ItemStack> allowedStacks = getRepairListStacks();
            List<String> allowedTags = getRepairListTags();
            IItemHandler rh = repairItemsHandler.orElse(null);
            if(rh!=null)
            {
                for (int i=0;i<rh.getSlots();i++)
                {
                    if(rh.getStackInSlot(i).isEmpty())
                    {
                        ItemStack repairItemForSlot = allowedStacks.get(i);
                        if(repairItemForSlot.getItem().equals(Items.BARRIER))
                        {
                            List<ItemStack> stackie = new ArrayList();
                            if(allowedTags.get(i).length()>0)
                            {
                                String[] parts = allowedTags.get(i).split(":");
                                Tag<Item> GETTAGS = ItemTags.getAllTags().getTag(new ResourceLocation(parts[0], parts[1]));
                                List<Item> items = GETTAGS.getValues();
                                for(Item item : items)
                                {
                                    stackie.add(new ItemStack(item));
                                }
                                return stackie;
                            }
                        }
                        else return Arrays.asList(repairItemForSlot);
                    }
                }
            }
        }

        return null;
    }



    /*============================================================================
    ==============================================================================
    ===========================  REPAIR ITEM START   =============================
    ==============================================================================
    ============================================================================*/

    public boolean addRepairItem(Player player, ItemStack repairItemFromBlock, boolean simulate)
    {
        IItemHandler rh = repairItemsHandler.orElse(null);
        ItemStack insertedItem = repairItemFromBlock.copy();
        insertedItem.setCount(1);
        if(rh!=null)
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
        IItemHandler rh = repairItemsHandler.orElse(null);
        if(rh!=null)
        {
            for(int i=0; i < rh.getSlots();i++)
            {
                if(getRenderAllowed(rh.getStackInSlot(i))) stacksList.add(rh.getStackInSlot(i));
            }
        }

        return stacksList;
    }

    private ItemStack getStoredRepairItemStack(int slot)
    {
        IItemHandler rh = repairItemsHandler.orElse(null);
        if(rh!=null)
        {
            return rh.getStackInSlot(slot);
        }

        return ItemStack.EMPTY;
    }

    /*============================================================================
    ==============================================================================
    ===========================   REPAIR ITEM END    =============================
    ==============================================================================
    ============================================================================*/

    @Nullable
    protected MachineBlockRenderItemsRecipe getRenderRecipe(List<ItemStack> stackIn) {
        Container cont = Constants.blankContainer;
        for(int i=0;i<stackIn.size();i++)
        {
            cont.setItem(i,stackIn.get(i));
        }
        List<MachineBlockRenderItemsRecipe> recipes = getLevel().getRecipeManager().getRecipesFor(MachineBlockRenderItemsRecipe.MACHINE_RENDER_ITEMS,cont,getLevel());
        return getLevel() != null ? (recipes.size() > 0)?(getLevel().getRecipeManager().getRecipesFor(MachineBlockRenderItemsRecipe.MACHINE_RENDER_ITEMS,cont,getLevel()).get(0)):(null) : null;
    }

    protected List<Float> getProcessResultRenderList(MachineBlockRenderItemsRecipe recipe) {
        return (recipe == null)?(new ArrayList<>()):(recipe.getResultList());
    }

    protected boolean getProcessResultRender(MachineBlockRenderItemsRecipe recipe) {
        return (recipe == null)?(false):(recipe.getResultRenderItem());
    }

    protected boolean getProcessResultRenderAsBlock(MachineBlockRenderItemsRecipe recipe) {
        return (recipe == null)?(false):(recipe.getResultRenderAsBlock());
    }

    protected Collection<ItemStack> getProcessResultRenderItem(MachineBlockRenderItemsRecipe recipe) {
        return (recipe == null)?(Arrays.asList(ItemStack.EMPTY)):(Collections.singleton(recipe.getResultItem()));
    }

    public List<Float> getRenderParams(ItemStack repairItem)
    {
        List<ItemStack> stackList = Arrays.asList(new ItemStack(DeferredRegisterTileBlocks.BLOCK_CRAFTER_SCROLL_T15.get()),repairItem);
        MachineBlockRenderItemsRecipe recipe = getRenderRecipe(stackList);
        if(recipe != null)return getProcessResultRenderList(recipe);
        return new ArrayList<>();
    }

    public boolean getRenderAllowed(ItemStack repairItem)
    {
        List<ItemStack> stackList = Arrays.asList(new ItemStack(DeferredRegisterTileBlocks.BLOCK_CRAFTER_SCROLL_T15.get()),repairItem);
        MachineBlockRenderItemsRecipe recipe = getRenderRecipe(stackList);
        if(recipe != null)return getProcessResultRender(recipe);
        return false;
    }

    public boolean getRenderAsBlock(ItemStack repairItem)
    {
        List<ItemStack> stackList = Arrays.asList(new ItemStack(DeferredRegisterTileBlocks.BLOCK_CRAFTER_SCROLL_T15.get()),repairItem);
        MachineBlockRenderItemsRecipe recipe = getRenderRecipe(stackList);
        if(recipe != null)return getProcessResultRenderAsBlock(recipe);
        return false;
    }

    public ItemStack getRenderItem(ItemStack repairItem)
    {
        List<ItemStack> stackList = Arrays.asList(new ItemStack(DeferredRegisterTileBlocks.BLOCK_CRAFTER_SCROLL_T15.get()),repairItem);
        MachineBlockRenderItemsRecipe recipe = getRenderRecipe(stackList);
        Collection<ItemStack> jsonResults = getProcessResultRenderItem(recipe);
        if(!jsonResults.iterator().next().isEmpty())
        {
            ItemStack returner = jsonResults.iterator().next();
            if(returner.getItem().equals(Items.BARRIER))return repairItem;
            else return returner;
        }
        return repairItem;
    }






    public IDustHandler createhandlerDust() {
        return new IDustHandler() {

            protected void onContentsChanged()
            {
                syncDustInTableWithContainer();
                update();
            }

            @Override
            public int getTanks() {
                return 1;
            }

            @NotNull
            @Override
            public DustMagic getDustMagicInTank(int tank) {
                return dustMagic;
            }

            @Override
            public int getTankCapacity(int tank) {
                return dustCapacity;
            }

            @Override
            public boolean isDustValid(int tank, @NotNull DustMagic dustIn) {
                return dustMagic.isDustEqualOrEmpty(dustIn);
            }

            @Override
            public int fill(DustMagic dust, DustAction action) {
                if (dust.isEmpty() || !isDustValid(0,dust))
                {
                    return 0;
                }
                if (action.simulate())
                {
                    if (dustMagic.isEmpty())
                    {
                        return Math.min(getTankCapacity(0), dust.getDustAmount());
                    }
                    if (!dustMagic.isDustEqual(dust))
                    {
                        return 0;
                    }
                    return Math.min(getTankCapacity(0) - dustMagic.getDustAmount(), dust.getDustAmount());
                }
                if (dustMagic.isEmpty())
                {
                    dustMagic = new DustMagic(dust.getDustColor(), Math.min(getTankCapacity(0), dust.getDustAmount()));
                    onContentsChanged();
                    return dustMagic.getDustAmount();
                }
                if (!dustMagic.isDustEqual(dust))
                {
                    return 0;
                }
                int filled = getTankCapacity(0) - dustMagic.getDustAmount();

                if (dust.getDustAmount() < filled)
                {
                    dustMagic.grow(dust.getDustAmount());
                    filled = dust.getDustAmount();
                }
                else
                {
                    dustMagic.setDustAmount(getTankCapacity(0));
                }
                if (filled > 0)
                    onContentsChanged();
                return filled;
            }

            @NotNull
            @Override
            public DustMagic drain(DustMagic dust, DustAction action) {
                if (dust.isEmpty() || !dust.isDustEqual(dustMagic))
                {
                    return new DustMagic(-1, 0);
                }
                return drain(dust.getDustAmount(), action);
            }

            @NotNull
            @Override
            public DustMagic drain(int maxDrain, DustAction action) {
                int drained = maxDrain;
                if (dustMagic.getDustAmount() < drained)
                {
                    drained = dustMagic.getDustAmount();
                }
                DustMagic magic = new DustMagic(dustMagic.getDustColor(), drained);
                if (action.execute() && drained > 0)
                {
                    dustMagic.shrink(drained);
                    onContentsChanged();
                }
                return magic;
            }
        };
    }

    private boolean hasDustContainer()
    {
        IItemHandler rh = repairItemsHandler.orElse(null);
        if(rh!=null)
        {
            for(int i=0; i < rh.getSlots();i++)
            {
                if(rh.getStackInSlot(i).getItem() instanceof DustJarBlockItem)return true;
            }
        }

        return false;
    }

    private List<Integer> getDustContainerSlots()
    {
        List<Integer> list = new ArrayList<>();
        IItemHandler rh = repairItemsHandler.orElse(null);
        if(rh!=null)
        {
            for(int i=0; i < rh.getSlots();i++)
            {
                if(rh.getStackInSlot(i).getItem() instanceof DustJarBlockItem)list.add(i);
            }
        }

        return list;
    }

    private void syncDustInTableWithContainer()
    {
        if(hasDustContainer())
        {
            int slot = getDustContainerSlots().get(0);
            ItemStack getDustContainerStack = getStoredRepairItemStack(slot);
            if(getDustContainerStack.getItem() instanceof DustJarBlockItem)
            {
                DustMagic.setDustMagicInStack(getDustContainerStack,dustMagic);
            }
        }
    }

    /*============================================================================
    ==============================================================================
    ===========================    DUST   START      =============================
    ==============================================================================
    ============================================================================*/

    public boolean hasDust()
    {
        IDustHandler h = dustHandler.orElse(null);
        if(!h.getDustMagicInTank(0).isEmpty())return true;
        return false;
    }

    public DustMagic getStoredDust()
    {
        IDustHandler h = dustHandler.orElse(null);
        if(!h.getDustMagicInTank(0).isEmpty())return h.getDustMagicInTank(0);
        return new DustMagic(-1, 0);
    }

    public void setDustCapacity(int capacity)
    {
        this.dustCapacity = capacity;
    }

    public int getDustCapacity()
    {
        IDustHandler h = dustHandler.orElse(null);
        return h.getTankCapacity(0);
    }

    public int spaceForDust()
    {
        return getDustCapacity()-getStoredDust().getDustAmount();
    }

    public boolean canAcceptDust(DustMagic dustMagicIn)
    {
        IDustHandler h = dustHandler.orElse(null);
        return h.isDustValid(0,dustMagicIn);
    }

    public DustMagic removeDust(DustMagic dustMagicToRemove, IDustHandler.DustAction action)
    {
        IDustHandler h = dustHandler.orElse(null);
        update();
        return h.drain(dustMagicToRemove,action);
    }

    public DustMagic removeDust(int dustAmountToRemove, IDustHandler.DustAction action)
    {
        IDustHandler h = dustHandler.orElse(null);
        update();
        return h.drain(new DustMagic(getStoredDust().getDustColor(),dustAmountToRemove),action);
    }

    public int addDust(DustMagic dustMagicIn, IDustHandler.DustAction action)
    {
        IDustHandler h = dustHandler.orElse(null);
        update();
        return h.fill(dustMagicIn,action);
    }

    /*============================================================================
    ==============================================================================
    ===========================     DUST   END       =============================
    ==============================================================================
    ============================================================================*/


    private IItemHandler createTableItemHandler() {
        /*
         * CONFIG IS USED TO SET THIS, USERS MUST DEFINE RECIPES AND CHANGE CONFIG TO MAKE CHANGES TO MACHINES NOW
         */

        return new ItemStackHandler(2) {

            @Override
            protected void onLoad() {

                if(getSlots()<2)
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
            protected void onContentsChanged(int slot) {
                if(!(stacksListTable.size()>0))
                {
                    update();
                }
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 0 && stack.getItem().equals(Items.PAPER)) return true;
                if (slot == 1 && stack.getItem().equals(Items.GOLD_NUGGET)) return true;
                return false;
            }
        };
    }

    /*============================================================================
    ==============================================================================
    ===========================     ITEM START       =============================
    ==============================================================================
    ============================================================================*/

    public int getSlotForItemStack(ItemStack stackIncoming)
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

    public boolean hasItem(int slot)
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
            if(hasItem(slot))return h.getStackInSlot(slot);
        }
        return ItemStack.EMPTY;
    }

    public ItemStack removeItem(int numToRemove, int slot) {
        IItemHandler h = tableItemHandler.orElse(null);
        ItemStack stack = ItemStack.EMPTY;
        if(h!=null)
        {
            stack = h.extractItem(slot,numToRemove,false);
        }

        return stack;
    }

    public ItemStack removeItem(int slot) {
        IItemHandler h = tableItemHandler.orElse(null);
        ItemStack stack = ItemStack.EMPTY;
        if(h!=null)
        {
            stack = h.extractItem(0,getItemInTable(slot).getMaxStackSize(),false);
        }

        return stack;
    }

    public boolean addItem(ItemStack stackIncoming ,boolean simulate)
    {
        if(isItemAllowedInTable(stackIncoming))
        {
            IItemHandler h = tableItemHandler.orElse(null);
            int slot = getAllowedSlot(stackIncoming);
            if(h.isItemValid(0,stackIncoming))
            {
                if(hasItem(slot))
                {
                    if(ItemHandlerHelper.canItemStacksStack(getItemInTable(slot),stackIncoming))
                    {
                        if(!simulate)
                        {
                            h.insertItem(0, stackIncoming.copy(), false);
                            update();
                        }
                        return true;
                    }
                }
                else
                {
                    if(!simulate)
                    {
                        h.insertItem(0, stackIncoming.copy(), false);
                        update();
                    }
                    return true;
                }
            }
        }

        return false;
    }

    public int getSlotSizeLimit(int slot)
    {
        IItemHandler h = tableItemHandler.orElse(null);
        return (h != null)?(h.getSlotLimit(slot)):(0);
    }

    /*============================================================================
    ==============================================================================
    ===========================      ITEM END        =============================
    ==============================================================================
    ============================================================================*/

    public void spawnItemStack(Level worldIn, double x, double y, double z, ItemStack stack) {
        Random RANDOM = new Random();
        double d0 = (double) EntityType.ITEM.getWidth();
        double d1 = 1.0D - d0;
        double d2 = d0 / 2.0D;
        double d3 = Math.floor(x) + RANDOM.nextDouble() * d1 + d2;
        double d4 = Math.floor(y) + RANDOM.nextDouble() * d1;
        double d5 = Math.floor(z) + RANDOM.nextDouble() * d1 + d2;

        while(!stack.isEmpty()) {
            ItemEntity itementity = new ItemEntity(worldIn, d3, d4, d5, stack.split(RANDOM.nextInt(21) + 10));
            float f = 0.05F;
            itementity.lerpMotion(RANDOM.nextGaussian() * 0.05000000074505806D, RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D, RANDOM.nextGaussian() * 0.05000000074505806D);
            worldIn.addFreshEntity(itementity);
        }
    }

    public void dropInventoryItems(Level worldIn, BlockPos pos) {
        IItemHandler h = repairItemsHandler.orElse(null);
        syncDustInTableWithContainer();
        for(int i = 0; i < h.getSlots(); ++i) {
            spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(i));
        }
    }

    public void dropInventoryItemsPrivate(Level worldIn, BlockPos pos) {
        IItemHandler ph = tableItemHandler.orElse(null);
        for(int i = 0; i < ph.getSlots(); ++i) {
            spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), ph.getStackInSlot(i));
        }
    }

    public void tick()
    {
        if(getLevel().getGameTime()%20 == 0){if(!isFullyRepaired()){ DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX(),getPos().getY()+1,getPos().getZ(),255,255,255));}}
    }


    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        CompoundTag invPrivateTag = p_155245_.getCompound("inv_repairs");
        repairItemsHandler.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(invPrivateTag));

        if(p_155245_.contains("inv_repairsList"))
        {
            List<ItemStack> repairList = new ArrayList<>();
            CompoundTag invTag = p_155245_.getCompound("inv_repairsList");
            ItemStackHandler handler = new ItemStackHandler();
            ((INBTSerializable<CompoundTag>) handler).deserializeNBT(invTag);
            for(int i=0;i<handler.getSlots();i++) {repairList.add(handler.getStackInSlot(i));}
            this.repairStackList = repairList;
        }

        this.isRepaired = p_155245_.getBoolean("isRepaired");
        this.dustCapacity = p_155245_.getInt(MODID + "_dustCapacity");
        this.dustMagic = DustMagic.getDustMagicInTag(p_155245_);
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        save(p_187471_);
    }

    @Override
    public CompoundTag save(CompoundTag p_58888_) {
        super.save(p_58888_);

        repairItemsHandler.ifPresent(h -> {
            CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            p_58888_.put("inv_repairs", compound);
        });

        CompoundTag compoundStorage = new CompoundTag();
        ItemStackHandler handler = new ItemStackHandler();
        handler.setSize(getRepairListStacks().size());
        for(int i=0;i<handler.getSlots();i++) {handler.setStackInSlot(i,getRepairListStacks().get(i));}
        compoundStorage = handler.serializeNBT();
        p_58888_.put("inv_repairsList",compoundStorage);

        p_58888_.putBoolean("isRepaired",isRepaired);

        p_58888_.putInt(MODID + "_dustCapacity", this.getDustCapacity());
        return DustMagic.setDustMagicInTag(p_58888_,this.dustMagic);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return save(new CompoundTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net,pkt);
        BlockState state = this.level.getBlockState(getPos());
        this.handleUpdateTag(pkt.getTag());
        this.level.sendBlockUpdated(getPos(), state, state, 3);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }


    @Override
    public void setRemoved() {
        super.setRemoved();
        if(this.repairItemsHandler != null) {
            this.repairItemsHandler.invalidate();
        }
        if(this.dustHandler != null) {
            this.dustHandler.invalidate();
        }
    }
}
