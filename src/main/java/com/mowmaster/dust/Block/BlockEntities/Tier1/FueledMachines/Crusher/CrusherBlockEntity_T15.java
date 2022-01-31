package com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.Crusher;

import com.mowmaster.dust.Block.BlockEntities.CustomDustBlock.CustomPowderedBlockEntity;
import com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.DustFueledMachineBaseBlockEntity;
import com.mowmaster.dust.Configs.DustConfig;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import com.mowmaster.dust.Items.ColoredCrystalBase;
import com.mowmaster.dust.Networking.DustPacketHandler;
import com.mowmaster.dust.Networking.DustPacketParticles;
import com.mowmaster.dust.Recipes.CrusherRecipe;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.References.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CrusherBlockEntity_T15 extends DustFueledMachineBaseBlockEntity {
    public CrusherBlockEntity_T15(BlockPos p_155229_, BlockState p_155230_) {
        super(DeferredBlockEntityTypes.CRUSHER_T15.get(), p_155229_, p_155230_);
    }

    /*============================================================================
    ==============================================================================
    =====================   BASE CLASS OVERRIDES - START   =======================
    ==============================================================================
    ============================================================================*/

    @Override
    public int getRepairSlotsForRepairs()
    {
        return DustConfig.COMMON.repairitemsToCraft_Crusher_T15.get();
    }

    @Override
    public Block getBlockForThisBlockEntity()
    {
        return DeferredRegisterTileBlocks.BLOCK_CRUSHER_T15.get();
    }

    @Override
    public RecipeType getRecipeTypeForBlock()
    {
        return CrusherRecipe.CRUSHING;
    }

    @Override
    public double getRepairParticleHeight(){ return 0.75;}

    /*============================================================================
    ==============================================================================
    =====================    BASE CLASS OVERRIDES - END    =======================
    ==============================================================================
    ============================================================================*/

    @Nullable
    public CrusherRecipe getRecipe(Level level, ItemStack stackIn) {
        Container cont = Constants.getContainer(1);
        cont.setItem(-1,stackIn);
        List<CrusherRecipe> recipes = level.getRecipeManager().getRecipesFor(CrusherRecipe.CRUSHING,cont,level);
        return recipes.size() > 0 ? level.getRecipeManager().getRecipesFor(CrusherRecipe.CRUSHING,cont,level).get(0) : null;
    }

    protected Collection<ItemStack> getProcessResults(CrusherRecipe recipe) {
        return (recipe == null)?(Arrays.asList(ItemStack.EMPTY)):(Collections.singleton(recipe.getResultItem()));
    }
    protected int getProcessResultColor(CrusherRecipe recipe) {
        return (recipe == null)?(ColorReference.DEFAULTCOLOR):(recipe.getColor());
    }

    public void placeCustomDustBlock(Level worldIn, BlockPos pos, ItemStack input)
    {
        ItemStack stackToAdd = ItemStack.EMPTY;
        BlockState stated = Blocks.AIR.defaultBlockState();
        CrusherRecipe recipe = null;
        if(input.getItem() instanceof ColoredCrystalBase)
        {
            int dustAmount = DustConfig.COMMON.dustPerCrystal.get();
            for(int i=1;i<=dustAmount;i++)
            {
                stated = ColorReference.addColorToBlockState(DeferredRegisterBlocks.CRYSTAL_DUST_BLOCK.get().defaultBlockState(),ColorReference.getColorFromItemStackInt(input));
            }
        }
        else
        {
            recipe = getRecipe(worldIn,input);
            Collection<ItemStack> jsonResults = getProcessResults(recipe);
            stackToAdd = (jsonResults.iterator().next().isEmpty())?(ItemStack.EMPTY):(jsonResults.iterator().next());

            if(Block.byItem(stackToAdd.getItem()) instanceof FallingBlock && stackToAdd.getCount()<=1)
            {
                stated = Block.byItem(stackToAdd.getItem()).defaultBlockState();
            }
            else
            {
                stated = DeferredRegisterTileBlocks.BLOCK_POWDERED_DUST.get().defaultBlockState();
            }
        }

        Random RANDOM = new Random();
        int counter = 0;
        int yPlus = 0;
        BlockPos posFound = new BlockPos(pos.getX()+RANDOM.nextInt(-2, 2 + 1),pos.getY()+yPlus,pos.getZ()+RANDOM.nextInt(-2, 2 + 1));
        while(!worldIn.getBlockState(posFound).getBlock().equals(Blocks.AIR))
        {
            counter++;
            if(counter <= 25)
            {
                counter = 0;
                yPlus +=1;
            }

            if(counter <= 25 && yPlus>5)
            {
                worldIn.explode(null,null,null,posFound.getX()+0.5,posFound.getY()+0.5,posFound.getZ()+0.5,1.0f,false, Explosion.BlockInteraction.BREAK);
                counter = 0;
                yPlus = 0;
            }
            posFound = new BlockPos(pos.getX()+RANDOM.nextInt(-2, 2 + 1),pos.getY()+yPlus,pos.getZ()+RANDOM.nextInt(-2, 2 + 1));
        }
        worldIn.setBlock(posFound,stated,3);

        if(worldIn.getBlockEntity(posFound) instanceof CustomPowderedBlockEntity)
        {
            int jsonResultsInt = (recipe != null)?(getProcessResultColor(recipe)):(0);
            CustomPowderedBlockEntity customDust = ((CustomPowderedBlockEntity)worldIn.getBlockEntity(posFound));
            customDust.setColor(jsonResultsInt);
            customDust.addItem(stackToAdd);
        }
    }

    @Override
    public void tick()
    {
        if(getLevel().getGameTime()%20 == 0){if(!isFullyRepaired()){ DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX(),getPos().getY()+getRepairParticleHeight(),getPos().getZ(),255,255,255));}}

        if(!getLevel().isClientSide())
        {
            if(getLevel().getGameTime()%20 == 0)
            {
                if(getBurnTime()>0)
                {
                    changeBurnTime(20, false);
                    if(getBurnTime()<=5)addToBurnTime();
                    if(getBurnTime()<=0)
                    {
                        setLit(false);
                        update();
                    }

                    if(!removeItemFromSlot(1,1,true).isEmpty())
                    {
                        changeCurrentCookTime(20, true);
                        if(getCurrentCookTime() >= getMaxCookTime())
                        {
                            setCurrentCookTime(0);
                            placeCustomDustBlock(getLevel(), getPos(), getInputItemInSlot(1));
                            getLevel().playSound(null,getPos(), SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS,1.0F, 1.0F);
                            removeItemFromSlot(1,1,false);
                            if(getXPOutput()>0f)
                            {
                                Random rand = new Random();
                                ExperienceOrb xpEntity = new ExperienceOrb(level,getPos().getX(), getPos().getY()+1, getPos().getZ(),(int)getXPOutput());
                                xpEntity.lerpMotion(0.1D*rand.nextInt(5),0.1D*rand.nextInt(5),0.1D*rand.nextInt(5));
                                getLevel().addFreshEntity(xpEntity);
                            }
                        }
                    }
                }
                else
                {
                    if(!getInputItemInSlot(0).isEmpty())addToBurnTime();
                    else if(getCurrentCookTime()>0)changeCurrentCookTime(20, false);
                }
            }

            if(getLevel().getGameTime()%20 == 0)
            {
                if(getCurrentCookTime()>0){DustPacketHandler.sendToNearby(level,getPos(),new DustPacketParticles(DustPacketParticles.EffectType.ANY_COLOR,getPos().getX(),getPos().getY()+getRepairParticleHeight(),getPos().getZ(),50,50,50));}
            }

            if(getLevel().getGameTime()%69 == 0)
            {
                if(isFurnaceLit())getLevel().playSound(null,getPos(), SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS,1.0F, 1.0F);
            }
        }
    }

}
