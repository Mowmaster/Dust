package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.mowlib.storage.StorageInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

// Research Crusher: crushes any block into pebbles of it ("Crushed Stone"), one block at a time, 100 ticks each.
//   Right-click with a block      drops one into the funnel (if it's empty). It sinks into the turning wheels, throwing
//                                 off bits of itself, and comes out as pebbles in the tray below.
//   Right-click empty-handed      takes the pebbles from the tray (or the block back, if the tray's empty)
// The step (block -> crusher -> pebbles) goes into the research notes of whoever put the block in.
public class ResearchCrusherBlock extends Block implements EntityBlock
{
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    public ResearchCrusherBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    // The tray faces the player who placed it
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new ResearchCrusherBlockEntity(pos, state);
    }

    // Ticks on both sides: the server crushes, the client counts for the animation.
    // The cast is safe because the type is checked first (the usual pattern for a block entity ticker).
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (type != DustBlockEntityRegistry.RESEARCH_CRUSHER.get()) return null;
        BlockEntityTicker<ResearchCrusherBlockEntity> ticker = level.isClientSide() ? ResearchCrusherBlockEntity::clientTick : ResearchCrusherBlockEntity::serverTick;
        return (BlockEntityTicker<T>) ticker;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!(stack.getItem() instanceof BlockItem) || !(level.getBlockEntity(pos) instanceof ResearchCrusherBlockEntity crusher) || !crusher.canInsert()) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        if (!level.isClientSide()) {
            crusher.insert(stack, player.getUUID());
            stack.consume(1, player);
            level.playSound(null, pos, SoundEvents.GRAVEL_PLACE, SoundSource.BLOCKS, 0.8F, 0.8F);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof ResearchCrusherBlockEntity crusher)) return InteractionResult.PASS;
        if (crusher.getOutput().isEmpty() && crusher.getInput().isEmpty()) return InteractionResult.PASS;
        if (!level.isClientSide()) {
            ItemStack taken = !crusher.getOutput().isEmpty() ? crusher.takeOutput() : crusher.takeInput();
            StorageInteractions.giveToPlayer(player, taken);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
        }
        return InteractionResult.SUCCESS;
    }

    // While crushing: bits of the block fly off where the wheels bite (its own break particles)
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (!(level.getBlockEntity(pos) instanceof ResearchCrusherBlockEntity crusher) || !crusher.isRunning()) return;
        if (!(crusher.getInput().getItem() instanceof BlockItem blockItem)) return;
        Vec3 at = CrusherLayout.turned(CrusherLayout.WHEELS_NIP, state.getValue(FACING));
        BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, blockItem.getBlock().defaultBlockState());
        for (int i = 0; i < 3; i++) {
            level.addParticle(particle, pos.getX() + at.x + (random.nextDouble() - 0.5) * 0.4, pos.getY() + at.y + random.nextDouble() * 0.15,
                    pos.getZ() + at.z + (random.nextDouble() - 0.5) * 0.3, (random.nextDouble() - 0.5) * 0.1, 0.05, (random.nextDouble() - 0.5) * 0.1);
        }
    }
}
