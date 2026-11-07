package com.mowmaster.dust.Features.AncientLanguage;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.mowlib.storage.StorageInteractions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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

// Rub it on a glyph stone to transcribe its glyph: you get a Transcription of a random ancient word containing that
// glyph (so deciphering it teaches you that glyph, among others).
// Each stone can be transcribed once per player. After that it looks worn blank to that player (Client/GlyphStoneTint),
// and Parchment does nothing on it. Other players can still transcribe it themselves.
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
        Player player = context.getPlayer();
        if (!(state.getBlock() instanceof GlyphStoneBlock) || player == null) {
            return InteractionResult.PASS;
        }

        TranscribedStones done = player.getData(DustAttachmentTypeRegistry.TRANSCRIBED_STONES);
        if (done.contains(level, pos)) {
            if (!level.isClientSide()) {
                player.sendOverlayMessage(Component.translatable("message.dust.ancient.already_transcribed").withStyle(ChatFormatting.GRAY));
            }
            return InteractionResult.FAIL;
        }
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        int glyph = state.getValue(GlyphStoneBlock.GLYPH);
        String word = AncientWords.randomContaining(glyph, level.getRandom());
        ItemStack transcription = AncientWritingItem.withWord(new ItemStack(DustItemRegistry.TRANSCRIPTION.get()), word);
        context.getItemInHand().consume(1, player);
        // MowLib: puts it in the inventory, or drops it if there's no room
        StorageInteractions.giveToPlayer(player, transcription);
        // Remember this stone (setting the new copy syncs it, and the player's client redraws the stone blank)
        player.setData(DustAttachmentTypeRegistry.TRANSCRIBED_STONES, done.with(level, pos));

        player.sendOverlayMessage(Component.translatable("message.dust.ancient.transcribed").withStyle(ChatFormatting.GOLD));
        level.playSound(null, pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1.0F, 0.8F);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        builder.accept(Component.translatable(DustReferences.TOOLTIP + "parchment").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, display, builder, flag);
    }
}
