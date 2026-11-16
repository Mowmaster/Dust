package com.mowmaster.dust.Features.Research.Tabs;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.Research.ResearchTab;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// The Codex: the ancient symbols the reader has deciphered, each with the letter it stands for (Features/AncientLanguage).
// It has no entries of its own: the book draws its page from the player's known glyphs (ResearchBook.drawCodex), and
// only the ones they know are listed (in alphabetical order, so the unknown ones can't be worked out from the gaps).
public class CodexResearchTab extends ResearchTab
{
    public CodexResearchTab()
    {
        super(Identifier.fromNamespaceAndPath(DustReferences.MODID, "codex"), 700);
    }

    @Override
    public ItemStack tabIcon()
    {
        return new ItemStack(DustItemRegistry.PARCHMENT.get());
    }

    @Override
    protected List<Identifier> computeEntries()
    {
        return List.of();
    }

    @Override
    public ItemStack icon(Identifier entry)
    {
        return tabIcon();
    }

    @Override
    public Component name(Identifier entry)
    {
        return title();
    }

    @Override
    public boolean showsDrops()
    {
        return false;
    }

    @Override
    public boolean showsDustPage()
    {
        return false;
    }

    // Along the bottom of the book
    @Override
    public boolean bottomTab()
    {
        return true;
    }
}
