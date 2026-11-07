package com.mowmaster.dust.Features.AncientLanguage;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.mowlib.storage.StorageInteractions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

// Rub it on a glyph wall to copy the word: right-click any glyph stone and the whole row of glyph stones it's part of
// (same facing, side by side) is read left to right and written onto a Transcription.
public class ParchmentItem extends Item
{
    public ParchmentItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof GlyphStoneBlock)) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        String word = readWall(level, pos, state);
        Player player = context.getPlayer();
        ItemStack transcription = AncientWritingItem.withWord(new ItemStack(DustItemRegistry.TRANSCRIPTION.get()), word);
        context.getItemInHand().consume(1, player);
        if (player != null) {
            // MowLib: puts it in the inventory, or drops it if there's no room
            StorageInteractions.giveToPlayer(player, transcription);
            player.sendOverlayMessage(Component.translatable("message.dust.ancient.transcribed").withStyle(ChatFormatting.GOLD));
        }
        level.playSound(null, pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1.0F, 0.8F);
        return InteractionResult.SUCCESS;
    }

    // The word on the wall: walk left to the first glyph stone of the row, then read rightwards.
    // Standing in front of a stone facing north you look south, so your right is west: right = facing's
    // counter-clockwise neighbour.
    public static String readWall(Level level, BlockPos clicked, BlockState clickedState)
    {
        Direction facing = clickedState.getValue(GlyphStoneBlock.FACING);
        Direction right = facing.getCounterClockWise();
        BlockPos start = clicked;
        for (int i = 0; i < DecipherMenu.MAX_LENGTH && isGlyph(level, start.relative(right.getOpposite()), facing); i++) {
            start = start.relative(right.getOpposite());
        }
        StringBuilder word = new StringBuilder();
        BlockPos pos = start;
        while (word.length() < DecipherMenu.MAX_LENGTH && isGlyph(level, pos, facing)) {
            word.append((char) ('a' + level.getBlockState(pos).getValue(GlyphStoneBlock.GLYPH)));
            pos = pos.relative(right);
        }
        return word.toString();
    }

    private static boolean isGlyph(Level level, BlockPos pos, Direction facing)
    {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof GlyphStoneBlock && state.getValue(GlyphStoneBlock.FACING) == facing;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        builder.accept(Component.translatable(DustReferences.TOOLTIP + "parchment").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, display, builder, flag);
    }
}
