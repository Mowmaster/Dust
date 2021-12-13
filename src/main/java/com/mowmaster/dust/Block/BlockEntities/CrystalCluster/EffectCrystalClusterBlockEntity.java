package com.mowmaster.dust.Block.BlockEntities.CrystalCluster;

import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.Items.ColoredCrystalBase;
import com.mowmaster.dust.Networking.DustPacketHandler;
import com.mowmaster.dust.Networking.DustPacketParticles;
import com.mowmaster.dust.Recipes.BaseBlockEntityFilter;
import com.mowmaster.dust.Recipes.CrystalClusterFuelRecipe;
import com.mowmaster.dust.Recipes.CrystalClusterModifiers;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.References.Constants;
import com.mowmaster.dust.References.EffectReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.mowmaster.dust.Block.BlockEntities.CrystalCluster.EffectCrystalClusterBlock.FACING;
import static com.mowmaster.dust.References.ColorReference.getTrueColorFromInt;

/*
* Alright, so how this will work,
* Fuel and Modifiers will be consumed every X duration (based on fuel used and modifiers)
* This is basically the cost to 'run' the 'machine'
*
* The effect will be given to any mob in the area(based on the base block's 'filter',
* the entities get the effects for about 5 seconds, and it'll be refreshed as long as they're within the AOE
*
 */

public class EffectCrystalClusterBlockEntity extends BlockEntity
{

    private LazyOptional<IItemHandler> handlerCrystalCluster = LazyOptional.of(this::createHandlerCrystalCluster);
    private MobEffectInstance storedPotionEffect = null;
    private int currentFuelTime = 0;
    private boolean showRenderRange = false;
    private List<Integer> allColors = new ArrayList<>();
    private int currentColor = 0;
    public BlockPos getPos() { return this.worldPosition; }
    private EffectCrystalClusterBlockEntity getCluster() { return this; }
    private List<ItemStack> stacksList = new ArrayList<>();


    public EffectCrystalClusterBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(DeferredBlockEntityTypes.CLUSTER.get(), p_155229_, p_155230_);
    }

    public void update()
    {
        BlockState state = level.getBlockState(getPos());
        this.level.sendBlockUpdated(getPos(), state, state, 3);
        this.setChanged();
    }

    private IItemHandler createHandlerCrystalCluster() {
        //going from 5 to 11 slots to future proof things
        return new ItemStackHandler(10) {

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
                if ((slot >=0 && slot<=3) && addCrystal(stack,true)) return true;
                //Allow any block to be used as a base block, but only some will be special filters...
                if ((slot == 4) && stack.getItem() instanceof BlockItem) return true;
                if ((slot == 5) && addFuel(stack,true).getCount()>0) return true;
                if ((slot == 6) && hasFuelItems() && addModifier(stack, true).getCount()>0) return true;
                return false;

                /*
                 * need to make a config for default cluster effect time (in ticks)
                 * 4 slots for crystal cluster
                 * 1 slot for 'base' blocks, to filter which mobs are effected? --Finally make some sort of recipe handler for this???
                 * 1 slot for 'fuel' could also do a recipe handler for this, but default to blaze powder???
                 * 1 slot for modifier (modifiers have a recipe that can say potency and duration modified???
                 * - packs can just enter in the modifier +# is increase -# is decrease (obv effect base is 1x potency so no idea how they could do negative potency...)
                 * - require items to also be tagged??? wouldnt checking for if an item is a valid input be laggy???... dust:crystal_cluster_modifier could be the tag?
                 */
            }
        };
    }



    /*
     * Crystal Start
     */

    public boolean hasCrystals()
    {
        IItemHandler h = handlerCrystalCluster.orElse(null);
        for(int i=0;i<4;i++)
        {
            if(!h.getStackInSlot(i).isEmpty())return true;
        }

        return false;
    }

    public int crystalCount()
    {
        int counter = 0;
        if(hasCrystals())
        {
            IItemHandler h = handlerCrystalCluster.orElse(null);
            for(int i=0;i<4;i++)
            {
                if(!h.getStackInSlot(i).isEmpty())counter+=h.getStackInSlot(i).getCount();
            }
        }

        return counter;
    }

    public int getCurrentColor()
    {
        return this.currentColor;
    }

    public boolean addCrystal(ItemStack stackIncoming, boolean simulate)
    {
        if(stackIncoming.getItem() instanceof ColoredCrystalBase)
        {
            if(crystalCount()<4)
            {
                if(!simulate)
                {
                    int nextAvailableSlot = 0;
                    IItemHandler h = handlerCrystalCluster.orElse(null);
                    for(int i=0;i<4;i++)
                    {
                        if(h.getStackInSlot(i).isEmpty())
                        {
                            nextAvailableSlot=i;
                            break;
                        }
                    }
                    ItemStack crystalToAdd = stackIncoming.copy();
                    crystalToAdd.setCount(1);
                    h.insertItem(nextAvailableSlot,crystalToAdd,simulate);

                    int color = getCurrentColor();
                    int newColor = ColorReference.getColorFromItemStackInt(crystalToAdd);
                    int mixedColor = (color != newColor)?((color==0)?(newColor):(ColorReference.mixColorsInt(color,newColor))):(color);
                    System.out.println("New Color: "+newColor);
                    System.out.println("Mixed Color: "+mixedColor);
                    this.currentColor = mixedColor;
                    this.allColors.add(mixedColor);

                    update();

                    return true;
                }

                return true;
            }
        }

        return false;
    }

    public ItemStack removeCrystal()
    {
        ItemStack returner = ItemStack.EMPTY;

        if(hasCrystals())
        {
            int nextAvailableSlot = 0;
            IItemHandler h = handlerCrystalCluster.orElse(null);
            for(int i=0;i<4;i++)
            {
                if(h.getStackInSlot(i).isEmpty())nextAvailableSlot=i;
                break;
            }
            int lastSlot = nextAvailableSlot-1;

            int color = getCurrentColor();
            int newColor = allColors.get(lastSlot-1);
            this.currentColor = newColor;
            this.allColors.remove(lastSlot);
            //Default Effect to 5 seconds or 100 ticks as that'll be whats applied to the entities
            MobEffectInstance newInstance = new MobEffectInstance(EffectReference.getIntEffect(newColor),100,calculateModifiedPotency(),false,false,false,null);
            this.storedPotionEffect = newInstance;
            update();

            return h.extractItem(lastSlot,1,false);
        }

        return returner;
    }

    /*
     * Crystal End
     */



    /*
     * Fuel Start
     */
    @Nullable
    protected CrystalClusterFuelRecipe getRecipe(Level level, ItemStack stackIn) {
        Container container = Constants.blankContainer;
        container.setItem(0,stackIn);
        List<CrystalClusterFuelRecipe> recipes = level.getRecipeManager().getRecipesFor(CrystalClusterFuelRecipe.CRYSTALCLUSTERFUEL,container,level);
        return recipes.size() > 0 ? level.getRecipeManager().getRecipesFor(CrystalClusterFuelRecipe.CRYSTALCLUSTERFUEL,container,level).get(0) : null;
    }

    protected int getProcessResultFuelAmount(CrystalClusterFuelRecipe recipe) {
        return (recipe == null)?(0):(recipe.getResultFuelValue());
    }

    protected int getProcessResultPotencyLimit(CrystalClusterFuelRecipe recipe) {
        return (recipe == null)?(0):(recipe.getResultPotencyCap());
    }



    public boolean hasFuelItems()
    {
        IItemHandler h = handlerCrystalCluster.orElse(null);
        if(h.getStackInSlot(5).isEmpty())return false;

        return true;
    }

    public boolean isFuelItem(ItemStack stack)
    {
        return getProcessResultFuelAmount(getRecipe(getLevel(),stack)) > 0;
    }

    public ItemStack getFuelStack()
    {
        if(hasFuelItems())
        {
            IItemHandler h = handlerCrystalCluster.orElse(null);
            return h.getStackInSlot(5);
        }

        return ItemStack.EMPTY;
    }

    public boolean doFuelsMatch(ItemStack stackIncoming)
    {
        if(hasFuelItems())
        {
            ItemStack currentFuel = getFuelStack();
            return ItemHandlerHelper.canItemStacksStack(currentFuel,stackIncoming);
        }

        return false;
    }


    // Returns the stack that can/will be added.
    public ItemStack addFuel(ItemStack stack, boolean simulate)
    {
        if(isFuelItem(stack))
        {
            if(hasFuelItems())
            {
                if(doFuelsMatch(stack))
                {
                    ItemStack currentFuel = getFuelStack();
                    int spaceAvailable = currentFuel.getMaxStackSize() - currentFuel.getCount();
                    int countToAdd = (stack.getCount()>spaceAvailable)?(spaceAvailable):(stack.getCount());
                    if(countToAdd > 0)
                    {
                        if(!simulate)
                        {
                            ItemStack returner = stack.copy();
                            returner.setCount(countToAdd);
                            IItemHandler h = handlerCrystalCluster.orElse(null);
                            h.insertItem(5,returner,simulate);

                            return returner;
                        }
                        else
                        {
                            ItemStack returner = stack.copy();
                            returner.setCount(countToAdd);
                            return returner;
                        }
                    }
                }
            }
            else
            {
                int spaceAvailable = stack.getMaxStackSize();
                int countToAdd = (stack.getCount()>spaceAvailable)?(spaceAvailable):(stack.getCount());
                if(countToAdd > 0)
                {
                    if(!simulate)
                    {
                        ItemStack returner = stack.copy();
                        returner.setCount(countToAdd);
                        IItemHandler h = handlerCrystalCluster.orElse(null);
                        h.insertItem(5,returner,simulate);

                        return returner;
                    }
                    else
                    {
                        ItemStack returner = stack.copy();
                        returner.setCount(countToAdd);
                        return returner;
                    }
                }
            }
        }

        return ItemStack.EMPTY;
    }

    public ItemStack removeFuel(int removalAmount)
    {
        if(hasFuelItems())
        {
            ItemStack returnStack = getFuelStack().copy();
            //-1 as an option to remove all
            int countToRemove = (removalAmount == -1)?(returnStack.getCount()):(removalAmount);
            returnStack.setCount(countToRemove);
            IItemHandler h = handlerCrystalCluster.orElse(null);
            h.extractItem(5,countToRemove,false);
            return returnStack;
        }
        return ItemStack.EMPTY;
    }

    /*
     * Fuel End
     */



    /*
     * Modifier Start
     */

    @Nullable
    protected CrystalClusterModifiers getRecipeModifier(Level level, ItemStack stackIn) {
        Container container = Constants.blankContainer;
        container.setItem(0,stackIn);
        List<CrystalClusterModifiers> recipes = level.getRecipeManager().getRecipesFor(CrystalClusterModifiers.CRYSTALCLUSTERMODIFIER,container,level);
        return recipes.size() > 0 ? level.getRecipeManager().getRecipesFor(CrystalClusterModifiers.CRYSTALCLUSTERMODIFIER,container,level).get(0) : null;
    }

    protected double getProcessResultModifierDuration(CrystalClusterModifiers recipe) {
        return (recipe == null)?(0.0):(recipe.getDurationModifier());
    }

    protected double getProcessResultModifierPotency(CrystalClusterModifiers recipe) {
        return (recipe == null)?(0.0):(recipe.getPotencyModifier());
    }



    public boolean hasModifierItems()
    {
        IItemHandler h = handlerCrystalCluster.orElse(null);
        if(h.getStackInSlot(6).isEmpty())return false;

        return true;
    }

    //Also has check for if fuel supports modifier
    public boolean isAcceptedModifierItem(ItemStack stack)
    {
        double durationMod = getProcessResultModifierDuration(getRecipeModifier(getLevel(),stack));
        double potencyMod = getProcessResultModifierPotency(getRecipeModifier(getLevel(),stack));
        //add 1 since potencyMod needs over 1 to be more effective by default, but default effect potency is 0
        int fuelAllowedPotency = getProcessResultPotencyLimit(getRecipe(getLevel(),getFuelStack()))+1;
        if(potencyMod > fuelAllowedPotency)return false;
        //Since we only check for INT values, and +/- ones count, but 0 would result null values in our maths, so those are bad.
        if(durationMod > 0.0 || durationMod < 0.0)return true;
        if(potencyMod > 0.0 || potencyMod < 0.0)return true;

        return false;
    }

    public ItemStack getModifierStack()
    {
        if(hasModifierItems())
        {
            IItemHandler h = handlerCrystalCluster.orElse(null);
            return h.getStackInSlot(6);
        }

        return ItemStack.EMPTY;
    }

    public boolean doModifiersMatch(ItemStack stackIncoming)
    {
        if(hasModifierItems())
        {
            ItemStack currentModifier = getModifierStack();
            return ItemHandlerHelper.canItemStacksStack(currentModifier,stackIncoming);
        }

        return false;
    }


    // Returns the stack that can/will be added.
    public ItemStack addModifier(ItemStack stack, boolean simulate)
    {
        if(isAcceptedModifierItem(stack))
        {
            if(hasModifierItems())
            {
                if(doModifiersMatch(stack))
                {
                    ItemStack currentModifier = getModifierStack();
                    int spaceAvailable = currentModifier.getMaxStackSize() - currentModifier.getCount();
                    int countToAdd = (stack.getCount()>spaceAvailable)?(spaceAvailable):(stack.getCount());
                    if(countToAdd > 0)
                    {
                        if(!simulate)
                        {
                            ItemStack returner = stack.copy();
                            returner.setCount(countToAdd);
                            IItemHandler h = handlerCrystalCluster.orElse(null);
                            h.insertItem(6,returner,simulate);

                            return returner;
                        }
                        else
                        {
                            ItemStack returner = stack.copy();
                            returner.setCount(countToAdd);
                            return returner;
                        }
                    }
                }
            }
            //For When Adding new Modifiers
            else
            {
                int spaceAvailable = stack.getMaxStackSize();
                int countToAdd = (stack.getCount()>spaceAvailable)?(spaceAvailable):(stack.getCount());
                if(countToAdd > 0)
                {
                    if(!simulate)
                    {
                        ItemStack returner = stack.copy();
                        returner.setCount(countToAdd);
                        IItemHandler h = handlerCrystalCluster.orElse(null);
                        h.insertItem(6,returner,simulate);

                        return returner;
                    }
                    else
                    {
                        ItemStack returner = stack.copy();
                        returner.setCount(countToAdd);
                        return returner;
                    }
                }
            }
        }

        return ItemStack.EMPTY;
    }

    public ItemStack removeModifier(int removalAmount)
    {
        if(hasModifierItems())
        {
            ItemStack returnStack = getModifierStack().copy();
            //-1 as an option to remove all
            int countToRemove = (removalAmount == -1)?(returnStack.getCount()):(removalAmount);
            returnStack.setCount(countToRemove);
            IItemHandler h = handlerCrystalCluster.orElse(null);
            h.extractItem(6,countToRemove,false);

            return returnStack;
        }
        return ItemStack.EMPTY;
    }

    public int calculateFuelModifiedDuration()
    {
        int fuelDuration = getProcessResultFuelAmount(getRecipe(getLevel(),getFuelStack()));
        double durationMod = getProcessResultModifierDuration(getRecipeModifier(getLevel(),getModifierStack()));
        if(durationMod > 0)
        {
            return (int)((double)fuelDuration * durationMod);
        }
        else if(durationMod < 0)
        {
            double modifierAbs = Math.abs(durationMod);
            return (int)((double)fuelDuration * modifierAbs);
        }
        else if(fuelDuration > 0)
        {
            return fuelDuration;
        }


        return 0;
    }

    public int calculateModifiedPotency()
    {
        //0 + 1 But will have to subtract 1 later
        double defaultPotency = 1.0;
        double potencyMod = getProcessResultModifierPotency(getRecipeModifier(getLevel(),getModifierStack()));
        double calculatedPotency = defaultPotency * potencyMod;

        //Subtracting 1 'Later'
        return (int)(calculatedPotency-1.0);
    }


    /*
     * Modifier End
     */


    /*
     * Base Block Start
     */

    public boolean hasBaseBlock()
    {
        IItemHandler h = handlerCrystalCluster.orElse(null);
        return !h.getStackInSlot(4).isEmpty();
    }

    public ItemStack getBaseBlock()
    {
        if(hasBaseBlock())
        {
            IItemHandler h = handlerCrystalCluster.orElse(null);
            return h.getStackInSlot(4);
        }

        return new ItemStack(Blocks.STONE);
    }

    //returns Block Swapped out with new Base Block
    public ItemStack addBaseBlock(ItemStack stack)
    {
        ItemStack returnStack = ItemStack.EMPTY;
        ItemStack baseStack = stack.copy();
        baseStack.setCount(1);
        IItemHandler h = handlerCrystalCluster.orElse(null);
        if(hasBaseBlock())returnStack = h.extractItem(4,1,false);
        else returnStack = new ItemStack(Blocks.STONE,1);
        h.insertItem(4,baseStack,false);

        return returnStack;
    }

    /*
     * Base Block End
     */

    public void dropInventoryItems(Level worldIn, BlockPos pos) {
        IItemHandler h = handlerCrystalCluster.orElse(null);
        for(int i = 0; i < h.getSlots(); ++i) {
            spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), h.getStackInSlot(i));
        }
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

    public void setFuelTime(int amountToSet)
    {
        this.currentFuelTime = amountToSet;
    }

    public boolean consumeFuelModsAndReCalcEffect()
    {
        if(hasFuelItems())
        {
            //Calculate Effect before consuming fuel each time, this does mean that any 'unlearned' effects will randomize every time fuel is consumed.
            MobEffectInstance newInstance = new MobEffectInstance(EffectReference.getIntEffect(getCurrentColor()),100,calculateModifiedPotency(),false,false,false,null);
            this.storedPotionEffect = newInstance;
            int duration = calculateFuelModifiedDuration();
            if(duration>0)
            {
                setFuelTime(duration);
                removeFuel(1);
                if(hasModifierItems())removeModifier(1);
                return true;
            }
        }

        return false;
    }

    @Nullable
    protected BaseBlockEntityFilter getRecipeFilterBlock(Level level, ItemStack stackIn) {
        Container container = Constants.blankContainer;
        container.setItem(0,stackIn);
        List<BaseBlockEntityFilter> recipes = level.getRecipeManager().getRecipesFor(BaseBlockEntityFilter.BLOCKENTITYFILTER,container,level);
        return recipes.size() > 0 ? level.getRecipeManager().getRecipesFor(BaseBlockEntityFilter.BLOCKENTITYFILTER,container,level).get(0) : null;
    }

    protected String getProcessResultFilterBlock(BaseBlockEntityFilter recipe) {
        return (recipe == null)?(""):(recipe.getEntityString());
    }

    //This is to check if any of the base blocks are filters, cause if so we have to restrict which entities get the effects.
    public boolean isABaseBlock(ItemStack baseBlock)
    {
        if(!baseBlock.isEmpty())
        {
            if(baseBlock.getItem().equals(Items.EMERALD_BLOCK)) {return true;}
            else if(baseBlock.getItem().equals(Blocks.DIAMOND_BLOCK)) {return true;}
            else if(baseBlock.getItem().equals(Blocks.GOLD_BLOCK)) {return true;}
            else if(baseBlock.getItem().equals(Blocks.LAPIS_BLOCK)) {return true;}
            else if(baseBlock.getItem().equals(Blocks.IRON_BLOCK)) {return true;}
            else if(baseBlock.getItem().equals(Blocks.COAL_BLOCK)) {return true;}
            else if(baseBlock.getItem().equals(Blocks.HAY_BLOCK)) {return true;}
            else if(baseBlock.getItem().equals(Blocks.DRIED_KELP_BLOCK)) {return true;}
            else if(baseBlock.getItem().equals(Blocks.MAGMA_BLOCK)) {return true;}
            else if(baseBlock.getItem().equals(Blocks.LIME_STAINED_GLASS)) {return true;}
            else if(baseBlock.getItem().equals(Blocks.BLACK_STAINED_GLASS)) {return true;}
            String jsonResultsInt = getProcessResultFilterBlock(getRecipeFilterBlock(getLevel(),getBaseBlock()));
            if(jsonResultsInt != "")
            {
                return true;
            }
        }

        return false;
    }

    public boolean allowEntity(ItemStack baseBlock, Entity entityIn)
    {
        if(!baseBlock.isEmpty())
        {
            if(baseBlock.getItem().equals(Items.EMERALD_BLOCK)) {if(entityIn instanceof Player) {return true;}}
            else if(baseBlock.getItem().equals(Blocks.DIAMOND_BLOCK)) {if(entityIn instanceof Monster) {return true;}}
            else if(baseBlock.getItem().equals(Blocks.GOLD_BLOCK)) {if(entityIn instanceof Animal) {return true;}}
            else if(baseBlock.getItem().equals(Blocks.LAPIS_BLOCK)) {if(entityIn instanceof FlyingMob) {return true;}}
            else if(baseBlock.getItem().equals(Blocks.IRON_BLOCK)) {if(entityIn instanceof AmbientCreature) {return true;}}
            else if(baseBlock.getItem().equals(Blocks.COAL_BLOCK)) {if(entityIn instanceof Mob) {return true;}}
            else if(baseBlock.getItem().equals(Blocks.HAY_BLOCK)) {if(entityIn instanceof Mob) {if(((Mob) entityIn).isBaby())return true;}}
            else if(baseBlock.getItem().equals(Blocks.DRIED_KELP_BLOCK)) {if(entityIn instanceof Mob) {if(!((Mob) entityIn).isBaby())return true;}}
            else if(baseBlock.getItem().equals(Blocks.MAGMA_BLOCK)) {if(entityIn instanceof AbstractPiglin) {return true;}}
            else if(baseBlock.getItem().equals(Blocks.LIME_STAINED_GLASS)) {if(entityIn instanceof AbstractVillager) {return true;}}
            else if(baseBlock.getItem().equals(Blocks.BLACK_STAINED_GLASS)) {if(entityIn instanceof PatrollingMonster) {return true;}}
            String jsonResultsInt = getProcessResultFilterBlock(getRecipeFilterBlock(getLevel(),getBaseBlock()));
            if(jsonResultsInt != "")
            {
                if(entityIn.getType().equals(EntityType.byString(jsonResultsInt).orElse(null)))
                {
                    return true;
                }
            }
        }

        return false;
    }


    //List Wont Persist, but should at least cache the allowed entities while the chunk stays loaded so it'll help reduce some lag.
    List<Entity> storedAllowedEntities = new ArrayList<>();
    public void applyEffectsToEntitiesNearby()
    {
        BlockPos clusterPos = getPos();
        int range = crystalCount();
        ItemStack baseBlock = getBaseBlock();
        boolean hasBaseBlock = isABaseBlock(baseBlock);
        //clusterPos.getX() - range, clusterPos.getY() - range, clusterPos.getZ() - range, clusterPos.getX() + range, clusterPos.getY() + range, clusterPos.getZ() + range
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(getNegRangePosEntity(level, clusterPos, range, range),getPosRangePosEntity(level, clusterPos, range, range)));
        for (Entity getEntity : entities)
        {
            if(getEntity instanceof LivingEntity)
            {
                LivingEntity entity = ((LivingEntity)getEntity);
                if(hasBaseBlock)
                {
                    if(storedAllowedEntities.contains(getEntity))
                    {
                        entity.addEffect(new MobEffectInstance(storedPotionEffect));
                    }
                    else
                    {
                        if(allowEntity(baseBlock,getEntity))
                        {
                            storedAllowedEntities.add(getEntity);
                            entity.addEffect(new MobEffectInstance(storedPotionEffect));
                        }
                    }
                }
                else
                {
                    entity.addEffect(new MobEffectInstance(storedPotionEffect));
                }
            }
        }
    }

    public BlockPos getNegRangePosEntity(Level world, BlockPos posOfPedestal, int intWidth, int intHeight)
    {
        BlockState state = world.getBlockState(posOfPedestal);
        Direction enumfacing = state.getValue(FACING);
        BlockPos blockBelow = posOfPedestal;
        switch (enumfacing)
        {
            case UP:
                return blockBelow.offset(-intWidth,0,-intWidth);
            case DOWN:
                return blockBelow.offset(-intWidth,-intHeight,-intWidth);
            case NORTH:
                return blockBelow.offset(-intWidth,-intWidth,-intHeight);
            case SOUTH:
                return blockBelow.offset(-intWidth,-intWidth,0);
            case EAST:
                return blockBelow.offset(0,-intWidth,-intWidth);
            case WEST:
                return blockBelow.offset(-intHeight,-intWidth,-intWidth);
            default:
                return blockBelow;
        }
    }

    public BlockPos getPosRangePosEntity(Level world, BlockPos posOfPedestal, int intWidth, int intHeight)
    {
        BlockState state = world.getBlockState(posOfPedestal);
        Direction enumfacing = state.getValue(FACING);
        BlockPos blockBelow = posOfPedestal;
        switch (enumfacing)
        {
            case UP:
                return blockBelow.offset(intWidth+1,intHeight,intWidth+1);
            case DOWN:
                return blockBelow.offset(intWidth+1,0,intWidth+1);
            case NORTH:
                return blockBelow.offset(intWidth+1,intWidth,0+1);
            case SOUTH:
                return blockBelow.offset(intWidth+1,intWidth,intHeight+1);
            case EAST:
                return blockBelow.offset(intHeight+1,intWidth,intWidth+1);
            case WEST:
                return blockBelow.offset(0+1,intWidth,intWidth+1);
            default:
                return blockBelow;
        }
    }

    public BlockPos getPosOfBlockBelow(Level world, BlockPos posOfPedestal, int numBelow)
    {
        BlockState state = world.getBlockState(posOfPedestal);

        Direction enumfacing = (state.hasProperty(FACING))?(state.getValue(FACING)):(Direction.UP);
        BlockPos blockBelow = posOfPedestal;
        switch (enumfacing)
        {
            case UP:
                return blockBelow.offset(0,-numBelow,0);
            case DOWN:
                return blockBelow.offset(0,numBelow,0);
            case NORTH:
                return blockBelow.offset(0,0,numBelow);
            case SOUTH:
                return blockBelow.offset(0,0,-numBelow);
            case EAST:
                return blockBelow.offset(-numBelow,0,0);
            case WEST:
                return blockBelow.offset(numBelow,0,0);
            default:
                return blockBelow;
        }
    }

    int clusterTicker = 0;

    public void tick()
    {
        if(!level.isClientSide() && level.isAreaLoaded(getPos(),1))
        {
            clusterTicker++;

            if (clusterTicker%20 == 0) {

                if(!level.hasNeighborSignal(getPos()) && crystalCount()>0)
                {
                    BlockPos posAdjusted = getPosOfBlockBelow(level, getPos(), -1);
                    if(this.currentFuelTime>0)
                    {
                        if(this.storedPotionEffect != null)
                        {
                            applyEffectsToEntitiesNearby();
                        }

                        List<Integer> colorList = getTrueColorFromInt(storedPotionEffect.getEffect().getColor());
                        DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,posAdjusted.getX(),posAdjusted.getY(),posAdjusted.getZ(),colorList.get(0),colorList.get(1),colorList.get(2)));

                        this.currentFuelTime--;
                    }
                    else
                    {
                        if(consumeFuelModsAndReCalcEffect())
                        {
                            DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,posAdjusted.getX(),posAdjusted.getY(),posAdjusted.getZ(),40,40,40));
                        }
                    }

                }

                if(clusterTicker >=100){clusterTicker=0;}
            }

        }
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        CompoundTag invTag = p_155245_.getCompound("inv");
        handlerCrystalCluster.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(invTag));
        this.storedPotionEffect = (MobEffectInstance.load(p_155245_)!=null)?(MobEffectInstance.load(p_155245_)):(null);
        this.currentFuelTime = p_155245_.getInt("storedEffectDuration");
        this.showRenderRange = p_155245_.getBoolean("showRenderRange");
        this.currentColor = p_155245_.getInt("currentColor");
        int getColors[] = p_155245_.getIntArray("allColors");
        for(int i=0;i<getColors.length;i++)
        {
            this.allColors.add(getColors[i]);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        save(p_187471_);
    }

    @Override
    public CompoundTag save(CompoundTag p_58888_) {
        super.save(p_58888_);
        handlerCrystalCluster.ifPresent(h -> {
            CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            p_58888_.put("inv", compound);
        });

        if(storedPotionEffect!=null)storedPotionEffect.save(p_58888_);
        p_58888_.putInt("storedEffectDuration",currentFuelTime);
        p_58888_.putBoolean("showRenderRange",showRenderRange);
        p_58888_.putInt("currentColor",currentColor);
        p_58888_.putIntArray("allColors",allColors);

        return p_58888_;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this.getCluster());
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
        if(this.handlerCrystalCluster != null) {
            this.handlerCrystalCluster.invalidate();
        }
    }
}
