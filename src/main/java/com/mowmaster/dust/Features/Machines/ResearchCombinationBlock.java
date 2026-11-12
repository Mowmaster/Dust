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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

// Combination Table (ResearchCombinationBlockEntity has the rules):
//   Right-click a side pad with solid Alchemical Dust   puts one on it
//   Right-click with a Magical Flint and Steel          starts it
//   Left-click a pad / the output                       takes from it (StationInteractions)
public class ResearchCombinationBlock extends StationBlock
{
    public ResearchCombinationBlock(Properties properties)
    {
        super(properties, TABLE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new ResearchCombinationBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (type != DustBlockEntityRegistry.RESEARCH_COMBINATION.get()) return null;
        BlockEntityTicker<ResearchCombinationBlockEntity> ticker = level.isClientSide() ? ResearchCombinationBlockEntity::clientTick : ResearchCombinationBlockEntity::serverTick;
        return (BlockEntityTicker<T>) ticker;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof ResearchCombinationBlockEntity table)) return InteractionResult.PASS;

        if (stack.getItem() instanceof MagicalFlintAndSteelItem) {
            String problem = table.cannotStart();
            if (problem != null) {
                if (!level.isClientSide()) player.sendOverlayMessage(Component.translatable("dust.combination." + problem));
                return InteractionResult.FAIL;
            }
            if (level instanceof ServerLevel serverLevel) {
                table.start(serverLevel, player);
                stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }
        return StationInteractions.rightClick(stack, level, pos, player, hit);
    }
}
