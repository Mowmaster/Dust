package com.mowmaster.dust.Block.BlockEntities.Tier1;

import com.mowmaster.dust.Block.BlockEntities.DustJar.DustJarBlockItem;
import com.mowmaster.dust.Capabilities.Dust.DustMagic;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class Tier1BaseBlockEntity extends BlockEntity {

    private LazyOptional<IItemHandler> repairItemsHandler = LazyOptional.of(this::createRepairItemsHandler);
    private List<ItemStack> stacksList = new ArrayList<>();
    private List<ItemStack> repairStackList = new ArrayList<>();
    private List<String> repairTagList = new ArrayList<>();
    private boolean isRepaired = false;
    public DustMagic dustMagic = new DustMagic(-1, 0);
    public BlockPos getPos() { return this.worldPosition; }

    public Tier1BaseBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    public void update()
    {
        BlockState state = level.getBlockState(getPos());
        this.level.sendBlockUpdated(getPos(), state, state, 3);
        this.setChanged();
    }

    public int getRepairSlotsForRepairs()
    {
        return 1;
    }


    private IItemHandler createRepairItemsHandler() {
        /*
         * CONFIG IS USED TO SET THIS, USERS MUST DEFINE RECIPES AND CHANGE CONFIG TO MAKE CHANGES TO MACHINES NOW
         */
        int slots = getRepairSlotsForRepairs();
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

    public ItemStack getStoredRepairItemStack(int slot)
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

    public List<ItemStack> getListofRepairs(Level level, ItemStack stackIn) {
        if(!level.isClientSide())
        {
            if(stackIn.isEmpty()) return new ArrayList<>();

            Container cont = Constants.getContainer(2);
            cont.setItem(-1,stackIn);
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

    public List<String> getListofRepairTags(Level level, ItemStack stackIn) {
        if(stackIn.isEmpty()) return new ArrayList<>();

        Container cont = Constants.getContainer(2);
        cont.setItem(-1,stackIn);
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
            return getListofRepairs(getLevel(),new ItemStack(getBlockForThisBlockEntity()));
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
            return getListofRepairTags(getLevel(),new ItemStack(getBlockForThisBlockEntity()));
        }
        else
        {
            return repairTagList;
        }
    }

    public boolean isStackAllowed(ItemStack stack)
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

    public boolean allowItemToBeInsert(ItemStack stack)
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

    public int getAllowedSlot(ItemStack stack)
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

    public void syncDustInTableWithContainer()
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


    @Nullable
    protected MachineBlockRenderItemsRecipe getRenderRecipe(List<ItemStack> stackIn) {
        Container cont = Constants.getContainer(2);
        for(int i=0;i<stackIn.size();i++)
        {
            if(cont.canPlaceItem(i,stackIn.get(i))) {
                cont.setItem(-1, stackIn.get(i));
            }
        }
        List<MachineBlockRenderItemsRecipe> recipes = getLevel().getRecipeManager().getRecipesFor(MachineBlockRenderItemsRecipe.MACHINE_RENDER_ITEMS,cont,getLevel());
        return getLevel() != null ? (recipes.size() > 0)?(recipes.get(0)):(null) : null;
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

    public Block getBlockForThisBlockEntity()
    {
        return Blocks.AIR;
    }

    public List<Float> getRenderParams(ItemStack repairItem)
    {
        List<ItemStack> stackList = Arrays.asList(new ItemStack(getBlockForThisBlockEntity()),repairItem);
        MachineBlockRenderItemsRecipe recipe = getRenderRecipe(stackList);
        if(recipe != null)return getProcessResultRenderList(recipe);
        return new ArrayList<>();
    }

    public boolean getRenderAllowed(ItemStack repairItem)
    {
        List<ItemStack> stackList = Arrays.asList(new ItemStack(getBlockForThisBlockEntity()),repairItem);
        MachineBlockRenderItemsRecipe recipe = getRenderRecipe(stackList);
        if(recipe != null)return getProcessResultRender(recipe);
        return false;
    }

    public boolean getRenderAsBlock(ItemStack repairItem)
    {
        List<ItemStack> stackList = Arrays.asList(new ItemStack(getBlockForThisBlockEntity()),repairItem);
        MachineBlockRenderItemsRecipe recipe = getRenderRecipe(stackList);
        if(recipe != null)return getProcessResultRenderAsBlock(recipe);
        return false;
    }

    public ItemStack getRenderItem(ItemStack repairItem)
    {
        List<ItemStack> stackList = Arrays.asList(new ItemStack(getBlockForThisBlockEntity()),repairItem);
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

    public double getRepairParticleHeight(){ return 1.0;}

    public void tick()
    {
        if(getLevel().getGameTime()%20 == 0){if(!isFullyRepaired()){ DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX(),getPos().getY()+getRepairParticleHeight(),getPos().getZ(),255,255,255));}}
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
        this.dustMagic = DustMagic.getDustMagicInTag(p_155245_);
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        save(p_187471_);
    }

    /*
    https://discord.com/channels/313125603924639766/915304642668290119/933514186267459658

    When you want to save some BE to something else:
- saveWithFullMetadata()if you want the full data (includes the position of the block, this may be problematic for certain applications)
- saveWithId() if you want to be able to reconstruct a BE from this data without knowing beforehand which BE type you need (for example picking up a BE with a special "carrier" item could use this)
- saveWithoutMetadata() if you only need the actual data but not the BE type or position
     */

    public CompoundTag save(CompoundTag p_58888_) {

        repairItemsHandler.ifPresent(h -> {
            CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            p_58888_.put("inv_repairs", compound);
        });

        List<ItemStack> listed = getRepairListStacks();
        if(listed.size()>0)
        {
            CompoundTag compoundStorage = new CompoundTag();
            ItemStackHandler handler = new ItemStackHandler();
            handler.setSize(listed.size());
            for(int i=0;i<handler.getSlots();i++) {handler.setStackInSlot(i,listed.get(i));}
            compoundStorage = handler.serializeNBT();
            p_58888_.put("inv_repairsList",compoundStorage);
        }

        p_58888_.putBoolean("isRepaired",isRepaired);

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
    }

}
