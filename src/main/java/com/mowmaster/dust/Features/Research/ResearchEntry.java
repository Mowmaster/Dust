package com.mowmaster.dust.Features.Research;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

// One researchable thing: an entry id within a tab
public record ResearchEntry(ResearchTab tab, Identifier id)
{
    public ItemStack icon()
    {
        return tab.icon(id);
    }

    public Component name()
    {
        return tab.name(id);
    }

    // Translation key for this entry's built-in notes, e.g. research.minecraft.note.zombie
    public String noteKey()
    {
        return "research." + id.getNamespace() + ".note." + id.getPath().replace('/', '.');
    }

    // The other members of its group (block families), itself included
    public java.util.List<Identifier> group()
    {
        return tab.groupOf(id);
    }
}
