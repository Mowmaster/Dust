package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.Features.Research.ResearchTabs;
import com.mowmaster.mowlib.storage.StorageInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

// Dust Testing Disk: a workstation that reads how much of each dust something holds. (A plain block for now; it gets
// its own model later.)
//   Right-click with an item    puts one on top (if it's empty). A beam of each dust's colour shines out of it, longer
//                               the more of that dust it holds (Client/TestingDiskRenderer)
//   Right-click empty-handed    takes it back
// Whoever puts something on it while carrying Research Notes learns its composition (the book's Research page).
public class TestingDiskBlock extends Block implements EntityBlock
{
    public TestingDiskBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new TestingDiskBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof TestingDiskBlockEntity disk)) return InteractionResult.PASS;
        // Something is on it already (or the hand is empty): treat it as an empty-handed click, which takes it back
        if (stack.isEmpty() || !disk.getItem().isEmpty()) return InteractionResult.TRY_WITH_EMPTY_HAND;

        if (!level.isClientSide()) {
            ItemStack placed = stack.copyWithCount(1);
            disk.setItem(placed);
            // consume(1, player): shrinks the stack unless the player is in creative
            stack.consume(1, player);
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (player instanceof ServerPlayer serverPlayer) reportTo(serverPlayer, placed);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (!(level.getBlockEntity(pos) instanceof TestingDiskBlockEntity disk) || disk.getItem().isEmpty()) return InteractionResult.PASS;
        if (!level.isClientSide()) {
            StorageInteractions.giveToPlayer(player, disk.getItem());
            disk.setItem(ItemStack.EMPTY);
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.SUCCESS;
    }

    // Notes the composition in their research. The disk itself shows it: one beam per dust (Client/TestingDiskRenderer).
    private static void reportTo(ServerPlayer player, ItemStack item)
    {
        DustComposition composition = DustCompositions.of(item);
        if (composition == null) {
            // sendOverlayMessage: shown over the hotbar (the "action bar") rather than in chat
            player.sendOverlayMessage(Component.translatable("dust.testing_disk.no_dust", item.getHoverName()));
            return;
        }
        DustResearch.noteComposition(player, ResearchTabs.entryForItem(item), composition);
    }
}
