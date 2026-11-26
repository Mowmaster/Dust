package com.mowmaster.dust.Features.Armor;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.function.Consumer;

// A piece of an elemental armor set (ElementalArmorSet). Its tooltip shows the set's bonuses, lit up as the wearer has
// enough of the set on (ArmorSetBonuses).
public class ElementalArmorItem extends Item
{
    private final ElementalArmorSet set;
    private final ArmorType type;

    public ElementalArmorItem(ElementalArmorSet set, ArmorType type, Properties properties)
    {
        super(properties);
        this.set = set;
        this.type = type;
    }

    public ElementalArmorSet set()
    {
        return set;
    }

    public ArmorType type()
    {
        return type;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        super.appendHoverText(stack, context, display, builder, flag);
        int worn = ArmorSetBonuses.localCount.applyAsInt(set);
        builder.accept(Component.translatable("dust.armor_set.header", Component.translatable("dust.armor_set." + set.id), worn)
                .withStyle(worn >= 2 ? ChatFormatting.GOLD : ChatFormatting.GRAY));
        builder.accept(Component.translatable("dust.armor_set.two", Component.translatable("dust.armor_set." + set.id + ".two"))
                .withStyle(worn >= 2 ? ChatFormatting.GREEN : ChatFormatting.DARK_GRAY));
        builder.accept(Component.translatable("dust.armor_set.four", Component.translatable("dust.armor_set." + set.id + ".four"))
                .withStyle(worn >= 4 ? ChatFormatting.GREEN : ChatFormatting.DARK_GRAY));
    }
}
