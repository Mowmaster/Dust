package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;

// One element of dust: a primal (fire, water, earth, order, chaos) or a mix of other elements (air = fire + water,
// void = vacuum + chaos...). All of them come down to some amount of the five primal dusts, which gives their colour.
// Each also has a phase (ElementPhase), which says what it needs to be caught when something is broken down.
// The elements themselves are listed in DustElements.
public final class DustElement
{
    private final Identifier id;
    private final List<DustElement> parts;
    // How much of each primal dust it holds, indexed by EnumElement#ordinal
    private final int[] primals = new int[EnumElement.values().length];
    private final int color;
    private final ElementPhase phase;

    // A primal element: one dust of its kind
    DustElement(Identifier id, EnumElement primal, ElementPhase phase)
    {
        this.id = id;
        this.phase = phase;
        this.parts = List.of();
        this.primals[primal.ordinal()] = 1;
        this.color = DustComposition.colorOf(primals);
    }

    // A mixed element: everything in its parts added together
    DustElement(Identifier id, List<DustElement> parts, ElementPhase phase)
    {
        this.id = id;
        this.phase = phase;
        this.parts = List.copyOf(parts);
        for (DustElement part : parts) {
            for (int i = 0; i < primals.length; i++) primals[i] += part.primals[i];
        }
        this.color = DustComposition.colorOf(primals);
    }

    public Identifier id()
    {
        return id;
    }

    // What it's mixed from (empty for a primal)
    public List<DustElement> parts()
    {
        return parts;
    }

    public boolean isPrimal()
    {
        return parts.isEmpty();
    }

    // Gaseous, aqueous, amorphous or crystalline: what it needs to be caught when it's broken out of something
    public ElementPhase phase()
    {
        return phase;
    }

    // How much of this primal dust it holds
    public int primal(EnumElement element)
    {
        return primals[element.ordinal()];
    }

    // Its colour, 0xRRGGBB (DustComposition#colorOf)
    public int color()
    {
        return color;
    }

    // "Fire", "Knowledge"...: dust.element.<namespace>.<path>
    public Component name()
    {
        return Component.translatable("dust.element." + id.getNamespace() + "." + id.getPath());
    }

    // Its icon: a greyscale GUI sprite (textures/gui/sprites/element/<path>.png in its namespace) that's drawn tinted
    // with its colour
    public Identifier icon()
    {
        return Identifier.fromNamespaceAndPath(id.getNamespace(), "element/" + id.getPath());
    }

    @Override
    public String toString()
    {
        return id.toString();
    }

    static Identifier dustId(String path)
    {
        return Identifier.fromNamespaceAndPath(DustReferences.MODID, path);
    }
}
