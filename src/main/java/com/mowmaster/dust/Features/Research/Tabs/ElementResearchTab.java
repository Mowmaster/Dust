package com.mowmaster.dust.Features.Research.Tabs;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import com.mowmaster.dust.Features.Research.ResearchTab;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// Elements: every element of dust (DustElements), from the five primals up, shown by their icons in their colours.
// Discovered through dust research (DustResearch): a primal by picking up its crystal, or seeing its pure flame; any
// element by measuring an item that's all that element on the Elemental Examiner (with that element's crystal on).
public class ElementResearchTab extends ResearchTab
{
    public ElementResearchTab()
    {
        super(Identifier.fromNamespaceAndPath(DustReferences.MODID, "elements"), 550);
    }

    @Override
    public ItemStack tabIcon()
    {
        return new ItemStack(DustItemRegistry.DUST_RED.get());
    }

    @Override
    protected List<Identifier> computeEntries()
    {
        List<Identifier> list = new ArrayList<>();
        for (DustElement element : DustElements.all()) list.add(element.id());
        return list;
    }

    // For toasts and the side view, which show items: the dust of its strongest primal (red dust for fire, energy...)
    @Override
    public ItemStack icon(Identifier entry)
    {
        DustElement element = DustElements.get(entry);
        if (element == null) return new ItemStack(Items.BARRIER);
        EnumElement strongest = EnumElement.FIRE;
        for (EnumElement primal : EnumElement.values()) {
            if (element.primal(primal) > element.primal(strongest)) strongest = primal;
        }
        return new ItemStack(switch (strongest) {
            case FIRE -> DustItemRegistry.DUST_RED.get();
            case WATER -> DustItemRegistry.DUST_BLUE.get();
            case EARTH -> DustItemRegistry.DUST_GREEN.get();
            case ORDER -> DustItemRegistry.DUST_WHITE.get();
            case CHAOS -> DustItemRegistry.DUST_BLACK.get();
        });
    }

    // In the list and on its page: the element's own icon, tinted with its colour
    @Override
    public @Nullable Identifier listSprite(Identifier entry)
    {
        DustElement element = DustElements.get(entry);
        return element == null ? null : element.icon();
    }

    @Override
    public int listTint(Identifier entry)
    {
        DustElement element = DustElements.get(entry);
        return element == null ? 0xFFFFFFFF : 0xFF000000 | element.color();
    }

    @Override
    public Component name(Identifier entry)
    {
        DustElement element = DustElements.get(entry);
        return element == null ? Component.literal(entry.toString()) : element.name();
    }

    // "Fire: one of the five primal dusts..." / "Energy: a mix of Fire and Order.", then its phase on a new line:
    // "Gaseous: caught in a glass bottle, or it hisses away..."
    @Override
    public Component defaultNote(Identifier entry)
    {
        DustElement element = DustElements.get(entry);
        if (element == null) return Component.translatable("research.dust.note.default.elements.primal", name(entry));
        Component made;
        if (element.isPrimal()) {
            made = Component.translatable("research.dust.note.default.elements.primal", name(entry));
        } else {
            List<DustElement> parts = element.parts();
            Component first = parts.get(0).name();
            Component second = parts.size() > 1 ? parts.get(1).name() : first;
            made = Component.translatable("research.dust.note.default.elements.mix", name(entry), first, second);
        }
        // Component.empty().append(...): a component built from pieces, the way chat messages join coloured parts
        return Component.empty().append(made).append("\n\n").append(element.phase().displayName()).append(": ").append(element.phase().note());
    }

    // Elements are found by research, never dropped, and are dust themselves
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
