package com.mowmaster.dust.Features.DustComposition;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// A crystal or dust of one element (the five primals keep their own older items: crystal_red, dust_blue...).
// Every element gets one of each (DustItemRegistry.CRYSTALS / DUSTS); their textures are one greyscale crystal and one
// greyscale dust, tinted with the element's colour by the item model.
public class ElementItem extends Item
{
    public enum Kind { CRYSTAL, DUST }

    private final DustElement element;
    private final Kind kind;

    public ElementItem(Properties properties, DustElement element, Kind kind)
    {
        super(properties);
        this.element = element;
        this.kind = kind;
    }

    public DustElement element()
    {
        return element;
    }

    public Kind kind()
    {
        return kind;
    }

    // "Knowledge Crystal": item.dust.element_crystal / element_dust with the element's name, so no lang line is needed
    // per element (and elements other mods add get names too)
    @Override
    public Component getName(ItemStack stack)
    {
        return Component.translatable(kind == Kind.CRYSTAL ? "item.dust.element_crystal" : "item.dust.element_dust", element.name());
    }
}
