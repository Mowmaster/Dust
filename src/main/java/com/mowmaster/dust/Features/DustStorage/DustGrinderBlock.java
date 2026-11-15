package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.Machines.StationInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

// The Dust Grinder (models/block/station/dust_grinder.json): grinds fully researched compounds, Alchemical Dust and
// element crystals into element orbs, or into Dust Jars next to it (DustGrinderBlockEntity).
//   right-click the hole on top with one       puts them in (a stack of one kind)
//   left-click the hole                        takes them back (1, or a stack sneaking)
//   right-click anywhere else / empty hand     turns the top one full turn, grinding one
//   a redstone pulse                           also turns it once
// The block model is the base (dust_grinder_base.json); the top and its handle turn in Client/DustGrinderRenderer.
public class DustGrinderBlock extends Block implements EntityBlock
{
    // Remembers whether it's powered, so only a new pulse turns it (not every neighbour update while it stays on)
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final VoxelShape SHAPE = Shapes.or(Block.box(0, 0, 0, 16, 16, 16), Block.box(2, 16, 2, 3, 18, 3));

    public DustGrinderBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(POWERED);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DustGrinderBlockEntity(pos, state);
    }

    // Ticks on both sides: the server turns and grinds, the client counts for the animation.
    // The cast is safe because the type is checked first (the usual pattern for a block entity ticker).
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (type != DustBlockEntityRegistry.DUST_GRINDER.get()) return null;
        BlockEntityTicker<DustGrinderBlockEntity> ticker = level.isClientSide() ? DustGrinderBlockEntity::clientTick : DustGrinderBlockEntity::serverTick;
        return (BlockEntityTicker<T>) ticker;
    }

    // Grindable items go into the hole; anything else falls through to useWithoutItem (TRY_WITH_EMPTY_HAND)
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return StationInteractions.rightClick(stack, level, pos, player, hit);
    }

    // Turns the top once
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof DustGrinderBlockEntity grinder) || grinder.getInput().isEmpty()) return InteractionResult.PASS;
        if (!level.isClientSide()) grinder.startTurn(level);
        return InteractionResult.SUCCESS;
    }

    // A redstone pulse turns it once: only when the power comes on
    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston)
    {
        if (level.isClientSide()) return;
        boolean powered = level.hasNeighborSignal(pos);
        if (powered == state.getValue(POWERED)) return;
        level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_CLIENTS);
        if (powered && level.getBlockEntity(pos) instanceof DustGrinderBlockEntity grinder) grinder.startTurn(level);
    }
}
