package com.mowmaster.dust.Block.BlockEntities.CustomDustBlock;

import com.mowmaster.dust.Configs.DustConfig;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import com.mowmaster.dust.Items.ColoredCrystalBase;
import com.mowmaster.dust.Recipes.CrusherRecipe;
import com.mowmaster.dust.References.ColorReference;
import com.mowmaster.dust.References.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CustomPowderedBlock extends Block implements EntityBlock {

    private static final float HORIZONTAL_PARTICLE_MOMENTUM_FACTOR = 0.083333336F;
    private static final float IN_BLOCK_HORIZONTAL_SPEED_MULTIPLIER = 0.9F;
    private static final float IN_BLOCK_VERTICAL_SPEED_MULTIPLIER = 1.5F;
    private static final float NUM_BLOCKS_TO_FALL_INTO_BLOCK = 2.5F;
    private static final VoxelShape FALLING_COLLISION_SHAPE = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, (double)0.9F, 1.0D);
    private static final double MINIMUM_FALL_DISTANCE_FOR_SOUND = 4.0D;
    private static final double MINIMUM_FALL_DISTANCE_FOR_BIG_SOUND = 7.0D;

    public CustomPowderedBlock(Properties p_49795_) {
        super(p_49795_);
    }

    public static int getColor(Level level, BlockPos pos, BlockState state)
    {
        if(state.getBlock() instanceof CustomPowderedBlock)
        {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof CustomPowderedBlockEntity)
            {
                CustomPowderedBlockEntity block = ((CustomPowderedBlockEntity)entity);
                return block.getColor();
            }
        }

        return 16777215;
    }

    @Override
    public void setPlacedBy(Level p_49847_, BlockPos p_49848_, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        super.setPlacedBy(p_49847_, p_49848_, p_49849_, p_49850_, p_49851_);
        if(p_49850_ instanceof Player)
        {
            Player player = ((Player)p_49850_);
            if(player.isCreative())
            {
                ItemStack input = new ItemStack(Items.BLAZE_ROD);
                if(!player.getOffhandItem().isEmpty())input = player.getOffhandItem();
                placeCustomDustBlock(p_49847_,p_49848_,input);
            }
        }
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if(p_60515_.getBlock() != p_60518_.getBlock())
        {
            BlockEntity blockEntity = p_60516_.getBlockEntity(p_60517_);
            if(blockEntity instanceof CustomPowderedBlockEntity) {
                CustomPowderedBlockEntity dustBlock = ((CustomPowderedBlockEntity) blockEntity);
                dustBlock.dropInventoryItems(p_60516_,p_60517_);
            }
            super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
        }
    }




    public boolean skipRendering(BlockState p_154268_, BlockState p_154269_, Direction p_154270_) {
        return p_154269_.is(this) ? true : super.skipRendering(p_154268_, p_154269_, p_154270_);
    }

    public VoxelShape getOcclusionShape(BlockState p_154272_, BlockGetter p_154273_, BlockPos p_154274_) {
        return Shapes.empty();
    }

    public void entityInside(BlockState p_154263_, Level p_154264_, BlockPos p_154265_, Entity p_154266_) {
        if (!(p_154266_ instanceof LivingEntity) || p_154266_.getFeetBlockState().is(this)) {
            p_154266_.makeStuckInBlock(p_154263_, new Vec3((double)0.9F, 1.5D, (double)0.9F));
            if (p_154264_.isClientSide) {
                Random random = p_154264_.getRandom();
                boolean flag = p_154266_.xOld != p_154266_.getX() || p_154266_.zOld != p_154266_.getZ();
                if (flag && random.nextBoolean()) {
                    p_154264_.addParticle(ParticleTypes.SNOWFLAKE, p_154266_.getX(), (double)(p_154265_.getY() + 1), p_154266_.getZ(), (double)(Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F), (double)0.05F, (double)(Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F));
                }
            }
        }

        p_154266_.setIsInPowderSnow(true);
        if (!p_154264_.isClientSide) {
            if (p_154266_.isOnFire() && (p_154264_.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || p_154266_ instanceof Player) && p_154266_.mayInteract(p_154264_, p_154265_)) {
                p_154264_.destroyBlock(p_154265_, false);
            }

            p_154266_.setSharedFlagOnFire(false);
        }

    }

    public void fallOn(Level p_196695_, BlockState p_196696_, BlockPos p_196697_, Entity p_196698_, float p_196699_) {
        if (!((double)p_196699_ < 4.0D) && p_196698_ instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)p_196698_;
            LivingEntity.Fallsounds $$7 = livingentity.getFallSounds();
            SoundEvent soundevent = (double)p_196699_ < 7.0D ? $$7.small() : $$7.big();
            p_196698_.playSound(soundevent, 1.0F, 1.0F);
        }
    }

    public VoxelShape getCollisionShape(BlockState p_154285_, BlockGetter p_154286_, BlockPos p_154287_, CollisionContext p_154288_) {
        if (p_154288_ instanceof EntityCollisionContext) {
            EntityCollisionContext entitycollisioncontext = (EntityCollisionContext)p_154288_;
            Entity entity = entitycollisioncontext.getEntity();
            if (entity != null) {
                if (entity.fallDistance > 2.5F) {
                    return FALLING_COLLISION_SHAPE;
                }

                boolean flag = entity instanceof FallingBlockEntity;
                if (flag || canEntityWalkOnPowderBlock(entity) && p_154288_.isAbove(Shapes.block(), p_154287_, false) && !p_154288_.isDescending()) {
                    return super.getCollisionShape(p_154285_, p_154286_, p_154287_, p_154288_);
                }
            }
        }

        return Shapes.empty();
    }

    public VoxelShape getVisualShape(BlockState p_154276_, BlockGetter p_154277_, BlockPos p_154278_, CollisionContext p_154279_) {
        return Shapes.empty();
    }

    public static boolean canEntityWalkOnPowderBlock(Entity p_154256_) {
        if (p_154256_.getType().is(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
            return true;
        } else {
            //return p_154256_ instanceof LivingEntity ? ((LivingEntity)p_154256_).getItemBySlot(EquipmentSlot.FEET).is(Items.LEATHER_BOOTS) : false;
            return false;
        }
    }

    public boolean isPathfindable(BlockState p_154258_, BlockGetter p_154259_, BlockPos p_154260_, PathComputationType p_154261_) {
        return true;
    }

    @Nullable
    protected CrusherRecipe getRecipe(Level level, ItemStack stackIn) {
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

    public void placeCustomDustBlock(Level world, BlockPos pos, ItemStack input)
    {
        if(input.getItem() instanceof ColoredCrystalBase)
        {
            int dustAmount = DustConfig.COMMON.dustPerCrystal.get();
            for(int i=1;i<=dustAmount;i++)
            {
                world.setBlock(pos.offset(0,i-1,0), ColorReference.addColorToBlockState(DeferredRegisterBlocks.CRYSTAL_DUST_BLOCK.get().defaultBlockState(),ColorReference.getColorFromItemStackInt(input)),3);
            }
        }
        else
        {
            world.setBlock(pos, DeferredRegisterTileBlocks.BLOCK_POWDERED_DUST.get().defaultBlockState(),3);
            if(world.getBlockEntity(pos) instanceof CustomPowderedBlockEntity)
            {
                Collection<ItemStack> jsonResults = getProcessResults(getRecipe(world,input));
                int jsonResultsInt = getProcessResultColor(getRecipe(world,input));
                CustomPowderedBlockEntity customDust = ((CustomPowderedBlockEntity)world.getBlockEntity(pos));
                //Set these based on some recipe handler
                customDust.setColor(jsonResultsInt);
                customDust.addItem((jsonResults.iterator().next().isEmpty())?(ItemStack.EMPTY):(jsonResults.iterator().next()));
                List<Integer> colorList = ColorReference.getTrueColorFromInt(jsonResultsInt);
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState p_50950_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return DeferredBlockEntityTypes.DUST.get().create(p_153215_,p_153216_);
    }
}
