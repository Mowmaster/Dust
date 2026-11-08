package com.mowmaster.dust.Features.Research;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Research.Client.ResearchClient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jspecify.annotations.Nullable;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

// Research Notes (shared = false): your own research, recorded while it's anywhere in your inventory.
// Shared Research Guide (shared = true): a read-only copy of someone's research (dust:research_snapshot), made by
// crafting Research Notes + a Book and Quill + Ancient Ink.
//
// Held in either hand the book shows at the side of the screen, so you can keep moving (Client/ResearchBookHud).
// Sneak + right-click (or MowLib's activation key) opens it for clicking (Client/ResearchBookScreen).
public class ResearchBookItem extends Item
{
    private final boolean shared;

    public ResearchBookItem(Properties properties, boolean shared)
    {
        super(properties);
        this.shared = shared;
    }

    public boolean isShared()
    {
        return shared;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        if (!player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) {
            // The screen is client-only; this branch only ever runs on the client
            ResearchClient.openBook(player.getItemInHand(hand));
        }
        return InteractionResult.SUCCESS;
    }

    // Research Notes come back out of the crafting grid (your research is on you, not the book, so this loses nothing):
    // crafting a Shared Research Guide doesn't use up your notes
    @Override
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemInstance instance)
    {
        return shared ? super.getCraftingRemainder(instance) : new ItemStackTemplate(this);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        builder.accept(Component.translatable(DustReferences.TOOLTIP + (shared ? "shared_research_guide" : "research_notes")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, display, builder, flag);
    }
}
