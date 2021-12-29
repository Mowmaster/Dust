package com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.T15;

import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import com.mowmaster.dust.Networking.DustPacketHandler;
import com.mowmaster.dust.Networking.DustPacketParticles;
import com.mowmaster.dust.Recipes.MachineBlockRepairItemsRecipe;
import com.mowmaster.dust.References.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.*;

public class ScrollCrafterBlockEntity_T15 extends BlockEntity {

    private LazyOptional<IItemHandler> repairItemsHandler = LazyOptional.of(this::createRepairItemsHandler);
    private List<ItemStack> stacksList = new ArrayList<>();
    private List<ItemStack> repairStackList = null;
    private List<String> repairTagList = null;
    private boolean isRepaired = false;
    public BlockPos getPos() { return this.worldPosition; }

    public ScrollCrafterBlockEntity_T15(BlockPos p_155229_, BlockState p_155230_) {
        super(DeferredBlockEntityTypes.CRAFTER_SCROLL_T15.get(), p_155229_, p_155230_);
    }

    protected List<ItemStack> getListofRepairs(Level level, ItemStack stackIn) {
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
        if(repairStackList == null)
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
        if(repairTagList == null)
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
                if(getSlots()<10)
                {
                    for(int i = 0; i < getSlots(); ++i) {
                        stacksList.add(i,getStackInSlot(i));
                    }
                    setSize(10);
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

    public void isValidItem(ItemStack stack)
    {
        IItemHandler rh = repairItemsHandler.orElse(null);
        if(rh!=null)
        {
            int slot = getAllowedSlot(stack);
            if(slot != -1)System.out.println(slot);
        }
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

    public void tick()
    {
        if(getLevel().getGameTime()%20 == 0){if(!isFullyRepaired()){ DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX(),getPos().getY()+1,getPos().getZ(),255,255,255));}}
    }
}
