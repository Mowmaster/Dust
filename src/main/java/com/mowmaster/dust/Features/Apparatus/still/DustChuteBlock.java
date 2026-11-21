package com.mowmaster.dust.Features.Apparatus.still;

import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.Map;

// The Dust Chute (models/block/apparatus/dust_chute.json; filtered: dust_chute_filtered.json): fixed to a side of a Still
// Pot (FACING: away from the pot, its back against it), it lets the pot's dust out as Dust Piles under it
// (StillPotBlockEntity.pourChutes). Filtered (FILTERED, the element kept on its block entity, its symbol drawn on it:
// Client/DustChuteRenderer): it only lets that element out, and keeps the pot to it. Filter it by right-clicking it with
// Research Notes open at an element's page (the client says which: DustChuteFilterPayload), or with an element's
// crystal, dust or Alchemical Dust; shears take the filter off.
public class DustChuteBlock extends Block implements EntityBlock
{
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty FILTERED = BooleanProperty.create("filtered");
    // Its back plate and flap, for a chute facing south (the back on the north edge), turned for the others
    private static final Map<Direction, VoxelShape> SHAPES = Map.of(
            Direction.SOUTH, Block.box(3, 2, 0, 13, 11, 5),
            Direction.NORTH, Block.box(3, 2, 11, 13, 11, 16),
            Direction.EAST, Block.box(0, 2, 3, 5, 11, 13),
            Direction.WEST, Block.box(11, 2, 3, 16, 11, 13));

    public DustChuteBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(FILTERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, FILTERED);
    }

    // Placed against a block's side: facing out from it
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Direction face = context.getClickedFace();
        Direction facing = face.getAxis().isHorizontal() ? face : context.getHorizontalDirection().getOpposite();
        return defaultBlockState().setValue(FACING, facing);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DustChuteBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof DustChuteBlockEntity chute)) return InteractionResult.TRY_WITH_EMPTY_HAND;
        // Research Notes: the element page open on the client
        if (stack.is(DustItemRegistry.RESEARCH_NOTES.get())) {
            if (level.isClientSide()) com.mowmaster.dust.Features.Apparatus.still.Client.DustChuteClient.filterFromNotes(pos);
            return InteractionResult.SUCCESS;
        }
        if (stack.getItem() instanceof ShearsItem) {
            if (chute.filter() == null) return InteractionResult.TRY_WITH_EMPTY_HAND;
            if (level.isClientSide()) return InteractionResult.SUCCESS;
            chute.setFilter(null);
            stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
            level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 0.8F, 1.2F);
            return InteractionResult.SUCCESS;
        }
        DustElement element = DustChuteBlockEntity.elementOf(stack);
        if (element == null) return InteractionResult.TRY_WITH_EMPTY_HAND;
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        chute.setFilter(element);
        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.8F, 1.0F);
        return InteractionResult.SUCCESS;
    }

    // An empty hand: what it lets out
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof DustChuteBlockEntity chute)) return InteractionResult.PASS;
        if (!level.isClientSide()) {
            player.sendOverlayMessage(chute.filter() == null ? Component.translatable("dust.chute.unfiltered")
                    : Component.translatable("dust.chute.filtered", com.mowmaster.dust.Features.DustComposition.ElementNames.forViewer(chute.filter(), player)));
        }
        return InteractionResult.SUCCESS;
    }
}
