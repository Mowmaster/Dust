package com.mowmaster.dust.Features.Research.Tabs;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Research.ResearchTab;
import com.mowmaster.dust.Features.Research.ResearchUnlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

// Research: discoveries unlocked by what you've learned (ResearchUnlocks), not by finding them in the world.
// Locked ones show faded in the list like anything else not yet researched.
public class ResearchUnlockTab extends ResearchTab
{
    public ResearchUnlockTab()
    {
        super(Identifier.fromNamespaceAndPath(DustReferences.MODID, "research"), 600);
    }

    @Override
    public ItemStack tabIcon()
    {
        return new ItemStack(Items.WRITABLE_BOOK);
    }

    @Override
    protected List<Identifier> computeEntries()
    {
        List<Identifier> list = new ArrayList<>();
        for (ResearchUnlocks.Unlock unlock : ResearchUnlocks.all()) list.add(unlock.id());
        return list;
    }

    @Override
    public ItemStack icon(Identifier entry)
    {
        ResearchUnlocks.Unlock unlock = ResearchUnlocks.byId(entry);
        return unlock == null ? new ItemStack(Items.BARRIER) : unlock.icon().get();
    }

    @Override
    public Component name(Identifier entry)
    {
        return icon(entry).getHoverName();
    }

    // Nothing drops a discovery, and it has no dust of its own
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
}
