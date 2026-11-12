package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustBlockEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.MagicalFlintAndSteelItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import org.jspecify.annotations.Nullable;

// Mixing Table (ResearchMixingBlockEntity has the rules):
//   Right-click a side pad with Alchemical Dust   puts one on it
//   Right-click with a Magical Flint and Steel    starts it: both dusts pour into the pot, the glyph spins over it
//   Right-click the pot (while it's going)        stirs: ResearchMixingBlockEntity.STIRS full turns of the spoon
//   Left-click a pad / the output                 takes from it (StationInteractions)
public class ResearchMixingBlock extends StationBlock
{
    public ResearchMixingBlock(Properties properties)
    {
        super(properties, Shapes.or(TABLE, Block.box(6, 16, 6, 10, 21, 10)));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new ResearchMixingBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (type != DustBlockEntityRegistry.RESEARCH_MIXING.get()) return null;
        BlockEntityTicker<ResearchMixingBlockEntity> ticker = level.isClientSide() ? ResearchMixingBlockEntity::clientTick : ResearchMixingBlockEntity::serverTick;
        return (BlockEntityTicker<T>) ticker;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof ResearchMixingBlockEntity table)) return InteractionResult.PASS;

        if (stack.getItem() instanceof MagicalFlintAndSteelItem && !table.isStirring()) {
            String problem = table.cannotStart();
            if (problem != null) {
                if (!level.isClientSide()) player.sendOverlayMessage(Component.translatable("dust.mixing." + problem));
                return InteractionResult.FAIL;
            }
            if (level instanceof ServerLevel serverLevel) {
                table.start(serverLevel);
                stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }
        // While it's going, the pot is for stirring, whatever's in hand
        if (table.isStirring() && StationInteractions.inArea(level, pos, hit, EvaporationLayout.POT_AREA)) {
            if (!level.isClientSide()) table.stir(level);
            return InteractionResult.SUCCESS;
        }
        return StationInteractions.rightClick(stack, level, pos, player, hit);
    }

    // Empty-handed: stirring
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof ResearchMixingBlockEntity table) || !table.isStirring()
                || !StationInteractions.inArea(level, pos, hit, EvaporationLayout.POT_AREA)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) table.stir(level);
        return InteractionResult.SUCCESS;
    }
}
