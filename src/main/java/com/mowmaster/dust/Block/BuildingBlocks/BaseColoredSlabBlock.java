package com.mowmaster.dust.Block.BuildingBlocks;

import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.Items.ColorApplicator;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.List;

public class BaseColoredSlabBlock extends SlabBlock
{
    public static final IntegerProperty COLOR_RED = IntegerProperty.create("color_red", 0, 3);
    public static final IntegerProperty COLOR_GREEN = IntegerProperty.create("color_green", 0, 3);
    public static final IntegerProperty COLOR_BLUE = IntegerProperty.create("color_blue", 0, 3);

    public BaseColoredSlabBlock(Properties p_152915_)
    {
        super(p_152915_);
        this.registerDefaultState(ColorReference.addColorToBlockState(this.defaultBlockState(),ColorReference.DEFAULTCOLOR));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_152043_) {
        p_152043_.add(WATERLOGGED, TYPE, COLOR_RED, COLOR_GREEN, COLOR_BLUE);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_56361_) {
        BlockPos blockpos = p_56361_.getClickedPos();
        BlockState blockstate = p_56361_.getLevel().getBlockState(blockpos);
        int color = ColorReference.DEFAULTCOLOR;
        if(p_56361_.getPlayer().getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ColorApplicator)
        {
            color = ColorReference.getColorFromItemStackInt(p_56361_.getPlayer().getItemInHand(InteractionHand.OFF_HAND));
        }
        else
        {
            color = ColorReference.getColorFromItemStackInt(p_56361_.getPlayer().getItemInHand(p_56361_.getHand()));
        }

        if (blockstate.is(this) && ColorReference.getColorFromStateInt(blockstate) == color) {
            return ColorReference.addColorToBlockState(blockstate,color).setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, Boolean.valueOf(false));
        }
        else if(blockstate.is(this)
                && ColorReference.getColorFromStateInt(blockstate) != color)
        {
            FluidState fluidstate = p_56361_.getLevel().getFluidState(blockpos);
            Direction direction = p_56361_.getClickedFace();

            p_56361_.getLevel().setBlock((direction != Direction.DOWN)?(blockpos.above()):(blockpos.below()),ColorReference.addColorToBlockState(this.defaultBlockState(),color).setValue(TYPE, (direction != Direction.DOWN)?(SlabType.BOTTOM):(SlabType.TOP)).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER)),3);
            if(!p_56361_.getPlayer().isCreative())p_56361_.getPlayer().getItemInHand(InteractionHand.OFF_HAND).shrink(1);
            p_56361_.getLevel().playSound(p_56361_.getPlayer(),blockpos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS,1.0F, 1.0F);
            return this.defaultBlockState().setValue(TYPE, blockstate.getValue(TYPE)).setValue(WATERLOGGED, blockstate.getValue(WATERLOGGED))
                    .setValue(COLOR_RED, blockstate.getValue(COLOR_RED)).setValue(COLOR_GREEN, blockstate.getValue(COLOR_GREEN)).setValue(COLOR_BLUE, blockstate.getValue(COLOR_BLUE));
        }
        else {
            FluidState fluidstate = p_56361_.getLevel().getFluidState(blockpos);
            BlockState blockstate1 = ColorReference.addColorToBlockState(this.defaultBlockState(),color).setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
            Direction direction = p_56361_.getClickedFace();
            return direction != Direction.DOWN && (direction == Direction.UP || !(p_56361_.getClickLocation().y - (double)blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.setValue(TYPE, SlabType.TOP);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        ItemStack picked = state.getBlock().getCloneItemStack(world, pos, state);
        int getColor = ColorReference.getColorFromStateInt(state);
        return ColorReference.addColorToItemStack(picked,getColor);
    }

    @Override
    public void setPlacedBy(Level p_49847_, BlockPos p_49848_, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        if(p_49850_ instanceof Player)
        {
            Player player = ((Player)p_49850_);
            if(player.getOffhandItem().getItem() instanceof ColorApplicator)
            {
                int getColor = ColorReference.getColorFromItemStackInt(player.getOffhandItem());
                BlockState newState = ColorReference.addColorToBlockState(p_49849_,getColor);
                p_49847_.setBlock(p_49848_,newState,3);
            }
            else
            {
                int getColor = ColorReference.getColorFromItemStackInt(p_49851_);
                BlockState newState = ColorReference.addColorToBlockState(p_49849_,getColor);
                p_49847_.setBlock(p_49848_,newState,3);
            }
        }

        super.setPlacedBy(p_49847_, p_49848_, p_49849_, p_49850_, p_49851_);
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {

        if(p_60506_.getItemInHand(p_60507_).getItem() instanceof ColorApplicator)
        {
            int getColor = ColorReference.getColorFromItemStackInt(p_60506_.getItemInHand(p_60507_));
            BlockState newState = ColorReference.addColorToBlockState(p_60503_,getColor);
            p_60504_.setBlock(p_60505_,newState,3);
            //p_60504_.markAndNotifyBlock(p_60505_,null,p_60503_,newState,3,1);
            return InteractionResult.SUCCESS;
        }

        /*if(p_60506_.getItemInHand(p_60507_).getItem() instanceof ItemChisel)
        {
            if(p_60503_.getBlock() instanceof CrystalSlab)
            {
                p_60504_.setBlock(p_60505_, BlockReference.getSlab(p_60503_,false),3);
                return InteractionResult.SUCCESS;
            }
        }*/

        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }

    @Override
    public void playerWillDestroy(Level p_56212_, BlockPos p_56213_, BlockState p_56214_, Player p_56215_) {

        if(!p_56212_.isClientSide())
        {
            if (p_56214_.getBlock() instanceof BaseColoredSlabBlock) {
                if (!p_56212_.isClientSide && !p_56215_.isCreative()) {
                    ItemStack itemstack = new ItemStack(this);
                    int getColor = ColorReference.getColorFromStateInt(p_56214_);
                    ItemStack newStack = ColorReference.addColorToItemStack(itemstack,getColor);
                    newStack.setCount((p_56214_.getValue(TYPE).equals(SlabType.DOUBLE))?(2):(1));
                    ItemEntity itementity = new ItemEntity(p_56212_, (double)p_56213_.getX() + 0.5D, (double)p_56213_.getY() + 0.5D, (double)p_56213_.getZ() + 0.5D, newStack);
                    itementity.setDefaultPickUpDelay();
                    p_56212_.addFreshEntity(itementity);
                }
            }
        }
        super.playerWillDestroy(p_56212_, p_56213_, p_56214_, p_56215_);
    }

    @Override
    public void appendHoverText(ItemStack p_49816_, @org.jetbrains.annotations.Nullable BlockGetter p_49817_, List<Component> p_49818_, TooltipFlag p_49819_) {
        super.appendHoverText(p_49816_, p_49817_, p_49818_, p_49819_);

        if(!p_49816_.getItem().equals(DeferredRegisterItems.PEDESTAL_UPGRADE_BASE)) {
            TranslatableComponent description = new TranslatableComponent(getDescriptionId() + ".description");
            description.withStyle(ChatFormatting.AQUA);
            p_49818_.add(description);
        }
    }
}
