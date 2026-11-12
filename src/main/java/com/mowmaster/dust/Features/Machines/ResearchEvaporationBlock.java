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

// Evaporation Table: boils the gaseous and aqueous elements out of something with solids in it, leaving the solids.
//   Right-click the pad behind the pot with paper   stocks it with paper (to catch the compound)
//   Right-click the pot with anything               puts one in (it shows as liquid in the colour of what will boil off)
//   Right-click with a Magical Flint and Steel      starts it: the glyph spins over the pot
//   Right-click the pot (while it's going)          stirs: one full turn of the spoon per element in the item; then the
//                                                   glyph sinks into the pot and the solids stream to the output pad
//   Left-click the pot / paper / output             takes from it (StationInteractions)
public class ResearchEvaporationBlock extends Block implements EntityBlock
{
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    // The table and the pot on its stand
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 12, 0, 16, 16, 16),
            Block.box(0, 0, 0, 4, 12, 4), Block.box(12, 0, 0, 16, 12, 4),
            Block.box(0, 0, 12, 4, 12, 16), Block.box(12, 0, 12, 16, 12, 16),
            Block.box(6, 16, 6, 10, 21, 10));

    public ResearchEvaporationBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    // Faces the way the player looks: the output on their side, the paper beyond the pot
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
        return new ResearchEvaporationBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (type != DustBlockEntityRegistry.RESEARCH_EVAPORATION.get()) return null;
        BlockEntityTicker<ResearchEvaporationBlockEntity> ticker = level.isClientSide() ? ResearchEvaporationBlockEntity::clientTick : ResearchEvaporationBlockEntity::serverTick;
        return (BlockEntityTicker<T>) ticker;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof ResearchEvaporationBlockEntity table)) return InteractionResult.PASS;

        if (stack.getItem() instanceof MagicalFlintAndSteelItem && !table.isStirring()) {
            String problem = table.cannotStart();
            if (problem != null) {
                if (!level.isClientSide()) player.sendOverlayMessage(Component.translatable("dust.evaporation." + problem));
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
        // While it's going, the pot is for stirring, whatever's in hand
        if (table.isStirring() && StationInteractions.inArea(level, pos, hit, EvaporationLayout.POT_AREA)) {
            if (!level.isClientSide()) table.stir(level);
            return InteractionResult.SUCCESS;
        }
        // Blocks go through the Research Crusher first
        if (ResearchInput.refuseBlock(stack, level, pos, player, hit, EvaporationLayout.POT_AREA)) return InteractionResult.FAIL;
        return StationInteractions.rightClick(stack, level, pos, player, hit);
    }

    // Empty-handed: stirring
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof ResearchEvaporationBlockEntity table) || !table.isStirring()
                || !StationInteractions.inArea(level, pos, hit, EvaporationLayout.POT_AREA)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) table.stir(level);
        return InteractionResult.SUCCESS;
    }
}
