package com.mowmaster.dust.Features.Spells.grimoire;

import com.mowmaster.dust.Features.Spells.SpellCasting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

// The Akashic: a grimoire with as many pages as there are grimoire page keys (GrimoireItem.PAGES). Spells cast from it
// cost no dust or mana, and their trees get ten times the spell experience (SpellCasting.isAkashic,
// SpellScrollItem.giveExperience). Otherwise they're cast exactly as from a Grimoire.
// Held in either hand, it also shows a Spell Pylon's pool on the reading HUD (SpellPylonReading).
public class AkashicItem extends GrimoireItem
{
    public AkashicItem(Properties properties)
    {
        super(properties);
    }

    // Whether this player holds one, in either hand
    public static boolean holds(Player player)
    {
        return player.getMainHandItem().getItem() instanceof AkashicItem || player.getOffhandItem().getItem() instanceof AkashicItem;
    }

    // Every cast from it goes through these three: the boost is switched on just for the cast
    @Override
    public boolean castPage(ServerPlayer player, ItemStack grimoire, int page)
    {
        return SpellCasting.akashic(() -> super.castPage(player, grimoire, page));
    }

    @Override
    protected boolean castHeld(ServerPlayer player, ItemStack scroll)
    {
        return SpellCasting.akashic(() -> super.castHeld(player, scroll));
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remaining)
    {
        SpellCasting.akashic(() -> {
            super.onUseTick(level, entity, stack, remaining);
            return true;
        });
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        builder.accept(Component.translatable("item.dust.akashic.tooltip.xp").withStyle(ChatFormatting.GOLD));
        builder.accept(Component.translatable("item.dust.akashic.tooltip.cost").withStyle(ChatFormatting.GOLD));
        builder.accept(Component.translatable("item.dust.akashic.tooltip.pool").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, display, builder, flag);
    }
}
