package com.mowmaster.dust.Features.Apparatus.still;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.Machines.StationBlock;
import com.mowmaster.dust.Features.Machines.StationInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import org.jspecify.annotations.Nullable;

// The Burnable Firebox (models/block/apparatus/firebox_burnable.json): the bottom of a Still (StillLayout). It burns
// furnace fuel (StillFireboxBlockEntity): right-click it with fuel, or feed it with hoppers and pipes from any side.
// LIT while it burns: it glows and flickers, and heats the still above it. Right-click with an empty hand: its fuel.
public class StillFireboxBlock extends StationBlock
{
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public StillFireboxBlock(Properties properties)
    {
        super(properties, Shapes.block());
        registerDefaultState(defaultBlockState().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new StillFireboxBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (level.isClientSide() || type != DustBlockEntityRegistry.STILL_FIREBOX.get()) return null;
        return (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof StillFireboxBlockEntity firebox && tickLevel instanceof ServerLevel server) firebox.serverTick(server);
        };
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return StationInteractions.rightClick(stack, level, pos, player, hit);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!player.getMainHandItem().isEmpty() || !(level.getBlockEntity(pos) instanceof StillFireboxBlockEntity firebox)) return InteractionResult.PASS;
        if (!level.isClientSide()) player.sendOverlayMessage(firebox.status());
        return InteractionResult.SUCCESS;
    }

    // Burning: flames and smoke at its front and sides, and a crackle now and then
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (!state.getValue(LIT)) return;
        if (random.nextInt(10) == 0) {
            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, net.minecraft.sounds.SoundEvents.FURNACE_FIRE_CRACKLE,
                    net.minecraft.sounds.SoundSource.BLOCKS, 0.8F, 1.0F, false);
        }
        net.minecraft.core.Direction side = net.minecraft.core.Direction.Plane.HORIZONTAL.getRandomDirection(random);
        double along = (random.nextDouble() - 0.5) * 0.6;
        double x = pos.getX() + 0.5 + side.getStepX() * 0.52 + (side.getStepX() == 0 ? along : 0);
        double z = pos.getZ() + 0.5 + side.getStepZ() * 0.52 + (side.getStepZ() == 0 ? along : 0);
        double y = pos.getY() + 0.2 + random.nextDouble() * 0.3;
        level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0, 0);
    }
}
