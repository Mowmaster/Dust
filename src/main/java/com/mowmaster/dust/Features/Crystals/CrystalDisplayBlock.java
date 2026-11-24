package com.mowmaster.dust.Features.Crystals;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustStorage.DustNetwork;
import com.mowmaster.dust.Features.DustStorage.DustTubeConnectable;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.grimoire.GrimoireItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

// The Crystal Display (models/block/crystal/crystal_display.json): a stand for a Large Crystal picked with Silk Touch,
// which then gives off its element's effect round it (CrystalDisplayBlockEntity). LIT while its crystal is working.
//   right-click with a Large Crystal       sets it on (keeping the charge it had)
//   right-click with a Spell Scroll / a    sets that spell on it (a self spell of the crystal's element)
//     Grimoire (its open page)
//   right-click with an empty hand         takes the crystal off
//   sneak-right-click with an empty hand   takes the spell off (or says what it's doing)
// Tubes join on to any side but the top, and fill its crystal with its element. Displays side by side in a 2x2 or 3x3
// square work as one (DisplayGroup): placing or breaking one works its group out again.
public class CrystalDisplayBlock extends Block implements EntityBlock, DustTubeConnectable
{
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final VoxelShape SHAPE = Shapes.or(Block.box(0, 0, 0, 16, 2, 16), Block.box(3, 2, 3, 13, 9, 13), Block.box(1, 9, 1, 15, 12, 15));

    public CrystalDisplayBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(LIT);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new CrystalDisplayBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (level.isClientSide() || type != DustBlockEntityRegistry.CRYSTAL_DISPLAY.get()) return null;
        return (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof CrystalDisplayBlockEntity display && tickLevel instanceof ServerLevel server) display.serverTick(server);
        };
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof CrystalDisplayBlockEntity display)) return InteractionResult.TRY_WITH_EMPTY_HAND;
        if (stack.getItem() instanceof LargeCrystalItem) {
            // One already on: nothing (rather than placing the crystal on top as a block)
            if (display.element() != null) return InteractionResult.CONSUME;
            if (level.isClientSide()) return InteractionResult.SUCCESS;
            if (display.insertCrystal(stack)) {
                if (!player.getAbilities().instabuild) stack.shrink(1);
                level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }
        Spell spell = stack.get(DustComponentDataRegistry.SPELL.get());
        if (spell == null) spell = GrimoireItem.openSpell(stack);
        if (spell != null) {
            if (level.isClientSide()) return InteractionResult.SUCCESS;
            String refused = display.setSpell(spell);
            player.sendOverlayMessage(Component.translatable(refused != null ? refused : "dust.display.spell_set"));
            if (refused == null) level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, 1.2F);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof CrystalDisplayBlockEntity display)) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (player.isSecondaryUseActive()) {
            if (display.clearSpell()) player.sendOverlayMessage(Component.translatable("dust.display.spell_cleared"));
            else player.sendOverlayMessage(display.status(player));
            return InteractionResult.SUCCESS;
        }
        ItemStack crystal = display.takeCrystal();
        if (crystal.isEmpty()) {
            player.sendOverlayMessage(display.status(player));
            return InteractionResult.SUCCESS;
        }
        if (!player.getInventory().add(crystal)) player.spawnAtLocation((ServerLevel) level, crystal);
        level.playSound(null, pos, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 0.8F, 1.2F);
        return InteractionResult.SUCCESS;
    }

    // Its group is worked out a tick after it's placed (its block entity isn't there yet while it's being placed)
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
    {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (oldState.is(this)) return;
        level.scheduleTick(pos, this, 1);
        DustNetwork.changed(level);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        CrystalDisplayBlockEntity.refresh(level, pos);
    }

    // Broken: the displays that were beside it work out their groups again
    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston)
    {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        for (Direction side : Direction.Plane.HORIZONTAL) {
            if (level.getBlockState(pos.relative(side)).getBlock() instanceof CrystalDisplayBlock) CrystalDisplayBlockEntity.refresh(level, pos.relative(side));
        }
        DustNetwork.changed(level);
    }

    // Tubes on any side but the top
    @Override
    public boolean connectsDustTube(BlockState state, Direction side)
    {
        return side != Direction.UP;
    }

    @Override
    public @Nullable DustElement tubeElement(BlockGetter level, BlockPos pos)
    {
        return level.getBlockEntity(pos) instanceof CrystalDisplayBlockEntity display ? display.element() : null;
    }
}
