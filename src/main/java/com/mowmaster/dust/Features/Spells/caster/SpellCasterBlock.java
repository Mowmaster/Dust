package com.mowmaster.dust.Features.Spells.caster;

import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

// The Spell Caster: casts the spell on the Spell Scroll inside it, out of its face, each time it gets a redstone pulse
// (like a dispenser). It needs no mana: it runs on dust and energy crystals (SpellCasterBlockEntity).
//   right-click with a Spell Scroll        put it in (swapping out any scroll already there). Not a Self spell:
//                                          a block has no self to cast it on.
//   right-click with element dust          feed it the dust (the whole stack)
//   right-click with energy crystals       feed it the crystals (the whole stack)
//   sneak-right-click with an empty hand   take the scroll out
//   right-click with an empty hand         what it holds: the spell, its dust and its energy
// Hoppers and pipes can feed it dust and energy crystals from any side.
public class SpellCasterBlock extends Block implements EntityBlock
{
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    // A dispenser's delay between the pulse and the shot
    private static final int DELAY = 4;

    public SpellCasterBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TRIGGERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, TRIGGERED);
    }

    // Faces away from the player, like a dispenser (looking down places it facing down)
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
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
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new SpellCasterBlockEntity(pos, state);
    }

    // Server side: it takes in what hoppers feed it each tick. (A BlockEntityTicker is a method called every tick.)
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (level.isClientSide() || type != com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry.SPELL_CASTER.get()) return null;
        return (BlockEntityTicker<T>) (BlockEntityTicker<SpellCasterBlockEntity>) SpellCasterBlockEntity::serverTick;
    }

    // ---- redstone: a pulse casts (after DELAY ticks), like a dispenser ----

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston)
    {
        boolean powered = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
        boolean triggered = state.getValue(TRIGGERED);
        if (powered && !triggered) {
            level.scheduleTick(pos, this, DELAY);
            level.setBlock(pos, state.setValue(TRIGGERED, true), Block.UPDATE_CLIENTS);
        } else if (!powered && triggered) {
            level.setBlock(pos, state.setValue(TRIGGERED, false), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if (level.getBlockEntity(pos) instanceof SpellCasterBlockEntity caster) caster.cast(level, state.getValue(FACING));
    }

    // ---- clicks ----

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof SpellCasterBlockEntity caster)) return InteractionResult.TRY_WITH_EMPTY_HAND;
        boolean scroll = stack.is(DustItemRegistry.SPELL_SCROLL.get());
        if (!scroll && !caster.accepts(stack)) return InteractionResult.TRY_WITH_EMPTY_HAND;
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (scroll && SpellCasterBlockEntity.isSelfSpell(stack)) {
            // A Self spell has nobody to land on when a block casts it
            player.sendOverlayMessage(Component.translatable("dust.caster.no_self"));
            return InteractionResult.FAIL;
        }
        if (scroll) {
            // In goes the held scroll; out comes any that was there
            ItemStack old = caster.swapScroll(stack.copyWithCount(1));
            stack.shrink(1);
            if (!old.isEmpty() && !player.addItem(old)) player.spawnAtLocation((ServerLevel) level, old);
        } else {
            caster.feed(stack);
        }
        if (player instanceof ServerPlayer serverPlayer) caster.report(serverPlayer);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof SpellCasterBlockEntity caster)) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (player.isShiftKeyDown()) {
            ItemStack old = caster.swapScroll(ItemStack.EMPTY);
            if (!old.isEmpty() && !player.addItem(old)) player.spawnAtLocation((ServerLevel) level, old);
        }
        if (player instanceof ServerPlayer serverPlayer) caster.report(serverPlayer);
        return InteractionResult.SUCCESS;
    }
}
