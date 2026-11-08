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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.UseOnContext;
import com.mowmaster.mowlib.paging.IPageable;

import java.util.function.Consumer;

// Research Notes (shared = false): your own research, recorded while it's anywhere in your inventory.
// Shared Research Guide (shared = true): a read-only copy of someone's research (dust:research_snapshot), made by
// crafting Research Notes + a Book and Quill + Ancient Ink.
//
// Held in either hand the book shows at the side of the screen (Client/ResearchBookHud), and MowLib's page keys turn
// its pages (IPageable#turnHeld). Opening it for clicking (Client/ResearchBookScreen):
// - right-click (at the air, a block or a mob): the main page of the open tab
// - sneak + right-click, or MowLib's activation key: straight to the entry of what you're looking at (within observing
//   range, see ResearchLook); looking at nothing opens the main page
public class ResearchBookItem extends Item implements IPageable
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

    // Right-clicking the air
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        open(level, player, player.getItemInHand(hand));
        return InteractionResult.SUCCESS;
    }

    // Right-clicking a block (after the block itself had its chance: chests etc. still open normally unless sneaking)
    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        if (context.getPlayer() == null) return InteractionResult.PASS;
        open(context.getLevel(), context.getPlayer(), context.getItemInHand());
        return InteractionResult.SUCCESS;
    }

    // Right-clicking a mob
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand)
    {
        open(player.level(), player, stack);
        return InteractionResult.SUCCESS;
    }

    // The screen is client-only; these branches only ever run on the client
    private static void open(Level level, Player player, ItemStack stack)
    {
        if (level.isClientSide()) {
            if (player.isShiftKeyDown()) ResearchClient.openAtLookedAt(stack);
            else ResearchClient.openBook(stack, null);
        }
    }

    // ---- MowLib page keys (IPageable) ----
    // The book's pages are what's open on the client (Client/ResearchBook), not something stored on the stack, so the
    // stack itself has a single "page" and the keys just turn the book on the client.

    @Override
    public int getPageCount(ItemStack stack)
    {
        return 1;
    }

    @Override
    public int getPage(ItemStack stack)
    {
        return 0;
    }

    @Override
    public void setPage(ItemStack stack, int page)
    {
    }

    @Override
    public void turnHeld(ItemStack stack, int direction, Player player)
    {
        if (player.level().isClientSide()) ResearchClient.turnPage(stack, direction);
    }

    // MowLib's "Page 1/1" over the hotbar would be meaningless here
    @Override
    public boolean showsHeldPageMessages(ItemStack stack)
    {
        return false;
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
