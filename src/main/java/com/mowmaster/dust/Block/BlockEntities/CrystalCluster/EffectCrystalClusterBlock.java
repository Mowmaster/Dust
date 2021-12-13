package com.mowmaster.dust.Block.BlockEntities.CrystalCluster;

import com.mowmaster.dust.Block.BaseBlocks.BaseColoredCrystalBlock;
import com.mowmaster.dust.Block.BlockEntities.Pedestal.BasePedestalBlock;
import com.mowmaster.dust.Block.BlockEntities.Pedestal.BasePedestalBlockEntity;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.Items.ColorApplicator;
import com.mowmaster.dust.Items.ColoredCrystalBase;
import com.mowmaster.dust.Items.Filters.IPedestalFilter;
import com.mowmaster.dust.Items.Tools.IPedestalTool;
import com.mowmaster.dust.Items.Tools.LinkingTool;
import com.mowmaster.dust.Items.Tools.LinkingToolBackwards;
import com.mowmaster.dust.Items.Upgrades.Pedestal.IPedestalUpgrade;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Random;

import static com.mowmaster.dust.References.Constants.MODID;

public class EffectCrystalClusterBlock extends Block implements SimpleWaterloggedBlock, EntityBlock
{
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected final VoxelShape CUP;
    protected final VoxelShape CDOWN;
    protected final VoxelShape CNORTH;
    protected final VoxelShape CSOUTH;
    protected final VoxelShape CEAST;
    protected final VoxelShape CWEST;

    public EffectCrystalClusterBlock(Properties p_152726_) {

        super(p_152726_);
        int p_152015_ = 12;
        int p_152016_ = 8;
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(FACING, Direction.UP));
        this.CUP = Shapes.or(Block.box(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D));
        this.CDOWN = Shapes.or(Block.box(2.0D, 14.0D, 2.0D, 14.0D, 16.0D, 14.0D));
        this.CNORTH = Shapes.or(Block.box(2.0D, 2.0D, 14.0D, 14.0D, 14.0D, 16.0D));
        this.CSOUTH = Shapes.or(Block.box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 2.0D));
        this.CEAST = Shapes.or(Block.box(0.0D, 2.0D, 2.0D, 2.0D, 14.0D, 14.0D));
        this.CWEST = Shapes.or(Block.box(14.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D));
    }

    public VoxelShape getShape(BlockState p_152021_, BlockGetter p_152022_, BlockPos p_152023_, CollisionContext p_152024_) {
        Direction direction = p_152021_.getValue(FACING);
        switch(direction) {
            case NORTH:
                return this.CNORTH;
            case SOUTH:
                return this.CSOUTH;
            case EAST:
                return this.CEAST;
            case WEST:
                return this.CWEST;
            case DOWN:
                return this.CDOWN;
            case UP:
            default:
                return this.CUP;
        }
    }

    public BlockState updateShape(BlockState p_152036_, Direction p_152037_, BlockState p_152038_, LevelAccessor p_152039_, BlockPos p_152040_, BlockPos p_152041_) {
        if (p_152036_.getValue(WATERLOGGED)) {
            p_152039_.getFluidTicks();
            //.scheduleTick(p_152040_, Fluids.WATER, Fluids.WATER.getTickDelay(p_152039_))
        }

        return p_152037_ == p_152036_.getValue(FACING).getOpposite() && !p_152036_.canSurvive(p_152039_, p_152040_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_152036_, p_152037_, p_152038_, p_152039_, p_152040_, p_152041_);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_152019_) {
        LevelAccessor levelaccessor = p_152019_.getLevel();
        BlockPos blockpos = p_152019_.getClickedPos();
        Direction direction = p_152019_.getClickedFace();
        BlockState blockstate = p_152019_.getLevel().getBlockState(p_152019_.getClickedPos().relative(direction.getOpposite()));
        int getColor = ColorReference.getColorFromStateInt(blockstate);
        //Also copied the facing direction stuff from EndRodBlock
        return blockstate.is(this) &&
                blockstate.getValue(FACING) == direction
                ?
                this.defaultBlockState().setValue(FACING, direction.getOpposite()).setValue(WATERLOGGED, Boolean.valueOf(levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER))
                :
                this.defaultBlockState().setValue(FACING, direction).setValue(WATERLOGGED, Boolean.valueOf(levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER));
    }

    @Override
    public void setPlacedBy(Level p_49847_, BlockPos p_49848_, BlockState p_49849_, @org.jetbrains.annotations.Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        if(!p_49847_.isClientSide())
        {
            if(p_49850_ instanceof Player)
            {
                Player player = ((Player)p_49850_);

            }
        }

        super.setPlacedBy(p_49847_, p_49848_, p_49849_, p_49850_, p_49851_);
    }

    public BlockState rotate(BlockState p_152033_, Rotation p_152034_) {
        return p_152033_.setValue(FACING, p_152034_.rotate(p_152033_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_152030_, Mirror p_152031_) {
        return p_152030_.rotate(p_152031_.getRotation(p_152030_.getValue(FACING)));
    }

    public FluidState getFluidState(BlockState p_152045_) {
        return p_152045_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_152045_);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_152043_) {
        p_152043_.add(WATERLOGGED, FACING);
    }

    public PushReaction getPistonPushReaction(BlockState p_152733_) {
        return PushReaction.IGNORE;
    }

    //Right Click Action
    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {

        if(!p_60504_.isClientSide())
        {
            BlockEntity blockEntity = p_60504_.getBlockEntity(p_60505_);
            if(blockEntity instanceof EffectCrystalClusterBlockEntity)
            {
                EffectCrystalClusterBlockEntity cluster = ((EffectCrystalClusterBlockEntity)blockEntity);

                ItemStack itemInHand = p_60506_.getMainHandItem();
                ItemStack itemInOffHand = p_60506_.getOffhandItem();

                if(itemInHand.getItem() instanceof ColoredCrystalBase)
                {
                    if(cluster.addCrystal(itemInHand,true)){

                        if(cluster.addCrystal(itemInHand,false))
                        {
                            itemInHand.shrink(1);
                            return InteractionResult.SUCCESS;
                        }

                    }
                }
                else if(itemInHand.getItem() instanceof BlockItem)
                {
                    itemInHand.shrink(1);
                    ItemHandlerHelper.giveItemToPlayer(p_60506_,cluster.addBaseBlock(itemInHand));
                    return InteractionResult.SUCCESS;
                }
                else if(cluster.addFuel(itemInHand,true).getCount()>0)
                {
                    int amountToShrink = cluster.addFuel(itemInHand,false).getCount();
                    itemInHand.shrink(amountToShrink);
                    return InteractionResult.SUCCESS;
                }
                else if(cluster.hasFuelItems() && cluster.addModifier(itemInHand, true).getCount()>0)
                {
                    int amountToShrink = cluster.addModifier(itemInHand, false).getCount();
                    itemInHand.shrink(amountToShrink);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        /*BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof BasePedestalBlockEntity) {
            BasePedestalBlockEntity pedestal = ((BasePedestalBlockEntity) blockEntity);
            return (state.getValue(LIT))?(pedestal.getLightBrightness()):(0);
        }*/

        return 15;
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        return super.canHarvestBlock(state, world, pos, player);
    }

    @Override
    public void playerDestroy(Level p_49827_, Player p_49828_, BlockPos p_49829_, BlockState p_49830_, @Nullable BlockEntity p_49831_, ItemStack p_49832_) {
        if(!p_49827_.isClientSide())
        {
            if (p_49830_.getBlock() instanceof EffectCrystalClusterBlock) {
                if (!p_49827_.isClientSide && !p_49828_.isCreative()) {
                    ItemStack itemstack = new ItemStack(this);
                    itemstack.setCount(1);
                    ItemEntity itementity = new ItemEntity(p_49827_, (double)p_49829_.getX() + 0.5D, (double)p_49829_.getY() + 0.5D, (double)p_49829_.getZ() + 0.5D, itemstack);
                    itementity.setDefaultPickUpDelay();
                    p_49827_.addFreshEntity(itementity);
                }
            }
        }
        super.playerDestroy(p_49827_, p_49828_, p_49829_, p_49830_, p_49831_, p_49832_);
        p_49827_.removeBlock(p_49829_,false);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        return willHarvest || super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if(p_60515_.getBlock() != p_60518_.getBlock())
        {
            BlockEntity blockEntity = p_60516_.getBlockEntity(p_60517_);
            if(blockEntity instanceof EffectCrystalClusterBlockEntity) {
                EffectCrystalClusterBlockEntity cluster = ((EffectCrystalClusterBlockEntity) blockEntity);
                cluster.dropInventoryItems(p_60516_,p_60517_);

                p_60516_.updateNeighbourForOutputSignal(p_60517_,p_60518_.getBlock());
            }
            super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
        }
    }

    @Override
    public void playerWillDestroy(Level p_56212_, BlockPos p_56213_, BlockState p_56214_, Player p_56215_) {

        /*if(!p_56212_.isClientSide())
        {
            if (p_56214_.getBlock() instanceof BasePedestalBlock) {
                if (!p_56212_.isClientSide && !p_56215_.isCreative()) {
                    ItemStack itemstack = new ItemStack(this);
                    int getColor = ColorReference.getColorFromStateInt(p_56214_);
                    ItemStack newStack = ColorReference.addColorToItemStack(itemstack,getColor);
                    newStack.setCount(1);
                    ItemEntity itementity = new ItemEntity(p_56212_, (double)p_56213_.getX() + 0.5D, (double)p_56213_.getY() + 0.5D, (double)p_56213_.getZ() + 0.5D, newStack);
                    itementity.setDefaultPickUpDelay();
                    p_56212_.addFreshEntity(itementity);
                }
            }
        }*/
        super.playerWillDestroy(p_56212_, p_56213_, p_56214_, p_56215_);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    /*
    private static int getRedstoneLevelPedestal(Level worldIn, BlockPos pos)
    {
        int hasItem=0;
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if(blockEntity instanceof BasePedestalBlockEntity) {
            BasePedestalBlockEntity pedestal = ((BasePedestalBlockEntity) blockEntity);
            ItemStack itemstack = pedestal.getItemInPedestal();
            ItemStack coin = pedestal.getCoinOnPedestal();
            if(!itemstack.isEmpty())
            {
                float f = (float)itemstack.getCount()/(float)itemstack.getMaxStackSize();
                hasItem = (int)Math.floor(f*14.0F)+1;
            }
        }

        return hasItem;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState p_60457_) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState p_60487_, Level p_60488_, BlockPos p_60489_) {
        return getRedstoneLevelPedestal(p_60488_,p_60489_);
    }*/

    public RenderShape getRenderShape(BlockState p_50950_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return DeferredBlockEntityTypes.CLUSTER.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : (level0, pos, state0, blockEntity) -> ((EffectCrystalClusterBlockEntity) blockEntity).tick();
    }
}
