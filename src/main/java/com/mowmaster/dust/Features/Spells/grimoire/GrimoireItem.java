package com.mowmaster.dust.Features.Spells.grimoire;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.Spells.SpellScrollItem;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.mowlib.paging.IPagedStorageItem;
import com.mowmaster.mowlib.paging.PagedStorageInteractions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.resource.ResourceStack;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

// The Grimoire: a book of spells. Each of its PAGES pages holds one Spell Scroll; the selected page is the spell it
// casts. It's built on MowLib's paged storage (IPagedStorageItem), which gives it for free:
//   putting scrolls in and taking them out with bundle-style clicks in inventories
//   choosing the page by scrolling over it in an inventory, or with MowLib's page keys while holding it
//   a tooltip picture of its pages, and a message over the hotbar when the held book's page changes (describePage)
// Casting: right-click it in hand, or press the grimoire keys with it anywhere in your inventory (Client/GrimoireKeys,
// GrimoirePayload): cast the selected spell, turn to the next / previous spell, or cast a numbered page directly.
// A spell cast from it works just like casting its scroll (its tree gets the experience). The whole book shares one
// cooldown.
public class GrimoireItem extends Item implements IPagedStorageItem
{
    public static final int PAGES = 9;

    public GrimoireItem(Properties properties)
    {
        super(properties);
    }

    // ---- paged storage: one scroll (with a spell on it) per page ----

    @Override
    public int pageCapacity(ItemStack stack) { return 1; }

    @Override
    public int defaultPages(ItemStack stack) { return PAGES; }

    @Override
    public boolean accepts(ItemStack self, ItemResource resource)
    {
        return resource.is(DustItemRegistry.SPELL_SCROLL.get()) && resource.toStack(1).has(DustComponentDataRegistry.SPELL.get());
    }

    // The three Item methods that hand MowLib the bundle-style clicks and the page tooltip
    @Override
    public boolean overrideStackedOnOther(ItemStack self, Slot slot, ClickAction action, Player player)
    {
        return PagedStorageInteractions.stackedOnOther(this, self, slot, action, player);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack self, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess carried)
    {
        return PagedStorageInteractions.otherStackedOnMe(this, self, other, slot, action, player, carried);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack)
    {
        return PagedStorageInteractions.tooltipImage(this, stack);
    }

    // "Spell 3/9: Fire Projectile" (or "Empty") over the hotbar when the held book's page changes
    @Override
    public @Nullable Component describePage(ItemStack stack, int page)
    {
        return describe(stack, page);
    }

    public Component describe(ItemStack stack, int page)
    {
        ItemStack scroll = scrollAt(stack, page);
        Component what = scroll.isEmpty() ? Component.translatable("dust.grimoire.empty") : spellName(scroll);
        return Component.translatable("dust.grimoire.page", page + 1, getPageCount(stack), what);
    }

    // ---- reading the pages ----

    // The scroll on a page (empty if there's none)
    public ItemStack scrollAt(ItemStack grimoire, int page)
    {
        ResourceStack<ItemResource> held = contents(grimoire).stack(page);
        return held.isEmpty() ? ItemStack.EMPTY : held.resource().toStack(held.amount());
    }

    // A scroll's name: the one it was given at an anvil, or else its element and shape ("Fire Projectile")
    public static Component spellName(ItemStack scroll)
    {
        if (scroll.has(net.minecraft.core.component.DataComponents.CUSTOM_NAME)) return scroll.getHoverName();
        Spell spell = scroll.get(DustComponentDataRegistry.SPELL.get());
        if (spell == null) return scroll.getHoverName();
        // A fused spell goes by its fusion's name ("Harvest Touch")
        return Component.translatable("dust.grimoire.spell",
                com.mowmaster.dust.Features.Spells.SpellNames.plainSpellForClient(spell.element(), com.mowmaster.dust.Features.Spells.SpellNames.fusionOf(spell)),
                Component.translatable("spell_shape." + spell.shape().getNamespace() + "." + spell.shape().getPath()));
    }

    // ---- casting ----

    // Casts the spell on a page (server). Nothing on that page, or the book cooling down: a message, and false.
    public boolean castPage(ServerPlayer player, ItemStack grimoire, int page)
    {
        if (player.getCooldowns().isOnCooldown(grimoire)) return false;
        ItemStack scroll = scrollAt(grimoire, page);
        if (scroll.isEmpty()) {
            player.sendOverlayMessage(Component.translatable("dust.grimoire.nothing", page + 1));
            return false;
        }
        if (!SpellScrollItem.castFrom(player, scroll)) return false;
        Spell cast = scroll.get(DustComponentDataRegistry.SPELL.get());
        player.getCooldowns().addCooldown(grimoire, cast == null ? SpellScrollItem.CAST_COOLDOWN : SpellScrollItem.cooldownFor(cast));
        return true;
    }

    // The first cast of a held spell (Telekinesis), as it's pressed: no cooldown yet (that starts when it's let go,
    // releaseUsing). Its own method so the Akashic grimoire can boost it, as it does castPage.
    protected boolean castHeld(ServerPlayer player, ItemStack scroll)
    {
        return SpellScrollItem.castFrom(player, scroll);
    }

    // Right-click in hand: cast the selected spell (a Beam spell: hold to channel it, onUseTick)
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        Spell selected = selectedSpell(stack);
        if (selected != null && com.mowmaster.dust.Features.Spells.Channeling.channels(selected)) {
            if (player.getCooldowns().isOnCooldown(stack)) return InteractionResult.FAIL;
            // Telekinesis casts the moment it's pressed, then it's held (onUseTick) until let go (releaseUsing)
            if (com.mowmaster.dust.Features.Spells.effects.Telekinesis.applies(selected) && player instanceof ServerPlayer serverPlayer
                    && !castHeld(serverPlayer, scrollAt(stack, getPage(stack)))) return InteractionResult.FAIL;
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
        if (player instanceof ServerPlayer serverPlayer) castPage(serverPlayer, stack, getPage(stack));
        return InteractionResult.SUCCESS;
    }

    // Carried: once a second the scrolls in it catch up with their trees (SpellSync)
    @Override
    public void inventoryTick(ItemStack stack, net.minecraft.server.level.ServerLevel level, net.minecraft.world.entity.Entity entity,
                              net.minecraft.world.entity.@Nullable EquipmentSlot slot)
    {
        if (level.getGameTime() % 20 != 0) return;
        com.mowmaster.mowlib.paging.PagedContents contents = contents(stack);
        java.util.List<ResourceStack<ItemResource>> pages = new java.util.ArrayList<>(contents.stacks());
        boolean changed = false;
        for (int page = 0; page < pages.size(); page++) {
            ResourceStack<ItemResource> held = pages.get(page);
            if (held.isEmpty()) continue;
            ItemStack scroll = held.resource().toStack(held.amount());
            if (!com.mowmaster.dust.Features.Spells.SpellSync.refresh(scroll, level.getServer())) continue;
            pages.set(page, new ResourceStack<>(ItemResource.of(scroll), held.amount()));
            changed = true;
        }
        if (changed) setContents(stack, new com.mowmaster.mowlib.paging.PagedContents(contents.pages(), contents.page(), java.util.List.copyOf(pages)));
    }

    // The spell on a grimoire's open page, if any (for blocks a spell is set on from it: the Crystal Display)
    public static @Nullable Spell openSpell(ItemStack grimoire)
    {
        return grimoire.getItem() instanceof GrimoireItem book ? book.selectedSpell(grimoire) : null;
    }

    // The spell on the open page, if any
    private @Nullable Spell selectedSpell(ItemStack grimoire)
    {
        ItemStack scroll = scrollAt(grimoire, getPage(grimoire));
        return scroll.isEmpty() ? null : scroll.get(DustComponentDataRegistry.SPELL.get());
    }

    // ---- channelling a Beam spell (held use), as a scroll does ----

    @Override
    public int getUseDuration(ItemStack stack, net.minecraft.world.entity.LivingEntity entity)
    {
        Spell spell = selectedSpell(stack);
        return spell != null && com.mowmaster.dust.Features.Spells.Channeling.channels(spell) ? com.mowmaster.dust.Features.Spells.Channeling.MAX_USE : 0;
    }

    @Override
    public net.minecraft.world.item.ItemUseAnimation getUseAnimation(ItemStack stack)
    {
        return net.minecraft.world.item.ItemUseAnimation.BOW;
    }

    @Override
    public void onUseTick(Level level, net.minecraft.world.entity.LivingEntity entity, ItemStack stack, int remaining)
    {
        if (!(entity instanceof ServerPlayer player)) return;
        Spell spell = selectedSpell(stack);
        ItemStack scroll = scrollAt(stack, getPage(stack));
        if (spell == null) return;
        int held = com.mowmaster.dust.Features.Spells.Channeling.MAX_USE - remaining;
        // Telekinesis (already cast in use): each tick held, what it took hold of is pulled in
        if (com.mowmaster.dust.Features.Spells.effects.Telekinesis.applies(spell)) {
            com.mowmaster.dust.Features.Spells.effects.Telekinesis.hold(player);
            return;
        }
        if (held % com.mowmaster.dust.Features.Spells.Channeling.interval(spell) != 0) return;
        if (!SpellScrollItem.castFrom(player, scroll, com.mowmaster.dust.Features.Spells.Channeling.reduction(spell, held))) player.stopUsingItem();
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, net.minecraft.world.entity.LivingEntity entity, int remaining)
    {
        Spell spell = selectedSpell(stack);
        // Telekinesis let go: a push, a throw, or a drop (effects/Telekinesis)
        if (entity instanceof ServerPlayer player && com.mowmaster.dust.Features.Spells.effects.Telekinesis.applies(spell)) {
            com.mowmaster.dust.Features.Spells.effects.Telekinesis.release(player, com.mowmaster.dust.Features.Spells.Channeling.MAX_USE - remaining);
        }
        if (entity instanceof Player player && spell != null) player.getCooldowns().addCooldown(stack, SpellScrollItem.cooldownFor(spell));
        return true;
    }

    // The grimoire the keys work with: in a hand (off hand first), or else the first one in the inventory
    public static ItemStack find(Player player)
    {
        if (player.getOffhandItem().getItem() instanceof GrimoireItem) return player.getOffhandItem();
        if (player.getMainHandItem().getItem() instanceof GrimoireItem) return player.getMainHandItem();
        for (ItemStack stack : player.getInventory()) {
            if (stack.getItem() instanceof GrimoireItem) return stack;
        }
        return ItemStack.EMPTY;
    }
}
