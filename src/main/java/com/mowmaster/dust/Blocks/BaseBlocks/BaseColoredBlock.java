package com.mowmaster.dust.Blocks.BaseBlocks;

import com.mowmaster.dust.Blocks.GeneratedBlocks.BaseCrystalNodeBlock;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class BaseColoredBlock extends Block
{
    public static final IntegerProperty COLOR_RED = IntegerProperty.create("color_red", 0, 3);
    public static final IntegerProperty COLOR_GREEN = IntegerProperty.create("color_green", 0, 3);
    public static final IntegerProperty COLOR_BLUE = IntegerProperty.create("color_blue", 0, 3);

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public BaseColoredBlock(BlockBehaviour.Properties p_152915_)
    {
        super(p_152915_);
        this.registerDefaultState(this.stateDefinition.any().setValue(COLOR_RED, Integer.valueOf(3)).setValue(COLOR_GREEN, Integer.valueOf(3)).setValue(COLOR_BLUE, Integer.valueOf(3)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_56120_) {
        p_56120_.add(COLOR_RED).add(COLOR_GREEN).add(COLOR_BLUE);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_56089_) {
        BlockState blockstate = p_56089_.getLevel().getBlockState(p_56089_.getClickedPos());
        if (blockstate.is(this))
        {
            return blockstate.setValue(COLOR_RED, Integer.valueOf(Math.min(3, blockstate.getValue(COLOR_RED)))).setValue(COLOR_GREEN, Integer.valueOf(Math.min(3, blockstate.getValue(COLOR_GREEN)))).setValue(COLOR_BLUE, Integer.valueOf(Math.min(3, blockstate.getValue(COLOR_BLUE))));
        }
        else return super.getStateForPlacement(p_56089_);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        ItemStack picked = state.getBlock().getCloneItemStack(world, pos, state);
        return ColorReference.addColorToItemStackRGB(picked,state.getValue(COLOR_RED),state.getValue(COLOR_GREEN),state.getValue(COLOR_BLUE));
    }

    @Override
    public void setPlacedBy(Level p_49847_, BlockPos p_49848_, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        if(p_49850_ instanceof Player)
        {
            Player player = ((Player)p_49850_);
            int[] colored = ColorReference.getColorFromItemStackRGB(p_49851_);
            BlockState newState = p_49849_.setValue(COLOR_RED,colored[0]).setValue(COLOR_GREEN,colored[1]).setValue(COLOR_BLUE,colored[2]);
            p_49847_.setBlock(p_49848_,newState,3);
        }

        super.setPlacedBy(p_49847_, p_49848_, p_49849_, p_49850_, p_49851_);
    }

    @Override
    public void playerWillDestroy(Level p_56212_, BlockPos p_56213_, BlockState p_56214_, Player p_56215_) {

        if(!p_56212_.isClientSide())
        {
            if (p_56214_.getBlock() instanceof BaseColoredBlock) {
                if (!p_56212_.isClientSide && !p_56215_.isCreative()) {
                    ItemStack itemstack = new ItemStack(this);
                    List<Integer> getRGB = ColorReference.getRGBFromState(p_56214_);
                    ItemStack newStack = ColorReference.addColorToItemStackRGB(itemstack,getRGB.get(0),getRGB.get(1),getRGB.get(2));
                    newStack.setCount(1);
                    ItemEntity itementity = new ItemEntity(p_56212_, (double)p_56213_.getX() + 0.5D, (double)p_56213_.getY() + 0.5D, (double)p_56213_.getZ() + 0.5D, newStack);
                    itementity.setDefaultPickUpDelay();
                    p_56212_.addFreshEntity(itementity);
                }
            }
        }
        super.playerWillDestroy(p_56212_, p_56213_, p_56214_, p_56215_);
    }
}
