package com.mowmaster.dust.Features.Bosses;

import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

// An Archon's loot bag (LootBags): right-click to open one, sneak and right-click to open the whole stack. Its level
// (the Archon's) is in its dust:loot_bag_level data, so bags of different levels don't stack.
public class LootBagItem extends Item
{
    private final ElementalArmorSet set;

    public LootBagItem(ElementalArmorSet set, Properties properties)
    {
        super(properties);
        this.set = set;
    }

    public ElementalArmorSet set()
    {
        return set;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel server) {
            int bagLevel = LootBags.level(stack);
            int count = player.isShiftKeyDown() ? stack.getCount() : 1;
            for (int i = 0; i < count && !stack.isEmpty(); i++) {
                stack.consume(1, player);
                LootBags.open(server, player, set, bagLevel);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        super.appendHoverText(stack, context, display, builder, flag);
        builder.accept(Component.translatable("dust.loot_bag.level", LootBags.level(stack)).withStyle(ChatFormatting.GOLD));
        builder.accept(Component.translatable("dust.loot_bag.open").withStyle(ChatFormatting.GRAY));
    }
}
