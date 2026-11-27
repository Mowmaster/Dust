package com.mowmaster.dust.Features.Crystals.crystalizer;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustStorage.DustNetwork;
import com.mowmaster.dust.Features.DustStorage.DustTubeConnectable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

// The Dust Crystalizer (models/block/station/dust_crystalizer.json): grows crystals of the element dust piped into it
// (DustCrystalizerBlockEntity).
//   right-click with an empty hand    takes the crystal (any stage)
//   left-click the top                also takes it (StationInteractions)
//   comparators                       read the stage, 0-4
// It's never a redstone source (no isSignalSource / getSignal), so it can't power or lock a hopper under it.
// Its top counts as a full face (getBlockSupportShape), so the crystals it grows can stand on it.
// Glass Dust Tubes join on to its sides and bottom (DustTubeConnectable); the top is where the crystal grows.
public class DustCrystalizerBlock extends Block implements EntityBlock, DustTubeConnectable
{
    private static final VoxelShape SHAPE = Shapes.or(Block.box(0, 0, 0, 16, 10, 16), Block.box(2, 10, 2, 14, 12, 14),
            Block.box(4, 12, 4, 12, 14, 12), Block.box(6, 14, 6, 10, 16, 10));

    public DustCrystalizerBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos)
    {
        return Shapes.block();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DustCrystalizerBlockEntity(pos, state);
    }

    // Server only: the client just draws what's synced. The cast is safe because the type is checked first.
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (level.isClientSide() || type != DustBlockEntityRegistry.DUST_CRYSTALIZER.get()) return null;
        BlockEntityTicker<DustCrystalizerBlockEntity> ticker = DustCrystalizerBlockEntity::serverTick;
        return (BlockEntityTicker<T>) ticker;
    }

    // An empty hand takes the crystal
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof DustCrystalizerBlockEntity crystalizer) || crystalizer.crystal().isEmpty()) return InteractionResult.PASS;
        if (!level.isClientSide()) {
            net.minecraft.world.item.ItemStack crystal = crystalizer.take();
            if (!player.getInventory().add(crystal)) Block.popResource(level, pos.above(), crystal);
        }
        return InteractionResult.SUCCESS;
    }

    // ---- comparators: the stage (it gives out no redstone of its own) ----

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction)
    {
        return level.getBlockEntity(pos) instanceof DustCrystalizerBlockEntity crystalizer ? crystalizer.stage() : 0;
    }

    // ---- tubes ----

    @Override
    public boolean connectsDustTube(BlockState state, Direction side)
    {
        return side != Direction.UP;
    }

    @Override
    public @Nullable DustElement tubeElement(BlockGetter level, BlockPos pos)
    {
        return level.getBlockEntity(pos) instanceof DustCrystalizerBlockEntity crystalizer ? crystalizer.element() : null;
    }

    // Placed or taken away: the tube networks round it have to be found again
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
    {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!oldState.is(this)) DustNetwork.changed(level);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, net.minecraft.server.level.ServerLevel level, BlockPos pos, boolean movedByPiston)
    {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        DustNetwork.changed(level);
    }
}
