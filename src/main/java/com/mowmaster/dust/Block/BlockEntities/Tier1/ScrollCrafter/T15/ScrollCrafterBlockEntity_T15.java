package com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.T15;

import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
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
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ScrollCrafterBlockEntity_T15 extends BlockEntity {

    private LazyOptional<IItemHandler> repairItemsHandler = LazyOptional.of(this::createRepairItemsHandler);
    private List<ItemStack> stacksList = new ArrayList<>();
    private List<ItemStack> repairStackList = new ArrayList<>();
    private List<String> repairTagList = new ArrayList<>();
    private boolean isRepaired = false;
    public BlockPos getPos() { return this.worldPosition; }

    public ScrollCrafterBlockEntity_T15(BlockPos p_155229_, BlockState p_155230_) {
        super(DeferredBlockEntityTypes.CRAFTER_SCROLL_T15.get(), p_155229_, p_155230_);
    }

    protected List<ItemStack> getListofRepairs(@NotNull Level level, ItemStack stackIn) {
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
        //Make the slot size the same as the number of repair items
        return new ItemStackHandler(getRepairListStacks().size()) {

            @Override
            protected void onLoad() {
                if(getSlots()<getRepairListStacks().size())
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

    @Nullable
    protected MachineBlockRenderItemsRecipe getRenderRecipe(Level level, List<ItemStack> stackIn) {
        Container cont = Constants.blankContainer;
        for(int i=0;i<stackIn.size();i++)
        {
            cont.setItem(i,stackIn.get(i));
        }
        List<MachineBlockRenderItemsRecipe> recipes = level.getRecipeManager().getRecipesFor(MachineBlockRenderItemsRecipe.MACHINE_RENDER_ITEMS,cont,level);
        return recipes.size() > 0 ? level.getRecipeManager().getRecipesFor(MachineBlockRenderItemsRecipe.MACHINE_RENDER_ITEMS,cont,level).get(0) : null;
    }

    protected List<Float> getProcessResultRenderList(MachineBlockRenderItemsRecipe recipe) {
        return (recipe == null)?(new ArrayList<>()):(recipe.getResultList());
    }

    protected boolean getProcessResultRender(MachineBlockRenderItemsRecipe recipe) {
        return (recipe == null)?(false):(recipe.getResultRenderItem());
    }

    protected Collection<ItemStack> getProcessResultRenderItem(MachineBlockRenderItemsRecipe recipe) {
        return (recipe == null)?(Arrays.asList(ItemStack.EMPTY)):(Collections.singleton(recipe.getResultItem()));
    }

    public List<Float> getRenderParams(ItemStack repairItem)
    {
        List<ItemStack> stackList = Arrays.asList(new ItemStack(DeferredRegisterTileBlocks.BLOCK_CRAFTER_SCROLL_T15.get()),repairItem);
        MachineBlockRenderItemsRecipe recipe = getRenderRecipe(getLevel(),stackList);
        if(recipe != null)return getProcessResultRenderList(recipe);
        return new ArrayList<>();
    }

    public boolean getRenderAllowed(ItemStack repairItem)
    {
        List<ItemStack> stackList = Arrays.asList(new ItemStack(DeferredRegisterTileBlocks.BLOCK_CRAFTER_SCROLL_T15.get()),repairItem);
        MachineBlockRenderItemsRecipe recipe = getRenderRecipe(getLevel(),stackList);
        if(recipe != null)return getProcessResultRender(recipe);
        return false;
    }

    public ItemStack getRenderItem(ItemStack repairItem)
    {
        List<ItemStack> stackList = Arrays.asList(new ItemStack(DeferredRegisterTileBlocks.BLOCK_CRAFTER_SCROLL_T15.get()),repairItem);
        MachineBlockRenderItemsRecipe recipe = getRenderRecipe(getLevel(),stackList);
        Collection<ItemStack> jsonResults = getProcessResultRenderItem(recipe);
        if(!jsonResults.iterator().next().isEmpty())
        {
            ItemStack returner = jsonResults.iterator().next();
            if(returner.getItem().equals(Items.BARRIER))return repairItem;
            else return returner;
        }
        return repairItem;
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

    /*============================================================================
    ==============================================================================
    ===========================   REPAIR ITEM END    =============================
    ==============================================================================
    ============================================================================*/

    public void tick()
    {
        if(getLevel().getGameTime()%20 == 0){if(!isFullyRepaired()){ DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX(),getPos().getY()+1,getPos().getZ(),255,255,255));}}
    }



    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        CompoundTag invPrivateTag = p_155245_.getCompound("inv_repairs");
        repairItemsHandler.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(invPrivateTag));

        List<ItemStack> repairList = new ArrayList<>();
        if(p_155245_.contains("inv_repairsList"))
        {
            CompoundTag invTag = p_155245_.getCompound("inv_repairsList");
            ItemStackHandler handler = new ItemStackHandler();
            ((INBTSerializable<CompoundTag>) handler).deserializeNBT(invTag);
            for(int i=0;i<handler.getSlots();i++) {repairList.add(handler.getStackInSlot(i));}
            this.repairStackList = repairList;
        }

        this.isRepaired = p_155245_.getBoolean("isRepaired");
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

        return p_58888_;
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
    }
}
