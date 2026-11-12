package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.MagicalFlintAndSteelItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

// Alchemical Separation Table: splits one solid element out of something (Separation has the rules).
//   Right-click the pad behind the scroll with paper    stocks it with paper (to catch compounds)
//   Right-click the scroll with anything                sets one on it to separate
//   Right-click with a Magical Flint and Steel          starts it: the glyph rises off the scroll and spins with the
//                                                       item, glowing, and dust streams down to the two outputs
//   Left-click any of them                              takes from it (1, or a stack sneaking); see StationInteractions
public class ResearchSeparationBlock extends Block implements EntityBlock
{
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    // The table top and its four legs
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 12, 0, 16, 16, 16),
            Block.box(0, 0, 0, 4, 12, 4), Block.box(12, 0, 0, 16, 12, 4),
            Block.box(0, 0, 12, 4, 12, 16), Block.box(12, 0, 12, 16, 12, 16));

    public ResearchSeparationBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    // Faces the way the player looks: the outputs on their side, the input pad beyond
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
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
        return new ResearchSeparationBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (type != DustBlockEntityRegistry.RESEARCH_SEPARATION.get()) return null;
        BlockEntityTicker<ResearchSeparationBlockEntity> ticker = level.isClientSide() ? ResearchSeparationBlockEntity::clientTick : ResearchSeparationBlockEntity::serverTick;
        return (BlockEntityTicker<T>) ticker;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof ResearchSeparationBlockEntity table)) return InteractionResult.PASS;

        if (stack.getItem() instanceof MagicalFlintAndSteelItem) {
            String problem = table.cannotStart();
            if (problem != null) {
                if (!level.isClientSide()) player.sendOverlayMessage(Component.translatable("dust.separation." + problem));
                return InteractionResult.FAIL;
            }
            if (level instanceof ServerLevel serverLevel) {
                table.start(serverLevel, player);
                EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                stack.hurtAndBreak(1, player, slot);
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }

        // Anything else goes where it's clicked (StationInteractions: paper on its pad, the item on the scroll)
        return StationInteractions.rightClick(stack, level, pos, player, hit);
    }
}
