package com.mowmaster.dust.Features.Apparatus.separator;

import com.mowmaster.dust.Features.DustStorage.DustNetwork;
import com.mowmaster.dust.Features.DustStorage.DustTubeConnectable;
import com.mowmaster.dust.Features.Machines.StationBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

// A Dust Separator Extension (models/block/apparatus/dust_seperator_extension.json; the block draws its base, the drum
// is drawn turning with the separator's: Client/DustSeparatorRenderer): one more drum section, placed against the open
// end of a separator (or of the extension before it), up to DustSeparatorBlock.MAX_EXTENSIONS. It takes the facing of
// the drum it's added to. Tubes join on underneath only. A Jar Label under it keeps its section to its element.
public class DustSeparatorExtensionBlock extends Block implements EntityBlock, DustTubeConnectable
{
    public static final net.minecraft.world.level.block.state.properties.EnumProperty<Direction> FACING = StationBlock.FACING;
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 14, 16);

    public DustSeparatorExtensionBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    // Against a drum's open end it lines up with it; otherwise its front toward the player
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        for (Direction side : Direction.Plane.HORIZONTAL) {
            BlockState neighbor = level.getBlockState(pos.relative(side));
            Direction facing = null;
            if (neighbor.getBlock() instanceof DustSeparatorBlock) facing = neighbor.getValue(StationBlock.FACING);
            else if (neighbor.getBlock() instanceof DustSeparatorExtensionBlock) facing = neighbor.getValue(FACING);
            // The drum's open side points at this block
            if (facing != null && facing.getClockWise() == side.getOpposite()) return defaultBlockState().setValue(FACING, facing);
        }
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DustSeparatorExtensionBlockEntity(pos, state);
    }

    @Override
    public boolean connectsDustTube(BlockState state, Direction side)
    {
        return side == Direction.DOWN;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        InteractionResult labelled = SectionLabels.use(stack, level, pos, player, hand);
        return labelled != null ? labelled : InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
    {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!oldState.is(this)) DustNetwork.changed(level);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston)
    {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        DustNetwork.changed(level);
    }
}
