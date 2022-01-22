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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class DustFurnacesBaseBlockEntity extends Tier1BaseBlockEntity
{
    private LazyOptional<IItemHandler> allowedInputsHandler = LazyOptional.of(this::createAllowedInputsHandler);
    private List<ItemStack> stacksListAllowedInputsHandler = new ArrayList<>();
    private int burnTime = 0;
    private int currentCookTime = 0;
    private int maxCookTime = 0;
    private float xpForOutput = 0F;
    private ItemStack outputItemStack = ItemStack.EMPTY;
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
            protected int getStackLimit(int slot, @NotNull ItemStack stack) {
                if(slot == 0)return 1;
                else if(slot == 1)return 4;
                else return 0;
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
            List<BlastingRecipe> optional = recipeManager.getRecipesFor(RecipeType.BLASTING, inv, world);
            List<SmokingRecipe> optional2 = recipeManager.getRecipesFor(RecipeType.SMOKING, inv, world);
            List<SmeltingRecipe> optional1 = recipeManager.getRecipesFor(RecipeType.SMELTING, inv, world);
            return getLevel() != null ? (optional.size() > 0 || optional2.size() > 0)?(null):((optional1.size() > 0)?(optional1.stream().findFirst().get()):(null)) : null;
        }
        else return null;
    }

    protected ItemStack getProcessResults(AbstractCookingRecipe recipe) {
        return (recipe == null)?(ItemStack.EMPTY):(recipe.getResultItem());
    }

    protected float getProcessResultsXP(AbstractCookingRecipe recipe) {
        return (recipe == null)?(0.0f):(recipe.getExperience());
    }

    protected int getProcessCookTime(AbstractCookingRecipe recipe) {
        return (recipe == null)?(0):(recipe.getCookingTime());
    }

    public boolean isAllowedInputItemForMachineSlot(int slot, ItemStack stack)
    {
        if(slot == 0 && !stack.getItem().equals(Items.LAVA_BUCKET) && ForgeHooks.getBurnTime(stack, getRecipeTypeForBlock())>0 && getInputItemInSlot(0).getCount()<1)return true;
        else if(slot == 1 && getRecipe(getLevel(), stack) !=null)return true;
        else return false;
    }

    public int getAllowedInputSlotForMachine(ItemStack stack)
    {
        if(!stack.getItem().equals(Items.LAVA_BUCKET) && ForgeHooks.getBurnTime(stack, getRecipeTypeForBlock())>0 && getInputItemInSlot(0).getCount()<1)return 0;
        else if(getRecipe(getLevel(), stack) !=null)return 1;
        else return -1;
    }

    /*============================================================================
    ==============================================================================
    ===========================  INPUT ITEM START    =============================
    ==============================================================================
    ============================================================================*/

    /*
    Returns:
            The remaining ItemStack that was not inserted (if the entire stack is accepted, then return an empty ItemStack).
            May be the same as the input ItemStack if unchanged, otherwise a new ItemStack.
            The returned ItemStack can be safely modified after.
     */
    public ItemStack addInputItem(ItemStack inputItemFromBlock, boolean simulate)
    {
        IItemHandler aih = allowedInputsHandler.orElse(null);
        ItemStack insertedItem = inputItemFromBlock.copy();
        if(aih!=null)
        {
            int slot = getAllowedInputSlotForMachine(insertedItem);
            if(slot==-1)return inputItemFromBlock;
            if(aih.isItemValid(slot,insertedItem))
            {
                if(slot<aih.getSlots())
                {
                    if(aih.insertItem(slot,insertedItem,true).getCount() != inputItemFromBlock.getCount())
                    {
                        if(!simulate)
                        {
                            if(slot==1 && getInputItemInSlot(slot).isEmpty())
                            {
                                AbstractCookingRecipe recipe = getRecipe(getLevel(),insertedItem);
                                this.outputItemStack = getProcessResults(recipe);
                                this.xpForOutput = getProcessResultsXP(recipe);
                                this.maxCookTime = getProcessCookTime(recipe);
                            }
                            return aih.insertItem(slot,insertedItem,false);
                        }
                        return aih.insertItem(slot,insertedItem,true);
                    }
                }
            }
        }

        return inputItemFromBlock;
    }

    public ItemStack getInputItemInSlot(int slot)
    {
        ItemStack returner = ItemStack.EMPTY;
        IItemHandler aih = allowedInputsHandler.orElse(null);
        if(aih!=null)
        {
            returner = aih.getStackInSlot(slot);
        }

        return returner;
    }

    public ItemStack removeItemFromSlot(int slot, int amount, boolean simulate)
    {
        ItemStack returner = ItemStack.EMPTY;
        IItemHandler aih = allowedInputsHandler.orElse(null);
        if(aih!=null)
        {
            //Returns:
            //ItemStack extracted from the slot, must be empty if nothing can be extracted.
            //The returned ItemStack can be safely modified after, so item handlers should return a new or copied stack.
            returner = aih.extractItem(slot,amount,true);
            if(!returner.isEmpty())
            {
                if(!simulate)returner = aih.extractItem(slot,amount,simulate);
                if(slot==1 && getInputItemInSlot(slot).isEmpty())
                {
                    this.outputItemStack = ItemStack.EMPTY;
                    this.xpForOutput = 0F;
                    this.maxCookTime = 0;
                }
            }
        }

        return returner;
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

    public int getBurnTime(){ return this.burnTime; }

    public int calcFuelBurnTime()
    {
        ItemStack burnSlot = getInputItemInSlot(0);
        if(!burnSlot.isEmpty())
        {
            return ForgeHooks.getBurnTime(burnSlot,getRecipeTypeForBlock());
        }
        return 0;
    }

    public void addToBurnTime()
    {
        ItemStack burnSlot = getInputItemInSlot(0);
        if(!burnSlot.isEmpty())
        {
            if(!removeItemFromSlot(0,1,true).isEmpty())
            {
                this.burnTime = calcFuelBurnTime();
                this.isLit = true;
                removeItemFromSlot(0,1,false);
            }
        }
    }


    public void spawnItemStackOutput(Level worldIn, double x, double y, double z, ItemStack stack) {
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
            itementity.lerpMotion(0.1D*RANDOM.nextInt(5),0.1D*RANDOM.nextInt(5),0.1D*RANDOM.nextInt(5));
            //itementity.lerpMotion(RANDOM.nextGaussian() * 0.05000000074505806D, RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D, RANDOM.nextGaussian() * 0.05000000074505806D);
            worldIn.addFreshEntity(itementity);
        }
    }

    @Override
    public void tick()
    {
        super.tick();
        if(!getLevel().isClientSide())
        {
            if(getLevel().getGameTime()%20 == 0)
            {
                if(getBurnTime()>0)
                {
                    this.burnTime -=20;
                    if(getBurnTime()<=5)addToBurnTime();
                    if(getBurnTime()<=0)
                    {
                        this.isLit = false;
                        update();
                    }

                    if(!removeItemFromSlot(1,1,true).isEmpty())
                    {
                        this.currentCookTime+=20;
                        if(this.currentCookTime >= this.maxCookTime)
                        {
                            this.currentCookTime = 0;
                            spawnItemStackOutput(getLevel(), getPos().getX(), getPos().getY()+1, getPos().getZ(), this.outputItemStack.copy());
                            removeItemFromSlot(1,1,false);
                            if(this.xpForOutput>0f)
                            {
                                Random rand = new Random();
                                ExperienceOrb xpEntity = new ExperienceOrb(level,getPos().getX(), getPos().getY()+1, getPos().getZ(),(int)this.xpForOutput);
                                xpEntity.lerpMotion(0.1D*rand.nextInt(5),0.1D*rand.nextInt(5),0.1D*rand.nextInt(5));
                                getLevel().addFreshEntity(xpEntity);
                            }
                        }
                    }
                }
                else
                {
                    if(!getInputItemInSlot(0).isEmpty())addToBurnTime();
                    else if(this.currentCookTime>0)this.currentCookTime-=20;
                }

                if(isFurnaceLit())
                {
                    if(getBurnTime()>=1000)
                    {
                        DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX()+0.0,getPos().getY()-1,getPos().getZ()+0.0,255,255,0));
                        DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX()+1,getPos().getY()-1,getPos().getZ()+0.0,255,255,0));
                        DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX()+0,getPos().getY()-1,getPos().getZ()+1,255,255,0));
                        DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX()+1,getPos().getY()-1,getPos().getZ()+1,255,255,0));
                    }
                    else if(getBurnTime()<1000 && getBurnTime()>=200)
                    {
                        DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX()+0.0,getPos().getY()-1,getPos().getZ()+0.0,255,125,0));
                        DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX()+1,getPos().getY()-1,getPos().getZ()+0.0,255,125,0));
                        DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX()+0,getPos().getY()-1,getPos().getZ()+1,255,125,0));
                        DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX()+1,getPos().getY()-1,getPos().getZ()+1,255,125,0));
                    }
                    else if(getBurnTime()<200)
                    {
                        DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX()+0.0,getPos().getY()-1,getPos().getZ()+0.0,255,0,0));
                        DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX()+1,getPos().getY()-1,getPos().getZ()+0.0,255,0,0));
                        DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX()+0,getPos().getY()-1,getPos().getZ()+1,255,0,0));
                        DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX()+1,getPos().getY()-1,getPos().getZ()+1,255,0,0));
                    }
                }
            }

            if(getLevel().getGameTime()%69 == 0)
            {
                if(isFurnaceLit())getLevel().playSound(null,getPos(), SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS,1.0F, 1.0F);
            }
        }
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

        CompoundTag allowedInputTag = p_155245_.getCompound("allowed_inputs");
        allowedInputsHandler.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(allowedInputTag));

        if(p_155245_.contains("output_itemstack"))
        {
            CompoundTag invTag = p_155245_.getCompound("output_itemstack");
            ItemStackHandler handler = new ItemStackHandler();
            ((INBTSerializable<CompoundTag>) handler).deserializeNBT(invTag);
            this.outputItemStack = handler.getStackInSlot(0);
        }

        this.burnTime = p_155245_.getInt("burnTime");
        this.currentCookTime = p_155245_.getInt("currentCookTime");
        this.maxCookTime = p_155245_.getInt("maxCookTime");
        this.xpForOutput = p_155245_.getFloat("xpForOutput");
        this.isLit = p_155245_.getBoolean("isLit");
    }

    @Override
    public CompoundTag save(CompoundTag p_58888_) {
        super.save(p_58888_);

        allowedInputsHandler.ifPresent(h -> {
            CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            p_58888_.put("allowed_inputs", compound);
        });

        CompoundTag compoundStorage = new CompoundTag();
        ItemStackHandler handler = new ItemStackHandler();
        handler.setSize(1);
        handler.setStackInSlot(0,outputItemStack);
        compoundStorage = handler.serializeNBT();
        p_58888_.put("output_itemstack",compoundStorage);

        p_58888_.putInt("burnTime",burnTime);
        p_58888_.putInt("currentCookTime",currentCookTime);
        p_58888_.putInt("maxCookTime",maxCookTime);
        p_58888_.putFloat("xpForOutput",xpForOutput);
        p_58888_.putBoolean("isLit",isLit);

        return p_58888_;
    }
}
