package com.mowmaster.dust.Features.Spells.pylons;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.DustComposition.CrystalElements;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.SpellScrollItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

// The Spell Pylon (SpellPylonBlockEntity has the tree):
//   Right-click with an element crystal   sets the spell's element (once; the crystal is used)
//   Right-click with a Spell Scroll        records the spell as it stands onto the scroll
//   Right-click empty-handed               its element, level, experience, levels to spend, blessing and corruption
// Broken, it takes its whole tree with it and drops remembering its element and a tenth of its levels.
// active: has an element (for the model)
public class SpellPylonBlock extends Block implements EntityBlock
{
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public SpellPylonBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(ACTIVE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new SpellPylonBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (level.isClientSide() || type != DustBlockEntityRegistry.SPELL_PYLON.get()) return null;
        BlockEntityTicker<SpellPylonBlockEntity> ticker = SpellPylonBlockEntity::serverTick;
        return (BlockEntityTicker<T>) ticker;
    }

    // A broken pylon's memory comes back with it
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof SpellPylonBlockEntity pylon) {
            pylon.setMemory(stack.get(DustComponentDataRegistry.PYLON_MEMORY.get()));
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof SpellPylonBlockEntity pylon)) return InteractionResult.PASS;
        DustElement crystal = CrystalElements.of(stack);
        if (crystal != null) {
            if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.SUCCESS;
            if (pylon.setElement(crystal)) {
                stack.consume(1, player);
                level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                serverPlayer.sendOverlayMessage(pylon.info());
            } else {
                serverPlayer.sendOverlayMessage(Component.translatable("dust.pylon.has_element"));
            }
            return InteractionResult.SUCCESS;
        }
        if (stack.getItem() instanceof SpellScrollItem) {
            if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.SUCCESS;
            if (pylon.record(stack)) {
                level.playSound(null, pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1.0F, 1.0F);
                serverPlayer.sendOverlayMessage(Component.translatable("dust.pylon.recorded"));
            } else {
                serverPlayer.sendOverlayMessage(Component.translatable("dust.pylon.cant_record"));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof SpellPylonBlockEntity pylon) {
            serverPlayer.sendOverlayMessage(pylon.info());
        }
        return InteractionResult.SUCCESS;
    }
}
