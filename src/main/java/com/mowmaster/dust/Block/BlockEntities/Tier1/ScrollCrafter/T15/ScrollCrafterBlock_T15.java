package com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.T15;

import com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.ScrollCrafterBlockBase;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ScrollCrafterBlock_T15 extends ScrollCrafterBlockBase {


    public static final DirectionProperty SIDED_ROTATION_4 = BlockStateProperties.HORIZONTAL_FACING;
    protected final VoxelShape TABLE;

    public ScrollCrafterBlock_T15(Properties p_152915_) {
        super(p_152915_);
        this.registerDefaultState(ColorReference.addColorToBlockState(this.defaultBlockState(),ColorReference.DEFAULTCOLOR).setValue(SIDED_ROTATION_4, Direction.NORTH));
        this.TABLE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 4.0D, 12.0D, 4.0D),
                Block.box(12.0D, 0.0D, 12.0D, 16.0D, 12.0D, 16.0D),
                Block.box(0.0D, 0.0D, 12.0D, 4.0D, 12.0D, 16.0D),
                Block.box(12.0D, 0.0D, 0.0D, 16.0D, 12.0D, 4.0D),
                Block.box(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return this.TABLE;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        ItemStack picked = state.getBlock().getCloneItemStack(world, pos, state);
        int getColor = ColorReference.getColorFromStateInt(state);
        return ColorReference.addColorToItemStack(picked,getColor);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_48689_) {
        LevelAccessor levelaccessor = p_48689_.getLevel();
        BlockPos blockpos = p_48689_.getClickedPos();
        Direction direction = p_48689_.getClickedFace();
        BlockState blockstate = levelaccessor.getBlockState(p_48689_.getClickedPos().relative(direction.getOpposite()));
        int getColor = ColorReference.getColorFromStateInt(blockstate);
        return blockstate.is(this) &&
                blockstate.getValue(SIDED_ROTATION_4) == direction
                ?
                ColorReference.addColorToBlockState(this.defaultBlockState(),getColor).setValue(SIDED_ROTATION_4, p_48689_.getHorizontalDirection())
                :
                ColorReference.addColorToBlockState(this.defaultBlockState(),getColor).setValue(SIDED_ROTATION_4, p_48689_.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState p_152033_, Rotation p_152034_) {
        return p_152033_.setValue(SIDED_ROTATION_4, p_152034_.rotate(p_152033_.getValue(SIDED_ROTATION_4)));
    }

    @Override
    public BlockState mirror(BlockState p_152030_, Mirror p_152031_) {
        return p_152030_.rotate(p_152031_.getRotation(p_152030_.getValue(SIDED_ROTATION_4)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_152043_) {
        p_152043_.add(SIDED_ROTATION_4, COLOR_RED, COLOR_GREEN, COLOR_BLUE);
    }



    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return DeferredBlockEntityTypes.CRAFTER_SCROLL_T15.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : (level0, pos, state0, blockEntity) -> ((ScrollCrafterBlockEntity_T15) blockEntity).tick();
    }
}
