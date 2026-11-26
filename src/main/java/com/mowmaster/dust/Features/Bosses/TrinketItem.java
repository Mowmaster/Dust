package com.mowmaster.dust.Features.Bosses;

import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

// A trinket from an Archon's loot bag. It works anywhere in your inventory (Trinkets); more of the same one do nothing more.
//   SIGIL        one per set: its element's spells are stronger and cost less dust, and its element hurts you less
//   PRISM        Prism of the Archons: every spell is stronger
//   MANA_HEART   Heart of Mana: mana comes back faster
//   VOID_LOCKET  Void Locket: now and then a spell costs no dust
public class TrinketItem extends Item
{
    public enum Kind { SIGIL, PRISM, MANA_HEART, VOID_LOCKET }

    private final Kind kind;
    private final @Nullable ElementalArmorSet set;

    public TrinketItem(Kind kind, @Nullable ElementalArmorSet set, Properties properties)
    {
        super(properties);
        this.kind = kind;
        this.set = set;
    }

    public Kind kind()
    {
        return kind;
    }

    // The sigil's set (null for the others)
    public @Nullable ElementalArmorSet set()
    {
        return set;
    }

    @Override
    public boolean isFoil(ItemStack stack)
    {
        return kind != Kind.SIGIL || super.isFoil(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        super.appendHoverText(stack, context, display, builder, flag);
        if (kind == Kind.SIGIL && set != null) {
            Component element = set.element().name();
            builder.accept(Component.translatable("dust.trinket.sigil", element).withStyle(ChatFormatting.BLUE));
            builder.accept(Component.translatable("dust.trinket.sigil.ward", element).withStyle(ChatFormatting.BLUE));
        } else {
            builder.accept(Component.translatable("dust.trinket." + kind.name().toLowerCase(java.util.Locale.ROOT)).withStyle(ChatFormatting.BLUE));
        }
        builder.accept(Component.translatable("dust.trinket.carried").withStyle(ChatFormatting.DARK_GRAY));
    }
}
