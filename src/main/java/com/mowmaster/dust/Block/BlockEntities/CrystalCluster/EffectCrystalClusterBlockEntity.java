package com.mowmaster.dust.Block.BlockEntities.CrystalCluster;

import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.Items.ColoredCrystalBase;
import com.mowmaster.dust.Networking.DustPacketHandler;
import com.mowmaster.dust.Networking.DustPacketParticles;
import com.mowmaster.dust.Recipes.*;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.References.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
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
                if ((slot == 6) && addModifier(stack, true).getCount()>0) return true;
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
                    this.currentColor = mixedColor;
                    this.allColors.add(mixedColor);
                    //set current fuel to 0 to recalc the effect
                    this.currentFuelTime=0;

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
                if(h.getStackInSlot(i).isEmpty())
                {
                    nextAvailableSlot=i;
                    break;
                }
            }
            int lastSlot = nextAvailableSlot-1;

            int color = getCurrentColor();
            int newColor = allColors.get(lastSlot-1);
            int mixedColor = (color != newColor)?((color==0)?(newColor):(ColorReference.mixColorsInt(color,newColor))):(color);
            this.currentColor = mixedColor;
            this.allColors.remove(lastSlot);
            //set current fuel to 0 to recalc the effect
            this.currentFuelTime=0;
            //Default Effect to 5 seconds or 100 ticks as that'll be whats applied to the entities
            //MobEffectInstance newInstance = new MobEffectInstance(EffectReference.getIntEffect(newColor),100,calculateModifiedPotency(),false,false,false,null);
            //this.storedPotionEffect = newInstance;
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
        return (recipe == null)?(0.0):(recipe.getResultDurationModifier());
    }

    protected double getProcessResultModifierPotency(CrystalClusterModifiers recipe) {
        return (recipe == null)?(0.0):(recipe.getResultPotencyModifier());
    }

    protected boolean getProcessResultCorruption(CrystalClusterModifiers recipe) {
        return (recipe == null)?(false):(recipe.getResultCorruption());
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
        else if(potencyMod > 0.0 || potencyMod < 0.0)return true;
        else if(hasCorruption())return true;

        return false;
    }

    public boolean isModifierItem(ItemStack stack)
    {
        double durationMod = getProcessResultModifierDuration(getRecipeModifier(getLevel(),stack));
        double potencyMod = getProcessResultModifierPotency(getRecipeModifier(getLevel(),stack));
        //Since we only check for INT values, and +/- ones count, but 0 would result null values in our maths, so those are bad.
        if(durationMod > 0.0 || durationMod < 0.0)return true;
        if(potencyMod > 0.0 || potencyMod < 0.0)return true;
        else if(hasCorruption())return true;

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
        if(isModifierItem(stack))
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
            else
            {
                //For When Adding new Modifiers

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
        double durationMod = 0;
        //Verify fuel can support modifier
        if(isAcceptedModifierItem(getModifierStack())) durationMod = getProcessResultModifierDuration(getRecipeModifier(getLevel(),getModifierStack()));
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
        double calculatedPotency = 0.0;
        double potencyMod = getProcessResultModifierPotency(getRecipeModifier(getLevel(),getModifierStack()));
        //Verify fuel can support potency modifier
        if(isAcceptedModifierItem(getModifierStack()))calculatedPotency = defaultPotency * potencyMod;

        //Subtracting 1 'Later'
        return (int)(calculatedPotency-1.0);
    }

    @Nullable
    protected MobEffectColorRecipe getRecipeMobEffectColor(Level level, ItemStack stackIn) {
        Container container = Constants.blankContainer;
        container.setItem(0,stackIn);
        List<MobEffectColorRecipe> recipes = level.getRecipeManager().getRecipesFor(MobEffectColorRecipe.MOBEFFECTCOLOR,container,level);
        return recipes.size() > 0 ? level.getRecipeManager().getRecipesFor(MobEffectColorRecipe.MOBEFFECTCOLOR,container,level).get(0) : null;
    }

    protected String getProcessResultMobEffectColorRecipe(MobEffectColorRecipe recipe) {
        return (recipe == null)?(""):(recipe.getResultEffectName());
    }

    @Nullable
    protected MobEffectColorRecipeCorrupted getRecipeMobEffectColorCorrupted(Level level, ItemStack stackIn) {
        Container container = Constants.blankContainer;
        container.setItem(0,stackIn);
        List<MobEffectColorRecipeCorrupted> recipes = level.getRecipeManager().getRecipesFor(MobEffectColorRecipeCorrupted.MOBEFFECTCOLORCORRUPTED,container,level);
        return recipes.size() > 0 ? level.getRecipeManager().getRecipesFor(MobEffectColorRecipeCorrupted.MOBEFFECTCOLORCORRUPTED,container,level).get(0) : null;
    }

    protected String getProcessResultMobEffectColorRecipeCorrupted(MobEffectColorRecipeCorrupted recipe) {
        return (recipe == null)?(""):(recipe.getResultEffectName());
    }

    public boolean hasCorruption()
    {
        if(hasModifierItems())
        {
            return getProcessResultCorruption(getRecipeModifier(getLevel(),getModifierStack()));
        }
        return false;
    }

    public MobEffect calculateMobEffect()
    {
        boolean corruption = hasCorruption();
        if(corruption)
        {
            ItemStack stack = ColorReference.addColorToItemStack(new ItemStack(DeferredRegisterItems.COLORED_CRYSTAL.get()),getCurrentColor());
            ResourceLocation location = new ResourceLocation(getProcessResultMobEffectColorRecipeCorrupted(getRecipeMobEffectColorCorrupted(getLevel(),stack)));
            if(Registry.MOB_EFFECT.getOptional(location).isPresent())return Registry.MOB_EFFECT.getOptional(location).get();
        }
        else if (!corruption)
        {
            ItemStack stack = ColorReference.addColorToItemStack(new ItemStack(DeferredRegisterItems.COLORED_CRYSTAL.get()),getCurrentColor());
            ResourceLocation location = new ResourceLocation(getProcessResultMobEffectColorRecipe(getRecipeMobEffectColor(getLevel(),stack)));
            if(Registry.MOB_EFFECT.getOptional(location).isPresent())return Registry.MOB_EFFECT.getOptional(location).get();
        }

        return getRandomNegativeEffect();
    }

    public MobEffect getRandomNegativeEffect()
    {
        Random rand = new Random();
        Map<Integer, MobEffect> NEGEFFECT = Map.ofEntries(
                Map.entry(0, MobEffects.BAD_OMEN),
                Map.entry(1,MobEffects.BLINDNESS),
                Map.entry(2,MobEffects.GLOWING),
                Map.entry(3,MobEffects.HUNGER),
                Map.entry(4,MobEffects.HARM),
                Map.entry(5,MobEffects.LEVITATION),
                Map.entry(6,MobEffects.DIG_SLOWDOWN),
                Map.entry(7,MobEffects.CONFUSION),
                Map.entry(8,MobEffects.POISON),
                Map.entry(9,MobEffects.MOVEMENT_SLOWDOWN),
                Map.entry(10,MobEffects.UNLUCK),
                Map.entry(11,MobEffects.WEAKNESS),
                Map.entry(12,MobEffects.WITHER)
        );

        return NEGEFFECT.getOrDefault(rand.nextInt(13),MobEffects.HUNGER);
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
        else returnStack = ItemStack.EMPTY;
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
            MobEffectInstance newInstance = new MobEffectInstance(calculateMobEffect(),100,calculateModifiedPotency(),false,false,false,null);
            this.storedPotionEffect = newInstance;
            int duration = calculateFuelModifiedDuration();
            if(duration>0)
            {
                setFuelTime(duration);
                removeFuel(1);
                //Verify that modifier exists and was actually able to be used
                if(hasModifierItems() && isAcceptedModifierItem(getModifierStack()))removeModifier(1);
                return true;
            }
        }

        return false;
    }

    @Nullable
    protected BaseBlockEntityFilter getRecipeFilterBlock(Level level, ItemStack stackIn) {
        Container container = Constants.blankContainer;
        container.setItem(0,stackIn);
        List<BaseBlockEntityFilter> recipes = getLevel().getRecipeManager().getRecipesFor(BaseBlockEntityFilter.BLOCKENTITYFILTER,container,getLevel());
        return getLevel() != null ? (recipes.size() > 0)?(recipes.stream().findFirst().get()):(null) : null;
    }

    protected String getProcessResultFilterBlock(BaseBlockEntityFilter recipe) {
        return (recipe == null)?(""):(recipe.getResultEntityString());
    }

    protected int getProcessResultFilterBlockMobType(BaseBlockEntityFilter recipe) {
        return (recipe == null)?(1):(recipe.getResultMobType());
    }

    protected boolean getProcessResultFilterBlockIsBaby(BaseBlockEntityFilter recipe) {
        return (recipe == null)?(false):(recipe.getResultBaby());
    }

    //This is to check if any of the base blocks are filters, cause if so we have to restrict which entities get the effects.
    public boolean isABaseBlock(ItemStack baseBlock)
    {
        if(!baseBlock.isEmpty())
        {
            if(getProcessResultFilterBlock(getRecipeFilterBlock(getLevel(),getBaseBlock())) != "")
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
            BaseBlockEntityFilter filter = getRecipeFilterBlock(getLevel(),getBaseBlock());
            if(getProcessResultFilterBlock(filter) != "")
            {
                if(getProcessResultFilterBlockMobType(filter)==0)
                {
                    if(entityIn.getClassification(false).equals(MobCategory.byName(getProcessResultFilterBlock(filter))))
                    {
                        if(getProcessResultFilterBlockIsBaby(filter) && entityIn instanceof LivingEntity)
                        {
                            LivingEntity entity = ((LivingEntity)entityIn);
                            if (entity.isBaby())
                            {
                                return true;
                            }

                            return false;
                        }

                        return true;
                    }
                }
                else if(getProcessResultFilterBlockMobType(filter)==1)
                {
                    if(!EntityType.byString(getProcessResultFilterBlock(filter)).isPresent())
                    {
                        System.out.println(getProcessResultFilterBlock(filter) +" is Not a Valid Entity Type");
                        return false;
                    }
                    else if(entityIn.getType().equals(EntityType.byString(getProcessResultFilterBlock(filter)).get()))
                    {
                        if(getProcessResultFilterBlockIsBaby(filter) && entityIn instanceof LivingEntity)
                        {
                            LivingEntity entity = ((LivingEntity)entityIn);
                            if (entity.isBaby())
                            {
                                return true;
                            }

                            return false;
                        }

                        return true;
                    }
                }

                return false;
            }
        }

        return true;
    }

    public String getEntityForFilter(ItemStack baseBlock)
    {
        if(!baseBlock.isEmpty())
        {
            BaseBlockEntityFilter filter = getRecipeFilterBlock(getLevel(),getBaseBlock());
            if(getProcessResultFilterBlock(filter) != "")
            {
                if(getProcessResultFilterBlockMobType(filter)==0)
                {
                    return MobCategory.byName(getProcessResultFilterBlock(filter)).getName();
                }
                else if(getProcessResultFilterBlockMobType(filter)==1)
                {
                    if(EntityType.byString(getProcessResultFilterBlock(filter)).isPresent())
                    {
                        return EntityType.byString(getProcessResultFilterBlock(filter)).get().getDescription().getString();
                    }
                }
            }
        }

        return "";
    }

    public boolean isFilteredEntityBaby(ItemStack baseBlock)
    {
        if(!baseBlock.isEmpty())
        {
            BaseBlockEntityFilter filter = getRecipeFilterBlock(getLevel(),getBaseBlock());
            if(getProcessResultFilterBlock(filter) != "")
            {
                return getProcessResultFilterBlockIsBaby(filter);
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
                    if(allowEntity(baseBlock,getEntity))
                    {
                        if(storedAllowedEntities.contains(getEntity))
                        {
                            if(storedPotionEffect.getEffect().isInstantenous()){
                                storedPotionEffect.getEffect().applyInstantenousEffect(null, null, entity, storedPotionEffect.getAmplifier()+1, 0.25D);
                                this.currentFuelTime-=45;
                                if(this.currentFuelTime<=0)break;
                            }
                            else
                            {
                                entity.addEffect(new MobEffectInstance(storedPotionEffect));
                            }
                        }
                        else
                        {
                            if(storedPotionEffect.getEffect().isInstantenous()){
                                storedPotionEffect.getEffect().applyInstantenousEffect(null, null, entity, storedPotionEffect.getAmplifier()+1, 0.25D);
                                this.currentFuelTime-=45;
                                if(this.currentFuelTime<=0)break;
                            }
                            else
                            {
                                entity.addEffect(new MobEffectInstance(storedPotionEffect));
                            }
                        }
                    }
                }
                else
                {
                    //MobEffectInstance(effect, duration, amplifier, ambient, visible, showIcon, new MEI(hiddenEffect))
                    if(storedPotionEffect.getEffect().isInstantenous()){
                        storedPotionEffect.getEffect().applyInstantenousEffect(null, null, entity, storedPotionEffect.getAmplifier()+1, 0.25D);
                        this.currentFuelTime-=45;
                        if(this.currentFuelTime<=0)break;
                    }
                    else
                    {
                        entity.addEffect(new MobEffectInstance(storedPotionEffect));
                    }
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

                        if(!storedPotionEffect.getEffect().isInstantenous())this.currentFuelTime--;
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
