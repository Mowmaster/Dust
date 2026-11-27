package com.mowmaster.dust.Features.Apparatus;

import com.mowmaster.dust.Features.DustStorage.DustNetwork;
import com.mowmaster.dust.Features.DustStorage.DustTubeConnectable;
import com.mowmaster.dust.Features.Machines.StationBlock;
import com.mowmaster.dust.Features.Machines.StationInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.function.BiFunction;

// The block every alchemical apparatus uses (one block class; each apparatus has its own block entity:
// ApparatusBlockEntity has the shared rules). Built of copper, brass and iron, they face the player who places them,
// like a furnace (the front, with its spout, toward them).
//   right-click the intake on top with an item     puts it in (StationInteractions; sneak to place a block on top instead)
//   left-click the spout on the front              takes what it has handed on (sneak: a stack)
//   right-click with an empty hand                 what it's doing (the first to do so owns an apparatus nobody placed)
//   a redstone signal                              pauses it
// Glass Dust Tubes join on to every side (DustTubeConnectable).
public class ApparatusBlock extends StationBlock implements DustTubeConnectable
{
    // How to make its block entity (a constructor reference such as StillPotBlockEntity::new fits a BiFunction:
    // a function taking two values, here the position and the block state)
    private final BiFunction<BlockPos, BlockState, ? extends ApparatusBlockEntity> factory;
    // Its shape turned to each facing (ApparatusShapes.turned)
    private final java.util.Map<net.minecraft.core.Direction, VoxelShape> shapes;

    // How far in its body is on each side, measured off its model (facing north): where tubes meet it (AttachInsets).
    // null: worked out from its shape
    private final com.mowmaster.dust.Features.DustStorage.AttachInsets.@Nullable Insets insets;

    public ApparatusBlock(Properties properties, VoxelShape shape, BiFunction<BlockPos, BlockState, ? extends ApparatusBlockEntity> factory)
    {
        this(properties, shape, null, factory);
    }

    public ApparatusBlock(Properties properties, VoxelShape shape, com.mowmaster.dust.Features.DustStorage.AttachInsets.@Nullable Insets insets,
                          BiFunction<BlockPos, BlockState, ? extends ApparatusBlockEntity> factory)
    {
        super(properties, shape);
        this.factory = factory;
        this.shapes = ApparatusShapes.turned(shape);
        this.insets = insets;
    }

    // Its insets on its sides (measured off its model, turned with it); the bottom and top from its shape
    @Override
    public double attachInset(BlockState state, net.minecraft.core.Direction side)
    {
        if (insets == null || !side.getAxis().isHorizontal()) return com.mowmaster.dust.Features.DustStorage.AttachInsets.fromShape(state, side);
        return insets.turned(state.getValue(FACING), side);
    }

    @Override
    protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context)
    {
        return shapes.getOrDefault(state.getValue(FACING), super.getShape(state, level, pos, context));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return factory.apply(pos, state);
    }

    // Every apparatus ticks the same way (ApparatusBlockEntity.tick: work on the server, animation on the client)
    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        return (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof ApparatusBlockEntity apparatus) apparatus.tick(tickLevel);
        };
    }

    // The front toward the player (the opposite of the way they're looking)
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    // Whoever places it owns it: it catches the elements they've discovered
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide() && placer instanceof Player player && level.getBlockEntity(pos) instanceof ApparatusBlockEntity apparatus) {
            apparatus.setOwner(player);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return StationInteractions.rightClick(stack, level, pos, player, hit);
    }

    // An empty hand: what it's doing (an apparatus nobody placed is owned by whoever does this first)
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!player.getMainHandItem().isEmpty() || !(level.getBlockEntity(pos) instanceof ApparatusBlockEntity apparatus)) return InteractionResult.PASS;
        if (!level.isClientSide()) {
            if (!apparatus.hasOwner()) apparatus.setOwner(player);
            player.sendOverlayMessage(apparatus.status());
        }
        return InteractionResult.SUCCESS;
    }

    // A neighbour changed: a redstone signal pauses it
    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston)
    {
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof ApparatusBlockEntity apparatus) apparatus.checkPower(level);
    }

    // Placed or taken away: the tube networks round it have to be found again
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
