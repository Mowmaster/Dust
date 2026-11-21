package com.mowmaster.dust.Features.Apparatus.separator;

import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

// A Jar Label stuck under a drum section (it keeps the section to its element), or scraped off with shears
public final class SectionLabels
{
    private SectionLabels() {}

    // null: not a label or shears, or nothing to do (carry on as normal)
    public static @Nullable InteractionResult use(ItemStack stack, Level level, BlockPos pos, Player player, InteractionHand hand)
    {
        if (!(level.getBlockEntity(pos) instanceof SectionHolder holder)) return null;
        DrumSection section = holder.section();
        if (stack.is(DustItemRegistry.JAR_LABEL.get())) {
            if (level.isClientSide()) return InteractionResult.SUCCESS;
            if (section.element() == null) {
                player.sendOverlayMessage(Component.translatable("dust.separator.label_needs_element"));
                return InteractionResult.FAIL;
            }
            if (!section.label()) return InteractionResult.FAIL;
            holder.sectionChanged();
            stack.consume(1, player);
            level.playSound(null, pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 0.8F, 1.2F);
            return InteractionResult.SUCCESS;
        }
        if (stack.getItem() instanceof ShearsItem && section.labelled()) {
            if (level.isClientSide()) return InteractionResult.SUCCESS;
            section.unlabel();
            holder.sectionChanged();
            stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
            level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 0.8F, 1.2F);
            return InteractionResult.SUCCESS;
        }
        return null;
    }
}
