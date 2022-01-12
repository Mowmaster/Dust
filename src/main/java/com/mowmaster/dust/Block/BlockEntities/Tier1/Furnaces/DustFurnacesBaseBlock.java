package com.mowmaster.dust.Block.BlockEntities.Tier1.Furnaces;

import com.mowmaster.dust.Block.BlockEntities.Tier1.Tier1BaseBlock;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class DustFurnacesBaseBlock extends Tier1BaseBlock
{
    public static final DirectionProperty SIDED_ROTATION_4 = BlockStateProperties.HORIZONTAL_FACING;

    public DustFurnacesBaseBlock(Properties p_152915_) {
        super(p_152915_);
        this.registerDefaultState(ColorReference.addColorToBlockState(this.defaultBlockState(),ColorReference.DEFAULTCOLOR).setValue(SIDED_ROTATION_4, Direction.NORTH));
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
}
